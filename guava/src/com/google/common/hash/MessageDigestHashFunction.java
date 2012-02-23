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
name|checkPositionIndexes
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
name|checkState
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
name|Chars
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Longs
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
name|Shorts
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
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

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_comment
comment|/**  * {@link HashFunction} adapter for {@link MessageDigest}s.  *   * @author kevinb@google.com (Kevin Bourrillion)  * @author andreou@google.com (Dimitris Andreou)  */
end_comment

begin_class
DECL|class|MessageDigestHashFunction
specifier|final
class|class
name|MessageDigestHashFunction
extends|extends
name|AbstractStreamingHashFunction
block|{
DECL|field|algorithmName
specifier|private
specifier|final
name|String
name|algorithmName
decl_stmt|;
DECL|field|bits
specifier|private
specifier|final
name|int
name|bits
decl_stmt|;
DECL|method|MessageDigestHashFunction (String algorithmName)
name|MessageDigestHashFunction
parameter_list|(
name|String
name|algorithmName
parameter_list|)
block|{
name|this
operator|.
name|algorithmName
operator|=
name|algorithmName
expr_stmt|;
name|this
operator|.
name|bits
operator|=
name|getMessageDigest
argument_list|(
name|algorithmName
argument_list|)
operator|.
name|getDigestLength
argument_list|()
operator|*
literal|8
expr_stmt|;
block|}
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
return|return
name|bits
return|;
block|}
DECL|method|getMessageDigest (String algorithmName)
specifier|private
specifier|static
name|MessageDigest
name|getMessageDigest
parameter_list|(
name|String
name|algorithmName
parameter_list|)
block|{
try|try
block|{
return|return
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algorithmName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|newHasher ()
annotation|@
name|Override
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|MessageDigestHasher
argument_list|(
name|getMessageDigest
argument_list|(
name|algorithmName
argument_list|)
argument_list|)
return|;
block|}
DECL|class|MessageDigestHasher
specifier|private
specifier|static
class|class
name|MessageDigestHasher
implements|implements
name|Hasher
block|{
DECL|field|digest
specifier|private
specifier|final
name|MessageDigest
name|digest
decl_stmt|;
DECL|field|scratch
specifier|private
specifier|final
name|ByteBuffer
name|scratch
decl_stmt|;
comment|// lazy convenience
DECL|field|done
specifier|private
name|boolean
name|done
decl_stmt|;
DECL|method|MessageDigestHasher (MessageDigest digest)
specifier|private
name|MessageDigestHasher
parameter_list|(
name|MessageDigest
name|digest
parameter_list|)
block|{
name|this
operator|.
name|digest
operator|=
name|digest
expr_stmt|;
name|this
operator|.
name|scratch
operator|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|8
argument_list|)
operator|.
name|order
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
expr_stmt|;
block|}
DECL|method|putByte (byte b)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putBytes (byte[] bytes)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putBytes (byte[] bytes, int off, int len)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|checkPositionIndexes
argument_list|(
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|bytes
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putShort (short s)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putShort
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|Shorts
operator|.
name|BYTES
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putInt (int i)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putInt
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|Ints
operator|.
name|BYTES
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putLong (long l)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putLong
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|Longs
operator|.
name|BYTES
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putFloat (float f)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putFloat
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putDouble (double d)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putDouble
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putBoolean (boolean b)
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
return|return
name|putByte
argument_list|(
name|b
condition|?
operator|(
name|byte
operator|)
literal|1
else|:
operator|(
name|byte
operator|)
literal|0
argument_list|)
return|;
block|}
DECL|method|putChar (char c)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|scratch
operator|.
name|putChar
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|digest
operator|.
name|update
argument_list|(
name|scratch
operator|.
name|array
argument_list|()
argument_list|,
literal|0
argument_list|,
name|Chars
operator|.
name|BYTES
argument_list|)
expr_stmt|;
name|scratch
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|putString (CharSequence charSequence)
annotation|@
name|Override
specifier|public
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|charSequence
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|putChar
argument_list|(
name|charSequence
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|putString (CharSequence charSequence, Charset charset)
annotation|@
name|Override
specifier|public
name|Hasher
name|putString
parameter_list|(
name|CharSequence
name|charSequence
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
return|return
name|putBytes
argument_list|(
name|charSequence
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
DECL|method|putObject (T instance, Funnel<? super T> funnel)
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
name|checkNotDone
argument_list|()
expr_stmt|;
name|funnel
operator|.
name|funnel
argument_list|(
name|instance
argument_list|,
name|this
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|checkNotDone ()
specifier|private
name|void
name|checkNotDone
parameter_list|()
block|{
name|checkState
argument_list|(
operator|!
name|done
argument_list|,
literal|"Cannot use Hasher after calling #hash() on it"
argument_list|)
expr_stmt|;
block|}
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
name|done
operator|=
literal|true
expr_stmt|;
return|return
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|digest
operator|.
name|digest
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

