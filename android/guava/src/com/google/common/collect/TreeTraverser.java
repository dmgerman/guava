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
name|Function
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
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * Views elements of a type {@code T} as nodes in a tree, and provides methods to traverse the trees  * induced by this traverser.  *  *<p>For example, the tree  *  *<pre>{@code  *        h  *      / | \  *     /  e  \  *    d       g  *   /|\      |  *  / | \     f  * a  b  c  * }</pre>  *  *<p>can be iterated over in preorder (hdabcegf), postorder (abcdefgh), or breadth-first order  * (hdegabcf).  *  *<p>Null nodes are strictly forbidden.  *  *<p><b>For Java 8 users:</b> Because this is an abstract class, not an interface, you can't use a  * lambda expression to extend it:  *  *<pre>{@code  * // won't work  * TreeTraverser<NodeType> traverser = node -> node.getChildNodes();  * }</pre>  *  * Instead, you can pass a lambda expression to the {@code using} factory method:  *  *<pre>{@code  * TreeTraverser<NodeType> traverser = TreeTraverser.using(node -> node.getChildNodes());  * }</pre>  *  * @author Louis Wasserman  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|TreeTraverser
specifier|public
specifier|abstract
class|class
name|TreeTraverser
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns a tree traverser that uses the given function to navigate from a node to its children.    * This is useful if the function instance already exists, or so that you can supply a lambda    * expressions. If those circumstances don't apply, you probably don't need to use this; subclass    * {@code TreeTraverser} and implement its {@link #children} method directly.    *    * @since 20.0    */
DECL|method|using ( final Function<T, ? extends Iterable<T>> nodeToChildrenFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TreeTraverser
argument_list|<
name|T
argument_list|>
name|using
parameter_list|(
specifier|final
name|Function
argument_list|<
name|T
argument_list|,
name|?
extends|extends
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|nodeToChildrenFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nodeToChildrenFunction
argument_list|)
expr_stmt|;
return|return
operator|new
name|TreeTraverser
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterable
argument_list|<
name|T
argument_list|>
name|children
parameter_list|(
name|T
name|root
parameter_list|)
block|{
return|return
name|nodeToChildrenFunction
operator|.
name|apply
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns the children of the specified node.  Must not contain null.    */
DECL|method|children (T root)
specifier|public
specifier|abstract
name|Iterable
argument_list|<
name|T
argument_list|>
name|children
parameter_list|(
name|T
name|root
parameter_list|)
function_decl|;
comment|/**    * Returns an unmodifiable iterable over the nodes in a tree structure, using pre-order    * traversal. That is, each node's subtrees are traversed after the node itself is returned.    *    *<p>No guarantees are made about the behavior of the traversal when nodes change while    * iteration is in progress or when the iterators generated by {@link #children} are advanced.    */
DECL|method|preOrderTraversal (final T root)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|preOrderTraversal
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
name|preOrderIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|// overridden in BinaryTreeTraverser
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
block|{
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|Iterator
argument_list|<
name|T
argument_list|>
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
argument_list|<>
argument_list|()
expr_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|Iterators
operator|.
name|singletonIterator
argument_list|(
name|checkNotNull
argument_list|(
name|root
argument_list|)
argument_list|)
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
name|Iterator
argument_list|<
name|T
argument_list|>
name|itr
init|=
name|stack
operator|.
name|getLast
argument_list|()
decl_stmt|;
comment|// throws NSEE if empty
name|T
name|result
init|=
name|checkNotNull
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|stack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|T
argument_list|>
name|childItr
init|=
name|children
argument_list|(
name|result
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|childItr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|stack
operator|.
name|addLast
argument_list|(
name|childItr
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
comment|/**    * Returns an unmodifiable iterable over the nodes in a tree structure, using post-order    * traversal. That is, each node's subtrees are traversed before the node itself is returned.    *    *<p>No guarantees are made about the behavior of the traversal when nodes change while    * iteration is in progress or when the iterators generated by {@link #children} are advanced.    */
DECL|method|postOrderTraversal (final T root)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|postOrderTraversal
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
name|postOrderIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|// overridden in BinaryTreeTraverser
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
DECL|class|PostOrderNode
specifier|private
specifier|static
specifier|final
class|class
name|PostOrderNode
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|root
specifier|final
name|T
name|root
decl_stmt|;
DECL|field|childIterator
specifier|final
name|Iterator
argument_list|<
name|T
argument_list|>
name|childIterator
decl_stmt|;
DECL|method|PostOrderNode (T root, Iterator<T> childIterator)
name|PostOrderNode
parameter_list|(
name|T
name|root
parameter_list|,
name|Iterator
argument_list|<
name|T
argument_list|>
name|childIterator
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|checkNotNull
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|this
operator|.
name|childIterator
operator|=
name|checkNotNull
argument_list|(
name|childIterator
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PostOrderIterator
specifier|private
specifier|final
class|class
name|PostOrderIterator
extends|extends
name|AbstractIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|stack
specifier|private
specifier|final
name|ArrayDeque
argument_list|<
name|PostOrderNode
argument_list|<
name|T
argument_list|>
argument_list|>
name|stack
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
argument_list|<>
argument_list|()
expr_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|expand
argument_list|(
name|root
argument_list|)
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
name|PostOrderNode
argument_list|<
name|T
argument_list|>
name|top
init|=
name|stack
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|top
operator|.
name|childIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|child
init|=
name|top
operator|.
name|childIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|stack
operator|.
name|addLast
argument_list|(
name|expand
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
return|return
name|top
operator|.
name|root
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
DECL|method|expand (T t)
specifier|private
name|PostOrderNode
argument_list|<
name|T
argument_list|>
name|expand
parameter_list|(
name|T
name|t
parameter_list|)
block|{
return|return
operator|new
name|PostOrderNode
argument_list|<
name|T
argument_list|>
argument_list|(
name|t
argument_list|,
name|children
argument_list|(
name|t
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an unmodifiable iterable over the nodes in a tree structure, using breadth-first    * traversal. That is, all the nodes of depth 0 are returned, then depth 1, then 2, and so on.    *    *<p>No guarantees are made about the behavior of the traversal when nodes change while    * iteration is in progress or when the iterators generated by {@link #children} are advanced.    */
DECL|method|breadthFirstTraversal (final T root)
specifier|public
specifier|final
name|FluentIterable
argument_list|<
name|T
argument_list|>
name|breadthFirstTraversal
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
name|BreadthFirstIterator
argument_list|(
name|root
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|class|BreadthFirstIterator
specifier|private
specifier|final
class|class
name|BreadthFirstIterator
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
DECL|field|queue
specifier|private
specifier|final
name|Queue
argument_list|<
name|T
argument_list|>
name|queue
decl_stmt|;
DECL|method|BreadthFirstIterator (T root)
name|BreadthFirstIterator
parameter_list|(
name|T
name|root
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
operator|new
name|ArrayDeque
argument_list|<
name|T
argument_list|>
argument_list|()
expr_stmt|;
name|queue
operator|.
name|add
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
name|queue
operator|.
name|isEmpty
argument_list|()
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
name|queue
operator|.
name|element
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
name|queue
operator|.
name|remove
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|queue
argument_list|,
name|children
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

