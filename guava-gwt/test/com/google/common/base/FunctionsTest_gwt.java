begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
package|;
end_package

begin_class
DECL|class|FunctionsTest_gwt
specifier|public
class|class
name|FunctionsTest_gwt
extends|extends
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|junit
operator|.
name|client
operator|.
name|GWTTestCase
block|{
DECL|method|getModuleName ()
annotation|@
name|Override
specifier|public
name|String
name|getModuleName
parameter_list|()
block|{
return|return
literal|"com.google.common.base.testModule"
return|;
block|}
DECL|method|testComposeOfFunctionsIsAssociative ()
specifier|public
name|void
name|testComposeOfFunctionsIsAssociative
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testComposeOfFunctionsIsAssociative
argument_list|()
expr_stmt|;
block|}
DECL|method|testComposeOfPredicateAndFunctionIsAssociative ()
specifier|public
name|void
name|testComposeOfPredicateAndFunctionIsAssociative
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testComposeOfPredicateAndFunctionIsAssociative
argument_list|()
expr_stmt|;
block|}
DECL|method|testComposition ()
specifier|public
name|void
name|testComposition
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testComposition
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompositionWildcard ()
specifier|public
name|void
name|testCompositionWildcard
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testCompositionWildcard
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstant ()
specifier|public
name|void
name|testConstant
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testConstant
argument_list|()
expr_stmt|;
block|}
DECL|method|testForMapWildCardWithDefault ()
specifier|public
name|void
name|testForMapWildCardWithDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForMapWildCardWithDefault
argument_list|()
expr_stmt|;
block|}
DECL|method|testForMapWithDefault ()
specifier|public
name|void
name|testForMapWithDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForMapWithDefault
argument_list|()
expr_stmt|;
block|}
DECL|method|testForMapWithDefault_null ()
specifier|public
name|void
name|testForMapWithDefault_null
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForMapWithDefault_null
argument_list|()
expr_stmt|;
block|}
DECL|method|testForMapWithoutDefault ()
specifier|public
name|void
name|testForMapWithoutDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForMapWithoutDefault
argument_list|()
expr_stmt|;
block|}
DECL|method|testForPredicate ()
specifier|public
name|void
name|testForPredicate
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForPredicate
argument_list|()
expr_stmt|;
block|}
DECL|method|testForSupplier ()
specifier|public
name|void
name|testForSupplier
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testForSupplier
argument_list|()
expr_stmt|;
block|}
DECL|method|testIdentity_notSame ()
specifier|public
name|void
name|testIdentity_notSame
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIdentity_notSame
argument_list|()
expr_stmt|;
block|}
DECL|method|testIdentity_same ()
specifier|public
name|void
name|testIdentity_same
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testIdentity_same
argument_list|()
expr_stmt|;
block|}
DECL|method|testToStringFunction_apply ()
specifier|public
name|void
name|testToStringFunction_apply
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
name|testCase
init|=
operator|new
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|FunctionsTest
argument_list|()
decl_stmt|;
name|testCase
operator|.
name|testToStringFunction_apply
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

