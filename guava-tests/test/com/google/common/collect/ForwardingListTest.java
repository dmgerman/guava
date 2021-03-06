begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|testing
operator|.
name|ListTestSuiteBuilder
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
name|TestStringListGenerator
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
name|features
operator|.
name|ListFeature
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
name|ForwardingWrapperTester
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
name|ListIterator
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

begin_comment
comment|/**  * Tests for {@code ForwardingList}.  *  * @author Robert Konigsberg  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingListTest
specifier|public
class|class
name|ForwardingListTest
extends|extends
name|TestCase
block|{
DECL|class|StandardImplForwardingList
specifier|static
specifier|final
class|class
name|StandardImplForwardingList
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ForwardingList
argument_list|<
name|T
argument_list|>
block|{
DECL|field|backingList
specifier|private
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|backingList
decl_stmt|;
DECL|method|StandardImplForwardingList (List<T> backingList)
name|StandardImplForwardingList
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|backingList
parameter_list|)
block|{
name|this
operator|.
name|backingList
operator|=
name|backingList
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|List
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingList
return|;
block|}
annotation|@
name|Override
DECL|method|add (T element)
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
annotation|@
name|Override
DECL|method|addAll (Collection<? extends T> collection)
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
annotation|@
name|Override
DECL|method|addAll (int index, Collection<? extends T> elements)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|elements
parameter_list|)
block|{
return|return
name|standardAddAll
argument_list|(
name|index
argument_list|,
name|elements
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
DECL|method|contains (Object object)
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
DECL|method|remove (Object object)
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
annotation|@
name|Override
DECL|method|toString ()
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
annotation|@
name|Override
DECL|method|equals (Object object)
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
DECL|method|indexOf (Object element)
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|standardIndexOf
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|lastIndexOf (Object element)
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
return|return
name|standardLastIndexOf
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|listIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator ()
specifier|public
name|ListIterator
argument_list|<
name|T
argument_list|>
name|listIterator
parameter_list|()
block|{
return|return
name|listIterator
argument_list|(
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|listIterator (int index)
specifier|public
name|ListIterator
argument_list|<
name|T
argument_list|>
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|standardListIterator
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
return|return
name|standardSubList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
return|;
block|}
block|}
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
name|ForwardingListTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringListGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|List
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
name|StandardImplForwardingList
argument_list|<>
argument_list|(
name|Lists
operator|.
name|newArrayList
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
literal|"ForwardingList[ArrayList] with standard implementations"
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
name|ListFeature
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
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringListGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|List
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
name|StandardImplForwardingList
argument_list|<>
argument_list|(
name|ImmutableList
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
literal|"ForwardingList[ImmutableList] with standard implementations"
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
return|return
name|suite
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
DECL|method|testForwarding ()
specifier|public
name|void
name|testForwarding
parameter_list|()
block|{
operator|new
name|ForwardingWrapperTester
argument_list|()
operator|.
name|testForwarding
argument_list|(
name|List
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|List
argument_list|,
name|List
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
name|apply
parameter_list|(
name|List
name|delegate
parameter_list|)
block|{
return|return
name|wrap
argument_list|(
name|delegate
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list1
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"one"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list2
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"two"
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|list1
argument_list|,
name|wrap
argument_list|(
name|list1
argument_list|)
argument_list|,
name|wrap
argument_list|(
name|list1
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|list2
argument_list|,
name|wrap
argument_list|(
name|list2
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|wrap (final List<T> delegate)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ForwardingList
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

