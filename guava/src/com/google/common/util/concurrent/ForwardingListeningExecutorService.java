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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * A listening executor service which forwards all its method calls to another  * listening executor service. Subclasses should override one or more methods to  * modify the behavior of the backing executor service as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @author Isaac Shum  * @since 10.0  */
end_comment

begin_class
DECL|class|ForwardingListeningExecutorService
specifier|public
specifier|abstract
class|class
name|ForwardingListeningExecutorService
extends|extends
name|ForwardingExecutorService
implements|implements
name|ListeningExecutorService
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingListeningExecutorService ()
specifier|protected
name|ForwardingListeningExecutorService
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|ListeningExecutorService
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task)
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, T result)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|,
name|T
name|result
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|,
name|result
argument_list|)
return|;
block|}
block|}
end_class

end_unit

