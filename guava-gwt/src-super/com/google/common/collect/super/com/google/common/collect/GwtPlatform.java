begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
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
name|core
operator|.
name|client
operator|.
name|GwtScriptOnly
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
comment|/**  * Version of {@link GwtPlatform} used in web-mode.  It includes methods in  * {@link Platform} that requires different implementions in web mode and  * hosted mode.  It is factored out from {@link Platform} because<code>  * {@literal @}GwtScriptOnly</code> only supports public classes and methods.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
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
DECL|method|newArray (T[] reference, int length)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|T
index|[]
name|reference
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|T
index|[]
name|clone
init|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|reference
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|resizeArray
argument_list|(
name|clone
argument_list|,
name|length
argument_list|)
expr_stmt|;
return|return
name|clone
return|;
block|}
DECL|method|resizeArray (Object array, int newSize)
specifier|private
specifier|static
specifier|native
name|void
name|resizeArray
parameter_list|(
name|Object
name|array
parameter_list|,
name|int
name|newSize
parameter_list|)
comment|/*-{     array.length = newSize;   }-*/
function_decl|;
block|}
end_class

end_unit

