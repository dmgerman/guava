begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|java
operator|.
name|util
operator|.
name|HashSet
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
comment|/**  * Unit tests for {@link Sets#union}, {@link Sets#intersection} and  * {@link Sets#difference}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SetOperationsTest
specifier|public
class|class
name|SetOperationsTest
extends|extends
name|TestCase
block|{
annotation|@
name|GwtIncompatible
comment|// suite
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
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
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|,
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"empty U empty"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|checkArgument
argument_list|(
name|elements
operator|.
name|length
operator|==
literal|1
argument_list|)
expr_stmt|;
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
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
literal|"singleton U itself"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|,
name|Sets
operator|.
name|newHashSet
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
literal|"empty U set"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|,
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set U empty"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|checkArgument
argument_list|(
name|elements
operator|.
name|length
operator|==
literal|3
argument_list|)
expr_stmt|;
comment|// Put the sets in different orders for the hell of it
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
argument_list|,
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|asList
argument_list|(
name|elements
index|[
literal|1
index|]
argument_list|,
name|elements
index|[
literal|0
index|]
argument_list|,
name|elements
index|[
literal|2
index|]
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set U itself"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|checkArgument
argument_list|(
name|elements
operator|.
name|length
operator|==
literal|3
argument_list|)
expr_stmt|;
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
index|[
literal|1
index|]
argument_list|,
name|elements
index|[
literal|2
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"union of disjoint"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|union
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|(
name|elements
index|[
literal|0
index|]
argument_list|,
name|elements
index|[
literal|1
index|]
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
index|[
literal|1
index|]
argument_list|,
name|elements
index|[
literal|2
index|]
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"venn"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|,
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"empty& empty"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"empty& singleton"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"c"
argument_list|,
literal|"d"
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"intersection of disjoint"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
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
literal|"set& itself"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|,
name|elements
index|[
literal|0
index|]
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"c"
argument_list|,
name|elements
index|[
literal|0
index|]
argument_list|,
literal|"d"
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"intersection with overlap of one"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|,
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"empty - empty"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"singleton - itself"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"b"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|other
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|set
argument_list|,
name|other
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set - superset"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ZERO
argument_list|,
name|CollectionFeature
operator|.
name|NONE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|other
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"wz"
argument_list|,
literal|"xq"
argument_list|)
decl_stmt|;
name|set
operator|.
name|addAll
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|other
operator|.
name|add
argument_list|(
literal|"pq"
argument_list|)
expr_stmt|;
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|set
argument_list|,
name|other
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set - set"
argument_list|)
operator|.
name|withFeatures
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
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set - empty"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
return|return
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
expr|<
name|String
operator|>
name|newHashSet
argument_list|(
name|elements
argument_list|)
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"xx"
argument_list|,
literal|"xq"
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"set - disjoint"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|MoreTests
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|class|MoreTests
specifier|public
specifier|static
class|class
name|MoreTests
extends|extends
name|TestCase
block|{
DECL|field|friends
name|Set
argument_list|<
name|String
argument_list|>
name|friends
decl_stmt|;
DECL|field|enemies
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|friends
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
expr_stmt|;
name|enemies
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnion ()
specifier|public
name|void
name|testUnion
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|all
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|all
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Buck"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|all
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersection ()
specifier|public
name|void
name|testIntersection
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|frenemies
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|frenemies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Joe"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|frenemies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifference ()
specifier|public
name|void
name|testDifference
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|goodFriends
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|goodFriends
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|goodFriends
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSymmetricDifference ()
specifier|public
name|void
name|testSymmetricDifference
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|symmetricDifferenceFriendsFirst
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|symmetricDifferenceFriendsFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|symmetricDifferenceEnemiesFirst
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|symmetricDifferenceEnemiesFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|symmetricDifferenceFriendsFirst
argument_list|,
name|symmetricDifferenceEnemiesFirst
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|symmetricDifferenceFriendsFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|immut
operator|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
operator|.
name|immutableCopy
argument_list|()
expr_stmt|;
name|mut
operator|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|friends
operator|.
name|add
argument_list|(
literal|"Harry"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|symmetricDifferenceEnemiesFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
