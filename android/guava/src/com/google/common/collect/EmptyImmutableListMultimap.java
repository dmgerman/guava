begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Implementation of {@link ImmutableListMultimap} with no entries.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|EmptyImmutableListMultimap
class|class
name|EmptyImmutableListMultimap
extends|extends
name|ImmutableListMultimap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|EmptyImmutableListMultimap
name|INSTANCE
init|=
operator|new
name|EmptyImmutableListMultimap
argument_list|()
decl_stmt|;
DECL|method|EmptyImmutableListMultimap ()
specifier|private
name|EmptyImmutableListMultimap
parameter_list|()
block|{
name|super
argument_list|(
name|ImmutableMap
operator|.
expr|<
name|Object
argument_list|,
name|ImmutableList
argument_list|<
name|Object
argument_list|>
operator|>
name|of
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
comment|// preserve singleton property
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

