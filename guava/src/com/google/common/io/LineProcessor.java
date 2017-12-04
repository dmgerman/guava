begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|GwtIncompatible
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
comment|/**  * A callback to be used with the streaming {@code readLines} methods.  *  *<p>{@link #processLine} will be called for each line that is read, and should return {@code  * false} when you want to stop processing.  *  * @author Miles Barr  * @since 1.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|interface|LineProcessor
specifier|public
interface|interface
name|LineProcessor
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * This method will be called once for each line.    *    * @param line the line read from the input, without delimiter    * @return true to continue processing, false to stop    */
annotation|@
name|CanIgnoreReturnValue
comment|// some uses know that their processor never returns false
DECL|method|processLine (String line)
name|boolean
name|processLine
parameter_list|(
name|String
name|line
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/** Return the result of processing all the lines. */
DECL|method|getResult ()
name|T
name|getResult
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

