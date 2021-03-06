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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|NullnessCasts
operator|.
name|uncheckedCastNullableTToT
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
name|GwtCompatible
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
name|VisibleForTesting
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
name|AbstractOwnableSynchronizer
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

begin_annotation
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
end_annotation

begin_annotation
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
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_comment
comment|// Some Android 5.0.x Samsung devices have bugs in JDK reflection APIs that cause
end_comment

begin_comment
comment|// getDeclaredField to throw a NoSuchFieldException when the field is definitely there.
end_comment

begin_comment
comment|// Since this class only needs CAS on one field, we can avoid this bug by extending AtomicReference
end_comment

begin_comment
comment|// instead of using an AtomicReferenceFieldUpdater. This reference stores Thread instances
end_comment

begin_comment
comment|// and DONE/INTERRUPTED - they have a common ancestor of Runnable.
end_comment

begin_expr_stmt
DECL|class|InterruptibleTask
specifier|abstract
name|class
name|InterruptibleTask
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AtomicReference
argument_list|<
annotation|@
name|Nullable
name|Runnable
argument_list|>
expr|implements
name|Runnable
block|{
specifier|static
block|{
comment|// Prevent rare disastrous classloading in first call to LockSupport.park.
comment|// See: https://bugs.openjdk.java.net/browse/JDK-8074773
block|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|Class
argument_list|<
name|?
argument_list|>
name|ensureLoaded
operator|=
name|LockSupport
operator|.
name|class
block|;   }
DECL|class|DoNothingRunnable
specifier|private
specifier|static
name|final
name|class
name|DoNothingRunnable
expr|implements
name|Runnable
block|{     @
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
argument_list|()
block|{}
block|}
comment|// The thread executing the task publishes itself to the superclass' reference and the thread
comment|// interrupting sets DONE when it has finished interrupting.
DECL|field|DONE
specifier|private
specifier|static
name|final
name|Runnable
name|DONE
operator|=
operator|new
name|DoNothingRunnable
argument_list|()
block|;
DECL|field|PARKED
specifier|private
specifier|static
name|final
name|Runnable
name|PARKED
operator|=
operator|new
name|DoNothingRunnable
argument_list|()
block|;
comment|// Why 1000?  WHY NOT!
DECL|field|MAX_BUSY_WAIT_SPINS
specifier|private
specifier|static
name|final
name|int
name|MAX_BUSY_WAIT_SPINS
operator|=
literal|1000
block|;    @
name|SuppressWarnings
argument_list|(
literal|"ThreadPriorityCheck"
argument_list|)
comment|// The cow told me to
expr|@
name|Override
DECL|method|run ()
specifier|public
name|final
name|void
name|run
argument_list|()
block|{
comment|/*      * Set runner thread before checking isDone(). If we were to check isDone() first, the task      * might be cancelled before we set the runner thread. That would make it impossible to      * interrupt, yet it will still run, since interruptTask will leave the runner value null,      * allowing the CAS below to succeed.      */
name|Thread
name|currentThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
block|;
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
operator|=
operator|!
name|isDone
argument_list|()
block|;
name|T
name|result
operator|=
literal|null
block|;
name|Throwable
name|error
operator|=
literal|null
block|;
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
end_expr_stmt

begin_expr_stmt
unit|} catch
operator|(
name|Throwable
name|t
operator|)
block|{
name|error
operator|=
name|t
block|;     }
end_expr_stmt

begin_finally
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
name|waitForInterrupt
argument_list|(
name|currentThread
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|run
condition|)
block|{
if|if
condition|(
name|error
operator|==
literal|null
condition|)
block|{
comment|// The cast is safe because of the `run` and `error` checks.
name|afterRanInterruptiblySuccess
argument_list|(
name|uncheckedCastNullableTToT
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|afterRanInterruptiblyFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_finally

begin_function
unit|}    private
DECL|method|waitForInterrupt (Thread currentThread)
name|void
name|waitForInterrupt
parameter_list|(
name|Thread
name|currentThread
parameter_list|)
block|{
comment|/*      * If someone called cancel(true), it is possible that the interrupted bit hasn't been set yet.      * Wait for the interrupting thread to set DONE. (See interruptTask().) We want to wait so that      * the interrupting thread doesn't interrupt the _next_ thing to run on this thread.      *      * Note: We don't reset the interrupted bit, just wait for it to be set. If this is a thread      * pool thread, the thread pool will reset it for us. Otherwise, the interrupted bit may have      * been intended for something else, so don't clear it.      */
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
name|Blocker
name|blocker
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|state
operator|instanceof
name|Blocker
operator|||
name|state
operator|==
name|PARKED
condition|)
block|{
if|if
condition|(
name|state
operator|instanceof
name|Blocker
condition|)
block|{
name|blocker
operator|=
operator|(
name|Blocker
operator|)
name|state
expr_stmt|;
block|}
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
comment|/*          * If we have spun a lot, just park ourselves. This will save CPU while we wait for a slow          * interrupting thread. In theory, interruptTask() should be very fast, but due to          * InterruptibleChannel and JavaLangAccess.blockedOn(Thread, Interruptible), it isn't          * predictable what work might be done. (e.g., close a file and flush buffers to disk). To          * protect ourselves from this, we park ourselves and tell our interrupter that we did so.          */
if|if
condition|(
name|state
operator|==
name|PARKED
operator|||
name|compareAndSet
argument_list|(
name|state
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
comment|// We need to clear the interrupted bit prior to calling park and maintain it in case we
comment|// wake up spuriously.
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
name|blocker
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
comment|/*      * TODO(cpovirk): Clear interrupt status here? We currently don't, which means that an interrupt      * before, during, or after runInterruptibly() (unless it produced an InterruptedException      * caught above) can linger and affect listeners.      */
block|}
end_function

begin_comment
comment|/**    * Called before runInterruptibly - if true, runInterruptibly and afterRanInterruptibly will not    * be called.    */
end_comment

begin_function_decl
DECL|method|isDone ()
specifier|abstract
name|boolean
name|isDone
parameter_list|()
function_decl|;
end_function_decl

begin_comment
comment|/**    * Do interruptible work here - do not complete Futures here, as their listeners could be    * interrupted.    */
end_comment

begin_function_decl
annotation|@
name|ParametricNullness
DECL|method|runInterruptibly ()
specifier|abstract
name|T
name|runInterruptibly
parameter_list|()
throws|throws
name|Exception
function_decl|;
end_function_decl

begin_comment
comment|/**    * Any interruption that happens as a result of calling interruptTask will arrive before this    * method is called. Complete Futures here.    */
end_comment

begin_function_decl
DECL|method|afterRanInterruptiblySuccess (@arametricNullness T result)
specifier|abstract
name|void
name|afterRanInterruptiblySuccess
parameter_list|(
annotation|@
name|ParametricNullness
name|T
name|result
parameter_list|)
function_decl|;
end_function_decl

begin_comment
comment|/**    * Any interruption that happens as a result of calling interruptTask will arrive before this    * method is called. Complete Futures here.    */
end_comment

begin_function_decl
DECL|method|afterRanInterruptiblyFailure (Throwable error)
specifier|abstract
name|void
name|afterRanInterruptiblyFailure
parameter_list|(
name|Throwable
name|error
parameter_list|)
function_decl|;
end_function_decl

begin_comment
comment|/**    * Interrupts the running task. Because this internally calls {@link Thread#interrupt()} which can    * in turn invoke arbitrary code it is not safe to call while holding a lock.    */
end_comment

begin_function
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
condition|)
block|{
name|Blocker
name|blocker
init|=
operator|new
name|Blocker
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|blocker
operator|.
name|setOwner
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|compareAndSet
argument_list|(
name|currentRunner
argument_list|,
name|blocker
argument_list|)
condition|)
block|{
comment|// Thread.interrupt can throw arbitrary exceptions due to the nio InterruptibleChannel API
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
block|}
end_function

begin_comment
comment|/**    * Using this as the blocker object allows introspection and debugging tools to see that the    * currentRunner thread is blocked on the progress of the interruptor thread, which can help    * identify deadlocks.    */
end_comment

begin_class
annotation|@
name|VisibleForTesting
DECL|class|Blocker
specifier|static
specifier|final
class|class
name|Blocker
extends|extends
name|AbstractOwnableSynchronizer
implements|implements
name|Runnable
block|{
DECL|field|task
specifier|private
specifier|final
name|InterruptibleTask
argument_list|<
name|?
argument_list|>
name|task
decl_stmt|;
DECL|method|Blocker (InterruptibleTask<?> task)
specifier|private
name|Blocker
parameter_list|(
name|InterruptibleTask
argument_list|<
name|?
argument_list|>
name|task
parameter_list|)
block|{
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{}
DECL|method|setOwner (Thread thread)
specifier|private
name|void
name|setOwner
parameter_list|(
name|Thread
name|thread
parameter_list|)
block|{
name|super
operator|.
name|setExclusiveOwnerThread
argument_list|(
name|thread
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|task
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

begin_function
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
operator|instanceof
name|Blocker
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
end_function

begin_function_decl
DECL|method|toPendingString ()
specifier|abstract
name|String
name|toPendingString
parameter_list|()
function_decl|;
end_function_decl

unit|}
end_unit

