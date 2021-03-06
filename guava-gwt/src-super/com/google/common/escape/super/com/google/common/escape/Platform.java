begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
package|;
end_package

begin_comment
comment|/** @author Jesse Wilson */
end_comment

begin_class
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|field|CHAR_BUFFER
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|CHAR_BUFFER
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
DECL|method|charBufferFromThreadLocal ()
specifier|static
name|char
index|[]
name|charBufferFromThreadLocal
parameter_list|()
block|{
comment|// ThreadLocal is not available to GWT, so we always reuse the same
comment|// instance.  It is always safe to return the same instance because
comment|// javascript is single-threaded, and only used by blocks that doesn't
comment|// involve async callbacks.
return|return
name|CHAR_BUFFER
return|;
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

