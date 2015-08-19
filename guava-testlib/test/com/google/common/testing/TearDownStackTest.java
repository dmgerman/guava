begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|annotations
operator|.
name|GwtCompatible
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
comment|/**  * @author Luiz-Otavio "Z" Zorzella  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|TearDownStackTest
specifier|public
class|class
name|TearDownStackTest
extends|extends
name|TestCase
block|{
DECL|field|tearDownStack
specifier|private
name|TearDownStack
name|tearDownStack
init|=
operator|new
name|TearDownStack
argument_list|()
decl_stmt|;
DECL|method|testSingleTearDown ()
specifier|public
name|void
name|testSingleTearDown
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TearDownStack
name|stack
init|=
name|buildTearDownStack
argument_list|()
decl_stmt|;
specifier|final
name|SimpleTearDown
name|tearDown
init|=
operator|new
name|SimpleTearDown
argument_list|()
decl_stmt|;
name|stack
operator|.
name|addTearDown
argument_list|(
name|tearDown
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|tearDown
operator|.
name|ran
argument_list|)
expr_stmt|;
name|stack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tearDown should have run"
argument_list|,
literal|true
argument_list|,
name|tearDown
operator|.
name|ran
argument_list|)
expr_stmt|;
block|}
DECL|method|testMultipleTearDownsHappenInOrder ()
specifier|public
name|void
name|testMultipleTearDownsHappenInOrder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TearDownStack
name|stack
init|=
name|buildTearDownStack
argument_list|()
decl_stmt|;
specifier|final
name|SimpleTearDown
name|tearDownOne
init|=
operator|new
name|SimpleTearDown
argument_list|()
decl_stmt|;
name|stack
operator|.
name|addTearDown
argument_list|(
name|tearDownOne
argument_list|)
expr_stmt|;
specifier|final
name|Callback
name|callback
init|=
operator|new
name|Callback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"tearDownTwo should have been run before tearDownOne"
argument_list|,
literal|false
argument_list|,
name|tearDownOne
operator|.
name|ran
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|SimpleTearDown
name|tearDownTwo
init|=
operator|new
name|SimpleTearDown
argument_list|(
name|callback
argument_list|)
decl_stmt|;
name|stack
operator|.
name|addTearDown
argument_list|(
name|tearDownTwo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|tearDownOne
operator|.
name|ran
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|tearDownTwo
operator|.
name|ran
argument_list|)
expr_stmt|;
name|stack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tearDownOne should have run"
argument_list|,
literal|true
argument_list|,
name|tearDownOne
operator|.
name|ran
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tearDownTwo should have run"
argument_list|,
literal|true
argument_list|,
name|tearDownTwo
operator|.
name|ran
argument_list|)
expr_stmt|;
block|}
DECL|method|testThrowingTearDown ()
specifier|public
name|void
name|testThrowingTearDown
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|TearDownStack
name|stack
init|=
name|buildTearDownStack
argument_list|()
decl_stmt|;
specifier|final
name|ThrowingTearDown
name|tearDownOne
init|=
operator|new
name|ThrowingTearDown
argument_list|(
literal|"one"
argument_list|)
decl_stmt|;
name|stack
operator|.
name|addTearDown
argument_list|(
name|tearDownOne
argument_list|)
expr_stmt|;
specifier|final
name|ThrowingTearDown
name|tearDownTwo
init|=
operator|new
name|ThrowingTearDown
argument_list|(
literal|"two"
argument_list|)
decl_stmt|;
name|stack
operator|.
name|addTearDown
argument_list|(
name|tearDownTwo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|tearDownOne
operator|.
name|ran
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|tearDownTwo
operator|.
name|ran
argument_list|)
expr_stmt|;
try|try
block|{
name|stack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"runTearDown should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClusterException
name|expected
parameter_list|)
block|{
name|assertThat
argument_list|(
name|expected
operator|.
name|getCause
argument_list|()
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"A ClusterException should have been thrown, rather than a "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|tearDownOne
operator|.
name|ran
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|tearDownTwo
operator|.
name|ran
argument_list|)
expr_stmt|;
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
name|tearDownStack
operator|.
name|runTearDown
argument_list|()
expr_stmt|;
block|}
comment|/**    * Builds a {@link TearDownStack} that makes sure it's clear by the end of    * this test.    */
DECL|method|buildTearDownStack ()
specifier|private
name|TearDownStack
name|buildTearDownStack
parameter_list|()
block|{
specifier|final
name|TearDownStack
name|result
init|=
operator|new
name|TearDownStack
argument_list|()
decl_stmt|;
name|tearDownStack
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
name|assertEquals
argument_list|(
literal|"The test should have cleared the stack (say, by virtue of running runTearDown)"
argument_list|,
literal|0
argument_list|,
name|result
operator|.
name|stack
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|class|ThrowingTearDown
specifier|private
specifier|static
specifier|final
class|class
name|ThrowingTearDown
implements|implements
name|TearDown
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|ran
name|boolean
name|ran
init|=
literal|false
decl_stmt|;
DECL|method|ThrowingTearDown (String id)
name|ThrowingTearDown
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|ran
operator|=
literal|true
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|id
argument_list|)
throw|;
block|}
block|}
DECL|class|SimpleTearDown
specifier|private
specifier|static
specifier|final
class|class
name|SimpleTearDown
implements|implements
name|TearDown
block|{
DECL|field|ran
name|boolean
name|ran
init|=
literal|false
decl_stmt|;
DECL|field|callback
name|Callback
name|callback
init|=
literal|null
decl_stmt|;
DECL|method|SimpleTearDown ()
specifier|public
name|SimpleTearDown
parameter_list|()
block|{}
DECL|method|SimpleTearDown (Callback callback)
specifier|public
name|SimpleTearDown
parameter_list|(
name|Callback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|callback
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
name|ran
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|interface|Callback
specifier|private
interface|interface
name|Callback
block|{
DECL|method|run ()
name|void
name|run
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

