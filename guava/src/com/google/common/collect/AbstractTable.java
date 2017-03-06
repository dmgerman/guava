begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Table
operator|.
name|Cell
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
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractCollection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|java
operator|.
name|util
operator|.
name|Spliterator
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
comment|/**  * Skeletal, implementation-agnostic implementation of the {@link Table} interface.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractTable
specifier|abstract
class|class
name|AbstractTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
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
name|Maps
operator|.
name|safeContainsKey
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|rowKey
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
name|Maps
operator|.
name|safeContainsKey
argument_list|(
name|columnMap
argument_list|()
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|rowKeySet ()
specifier|public
name|Set
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
annotation|@
name|Override
DECL|method|columnKeySet ()
specifier|public
name|Set
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
for|for
control|(
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
range|:
name|rowMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
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
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|rowKey
argument_list|)
decl_stmt|;
return|return
name|row
operator|!=
literal|null
operator|&&
name|Maps
operator|.
name|safeContainsKey
argument_list|(
name|row
argument_list|,
name|columnKey
argument_list|)
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
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|rowKey
argument_list|)
decl_stmt|;
return|return
operator|(
name|row
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|Maps
operator|.
name|safeGet
argument_list|(
name|row
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|==
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Iterators
operator|.
name|clear
argument_list|(
name|cellSet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
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
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|rowKey
argument_list|)
decl_stmt|;
return|return
operator|(
name|row
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|Maps
operator|.
name|safeRemove
argument_list|(
name|row
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|put (R rowKey, C columnKey, V value)
specifier|public
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
return|return
name|row
argument_list|(
name|rowKey
argument_list|)
operator|.
name|put
argument_list|(
name|columnKey
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Table<? extends R, ? extends C, ? extends V> table)
specifier|public
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
for|for
control|(
name|Table
operator|.
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
block|}
DECL|field|cellSet
specifier|private
specifier|transient
name|Set
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
decl_stmt|;
annotation|@
name|Override
DECL|method|cellSet ()
specifier|public
name|Set
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
name|Set
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
name|result
init|=
name|cellSet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|cellSet
operator|=
name|createCellSet
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createCellSet ()
name|Set
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
block|{
return|return
operator|new
name|CellSet
argument_list|()
return|;
block|}
DECL|method|cellIterator ()
specifier|abstract
name|Iterator
argument_list|<
name|Table
operator|.
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
function_decl|;
DECL|method|cellSpliterator ()
specifier|abstract
name|Spliterator
argument_list|<
name|Table
operator|.
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
function_decl|;
annotation|@
name|WeakOuter
DECL|class|CellSet
class|class
name|CellSet
extends|extends
name|AbstractSet
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
block|{
annotation|@
name|Override
DECL|method|contains (Object o)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Cell
condition|)
block|{
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|cell
init|=
operator|(
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|row
operator|!=
literal|null
operator|&&
name|Collections2
operator|.
name|safeContains
argument_list|(
name|row
operator|.
name|entrySet
argument_list|()
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
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
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullable Object o)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Cell
condition|)
block|{
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|cell
init|=
operator|(
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
name|row
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|rowMap
argument_list|()
argument_list|,
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|row
operator|!=
literal|null
operator|&&
name|Collections2
operator|.
name|safeRemove
argument_list|(
name|row
operator|.
name|entrySet
argument_list|()
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
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
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|AbstractTable
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Table
operator|.
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|cellIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
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
name|spliterator
parameter_list|()
block|{
return|return
name|cellSpliterator
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
name|AbstractTable
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
block|}
DECL|field|values
specifier|private
specifier|transient
name|Collection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
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
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|values
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|values
operator|=
name|createValues
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createValues ()
name|Collection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|Values
argument_list|()
return|;
block|}
DECL|method|valuesIterator ()
name|Iterator
argument_list|<
name|V
argument_list|>
name|valuesIterator
parameter_list|()
block|{
return|return
operator|new
name|TransformedIterator
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|,
name|V
argument_list|>
argument_list|(
name|cellSet
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
name|V
name|transform
parameter_list|(
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
parameter_list|)
block|{
return|return
name|cell
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|valuesSpliterator ()
name|Spliterator
argument_list|<
name|V
argument_list|>
name|valuesSpliterator
parameter_list|()
block|{
return|return
name|CollectSpliterators
operator|.
name|map
argument_list|(
name|cellSpliterator
argument_list|()
argument_list|,
name|Table
operator|.
name|Cell
operator|::
name|getValue
argument_list|)
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|Values
class|class
name|Values
extends|extends
name|AbstractCollection
argument_list|<
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|valuesIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
name|Spliterator
argument_list|<
name|V
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|valuesSpliterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object o)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|containsValue
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|AbstractTable
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|AbstractTable
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
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
name|Tables
operator|.
name|equalsImpl
argument_list|(
name|this
argument_list|,
name|obj
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|cellSet
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**    * Returns the string representation {@code rowMap().toString()}.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|rowMap
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

