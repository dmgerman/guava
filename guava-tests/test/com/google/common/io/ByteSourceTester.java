begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|SourceSinkFactory
operator|.
name|ByteSourceFactory
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
name|io
operator|.
name|SourceSinkFactory
operator|.
name|CharSourceFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
name|Charsets
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
name|Optional
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
name|hash
operator|.
name|HashCode
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
name|Hashing
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * A generator of {@code TestSuite} instances for testing {@code ByteSource} implementations.  * Generates tests of a all methods on a {@code ByteSource} given various inputs the source is  * expected to contain as well as as sub-suites for testing the {@code CharSource} view and  * {@code slice()} views in the same way.  *  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|SuppressUnderAndroid
comment|// Android doesn't understand tests that lack default constructors.
DECL|class|ByteSourceTester
specifier|public
class|class
name|ByteSourceTester
extends|extends
name|SourceSinkTester
argument_list|<
name|ByteSource
argument_list|,
name|byte
index|[]
argument_list|,
name|ByteSourceFactory
argument_list|>
block|{
DECL|field|testMethods
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|testMethods
init|=
name|getTestMethods
argument_list|(
name|ByteSourceTester
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|tests (String name, ByteSourceFactory factory, boolean testAsCharSource)
specifier|static
name|TestSuite
name|tests
parameter_list|(
name|String
name|name
parameter_list|,
name|ByteSourceFactory
name|factory
parameter_list|,
name|boolean
name|testAsCharSource
parameter_list|)
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|TEST_STRINGS
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|testAsCharSource
condition|)
block|{
name|suite
operator|.
name|addTest
argument_list|(
name|suiteForString
argument_list|(
name|factory
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|name
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|suite
operator|.
name|addTest
argument_list|(
name|suiteForBytes
argument_list|(
name|factory
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|name
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|suite
return|;
block|}
DECL|method|suiteForString (ByteSourceFactory factory, String string, String name, String desc)
specifier|private
specifier|static
name|TestSuite
name|suiteForString
parameter_list|(
name|ByteSourceFactory
name|factory
parameter_list|,
name|String
name|string
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|)
block|{
name|TestSuite
name|suite
init|=
name|suiteForBytes
argument_list|(
name|factory
argument_list|,
name|string
operator|.
name|getBytes
argument_list|(
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|name
argument_list|,
name|desc
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|CharSourceFactory
name|charSourceFactory
init|=
name|SourceSinkFactories
operator|.
name|asCharSourceFactory
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|CharSourceTester
operator|.
name|suiteForString
argument_list|(
name|charSourceFactory
argument_list|,
name|string
argument_list|,
name|name
operator|+
literal|".asCharSource[Charset]"
argument_list|,
name|desc
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|suiteForBytes (ByteSourceFactory factory, byte[] bytes, String name, String desc, boolean slice)
specifier|private
specifier|static
name|TestSuite
name|suiteForBytes
parameter_list|(
name|ByteSourceFactory
name|factory
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|,
name|boolean
name|slice
parameter_list|)
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
name|name
operator|+
literal|" ["
operator|+
name|desc
operator|+
literal|"]"
argument_list|)
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|testMethods
control|)
block|{
name|suite
operator|.
name|addTest
argument_list|(
operator|new
name|ByteSourceTester
argument_list|(
name|factory
argument_list|,
name|bytes
argument_list|,
name|name
argument_list|,
name|desc
argument_list|,
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|slice
operator|&&
name|bytes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
comment|// test a random slice() of the ByteSource
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|byte
index|[]
name|expected
init|=
name|factory
operator|.
name|getExpected
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
comment|// if expected.length == 0, off has to be 0 but length doesn't matter--result will be empty
name|int
name|off
init|=
name|expected
operator|.
name|length
operator|==
literal|0
condition|?
literal|0
else|:
name|random
operator|.
name|nextInt
argument_list|(
name|expected
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|expected
operator|.
name|length
operator|==
literal|0
condition|?
literal|4
else|:
name|random
operator|.
name|nextInt
argument_list|(
name|expected
operator|.
name|length
operator|-
name|off
argument_list|)
decl_stmt|;
name|ByteSourceFactory
name|sliced
init|=
name|SourceSinkFactories
operator|.
name|asSlicedByteSourceFactory
argument_list|(
name|factory
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|suiteForBytes
argument_list|(
name|sliced
argument_list|,
name|bytes
argument_list|,
name|name
operator|+
literal|".slice[long, long]"
argument_list|,
name|desc
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
comment|// test a slice() of the ByteSource starting at a random offset with a length of
comment|// Long.MAX_VALUE
name|ByteSourceFactory
name|slicedLongMaxValue
init|=
name|SourceSinkFactories
operator|.
name|asSlicedByteSourceFactory
argument_list|(
name|factory
argument_list|,
name|off
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|suiteForBytes
argument_list|(
name|slicedLongMaxValue
argument_list|,
name|bytes
argument_list|,
name|name
operator|+
literal|".slice[long, Long.MAX_VALUE]"
argument_list|,
name|desc
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|suite
return|;
block|}
DECL|field|source
specifier|private
name|ByteSource
name|source
decl_stmt|;
DECL|method|ByteSourceTester (ByteSourceFactory factory, byte[] bytes, String suiteName, String caseDesc, Method method)
specifier|public
name|ByteSourceTester
parameter_list|(
name|ByteSourceFactory
name|factory
parameter_list|,
name|byte
index|[]
name|bytes
parameter_list|,
name|String
name|suiteName
parameter_list|,
name|String
name|caseDesc
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|factory
argument_list|,
name|bytes
argument_list|,
name|suiteName
argument_list|,
name|caseDesc
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|source
operator|=
name|factory
operator|.
name|createSource
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
DECL|method|testOpenStream ()
specifier|public
name|void
name|testOpenStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|source
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|readBytes
init|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertExpectedBytes
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testOpenBufferedStream ()
specifier|public
name|void
name|testOpenBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|source
operator|.
name|openBufferedStream
argument_list|()
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|readBytes
init|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertExpectedBytes
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testRead ()
specifier|public
name|void
name|testRead
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|readBytes
init|=
name|source
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertExpectedBytes
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_outputStream ()
specifier|public
name|void
name|testCopyTo_outputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|source
operator|.
name|copyTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertExpectedBytes
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_byteSink ()
specifier|public
name|void
name|testCopyTo_byteSink
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|// HERESY! but it's ok just for this I guess
name|source
operator|.
name|copyTo
argument_list|(
operator|new
name|ByteSink
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|OutputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|out
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertExpectedBytes
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
operator|==
literal|0
argument_list|,
name|source
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
argument_list|,
name|source
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSizeIfKnown ()
specifier|public
name|void
name|testSizeIfKnown
parameter_list|()
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|Long
argument_list|>
name|sizeIfKnown
init|=
name|source
operator|.
name|sizeIfKnown
argument_list|()
decl_stmt|;
if|if
condition|(
name|sizeIfKnown
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
argument_list|,
operator|(
name|long
operator|)
name|sizeIfKnown
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testContentEquals ()
specifier|public
name|void
name|testContentEquals
parameter_list|()
throws|throws
name|IOException
block|{
name|assertTrue
argument_list|(
name|source
operator|.
name|contentEquals
argument_list|(
operator|new
name|ByteSource
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|InputStream
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|RandomAmountInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|expected
argument_list|)
argument_list|,
operator|new
name|Random
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testRead_usingByteProcessor ()
specifier|public
name|void
name|testRead_usingByteProcessor
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|readBytes
init|=
name|source
operator|.
name|read
argument_list|(
operator|new
name|ByteProcessor
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|()
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processBytes
parameter_list|(
name|byte
index|[]
name|buf
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
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getResult
parameter_list|()
block|{
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExpectedBytes
argument_list|(
name|readBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|testHash ()
specifier|public
name|void
name|testHash
parameter_list|()
throws|throws
name|IOException
block|{
name|HashCode
name|expectedHash
init|=
name|Hashing
operator|.
name|md5
argument_list|()
operator|.
name|hashBytes
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedHash
argument_list|,
name|source
operator|.
name|hash
argument_list|(
name|Hashing
operator|.
name|md5
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSlice_illegalArguments ()
specifier|public
name|void
name|testSlice_illegalArguments
parameter_list|()
block|{
try|try
block|{
name|source
operator|.
name|slice
argument_list|(
operator|-
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected IllegalArgumentException for call to slice with offset -1: "
operator|+
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|source
operator|.
name|slice
argument_list|(
literal|0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected IllegalArgumentException for call to slice with length -1: "
operator|+
name|source
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
comment|// Test that you can not expand the readable data in a previously sliced ByteSource.
DECL|method|testSlice_constrainedRange ()
specifier|public
name|void
name|testSlice_constrainedRange
parameter_list|()
throws|throws
name|IOException
block|{
name|long
name|size
init|=
name|source
operator|.
name|read
argument_list|()
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|size
operator|>=
literal|2
condition|)
block|{
name|ByteSource
name|sliced
init|=
name|source
operator|.
name|slice
argument_list|(
literal|1
argument_list|,
name|size
operator|-
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|size
operator|-
literal|2
argument_list|,
name|sliced
operator|.
name|read
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|ByteSource
name|resliced
init|=
name|sliced
operator|.
name|slice
argument_list|(
literal|0
argument_list|,
name|size
operator|-
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|sliced
operator|.
name|contentEquals
argument_list|(
name|resliced
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertExpectedBytes (byte[] readBytes)
specifier|private
name|void
name|assertExpectedBytes
parameter_list|(
name|byte
index|[]
name|readBytes
parameter_list|)
block|{
name|assertArrayEquals
argument_list|(
name|expected
argument_list|,
name|readBytes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

