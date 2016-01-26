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
comment|/**  * A subinterface of {@code Graph} which specifies that all edges are hyperedges, that is,  * they connect arbitrary sets of nodes rather than pairs of nodes.  *  *<p>A few notes about how hyperedges and connectivity:  *<ul>  *<li>Hyperedges, like undirected edges, are both incoming and outgoing edges.  *<li>Hyperedges incident to a single node {@code node} connect {@code node} to itself; such edges  *     are analogous to self-loops in graphs.  Hyperedges incident to> 1 nodes do not connect any  *     of their incident nodes to themselves.  *</ul>  *  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|Hypergraph
specifier|public
interface|interface
name|Hypergraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
comment|/**    * Adds {@code edge} to this graph, connecting {@code nodes}.    *    * @return {@code true} iff the graph was modified as a result of this call    * @throws UnsupportedOperationException if the add operation is not supported by this graph    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (E edge, N... nodes)
name|boolean
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
modifier|...
name|nodes
parameter_list|)
function_decl|;
comment|/**    * Adds {@code edge} to this graph, connecting {@code nodes}.    *    * @return {@code true} iff the graph was modified as a result of this call    * @throws UnsupportedOperationException if the add operation is not supported by this graph    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (E edge, Iterable<N> nodes)
name|boolean
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|Iterable
argument_list|<
name|N
argument_list|>
name|nodes
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

