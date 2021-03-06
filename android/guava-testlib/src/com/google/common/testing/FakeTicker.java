begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
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
name|checkArgument
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
name|Beta
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
name|base
operator|.
name|Ticker
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * A Ticker whose value can be advanced programmatically in test.  *  *<p>The ticker can be configured so that the time is incremented whenever {@link #read} is called:  * see {@link #setAutoIncrementStep}.  *  *<p>This class is thread-safe.  *  * @author Jige Yu  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|FakeTicker
specifier|public
class|class
name|FakeTicker
extends|extends
name|Ticker
block|{
DECL|field|nanos
specifier|private
specifier|final
name|AtomicLong
name|nanos
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|autoIncrementStepNanos
specifier|private
specifier|volatile
name|long
name|autoIncrementStepNanos
decl_stmt|;
comment|/** Advances the ticker value by {@code time} in {@code timeUnit}. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|advance (long time, TimeUnit timeUnit)
specifier|public
name|FakeTicker
name|advance
parameter_list|(
name|long
name|time
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
return|return
name|advance
argument_list|(
name|timeUnit
operator|.
name|toNanos
argument_list|(
name|time
argument_list|)
argument_list|)
return|;
block|}
comment|/** Advances the ticker value by {@code nanoseconds}. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|advance (long nanoseconds)
specifier|public
name|FakeTicker
name|advance
parameter_list|(
name|long
name|nanoseconds
parameter_list|)
block|{
name|nanos
operator|.
name|addAndGet
argument_list|(
name|nanoseconds
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the increment applied to the ticker whenever it is queried.    *    *<p>The default behavior is to auto increment by zero. i.e: The ticker is left unchanged when    * queried.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|setAutoIncrementStep (long autoIncrementStep, TimeUnit timeUnit)
specifier|public
name|FakeTicker
name|setAutoIncrementStep
parameter_list|(
name|long
name|autoIncrementStep
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|autoIncrementStep
operator|>=
literal|0
argument_list|,
literal|"May not auto-increment by a negative amount"
argument_list|)
expr_stmt|;
name|this
operator|.
name|autoIncrementStepNanos
operator|=
name|timeUnit
operator|.
name|toNanos
argument_list|(
name|autoIncrementStep
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|long
name|read
parameter_list|()
block|{
return|return
name|nanos
operator|.
name|getAndAdd
argument_list|(
name|autoIncrementStepNanos
argument_list|)
return|;
block|}
block|}
end_class

end_unit

