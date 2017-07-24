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
name|javax
operator|.
name|annotation
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
while|while
condition|(
name|get
argument_list|()
operator|==
name|INTERRUPTING
condition|)
block|{
name|Thread
operator|.
name|yield
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
name|set
argument_list|(
name|DONE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|abstract
name|String
name|toString
parameter_list|()
function_decl|;
block|}
end_class

end_unit

