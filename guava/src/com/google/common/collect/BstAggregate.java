begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An integer-valued function on binary search tree nodes that adds between nodes.  *   * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BstAggregate
specifier|public
interface|interface
name|BstAggregate
parameter_list|<
name|N
extends|extends
name|BstNode
parameter_list|<
name|?
parameter_list|,
name|N
parameter_list|>
parameter_list|>
block|{
comment|/**    * The total value on an entire subtree. Must be equal to the sum of the {@link #entryValue    * entryValue} of this node and all its descendants.    */
DECL|method|treeValue (@ullable N tree)
name|int
name|treeValue
parameter_list|(
annotation|@
name|Nullable
name|N
name|tree
parameter_list|)
function_decl|;
comment|/**    * The value on a single entry, ignoring its descendants.    */
DECL|method|entryValue (N entry)
name|int
name|entryValue
parameter_list|(
name|N
name|entry
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

