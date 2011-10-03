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
comment|/**  * An empty implementation of {@link ImmutableTable}.  *  * @author gak@google.com (Gregory Kick)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Immutable
DECL|class|EmptyImmutableTable
specifier|final
class|class
name|EmptyImmutableTable
extends|extends
name|ImmutableTable
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|EmptyImmutableTable
name|INSTANCE
init|=
operator|new
name|EmptyImmutableTable
argument_list|()
decl_stmt|;
DECL|method|EmptyImmutableTable ()
specifier|private
name|EmptyImmutableTable
parameter_list|()
block|{}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
DECL|method|get (@ullable Object rowKey, @Nullable Object columnKey)
annotation|@
name|Override
specifier|public
name|Object
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
literal|true
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
return|return
name|that
operator|.
name|isEmpty
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
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
literal|0
return|;
block|}
DECL|method|cellSet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
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
name|cellSet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|column (Object columnKey)
annotation|@
name|Override
specifier|public
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|column
parameter_list|(
name|Object
name|columnKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|columnKey
argument_list|)
expr_stmt|;
return|return
name|ImmutableMap
operator|.
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
name|Object
argument_list|>
name|columnKeySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
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
name|Object
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|columnMap
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
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
literal|false
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
literal|false
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
literal|false
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
literal|false
return|;
block|}
DECL|method|row (Object rowKey)
annotation|@
name|Override
specifier|public
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|row
parameter_list|(
name|Object
name|rowKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|rowKey
argument_list|)
expr_stmt|;
return|return
name|ImmutableMap
operator|.
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
name|Object
argument_list|>
name|rowKeySet
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|rowMap ()
annotation|@
name|Override
specifier|public
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|rowMap
parameter_list|()
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"{}"
return|;
block|}
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|ImmutableCollection
argument_list|<
name|Object
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
comment|// preserve singleton property
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

