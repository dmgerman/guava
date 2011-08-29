begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|Function
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
name|Predicate
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
name|Maps
operator|.
name|EntryTransformer
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
name|Maps
operator|.
name|ValueDifferenceImpl
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
name|SortedMapInterfaceTest
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
name|EqualsTester
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
name|Comparator
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
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests for SortedMaps.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SortedMapsTest
specifier|public
class|class
name|SortedMapsTest
extends|extends
name|TestCase
block|{
DECL|field|ALWAYS_NULL
specifier|private
specifier|static
specifier|final
name|EntryTransformer
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
name|ALWAYS_NULL
init|=
operator|new
name|EntryTransformer
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|transformEntry
parameter_list|(
name|Object
name|k
parameter_list|,
name|Object
name|v1
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullPointer ()
specifier|public
name|void
name|testNullPointer
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|nullPointerTester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|nullPointerTester
operator|.
name|setDefault
argument_list|(
name|EntryTransformer
operator|.
name|class
argument_list|,
name|ALWAYS_NULL
argument_list|)
expr_stmt|;
name|nullPointerTester
operator|.
name|setDefault
argument_list|(
name|SortedMap
operator|.
name|class
argument_list|,
name|Maps
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|newTreeMap
argument_list|()
argument_list|)
expr_stmt|;
name|nullPointerTester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|SortedMaps
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformSortedValues ()
specifier|public
name|void
name|testTransformSortedValues
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|4
argument_list|,
literal|"b"
argument_list|,
literal|9
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Integer
argument_list|,
name|Double
argument_list|>
name|sqrt
init|=
operator|new
name|Function
argument_list|<
name|Integer
argument_list|,
name|Double
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Double
name|apply
parameter_list|(
name|Integer
name|in
parameter_list|)
block|{
return|return
name|Math
operator|.
name|sqrt
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
name|transformed
init|=
name|SortedMaps
operator|.
name|transformValues
argument_list|(
name|map
argument_list|,
name|sqrt
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|2.0
argument_list|,
literal|"b"
argument_list|,
literal|3.0
argument_list|)
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransformSortedEntries ()
specifier|public
name|void
name|testTransformSortedEntries
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"4"
argument_list|,
literal|"b"
argument_list|,
literal|"9"
argument_list|)
decl_stmt|;
name|EntryTransformer
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|String
argument_list|>
name|concat
init|=
operator|new
name|EntryTransformer
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|transformEntry
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|key
operator|+
name|value
return|;
block|}
block|}
decl_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformed
init|=
name|SortedMaps
operator|.
name|transformEntries
argument_list|(
name|map
argument_list|,
name|concat
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"a4"
argument_list|,
literal|"b"
argument_list|,
literal|"b9"
argument_list|)
argument_list|,
name|transformed
argument_list|)
expr_stmt|;
block|}
DECL|field|EMPTY
specifier|private
specifier|static
specifier|final
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|EMPTY
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
DECL|field|SINGLETON
specifier|private
specifier|static
specifier|final
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|SINGLETON
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
decl_stmt|;
DECL|method|testMapDifferenceEmptyEmpty ()
specifier|public
name|void
name|testMapDifferenceEmptyEmpty
parameter_list|()
block|{
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|diff
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|EMPTY
argument_list|,
name|EMPTY
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|diff
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesOnlyOnLeft
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesOnlyOnRight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesInCommon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesDiffering
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"equal"
argument_list|,
name|diff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMapDifferenceEmptySingleton ()
specifier|public
name|void
name|testMapDifferenceEmptySingleton
parameter_list|()
block|{
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|diff
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|EMPTY
argument_list|,
name|SINGLETON
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|diff
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesOnlyOnLeft
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SINGLETON
argument_list|,
name|diff
operator|.
name|entriesOnlyOnRight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesInCommon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesDiffering
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"not equal: only on right={1=2}"
argument_list|,
name|diff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMapDifferenceSingletonEmpty ()
specifier|public
name|void
name|testMapDifferenceSingletonEmpty
parameter_list|()
block|{
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|diff
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|SINGLETON
argument_list|,
name|EMPTY
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|diff
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SINGLETON
argument_list|,
name|diff
operator|.
name|entriesOnlyOnLeft
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesOnlyOnRight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesInCommon
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EMPTY
argument_list|,
name|diff
operator|.
name|entriesDiffering
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"not equal: only on left={1=2}"
argument_list|,
name|diff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMapDifferenceTypical ()
specifier|public
name|void
name|testMapDifferenceTypical
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|left
init|=
name|ImmutableSortedMap
operator|.
expr|<
name|Integer
decl_stmt|,
name|String
decl|>
name|reverseOrder
argument_list|()
decl|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|)
decl|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"b"
argument_list|)
decl|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"c"
argument_list|)
decl|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"d"
argument_list|)
decl|.
name|put
argument_list|(
literal|5
argument_list|,
literal|"e"
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|right
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|3
argument_list|,
literal|"f"
argument_list|,
literal|5
argument_list|,
literal|"g"
argument_list|,
literal|6
argument_list|,
literal|"z"
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|diff1
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|diff1
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesOnlyOnLeft
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|4
argument_list|,
literal|"d"
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|2
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesOnlyOnRight
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|6
argument_list|,
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesInCommon
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesDiffering
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|5
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"e"
argument_list|,
literal|"g"
argument_list|)
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|3
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"c"
argument_list|,
literal|"f"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"not equal: only on left={4=d, 2=b}: only on right={6=z}: "
operator|+
literal|"value differences={5=(e, g), 3=(c, f)}"
argument_list|,
name|diff1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|diff2
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|right
argument_list|,
name|left
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|diff2
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff2
operator|.
name|entriesOnlyOnLeft
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|6
argument_list|,
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff2
operator|.
name|entriesOnlyOnRight
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|2
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|4
argument_list|,
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesInCommon
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|3
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"f"
argument_list|,
literal|"c"
argument_list|)
argument_list|,
literal|5
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"g"
argument_list|,
literal|"e"
argument_list|)
argument_list|)
argument_list|,
name|diff2
operator|.
name|entriesDiffering
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"not equal: only on left={6=z}: only on right={2=b, 4=d}: "
operator|+
literal|"value differences={3=(f, c), 5=(g, e)}"
argument_list|,
name|diff2
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMapDifferenceImmutable ()
specifier|public
name|void
name|testMapDifferenceImmutable
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|left
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|2
argument_list|,
literal|"b"
argument_list|,
literal|3
argument_list|,
literal|"c"
argument_list|,
literal|4
argument_list|,
literal|"d"
argument_list|,
literal|5
argument_list|,
literal|"e"
argument_list|)
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|right
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|3
argument_list|,
literal|"f"
argument_list|,
literal|5
argument_list|,
literal|"g"
argument_list|,
literal|6
argument_list|,
literal|"z"
argument_list|)
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|diff1
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
decl_stmt|;
name|left
operator|.
name|put
argument_list|(
literal|6
argument_list|,
literal|"z"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|diff1
operator|.
name|areEqual
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesOnlyOnLeft
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|2
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|4
argument_list|,
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesOnlyOnRight
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|6
argument_list|,
literal|"z"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesInCommon
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|diff1
operator|.
name|entriesDiffering
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|3
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"c"
argument_list|,
literal|"f"
argument_list|)
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|5
argument_list|,
name|ValueDifferenceImpl
operator|.
name|create
argument_list|(
literal|"e"
argument_list|,
literal|"g"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|diff1
operator|.
name|entriesInCommon
argument_list|()
operator|.
name|put
argument_list|(
literal|7
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|diff1
operator|.
name|entriesOnlyOnLeft
argument_list|()
operator|.
name|put
argument_list|(
literal|7
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
try|try
block|{
name|diff1
operator|.
name|entriesOnlyOnRight
argument_list|()
operator|.
name|put
argument_list|(
literal|7
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testMapDifferenceEquals ()
specifier|public
name|void
name|testMapDifferenceEquals
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|left
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|2
argument_list|,
literal|"b"
argument_list|,
literal|3
argument_list|,
literal|"c"
argument_list|,
literal|4
argument_list|,
literal|"d"
argument_list|,
literal|5
argument_list|,
literal|"e"
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|right
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|3
argument_list|,
literal|"f"
argument_list|,
literal|5
argument_list|,
literal|"g"
argument_list|,
literal|6
argument_list|,
literal|"z"
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|right2
init|=
name|ImmutableSortedMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"a"
argument_list|,
literal|3
argument_list|,
literal|"h"
argument_list|,
literal|5
argument_list|,
literal|"g"
argument_list|,
literal|6
argument_list|,
literal|"z"
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|original
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|same
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|reverse
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|right
argument_list|,
name|left
argument_list|)
decl_stmt|;
name|SortedMapDifference
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|diff2
init|=
name|SortedMaps
operator|.
name|difference
argument_list|(
name|left
argument_list|,
name|right2
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|original
argument_list|,
name|same
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|reverse
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|diff2
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
comment|// Not testing Map methods of SortedMaps.filter*, since the implementation
comment|// doesn't override Maps.FilteredEntryMap, which is already tested.
DECL|field|EVEN
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Integer
argument_list|>
name|EVEN
init|=
operator|new
name|Predicate
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Integer
name|input
parameter_list|)
block|{
return|return
name|input
operator|%
literal|2
operator|==
literal|0
return|;
block|}
block|}
decl_stmt|;
DECL|method|testFilterKeys ()
specifier|public
name|void
name|testFilterKeys
parameter_list|()
block|{
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
init|=
name|Ordering
operator|.
name|natural
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|unfiltered
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|5
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|6
argument_list|,
literal|"six"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|7
argument_list|,
literal|"seven"
argument_list|)
expr_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|filtered
init|=
name|SortedMaps
operator|.
name|filterKeys
argument_list|(
name|unfiltered
argument_list|,
name|EVEN
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|comparator
argument_list|,
name|filtered
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|2
argument_list|,
name|filtered
operator|.
name|firstKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|6
argument_list|,
name|filtered
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|headMap
argument_list|(
literal|5
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|tailMap
argument_list|(
literal|3
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|4
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|subMap
argument_list|(
literal|3
argument_list|,
literal|5
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
DECL|field|NOT_LENGTH_3
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|NOT_LENGTH_3
init|=
operator|new
name|Predicate
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|==
literal|null
operator|||
name|input
operator|.
name|length
argument_list|()
operator|!=
literal|3
return|;
block|}
block|}
decl_stmt|;
DECL|method|testFilterValues ()
specifier|public
name|void
name|testFilterValues
parameter_list|()
block|{
name|Comparator
argument_list|<
name|Integer
argument_list|>
name|comparator
init|=
name|Ordering
operator|.
name|natural
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|unfiltered
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|comparator
argument_list|)
decl_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|5
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|6
argument_list|,
literal|"six"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|7
argument_list|,
literal|"seven"
argument_list|)
expr_stmt|;
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|filtered
init|=
name|SortedMaps
operator|.
name|filterValues
argument_list|(
name|unfiltered
argument_list|,
name|NOT_LENGTH_3
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|comparator
argument_list|,
name|filtered
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|3
argument_list|,
name|filtered
operator|.
name|firstKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|7
argument_list|,
name|filtered
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|headMap
argument_list|(
literal|5
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|tailMap
argument_list|(
literal|4
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|4
argument_list|,
literal|5
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|filtered
operator|.
name|subMap
argument_list|(
literal|4
argument_list|,
literal|6
argument_list|)
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|4
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
DECL|field|EVEN_AND_LENGTH_3
name|EVEN_AND_LENGTH_3
init|=
operator|new
name|Predicate
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|(
name|entry
operator|.
name|getKey
argument_list|()
operator|==
literal|null
operator|||
name|entry
operator|.
name|getKey
argument_list|()
operator|%
literal|2
operator|==
literal|0
operator|)
operator|&&
operator|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
operator|||
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|3
operator|)
return|;
block|}
block|}
decl_stmt|;
DECL|class|ContainsKeySafeSortedMap
specifier|private
specifier|static
class|class
name|ContainsKeySafeSortedMap
extends|extends
name|ForwardingSortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
block|{
DECL|field|delegate
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|delegate
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
comment|// Needed by MapInterfaceTest.testContainsKey()
DECL|method|containsKey (Object key)
annotation|@
name|Override
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
try|try
block|{
return|return
name|super
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
DECL|class|FilteredEntriesSortedMapInterfaceTest
specifier|public
specifier|static
class|class
name|FilteredEntriesSortedMapInterfaceTest
extends|extends
name|SortedMapInterfaceTest
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
block|{
DECL|method|FilteredEntriesSortedMapInterfaceTest ()
specifier|public
name|FilteredEntriesSortedMapInterfaceTest
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|makeEmptyMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|makeEmptyMap
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|unfiltered
init|=
operator|new
name|ContainsKeySafeSortedMap
argument_list|()
decl_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|5
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
return|return
name|SortedMaps
operator|.
name|filterEntries
argument_list|(
name|unfiltered
argument_list|,
name|EVEN_AND_LENGTH_3
argument_list|)
return|;
block|}
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
name|SortedMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|unfiltered
init|=
operator|new
name|ContainsKeySafeSortedMap
argument_list|()
decl_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|2
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"four"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|5
argument_list|,
literal|"five"
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|6
argument_list|,
literal|"six"
argument_list|)
expr_stmt|;
return|return
name|SortedMaps
operator|.
name|filterEntries
argument_list|(
name|unfiltered
argument_list|,
name|EVEN_AND_LENGTH_3
argument_list|)
return|;
block|}
DECL|method|getKeyNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Integer
name|getKeyNotInPopulatedMap
parameter_list|()
block|{
return|return
literal|10
return|;
block|}
DECL|method|getValueNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getValueNotInPopulatedMap
parameter_list|()
block|{
return|return
literal|"ten"
return|;
block|}
comment|// Iterators don't support remove.
DECL|method|testEntrySetIteratorRemove ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetIteratorRemove
parameter_list|()
block|{}
DECL|method|testValuesIteratorRemove ()
annotation|@
name|Override
specifier|public
name|void
name|testValuesIteratorRemove
parameter_list|()
block|{}
comment|// These tests fail on GWT.
comment|// TODO: Investigate why.
DECL|method|testEntrySetRemoveAll ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetRemoveAll
parameter_list|()
block|{}
DECL|method|testEntrySetRetainAll ()
annotation|@
name|Override
specifier|public
name|void
name|testEntrySetRetainAll
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

