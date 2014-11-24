begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@link ListenableFuture} whose result may be set by a {@link #set(Object)},  * {@link #setException(Throwable)} or {@link #setFuture(ListenableFuture)} call.   * It may also be cancelled.  *  * @author Sven Mawson  * @since 9.0 (in 1.0 as {@code ValueFuture})  */
end_comment

begin_class
DECL|class|SettableFuture
specifier|public
specifier|final
class|class
name|SettableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Creates a new {@code SettableFuture} in the default state.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|SettableFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|SettableFuture
argument_list|<
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Explicit private constructor, use the {@link #create} factory method to    * create instances of {@code SettableFuture}.    */
DECL|method|SettableFuture ()
specifier|private
name|SettableFuture
parameter_list|()
block|{}
DECL|method|set (@ullable V value)
annotation|@
name|Override
specifier|public
name|boolean
name|set
parameter_list|(
annotation|@
name|Nullable
name|V
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|set
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|setException (Throwable throwable)
annotation|@
name|Override
specifier|public
name|boolean
name|setException
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|super
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
return|;
block|}
annotation|@
name|Beta
annotation|@
name|Override
DECL|method|setFuture (ListenableFuture<? extends V> future)
specifier|public
name|boolean
name|setFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|future
parameter_list|)
block|{
return|return
name|super
operator|.
name|setFuture
argument_list|(
name|future
argument_list|)
return|;
block|}
block|}
end_class

end_unit

