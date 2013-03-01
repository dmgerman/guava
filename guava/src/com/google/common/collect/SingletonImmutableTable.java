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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link ImmutableTable} that holds a single cell.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SingletonImmutableTable
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
specifier|final
name|R
name|singleRowKey
decl_stmt|;
DECL|field|singleColumnKey
specifier|final
name|C
name|singleColumnKey
decl_stmt|;
DECL|field|singleValue
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
annotation|@
name|Override
DECL|method|createCellSet ()
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
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|(
name|cellOf
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
DECL|method|createValues ()
annotation|@
name|Override
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
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
block|}
end_class

end_unit

