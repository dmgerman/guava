begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|errorprone
operator|.
name|annotations
operator|.
name|Immutable
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/** A {@code RegularImmutableTable} optimized for sparse data. */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Immutable
argument_list|(
name|containerOf
operator|=
block|{
literal|"R"
block|,
literal|"C"
block|,
literal|"V"
block|}
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|SparseImmutableTable
specifier|final
class|class
name|SparseImmutableTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|RegularImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|EMPTY
specifier|static
specifier|final
name|ImmutableTable
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|SparseImmutableTable
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Cell
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
operator|>
name|of
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|rowMap
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowMap
decl_stmt|;
DECL|field|columnMap
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|>
name|columnMap
decl_stmt|;
comment|// For each cell in iteration order, the index of that cell's row key in the row key list.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
comment|// We don't modify this after construction.
DECL|field|cellRowIndices
specifier|private
specifier|final
name|int
index|[]
name|cellRowIndices
decl_stmt|;
comment|// For each cell in iteration order, the index of that cell's column key in the list of column
comment|// keys present in that row.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"Immutable"
argument_list|)
comment|// We don't modify this after construction.
DECL|field|cellColumnInRowIndices
specifier|private
specifier|final
name|int
index|[]
name|cellColumnInRowIndices
decl_stmt|;
DECL|method|SparseImmutableTable ( ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
name|SparseImmutableTable
parameter_list|(
name|ImmutableList
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
name|cellList
parameter_list|,
name|ImmutableSet
argument_list|<
name|R
argument_list|>
name|rowSpace
parameter_list|,
name|ImmutableSet
argument_list|<
name|C
argument_list|>
name|columnSpace
parameter_list|)
block|{
name|Map
argument_list|<
name|R
argument_list|,
name|Integer
argument_list|>
name|rowIndex
init|=
name|Maps
operator|.
name|indexMap
argument_list|(
name|rowSpace
argument_list|)
decl_stmt|;
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
name|rows
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|R
name|row
range|:
name|rowSpace
control|)
block|{
name|rows
operator|.
name|put
argument_list|(
name|row
argument_list|,
operator|new
name|LinkedHashMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Map
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
name|columns
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|C
name|col
range|:
name|columnSpace
control|)
block|{
name|columns
operator|.
name|put
argument_list|(
name|col
argument_list|,
operator|new
name|LinkedHashMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
index|[]
name|cellRowIndices
init|=
operator|new
name|int
index|[
name|cellList
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
index|[]
name|cellColumnInRowIndices
init|=
operator|new
name|int
index|[
name|cellList
operator|.
name|size
argument_list|()
index|]
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
name|cellList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
init|=
name|cellList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|R
name|rowKey
init|=
name|cell
operator|.
name|getRowKey
argument_list|()
decl_stmt|;
name|C
name|columnKey
init|=
name|cell
operator|.
name|getColumnKey
argument_list|()
decl_stmt|;
name|V
name|value
init|=
name|cell
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|/*        * These requireNonNull calls are safe because we construct the maps to hold all the provided        * cells.        */
name|cellRowIndices
index|[
name|i
index|]
operator|=
name|requireNonNull
argument_list|(
name|rowIndex
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|thisRow
init|=
name|requireNonNull
argument_list|(
name|rows
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
argument_list|)
decl_stmt|;
name|cellColumnInRowIndices
index|[
name|i
index|]
operator|=
name|thisRow
operator|.
name|size
argument_list|()
expr_stmt|;
name|V
name|oldValue
init|=
name|thisRow
operator|.
name|put
argument_list|(
name|columnKey
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|checkNoDuplicate
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|,
name|oldValue
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|columns
operator|.
name|get
argument_list|(
name|columnKey
argument_list|)
argument_list|)
operator|.
name|put
argument_list|(
name|rowKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|cellRowIndices
operator|=
name|cellRowIndices
expr_stmt|;
name|this
operator|.
name|cellColumnInRowIndices
operator|=
name|cellColumnInRowIndices
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|R
argument_list|,
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowBuilder
init|=
operator|new
name|ImmutableMap
operator|.
name|Builder
argument_list|<>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
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
name|row
range|:
name|rows
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|rowBuilder
operator|.
name|put
argument_list|(
name|row
operator|.
name|getKey
argument_list|()
argument_list|,
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|row
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|rowMap
operator|=
name|rowBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|C
argument_list|,
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|>
name|columnBuilder
init|=
operator|new
name|ImmutableMap
operator|.
name|Builder
argument_list|<>
argument_list|(
name|columns
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
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
name|col
range|:
name|columns
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|columnBuilder
operator|.
name|put
argument_list|(
name|col
operator|.
name|getKey
argument_list|()
argument_list|,
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|col
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|columnMap
operator|=
name|columnBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|columnMap ()
specifier|public
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
block|{
comment|// Casts without copying.
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
argument_list|>
name|columnMap
init|=
name|this
operator|.
name|columnMap
decl_stmt|;
return|return
name|ImmutableMap
operator|.
expr|<
name|C
operator|,
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
operator|>
name|copyOf
argument_list|(
name|columnMap
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|rowMap ()
specifier|public
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
block|{
comment|// Casts without copying.
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowMap
init|=
name|this
operator|.
name|rowMap
decl_stmt|;
return|return
name|ImmutableMap
operator|.
expr|<
name|R
operator|,
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|>
name|copyOf
argument_list|(
name|rowMap
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|cellRowIndices
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|getCell (int index)
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|getCell
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|int
name|rowIndex
init|=
name|cellRowIndices
index|[
name|index
index|]
decl_stmt|;
name|Entry
argument_list|<
name|R
argument_list|,
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowEntry
init|=
name|rowMap
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|rowEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|int
name|columnIndex
init|=
name|cellColumnInRowIndices
index|[
name|index
index|]
decl_stmt|;
name|Entry
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|colEntry
init|=
name|row
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|columnIndex
argument_list|)
decl_stmt|;
return|return
name|cellOf
argument_list|(
name|rowEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|colEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|colEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int index)
name|V
name|getValue
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|int
name|rowIndex
init|=
name|cellRowIndices
index|[
name|index
index|]
decl_stmt|;
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|rowMap
operator|.
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|rowIndex
argument_list|)
decl_stmt|;
name|int
name|columnIndex
init|=
name|cellColumnInRowIndices
index|[
name|index
index|]
decl_stmt|;
return|return
name|row
operator|.
name|values
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|columnIndex
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createSerializedForm ()
name|SerializedForm
name|createSerializedForm
parameter_list|()
block|{
name|Map
argument_list|<
name|C
argument_list|,
name|Integer
argument_list|>
name|columnKeyToIndex
init|=
name|Maps
operator|.
name|indexMap
argument_list|(
name|columnKeySet
argument_list|()
argument_list|)
decl_stmt|;
name|int
index|[]
name|cellColumnIndices
init|=
operator|new
name|int
index|[
name|cellSet
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
range|:
name|cellSet
argument_list|()
control|)
block|{
comment|// requireNonNull is safe because the cell exists in the table.
name|cellColumnIndices
index|[
name|i
operator|++
index|]
operator|=
name|requireNonNull
argument_list|(
name|columnKeyToIndex
operator|.
name|get
argument_list|(
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|SerializedForm
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|cellRowIndices
argument_list|,
name|cellColumnIndices
argument_list|)
return|;
block|}
block|}
end_class

end_unit

