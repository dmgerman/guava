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
comment|/**  * A Ticker whose value can be advanced programmatically in test.  *<p>  * This class is thread-safe.  *  * @author Jige Yu  * @since Guava release 10  */
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
comment|/** Advances the ticker value by {@code time} in {@code timeUnit}. */
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
DECL|method|read ()
annotation|@
name|Override
specifier|public
name|long
name|read
parameter_list|()
block|{
return|return
name|nanos
operator|.
name|get
argument_list|()
return|;
block|}
block|}
end_class

end_unit

