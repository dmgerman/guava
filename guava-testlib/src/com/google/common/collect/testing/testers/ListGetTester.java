begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
operator|.
name|testers
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
comment|/**  * A generic JUnit test which tests {@code get()} operations on a list. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressUnderAndroid
DECL|class|ListGetTester
specifier|public
class|class
name|ListGetTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testGet_valid ()
specifier|public
name|void
name|testGet_valid
parameter_list|()
block|{
comment|// This calls get() on each index and checks the result:
name|expectContents
argument_list|(
name|createOrderedArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet_negative ()
specifier|public
name|void
name|testGet_negative
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|get
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"get(-1) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testGet_tooLarge ()
specifier|public
name|void
name|testGet_tooLarge
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|get
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"get(size) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

