begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|BstSide
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
name|BstSide
operator|.
name|RIGHT
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|base
operator|.
name|Objects
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
name|testing
operator|.
name|NullPointerTester
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
comment|/**  * Testing classes and utilities to be used in tests of the binary search tree framework.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BstTesting
specifier|public
class|class
name|BstTesting
block|{
DECL|class|SimpleNode
specifier|static
specifier|final
class|class
name|SimpleNode
extends|extends
name|BstNode
argument_list|<
name|Character
argument_list|,
name|SimpleNode
argument_list|>
block|{
DECL|method|SimpleNode (Character key, @Nullable SimpleNode left, @Nullable SimpleNode right)
name|SimpleNode
parameter_list|(
name|Character
name|key
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|left
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|right
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|left
argument_list|,
name|right
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SimpleNode
condition|)
block|{
name|SimpleNode
name|node
init|=
operator|(
name|SimpleNode
operator|)
name|obj
decl_stmt|;
return|return
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|,
name|node
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|,
name|node
operator|.
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|getKey
argument_list|()
argument_list|,
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|,
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|field|nodeFactory
specifier|static
specifier|final
name|BstNodeFactory
argument_list|<
name|SimpleNode
argument_list|>
name|nodeFactory
init|=
operator|new
name|BstNodeFactory
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SimpleNode
name|createNode
parameter_list|(
name|SimpleNode
name|source
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|left
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|right
parameter_list|)
block|{
return|return
operator|new
name|SimpleNode
argument_list|(
name|source
operator|.
name|getKey
argument_list|()
argument_list|,
name|left
argument_list|,
name|right
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|balancePolicy
specifier|static
specifier|final
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
name|balancePolicy
init|=
operator|new
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SimpleNode
name|balance
parameter_list|(
name|BstNodeFactory
argument_list|<
name|SimpleNode
argument_list|>
name|nodeFactory
parameter_list|,
name|SimpleNode
name|source
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|left
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|right
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|nodeFactory
argument_list|)
operator|.
name|createNode
argument_list|(
name|source
argument_list|,
name|left
argument_list|,
name|right
argument_list|)
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|SimpleNode
name|combine
parameter_list|(
name|BstNodeFactory
argument_list|<
name|SimpleNode
argument_list|>
name|nodeFactory
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|left
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|right
parameter_list|)
block|{
comment|// Shove right into the rightmost position in the left tree.
if|if
condition|(
name|left
operator|==
literal|null
condition|)
block|{
return|return
name|right
return|;
block|}
elseif|else
if|if
condition|(
name|right
operator|==
literal|null
condition|)
block|{
return|return
name|left
return|;
block|}
elseif|else
if|if
condition|(
name|left
operator|.
name|hasChild
argument_list|(
name|RIGHT
argument_list|)
condition|)
block|{
return|return
name|nodeFactory
operator|.
name|createNode
argument_list|(
name|left
argument_list|,
name|left
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|,
name|combine
argument_list|(
name|nodeFactory
argument_list|,
name|left
operator|.
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|,
name|right
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|nodeFactory
operator|.
name|createNode
argument_list|(
name|left
argument_list|,
name|left
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|,
name|right
argument_list|)
return|;
block|}
block|}
block|}
decl_stmt|;
DECL|field|pathFactory
specifier|static
specifier|final
name|BstPathFactory
argument_list|<
name|SimpleNode
argument_list|,
name|BstInOrderPath
argument_list|<
name|SimpleNode
argument_list|>
argument_list|>
name|pathFactory
init|=
name|BstInOrderPath
operator|.
name|inOrderFactory
argument_list|()
decl_stmt|;
comment|// A direct, if dumb, way to count total nodes in a tree.
DECL|field|countAggregate
specifier|static
specifier|final
name|BstAggregate
argument_list|<
name|SimpleNode
argument_list|>
name|countAggregate
init|=
operator|new
name|BstAggregate
argument_list|<
name|SimpleNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|entryValue
parameter_list|(
name|SimpleNode
name|entry
parameter_list|)
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|treeValue
parameter_list|(
annotation|@
name|Nullable
name|SimpleNode
name|tree
parameter_list|)
block|{
if|if
condition|(
name|tree
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
return|return
literal|1
operator|+
name|treeValue
argument_list|(
name|tree
operator|.
name|childOrNull
argument_list|(
name|LEFT
argument_list|)
argument_list|)
operator|+
name|treeValue
argument_list|(
name|tree
operator|.
name|childOrNull
argument_list|(
name|RIGHT
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|pathToList (P path)
specifier|static
parameter_list|<
name|P
extends|extends
name|BstPath
argument_list|<
name|SimpleNode
argument_list|,
name|P
argument_list|>
parameter_list|>
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|pathToList
parameter_list|(
name|P
name|path
parameter_list|)
block|{
name|List
argument_list|<
name|SimpleNode
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
init|;
name|path
operator|!=
literal|null
condition|;
name|path
operator|=
name|path
operator|.
name|prefixOrNull
argument_list|()
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|path
operator|.
name|getTip
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|extension ( BstPathFactory<N, P> factory, N root, BstSide... sides)
specifier|static
parameter_list|<
name|N
extends|extends
name|BstNode
argument_list|<
name|?
argument_list|,
name|N
argument_list|>
parameter_list|,
name|P
extends|extends
name|BstPath
argument_list|<
name|N
argument_list|,
name|P
argument_list|>
parameter_list|>
name|P
name|extension
parameter_list|(
name|BstPathFactory
argument_list|<
name|N
argument_list|,
name|P
argument_list|>
name|factory
parameter_list|,
name|N
name|root
parameter_list|,
name|BstSide
modifier|...
name|sides
parameter_list|)
block|{
name|P
name|path
init|=
name|factory
operator|.
name|initialPath
argument_list|(
name|root
argument_list|)
decl_stmt|;
for|for
control|(
name|BstSide
name|side
range|:
name|sides
control|)
block|{
name|path
operator|=
name|factory
operator|.
name|extension
argument_list|(
name|path
argument_list|,
name|side
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
DECL|method|assertInOrderTraversalIs (@ullable SimpleNode root, String order)
specifier|static
name|void
name|assertInOrderTraversalIs
parameter_list|(
annotation|@
name|Nullable
name|SimpleNode
name|root
parameter_list|,
name|String
name|order
parameter_list|)
block|{
if|if
condition|(
name|root
operator|==
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|order
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|BstInOrderPath
argument_list|<
name|SimpleNode
argument_list|>
name|path
init|=
name|pathFactory
operator|.
name|initialPath
argument_list|(
name|root
argument_list|)
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|getTip
argument_list|()
operator|.
name|hasChild
argument_list|(
name|LEFT
argument_list|)
condition|)
block|{
name|path
operator|=
name|pathFactory
operator|.
name|extension
argument_list|(
name|path
argument_list|,
name|LEFT
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|order
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|,
name|path
operator|.
name|getTip
argument_list|()
operator|.
name|getKey
argument_list|()
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|1
init|;
name|path
operator|.
name|hasNext
argument_list|(
name|RIGHT
argument_list|)
condition|;
name|i
operator|++
control|)
block|{
name|path
operator|=
name|path
operator|.
name|next
argument_list|(
name|RIGHT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|order
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
argument_list|,
name|path
operator|.
name|getTip
argument_list|()
operator|.
name|getKey
argument_list|()
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|i
argument_list|,
name|order
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|defaultNullPointerTester ()
specifier|static
name|NullPointerTester
name|defaultNullPointerTester
parameter_list|()
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|SimpleNode
name|node
init|=
operator|new
name|SimpleNode
argument_list|(
literal|'a'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstNode
operator|.
name|class
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstSide
operator|.
name|class
argument_list|,
name|LEFT
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstNodeFactory
operator|.
name|class
argument_list|,
name|nodeFactory
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstBalancePolicy
operator|.
name|class
argument_list|,
name|balancePolicy
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstPathFactory
operator|.
name|class
argument_list|,
name|pathFactory
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstPath
operator|.
name|class
argument_list|,
name|pathFactory
operator|.
name|initialPath
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstInOrderPath
operator|.
name|class
argument_list|,
name|pathFactory
operator|.
name|initialPath
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|Object
operator|.
name|class
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|GeneralRange
operator|.
name|class
argument_list|,
name|GeneralRange
operator|.
name|all
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstAggregate
operator|.
name|class
argument_list|,
name|countAggregate
argument_list|)
expr_stmt|;
name|BstModifier
argument_list|<
name|Character
argument_list|,
name|SimpleNode
argument_list|>
name|modifier
init|=
operator|new
name|BstModifier
argument_list|<
name|Character
argument_list|,
name|SimpleNode
argument_list|>
argument_list|()
block|{
annotation|@
name|Nullable
annotation|@
name|Override
specifier|public
name|BstModificationResult
argument_list|<
name|SimpleNode
argument_list|>
name|modify
parameter_list|(
name|Character
name|key
parameter_list|,
annotation|@
name|Nullable
name|SimpleNode
name|originalEntry
parameter_list|)
block|{
return|return
name|BstModificationResult
operator|.
name|identity
argument_list|(
name|originalEntry
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstModificationResult
operator|.
name|class
argument_list|,
name|BstModificationResult
operator|.
expr|<
name|SimpleNode
operator|>
name|identity
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstModifier
operator|.
name|class
argument_list|,
name|modifier
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BstMutationRule
operator|.
name|class
argument_list|,
name|BstMutationRule
operator|.
name|createRule
argument_list|(
name|modifier
argument_list|,
name|balancePolicy
argument_list|,
name|nodeFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|tester
return|;
block|}
block|}
end_class

end_unit

