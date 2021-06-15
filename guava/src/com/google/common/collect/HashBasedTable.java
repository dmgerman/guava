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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|LinkedHashMap
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
comment|/**  * Implementation of {@link Table} using linked hash tables. This guarantees predictable iteration  * order of the various views.  *  *<p>The views returned by {@link #column}, {@link #columnKeySet()}, and {@link #columnMap()} have  * iterators that don't support {@code remove()}. Otherwise, all optional operations are supported.  * Null row keys, columns keys, and values are not supported.  *  *<p>Lookups by row key are often faster than lookups by column key, because the data is stored in  * a {@code Map<R, Map<C, V>>}. A method call like {@code column(columnKey).get(rowKey)} still runs  * quickly, since the row key is provided. However, {@code column(columnKey).size()} takes longer,  * since an iteration across all row keys occurs.  *  *<p>Note that this implementation is not synchronized. If multiple threads access this table  * concurrently and one of the threads modifies the table, it must be synchronized externally.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#table"> {@code Table}</a>.  *  * @author Jared Levy  * @since 7.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|HashBasedTable
specifier|public
class|class
name|HashBasedTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|StandardTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
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
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|expectedSize
specifier|final
name|int
name|expectedSize
decl_stmt|;
DECL|method|Factory (int expectedSize)
name|Factory
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
name|this
operator|.
name|expectedSize
operator|=
name|expectedSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedSize
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
comment|/** Creates an empty {@code HashBasedTable}. */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|HashBasedTable
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
name|HashBasedTable
argument_list|<>
argument_list|(
operator|new
name|LinkedHashMap
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
argument_list|()
argument_list|,
operator|new
name|Factory
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates an empty {@code HashBasedTable} with the specified map sizes.    *    * @param expectedRows the expected number of distinct row keys    * @param expectedCellsPerRow the expected number of column key / value mappings in each row    * @throws IllegalArgumentException if {@code expectedRows} or {@code expectedCellsPerRow} is    *     negative    */
DECL|method|create ( int expectedRows, int expectedCellsPerRow)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|HashBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|int
name|expectedRows
parameter_list|,
name|int
name|expectedCellsPerRow
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|expectedCellsPerRow
argument_list|,
literal|"expectedCellsPerRow"
argument_list|)
expr_stmt|;
name|Map
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
name|backingMap
init|=
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedRows
argument_list|)
decl_stmt|;
return|return
operator|new
name|HashBasedTable
argument_list|<>
argument_list|(
name|backingMap
argument_list|,
operator|new
name|Factory
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|expectedCellsPerRow
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code HashBasedTable} with the same mappings as the specified table.    *    * @param table the table to copy    * @throws NullPointerException if any of the row keys, column keys, or values in {@code table} is    *     null    */
DECL|method|create ( Table<? extends R, ? extends C, ? extends V> table)
specifier|public
specifier|static
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|HashBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|create
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
name|HashBasedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|result
init|=
name|create
argument_list|()
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
DECL|method|HashBasedTable (Map<R, Map<C, V>> backingMap, Factory<C, V> factory)
name|HashBasedTable
parameter_list|(
name|Map
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
name|backingMap
parameter_list|,
name|Factory
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|backingMap
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
comment|// Overriding so NullPointerTester test passes.
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
annotation|@
name|Override
DECL|method|containsColumn (@ullable Object columnKey)
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
annotation|@
name|Override
DECL|method|containsRow (@ullable Object rowKey)
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
name|super
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|get (@ullable Object rowKey, @Nullable Object columnKey)
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
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
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
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|remove (@ullable Object rowKey, @Nullable Object columnKey)
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

