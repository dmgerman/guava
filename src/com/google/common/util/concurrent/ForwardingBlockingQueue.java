begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2010 Google Inc. All Rights Reserved.
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
name|collect
operator|.
name|ForwardingQueue
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
comment|/**  * A {@link BlockingQueue} which forwards all its method calls to another  * {@link BlockingQueue}. Subclasses should override one or more methods to  * modify the behavior of the backing collection as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @author Raimundo Mirisola  *  * @param<E> the type of elements held in this collection  * @since 4  */
end_comment

begin_class
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
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|BlockingQueue
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
function_decl|;
DECL|method|drainTo ( Collection<? super E> c, int maxElements)
comment|/* @Override */
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
DECL|method|drainTo (Collection<? super E> c)
comment|/* @Override */
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
DECL|method|offer (E e, long timeout, TimeUnit unit)
comment|/* @Override */
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
DECL|method|poll (long timeout, TimeUnit unit)
comment|/* @Override */
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
DECL|method|put (E e)
comment|/* @Override */
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
DECL|method|remainingCapacity ()
comment|/* @Override */
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
DECL|method|take ()
comment|/* @Override */
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

