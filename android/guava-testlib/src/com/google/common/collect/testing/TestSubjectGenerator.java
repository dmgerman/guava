begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * To be implemented by test generators that can produce test subjects without  * requiring any parameters.  *  * @param<T> the type created by this generator.  *  * @author George van den Driessche  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|TestSubjectGenerator
specifier|public
interface|interface
name|TestSubjectGenerator
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|createTestSubject ()
name|T
name|createTestSubject
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

