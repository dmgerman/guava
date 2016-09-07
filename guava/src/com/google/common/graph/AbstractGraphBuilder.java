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
name|base
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * A base class for builders that construct graphs with user-defined properties.  *  * @author James Sexton  */
end_comment

begin_class
DECL|class|AbstractGraphBuilder
specifier|abstract
class|class
name|AbstractGraphBuilder
parameter_list|<
name|N
parameter_list|>
block|{
DECL|field|directed
specifier|final
name|boolean
name|directed
decl_stmt|;
DECL|field|allowsSelfLoops
name|boolean
name|allowsSelfLoops
init|=
literal|false
decl_stmt|;
DECL|field|nodeOrder
name|ElementOrder
argument_list|<
name|N
argument_list|>
name|nodeOrder
init|=
name|ElementOrder
operator|.
name|insertion
argument_list|()
decl_stmt|;
DECL|field|expectedNodeCount
name|Optional
argument_list|<
name|Integer
argument_list|>
name|expectedNodeCount
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
comment|/**    * Creates a new instance with the specified edge directionality.    *    * @param directed if true, creates an instance for graphs whose edges are each directed; if    *     false, creates an instance for graphs whose edges are each undirected.    */
DECL|method|AbstractGraphBuilder (boolean directed)
name|AbstractGraphBuilder
parameter_list|(
name|boolean
name|directed
parameter_list|)
block|{
name|this
operator|.
name|directed
operator|=
name|directed
expr_stmt|;
block|}
block|}
end_class

end_unit

