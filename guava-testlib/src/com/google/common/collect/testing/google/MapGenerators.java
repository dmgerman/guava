begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
name|google
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
name|mapEntry
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
name|Maps
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
name|AnEnum
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
name|TestEnumMapGenerator
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
name|TestListGenerator
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
name|TestStringListGenerator
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
name|TestStringMapGenerator
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
name|TestUnhashableCollectionGenerator
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
name|UnhashableObject
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
name|EnumMap
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

begin_comment
comment|/**  * Generators of different types of map and related collections, such as  * keys, entries and values.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MapGenerators
specifier|public
class|class
name|MapGenerators
block|{
DECL|class|ImmutableMapGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapGenerator
extends|extends
name|TestStringMapGenerator
block|{
DECL|method|create (Entry<String, String>[] entries)
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
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
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
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableMapCopyOfGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapCopyOfGenerator
extends|extends
name|TestStringMapGenerator
block|{
DECL|method|create (Entry<String, String>[] entries)
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|Maps
operator|.
name|newLinkedHashMap
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
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|builder
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableMapCopyOfEntriesGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapCopyOfEntriesGenerator
extends|extends
name|TestStringMapGenerator
block|{
DECL|method|create (Entry<String, String>[] entries)
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
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|entries
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableMapUnhashableValuesGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapUnhashableValuesGenerator
extends|extends
name|TestUnhashableCollectionGenerator
argument_list|<
name|Collection
argument_list|<
name|UnhashableObject
argument_list|>
argument_list|>
block|{
DECL|method|create ( UnhashableObject[] elements)
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|UnhashableObject
argument_list|>
name|create
parameter_list|(
name|UnhashableObject
index|[]
name|elements
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|UnhashableObject
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|int
name|key
init|=
literal|1
decl_stmt|;
for|for
control|(
name|UnhashableObject
name|value
range|:
name|elements
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|key
operator|++
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|values
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableMapKeyListGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapKeyListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableMapValueListGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapValueListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|i
argument_list|,
name|elements
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableMapEntryListGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapEntryListGenerator
implements|implements
name|TestListGenerator
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
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
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|(
name|mapEntry
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
literal|"bar"
argument_list|,
literal|3
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
literal|"baz"
argument_list|,
literal|17
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
literal|"quux"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|mapEntry
argument_list|(
literal|"toaster"
argument_list|,
operator|-
literal|2
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
name|String
argument_list|,
name|Integer
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
DECL|method|order (List<Entry<String, Integer>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|elements
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
operator|)
name|o
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|asList
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableEnumMapGenerator
specifier|public
specifier|static
class|class
name|ImmutableEnumMapGenerator
extends|extends
name|TestEnumMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<AnEnum, String>[] entries)
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
name|Map
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
comment|// checkArgument(!map.containsKey(entry.getKey()));
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
name|Maps
operator|.
name|immutableEnumMap
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableMapCopyOfEnumMapGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapCopyOfEnumMapGenerator
extends|extends
name|TestEnumMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<AnEnum, String>[] entries)
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
name|EnumMap
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|map
init|=
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
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|AnEnum
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
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<Entry<AnEnum, String>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
operator|new
name|Ordering
argument_list|<
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|left
parameter_list|,
name|Entry
argument_list|<
name|AnEnum
argument_list|,
name|String
argument_list|>
name|right
parameter_list|)
block|{
return|return
name|left
operator|.
name|getKey
argument_list|()
operator|.
name|compareTo
argument_list|(
name|right
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
block|}
operator|.
name|sortedCopy
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
DECL|method|toStringOrNull (Object o)
specifier|private
specifier|static
name|String
name|toStringOrNull
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
operator|(
name|o
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

