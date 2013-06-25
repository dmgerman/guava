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
name|annotations
operator|.
name|GwtCompatible
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
name|annotations
operator|.
name|GwtIncompatible
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
name|ListMultimapTestSuiteBuilder
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
name|TestStringListMultimapGenerator
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
name|util
operator|.
name|ConcurrentModificationException
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
name|RandomAccess
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@code ArrayListMultimap}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ArrayListMultimapTest
specifier|public
class|class
name|ArrayListMultimapTest
extends|extends
name|AbstractMultimapTest
block|{
annotation|@
name|GwtIncompatible
argument_list|(
literal|"suite"
argument_list|)
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
name|addTest
argument_list|(
name|ListMultimapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringListMultimapGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ListMultimap
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
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
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
name|multimap
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
name|multimap
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ArrayListMultimap"
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
name|GENERAL_PURPOSE
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
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|ArrayListMultimapTest
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
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|ArrayListMultimap
operator|.
name|create
argument_list|()
return|;
block|}
comment|/**    * Confirm that get() returns a List implementing RandomAccess.    */
DECL|method|testGetRandomAccess ()
specifier|public
name|void
name|testGetRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
comment|/**    * Confirm that removeAll() returns a List implementing RandomAccess.    */
DECL|method|testRemoveAllRandomAccess ()
specifier|public
name|void
name|testRemoveAllRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|removeAll
argument_list|(
literal|"bar"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
comment|/**    * Confirm that replaceValues() returns a List implementing RandomAccess.    */
DECL|method|testReplaceValuesRandomAccess ()
specifier|public
name|void
name|testReplaceValuesRandomAccess
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"foo"
argument_list|,
name|asList
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|replaceValues
argument_list|(
literal|"bar"
argument_list|,
name|asList
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test throwing ConcurrentModificationException when a sublist's ancestor's    * delegate changes.    */
DECL|method|testSublistConcurrentModificationException ()
specifier|public
name|void
name|testSublistConcurrentModificationException
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|putAll
argument_list|(
literal|"foo"
argument_list|,
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|sublist
init|=
name|list
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|sublist
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|sublist
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|sublist
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
try|try
block|{
name|sublist
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testCreateFromMultimap ()
specifier|public
name|void
name|testCreateFromMultimap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createSample
argument_list|()
decl_stmt|;
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|(
name|multimap
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromSizes ()
specifier|public
name|void
name|testCreateFromSizes
parameter_list|()
block|{
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|(
literal|15
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIllegalSizes ()
specifier|public
name|void
name|testCreateFromIllegalSizes
parameter_list|()
block|{
try|try
block|{
name|ArrayListMultimap
operator|.
name|create
argument_list|(
literal|15
argument_list|,
operator|-
literal|2
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
try|try
block|{
name|ArrayListMultimap
operator|.
name|create
argument_list|(
operator|-
literal|15
argument_list|,
literal|2
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
DECL|method|testCreateFromHashMultimap ()
specifier|public
name|void
name|testCreateFromHashMultimap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|original
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromArrayListMultimap ()
specifier|public
name|void
name|testCreateFromArrayListMultimap
parameter_list|()
block|{
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|original
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|(
literal|15
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testTrimToSize ()
specifier|public
name|void
name|testTrimToSize
parameter_list|()
block|{
name|ArrayListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|trimToSize
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multimap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|item
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

