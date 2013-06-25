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
name|collect
operator|.
name|testing
operator|.
name|Helpers
operator|.
name|orderEntriesByKey
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
name|TestBiMapGenerator
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
name|NullPointerTester
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
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests for {@code EnumBiMap}.  *  * @author Mike Bostock  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumBiMapTest
specifier|public
class|class
name|EnumBiMapTest
extends|extends
name|TestCase
block|{
DECL|enum|Currency
DECL|enumConstant|DOLLAR
DECL|enumConstant|FRANC
DECL|enumConstant|PESO
DECL|enumConstant|POUND
DECL|enumConstant|YEN
specifier|private
enum|enum
name|Currency
block|{
name|DOLLAR
block|,
name|FRANC
block|,
name|PESO
block|,
name|POUND
block|,
name|YEN
block|}
DECL|enum|Country
DECL|enumConstant|CANADA
DECL|enumConstant|CHILE
DECL|enumConstant|JAPAN
DECL|enumConstant|SWITZERLAND
DECL|enumConstant|UK
specifier|private
enum|enum
name|Country
block|{
name|CANADA
block|,
name|CHILE
block|,
name|JAPAN
block|,
name|SWITZERLAND
block|,
name|UK
block|}
DECL|class|EnumBiMapGenerator
specifier|public
specifier|static
specifier|final
class|class
name|EnumBiMapGenerator
implements|implements
name|TestBiMapGenerator
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|create (Object... entries)
specifier|public
name|BiMap
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|entries
parameter_list|)
block|{
name|BiMap
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
name|result
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Country
operator|.
name|class
argument_list|,
name|Currency
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|entries
control|)
block|{
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
operator|)
name|object
decl_stmt|;
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
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
argument_list|>
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|DOLLAR
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|Country
operator|.
name|UK
argument_list|,
name|Currency
operator|.
name|POUND
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|Country
operator|.
name|JAPAN
argument_list|,
name|Currency
operator|.
name|YEN
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|Country
operator|.
name|SWITZERLAND
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
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
annotation|@
name|Override
DECL|method|order (List<Entry<Country, Currency>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|Country
argument_list|,
name|Currency
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|orderEntriesByKey
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeyArray (int length)
specifier|public
name|Country
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Country
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|createValueArray (int length)
specifier|public
name|Currency
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Currency
index|[
name|length
index|]
return|;
block|}
block|}
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
name|BiMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|EnumBiMapGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"EnumBiMap"
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
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
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
name|addTestSuite
argument_list|(
name|EnumBiMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|bimap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{}"
argument_list|,
name|bimap
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashBiMap
operator|.
name|create
argument_list|()
argument_list|,
name|bimap
argument_list|)
expr_stmt|;
name|bimap
operator|.
name|put
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|bimap
operator|.
name|get
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromMap ()
specifier|public
name|void
name|testCreateFromMap
parameter_list|()
block|{
comment|/* Test with non-empty Map. */
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|bimap
operator|.
name|get
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
expr_stmt|;
comment|/* Map must have at least one entry if not an EnumBiMap. */
try|try
block|{
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Collections
operator|.
expr|<
name|Currency
argument_list|,
name|Country
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"IllegalArgumentException expected"
argument_list|)
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
name|EnumBiMap
operator|.
name|create
argument_list|(
name|EnumHashBiMap
operator|.
expr|<
name|Currency
argument_list|,
name|Country
operator|>
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"IllegalArgumentException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
comment|/* Map can be empty if it's an EnumBiMap. */
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|emptyBimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|bimap
operator|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|emptyBimap
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bimap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnumBiMapConstructor ()
specifier|public
name|void
name|testEnumBiMapConstructor
parameter_list|()
block|{
comment|/* Test that it copies existing entries. */
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap1
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|bimap1
operator|.
name|put
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
expr_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap2
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|bimap1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|bimap2
operator|.
name|get
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|bimap1
argument_list|,
name|bimap2
argument_list|)
expr_stmt|;
name|bimap2
operator|.
name|inverse
argument_list|()
operator|.
name|put
argument_list|(
name|Country
operator|.
name|SWITZERLAND
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Country
operator|.
name|SWITZERLAND
argument_list|,
name|bimap2
operator|.
name|get
argument_list|(
name|Currency
operator|.
name|FRANC
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|bimap1
operator|.
name|get
argument_list|(
name|Currency
operator|.
name|FRANC
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|bimap2
operator|.
name|equals
argument_list|(
name|bimap1
argument_list|)
argument_list|)
expr_stmt|;
comment|/* Test that it can be empty. */
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|emptyBimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap3
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|emptyBimap
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|bimap3
argument_list|,
name|emptyBimap
argument_list|)
expr_stmt|;
block|}
DECL|method|testKeyType ()
specifier|public
name|void
name|testKeyType
parameter_list|()
block|{
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|bimap
operator|.
name|keyType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testValueType ()
specifier|public
name|void
name|testValueType
parameter_list|()
block|{
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|Currency
operator|.
name|class
argument_list|,
name|Country
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Country
operator|.
name|class
argument_list|,
name|bimap
operator|.
name|valueType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIterationOrder ()
specifier|public
name|void
name|testIterationOrder
parameter_list|()
block|{
comment|// The enum orderings are alphabetical, leading to the bimap and its inverse
comment|// having inconsistent iteration orderings.
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
decl_stmt|;
comment|// forward map ordered by currency
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// forward map ordered by currency (even for country values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country (even for currency values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testKeySetIteratorRemove ()
specifier|public
name|void
name|testKeySetIteratorRemove
parameter_list|()
block|{
comment|// The enum orderings are alphabetical, leading to the bimap and its inverse
comment|// having inconsistent iteration orderings.
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Currency
argument_list|>
name|iter
init|=
name|bimap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// forward map ordered by currency
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|FRANC
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// forward map ordered by currency (even for country values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|SWITZERLAND
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|CHILE
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country (even for currency values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|PESO
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testValuesIteratorRemove ()
specifier|public
name|void
name|testValuesIteratorRemove
parameter_list|()
block|{
comment|// The enum orderings are alphabetical, leading to the bimap and its inverse
comment|// having inconsistent iteration orderings.
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|Currency
argument_list|>
name|iter
init|=
name|bimap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Currency
operator|.
name|FRANC
argument_list|,
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
comment|// forward map ordered by currency
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// forward map ordered by currency (even for country values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Country
operator|.
name|CANADA
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
comment|// backward map ordered by country (even for currency values)
name|ASSERT
operator|.
name|that
argument_list|(
name|bimap
operator|.
name|inverse
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|exactly
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testEntrySet ()
specifier|public
name|void
name|testEntrySet
parameter_list|()
block|{
comment|// Bug 3168290
name|Map
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|,
name|Currency
operator|.
name|PESO
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|,
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|SWITZERLAND
argument_list|)
decl_stmt|;
name|EnumBiMap
argument_list|<
name|Currency
argument_list|,
name|Country
argument_list|>
name|bimap
init|=
name|EnumBiMap
operator|.
name|create
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Object
argument_list|>
name|uniqueEntries
init|=
name|Sets
operator|.
name|newIdentityHashSet
argument_list|()
decl_stmt|;
name|uniqueEntries
operator|.
name|addAll
argument_list|(
name|bimap
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|uniqueEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"serialization"
argument_list|)
DECL|method|testSerializable ()
specifier|public
name|void
name|testSerializable
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|EnumBiMap
operator|.
name|class
argument_list|)
expr_stmt|;
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
argument_list|,
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|DOLLAR
argument_list|,
name|Country
operator|.
name|CHILE
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|EnumBiMap
operator|.
name|create
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Currency
operator|.
name|FRANC
argument_list|,
name|Country
operator|.
name|CANADA
argument_list|)
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
comment|/* Remaining behavior tested by AbstractBiMapTest. */
block|}
end_class

end_unit

