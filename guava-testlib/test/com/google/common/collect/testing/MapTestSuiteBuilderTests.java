begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ALLOWS_NULL_KEYS
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
name|ALLOWS_NULL_VALUES
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
name|Lists
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
name|Feature
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
name|AbstractMap
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

begin_comment
comment|/**  * Tests {@link MapTestSuiteBuilder} by using it against maps that have various  * negative behaviors.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|MapTestSuiteBuilderTests
specifier|public
specifier|final
class|class
name|MapTestSuiteBuilderTests
extends|extends
name|TestCase
block|{
DECL|method|MapTestSuiteBuilderTests ()
specifier|private
name|MapTestSuiteBuilderTests
parameter_list|()
block|{}
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
argument_list|(
name|MapTestSuiteBuilderTests
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForHashMapNullKeysForbidden
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForHashMapNullValuesForbidden
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|class|WrappedHashMapGenerator
specifier|private
specifier|abstract
specifier|static
class|class
name|WrappedHashMapGenerator
extends|extends
name|TestStringMapGenerator
block|{
DECL|method|create ( Map.Entry<String, String>[] entries)
annotation|@
name|Override
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Map
operator|.
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
name|HashMap
argument_list|<
name|String
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
name|Map
operator|.
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
name|wrap
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|method|wrap (HashMap<String, String> map)
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wrap
parameter_list|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
function_decl|;
block|}
DECL|method|wrappedHashMapTests ( WrappedHashMapGenerator generator, String name, Feature<?>... features)
specifier|private
specifier|static
name|TestSuite
name|wrappedHashMapTests
parameter_list|(
name|WrappedHashMapGenerator
name|generator
parameter_list|,
name|String
name|name
parameter_list|,
name|Feature
argument_list|<
name|?
argument_list|>
modifier|...
name|features
parameter_list|)
block|{
return|return
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
name|generator
argument_list|)
operator|.
name|named
argument_list|(
name|name
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|Lists
operator|.
name|asList
argument_list|(
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|features
argument_list|)
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
comment|// TODO: consider being null-hostile in these tests
DECL|method|testsForHashMapNullKeysForbidden ()
specifier|private
specifier|static
name|Test
name|testsForHashMapNullKeysForbidden
parameter_list|()
block|{
return|return
name|wrappedHashMapTests
argument_list|(
operator|new
name|WrappedHashMapGenerator
argument_list|()
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|.
name|containsKey
argument_list|(
literal|null
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
return|return
operator|new
name|AbstractMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"HashMap w/out null keys"
argument_list|,
name|ALLOWS_NULL_VALUES
argument_list|)
return|;
block|}
DECL|method|testsForHashMapNullValuesForbidden ()
specifier|private
specifier|static
name|Test
name|testsForHashMapNullValuesForbidden
parameter_list|()
block|{
return|return
name|wrappedHashMapTests
argument_list|(
operator|new
name|WrappedHashMapGenerator
argument_list|()
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|.
name|containsValue
argument_list|(
literal|null
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
return|return
operator|new
name|AbstractMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|put
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"HashMap w/out null values"
argument_list|,
name|ALLOWS_NULL_KEYS
argument_list|)
return|;
block|}
block|}
end_class

end_unit

