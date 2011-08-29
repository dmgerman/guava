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
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
import|;
end_import

begin_import
import|import
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
name|UncaughtExceptionHandlers
operator|.
name|Exiter
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author Gregory Kick  */
end_comment

begin_class
DECL|class|UncaughtExceptionHandlersTest
specifier|public
class|class
name|UncaughtExceptionHandlersTest
extends|extends
name|TestCase
block|{
DECL|field|runtimeMock
specifier|private
name|Runtime
name|runtimeMock
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
name|runtimeMock
operator|=
name|createMock
argument_list|(
name|Runtime
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testExiter ()
annotation|@
name|Test
specifier|public
name|void
name|testExiter
parameter_list|()
block|{
name|runtimeMock
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|runtimeMock
argument_list|)
expr_stmt|;
operator|new
name|Exiter
argument_list|(
name|runtimeMock
argument_list|)
operator|.
name|uncaughtException
argument_list|(
operator|new
name|Thread
argument_list|()
argument_list|,
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|runtimeMock
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

