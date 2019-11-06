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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
operator|.
name|STATIC
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|atLeast
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verifyNoMoreInteractions
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
name|MapTestSuiteBuilder
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
name|TestStringMapGenerator
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
name|MapFeature
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
name|reflect
operator|.
name|AbstractInvocationHandler
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
name|reflect
operator|.
name|Parameter
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
name|reflect
operator|.
name|Reflection
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
name|reflect
operator|.
name|TypeToken
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
name|ArbitraryInstances
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Set
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
comment|/**  * Unit test for {@link ForwardingMap}.  *  * @author Hayward Chan  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingMapTest
specifier|public
class|class
name|ForwardingMapTest
extends|extends
name|TestCase
block|{
DECL|class|StandardImplForwardingMap
specifier|static
class|class
name|StandardImplForwardingMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|backingMap
specifier|private
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
decl_stmt|;
DECL|method|StandardImplForwardingMap (Map<K, V> backingMap)
name|StandardImplForwardingMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
parameter_list|)
block|{
name|this
operator|.
name|backingMap
operator|=
name|backingMap
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|backingMap
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|standardContainsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsValue (Object value)
specifier|public
name|boolean
name|containsValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|standardContainsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|putAll (Map<? extends K, ? extends V> map)
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|standardPutAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object object)
specifier|public
name|V
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
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|StandardKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|values ()
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|StandardValues
argument_list|()
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
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|StandardEntrySet
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|delegate
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
name|ForwardingMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|StandardImplForwardingMap
argument_list|<>
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingMap[LinkedHashMap] with standard implementations"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
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
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|StandardImplForwardingMap
argument_list|<>
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ForwardingMap[ImmutableMap] with standard implementations"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
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
name|Map
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Map
argument_list|,
name|Map
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Map
name|apply
parameter_list|(
name|Map
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
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|map1
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|"one"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|map2
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|2
argument_list|,
literal|"two"
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|map1
argument_list|,
name|wrap
argument_list|(
name|map1
argument_list|)
argument_list|,
name|wrap
argument_list|(
name|map1
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|map2
argument_list|,
name|wrap
argument_list|(
name|map2
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testStandardEntrySet ()
specifier|public
name|void
name|testStandardEntrySet
parameter_list|()
throws|throws
name|InvocationTargetException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
name|mock
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|forward
init|=
operator|new
name|ForwardingMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|StandardEntrySet
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|emptyIterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
name|callAllPublicMethods
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|,
name|forward
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
comment|// These are the methods specified by StandardEntrySet
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|containsKey
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|remove
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
name|verifyNoMoreInteractions
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testStandardKeySet ()
specifier|public
name|void
name|testStandardKeySet
parameter_list|()
throws|throws
name|InvocationTargetException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
name|mock
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|forward
init|=
operator|new
name|ForwardingMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|new
name|StandardKeySet
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|callAllPublicMethods
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|,
name|forward
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
comment|// These are the methods specified by StandardKeySet
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|containsKey
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|remove
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|entrySet
argument_list|()
expr_stmt|;
name|verifyNoMoreInteractions
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testStandardValues ()
specifier|public
name|void
name|testStandardValues
parameter_list|()
throws|throws
name|InvocationTargetException
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|map
init|=
name|mock
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|forward
init|=
operator|new
name|ForwardingMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Boolean
argument_list|>
name|values
parameter_list|()
block|{
return|return
operator|new
name|StandardValues
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|callAllPublicMethods
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|Collection
argument_list|<
name|Boolean
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|,
name|forward
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
comment|// These are the methods specified by StandardValues
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|containsValue
argument_list|(
name|any
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|size
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|,
name|atLeast
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|entrySet
argument_list|()
expr_stmt|;
name|verifyNoMoreInteractions
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringWithNullKeys ()
specifier|public
name|void
name|testToStringWithNullKeys
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hashmap
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|hashmap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|hashmap
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|StandardImplForwardingMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|forwardingMap
init|=
operator|new
name|StandardImplForwardingMap
argument_list|<>
argument_list|(
name|Maps
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|newHashMap
argument_list|()
argument_list|)
decl_stmt|;
name|forwardingMap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|forwardingMap
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|"baz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hashmap
operator|.
name|toString
argument_list|()
argument_list|,
name|forwardingMap
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringWithNullValues ()
specifier|public
name|void
name|testToStringWithNullValues
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hashmap
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|hashmap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|hashmap
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|StandardImplForwardingMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|forwardingMap
init|=
operator|new
name|StandardImplForwardingMap
argument_list|<>
argument_list|(
name|Maps
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|newHashMap
argument_list|()
argument_list|)
decl_stmt|;
name|forwardingMap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|forwardingMap
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hashmap
operator|.
name|toString
argument_list|()
argument_list|,
name|forwardingMap
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|wrap (final Map<K, V> delegate)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ForwardingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|K
argument_list|,
name|V
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
DECL|field|JUF_METHODS
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|JUF_METHODS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"java.util.function.Predicate"
argument_list|,
literal|"test"
argument_list|,
literal|"java.util.function.Consumer"
argument_list|,
literal|"accept"
argument_list|,
literal|"java.util.function.IntFunction"
argument_list|,
literal|"apply"
argument_list|)
decl_stmt|;
DECL|method|getDefaultValue (final TypeToken<?> type)
specifier|private
specifier|static
name|Object
name|getDefaultValue
parameter_list|(
specifier|final
name|TypeToken
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|rawType
init|=
name|type
operator|.
name|getRawType
argument_list|()
decl_stmt|;
name|Object
name|defaultValue
init|=
name|ArbitraryInstances
operator|.
name|get
argument_list|(
name|rawType
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
specifier|final
name|String
name|typeName
init|=
name|rawType
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
if|if
condition|(
name|JUF_METHODS
operator|.
name|containsKey
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
comment|// Generally, methods that accept java.util.function.* instances
comment|// don't like to get null values.  We generate them dynamically
comment|// using Proxy so that we can have Java 7 compliant code.
return|return
name|Reflection
operator|.
name|newProxy
argument_list|(
name|rawType
argument_list|,
operator|new
name|AbstractInvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|handleInvocation
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
comment|// Crude, but acceptable until we can use Java 8.  Other
comment|// methods have default implementations, and it is hard to
comment|// distinguish.
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|JUF_METHODS
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|getDefaultValue
argument_list|(
name|type
operator|.
name|method
argument_list|(
name|method
argument_list|)
operator|.
name|getReturnType
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected "
operator|+
name|method
operator|+
literal|" invoked on "
operator|+
name|proxy
argument_list|)
throw|;
block|}
block|}
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|callAllPublicMethods (TypeToken<T> type, T object)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|callAllPublicMethods
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|object
parameter_list|)
throws|throws
name|InvocationTargetException
block|{
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getRawType
argument_list|()
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|method
operator|.
name|getModifiers
argument_list|()
operator|&
name|STATIC
operator|)
operator|!=
literal|0
condition|)
block|{
continue|continue;
block|}
name|ImmutableList
argument_list|<
name|Parameter
argument_list|>
name|parameters
init|=
name|type
operator|.
name|method
argument_list|(
name|method
argument_list|)
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
name|parameters
operator|.
name|size
argument_list|()
index|]
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
name|parameters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|getDefaultValue
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
try|try
block|{
throw|throw
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|unsupported
parameter_list|)
block|{
comment|// this is a legit exception
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InvocationTargetException
argument_list|(
name|cause
argument_list|,
name|method
operator|+
literal|" with args: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|args
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

