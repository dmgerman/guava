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
name|common
operator|.
name|math
operator|.
name|DoubleMath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_comment
comment|/**  * A utility class to hold various constants used by the Guava Graph library.  */
end_comment

begin_class
DECL|class|GraphConstants
specifier|final
class|class
name|GraphConstants
block|{
DECL|method|GraphConstants ()
specifier|private
name|GraphConstants
parameter_list|()
block|{}
DECL|field|EXPECTED_DEGREE
specifier|static
specifier|final
name|int
name|EXPECTED_DEGREE
init|=
literal|2
decl_stmt|;
DECL|field|DEFAULT_NODE_COUNT
specifier|static
specifier|final
name|int
name|DEFAULT_NODE_COUNT
init|=
literal|10
decl_stmt|;
DECL|field|DEFAULT_EDGE_COUNT
specifier|static
specifier|final
name|int
name|DEFAULT_EDGE_COUNT
init|=
name|DEFAULT_NODE_COUNT
operator|*
name|EXPECTED_DEGREE
decl_stmt|;
comment|// Load factor and capacity for "inner" (i.e. per node/edge element) hash sets or maps
DECL|field|INNER_LOAD_FACTOR
specifier|static
specifier|final
name|float
name|INNER_LOAD_FACTOR
init|=
literal|1.0f
decl_stmt|;
DECL|field|INNER_CAPACITY
specifier|static
specifier|final
name|int
name|INNER_CAPACITY
init|=
name|DoubleMath
operator|.
name|roundToInt
argument_list|(
operator|(
name|double
operator|)
name|EXPECTED_DEGREE
operator|/
name|INNER_LOAD_FACTOR
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
decl_stmt|;
comment|// Error messages
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
DECL|field|EDGE_CONNECTING_NOT_IN_GRAPH
specifier|static
specifier|final
name|String
name|EDGE_CONNECTING_NOT_IN_GRAPH
init|=
literal|"Edge connecting %s to %s is not present in this graph."
decl_stmt|;
DECL|field|REUSING_EDGE
specifier|static
specifier|final
name|String
name|REUSING_EDGE
init|=
literal|"Edge %s already exists between the following nodes: %s, "
operator|+
literal|"so it cannot be reused to connect the following nodes: %s."
decl_stmt|;
DECL|field|PARALLEL_EDGES_NOT_ALLOWED
specifier|static
specifier|final
name|String
name|PARALLEL_EDGES_NOT_ALLOWED
init|=
literal|"Nodes %s and %s are already connected by a different edge. To construct a graph "
operator|+
literal|"that allows parallel edges, call allowsParallelEdges(true) on the Builder."
decl_stmt|;
DECL|field|SELF_LOOPS_NOT_ALLOWED
specifier|static
specifier|final
name|String
name|SELF_LOOPS_NOT_ALLOWED
init|=
literal|"Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph "
operator|+
literal|"that allows self-loops, call allowsSelfLoops(true) on the Builder."
decl_stmt|;
DECL|field|NOT_AVAILABLE_ON_UNDIRECTED
specifier|static
specifier|final
name|String
name|NOT_AVAILABLE_ON_UNDIRECTED
init|=
literal|"Cannot call source()/target() on the endpoints of an undirected edge. Consider calling "
operator|+
literal|"otherNode() to get a single node or using the endpoints' iterator to get both nodes."
decl_stmt|;
DECL|field|EDGE_ALREADY_EXISTS
specifier|static
specifier|final
name|String
name|EDGE_ALREADY_EXISTS
init|=
literal|"Edge %s already exists in the graph."
decl_stmt|;
DECL|field|GRAPH_STRING_FORMAT
specifier|static
specifier|final
name|String
name|GRAPH_STRING_FORMAT
init|=
literal|"%s, nodes: %s, edges: %s"
decl_stmt|;
block|}
end_class

end_unit

