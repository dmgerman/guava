begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * A TimeLimiter implementation which actually does not attempt to limit time  * at all.  This may be desirable to use in some unit tests.  More importantly,  * attempting to debug a call which is time-limited would be extremely annoying,  * so this gives you a time-limiter you can easily swap in for your real  * time-limiter while you're debugging.  *  * @author Kevin Bourrillion  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|FakeTimeLimiter
specifier|public
specifier|final
class|class
name|FakeTimeLimiter
implements|implements
name|TimeLimiter
block|{
annotation|@
name|Override
DECL|method|newProxy (T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|T
name|target
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|)
block|{
return|return
name|target
return|;
comment|// ha ha
block|}
annotation|@
name|Override
DECL|method|callWithTimeout (Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|callWithTimeout
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|,
name|boolean
name|amInterruptible
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
comment|// fooled you
block|}
block|}
end_class

end_unit

