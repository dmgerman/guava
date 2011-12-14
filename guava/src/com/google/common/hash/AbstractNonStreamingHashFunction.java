begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|Throwables
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Skeleton implementation of {@link HashFunction}, appropriate for non-streaming algorithms.  * All the hash computation done using {@linkplain #newHasher()} are delegated to the {@linkplain   * #hashBytes(byte[], int, int)} method.   *   * @author andreou@google.com (Dimitris Andreou)  */
end_comment

begin_class
DECL|class|AbstractNonStreamingHashFunction
specifier|abstract
class|class
name|AbstractNonStreamingHashFunction
implements|implements
name|HashFunction
block|{
annotation|@
name|Override
DECL|method|newHasher ()
specifier|public
name|Hasher
name|newHasher
parameter_list|()
block|{
return|return
operator|new
name|BufferingHasher
argument_list|(
literal|32
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
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|expectedInputSize
operator|>=
literal|0
argument_list|)
expr_stmt|;
return|return
operator|new
name|BufferingHasher
argument_list|(
name|expectedInputSize
argument_list|)
return|;
block|}
comment|/**    * In-memory stream-based implementation of Hasher.      */
DECL|class|BufferingHasher
specifier|private
specifier|final
class|class
name|BufferingHasher
extends|extends
name|AbstractHasher
block|{
DECL|field|stream
specifier|final
name|ExposedByteArrayOutputStream
name|stream
decl_stmt|;
DECL|field|BOTTOM_BYTE
specifier|static
specifier|final
name|int
name|BOTTOM_BYTE
init|=
literal|0xFF
decl_stmt|;
DECL|method|BufferingHasher (int expectedInputSize)
name|BufferingHasher
parameter_list|(
name|int
name|expectedInputSize
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
operator|new
name|ExposedByteArrayOutputStream
argument_list|(
name|expectedInputSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putByte (byte b)
specifier|public
name|Hasher
name|putByte
parameter_list|(
name|byte
name|b
parameter_list|)
block|{
name|stream
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes)
specifier|public
name|Hasher
name|putBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
block|{
name|stream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putBytes (byte[] bytes, int off, int len)
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
name|stream
operator|.
name|write
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
annotation|@
name|Override
DECL|method|putShort (short s)
specifier|public
name|Hasher
name|putShort
parameter_list|(
name|short
name|s
parameter_list|)
block|{
name|stream
operator|.
name|write
argument_list|(
name|s
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
operator|(
name|s
operator|>>>
literal|8
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putInt (int i)
specifier|public
name|Hasher
name|putInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|stream
operator|.
name|write
argument_list|(
name|i
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
operator|(
name|i
operator|>>>
literal|8
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
operator|(
name|i
operator|>>>
literal|16
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
operator|(
name|i
operator|>>>
literal|24
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putLong (long l)
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|64
condition|;
name|i
operator|+=
literal|8
control|)
block|{
name|stream
operator|.
name|write
argument_list|(
call|(
name|byte
call|)
argument_list|(
operator|(
name|l
operator|>>>
name|i
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putChar (char c)
specifier|public
name|Hasher
name|putChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|stream
operator|.
name|write
argument_list|(
name|c
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|write
argument_list|(
operator|(
name|c
operator|>>>
literal|8
operator|)
operator|&
name|BOTTOM_BYTE
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|putObject (T instance, Funnel<? super T> funnel)
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
annotation|@
name|Override
DECL|method|hash ()
specifier|public
name|HashCode
name|hash
parameter_list|()
block|{
return|return
name|hashBytes
argument_list|(
name|stream
operator|.
name|byteArray
argument_list|()
argument_list|,
literal|0
argument_list|,
name|stream
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|// Just to access the byte[] without introducing an unnecessary copy
DECL|class|ExposedByteArrayOutputStream
specifier|private
specifier|static
specifier|final
class|class
name|ExposedByteArrayOutputStream
extends|extends
name|ByteArrayOutputStream
block|{
DECL|method|ExposedByteArrayOutputStream (int expectedInputSize)
name|ExposedByteArrayOutputStream
parameter_list|(
name|int
name|expectedInputSize
parameter_list|)
block|{
name|super
argument_list|(
name|expectedInputSize
argument_list|)
expr_stmt|;
block|}
DECL|method|byteArray ()
name|byte
index|[]
name|byteArray
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
DECL|method|length ()
name|int
name|length
parameter_list|()
block|{
return|return
name|count
return|;
block|}
block|}
block|}
end_class

end_unit

