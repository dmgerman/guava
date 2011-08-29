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

begin_comment
comment|/**  * Unit test for {@link ConcurrentHashMultiset} behavior when backed by the  * standard {@link java.util.concurrent.ConcurrentHashMap}.  *  * @author Kevin Bourrillion  * @author Jared Levy  */
end_comment

begin_class
DECL|class|ConcurrentHashMultisetWithChmTest
specifier|public
class|class
name|ConcurrentHashMultisetWithChmTest
extends|extends
name|AbstractConcurrentHashMultisetTest
block|{
DECL|method|create ()
annotation|@
name|Override
specifier|protected
parameter_list|<
name|E
parameter_list|>
name|Multiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
return|;
block|}
block|}
end_class

end_unit

