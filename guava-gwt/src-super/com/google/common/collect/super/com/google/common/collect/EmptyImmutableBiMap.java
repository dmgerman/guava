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
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link EmptyImmutableBiMap}.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
DECL|class|EmptyImmutableBiMap
specifier|final
class|class
name|EmptyImmutableBiMap
extends|extends
name|ImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|EmptyImmutableBiMap
name|INSTANCE
init|=
operator|new
name|EmptyImmutableBiMap
argument_list|()
decl_stmt|;
DECL|method|EmptyImmutableBiMap ()
specifier|private
name|EmptyImmutableBiMap
parameter_list|()
block|{
name|super
argument_list|(
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|inverse ()
annotation|@
name|Override
specifier|public
name|ImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|inverse
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

