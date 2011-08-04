begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|testing
operator|.
name|SampleElements
operator|.
name|Colliders
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

begin_comment
comment|/**  * A generator using sample elements whose hash codes all collide badly.  *  *<p>This class is GWT compatible.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|TestCollidingSetGenerator
specifier|public
specifier|abstract
class|class
name|TestCollidingSetGenerator
implements|implements
name|TestSetGenerator
argument_list|<
name|Object
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Object
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|Colliders
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|Object
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[
name|length
index|]
return|;
block|}
comment|/** Returns the original element list, unchanged. */
annotation|@
name|Override
DECL|method|order (List<Object> insertionOrder)
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
return|;
block|}
block|}
end_class

end_unit

