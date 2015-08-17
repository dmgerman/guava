begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * an Iterator implementation.  *  *<p>At least, it will do when it's finished.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|IteratorTestSuiteBuilder
specifier|public
class|class
name|IteratorTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|IteratorTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|TestIteratorGenerator
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|method|getTesters ()
annotation|@
name|Override
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
name|ExampleIteratorTester
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

