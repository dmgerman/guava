begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
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
name|Beta
import|;
end_import

begin_comment
comment|/**  * Any object which can accept registrations of {@link TearDown} instances.  *  * @author Kevin Bourrillion  * @since Guava release 10  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|TearDownAccepter
specifier|public
interface|interface
name|TearDownAccepter
block|{
comment|/**    * Registers a TearDown implementor which will be run after the test proper.    *    *<p>In JUnit4 language, that means as an {@code @After}.    *    *<p>In JUnit3 language, that means during the    * {@link junit.framework.TestCase#tearDown()} step.    */
DECL|method|addTearDown (TearDown tearDown)
name|void
name|addTearDown
parameter_list|(
name|TearDown
name|tearDown
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

