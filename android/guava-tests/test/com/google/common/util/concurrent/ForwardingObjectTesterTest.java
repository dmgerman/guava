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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ForwardingObject
import|;
end_import

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
comment|/**  * Tests for {@link ForwardingObjectTester}.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|ForwardingObjectTesterTest
specifier|public
class|class
name|ForwardingObjectTesterTest
extends|extends
name|TestCase
block|{
DECL|method|testFailsToForward ()
specifier|public
name|void
name|testFailsToForward
parameter_list|()
block|{
try|try
block|{
name|ForwardingObjectTester
operator|.
name|testForwardingObject
argument_list|(
name|FailToForward
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionError
decl||
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{
comment|// UnsupportedOperationException is what we see on Android.
return|return;
block|}
name|fail
argument_list|(
literal|"Should have thrown"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AndroidIncompatible
comment|// TODO(cpovirk): java.lang.IllegalAccessError: superclass not accessible
DECL|method|testSuccessfulForwarding ()
specifier|public
name|void
name|testSuccessfulForwarding
parameter_list|()
block|{
name|ForwardingObjectTester
operator|.
name|testForwardingObject
argument_list|(
name|ForwardToDelegate
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|class|FailToForward
specifier|private
specifier|abstract
specifier|static
class|class
name|FailToForward
extends|extends
name|ForwardingObject
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{}
block|}
DECL|class|ForwardToDelegate
specifier|private
specifier|abstract
specifier|static
class|class
name|ForwardToDelegate
extends|extends
name|ForwardingObject
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|delegate
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|Runnable
name|delegate
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

