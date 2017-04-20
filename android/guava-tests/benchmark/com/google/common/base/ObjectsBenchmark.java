begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Some microbenchmarks for the {@link com.google.common.base.Objects} class.  *  * @author Ben L. Titzer  */
end_comment

begin_class
DECL|class|ObjectsBenchmark
specifier|public
class|class
name|ObjectsBenchmark
block|{
DECL|field|I0
specifier|private
specifier|static
specifier|final
name|Integer
name|I0
init|=
operator|-
literal|45
decl_stmt|;
DECL|field|I1
specifier|private
specifier|static
specifier|final
name|Integer
name|I1
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|I2
specifier|private
specifier|static
specifier|final
name|Integer
name|I2
init|=
literal|3
decl_stmt|;
DECL|field|S0
specifier|private
specifier|static
specifier|final
name|String
name|S0
init|=
literal|"3"
decl_stmt|;
DECL|field|S1
specifier|private
specifier|static
specifier|final
name|String
name|S1
init|=
literal|"Ninety five"
decl_stmt|;
DECL|field|S2
specifier|private
specifier|static
specifier|final
name|String
name|S2
init|=
literal|"44 one million"
decl_stmt|;
DECL|field|S3
specifier|private
specifier|static
specifier|final
name|String
name|S3
init|=
literal|"Lowly laundry lefties"
decl_stmt|;
DECL|field|S4
specifier|private
specifier|static
specifier|final
name|String
name|S4
init|=
literal|"89273487U#*&#"
decl_stmt|;
DECL|field|D0
specifier|private
specifier|static
specifier|final
name|Double
name|D0
init|=
literal|9.234d
decl_stmt|;
DECL|field|D1
specifier|private
specifier|static
specifier|final
name|Double
name|D1
init|=
operator|-
literal|1.2e55
decl_stmt|;
DECL|method|hashString_2 (int reps)
annotation|@
name|Benchmark
name|int
name|hashString_2
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
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|S0
argument_list|,
name|S1
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|hashString_3 (int reps)
annotation|@
name|Benchmark
name|int
name|hashString_3
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
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|S0
argument_list|,
name|S1
argument_list|,
name|S2
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|hashString_4 (int reps)
annotation|@
name|Benchmark
name|int
name|hashString_4
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
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|S0
argument_list|,
name|S1
argument_list|,
name|S2
argument_list|,
name|S3
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|hashString_5 (int reps)
annotation|@
name|Benchmark
name|int
name|hashString_5
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
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|S0
argument_list|,
name|S1
argument_list|,
name|S2
argument_list|,
name|S3
argument_list|,
name|S4
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
DECL|method|hashMixed_5 (int reps)
annotation|@
name|Benchmark
name|int
name|hashMixed_5
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
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|I2
argument_list|,
name|S1
argument_list|,
name|D1
argument_list|,
name|S2
argument_list|,
name|I0
argument_list|)
expr_stmt|;
name|dummy
operator|+=
name|Objects
operator|.
name|hashCode
argument_list|(
name|D0
argument_list|,
name|I1
argument_list|,
name|S3
argument_list|,
name|I2
argument_list|,
name|S0
argument_list|)
expr_stmt|;
block|}
return|return
name|dummy
return|;
block|}
block|}
end_class

end_unit

