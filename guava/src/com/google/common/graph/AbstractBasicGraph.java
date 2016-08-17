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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
operator|.
name|GraphConstants
operator|.
name|GRAPH_STRING_FORMAT
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
name|graph
operator|.
name|BasicGraph
operator|.
name|Presence
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of {@link BasicGraph}. It is recommended to extend  * this class rather than implement {@link BasicGraph} directly, to ensure consistent {@link  * #equals(Object)} and {@link #hashCode()} results across different graph implementations.  *  * @author James Sexton  * @param<N> Node parameter type  * @since 20.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractBasicGraph
specifier|public
specifier|abstract
class|class
name|AbstractBasicGraph
parameter_list|<
name|N
parameter_list|>
extends|extends
name|AbstractGraph
argument_list|<
name|N
argument_list|,
name|Presence
argument_list|>
implements|implements
name|BasicGraph
argument_list|<
name|N
argument_list|>
block|{
comment|/**    * Returns a string representation of this graph.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|propertiesString
init|=
name|String
operator|.
name|format
argument_list|(
literal|"isDirected: %s, allowsSelfLoops: %s"
argument_list|,
name|isDirected
argument_list|()
argument_list|,
name|allowsSelfLoops
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
name|GRAPH_STRING_FORMAT
argument_list|,
name|propertiesString
argument_list|,
name|nodes
argument_list|()
argument_list|,
name|edges
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

