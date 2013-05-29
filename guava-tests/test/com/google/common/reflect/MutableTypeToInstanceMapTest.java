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
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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
name|ImmutableMap
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
name|testers
operator|.
name|MapPutTester
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
name|ImmutableTypeToInstanceMapTest
operator|.
name|TestTypeToInstanceMapGenerator
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

begin_comment
comment|/**  * Unit test of {@link MutableTypeToInstanceMap}.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|MutableTypeToInstanceMapTest
specifier|public
class|class
name|MutableTypeToInstanceMapTest
extends|extends
name|TestCase
block|{
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
name|MutableTypeToInstanceMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Suppress this one because the tester framework doesn't understand that
comment|// *some* remappings will be allowed and others not.
name|Method
name|remapTest
init|=
literal|null
decl_stmt|;
try|try
block|{
name|remapTest
operator|=
name|MapPutTester
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testPut_replaceNullValueWithNonNullSupported"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
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
name|MutableTypeToInstanceMap
argument_list|<
name|Object
argument_list|>
name|map
init|=
operator|new
name|MutableTypeToInstanceMap
argument_list|<
name|Object
argument_list|>
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
name|map
operator|.
name|putInstance
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
name|map
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"MutableTypeToInstanceMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
argument_list|,
name|MapFeature
operator|.
name|RESTRICTS_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_QUERIES
argument_list|)
operator|.
name|suppressing
argument_list|(
name|remapTest
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
DECL|field|map
specifier|private
name|TypeToInstanceMap
argument_list|<
name|Object
argument_list|>
name|map
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|map
operator|=
operator|new
name|MutableTypeToInstanceMap
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|testPutThrows ()
specifier|public
name|void
name|testPutThrows
parameter_list|()
block|{
try|try
block|{
name|map
operator|.
name|put
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
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testPutAllThrows ()
specifier|public
name|void
name|testPutAllThrows
parameter_list|()
block|{
try|try
block|{
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
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
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testEntrySetMutationThrows ()
specifier|public
name|void
name|testEntrySetMutationThrows
parameter_list|()
block|{
name|map
operator|.
name|putInstance
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|setValue
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testEntrySetToArrayMutationThrows ()
specifier|public
name|void
name|testEntrySetToArrayMutationThrows
parameter_list|()
block|{
name|map
operator|.
name|putInstance
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Should get a CCE later if cast is wrong
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testEntrySetToTypedArrayMutationThrows ()
specifier|public
name|void
name|testEntrySetToTypedArrayMutationThrows
parameter_list|()
block|{
name|map
operator|.
name|putInstance
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Should get a CCE later if cast is wrong
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Entry
index|[
literal|0
index|]
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testPutAndGetInstance ()
specifier|public
name|void
name|testPutAndGetInstance
parameter_list|()
block|{
name|assertNull
argument_list|(
name|map
operator|.
name|putInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Integer
name|oldValue
init|=
name|map
operator|.
name|putInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
operator|new
name|Integer
argument_list|(
literal|7
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
operator|(
name|int
operator|)
name|oldValue
argument_list|)
expr_stmt|;
name|Integer
name|newValue
init|=
name|map
operator|.
name|getInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
operator|(
name|int
operator|)
name|newValue
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
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
comment|// Won't compile: map.putInstance(Double.class, new Long(42));
block|}
DECL|method|testNull ()
specifier|public
name|void
name|testNull
parameter_list|()
block|{
try|try
block|{
name|map
operator|.
name|putInstance
argument_list|(
operator|(
name|TypeToken
operator|)
literal|null
argument_list|,
operator|new
name|Integer
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
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|map
operator|.
name|putInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
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
name|map
operator|.
name|putInstance
argument_list|(
name|Long
operator|.
name|class
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|Long
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getInstance
argument_list|(
name|Long
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPrimitiveAndWrapper ()
specifier|public
name|void
name|testPrimitiveAndWrapper
parameter_list|()
block|{
name|assertNull
argument_list|(
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
name|assertNull
argument_list|(
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
name|assertNull
argument_list|(
name|map
operator|.
name|putInstance
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|putInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
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
name|putInstance
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|null
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
name|putInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
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
name|assertNull
argument_list|(
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
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
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
name|map
operator|.
name|putInstance
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
expr_stmt|;
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
DECL|method|testGeneriArrayType ()
specifier|public
name|void
name|testGeneriArrayType
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
name|map
operator|.
name|putInstance
argument_list|(
name|type
argument_list|,
name|array
argument_list|)
expr_stmt|;
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
name|ASSERT
operator|.
name|that
argument_list|(
name|map
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allOf
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
operator|.
name|inOrder
argument_list|()
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
name|map
operator|.
name|putInstance
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
expr_stmt|;
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
DECL|method|testGetInstance_withTypeVariable ()
specifier|public
name|void
name|testGetInstance_withTypeVariable
parameter_list|()
block|{
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
block|{}
block|}
DECL|method|testPutInstance_withTypeVariable ()
specifier|public
name|void
name|testPutInstance_withTypeVariable
parameter_list|()
block|{
try|try
block|{
name|map
operator|.
name|putInstance
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
block|{}
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
block|}
end_class

end_unit

