begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * Base class for {@link RangeSet} tests.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// TreeRangeSet
DECL|class|AbstractRangeSetTest
specifier|public
specifier|abstract
class|class
name|AbstractRangeSetTest
extends|extends
name|TestCase
block|{
DECL|method|testInvariants (RangeSet<?> rangeSet)
specifier|public
specifier|static
name|void
name|testInvariants
parameter_list|(
name|RangeSet
argument_list|<
name|?
argument_list|>
name|rangeSet
parameter_list|)
block|{
name|testInvariantsInternal
argument_list|(
name|rangeSet
argument_list|)
expr_stmt|;
name|testInvariantsInternal
argument_list|(
name|rangeSet
operator|.
name|complement
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvariantsInternal (RangeSet<C> rangeSet)
specifier|private
specifier|static
parameter_list|<
name|C
extends|extends
name|Comparable
parameter_list|>
name|void
name|testInvariantsInternal
parameter_list|(
name|RangeSet
argument_list|<
name|C
argument_list|>
name|rangeSet
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|rangeSet
operator|.
name|asRanges
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|rangeSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rangeSet
operator|.
name|asDescendingSetOfRanges
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|,
name|rangeSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|!
name|rangeSet
operator|.
name|asRanges
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|,
name|rangeSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|!
name|rangeSet
operator|.
name|asDescendingSetOfRanges
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
argument_list|,
name|rangeSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|asRanges
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|rangeSet
operator|.
name|asRanges
argument_list|()
argument_list|)
decl_stmt|;
comment|// test that connected ranges are coalesced
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|+
literal|1
operator|<
name|asRanges
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Range
argument_list|<
name|C
argument_list|>
name|range1
init|=
name|asRanges
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Range
argument_list|<
name|C
argument_list|>
name|range2
init|=
name|asRanges
operator|.
name|get
argument_list|(
name|i
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// test that there are no empty ranges
for|for
control|(
name|Range
argument_list|<
name|C
argument_list|>
name|range
range|:
name|asRanges
control|)
block|{
name|assertFalse
argument_list|(
name|range
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// test that the RangeSet's span is the span of all the ranges
name|Iterator
argument_list|<
name|Range
argument_list|<
name|C
argument_list|>
argument_list|>
name|itr
init|=
name|rangeSet
operator|.
name|asRanges
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Range
argument_list|<
name|C
argument_list|>
name|expectedSpan
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|expectedSpan
operator|=
name|itr
operator|.
name|next
argument_list|()
expr_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|expectedSpan
operator|=
name|expectedSpan
operator|.
name|span
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|Range
argument_list|<
name|C
argument_list|>
name|span
init|=
name|rangeSet
operator|.
name|span
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedSpan
argument_list|,
name|span
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
name|assertNull
argument_list|(
name|expectedSpan
argument_list|)
expr_stmt|;
block|}
comment|// test that asDescendingSetOfRanges is the reverse of asRanges
name|assertEquals
argument_list|(
name|Lists
operator|.
name|reverse
argument_list|(
name|asRanges
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|rangeSet
operator|.
name|asDescendingSetOfRanges
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

