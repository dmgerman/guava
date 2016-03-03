begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_class
DECL|class|FuturesGetDoneTest_gwt
specifier|public
class|class
name|FuturesGetDoneTest_gwt
extends|extends
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
block|{
DECL|method|getModuleName ()
annotation|@
name|Override
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
literal|"com.google.common.util.concurrent.testModule"
return|;
block|}
DECL|method|testCancelled ()
specifier|public
name|void
name|testCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testFailed ()
specifier|public
name|void
name|testFailed
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testFailed
argument_list|()
expr_stmt|;
block|}
DECL|method|testPending ()
specifier|public
name|void
name|testPending
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testPending
argument_list|()
expr_stmt|;
block|}
DECL|method|testSuccessful ()
specifier|public
name|void
name|testSuccessful
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSuccessful
argument_list|()
expr_stmt|;
block|}
DECL|method|testSuccessfulNull ()
specifier|public
name|void
name|testSuccessfulNull
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|FuturesGetDoneTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testSuccessfulNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

