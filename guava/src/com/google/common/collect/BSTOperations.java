begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|BSTSide
operator|.
name|LEFT
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|BSTSide
operator|.
name|RIGHT
import|;
end_import

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
name|Comparator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Tools to perform single-key queries and mutations in binary search trees.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BSTOperations
specifier|final
class|class
name|BSTOperations
block|{
DECL|method|BSTOperations ()
specifier|private
name|BSTOperations
parameter_list|()
block|{}
comment|/**    * Returns the node with key {@code key} in {@code tree}, if any.    */
annotation|@
name|Nullable
DECL|method|seek ( Comparator<? super K> comparator, @Nullable N tree, K key)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|N
extends|extends
name|BSTNode
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
parameter_list|>
name|N
name|seek
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
annotation|@
name|Nullable
name|N
name|tree
parameter_list|,
name|K
name|key
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
if|if
condition|(
name|tree
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|key
argument_list|,
name|tree
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|==
literal|0
condition|)
block|{
return|return
name|tree
return|;
block|}
else|else
block|{
name|BSTSide
name|side
init|=
operator|(
name|cmp
operator|<
literal|0
operator|)
condition|?
name|LEFT
else|:
name|RIGHT
decl_stmt|;
return|return
name|seek
argument_list|(
name|comparator
argument_list|,
name|tree
operator|.
name|childOrNull
argument_list|(
name|side
argument_list|)
argument_list|,
name|key
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns the result of performing the mutation specified by {@code mutationRule} in {@code    * tree} at the location with key {@code key}.    */
DECL|method|mutate ( Comparator<? super K> comparator, BSTMutationRule<K, N> mutationRule, @Nullable N tree, K key)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|N
extends|extends
name|BSTNode
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
parameter_list|>
name|BSTMutationResult
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
name|mutate
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
parameter_list|,
name|BSTMutationRule
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
name|mutationRule
parameter_list|,
annotation|@
name|Nullable
name|N
name|tree
parameter_list|,
name|K
name|key
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|mutationRule
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|BSTBalancePolicy
argument_list|<
name|N
argument_list|>
name|rebalancePolicy
init|=
name|mutationRule
operator|.
name|getBalancePolicy
argument_list|()
decl_stmt|;
name|BSTNodeFactory
argument_list|<
name|N
argument_list|>
name|nodeFactory
init|=
name|mutationRule
operator|.
name|getNodeFactory
argument_list|()
decl_stmt|;
name|BSTModifier
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
name|modifier
init|=
name|mutationRule
operator|.
name|getModifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|tree
operator|!=
literal|null
condition|)
block|{
name|int
name|cmp
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|key
argument_list|,
name|tree
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmp
operator|!=
literal|0
condition|)
block|{
name|BSTSide
name|side
init|=
operator|(
name|cmp
operator|<
literal|0
operator|)
condition|?
name|LEFT
else|:
name|RIGHT
decl_stmt|;
name|BSTMutationResult
argument_list|<
name|K
argument_list|,
name|N
argument_list|>
name|mutation
init|=
name|mutate
argument_list|(
name|comparator
argument_list|,
name|mutationRule
argument_list|,
name|tree
operator|.
name|childOrNull
argument_list|(
name|side
argument_list|)
argument_list|,
name|key
argument_list|)
decl_stmt|;
return|return
name|mutation
operator|.
name|lift
argument_list|(
name|tree
argument_list|,
name|side
argument_list|,
name|nodeFactory
argument_list|,
name|rebalancePolicy
argument_list|)
return|;
block|}
block|}
comment|// We're modifying this node
name|N
name|newTree
init|=
name|modifier
operator|.
name|modify
argument_list|(
name|key
argument_list|,
name|tree
argument_list|)
decl_stmt|;
if|if
condition|(
name|newTree
operator|==
name|tree
condition|)
block|{
return|return
name|BSTMutationResult
operator|.
name|identity
argument_list|(
name|key
argument_list|,
name|tree
argument_list|,
name|tree
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|newTree
operator|==
literal|null
condition|)
block|{
name|newTree
operator|=
name|rebalancePolicy
operator|.
name|combine
argument_list|(
name|nodeFactory
argument_list|,
name|tree
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|,
name|tree
operator|.
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|N
name|left
init|=
literal|null
decl_stmt|;
name|N
name|right
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|tree
operator|!=
literal|null
condition|)
block|{
name|left
operator|=
name|tree
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
expr_stmt|;
name|right
operator|=
name|tree
operator|.
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
expr_stmt|;
block|}
name|newTree
operator|=
name|rebalancePolicy
operator|.
name|balance
argument_list|(
name|nodeFactory
argument_list|,
name|newTree
argument_list|,
name|left
argument_list|,
name|right
argument_list|)
expr_stmt|;
block|}
return|return
name|BSTMutationResult
operator|.
name|mutationResult
argument_list|(
name|key
argument_list|,
name|tree
argument_list|,
name|newTree
argument_list|,
name|tree
argument_list|,
name|newTree
argument_list|)
return|;
block|}
block|}
end_class

end_unit

