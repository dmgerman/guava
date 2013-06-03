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
name|collect
operator|.
name|Lists
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
name|FilterInputStream
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

begin_comment
comment|/**  * Test class for {@link MultiInputStream}.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|MultiInputStreamTest
specifier|public
class|class
name|MultiInputStreamTest
extends|extends
name|IoTestCase
block|{
DECL|method|testJoin ()
specifier|public
name|void
name|testJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|joinHelper
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|10
argument_list|,
literal|0
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|10
argument_list|,
literal|20
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|joinHelper
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testOnlyOneOpen ()
specifier|public
name|void
name|testOnlyOneOpen
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|ByteSource
name|source
init|=
name|newByteSource
argument_list|(
literal|0
argument_list|,
literal|50
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
name|ByteSource
name|checker
init|=
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
name|FilterInputStream
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
name|byte
index|[]
name|result
init|=
name|ByteSource
operator|.
name|concat
argument_list|(
name|checker
argument_list|,
name|checker
argument_list|,
name|checker
argument_list|)
operator|.
name|read
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|150
argument_list|,
name|result
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|joinHelper (Integer... spans)
specifier|private
name|void
name|joinHelper
parameter_list|(
name|Integer
modifier|...
name|spans
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ByteSource
argument_list|>
name|sources
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|int
name|start
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Integer
name|span
range|:
name|spans
control|)
block|{
name|sources
operator|.
name|add
argument_list|(
name|newByteSource
argument_list|(
name|start
argument_list|,
name|span
argument_list|)
argument_list|)
expr_stmt|;
name|start
operator|+=
name|span
expr_stmt|;
block|}
name|ByteSource
name|joined
init|=
name|ByteSource
operator|.
name|concat
argument_list|(
name|sources
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|newByteSource
argument_list|(
literal|0
argument_list|,
name|start
argument_list|)
operator|.
name|contentEquals
argument_list|(
name|joined
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReadSingleByte ()
specifier|public
name|void
name|testReadSingleByte
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteSource
name|source
init|=
name|newByteSource
argument_list|(
literal|0
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|ByteSource
name|joined
init|=
name|ByteSource
operator|.
name|concat
argument_list|(
name|source
argument_list|,
name|source
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|joined
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|joined
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|in
operator|.
name|markSupported
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|in
operator|.
name|available
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|read
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|total
operator|++
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|in
operator|.
name|available
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
DECL|method|testSkip ()
specifier|public
name|void
name|testSkip
parameter_list|()
throws|throws
name|Exception
block|{
name|MultiInputStream
name|multi
init|=
operator|new
name|MultiInputStream
argument_list|(
name|Collections
operator|.
name|singleton
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
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|newPreFilledByteArray
argument_list|(
literal|0
argument_list|,
literal|50
argument_list|)
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|n
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
name|multi
operator|.
name|skip
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|multi
operator|.
name|skip
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|multi
operator|.
name|skip
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ByteStreams
operator|.
name|skipFully
argument_list|(
name|multi
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|multi
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|newByteSource (final int start, final int size)
specifier|private
specifier|static
name|ByteSource
name|newByteSource
parameter_list|(
specifier|final
name|int
name|start
parameter_list|,
specifier|final
name|int
name|size
parameter_list|)
block|{
return|return
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
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|newPreFilledByteArray
argument_list|(
name|start
argument_list|,
name|size
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

