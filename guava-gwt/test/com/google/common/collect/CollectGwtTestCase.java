begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|gwt
operator|.
name|junit
operator|.
name|client
operator|.
name|GWTTestCase
import|;
end_import

begin_comment
comment|/**  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|CollectGwtTestCase
specifier|public
class|class
name|CollectGwtTestCase
extends|extends
name|GWTTestCase
block|{
DECL|field|MODULE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|MODULE_NAME
init|=
literal|"com.google.common.collect.testModule"
decl_stmt|;
DECL|method|getModuleName ()
annotation|@
name|Override
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
name|MODULE_NAME
return|;
block|}
block|}
end_class

end_unit

