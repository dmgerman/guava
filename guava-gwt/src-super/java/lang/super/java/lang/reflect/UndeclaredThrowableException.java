begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.lang.reflect
package|package
name|java
operator|.
name|lang
operator|.
name|reflect
package|;
end_package

begin_comment
comment|/**  * GWT emulation of UndeclaredThrowableException.  */
end_comment

begin_class
DECL|class|UndeclaredThrowableException
specifier|public
class|class
name|UndeclaredThrowableException
extends|extends
name|RuntimeException
block|{
DECL|method|UndeclaredThrowableException (Throwable undeclaredThrowable)
specifier|public
name|UndeclaredThrowableException
parameter_list|(
name|Throwable
name|undeclaredThrowable
parameter_list|)
block|{
name|super
argument_list|(
name|undeclaredThrowable
argument_list|)
expr_stmt|;
block|}
DECL|method|UndeclaredThrowableException (Throwable undeclaredThrowable, String message)
specifier|public
name|UndeclaredThrowableException
parameter_list|(
name|Throwable
name|undeclaredThrowable
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|undeclaredThrowable
argument_list|)
expr_stmt|;
block|}
DECL|method|getUndeclaredThrowable ()
specifier|public
name|Throwable
name|getUndeclaredThrowable
parameter_list|()
block|{
return|return
name|getCause
argument_list|()
return|;
block|}
block|}
end_class

end_unit

