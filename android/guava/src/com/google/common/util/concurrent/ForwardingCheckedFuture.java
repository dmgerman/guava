begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/**  * A future which forwards all its method calls to another future. Subclasses should override one or  * more methods to modify the behavior of the backing future as desired per the<a href=  * "http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p>Most subclasses can simply extend {@link SimpleForwardingCheckedFuture}.  *  * @param<V> The result type returned by this Future's {@code get} method  * @param<X> The type of the Exception thrown by the Future's {@code checkedGet} method  * @author Anthony Zana  * @since 9.0  * @deprecated {@link CheckedFuture} cannot properly support the chained operations that are the  *     primary goal of {@link ListenableFuture}. {@code CheckedFuture} also encourages users to  *     rethrow exceptions from one thread in another thread, producing misleading stack traces.  *     Additionally, it has a surprising policy about which exceptions to map and which to leave  *     untouched. Guava users who want a {@code CheckedFuture} can fork the classes for their own  *     use, possibly specializing them to the particular exception type they use. We recommend that  *     most people use {@code ListenableFuture} and perform any exception wrapping themselves. This  *     class is scheduled for removal from Guava in February 2018.  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Deprecated
annotation|@
name|GwtIncompatible
DECL|class|ForwardingCheckedFuture
specifier|public
specifier|abstract
class|class
name|ForwardingCheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ForwardingListenableFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
block|{
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|checkedGet ()
specifier|public
name|V
name|checkedGet
parameter_list|()
throws|throws
name|X
block|{
return|return
name|delegate
argument_list|()
operator|.
name|checkedGet
argument_list|()
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|checkedGet (long timeout, TimeUnit unit)
specifier|public
name|V
name|checkedGet
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|TimeoutException
throws|,
name|X
block|{
return|return
name|delegate
argument_list|()
operator|.
name|checkedGet
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|delegate
parameter_list|()
function_decl|;
comment|// TODO(cpovirk): Use Standard Javadoc form for SimpleForwarding*
comment|/**    * A simplified version of {@link ForwardingCheckedFuture} where subclasses can pass in an already    * constructed {@link CheckedFuture} as the delegate.    *    * @since 9.0    * @deprecated {@link CheckedFuture} cannot properly support the chained operations that are the    *     primary goal of {@link ListenableFuture}. {@code CheckedFuture} also encourages users to    *     rethrow exceptions from one thread in another thread, producing misleading stack traces.    *     Additionally, it has a surprising policy about which exceptions to map and which to leave    *     untouched. Guava users who want a {@code CheckedFuture} can fork the classes for their own    *     use, possibly specializing them to the particular exception type they use. We recommend    *     that most people use {@code ListenableFuture} and perform any exception wrapping    *     themselves. This class is scheduled for removal from Guava in February 2018.    */
annotation|@
name|Beta
annotation|@
name|Deprecated
DECL|class|SimpleForwardingCheckedFuture
specifier|public
specifier|abstract
specifier|static
class|class
name|SimpleForwardingCheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ForwardingCheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SimpleForwardingCheckedFuture (CheckedFuture<V, X> delegate)
specifier|protected
name|SimpleForwardingCheckedFuture
parameter_list|(
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|final
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
block|}
end_class

end_unit
