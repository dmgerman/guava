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
DECL|class|TrustedInputFutureTest_gwt
specifier|public
class|class
name|TrustedInputFutureTest_gwt
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
DECL|method|testCanceled ()
specifier|public
name|void
name|testCanceled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testCanceled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testFailed
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
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
DECL|method|testListenLaterCancelled ()
specifier|public
name|void
name|testListenLaterCancelled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterFailed ()
specifier|public
name|void
name|testListenLaterFailed
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterFailed
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterInterrupted ()
specifier|public
name|void
name|testListenLaterInterrupted
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronously ()
specifier|public
name|void
name|testListenLaterSetAsynchronously
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronously
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterDelegateCancelled ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterDelegateCancelled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterDelegateCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterDelegateFailed ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterDelegateFailed
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterDelegateFailed
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterDelegateInterrupted ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterDelegateInterrupted
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterDelegateInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterDelegateSuccessful ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterDelegateSuccessful
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterDelegateSuccessful
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterSelfCancelled ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterSelfCancelled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterSelfCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSetAsynchronouslyLaterSelfInterrupted ()
specifier|public
name|void
name|testListenLaterSetAsynchronouslyLaterSelfInterrupted
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSetAsynchronouslyLaterSelfInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testListenLaterSuccessful ()
specifier|public
name|void
name|testListenLaterSuccessful
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testListenLaterSuccessful
argument_list|()
expr_stmt|;
block|}
DECL|method|testMisbehavingListenerAlreadyDone ()
specifier|public
name|void
name|testMisbehavingListenerAlreadyDone
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testMisbehavingListenerAlreadyDone
argument_list|()
expr_stmt|;
block|}
DECL|method|testMisbehavingListenerLaterDone ()
specifier|public
name|void
name|testMisbehavingListenerLaterDone
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testMisbehavingListenerLaterDone
argument_list|()
expr_stmt|;
block|}
DECL|method|testNullExecutor ()
specifier|public
name|void
name|testNullExecutor
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testNullExecutor
argument_list|()
expr_stmt|;
block|}
DECL|method|testNullListener ()
specifier|public
name|void
name|testNullListener
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testNullListener
argument_list|()
expr_stmt|;
block|}
DECL|method|testNullTimeUnit ()
specifier|public
name|void
name|testNullTimeUnit
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testNullTimeUnit
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
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
DECL|method|testSetExceptionNull ()
specifier|public
name|void
name|testSetExceptionNull
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetExceptionNull
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateAlreadyCancelled ()
specifier|public
name|void
name|testSetFutureDelegateAlreadyCancelled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateAlreadyCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateAlreadyInterrupted ()
specifier|public
name|void
name|testSetFutureDelegateAlreadyInterrupted
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateAlreadyInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateAlreadySuccessful ()
specifier|public
name|void
name|testSetFutureDelegateAlreadySuccessful
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateAlreadySuccessful
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateLaterCancelled ()
specifier|public
name|void
name|testSetFutureDelegateLaterCancelled
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateLaterCancelled
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateLaterInterrupted ()
specifier|public
name|void
name|testSetFutureDelegateLaterInterrupted
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateLaterInterrupted
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureDelegateLaterSuccessful ()
specifier|public
name|void
name|testSetFutureDelegateLaterSuccessful
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureDelegateLaterSuccessful
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
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
DECL|method|testSetFuturePending ()
specifier|public
name|void
name|testSetFuturePending
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFuturePending
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureThenCancel ()
specifier|public
name|void
name|testSetFutureThenCancel
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureThenCancel
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetFutureThenInterrupt ()
specifier|public
name|void
name|testSetFutureThenInterrupt
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetFutureThenInterrupt
argument_list|()
expr_stmt|;
block|}
DECL|method|testSetNull ()
specifier|public
name|void
name|testSetNull
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSetNull
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
name|TrustedInputFutureTest
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
name|TrustedInputFutureTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testCase
operator|.
name|testSuccessful
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

