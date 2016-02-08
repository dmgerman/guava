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
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|Comparator
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
comment|/**  * Implementation of {@code Multimap} whose keys and values are ordered by  * their natural ordering or by supplied comparators. In all cases, this  * implementation uses {@link Comparable#compareTo} or {@link  * Comparator#compare} instead of {@link Object#equals} to determine  * equivalence of instances.  *  *<p><b>Warning:</b> The comparators or comparables used must be<i>consistent  * with equals</i> as explained by the {@link Comparable} class specification.  * Otherwise, the resulting multiset will violate the general contract of {@link  * SetMultimap}, which it is specified in terms of {@link Object#equals}.  *  *<p>The collections returned by {@code keySet} and {@code asMap} iterate  * through the keys according to the key comparator ordering or the natural  * ordering of the keys. Similarly, {@code get}, {@code removeAll}, and {@code  * replaceValues} return collections that iterate through the values according  * to the value comparator ordering or the natural ordering of the values. The  * collections generated by {@code entries}, {@code keys}, and {@code values}  * iterate across the keys according to the above key ordering, and for each  * key they iterate across the values according to the value ordering.  *  *<p>The multimap does not store duplicate key-value pairs. Adding a new  * key-value pair equal to an existing key-value pair has no effect.  *  *<p>Null keys and values are permitted (provided, of course, that the  * respective comparators support them). All optional multimap methods are  * supported, and all returned views are modifiable.  *  *<p>This class is not threadsafe when any concurrent operations update the  * multimap. Concurrent read operations will work correctly. To allow concurrent  * update operations, wrap your multimap with a call to {@link  * Multimaps#synchronizedSortedSetMultimap}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap">  * {@code Multimap}</a>.  *  * @author Jared Levy  * @author Louis Wasserman  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TreeMultimap
specifier|public
class|class
name|TreeMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSortedKeySortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|keyComparator
specifier|private
specifier|transient
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
decl_stmt|;
DECL|field|valueComparator
specifier|private
specifier|transient
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
decl_stmt|;
comment|/**    * Creates an empty {@code TreeMultimap} ordered by the natural ordering of    * its keys and values.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|,
name|V
extends|extends
name|Comparable
parameter_list|>
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an empty {@code TreeMultimap} instance using explicit comparators.    * Neither comparator may be null; use {@link Ordering#natural()} to specify    * natural order.    *    * @param keyComparator the comparator that determines the key ordering    * @param valueComparator the comparator that determines the value ordering    */
DECL|method|create ( Comparator<? super K> keyComparator, Comparator<? super V> valueComparator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
return|return
operator|new
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|checkNotNull
argument_list|(
name|keyComparator
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|valueComparator
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Constructs a {@code TreeMultimap}, ordered by the natural ordering of its    * keys and values, with the same mappings as the specified multimap.    *    * @param multimap the multimap whose contents are copied to this multimap    */
DECL|method|create ( Multimap<? extends K, ? extends V> multimap)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|,
name|V
extends|extends
name|Comparable
parameter_list|>
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Multimap
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
return|return
operator|new
name|TreeMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|multimap
argument_list|)
return|;
block|}
DECL|method|TreeMultimap (Comparator<? super K> keyComparator, Comparator<? super V> valueComparator)
name|TreeMultimap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|keyComparator
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyComparator
operator|=
name|keyComparator
expr_stmt|;
name|this
operator|.
name|valueComparator
operator|=
name|valueComparator
expr_stmt|;
block|}
DECL|method|TreeMultimap ( Comparator<? super K> keyComparator, Comparator<? super V> valueComparator, Multimap<? extends K, ? extends V> multimap)
specifier|private
name|TreeMultimap
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|,
name|Multimap
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|multimap
parameter_list|)
block|{
name|this
argument_list|(
name|keyComparator
argument_list|,
name|valueComparator
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Creates an empty {@code TreeSet} for a collection of values for one key.    *    * @return a new {@code TreeSet} containing a collection of values for one    *     key    */
annotation|@
name|Override
DECL|method|createCollection ()
name|SortedSet
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|()
block|{
return|return
operator|new
name|TreeSet
argument_list|<
name|V
argument_list|>
argument_list|(
name|valueComparator
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCollection (@ullable K key)
name|Collection
argument_list|<
name|V
argument_list|>
name|createCollection
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|keyComparator
argument_list|()
operator|.
name|compare
argument_list|(
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|createCollection
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Returns the comparator that orders the multimap keys.    */
DECL|method|keyComparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|()
block|{
return|return
name|keyComparator
return|;
block|}
annotation|@
name|Override
DECL|method|valueComparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|()
block|{
return|return
name|valueComparator
return|;
block|}
comment|/**    * @since 14.0 (present with return type {@code SortedSet} since 2.0)    */
annotation|@
name|Override
annotation|@
name|GwtIncompatible
comment|// NavigableSet
DECL|method|get (@ullable K key)
specifier|public
name|NavigableSet
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|Nullable
name|K
name|key
parameter_list|)
block|{
return|return
operator|(
name|NavigableSet
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code TreeMultimap} has unique sorted keys, this method    * returns a {@link NavigableSet}, instead of the {@link java.util.Set} specified    * in the {@link Multimap} interface.    *    * @since 14.0 (present with return type {@code SortedSet} since 2.0)    */
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|(
name|NavigableSet
argument_list|<
name|K
argument_list|>
operator|)
name|super
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code TreeMultimap} has unique sorted keys, this method    * returns a {@link NavigableMap}, instead of the {@link java.util.Map} specified    * in the {@link Multimap} interface.    *    * @since 14.0 (present with return type {@code SortedMap} since 2.0)    */
annotation|@
name|Override
DECL|method|asMap ()
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
operator|)
name|super
operator|.
name|asMap
argument_list|()
return|;
block|}
comment|/**    * @serialData key comparator, value comparator, number of distinct keys, and    *     then for each distinct key: the key, number of values for that key, and    *     key values    */
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectOutputStream
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|keyComparator
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|valueComparator
argument_list|()
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|writeMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.io.ObjectInputStream
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|keyComparator
operator|=
name|checkNotNull
argument_list|(
operator|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
name|valueComparator
operator|=
name|checkNotNull
argument_list|(
operator|(
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
name|setMap
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|(
name|keyComparator
argument_list|)
argument_list|)
expr_stmt|;
name|Serialization
operator|.
name|populateMultimap
argument_list|(
name|this
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// not needed in emulated source
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

