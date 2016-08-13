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
name|annotations
operator|.
name|Beta
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A subtype of {@link Graph} that associates a value with each edge.  *  * TODO(b/30133524) Flesh out class-level javadoc.  *  * @author James Sexton  * @param<N> Node parameter type  * @param<V> Value parameter type  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|ValueGraph
specifier|public
interface|interface
name|ValueGraph
parameter_list|<
name|N
parameter_list|,
name|V
parameter_list|>
extends|extends
name|Graph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * If there is an edge connecting {@code nodeA} to {@code nodeB}, returns the non-null value    * associated with that edge.    *    * @throws IllegalArgumentException if there is no edge connecting {@code nodeA} to {@code nodeB}    */
DECL|method|edgeValue (Object nodeA, Object nodeB)
name|V
name|edgeValue
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|)
function_decl|;
comment|/**    * If there is an edge connecting {@code nodeA} to {@code nodeB}, returns the non-null value    * associated with that edge. Otherwise, returns {@code defaultValue}.    */
DECL|method|edgeValueOrDefault (Object nodeA, Object nodeB, @Nullable V defaultValue)
name|V
name|edgeValueOrDefault
parameter_list|(
name|Object
name|nodeA
parameter_list|,
name|Object
name|nodeB
parameter_list|,
annotation|@
name|Nullable
name|V
name|defaultValue
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

