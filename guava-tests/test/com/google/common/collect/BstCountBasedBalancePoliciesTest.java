begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|countAggregate
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
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Tests for the policies exported by {@link BstCountBasedBalancePolicies}  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|BstCountBasedBalancePoliciesTest
specifier|public
class|class
name|BstCountBasedBalancePoliciesTest
extends|extends
name|TestCase
block|{
DECL|class|NoRebalanceTest
specifier|public
specifier|static
class|class
name|NoRebalanceTest
extends|extends
name|AbstractBstBalancePolicyTest
block|{
annotation|@
name|Override
DECL|method|getBalancePolicy ()
specifier|protected
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
name|getBalancePolicy
parameter_list|()
block|{
return|return
name|BstCountBasedBalancePolicies
operator|.
name|noRebalancePolicy
argument_list|(
name|countAggregate
argument_list|)
return|;
block|}
block|}
DECL|class|SingleRebalanceTest
specifier|public
specifier|static
class|class
name|SingleRebalanceTest
extends|extends
name|AbstractBstBalancePolicyTest
block|{
annotation|@
name|Override
DECL|method|getBalancePolicy ()
specifier|protected
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
name|getBalancePolicy
parameter_list|()
block|{
return|return
name|BstCountBasedBalancePolicies
operator|.
expr|<
name|Character
operator|,
name|SimpleNode
operator|>
name|singleRebalancePolicy
argument_list|(
name|countAggregate
argument_list|)
return|;
block|}
block|}
DECL|class|FullRebalanceTest
specifier|public
specifier|static
class|class
name|FullRebalanceTest
extends|extends
name|AbstractBstBalancePolicyTest
block|{
annotation|@
name|Override
DECL|method|getBalancePolicy ()
specifier|protected
name|BstBalancePolicy
argument_list|<
name|SimpleNode
argument_list|>
name|getBalancePolicy
parameter_list|()
block|{
return|return
name|BstCountBasedBalancePolicies
operator|.
expr|<
name|Character
operator|,
name|SimpleNode
operator|>
name|fullRebalancePolicy
argument_list|(
name|countAggregate
argument_list|)
return|;
block|}
block|}
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|NoRebalanceTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|SingleRebalanceTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|FullRebalanceTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
block|}
end_class

end_unit

