begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|DiscreteDomains
operator|.
name|integers
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
name|SampleElements
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
name|TestSetGenerator
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
name|testers
operator|.
name|SetHashCodeTester
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * @author gak@google.com (Gregory Kick)  */
end_comment

begin_class
DECL|class|ContiguousSetNonGwtTest
specifier|public
class|class
name|ContiguousSetNonGwtTest
extends|extends
name|TestCase
block|{
DECL|class|BuiltTests
specifier|public
specifier|static
class|class
name|BuiltTests
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
name|TestIntegerSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
block|{
comment|// reject duplicates at creation, just so that I can use
comment|// that SetFeature below, which stops a test from running
comment|// that doesn't work. hack!
name|SortedSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
operator|new
name|TreeSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|set
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|set
operator|.
name|size
argument_list|()
operator|==
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|Ranges
operator|.
name|closed
argument_list|(
name|set
operator|.
name|first
argument_list|()
argument_list|,
name|set
operator|.
name|last
argument_list|()
argument_list|)
operator|.
name|asSet
argument_list|(
name|integers
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|,
name|CollectionFeature
operator|.
name|NON_STANDARD_TOSTRING
argument_list|,
name|CollectionFeature
operator|.
name|RESTRICTS_ELEMENTS
argument_list|,
name|CollectionFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|)
operator|.
name|suppressing
argument_list|(
name|SetHashCodeTester
operator|.
name|getHashCodeMethods
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"DiscreteRange.asSet, closed"
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
block|}
DECL|class|TestIntegerSetGenerator
specifier|abstract
specifier|static
class|class
name|TestIntegerSetGenerator
implements|implements
name|TestSetGenerator
argument_list|<
name|Integer
argument_list|>
block|{
DECL|method|samples ()
annotation|@
name|Override
specifier|public
name|SampleElements
argument_list|<
name|Integer
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<
name|Integer
argument_list|>
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|)
return|;
block|}
DECL|method|create (Object... elements)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|Integer
index|[]
name|array
init|=
operator|new
name|Integer
index|[
name|elements
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|e
range|:
name|elements
control|)
block|{
name|array
index|[
name|i
operator|++
index|]
operator|=
operator|(
name|Integer
operator|)
name|e
expr_stmt|;
block|}
return|return
name|create
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|create (Integer[] elements)
specifier|protected
specifier|abstract
name|Set
argument_list|<
name|Integer
argument_list|>
name|create
parameter_list|(
name|Integer
index|[]
name|elements
parameter_list|)
function_decl|;
DECL|method|createArray (int length)
annotation|@
name|Override
specifier|public
name|Integer
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Integer
index|[
name|length
index|]
return|;
block|}
DECL|method|order (List<Integer> insertionOrder)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

