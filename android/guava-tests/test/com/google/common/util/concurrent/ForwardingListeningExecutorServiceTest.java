begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link ForwardingListeningExecutorService}  */
end_comment

begin_class
DECL|class|ForwardingListeningExecutorServiceTest
specifier|public
class|class
name|ForwardingListeningExecutorServiceTest
extends|extends
name|TestCase
block|{
DECL|method|testForwarding ()
specifier|public
name|void
name|testForwarding
parameter_list|()
block|{
name|ForwardingObjectTester
operator|.
name|testForwardingObject
argument_list|(
name|ForwardingListeningExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

