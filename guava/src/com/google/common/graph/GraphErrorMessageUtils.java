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
comment|/**  * A utility class for graph error messages.  */
end_comment

begin_class
DECL|class|GraphErrorMessageUtils
specifier|final
class|class
name|GraphErrorMessageUtils
block|{
DECL|method|GraphErrorMessageUtils ()
specifier|private
name|GraphErrorMessageUtils
parameter_list|()
block|{}
DECL|field|NODE_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|NODE_NOT_IN_GRAPH
init|=
literal|"Node %s is not an element of this graph."
decl_stmt|;
DECL|field|EDGE_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|EDGE_NOT_IN_GRAPH
init|=
literal|"Edge %s is not an element of this graph."
decl_stmt|;
DECL|field|REUSING_EDGE
specifier|static
specifier|final
name|String
name|REUSING_EDGE
init|=
literal|"Edge %s already exists between the following nodes: %s, "
operator|+
literal|"so it can't be reused to connect the following nodes: %s."
decl_stmt|;
DECL|field|ADDING_PARALLEL_EDGE
specifier|static
specifier|final
name|String
name|ADDING_PARALLEL_EDGE
init|=
literal|"Nodes %s and %s are already connected by a different edge."
decl_stmt|;
DECL|field|EDGE_ALREADY_EXISTS
specifier|static
specifier|final
name|String
name|EDGE_ALREADY_EXISTS
init|=
literal|"Edge %s already exists in the graph."
decl_stmt|;
DECL|field|SELF_LOOPS_NOT_ALLOWED
specifier|static
specifier|final
name|String
name|SELF_LOOPS_NOT_ALLOWED
init|=
literal|"Cannot add self-loop edge on node %s, as self-loops are not allowed."
decl_stmt|;
DECL|field|NOT_AVAILABLE_ON_UNDIRECTED
specifier|static
specifier|final
name|String
name|NOT_AVAILABLE_ON_UNDIRECTED
init|=
literal|"Cannot call source()/target() on undirected endpoints."
decl_stmt|;
DECL|field|ENDPOINTS_GRAPH_DIRECTEDNESS
specifier|static
specifier|final
name|String
name|ENDPOINTS_GRAPH_DIRECTEDNESS
init|=
literal|"The endpoints' directedness (isDirected = %s) does not match the graph's directedness "
operator|+
literal|"(isDirected = %s). A graph cannot have both directed and undirected edges."
decl_stmt|;
DECL|field|NETWORK_WITH_PARALLEL_EDGE
specifier|static
specifier|final
name|String
name|NETWORK_WITH_PARALLEL_EDGE
init|=
literal|"Cannot make a Graph copy of a Network that allows parallel edges."
decl_stmt|;
block|}
end_class

end_unit

