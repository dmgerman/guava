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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|LogRecord
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link TestLogHandler}.  *  * @author kevinb  */
end_comment

begin_class
DECL|class|TestLogHandlerTest
specifier|public
class|class
name|TestLogHandlerTest
extends|extends
name|TestCase
block|{
DECL|field|handler
specifier|private
name|TestLogHandler
name|handler
decl_stmt|;
DECL|field|stack
specifier|private
name|TearDownStack
name|stack
init|=
operator|new
name|TearDownStack
argument_list|()
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|handler
operator|=
operator|new
name|TestLogHandler
argument_list|()
expr_stmt|;
comment|// You could also apply it higher up the Logger hierarchy than this
name|ExampleClassUnderTest
operator|.
name|logger
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|ExampleClassUnderTest
operator|.
name|logger
operator|.
name|setUseParentHandlers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// optional
name|stack
operator|.
name|addTearDown
argument_list|(
operator|new
name|TearDown
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ExampleClassUnderTest
operator|.
name|logger
operator|.
name|setUseParentHandlers
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ExampleClassUnderTest
operator|.
name|logger
operator|.
name|removeHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|handler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|ExampleClassUnderTest
operator|.
name|foo
argument_list|()
expr_stmt|;
name|LogRecord
name|record
init|=
name|handler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
name|record
operator|.
name|getLevel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"message"
argument_list|,
name|record
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|EXCEPTION
argument_list|,
name|record
operator|.
name|getThrown
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConcurrentModification ()
specifier|public
name|void
name|testConcurrentModification
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Tests for the absence of a bug where logging while iterating over the
comment|// stored log records causes a ConcurrentModificationException
name|assertTrue
argument_list|(
name|handler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|ExampleClassUnderTest
operator|.
name|foo
argument_list|()
expr_stmt|;
name|ExampleClassUnderTest
operator|.
name|foo
argument_list|()
expr_stmt|;
for|for
control|(
name|LogRecord
name|unused
range|:
name|handler
operator|.
name|getStoredLogRecords
argument_list|()
control|)
block|{
name|ExampleClassUnderTest
operator|.
name|foo
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|runBare ()
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|runBare
parameter_list|()
throws|throws
name|Throwable
block|{
try|try
block|{
name|setUp
argument_list|()
expr_stmt|;
name|runTest
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|tearDown ()
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
block|{
name|stack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
block|}
DECL|field|EXCEPTION
specifier|static
specifier|final
name|Exception
name|EXCEPTION
init|=
operator|new
name|Exception
argument_list|()
decl_stmt|;
DECL|class|ExampleClassUnderTest
specifier|static
class|class
name|ExampleClassUnderTest
block|{
DECL|field|logger
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|ExampleClassUnderTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|foo ()
specifier|static
name|void
name|foo
parameter_list|()
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"message"
argument_list|,
name|EXCEPTION
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

