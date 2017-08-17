begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|J2ObjCIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CancellationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_comment
comment|/**  * Testing utilities relating to garbage collection finalization.  *  *<p>Use this class to test code triggered by<em>finalization</em>, that is, one of the  * following actions taken by the java garbage collection system:  *  *<ul>  *<li>invoking the {@code finalize} methods of unreachable objects  *<li>clearing weak references to unreachable referents  *<li>enqueuing weak references to unreachable referents in their reference queue  *</ul>  *  *<p>This class uses (possibly repeated) invocations of {@link java.lang.System#gc()} to cause  * finalization to happen.  However, a call to {@code System.gc()} is specified to be no more  * than a hint, so this technique may fail at the whim of the JDK implementation, for example if  * a user specified the JVM flag {@code -XX:+DisableExplicitGC}.  But in practice, it works very  * well for ordinary tests.  *  *<p>Failure of the expected event to occur within an implementation-defined "reasonable" time  * period or an interrupt while waiting for the expected event will result in a {@link  * RuntimeException}.  *  *<p>Here's an example that tests a {@code finalize} method:  *  *<pre>   {@code  *   final CountDownLatch latch = new CountDownLatch(1);  *   Object x = new MyClass() {  *     ...  *     protected void finalize() { latch.countDown(); ... }  *   };  *   x = null;  // Hint to the JIT that x is stack-unreachable  *   GcFinalization.await(latch);}</pre>  *  *<p>Here's an example that uses a user-defined finalization predicate:  *  *<pre>   {@code  *   final WeakHashMap<Object, Object> map = new WeakHashMap<Object, Object>();  *   map.put(new Object(), Boolean.TRUE);  *   GcFinalization.awaitDone(new FinalizationPredicate() {  *     public boolean isDone() {  *       return map.isEmpty();  *     }  *   });}</pre>  *  *<p>Even if your non-test code does not use finalization, you can  * use this class to test for leaks, by ensuring that objects are no  * longer strongly referenced:  *  *<pre> {@code  * // Helper function keeps victim stack-unreachable.  * private WeakReference<Foo> fooWeakRef() {  *   Foo x = ....;  *   WeakReference<Foo> weakRef = new WeakReference<Foo>(x);  *   // ... use x ...  *   x = null;  // Hint to the JIT that x is stack-unreachable  *   return weakRef;  * }  * public void testFooLeak() {  *   GcFinalization.awaitClear(fooWeakRef());  * }}</pre>  *  *<p>This class cannot currently be used to test soft references, since this class does not try to  * create the memory pressure required to cause soft references to be cleared.  *  *<p>This class only provides testing utilities.  It is not designed for direct use in production  * or for benchmarking.  *  * @author mike nonemacher  * @author Martin Buchholz  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|J2ObjCIncompatible
comment|// gc
DECL|class|GcFinalization
specifier|public
specifier|final
class|class
name|GcFinalization
block|{
DECL|method|GcFinalization ()
specifier|private
name|GcFinalization
parameter_list|()
block|{}
comment|/**    * 10 seconds ought to be long enough for any object to be GC'ed and finalized.  Unless we have a    * gigantic heap, in which case we scale by heap size.    */
DECL|method|timeoutSeconds ()
specifier|private
specifier|static
name|long
name|timeoutSeconds
parameter_list|()
block|{
comment|// This class can make no hard guarantees.  The methods in this class are inherently flaky, but
comment|// we try hard to make them robust in practice.  We could additionally try to add in a system
comment|// load timeout multiplier.  Or we could try to use a CPU time bound instead of wall clock time
comment|// bound.  But these ideas are harder to implement.  We do not try to detect or handle a
comment|// user-specified -XX:+DisableExplicitGC.
comment|//
comment|// TODO(user): Consider using
comment|// java/lang/management/OperatingSystemMXBean.html#getSystemLoadAverage()
comment|//
comment|// TODO(user): Consider scaling by number of mutator threads,
comment|// e.g. using Thread#activeCount()
return|return
name|Math
operator|.
name|max
argument_list|(
literal|10L
argument_list|,
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|totalMemory
argument_list|()
operator|/
operator|(
literal|32L
operator|*
literal|1024L
operator|*
literal|1024L
operator|)
argument_list|)
return|;
block|}
comment|/**    * Waits until the given future {@linkplain Future#isDone is done}, invoking the garbage    * collector as necessary to try to ensure that this will happen.    *    * @throws RuntimeException if timed out or interrupted while waiting    */
DECL|method|awaitDone (Future<?> future)
specifier|public
specifier|static
name|void
name|awaitDone
parameter_list|(
name|Future
argument_list|<
name|?
argument_list|>
name|future
parameter_list|)
block|{
if|if
condition|(
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|long
name|timeoutSeconds
init|=
name|timeoutSeconds
argument_list|()
decl_stmt|;
specifier|final
name|long
name|deadline
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|SECONDS
operator|.
name|toNanos
argument_list|(
name|timeoutSeconds
argument_list|)
decl_stmt|;
do|do
block|{
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
if|if
condition|(
name|future
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|(
literal|1L
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|CancellationException
decl||
name|ExecutionException
name|ok
parameter_list|)
block|{
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unexpected interrupt while waiting for future"
argument_list|,
name|ie
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|tryHarder
parameter_list|)
block|{
comment|/* OK */
block|}
block|}
do|while
condition|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|deadline
operator|<
literal|0
condition|)
do|;
throw|throw
name|formatRuntimeException
argument_list|(
literal|"Future not done within %d second timeout"
argument_list|,
name|timeoutSeconds
argument_list|)
throw|;
block|}
comment|/**    * Waits until the given latch has {@linkplain CountDownLatch#countDown counted down} to zero,    * invoking the garbage collector as necessary to try to ensure that this will happen.    *    * @throws RuntimeException if timed out or interrupted while waiting    */
DECL|method|await (CountDownLatch latch)
specifier|public
specifier|static
name|void
name|await
parameter_list|(
name|CountDownLatch
name|latch
parameter_list|)
block|{
if|if
condition|(
name|latch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
specifier|final
name|long
name|timeoutSeconds
init|=
name|timeoutSeconds
argument_list|()
decl_stmt|;
specifier|final
name|long
name|deadline
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|SECONDS
operator|.
name|toNanos
argument_list|(
name|timeoutSeconds
argument_list|)
decl_stmt|;
do|do
block|{
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
if|if
condition|(
name|latch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|latch
operator|.
name|await
argument_list|(
literal|1L
argument_list|,
name|SECONDS
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unexpected interrupt while waiting for latch"
argument_list|,
name|ie
argument_list|)
throw|;
block|}
block|}
do|while
condition|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|deadline
operator|<
literal|0
condition|)
do|;
throw|throw
name|formatRuntimeException
argument_list|(
literal|"Latch failed to count down within %d second timeout"
argument_list|,
name|timeoutSeconds
argument_list|)
throw|;
block|}
comment|/**    * Creates a garbage object that counts down the latch in its finalizer.  Sequestered into a    * separate method to make it somewhat more likely to be unreachable.    */
DECL|method|createUnreachableLatchFinalizer (final CountDownLatch latch)
specifier|private
specifier|static
name|void
name|createUnreachableLatchFinalizer
parameter_list|(
specifier|final
name|CountDownLatch
name|latch
parameter_list|)
block|{
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|finalize
parameter_list|()
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
comment|/**    * A predicate that is expected to return true subsequent to<em>finalization</em>, that is, one    * of the following actions taken by the garbage collector when performing a full collection in    * response to {@link System#gc()}:    *    *<ul>    *<li>invoking the {@code finalize} methods of unreachable objects    *<li>clearing weak references to unreachable referents    *<li>enqueuing weak references to unreachable referents in their reference queue    *</ul>    */
DECL|interface|FinalizationPredicate
specifier|public
interface|interface
name|FinalizationPredicate
block|{
DECL|method|isDone ()
name|boolean
name|isDone
parameter_list|()
function_decl|;
block|}
comment|/**    * Waits until the given predicate returns true, invoking the garbage collector as necessary to    * try to ensure that this will happen.    *    * @throws RuntimeException if timed out or interrupted while waiting    */
DECL|method|awaitDone (FinalizationPredicate predicate)
specifier|public
specifier|static
name|void
name|awaitDone
parameter_list|(
name|FinalizationPredicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|long
name|timeoutSeconds
init|=
name|timeoutSeconds
argument_list|()
decl_stmt|;
specifier|final
name|long
name|deadline
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|SECONDS
operator|.
name|toNanos
argument_list|(
name|timeoutSeconds
argument_list|)
decl_stmt|;
do|do
block|{
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
if|if
condition|(
name|predicate
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
name|CountDownLatch
name|done
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|createUnreachableLatchFinalizer
argument_list|(
name|done
argument_list|)
expr_stmt|;
name|await
argument_list|(
name|done
argument_list|)
expr_stmt|;
if|if
condition|(
name|predicate
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
do|while
condition|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|deadline
operator|<
literal|0
condition|)
do|;
throw|throw
name|formatRuntimeException
argument_list|(
literal|"Predicate did not become true within %d second timeout"
argument_list|,
name|timeoutSeconds
argument_list|)
throw|;
block|}
comment|/**    * Waits until the given weak reference is cleared, invoking the garbage collector as necessary    * to try to ensure that this will happen.    *    *<p>This is a convenience method, equivalent to:    *<pre>   {@code    *   awaitDone(new FinalizationPredicate() {    *     public boolean isDone() {    *       return ref.get() == null;    *     }    *   });}</pre>    *    * @throws RuntimeException if timed out or interrupted while waiting    */
DECL|method|awaitClear (final WeakReference<?> ref)
specifier|public
specifier|static
name|void
name|awaitClear
parameter_list|(
specifier|final
name|WeakReference
argument_list|<
name|?
argument_list|>
name|ref
parameter_list|)
block|{
name|awaitDone
argument_list|(
operator|new
name|FinalizationPredicate
argument_list|()
block|{
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|ref
operator|.
name|get
argument_list|()
operator|==
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tries to perform a "full" garbage collection cycle (including processing of weak references    * and invocation of finalize methods) and waits for it to complete.  Ensures that at least one    * weak reference has been cleared and one {@code finalize} method has been run before this    * method returns.  This method may be useful when testing the garbage collection mechanism    * itself, or inhibiting a spontaneous GC initiation in subsequent code.    *    *<p>In contrast, a plain call to {@link java.lang.System#gc()} does not ensure finalization    * processing and may run concurrently, for example, if the JVM flag {@code    * -XX:+ExplicitGCInvokesConcurrent} is used.    *    *<p>Whenever possible, it is preferable to test directly for some observable change resulting    * from GC, as with {@link #awaitClear}.  Because there are no guarantees for the order of GC    * finalization processing, there may still be some unfinished work for the GC to do after this    * method returns.    *    *<p>This method does not create any memory pressure as would be required to cause soft    * references to be processed.    *    * @throws RuntimeException if timed out or interrupted while waiting    * @since 12.0    */
DECL|method|awaitFullGc ()
specifier|public
specifier|static
name|void
name|awaitFullGc
parameter_list|()
block|{
specifier|final
name|CountDownLatch
name|finalizerRan
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|ref
init|=
operator|new
name|WeakReference
argument_list|<
name|Object
argument_list|>
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|finalize
parameter_list|()
block|{
name|finalizerRan
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|await
argument_list|(
name|finalizerRan
argument_list|)
expr_stmt|;
name|awaitClear
argument_list|(
name|ref
argument_list|)
expr_stmt|;
comment|// Hope to catch some stragglers queued up behind our finalizable object
name|System
operator|.
name|runFinalization
argument_list|()
expr_stmt|;
block|}
DECL|method|formatRuntimeException (String format, Object... args)
specifier|private
specifier|static
name|RuntimeException
name|formatRuntimeException
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
operator|new
name|RuntimeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
name|format
argument_list|,
name|args
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

