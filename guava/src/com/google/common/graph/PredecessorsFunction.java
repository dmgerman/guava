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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A functional interface for<a  * href="https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)">graph</a>-structured data.  *  *<p>A graph is composed of a set of nodes and a set of edges connecting pairs of nodes.  *  *<p>There are three main interfaces provided to represent graphs. In order of increasing  * complexity they are: {@link Graph}, {@link ValueGraph}, and {@link Network}. You should generally  * prefer the simplest interface that satisfies your use case. See the<a  * href="https://github.com/google/guava/wiki/GraphsExplained#choosing-the-right-graph-type">  * "Choosing the right graph type"</a> section of the Guava User Guide for more details.  *  *<h3>Usage</h3>  *  * Some graph algorithms only care about the nodes that are adjacent to a node but not about other  * properties such as the size or type of edges (e.g. topological sort). These algorithms should  * prefer working with {@code PredecessorsFunction} rather than a more specialized type.  *  *<p>When calling a method that requires a {@code PredecessorsFunction}, you can use {@link Graph},  * {@link ValueGraph}, {@link Network} or implement this interface for an already existing data  * structure.  *  *<h3>Additional documentation</h3>  *  *<p>See the Guava User Guide for the {@code common.graph} package (<a  * href="https://github.com/google/guava/wiki/GraphsExplained">"Graphs Explained"</a>) for  * additional documentation, including<a  * href="https://github.com/google/guava/wiki/GraphsExplained#notes-for-implementors">notes for  * implementors</a>  *  * @author Joshua O'Madadhain  * @author Jens Nyman  * @param<N> Node parameter type  * @since 22.0  */
end_comment

begin_comment
comment|// TODO(b/35456940): Update the documentation to reflect the new interfaces
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|PredecessorsFunction
specifier|public
interface|interface
name|PredecessorsFunction
parameter_list|<
name|N
parameter_list|>
block|{
comment|/**    * Returns all nodes in this graph adjacent to {@code node} which can be reached by traversing    * {@code node}'s incoming edges<i>against</i> the direction (if any) of the edge.    *    * @throws IllegalArgumentException if {@code node} is not an element of this graph    */
DECL|method|predecessors (N node)
name|Set
argument_list|<
name|N
argument_list|>
name|predecessors
parameter_list|(
name|N
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

