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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Synchronized
operator|.
name|SynchronizedNavigableSet
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
name|Synchronized
operator|.
name|SynchronizedSortedSet
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
name|NavigableSetTestSuiteBuilder
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
name|SafeTreeSet
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
name|TestStringSortedSetGenerator
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
comment|/**  * Tests for {@link Sets#synchronizedNavigableSet(NavigableSet)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|SynchronizedNavigableSetTest
specifier|public
class|class
name|SynchronizedNavigableSetTest
extends|extends
name|TestCase
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|create ()
specifier|protected
parameter_list|<
name|E
parameter_list|>
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
name|TestSet
argument_list|<
name|E
argument_list|>
name|inner
init|=
operator|new
name|TestSet
argument_list|<>
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|(
operator|(
name|Comparator
argument_list|<
name|E
argument_list|>
operator|)
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|navigableSet
argument_list|(
name|inner
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|inner
operator|.
name|mutex
operator|=
name|outer
expr_stmt|;
return|return
name|outer
return|;
block|}
DECL|class|TestSet
specifier|static
class|class
name|TestSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|SynchronizedSetTest
operator|.
name|TestSet
argument_list|<
name|E
argument_list|>
implements|implements
name|NavigableSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|TestSet (NavigableSet<E> delegate, Object mutex)
name|TestSet
parameter_list|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Object
name|mutex
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|,
name|mutex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|delegate
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|ceiling (E e)
specifier|public
name|E
name|ceiling
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|ceiling
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|descendingIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingSet ()
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|descendingSet
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|descendingSet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|floor (E e)
specifier|public
name|E
name|floor
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|floor
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headSet (E toElement, boolean inclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|headSet
argument_list|(
name|toElement
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headSet (E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
name|headSet
argument_list|(
name|toElement
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higher (E e)
specifier|public
name|E
name|higher
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|higher
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lower (E e)
specifier|public
name|E
name|lower
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|lower
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pollFirst ()
specifier|public
name|E
name|pollFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|pollFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollLast ()
specifier|public
name|E
name|pollLast
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|pollLast
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|subSet ( E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|E
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|subSet
argument_list|(
name|fromElement
argument_list|,
name|fromInclusive
argument_list|,
name|toElement
argument_list|,
name|toInclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subSet (E fromElement, E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
name|subSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|,
name|toElement
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSet (E fromElement, boolean inclusive)
specifier|public
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|tailSet
argument_list|(
name|fromElement
argument_list|,
name|inclusive
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSet (E fromElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
name|tailSet
argument_list|(
name|fromElement
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|first
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
argument_list|()
operator|.
name|last
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
DECL|method|suite ()
specifier|public
specifier|static
name|TestSuite
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
name|SynchronizedNavigableSetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|NavigableSetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|NavigableSet
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
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|innermost
init|=
operator|new
name|SafeTreeSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|innermost
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|TestSet
argument_list|<
name|String
argument_list|>
name|inner
init|=
operator|new
name|TestSet
argument_list|<>
argument_list|(
name|innermost
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|navigableSet
argument_list|(
name|inner
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|inner
operator|.
name|mutex
operator|=
name|outer
expr_stmt|;
return|return
name|outer
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
literal|"Sets.synchronizedNavigableSet[SafeTreeSet]"
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
name|SERIALIZABLE
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
DECL|method|testDescendingSet ()
specifier|public
name|void
name|testDescendingSet
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|descendingSet
init|=
name|set
operator|.
name|descendingSet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|descendingSet
operator|instanceof
name|SynchronizedNavigableSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedNavigableSet
argument_list|<
name|String
argument_list|>
operator|)
name|descendingSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadSet_E ()
specifier|public
name|void
name|testHeadSet_E
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|headSet
init|=
name|set
operator|.
name|headSet
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|headSet
operator|instanceof
name|SynchronizedSortedSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedSortedSet
argument_list|<
name|String
argument_list|>
operator|)
name|headSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadSet_E_B ()
specifier|public
name|void
name|testHeadSet_E_B
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|headSet
init|=
name|set
operator|.
name|headSet
argument_list|(
literal|"a"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|headSet
operator|instanceof
name|SynchronizedNavigableSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedNavigableSet
argument_list|<
name|String
argument_list|>
operator|)
name|headSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubSet_E_E ()
specifier|public
name|void
name|testSubSet_E_E
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|subSet
init|=
name|set
operator|.
name|subSet
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|subSet
operator|instanceof
name|SynchronizedSortedSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedSortedSet
argument_list|<
name|String
argument_list|>
operator|)
name|subSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubSet_E_B_E_B ()
specifier|public
name|void
name|testSubSet_E_B_E_B
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|subSet
init|=
name|set
operator|.
name|subSet
argument_list|(
literal|"a"
argument_list|,
literal|false
argument_list|,
literal|"b"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|subSet
operator|instanceof
name|SynchronizedNavigableSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedNavigableSet
argument_list|<
name|String
argument_list|>
operator|)
name|subSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailSet_E ()
specifier|public
name|void
name|testTailSet_E
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|String
argument_list|>
name|tailSet
init|=
name|set
operator|.
name|tailSet
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tailSet
operator|instanceof
name|SynchronizedSortedSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedSortedSet
argument_list|<
name|String
argument_list|>
operator|)
name|tailSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailSet_E_B ()
specifier|public
name|void
name|testTailSet_E_B
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|set
init|=
name|create
argument_list|()
decl_stmt|;
name|NavigableSet
argument_list|<
name|String
argument_list|>
name|tailSet
init|=
name|set
operator|.
name|tailSet
argument_list|(
literal|"a"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|tailSet
operator|instanceof
name|SynchronizedNavigableSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|set
argument_list|,
operator|(
operator|(
name|SynchronizedNavigableSet
argument_list|<
name|String
argument_list|>
operator|)
name|tailSet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

