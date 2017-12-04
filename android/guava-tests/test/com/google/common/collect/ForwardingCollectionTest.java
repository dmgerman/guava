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
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|collect
operator|.
name|testing
operator|.
name|CollectionTestSuiteBuilder
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
name|MinimalCollection
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
name|TestStringCollectionGenerator
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
comment|/**  * Tests for {@link ForwardingCollection}.  *  * @author Robert Konigsberg  * @author Hayward Chan  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingCollectionTest
specifier|public
class|class
name|ForwardingCollectionTest
extends|extends
name|TestCase
block|{
DECL|class|StandardImplForwardingCollection
specifier|static
specifier|final
class|class
name|StandardImplForwardingCollection
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ForwardingCollection
argument_list|<
name|T
argument_list|>
block|{
DECL|field|backingCollection
specifier|private
specifier|final
name|Collection
argument_list|<
name|T
argument_list|>
name|backingCollection
decl_stmt|;
DECL|method|StandardImplForwardingCollection (Collection<T> backingCollection)
name|StandardImplForwardingCollection
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|backingCollection
parameter_list|)
block|{
name|this
operator|.
name|backingCollection
operator|=
name|backingCollection
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Collection
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
name|ForwardingCollectionTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|CollectionTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringCollectionGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Collection
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
name|StandardImplForwardingCollection
argument_list|<>
argument_list|(
name|Lists
operator|.
name|newLinkedList
argument_list|(
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
literal|"ForwardingCollection[LinkedList] with standard implementations"
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
name|CollectionTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringCollectionGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Collection
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
name|StandardImplForwardingCollection
argument_list|<>
argument_list|(
name|MinimalCollection
operator|.
name|of
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
literal|"ForwardingCollection[MinimalCollection] with standard"
operator|+
literal|" implementations"
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
name|Collection
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Collection
argument_list|,
name|Collection
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Collection
name|apply
parameter_list|(
name|Collection
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
DECL|method|wrap (final Collection<T> delegate)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ForwardingCollection
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Collection
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

