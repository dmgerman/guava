begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
package|;
end_package

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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An interface for representing and manipulating an origin node's adjacent nodes and edge values  * in a {@link Graph}.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  */
end_comment

begin_interface
DECL|interface|GraphConnections
interface|interface
name|GraphConnections
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|NodeConnections
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Returns the value associated with the edge connecting the origin node to {@code node}, or null    * if there is no such edge.    */
DECL|method|value (Object node)
annotation|@
name|Nullable
name|V
name|value
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Remove {@code node} from the set of predecessors.    */
DECL|method|removePredecessor (Object node)
name|void
name|removePredecessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Remove {@code node} from the set of successors. Returns the value previously associated with    * the edge connecting the two nodes.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeSuccessor (Object node)
name|V
name|removeSuccessor
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a predecessor to the origin node. In the case of an undirected graph, it    * also becomes a successor. Associates {@code value} with the edge connecting the two nodes.    */
DECL|method|addPredecessor (N node, V value)
name|void
name|addPredecessor
parameter_list|(
name|N
name|node
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * Add {@code node} as a successor to the origin node. In the case of an undirected graph, it    * also becomes a predecessor. Associates {@code value} with the edge connecting the two nodes.    * Returns the value previously associated with the edge connecting the two nodes.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addSuccessor (N node, V value)
name|V
name|addSuccessor
parameter_list|(
name|N
name|node
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

