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
comment|/**  * Unit test covering the inverse view of a {@code BiMap}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|InverseBiMapTest
specifier|public
class|class
name|InverseBiMapTest
extends|extends
name|AbstractBiMapTest
block|{
DECL|method|create ()
annotation|@
name|Override
specifier|protected
name|BiMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|create
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|inverse
init|=
name|HashBiMap
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|inverse
operator|.
name|inverse
argument_list|()
return|;
block|}
block|}
end_class

end_unit

