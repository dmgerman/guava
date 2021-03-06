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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
comment|/**  * A map which forwards all its method calls to another map. Subclasses should override one or more  * methods to modify the behavior of the backing map as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>Warning:</b> The methods of {@code ForwardingMap} forward<i>indiscriminately</i> to the  * methods of the delegate. For example, overriding {@link #put} alone<i>will not</i> change the  * behavior of {@link #putAll}, which can lead to unexpected behavior. In this case, you should  * override {@code putAll} as well, either providing your own implementation, or delegating to the  * provided {@code standardPutAll} method.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingMap}.  *  *<p>Each of the {@code standard} methods, where appropriate, use {@link Objects#equal} to test  * equality for both keys and values. This may not be the desired behavior for map implementations  * that use non-standard notions of key equality, such as a {@code SortedMap} whose comparator is  * not consistent with {@code equals}.  *  *<p>The {@code standard} methods and the collection views they return are not guaranteed to be  * thread-safe, even when all of the methods that they depend on are thread-safe.  *  * @author Kevin Bourrillion  * @author Jared Levy  * @author Louis Wasserman  * @since 2.0  */
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
DECL|class|ForwardingMap
specifier|public
specifier|abstract
name|class
name|ForwardingMap
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingObject
expr|implements
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// TODO(lowasser): identify places where thread safety is actually lost
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingMap ()
specifier|protected
name|ForwardingMap
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
argument_list|()
block|;    @
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
expr|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
argument_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|remove (@heckForNull Object key)
specifier|public
name|V
name|remove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|key
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
DECL|method|containsKey (@heckForNull Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|containsValue (@heckForNull Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|get (@heckForNull Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|put (@arametricNullness K key, @ParametricNullness V value)
specifier|public
name|V
name|put
parameter_list|(
annotation|@
name|ParametricNullness
name|K
name|key
parameter_list|,
annotation|@
name|ParametricNullness
name|V
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|delegate
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|==
name|this
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #putAll(Map)} in terms of {@link #put(Object, Object)}. If you    * override {@link #put(Object, Object)}, you may wish to override {@link #putAll(Map)} to forward    * to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardPutAll (Map<? extends K, ? extends V> map)
specifier|protected
name|void
name|standardPutAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|Maps
operator|.
name|putAllImpl
argument_list|(
name|this
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**    * A sensible, albeit inefficient, definition of {@link #remove} in terms of the {@code iterator}    * method of {@link #entrySet}. If you override {@link #entrySet}, you may wish to override {@link    * #remove} to forward to this implementation.    *    *<p>Alternately, you may wish to override {@link #remove} with {@code keySet().remove}, assuming    * that approach would not lead to an infinite loop.    *    * @since 7.0    */
end_comment

begin_function
annotation|@
name|Beta
annotation|@
name|CheckForNull
DECL|method|standardRemove (@heckForNull Object key)
specifier|protected
name|V
name|standardRemove
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryIterator
init|=
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|key
argument_list|)
condition|)
block|{
name|V
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #clear} in terms of the {@code iterator} method of {@link    * #entrySet}. In many cases, you may wish to override {@link #clear} to forward to this    * implementation.    *    * @since 7.0    */
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
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|/**    * A sensible implementation of {@link Map#keySet} in terms of the following methods: {@link    * ForwardingMap#clear}, {@link ForwardingMap#containsKey}, {@link ForwardingMap#isEmpty}, {@link    * ForwardingMap#remove}, {@link ForwardingMap#size}, and the {@link Set#iterator} method of    * {@link ForwardingMap#entrySet}. In many cases, you may wish to override {@link    * ForwardingMap#keySet} to forward to this implementation or a subclass thereof.    *    * @since 10.0    */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|StandardKeySet
specifier|protected
class|class
name|StandardKeySet
extends|extends
name|Maps
operator|.
name|KeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardKeySet ()
specifier|public
name|StandardKeySet
parameter_list|()
block|{
name|super
argument_list|(
name|ForwardingMap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|/**    * A sensible, albeit inefficient, definition of {@link #containsKey} in terms of the {@code    * iterator} method of {@link #entrySet}. If you override {@link #entrySet}, you may wish to    * override {@link #containsKey} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
annotation|@
name|Beta
DECL|method|standardContainsKey (@heckForNull Object key)
specifier|protected
name|boolean
name|standardContainsKey
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|containsKeyImpl
argument_list|(
name|this
argument_list|,
name|key
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible implementation of {@link Map#values} in terms of the following methods: {@link    * ForwardingMap#clear}, {@link ForwardingMap#containsValue}, {@link ForwardingMap#isEmpty},    * {@link ForwardingMap#size}, and the {@link Set#iterator} method of {@link    * ForwardingMap#entrySet}. In many cases, you may wish to override {@link ForwardingMap#values}    * to forward to this implementation or a subclass thereof.    *    * @since 10.0    */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|StandardValues
specifier|protected
class|class
name|StandardValues
extends|extends
name|Maps
operator|.
name|Values
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardValues ()
specifier|public
name|StandardValues
parameter_list|()
block|{
name|super
argument_list|(
name|ForwardingMap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|/**    * A sensible definition of {@link #containsValue} in terms of the {@code iterator} method of    * {@link #entrySet}. If you override {@link #entrySet}, you may wish to override {@link    * #containsValue} to forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardContainsValue (@heckForNull Object value)
specifier|protected
name|boolean
name|standardContainsValue
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|value
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|containsValueImpl
argument_list|(
name|this
argument_list|,
name|value
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible implementation of {@link Map#entrySet} in terms of the following methods: {@link    * ForwardingMap#clear}, {@link ForwardingMap#containsKey}, {@link ForwardingMap#get}, {@link    * ForwardingMap#isEmpty}, {@link ForwardingMap#remove}, and {@link ForwardingMap#size}. In many    * cases, you may wish to override {@link #entrySet} to forward to this implementation or a    * subclass thereof.    *    * @since 10.0    */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|StandardEntrySet
specifier|protected
specifier|abstract
class|class
name|StandardEntrySet
extends|extends
name|Maps
operator|.
name|EntrySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|StandardEntrySet ()
specifier|public
name|StandardEntrySet
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|map ()
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|ForwardingMap
operator|.
name|this
return|;
block|}
block|}
end_class

begin_comment
comment|/**    * A sensible definition of {@link #isEmpty} in terms of the {@code iterator} method of {@link    * #entrySet}. If you override {@link #entrySet}, you may wish to override {@link #isEmpty} to    * forward to this implementation.    *    * @since 7.0    */
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
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #equals} in terms of the {@code equals} method of {@link    * #entrySet}. If you override {@link #entrySet}, you may wish to override {@link #equals} to    * forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardEquals (@heckForNull Object object)
specifier|protected
name|boolean
name|standardEquals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|object
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|object
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #hashCode} in terms of the {@code iterator} method of {@link    * #entrySet}. If you override {@link #entrySet}, you may wish to override {@link #hashCode} to    * forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardHashCode ()
specifier|protected
name|int
name|standardHashCode
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|hashCodeImpl
argument_list|(
name|entrySet
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * A sensible definition of {@link #toString} in terms of the {@code iterator} method of {@link    * #entrySet}. If you override {@link #entrySet}, you may wish to override {@link #toString} to    * forward to this implementation.    *    * @since 7.0    */
end_comment

begin_function
DECL|method|standardToString ()
specifier|protected
name|String
name|standardToString
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|toStringImpl
argument_list|(
name|this
argument_list|)
return|;
block|}
end_function

unit|}
end_unit

