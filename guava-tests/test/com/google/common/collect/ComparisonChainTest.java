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
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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

begin_comment
comment|/**  * Unit test for {@link ComparisonChain}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ComparisonChainTest
specifier|public
class|class
name|ComparisonChainTest
extends|extends
name|TestCase
block|{
DECL|field|DONT_COMPARE_ME
specifier|private
specifier|static
specifier|final
name|DontCompareMe
name|DONT_COMPARE_ME
init|=
operator|new
name|DontCompareMe
argument_list|()
decl_stmt|;
DECL|class|DontCompareMe
specifier|private
specifier|static
class|class
name|DontCompareMe
implements|implements
name|Comparable
argument_list|<
name|DontCompareMe
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compareTo (DontCompareMe o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|DontCompareMe
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|()
throw|;
block|}
block|}
DECL|method|testDegenerate ()
specifier|public
name|void
name|testDegenerate
parameter_list|()
block|{
comment|// kinda bogus, but who cares?
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|result
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOneEqual ()
specifier|public
name|void
name|testOneEqual
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
operator|.
name|result
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOneEqualUsingComparator ()
specifier|public
name|void
name|testOneEqualUsingComparator
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"A"
argument_list|,
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
operator|.
name|result
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testManyEqual ()
specifier|public
name|void
name|testManyEqual
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
operator|.
name|compare
argument_list|(
literal|1L
argument_list|,
literal|1L
argument_list|)
operator|.
name|compareFalseFirst
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
operator|.
name|compare
argument_list|(
literal|1.0
argument_list|,
literal|1.0
argument_list|)
operator|.
name|compare
argument_list|(
literal|1.0f
argument_list|,
literal|1.0f
argument_list|)
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|,
name|Ordering
operator|.
name|usingToString
argument_list|()
argument_list|)
operator|.
name|result
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testShortCircuitLess ()
specifier|public
name|void
name|testShortCircuitLess
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|compare
argument_list|(
name|DONT_COMPARE_ME
argument_list|,
name|DONT_COMPARE_ME
argument_list|)
operator|.
name|result
argument_list|()
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testShortCircuitGreater ()
specifier|public
name|void
name|testShortCircuitGreater
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|"b"
argument_list|,
literal|"a"
argument_list|)
operator|.
name|compare
argument_list|(
name|DONT_COMPARE_ME
argument_list|,
name|DONT_COMPARE_ME
argument_list|)
operator|.
name|result
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testShortCircuitSecondStep ()
specifier|public
name|void
name|testShortCircuitSecondStep
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"a"
argument_list|)
operator|.
name|compare
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
operator|.
name|compare
argument_list|(
name|DONT_COMPARE_ME
argument_list|,
name|DONT_COMPARE_ME
argument_list|)
operator|.
name|result
argument_list|()
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompareFalseFirst ()
specifier|public
name|void
name|testCompareFalseFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareFalseFirst
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
operator|.
name|result
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareFalseFirst
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
operator|.
name|result
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareFalseFirst
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
operator|.
name|result
argument_list|()
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareFalseFirst
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
operator|.
name|result
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompareTrueFirst ()
specifier|public
name|void
name|testCompareTrueFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareTrueFirst
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
operator|.
name|result
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareTrueFirst
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
operator|.
name|result
argument_list|()
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareTrueFirst
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
operator|.
name|result
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compareTrueFirst
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
operator|.
name|result
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

