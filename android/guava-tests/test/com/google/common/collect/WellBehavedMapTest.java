begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|GwtCompatible
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
name|Map
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

begin_class
annotation|@
name|GwtCompatible
DECL|class|WellBehavedMapTest
specifier|public
class|class
name|WellBehavedMapTest
extends|extends
name|TestCase
block|{
DECL|enum|Foo
enum|enum
name|Foo
block|{
DECL|enumConstant|X
DECL|enumConstant|Y
DECL|enumConstant|Z
DECL|enumConstant|T
name|X
block|,
name|Y
block|,
name|Z
block|,
name|T
block|}
DECL|method|testEntrySet_contain ()
specifier|public
name|void
name|testEntrySet_contain
parameter_list|()
block|{
name|WellBehavedMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|1
argument_list|,
name|Foo
operator|.
name|Y
argument_list|,
literal|2
argument_list|,
name|Foo
operator|.
name|Z
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// testing with the exact entry
name|assertTrue
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|Y
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// testing an entry with a contained key, but not the same value
name|assertFalse
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// testing a non-existent key
name|assertFalse
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|T
argument_list|,
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntry_setValue ()
specifier|public
name|void
name|testEntry_setValue
parameter_list|()
block|{
name|WellBehavedMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|1
argument_list|,
name|Foo
operator|.
name|Y
argument_list|,
literal|2
argument_list|,
name|Foo
operator|.
name|Z
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|entry
operator|.
name|setValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|+
literal|5
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|6
argument_list|,
name|Foo
operator|.
name|Y
argument_list|,
literal|7
argument_list|,
name|Foo
operator|.
name|Z
argument_list|,
literal|8
argument_list|)
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntriesAreMutableAndConsistent ()
specifier|public
name|void
name|testEntriesAreMutableAndConsistent
parameter_list|()
block|{
name|WellBehavedMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|entry1
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|entry2
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
comment|// the entries are constructed and forgotten, thus different
name|assertNotSame
argument_list|(
name|entry1
argument_list|,
name|entry2
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
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
operator|.
name|contains
argument_list|(
name|entry1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|entry2
argument_list|)
argument_list|)
expr_stmt|;
comment|// mutating entry
name|entry1
operator|.
name|setValue
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// entry2 is also modified
name|assertEquals
argument_list|(
name|entry1
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry2
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// and both are still contained in the set
name|assertTrue
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|entry1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|entry2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet_remove ()
specifier|public
name|void
name|testEntrySet_remove
parameter_list|()
block|{
name|WellBehavedMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|X
argument_list|,
literal|1
argument_list|,
name|Foo
operator|.
name|Y
argument_list|,
literal|2
argument_list|,
name|Foo
operator|.
name|Z
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
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
comment|// removing an existing entry, verifying consistency
name|Map
operator|.
name|Entry
argument_list|<
name|Foo
argument_list|,
name|Integer
argument_list|>
name|entry
init|=
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|Y
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|Foo
operator|.
name|Y
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|Foo
operator|.
name|Y
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
comment|// we didn't have that entry, not removed
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|T
argument_list|,
literal|4
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// we didn't have that entry, only<Z, 3>, must not remove
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|remove
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|Foo
operator|.
name|Z
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
