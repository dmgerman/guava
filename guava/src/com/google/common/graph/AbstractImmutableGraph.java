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
comment|/**  * Abstract base class for implementation of immutable graphs/hypergraphs.  *  *<p>All mutation methods are not supported as the graph can't be modified.  *  * @author Joshua O'Madadhain  * @param<N> Node parameter type  * @param<E> Edge parameter type  */
end_comment

begin_class
DECL|class|AbstractImmutableGraph
specifier|abstract
class|class
name|AbstractImmutableGraph
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
implements|implements
name|Graph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
block|{
annotation|@
name|Override
DECL|method|addNode (N n)
specifier|public
name|boolean
name|addNode
parameter_list|(
name|N
name|n
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
DECL|method|addEdge (E e, N n1, N n2)
specifier|public
name|boolean
name|addEdge
parameter_list|(
name|E
name|e
parameter_list|,
name|N
name|n1
parameter_list|,
name|N
name|n2
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
DECL|method|removeNode (Object n)
specifier|public
name|boolean
name|removeNode
parameter_list|(
name|Object
name|n
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
DECL|method|removeEdge (Object e)
specifier|public
name|boolean
name|removeEdge
parameter_list|(
name|Object
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * An interface for builders of immutable graph instances.    *    * @param<N> Node parameter type    * @param<E> Edge parameter type    * TODO(user): Consider throwing exception when the graph is not affected    * by method calls (methods returning false).    */
DECL|interface|Builder
interface|interface
name|Builder
parameter_list|<
name|N
parameter_list|,
name|E
parameter_list|>
block|{
comment|/**      * Adds {@code n} to the graph being built.      *      * @return this {@code Builder} instance      * @throws NullPointerException if {@code n} is null      */
DECL|method|addNode (N n)
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addNode
parameter_list|(
name|N
name|n
parameter_list|)
function_decl|;
comment|/**      * Adds {@code e} to the graph being built, connecting {@code n1} and {@code n2};      * adds {@code n1} and {@code n2} if not already present.      *      * @return this {@code Builder} instance      * @throws IllegalArgumentException when {@code Graph.addEdge(e, n1, n2)} throws      *     on the graph being built      * @throws NullPointerException if {@code e}, {@code n1}, or {@code n2} is null      * @see Graph#addEdge      */
DECL|method|addEdge (E e, N n1, N n2)
name|Builder
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|addEdge
parameter_list|(
name|E
name|e
parameter_list|,
name|N
name|n1
parameter_list|,
name|N
name|n2
parameter_list|)
function_decl|;
comment|/**      * Creates and returns a new instance of {@code AbstractImmutableGraph}      * based on the contents of the {@code Builder}.      */
DECL|method|build ()
name|AbstractImmutableGraph
argument_list|<
name|N
argument_list|,
name|E
argument_list|>
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

