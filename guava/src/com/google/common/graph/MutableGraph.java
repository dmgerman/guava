begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_comment
comment|/**  * A subinterface of {@link Graph} which adds mutation methods. When mutation is not required, users  * should prefer the {@link Graph} interface.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|MutableGraph
specifier|public
interface|interface
name|MutableGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|Graph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Adds {@code node} if it is not already present.    *    *<p><b>Nodes must be unique</b>, just as {@code Map} keys must be. They must also be non-null.    *    * @return {@code true} if the graph was modified as a result of this call    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addNode (N node)
name|boolean
name|addNode
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
comment|/**    * Adds an edge connecting {@code nodeU} to {@code nodeV} if one is not already present. In an    * undirected graph, the edge will also connect {@code nodeV} to {@code nodeU}.    *    *<p>Behavior if {@code nodeU} and {@code nodeV} are not already present in this graph is    * implementation-dependent. Suggested behaviors include (a) silently {@link #addNode(Object)    * adding} {@code nodeU} and {@code nodeV} to the graph (this is the behavior of the default    * implementations) or (b) throwing {@code IllegalArgumentException}.    *    * @return {@code true} if the graph was modified as a result of this call    * @throws IllegalArgumentException if the introduction of the edge would violate {@link    *     #allowsSelfLoops()}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdge (N nodeU, N nodeV)
name|boolean
name|putEdge
parameter_list|(
name|N
name|nodeU
parameter_list|,
name|N
name|nodeV
parameter_list|)
function_decl|;
comment|/**    * Removes {@code node} if it is present; all edges incident to {@code node} will also be removed.    *    * @return {@code true} if the graph was modified as a result of this call    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeNode (Object node)
name|boolean
name|removeNode
parameter_list|(
name|Object
name|node
parameter_list|)
function_decl|;
comment|/**    * Removes the edge connecting {@code nodeU} to {@code nodeV}, if it is present.    *    * @return {@code true} if the graph was modified as a result of this call    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeEdge (Object nodeU, Object nodeV)
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|nodeU
parameter_list|,
name|Object
name|nodeV
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

