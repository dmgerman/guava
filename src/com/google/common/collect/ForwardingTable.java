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

begin_comment
comment|/**  * A table which forwards all its method calls to another table. Subclasses  * should override one or more methods to modify the behavior of the backing  * map as desired per the<a  * href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  * @author Gregory Kick  * @since 7  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|ForwardingTable
specifier|public
specifier|abstract
class|class
name|ForwardingTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingObject
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
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingTable ()
specifier|protected
name|ForwardingTable
parameter_list|()
block|{}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
function_decl|;
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
return|return
name|delegate
argument_list|()
operator|.
name|cellSet
argument_list|()
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|column
argument_list|(
name|columnKey
argument_list|)
return|;
block|}
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
name|delegate
argument_list|()
operator|.
name|columnKeySet
argument_list|()
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|columnMap
argument_list|()
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|contains
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
DECL|method|containsColumn (Object columnKey)
specifier|public
name|boolean
name|containsColumn
parameter_list|(
name|Object
name|columnKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsColumn
argument_list|(
name|columnKey
argument_list|)
return|;
block|}
DECL|method|containsRow (Object rowKey)
specifier|public
name|boolean
name|containsRow
parameter_list|(
name|Object
name|rowKey
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsRow
argument_list|(
name|rowKey
argument_list|)
return|;
block|}
DECL|method|containsValue (Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
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
name|delegate
argument_list|()
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
name|delegate
argument_list|()
operator|.
name|putAll
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|remove
argument_list|(
name|rowKey
argument_list|,
name|columnKey
argument_list|)
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|row
argument_list|(
name|rowKey
argument_list|)
return|;
block|}
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
name|delegate
argument_list|()
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
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
return|return
name|delegate
argument_list|()
operator|.
name|rowMap
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|equals (Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
operator|(
name|obj
operator|==
name|this
operator|)
operator|||
name|delegate
argument_list|()
operator|.
name|equals
argument_list|(
name|obj
argument_list|)
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

