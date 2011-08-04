begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Create string sets for testing collections that are sorted by natural  * ordering.  *  *<p>This class is GWT compatible.  *  * @author Jared Levy  */
end_comment

begin_class
DECL|class|TestStringSortedSetGenerator
specifier|public
specifier|abstract
class|class
name|TestStringSortedSetGenerator
extends|extends
name|TestStringSetGenerator
block|{
DECL|method|create (String[] elements)
annotation|@
name|Override
specifier|protected
specifier|abstract
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
function_decl|;
comment|/** Sorts the elements by their natural ordering. */
DECL|method|order (List<String> insertionOrder)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|insertionOrder
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
block|}
end_class

end_unit

