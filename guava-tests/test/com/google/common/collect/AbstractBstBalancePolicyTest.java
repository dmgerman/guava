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
name|collect
operator|.
name|BstTesting
operator|.
name|assertInOrderTraversalIs
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
name|BstTesting
operator|.
name|nodeFactory
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
name|collect
operator|.
name|BstTesting
operator|.
name|SimpleNode
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
comment|/**  * Tests for an arbitrary {@code BSTRebalancePolicy}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractBstBalancePolicyTest
specifier|public
specifier|abstract
class|class
name|AbstractBstBalancePolicyTest
extends|extends
name|TestCase
block|{
DECL|method|getBalancePolicy ()
specifier|protected
specifier|abstract
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
name|getBalancePolicy
parameter_list|()
function_decl|;
DECL|method|testBalanceLeaf ()
specifier|public
name|void
name|testBalanceLeaf
parameter_list|()
block|{
name|SimpleNode
name|a
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
name|assertInOrderTraversalIs
argument_list|(
name|getBalancePolicy
argument_list|()
operator|.
name|balance
argument_list|(
name|nodeFactory
argument_list|,
name|a
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
block|}
DECL|method|balanceNew (char c, @Nullable SimpleNode left, @Nullable SimpleNode right)
specifier|private
name|SimpleNode
name|balanceNew
parameter_list|(
name|char
name|c
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
name|getBalancePolicy
argument_list|()
operator|.
name|balance
argument_list|(
name|nodeFactory
argument_list|,
operator|new
name|SimpleNode
argument_list|(
name|c
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
name|left
argument_list|,
name|right
argument_list|)
return|;
block|}
DECL|method|testBalanceTree1 ()
specifier|public
name|void
name|testBalanceTree1
parameter_list|()
block|{
comment|//   b
comment|//    \
comment|//    c
name|SimpleNode
name|c
init|=
name|balanceNew
argument_list|(
literal|'c'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|b
init|=
name|balanceNew
argument_list|(
literal|'b'
argument_list|,
literal|null
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|assertInOrderTraversalIs
argument_list|(
name|b
argument_list|,
literal|"bc"
argument_list|)
expr_stmt|;
block|}
DECL|method|testBalanceTree2 ()
specifier|public
name|void
name|testBalanceTree2
parameter_list|()
block|{
comment|//   b
comment|//  /
comment|//  a
name|SimpleNode
name|a
init|=
name|balanceNew
argument_list|(
literal|'a'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|b
init|=
name|balanceNew
argument_list|(
literal|'b'
argument_list|,
name|a
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertInOrderTraversalIs
argument_list|(
name|b
argument_list|,
literal|"ab"
argument_list|)
expr_stmt|;
block|}
DECL|method|testBalanceTree3 ()
specifier|public
name|void
name|testBalanceTree3
parameter_list|()
block|{
comment|//   b
comment|//  / \
comment|//  a c
name|SimpleNode
name|a
init|=
name|balanceNew
argument_list|(
literal|'a'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|c
init|=
name|balanceNew
argument_list|(
literal|'c'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|b
init|=
name|balanceNew
argument_list|(
literal|'b'
argument_list|,
name|a
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|assertInOrderTraversalIs
argument_list|(
name|b
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
block|}
DECL|method|testBalanceTree4 ()
specifier|public
name|void
name|testBalanceTree4
parameter_list|()
block|{
comment|// a
comment|//  \
comment|//  b
comment|//   \
comment|//   c
comment|//    \
comment|//    d
comment|//     \
comment|//     e
comment|//      \
comment|//       f
name|SimpleNode
name|f
init|=
name|balanceNew
argument_list|(
literal|'f'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|e
init|=
name|balanceNew
argument_list|(
literal|'e'
argument_list|,
literal|null
argument_list|,
name|f
argument_list|)
decl_stmt|;
name|SimpleNode
name|d
init|=
name|balanceNew
argument_list|(
literal|'d'
argument_list|,
literal|null
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|SimpleNode
name|c
init|=
name|balanceNew
argument_list|(
literal|'c'
argument_list|,
literal|null
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|SimpleNode
name|b
init|=
name|balanceNew
argument_list|(
literal|'b'
argument_list|,
literal|null
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|SimpleNode
name|a
init|=
name|balanceNew
argument_list|(
literal|'a'
argument_list|,
literal|null
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|assertInOrderTraversalIs
argument_list|(
name|a
argument_list|,
literal|"abcdef"
argument_list|)
expr_stmt|;
block|}
DECL|method|testBalanceTree5 ()
specifier|public
name|void
name|testBalanceTree5
parameter_list|()
block|{
comment|//       f
comment|//      /
comment|//      e
comment|//     /
comment|//     d
comment|//    /
comment|//    c
comment|//   /
comment|//   b
comment|//  /
comment|//  a
name|SimpleNode
name|a
init|=
name|balanceNew
argument_list|(
literal|'a'
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|b
init|=
name|balanceNew
argument_list|(
literal|'b'
argument_list|,
name|a
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|c
init|=
name|balanceNew
argument_list|(
literal|'c'
argument_list|,
name|b
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|d
init|=
name|balanceNew
argument_list|(
literal|'d'
argument_list|,
name|c
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|e
init|=
name|balanceNew
argument_list|(
literal|'e'
argument_list|,
name|d
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SimpleNode
name|f
init|=
name|balanceNew
argument_list|(
literal|'f'
argument_list|,
name|e
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertInOrderTraversalIs
argument_list|(
name|f
argument_list|,
literal|"abcdef"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

