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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|io
operator|.
name|SourceSinkFactory
operator|.
name|ByteSourceFactory
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
name|io
operator|.
name|SourceSinkFactory
operator|.
name|CharSourceFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|List
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
name|stream
operator|.
name|Stream
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

begin_comment
comment|/**  * A generator of {@code TestSuite} instances for testing {@code CharSource} implementations.  * Generates tests of a all methods on a {@code CharSource} given various inputs the source is  * expected to contain.  *  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|AndroidIncompatible
comment|// Android doesn't understand tests that lack default constructors.
DECL|class|CharSourceTester
specifier|public
class|class
name|CharSourceTester
extends|extends
name|SourceSinkTester
argument_list|<
name|CharSource
argument_list|,
name|String
argument_list|,
name|CharSourceFactory
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
name|CharSourceTester
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|tests (String name, CharSourceFactory factory, boolean testAsByteSource)
specifier|static
name|TestSuite
name|tests
parameter_list|(
name|String
name|name
parameter_list|,
name|CharSourceFactory
name|factory
parameter_list|,
name|boolean
name|testAsByteSource
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
name|testAsByteSource
condition|)
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
else|else
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
block|}
return|return
name|suite
return|;
block|}
DECL|method|suiteForBytes (CharSourceFactory factory, byte[] bytes, String name, String desc, boolean slice)
specifier|static
name|TestSuite
name|suiteForBytes
parameter_list|(
name|CharSourceFactory
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
name|suiteForString
argument_list|(
name|factory
argument_list|,
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
argument_list|,
name|name
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|ByteSourceFactory
name|byteSourceFactory
init|=
name|SourceSinkFactories
operator|.
name|asByteSourceFactory
argument_list|(
name|factory
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|ByteSourceTester
operator|.
name|suiteForBytes
argument_list|(
name|byteSourceFactory
argument_list|,
name|bytes
argument_list|,
name|name
operator|+
literal|".asByteSource[Charset]"
argument_list|,
name|desc
argument_list|,
name|slice
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|suiteForString (CharSourceFactory factory, String string, String name, String desc)
specifier|static
name|TestSuite
name|suiteForString
parameter_list|(
name|CharSourceFactory
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
name|CharSourceTester
argument_list|(
name|factory
argument_list|,
name|string
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
return|return
name|suite
return|;
block|}
DECL|field|expectedLines
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|expectedLines
decl_stmt|;
DECL|field|source
specifier|private
name|CharSource
name|source
decl_stmt|;
DECL|method|CharSourceTester (CharSourceFactory factory, String string, String suiteName, String caseDesc, Method method)
specifier|public
name|CharSourceTester
parameter_list|(
name|CharSourceFactory
name|factory
parameter_list|,
name|String
name|string
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
name|string
argument_list|,
name|suiteName
argument_list|,
name|caseDesc
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|this
operator|.
name|expectedLines
operator|=
name|getLines
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
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
name|Reader
name|reader
init|=
name|source
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|64
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertExpectedString
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOpenBufferedStream ()
specifier|public
name|void
name|testOpenBufferedStream
parameter_list|()
throws|throws
name|IOException
block|{
name|BufferedReader
name|reader
init|=
name|source
operator|.
name|openBufferedStream
argument_list|()
decl_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|64
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertExpectedString
argument_list|(
name|writer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLines ()
specifier|public
name|void
name|testLines
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|Stream
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|source
operator|.
name|lines
argument_list|()
init|)
block|{
name|assertExpectedLines
argument_list|(
name|lines
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCopyTo_appendable ()
specifier|public
name|void
name|testCopyTo_appendable
parameter_list|()
throws|throws
name|IOException
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
argument_list|()
argument_list|,
name|source
operator|.
name|copyTo
argument_list|(
name|builder
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpectedString
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyTo_charSink ()
specifier|public
name|void
name|testCopyTo_charSink
parameter_list|()
throws|throws
name|IOException
block|{
name|TestCharSink
name|sink
init|=
operator|new
name|TestCharSink
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
argument_list|()
argument_list|,
name|source
operator|.
name|copyTo
argument_list|(
name|sink
argument_list|)
argument_list|)
expr_stmt|;
name|assertExpectedString
argument_list|(
name|sink
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRead_toString ()
specifier|public
name|void
name|testRead_toString
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|string
init|=
name|source
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertExpectedString
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadFirstLine ()
specifier|public
name|void
name|testReadFirstLine
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|expectedLines
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertNull
argument_list|(
name|source
operator|.
name|readFirstLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|expectedLines
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|source
operator|.
name|readFirstLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadLines_toList ()
specifier|public
name|void
name|testReadLines_toList
parameter_list|()
throws|throws
name|IOException
block|{
name|assertExpectedLines
argument_list|(
name|source
operator|.
name|readLines
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
name|isEmpty
argument_list|()
argument_list|,
name|source
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLength ()
specifier|public
name|void
name|testLength
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|length
argument_list|()
argument_list|,
name|source
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLengthIfKnown ()
specifier|public
name|void
name|testLengthIfKnown
parameter_list|()
throws|throws
name|IOException
block|{
name|Optional
argument_list|<
name|Long
argument_list|>
name|lengthIfKnown
init|=
name|source
operator|.
name|lengthIfKnown
argument_list|()
decl_stmt|;
if|if
condition|(
name|lengthIfKnown
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
argument_list|()
argument_list|,
operator|(
name|long
operator|)
name|lengthIfKnown
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadLines_withProcessor ()
specifier|public
name|void
name|testReadLines_withProcessor
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|source
operator|.
name|readLines
argument_list|(
operator|new
name|LineProcessor
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
throws|throws
name|IOException
block|{
name|list
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResult
parameter_list|()
block|{
return|return
name|list
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertExpectedLines
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadLines_withProcessor_stopsOnFalse ()
specifier|public
name|void
name|testReadLines_withProcessor_stopsOnFalse
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|source
operator|.
name|readLines
argument_list|(
operator|new
name|LineProcessor
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
throws|throws
name|IOException
block|{
name|list
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getResult
parameter_list|()
block|{
return|return
name|list
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedLines
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|list
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
name|expectedLines
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testForEachLine ()
specifier|public
name|void
name|testForEachLine
parameter_list|()
throws|throws
name|IOException
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
name|source
operator|.
name|forEachLine
argument_list|(
name|builder
operator|::
name|add
argument_list|)
expr_stmt|;
name|assertExpectedLines
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertExpectedString (String string)
specifier|private
name|void
name|assertExpectedString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|string
argument_list|)
expr_stmt|;
block|}
DECL|method|assertExpectedLines (List<String> list)
specifier|private
name|void
name|assertExpectedLines
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedLines
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

