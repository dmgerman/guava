begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestResult
import|;
end_import

begin_comment
comment|/**  * @author Max Ross  */
end_comment

begin_class
DECL|class|FeatureSpecificTestSuiteBuilderTest
specifier|public
class|class
name|FeatureSpecificTestSuiteBuilderTest
extends|extends
name|TestCase
block|{
DECL|field|testWasRun
specifier|static
name|boolean
name|testWasRun
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
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
name|testWasRun
operator|=
literal|false
expr_stmt|;
block|}
DECL|class|MyAbstractTester
specifier|public
specifier|static
specifier|final
class|class
name|MyAbstractTester
extends|extends
name|AbstractTester
argument_list|<
name|Void
argument_list|>
block|{
DECL|method|testNothing ()
specifier|public
name|void
name|testNothing
parameter_list|()
block|{
name|testWasRun
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|class|MyTestSuiteBuilder
specifier|private
specifier|static
specifier|final
class|class
name|MyTestSuiteBuilder
extends|extends
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|MyTestSuiteBuilder
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|getTesters ()
specifier|protected
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|getTesters
parameter_list|()
block|{
return|return
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
operator|>
name|singletonList
argument_list|(
name|MyAbstractTester
operator|.
name|class
argument_list|)
return|;
block|}
block|}
DECL|method|testLifecycle ()
specifier|public
name|void
name|testLifecycle
parameter_list|()
block|{
specifier|final
name|boolean
name|setUp
index|[]
init|=
block|{
literal|false
block|}
decl_stmt|;
name|Runnable
name|setUpRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|setUp
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|boolean
name|tearDown
index|[]
init|=
block|{
literal|false
block|}
decl_stmt|;
name|Runnable
name|tearDownRunnable
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|tearDown
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
name|MyTestSuiteBuilder
name|builder
init|=
operator|new
name|MyTestSuiteBuilder
argument_list|()
decl_stmt|;
name|Test
name|test
init|=
name|builder
operator|.
name|usingGenerator
argument_list|(
literal|"yam"
argument_list|)
operator|.
name|named
argument_list|(
literal|"yam"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|NONE
argument_list|)
operator|.
name|withSetUp
argument_list|(
name|setUpRunnable
argument_list|)
operator|.
name|withTearDown
argument_list|(
name|tearDownRunnable
argument_list|)
operator|.
name|createTestSuite
argument_list|()
decl_stmt|;
name|TestResult
name|result
init|=
operator|new
name|TestResult
argument_list|()
decl_stmt|;
name|test
operator|.
name|run
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|testWasRun
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|setUp
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tearDown
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

