begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|SetTestSuiteBuilder
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
name|TestStringSetGenerator
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
name|CollectionFeature
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
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Tests for CompactHashSet.  *  * @author Dimitris Andreou  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// java.util.Arrays#copyOf(Object[], int), java.lang.reflect.Array
DECL|class|CompactHashSetTest
specifier|public
class|class
name|CompactHashSetTest
extends|extends
name|TestCase
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|List
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|allFeatures
init|=
name|Arrays
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|asList
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|REMOVE_OPERATIONS
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ADD
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_REMOVE
argument_list|)
decl_stmt|;
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|CompactHashSetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|CompactHashSet
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"CompactHashSet"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|allFeatures
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|CompactHashSet
name|set
init|=
name|CompactHashSet
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|set
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|set
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|set
operator|.
name|trimToSize
argument_list|()
expr_stmt|;
return|return
name|set
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"CompactHashSet#TrimToSize"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|allFeatures
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testAllocArraysDefault ()
specifier|public
name|void
name|testAllocArraysDefault
parameter_list|()
block|{
name|CompactHashSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|CompactHashSet
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|needsAllocArrays
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|elements
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|needsAllocArrays
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|elements
argument_list|)
operator|.
name|hasLength
argument_list|(
name|CompactHashSet
operator|.
name|DEFAULT_SIZE
argument_list|)
expr_stmt|;
block|}
DECL|method|testAllocArraysExpectedSize ()
specifier|public
name|void
name|testAllocArraysExpectedSize
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
name|CompactHashSet
operator|.
name|DEFAULT_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|CompactHashSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|CompactHashSet
operator|.
name|createWithExpectedSize
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|needsAllocArrays
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|elements
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|set
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|needsAllocArrays
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|int
name|expectedSize
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|set
operator|.
name|elements
argument_list|)
operator|.
name|hasLength
argument_list|(
name|expectedSize
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

