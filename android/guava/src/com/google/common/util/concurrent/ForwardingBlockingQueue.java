begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|common
operator|.
name|collect
operator|.
name|ForwardingQueue
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
name|Collection
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
name|BlockingQueue
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
comment|/**  * A {@link BlockingQueue} which forwards all its method calls to another {@link BlockingQueue}.  * Subclasses should override one or more methods to modify the behavior of the backing collection  * as desired per the<a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator  * pattern</a>.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingBlockingQueue}.  *  * @author Raimundo Mirisola  *  * @param<E> the type of elements held in this collection  * @since 4.0  */
end_comment

begin_class
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider being more strict.
annotation|@
name|GwtIncompatible
DECL|class|ForwardingBlockingQueue
specifier|public
specifier|abstract
class|class
name|ForwardingBlockingQueue
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingQueue
argument_list|<
name|E
argument_list|>
implements|implements
name|BlockingQueue
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingBlockingQueue ()
specifier|protected
name|ForwardingBlockingQueue
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|drainTo (Collection<? super E> c, int maxElements)
specifier|public
name|int
name|drainTo
parameter_list|(
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|c
parameter_list|,
name|int
name|maxElements
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|drainTo
argument_list|(
name|c
argument_list|,
name|maxElements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|drainTo (Collection<? super E> c)
specifier|public
name|int
name|drainTo
parameter_list|(
name|Collection
argument_list|<
name|?
super|super
name|E
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|drainTo
argument_list|(
name|c
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|offer (E e, long timeout, TimeUnit unit)
specifier|public
name|boolean
name|offer
parameter_list|(
name|E
name|e
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|offer
argument_list|(
name|e
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|poll (long timeout, TimeUnit unit)
specifier|public
name|E
name|poll
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|poll
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (E e)
specifier|public
name|void
name|put
parameter_list|(
name|E
name|e
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|delegate
argument_list|()
operator|.
name|put
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remainingCapacity ()
specifier|public
name|int
name|remainingCapacity
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remainingCapacity
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|take ()
specifier|public
name|E
name|take
parameter_list|()
throws|throws
name|InterruptedException
block|{
return|return
name|delegate
argument_list|()
operator|.
name|take
argument_list|()
return|;
block|}
block|}
end_class

end_unit
