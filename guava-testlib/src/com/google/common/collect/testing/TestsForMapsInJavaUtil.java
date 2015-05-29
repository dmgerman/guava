begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|io
operator|.
name|Serializable
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumMap
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
name|LinkedHashMap
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
name|SortedMap
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentSkipListMap
import|;
end_import

begin_comment
comment|/**  * Generates a test suite covering the {@link Map} implementations in the  * {@link java.util} package. Can be subclassed to specify tests that should  * be suppressed.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|TestsForMapsInJavaUtil
specifier|public
class|class
name|TestsForMapsInJavaUtil
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestsForMapsInJavaUtil
argument_list|()
operator|.
name|allTests
argument_list|()
return|;
block|}
DECL|method|allTests ()
specifier|public
name|Test
name|allTests
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
literal|"java.util Maps"
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForEmptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForSingletonMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForHashMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForLinkedHashMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForTreeMapNatural
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForTreeMapWithComparator
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForEnumMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForConcurrentHashMap
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForConcurrentSkipListMapNatural
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForConcurrentSkipListMapWithComparator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|suppressForEmptyMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForEmptyMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForSingletonMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForSingletonMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForHashMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForHashMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForLinkedHashMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForLinkedHashMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForTreeMapNatural ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForTreeMapNatural
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForTreeMapWithComparator ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForTreeMapWithComparator
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForEnumMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForEnumMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForConcurrentHashMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForConcurrentHashMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForConcurrentSkipListMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForConcurrentSkipListMap
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|testsForEmptyMap ()
specifier|public
name|Test
name|testsForEmptyMap
parameter_list|()
block|{
return|return
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
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"emptyMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ZERO
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForEmptyMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForSingletonMap ()
specifier|public
name|Test
name|testsForSingletonMap
parameter_list|()
block|{
return|return
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
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|entries
index|[
literal|0
index|]
operator|.
name|getKey
argument_list|()
argument_list|,
name|entries
index|[
literal|0
index|]
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"singletonMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
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
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ONE
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForSingletonMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForHashMap ()
specifier|public
name|Test
name|testsForHashMap
parameter_list|()
block|{
return|return
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
return|return
name|toHashMap
argument_list|(
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"HashMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForHashMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForLinkedHashMap ()
specifier|public
name|Test
name|testsForLinkedHashMap
parameter_list|()
block|{
return|return
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
return|return
name|populate
argument_list|(
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"LinkedHashMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForLinkedHashMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForTreeMapNatural ()
specifier|public
name|Test
name|testsForTreeMapNatural
parameter_list|()
block|{
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMap
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
comment|/*                * TODO(cpovirk): it would be nice to create an input Map and use                * the copy constructor here and in the other tests                */
return|return
name|populate
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"TreeMap, natural"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForTreeMapNatural
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForTreeMapWithComparator ()
specifier|public
name|Test
name|testsForTreeMapWithComparator
parameter_list|()
block|{
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMap
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
return|return
name|populate
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|arbitraryNullFriendlyComparator
argument_list|()
argument_list|)
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"TreeMap, with comparator"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForTreeMapWithComparator
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForEnumMap ()
specifier|public
name|Test
name|testsForEnumMap
parameter_list|()
block|{
return|return
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestEnumMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
return|return
name|populate
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
argument_list|(
name|AnEnum
operator|.
name|class
argument_list|)
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"EnumMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|RESTRICTS_KEYS
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForEnumMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForConcurrentHashMap ()
specifier|public
name|Test
name|testsForConcurrentHashMap
parameter_list|()
block|{
return|return
name|ConcurrentMapTestSuiteBuilder
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
return|return
name|populate
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ConcurrentHashMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
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
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForConcurrentHashMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForConcurrentSkipListMapNatural ()
specifier|public
name|Test
name|testsForConcurrentSkipListMapNatural
parameter_list|()
block|{
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMap
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
return|return
name|populate
argument_list|(
operator|new
name|ConcurrentSkipListMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ConcurrentSkipListMap, natural"
argument_list|)
operator|.
name|withFeatures
argument_list|(
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
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForConcurrentSkipListMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForConcurrentSkipListMapWithComparator ()
specifier|public
name|Test
name|testsForConcurrentSkipListMapWithComparator
parameter_list|()
block|{
return|return
name|NavigableMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSortedMapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|SortedMap
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
return|return
name|populate
argument_list|(
operator|new
name|ConcurrentSkipListMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|arbitraryNullFriendlyComparator
argument_list|()
argument_list|)
argument_list|,
name|entries
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ConcurrentSkipListMap, with comparator"
argument_list|)
operator|.
name|withFeatures
argument_list|(
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
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForConcurrentSkipListMap
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
comment|// TODO: IdentityHashMap, AbstractMap
DECL|method|toHashMap ( Entry<String, String>[] entries)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toHashMap
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
return|return
name|populate
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|,
name|entries
argument_list|)
return|;
block|}
comment|// TODO: call conversion constructors or factory methods instead of using
comment|// populate() on an empty map
DECL|method|populate ( M map, Entry<T, String>[] entries)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|,
name|M
extends|extends
name|Map
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
parameter_list|>
name|M
name|populate
parameter_list|(
name|M
name|map
parameter_list|,
name|Entry
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|T
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
name|map
return|;
block|}
DECL|method|arbitraryNullFriendlyComparator ()
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Comparator
argument_list|<
name|T
argument_list|>
name|arbitraryNullFriendlyComparator
parameter_list|()
block|{
return|return
operator|new
name|NullFriendlyComparator
argument_list|<
name|T
argument_list|>
argument_list|()
return|;
block|}
DECL|class|NullFriendlyComparator
specifier|private
specifier|static
specifier|final
class|class
name|NullFriendlyComparator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Comparator
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
annotation|@
name|Override
DECL|method|compare (T left, T right)
specifier|public
name|int
name|compare
parameter_list|(
name|T
name|left
parameter_list|,
name|T
name|right
parameter_list|)
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|left
argument_list|)
operator|.
name|compareTo
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|right
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

