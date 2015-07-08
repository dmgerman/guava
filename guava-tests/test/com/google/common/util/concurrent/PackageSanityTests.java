begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|testing
operator|.
name|AbstractPackageSanityTests
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
name|util
operator|.
name|concurrent
operator|.
name|RateLimiter
operator|.
name|SleepingStopwatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Basic sanity tests for the entire package.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|PackageSanityTests
specifier|public
class|class
name|PackageSanityTests
extends|extends
name|AbstractPackageSanityTests
block|{
DECL|field|NO_OP_STOPWATCH
specifier|private
specifier|static
specifier|final
name|SleepingStopwatch
name|NO_OP_STOPWATCH
init|=
operator|new
name|SleepingStopwatch
argument_list|()
block|{
annotation|@
name|Override
name|long
name|readMicros
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
name|void
name|sleepMicrosUninterruptibly
parameter_list|(
name|long
name|micros
parameter_list|)
block|{     }
block|}
decl_stmt|;
DECL|method|PackageSanityTests ()
specifier|public
name|PackageSanityTests
parameter_list|()
block|{
name|setDefault
argument_list|(
name|RateLimiter
operator|.
name|class
argument_list|,
name|RateLimiter
operator|.
name|create
argument_list|(
literal|1.0
argument_list|)
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|SleepingStopwatch
operator|.
name|class
argument_list|,
name|NO_OP_STOPWATCH
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|long
operator|.
name|class
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

