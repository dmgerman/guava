begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2019 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_comment
comment|/** This class is for {@code com.google.common.util.concurrent} use only! */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// java.time.Duration
DECL|class|Internal
specifier|final
class|class
name|Internal
block|{
comment|/**    * Returns the number of nanoseconds of the given duration without throwing or overflowing.    *    *<p>Instead of throwing {@link ArithmeticException}, this method silently saturates to either    * {@link Long#MAX_VALUE} or {@link Long#MIN_VALUE}. This behavior can be useful when decomposing    * a duration in order to call a legacy API which requires a {@code long, TimeUnit} pair.    */
DECL|method|saturatedToNanos (Duration duration)
specifier|static
name|long
name|saturatedToNanos
parameter_list|(
name|Duration
name|duration
parameter_list|)
block|{
comment|// Using a try/catch seems lazy, but the catch block will rarely get invoked (except for
comment|// durations longer than approximately +/- 292 years).
try|try
block|{
return|return
name|duration
operator|.
name|toNanos
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|tooBig
parameter_list|)
block|{
return|return
name|duration
operator|.
name|isNegative
argument_list|()
condition|?
name|Long
operator|.
name|MIN_VALUE
else|:
name|Long
operator|.
name|MAX_VALUE
return|;
block|}
block|}
DECL|method|Internal ()
specifier|private
name|Internal
parameter_list|()
block|{}
block|}
end_class

end_unit

