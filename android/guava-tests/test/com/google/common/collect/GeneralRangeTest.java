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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|BoundType
operator|.
name|CLOSED
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
name|BoundType
operator|.
name|OPEN
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
name|base
operator|.
name|Objects
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
name|testing
operator|.
name|NullPointerTester
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@code GeneralRange}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|GeneralRangeTest
specifier|public
class|class
name|GeneralRangeTest
extends|extends
name|TestCase
block|{
DECL|field|ORDERING
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|Integer
argument_list|>
name|ORDERING
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
DECL|field|IN_ORDER_VALUES
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|IN_ORDER_VALUES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|null
argument_list|,
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
decl_stmt|;
DECL|method|testCreateEmptyRangeFails ()
specifier|public
name|void
name|testCreateEmptyRangeFails
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|lboundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|BoundType
name|uboundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
try|try
block|{
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|4
argument_list|,
name|lboundType
argument_list|,
literal|2
argument_list|,
name|uboundType
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IAE"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
block|}
DECL|method|testCreateEmptyRangeOpenOpenFails ()
specifier|public
name|void
name|testCreateEmptyRangeOpenOpenFails
parameter_list|()
block|{
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
try|try
block|{
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
name|i
argument_list|,
name|OPEN
argument_list|,
name|i
argument_list|,
name|OPEN
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IAE"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
DECL|method|testCreateEmptyRangeClosedOpenSucceeds ()
specifier|public
name|void
name|testCreateEmptyRangeClosedOpenSucceeds
parameter_list|()
block|{
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
name|i
argument_list|,
name|CLOSED
argument_list|,
name|i
argument_list|,
name|OPEN
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|j
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertFalse
argument_list|(
name|range
operator|.
name|contains
argument_list|(
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testCreateEmptyRangeOpenClosedSucceeds ()
specifier|public
name|void
name|testCreateEmptyRangeOpenClosedSucceeds
parameter_list|()
block|{
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
name|i
argument_list|,
name|OPEN
argument_list|,
name|i
argument_list|,
name|CLOSED
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|j
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertFalse
argument_list|(
name|range
operator|.
name|contains
argument_list|(
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testCreateSingletonRangeSucceeds ()
specifier|public
name|void
name|testCreateSingletonRangeSucceeds
parameter_list|()
block|{
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
name|i
argument_list|,
name|CLOSED
argument_list|,
name|i
argument_list|,
name|CLOSED
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|j
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
name|i
argument_list|,
name|j
argument_list|)
argument_list|,
name|range
operator|.
name|contains
argument_list|(
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testSingletonRange ()
specifier|public
name|void
name|testSingletonRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|==
literal|0
argument_list|,
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLowerRange ()
specifier|public
name|void
name|testLowerRange
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|lBoundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|downTo
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|lBoundType
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|>
literal|0
operator|||
operator|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|==
literal|0
operator|&&
name|lBoundType
operator|==
name|CLOSED
operator|)
argument_list|,
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|<
literal|0
operator|||
operator|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|==
literal|0
operator|&&
name|lBoundType
operator|==
name|OPEN
operator|)
argument_list|,
name|range
operator|.
name|tooLow
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|range
operator|.
name|tooHigh
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testUpperRange ()
specifier|public
name|void
name|testUpperRange
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|lBoundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|upTo
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|lBoundType
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|<
literal|0
operator|||
operator|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|==
literal|0
operator|&&
name|lBoundType
operator|==
name|CLOSED
operator|)
argument_list|,
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|>
literal|0
operator|||
operator|(
name|ORDERING
operator|.
name|compare
argument_list|(
name|i
argument_list|,
literal|3
argument_list|)
operator|==
literal|0
operator|&&
name|lBoundType
operator|==
name|OPEN
operator|)
argument_list|,
name|range
operator|.
name|tooHigh
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|range
operator|.
name|tooLow
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testDoublyBoundedAgainstRange ()
specifier|public
name|void
name|testDoublyBoundedAgainstRange
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|lboundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|BoundType
name|uboundType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|Range
operator|.
name|range
argument_list|(
literal|2
argument_list|,
name|lboundType
argument_list|,
literal|4
argument_list|,
name|uboundType
argument_list|)
decl_stmt|;
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|gRange
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|lboundType
argument_list|,
literal|4
argument_list|,
name|uboundType
argument_list|)
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|IN_ORDER_VALUES
control|)
block|{
name|assertEquals
argument_list|(
name|i
operator|!=
literal|null
operator|&&
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|,
name|gRange
operator|.
name|contains
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testIntersectAgainstMatchingEndpointsRange ()
specifier|public
name|void
name|testIntersectAgainstMatchingEndpointsRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersectAgainstBiggerRange ()
specifier|public
name|void
name|testIntersectAgainstBiggerRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|null
argument_list|,
name|OPEN
argument_list|,
literal|5
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|5
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|1
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersectAgainstSmallerRange ()
specifier|public
name|void
name|testIntersectAgainstSmallerRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersectOverlappingRange ()
specifier|public
name|void
name|testIntersectOverlappingRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|CLOSED
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|4
argument_list|,
name|CLOSED
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|5
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|3
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|1
argument_list|,
name|OPEN
argument_list|,
literal|3
argument_list|,
name|OPEN
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersectNonOverlappingRange ()
specifier|public
name|void
name|testIntersectNonOverlappingRange
parameter_list|()
block|{
name|GeneralRange
argument_list|<
name|Integer
argument_list|>
name|range
init|=
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|,
literal|4
argument_list|,
name|CLOSED
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|5
argument_list|,
name|CLOSED
argument_list|,
literal|6
argument_list|,
name|CLOSED
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|range
operator|.
name|intersect
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|1
argument_list|,
name|OPEN
argument_list|,
literal|2
argument_list|,
name|OPEN
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromRangeAll ()
specifier|public
name|void
name|testFromRangeAll
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|all
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|from
argument_list|(
name|Range
operator|.
name|all
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromRangeOneEnd ()
specifier|public
name|void
name|testFromRangeOneEnd
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|endpointType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|upTo
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
literal|3
argument_list|,
name|endpointType
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|from
argument_list|(
name|Range
operator|.
name|upTo
argument_list|(
literal|3
argument_list|,
name|endpointType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|downTo
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
literal|3
argument_list|,
name|endpointType
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|from
argument_list|(
name|Range
operator|.
name|downTo
argument_list|(
literal|3
argument_list|,
name|endpointType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testFromRangeTwoEnds ()
specifier|public
name|void
name|testFromRangeTwoEnds
parameter_list|()
block|{
for|for
control|(
name|BoundType
name|lowerType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|BoundType
name|upperType
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
literal|3
argument_list|,
name|lowerType
argument_list|,
literal|4
argument_list|,
name|upperType
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|from
argument_list|(
name|Range
operator|.
name|range
argument_list|(
literal|3
argument_list|,
name|lowerType
argument_list|,
literal|4
argument_list|,
name|upperType
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testReverse ()
specifier|public
name|void
name|testReverse
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|all
argument_list|(
name|ORDERING
operator|.
name|reverse
argument_list|()
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|all
argument_list|(
name|ORDERING
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|downTo
argument_list|(
name|ORDERING
operator|.
name|reverse
argument_list|()
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|upTo
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|upTo
argument_list|(
name|ORDERING
operator|.
name|reverse
argument_list|()
argument_list|,
literal|3
argument_list|,
name|OPEN
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|downTo
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|OPEN
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
operator|.
name|reverse
argument_list|()
argument_list|,
literal|5
argument_list|,
name|OPEN
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|)
argument_list|,
name|GeneralRange
operator|.
name|range
argument_list|(
name|ORDERING
argument_list|,
literal|3
argument_list|,
name|CLOSED
argument_list|,
literal|5
argument_list|,
name|OPEN
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|GeneralRange
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
