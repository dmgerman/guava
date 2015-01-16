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
DECL|class|UntrustedInputFutureTest_gwt
specifier|public
class|class
name|UntrustedInputFutureTest_gwt
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancelled_afterCancelled ()
specifier|public
name|void
name|testCancelled_afterCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled_afterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancelled_afterDelegated ()
specifier|public
name|void
name|testCancelled_afterDelegated
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled_afterDelegated
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancelled_afterException ()
specifier|public
name|void
name|testCancelled_afterException
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled_afterException
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancelled_afterInterrupted ()
specifier|public
name|void
name|testCancelled_afterInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled_afterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testCancelled_afterResolved ()
specifier|public
name|void
name|testCancelled_afterResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCancelled_afterResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_afterCancelled ()
specifier|public
name|void
name|testDelegated_afterCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_afterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_afterDelegated ()
specifier|public
name|void
name|testDelegated_afterDelegated
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_afterDelegated
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_afterResolved ()
specifier|public
name|void
name|testDelegated_afterResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_afterResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateIsCancelled ()
specifier|public
name|void
name|testDelegated_delegateIsCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateIsCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateIsInterrupted ()
specifier|public
name|void
name|testDelegated_delegateIsInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateIsInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateIsResolved ()
specifier|public
name|void
name|testDelegated_delegateIsResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateIsResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateIsStillPending ()
specifier|public
name|void
name|testDelegated_delegateIsStillPending
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateIsStillPending
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateWasCancelled ()
specifier|public
name|void
name|testDelegated_delegateWasCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateWasCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateWasInterrupted ()
specifier|public
name|void
name|testDelegated_delegateWasInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateWasInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testDelegated_delegateWasResolved ()
specifier|public
name|void
name|testDelegated_delegateWasResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testDelegated_delegateWasResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional ()
specifier|public
name|void
name|testExceptional
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional_afterCancelled ()
specifier|public
name|void
name|testExceptional_afterCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional_afterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional_afterDelegated ()
specifier|public
name|void
name|testExceptional_afterDelegated
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional_afterDelegated
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional_afterException ()
specifier|public
name|void
name|testExceptional_afterException
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional_afterException
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional_afterInterrupted ()
specifier|public
name|void
name|testExceptional_afterInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional_afterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testExceptional_afterResolved ()
specifier|public
name|void
name|testExceptional_afterResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testExceptional_afterResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted ()
specifier|public
name|void
name|testInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted_afterCancelled ()
specifier|public
name|void
name|testInterrupted_afterCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted_afterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted_afterDelegated ()
specifier|public
name|void
name|testInterrupted_afterDelegated
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted_afterDelegated
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted_afterException ()
specifier|public
name|void
name|testInterrupted_afterException
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted_afterException
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted_afterInterrupted ()
specifier|public
name|void
name|testInterrupted_afterInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted_afterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testInterrupted_afterResolved ()
specifier|public
name|void
name|testInterrupted_afterResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testInterrupted_afterResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToCancelled ()
specifier|public
name|void
name|testListenToCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegateCancelled ()
specifier|public
name|void
name|testListenToDelegateCancelled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegateCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegateCancelled_byDelegator ()
specifier|public
name|void
name|testListenToDelegateCancelled_byDelegator
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegateCancelled_byDelegator
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegateExceptional ()
specifier|public
name|void
name|testListenToDelegateExceptional
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegateExceptional
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegateInterrupted_byDelegator ()
specifier|public
name|void
name|testListenToDelegateInterrupted_byDelegator
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegateInterrupted_byDelegator
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegatePending ()
specifier|public
name|void
name|testListenToDelegatePending
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegatePending
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToDelegateResolved ()
specifier|public
name|void
name|testListenToDelegateResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToDelegateResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToExceptional ()
specifier|public
name|void
name|testListenToExceptional
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToExceptional
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToInterrupted ()
specifier|public
name|void
name|testListenToInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToPending ()
specifier|public
name|void
name|testListenToPending
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToPending
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToResolved ()
specifier|public
name|void
name|testListenToResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenToResolved_misbehavingListener ()
specifier|public
name|void
name|testListenToResolved_misbehavingListener
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenToResolved_misbehavingListener
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testPending
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved ()
specifier|public
name|void
name|testResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved_afterCanceled ()
specifier|public
name|void
name|testResolved_afterCanceled
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved_afterCanceled
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved_afterDelegated ()
specifier|public
name|void
name|testResolved_afterDelegated
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved_afterDelegated
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved_afterException ()
specifier|public
name|void
name|testResolved_afterException
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved_afterException
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved_afterInterrupted ()
specifier|public
name|void
name|testResolved_afterInterrupted
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved_afterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testResolved_afterResolved ()
specifier|public
name|void
name|testResolved_afterResolved
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testResolved_afterResolved
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureNull ()
specifier|public
name|void
name|testSetFutureNull
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
name|UntrustedInputFutureTest
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
name|UntrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

