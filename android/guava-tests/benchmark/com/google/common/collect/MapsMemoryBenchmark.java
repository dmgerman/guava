begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2017 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Functions
operator|.
name|toStringFunction
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
name|Maps
operator|.
name|uniqueIndex
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Param
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|api
operator|.
name|Footprint
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
name|BenchmarkHelpers
operator|.
name|BiMapImpl
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
name|BenchmarkHelpers
operator|.
name|MapImpl
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
name|BenchmarkHelpers
operator|.
name|MapsImplEnum
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
name|BenchmarkHelpers
operator|.
name|SortedMapImpl
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
name|CollectionBenchmarkSampleData
operator|.
name|Element
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
name|Map
import|;
end_import

begin_comment
comment|/** Benchmarks for memory consumption of map implementations. */
end_comment

begin_class
DECL|class|MapsMemoryBenchmark
specifier|public
class|class
name|MapsMemoryBenchmark
block|{
DECL|field|mapEnums
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|MapsImplEnum
argument_list|>
name|mapEnums
init|=
name|uniqueIndex
argument_list|(
name|Iterables
operator|.
name|concat
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|MapImpl
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SortedMapImpl
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|BiMapImpl
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|toStringFunction
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"HashMapImpl"
block|,
literal|"LinkedHashMapImpl"
block|,
literal|"ConcurrentHashMapImpl"
block|,
literal|"ImmutableMapImpl"
block|,
literal|"TreeMapImpl"
block|,
literal|"ImmutableSortedMapImpl"
block|,
literal|"MapMakerWeakKeysWeakValues"
block|,
literal|"MapMakerWeakKeysStrongValues"
block|,
literal|"MapMakerStrongKeysWeakValues"
block|,
literal|"MapMakerStrongKeysStrongValues"
block|,
literal|"HashBiMapImpl"
block|,
literal|"ImmutableBiMapImpl"
block|}
argument_list|)
DECL|field|implName
name|String
name|implName
decl_stmt|;
DECL|field|mapsImpl
name|MapsImplEnum
name|mapsImpl
decl_stmt|;
comment|/**    * A map of contents pre-created before experiment starts to only measure map creation cost.    * The implementation for the creation of contents is independent and could be different from    * that of the map under test.    */
DECL|field|contents
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|contents
decl_stmt|;
comment|/**    * Map pre-created before experiment starts to only measure iteration cost during experiment.    */
DECL|field|map
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|map
decl_stmt|;
DECL|field|elems
name|CollectionBenchmarkSampleData
name|elems
decl_stmt|;
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"1"
block|,
literal|"100"
block|,
literal|"10000"
block|}
argument_list|)
DECL|field|elements
name|int
name|elements
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|prepareContents ()
specifier|public
name|void
name|prepareContents
parameter_list|()
throws|throws
name|Exception
block|{
name|mapsImpl
operator|=
name|mapEnums
operator|.
name|get
argument_list|(
name|implName
argument_list|)
expr_stmt|;
name|elems
operator|=
operator|new
name|CollectionBenchmarkSampleData
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|contents
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|Element
name|key
range|:
name|elems
operator|.
name|getValuesInSet
argument_list|()
control|)
block|{
name|contents
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|map
operator|=
name|mapsImpl
operator|.
name|create
argument_list|(
name|contents
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
annotation|@
name|Footprint
argument_list|(
name|exclude
operator|=
name|Element
operator|.
name|class
argument_list|)
DECL|method|create ()
specifier|public
name|Map
argument_list|<
name|Element
argument_list|,
name|Element
argument_list|>
name|create
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|mapsImpl
operator|.
name|create
argument_list|(
name|contents
argument_list|)
return|;
block|}
annotation|@
name|Benchmark
DECL|method|iterate ()
specifier|public
name|int
name|iterate
parameter_list|()
block|{
name|long
name|retVal
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|retVal
operator|+=
name|entry
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|int
operator|)
name|retVal
return|;
block|}
annotation|@
name|Benchmark
DECL|method|keyIterate ()
specifier|public
name|int
name|keyIterate
parameter_list|()
block|{
name|long
name|retVal
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|retVal
operator|+=
name|key
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|int
operator|)
name|retVal
return|;
block|}
block|}
end_class

end_unit

