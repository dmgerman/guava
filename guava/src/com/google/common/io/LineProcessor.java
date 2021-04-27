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
comment|/**  * A callback to be used with the streaming {@code readLines} methods.  *  *<p>{@link #processLine} will be called for each line that is read, and should return {@code  * false} when you want to stop processing.  *  * @author Miles Barr  * @since 1.0  */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_annotation
annotation|@
name|GwtIncompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|LineProcessor
specifier|public
expr|interface
name|LineProcessor
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/**    * This method will be called once for each line.    *    * @param line the line read from the input, without delimiter    * @return true to continue processing, false to stop    */
block|@
name|CanIgnoreReturnValue
comment|// some uses know that their processor never returns false
DECL|method|processLine (String line)
name|boolean
name|processLine
argument_list|(
name|String
name|line
argument_list|)
throws|throws
name|IOException
block|;
comment|/** Return the result of processing all the lines. */
block|@
name|ParametricNullness
DECL|method|getResult ()
name|T
name|getResult
argument_list|()
block|; }
end_expr_stmt

end_unit

