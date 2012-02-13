begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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

begin_comment
comment|/**  * An object which can send data from an object of type {@code T} into a {@code PrimitiveSink}.  *   * @author Dimitris Andreou  * @since 11.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Funnel
specifier|public
interface|interface
name|Funnel
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Sends a stream of data from the {@code from} object into the sink {@code into}. There    * is no requirement that this data be complete enough to fully reconstitute the object    * later.    *    * @since 12.0 (in 11.0 version, {@code PrimitiveSink} was still called {@code Sink})    */
DECL|method|funnel (T from, PrimitiveSink into)
name|void
name|funnel
parameter_list|(
name|T
name|from
parameter_list|,
name|PrimitiveSink
name|into
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

