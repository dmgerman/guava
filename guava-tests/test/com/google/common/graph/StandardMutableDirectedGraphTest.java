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

begin_comment
comment|/** Tests for a directed {@link StandardMutableGraph}. */
end_comment

begin_class
annotation|@
name|AndroidIncompatible
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|StandardMutableDirectedGraphTest
specifier|public
specifier|final
class|class
name|StandardMutableDirectedGraphTest
extends|extends
name|AbstractStandardDirectedGraphTest
block|{
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"allowsSelfLoops={0}, incidentEdgeOrder={1}"
argument_list|)
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
literal|false
block|,
name|ElementOrder
operator|.
name|unordered
argument_list|()
block|}
block|,
block|{
literal|true
block|,
name|ElementOrder
operator|.
name|unordered
argument_list|()
block|}
block|,
block|{
literal|false
block|,
name|ElementOrder
operator|.
name|stable
argument_list|()
block|}
block|,
block|{
literal|true
block|,
name|ElementOrder
operator|.
name|stable
argument_list|()
block|}
block|,         }
argument_list|)
return|;
block|}
DECL|field|allowsSelfLoops
specifier|private
specifier|final
name|boolean
name|allowsSelfLoops
decl_stmt|;
DECL|field|incidentEdgeOrder
specifier|private
specifier|final
name|ElementOrder
argument_list|<
name|Integer
argument_list|>
name|incidentEdgeOrder
decl_stmt|;
DECL|method|StandardMutableDirectedGraphTest ( boolean allowsSelfLoops, ElementOrder<Integer> incidentEdgeOrder)
specifier|public
name|StandardMutableDirectedGraphTest
parameter_list|(
name|boolean
name|allowsSelfLoops
parameter_list|,
name|ElementOrder
argument_list|<
name|Integer
argument_list|>
name|incidentEdgeOrder
parameter_list|)
block|{
name|this
operator|.
name|allowsSelfLoops
operator|=
name|allowsSelfLoops
expr_stmt|;
name|this
operator|.
name|incidentEdgeOrder
operator|=
name|incidentEdgeOrder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createGraph ()
specifier|public
name|MutableGraph
argument_list|<
name|Integer
argument_list|>
name|createGraph
parameter_list|()
block|{
return|return
name|GraphBuilder
operator|.
name|directed
argument_list|()
operator|.
name|allowsSelfLoops
argument_list|(
name|allowsSelfLoops
argument_list|)
operator|.
name|incidentEdgeOrder
argument_list|(
name|incidentEdgeOrder
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addNode (Integer n)
specifier|final
name|void
name|addNode
parameter_list|(
name|Integer
name|n
parameter_list|)
block|{
name|graphAsMutableGraph
operator|.
name|addNode
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|putEdge (Integer n1, Integer n2)
specifier|final
name|void
name|putEdge
parameter_list|(
name|Integer
name|n1
parameter_list|,
name|Integer
name|n2
parameter_list|)
block|{
name|graphAsMutableGraph
operator|.
name|putEdge
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

