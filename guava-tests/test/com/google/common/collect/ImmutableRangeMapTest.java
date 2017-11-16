begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|testing
operator|.
name|CollectorTester
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
name|SerializableTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
comment|/**  * Tests for {@code ImmutableRangeMap}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// NavigableMap
DECL|class|ImmutableRangeMapTest
specifier|public
class|class
name|ImmutableRangeMapTest
extends|extends
name|TestCase
block|{
DECL|field|RANGES
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|RANGES
decl_stmt|;
DECL|field|MIN_BOUND
specifier|private
specifier|static
specifier|final
name|int
name|MIN_BOUND
init|=
literal|0
decl_stmt|;
DECL|field|MAX_BOUND
specifier|private
specifier|static
specifier|final
name|int
name|MAX_BOUND
init|=
literal|10
decl_stmt|;
static|static
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|Range
operator|.
expr|<
name|Integer
operator|>
name|all
argument_list|()
argument_list|)
expr_stmt|;
comment|// Add one-ended ranges
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
init|;
name|i
operator|<=
name|MAX_BOUND
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|BoundType
name|type
range|:
name|BoundType
operator|.
name|values
argument_list|()
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|Range
operator|.
name|upTo
argument_list|(
name|i
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|Range
operator|.
name|downTo
argument_list|(
name|i
argument_list|,
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Add two-ended ranges
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
init|;
name|i
operator|<=
name|MAX_BOUND
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<=
name|MAX_BOUND
condition|;
name|j
operator|++
control|)
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
if|if
condition|(
name|i
operator|==
name|j
operator|&
name|lowerType
operator|==
name|OPEN
operator|&
name|upperType
operator|==
name|OPEN
condition|)
block|{
continue|continue;
block|}
name|builder
operator|.
name|add
argument_list|(
name|Range
operator|.
name|range
argument_list|(
name|i
argument_list|,
name|lowerType
argument_list|,
name|j
argument_list|,
name|upperType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|RANGES
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|testBuilderRejectsEmptyRanges ()
specifier|public
name|void
name|testBuilderRejectsEmptyRanges
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
init|;
name|i
operator|<=
name|MAX_BOUND
condition|;
name|i
operator|++
control|)
block|{
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableRangeMap
operator|.
name|builder
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|put
argument_list|(
name|Range
operator|.
name|closedOpen
argument_list|(
name|i
argument_list|,
name|i
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// success
block|}
try|try
block|{
name|builder
operator|.
name|put
argument_list|(
name|Range
operator|.
name|openClosed
argument_list|(
name|i
argument_list|,
name|i
argument_list|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{       }
block|}
block|}
DECL|method|testOverlapRejection ()
specifier|public
name|void
name|testOverlapRejection
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
name|boolean
name|expectRejection
init|=
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|&&
operator|!
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
decl_stmt|;
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableRangeMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
operator|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
try|try
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|unused
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|expectRejection
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|expectRejection
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
if|if
condition|(
operator|!
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|||
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangeMap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
init|;
name|i
operator|<=
name|MAX_BOUND
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|expectedValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|range1
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|expectedValue
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|range2
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|expectedValue
operator|=
literal|2
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedValue
argument_list|,
name|rangeMap
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|testSpanEmpty ()
specifier|public
name|void
name|testSpanEmpty
parameter_list|()
block|{
try|try
block|{
name|ImmutableRangeMap
operator|.
name|of
argument_list|()
operator|.
name|span
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NoSuchElementException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testSpanSingleRange ()
specifier|public
name|void
name|testSpanSingleRange
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
range|:
name|RANGES
control|)
block|{
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangemap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range
argument_list|,
literal|1
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|range
argument_list|,
name|rangemap
operator|.
name|span
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSpanTwoRanges ()
specifier|public
name|void
name|testSpanTwoRanges
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
if|if
condition|(
operator|!
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|||
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangemap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|range1
operator|.
name|span
argument_list|(
name|range2
argument_list|)
argument_list|,
name|rangemap
operator|.
name|span
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testGetEntry ()
specifier|public
name|void
name|testGetEntry
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
if|if
condition|(
operator|!
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|||
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangeMap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
init|;
name|i
operator|<=
name|MAX_BOUND
condition|;
name|i
operator|++
control|)
block|{
name|Entry
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|expectedEntry
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|range1
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|expectedEntry
operator|=
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|range2
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|expectedEntry
operator|=
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedEntry
argument_list|,
name|rangeMap
operator|.
name|getEntry
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|testGetLargeRangeMap ()
specifier|public
name|void
name|testGetLargeRangeMap
parameter_list|()
block|{
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableRangeMap
operator|.
name|builder
argument_list|()
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
name|builder
operator|.
name|put
argument_list|(
name|Range
operator|.
name|closedOpen
argument_list|(
name|i
argument_list|,
name|i
operator|+
literal|1
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|builder
operator|.
name|build
argument_list|()
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
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AndroidIncompatible
comment|// slow
DECL|method|testAsMapOfRanges ()
specifier|public
name|void
name|testAsMapOfRanges
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
if|if
condition|(
operator|!
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|||
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangeMap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|expectedAsMap
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|,
name|range2
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|asMap
init|=
name|rangeMap
operator|.
name|asMapOfRanges
argument_list|()
decl_stmt|;
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|descendingMap
init|=
name|rangeMap
operator|.
name|asDescendingMapOfRanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedAsMap
argument_list|,
name|asMap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedAsMap
argument_list|,
name|descendingMap
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|asMap
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|descendingMap
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|asMap
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|reverse
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|descendingMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|query
range|:
name|RANGES
control|)
block|{
name|assertEquals
argument_list|(
name|expectedAsMap
operator|.
name|get
argument_list|(
name|query
argument_list|)
argument_list|,
name|asMap
operator|.
name|get
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|testSubRangeMap ()
specifier|public
name|void
name|testSubRangeMap
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range1
range|:
name|RANGES
control|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range2
range|:
name|RANGES
control|)
block|{
if|if
condition|(
operator|!
name|range1
operator|.
name|isConnected
argument_list|(
name|range2
argument_list|)
operator|||
name|range1
operator|.
name|intersection
argument_list|(
name|range2
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|subRange
range|:
name|RANGES
control|)
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangeMap
init|=
name|ImmutableRangeMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|expectedBuilder
init|=
name|ImmutableRangeMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|rangeMap
operator|.
name|asMapOfRanges
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|isConnected
argument_list|(
name|subRange
argument_list|)
operator|&&
operator|!
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|intersection
argument_list|(
name|subRange
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|expectedBuilder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|intersection
argument_list|(
name|subRange
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|expected
init|=
name|expectedBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|rangeMap
operator|.
name|subRangeMap
argument_list|(
name|subRange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|emptyRangeMap
init|=
name|ImmutableRangeMap
operator|.
name|of
argument_list|()
decl_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|emptyRangeMap
argument_list|)
expr_stmt|;
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|nonEmptyRangeMap
init|=
operator|new
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
operator|.
name|put
argument_list|(
name|Range
operator|.
name|closed
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|,
literal|5
argument_list|)
operator|.
name|put
argument_list|(
name|Range
operator|.
name|open
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|)
argument_list|,
literal|3
argument_list|)
operator|.
name|put
argument_list|(
name|Range
operator|.
name|closedOpen
argument_list|(
literal|8
argument_list|,
literal|10
argument_list|)
argument_list|,
literal|4
argument_list|)
operator|.
name|put
argument_list|(
name|Range
operator|.
name|openClosed
argument_list|(
literal|15
argument_list|,
literal|17
argument_list|)
argument_list|,
literal|2
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ImmutableMap
argument_list|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|nonEmptyRangeMap
operator|.
name|asMapOfRanges
argument_list|()
decl_stmt|;
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
range|:
name|test
operator|.
name|keySet
argument_list|()
control|)
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|test
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|nonEmptyRangeMap
argument_list|)
expr_stmt|;
block|}
DECL|method|testToImmutableRangeSet ()
specifier|public
name|void
name|testToImmutableRangeSet
parameter_list|()
block|{
name|Range
argument_list|<
name|Integer
argument_list|>
name|rangeOne
init|=
name|Range
operator|.
name|closedOpen
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|Range
argument_list|<
name|Integer
argument_list|>
name|rangeTwo
init|=
name|Range
operator|.
name|openClosed
argument_list|(
literal|6
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|ImmutableRangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|rangeMap
init|=
operator|new
name|ImmutableRangeMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
operator|.
name|put
argument_list|(
name|rangeOne
argument_list|,
literal|1
argument_list|)
operator|.
name|put
argument_list|(
name|rangeTwo
argument_list|,
literal|6
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|CollectorTester
operator|.
name|of
argument_list|(
name|ImmutableRangeMap
operator|.
expr|<
name|Range
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|,
name|Integer
operator|>
name|toImmutableRangeMap
argument_list|(
name|k
lambda|->
name|k
argument_list|,
name|k
lambda|->
name|k
operator|.
name|lowerEndpoint
argument_list|()
argument_list|)
argument_list|)
operator|.
name|expectCollects
argument_list|(
name|rangeMap
argument_list|,
name|rangeOne
argument_list|,
name|rangeTwo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

