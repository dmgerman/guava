begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Implementation of {@code Table} whose row keys and column keys are ordered  * by their natural ordering or by supplied comparators. When constructing a  * {@code TreeBasedTable}, you may provide comparators for the row keys and  * the column keys, or you may use natural ordering for both.  *  *<p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link  * #rowMap} method returns a {@link SortedMap}, instead of the {@link Set} and  * {@link Map} specified by the {@link Table} interface.  *  *<p>Note that the column keys as returned by {@link #columnKeySet()} and  * {@link #columnMap()} are<i>not</i> sorted. Sorted column keys can be  * observed in {@link Table#row(Object)} and the values of {@link #rowMap()}.  *  *<p>The views returned by {@link #column}, {@link #columnKeySet()}, and {@link  * #columnMap()} have iterators that don't support {@code remove()}. Otherwise,  * all optional operations are supported. Null row keys, columns keys, and  * values are not supported.  *  *<p>Lookups by row key are often faster than lookups by column key, because  * the data is stored in a {@code Map<R, Map<C, V>>}. A method call like {@code  * column(columnKey).get(rowKey)} still runs quickly, since the row key is  * provided. However, {@code column(columnKey).size()} takes longer, since an  * iteration across all row keys occurs.  *  *<p>Note that this implementation is not synchronized. If multiple threads  * access this table concurrently and one of the threads modifies the table, it  * must be synchronized externally.  *  * @author Jared Levy  * @since 7  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|Beta
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// eclipse doesn't like the raw Comparable
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
DECL|method|create ()
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
DECL|method|create ( TreeBasedTable<R, C, ? extends V> table)
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
comment|/**    * Returns the comparator that orders the rows. With natural ordering,    * {@link Ordering#natural()} is returned.    */
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
comment|/**    * Returns the comparator that orders the columns. With natural ordering,    * {@link Ordering#natural()} is returned.    */
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
comment|// rowKeySet() and rowMap() are defined here so they appear in the Javadoc.
DECL|method|rowKeySet ()
annotation|@
name|Override
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
DECL|method|rowMap ()
annotation|@
name|Override
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
comment|// Overriding so NullPointerTester test passes.
DECL|method|contains ( @ullable Object rowKey, @Nullable Object columnKey)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|rowKey
parameter_list|,
annotation|@
name|Nullable
name|Object
name|columnKey
parameter_list|)
block|{
return|return
name|super
operator|.
name|contains
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
DECL|method|containsColumn (@ullable Object columnKey)
annotation|@
name|Override
specifier|public
name|boolean
name|containsColumn
parameter_list|(
annotation|@
name|Nullable
name|Object
name|columnKey
parameter_list|)
block|{
return|return
name|super
operator|.
name|containsColumn
argument_list|(
name|columnKey
argument_list|)
return|;
block|}
DECL|method|containsRow (@ullable Object rowKey)
annotation|@
name|Override
specifier|public
name|boolean
name|containsRow
parameter_list|(
annotation|@
name|Nullable
name|Object
name|rowKey
parameter_list|)
block|{
return|return
name|super
operator|.
name|containsRow
argument_list|(
name|rowKey
argument_list|)
return|;
block|}
DECL|method|containsValue (@ullable Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
return|return
name|super
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|get (@ullable Object rowKey, @Nullable Object columnKey)
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|rowKey
parameter_list|,
annotation|@
name|Nullable
name|Object
name|columnKey
parameter_list|)
block|{
return|return
name|super
operator|.
name|get
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
return|return
name|super
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
DECL|method|remove ( @ullable Object rowKey, @Nullable Object columnKey)
annotation|@
name|Override
specifier|public
name|V
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|rowKey
parameter_list|,
annotation|@
name|Nullable
name|Object
name|columnKey
parameter_list|)
block|{
return|return
name|super
operator|.
name|remove
argument_list|(
name|rowKey
argument_list|,
name|columnKey
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
end_class

end_unit

