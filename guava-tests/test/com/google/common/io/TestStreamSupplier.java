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

begin_comment
comment|/**  * Interface for a supplier of streams that can report whether a stream was opened and whether that  * stream was closed. Intended for use in a test where only a single stream should be opened and  * possibly closed.  *  * @author Colin Decker  */
end_comment

begin_interface
DECL|interface|TestStreamSupplier
specifier|public
interface|interface
name|TestStreamSupplier
block|{
comment|/**    * Returns whether or not a new stream was opened.    */
DECL|method|wasStreamOpened ()
name|boolean
name|wasStreamOpened
parameter_list|()
function_decl|;
comment|/**    * Returns whether or not an open stream was closed.    */
DECL|method|wasStreamClosed ()
name|boolean
name|wasStreamClosed
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

