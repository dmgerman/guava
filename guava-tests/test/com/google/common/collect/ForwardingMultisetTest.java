begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MultisetTestSuiteBuilder
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
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests for {@link ForwardingMultiset}.  *  * @author Hayward Chan  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingMultisetTest
specifier|public
class|class
name|ForwardingMultisetTest
extends|extends
name|ForwardingTestCase
block|{
DECL|class|StandardImplForwardingMultiset
specifier|static
specifier|final
class|class
name|StandardImplForwardingMultiset
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ForwardingMultiset
argument_list|<
name|T
argument_list|>
block|{
DECL|field|backingCollection
specifier|private
specifier|final
name|Multiset
argument_list|<
name|T
argument_list|>
name|backingCollection
decl_stmt|;
DECL|method|StandardImplForwardingMultiset (Multiset<T> backingMultiset)
name|StandardImplForwardingMultiset
parameter_list|(
name|Multiset
argument_list|<
name|T
argument_list|>
name|backingMultiset
parameter_list|)
block|{
name|this
operator|.
name|backingCollection
operator|=
name|backingMultiset
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingCollection
return|;
block|}
DECL|method|addAll (Collection<? extends T> collection)
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|T
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
DECL|method|add (T element)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|T
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
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|standardClear
argument_list|()
expr_stmt|;
block|}
DECL|method|count (Object element)
annotation|@
name|Override
specifier|public
name|int
name|count
parameter_list|(
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
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
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
DECL|method|containsAll (Collection<?> collection)
annotation|@
name|Override
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
DECL|method|remove (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
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
DECL|method|removeAll (Collection<?> collection)
annotation|@
name|Override
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
DECL|method|retainAll (Collection<?> collection)
annotation|@
name|Override
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
DECL|method|toArray ()
annotation|@
name|Override
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
DECL|method|toArray (T[] array)
annotation|@
name|Override
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
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|standardToString
argument_list|()
return|;
block|}
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
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
DECL|method|hashCode ()
annotation|@
name|Override
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
DECL|method|setCount (T element, int oldCount, int newCount)
annotation|@
name|Override
specifier|public
name|boolean
name|setCount
parameter_list|(
name|T
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
return|return
name|standardSetCount
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
return|;
block|}
DECL|method|setCount (T element, int count)
annotation|@
name|Override
specifier|public
name|int
name|setCount
parameter_list|(
name|T
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|standardSetCount
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
DECL|method|elementSet ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|T
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
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|standardIterator
argument_list|()
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
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
DECL|method|size ()
annotation|@
name|Override
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
block|}
DECL|field|EMPTY_COLLECTION
specifier|private
specifier|static
specifier|final
name|Collection
argument_list|<
name|String
argument_list|>
name|EMPTY_COLLECTION
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
DECL|field|forward
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|forward
decl_stmt|;
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
name|ForwardingMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
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
name|StandardImplForwardingMultiset
argument_list|<
name|String
argument_list|>
argument_list|(
name|LinkedHashMultiset
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
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingMultiset[LinkedHashMultiset] with standard "
operator|+
literal|"implementations"
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
name|GENERAL_PURPOSE
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
name|MultisetTestSuiteBuilder
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
name|StandardImplForwardingMultiset
argument_list|<
name|String
argument_list|>
argument_list|(
name|ImmutableMultiset
operator|.
name|copyOf
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
literal|"ForwardingMultiset[ImmutableMultiset] with standard "
operator|+
literal|"implementations"
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
name|ALLOWS_NULL_QUERIES
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
comment|/**        * Returns a Multiset that throws an exception on any attempt to use a        * method not specifically authorized by the elementSet() or hashCode()        * docs.        */
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
specifier|final
name|Multiset
argument_list|<
name|String
argument_list|>
name|inner
init|=
name|LinkedHashMultiset
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
decl_stmt|;
return|return
operator|new
name|ForwardingMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|inner
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
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
specifier|public
name|int
name|add
parameter_list|(
name|String
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|backingSet
init|=
name|super
operator|.
name|entrySet
argument_list|()
decl_stmt|;
return|return
operator|new
name|ForwardingSet
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingSet
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|>
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|String
argument_list|>
argument_list|>
name|collection
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|setCount
parameter_list|(
name|String
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|setCount
parameter_list|(
name|String
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|String
argument_list|>
name|collection
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
operator|.
name|elementSet
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"standardElementSet tripwire"
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
name|REMOVE_OPERATIONS
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
DECL|method|setUp ()
annotation|@
name|Override
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
comment|/*      * Class parameters must be raw, so we can't create a proxy with generic      * type arguments. The created proxy only records calls and returns null, so      * the type is irrelevant at runtime.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|createProxyInstance
argument_list|(
name|Multiset
operator|.
name|class
argument_list|)
decl_stmt|;
name|forward
operator|=
operator|new
name|ForwardingMultiset
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|multiset
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|testAdd_T ()
specifier|public
name|void
name|testAdd_T
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|add
argument_list|(
literal|"asdf"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[add(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAddAll_Collection ()
specifier|public
name|void
name|testAddAll_Collection
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|addAll
argument_list|(
name|EMPTY_COLLECTION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[addAll(Collection)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[clear]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testContains_Object ()
specifier|public
name|void
name|testContains_Object
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[contains(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsAll_Collection ()
specifier|public
name|void
name|testContainsAll_Collection
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|containsAll
argument_list|(
name|EMPTY_COLLECTION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[containsAll(Collection)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[isEmpty]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIterator ()
specifier|public
name|void
name|testIterator
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[iterator]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemove_Object ()
specifier|public
name|void
name|testRemove_Object
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[remove(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoveAll_Collection ()
specifier|public
name|void
name|testRemoveAll_Collection
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|removeAll
argument_list|(
name|EMPTY_COLLECTION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[removeAll(Collection)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRetainAll_Collection ()
specifier|public
name|void
name|testRetainAll_Collection
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|retainAll
argument_list|(
name|EMPTY_COLLECTION
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[retainAll(Collection)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[size]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray ()
specifier|public
name|void
name|testToArray
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|toArray
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[toArray]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_TArray ()
specifier|public
name|void
name|testToArray_TArray
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[toArray(Object[])]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[toString]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_Object ()
specifier|public
name|void
name|testEquals_Object
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|equals
argument_list|(
literal|"asdf"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[equals(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[hashCode]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCount_Object ()
specifier|public
name|void
name|testCount_Object
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|count
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[count(Object)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAdd_Object_int ()
specifier|public
name|void
name|testAdd_Object_int
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|add
argument_list|(
literal|"asd"
argument_list|,
literal|23
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[add(Object,int)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemove_Object_int ()
specifier|public
name|void
name|testRemove_Object_int
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|remove
argument_list|(
literal|"asd"
argument_list|,
literal|23
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[remove(Object,int)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSetCount_Object_int ()
specifier|public
name|void
name|testSetCount_Object_int
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|setCount
argument_list|(
literal|"asdf"
argument_list|,
literal|233
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[setCount(Object,int)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSetCount_Object_oldCount_newCount ()
specifier|public
name|void
name|testSetCount_Object_oldCount_newCount
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|setCount
argument_list|(
literal|"asdf"
argument_list|,
literal|4552
argument_list|,
literal|1233
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[setCount(Object,int,int)]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressUnderAndroid
comment|// Proxy problem, perhaps around SortedMultisetBridge?
DECL|method|testElementSet ()
specifier|public
name|void
name|testElementSet
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|elementSet
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[elementSet]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet ()
specifier|public
name|void
name|testEntrySet
parameter_list|()
block|{
name|forward
argument_list|()
operator|.
name|entrySet
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[entrySet]"
argument_list|,
name|getCalls
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|forward ()
specifier|protected
name|Multiset
argument_list|<
name|String
argument_list|>
name|forward
parameter_list|()
block|{
return|return
name|forward
return|;
block|}
block|}
end_class

end_unit

