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
name|Charsets
operator|.
name|UTF_8
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|InputStreamReader
import|;
end_import

begin_comment
comment|/**  * @author Colin Decker  */
end_comment

begin_class
DECL|class|TestReader
specifier|public
class|class
name|TestReader
extends|extends
name|FilterReader
block|{
DECL|field|in
specifier|private
specifier|final
name|TestInputStream
name|in
decl_stmt|;
DECL|method|TestReader (TestOption... options)
specifier|public
name|TestReader
parameter_list|(
name|TestOption
modifier|...
name|options
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|TestInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[
literal|10
index|]
argument_list|)
argument_list|,
name|options
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|TestReader (TestInputStream in)
specifier|public
name|TestReader
parameter_list|(
name|TestInputStream
name|in
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|checkNotNull
argument_list|(
name|in
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
DECL|method|closed ()
specifier|public
name|boolean
name|closed
parameter_list|()
block|{
return|return
name|in
operator|.
name|closed
argument_list|()
return|;
block|}
block|}
end_class

end_unit

