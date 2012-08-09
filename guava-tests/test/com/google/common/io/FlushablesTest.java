begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|createStrictMock
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
name|expectLastCall
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
name|reset
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
name|io
operator|.
name|Flushable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Flushables}.  *  *<p>Checks proper flushing behavior, and ensures that  * IOExceptions on Flushable.flush() are not  * propagated out from the {@link Flushables#flush} method if {@code  * swallowException} is true.  *  * @author Michael Lancaster  */
end_comment

begin_class
DECL|class|FlushablesTest
specifier|public
class|class
name|FlushablesTest
extends|extends
name|TestCase
block|{
DECL|field|mockFlushable
specifier|private
name|Flushable
name|mockFlushable
decl_stmt|;
DECL|method|testFlush_clean ()
specifier|public
name|void
name|testFlush_clean
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that no exception is thrown regardless of value of
comment|// 'swallowException' when the mock does not throw an exception.
name|setupFlushable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doFlush
argument_list|(
name|mockFlushable
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setupFlushable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doFlush
argument_list|(
name|mockFlushable
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testFlush_flushableWithEatenException ()
specifier|public
name|void
name|testFlush_flushableWithEatenException
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that no exception is thrown if 'swallowException' is true
comment|// when the mock does throw an exception on flush.
name|setupFlushable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doFlush
argument_list|(
name|mockFlushable
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testFlush_flushableWithThrownException ()
specifier|public
name|void
name|testFlush_flushableWithThrownException
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that the exception is thrown if 'swallowException' is false
comment|// when the mock does throw an exception on flush.
name|setupFlushable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doFlush
argument_list|(
name|mockFlushable
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testFlushQuietly_flushableWithEatenException ()
specifier|public
name|void
name|testFlushQuietly_flushableWithEatenException
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that no exception is thrown by flushQuietly when the mock does
comment|// throw an exception on flush.
name|setupFlushable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Flushables
operator|.
name|flushQuietly
argument_list|(
name|mockFlushable
argument_list|)
expr_stmt|;
block|}
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
name|mockFlushable
operator|=
name|createStrictMock
argument_list|(
name|Flushable
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|expectThrown ()
specifier|private
name|void
name|expectThrown
parameter_list|()
block|{
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
operator|new
name|IOException
argument_list|(
literal|"This should only appear in the "
operator|+
literal|"logs. It should not be rethrown."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Set up a flushable to expect to be flushed, and optionally to
comment|// throw an exception.
DECL|method|setupFlushable (boolean shouldThrowOnFlush)
specifier|private
name|void
name|setupFlushable
parameter_list|(
name|boolean
name|shouldThrowOnFlush
parameter_list|)
throws|throws
name|IOException
block|{
name|reset
argument_list|(
name|mockFlushable
argument_list|)
expr_stmt|;
name|mockFlushable
operator|.
name|flush
argument_list|()
expr_stmt|;
if|if
condition|(
name|shouldThrowOnFlush
condition|)
block|{
name|expectThrown
argument_list|()
expr_stmt|;
block|}
name|replay
argument_list|(
name|mockFlushable
argument_list|)
expr_stmt|;
block|}
comment|// Flush the flushable using the Flushables, passing in the swallowException
comment|// parameter. expectThrown determines whether we expect an exception to
comment|// be thrown by Flushables.flush;
DECL|method|doFlush (Flushable flushable, boolean swallowException, boolean expectThrown)
specifier|private
name|void
name|doFlush
parameter_list|(
name|Flushable
name|flushable
parameter_list|,
name|boolean
name|swallowException
parameter_list|,
name|boolean
name|expectThrown
parameter_list|)
block|{
try|try
block|{
name|Flushables
operator|.
name|flush
argument_list|(
name|flushable
argument_list|,
name|swallowException
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectThrown
condition|)
block|{
name|fail
argument_list|(
literal|"Didn't throw exception."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expectThrown
condition|)
block|{
name|fail
argument_list|(
literal|"Threw exception"
argument_list|)
expr_stmt|;
block|}
block|}
name|verify
argument_list|(
name|flushable
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

