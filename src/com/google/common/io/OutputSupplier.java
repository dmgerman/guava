begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|IOException
import|;
end_import

begin_comment
comment|/**  * An factory for writable streams of bytes or characters.  *  * @author Chris Nokleberg  * @since 1  */
end_comment

begin_interface
DECL|interface|OutputSupplier
specifier|public
interface|interface
name|OutputSupplier
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|getOutput ()
name|T
name|getOutput
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

