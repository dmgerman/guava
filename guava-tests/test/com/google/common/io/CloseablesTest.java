begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Closeable
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
comment|/**  * Unit tests for {@link Closeables}.  *  *<p>Checks proper closing behavior, and ensures that  * IOExceptions on Closeable.close() are not  * propagated out from the {@link Closeables#close} method if {@code  * swallowException} is true.  *  * @author Michael Lancaster  */
end_comment

begin_class
DECL|class|CloseablesTest
specifier|public
class|class
name|CloseablesTest
extends|extends
name|TestCase
block|{
DECL|field|mockCloseable
specifier|private
name|Closeable
name|mockCloseable
decl_stmt|;
DECL|method|testClose_closeableClean ()
specifier|public
name|void
name|testClose_closeableClean
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that no exception is thrown regardless of value of
comment|// 'swallowException' when the mock does not throw an exception.
name|setupCloseable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doClose
argument_list|(
name|mockCloseable
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setupCloseable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|doClose
argument_list|(
name|mockCloseable
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testClose_closeableWithEatenException ()
specifier|public
name|void
name|testClose_closeableWithEatenException
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that no exception is thrown if 'swallowException' is true
comment|// when the mock does throw an exception.
name|setupCloseable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doClose
argument_list|(
name|mockCloseable
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testClose_closeableWithThrownException ()
specifier|public
name|void
name|testClose_closeableWithThrownException
parameter_list|()
throws|throws
name|IOException
block|{
comment|// make sure that the exception is thrown if 'swallowException' is false
comment|// when the mock does throw an exception.
name|setupCloseable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|doClose
argument_list|(
name|mockCloseable
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|testCloseNull ()
specifier|public
name|void
name|testCloseNull
parameter_list|()
throws|throws
name|IOException
block|{
name|Closeables
operator|.
name|close
argument_list|(
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Closeables
operator|.
name|close
argument_list|(
literal|null
argument_list|,
literal|false
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
name|mockCloseable
operator|=
name|createStrictMock
argument_list|(
name|Closeable
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
comment|// Set up a closeable to expect to be closed, and optionally to throw an
comment|// exception.
DECL|method|setupCloseable (boolean shouldThrow)
specifier|private
name|void
name|setupCloseable
parameter_list|(
name|boolean
name|shouldThrow
parameter_list|)
throws|throws
name|IOException
block|{
name|reset
argument_list|(
name|mockCloseable
argument_list|)
expr_stmt|;
name|mockCloseable
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|shouldThrow
condition|)
block|{
name|expectThrown
argument_list|()
expr_stmt|;
block|}
name|replay
argument_list|(
name|mockCloseable
argument_list|)
expr_stmt|;
block|}
DECL|method|doClose (Closeable closeable, boolean swallowException)
specifier|private
name|void
name|doClose
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|boolean
name|swallowException
parameter_list|)
block|{
name|doClose
argument_list|(
name|closeable
argument_list|,
name|swallowException
argument_list|,
operator|!
name|swallowException
argument_list|)
expr_stmt|;
block|}
comment|// Close the closeable using the Closeables, passing in the swallowException
comment|// parameter. expectThrown determines whether we expect an exception to
comment|// be thrown by Closeables.close;
DECL|method|doClose (Closeable closeable, boolean swallowException, boolean expectThrown)
specifier|private
name|void
name|doClose
parameter_list|(
name|Closeable
name|closeable
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
name|Closeables
operator|.
name|close
argument_list|(
name|closeable
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
name|closeable
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

