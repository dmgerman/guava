begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|EntryPoint
import|;
end_import

begin_comment
comment|/**  * A dummy entry point of the test module.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|TestModuleEntryPoint
specifier|public
class|class
name|TestModuleEntryPoint
implements|implements
name|EntryPoint
block|{
DECL|method|onModuleLoad ()
annotation|@
name|Override
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{   }
block|}
end_class

end_unit

