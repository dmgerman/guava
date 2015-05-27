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
name|collect
operator|.
name|ImmutableMap
operator|.
name|IteratorBasedImmutableMap
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
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
comment|/**  * A {@code RegularImmutableTable} optimized for dense data.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Immutable
DECL|class|DenseImmutableTable
specifier|final
class|class
name|DenseImmutableTable
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
DECL|field|rowKeyToIndex
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|Integer
argument_list|>
name|rowKeyToIndex
decl_stmt|;
DECL|field|columnKeyToIndex
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|Integer
argument_list|>
name|columnKeyToIndex
decl_stmt|;
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
DECL|field|rowCounts
specifier|private
specifier|final
name|int
index|[]
name|rowCounts
decl_stmt|;
DECL|field|columnCounts
specifier|private
specifier|final
name|int
index|[]
name|columnCounts
decl_stmt|;
DECL|field|values
specifier|private
specifier|final
name|V
index|[]
index|[]
name|values
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
DECL|method|DenseImmutableTable ( ImmutableList<Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
name|DenseImmutableTable
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|V
index|[]
index|[]
name|array
init|=
operator|(
name|V
index|[]
index|[]
operator|)
operator|new
name|Object
index|[
name|rowSpace
operator|.
name|size
argument_list|()
index|]
index|[
name|columnSpace
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|this
operator|.
name|values
operator|=
name|array
expr_stmt|;
name|this
operator|.
name|rowKeyToIndex
operator|=
name|Maps
operator|.
name|indexMap
argument_list|(
name|rowSpace
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnKeyToIndex
operator|=
name|Maps
operator|.
name|indexMap
argument_list|(
name|columnSpace
argument_list|)
expr_stmt|;
name|rowCounts
operator|=
operator|new
name|int
index|[
name|rowKeyToIndex
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
name|columnCounts
operator|=
operator|new
name|int
index|[
name|columnKeyToIndex
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
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
name|int
name|rowIndex
init|=
name|rowKeyToIndex
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
decl_stmt|;
name|int
name|columnIndex
init|=
name|columnKeyToIndex
operator|.
name|get
argument_list|(
name|columnKey
argument_list|)
decl_stmt|;
name|V
name|existingValue
init|=
name|values
index|[
name|rowIndex
index|]
index|[
name|columnIndex
index|]
decl_stmt|;
name|checkArgument
argument_list|(
name|existingValue
operator|==
literal|null
argument_list|,
literal|"duplicate key: (%s, %s)"
argument_list|,
name|rowKey
argument_list|,
name|columnKey
argument_list|)
expr_stmt|;
name|values
index|[
name|rowIndex
index|]
index|[
name|columnIndex
index|]
operator|=
name|cell
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|rowCounts
index|[
name|rowIndex
index|]
operator|++
expr_stmt|;
name|columnCounts
index|[
name|columnIndex
index|]
operator|++
expr_stmt|;
name|iterationOrderRow
index|[
name|i
index|]
operator|=
name|rowIndex
expr_stmt|;
name|iterationOrderColumn
index|[
name|i
index|]
operator|=
name|columnIndex
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
name|this
operator|.
name|rowMap
operator|=
operator|new
name|RowMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|columnMap
operator|=
operator|new
name|ColumnMap
argument_list|()
expr_stmt|;
block|}
comment|/**    * An immutable map implementation backed by an indexed nullable array.    */
DECL|class|ImmutableArrayMap
specifier|private
specifier|abstract
specifier|static
class|class
name|ImmutableArrayMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|IteratorBasedImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|size
specifier|private
specifier|final
name|int
name|size
decl_stmt|;
DECL|method|ImmutableArrayMap (int size)
name|ImmutableArrayMap
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
DECL|method|keyToIndex ()
specifier|abstract
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|Integer
argument_list|>
name|keyToIndex
parameter_list|()
function_decl|;
comment|// True if getValue never returns null.
DECL|method|isFull ()
specifier|private
name|boolean
name|isFull
parameter_list|()
block|{
return|return
name|size
operator|==
name|keyToIndex
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getKey (int index)
name|K
name|getKey
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|keyToIndex
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|asList
argument_list|()
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Nullable
DECL|method|getValue (int keyIndex)
specifier|abstract
name|V
name|getValue
parameter_list|(
name|int
name|keyIndex
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
name|isFull
argument_list|()
condition|?
name|keyToIndex
argument_list|()
operator|.
name|keySet
argument_list|()
else|:
name|super
operator|.
name|createKeySet
argument_list|()
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
name|size
return|;
block|}
annotation|@
name|Override
DECL|method|get (@ullable Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
name|Integer
name|keyIndex
init|=
name|keyToIndex
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|keyIndex
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|getValue
argument_list|(
name|keyIndex
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|UnmodifiableIterator
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
name|AbstractIterator
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
name|int
name|index
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
specifier|final
name|int
name|maxIndex
init|=
name|keyToIndex
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|computeNext
parameter_list|()
block|{
for|for
control|(
name|index
operator|++
init|;
name|index
operator|<
name|maxIndex
condition|;
name|index
operator|++
control|)
block|{
name|V
name|value
init|=
name|getValue
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|getKey
argument_list|(
name|index
argument_list|)
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
DECL|class|Row
specifier|private
specifier|final
class|class
name|Row
extends|extends
name|ImmutableArrayMap
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|rowIndex
specifier|private
specifier|final
name|int
name|rowIndex
decl_stmt|;
DECL|method|Row (int rowIndex)
name|Row
parameter_list|(
name|int
name|rowIndex
parameter_list|)
block|{
name|super
argument_list|(
name|rowCounts
index|[
name|rowIndex
index|]
argument_list|)
expr_stmt|;
name|this
operator|.
name|rowIndex
operator|=
name|rowIndex
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keyToIndex ()
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|Integer
argument_list|>
name|keyToIndex
parameter_list|()
block|{
return|return
name|columnKeyToIndex
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int keyIndex)
name|V
name|getValue
parameter_list|(
name|int
name|keyIndex
parameter_list|)
block|{
return|return
name|values
index|[
name|rowIndex
index|]
index|[
name|keyIndex
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|class|Column
specifier|private
specifier|final
class|class
name|Column
extends|extends
name|ImmutableArrayMap
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
block|{
DECL|field|columnIndex
specifier|private
specifier|final
name|int
name|columnIndex
decl_stmt|;
DECL|method|Column (int columnIndex)
name|Column
parameter_list|(
name|int
name|columnIndex
parameter_list|)
block|{
name|super
argument_list|(
name|columnCounts
index|[
name|columnIndex
index|]
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnIndex
operator|=
name|columnIndex
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keyToIndex ()
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|Integer
argument_list|>
name|keyToIndex
parameter_list|()
block|{
return|return
name|rowKeyToIndex
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int keyIndex)
name|V
name|getValue
parameter_list|(
name|int
name|keyIndex
parameter_list|)
block|{
return|return
name|values
index|[
name|keyIndex
index|]
index|[
name|columnIndex
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|class|RowMap
specifier|private
specifier|final
class|class
name|RowMap
extends|extends
name|ImmutableArrayMap
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
block|{
DECL|method|RowMap ()
specifier|private
name|RowMap
parameter_list|()
block|{
name|super
argument_list|(
name|rowCounts
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keyToIndex ()
name|ImmutableMap
argument_list|<
name|R
argument_list|,
name|Integer
argument_list|>
name|keyToIndex
parameter_list|()
block|{
return|return
name|rowKeyToIndex
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int keyIndex)
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|getValue
parameter_list|(
name|int
name|keyIndex
parameter_list|)
block|{
return|return
operator|new
name|Row
argument_list|(
name|keyIndex
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|ColumnMap
specifier|private
specifier|final
class|class
name|ColumnMap
extends|extends
name|ImmutableArrayMap
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
block|{
DECL|method|ColumnMap ()
specifier|private
name|ColumnMap
parameter_list|()
block|{
name|super
argument_list|(
name|columnCounts
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|keyToIndex ()
name|ImmutableMap
argument_list|<
name|C
argument_list|,
name|Integer
argument_list|>
name|keyToIndex
parameter_list|()
block|{
return|return
name|columnKeyToIndex
return|;
block|}
annotation|@
name|Override
DECL|method|getValue (int keyIndex)
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
name|getValue
parameter_list|(
name|int
name|keyIndex
parameter_list|)
block|{
return|return
operator|new
name|Column
argument_list|(
name|keyIndex
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
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
return|return
name|columnMap
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
return|return
name|rowMap
return|;
block|}
annotation|@
name|Override
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
name|Integer
name|rowIndex
init|=
name|rowKeyToIndex
operator|.
name|get
argument_list|(
name|rowKey
argument_list|)
decl_stmt|;
name|Integer
name|columnIndex
init|=
name|columnKeyToIndex
operator|.
name|get
argument_list|(
name|columnKey
argument_list|)
decl_stmt|;
return|return
operator|(
operator|(
name|rowIndex
operator|==
literal|null
operator|)
operator|||
operator|(
name|columnIndex
operator|==
literal|null
operator|)
operator|)
condition|?
literal|null
else|:
name|values
index|[
name|rowIndex
index|]
index|[
name|columnIndex
index|]
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
name|int
name|columnIndex
init|=
name|iterationOrderColumn
index|[
name|index
index|]
decl_stmt|;
name|R
name|rowKey
init|=
name|rowKeySet
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
name|C
name|columnKey
init|=
name|columnKeySet
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
name|V
name|value
init|=
name|values
index|[
name|rowIndex
index|]
index|[
name|columnIndex
index|]
decl_stmt|;
return|return
name|cellOf
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|,
name|value
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
return|return
name|values
index|[
name|iterationOrderRow
index|[
name|index
index|]
index|]
index|[
name|iterationOrderColumn
index|[
name|index
index|]
index|]
return|;
block|}
block|}
end_class

end_unit

