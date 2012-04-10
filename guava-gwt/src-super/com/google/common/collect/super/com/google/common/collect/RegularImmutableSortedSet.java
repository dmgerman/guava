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
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * GWT emulation of {@link RegularImmutableSortedSet}.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|RegularImmutableSortedSet
specifier|final
class|class
name|RegularImmutableSortedSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableSortedSet
argument_list|<
name|E
argument_list|>
block|{
comment|/** true if this set is a subset of another immutable sorted set. */
DECL|field|isSubset
specifier|final
name|boolean
name|isSubset
decl_stmt|;
DECL|method|RegularImmutableSortedSet (SortedSet<E> delegate, boolean isSubset)
name|RegularImmutableSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|boolean
name|isSubset
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|isSubset
operator|=
name|isSubset
expr_stmt|;
block|}
DECL|method|createAsList ()
annotation|@
name|Override
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|new
name|ImmutableSortedAsList
argument_list|<
name|E
argument_list|>
argument_list|(
name|this
argument_list|,
name|ImmutableList
operator|.
expr|<
name|E
operator|>
name|asImmutableList
argument_list|(
name|toArray
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

