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
name|testers
operator|.
name|CollectionAddAllTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionAddTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionClearTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionContainsAllTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionContainsTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionCreationTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionEqualsTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionIsEmptyTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionIteratorTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionRemoveAllTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionRemoveTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionRetainAllTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionSerializationTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionSizeTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionToArrayTester
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
name|collect
operator|.
name|testing
operator|.
name|testers
operator|.
name|CollectionToStringTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  * Abstract superclass of all test-suite builders for collection interfaces.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|AbstractCollectionTestSuiteBuilder
specifier|public
specifier|abstract
class|class
name|AbstractCollectionTestSuiteBuilder
parameter_list|<
name|B
extends|extends
name|AbstractCollectionTestSuiteBuilder
parameter_list|<
name|B
parameter_list|,
name|E
parameter_list|>
parameter_list|,
name|E
parameter_list|>
extends|extends
name|PerCollectionSizeTestSuiteBuilder
argument_list|<
name|B
argument_list|,
name|TestCollectionGenerator
argument_list|<
name|E
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|E
argument_list|>
argument_list|,
name|E
argument_list|>
block|{
comment|// Class parameters must be raw.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|Arrays
operator|.
expr|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
operator|>
name|asList
argument_list|(
name|CollectionAddAllTester
operator|.
name|class
argument_list|,
name|CollectionAddTester
operator|.
name|class
argument_list|,
name|CollectionClearTester
operator|.
name|class
argument_list|,
name|CollectionContainsAllTester
operator|.
name|class
argument_list|,
name|CollectionContainsTester
operator|.
name|class
argument_list|,
name|CollectionCreationTester
operator|.
name|class
argument_list|,
name|CollectionEqualsTester
operator|.
name|class
argument_list|,
name|CollectionIsEmptyTester
operator|.
name|class
argument_list|,
name|CollectionIteratorTester
operator|.
name|class
argument_list|,
name|CollectionRemoveAllTester
operator|.
name|class
argument_list|,
name|CollectionRemoveTester
operator|.
name|class
argument_list|,
name|CollectionRetainAllTester
operator|.
name|class
argument_list|,
name|CollectionSerializationTester
operator|.
name|class
argument_list|,
name|CollectionSizeTester
operator|.
name|class
argument_list|,
name|CollectionToArrayTester
operator|.
name|class
argument_list|,
name|CollectionToStringTester
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

