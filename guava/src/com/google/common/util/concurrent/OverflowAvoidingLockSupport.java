begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import static
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
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
name|locks
operator|.
name|LockSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Works around an android bug, where parking for more than INT_MAX seconds can produce an abort  * signal on 32 bit devices running Android Q.  */
end_comment

begin_class
DECL|class|OverflowAvoidingLockSupport
specifier|final
class|class
name|OverflowAvoidingLockSupport
block|{
comment|// Represents the max nanoseconds representable on a linux timespec with a 32 bit tv_sec
DECL|field|MAX_NANOSECONDS_THRESHOLD
specifier|static
specifier|final
name|long
name|MAX_NANOSECONDS_THRESHOLD
init|=
operator|(
literal|1L
operator|+
name|Integer
operator|.
name|MAX_VALUE
operator|)
operator|*
literal|1_000_000_000L
operator|-
literal|1L
decl_stmt|;
DECL|method|OverflowAvoidingLockSupport ()
specifier|private
name|OverflowAvoidingLockSupport
parameter_list|()
block|{}
DECL|method|parkNanos (@ullable Object blocker, long nanos)
specifier|static
name|void
name|parkNanos
parameter_list|(
annotation|@
name|Nullable
name|Object
name|blocker
parameter_list|,
name|long
name|nanos
parameter_list|)
block|{
comment|// Even in the extremely unlikely event that a thread unblocks itself early after only 68 years,
comment|// this is indistinguishable from a spurious wakeup, which LockSupport allows.
name|LockSupport
operator|.
name|parkNanos
argument_list|(
name|blocker
argument_list|,
name|min
argument_list|(
name|nanos
argument_list|,
name|MAX_NANOSECONDS_THRESHOLD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

