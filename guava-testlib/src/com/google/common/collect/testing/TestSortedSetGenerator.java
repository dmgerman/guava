begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Creates sorted sets, containing sample elements, to be tested.  *  *<p>This class is GWT compatible.  *   * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|TestSortedSetGenerator
specifier|public
interface|interface
name|TestSortedSetGenerator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|TestSetGenerator
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (Object... elements)
name|SortedSet
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
function_decl|;
comment|/**    * Returns an element less than the {@link #samples()} and less than    * {@link #belowSamplesGreater()}.    */
DECL|method|belowSamplesLesser ()
name|E
name|belowSamplesLesser
parameter_list|()
function_decl|;
comment|/**    * Returns an element less than the {@link #samples()} but greater than    * {@link #belowSamplesLesser()}.    */
DECL|method|belowSamplesGreater ()
name|E
name|belowSamplesGreater
parameter_list|()
function_decl|;
comment|/**    * Returns an element greater than the {@link #samples()} but less than    * {@link #aboveSamplesGreater()}.    */
DECL|method|aboveSamplesLesser ()
name|E
name|aboveSamplesLesser
parameter_list|()
function_decl|;
comment|/**    * Returns an element greater than the {@link #samples()} and greater than    * {@link #aboveSamplesLesser()}.    */
DECL|method|aboveSamplesGreater ()
name|E
name|aboveSamplesGreater
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

