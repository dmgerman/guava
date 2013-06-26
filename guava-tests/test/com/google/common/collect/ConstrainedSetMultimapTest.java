begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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

begin_comment
comment|/**  * Tests for {@link MapConstraints#constrainedSetMultimap}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ConstrainedSetMultimapTest
specifier|public
class|class
name|ConstrainedSetMultimapTest
extends|extends
name|AbstractSetMultimapTest
block|{
DECL|method|create ()
annotation|@
name|Override
specifier|protected
name|SetMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|MapConstraints
operator|.
expr|<
name|String
operator|,
name|Integer
operator|>
name|constrainedSetMultimap
argument_list|(
name|HashMultimap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|create
argument_list|()
argument_list|,
name|MapConstraintsTest
operator|.
name|TEST_CONSTRAINT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

