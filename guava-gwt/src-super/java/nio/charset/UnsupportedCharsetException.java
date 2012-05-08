begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.nio.charset
package|package
name|java
operator|.
name|nio
operator|.
name|charset
package|;
end_package

begin_comment
comment|/**  * GWT emulation of {@link UnsupportedCharsetException}.  *  * @author Gregory Kick  */
end_comment

begin_class
DECL|class|UnsupportedCharsetException
specifier|public
class|class
name|UnsupportedCharsetException
extends|extends
name|IllegalArgumentException
block|{
DECL|field|charsetName
specifier|private
specifier|final
name|String
name|charsetName
decl_stmt|;
DECL|method|UnsupportedCharsetException (String charsetName)
specifier|public
name|UnsupportedCharsetException
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|super
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|charsetName
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|charsetName
operator|=
name|charsetName
expr_stmt|;
block|}
DECL|method|getCharsetName ()
specifier|public
name|String
name|getCharsetName
parameter_list|()
block|{
return|return
name|charsetName
return|;
block|}
block|}
end_class

end_unit

