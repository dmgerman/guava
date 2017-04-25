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
name|features
operator|.
name|CollectionSize
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
name|features
operator|.
name|Feature
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
name|features
operator|.
name|FeatureUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * This builder creates a composite test suite, containing a separate test suite  * for each {@link CollectionSize} present in the features specified  * by {@link #withFeatures(Feature...)}.  *  * @param<B> The concrete type of this builder (the 'self-type'). All the  * Builder methods of this class (such as {@link #named(String)}) return this  * type, so that Builder methods of more derived classes can be chained onto  * them without casting.  * @param<G> The type of the generator to be passed to testers in the  * generated test suite. An instance of G should somehow provide an  * instance of the class under test, plus any other information required  * to parameterize the test.  *  * @see FeatureSpecificTestSuiteBuilder  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|PerCollectionSizeTestSuiteBuilder
specifier|public
specifier|abstract
class|class
name|PerCollectionSizeTestSuiteBuilder
parameter_list|<
name|B
extends|extends
name|PerCollectionSizeTestSuiteBuilder
parameter_list|<
name|B
parameter_list|,
name|G
parameter_list|,
name|T
parameter_list|,
name|E
parameter_list|>
parameter_list|,
name|G
extends|extends
name|TestContainerGenerator
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
parameter_list|,
name|T
parameter_list|,
name|E
parameter_list|>
extends|extends
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|B
argument_list|,
name|G
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|PerCollectionSizeTestSuiteBuilder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Creates a runnable JUnit test suite based on the criteria already given.    */
annotation|@
name|Override
DECL|method|createTestSuite ()
specifier|public
name|TestSuite
name|createTestSuite
parameter_list|()
block|{
name|checkCanCreate
argument_list|()
expr_stmt|;
name|String
name|name
init|=
name|getName
argument_list|()
decl_stmt|;
comment|// Copy this set, so we can modify it.
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|getFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
init|=
name|getTesters
argument_list|()
decl_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|" Testing: "
operator|+
name|name
argument_list|)
expr_stmt|;
comment|// Split out all the specified sizes.
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|sizesToTest
init|=
name|Helpers
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|copyToSet
argument_list|(
name|CollectionSize
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|sizesToTest
operator|.
name|retainAll
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|features
operator|.
name|removeAll
argument_list|(
name|sizesToTest
argument_list|)
expr_stmt|;
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|sizesToTest
argument_list|)
expr_stmt|;
name|sizesToTest
operator|.
name|retainAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|"   Sizes: "
operator|+
name|formatFeatureSet
argument_list|(
name|sizesToTest
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|sizesToTest
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|name
operator|+
literal|": no CollectionSizes specified (check the argument to "
operator|+
literal|"FeatureSpecificTestSuiteBuilder.withFeatures().)"
argument_list|)
throw|;
block|}
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|Feature
argument_list|<
name|?
argument_list|>
name|collectionSize
range|:
name|sizesToTest
control|)
block|{
name|String
name|oneSizeName
init|=
name|Platform
operator|.
name|format
argument_list|(
literal|"%s [collection size: %s]"
argument_list|,
name|name
argument_list|,
name|collectionSize
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
name|OneSizeGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|oneSizeGenerator
init|=
operator|new
name|OneSizeGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
argument_list|(
name|getSubjectGenerator
argument_list|()
argument_list|,
operator|(
name|CollectionSize
operator|)
name|collectionSize
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|oneSizeFeatures
init|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|features
argument_list|)
decl_stmt|;
name|oneSizeFeatures
operator|.
name|add
argument_list|(
name|collectionSize
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Method
argument_list|>
name|oneSizeSuppressedTests
init|=
name|getSuppressedTests
argument_list|()
decl_stmt|;
name|OneSizeTestSuiteBuilder
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|oneSizeBuilder
init|=
operator|new
name|OneSizeTestSuiteBuilder
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
argument_list|(
name|testers
argument_list|)
operator|.
name|named
argument_list|(
name|oneSizeName
argument_list|)
operator|.
name|usingGenerator
argument_list|(
name|oneSizeGenerator
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|oneSizeFeatures
argument_list|)
operator|.
name|withSetUp
argument_list|(
name|getSetUp
argument_list|()
argument_list|)
operator|.
name|withTearDown
argument_list|(
name|getTearDown
argument_list|()
argument_list|)
operator|.
name|suppressing
argument_list|(
name|oneSizeSuppressedTests
argument_list|)
decl_stmt|;
name|TestSuite
name|oneSizeSuite
init|=
name|oneSizeBuilder
operator|.
name|createTestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|oneSizeSuite
argument_list|)
expr_stmt|;
for|for
control|(
name|TestSuite
name|derivedSuite
range|:
name|createDerivedSuites
argument_list|(
name|oneSizeBuilder
argument_list|)
control|)
block|{
name|oneSizeSuite
operator|.
name|addTest
argument_list|(
name|derivedSuite
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|suite
return|;
block|}
DECL|method|createDerivedSuites ( FeatureSpecificTestSuiteBuilder<?, ? extends OneSizeTestContainerGenerator<T, E>> parentBuilder)
specifier|protected
name|List
argument_list|<
name|TestSuite
argument_list|>
name|createDerivedSuites
parameter_list|(
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|?
argument_list|,
name|?
extends|extends
name|OneSizeTestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
argument_list|>
name|parentBuilder
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|TestSuite
argument_list|>
argument_list|()
return|;
block|}
comment|/** Builds a test suite for one particular {@link CollectionSize}. */
DECL|class|OneSizeTestSuiteBuilder
specifier|private
specifier|static
specifier|final
class|class
name|OneSizeTestSuiteBuilder
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
extends|extends
name|FeatureSpecificTestSuiteBuilder
argument_list|<
name|OneSizeTestSuiteBuilder
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
argument_list|,
name|OneSizeGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
argument_list|>
block|{
DECL|field|testers
specifier|private
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
decl_stmt|;
DECL|method|OneSizeTestSuiteBuilder (List<Class<? extends AbstractTester>> testers)
specifier|public
name|OneSizeTestSuiteBuilder
parameter_list|(
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
parameter_list|)
block|{
name|this
operator|.
name|testers
operator|=
name|testers
expr_stmt|;
block|}
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
name|testers
return|;
block|}
block|}
block|}
end_class

end_unit
