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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|TestOption
operator|.
name|CLOSE_THROWS
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
name|TestOption
operator|.
name|OPEN_THROWS
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
name|TestOption
operator|.
name|READ_THROWS
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
name|TestOption
operator|.
name|SKIP_THROWS
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
name|ImmutableSet
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
name|Arrays
import|;
end_import

begin_comment
comment|/** @author Colin Decker */
end_comment

begin_class
DECL|class|TestInputStream
specifier|public
class|class
name|TestInputStream
extends|extends
name|FilterInputStream
block|{
DECL|field|options
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|TestOption
argument_list|>
name|options
decl_stmt|;
DECL|field|closed
specifier|private
name|boolean
name|closed
decl_stmt|;
DECL|method|TestInputStream (InputStream in, TestOption... options)
specifier|public
name|TestInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|TestOption
modifier|...
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|in
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|TestInputStream (InputStream in, Iterable<TestOption> options)
specifier|public
name|TestInputStream
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Iterable
argument_list|<
name|TestOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|checkNotNull
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|throwIf
argument_list|(
name|OPEN_THROWS
argument_list|)
expr_stmt|;
block|}
DECL|method|closed ()
specifier|public
name|boolean
name|closed
parameter_list|()
block|{
return|return
name|closed
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|int
name|read
parameter_list|()
throws|throws
name|IOException
block|{
name|throwIf
argument_list|(
name|closed
argument_list|)
expr_stmt|;
name|throwIf
argument_list|(
name|READ_THROWS
argument_list|)
expr_stmt|;
return|return
name|in
operator|.
name|read
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|read (byte[] b, int off, int len)
specifier|public
name|int
name|read
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
name|throwIf
argument_list|(
name|closed
argument_list|)
expr_stmt|;
name|throwIf
argument_list|(
name|READ_THROWS
argument_list|)
expr_stmt|;
return|return
name|in
operator|.
name|read
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|skip (long n)
specifier|public
name|long
name|skip
parameter_list|(
name|long
name|n
parameter_list|)
throws|throws
name|IOException
block|{
name|throwIf
argument_list|(
name|closed
argument_list|)
expr_stmt|;
name|throwIf
argument_list|(
name|SKIP_THROWS
argument_list|)
expr_stmt|;
return|return
name|in
operator|.
name|skip
argument_list|(
name|n
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|available ()
specifier|public
name|int
name|available
parameter_list|()
throws|throws
name|IOException
block|{
name|throwIf
argument_list|(
name|closed
argument_list|)
expr_stmt|;
return|return
name|options
operator|.
name|contains
argument_list|(
name|TestOption
operator|.
name|AVAILABLE_ALWAYS_ZERO
argument_list|)
condition|?
literal|0
else|:
name|in
operator|.
name|available
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|closed
operator|=
literal|true
expr_stmt|;
name|throwIf
argument_list|(
name|CLOSE_THROWS
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|throwIf (TestOption option)
specifier|private
name|void
name|throwIf
parameter_list|(
name|TestOption
name|option
parameter_list|)
throws|throws
name|IOException
block|{
name|throwIf
argument_list|(
name|options
operator|.
name|contains
argument_list|(
name|option
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|throwIf (boolean condition)
specifier|private
specifier|static
name|void
name|throwIf
parameter_list|(
name|boolean
name|condition
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|condition
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

