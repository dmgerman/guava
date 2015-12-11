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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

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
name|common
operator|.
name|collect
operator|.
name|ImmutableSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_comment
comment|/**  * Static utility methods for calculating properties of {@link Graph} instances.  *  * @author Joshua O'Madadhain  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|CheckReturnValue
DECL|class|GraphProperties
specifier|public
specifier|final
class|class
name|GraphProperties
block|{
DECL|method|GraphProperties ()
specifier|private
name|GraphProperties
parameter_list|()
block|{}
comment|/**    * Returns true iff {@code graph} has at least one cycle.    */
comment|// TODO(user): Implement a similar method for undirected graphs, taking into
comment|// consideration the difference in implementation, due to the notion of undirected
comment|// edges. For instance, we should keep track of the edge used to reach a node to avoid
comment|// reusing it (making a cycle by getting back to that node). Also, parallel edges will
comment|// need to be carefully handled for undirected graphs.
DECL|method|isCyclic (DirectedGraph<?, ?> graph)
specifier|public
specifier|static
name|boolean
name|isCyclic
parameter_list|(
name|DirectedGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|graph
argument_list|,
literal|"Directed graph passed can't be null."
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|NodeVisitState
argument_list|>
name|nodeToVisitState
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|node
range|:
name|graph
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
name|nodeToVisitState
operator|.
name|get
argument_list|(
name|node
argument_list|)
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isSubgraphCyclic
argument_list|(
name|graph
argument_list|,
name|nodeToVisitState
argument_list|,
name|node
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns true iff there is a cycle in the subgraph of {@code graph} reachable from    * {@code node}.    */
DECL|method|isSubgraphCyclic ( DirectedGraph<?, ?> graph, Map<Object, NodeVisitState> nodeToVisitState, Object node)
specifier|private
specifier|static
name|boolean
name|isSubgraphCyclic
parameter_list|(
name|DirectedGraph
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|graph
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|NodeVisitState
argument_list|>
name|nodeToVisitState
parameter_list|,
name|Object
name|node
parameter_list|)
block|{
name|nodeToVisitState
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|NodeVisitState
operator|.
name|PENDING
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|successor
range|:
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
control|)
block|{
name|NodeVisitState
name|nodeVisitState
init|=
name|nodeToVisitState
operator|.
name|get
argument_list|(
name|successor
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeVisitState
operator|==
name|NodeVisitState
operator|.
name|PENDING
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|nodeVisitState
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isSubgraphCyclic
argument_list|(
name|graph
argument_list|,
name|nodeToVisitState
argument_list|,
name|successor
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// otherwise the state is COMPLETE, nothing to do
block|}
name|nodeToVisitState
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|NodeVisitState
operator|.
name|COMPLETE
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|/**    * An enum representing the state of a node during DFS. {@code PENDING} means that    * the node is on the stack of the DFS, while {@code COMPLETE} means that    * the node and all its successors have been already explored. Any node that    * has not been explored will not have a state at all.    */
DECL|enum|NodeVisitState
specifier|private
enum|enum
name|NodeVisitState
block|{
DECL|enumConstant|PENDING
name|PENDING
block|,
DECL|enumConstant|COMPLETE
name|COMPLETE
block|}
comment|/**    * Returns the set of all nodes in {@code directedGraph} that have no predecessors.    */
DECL|method|roots (DirectedGraph<N, ?> directedGraph)
specifier|public
specifier|static
parameter_list|<
name|N
parameter_list|>
name|ImmutableSet
argument_list|<
name|N
argument_list|>
name|roots
parameter_list|(
name|DirectedGraph
argument_list|<
name|N
argument_list|,
name|?
argument_list|>
name|directedGraph
parameter_list|)
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|N
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|N
name|node
range|:
name|directedGraph
operator|.
name|nodes
argument_list|()
control|)
block|{
if|if
condition|(
name|directedGraph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

