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
comment|/**  * A {@link ListenableFuture} whose result may be set by a {@link #set(Object)}  * or {@link #setException(Throwable)} call.  *  * @author Sven Mawson  * @since 9 (in version 1 as {@code ValueFuture})  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|SettableFuture
specifier|public
specifier|final
class|class
name|SettableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractListenableFuture
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
comment|/**    * Sets the value of this future.  This method will return {@code true} if    * the value was successfully set, or {@code false} if the future has already    * been set or cancelled.    *    * @param newValue the value the future should hold.    * @return true if the value was successfully set.    */
annotation|@
name|Override
DECL|method|set (@ullable V newValue)
specifier|public
name|boolean
name|set
parameter_list|(
annotation|@
name|Nullable
name|V
name|newValue
parameter_list|)
block|{
return|return
name|super
operator|.
name|set
argument_list|(
name|newValue
argument_list|)
return|;
block|}
comment|/**    * Sets the future to having failed with the given exception.  This exception    * will be wrapped in an ExecutionException and thrown from the get methods.    * This method will return {@code true} if the exception was successfully set,    * or {@code false} if the future has already been set or cancelled.    *    * @param t the exception the future should hold.    * @return true if the exception was successfully set.    */
annotation|@
name|Override
DECL|method|setException (Throwable t)
specifier|public
name|boolean
name|setException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
name|super
operator|.
name|setException
argument_list|(
name|t
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>A SettableFuture is never considered in the running state, so the    * {@code mayInterruptIfRunning} argument is ignored.    */
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
return|return
name|super
operator|.
name|cancel
argument_list|()
return|;
block|}
block|}
end_class

end_unit

