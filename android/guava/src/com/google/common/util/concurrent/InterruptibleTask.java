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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReferenceFieldUpdater
operator|.
name|newUpdater
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReferenceFieldUpdater
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
DECL|class|InterruptibleTask
specifier|abstract
class|class
name|InterruptibleTask
implements|implements
name|Runnable
block|{
comment|// These two fields are used to interrupt running tasks. The thread executing the task publishes
comment|// itself to the 'runner' field and the thread interrupting sets 'doneInterrupting' when it has
comment|// finished interrupting.
DECL|field|runner
specifier|private
specifier|volatile
name|Thread
name|runner
decl_stmt|;
DECL|field|doneInterrupting
specifier|private
specifier|volatile
name|boolean
name|doneInterrupting
decl_stmt|;
DECL|field|ATOMIC_HELPER
specifier|private
specifier|static
specifier|final
name|AtomicHelper
name|ATOMIC_HELPER
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|InterruptibleTask
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
static|static
block|{
name|AtomicHelper
name|helper
decl_stmt|;
try|try
block|{
name|helper
operator|=
operator|new
name|SafeAtomicHelper
argument_list|(
name|newUpdater
argument_list|(
name|InterruptibleTask
operator|.
name|class
argument_list|,
operator|(
name|Class
operator|)
name|Thread
operator|.
name|class
argument_list|,
literal|"runner"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|reflectionFailure
parameter_list|)
block|{
comment|// Some Android 5.0.x Samsung devices have bugs in JDK reflection APIs that cause
comment|// getDeclaredField to throw a NoSuchFieldException when the field is definitely there.
comment|// For these users fallback to a suboptimal implementation, based on synchronized. This will
comment|// be a definite performance hit to those users.
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SafeAtomicHelper is broken!"
argument_list|,
name|reflectionFailure
argument_list|)
expr_stmt|;
name|helper
operator|=
operator|new
name|SynchronizedAtomicHelper
argument_list|()
expr_stmt|;
block|}
name|ATOMIC_HELPER
operator|=
name|helper
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
operator|!
name|ATOMIC_HELPER
operator|.
name|compareAndSetRunner
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
condition|)
block|{
return|return;
comment|// someone else has run or is running.
block|}
try|try
block|{
name|runInterruptibly
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|wasInterrupted
argument_list|()
condition|)
block|{
comment|// We were interrupted, it is possible that the interrupted bit hasn't been set yet. Wait
comment|// for the interrupting thread to set 'doneInterrupting' to true. See interruptTask().
comment|// We want to wait so that we don't interrupt the _next_ thing run on the thread.
comment|// Note: We don't reset the interrupted bit, just wait for it to be set.
comment|// If this is a thread pool thread, the thread pool will reset it for us. Otherwise, the
comment|// interrupted bit may have been intended for something else, so don't clear it.
while|while
condition|(
operator|!
name|doneInterrupting
condition|)
block|{
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|runInterruptibly ()
specifier|abstract
name|void
name|runInterruptibly
parameter_list|()
function_decl|;
DECL|method|wasInterrupted ()
specifier|abstract
name|boolean
name|wasInterrupted
parameter_list|()
function_decl|;
DECL|method|interruptTask ()
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{
comment|// interruptTask is guaranteed to be called at most once, and if runner is non-null when that
comment|// happens, then it must have been the first thread that entered run(). So there is no risk that
comment|// we are interrupting the wrong thread.
name|Thread
name|currentRunner
init|=
name|runner
decl_stmt|;
if|if
condition|(
name|currentRunner
operator|!=
literal|null
condition|)
block|{
name|currentRunner
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|doneInterrupting
operator|=
literal|true
expr_stmt|;
block|}
DECL|class|AtomicHelper
specifier|private
specifier|abstract
specifier|static
class|class
name|AtomicHelper
block|{
comment|/**      * Atomic compare-and-set of the {@link InterruptibleTask#runner} field.      * @return true if successful      */
DECL|method|compareAndSetRunner (InterruptibleTask task, Thread expect, Thread update)
specifier|abstract
name|boolean
name|compareAndSetRunner
parameter_list|(
name|InterruptibleTask
name|task
parameter_list|,
name|Thread
name|expect
parameter_list|,
name|Thread
name|update
parameter_list|)
function_decl|;
block|}
DECL|class|SafeAtomicHelper
specifier|private
specifier|static
specifier|final
class|class
name|SafeAtomicHelper
extends|extends
name|AtomicHelper
block|{
DECL|field|runnerUpdater
specifier|final
name|AtomicReferenceFieldUpdater
argument_list|<
name|InterruptibleTask
argument_list|,
name|Thread
argument_list|>
name|runnerUpdater
decl_stmt|;
DECL|method|SafeAtomicHelper (AtomicReferenceFieldUpdater runnerUpdater)
name|SafeAtomicHelper
parameter_list|(
name|AtomicReferenceFieldUpdater
name|runnerUpdater
parameter_list|)
block|{
name|this
operator|.
name|runnerUpdater
operator|=
name|runnerUpdater
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compareAndSetRunner (InterruptibleTask task, Thread expect, Thread update)
name|boolean
name|compareAndSetRunner
parameter_list|(
name|InterruptibleTask
name|task
parameter_list|,
name|Thread
name|expect
parameter_list|,
name|Thread
name|update
parameter_list|)
block|{
return|return
name|runnerUpdater
operator|.
name|compareAndSet
argument_list|(
name|task
argument_list|,
name|expect
argument_list|,
name|update
argument_list|)
return|;
block|}
block|}
DECL|class|SynchronizedAtomicHelper
specifier|private
specifier|static
specifier|final
class|class
name|SynchronizedAtomicHelper
extends|extends
name|AtomicHelper
block|{
annotation|@
name|Override
DECL|method|compareAndSetRunner (InterruptibleTask task, Thread expect, Thread update)
name|boolean
name|compareAndSetRunner
parameter_list|(
name|InterruptibleTask
name|task
parameter_list|,
name|Thread
name|expect
parameter_list|,
name|Thread
name|update
parameter_list|)
block|{
synchronized|synchronized
init|(
name|task
init|)
block|{
if|if
condition|(
name|task
operator|.
name|runner
operator|==
name|expect
condition|)
block|{
name|task
operator|.
name|runner
operator|=
name|update
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit
