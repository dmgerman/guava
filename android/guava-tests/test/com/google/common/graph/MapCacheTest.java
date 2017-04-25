begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Ordering
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
name|Comparator
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
name|TreeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_comment
comment|/** Tests for {@link MapIteratorCache} and {@link MapRetrievalCache}. */
end_comment

begin_class
annotation|@
name|AndroidIncompatible
comment|//TODO(cpovirk): Figure out Android JUnit 4 support. Does it work with Gingerbread? @RunWith?
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|MapCacheTest
specifier|public
specifier|final
class|class
name|MapCacheTest
block|{
DECL|field|mapCache
specifier|private
specifier|final
name|MapIteratorCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapCache
decl_stmt|;
DECL|method|MapCacheTest (MapIteratorCache<String, String> mapCache)
specifier|public
name|MapCacheTest
parameter_list|(
name|MapIteratorCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mapCache
parameter_list|)
block|{
name|this
operator|.
name|mapCache
operator|=
name|mapCache
expr_stmt|;
block|}
annotation|@
name|Parameters
DECL|method|parameters ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|parameters
parameter_list|()
block|{
name|Comparator
argument_list|<
name|String
argument_list|>
name|nullsLast
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsLast
argument_list|()
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
index|[]
block|{
block|{
operator|new
name|MapIteratorCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
block|}
block|,
block|{
operator|new
name|MapIteratorCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|nullsLast
argument_list|)
argument_list|)
block|}
block|,
block|{
operator|new
name|MapRetrievalCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
block|}
block|,
block|{
operator|new
name|MapRetrievalCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|nullsLast
argument_list|)
argument_list|)
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|mapCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testKeySetIterator ()
specifier|public
name|void
name|testKeySetIterator
parameter_list|()
block|{
name|mapCache
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|"A_value"
argument_list|)
expr_stmt|;
name|mapCache
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
literal|"B_value"
argument_list|)
expr_stmt|;
name|mapCache
operator|.
name|put
argument_list|(
literal|"C"
argument_list|,
literal|"C_value"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|unmodifiableKeySet
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|mapCache
operator|.
name|unmodifiableKeySet
argument_list|()
control|)
block|{
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|key
operator|+
literal|"_value"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPutNewValue ()
specifier|public
name|void
name|testPutNewValue
parameter_list|()
block|{
name|assertThat
argument_list|(
name|mapCache
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
comment|// ensure key/value is cached
name|assertThat
argument_list|(
name|mapCache
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"new value"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"new value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveEqualKeyWithDifferentReference ()
specifier|public
name|void
name|testRemoveEqualKeyWithDifferentReference
parameter_list|()
block|{
name|String
name|fooReference1
init|=
operator|new
name|String
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|String
name|fooReference2
init|=
operator|new
name|String
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|fooReference1
argument_list|)
operator|.
name|isNotSameAs
argument_list|(
name|fooReference2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|put
argument_list|(
name|fooReference1
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
name|fooReference1
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
comment|// ensure first reference is cached
name|assertThat
argument_list|(
name|mapCache
operator|.
name|remove
argument_list|(
name|fooReference2
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
name|fooReference1
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHandleNulls ()
specifier|public
name|void
name|testHandleNulls
parameter_list|()
block|{
name|mapCache
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|mapCache
operator|.
name|put
argument_list|(
literal|"non-null key"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|mapCache
operator|.
name|put
argument_list|(
literal|null
argument_list|,
literal|"non-null value"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"non-null key"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"non-null value"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|containsKey
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|containsKey
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|containsKey
argument_list|(
literal|"non-null key"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|containsKey
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Test again - in reverse order.
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"non-null value"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"non-null key"
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mapCache
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
