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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|AndroidIncompatible
comment|// TODO(cpovirk): Figure out Android JUnit 4 support. Does it work with Gingerbread? @RunWith?
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|NetworkEquivalenceTest
specifier|public
specifier|final
class|class
name|NetworkEquivalenceTest
block|{
DECL|field|N1
specifier|private
specifier|static
specifier|final
name|Integer
name|N1
init|=
literal|1
decl_stmt|;
DECL|field|N2
specifier|private
specifier|static
specifier|final
name|Integer
name|N2
init|=
literal|2
decl_stmt|;
DECL|field|N3
specifier|private
specifier|static
specifier|final
name|Integer
name|N3
init|=
literal|3
decl_stmt|;
DECL|field|E11
specifier|private
specifier|static
specifier|final
name|String
name|E11
init|=
literal|"1-1"
decl_stmt|;
DECL|field|E12
specifier|private
specifier|static
specifier|final
name|String
name|E12
init|=
literal|"1-2"
decl_stmt|;
DECL|field|E12_A
specifier|private
specifier|static
specifier|final
name|String
name|E12_A
init|=
literal|"1-2a"
decl_stmt|;
DECL|field|E13
specifier|private
specifier|static
specifier|final
name|String
name|E13
init|=
literal|"1-3"
decl_stmt|;
DECL|enum|EdgeType
enum|enum
name|EdgeType
block|{
DECL|enumConstant|UNDIRECTED
name|UNDIRECTED
block|,
DECL|enumConstant|DIRECTED
name|DIRECTED
block|}
DECL|field|edgeType
specifier|private
specifier|final
name|EdgeType
name|edgeType
decl_stmt|;
DECL|field|network
specifier|private
specifier|final
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|network
decl_stmt|;
comment|// add parameters: directed/undirected
annotation|@
name|Parameters
DECL|method|parameters ()
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|parameters
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
index|[]
block|{
block|{
name|EdgeType
operator|.
name|UNDIRECTED
block|}
block|,
block|{
name|EdgeType
operator|.
name|DIRECTED
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|NetworkEquivalenceTest (EdgeType edgeType)
specifier|public
name|NetworkEquivalenceTest
parameter_list|(
name|EdgeType
name|edgeType
parameter_list|)
block|{
name|this
operator|.
name|edgeType
operator|=
name|edgeType
expr_stmt|;
name|this
operator|.
name|network
operator|=
name|createNetwork
argument_list|(
name|edgeType
argument_list|)
expr_stmt|;
block|}
DECL|method|createNetwork (EdgeType edgeType)
specifier|private
specifier|static
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|createNetwork
parameter_list|(
name|EdgeType
name|edgeType
parameter_list|)
block|{
switch|switch
condition|(
name|edgeType
condition|)
block|{
case|case
name|UNDIRECTED
case|:
return|return
name|NetworkBuilder
operator|.
name|undirected
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
return|;
case|case
name|DIRECTED
case|:
return|return
name|NetworkBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected edge type: "
operator|+
name|edgeType
argument_list|)
throw|;
block|}
block|}
DECL|method|oppositeType (EdgeType edgeType)
specifier|private
specifier|static
name|EdgeType
name|oppositeType
parameter_list|(
name|EdgeType
name|edgeType
parameter_list|)
block|{
switch|switch
condition|(
name|edgeType
condition|)
block|{
case|case
name|UNDIRECTED
case|:
return|return
name|EdgeType
operator|.
name|DIRECTED
return|;
case|case
name|DIRECTED
case|:
return|return
name|EdgeType
operator|.
name|UNDIRECTED
return|;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected edge type: "
operator|+
name|edgeType
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Test
DECL|method|equivalent_nodeSetsDiffer ()
specifier|public
name|void
name|equivalent_nodeSetsDiffer
parameter_list|()
block|{
name|network
operator|.
name|addNode
argument_list|(
name|N1
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|edgeType
argument_list|)
decl_stmt|;
name|g2
operator|.
name|addNode
argument_list|(
name|N2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node sets are the same, but edge sets differ.
annotation|@
name|Test
DECL|method|equivalent_edgeSetsDiffer ()
specifier|public
name|void
name|equivalent_edgeSetsDiffer
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|edgeType
argument_list|)
decl_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E13
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node/edge sets are the same, but node/edge connections differ due to edge type.
annotation|@
name|Test
DECL|method|equivalent_directedVsUndirected ()
specifier|public
name|void
name|equivalent_directedVsUndirected
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|oppositeType
argument_list|(
name|edgeType
argument_list|)
argument_list|)
decl_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node/edge sets and node/edge connections are the same, but directedness differs.
annotation|@
name|Test
DECL|method|equivalent_selfLoop_directedVsUndirected ()
specifier|public
name|void
name|equivalent_selfLoop_directedVsUndirected
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|oppositeType
argument_list|(
name|edgeType
argument_list|)
argument_list|)
decl_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N1
argument_list|,
name|E11
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node/edge sets are the same, but node/edge connections differ.
annotation|@
name|Test
DECL|method|equivalent_connectionsDiffer ()
specifier|public
name|void
name|equivalent_connectionsDiffer
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|,
name|E13
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|edgeType
argument_list|)
decl_stmt|;
comment|// connect E13 to N1 and N2, and E12 to N1 and N3 => not equivalent
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E13
argument_list|)
expr_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N3
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node/edge sets and node/edge connections are the same, but network properties differ.
comment|// (In this case the networks are considered equivalent; the property differences are irrelevant.)
annotation|@
name|Test
DECL|method|equivalent_propertiesDiffer ()
specifier|public
name|void
name|equivalent_propertiesDiffer
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|NetworkBuilder
operator|.
name|from
argument_list|(
name|network
argument_list|)
operator|.
name|allowsParallelEdges
argument_list|(
operator|!
name|network
operator|.
name|allowsParallelEdges
argument_list|()
argument_list|)
operator|.
name|allowsSelfLoops
argument_list|(
operator|!
name|network
operator|.
name|allowsSelfLoops
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
comment|// Node/edge sets and node/edge connections are the same, but edge order differs.
comment|// (In this case the networks are considered equivalent; the edge add orderings are irrelevant.)
annotation|@
name|Test
DECL|method|equivalent_edgeAddOrdersDiffer ()
specifier|public
name|void
name|equivalent_edgeAddOrdersDiffer
parameter_list|()
block|{
name|NetworkBuilder
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|builder
init|=
name|NetworkBuilder
operator|.
name|from
argument_list|(
name|network
argument_list|)
operator|.
name|allowsParallelEdges
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g1
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// for ug1, add e12 first, then e12_a
name|g1
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|g1
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12_A
argument_list|)
expr_stmt|;
comment|// for ug2, add e12_a first, then e12
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12_A
argument_list|)
expr_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|g1
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|equivalent_edgeDirectionsDiffer ()
specifier|public
name|void
name|equivalent_edgeDirectionsDiffer
parameter_list|()
block|{
name|network
operator|.
name|addEdge
argument_list|(
name|N1
argument_list|,
name|N2
argument_list|,
name|E12
argument_list|)
expr_stmt|;
name|MutableNetwork
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|g2
init|=
name|createNetwork
argument_list|(
name|edgeType
argument_list|)
decl_stmt|;
name|g2
operator|.
name|addEdge
argument_list|(
name|N2
argument_list|,
name|N1
argument_list|,
name|E12
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|edgeType
condition|)
block|{
case|case
name|UNDIRECTED
case|:
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
break|break;
case|case
name|DIRECTED
case|:
name|assertThat
argument_list|(
name|network
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|g2
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unexpected edge type: "
operator|+
name|edgeType
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
