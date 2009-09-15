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
comment|/**  * An factory for readable streams of bytes or characters.  *  * @author Chris Nokleberg  * @param<T> the type of object being supplied  * @since 9.09.15<b>tentative</b>  */
end_comment

begin_interface
DECL|interface|InputSupplier
specifier|public
interface|interface
name|InputSupplier
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|getInput ()
name|T
name|getInput
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

