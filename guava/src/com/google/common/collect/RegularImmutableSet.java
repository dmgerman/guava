begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Spliterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Spliterators
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
name|qual
operator|.
name|Nullable
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
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|RegularImmutableSet
specifier|final
class|class
name|RegularImmutableSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSet
operator|.
name|CachingAsList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|EMPTY_ARRAY
specifier|private
specifier|static
specifier|final
name|Object
index|[]
name|EMPTY_ARRAY
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
DECL|field|EMPTY
specifier|static
specifier|final
name|RegularImmutableSet
argument_list|<
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|RegularImmutableSet
argument_list|<>
argument_list|(
name|EMPTY_ARRAY
argument_list|,
literal|0
argument_list|,
name|EMPTY_ARRAY
argument_list|,
literal|0
argument_list|)
decl_stmt|;
DECL|field|elements
specifier|private
specifier|final
specifier|transient
name|Object
index|[]
name|elements
decl_stmt|;
DECL|field|hashCode
specifier|private
specifier|final
specifier|transient
name|int
name|hashCode
decl_stmt|;
comment|// the same values as `elements` in hashed positions (plus nulls)
DECL|field|table
annotation|@
name|VisibleForTesting
specifier|final
specifier|transient
annotation|@
name|Nullable
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
DECL|method|RegularImmutableSet (Object[] elements, int hashCode, @Nullable Object[] table, int mask)
name|RegularImmutableSet
parameter_list|(
name|Object
index|[]
name|elements
parameter_list|,
name|int
name|hashCode
parameter_list|,
annotation|@
name|Nullable
name|Object
index|[]
name|table
parameter_list|,
name|int
name|mask
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
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
block|}
annotation|@
name|Override
DECL|method|contains (@heckForNull Object target)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|target
parameter_list|)
block|{
annotation|@
name|Nullable
name|Object
index|[]
name|table
init|=
name|this
operator|.
name|table
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
operator|||
name|table
operator|.
name|length
operator|==
literal|0
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
name|smearedHash
argument_list|(
name|target
argument_list|)
init|;
condition|;
name|i
operator|++
control|)
block|{
name|i
operator|&=
name|mask
expr_stmt|;
name|Object
name|candidate
init|=
name|table
index|[
name|i
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
elseif|else
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
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|(
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
operator|)
name|Iterators
operator|.
name|forArray
argument_list|(
name|elements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|spliterator ()
specifier|public
name|Spliterator
argument_list|<
name|E
argument_list|>
name|spliterator
parameter_list|()
block|{
return|return
name|Spliterators
operator|.
name|spliterator
argument_list|(
name|elements
argument_list|,
name|SPLITERATOR_CHARACTERISTICS
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|internalArray ()
name|Object
index|[]
name|internalArray
parameter_list|()
block|{
return|return
name|elements
return|;
block|}
annotation|@
name|Override
DECL|method|internalArrayStart ()
name|int
name|internalArrayStart
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|internalArrayEnd ()
name|int
name|internalArrayEnd
parameter_list|()
block|{
return|return
name|elements
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|copyIntoArray (@ullable Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
annotation|@
name|Nullable
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|dst
argument_list|,
name|offset
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|offset
operator|+
name|elements
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|(
name|table
operator|.
name|length
operator|==
literal|0
operator|)
condition|?
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
else|:
operator|new
name|RegularImmutableAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|elements
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
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
annotation|@
name|Override
DECL|method|isHashCodeFast ()
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

