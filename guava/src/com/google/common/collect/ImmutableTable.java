begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|MoreObjects
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
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotCall
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
name|DoNotMock
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
name|List
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
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BinaryOperator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
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
name|stream
operator|.
name|Collector
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
comment|/**  * A {@link Table} whose contents will never change, with many other important properties detailed  * at {@link ImmutableCollection}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/ImmutableCollectionsExplained"> immutable collections</a>.  *  * @author Gregory Kick  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ImmutableTable
specifier|public
specifier|abstract
class|class
name|ImmutableTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
implements|implements
name|Serializable
block|{
comment|/**    * Returns a {@code Collector} that accumulates elements into an {@code ImmutableTable}. Each    * input element is mapped to one cell in the returned table, with the rows, columns, and values    * generated by applying the specified functions.    *    *<p>The returned {@code Collector} will throw a {@code NullPointerException} at collection time    * if the row, column, or value functions return null on any input.    *    * @since 21.0    */
DECL|method|toImmutableTable ( Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableTable
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|)
block|{
return|return
name|TableCollectors
operator|.
name|toImmutableTable
argument_list|(
name|rowFunction
argument_list|,
name|columnFunction
argument_list|,
name|valueFunction
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code Collector} that accumulates elements into an {@code ImmutableTable}. Each    * input element is mapped to one cell in the returned table, with the rows, columns, and values    * generated by applying the specified functions. If multiple inputs are mapped to the same row    * and column pair, they will be combined with the specified merging function in encounter order.    *    *<p>The returned {@code Collector} will throw a {@code NullPointerException} at collection time    * if the row, column, value, or merging functions return null on any input.    *    * @since 21.0    */
DECL|method|toImmutableTable ( Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|Collector
argument_list|<
name|T
argument_list|,
name|?
argument_list|,
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|toImmutableTable
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|R
argument_list|>
name|rowFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|C
argument_list|>
name|columnFunction
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|valueFunction
parameter_list|,
name|BinaryOperator
argument_list|<
name|V
argument_list|>
name|mergeFunction
parameter_list|)
block|{
return|return
name|TableCollectors
operator|.
name|toImmutableTable
argument_list|(
name|rowFunction
argument_list|,
name|columnFunction
argument_list|,
name|valueFunction
argument_list|,
name|mergeFunction
argument_list|)
return|;
block|}
comment|/** Returns an empty immutable table. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|of ()
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|of
parameter_list|()
block|{
return|return
operator|(
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|SparseImmutableTable
operator|.
name|EMPTY
return|;
block|}
comment|/** Returns an immutable table containing a single cell. */
DECL|method|of (R rowKey, C columnKey, V value)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|of
parameter_list|(
name|R
name|rowKey
parameter_list|,
name|C
name|columnKey
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
operator|new
name|SingletonImmutableTable
argument_list|<>
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns an immutable copy of the provided table.    *    *<p>The {@link Table#cellSet()} iteration order of the provided table determines the iteration    * ordering of all views in the returned table. Note that some views of the original table and the    * copied table may have different iteration orders. For more control over the ordering, create a    * {@link Builder} and call {@link Builder#orderRowsBy}, {@link Builder#orderColumnsBy}, and    * {@link Builder#putAll}    *    *<p>Despite the method name, this method attempts to avoid actually copying the data when it is    * safe to do so. The exact circumstances under which a copy will or will not be performed are    * undocumented and subject to change.    */
DECL|method|copyOf ( Table<? extends R, ? extends C, ? extends V> table)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Table
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|table
parameter_list|)
block|{
if|if
condition|(
name|table
operator|instanceof
name|ImmutableTable
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|parameterizedTable
init|=
operator|(
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|table
decl_stmt|;
return|return
name|parameterizedTable
return|;
block|}
else|else
block|{
return|return
name|copyOf
argument_list|(
name|table
operator|.
name|cellSet
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|copyOf ( Iterable<? extends Cell<? extends R, ? extends C, ? extends V>> cells)
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|copyOf
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Cell
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|cells
parameter_list|)
block|{
name|ImmutableTable
operator|.
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|builder
init|=
name|ImmutableTable
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Cell
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|cell
range|:
name|cells
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|cell
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder created by the {@link    * Builder#Builder() ImmutableTable.Builder()} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<>
argument_list|()
return|;
block|}
comment|/**    * Verifies that {@code rowKey}, {@code columnKey} and {@code value} are non-null, and returns a    * new entry with those values.    */
DECL|method|cellOf (R rowKey, C columnKey, V value)
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cellOf
parameter_list|(
name|R
name|rowKey
parameter_list|,
name|C
name|columnKey
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|Tables
operator|.
name|immutableCell
argument_list|(
name|checkNotNull
argument_list|(
name|rowKey
argument_list|,
literal|"rowKey"
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|columnKey
argument_list|,
literal|"columnKey"
argument_list|)
argument_list|,
name|checkNotNull
argument_list|(
name|value
argument_list|,
literal|"value"
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * A builder for creating immutable table instances, especially {@code public static final} tables    * ("constant tables"). Example:    *    *<pre>{@code    * static final ImmutableTable<Integer, Character, String> SPREADSHEET =    *     new ImmutableTable.Builder<Integer, Character, String>()    *         .put(1, 'A', "foo")    *         .put(1, 'B', "bar")    *         .put(2, 'A', "baz")    *         .build();    * }</pre>    *    *<p>By default, the order in which cells are added to the builder determines the iteration    * ordering of all views in the returned table, with {@link #putAll} following the {@link    * Table#cellSet()} iteration order. However, if {@link #orderRowsBy} or {@link #orderColumnsBy}    * is called, the views are sorted by the supplied comparators.    *    *<p>For empty or single-cell immutable tables, {@link #of()} and {@link #of(Object, Object,    * Object)} are even more convenient.    *    *<p>Builder instances can be reused - it is safe to call {@link #build} multiple times to build    * multiple tables in series. Each table is a superset of the tables created before it.    *    * @since 11.0    */
annotation|@
name|DoNotMock
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|cells
specifier|private
specifier|final
name|List
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cells
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|rowComparator
specifier|private
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
decl_stmt|;
DECL|field|columnComparator
specifier|private
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
decl_stmt|;
comment|/**      * Creates a new builder. The returned builder is equivalent to the builder generated by {@link      * ImmutableTable#builder}.      */
DECL|method|Builder ()
specifier|public
name|Builder
parameter_list|()
block|{}
comment|/** Specifies the ordering of the generated table's rows. */
annotation|@
name|CanIgnoreReturnValue
DECL|method|orderRowsBy (Comparator<? super R> rowComparator)
specifier|public
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|orderRowsBy
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|)
block|{
name|this
operator|.
name|rowComparator
operator|=
name|checkNotNull
argument_list|(
name|rowComparator
argument_list|,
literal|"rowComparator"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** Specifies the ordering of the generated table's columns. */
annotation|@
name|CanIgnoreReturnValue
DECL|method|orderColumnsBy (Comparator<? super C> columnComparator)
specifier|public
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|orderColumnsBy
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|)
block|{
name|this
operator|.
name|columnComparator
operator|=
name|checkNotNull
argument_list|(
name|columnComparator
argument_list|,
literal|"columnComparator"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Associates the ({@code rowKey}, {@code columnKey}) pair with {@code value} in the built      * table. Duplicate key pairs are not allowed and will cause {@link #build} to fail.      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (R rowKey, C columnKey, V value)
specifier|public
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|R
name|rowKey
parameter_list|,
name|C
name|columnKey
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|cells
operator|.
name|add
argument_list|(
name|cellOf
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds the given {@code cell} to the table, making it immutable if necessary. Duplicate key      * pairs are not allowed and will cause {@link #build} to fail.      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|put (Cell<? extends R, ? extends C, ? extends V> cell)
specifier|public
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|put
parameter_list|(
name|Cell
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|cell
parameter_list|)
block|{
if|if
condition|(
name|cell
operator|instanceof
name|Tables
operator|.
name|ImmutableCell
condition|)
block|{
name|checkNotNull
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
literal|"row"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|,
literal|"column"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|cell
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all supported methods are covariant
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|immutableCell
init|=
operator|(
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|cell
decl_stmt|;
name|cells
operator|.
name|add
argument_list|(
name|immutableCell
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|put
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Associates all of the given table's keys and values in the built table. Duplicate row key      * column key pairs are not allowed, and will cause {@link #build} to fail.      *      * @throws NullPointerException if any key or value in {@code table} is null      */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putAll (Table<? extends R, ? extends C, ? extends V> table)
specifier|public
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|putAll
parameter_list|(
name|Table
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|table
parameter_list|)
block|{
for|for
control|(
name|Cell
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|cell
range|:
name|table
operator|.
name|cellSet
argument_list|()
control|)
block|{
name|put
argument_list|(
name|cell
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
DECL|method|combine (Builder<R, C, V> other)
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|combine
parameter_list|(
name|Builder
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|other
parameter_list|)
block|{
name|this
operator|.
name|cells
operator|.
name|addAll
argument_list|(
name|other
operator|.
name|cells
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a newly-created immutable table.      *      * @throws IllegalArgumentException if duplicate key pairs were added      */
DECL|method|build ()
specifier|public
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|build
parameter_list|()
block|{
name|int
name|size
init|=
name|cells
operator|.
name|size
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|size
condition|)
block|{
case|case
literal|0
case|:
return|return
name|of
argument_list|()
return|;
case|case
literal|1
case|:
return|return
operator|new
name|SingletonImmutableTable
argument_list|<>
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|cells
argument_list|)
argument_list|)
return|;
default|default:
return|return
name|RegularImmutableTable
operator|.
name|forCells
argument_list|(
name|cells
argument_list|,
name|rowComparator
argument_list|,
name|columnComparator
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|ImmutableTable ()
name|ImmutableTable
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|cellSet ()
specifier|public
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellSet
parameter_list|()
block|{
return|return
operator|(
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|super
operator|.
name|cellSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createCellSet ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|createCellSet
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|cellIterator ()
specifier|final
name|UnmodifiableIterator
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellIterator
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|cellSpliterator ()
specifier|final
name|Spliterator
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellSpliterator
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|(
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|values
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createValues ()
specifier|abstract
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|valuesIterator ()
specifier|final
name|Iterator
argument_list|<
name|V
argument_list|>
name|valuesIterator
parameter_list|()
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
comment|/**    * {@inheritDoc}    *    * @throws NullPointerException if {@code columnKey} is {@code null}    */
annotation|@
name|Override
DECL|method|column (C columnKey)
specifier|public
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
name|column
parameter_list|(
name|C
name|columnKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|columnKey
argument_list|,
literal|"columnKey"
argument_list|)
expr_stmt|;
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
operator|(
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
operator|)
name|columnMap
argument_list|()
operator|.
name|get
argument_list|(
name|columnKey
argument_list|)
argument_list|,
name|ImmutableMap
operator|.
expr|<
name|R
argument_list|,
name|V
operator|>
name|of
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|columnKeySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|C
argument_list|>
name|columnKeySet
parameter_list|()
block|{
return|return
name|columnMap
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>The value {@code Map<R, V>} instances in the returned map are {@link ImmutableMap} instances    * as well.    */
annotation|@
name|Override
DECL|method|columnMap ()
specifier|public
specifier|abstract
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|>
name|columnMap
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    * @throws NullPointerException if {@code rowKey} is {@code null}    */
annotation|@
name|Override
DECL|method|row (R rowKey)
specifier|public
name|ImmutableMap
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
name|checkNotNull
argument_list|(
name|rowKey
argument_list|,
literal|"rowKey"
argument_list|)
expr_stmt|;
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
operator|(
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|rowMap
argument_list|()
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
argument_list|,
name|ImmutableMap
operator|.
expr|<
name|C
argument_list|,
name|V
operator|>
name|of
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|rowKeySet ()
specifier|public
name|ImmutableSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
parameter_list|()
block|{
return|return
name|rowMap
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>The value {@code Map<C, V>} instances in the returned map are {@link ImmutableMap} instances    * as well.    */
annotation|@
name|Override
DECL|method|rowMap ()
specifier|public
specifier|abstract
name|ImmutableMap
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
function_decl|;
annotation|@
name|Override
DECL|method|contains (@ullable Object rowKey, @Nullable Object columnKey)
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
name|get
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (@ullable Object value)
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
name|values
argument_list|()
operator|.
name|contains
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the table unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|clear ()
specifier|public
specifier|final
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the table unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|put (R rowKey, C columnKey, V value)
specifier|public
specifier|final
name|V
name|put
parameter_list|(
name|R
name|rowKey
parameter_list|,
name|C
name|columnKey
parameter_list|,
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the table unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|putAll (Table<? extends R, ? extends C, ? extends V> table)
specifier|public
specifier|final
name|void
name|putAll
parameter_list|(
name|Table
argument_list|<
name|?
extends|extends
name|R
argument_list|,
name|?
extends|extends
name|C
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|table
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the table unmodified.    *    * @throws UnsupportedOperationException always    * @deprecated Unsupported operation.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Deprecated
annotation|@
name|Override
annotation|@
name|DoNotCall
argument_list|(
literal|"Always throws UnsupportedOperationException"
argument_list|)
DECL|method|remove (Object rowKey, Object columnKey)
specifier|public
specifier|final
name|V
name|remove
parameter_list|(
name|Object
name|rowKey
parameter_list|,
name|Object
name|columnKey
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/** Creates the common serialized form for this table. */
DECL|method|createSerializedForm ()
specifier|abstract
name|SerializedForm
name|createSerializedForm
parameter_list|()
function_decl|;
comment|/**    * Serialized type for all ImmutableTable instances. It captures the logical contents and    * preserves iteration order of all views.    */
DECL|class|SerializedForm
specifier|static
specifier|final
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|rowKeys
specifier|private
specifier|final
name|Object
index|[]
name|rowKeys
decl_stmt|;
DECL|field|columnKeys
specifier|private
specifier|final
name|Object
index|[]
name|columnKeys
decl_stmt|;
DECL|field|cellValues
specifier|private
specifier|final
name|Object
index|[]
name|cellValues
decl_stmt|;
DECL|field|cellRowIndices
specifier|private
specifier|final
name|int
index|[]
name|cellRowIndices
decl_stmt|;
DECL|field|cellColumnIndices
specifier|private
specifier|final
name|int
index|[]
name|cellColumnIndices
decl_stmt|;
DECL|method|SerializedForm ( Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices)
specifier|private
name|SerializedForm
parameter_list|(
name|Object
index|[]
name|rowKeys
parameter_list|,
name|Object
index|[]
name|columnKeys
parameter_list|,
name|Object
index|[]
name|cellValues
parameter_list|,
name|int
index|[]
name|cellRowIndices
parameter_list|,
name|int
index|[]
name|cellColumnIndices
parameter_list|)
block|{
name|this
operator|.
name|rowKeys
operator|=
name|rowKeys
expr_stmt|;
name|this
operator|.
name|columnKeys
operator|=
name|columnKeys
expr_stmt|;
name|this
operator|.
name|cellValues
operator|=
name|cellValues
expr_stmt|;
name|this
operator|.
name|cellRowIndices
operator|=
name|cellRowIndices
expr_stmt|;
name|this
operator|.
name|cellColumnIndices
operator|=
name|cellColumnIndices
expr_stmt|;
block|}
DECL|method|create ( ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices)
specifier|static
name|SerializedForm
name|create
parameter_list|(
name|ImmutableTable
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|table
parameter_list|,
name|int
index|[]
name|cellRowIndices
parameter_list|,
name|int
index|[]
name|cellColumnIndices
parameter_list|)
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|table
operator|.
name|rowKeySet
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|,
name|table
operator|.
name|columnKeySet
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|,
name|table
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|,
name|cellRowIndices
argument_list|,
name|cellColumnIndices
argument_list|)
return|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
if|if
condition|(
name|cellValues
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|of
argument_list|()
return|;
block|}
if|if
condition|(
name|cellValues
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|of
argument_list|(
name|rowKeys
index|[
literal|0
index|]
argument_list|,
name|columnKeys
index|[
literal|0
index|]
argument_list|,
name|cellValues
index|[
literal|0
index|]
argument_list|)
return|;
block|}
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Cell
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|cellListBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<>
argument_list|(
name|cellValues
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cellValues
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|cellListBuilder
operator|.
name|add
argument_list|(
name|cellOf
argument_list|(
name|rowKeys
index|[
name|cellRowIndices
index|[
name|i
index|]
index|]
argument_list|,
name|columnKeys
index|[
name|cellColumnIndices
index|[
name|i
index|]
index|]
argument_list|,
name|cellValues
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|RegularImmutableTable
operator|.
name|forOrderedComponents
argument_list|(
name|cellListBuilder
operator|.
name|build
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|rowKeys
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|columnKeys
argument_list|)
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
DECL|method|writeReplace ()
specifier|final
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
name|createSerializedForm
argument_list|()
return|;
block|}
block|}
end_class

end_unit

