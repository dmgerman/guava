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
comment|/**  * A subinterface of {@link Network} which adds mutation methods. When mutation is not required,  * users should prefer the {@link Network} interface.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|MutableNetwork
specifier|public
interface|interface
name|MutableNetwork
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|Network
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|/**    * Adds {@code node} if it is not already present.    *    *<p><b>Nodes must be unique</b>, just as {@code Map} keys must be. They must also be non-null.    *    * @return {@code true} iff the graph was modified as a result of this call    */
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
comment|/**    * Adds {@code edge} connecting {@code nodeA} to {@code nodeB}.    *    *<p><b>Edges must be unique</b>, just as {@code Map} keys must be. They must also be non-null.    *    *<p>Behavior if {@code nodeA} and {@code nodeB} are not already present in this graph is    * implementation-dependent. Suggested behaviors include (a) silently {@link #addNode(Object)    * adding} {@code nodeA} and {@code nodeB} to the graph (this is the behavior of the default    * implementations) or (b) throwing {@code IllegalArgumentException}.    *    *<p>If {@code edge} already connects {@code nodeA} to {@code nodeB} (in the specified order if    * this graph {@link #isDirected()}, else in any order), then this method will have no effect.    *    * @return {@code true} iff the graph was modified as a result of this call    * @throws IllegalArgumentException if {@code edge} already exists and does not connect {@code    *     nodeA} to {@code nodeB}, or if the introduction of the edge would violate {@link    *     #allowsParallelEdges()} or {@link #allowsSelfLoops()}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (N nodeA, N nodeB, E edge)
name|boolean
name|addEdge
parameter_list|(
name|N
name|nodeA
parameter_list|,
name|N
name|nodeB
parameter_list|,
name|E
name|edge
parameter_list|)
function_decl|;
comment|/**    * Removes {@code node} if it is present; all edges incident to {@code node} will also be removed.    *    * @return {@code true} iff the graph was modified as a result of this call    */
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
comment|/**    * Removes {@code edge} from this graph, if it is present.    *    * @return {@code true} iff the graph was modified as a result of this call    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|removeEdge (Object edge)
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

