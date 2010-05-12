begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
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
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|Executor
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

begin_comment
comment|/**  *<p>A list of ({@code Runnable}, {@code Executor}) pairs that guarantees  * that every {@code Runnable} that is added using the add method will be  * executed in its associated {@code Executor} after {@link #run()} is called.  * {@code Runnable}s added after {@code run} is called are still guaranteed to  * execute.  *   * @author Nishant Thakkar  * @author Sven Mawson  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ExecutionList
specifier|public
class|class
name|ExecutionList
implements|implements
name|Runnable
block|{
comment|// Logger to log exceptions caught when running runnables.
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|ExecutionList
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// The runnable,executor pairs to execute.
DECL|field|runnables
specifier|private
specifier|final
name|Queue
argument_list|<
name|RunnableExecutorPair
argument_list|>
name|runnables
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
comment|// Boolean we use mark when execution has started.  Only accessed from within
comment|// synchronized blocks.
DECL|field|executed
specifier|private
name|boolean
name|executed
init|=
literal|false
decl_stmt|;
comment|/**    * Add the runnable/executor pair to the list of pairs to execute.  Executes    * the pair immediately if we've already started execution.    */
DECL|method|add (Runnable runnable, Executor executor)
specifier|public
name|void
name|add
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
comment|// Fail fast on a null.  We throw NPE here because the contract of
comment|// Executor states that it throws NPE on null listener, so we propagate
comment|// that contract up into the add method as well.
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|runnable
argument_list|,
literal|"Runnable was null."
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|executor
argument_list|,
literal|"Executor was null."
argument_list|)
expr_stmt|;
name|boolean
name|executeImmediate
init|=
literal|false
decl_stmt|;
comment|// Lock while we check state.  We must maintain the lock while adding the
comment|// new pair so that another thread can't run the list out from under us.
comment|// We only add to the list if we have not yet started execution.
synchronized|synchronized
init|(
name|runnables
init|)
block|{
if|if
condition|(
operator|!
name|executed
condition|)
block|{
name|runnables
operator|.
name|add
argument_list|(
operator|new
name|RunnableExecutorPair
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executeImmediate
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// Execute the runnable immediately.  Because of scheduling this may end up
comment|// getting called before some of the previously added runnables, but we're
comment|// ok with that.  If we want to change the contract to guarantee ordering
comment|// among runnables we'd have to modify the logic here to allow it.
if|if
condition|(
name|executeImmediate
condition|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Runs this execution list, executing all pairs in the order they were    * added.  Pairs added after this method has started executing the list will    * be executed immediately.    */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// Lock while we update our state so the add method above will finish adding
comment|// any listeners before we start to run them.
synchronized|synchronized
init|(
name|runnables
init|)
block|{
name|executed
operator|=
literal|true
expr_stmt|;
block|}
comment|// At this point the runnables will never be modified by another
comment|// thread, so we are safe using it outside of the synchronized block.
while|while
condition|(
operator|!
name|runnables
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|runnables
operator|.
name|poll
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|RunnableExecutorPair
specifier|private
specifier|static
class|class
name|RunnableExecutorPair
block|{
DECL|field|runnable
specifier|final
name|Runnable
name|runnable
decl_stmt|;
DECL|field|executor
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|method|RunnableExecutorPair (Runnable runnable, Executor executor)
name|RunnableExecutorPair
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|runnable
operator|=
name|runnable
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
DECL|method|execute ()
name|void
name|execute
parameter_list|()
block|{
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Log it and keep going, bad runnable and/or executor.  Don't
comment|// punish the other runnables if we're given a bad one.  We only
comment|// catch RuntimeException because we want Errors to propagate up.
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"RuntimeException while executing runnable "
operator|+
name|runnable
operator|+
literal|" with executor "
operator|+
name|executor
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

