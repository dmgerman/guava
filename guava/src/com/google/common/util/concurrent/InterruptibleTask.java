begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

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
name|GwtCompatible
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
name|ReflectionSupport
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
name|atomic
operator|.
name|AtomicReference
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
name|locks
operator|.
name|LockSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ReflectionSupport
argument_list|(
name|value
operator|=
name|ReflectionSupport
operator|.
name|Level
operator|.
name|FULL
argument_list|)
comment|// Some Android 5.0.x Samsung devices have bugs in JDK reflection APIs that cause
comment|// getDeclaredField to throw a NoSuchFieldException when the field is definitely there.
comment|// Since this class only needs CAS on one field, we can avoid this bug by extending AtomicReference
comment|// instead of using an AtomicReferenceFieldUpdater. This reference stores Thread instances
comment|// and DONE/INTERRUPTED - they have a common ancestor of Runnable.
DECL|class|InterruptibleTask
specifier|abstract
class|class
name|InterruptibleTask
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AtomicReference
argument_list|<
name|Runnable
argument_list|>
implements|implements
name|Runnable
block|{
static|static
block|{
comment|// Prevent rare disastrous classloading in first call to LockSupport.park.
comment|// See: https://bugs.openjdk.java.net/browse/JDK-8074773
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Class
argument_list|<
name|?
argument_list|>
name|ensureLoaded
init|=
name|LockSupport
operator|.
name|class
decl_stmt|;
block|}
DECL|class|DoNothingRunnable
specifier|private
specifier|static
specifier|final
class|class
name|DoNothingRunnable
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{}
block|}
comment|// The thread executing the task publishes itself to the superclass' reference and the thread
comment|// interrupting sets DONE when it has finished interrupting.
DECL|field|DONE
specifier|private
specifier|static
specifier|final
name|Runnable
name|DONE
init|=
operator|new
name|DoNothingRunnable
argument_list|()
decl_stmt|;
DECL|field|INTERRUPTING
specifier|private
specifier|static
specifier|final
name|Runnable
name|INTERRUPTING
init|=
operator|new
name|DoNothingRunnable
argument_list|()
decl_stmt|;
DECL|field|PARKED
specifier|private
specifier|static
specifier|final
name|Runnable
name|PARKED
init|=
operator|new
name|DoNothingRunnable
argument_list|()
decl_stmt|;
comment|// Why 1000?  WHY NOT!
DECL|field|MAX_BUSY_WAIT_SPINS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_BUSY_WAIT_SPINS
init|=
literal|1000
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ThreadPriorityCheck"
argument_list|)
comment|// The cow told me to
annotation|@
name|Override
DECL|method|run ()
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
comment|/*      * Set runner thread before checking isDone(). If we were to check isDone() first, the task      * might be cancelled before we set the runner thread. That would make it impossible to      * interrupt, yet it will still run, since interruptTask will leave the runner value null,      * allowing the CAS below to succeed.      */
name|Thread
name|currentThread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|compareAndSet
argument_list|(
literal|null
argument_list|,
name|currentThread
argument_list|)
condition|)
block|{
return|return;
comment|// someone else has run or is running.
block|}
name|boolean
name|run
init|=
operator|!
name|isDone
argument_list|()
decl_stmt|;
name|T
name|result
init|=
literal|null
decl_stmt|;
name|Throwable
name|error
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|run
condition|)
block|{
name|result
operator|=
name|runInterruptibly
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|error
operator|=
name|t
expr_stmt|;
block|}
finally|finally
block|{
comment|// Attempt to set the task as done so that further attempts to interrupt will fail.
if|if
condition|(
operator|!
name|compareAndSet
argument_list|(
name|currentThread
argument_list|,
name|DONE
argument_list|)
condition|)
block|{
comment|// If we were interrupted, it is possible that the interrupted bit hasn't been set yet. Wait
comment|// for the interrupting thread to set DONE. See interruptTask().
comment|// We want to wait so that we don't interrupt the _next_ thing run on the thread.
comment|// Note: We don't reset the interrupted bit, just wait for it to be set.
comment|// If this is a thread pool thread, the thread pool will reset it for us. Otherwise, the
comment|// interrupted bit may have been intended for something else, so don't clear it.
name|boolean
name|restoreInterruptedBit
init|=
literal|false
decl_stmt|;
name|int
name|spinCount
init|=
literal|0
decl_stmt|;
comment|// Interrupting Cow Says:
comment|//  ______
comment|//< Spin>
comment|//  ------
comment|//        \   ^__^
comment|//         \  (oo)\_______
comment|//            (__)\       )\/\
comment|//                ||----w |
comment|//                ||     ||
name|Runnable
name|state
init|=
name|get
argument_list|()
decl_stmt|;
while|while
condition|(
name|state
operator|==
name|INTERRUPTING
operator|||
name|state
operator|==
name|PARKED
condition|)
block|{
name|spinCount
operator|++
expr_stmt|;
if|if
condition|(
name|spinCount
operator|>
name|MAX_BUSY_WAIT_SPINS
condition|)
block|{
comment|// If we have spun a lot just park ourselves.
comment|// This will save CPU while we wait for a slow interrupting thread.  In theory
comment|// interruptTask() should be very fast but due to InterruptibleChannel and
comment|// JavaLangAccess.blockedOn(Thread, Interruptible), it isn't predictable what work might
comment|// be done.  (e.g. close a file and flush buffers to disk).  To protect ourselve from
comment|// this we park ourselves and tell our interrupter that we did so.
if|if
condition|(
name|state
operator|==
name|PARKED
operator|||
name|compareAndSet
argument_list|(
name|INTERRUPTING
argument_list|,
name|PARKED
argument_list|)
condition|)
block|{
comment|// Interrupting Cow Says:
comment|//  ______
comment|//< Park>
comment|//  ------
comment|//        \   ^__^
comment|//         \  (oo)\_______
comment|//            (__)\       )\/\
comment|//                ||----w |
comment|//                ||     ||
comment|// We need to clear the interrupted bit prior to calling park and maintain it in case
comment|// we wake up spuriously.
name|restoreInterruptedBit
operator|=
name|Thread
operator|.
name|interrupted
argument_list|()
operator|||
name|restoreInterruptedBit
expr_stmt|;
name|LockSupport
operator|.
name|park
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
block|}
name|state
operator|=
name|get
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|restoreInterruptedBit
condition|)
block|{
name|currentThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
comment|/*          * TODO(cpovirk): Clear interrupt status here? We currently don't, which means that an          * interrupt before, during, or after runInterruptibly() (unless it produced an          * InterruptedException caught above) can linger and affect listeners.          */
block|}
if|if
condition|(
name|run
condition|)
block|{
name|afterRanInterruptibly
argument_list|(
name|result
argument_list|,
name|error
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Called before runInterruptibly - if true, runInterruptibly and afterRanInterruptibly will not    * be called.    */
DECL|method|isDone ()
specifier|abstract
name|boolean
name|isDone
parameter_list|()
function_decl|;
comment|/**    * Do interruptible work here - do not complete Futures here, as their listeners could be    * interrupted.    */
DECL|method|runInterruptibly ()
specifier|abstract
name|T
name|runInterruptibly
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**    * Any interruption that happens as a result of calling interruptTask will arrive before this    * method is called. Complete Futures here.    */
DECL|method|afterRanInterruptibly (@ullable T result, @Nullable Throwable error)
specifier|abstract
name|void
name|afterRanInterruptibly
parameter_list|(
annotation|@
name|Nullable
name|T
name|result
parameter_list|,
annotation|@
name|Nullable
name|Throwable
name|error
parameter_list|)
function_decl|;
comment|/**    * Interrupts the running task. Because this internally calls {@link Thread#interrupt()} which can in turn    * invoke arbitrary code it is not safe to call while holding a lock.    */
DECL|method|interruptTask ()
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{
comment|// Since the Thread is replaced by DONE before run() invokes listeners or returns, if we succeed
comment|// in this CAS, there's no risk of interrupting the wrong thread or interrupting a thread that
comment|// isn't currently executing this task.
name|Runnable
name|currentRunner
init|=
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentRunner
operator|instanceof
name|Thread
operator|&&
name|compareAndSet
argument_list|(
name|currentRunner
argument_list|,
name|INTERRUPTING
argument_list|)
condition|)
block|{
comment|// Thread.interrupt can throw aribitrary exceptions due to the nio InterruptibleChannel API
comment|// This will make sure that tasks don't get stuck busy waiting.
comment|// Some of this is fixed in jdk11 (see https://bugs.openjdk.java.net/browse/JDK-8198692) but
comment|// not all.  See the test cases for examples on how this can happen.
try|try
block|{
operator|(
operator|(
name|Thread
operator|)
name|currentRunner
operator|)
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|Runnable
name|prev
init|=
name|getAndSet
argument_list|(
name|DONE
argument_list|)
decl_stmt|;
if|if
condition|(
name|prev
operator|==
name|PARKED
condition|)
block|{
name|LockSupport
operator|.
name|unpark
argument_list|(
operator|(
name|Thread
operator|)
name|currentRunner
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
name|Runnable
name|state
init|=
name|get
argument_list|()
decl_stmt|;
specifier|final
name|String
name|result
decl_stmt|;
if|if
condition|(
name|state
operator|==
name|DONE
condition|)
block|{
name|result
operator|=
literal|"running=[DONE]"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|==
name|INTERRUPTING
condition|)
block|{
name|result
operator|=
literal|"running=[INTERRUPTED]"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|state
operator|instanceof
name|Thread
condition|)
block|{
comment|// getName is final on Thread, no need to worry about exceptions
name|result
operator|=
literal|"running=[RUNNING ON "
operator|+
operator|(
operator|(
name|Thread
operator|)
name|state
operator|)
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|"running=[NOT STARTED YET]"
expr_stmt|;
block|}
return|return
name|result
operator|+
literal|", "
operator|+
name|toPendingString
argument_list|()
return|;
block|}
DECL|method|toPendingString ()
specifier|abstract
name|String
name|toPendingString
parameter_list|()
function_decl|;
block|}
end_class

end_unit

