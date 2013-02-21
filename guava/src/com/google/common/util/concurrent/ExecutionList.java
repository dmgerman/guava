begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|VisibleForTesting
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_comment
comment|/**  *<p>A list of listeners, each with an associated {@code Executor}, that  * guarantees that every {@code Runnable} that is {@linkplain #add added} will  * be executed after {@link #execute()} is called. Any {@code Runnable} added  * after the call to {@code execute} is still guaranteed to execute. There is no  * guarantee, however, that listeners will be executed in the order that they  * are added.  *  *<p>Exceptions thrown by a listener will be propagated up to the executor.  * Any exception thrown during {@code Executor.execute} (e.g., a {@code  * RejectedExecutionException} or an exception thrown by {@linkplain  * MoreExecutors#sameThreadExecutor inline execution}) will be caught and  * logged.  *  * @author Nishant Thakkar  * @author Sven Mawson  * @since 1.0  */
end_comment

begin_class
DECL|class|ExecutionList
specifier|public
specifier|final
class|class
name|ExecutionList
block|{
comment|// Logger to log exceptions caught when running runnables.
DECL|field|log
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|Logger
name|log
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
comment|/**    * The runnable, executor pairs to execute.  This acts as a stack threaded through the     * {@link RunnableExecutorPair#next} field.    */
annotation|@
name|GuardedBy
argument_list|(
literal|"this"
argument_list|)
DECL|field|runnables
specifier|private
name|RunnableExecutorPair
name|runnables
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"this"
argument_list|)
DECL|field|executed
specifier|private
name|boolean
name|executed
decl_stmt|;
comment|/** Creates a new, empty {@link ExecutionList}. */
DECL|method|ExecutionList ()
specifier|public
name|ExecutionList
parameter_list|()
block|{}
comment|/**    * Adds the {@code Runnable} and accompanying {@code Executor} to the list of    * listeners to execute. If execution has already begun, the listener is    * executed immediately.    *    *<p>Note: For fast, lightweight listeners that would be safe to execute in    * any thread, consider {@link MoreExecutors#sameThreadExecutor}. For heavier    * listeners, {@code sameThreadExecutor()} carries some caveats: First, the    * thread that the listener runs in depends on whether the {@code    * ExecutionList} has been executed at the time it is added. In particular,    * listeners may run in the thread that calls {@code add}. Second, the thread    * that calls {@link #execute} may be an internal implementation thread, such    * as an RPC network thread, and {@code sameThreadExecutor()} listeners may    * run in this thread. Finally, during the execution of a {@code    * sameThreadExecutor} listener, all other registered but unexecuted    * listeners are prevented from running, even if those listeners are to run    * in other executors.    */
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
comment|// Lock while we check state.  We must maintain the lock while adding the
comment|// new pair so that another thread can't run the list out from under us.
comment|// We only add to the list if we have not yet started execution.
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|executed
condition|)
block|{
name|runnables
operator|=
operator|new
name|RunnableExecutorPair
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|,
name|runnables
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// Execute the runnable immediately. Because of scheduling this may end up
comment|// getting called before some of the previously added runnables, but we're
comment|// OK with that.  If we want to change the contract to guarantee ordering
comment|// among runnables we'd have to modify the logic here to allow it.
name|executeListener
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/**    * Runs this execution list, executing all existing pairs in the order they    * were added. However, note that listeners added after this point may be    * executed before those previously added, and note that the execution order    * of all listeners is ultimately chosen by the implementations of the    * supplied executors.    *    *<p>This method is idempotent. Calling it several times in parallel is    * semantically equivalent to calling it exactly once.    *    * @since 10.0 (present in 1.0 as {@code run})    */
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
block|{
comment|// Lock while we update our state so the add method above will finish adding
comment|// any listeners before we start to run them.
name|RunnableExecutorPair
name|list
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|executed
condition|)
block|{
return|return;
block|}
name|executed
operator|=
literal|true
expr_stmt|;
name|list
operator|=
name|runnables
expr_stmt|;
name|runnables
operator|=
literal|null
expr_stmt|;
comment|// allow GC to free listeners even if this stays around for a while.
block|}
comment|// If we succeeded then list holds all the runnables we to execute.  The pairs in the stack are
comment|// in the opposite order from how they were added so we need to reverse the list to fulfill our
comment|// contract.
comment|// This is somewhat annoying, but turns out to be very fast in practice.  Alternatively, we
comment|// could drop the contract on the method that enforces this queue like behavior since depending
comment|// on it is likely to be a bug anyway.
comment|// N.B. All writes to the list and the next pointers must have happened before the above
comment|// synchronized block, so we can iterate the list without the lock held here.
name|RunnableExecutorPair
name|reversedList
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|RunnableExecutorPair
name|tmp
init|=
name|list
decl_stmt|;
name|list
operator|=
name|list
operator|.
name|next
expr_stmt|;
name|tmp
operator|.
name|next
operator|=
name|reversedList
expr_stmt|;
name|reversedList
operator|=
name|tmp
expr_stmt|;
block|}
while|while
condition|(
name|reversedList
operator|!=
literal|null
condition|)
block|{
name|executeListener
argument_list|(
name|reversedList
operator|.
name|runnable
argument_list|,
name|reversedList
operator|.
name|executor
argument_list|)
expr_stmt|;
name|reversedList
operator|=
name|reversedList
operator|.
name|next
expr_stmt|;
block|}
block|}
comment|/**    * Submits the given runnable to the given {@link Executor} catching and logging all     * {@linkplain RuntimeException runtime exceptions} thrown by the executor.    */
DECL|method|executeListener (Runnable runnable, Executor executor)
specifier|private
specifier|static
name|void
name|executeListener
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
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
name|log
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
DECL|class|RunnableExecutorPair
specifier|private
specifier|static
specifier|final
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
DECL|field|next
annotation|@
name|Nullable
name|RunnableExecutorPair
name|next
decl_stmt|;
DECL|method|RunnableExecutorPair (Runnable runnable, Executor executor, RunnableExecutorPair next)
name|RunnableExecutorPair
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|,
name|RunnableExecutorPair
name|next
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
name|this
operator|.
name|next
operator|=
name|next
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

