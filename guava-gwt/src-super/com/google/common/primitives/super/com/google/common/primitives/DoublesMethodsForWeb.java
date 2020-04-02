begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
package|;
end_package

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsMethod
import|;
end_import

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsPackage
import|;
end_import

begin_comment
comment|/** Web specializations for {@link Doubles} methods. */
end_comment

begin_class
DECL|class|DoublesMethodsForWeb
specifier|abstract
class|class
name|DoublesMethodsForWeb
block|{
annotation|@
name|JsMethod
argument_list|(
name|name
operator|=
literal|"Math.min"
argument_list|,
name|namespace
operator|=
name|JsPackage
operator|.
name|GLOBAL
argument_list|)
DECL|method|min (double... array)
specifier|public
specifier|static
specifier|native
name|double
name|min
parameter_list|(
name|double
modifier|...
name|array
parameter_list|)
function_decl|;
annotation|@
name|JsMethod
argument_list|(
name|name
operator|=
literal|"Math.max"
argument_list|,
name|namespace
operator|=
name|JsPackage
operator|.
name|GLOBAL
argument_list|)
DECL|method|max (double... array)
specifier|public
specifier|static
specifier|native
name|double
name|max
parameter_list|(
name|double
modifier|...
name|array
parameter_list|)
function_decl|;
block|}
end_class

end_unit

