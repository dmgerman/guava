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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|ImmutableMap
operator|.
name|Builder
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
name|TestCollectionGenerator
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
name|TestMapEntrySetGenerator
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
name|TestStringSetGenerator
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
name|Collection
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
DECL|class|ImmutableMapKeySetGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapKeySetGenerator
extends|extends
name|TestStringSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
name|Set
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
name|String
name|key
range|:
name|elements
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
literal|4
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
return|;
block|}
block|}
DECL|class|ImmutableMapValuesGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapValuesGenerator
implements|implements
name|TestCollectionGenerator
argument_list|<
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|String
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
operator|.
name|Strings
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|Builder
argument_list|<
name|Object
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
name|Object
name|key
range|:
name|elements
control|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|key
argument_list|)
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
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|String
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|String
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<String> insertionOrder)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
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
DECL|class|ImmutableMapEntrySetGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapEntrySetGenerator
extends|extends
name|TestMapEntrySetGenerator
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
DECL|method|ImmutableMapEntrySetGenerator ()
specifier|public
name|ImmutableMapEntrySetGenerator
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|SampleElements
operator|.
name|Strings
argument_list|()
argument_list|,
operator|new
name|SampleElements
operator|.
name|Strings
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createFromEntries ( Entry<String, String>[] entries)
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|createFromEntries
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
comment|// This null-check forces NPE to be thrown for tests with null
comment|// elements.  Those tests aren't useful in testing entry sets
comment|// because entry sets never have null elements.
name|checkNotNull
argument_list|(
name|entry
argument_list|)
expr_stmt|;
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
operator|.
name|entrySet
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableMapValueListGenerator
specifier|public
specifier|static
class|class
name|ImmutableMapValueListGenerator
implements|implements
name|TestListGenerator
argument_list|<
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|String
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
operator|.
name|Strings
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
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
name|toStringOrNull
argument_list|(
name|elements
index|[
name|i
index|]
argument_list|)
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
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|String
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|String
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<String> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
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

