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
name|collect
operator|.
name|Iterables
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
name|Lists
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
name|hash
operator|.
name|AbstractStreamingHashFunction
operator|.
name|AbstractStreamingHasher
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
name|hash
operator|.
name|HashTestUtils
operator|.
name|RandomHasherAction
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
name|io
operator|.
name|ByteArrayOutputStream
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Tests for AbstractHashSink.  *   * @author andreou@google.com (Dimitris Andreou)  */
end_comment

begin_class
DECL|class|AbstractStreamingHasherTest
specifier|public
class|class
name|AbstractStreamingHasherTest
extends|extends
name|TestCase
block|{
comment|/** Test we get the HashCode that is created by the sink. Later we ignore the result */
DECL|method|testSanity ()
specifier|public
name|void
name|testSanity
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0xDeadBeef
argument_list|,
name|sink
operator|.
name|makeHash
argument_list|()
operator|.
name|asInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBytes ()
specifier|public
name|void
name|testBytes
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
comment|// byte order insignificant here
name|byte
index|[]
name|expected
init|=
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
decl_stmt|;
name|sink
operator|.
name|putByte
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
expr_stmt|;
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|2
block|,
literal|3
block|,
literal|4
block|,
literal|5
block|,
literal|6
block|}
argument_list|)
expr_stmt|;
name|sink
operator|.
name|putByte
argument_list|(
operator|(
name|byte
operator|)
literal|7
argument_list|)
expr_stmt|;
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[]
block|{ }
argument_list|)
expr_stmt|;
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|8
block|}
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|testShort ()
specifier|public
name|void
name|testShort
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putShort
argument_list|(
operator|(
name|short
operator|)
literal|0x0201
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
comment|// padded with zeros
block|}
DECL|method|testInt ()
specifier|public
name|void
name|testInt
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putInt
argument_list|(
literal|0x04030201
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
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
argument_list|)
expr_stmt|;
block|}
DECL|method|testLong ()
specifier|public
name|void
name|testLong
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putLong
argument_list|(
literal|0x0807060504030201L
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
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
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testChar ()
specifier|public
name|void
name|testChar
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putChar
argument_list|(
operator|(
name|char
operator|)
literal|0x0201
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
expr_stmt|;
comment|// padded with zeros
block|}
DECL|method|testFloat ()
specifier|public
name|void
name|testFloat
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putFloat
argument_list|(
name|Float
operator|.
name|intBitsToFloat
argument_list|(
literal|0x04030201
argument_list|)
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
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
argument_list|)
expr_stmt|;
block|}
DECL|method|testDouble ()
specifier|public
name|void
name|testDouble
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|sink
operator|.
name|putDouble
argument_list|(
name|Double
operator|.
name|longBitsToDouble
argument_list|(
literal|0x0807060504030201L
argument_list|)
argument_list|)
expr_stmt|;
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
name|sink
operator|.
name|assertInvariants
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
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
block|,
literal|5
block|,
literal|6
block|,
literal|7
block|,
literal|8
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testCorrectExceptions ()
specifier|public
name|void
name|testCorrectExceptions
parameter_list|()
block|{
name|Sink
name|sink
init|=
operator|new
name|Sink
argument_list|(
literal|4
argument_list|)
decl_stmt|;
try|try
block|{
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[
literal|8
index|]
argument_list|,
operator|-
literal|1
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|ok
parameter_list|)
block|{}
try|try
block|{
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[
literal|8
index|]
argument_list|,
literal|0
argument_list|,
literal|16
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|ok
parameter_list|)
block|{}
try|try
block|{
name|sink
operator|.
name|putBytes
argument_list|(
operator|new
name|byte
index|[
literal|8
index|]
argument_list|,
literal|0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|ok
parameter_list|)
block|{}
block|}
comment|/**    * This test creates a long random sequence of inputs, then a lot of differently configured    * sinks process it; all should produce the same answer, the only difference should be the    * number of process()/processRemaining() invocations, due to alignment.      */
DECL|method|testExhaustive ()
specifier|public
name|void
name|testExhaustive
parameter_list|()
throws|throws
name|Exception
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// will iteratively make more debuggable, each time it breaks
for|for
control|(
name|int
name|totalInsertions
init|=
literal|0
init|;
name|totalInsertions
operator|<
literal|200
condition|;
name|totalInsertions
operator|++
control|)
block|{
name|List
argument_list|<
name|Sink
argument_list|>
name|sinks
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|chunkSize
init|=
literal|4
init|;
name|chunkSize
operator|<=
literal|32
condition|;
name|chunkSize
operator|++
control|)
block|{
for|for
control|(
name|int
name|bufferSize
init|=
name|chunkSize
init|;
name|bufferSize
operator|<=
name|chunkSize
operator|*
literal|4
condition|;
name|bufferSize
operator|+=
name|chunkSize
control|)
block|{
comment|// yes, that's a lot of sinks!
name|sinks
operator|.
name|add
argument_list|(
operator|new
name|Sink
argument_list|(
name|chunkSize
argument_list|,
name|bufferSize
argument_list|)
argument_list|)
expr_stmt|;
comment|// For convenience, testing only with big endianness, to match DataOutputStream.
comment|// I regard highly unlikely that both the little endianness tests above and this one
comment|// passes, and there is still a little endianness bug lurking around.
block|}
block|}
name|Control
name|control
init|=
operator|new
name|Control
argument_list|()
decl_stmt|;
name|Hasher
name|controlSink
init|=
name|control
operator|.
name|newHasher
argument_list|(
literal|1024
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|Hasher
argument_list|>
name|sinksAndControl
init|=
name|Iterables
operator|.
name|concat
argument_list|(
name|sinks
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|controlSink
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|insertion
init|=
literal|0
init|;
name|insertion
operator|<
name|totalInsertions
condition|;
name|insertion
operator|++
control|)
block|{
name|RandomHasherAction
operator|.
name|pickAtRandom
argument_list|(
name|random
argument_list|)
operator|.
name|performAction
argument_list|(
name|random
argument_list|,
name|sinksAndControl
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Sink
name|sink
range|:
name|sinks
control|)
block|{
name|sink
operator|.
name|hash
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|expected
init|=
name|controlSink
operator|.
name|hash
argument_list|()
operator|.
name|asBytes
argument_list|()
decl_stmt|;
for|for
control|(
name|Sink
name|sink
range|:
name|sinks
control|)
block|{
name|sink
operator|.
name|assertInvariants
argument_list|(
name|expected
operator|.
name|length
argument_list|)
expr_stmt|;
name|sink
operator|.
name|assertBytes
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|Sink
specifier|private
specifier|static
class|class
name|Sink
extends|extends
name|AbstractStreamingHasher
block|{
DECL|field|chunkSize
specifier|final
name|int
name|chunkSize
decl_stmt|;
DECL|field|bufferSize
specifier|final
name|int
name|bufferSize
decl_stmt|;
DECL|field|out
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
DECL|field|processCalled
name|int
name|processCalled
init|=
literal|0
decl_stmt|;
DECL|field|remainingCalled
name|boolean
name|remainingCalled
init|=
literal|false
decl_stmt|;
DECL|method|Sink (int chunkSize, int bufferSize)
name|Sink
parameter_list|(
name|int
name|chunkSize
parameter_list|,
name|int
name|bufferSize
parameter_list|)
block|{
name|super
argument_list|(
name|chunkSize
argument_list|,
name|bufferSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|chunkSize
operator|=
name|chunkSize
expr_stmt|;
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
expr_stmt|;
block|}
DECL|method|Sink (int chunkSize)
name|Sink
parameter_list|(
name|int
name|chunkSize
parameter_list|)
block|{
name|super
argument_list|(
name|chunkSize
argument_list|)
expr_stmt|;
name|this
operator|.
name|chunkSize
operator|=
name|chunkSize
expr_stmt|;
name|this
operator|.
name|bufferSize
operator|=
name|chunkSize
expr_stmt|;
block|}
DECL|method|makeHash ()
annotation|@
name|Override
name|HashCode
name|makeHash
parameter_list|()
block|{
return|return
name|HashCodes
operator|.
name|fromInt
argument_list|(
literal|0xDeadBeef
argument_list|)
return|;
block|}
DECL|method|process (ByteBuffer bb)
annotation|@
name|Override
specifier|protected
name|void
name|process
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
name|processCalled
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|,
name|bb
operator|.
name|order
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bb
operator|.
name|remaining
argument_list|()
operator|>=
name|chunkSize
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chunkSize
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|bb
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processRemaining (ByteBuffer bb)
annotation|@
name|Override
specifier|protected
name|void
name|processRemaining
parameter_list|(
name|ByteBuffer
name|bb
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|remainingCalled
argument_list|)
expr_stmt|;
name|remainingCalled
operator|=
literal|true
expr_stmt|;
name|assertEquals
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|,
name|bb
operator|.
name|order
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bb
operator|.
name|remaining
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bb
operator|.
name|remaining
argument_list|()
operator|<
name|bufferSize
argument_list|)
expr_stmt|;
name|int
name|before
init|=
name|processCalled
decl_stmt|;
name|super
operator|.
name|processRemaining
argument_list|(
name|bb
argument_list|)
expr_stmt|;
name|int
name|after
init|=
name|processCalled
decl_stmt|;
name|assertEquals
argument_list|(
name|before
operator|+
literal|1
argument_list|,
name|after
argument_list|)
expr_stmt|;
comment|// default implementation pads and calls process()
name|processCalled
operator|--
expr_stmt|;
comment|// don't count the tail invocation (makes tests a bit more understandable)
block|}
comment|// ensures that the number of invocations looks sane
DECL|method|assertInvariants (int expectedBytes)
name|void
name|assertInvariants
parameter_list|(
name|int
name|expectedBytes
parameter_list|)
block|{
comment|// we should have seen as many bytes as the next multiple of chunk after expectedBytes - 1
name|assertEquals
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
operator|.
name|length
argument_list|,
name|ceilToMultiple
argument_list|(
name|expectedBytes
argument_list|,
name|chunkSize
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedBytes
operator|/
name|chunkSize
argument_list|,
name|processCalled
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedBytes
operator|%
name|chunkSize
operator|!=
literal|0
argument_list|,
name|remainingCalled
argument_list|)
expr_stmt|;
block|}
comment|// returns the minimum x such as x>= a&& (x % b) == 0
DECL|method|ceilToMultiple (int a, int b)
specifier|private
specifier|static
name|int
name|ceilToMultiple
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|int
name|remainder
init|=
name|a
operator|%
name|b
decl_stmt|;
return|return
name|remainder
operator|==
literal|0
condition|?
name|a
else|:
name|a
operator|+
name|b
operator|-
name|remainder
return|;
block|}
DECL|method|assertBytes (byte[] expected)
name|void
name|assertBytes
parameter_list|(
name|byte
index|[]
name|expected
parameter_list|)
block|{
name|byte
index|[]
name|got
init|=
name|out
operator|.
name|toByteArray
argument_list|()
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
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|expected
index|[
name|i
index|]
argument_list|,
name|got
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|Control
specifier|private
specifier|static
class|class
name|Control
extends|extends
name|AbstractNonStreamingHashFunction
block|{
annotation|@
name|Override
DECL|method|hashBytes (byte[] input)
specifier|public
name|HashCode
name|hashBytes
parameter_list|(
name|byte
index|[]
name|input
parameter_list|)
block|{
return|return
name|HashCodes
operator|.
name|fromBytes
argument_list|(
name|input
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashBytes (byte[] input, int off, int len)
specifier|public
name|HashCode
name|hashBytes
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
return|return
name|hashBytes
argument_list|(
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|input
argument_list|,
name|off
argument_list|,
name|off
operator|+
name|len
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|bits ()
specifier|public
name|int
name|bits
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hashString (CharSequence input)
specifier|public
name|HashCode
name|hashString
parameter_list|(
name|CharSequence
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hashString (CharSequence input, Charset charset)
specifier|public
name|HashCode
name|hashString
parameter_list|(
name|CharSequence
name|input
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|hashLong (long input)
specifier|public
name|HashCode
name|hashLong
parameter_list|(
name|long
name|input
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

