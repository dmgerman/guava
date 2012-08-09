begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GwtScriptOnly
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|lang
operator|.
name|Array
import|;
end_import

begin_comment
comment|/**  * Version of {@link GwtPlatform} used in web-mode.  It includes methods in  * {@link Platform} that requires different implementions in web mode and  * hosted mode.  It is factored out from {@link Platform} because<code>  * {@literal @}GwtScriptOnly</code> only supports public classes and methods.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtScriptOnly
DECL|class|GwtPlatform
specifier|public
specifier|final
class|class
name|GwtPlatform
block|{
DECL|method|GwtPlatform ()
specifier|private
name|GwtPlatform
parameter_list|()
block|{}
DECL|method|clone (T[] array)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|clone
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|clone
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
end_class

end_unit

