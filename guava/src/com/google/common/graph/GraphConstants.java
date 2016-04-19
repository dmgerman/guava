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

begin_comment
comment|/**  * A utility class to hold various constants used by the Guava Graph library.  */
end_comment

begin_comment
comment|// TODO(user): Decide what else to put here (error message strings, node/edge map sizes, etc.)
end_comment

begin_class
DECL|class|GraphConstants
specifier|final
class|class
name|GraphConstants
block|{
DECL|method|GraphConstants ()
specifier|private
name|GraphConstants
parameter_list|()
block|{}
comment|// TODO(user): Enable users to specify the expected (in/out?) degree of nodes.
DECL|field|EXPECTED_DEGREE
specifier|static
specifier|final
name|int
name|EXPECTED_DEGREE
init|=
literal|11
decl_stmt|;
block|}
end_class

end_unit

