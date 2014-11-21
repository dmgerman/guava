begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
comment|/**  * A settable future that can be set asynchronously via {@link #setFuture}.  * A similar effect could be accomplished by adding a listener to the delegate  * future that sets a normal settable future after the delegate is complete.  * This approach gains us the ability to keep track of whether a delegate has  * been set (i.e. so that we can prevent collisions from setting it twice and  * can know before the computation is done whether it has been set), as well  * as improved cancellation semantics (i.e. if either future is cancelled,  * then the other one is too).  This class is thread-safe.  *  * @param<V> The result type returned by the Future's {@code get} method.  *  * @author Stephen Hicks  */
end_comment

begin_class
DECL|class|AsyncSettableFuture
specifier|final
class|class
name|AsyncSettableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
argument_list|<
name|V
argument_list|>
block|{
comment|// TODO(user): add setFuture to SettableFuture and delete this class.
comment|/** Creates a new asynchronously-settable future. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|AsyncSettableFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|AsyncSettableFuture
argument_list|<
name|V
argument_list|>
argument_list|()
return|;
block|}
DECL|method|AsyncSettableFuture ()
specifier|private
name|AsyncSettableFuture
parameter_list|()
block|{}
DECL|method|setFuture (ListenableFuture<? extends V> future)
annotation|@
name|Override
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
name|checkNotNull
argument_list|(
name|future
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Convenience method that calls {@link #setFuture} on a {@link    * Futures#immediateFuture}.  Returns {@code true} if the future    * was able to be set (i.e. it hasn't been set already).    */
DECL|method|setValue (@ullable V value)
specifier|public
name|boolean
name|setValue
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
comment|/**    * Convenience method that calls {@link #setFuture} on a {@link    * Futures#immediateFailedFuture}.  Returns {@code true} if the    * future was able to be set (i.e. it hasn't been set already).    */
DECL|method|setException (Throwable exception)
annotation|@
name|Override
specifier|public
name|boolean
name|setException
parameter_list|(
name|Throwable
name|exception
parameter_list|)
block|{
return|return
name|super
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
return|;
block|}
block|}
end_class

end_unit

