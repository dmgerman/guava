begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multisets
operator|.
name|ImmutableEntry
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
name|primitives
operator|.
name|Ints
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link ImmutableMultiset} with zero or more elements.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|RegularImmutableMultiset
class|class
name|RegularImmutableMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|EMPTY
specifier|static
specifier|final
name|RegularImmutableMultiset
argument_list|<
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|RegularImmutableMultiset
argument_list|<
name|Object
argument_list|>
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Entry
argument_list|<
name|Object
argument_list|>
operator|>
name|of
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|entries
specifier|private
specifier|final
specifier|transient
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
index|[]
name|entries
decl_stmt|;
DECL|field|hashTable
specifier|private
specifier|final
specifier|transient
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
index|[]
name|hashTable
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
DECL|field|hashCode
specifier|private
specifier|final
specifier|transient
name|int
name|hashCode
decl_stmt|;
DECL|field|elementSet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|elementSet
decl_stmt|;
DECL|method|RegularImmutableMultiset (Collection<? extends Entry<? extends E>> entries)
name|RegularImmutableMultiset
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|E
argument_list|>
argument_list|>
name|entries
parameter_list|)
block|{
name|int
name|distinct
init|=
name|entries
operator|.
name|size
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
index|[]
name|entryArray
init|=
operator|new
name|Multisets
operator|.
name|ImmutableEntry
index|[
name|distinct
index|]
decl_stmt|;
if|if
condition|(
name|distinct
operator|==
literal|0
condition|)
block|{
name|this
operator|.
name|entries
operator|=
name|entryArray
expr_stmt|;
name|this
operator|.
name|hashTable
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|size
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
literal|0
expr_stmt|;
name|this
operator|.
name|elementSet
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|distinct
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
name|int
name|mask
init|=
name|tableSize
operator|-
literal|1
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
index|[]
name|hashTable
init|=
operator|new
name|Multisets
operator|.
name|ImmutableEntry
index|[
name|tableSize
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
name|long
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|E
name|element
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
name|int
name|hash
init|=
name|element
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|int
name|bucket
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|hash
argument_list|)
operator|&
name|mask
decl_stmt|;
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|bucketHead
init|=
name|hashTable
index|[
name|bucket
index|]
decl_stmt|;
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|newEntry
decl_stmt|;
if|if
condition|(
name|bucketHead
operator|==
literal|null
condition|)
block|{
name|boolean
name|canReuseEntry
init|=
name|entry
operator|instanceof
name|Multisets
operator|.
name|ImmutableEntry
operator|&&
operator|!
operator|(
name|entry
operator|instanceof
name|NonTerminalEntry
operator|)
decl_stmt|;
name|newEntry
operator|=
name|canReuseEntry
condition|?
operator|(
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
operator|)
name|entry
else|:
operator|new
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newEntry
operator|=
operator|new
name|NonTerminalEntry
argument_list|<
name|E
argument_list|>
argument_list|(
name|element
argument_list|,
name|count
argument_list|,
name|bucketHead
argument_list|)
expr_stmt|;
block|}
name|hashCode
operator|+=
name|hash
operator|^
name|count
expr_stmt|;
name|entryArray
index|[
name|index
operator|++
index|]
operator|=
name|newEntry
expr_stmt|;
name|hashTable
index|[
name|bucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|size
operator|+=
name|count
expr_stmt|;
block|}
name|this
operator|.
name|entries
operator|=
name|entryArray
expr_stmt|;
name|this
operator|.
name|hashTable
operator|=
name|hashTable
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
expr_stmt|;
block|}
block|}
DECL|class|NonTerminalEntry
specifier|private
specifier|static
specifier|final
class|class
name|NonTerminalEntry
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
block|{
DECL|field|nextInBucket
specifier|private
specifier|final
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|nextInBucket
decl_stmt|;
DECL|method|NonTerminalEntry (E element, int count, ImmutableEntry<E> nextInBucket)
name|NonTerminalEntry
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|,
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|nextInBucket
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|this
operator|.
name|nextInBucket
operator|=
name|nextInBucket
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|nextInBucket ()
specifier|public
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|nextInBucket
parameter_list|()
block|{
return|return
name|nextInBucket
return|;
block|}
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
DECL|method|count (@ullable Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
index|[]
name|hashTable
init|=
name|this
operator|.
name|hashTable
decl_stmt|;
if|if
condition|(
name|element
operator|==
literal|null
operator|||
name|hashTable
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
name|int
name|hash
init|=
name|Hashing
operator|.
name|smearedHash
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|int
name|mask
init|=
name|hashTable
operator|.
name|length
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|Multisets
operator|.
name|ImmutableEntry
argument_list|<
name|E
argument_list|>
name|entry
init|=
name|hashTable
index|[
name|hash
operator|&
name|mask
index|]
init|;
name|entry
operator|!=
literal|null
condition|;
name|entry
operator|=
name|entry
operator|.
name|nextInBucket
argument_list|()
control|)
block|{
if|if
condition|(
name|Objects
operator|.
name|equal
argument_list|(
name|element
argument_list|,
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getCount
argument_list|()
return|;
block|}
block|}
return|return
literal|0
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
DECL|method|elementSet ()
specifier|public
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|E
argument_list|>
name|result
init|=
name|elementSet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|elementSet
operator|=
operator|new
name|ElementSet
argument_list|()
else|:
name|result
return|;
block|}
DECL|class|ElementSet
specifier|private
specifier|final
class|class
name|ElementSet
extends|extends
name|ImmutableSet
operator|.
name|Indexed
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get (int index)
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|entries
index|[
name|index
index|]
operator|.
name|getElement
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|RegularImmutableMultiset
operator|.
name|this
operator|.
name|contains
argument_list|(
name|object
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
literal|true
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
name|entries
operator|.
name|length
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEntry (int index)
name|Entry
argument_list|<
name|E
argument_list|>
name|getEntry
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|entries
index|[
name|index
index|]
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
block|}
end_class

end_unit

