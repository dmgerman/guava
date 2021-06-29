begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|Deque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A deque which forwards all its method calls to another deque. Subclasses should override one or  * more methods to modify the behavior of the backing deque as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingDeque} forward<b>indiscriminately</b> to the  * methods of the delegate. For example, overriding {@link #add} alone<b>will not</b> change the  * behavior of {@link #offer} which can lead to unexpected behavior. In this case, you should  * override {@code offer} as well.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingDeque}.  *  * @author Kurt Alfred Kluever  * @since 12.0  */
end_comment

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ForwardingDeque
specifier|public
specifier|abstract
name|class
name|ForwardingDeque
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingQueue
argument_list|<
name|E
argument_list|>
expr|implements
name|Deque
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingDeque ()
specifier|protected
name|ForwardingDeque
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Deque
argument_list|<
name|E
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|addFirst (@arametricNullness E e)
specifier|public
name|void
name|addFirst
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
name|delegate
argument_list|()
operator|.
name|addFirst
argument_list|(
name|e
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|addLast (@arametricNullness E e)
specifier|public
name|void
name|addLast
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|e
argument_list|)
block|{
name|delegate
argument_list|()
operator|.
name|addLast
argument_list|(
name|e
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|descendingIterator
argument_list|()
return|;
block|}
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|getFirst ()
specifier|public
name|E
name|getFirst
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getFirst
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|getLast ()
specifier|public
name|E
name|getLast
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|getLast
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
annotation|@
name|Override
DECL|method|offerFirst (@arametricNullness E e)
specifier|public
name|boolean
name|offerFirst
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|offerFirst
argument_list|(
name|e
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
annotation|@
name|Override
DECL|method|offerLast (@arametricNullness E e)
specifier|public
name|boolean
name|offerLast
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|offerLast
argument_list|(
name|e
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|peekFirst ()
specifier|public
name|E
name|peekFirst
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|peekFirst
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|peekLast ()
specifier|public
name|E
name|peekLast
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|peekLast
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|pollFirst ()
specifier|public
name|E
name|pollFirst
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|pollFirst
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|pollLast ()
specifier|public
name|E
name|pollLast
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|pollLast
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|pop ()
specifier|public
name|E
name|pop
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|pop
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|push (@arametricNullness E e)
specifier|public
name|void
name|push
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|e
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|push
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|removeFirst ()
specifier|public
name|E
name|removeFirst
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeFirst
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|removeLast ()
specifier|public
name|E
name|removeLast
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeLast
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeFirstOccurrence (@heckForNull Object o)
specifier|public
name|boolean
name|removeFirstOccurrence
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|o
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeFirstOccurrence
argument_list|(
name|o
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeLastOccurrence (@heckForNull Object o)
specifier|public
name|boolean
name|removeLastOccurrence
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|o
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeLastOccurrence
argument_list|(
name|o
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

