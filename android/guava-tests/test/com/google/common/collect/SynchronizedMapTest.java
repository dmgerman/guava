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
name|checkNotNull
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
name|SynchronizedCollection
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
name|testing
operator|.
name|SerializableTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|HashMap
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
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Synchronized#map}.  *  * @author Mike Bostock  */
end_comment

begin_class
DECL|class|SynchronizedMapTest
specifier|public
class|class
name|SynchronizedMapTest
extends|extends
name|TestCase
block|{
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// something Serializable
DECL|method|create ()
specifier|protected
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
name|create
parameter_list|()
block|{
name|TestMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|inner
init|=
operator|new
name|TestMap
argument_list|<>
argument_list|(
operator|new
name|HashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
argument_list|,
name|mutex
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|map
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
DECL|class|TestMap
specifier|static
class|class
name|TestMap
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
implements|implements
name|Serializable
block|{
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
decl_stmt|;
DECL|field|delegate
specifier|private
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|TestMap (Map<K, V> delegate, Object mutex)
specifier|public
name|TestMap
parameter_list|(
name|Map
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
name|checkNotNull
argument_list|(
name|mutex
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|mutex
operator|=
name|mutex
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
name|delegate
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
name|super
operator|.
name|size
argument_list|()
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
name|super
operator|.
name|isEmpty
argument_list|()
return|;
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
name|super
operator|.
name|remove
argument_list|(
name|object
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
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|containsKey
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
name|super
operator|.
name|containsValue
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (Object key)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|key
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
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|V
name|put
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
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
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
name|super
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|keySet
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
name|super
operator|.
name|values
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
name|super
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
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
name|super
operator|.
name|equals
argument_list|(
name|obj
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
name|super
operator|.
name|hashCode
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
name|super
operator|.
name|toString
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
comment|/*    * This is somewhat of a weak test; we verify that all of the methods are    * correct, but not that they're actually forwarding correctly. We also rely    * on the other tests (e.g., SynchronizedSetTest) to verify that the    * collection views are synchronized correctly.    */
DECL|method|testSize ()
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
DECL|method|testIsEmpty ()
specifier|public
name|void
name|testIsEmpty
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|testContainsKey ()
specifier|public
name|void
name|testContainsKey
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsValue ()
specifier|public
name|void
name|testContainsValue
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|containsValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|get
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testPut ()
specifier|public
name|void
name|testPut
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutAll ()
specifier|public
name|void
name|testPutAll
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|putAll
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeySet ()
specifier|public
name|void
name|testKeySet
parameter_list|()
block|{
name|Map
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
name|String
argument_list|>
name|keySet
init|=
name|map
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|keySet
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
name|keySet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testValues ()
specifier|public
name|void
name|testValues
parameter_list|()
block|{
name|Map
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
name|Collection
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
name|SynchronizedCollection
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mutex
argument_list|,
operator|(
operator|(
name|SynchronizedCollection
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
DECL|method|testEntrySet ()
specifier|public
name|void
name|testEntrySet
parameter_list|()
block|{
name|Map
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
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|entrySet
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entrySet
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
name|entrySet
operator|)
operator|.
name|mutex
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
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
name|create
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

