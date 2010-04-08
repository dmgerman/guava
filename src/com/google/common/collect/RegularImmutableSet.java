begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|VisibleForTesting
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
name|ImmutableSet
operator|.
name|ArrayImmutableSet
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableSet} with two or more elements.  *  * @author Kevin Bourrillion  */
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
DECL|class|RegularImmutableSet
specifier|final
class|class
name|RegularImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ArrayImmutableSet
argument_list|<
name|E
argument_list|>
block|{
comment|// the same elements in hashed positions (plus nulls)
DECL|field|table
annotation|@
name|VisibleForTesting
specifier|final
specifier|transient
name|Object
index|[]
name|table
decl_stmt|;
comment|// 'and' with an int to get a valid table index.
DECL|field|mask
specifier|private
specifier|final
specifier|transient
name|int
name|mask
decl_stmt|;
DECL|field|hashCode
specifier|private
specifier|final
specifier|transient
name|int
name|hashCode
decl_stmt|;
DECL|method|RegularImmutableSet ( Object[] elements, int hashCode, Object[] table, int mask)
name|RegularImmutableSet
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|,
name|int
name|hashCode
parameter_list|,
name|Object
index|[]
name|table
parameter_list|,
name|int
name|mask
parameter_list|)
block|{
name|super
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
expr_stmt|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|target
operator|.
name|hashCode
argument_list|()
argument_list|)
init|;
literal|true
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|candidate
init|=
name|table
index|[
name|i
operator|&
name|mask
index|]
decl_stmt|;
if|if
condition|(
name|candidate
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|candidate
operator|.
name|equals
argument_list|(
name|target
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
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
name|hashCode
return|;
block|}
DECL|method|isHashCodeFast ()
annotation|@
name|Override
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

