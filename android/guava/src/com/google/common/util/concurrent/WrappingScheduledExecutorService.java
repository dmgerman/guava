begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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

begin_comment
comment|/**  * An abstract {@code ScheduledExecutorService} that allows subclasses to {@linkplain  * #wrapTask(Callable) wrap} tasks before they are submitted to the underlying executor.  *  *<p>Note that task wrapping may occur even if the task is never executed.  *  * @author Luke Sandberg  */
end_comment

begin_class
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|WrappingScheduledExecutorService
specifier|abstract
class|class
name|WrappingScheduledExecutorService
extends|extends
name|WrappingExecutorService
implements|implements
name|ScheduledExecutorService
block|{
DECL|field|delegate
specifier|final
name|ScheduledExecutorService
name|delegate
decl_stmt|;
DECL|method|WrappingScheduledExecutorService (ScheduledExecutorService delegate)
specifier|protected
name|WrappingScheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
specifier|public
specifier|final
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
name|delegate
operator|.
name|schedule
argument_list|(
name|wrapTask
argument_list|(
name|command
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|schedule ( Callable<V> task, long delay, TimeUnit unit)
specifier|public
name|final
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
argument_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|task
argument_list|,
name|long
name|delay
argument_list|,
name|TimeUnit
name|unit
argument_list|)
block|{
return|return
name|delegate
operator|.
name|schedule
argument_list|(
name|wrapTask
argument_list|(
name|task
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleAtFixedRate ( Runnable command, long initialDelay, long period, TimeUnit unit)
specifier|public
specifier|final
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
name|delegate
operator|.
name|scheduleAtFixedRate
argument_list|(
name|wrapTask
argument_list|(
name|command
argument_list|)
argument_list|,
name|initialDelay
argument_list|,
name|period
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleWithFixedDelay ( Runnable command, long initialDelay, long delay, TimeUnit unit)
specifier|public
specifier|final
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
name|delegate
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|wrapTask
argument_list|(
name|command
argument_list|)
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
end_class

end_unit

