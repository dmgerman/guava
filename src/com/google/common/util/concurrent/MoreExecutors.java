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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|AbstractExecutorService
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
name|Executors
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
name|ExecutorService
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
name|RejectedExecutionException
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
name|ScheduledExecutorService
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
name|ScheduledThreadPoolExecutor
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
name|ThreadFactory
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
name|ThreadPoolExecutor
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
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
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
name|TimeUnit
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
name|Condition
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
name|Lock
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
name|ReentrantLock
import|;
end_import

begin_comment
comment|/**  * Factory and utility methods for {@link java.util.concurrent.Executor}, {@link  * ExecutorService}, and {@link ThreadFactory}.  *  * @author Eric Fellheimer  * @author Kyle Littlefield  * @author Justin Mahoney  * @since 3  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|MoreExecutors
specifier|public
specifier|final
class|class
name|MoreExecutors
block|{
DECL|method|MoreExecutors ()
specifier|private
name|MoreExecutors
parameter_list|()
block|{}
comment|/**    * Converts the given ThreadPoolExecutor into an ExecutorService that exits    * when the application is complete.  It does so by using daemon threads and    * adding a shutdown hook to wait for their completion.    *    *<p>This is mainly for fixed thread pools.    * See {@link java.util.concurrent.Executors#newFixedThreadPool(int)}.    *    * @param executor the executor to modify to make sure it exits when the    *        application is finished    * @param terminationTimeout how long to wait for the executor to    *        finish before terminating the JVM    * @param timeUnit unit of time for the time parameter    * @return an unmodifiable version of the input which will not hang the JVM    */
DECL|method|getExitingExecutorService ( ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
specifier|public
specifier|static
name|ExecutorService
name|getExitingExecutorService
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|,
name|long
name|terminationTimeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|executor
operator|.
name|setThreadFactory
argument_list|(
name|daemonThreadFactory
argument_list|(
name|executor
operator|.
name|getThreadFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|unconfigurableExecutorService
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|addDelayedShutdownHook
argument_list|(
name|service
argument_list|,
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
comment|/**    * Converts the given ScheduledThreadPoolExecutor into a    * ScheduledExecutorService that exits when the application is complete.  It    * does so by using daemon threads and adding a shutdown hook to wait for    * their completion.    *    *<p>This is mainly for fixed thread pools.    * See {@link java.util.concurrent.Executors#newScheduledThreadPool(int)}.    *    * @param executor the executor to modify to make sure it exits when the    *        application is finished    * @param terminationTimeout how long to wait for the executor to    *        finish before terminating the JVM    * @param timeUnit unit of time for the time parameter    * @return an unmodifiable version of the input which will not hang the JVM    */
DECL|method|getExitingScheduledExecutorService ( ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
specifier|public
specifier|static
name|ScheduledExecutorService
name|getExitingScheduledExecutorService
parameter_list|(
name|ScheduledThreadPoolExecutor
name|executor
parameter_list|,
name|long
name|terminationTimeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|executor
operator|.
name|setThreadFactory
argument_list|(
name|daemonThreadFactory
argument_list|(
name|executor
operator|.
name|getThreadFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ScheduledExecutorService
name|service
init|=
name|Executors
operator|.
name|unconfigurableScheduledExecutorService
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|addDelayedShutdownHook
argument_list|(
name|service
argument_list|,
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|service
return|;
block|}
comment|/**    * Add a shutdown hook to wait for thread completion in the given    * {@link ExecutorService service}.  This is useful if the given service uses    * daemon threads, and we want to keep the JVM from exiting immediately on    * shutdown, instead giving these daemon threads a chance to terminate    * normally.    * @param service ExecutorService which uses daemon threads    * @param terminationTimeout how long to wait for the executor to finish    *        before terminating the JVM    * @param timeUnit unit of time for the time parameter    */
DECL|method|addDelayedShutdownHook ( final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit)
specifier|public
specifier|static
name|void
name|addDelayedShutdownHook
parameter_list|(
specifier|final
name|ExecutorService
name|service
parameter_list|,
specifier|final
name|long
name|terminationTimeout
parameter_list|,
specifier|final
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// We'd like to log progress and failures that may arise in the
comment|// following code, but unfortunately the behavior of logging
comment|// is undefined in shutdown hooks.
comment|// This is because the logging code installs a shutdown hook of its
comment|// own. See Cleaner class inside {@link LogManager}.
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|service
operator|.
name|awaitTermination
argument_list|(
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ignored
parameter_list|)
block|{
comment|// We're shutting down anyway, so just ignore.
block|}
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Converts the given ThreadPoolExecutor into an ExecutorService that exits    * when the application is complete.  It does so by using daemon threads and    * adding a shutdown hook to wait for their completion.    *    *<p>This method waits 120 seconds before continuing with JVM termination,    * even if the executor has not finished its work.    *    *<p>This is mainly for fixed thread pools.    * See {@link java.util.concurrent.Executors#newFixedThreadPool(int)}.    *    * @param executor the executor to modify to make sure it exits when the    *        application is finished    * @return an unmodifiable version of the input which will not hang the JVM    */
DECL|method|getExitingExecutorService ( ThreadPoolExecutor executor)
specifier|public
specifier|static
name|ExecutorService
name|getExitingExecutorService
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
return|return
name|getExitingExecutorService
argument_list|(
name|executor
argument_list|,
literal|120
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
comment|/**    * Converts the given ThreadPoolExecutor into a ScheduledExecutorService that    * exits when the application is complete.  It does so by using daemon threads    * and adding a shutdown hook to wait for their completion.    *    *<p>This method waits 120 seconds before continuing with JVM termination,    * even if the executor has not finished its work.    *    *<p>This is mainly for fixed thread pools.    * See {@link java.util.concurrent.Executors#newScheduledThreadPool(int)}.    *    * @param executor the executor to modify to make sure it exits when the    *        application is finished    * @return an unmodifiable version of the input which will not hang the JVM    */
DECL|method|getExitingScheduledExecutorService ( ScheduledThreadPoolExecutor executor)
specifier|public
specifier|static
name|ScheduledExecutorService
name|getExitingScheduledExecutorService
parameter_list|(
name|ScheduledThreadPoolExecutor
name|executor
parameter_list|)
block|{
return|return
name|getExitingScheduledExecutorService
argument_list|(
name|executor
argument_list|,
literal|120
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
comment|/**    * Returns a {@link ThreadFactory} which creates daemon threads. This is    * implemented by wrapping {@link    * java.util.concurrent.Executors#defaultThreadFactory()}, marking all new    * threads as daemon threads.    *    * @return a {@link ThreadFactory} which creates daemon threads    */
comment|// TODO: Deprecate this method.
DECL|method|daemonThreadFactory ()
specifier|public
specifier|static
name|ThreadFactory
name|daemonThreadFactory
parameter_list|()
block|{
return|return
name|daemonThreadFactory
argument_list|(
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Wraps another {@link ThreadFactory}, making all new threads daemon threads.    *    * @param factory the {@link ThreadFactory} used to generate new threads    * @return a new {@link ThreadFactory} backed by {@code factory} whose created    *         threads are all daemon threads    */
comment|// TODO: Deprecate this method.
DECL|method|daemonThreadFactory (ThreadFactory factory)
specifier|public
specifier|static
name|ThreadFactory
name|daemonThreadFactory
parameter_list|(
name|ThreadFactory
name|factory
parameter_list|)
block|{
return|return
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setThreadFactory
argument_list|(
name|factory
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Creates an executor service that runs each task in the thread    * that invokes {@code execute/submit}, as in {@link CallerRunsPolicy}  This    * applies both to individually submitted tasks and to collections of tasks    * submitted via {@code invokeAll} or {@code invokeAny}.  In the latter case,    * tasks will run serially on the calling thread.  Tasks are run to    * completion before a {@code Future} is returned to the caller (unless the    * executor has been shutdown).    *    *<p>Although all tasks are immediately executed in the thread that    * submitted the task, this {@code ExecutorService} imposes a small    * locking overhead on each task submission in order to implement shutdown    * and termination behavior.    *    *<p>The implementation deviates from the {@code ExecutorService}    * specification with regards to the {@code shutdownNow} method.  First,    * "best-effort" with regards to canceling running tasks is implemented    * as "no-effort".  No interrupts or other attempts are made to stop    * threads executing tasks.  Second, the returned list will always be empty,    * as any submitted task is considered to have started execution.    * This applies also to tasks given to {@code invokeAll} or {@code invokeAny}    * which are pending serial execution, even the subset of the tasks that    * have not yet started execution.  It is unclear from the    * {@code ExecutorService} specification if these should be included, and    * it's much easier to implement the interpretation that they not be.    * Finally, a call to {@code shutdown} or {@code shutdownNow} may result    * in concurrent calls to {@code invokeAll/invokeAny} throwing    * RejectedExecutionException, although a subset of the tasks may already    * have been executed.    */
DECL|method|sameThreadExecutor ()
specifier|public
specifier|static
name|ExecutorService
name|sameThreadExecutor
parameter_list|()
block|{
return|return
operator|new
name|SameThreadExecutorService
argument_list|()
return|;
block|}
comment|// See sameThreadExecutor javadoc for behavioral notes.
DECL|class|SameThreadExecutorService
specifier|private
specifier|static
class|class
name|SameThreadExecutorService
extends|extends
name|AbstractExecutorService
block|{
comment|/**      * Lock used whenever accessing the state variables      * (runningTasks, shutdown, terminationCondition) of the executor      */
DECL|field|lock
specifier|private
specifier|final
name|Lock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
comment|/** Signaled after the executor is shutdown and running tasks are done */
DECL|field|termination
specifier|private
specifier|final
name|Condition
name|termination
init|=
name|lock
operator|.
name|newCondition
argument_list|()
decl_stmt|;
comment|/*      * Conceptually, these two variables describe the executor being in      * one of three states:      *   - Active: shutdown == false      *   - Shutdown: runningTasks> 0 and shutdown == true      *   - Terminated: runningTasks == 0 and shutdown == true      */
DECL|field|runningTasks
specifier|private
name|int
name|runningTasks
init|=
literal|0
decl_stmt|;
DECL|field|shutdown
specifier|private
name|boolean
name|shutdown
init|=
literal|false
decl_stmt|;
comment|/*@Override*/
DECL|method|execute (Runnable command)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|startTask
argument_list|()
expr_stmt|;
try|try
block|{
name|command
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|endTask
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*@Override*/
DECL|method|isShutdown ()
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|shutdown
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*@Override*/
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|shutdown
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|// See sameThreadExecutor javadoc for unusual behavior of this method.
comment|/*@Override*/
DECL|method|shutdownNow ()
specifier|public
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|()
block|{
name|shutdown
argument_list|()
expr_stmt|;
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/*@Override*/
DECL|method|isTerminated ()
specifier|public
name|boolean
name|isTerminated
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|shutdown
operator|&&
name|runningTasks
operator|==
literal|0
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*@Override*/
DECL|method|awaitTermination (long timeout, TimeUnit unit)
specifier|public
name|boolean
name|awaitTermination
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|long
name|nanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
if|if
condition|(
name|isTerminated
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|nanos
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|nanos
operator|=
name|termination
operator|.
name|awaitNanos
argument_list|(
name|nanos
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Checks if the executor has been shut down and increments the running      * task count.      *      * @throws RejectedExecutionException if the executor has been previously      *         shutdown      */
DECL|method|startTask ()
specifier|private
name|void
name|startTask
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|isShutdown
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Executor already shutdown"
argument_list|)
throw|;
block|}
name|runningTasks
operator|++
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Decrements the running task count.      */
DECL|method|endTask ()
specifier|private
name|void
name|endTask
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|runningTasks
operator|--
expr_stmt|;
if|if
condition|(
name|isTerminated
argument_list|()
condition|)
block|{
name|termination
operator|.
name|signalAll
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

