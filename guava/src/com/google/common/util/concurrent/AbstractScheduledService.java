begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Throwables
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
name|Callable
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
name|ReentrantLock
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
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_comment
comment|/**  * Base class for services that can implement {@link #startUp} and {@link #shutDown} but while in  * the "running" state need to perform a periodic task.  Subclasses can implement {@link #startUp},  * {@link #shutDown} and also a {@link #runOneIteration} method that will be executed periodically.  *  *<p>This class uses the {@link ScheduledExecutorService} returned from {@link #executor} to run  * the {@link #startUp} and {@link #shutDown} methods and also uses that service to schedule the  * {@link #runOneIteration} that will be executed periodically as specified by its  * {@link Scheduler}. When this service is asked to stop via {@link #stop} or {@link #stopAndWait},  * it will cancel the periodic task (but not interrupt it) and wait for it to stop before running  * the {@link #shutDown} method.  *  *<p>Subclasses are guaranteed that the life cycle methods ({@link #runOneIteration}, {@link  * #startUp} and {@link #shutDown}) will never run concurrently. Notably, if any execution of {@link  * #runOneIteration} takes longer than its schedule defines, then subsequent executions may start  * late.  Also, all life cycle methods are executed with a lock held, so subclasses can safely  * modify shared state without additional synchronization necessary for visibility to later  * executions of the life cycle methods.  *  *<h3>Usage Example</h3>  *  * Here is a sketch of a service which crawls a website and uses the scheduling capabilities to  * rate limit itself.<pre> {@code  * class CrawlingService extends AbstractScheduledService {  *   private Set<Uri> visited;  *   private Queue<Uri> toCrawl;  *   protected void startUp() throws Exception {  *     toCrawl = readStartingUris();  *   }  *  *   protected void runOneIteration() throws Exception {  *     Uri uri = toCrawl.remove();  *     Collection<Uri> newUris = crawl(uri);  *     visited.add(uri);  *     for (Uri newUri : newUris) {  *       if (!visited.contains(newUri)) { toCrawl.add(newUri); }  *     }  *   }  *  *   protected void shutDown() throws Exception {  *     saveUris(toCrawl);  *   }  *  *   protected Scheduler scheduler() {  *     return Scheduler.newFixedRateSchedule(0, 1, TimeUnit.SECONDS);  *   }  * }}</pre>  *  * This class uses the life cycle methods to read in a list of starting URIs and save the set of  * outstanding URIs when shutting down.  Also, it takes advantage of the scheduling functionality to  * rate limit the number of queries we perform.  *  * @author Luke Sandberg  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractScheduledService
specifier|public
specifier|abstract
class|class
name|AbstractScheduledService
implements|implements
name|Service
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|AbstractScheduledService
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * A scheduler defines the policy for how the {@link AbstractScheduledService} should run its    * task.    *    *<p>Consider using the {@link #newFixedDelaySchedule} and {@link #newFixedRateSchedule} factory    * methods, these provide {@link Scheduler} instances for the common use case of running the    * service with a fixed schedule.  If more flexibility is needed then consider subclassing    * {@link CustomScheduler}.    *    * @author Luke Sandberg    * @since 11.0    */
DECL|class|Scheduler
specifier|public
specifier|abstract
specifier|static
class|class
name|Scheduler
block|{
comment|/**      * Returns a {@link Scheduler} that schedules the task using the      * {@link ScheduledExecutorService#scheduleWithFixedDelay} method.      *      * @param initialDelay the time to delay first execution      * @param delay the delay between the termination of one execution and the commencement of the      *        next      * @param unit the time unit of the initialDelay and delay parameters      */
DECL|method|newFixedDelaySchedule (final long initialDelay, final long delay, final TimeUnit unit)
specifier|public
specifier|static
name|Scheduler
name|newFixedDelaySchedule
parameter_list|(
specifier|final
name|long
name|initialDelay
parameter_list|,
specifier|final
name|long
name|delay
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|Scheduler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|AbstractService
name|service
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Runnable
name|task
parameter_list|)
block|{
return|return
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|task
argument_list|,
name|initialDelay
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**      * Returns a {@link Scheduler} that schedules the task using the      * {@link ScheduledExecutorService#scheduleAtFixedRate} method.      *      * @param initialDelay the time to delay first execution      * @param period the period between successive executions of the task      * @param unit the time unit of the initialDelay and period parameters      */
DECL|method|newFixedRateSchedule (final long initialDelay, final long period, final TimeUnit unit)
specifier|public
specifier|static
name|Scheduler
name|newFixedRateSchedule
parameter_list|(
specifier|final
name|long
name|initialDelay
parameter_list|,
specifier|final
name|long
name|period
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|Scheduler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|AbstractService
name|service
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Runnable
name|task
parameter_list|)
block|{
return|return
name|executor
operator|.
name|scheduleAtFixedRate
argument_list|(
name|task
argument_list|,
name|initialDelay
argument_list|,
name|period
argument_list|,
name|unit
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/** Schedules the task to run on the provided executor on behalf of the service.  */
DECL|method|schedule (AbstractService service, ScheduledExecutorService executor, Runnable runnable)
specifier|abstract
name|Future
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|AbstractService
name|service
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Runnable
name|runnable
parameter_list|)
function_decl|;
DECL|method|Scheduler ()
specifier|private
name|Scheduler
parameter_list|()
block|{}
block|}
comment|/* use AbstractService for state management */
DECL|field|delegate
specifier|private
specifier|final
name|AbstractService
name|delegate
init|=
operator|new
name|AbstractService
argument_list|()
block|{
comment|// A handle to the running task so that we can stop it when a shutdown has been requested.
comment|// These two fields are volatile because their values will be accessed from multiple threads.
specifier|private
specifier|volatile
name|Future
argument_list|<
name|?
argument_list|>
name|runningTask
decl_stmt|;
specifier|private
specifier|volatile
name|ScheduledExecutorService
name|executorService
decl_stmt|;
comment|// This lock protects the task so we can ensure that none of the template methods (startUp,
comment|// shutDown or runOneIteration) run concurrently with one another.
specifier|private
specifier|final
name|ReentrantLock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Runnable
name|task
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|AbstractScheduledService
operator|.
name|this
operator|.
name|runOneIteration
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
try|try
block|{
name|shutDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignored
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Error while attempting to shut down the service after failure."
argument_list|,
name|ignored
argument_list|)
expr_stmt|;
block|}
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|t
argument_list|)
throw|;
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
decl_stmt|;
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|doStart
parameter_list|()
block|{
name|executorService
operator|=
name|executor
argument_list|()
expr_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|startUp
argument_list|()
expr_stmt|;
name|runningTask
operator|=
name|scheduler
argument_list|()
operator|.
name|schedule
argument_list|(
name|delegate
argument_list|,
name|executorService
argument_list|,
name|task
argument_list|)
expr_stmt|;
name|notifyStarted
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|t
argument_list|)
throw|;
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|doStop
parameter_list|()
block|{
name|runningTask
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
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
name|state
argument_list|()
operator|!=
name|State
operator|.
name|STOPPING
condition|)
block|{
comment|// This means that the state has changed since we were scheduled.  This implies that
comment|// an execution of runOneIteration has thrown an exception and we have transitioned
comment|// to a failed state, also this means that shutDown has already been called, so we
comment|// do not want to call it again.
return|return;
block|}
name|shutDown
argument_list|()
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
name|notifyStopped
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|notifyFailed
argument_list|(
name|t
argument_list|)
expr_stmt|;
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
comment|/**    * Run one iteration of the scheduled task. If any invocation of this method throws an exception,    * the service will transition to the {@link Service.State#FAILED} state and this method will no    * longer be called.    */
DECL|method|runOneIteration ()
specifier|protected
specifier|abstract
name|void
name|runOneIteration
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**    * Start the service.    *    *<p>By default this method does nothing.    */
DECL|method|startUp ()
specifier|protected
name|void
name|startUp
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Stop the service. This is guaranteed not to run concurrently with {@link #runOneIteration}.    *    *<p>By default this method does nothing.    */
DECL|method|shutDown ()
specifier|protected
name|void
name|shutDown
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/**    * Returns the {@link Scheduler} object used to configure this service.  This method will only be    * called once.    */
DECL|method|scheduler ()
specifier|protected
specifier|abstract
name|Scheduler
name|scheduler
parameter_list|()
function_decl|;
comment|/**    * Returns the {@link ScheduledExecutorService} that will be used to execute the {@link #startUp},    * {@link #runOneIteration} and {@link #shutDown} methods.  The executor will not be    * {@link ScheduledExecutorService#shutdown} when this service stops. Subclasses may override this    * method to use a custom {@link ScheduledExecutorService} instance.    *    *<p>By default this returns a new {@link ScheduledExecutorService} with a single thread thread    * pool.  This method will only be called once.    */
DECL|method|executor ()
specifier|protected
name|ScheduledExecutorService
name|executor
parameter_list|()
block|{
return|return
name|Executors
operator|.
name|newSingleThreadScheduledExecutor
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" ["
operator|+
name|state
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|// We override instead of using ForwardingService so that these can be final.
DECL|method|start ()
annotation|@
name|Override
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|start
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|start
argument_list|()
return|;
block|}
DECL|method|startAndWait ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|startAndWait
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|startAndWait
argument_list|()
return|;
block|}
DECL|method|isRunning ()
annotation|@
name|Override
specifier|public
specifier|final
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isRunning
argument_list|()
return|;
block|}
DECL|method|state ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|state
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|state
argument_list|()
return|;
block|}
DECL|method|stop ()
annotation|@
name|Override
specifier|public
specifier|final
name|ListenableFuture
argument_list|<
name|State
argument_list|>
name|stop
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|stop
argument_list|()
return|;
block|}
DECL|method|stopAndWait ()
annotation|@
name|Override
specifier|public
specifier|final
name|State
name|stopAndWait
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|stopAndWait
argument_list|()
return|;
block|}
DECL|method|addListener (Listener listener, Executor executor)
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|addListener
parameter_list|(
name|Listener
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|delegate
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/**    * A {@link Scheduler} that provides a convenient way for the {@link AbstractScheduledService} to    * use a dynamically changing schedule.  After every execution of the task, assuming it hasn't    * been cancelled, the {@link #getNextSchedule} method will be called.    *    * @author Luke Sandberg    * @since 11.0    */
annotation|@
name|Beta
DECL|class|CustomScheduler
specifier|public
specifier|abstract
specifier|static
class|class
name|CustomScheduler
extends|extends
name|Scheduler
block|{
comment|/**      * A callable class that can reschedule itself using a {@link CustomScheduler}.      */
DECL|class|ReschedulableCallable
specifier|private
class|class
name|ReschedulableCallable
extends|extends
name|ForwardingFuture
argument_list|<
name|Void
argument_list|>
implements|implements
name|Callable
argument_list|<
name|Void
argument_list|>
block|{
comment|/** The underlying task. */
DECL|field|wrappedRunnable
specifier|private
specifier|final
name|Runnable
name|wrappedRunnable
decl_stmt|;
comment|/** The executor on which this Callable will be scheduled. */
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
comment|/**        * The service that is managing this callable.  This is used so that failure can be        * reported properly.        */
DECL|field|service
specifier|private
specifier|final
name|AbstractService
name|service
decl_stmt|;
comment|/**        * This lock is used to ensure safe and correct cancellation, it ensures that a new task is        * not scheduled while a cancel is ongoing.  Also it protects the currentFuture variable to        * ensure that it is assigned atomically with being scheduled.        */
DECL|field|lock
specifier|private
specifier|final
name|ReentrantLock
name|lock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
comment|/** The future that represents the next execution of this task.*/
annotation|@
name|GuardedBy
argument_list|(
literal|"lock"
argument_list|)
DECL|field|currentFuture
specifier|private
name|Future
argument_list|<
name|Void
argument_list|>
name|currentFuture
decl_stmt|;
DECL|method|ReschedulableCallable (AbstractService service, ScheduledExecutorService executor, Runnable runnable)
name|ReschedulableCallable
parameter_list|(
name|AbstractService
name|service
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Runnable
name|runnable
parameter_list|)
block|{
name|this
operator|.
name|wrappedRunnable
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
name|service
operator|=
name|service
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|wrappedRunnable
operator|.
name|run
argument_list|()
expr_stmt|;
name|reschedule
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**        * Atomically reschedules this task and assigns the new future to {@link #currentFuture}.        */
DECL|method|reschedule ()
specifier|public
name|void
name|reschedule
parameter_list|()
block|{
comment|// We reschedule ourselves with a lock held for two reasons. 1. we want to make sure that
comment|// cancel calls cancel on the correct future. 2. we want to make sure that the assignment
comment|// to currentFuture doesn't race with itself so that currentFuture is assigned in the
comment|// correct order.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|currentFuture
operator|==
literal|null
operator|||
operator|!
name|currentFuture
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
specifier|final
name|Schedule
name|schedule
init|=
name|CustomScheduler
operator|.
name|this
operator|.
name|getNextSchedule
argument_list|()
decl_stmt|;
name|currentFuture
operator|=
name|executor
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|schedule
operator|.
name|delay
argument_list|,
name|schedule
operator|.
name|unit
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// If an exception is thrown by the subclass then we need to make sure that the service
comment|// notices and transitions to the FAILED state.  We do it by calling notifyFailed directly
comment|// because the service does not monitor the state of the future so if the exception is not
comment|// caught and forwarded to the service the task would stop executing but the service would
comment|// have no idea.
name|service
operator|.
name|notifyFailed
argument_list|(
name|e
argument_list|)
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
comment|// N.B. Only protect cancel and isCancelled because those are the only methods that are
comment|// invoked by the AbstractScheduledService.
annotation|@
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
comment|// Ensure that a task cannot be rescheduled while a cancel is ongoing.
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
return|return
name|currentFuture
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
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
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Future
argument_list|<
name|Void
argument_list|>
name|delegate
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Only cancel is supported by this future"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|schedule (AbstractService service, ScheduledExecutorService executor, Runnable runnable)
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|AbstractService
name|service
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|,
name|Runnable
name|runnable
parameter_list|)
block|{
name|ReschedulableCallable
name|task
init|=
operator|new
name|ReschedulableCallable
argument_list|(
name|service
argument_list|,
name|executor
argument_list|,
name|runnable
argument_list|)
decl_stmt|;
name|task
operator|.
name|reschedule
argument_list|()
expr_stmt|;
return|return
name|task
return|;
block|}
comment|/**      * A value object that represents an absolute delay until a task should be invoked.      *      * @author Luke Sandberg      * @since 11.0      */
annotation|@
name|Beta
DECL|class|Schedule
specifier|protected
specifier|static
specifier|final
class|class
name|Schedule
block|{
DECL|field|delay
specifier|private
specifier|final
name|long
name|delay
decl_stmt|;
DECL|field|unit
specifier|private
specifier|final
name|TimeUnit
name|unit
decl_stmt|;
comment|/**        * @param delay the time from now to delay execution        * @param unit the time unit of the delay parameter        */
DECL|method|Schedule (long delay, TimeUnit unit)
specifier|public
name|Schedule
parameter_list|(
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
name|this
operator|.
name|unit
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|unit
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Calculates the time at which to next invoke the task.      *      *<p>This is guaranteed to be called immediately after the task has completed an iteration and      * on the same thread as the previous execution of {@link      * AbstractScheduledService#runOneIteration}.      *      * @return a schedule that defines the delay before the next execution.      */
DECL|method|getNextSchedule ()
specifier|protected
specifier|abstract
name|Schedule
name|getNextSchedule
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
block|}
end_class

end_unit

