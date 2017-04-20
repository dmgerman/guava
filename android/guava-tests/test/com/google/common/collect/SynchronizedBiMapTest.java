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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|SynchronizedBiMap
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
name|SynchronizedSet
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
name|collect
operator|.
name|testing
operator|.
name|google
operator|.
name|BiMapInverseTester
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
name|BiMapTestSuiteBuilder
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
name|TestStringBiMapGenerator
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
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Synchronized#biMap}.  *  * @author Mike Bostock  */
end_comment

begin_class
DECL|class|SynchronizedBiMapTest
specifier|public
class|class
name|SynchronizedBiMapTest
extends|extends
name|SynchronizedMapTest
block|{
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
argument_list|(
name|SynchronizedBiMapTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|BiMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|SynchTestingBiMapGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Synchronized.biMap[TestBiMap]"
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
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
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
name|BiMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|SynchronizedHashBiMapGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"synchronizedBiMap[HashBiMap]"
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
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
operator|.
name|suppressing
argument_list|(
name|BiMapInverseTester
operator|.
name|getInverseSameAfterSerializingMethods
argument_list|()
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
DECL|method|create ()
annotation|@
name|Override
specifier|protected
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
name|TestBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|inner
init|=
operator|new
name|TestBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|HashBiMap
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|create
argument_list|()
argument_list|,
name|mutex
argument_list|)
decl_stmt|;
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|biMap
argument_list|(
name|inner
argument_list|,
name|mutex
argument_list|)
decl_stmt|;
return|return
name|outer
return|;
block|}
DECL|class|SynchronizedHashBiMapGenerator
specifier|public
specifier|static
specifier|final
class|class
name|SynchronizedHashBiMapGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
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
name|Object
name|mutex
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
name|HashBiMap
operator|.
name|create
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
name|checkArgument
argument_list|(
operator|!
name|result
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
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
name|Maps
operator|.
name|synchronizedBiMap
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
DECL|class|SynchTestingBiMapGenerator
specifier|public
specifier|static
specifier|final
class|class
name|SynchTestingBiMapGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
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
name|Object
name|mutex
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|backing
init|=
operator|new
name|TestBiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|HashBiMap
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|create
argument_list|()
argument_list|,
name|mutex
argument_list|)
decl_stmt|;
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
name|Synchronized
operator|.
name|biMap
argument_list|(
name|backing
argument_list|,
name|mutex
argument_list|)
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
name|checkArgument
argument_list|(
operator|!
name|result
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|result
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
name|result
return|;
block|}
block|}
DECL|class|TestBiMap
specifier|static
class|class
name|TestBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|TestMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|TestBiMap (BiMap<K, V> delegate, Object mutex)
specifier|public
name|TestBiMap
parameter_list|(
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
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
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|forcePut (K key, V value)
specifier|public
name|V
name|forcePut
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
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
operator|.
name|forcePut
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
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
operator|.
name|inverse
argument_list|()
return|;
block|}
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|V
argument_list|>
name|values
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
operator|.
name|values
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
DECL|method|testForcePut ()
specifier|public
name|void
name|testForcePut
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|forcePut
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testInverse ()
specifier|public
name|void
name|testInverse
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|bimap
init|=
name|create
argument_list|()
decl_stmt|;
name|BiMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|inverse
init|=
name|bimap
operator|.
name|inverse
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|bimap
argument_list|,
name|inverse
operator|.
name|inverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|inverse
operator|instanceof
name|SynchronizedBiMap
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mutex
argument_list|,
operator|(
operator|(
name|SynchronizedBiMap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|inverse
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testValues ()
annotation|@
name|Override
specifier|public
name|void
name|testValues
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|create
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|values
init|=
name|map
operator|.
name|values
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|values
operator|instanceof
name|SynchronizedSet
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mutex
argument_list|,
operator|(
operator|(
name|SynchronizedSet
argument_list|<
name|?
argument_list|>
operator|)
name|values
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

