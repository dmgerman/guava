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
comment|/**  * An object that can perform a {@link #tearDown} operation.  *  * @author Kevin Bourrillion  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|TearDown
specifier|public
interface|interface
name|TearDown
block|{
comment|/**    * Performs a<b>single</b> tear-down operation. See    * {@link com.google.common.testing.junit3.TearDownTestCase} and    * {@link com.google.common.testing.junit4.TearDownTestCase} for example.    *    *<p>If you want to not fail a test when a {@link TearDown} throws an    * exception, you should implement a {@link SloppyTearDown} instead.    *    *<p> Note that, for backwards compatibility, JUnit 3's    * {@link com.google.common.testing.junit3.TearDownTestCase} currently does    * not fail a test when an exception is thrown from one of its    * {@link TearDown}s, but this is subject to change. Also, Junit 4's    * {@link com.google.common.testing.junit4.TearDownTestCase} will.    *    * @throws Exception for any reason. {@code TearDownTestCase} ensures that    *     any exception thrown will not interfere with other TearDown    *     operations.    */
DECL|method|tearDown ()
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

