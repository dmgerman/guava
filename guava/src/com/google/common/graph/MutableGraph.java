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
comment|/**  * A subtype of {@link Graph} which permits mutations.  * Users should generally use the {@link Graph} interface where possible.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @since 20.0  */
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
comment|/**    * Adds {@code node} to this graph if it is not already present.    *    *<p><b>Nodes must be unique</b>, just as {@code Map} keys must be; they must also be non-null.    *    * @return {@code true} iff the graph was modified as a result of this call    */
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
comment|/**    * Adds an (implicit) edge connecting {@code nodeA} to {@code nodeB} to this graph, if such an    * edge is not already present.    *    *<p>Behavior if {@code nodeA} and {@code nodeB} are not already elements of the graph is    * unspecified. Suggested behaviors include (a) silently adding {@code nodeA} and {@code nodeB}    * to the graph (this is the behavior of the default graph implementations) or (b) throwing    * {@code IllegalArgumentException}.    *    * @return {@code true} iff the graph was modified as a result of this call    * @throws IllegalArgumentException if the introduction of the edge would violate    *     {@link #allowsSelfLoops()}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|putEdge (N nodeA, N nodeB)
name|boolean
name|putEdge
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|)
function_decl|;
comment|/**    * Removes {@code node} from this graph, if it is present.    * All edges incident to {@code node} in this graph will also be removed.    *    * @return {@code true} iff the graph was modified as a result of this call    */
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
comment|/**    * Removes the edge connecting {@code nodeA} to {@code nodeB} from this graph, if it is present.    *    *<p>In general, the input nodes are unaffected (although implementations may choose    * to disallow certain configurations, e.g., isolated nodes).    *    * @return {@code true} iff the graph was modified as a result of this call    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeEdge (Object nodeA, Object nodeB)
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

