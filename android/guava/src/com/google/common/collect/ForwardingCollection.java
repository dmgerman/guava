begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Objects
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
comment|/**  * A collection which forwards all its method calls to another collection. Subclasses should  * override one or more methods to modify the behavior of the backing collection as desired per the  *<a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingCollection} forward<b>indiscriminately</b> to  * the methods of the delegate. For example, overriding {@link #add} alone<b>will not</b> change  * the behavior of {@link #addAll}, which can lead to unexpected behavior. In this case, you should  * override {@code addAll} as well, either providing your own implementation, or delegating to the  * provided {@code standardAddAll} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingCollection}.  *  *<p>The {@code standard} methods are not guaranteed to be thread-safe, even when all of the  * methods that they depend on are thread-safe.  *  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 2.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ForwardingCollection
specifier|public
specifier|abstract
name|class
name|ForwardingCollection
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingObject
expr|implements
name|Collection
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO(lowasser): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingCollection ()
specifier|protected
name|ForwardingCollection
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Collection
argument_list|<
name|E
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
expr|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|removeAll (Collection<?> collection)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|contains (@heckForNull Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|add (@arametricNullness E element)
specifier|public
name|boolean
name|add
parameter_list|(
annotation|@
name|ParametricNullness
name|E
name|element
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|remove (@heckForNull Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|containsAll (Collection<?> collection)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|retainAll (Collection<?> collection)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|retainAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
annotation|@
name|Nullable
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
end_function

begin_annotation
annotation|@
name|CanIgnoreReturnValue
end_annotation

begin_annotation
annotation|@
name|Override
end_annotation

begin_annotation
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
end_annotation

begin_comment
comment|// b/192354773 in our checker affects toArray declarations
end_comment

begin_expr_stmt
DECL|method|toArray (T[] array)
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|toArray
argument_list|(
name|T
index|[]
name|array
argument_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
end_expr_stmt

begin_comment
comment|/**    * A sensible definition of {@link #contains} in terms of {@link #iterator}. If you override    * {@link #iterator}, you may wish to override {@link #contains} to forward to this    * implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardContains (@heckForNull Object object)
specifier|protected
name|boolean
name|standardContains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|contains
argument_list|(
name|iterator
argument_list|()
argument_list|,
name|object
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #containsAll} in terms of {@link #contains} . If you override    * {@link #contains}, you may wish to override {@link #containsAll} to forward to this    * implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardContainsAll (Collection<?> collection)
specifier|protected
name|boolean
name|standardContainsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|Collections2
operator|.
name|containsAllImpl
argument_list|(
name|this
argument_list|,
name|collection
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #addAll} in terms of {@link #add}. If you override {@link    * #add}, you may wish to override {@link #addAll} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardAddAll (Collection<? extends E> collection)
specifier|protected
name|boolean
name|standardAddAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|addAll
argument_list|(
name|this
argument_list|,
name|collection
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #remove} in terms of {@link #iterator}, using the iterator's    * {@code remove} method. If you override {@link #iterator}, you may wish to override {@link    * #remove} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardRemove (@heckForNull Object object)
specifier|protected
name|boolean
name|standardRemove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|object
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #removeAll} in terms of {@link #iterator}, using the iterator's    * {@code remove} method. If you override {@link #iterator}, you may wish to override {@link    * #removeAll} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardRemoveAll (Collection<?> collection)
specifier|protected
name|boolean
name|standardRemoveAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|removeAll
argument_list|(
name|iterator
argument_list|()
argument_list|,
name|collection
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #retainAll} in terms of {@link #iterator}, using the iterator's    * {@code remove} method. If you override {@link #iterator}, you may wish to override {@link    * #retainAll} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardRetainAll (Collection<?> collection)
specifier|protected
name|boolean
name|standardRetainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|Iterators
operator|.
name|retainAll
argument_list|(
name|iterator
argument_list|()
argument_list|,
name|collection
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #clear} in terms of {@link #iterator}, using the iterator's    * {@code remove} method. If you override {@link #iterator}, you may wish to override {@link    * #clear} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardClear ()
specifier|protected
name|void
name|standardClear
parameter_list|()
block|{
name|Iterators
operator|.
name|clear
argument_list|(
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #isEmpty} as {@code !iterator().hasNext}. If you override    * {@link #isEmpty}, you may wish to override {@link #isEmpty} to forward to this implementation.    * Alternately, it may be more efficient to implement {@code isEmpty} as {@code size() == 0}.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardIsEmpty ()
specifier|protected
name|boolean
name|standardIsEmpty
parameter_list|()
block|{
return|return
operator|!
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #toString} in terms of {@link #iterator}. If you override    * {@link #iterator}, you may wish to override {@link #toString} to forward to this    * implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardToString ()
specifier|protected
name|String
name|standardToString
parameter_list|()
block|{
return|return
name|Collections2
operator|.
name|toStringImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #toArray()} in terms of {@link #toArray(Object[])}. If you    * override {@link #toArray(Object[])}, you may wish to override {@link #toArray} to forward to    * this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardToArray ()
specifier|protected
annotation|@
name|Nullable
name|Object
index|[]
name|standardToArray
parameter_list|()
block|{
annotation|@
name|Nullable
name|Object
index|[]
name|newArray
init|=
operator|new
expr|@
name|Nullable
name|Object
index|[
name|size
argument_list|()
index|]
decl_stmt|;
return|return
name|toArray
argument_list|(
name|newArray
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #toArray(Object[])} in terms of {@link #size} and {@link    * #iterator}. If you override either of these methods, you may wish to override {@link #toArray}    * to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_expr_stmt
DECL|method|standardToArray (T[] array)
specifier|protected
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
index|[]
name|standardToArray
argument_list|(
name|T
index|[]
name|array
argument_list|)
block|{
return|return
name|ObjectArrays
operator|.
name|toArrayImpl
argument_list|(
name|this
argument_list|,
name|array
argument_list|)
return|;
block|}
end_expr_stmt

unit|}
end_unit

