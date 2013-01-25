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
name|Preconditions
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableList} with one or more elements.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|RegularImmutableList
class|class
name|RegularImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|offset
specifier|private
specifier|final
specifier|transient
name|int
name|offset
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
DECL|field|array
specifier|private
specifier|final
specifier|transient
name|Object
index|[]
name|array
decl_stmt|;
DECL|method|RegularImmutableList (Object[] array, int offset, int size)
name|RegularImmutableList
parameter_list|(
name|Object
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|array
operator|=
name|array
expr_stmt|;
block|}
DECL|method|RegularImmutableList (Object[] array)
name|RegularImmutableList
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|this
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
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
name|size
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
DECL|method|isPartialView ()
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|offset
operator|!=
literal|0
operator|||
name|size
operator|!=
name|array
operator|.
name|length
return|;
block|}
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|ObjectArrays
operator|.
name|copyAsObjectArray
argument_list|(
name|array
argument_list|,
name|offset
argument_list|,
name|size
argument_list|)
return|;
block|}
DECL|method|toArray (T[] other)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|.
name|length
operator|<
name|size
condition|)
block|{
name|other
operator|=
name|ObjectArrays
operator|.
name|newArray
argument_list|(
name|other
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|other
operator|.
name|length
operator|>
name|size
condition|)
block|{
name|other
index|[
name|size
index|]
operator|=
literal|null
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
name|offset
argument_list|,
name|other
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|other
return|;
block|}
comment|// The fake cast to E is safe because the creation methods only allow E's
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|get (int index)
specifier|public
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
operator|(
name|E
operator|)
name|array
index|[
name|index
operator|+
name|offset
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|subListUnchecked (int fromIndex, int toIndex)
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|subListUnchecked
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
operator|new
name|RegularImmutableList
argument_list|<
name|E
argument_list|>
argument_list|(
name|array
argument_list|,
name|offset
operator|+
name|fromIndex
argument_list|,
name|toIndex
operator|-
name|fromIndex
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|listIterator (int index)
specifier|public
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
comment|// for performance
comment|// The fake cast to E is safe because the creation methods only allow E's
return|return
operator|(
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
operator|)
name|Iterators
operator|.
name|forArray
argument_list|(
name|array
argument_list|,
name|offset
argument_list|,
name|size
argument_list|,
name|index
argument_list|)
return|;
block|}
comment|// TODO(user): benchmark optimizations for equals() and see if they're worthwhile
block|}
end_class

end_unit

