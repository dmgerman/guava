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

begin_comment
comment|/**  * Configurable implementation of an undirected graph consisting of nodes of type N  * and edges of type E.  *  *<p>{@link Graphs#createUndirected} should be used to get an instance of this class.  *  *<p>Some invariants/assumptions are maintained in this implementation:  *<ul>  *<li>An edge has exactly two end-points, which may or may not be distinct.  *<li>By default, this is not a multigraph, that is, parallel edges (multiple  *     edges between node1 and node2) are not allowed.  If you want a multigraph,  *     create the graph with the 'multigraph' option:  *<pre>Graphs.createUndirected(Graphs.config().multigraph());</pre>  *<li>By default, self-loop edges are allowed. If you want to disallow them,  *     create the graph without the option of self-loops:  *<pre>Graphs.createUndirected(Graphs.config().noSelfLoops());</pre>  *<li>Edges are not adjacent to themselves by definition. In the case of a  *     self-loop, a node can be adjacent to itself, but an edge will never be.  *</ul>  *  *<p>Time complexities for mutation methods:  *<ul>  *<li>{@code addNode}: O(1).  *<li>{@code addEdge(E edge, N node1, N node2)}: O(1).  *<li>{@code removeNode(node)}: O(d_node).  *<li>{@code removeEdge}: O(1), unless this graph is a multigraph (supports parallel edges);  *     in that case this method is O(min(d_edgeNode1, d_edgeNode2)).  *</ul>  * where d_node is the degree of node.  *  * @author James Sexton  * @author Joshua O'Madadhain  * @author Omar Darwish  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @see AbstractConfigurableGraph  * @see Graphs  */
end_comment

begin_class
DECL|class|IncidenceSetUndirectedGraph
specifier|final
class|class
name|IncidenceSetUndirectedGraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractConfigurableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
implements|implements
name|UndirectedGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
DECL|method|IncidenceSetUndirectedGraph (GraphConfig config)
name|IncidenceSetUndirectedGraph
parameter_list|(
name|GraphConfig
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newNodeConnections ()
name|NodeConnections
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|newNodeConnections
parameter_list|()
block|{
return|return
name|UndirectedNodeConnections
operator|.
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

