begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
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
name|Maps
operator|.
name|immutableEntry
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
name|ImmutableList
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
name|SampleElements
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
name|TestMapGenerator
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
comment|/**  * Unit test for {@link ImmutableTypeToInstanceMap}.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|ImmutableTypeToInstanceMapTest
specifier|public
class|class
name|ImmutableTypeToInstanceMapTest
extends|extends
name|TestCase
block|{
annotation|@
name|AndroidIncompatible
comment|// problem with suite builders on Android
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
name|ImmutableTypeToInstanceMapTest
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
name|TestTypeToInstanceMapGenerator
argument_list|()
block|{
comment|// Other tests will verify what real, warning-free usage looks like
comment|// but here we have to do some serious fudging
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Map
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|ImmutableTypeToInstanceMap
operator|.
name|Builder
argument_list|<
name|Object
argument_list|>
name|builder
init|=
name|ImmutableTypeToInstanceMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|elements
control|)
block|{
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
operator|)
name|object
decl_stmt|;
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
operator|(
name|Map
operator|)
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ImmutableTypeToInstanceMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|MapFeature
operator|.
name|RESTRICTS_KEYS
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
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
DECL|method|testEmpty ()
specifier|public
name|void
name|testEmpty
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ImmutableTypeToInstanceMap
operator|.
name|of
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPrimitiveAndWrapper ()
specifier|public
name|void
name|testPrimitiveAndWrapper
parameter_list|()
block|{
name|ImmutableTypeToInstanceMap
argument_list|<
name|Number
argument_list|>
name|map
init|=
name|ImmutableTypeToInstanceMap
operator|.
expr|<
name|Number
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
operator|.
name|put
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|map
operator|.
name|getInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|map
operator|.
name|getInstance
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|map
operator|.
name|getInstance
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|map
operator|.
name|getInstance
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testParameterizedType ()
specifier|public
name|void
name|testParameterizedType
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|type
init|=
operator|new
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|ImmutableTypeToInstanceMap
argument_list|<
name|Iterable
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
name|ImmutableTypeToInstanceMap
operator|.
expr|<
name|Iterable
argument_list|<
name|?
argument_list|>
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|map
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGenericArrayType ()
specifier|public
name|void
name|testGenericArrayType
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Trying to test generic array
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
index|[]
name|array
init|=
operator|new
name|ImmutableList
index|[]
block|{
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
block|}
decl_stmt|;
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
index|[]
argument_list|>
name|type
init|=
operator|new
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
index|[]
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|ImmutableTypeToInstanceMap
argument_list|<
name|Iterable
argument_list|<
name|?
argument_list|>
index|[]
argument_list|>
name|map
init|=
name|ImmutableTypeToInstanceMap
operator|.
expr|<
name|Iterable
argument_list|<
name|?
argument_list|>
index|[]
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|array
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Redundant cast works around a javac bug.
name|assertThat
argument_list|(
operator|(
name|Iterable
argument_list|<
name|?
argument_list|>
index|[]
operator|)
name|map
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|asList
argument_list|()
operator|.
name|containsExactly
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testWildcardType ()
specifier|public
name|void
name|testWildcardType
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|?
argument_list|>
argument_list|>
name|type
init|=
operator|new
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{}
empty_stmt|;
name|ImmutableTypeToInstanceMap
argument_list|<
name|Iterable
argument_list|<
name|?
argument_list|>
argument_list|>
name|map
init|=
name|ImmutableTypeToInstanceMap
operator|.
expr|<
name|Iterable
argument_list|<
name|?
argument_list|>
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|,
name|map
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testGetInstance_containsTypeVariable ()
specifier|public
name|void
name|testGetInstance_containsTypeVariable
parameter_list|()
block|{
name|ImmutableTypeToInstanceMap
argument_list|<
name|Iterable
argument_list|<
name|Number
argument_list|>
argument_list|>
name|map
init|=
name|ImmutableTypeToInstanceMap
operator|.
name|of
argument_list|()
decl_stmt|;
try|try
block|{
name|map
operator|.
name|getInstance
argument_list|(
name|this
operator|.
expr|<
name|Number
operator|>
name|anyIterableType
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testPut_containsTypeVariable ()
specifier|public
name|void
name|testPut_containsTypeVariable
parameter_list|()
block|{
name|ImmutableTypeToInstanceMap
operator|.
name|Builder
argument_list|<
name|Iterable
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|builder
init|=
name|ImmutableTypeToInstanceMap
operator|.
name|builder
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|put
argument_list|(
name|this
operator|.
expr|<
name|Integer
operator|>
name|anyIterableType
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|anyIterableType ()
specifier|private
parameter_list|<
name|T
parameter_list|>
name|TypeToken
argument_list|<
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
name|anyIterableType
parameter_list|()
block|{
return|return
operator|new
name|TypeToken
argument_list|<
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
return|;
block|}
DECL|class|TestTypeToInstanceMapGenerator
specifier|abstract
specifier|static
class|class
name|TestTypeToInstanceMapGenerator
implements|implements
name|TestMapGenerator
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createKeyArray (int length)
specifier|public
name|TypeToken
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|TypeToken
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|createValueArray (int length)
specifier|public
name|Object
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<>
argument_list|(
name|entry
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|,
literal|0
argument_list|)
argument_list|,
name|entry
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|Number
operator|.
name|class
argument_list|)
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entry
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|,
name|entry
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|int
index|[]
argument_list|>
argument_list|()
block|{}
argument_list|,
operator|new
name|int
index|[]
block|{
literal|3
block|}
argument_list|)
argument_list|,
name|entry
argument_list|(
operator|new
name|TypeToken
argument_list|<
name|Iterable
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"4"
argument_list|)
block|)
block|)
class|;
block|}
end_class

begin_function
DECL|method|entry (TypeToken<?> k, Object v)
specifier|private
specifier|static
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
name|entry
parameter_list|(
name|TypeToken
argument_list|<
name|?
argument_list|>
name|k
parameter_list|,
name|Object
name|v
parameter_list|)
block|{
return|return
name|immutableEntry
argument_list|(
operator|(
name|TypeToken
operator|)
name|k
argument_list|,
name|v
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createArray (int length)
specifier|public
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Entry
index|[
name|length
index|]
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|order (List<Entry<TypeToken, Object>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|TypeToken
argument_list|,
name|Object
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
return|;
block|}
end_function

unit|} }
end_unit

