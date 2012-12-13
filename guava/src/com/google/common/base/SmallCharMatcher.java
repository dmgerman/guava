begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|GwtIncompatible
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
name|base
operator|.
name|CharMatcher
operator|.
name|FastMatcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
import|;
end_import

begin_comment
comment|/**  * An immutable version of CharMatcher for smallish sets of characters that uses a hash table  * with linear probing to check for matches.  *  * @author Christopher Swenson  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
literal|"no precomputation is done in GWT"
argument_list|)
DECL|class|SmallCharMatcher
specifier|final
class|class
name|SmallCharMatcher
extends|extends
name|FastMatcher
block|{
DECL|field|MAX_SIZE
specifier|static
specifier|final
name|int
name|MAX_SIZE
init|=
literal|1023
decl_stmt|;
DECL|field|table
specifier|private
specifier|final
name|char
index|[]
name|table
decl_stmt|;
DECL|field|containsZero
specifier|private
specifier|final
name|boolean
name|containsZero
decl_stmt|;
DECL|field|filter
specifier|private
specifier|final
name|long
name|filter
decl_stmt|;
DECL|method|SmallCharMatcher (char[] table, long filter, boolean containsZero, String description)
specifier|private
name|SmallCharMatcher
parameter_list|(
name|char
index|[]
name|table
parameter_list|,
name|long
name|filter
parameter_list|,
name|boolean
name|containsZero
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|super
argument_list|(
name|description
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
name|filter
operator|=
name|filter
expr_stmt|;
name|this
operator|.
name|containsZero
operator|=
name|containsZero
expr_stmt|;
block|}
DECL|field|C1
specifier|private
specifier|static
specifier|final
name|int
name|C1
init|=
literal|0xcc9e2d51
decl_stmt|;
DECL|field|C2
specifier|private
specifier|static
specifier|final
name|int
name|C2
init|=
literal|0x1b873593
decl_stmt|;
comment|/*    * This method was rewritten in Java from an intermediate step of the Murmur hash function in    * http://code.google.com/p/smhasher/source/browse/trunk/MurmurHash3.cpp, which contained the    * following header:    *    * MurmurHash3 was written by Austin Appleby, and is placed in the public domain. The author    * hereby disclaims copyright to this source code.    */
DECL|method|smear (int hashCode)
specifier|static
name|int
name|smear
parameter_list|(
name|int
name|hashCode
parameter_list|)
block|{
return|return
name|C2
operator|*
name|Integer
operator|.
name|rotateLeft
argument_list|(
name|hashCode
operator|*
name|C1
argument_list|,
literal|15
argument_list|)
return|;
block|}
DECL|method|checkFilter (int c)
specifier|private
name|boolean
name|checkFilter
parameter_list|(
name|int
name|c
parameter_list|)
block|{
return|return
literal|1
operator|==
operator|(
literal|1
operator|&
operator|(
name|filter
operator|>>
name|c
operator|)
operator|)
return|;
block|}
comment|// This is all essentially copied from ImmutableSet, but we have to duplicate because
comment|// of dependencies.
comment|// Represents how tightly we can pack things, as a maximum.
DECL|field|DESIRED_LOAD_FACTOR
specifier|private
specifier|static
specifier|final
name|double
name|DESIRED_LOAD_FACTOR
init|=
literal|0.5
decl_stmt|;
comment|/**   * Returns an array size suitable for the backing array of a hash table that   * uses open addressing with linear probing in its implementation.  The   * returned size is the smallest power of two that can hold setSize elements   * with the desired load factor.   */
DECL|method|chooseTableSize (int setSize)
annotation|@
name|VisibleForTesting
specifier|static
name|int
name|chooseTableSize
parameter_list|(
name|int
name|setSize
parameter_list|)
block|{
if|if
condition|(
name|setSize
operator|==
literal|1
condition|)
block|{
return|return
literal|2
return|;
block|}
comment|// Correct the size for open addressing to match desired load factor.
comment|// Round up to the next highest power of 2.
name|int
name|tableSize
init|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|setSize
operator|-
literal|1
argument_list|)
operator|<<
literal|1
decl_stmt|;
while|while
condition|(
name|tableSize
operator|*
name|DESIRED_LOAD_FACTOR
operator|<
name|setSize
condition|)
block|{
name|tableSize
operator|<<=
literal|1
expr_stmt|;
block|}
return|return
name|tableSize
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.BitSet"
argument_list|)
DECL|method|from (BitSet chars, String description)
specifier|static
name|CharMatcher
name|from
parameter_list|(
name|BitSet
name|chars
parameter_list|,
name|String
name|description
parameter_list|)
block|{
comment|// Compute the filter.
name|long
name|filter
init|=
literal|0
decl_stmt|;
name|int
name|size
init|=
name|chars
operator|.
name|cardinality
argument_list|()
decl_stmt|;
name|boolean
name|containsZero
init|=
name|chars
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Compute the hash table.
name|char
index|[]
name|table
init|=
operator|new
name|char
index|[
name|chooseTableSize
argument_list|(
name|size
argument_list|)
index|]
decl_stmt|;
name|int
name|mask
init|=
name|table
operator|.
name|length
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
name|chars
operator|.
name|nextSetBit
argument_list|(
literal|0
argument_list|)
init|;
name|c
operator|!=
operator|-
literal|1
condition|;
name|c
operator|=
name|chars
operator|.
name|nextSetBit
argument_list|(
name|c
operator|+
literal|1
argument_list|)
control|)
block|{
comment|// Compute the filter at the same time.
name|filter
operator||=
literal|1L
operator|<<
name|c
expr_stmt|;
name|int
name|index
init|=
name|smear
argument_list|(
name|c
argument_list|)
operator|&
name|mask
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
comment|// Check for empty.
if|if
condition|(
name|table
index|[
name|index
index|]
operator|==
literal|0
condition|)
block|{
name|table
index|[
name|index
index|]
operator|=
operator|(
name|char
operator|)
name|c
expr_stmt|;
break|break;
block|}
comment|// Linear probing.
name|index
operator|=
operator|(
name|index
operator|+
literal|1
operator|)
operator|&
name|mask
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SmallCharMatcher
argument_list|(
name|table
argument_list|,
name|filter
argument_list|,
name|containsZero
argument_list|,
name|description
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|matches (char c)
specifier|public
name|boolean
name|matches
parameter_list|(
name|char
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
return|return
name|containsZero
return|;
block|}
if|if
condition|(
operator|!
name|checkFilter
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|mask
init|=
name|table
operator|.
name|length
operator|-
literal|1
decl_stmt|;
name|int
name|startingIndex
init|=
name|smear
argument_list|(
name|c
argument_list|)
operator|&
name|mask
decl_stmt|;
name|int
name|index
init|=
name|startingIndex
decl_stmt|;
do|do
block|{
comment|// Check for empty.
if|if
condition|(
name|table
index|[
name|index
index|]
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
comment|// Check for match.
block|}
elseif|else
if|if
condition|(
name|table
index|[
name|index
index|]
operator|==
name|c
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// Linear probing.
name|index
operator|=
operator|(
name|index
operator|+
literal|1
operator|)
operator|&
name|mask
expr_stmt|;
block|}
comment|// Check to see if we wrapped around the whole table.
block|}
do|while
condition|(
name|index
operator|!=
name|startingIndex
condition|)
do|;
return|return
literal|false
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.BitSet"
argument_list|)
annotation|@
name|Override
DECL|method|setBits (BitSet table)
name|void
name|setBits
parameter_list|(
name|BitSet
name|table
parameter_list|)
block|{
if|if
condition|(
name|containsZero
condition|)
block|{
name|table
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|char
name|c
range|:
name|this
operator|.
name|table
control|)
block|{
if|if
condition|(
name|c
operator|!=
literal|0
condition|)
block|{
name|table
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

