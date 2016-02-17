begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_comment
comment|/**  * A time source; returns a time value representing the number of nanoseconds elapsed since some  * fixed but arbitrary point in time. Note that most users should use {@link Stopwatch} instead of  * interacting with this class directly.  *  *<p><b>Warning:</b> this interface can only be used to measure elapsed time, not wall time.  *  * @author Kevin Bourrillion  * @since 10.0 (<a href="https://github.com/google/guava/wiki/Compatibility">mostly  *     source-compatible</a> since 9.0)  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|Ticker
specifier|public
specifier|abstract
class|class
name|Ticker
block|{
comment|/**    * Constructor for use by subclasses.    */
DECL|method|Ticker ()
specifier|protected
name|Ticker
parameter_list|()
block|{}
comment|/**    * Returns the number of nanoseconds elapsed since this ticker's fixed point of reference.    */
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(kak): Consider removing this
DECL|method|read ()
specifier|public
specifier|abstract
name|long
name|read
parameter_list|()
function_decl|;
comment|/**    * A ticker that reads the current time using {@link System#nanoTime}.    *    * @since 10.0    */
DECL|method|systemTicker ()
specifier|public
specifier|static
name|Ticker
name|systemTicker
parameter_list|()
block|{
return|return
name|SYSTEM_TICKER
return|;
block|}
DECL|field|SYSTEM_TICKER
specifier|private
specifier|static
specifier|final
name|Ticker
name|SYSTEM_TICKER
init|=
operator|new
name|Ticker
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|long
name|read
parameter_list|()
block|{
return|return
name|Platform
operator|.
name|systemNanoTime
argument_list|()
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

