begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2019 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AbstractSet
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Abstract base class for an incident edges set that allows different implementations of {@link  * AbstractSet#iterator()}.  */
end_comment

begin_class
DECL|class|IncidentEdgeSet
specifier|abstract
class|class
name|IncidentEdgeSet
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|EndpointPair
argument_list|<
name|N
argument_list|>
argument_list|>
block|{
DECL|field|node
specifier|protected
specifier|final
name|N
name|node
decl_stmt|;
DECL|field|graph
specifier|protected
specifier|final
name|BaseGraph
argument_list|<
name|N
argument_list|>
name|graph
decl_stmt|;
DECL|method|IncidentEdgeSet (BaseGraph<N> graph, N node)
name|IncidentEdgeSet
parameter_list|(
name|BaseGraph
argument_list|<
name|N
argument_list|>
name|graph
parameter_list|,
name|N
name|node
parameter_list|)
block|{
name|this
operator|.
name|graph
operator|=
name|graph
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
return|return
name|graph
operator|.
name|inDegree
argument_list|(
name|node
argument_list|)
operator|+
name|graph
operator|.
name|outDegree
argument_list|(
name|node
argument_list|)
operator|-
operator|(
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|node
argument_list|)
condition|?
literal|1
else|:
literal|0
operator|)
return|;
block|}
else|else
block|{
return|return
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
operator|.
name|size
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|contains (@ullableDecl Object obj)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|EndpointPair
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointPair
argument_list|<
name|?
argument_list|>
name|endpointPair
init|=
operator|(
name|EndpointPair
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|graph
operator|.
name|isDirected
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|endpointPair
operator|.
name|isOrdered
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Object
name|source
init|=
name|endpointPair
operator|.
name|source
argument_list|()
decl_stmt|;
name|Object
name|target
init|=
name|endpointPair
operator|.
name|target
argument_list|()
decl_stmt|;
return|return
operator|(
name|node
operator|.
name|equals
argument_list|(
name|source
argument_list|)
operator|&&
name|graph
operator|.
name|successors
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|target
argument_list|)
operator|)
operator|||
operator|(
name|node
operator|.
name|equals
argument_list|(
name|target
argument_list|)
operator|&&
name|graph
operator|.
name|predecessors
argument_list|(
name|node
argument_list|)
operator|.
name|contains
argument_list|(
name|source
argument_list|)
operator|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|endpointPair
operator|.
name|isOrdered
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Set
argument_list|<
name|N
argument_list|>
name|adjacent
init|=
name|graph
operator|.
name|adjacentNodes
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|Object
name|nodeU
init|=
name|endpointPair
operator|.
name|nodeU
argument_list|()
decl_stmt|;
name|Object
name|nodeV
init|=
name|endpointPair
operator|.
name|nodeV
argument_list|()
decl_stmt|;
return|return
operator|(
name|node
operator|.
name|equals
argument_list|(
name|nodeV
argument_list|)
operator|&&
name|adjacent
operator|.
name|contains
argument_list|(
name|nodeU
argument_list|)
operator|)
operator|||
operator|(
name|node
operator|.
name|equals
argument_list|(
name|nodeU
argument_list|)
operator|&&
name|adjacent
operator|.
name|contains
argument_list|(
name|nodeV
argument_list|)
operator|)
return|;
block|}
block|}
block|}
end_class

end_unit
