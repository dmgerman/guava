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
name|concurrent
operator|.
name|Immutable
import|;
end_import

begin_comment
comment|/**  * A {@code RegularImmutableTable} optimized for sparse data.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Immutable
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
DECL|field|rowMap
specifier|private
specifier|final
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
decl_stmt|;
DECL|field|columnMap
specifier|private
specifier|final
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
decl_stmt|;
DECL|field|iterationOrderRow
specifier|private
specifier|final
name|int
index|[]
name|iterationOrderRow
decl_stmt|;
DECL|field|iterationOrderColumn
specifier|private
specifier|final
name|int
index|[]
name|iterationOrderColumn
decl_stmt|;
DECL|method|SparseImmutableTable (ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
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
name|Lists
operator|.
name|indexMap
argument_list|(
name|rowSpace
operator|.
name|asList
argument_list|()
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
name|iterationOrderRow
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
name|iterationOrderColumn
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
name|iterationOrderRow
index|[
name|i
index|]
operator|=
name|rowIndex
operator|.
name|get
argument_list|(
name|rowKey
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
name|rows
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
decl_stmt|;
name|iterationOrderColumn
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
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Duplicate value for row="
operator|+
name|rowKey
operator|+
literal|", column="
operator|+
name|columnKey
operator|+
literal|": "
operator|+
name|value
operator|+
literal|", "
operator|+
name|oldValue
argument_list|)
throw|;
block|}
name|columns
operator|.
name|get
argument_list|(
name|columnKey
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
name|iterationOrderRow
operator|=
name|iterationOrderRow
expr_stmt|;
name|this
operator|.
name|iterationOrderColumn
operator|=
name|iterationOrderColumn
expr_stmt|;
name|ImmutableMap
operator|.
name|Builder
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
name|rowBuilder
init|=
operator|new
name|ImmutableMap
operator|.
name|Builder
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
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
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
name|Map
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
argument_list|(
name|columns
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
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
DECL|method|columnMap ()
annotation|@
name|Override
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
return|return
name|columnMap
return|;
block|}
DECL|method|rowMap ()
annotation|@
name|Override
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
return|return
name|rowMap
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
name|iterationOrderRow
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
name|iterationOrderRow
index|[
name|index
index|]
decl_stmt|;
name|Map
operator|.
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
operator|(
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|rowEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|int
name|columnIndex
init|=
name|iterationOrderColumn
index|[
name|index
index|]
decl_stmt|;
name|Map
operator|.
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
name|iterationOrderRow
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
operator|(
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
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
name|iterationOrderColumn
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
block|}
end_class

end_unit

