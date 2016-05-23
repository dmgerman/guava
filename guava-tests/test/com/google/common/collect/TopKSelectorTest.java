begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|math
operator|.
name|IntMath
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
name|primitives
operator|.
name|Ints
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
name|math
operator|.
name|RoundingMode
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
name|Comparator
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
comment|/**  * Tests for {@code TopKSelector}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|TopKSelectorTest
specifier|public
class|class
name|TopKSelectorTest
extends|extends
name|TestCase
block|{
DECL|method|testNegativeK ()
specifier|public
name|void
name|testNegativeK
parameter_list|()
block|{
try|try
block|{
name|TopKSelector
operator|.
name|least
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|TopKSelector
operator|.
name|greatest
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|TopKSelector
operator|.
name|least
argument_list|(
operator|-
literal|1
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|TopKSelector
operator|.
name|greatest
argument_list|(
operator|-
literal|1
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testZeroK ()
specifier|public
name|void
name|testZeroK
parameter_list|()
block|{
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|0
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
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|top
operator|.
name|offer
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testNoElementsOffered ()
specifier|public
name|void
name|testNoElementsOffered
parameter_list|()
block|{
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testOfferedFewerThanK ()
specifier|public
name|void
name|testOfferedFewerThanK
parameter_list|()
block|{
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|top
operator|.
name|offer
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|top
operator|.
name|offer
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|top
operator|.
name|offer
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testOfferedKPlusOne ()
specifier|public
name|void
name|testOfferedKPlusOne
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|list
range|:
name|Collections2
operator|.
name|permutations
argument_list|(
name|Ints
operator|.
name|asList
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
argument_list|)
control|)
block|{
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|top
operator|.
name|offerAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testOfferedThreeK ()
specifier|public
name|void
name|testOfferedThreeK
parameter_list|()
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|list
range|:
name|Collections2
operator|.
name|permutations
argument_list|(
name|Ints
operator|.
name|asList
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
argument_list|,
literal|6
argument_list|)
argument_list|)
control|)
block|{
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|top
operator|.
name|offerAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testDifferentComparator ()
specifier|public
name|void
name|testDifferentComparator
parameter_list|()
block|{
name|TopKSelector
argument_list|<
name|String
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
literal|3
argument_list|,
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|top
operator|.
name|offerAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"B"
argument_list|,
literal|"c"
argument_list|,
literal|"D"
argument_list|,
literal|"e"
argument_list|,
literal|"F"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"B"
argument_list|,
literal|"c"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testWorstCase ()
specifier|public
name|void
name|testWorstCase
parameter_list|()
block|{
name|int
name|n
init|=
literal|2000000
decl_stmt|;
name|int
name|k
init|=
literal|200000
decl_stmt|;
specifier|final
name|long
index|[]
name|compareCalls
init|=
block|{
literal|0
block|}
decl_stmt|;
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|cmp
init|=
operator|new
name|Comparator
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Integer
name|o1
parameter_list|,
name|Integer
name|o2
parameter_list|)
block|{
name|compareCalls
index|[
literal|0
index|]
operator|++
expr_stmt|;
return|return
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|TopKSelector
argument_list|<
name|Integer
argument_list|>
name|top
init|=
name|TopKSelector
operator|.
name|least
argument_list|(
name|k
argument_list|,
name|cmp
argument_list|)
decl_stmt|;
name|top
operator|.
name|offer
argument_list|(
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
name|top
operator|.
name|offer
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|top
operator|.
name|topK
argument_list|()
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|Collections
operator|.
name|nCopies
argument_list|(
name|k
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|compareCalls
index|[
literal|0
index|]
argument_list|)
operator|.
name|isAtMost
argument_list|(
literal|10L
operator|*
name|n
operator|*
name|IntMath
operator|.
name|log2
argument_list|(
name|k
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

