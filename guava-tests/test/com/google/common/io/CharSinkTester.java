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
name|CharSinkFactory
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
name|Joiner
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
name|BufferedWriter
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
name|Writer
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

begin_comment
comment|/**  * A generator of {@code TestSuite} instances for testing {@code CharSink} implementations.  * Generates tests of a all methods on a {@code CharSink} given various inputs written to it.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|CharSinkTester
specifier|public
class|class
name|CharSinkTester
extends|extends
name|SourceSinkTester
argument_list|<
name|CharSink
argument_list|,
name|String
argument_list|,
name|CharSinkFactory
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
name|CharSinkTester
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|tests (String name, CharSinkFactory factory)
specifier|static
name|TestSuite
name|tests
parameter_list|(
name|String
name|name
parameter_list|,
name|CharSinkFactory
name|factory
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
name|String
name|desc
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|TestSuite
name|stringSuite
init|=
name|suiteForString
argument_list|(
name|name
argument_list|,
name|factory
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|stringSuite
argument_list|)
expr_stmt|;
block|}
return|return
name|suite
return|;
block|}
DECL|method|suiteForString (String name, CharSinkFactory factory, String string, String desc)
specifier|static
name|TestSuite
name|suiteForString
parameter_list|(
name|String
name|name
parameter_list|,
name|CharSinkFactory
name|factory
parameter_list|,
name|String
name|string
parameter_list|,
name|String
name|desc
parameter_list|)
block|{
name|TestSuite
name|stringSuite
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
specifier|final
name|Method
name|method
range|:
name|testMethods
control|)
block|{
name|stringSuite
operator|.
name|addTest
argument_list|(
operator|new
name|CharSinkTester
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
name|stringSuite
return|;
block|}
DECL|field|lines
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|lines
decl_stmt|;
DECL|field|expectedLines
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|expectedLines
decl_stmt|;
DECL|field|sink
specifier|private
name|CharSink
name|sink
decl_stmt|;
DECL|method|CharSinkTester (CharSinkFactory factory, String string, String suiteName, String caseDesc, Method method)
specifier|public
name|CharSinkTester
parameter_list|(
name|CharSinkFactory
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
name|lines
operator|=
name|getLines
argument_list|(
name|string
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
name|sink
operator|=
name|factory
operator|.
name|createSink
argument_list|()
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
name|Writer
name|writer
init|=
name|sink
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertContainsExpectedString
argument_list|()
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
name|BufferedWriter
name|writer
init|=
name|sink
operator|.
name|openBufferedStream
argument_list|()
decl_stmt|;
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertContainsExpectedString
argument_list|()
expr_stmt|;
block|}
DECL|method|testWrite ()
specifier|public
name|void
name|testWrite
parameter_list|()
throws|throws
name|IOException
block|{
name|sink
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertContainsExpectedString
argument_list|()
expr_stmt|;
block|}
DECL|method|testWriteLines_systemDefaultSeparator ()
specifier|public
name|void
name|testWriteLines_systemDefaultSeparator
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|separator
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
name|sink
operator|.
name|writeLines
argument_list|(
name|lines
argument_list|)
expr_stmt|;
name|assertContainsExpectedLines
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
DECL|method|testWriteLines_specificSeparator ()
specifier|public
name|void
name|testWriteLines_specificSeparator
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|separator
init|=
literal|"\r\n"
decl_stmt|;
name|sink
operator|.
name|writeLines
argument_list|(
name|lines
argument_list|,
name|separator
argument_list|)
expr_stmt|;
name|assertContainsExpectedLines
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
DECL|method|assertContainsExpectedString ()
specifier|private
name|void
name|assertContainsExpectedString
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|factory
operator|.
name|getSinkContents
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertContainsExpectedLines (String separator)
specifier|private
name|void
name|assertContainsExpectedLines
parameter_list|(
name|String
name|separator
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|expected
init|=
name|expectedLines
operator|.
name|isEmpty
argument_list|()
condition|?
literal|""
else|:
name|Joiner
operator|.
name|on
argument_list|(
name|separator
argument_list|)
operator|.
name|join
argument_list|(
name|expectedLines
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lines
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// if we wrote any lines in writeLines(), there will be a trailing newline
name|expected
operator|+=
name|separator
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|factory
operator|.
name|getSinkContents
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

