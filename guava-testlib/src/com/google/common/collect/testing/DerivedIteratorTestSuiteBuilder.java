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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
comment|/**  * Given a test iterable generator, builds a test suite for the  * iterable's iterator, by delegating to a {@link IteratorTestSuiteBuilder}.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|DerivedIteratorTestSuiteBuilder
specifier|public
class|class
name|DerivedIteratorTestSuiteBuilder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|DerivedIteratorTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|,
name|TestSubjectGenerator
argument_list|<
name|?
extends|extends
name|Iterable
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
block|{
comment|/**    * We rely entirely on the delegate builder for test creation, so this    * just throws UnsupportedOperationException.    *    * @return never.    */
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|createTestSuite ()
annotation|@
name|Override
specifier|public
name|TestSuite
name|createTestSuite
parameter_list|()
block|{
name|checkCanCreate
argument_list|()
expr_stmt|;
return|return
operator|new
name|IteratorTestSuiteBuilder
argument_list|<
name|E
argument_list|>
argument_list|()
operator|.
name|named
argument_list|(
name|getName
argument_list|()
operator|+
literal|" iterator"
argument_list|)
operator|.
name|usingGenerator
argument_list|(
operator|new
name|DerivedTestIteratorGenerator
argument_list|<
name|E
argument_list|>
argument_list|(
name|getSubjectGenerator
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
block|}
end_class

end_unit

