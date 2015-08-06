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
name|google
operator|.
name|SortedMultisetTestSuiteBuilder
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
name|google
operator|.
name|TestStringMultisetGenerator
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
name|TestSuite
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
name|Collection
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
name|NavigableSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Tests for {@link ForwardingSortedMultiset}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingSortedMultisetTest
specifier|public
class|class
name|ForwardingSortedMultisetTest
extends|extends
name|ForwardingMultisetTest
block|{
DECL|class|StandardImplForwardingSortedMultiset
specifier|static
class|class
name|StandardImplForwardingSortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingSortedMultiset
argument_list|<
name|E
argument_list|>
block|{
DECL|field|backingMultiset
specifier|private
specifier|final
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|backingMultiset
decl_stmt|;
DECL|method|StandardImplForwardingSortedMultiset (SortedMultiset<E> backingMultiset)
name|StandardImplForwardingSortedMultiset
parameter_list|(
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|backingMultiset
parameter_list|)
block|{
name|this
operator|.
name|backingMultiset
operator|=
name|backingMultiset
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingMultiset
return|;
block|}
annotation|@
name|Override
DECL|method|descendingMultiset ()
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
block|{
return|return
operator|new
name|StandardDescendingMultiset
argument_list|()
block|{
annotation|@
name|Override
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
return|return
name|backingMultiset
operator|.
name|descendingMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|elementSet ()
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
return|return
operator|new
name|StandardElementSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|firstEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|standardFirstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lastEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|standardLastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollFirstEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
parameter_list|()
block|{
return|return
name|standardPollFirstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollLastEntry ()
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
parameter_list|()
block|{
return|return
name|standardPollLastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subMultiset ( E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType)
specifier|public
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
name|E
name|lowerBound
parameter_list|,
name|BoundType
name|lowerBoundType
parameter_list|,
name|E
name|upperBound
parameter_list|,
name|BoundType
name|upperBoundType
parameter_list|)
block|{
return|return
name|standardSubMultiset
argument_list|(
name|lowerBound
argument_list|,
name|lowerBoundType
argument_list|,
name|upperBound
argument_list|,
name|upperBoundType
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|count (@ullable Object element)
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
return|return
name|standardCount
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|standardEquals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|standardHashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
return|return
name|standardAdd
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardAddAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|standardClear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|standardContains
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAll (Collection<?> collection)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardContainsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|standardIsEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|standardIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullable Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|standardRemove
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Collection<?> collection)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardRemoveAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (Collection<?> collection)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardRetainAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|standardSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|standardToArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|standardToArray
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressUnderAndroid
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
name|addTestSuite
argument_list|(
name|ForwardingSortedMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|SortedMultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
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
operator|new
name|StandardImplForwardingSortedMultiset
argument_list|<
name|String
argument_list|>
argument_list|(
name|TreeMultiset
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
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
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
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingSortedMultiset with standard impls"
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
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_QUERIES
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
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
comment|/*      * Class parameters must be raw, so we can't create a proxy with generic type arguments. The      * created proxy only records calls and returns null, so the type is irrelevant at runtime.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|SortedMultiset
argument_list|<
name|String
argument_list|>
name|sortedMultiset
init|=
name|createProxyInstance
argument_list|(
name|SortedMultiset
operator|.
name|class
argument_list|)
decl_stmt|;
name|forward
operator|=
operator|new
name|ForwardingSortedMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMultiset
argument_list|<
name|String
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|sortedMultiset
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|testComparator ()
specifier|public
name|void
name|testComparator
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|comparator
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[comparator]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstEntry ()
specifier|public
name|void
name|testFirstEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|firstEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[firstEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastEntry ()
specifier|public
name|void
name|testLastEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|lastEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[lastEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPollFirstEntry ()
specifier|public
name|void
name|testPollFirstEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|pollFirstEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[pollFirstEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPollLastEntry ()
specifier|public
name|void
name|testPollLastEntry
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|pollLastEntry
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[pollLastEntry]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDescendingMultiset ()
specifier|public
name|void
name|testDescendingMultiset
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|descendingMultiset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[descendingMultiset]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadMultiset ()
specifier|public
name|void
name|testHeadMultiset
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|headMultiset
argument_list|(
literal|"abcd"
argument_list|,
name|CLOSED
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[headMultiset(Object,BoundType)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubMultiset ()
specifier|public
name|void
name|testSubMultiset
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|subMultiset
argument_list|(
literal|"abcd"
argument_list|,
name|CLOSED
argument_list|,
literal|"dcba"
argument_list|,
name|OPEN
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[subMultiset(Object,BoundType,Object,BoundType)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMultiset ()
specifier|public
name|void
name|testTailMultiset
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|tailMultiset
argument_list|(
literal|"last"
argument_list|,
name|OPEN
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[tailMultiset(Object,BoundType)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|forward ()
specifier|protected
name|SortedMultiset
argument_list|<
name|String
argument_list|>
name|forward
parameter_list|()
block|{
return|return
operator|(
name|SortedMultiset
argument_list|<
name|String
argument_list|>
operator|)
name|super
operator|.
name|forward
argument_list|()
return|;
block|}
block|}
end_class

end_unit

