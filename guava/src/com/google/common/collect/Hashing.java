begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|primitives
operator|.
name|Ints
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
comment|/**  * Static methods for implementing hash-based collections.  *  * @author Kevin Bourrillion  * @author Jesse Wilson  * @author Austin Appleby  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Hashing
specifier|final
class|class
name|Hashing
block|{
DECL|method|Hashing ()
specifier|private
name|Hashing
parameter_list|()
block|{}
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
DECL|method|smearedHash (@ullable Object o)
specifier|static
name|int
name|smearedHash
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
return|return
name|smear
argument_list|(
operator|(
name|o
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|o
operator|.
name|hashCode
argument_list|()
argument_list|)
return|;
block|}
DECL|field|MAX_TABLE_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|MAX_TABLE_SIZE
init|=
name|Ints
operator|.
name|MAX_POWER_OF_TWO
decl_stmt|;
DECL|method|closedTableSize (int expectedEntries, double loadFactor)
specifier|static
name|int
name|closedTableSize
parameter_list|(
name|int
name|expectedEntries
parameter_list|,
name|double
name|loadFactor
parameter_list|)
block|{
comment|// Get the recommended table size.
comment|// Round down to the nearest power of 2.
name|expectedEntries
operator|=
name|Math
operator|.
name|max
argument_list|(
name|expectedEntries
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|int
name|tableSize
init|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|expectedEntries
argument_list|)
decl_stmt|;
comment|// Check to make sure that we will not exceed the maximum load factor.
if|if
condition|(
name|expectedEntries
operator|>
call|(
name|int
call|)
argument_list|(
name|loadFactor
operator|*
name|tableSize
argument_list|)
condition|)
block|{
name|tableSize
operator|<<=
literal|1
expr_stmt|;
return|return
operator|(
name|tableSize
operator|>
literal|0
operator|)
condition|?
name|tableSize
else|:
name|MAX_TABLE_SIZE
return|;
block|}
return|return
name|tableSize
return|;
block|}
DECL|method|needsResizing (int size, int tableSize, double loadFactor)
specifier|static
name|boolean
name|needsResizing
parameter_list|(
name|int
name|size
parameter_list|,
name|int
name|tableSize
parameter_list|,
name|double
name|loadFactor
parameter_list|)
block|{
return|return
name|size
operator|>
name|loadFactor
operator|*
name|tableSize
operator|&&
name|tableSize
operator|<
name|MAX_TABLE_SIZE
return|;
block|}
block|}
end_class

end_unit

