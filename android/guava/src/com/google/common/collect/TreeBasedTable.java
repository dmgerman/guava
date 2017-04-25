begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
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
name|base
operator|.
name|Function
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
name|Supplier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|NoSuchElementException
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
name|java
operator|.
name|util
operator|.
name|SortedMap
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of {@code Table} whose row keys and column keys are ordered  * by their natural ordering or by supplied comparators. When constructing a  * {@code TreeBasedTable}, you may provide comparators for the row keys and  * the column keys, or you may use natural ordering for both.  *  *<p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link  * #rowMap} method returns a {@link SortedMap}, instead of the {@link Set} and  * {@link Map} specified by the {@link Table} interface.  *  *<p>The views returned by {@link #column}, {@link #columnKeySet()}, and {@link  * #columnMap()} have iterators that don't support {@code remove()}. Otherwise,  * all optional operations are supported. Null row keys, columns keys, and  * values are not supported.  *  *<p>Lookups by row key are often faster than lookups by column key, because  * the data is stored in a {@code Map<R, Map<C, V>>}. A method call like {@code  * column(columnKey).get(rowKey)} still runs quickly, since the row key is  * provided. However, {@code column(columnKey).size()} takes longer, since an  * iteration across all row keys occurs.  *  *<p>Because a {@code TreeBasedTable} has unique sorted values for a given  * row, both {@code row(rowKey)} and {@code rowMap().get(rowKey)} are {@link  * SortedMap} instances, instead of the {@link Map} specified in the {@link  * Table} interface.  *  *<p>Note that this implementation is not synchronized. If multiple threads  * access this table concurrently and one of the threads modifies the table, it  * must be synchronized externally.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#table">  * {@code Table}</a>.  *  * @author Jared Levy  * @author Louis Wasserman  * @since 7.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|TreeBasedTable
specifier|public
class|class
name|TreeBasedTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|columnComparator
specifier|private
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
decl_stmt|;
DECL|class|Factory
specifier|private
specifier|static
class|class
name|Factory
parameter_list|<
name|C
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|TreeMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|comparator
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|comparator
decl_stmt|;
DECL|method|Factory (Comparator<? super C> comparator)
name|Factory
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|comparator
parameter_list|)
block|{
name|this
operator|.
name|comparator
operator|=
name|comparator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|TreeMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|TreeMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
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
comment|/**    * Creates an empty {@code TreeBasedTable} that uses the natural orderings    * of both row and column keys.    *    *<p>The method signature specifies {@code R extends Comparable} with a raw    * {@link Comparable}, instead of {@code R extends Comparable<? super R>},    * and the same for {@code C}. That's necessary to support classes defined    * without generics.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|R
extends|extends
name|Comparable
parameter_list|,
name|C
extends|extends
name|Comparable
parameter_list|,
name|V
parameter_list|>
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
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
comment|/**    * Creates an empty {@code TreeBasedTable} that is ordered by the specified    * comparators.    *    * @param rowComparator the comparator that orders the row keys    * @param columnComparator the comparator that orders the column keys    */
DECL|method|create ( Comparator<? super R> rowComparator, Comparator<? super C> columnComparator)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|rowComparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|columnComparator
argument_list|)
expr_stmt|;
return|return
operator|new
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|rowComparator
argument_list|,
name|columnComparator
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code TreeBasedTable} with the same mappings and sort order    * as the specified {@code TreeBasedTable}.    */
DECL|method|create (TreeBasedTable<R, C, ? extends V> table)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|table
parameter_list|)
block|{
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|result
init|=
operator|new
name|TreeBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|table
operator|.
name|rowComparator
argument_list|()
argument_list|,
name|table
operator|.
name|columnComparator
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|putAll
argument_list|(
name|table
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|TreeBasedTable (Comparator<? super R> rowComparator, Comparator<? super C> columnComparator)
name|TreeBasedTable
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|R
argument_list|,
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|(
name|rowComparator
argument_list|)
argument_list|,
operator|new
name|Factory
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|columnComparator
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnComparator
operator|=
name|columnComparator
expr_stmt|;
block|}
comment|// TODO(jlevy): Move to StandardRowSortedTable?
comment|/**    * Returns the comparator that orders the rows. With natural ordering, {@link Ordering#natural()}    * is returned.    *    * @deprecated Use {@code table.rowKeySet().comparator()} instead. This method is scheduled for    *     removal in April 2019.    */
annotation|@
name|Deprecated
DECL|method|rowComparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|()
block|{
return|return
name|rowKeySet
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
comment|/**    * Returns the comparator that orders the columns. With natural ordering, {@link    * Ordering#natural()} is returned.    *    * @deprecated Store the {@link Comparator} alongside the {@link Table}. Or, if you know that the    *     {@link Table} contains at least one value, you can retrieve the {@link Comparator} with:    *     {@code ((SortedMap<C, V>) table.rowMap().values().iterator().next()).comparator();}. This    *     method is scheduled for removal in April 2019.    */
annotation|@
name|Deprecated
DECL|method|columnComparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|()
block|{
return|return
name|columnComparator
return|;
block|}
comment|// TODO(lowasser): make column return a SortedMap
comment|/**    * {@inheritDoc}    *    *<p>Because a {@code TreeBasedTable} has unique sorted values for a given    * row, this method returns a {@link SortedMap}, instead of the {@link Map}    * specified in the {@link Table} interface.    * @since 10.0    *     (<a href="https://github.com/google/guava/wiki/Compatibility"    *>mostly source-compatible</a> since 7.0)    */
annotation|@
name|Override
DECL|method|row (R rowKey)
specifier|public
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
parameter_list|(
name|R
name|rowKey
parameter_list|)
block|{
return|return
operator|new
name|TreeRow
argument_list|(
name|rowKey
argument_list|)
return|;
block|}
DECL|class|TreeRow
specifier|private
class|class
name|TreeRow
extends|extends
name|Row
implements|implements
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|lowerBound
annotation|@
name|Nullable
specifier|final
name|C
name|lowerBound
decl_stmt|;
DECL|field|upperBound
annotation|@
name|Nullable
specifier|final
name|C
name|upperBound
decl_stmt|;
DECL|method|TreeRow (R rowKey)
name|TreeRow
parameter_list|(
name|R
name|rowKey
parameter_list|)
block|{
name|this
argument_list|(
name|rowKey
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|TreeRow (R rowKey, @Nullable C lowerBound, @Nullable C upperBound)
name|TreeRow
parameter_list|(
name|R
name|rowKey
parameter_list|,
annotation|@
name|Nullable
name|C
name|lowerBound
parameter_list|,
annotation|@
name|Nullable
name|C
name|upperBound
parameter_list|)
block|{
name|super
argument_list|(
name|rowKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|lowerBound
operator|=
name|lowerBound
expr_stmt|;
name|this
operator|.
name|upperBound
operator|=
name|upperBound
expr_stmt|;
name|checkArgument
argument_list|(
name|lowerBound
operator|==
literal|null
operator|||
name|upperBound
operator|==
literal|null
operator|||
name|compare
argument_list|(
name|lowerBound
argument_list|,
name|upperBound
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|SortedSet
argument_list|<
name|C
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|Maps
operator|.
name|SortedKeySet
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|columnComparator
argument_list|()
return|;
block|}
DECL|method|compare (Object a, Object b)
name|int
name|compare
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
comment|// pretend we can compare anything
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
name|Comparator
argument_list|<
name|Object
argument_list|>
name|cmp
init|=
operator|(
name|Comparator
operator|)
name|comparator
argument_list|()
decl_stmt|;
return|return
name|cmp
operator|.
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
DECL|method|rangeContains (@ullable Object o)
name|boolean
name|rangeContains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|!=
literal|null
operator|&&
operator|(
name|lowerBound
operator|==
literal|null
operator|||
name|compare
argument_list|(
name|lowerBound
argument_list|,
name|o
argument_list|)
operator|<=
literal|0
operator|)
operator|&&
operator|(
name|upperBound
operator|==
literal|null
operator|||
name|compare
argument_list|(
name|upperBound
argument_list|,
name|o
argument_list|)
operator|>
literal|0
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|subMap (C fromKey, C toKey)
specifier|public
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|C
name|fromKey
parameter_list|,
name|C
name|toKey
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rangeContains
argument_list|(
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
argument_list|)
operator|&&
name|rangeContains
argument_list|(
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|TreeRow
argument_list|(
name|rowKey
argument_list|,
name|fromKey
argument_list|,
name|toKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headMap (C toKey)
specifier|public
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|C
name|toKey
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rangeContains
argument_list|(
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|TreeRow
argument_list|(
name|rowKey
argument_list|,
name|lowerBound
argument_list|,
name|toKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailMap (C fromKey)
specifier|public
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|C
name|fromKey
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rangeContains
argument_list|(
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|TreeRow
argument_list|(
name|rowKey
argument_list|,
name|fromKey
argument_list|,
name|upperBound
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|firstKey ()
specifier|public
name|C
name|firstKey
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|backing
init|=
name|backingRowMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|backing
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
return|return
name|backingRowMap
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lastKey ()
specifier|public
name|C
name|lastKey
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|backing
init|=
name|backingRowMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|backing
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
return|return
name|backingRowMap
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
DECL|field|wholeRow
specifier|transient
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|wholeRow
decl_stmt|;
comment|/*      * If the row was previously empty, we check if there's a new row here every      * time we're queried.      */
DECL|method|wholeRow ()
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|wholeRow
parameter_list|()
block|{
if|if
condition|(
name|wholeRow
operator|==
literal|null
operator|||
operator|(
name|wholeRow
operator|.
name|isEmpty
argument_list|()
operator|&&
name|backingMap
operator|.
name|containsKey
argument_list|(
name|rowKey
argument_list|)
operator|)
condition|)
block|{
name|wholeRow
operator|=
operator|(
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|backingMap
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
expr_stmt|;
block|}
return|return
name|wholeRow
return|;
block|}
annotation|@
name|Override
DECL|method|backingRowMap ()
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|backingRowMap
parameter_list|()
block|{
return|return
operator|(
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|backingRowMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|computeBackingRowMap ()
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|computeBackingRowMap
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|map
init|=
name|wholeRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|lowerBound
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
name|map
operator|.
name|tailMap
argument_list|(
name|lowerBound
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|upperBound
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
name|map
operator|.
name|headMap
argument_list|(
name|upperBound
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|maintainEmptyInvariant ()
name|void
name|maintainEmptyInvariant
parameter_list|()
block|{
if|if
condition|(
name|wholeRow
argument_list|()
operator|!=
literal|null
operator|&&
name|wholeRow
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|backingMap
operator|.
name|remove
argument_list|(
name|rowKey
argument_list|)
expr_stmt|;
name|wholeRow
operator|=
literal|null
expr_stmt|;
name|backingRowMap
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|rangeContains
argument_list|(
name|key
argument_list|)
operator|&&
name|super
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (C key, V value)
specifier|public
name|V
name|put
parameter_list|(
name|C
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|rangeContains
argument_list|(
name|checkNotNull
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
comment|// rowKeySet() and rowMap() are defined here so they appear in the Javadoc.
annotation|@
name|Override
DECL|method|rowKeySet ()
specifier|public
name|SortedSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
parameter_list|()
block|{
return|return
name|super
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|rowMap ()
specifier|public
name|SortedMap
argument_list|<
name|R
argument_list|,
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowMap
parameter_list|()
block|{
return|return
name|super
operator|.
name|rowMap
argument_list|()
return|;
block|}
comment|/**    * Overridden column iterator to return columns values in globally sorted    * order.    */
annotation|@
name|Override
DECL|method|createColumnKeyIterator ()
name|Iterator
argument_list|<
name|C
argument_list|>
name|createColumnKeyIterator
parameter_list|()
block|{
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|comparator
init|=
name|columnComparator
argument_list|()
decl_stmt|;
specifier|final
name|Iterator
argument_list|<
name|C
argument_list|>
name|merged
init|=
name|Iterators
operator|.
name|mergeSorted
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|backingMap
operator|.
name|values
argument_list|()
argument_list|,
operator|new
name|Function
argument_list|<
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|,
name|Iterator
argument_list|<
name|C
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|C
argument_list|>
name|apply
parameter_list|(
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
argument_list|)
argument_list|,
name|comparator
argument_list|)
decl_stmt|;
return|return
operator|new
name|AbstractIterator
argument_list|<
name|C
argument_list|>
argument_list|()
block|{
name|C
name|lastValue
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|C
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|merged
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|C
name|next
init|=
name|merged
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|duplicate
init|=
name|lastValue
operator|!=
literal|null
operator|&&
name|comparator
operator|.
name|compare
argument_list|(
name|next
argument_list|,
name|lastValue
argument_list|)
operator|==
literal|0
decl_stmt|;
comment|// Keep looping till we find a non-duplicate value.
if|if
condition|(
operator|!
name|duplicate
condition|)
block|{
name|lastValue
operator|=
name|next
expr_stmt|;
return|return
name|lastValue
return|;
block|}
block|}
name|lastValue
operator|=
literal|null
expr_stmt|;
comment|// clear reference to unused data
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
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
