begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|Beta
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
name|primitives
operator|.
name|Longs
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterOutputStream
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * An implementation of {@link DataOutput} that uses little-endian byte ordering  * for writing {@code char}, {@code short}, {@code int}, {@code float}, {@code  * double}, and {@code long} values.  *<p>  *<b>Note:</b> This class intentionally violates the specification of its  * supertype {@code DataOutput}, which explicitly requires big-endian byte  * order.  *  * @author Chris Nokleberg  * @author Keith Bottner  * @since 8.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|LittleEndianDataOutputStream
specifier|public
class|class
name|LittleEndianDataOutputStream
extends|extends
name|FilterOutputStream
implements|implements
name|DataOutput
block|{
comment|/**    * Creates a {@code LittleEndianDataOutputStream} that wraps the given stream.    *    * @param out the stream to delegate to    */
DECL|method|LittleEndianDataOutputStream (OutputStream out)
specifier|public
name|LittleEndianDataOutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|DataOutputStream
argument_list|(
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|out
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|write (byte[] b, int off, int len)
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Override slow FilterOutputStream impl
name|out
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
DECL|method|writeBoolean (boolean v)
annotation|@
name|Override
specifier|public
name|void
name|writeBoolean
parameter_list|(
name|boolean
name|v
parameter_list|)
throws|throws
name|IOException
block|{
operator|(
operator|(
name|DataOutputStream
operator|)
name|out
operator|)
operator|.
name|writeBoolean
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|writeByte (int v)
annotation|@
name|Override
specifier|public
name|void
name|writeByte
parameter_list|(
name|int
name|v
parameter_list|)
throws|throws
name|IOException
block|{
operator|(
operator|(
name|DataOutputStream
operator|)
name|out
operator|)
operator|.
name|writeByte
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
comment|/**    * @deprecated The semantics of {@code writeBytes(String s)} are considered    *             dangerous. Please use {@link #writeUTF(String s)},    *             {@link #writeChars(String s)} or another write method instead.    */
annotation|@
name|Deprecated
DECL|method|writeBytes (String s)
annotation|@
name|Override
specifier|public
name|void
name|writeBytes
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|IOException
block|{
operator|(
operator|(
name|DataOutputStream
operator|)
name|out
operator|)
operator|.
name|writeBytes
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes a char as specified by {@link DataOutputStream#writeChar(int)},    * except using little-endian byte order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeChar (int v)
annotation|@
name|Override
specifier|public
name|void
name|writeChar
parameter_list|(
name|int
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|writeShort
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes a {@code String} as specified by    * {@link DataOutputStream#writeChars(String)}, except each character is    * written using little-endian byte order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeChars (String s)
annotation|@
name|Override
specifier|public
name|void
name|writeChars
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|IOException
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
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|writeChar
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Writes a {@code double} as specified by    * {@link DataOutputStream#writeDouble(double)}, except using little-endian    * byte order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeDouble (double v)
annotation|@
name|Override
specifier|public
name|void
name|writeDouble
parameter_list|(
name|double
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|writeLong
argument_list|(
name|Double
operator|.
name|doubleToLongBits
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes a {@code float} as specified by    * {@link DataOutputStream#writeFloat(float)}, except using little-endian byte    * order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeFloat (float v)
annotation|@
name|Override
specifier|public
name|void
name|writeFloat
parameter_list|(
name|float
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|writeInt
argument_list|(
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes an {@code int} as specified by    * {@link DataOutputStream#writeInt(int)}, except using little-endian byte    * order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeInt (int v)
annotation|@
name|Override
specifier|public
name|void
name|writeInt
parameter_list|(
name|int
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
name|v
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
operator|(
name|v
operator|>>
literal|8
operator|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
operator|(
name|v
operator|>>
literal|16
operator|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
operator|(
name|v
operator|>>
literal|24
operator|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes a {@code long} as specified by    * {@link DataOutputStream#writeLong(long)}, except using little-endian byte    * order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeLong (long v)
annotation|@
name|Override
specifier|public
name|void
name|writeLong
parameter_list|(
name|long
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bytes
init|=
name|Longs
operator|.
name|toByteArray
argument_list|(
name|Long
operator|.
name|reverseBytes
argument_list|(
name|v
argument_list|)
argument_list|)
decl_stmt|;
name|write
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**    * Writes a {@code short} as specified by    * {@link DataOutputStream#writeShort(int)}, except using little-endian byte    * order.    *    * @throws IOException if an I/O error occurs    */
DECL|method|writeShort (int v)
annotation|@
name|Override
specifier|public
name|void
name|writeShort
parameter_list|(
name|int
name|v
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
name|v
argument_list|)
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|0xFF
operator|&
operator|(
name|v
operator|>>
literal|8
operator|)
argument_list|)
expr_stmt|;
block|}
DECL|method|writeUTF (String str)
annotation|@
name|Override
specifier|public
name|void
name|writeUTF
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|IOException
block|{
operator|(
operator|(
name|DataOutputStream
operator|)
name|out
operator|)
operator|.
name|writeUTF
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
comment|// Overriding close() because FilterOutputStream's close() method pre-JDK8 has bad behavior:
comment|// it silently ignores any exception thrown by flush(). Instead, just close the delegate stream.
comment|// It should flush itself if necessary.
DECL|method|close ()
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

