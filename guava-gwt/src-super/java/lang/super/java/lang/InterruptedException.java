begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.lang
package|package
name|java
operator|.
name|lang
package|;
end_package

begin_comment
comment|/**  * Minimal emulation of {@link java.lang.InterruptedException}, that should only be used in method  * signatures. New GWT code should not reference this class at all. It is here only to ease the  * GWTification of common code.  *  * @author Tom O'Neill  */
end_comment

begin_class
DECL|class|InterruptedException
specifier|public
class|class
name|InterruptedException
extends|extends
name|Exception
block|{
DECL|method|InterruptedException ()
specifier|public
name|InterruptedException
parameter_list|()
block|{}
DECL|method|InterruptedException (String message)
specifier|public
name|InterruptedException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

