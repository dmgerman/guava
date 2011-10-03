begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Objects
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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link ImmutableTable} that holds a single cell.  *  * @author gak@google.com (Gregory Kick)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SingletonImmutableTable
specifier|final
class|class
name|SingletonImmutableTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|field|singleRowKey
specifier|private
specifier|final
name|R
name|singleRowKey
decl_stmt|;
DECL|field|singleColumnKey
specifier|private
specifier|final
name|C
name|singleColumnKey
decl_stmt|;
DECL|field|singleValue
specifier|private
specifier|final
name|V
name|singleValue
decl_stmt|;
DECL|method|SingletonImmutableTable (R rowKey, C columnKey, V value)
name|SingletonImmutableTable
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
name|this
operator|.
name|singleRowKey
operator|=
name|checkNotNull
argument_list|(
name|rowKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleColumnKey
operator|=
name|checkNotNull
argument_list|(
name|columnKey
argument_list|)
expr_stmt|;
name|this
operator|.
name|singleValue
operator|=
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|SingletonImmutableTable (Cell<R, C, V> cell)
name|SingletonImmutableTable
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
name|this
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
DECL|method|cellSet ()
annotation|@
name|Override
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
name|ImmutableSet
operator|.
name|of
argument_list|(
name|Tables
operator|.
name|immutableCell
argument_list|(
name|singleRowKey
argument_list|,
name|singleColumnKey
argument_list|,
name|singleValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|column (C columnKey)
annotation|@
name|Override
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
argument_list|)
expr_stmt|;
return|return
name|containsColumn
argument_list|(
name|columnKey
argument_list|)
condition|?
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleRowKey
argument_list|,
name|singleValue
argument_list|)
else|:
name|ImmutableMap
operator|.
expr|<
name|R
operator|,
name|V
operator|>
name|of
argument_list|()
return|;
block|}
DECL|method|columnKeySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|C
argument_list|>
name|columnKeySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|singleColumnKey
argument_list|)
return|;
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
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleColumnKey
argument_list|,
operator|(
name|Map
argument_list|<
name|R
argument_list|,
name|V
argument_list|>
operator|)
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleRowKey
argument_list|,
name|singleValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|contains (@ullable Object rowKey, @Nullable Object columnKey)
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
name|containsRow
argument_list|(
name|rowKey
argument_list|)
operator|&&
name|containsColumn
argument_list|(
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
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleColumnKey
argument_list|,
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
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleRowKey
argument_list|,
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
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleValue
argument_list|,
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
name|contains
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
condition|?
name|singleValue
else|:
literal|null
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|row (R rowKey)
annotation|@
name|Override
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
argument_list|)
expr_stmt|;
return|return
name|containsRow
argument_list|(
name|rowKey
argument_list|)
condition|?
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleColumnKey
argument_list|,
name|singleValue
argument_list|)
else|:
name|ImmutableMap
operator|.
expr|<
name|C
operator|,
name|V
operator|>
name|of
argument_list|()
return|;
block|}
DECL|method|rowKeySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|singleRowKey
argument_list|)
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
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleRowKey
argument_list|,
operator|(
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
operator|)
name|ImmutableMap
operator|.
name|of
argument_list|(
name|singleColumnKey
argument_list|,
name|singleValue
argument_list|)
argument_list|)
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|singleValue
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
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|instanceof
name|Table
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
condition|)
block|{
name|Table
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Table
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|that
operator|.
name|size
argument_list|()
operator|==
literal|1
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
name|thatCell
init|=
name|that
operator|.
name|cellSet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleRowKey
argument_list|,
name|thatCell
operator|.
name|getRowKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleColumnKey
argument_list|,
name|thatCell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|singleValue
argument_list|,
name|thatCell
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_class

begin_function
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|singleRowKey
argument_list|,
name|singleColumnKey
argument_list|,
name|singleValue
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
operator|.
name|append
argument_list|(
name|singleRowKey
argument_list|)
operator|.
name|append
argument_list|(
literal|"={"
argument_list|)
operator|.
name|append
argument_list|(
name|singleColumnKey
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|singleValue
argument_list|)
operator|.
name|append
argument_list|(
literal|"}}"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

