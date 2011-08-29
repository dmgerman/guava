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
name|Set
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
name|addTestSuite
argument_list|(
name|AbstractBiMapTests
operator|.
name|class
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
DECL|class|AbstractBiMapTests
specifier|public
specifier|static
class|class
name|AbstractBiMapTests
extends|extends
name|AbstractBiMapTest
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
annotation|@
name|Override
specifier|protected
name|BiMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|create
parameter_list|()
block|{
name|TestBiMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|inner
init|=
operator|new
name|TestBiMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|(
name|HashBiMap
operator|.
expr|<
name|Integer
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
name|Integer
argument_list|,
name|String
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
comment|/**      * If you serialize a synchronized bimap and its inverse together, the      * reserialized bimaps will have backing maps that stay in sync, as shown      * by the {@code testSerializationWithInverseEqual()} test. However, the      * inverse of one won't be the same as the other.      *      * To make them the same, the inverse synchronized bimap would need a custom      * serialized form, similar to what {@code AbstractBiMap.Inverse} does.      */
DECL|method|testSerializationWithInverseSame ()
annotation|@
name|Override
specifier|public
name|void
name|testSerializationWithInverseSame
parameter_list|()
block|{}
block|}
block|}
end_class

end_unit

