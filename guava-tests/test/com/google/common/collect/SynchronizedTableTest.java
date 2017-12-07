begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_class
DECL|class|SynchronizedTableTest
specifier|public
class|class
name|SynchronizedTableTest
extends|extends
name|AbstractTableTest
block|{
DECL|class|TestTable
specifier|private
specifier|static
specifier|final
class|class
name|TestTable
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
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|delegate
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// something Serializable
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullableDecl Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|o
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|equals
argument_list|(
name|o
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|hashCode
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|size
argument_list|()
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (@ullableDecl Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|value
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|containsValue
argument_list|(
name|value
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
comment|/* TODO: verify that the Collection is also synchronized? */
return|return
name|delegate
operator|.
name|values
argument_list|()
return|;
block|}
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|cellSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|column (C columnKey)
specifier|public
name|Map
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|column
argument_list|(
name|columnKey
argument_list|)
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|columnKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|columnMap ()
specifier|public
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
name|columnMap
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|columnMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object rowKey, Object columnKey)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|rowKey
parameter_list|,
name|Object
name|columnKey
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
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
DECL|method|containsColumn (Object columnKey)
specifier|public
name|boolean
name|containsColumn
parameter_list|(
name|Object
name|columnKey
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|containsColumn
argument_list|(
name|columnKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsRow (Object rowKey)
specifier|public
name|boolean
name|containsRow
parameter_list|(
name|Object
name|rowKey
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|containsRow
argument_list|(
name|rowKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (Object rowKey, Object columnKey)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|rowKey
parameter_list|,
name|Object
name|columnKey
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|put
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|putAll
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object rowKey, Object columnKey)
specifier|public
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|row (R rowKey)
specifier|public
name|Map
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|row
argument_list|(
name|rowKey
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
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|rowMap ()
specifier|public
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
name|rowMap
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|rowMap
argument_list|()
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
annotation|@
name|Override
DECL|method|create (Object... data)
specifier|protected
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|data
parameter_list|)
block|{
name|TestTable
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table
init|=
operator|new
name|TestTable
argument_list|<>
argument_list|()
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|synced
init|=
name|Synchronized
operator|.
name|table
argument_list|(
name|table
argument_list|,
name|table
operator|.
name|mutex
argument_list|)
decl_stmt|;
name|populate
argument_list|(
name|synced
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return
name|synced
return|;
block|}
block|}
end_class

end_unit

