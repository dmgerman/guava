begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|BaseEncoding
operator|.
name|base16
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
name|ImmutableList
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
name|testing
operator|.
name|ClassSanityTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Tests for HashCodes, especially making sure that their endianness promises (big-endian)  * are upheld.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|HashCodesTest
specifier|public
class|class
name|HashCodesTest
extends|extends
name|TestCase
block|{
comment|// note: asInt(), asLong() are in little endian
DECL|field|expectedHashCodes
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|ExpectedHashCode
argument_list|>
name|expectedHashCodes
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|ExpectedHashCode
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xef
block|,
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x89
block|,
operator|(
name|byte
operator|)
literal|0x67
block|,
operator|(
name|byte
operator|)
literal|0x45
block|,
operator|(
name|byte
operator|)
literal|0x23
block|,
operator|(
name|byte
operator|)
literal|0x01
block|}
argument_list|,
literal|0x89abcdef
argument_list|,
literal|0x0123456789abcdefL
argument_list|,
literal|"efcdab8967452301"
argument_list|)
argument_list|,
operator|new
name|ExpectedHashCode
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xef
block|,
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x89
block|,
operator|(
name|byte
operator|)
literal|0x67
block|,
operator|(
name|byte
operator|)
literal|0x45
block|,
operator|(
name|byte
operator|)
literal|0x23
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
comment|// up to here, same bytes as above
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x02
block|,
operator|(
name|byte
operator|)
literal|0x03
block|,
operator|(
name|byte
operator|)
literal|0x04
block|,
operator|(
name|byte
operator|)
literal|0x05
block|,
operator|(
name|byte
operator|)
literal|0x06
block|,
operator|(
name|byte
operator|)
literal|0x07
block|,
operator|(
name|byte
operator|)
literal|0x08
block|}
argument_list|,
literal|0x89abcdef
argument_list|,
literal|0x0123456789abcdefL
argument_list|,
comment|// asInt/asLong as above, due to equal eight first bytes
literal|"efcdab89674523010102030405060708"
argument_list|)
argument_list|,
operator|new
name|ExpectedHashCode
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xdf
block|,
operator|(
name|byte
operator|)
literal|0x9b
block|,
operator|(
name|byte
operator|)
literal|0x57
block|,
operator|(
name|byte
operator|)
literal|0x13
block|}
argument_list|,
literal|0x13579bdf
argument_list|,
literal|null
argument_list|,
literal|"df9b5713"
argument_list|)
argument_list|,
operator|new
name|ExpectedHashCode
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
argument_list|,
literal|0x0000abcd
argument_list|,
literal|null
argument_list|,
literal|"cdab0000"
argument_list|)
argument_list|,
operator|new
name|ExpectedHashCode
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xef
block|,
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
argument_list|,
literal|0x00abcdef
argument_list|,
literal|0x0000000000abcdefL
argument_list|,
literal|"efcdab0000000000"
argument_list|)
argument_list|)
decl_stmt|;
comment|// expectedHashCodes must contain at least one hash code with 4 bytes
DECL|method|testFromInt ()
specifier|public
name|void
name|testFromInt
parameter_list|()
block|{
for|for
control|(
name|ExpectedHashCode
name|expected
range|:
name|expectedHashCodes
control|)
block|{
if|if
condition|(
name|expected
operator|.
name|bytes
operator|.
name|length
operator|==
literal|4
condition|)
block|{
name|HashCode
name|fromInt
init|=
name|HashCodes
operator|.
name|fromInt
argument_list|(
name|expected
operator|.
name|asInt
argument_list|)
decl_stmt|;
name|assertExpectedHashCode
argument_list|(
name|expected
argument_list|,
name|fromInt
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// expectedHashCodes must contain at least one hash code with 8 bytes
DECL|method|testFromLong ()
specifier|public
name|void
name|testFromLong
parameter_list|()
block|{
for|for
control|(
name|ExpectedHashCode
name|expected
range|:
name|expectedHashCodes
control|)
block|{
if|if
condition|(
name|expected
operator|.
name|bytes
operator|.
name|length
operator|==
literal|8
condition|)
block|{
name|HashCode
name|fromLong
init|=
name|HashCodes
operator|.
name|fromLong
argument_list|(
name|expected
operator|.
name|asLong
argument_list|)
decl_stmt|;
name|assertExpectedHashCode
argument_list|(
name|expected
argument_list|,
name|fromLong
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testFromBytes ()
specifier|public
name|void
name|testFromBytes
parameter_list|()
block|{
for|for
control|(
name|ExpectedHashCode
name|expected
range|:
name|expectedHashCodes
control|)
block|{
name|HashCode
name|fromBytes
init|=
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|expected
operator|.
name|bytes
argument_list|)
decl_stmt|;
name|assertExpectedHashCode
argument_list|(
name|expected
argument_list|,
name|fromBytes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testFromBytes_copyOccurs ()
specifier|public
name|void
name|testFromBytes_copyOccurs
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
decl_stmt|;
name|HashCode
name|hashCode
init|=
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|int
name|expectedInt
init|=
literal|0x0000abcd
decl_stmt|;
name|String
name|expectedToString
init|=
literal|"cdab0000"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedInt
argument_list|,
name|hashCode
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedToString
argument_list|,
name|hashCode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|bytes
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
literal|0x00
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedInt
argument_list|,
name|hashCode
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedToString
argument_list|,
name|hashCode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromBytesNoCopy_noCopyOccurs ()
specifier|public
name|void
name|testFromBytesNoCopy_noCopyOccurs
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xcd
block|,
operator|(
name|byte
operator|)
literal|0xab
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
decl_stmt|;
name|HashCode
name|hashCode
init|=
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0x0000abcd
argument_list|,
name|hashCode
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cdab0000"
argument_list|,
name|hashCode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|bytes
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
literal|0x00
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0000ab00
argument_list|,
name|hashCode
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"00ab0000"
argument_list|,
name|hashCode
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPadToLong ()
specifier|public
name|void
name|testPadToLong
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0x1111111111111111L
argument_list|,
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|0x1111111111111111L
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x9999999999999999L
argument_list|,
name|HashCodes
operator|.
name|fromLong
argument_list|(
literal|0x9999999999999999L
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0000000011111111L
argument_list|,
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0x11111111
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0000000099999999L
argument_list|,
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0x99999999
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPadToLongWith4Bytes ()
specifier|public
name|void
name|testPadToLongWith4Bytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0x0000000099999999L
argument_list|,
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|byteArrayWith9s
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPadToLongWith6Bytes ()
specifier|public
name|void
name|testPadToLongWith6Bytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0x0000999999999999L
argument_list|,
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|byteArrayWith9s
argument_list|(
literal|6
argument_list|)
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPadToLongWith8Bytes ()
specifier|public
name|void
name|testPadToLongWith8Bytes
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0x9999999999999999L
argument_list|,
name|HashCodes
operator|.
name|fromBytesNoCopy
argument_list|(
name|byteArrayWith9s
argument_list|(
literal|8
argument_list|)
argument_list|)
operator|.
name|padToLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|byteArrayWith9s (int size)
specifier|private
specifier|static
name|byte
index|[]
name|byteArrayWith9s
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|byte
index|[]
name|bytez
init|=
operator|new
name|byte
index|[
name|size
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|bytez
argument_list|,
operator|(
name|byte
operator|)
literal|0x99
argument_list|)
expr_stmt|;
return|return
name|bytez
return|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|byte
index|[]
name|data
init|=
operator|new
name|byte
index|[]
block|{
literal|127
block|,
operator|-
literal|128
block|,
literal|5
block|,
operator|-
literal|1
block|,
literal|14
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|"7f8005ff0e"
argument_list|,
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|data
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"7f8005ff0e"
argument_list|,
name|base16
argument_list|()
operator|.
name|lowerCase
argument_list|()
operator|.
name|encode
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHashCodes_nulls ()
specifier|public
name|void
name|testHashCodes_nulls
parameter_list|()
throws|throws
name|Exception
block|{
name|sanityTester
argument_list|()
operator|.
name|testNulls
argument_list|()
expr_stmt|;
block|}
DECL|method|testHashCodes_equalsAndSerializable ()
specifier|public
name|void
name|testHashCodes_equalsAndSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|sanityTester
argument_list|()
operator|.
name|testEqualsAndSerializable
argument_list|()
expr_stmt|;
block|}
DECL|method|sanityTester ()
specifier|private
specifier|static
name|ClassSanityTester
operator|.
name|FactoryMethodReturnValueTester
name|sanityTester
parameter_list|()
block|{
return|return
operator|new
name|ClassSanityTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|)
operator|.
name|setSampleInstances
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|}
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|)
argument_list|)
operator|.
name|forAllPublicStaticMethods
argument_list|(
name|HashCodes
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|assertExpectedHashCode (ExpectedHashCode expected, HashCode hash)
specifier|private
specifier|static
name|void
name|assertExpectedHashCode
parameter_list|(
name|ExpectedHashCode
name|expected
parameter_list|,
name|HashCode
name|hash
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
operator|.
name|bytes
argument_list|,
name|hash
operator|.
name|asBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bb
init|=
operator|new
name|byte
index|[
name|hash
operator|.
name|bits
argument_list|()
operator|/
literal|8
index|]
decl_stmt|;
name|hash
operator|.
name|writeBytesTo
argument_list|(
name|bb
argument_list|,
literal|0
argument_list|,
name|bb
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|expected
operator|.
name|bytes
argument_list|,
name|bb
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|asInt
argument_list|,
name|hash
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|expected
operator|.
name|asLong
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|hash
operator|.
name|asLong
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ok
parameter_list|)
block|{}
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|asLong
operator|.
name|longValue
argument_list|()
argument_list|,
name|hash
operator|.
name|asLong
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
operator|.
name|toString
argument_list|,
name|hash
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertSideEffectFree
argument_list|(
name|hash
argument_list|)
expr_stmt|;
name|assertReadableBytes
argument_list|(
name|hash
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSideEffectFree (HashCode hash)
specifier|private
specifier|static
name|void
name|assertSideEffectFree
parameter_list|(
name|HashCode
name|hash
parameter_list|)
block|{
name|byte
index|[]
name|original
init|=
name|hash
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|byte
index|[]
name|mutated
init|=
name|hash
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|mutated
index|[
literal|0
index|]
operator|++
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|original
argument_list|,
name|hash
operator|.
name|asBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertReadableBytes (HashCode hashCode)
specifier|private
specifier|static
name|void
name|assertReadableBytes
parameter_list|(
name|HashCode
name|hashCode
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|hashCode
operator|.
name|bits
argument_list|()
operator|>=
literal|32
argument_list|)
expr_stmt|;
comment|// sanity
name|byte
index|[]
name|hashBytes
init|=
name|hashCode
operator|.
name|asBytes
argument_list|()
decl_stmt|;
name|int
name|totalBytes
init|=
name|hashCode
operator|.
name|bits
argument_list|()
operator|/
literal|8
decl_stmt|;
for|for
control|(
name|int
name|bytes
init|=
literal|0
init|;
name|bytes
operator|<
name|totalBytes
condition|;
name|bytes
operator|++
control|)
block|{
name|byte
index|[]
name|bb
init|=
operator|new
name|byte
index|[
name|bytes
index|]
decl_stmt|;
name|hashCode
operator|.
name|writeBytesTo
argument_list|(
name|bb
argument_list|,
literal|0
argument_list|,
name|bb
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|Arrays
operator|.
name|copyOf
argument_list|(
name|hashBytes
argument_list|,
name|bytes
argument_list|)
argument_list|,
name|bb
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExpectedHashCode
specifier|private
specifier|static
class|class
name|ExpectedHashCode
block|{
DECL|field|bytes
specifier|final
name|byte
index|[]
name|bytes
decl_stmt|;
DECL|field|asInt
specifier|final
name|int
name|asInt
decl_stmt|;
DECL|field|asLong
specifier|final
name|Long
name|asLong
decl_stmt|;
comment|// null means that asLong should throw an exception
DECL|field|toString
specifier|final
name|String
name|toString
decl_stmt|;
DECL|method|ExpectedHashCode (byte[] bytes, int asInt, Long asLong, String toString)
name|ExpectedHashCode
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|int
name|asInt
parameter_list|,
name|Long
name|asLong
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|bytes
operator|=
name|bytes
expr_stmt|;
name|this
operator|.
name|asInt
operator|=
name|asInt
expr_stmt|;
name|this
operator|.
name|asLong
operator|=
name|asLong
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

