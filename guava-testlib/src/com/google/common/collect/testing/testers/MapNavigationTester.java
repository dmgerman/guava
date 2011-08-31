begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
operator|.
name|testers
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
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ONE
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|SEVERAL
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
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
name|AbstractMapTester
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
name|Helpers
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
name|ArrayList
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
name|NavigableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests operations on a NavigableMap. Can't be  * invoked directly; please see {@code MapTestSuiteBuilder}.  *  * @author Jesse Wilson  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|MapNavigationTester
specifier|public
class|class
name|MapNavigationTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|navigableMap
specifier|private
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|navigableMap
decl_stmt|;
DECL|field|entries
specifier|private
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
decl_stmt|;
DECL|field|a
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|b
decl_stmt|;
DECL|field|c
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|c
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|navigableMap
operator|=
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|getMap
argument_list|()
expr_stmt|;
name|entries
operator|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|getSampleElements
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|getCollectionSize
argument_list|()
operator|.
name|getNumElements
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|entries
argument_list|,
name|Helpers
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|entryComparator
argument_list|(
name|navigableMap
operator|.
name|comparator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// some tests assume SEVERAL == 3
if|if
condition|(
name|entries
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|a
operator|=
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|entries
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
name|b
operator|=
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|c
operator|=
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Resets the contents of navigableMap to have entries a, c, for the    * navigation tests.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Needed to stop Eclipse whining
DECL|method|resetWithHole ()
specifier|private
name|void
name|resetWithHole
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
operator|new
name|Entry
index|[]
block|{
name|a
block|,
name|c
block|}
decl_stmt|;
name|super
operator|.
name|resetMap
argument_list|(
name|entries
argument_list|)
expr_stmt|;
name|navigableMap
operator|=
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|getMap
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptyMapFirst ()
specifier|public
name|void
name|testEmptyMapFirst
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|firstEntry
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|navigableMap
operator|.
name|firstKey
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptyMapPollFirst ()
specifier|public
name|void
name|testEmptyMapPollFirst
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|pollFirstEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptyMapNearby ()
specifier|public
name|void
name|testEmptyMapNearby
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lowerEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lowerKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|floorEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|floorKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|ceilingEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|ceilingKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|higherEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|higherKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptyMapLast ()
specifier|public
name|void
name|testEmptyMapLast
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptyMapPollLast ()
specifier|public
name|void
name|testEmptyMapPollLast
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|pollLastEntry
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonMapFirst ()
specifier|public
name|void
name|testSingletonMapFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|firstEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|firstKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonMapPollFirst ()
specifier|public
name|void
name|testSingletonMapPollFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|pollFirstEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|navigableMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonMapNearby ()
specifier|public
name|void
name|testSingletonMapNearby
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lowerEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|lowerKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|floorEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|floorKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|ceilingEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|ceilingKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|higherEntry
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableMap
operator|.
name|higherKey
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonMapLast ()
specifier|public
name|void
name|testSingletonMapLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonMapPollLast ()
specifier|public
name|void
name|testSingletonMapPollLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|pollLastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|navigableMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testFirst ()
specifier|public
name|void
name|testFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|firstEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|firstKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPollFirst ()
specifier|public
name|void
name|testPollFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|pollFirstEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entries
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|copyToList
argument_list|(
name|navigableMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testPollFirstUnsupported ()
specifier|public
name|void
name|testPollFirstUnsupported
parameter_list|()
block|{
try|try
block|{
name|navigableMap
operator|.
name|pollFirstEntry
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testLower ()
specifier|public
name|void
name|testLower
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableMap
operator|.
name|lowerEntry
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableMap
operator|.
name|lowerKey
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|lowerEntry
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|lowerKey
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|lowerEntry
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|lowerKey
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testFloor ()
specifier|public
name|void
name|testFloor
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|floorEntry
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|floorKey
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|floorEntry
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|floorKey
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|floorEntry
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|floorKey
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testCeiling ()
specifier|public
name|void
name|testCeiling
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableMap
operator|.
name|ceilingEntry
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|ceilingKey
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|ceilingEntry
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|ceilingKey
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|ceilingEntry
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|ceilingKey
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testHigher ()
specifier|public
name|void
name|testHigher
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|higherEntry
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|higherKey
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|higherEntry
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|higherKey
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableMap
operator|.
name|higherEntry
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableMap
operator|.
name|higherKey
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testLast ()
specifier|public
name|void
name|testLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|lastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|,
name|navigableMap
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPollLast ()
specifier|public
name|void
name|testPollLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableMap
operator|.
name|pollLastEntry
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entries
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|entries
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|,
name|Helpers
operator|.
name|copyToList
argument_list|(
name|navigableMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPollLastUnsupported ()
specifier|public
name|void
name|testPollLastUnsupported
parameter_list|()
block|{
try|try
block|{
name|navigableMap
operator|.
name|pollLastEntry
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testDescendingNavigation ()
specifier|public
name|void
name|testDescendingNavigation
parameter_list|()
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|descending
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|navigableMap
operator|.
name|descendingMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|descending
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|descending
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entries
argument_list|,
name|descending
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

