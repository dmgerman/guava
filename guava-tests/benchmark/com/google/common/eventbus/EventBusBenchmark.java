begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.eventbus
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
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

begin_comment
comment|/**  * Benchmark for {@link EventBus}.  *  * @author Eric Fellheimer  */
end_comment

begin_class
DECL|class|EventBusBenchmark
specifier|public
class|class
name|EventBusBenchmark
block|{
DECL|field|eventBus
specifier|private
name|EventBus
name|eventBus
decl_stmt|;
annotation|@
name|BeforeExperiment
DECL|method|setUp ()
name|void
name|setUp
parameter_list|()
block|{
name|eventBus
operator|=
operator|new
name|EventBus
argument_list|(
literal|"for benchmarking purposes"
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Benchmark
DECL|method|postStrings (int reps)
name|void
name|postStrings
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
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
name|eventBus
operator|.
name|post
argument_list|(
literal|"hello there"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Subscribe
DECL|method|handleStrings (String string)
specifier|public
name|void
name|handleStrings
parameter_list|(
name|String
name|string
parameter_list|)
block|{
comment|// Nothing to do here.
block|}
block|}
end_class

end_unit

