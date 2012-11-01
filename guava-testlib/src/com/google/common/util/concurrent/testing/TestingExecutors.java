begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent.testing
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
operator|.
name|testing
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
name|collect
operator|.
name|ImmutableList
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
name|primitives
operator|.
name|Longs
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
name|util
operator|.
name|concurrent
operator|.
name|AbstractFuture
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
name|util
operator|.
name|concurrent
operator|.
name|AbstractListeningExecutorService
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningScheduledExecutorService
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
name|Delayed
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
name|ScheduledFuture
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

begin_comment
comment|/**  * Factory methods for {@link ExecutorService} for testing.  *  * @author Chris Nokleberg  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|TestingExecutors
specifier|public
specifier|final
class|class
name|TestingExecutors
block|{
DECL|method|TestingExecutors ()
specifier|private
name|TestingExecutors
parameter_list|()
block|{}
comment|/**    * Returns a {@link ScheduledExecutorService} that never executes anything.    *    *<p>The {@code shutdownNow} method of the returned executor always returns an empty list despite    * the fact that everything is still technically awaiting execution.    * The {@code getDelay} method of any {@link ScheduledFuture} returned by the executor will always    * return the max long value instead of the time until the user-specified delay.    */
DECL|method|noOpScheduledExecutor ()
specifier|public
specifier|static
name|ListeningScheduledExecutorService
name|noOpScheduledExecutor
parameter_list|()
block|{
return|return
operator|new
name|NoOpScheduledExecutorService
argument_list|()
return|;
block|}
DECL|class|NoOpScheduledExecutorService
specifier|private
specifier|static
specifier|final
class|class
name|NoOpScheduledExecutorService
extends|extends
name|AbstractListeningExecutorService
implements|implements
name|ListeningScheduledExecutorService
block|{
DECL|field|shutdown
specifier|private
specifier|volatile
name|boolean
name|shutdown
decl_stmt|;
DECL|method|shutdown ()
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|shutdown
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|shutdownNow ()
annotation|@
name|Override
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
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|isShutdown ()
annotation|@
name|Override
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|shutdown
return|;
block|}
DECL|method|isTerminated ()
annotation|@
name|Override
specifier|public
name|boolean
name|isTerminated
parameter_list|()
block|{
return|return
name|shutdown
return|;
block|}
DECL|method|awaitTermination (long timeout, TimeUnit unit)
annotation|@
name|Override
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
block|{
return|return
literal|true
return|;
block|}
DECL|method|execute (Runnable runnable)
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{}
DECL|method|schedule ( Callable<V> callable, long delay, TimeUnit unit)
annotation|@
name|Override
specifier|public
parameter_list|<
name|V
parameter_list|>
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|NeverScheduledFuture
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
annotation|@
name|Override
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|NeverScheduledFuture
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|scheduleAtFixedRate ( Runnable command, long initialDelay, long period, TimeUnit unit)
annotation|@
name|Override
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleAtFixedRate
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|period
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|NeverScheduledFuture
operator|.
name|create
argument_list|()
return|;
block|}
DECL|method|scheduleWithFixedDelay ( Runnable command, long initialDelay, long delay, TimeUnit unit)
annotation|@
name|Override
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleWithFixedDelay
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|NeverScheduledFuture
operator|.
name|create
argument_list|()
return|;
block|}
DECL|class|NeverScheduledFuture
specifier|private
specifier|static
class|class
name|NeverScheduledFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|method|create ()
specifier|static
parameter_list|<
name|V
parameter_list|>
name|NeverScheduledFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|NeverScheduledFuture
argument_list|<
name|V
argument_list|>
argument_list|()
return|;
block|}
DECL|method|getDelay (TimeUnit unit)
annotation|@
name|Override
specifier|public
name|long
name|getDelay
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|Long
operator|.
name|MAX_VALUE
return|;
block|}
DECL|method|compareTo (Delayed other)
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|Delayed
name|other
parameter_list|)
block|{
return|return
name|Longs
operator|.
name|compare
argument_list|(
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
argument_list|,
name|other
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

