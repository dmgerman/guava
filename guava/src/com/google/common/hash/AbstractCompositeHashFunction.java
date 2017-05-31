begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|checkArgument
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_comment
comment|/**  * An abstract composition of multiple hash functions. {@linkplain #newHasher()} delegates to the  * {@code Hasher} objects of the delegate hash functions, and in the end, they are used by  * {@linkplain #makeHash(Hasher[])} that constructs the final {@code HashCode}.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|AbstractCompositeHashFunction
specifier|abstract
class|class
name|AbstractCompositeHashFunction
extends|extends
name|AbstractHashFunction
block|{
DECL|field|functions
specifier|final
name|HashFunction
index|[]
name|functions
decl_stmt|;
DECL|method|AbstractCompositeHashFunction (HashFunction... functions)
name|AbstractCompositeHashFunction
parameter_list|(
name|HashFunction
modifier|...
name|functions
parameter_list|)
block|{
for|for
control|(
name|HashFunction
name|function
range|:
name|functions
control|)
block|{
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|functions
operator|=
name|functions
expr_stmt|;
block|}
comment|/**    * Constructs a {@code HashCode} from the {@code Hasher} objects of the functions. Each of them    * has consumed the entire input and they are ready to output a {@code HashCode}. The order of the    * hashers are the same order as the functions given to the constructor.    */
comment|// this could be cleaner if it passed HashCode[], but that would create yet another array...
DECL|method|makeHash (Hasher[] hashers)
comment|/* protected */
specifier|abstract
name|HashCode
name|makeHash
parameter_list|(
name|Hasher
index|[]
name|hashers
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
name|Hasher
index|[]
name|hashers
init|=
operator|new
name|Hasher
index|[
name|functions
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hashers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|hashers
index|[
name|i
index|]
operator|=
name|functions
index|[
name|i
index|]
operator|.
name|newHasher
argument_list|()
expr_stmt|;
block|}
return|return
name|fromHashers
argument_list|(
name|hashers
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newHasher (int expectedInputSize)
specifier|public
name|Hasher
name|newHasher
parameter_list|(
name|int
name|expectedInputSize
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedInputSize
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|Hasher
index|[]
name|hashers
init|=
operator|new
name|Hasher
index|[
name|functions
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|hashers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|hashers
index|[
name|i
index|]
operator|=
name|functions
index|[
name|i
index|]
operator|.
name|newHasher
argument_list|(
name|expectedInputSize
argument_list|)
expr_stmt|;
block|}
return|return
name|fromHashers
argument_list|(
name|hashers
argument_list|)
return|;
block|}
DECL|method|fromHashers (final Hasher[] hashers)
specifier|private
name|Hasher
name|fromHashers
parameter_list|(
specifier|final
name|Hasher
index|[]
name|hashers
parameter_list|)
block|{
return|return
operator|new
name|Hasher
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Hasher
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putByte
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putBytes
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putShort
parameter_list|(
name|short
name|s
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putShort
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putInt
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putLong
parameter_list|(
name|long
name|l
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putLong
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putFloat
parameter_list|(
name|float
name|f
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putFloat
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putDouble
parameter_list|(
name|double
name|d
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putDouble
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putBoolean
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putBoolean
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putChar
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putUnencodedChars
parameter_list|(
name|CharSequence
name|chars
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putUnencodedChars
argument_list|(
name|chars
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|chars
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putString
argument_list|(
name|chars
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Hasher
name|putObject
parameter_list|(
name|T
name|instance
parameter_list|,
name|Funnel
argument_list|<
name|?
super|super
name|T
argument_list|>
name|funnel
parameter_list|)
block|{
for|for
control|(
name|Hasher
name|hasher
range|:
name|hashers
control|)
block|{
name|hasher
operator|.
name|putObject
argument_list|(
name|instance
argument_list|,
name|funnel
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
return|return
name|makeHash
argument_list|(
name|hashers
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
end_class

end_unit

