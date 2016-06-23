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
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Flushable
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

begin_comment
comment|/**  * Unit test for {@link AppendableWriter}.  *  * @author Alan Green  */
end_comment

begin_class
DECL|class|AppendableWriterTest
specifier|public
class|class
name|AppendableWriterTest
extends|extends
name|IoTestCase
block|{
comment|/** Helper class for testing behavior with Flushable and Closeable targets. */
DECL|class|SpyAppendable
specifier|private
specifier|static
class|class
name|SpyAppendable
implements|implements
name|Appendable
implements|,
name|Flushable
implements|,
name|Closeable
block|{
DECL|field|flushed
name|boolean
name|flushed
decl_stmt|;
DECL|field|closed
name|boolean
name|closed
decl_stmt|;
DECL|field|result
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|method|append (CharSequence csq)
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|)
block|{
name|result
operator|.
name|append
argument_list|(
name|csq
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (char c)
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|char
name|c
parameter_list|)
block|{
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|append (CharSequence csq, int start, int end)
annotation|@
name|Override
specifier|public
name|Appendable
name|append
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|result
operator|.
name|append
argument_list|(
name|csq
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|flush ()
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|flushed
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|close ()
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|closed
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|testWriteMethods ()
specifier|public
name|void
name|testWriteMethods
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
name|Writer
name|writer
init|=
operator|new
name|AppendableWriter
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Hello"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|0xBEEF0020
argument_list|)
expr_stmt|;
comment|// only lower 16 bits are important
name|writer
operator|.
name|write
argument_list|(
literal|"Wo"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Whirled"
operator|.
name|toCharArray
argument_list|()
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Mad! Mad, I say"
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello, World!"
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAppendMethods ()
specifier|public
name|void
name|testAppendMethods
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
name|Writer
name|writer
init|=
operator|new
name|AppendableWriter
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|writer
operator|.
name|append
argument_list|(
literal|"Hello,"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
literal|"The World Wide Web"
argument_list|,
literal|4
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|writer
operator|.
name|append
argument_list|(
literal|"!"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello, World!"
argument_list|,
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCloseFlush ()
specifier|public
name|void
name|testCloseFlush
parameter_list|()
throws|throws
name|IOException
block|{
name|SpyAppendable
name|spy
init|=
operator|new
name|SpyAppendable
argument_list|()
decl_stmt|;
name|Writer
name|writer
init|=
operator|new
name|AppendableWriter
argument_list|(
name|spy
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|spy
operator|.
name|flushed
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|spy
operator|.
name|closed
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|spy
operator|.
name|flushed
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|spy
operator|.
name|closed
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|spy
operator|.
name|flushed
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|spy
operator|.
name|closed
argument_list|)
expr_stmt|;
block|}
DECL|method|testCloseIsFinal ()
specifier|public
name|void
name|testCloseIsFinal
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
name|Writer
name|writer
init|=
operator|new
name|AppendableWriter
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"Hi"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
try|try
block|{
name|writer
operator|.
name|write
argument_list|(
literal|" Greg"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown IOException due to writer already closed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|es
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown IOException due to writer already closed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|es
parameter_list|)
block|{
comment|// expected
block|}
comment|// close()ing already closed writer is allowed
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

