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
name|GwtCompatible
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
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
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

begin_comment
comment|/**  * A variant of {@link TreeTraverser} for binary trees, providing additional traversals specific to  * binary trees.  *  * @author Louis Wasserman  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BinaryTreeTraverser
specifier|public
specifier|abstract
class|class
name|BinaryTreeTraverser
parameter_list|<
name|T
parameter_list|>
extends|extends
name|TreeTraverser
argument_list|<
name|T
argument_list|>
block|{
comment|// TODO(lowasser): make this GWT-compatible when we've checked in ArrayDeque and BitSet emulation
comment|/**    * Returns the left child of the specified node, or {@link Optional#absent()} if the specified    * node has no left child.    */
DECL|method|leftChild (T root)
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|T
argument_list|>
name|leftChild
parameter_list|(
name|T
name|root
parameter_list|)
function_decl|;
comment|/**    * Returns the right child of the specified node, or {@link Optional#absent()} if the specified    * node has no right child.    */
DECL|method|rightChild (T root)
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|T
argument_list|>
name|rightChild
parameter_list|(
name|T
name|root
parameter_list|)
function_decl|;
comment|/**    * Returns the children of this node, in left-to-right order.    */
annotation|@
name|Override
DECL|method|children (final T root)
specifier|public
specifier|final
name|Iterable
argument_list|<
name|T
argument_list|>
name|children
parameter_list|(
specifier|final
name|T
name|root
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|root
argument_list|)
expr_stmt|;
return|return
operator|new
name|FluentIterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractIterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
name|boolean
name|doneLeft
decl_stmt|;
name|boolean
name|doneRight
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|T
name|computeNext
parameter_list|()
block|{
if|if
condition|(
operator|!
name|doneLeft
condition|)
block|{
name|doneLeft
operator|=
literal|true
expr_stmt|;
name|Optional
argument_list|<
name|T
argument_list|>
name|left
init|=
name|leftChild
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|left
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|left
operator|.
name|get
argument_list|()
return|;
block|}
block|}
if|if
condition|(
operator|!
name|doneRight
condition|)
block|{
name|doneRight
operator|=
literal|true
expr_stmt|;
name|Optional
argument_list|<
name|T
argument_list|>
name|right
init|=
name|rightChild
argument_list|(
name|root
argument_list|)
decl_stmt|;
if|if
condition|(
name|right
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
name|right
operator|.
name|get
argument_list|()
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|preOrderIterator (T root)
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
name|preOrderIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
return|return
operator|new
name|PreOrderIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
comment|/*    * Optimized implementation of preOrderIterator for binary trees.    */
DECL|class|PreOrderIterator
specifier|private
specifier|final
class|class
name|PreOrderIterator
extends|extends
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
implements|implements
name|PeekingIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|T
argument_list|>
name|stack
decl_stmt|;
DECL|method|PreOrderIterator (T root)
name|PreOrderIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
name|this
operator|.
name|stack
operator|=
operator|new
name|ArrayDeque
argument_list|<
name|T
argument_list|>
argument_list|()
expr_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
name|T
name|result
init|=
name|stack
operator|.
name|removeLast
argument_list|()
decl_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|rightChild
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|leftChild
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|peek ()
specifier|public
name|T
name|peek
parameter_list|()
block|{
return|return
name|stack
operator|.
name|getLast
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|postOrderIterator (T root)
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
name|postOrderIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
return|return
operator|new
name|PostOrderIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
comment|/*    * Optimized implementation of postOrderIterator for binary trees.    */
DECL|class|PostOrderIterator
specifier|private
specifier|final
class|class
name|PostOrderIterator
extends|extends
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|T
argument_list|>
name|stack
decl_stmt|;
DECL|field|hasExpanded
specifier|private
specifier|final
name|BitSet
name|hasExpanded
decl_stmt|;
DECL|method|PostOrderIterator (T root)
name|PostOrderIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
name|this
operator|.
name|stack
operator|=
operator|new
name|ArrayDeque
argument_list|<
name|T
argument_list|>
argument_list|()
expr_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|this
operator|.
name|hasExpanded
operator|=
operator|new
name|BitSet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|T
name|next
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|T
name|node
init|=
name|stack
operator|.
name|getLast
argument_list|()
decl_stmt|;
name|boolean
name|expandedNode
init|=
name|hasExpanded
operator|.
name|get
argument_list|(
name|stack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|expandedNode
condition|)
block|{
name|stack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
name|hasExpanded
operator|.
name|clear
argument_list|(
name|stack
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
else|else
block|{
name|hasExpanded
operator|.
name|set
argument_list|(
name|stack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|rightChild
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|leftChild
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// TODO(lowasser): see if any significant optimizations are possible for breadthFirstIterator
DECL|method|inOrderTraversal (final T root)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|inOrderTraversal
parameter_list|(
specifier|final
name|T
name|root
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|root
argument_list|)
expr_stmt|;
return|return
operator|new
name|FluentIterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|InOrderIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|InOrderIterator
specifier|private
specifier|final
class|class
name|InOrderIterator
extends|extends
name|AbstractIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|T
argument_list|>
name|stack
decl_stmt|;
DECL|field|hasExpandedLeft
specifier|private
specifier|final
name|BitSet
name|hasExpandedLeft
decl_stmt|;
DECL|method|InOrderIterator (T root)
name|InOrderIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
name|this
operator|.
name|stack
operator|=
operator|new
name|ArrayDeque
argument_list|<
name|T
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|hasExpandedLeft
operator|=
operator|new
name|BitSet
argument_list|()
expr_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|computeNext ()
specifier|protected
name|T
name|computeNext
parameter_list|()
block|{
while|while
condition|(
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|T
name|node
init|=
name|stack
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|hasExpandedLeft
operator|.
name|get
argument_list|(
name|stack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|stack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
name|hasExpandedLeft
operator|.
name|clear
argument_list|(
name|stack
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|rightChild
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
else|else
block|{
name|hasExpandedLeft
operator|.
name|set
argument_list|(
name|stack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|pushIfPresent
argument_list|(
name|stack
argument_list|,
name|leftChild
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
DECL|method|pushIfPresent (Deque<T> stack, Optional<T> node)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|pushIfPresent
parameter_list|(
name|Deque
argument_list|<
name|T
argument_list|>
name|stack
parameter_list|,
name|Optional
argument_list|<
name|T
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|stack
operator|.
name|addLast
argument_list|(
name|node
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

