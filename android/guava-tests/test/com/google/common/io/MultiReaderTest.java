begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilterReader
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
name|StringReader
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

begin_comment
comment|/**  * @author ricebin  */
end_comment

begin_class
DECL|class|MultiReaderTest
specifier|public
class|class
name|MultiReaderTest
extends|extends
name|TestCase
block|{
DECL|method|testOnlyOneOpen ()
specifier|public
name|void
name|testOnlyOneOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testString
init|=
literal|"abcdefgh"
decl_stmt|;
specifier|final
name|CharSource
name|source
init|=
name|newCharSource
argument_list|(
name|testString
argument_list|)
decl_stmt|;
specifier|final
name|int
index|[]
name|counter
init|=
operator|new
name|int
index|[
literal|1
index|]
decl_stmt|;
name|CharSource
name|reader
init|=
operator|new
name|CharSource
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Reader
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|counter
index|[
literal|0
index|]
operator|++
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"More than one source open"
argument_list|)
throw|;
block|}
return|return
operator|new
name|FilterReader
argument_list|(
name|source
operator|.
name|openStream
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|counter
index|[
literal|0
index|]
operator|--
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|Reader
name|joinedReader
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|reader
argument_list|,
name|reader
argument_list|,
name|reader
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|CharStreams
operator|.
name|toString
argument_list|(
name|joinedReader
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testString
operator|.
name|length
argument_list|()
operator|*
literal|3
argument_list|,
name|result
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReady ()
specifier|public
name|void
name|testReady
parameter_list|()
throws|throws
name|Exception
block|{
name|CharSource
name|source
init|=
name|newCharSource
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|?
extends|extends
name|CharSource
argument_list|>
name|list
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|source
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|Reader
name|joinedReader
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|list
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|joinedReader
operator|.
name|ready
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'a'
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'a'
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|joinedReader
operator|.
name|ready
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimple ()
specifier|public
name|void
name|testSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|testString
init|=
literal|"abcdefgh"
decl_stmt|;
name|CharSource
name|source
init|=
name|newCharSource
argument_list|(
name|testString
argument_list|)
decl_stmt|;
name|Reader
name|joinedReader
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|source
argument_list|,
name|source
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|String
name|expectedString
init|=
name|testString
operator|+
name|testString
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedString
argument_list|,
name|CharStreams
operator|.
name|toString
argument_list|(
name|joinedReader
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|newCharSource (final String text)
specifier|private
specifier|static
name|CharSource
name|newCharSource
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|CharSource
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Reader
name|openStream
parameter_list|()
block|{
return|return
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|testSkip ()
specifier|public
name|void
name|testSkip
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|begin
init|=
literal|"abcde"
decl_stmt|;
name|String
name|end
init|=
literal|"fghij"
decl_stmt|;
name|Reader
name|joinedReader
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|newCharSource
argument_list|(
name|begin
argument_list|)
argument_list|,
name|newCharSource
argument_list|(
name|end
argument_list|)
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|String
name|expected
init|=
name|begin
operator|+
name|end
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|joinedReader
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|charAt
argument_list|(
literal|2
argument_list|)
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|joinedReader
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|charAt
argument_list|(
literal|7
argument_list|)
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|CharStreams
operator|.
name|skipFully
argument_list|(
name|joinedReader
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|charAt
argument_list|(
literal|9
argument_list|)
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkipZero ()
specifier|public
name|void
name|testSkipZero
parameter_list|()
throws|throws
name|Exception
block|{
name|CharSource
name|source
init|=
name|newCharSource
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|Iterable
argument_list|<
name|CharSource
argument_list|>
name|list
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|source
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|Reader
name|joinedReader
init|=
name|CharSource
operator|.
name|concat
argument_list|(
name|list
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|joinedReader
operator|.
name|skip
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'a'
argument_list|,
name|joinedReader
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
