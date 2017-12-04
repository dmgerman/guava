begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
package|;
end_package

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

begin_comment
comment|/**  * Some microbenchmarks for the {@link MoreObjects.ToStringHelper} class.  *  * @author Osvaldo Doederlein  */
end_comment

begin_class
DECL|class|ToStringHelperBenchmark
specifier|public
class|class
name|ToStringHelperBenchmark
block|{
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"2"
block|,
literal|"5"
block|,
literal|"10"
block|}
argument_list|)
DECL|field|dataSize
name|int
name|dataSize
decl_stmt|;
DECL|field|NAME
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"abcdefgh"
decl_stmt|;
DECL|field|NAME3
specifier|private
specifier|static
specifier|final
name|String
name|NAME3
init|=
name|Strings
operator|.
name|repeat
argument_list|(
name|NAME
argument_list|,
literal|3
argument_list|)
decl_stmt|;
DECL|method|addEntries (MoreObjects.ToStringHelper helper)
specifier|private
specifier|static
name|void
name|addEntries
parameter_list|(
name|MoreObjects
operator|.
name|ToStringHelper
name|helper
parameter_list|)
block|{
name|helper
operator|.
name|add
argument_list|(
name|NAME
argument_list|,
literal|10
argument_list|)
operator|.
name|addValue
argument_list|(
literal|10L
argument_list|)
operator|.
name|add
argument_list|(
name|NAME
argument_list|,
literal|3.14f
argument_list|)
operator|.
name|addValue
argument_list|(
literal|3.14d
argument_list|)
operator|.
name|add
argument_list|(
name|NAME3
argument_list|,
literal|false
argument_list|)
operator|.
name|add
argument_list|(
name|NAME3
argument_list|,
name|NAME3
argument_list|)
operator|.
name|add
argument_list|(
name|NAME3
argument_list|,
literal|'x'
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|toString (int reps)
name|int
name|toString
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|dummy
init|=
literal|0
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|MoreObjects
operator|.
name|ToStringHelper
name|helper
init|=
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
literal|"klass"
argument_list|)
operator|.
name|omitNullValues
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|dataSize
condition|;
operator|++
name|j
control|)
block|{
name|addEntries
argument_list|(
name|helper
argument_list|)
expr_stmt|;
block|}
name|dummy
operator|^=
name|helper
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit

