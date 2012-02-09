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
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
operator|.
name|keyOrNull
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
name|NavigableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NavigableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
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
comment|/**  * A navigable map which forwards all its method calls to another navigable map. Subclasses should  * override one or more methods to modify the behavior of the backing map as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *   *<p><i>Warning:</i> The methods of {@code ForwardingNavigableMap} forward<i>indiscriminately</i>  * to the methods of the delegate. For example, overriding {@link #put} alone<i>will not</i>  * change the behavior of {@link #putAll}, which can lead to unexpected behavior. In this case, you  * should override {@code putAll} as well, either providing your own implementation, or delegating  * to the provided {@code standardPutAll} method.  *   *<p>Each of the {@code standard} methods uses the map's comparator (or the natural ordering of  * the elements, if there is no comparator) to test element equality. As a result, if the comparator  * is not consistent with equals, some of the standard implementations may violate the {@code Map}  * contract.  *   *<p>The {@code standard} methods and the collection views they return are not guaranteed to be  * thread-safe, even when all of the methods that they depend on are thread-safe.  *   * @author Louis Wasserman  * @since 12.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ForwardingNavigableMap
specifier|public
specifier|abstract
class|class
name|ForwardingNavigableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingSortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Nullable
DECL|method|poll (Iterator<T> iterator)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|poll
parameter_list|(
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|)
block|{
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|T
name|result
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|lowerEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lowerEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lowerEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #lowerEntry} in terms of the {@code lastEntry()} of    * {@link #headMap(Object, boolean)}. If you override {@code headMap}, you may wish to override    * {@code lowerEntry} to forward to this implementation.    */
DECL|method|standardLowerEntry (K key)
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardLowerEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
operator|.
name|lastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lowerKey (K key)
specifier|public
name|K
name|lowerKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lowerKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #lowerKey} in terms of {@code lowerEntry}. If you override    * {@link #lowerEntry}, you may wish to override {@code lowerKey} to forward to this    * implementation.    */
DECL|method|standardLowerKey (K key)
specifier|protected
name|K
name|standardLowerKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|keyOrNull
argument_list|(
name|lowerEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floorEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|floorEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|floorEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #floorEntry} in terms of the {@code lastEntry()} of    * {@link #headMap(Object, boolean)}. If you override {@code headMap}, you may wish to override    * {@code floorEntry} to forward to this implementation.    */
DECL|method|standardFloorEntry (K key)
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardFloorEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
operator|.
name|lastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|floorKey (K key)
specifier|public
name|K
name|floorKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|floorKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #floorKey} in terms of {@code floorEntry}. If you override    * {@code floorEntry}, you may wish to override {@code floorKey} to forward to this    * implementation.    */
DECL|method|standardFloorKey (K key)
specifier|protected
name|K
name|standardFloorKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|keyOrNull
argument_list|(
name|floorEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceilingEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ceilingEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|ceilingEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #ceilingEntry} in terms of the {@code firstEntry()} of    * {@link #tailMap(Object, boolean)}. If you override {@code tailMap}, you may wish to override    * {@code ceilingEntry} to forward to this implementation.    */
DECL|method|standardCeilingEntry (K key)
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardCeilingEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
operator|.
name|firstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|ceilingKey (K key)
specifier|public
name|K
name|ceilingKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|ceilingKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #ceilingKey} in terms of {@code ceilingEntry}. If you override    * {@code ceilingEntry}, you may wish to override {@code ceilingKey} to forward to this    * implementation.    */
DECL|method|standardCeilingKey (K key)
specifier|protected
name|K
name|standardCeilingKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|keyOrNull
argument_list|(
name|ceilingEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higherEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|higherEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|higherEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #higherEntry} in terms of the {@code firstEntry()} of    * {@link #tailMap(Object, boolean)}. If you override {@code tailMap}, you may wish to override    * {@code higherEntry} to forward to this implementation.    */
DECL|method|standardHigherEntry (K key)
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardHigherEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
operator|.
name|firstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|higherKey (K key)
specifier|public
name|K
name|higherKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|higherKey
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #higherKey} in terms of {@code higherEntry}. If you override    * {@code higherEntry}, you may wish to override {@code higherKey} to forward to this    * implementation.    */
DECL|method|standardHigherKey (K key)
specifier|protected
name|K
name|standardHigherKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|keyOrNull
argument_list|(
name|higherEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|firstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|firstEntry
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #firstEntry} in terms of the {@code iterator()} of    * {@link #entrySet}. If you override {@code entrySet}, you may wish to override    * {@code firstEntry} to forward to this implementation.    */
DECL|method|standardFirstEntry ()
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardFirstEntry
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|getFirst
argument_list|(
name|entrySet
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #firstKey} in terms of {@code firstEntry}. If you override    * {@code firstEntry}, you may wish to override {@code firstKey} to forward to this    * implementation.    */
DECL|method|standardFirstKey ()
specifier|protected
name|K
name|standardFirstKey
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|firstEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|lastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lastEntry
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #lastEntry} in terms of the {@code iterator()} of the    * {@link #entrySet} of {@link #descendingMap}. If you override {@code descendingMap}, you may    * wish to override {@code lastEntry} to forward to this implementation.    */
DECL|method|standardLastEntry ()
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardLastEntry
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|getFirst
argument_list|(
name|descendingMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #lastKey} in terms of {@code lastEntry}. If you override    * {@code lastEntry}, you may wish to override {@code lastKey} to forward to this implementation.    */
DECL|method|standardLastKey ()
specifier|protected
name|K
name|standardLastKey
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|lastEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|pollFirstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollFirstEntry
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|pollFirstEntry
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #pollFirstEntry} in terms of the {@code iterator} of    * {@code entrySet}. If you override {@code entrySet}, you may wish to override    * {@code pollFirstEntry} to forward to this implementation.    */
DECL|method|standardPollFirstEntry ()
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardPollFirstEntry
parameter_list|()
block|{
return|return
name|poll
argument_list|(
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pollLastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollLastEntry
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|pollLastEntry
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #pollFirstEntry} in terms of the {@code iterator} of the    * {@code entrySet} of {@code descendingMap}. If you override {@code descendingMap}, you may wish    * to override {@code pollFirstEntry} to forward to this implementation.    */
DECL|method|standardPollLastEntry ()
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardPollLastEntry
parameter_list|()
block|{
return|return
name|poll
argument_list|(
name|descendingMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|descendingMap ()
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|descendingMap
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|descendingMap
argument_list|()
return|;
block|}
comment|/**    * A sensible implementation of {@link NavigableMap#descendingMap} in terms of the methods of    * this {@code NavigableMap}. In many cases, you may wish to override    * {@link ForwardingNavigableMap#descendingMap} to forward to this implementation or a subclass    * thereof.    *     *<p>In particular, this map iterates over entries with repeated calls to    * {@link NavigableMap#lowerEntry}. If a more efficient means of iteration is available, you may    * wish to override the {@code entryIterator()} method of this class.    *     * @since 12.0    */
annotation|@
name|Beta
DECL|class|StandardDescendingMap
specifier|protected
class|class
name|StandardDescendingMap
extends|extends
name|Maps
operator|.
name|DescendingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|forward ()
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
parameter_list|()
block|{
return|return
name|ForwardingNavigableMap
operator|.
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
specifier|protected
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
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|toRemove
init|=
literal|null
decl_stmt|;
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextOrNull
init|=
name|forward
argument_list|()
operator|.
name|lastEntry
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextOrNull
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
try|try
block|{
return|return
name|nextOrNull
return|;
block|}
finally|finally
block|{
name|toRemove
operator|=
name|nextOrNull
expr_stmt|;
name|nextOrNull
operator|=
name|forward
argument_list|()
operator|.
name|lowerEntry
argument_list|(
name|nextOrNull
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkState
argument_list|(
name|toRemove
operator|!=
literal|null
argument_list|,
literal|"Each call to remove() must be preceded by a call to next()"
argument_list|)
expr_stmt|;
name|forward
argument_list|()
operator|.
name|remove
argument_list|(
name|toRemove
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|toRemove
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|navigableKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|navigableKeySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|navigableKeySet
argument_list|()
return|;
block|}
comment|/**    * A sensible implementation of {@link NavigableMap#navigableKeySet} in terms of the methods of    * this {@code NavigableMap}. In many cases, you may wish to override    * {@link ForwardingNavigableMap#navigableKeySet} to forward to this implementation or a subclass    * thereof.    *    * @since 12.0    */
annotation|@
name|Beta
DECL|class|StandardNavigableKeySet
specifier|protected
class|class
name|StandardNavigableKeySet
extends|extends
name|Maps
operator|.
name|NavigableKeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|map ()
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|ForwardingNavigableMap
operator|.
name|this
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|descendingKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|descendingKeySet
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|descendingKeySet
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #descendingKeySet} as the {@code navigableKeySet} of    * {@link #descendingMap}. (The {@link StandardDescendingMap} implementation implements    * {@code navigableKeySet} on its own, so as not to cause an infinite loop.) If you override    * {@code descendingMap}, you may wish to override {@code descendingKeySet} to forward to this    * implementation.    */
DECL|method|standardDescendingKeySet ()
specifier|protected
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|standardDescendingKeySet
parameter_list|()
block|{
return|return
name|descendingMap
argument_list|()
operator|.
name|navigableKeySet
argument_list|()
return|;
block|}
comment|/**    * A sensible definition of {@link #subMap(Object, Object)} in terms of    * {@link #subMap(Object, boolean, Object, boolean)}. If you override    * {@code subMap(K, boolean, K, boolean)}, you may wish to override {@code subMap} to forward to    * this implementation.    */
annotation|@
name|Override
DECL|method|standardSubMap (K fromKey, K toKey)
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardSubMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
return|return
name|subMap
argument_list|(
name|fromKey
argument_list|,
literal|true
argument_list|,
name|toKey
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subMap (K fromKey, boolean fromInclusive, K toKey, boolean toInclusive)
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|K
name|toKey
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromKey
argument_list|,
name|fromInclusive
argument_list|,
name|toKey
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headMap (K toKey, boolean inclusive)
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailMap (K fromKey, boolean inclusive)
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromKey
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #headMap(Object)} in terms of    * {@link #headMap(Object, boolean)}. If you override {@code headMap(K, boolean)}, you may wish    * to override {@code headMap} to forward to this implementation.    */
DECL|method|standardHeadMap (K toKey)
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardHeadMap
parameter_list|(
name|K
name|toKey
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|toKey
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**    * A sensible definition of {@link #tailMap(Object)} in terms of    * {@link #tailMap(Object, boolean)}. If you override {@code tailMap(K, boolean)}, you may wish    * to override {@code tailMap} to forward to this implementation.    */
DECL|method|standardTailMap (K fromKey)
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|standardTailMap
parameter_list|(
name|K
name|fromKey
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|fromKey
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

