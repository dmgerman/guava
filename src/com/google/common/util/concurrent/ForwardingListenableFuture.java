begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Executor
import|;
end_import

begin_comment
comment|/**  * A {@link ForwardingFuture} that also implements {@link ListenableFuture}.  * Subclasses will have to provide a delegate {@link ListenableFuture} through  * the {@link #delegate()} method.  *  * @param<V> The result type returned by this Future's<tt>get</tt> method  *  * @author Shardul Deo  * @since 4  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ForwardingListenableFuture
specifier|public
specifier|abstract
class|class
name|ForwardingListenableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ForwardingFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingListenableFuture ()
specifier|protected
name|ForwardingListenableFuture
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor exec)
specifier|public
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|exec
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|exec
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

