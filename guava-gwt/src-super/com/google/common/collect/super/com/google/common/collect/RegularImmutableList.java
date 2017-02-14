begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|unmodifiableList
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
comment|/**  * GWT emulated version of {@link RegularImmutableList}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|RegularImmutableList
class|class
name|RegularImmutableList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|forSerialization
name|E
name|forSerialization
decl_stmt|;
DECL|method|RegularImmutableList (List<E> delegate)
name|RegularImmutableList
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|)
block|{
comment|// TODO(cpovirk): avoid redundant unmodifiableList wrapping
name|this
operator|.
name|delegate
operator|=
name|unmodifiableList
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
DECL|method|delegateList ()
annotation|@
name|Override
name|List
argument_list|<
name|E
argument_list|>
name|delegateList
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
block|}
end_class

end_unit

