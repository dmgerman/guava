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
comment|/**  * A subinterface of {@code Graph} for graphs whose edges are all directed.  *  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|DirectedGraph
specifier|public
interface|interface
name|DirectedGraph
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
comment|/**    * Returns the node for which {@code edge} is an outgoing edge.    *    * @throws IllegalArgumentException if {@code edge} is not an element of this graph    */
DECL|method|source (Object edge)
name|N
name|source
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * Returns the node for which {@code edge} is an incoming edge.    *    * @throws IllegalArgumentException if {@code edge} is not an element of this graph    */
DECL|method|target (Object edge)
name|N
name|target
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>If {@code edge} is not a self-loop, the iteration order will be    * {@code [source(edge), target(edge)]}.    *    */
annotation|@
name|Override
DECL|method|incidentNodes (Object edge)
name|Set
argument_list|<
name|N
argument_list|>
name|incidentNodes
parameter_list|(
name|Object
name|edge
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>The {@linkplain #source(Object) source} and {@linkplain #target(Object) target}    * of the edges returned must be {@code source} and {@code target}, respectively.    *    */
annotation|@
name|Override
DECL|method|edgesConnecting (Object source, Object target)
name|Set
argument_list|<
name|E
argument_list|>
name|edgesConnecting
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>{@code edge} is an outgoing edge of {@code source} and an incoming edge of {@code target}.    */
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
DECL|method|addEdge (E edge, N source, N target)
name|boolean
name|addEdge
parameter_list|(
name|E
name|edge
parameter_list|,
name|N
name|source
parameter_list|,
name|N
name|target
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

