begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

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
comment|/**  * Create string sets for testing collections that are sorted by natural ordering.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|TestStringSortedSetGenerator
specifier|public
specifier|abstract
class|class
name|TestStringSortedSetGenerator
extends|extends
name|TestStringSetGenerator
implements|implements
name|TestSortedSetGenerator
argument_list|<
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|SortedSet
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
return|return
operator|(
name|SortedSet
argument_list|<
name|String
argument_list|>
operator|)
name|super
operator|.
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (String[] elements)
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
annotation|@
name|Override
DECL|method|order (List<String> insertionOrder)
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
annotation|@
name|Override
DECL|method|belowSamplesLesser ()
specifier|public
name|String
name|belowSamplesLesser
parameter_list|()
block|{
return|return
literal|"!! a"
return|;
block|}
annotation|@
name|Override
DECL|method|belowSamplesGreater ()
specifier|public
name|String
name|belowSamplesGreater
parameter_list|()
block|{
return|return
literal|"!! b"
return|;
block|}
annotation|@
name|Override
DECL|method|aboveSamplesLesser ()
specifier|public
name|String
name|aboveSamplesLesser
parameter_list|()
block|{
return|return
literal|"~~ a"
return|;
block|}
annotation|@
name|Override
DECL|method|aboveSamplesGreater ()
specifier|public
name|String
name|aboveSamplesGreater
parameter_list|()
block|{
return|return
literal|"~~ b"
return|;
block|}
block|}
end_class

end_unit

