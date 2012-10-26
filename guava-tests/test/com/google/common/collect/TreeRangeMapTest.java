begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Tests for {@code TreeRangeMap}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NavigableMap"
argument_list|)
DECL|class|TreeRangeMapTest
specifier|public
class|class
name|TreeRangeMapTest
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
operator|-
literal|1
decl_stmt|;
DECL|field|MAX_BOUND
specifier|private
specifier|static
specifier|final
name|int
name|MAX_BOUND
init|=
literal|1
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
DECL|method|testAllRangesAlone ()
specifier|public
name|void
name|testAllRangesAlone
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAllRangePairs ()
specifier|public
name|void
name|testAllRangePairs
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testAllRangeTriples ()
specifier|public
name|void
name|testAllRangeTriples
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
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range3
range|:
name|RANGES
control|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|test
operator|.
name|put
argument_list|(
name|range3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testPutAll ()
specifier|public
name|void
name|testPutAll
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
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|range3
range|:
name|RANGES
control|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|range3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test2
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// put range2 and range3 into test2, and then put test2 into test
name|test
operator|.
name|put
argument_list|(
name|range1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test2
operator|.
name|put
argument_list|(
name|range2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|test2
operator|.
name|put
argument_list|(
name|range3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|test
operator|.
name|putAll
argument_list|(
name|test2
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testPutAndRemove ()
specifier|public
name|void
name|testPutAndRemove
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|rangeToPut
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
name|rangeToRemove
range|:
name|RANGES
control|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|rangeToPut
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|removeModel
argument_list|(
name|model
argument_list|,
name|rangeToRemove
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|test
operator|.
name|put
argument_list|(
name|rangeToPut
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test
operator|.
name|remove
argument_list|(
name|rangeToRemove
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testPutTwoAndRemove ()
specifier|public
name|void
name|testPutTwoAndRemove
parameter_list|()
block|{
for|for
control|(
name|Range
argument_list|<
name|Integer
argument_list|>
name|rangeToPut1
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
name|rangeToPut2
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
name|rangeToRemove
range|:
name|RANGES
control|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|rangeToPut1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|putModel
argument_list|(
name|model
argument_list|,
name|rangeToPut2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|removeModel
argument_list|(
name|model
argument_list|,
name|rangeToRemove
argument_list|)
expr_stmt|;
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
init|=
name|TreeRangeMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|test
operator|.
name|put
argument_list|(
name|rangeToPut1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|test
operator|.
name|put
argument_list|(
name|rangeToPut2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|test
operator|.
name|remove
argument_list|(
name|rangeToRemove
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|model
argument_list|,
name|test
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|verify (Map<Integer, Integer> model, RangeMap<Integer, Integer> test)
specifier|private
name|void
name|verify
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
parameter_list|,
name|RangeMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|test
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
operator|-
literal|1
init|;
name|i
operator|<=
name|MAX_BOUND
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|model
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|test
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|Map
operator|.
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
init|=
name|test
operator|.
name|getEntry
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|model
operator|.
name|containsKey
argument_list|(
name|i
argument_list|)
argument_list|,
name|entry
operator|!=
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
name|test
operator|.
name|asMapOfRanges
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
name|asMapOfRanges
argument_list|()
operator|.
name|keySet
argument_list|()
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
block|}
DECL|method|putModel (Map<Integer, Integer> model, Range<Integer> range, int value)
specifier|private
name|void
name|putModel
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
parameter_list|,
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
parameter_list|,
name|int
name|value
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
operator|-
literal|1
init|;
name|i
operator|<=
name|MAX_BOUND
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|model
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|removeModel (Map<Integer, Integer> model, Range<Integer> range)
specifier|private
name|void
name|removeModel
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
parameter_list|,
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
operator|-
literal|1
init|;
name|i
operator|<=
name|MAX_BOUND
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|model
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|retainModel (Map<Integer, Integer> model, Range<Integer> range)
specifier|private
name|void
name|retainModel
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|model
parameter_list|,
name|Range
argument_list|<
name|Integer
argument_list|>
name|range
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|MIN_BOUND
operator|-
literal|1
init|;
name|i
operator|<=
name|MAX_BOUND
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|range
operator|.
name|contains
argument_list|(
name|i
argument_list|)
condition|)
block|{
name|model
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

