begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|CEILING
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|DOWN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|FLOOR
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|HALF_DOWN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|HALF_EVEN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|HALF_UP
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|UNNECESSARY
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|UP
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|RoundingMode
operator|.
name|values
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
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|MathContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|RoundingMode
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
annotation|@
name|GwtIncompatible
DECL|class|BigDecimalMathTest
specifier|public
class|class
name|BigDecimalMathTest
extends|extends
name|TestCase
block|{
DECL|class|RoundToDoubleTester
specifier|private
specifier|static
specifier|final
class|class
name|RoundToDoubleTester
block|{
DECL|field|input
specifier|private
specifier|final
name|BigDecimal
name|input
decl_stmt|;
DECL|field|expectedValues
specifier|private
specifier|final
name|Map
argument_list|<
name|RoundingMode
argument_list|,
name|Double
argument_list|>
name|expectedValues
init|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|RoundingMode
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|unnecessaryShouldThrow
specifier|private
name|boolean
name|unnecessaryShouldThrow
init|=
literal|false
decl_stmt|;
DECL|method|RoundToDoubleTester (BigDecimal input)
name|RoundToDoubleTester
parameter_list|(
name|BigDecimal
name|input
parameter_list|)
block|{
name|this
operator|.
name|input
operator|=
name|input
expr_stmt|;
block|}
DECL|method|setExpectation (double expectedValue, RoundingMode... modes)
name|RoundToDoubleTester
name|setExpectation
parameter_list|(
name|double
name|expectedValue
parameter_list|,
name|RoundingMode
modifier|...
name|modes
parameter_list|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|modes
control|)
block|{
name|Double
name|previous
init|=
name|expectedValues
operator|.
name|put
argument_list|(
name|mode
argument_list|,
name|expectedValue
argument_list|)
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|roundUnnecessaryShouldThrow ()
specifier|public
name|RoundToDoubleTester
name|roundUnnecessaryShouldThrow
parameter_list|()
block|{
name|unnecessaryShouldThrow
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{
name|assertThat
argument_list|(
name|expectedValues
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsAtLeastElementsIn
argument_list|(
name|EnumSet
operator|.
name|complementOf
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|UNNECESSARY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|RoundingMode
argument_list|,
name|Double
argument_list|>
name|entry
range|:
name|expectedValues
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|RoundingMode
name|mode
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Double
name|expectation
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"roundToDouble("
operator|+
name|input
operator|+
literal|", "
operator|+
name|mode
operator|+
literal|")"
argument_list|)
operator|.
name|that
argument_list|(
name|BigDecimalMath
operator|.
name|roundToDouble
argument_list|(
name|input
argument_list|,
name|mode
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|expectedValues
operator|.
name|containsKey
argument_list|(
name|UNNECESSARY
argument_list|)
condition|)
block|{
name|assertWithMessage
argument_list|(
literal|"Expected roundUnnecessaryShouldThrow call"
argument_list|)
operator|.
name|that
argument_list|(
name|unnecessaryShouldThrow
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
try|try
block|{
name|BigDecimalMath
operator|.
name|roundToDouble
argument_list|(
name|input
argument_list|,
name|UNNECESSARY
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ArithmeticException for roundToDouble("
operator|+
name|input
operator|+
literal|", UNNECESSARY)"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|expected
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
block|}
DECL|method|testRoundToDouble_zero ()
specifier|public
name|void
name|testRoundToDouble_zero
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|ZERO
argument_list|)
operator|.
name|setExpectation
argument_list|(
literal|0.0
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_oneThird ()
specifier|public
name|void
name|testRoundToDouble_oneThird
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|ONE
operator|.
name|divide
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|MathContext
argument_list|(
literal|50
argument_list|,
name|HALF_EVEN
argument_list|)
argument_list|)
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|setExpectation
argument_list|(
literal|0.33333333333333337
argument_list|,
name|UP
argument_list|,
name|CEILING
argument_list|)
operator|.
name|setExpectation
argument_list|(
literal|0.3333333333333333
argument_list|,
name|HALF_EVEN
argument_list|,
name|FLOOR
argument_list|,
name|DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_halfMinDouble ()
specifier|public
name|void
name|testRoundToDouble_halfMinDouble
parameter_list|()
block|{
name|BigDecimal
name|minDouble
init|=
operator|new
name|BigDecimal
argument_list|(
name|Double
operator|.
name|MIN_VALUE
argument_list|)
decl_stmt|;
name|BigDecimal
name|halfMinDouble
init|=
name|minDouble
operator|.
name|divide
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|halfMinDouble
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|MIN_VALUE
argument_list|,
name|UP
argument_list|,
name|CEILING
argument_list|,
name|HALF_UP
argument_list|)
operator|.
name|setExpectation
argument_list|(
literal|0.0
argument_list|,
name|HALF_EVEN
argument_list|,
name|FLOOR
argument_list|,
name|DOWN
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_halfNegativeMinDouble ()
specifier|public
name|void
name|testRoundToDouble_halfNegativeMinDouble
parameter_list|()
block|{
name|BigDecimal
name|minDouble
init|=
operator|new
name|BigDecimal
argument_list|(
operator|-
name|Double
operator|.
name|MIN_VALUE
argument_list|)
decl_stmt|;
name|BigDecimal
name|halfMinDouble
init|=
name|minDouble
operator|.
name|divide
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|halfMinDouble
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|setExpectation
argument_list|(
operator|-
name|Double
operator|.
name|MIN_VALUE
argument_list|,
name|UP
argument_list|,
name|FLOOR
argument_list|,
name|HALF_UP
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
literal|0.0
argument_list|,
name|HALF_EVEN
argument_list|,
name|CEILING
argument_list|,
name|DOWN
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_smallPositive ()
specifier|public
name|void
name|testRoundToDouble_smallPositive
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|16
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
literal|16.0
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_maxPreciselyRepresentable ()
specifier|public
name|void
name|testRoundToDouble_maxPreciselyRepresentable
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|1L
operator|<<
literal|53
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|53
argument_list|)
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_maxPreciselyRepresentablePlusOne ()
specifier|public
name|void
name|testRoundToDouble_maxPreciselyRepresentablePlusOne
parameter_list|()
block|{
name|double
name|twoToThe53
init|=
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|53
argument_list|)
decl_stmt|;
comment|// the representable doubles are 2^53 and 2^53 + 2.
comment|// 2^53+1 is halfway between, so HALF_UP will go up and HALF_DOWN will go down.
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
literal|1L
operator|<<
literal|53
operator|)
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|twoToThe53
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|nextUp
argument_list|(
name|twoToThe53
argument_list|)
argument_list|,
name|CEILING
argument_list|,
name|UP
argument_list|,
name|HALF_UP
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_twoToThe54PlusOne ()
specifier|public
name|void
name|testRoundToDouble_twoToThe54PlusOne
parameter_list|()
block|{
name|double
name|twoToThe54
init|=
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
decl_stmt|;
comment|// the representable doubles are 2^54 and 2^54 + 4
comment|// 2^54+1 is less than halfway between, so HALF_DOWN and HALF_UP will both go down.
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
literal|1L
operator|<<
literal|54
operator|)
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|twoToThe54
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|nextUp
argument_list|(
name|twoToThe54
argument_list|)
argument_list|,
name|CEILING
argument_list|,
name|UP
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_twoToThe54PlusOneHalf ()
specifier|public
name|void
name|testRoundToDouble_twoToThe54PlusOneHalf
parameter_list|()
block|{
name|double
name|twoToThe54
init|=
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
decl_stmt|;
comment|// the representable doubles are 2^54 and 2^54 + 4
comment|// 2^54+1 is less than halfway between, so HALF_DOWN and HALF_UP will both go down.
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|1L
operator|<<
literal|54
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|0.5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|twoToThe54
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|nextUp
argument_list|(
name|twoToThe54
argument_list|)
argument_list|,
name|CEILING
argument_list|,
name|UP
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_twoToThe54PlusThree ()
specifier|public
name|void
name|testRoundToDouble_twoToThe54PlusThree
parameter_list|()
block|{
name|double
name|twoToThe54
init|=
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
decl_stmt|;
comment|// the representable doubles are 2^54 and 2^54 + 4
comment|// 2^54+3 is more than halfway between, so HALF_DOWN and HALF_UP will both go up.
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
literal|1L
operator|<<
literal|54
operator|)
operator|+
literal|3
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|twoToThe54
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|nextUp
argument_list|(
name|twoToThe54
argument_list|)
argument_list|,
name|CEILING
argument_list|,
name|UP
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_twoToThe54PlusFour ()
specifier|public
name|void
name|testRoundToDouble_twoToThe54PlusFour
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
literal|1L
operator|<<
literal|54
operator|)
operator|+
literal|4
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
operator|+
literal|4
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_maxDouble ()
specifier|public
name|void
name|testRoundToDouble_maxDouble
parameter_list|()
block|{
name|BigDecimal
name|maxDoubleAsBD
init|=
operator|new
name|BigDecimal
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|maxDoubleAsBD
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_maxDoublePlusOne ()
specifier|public
name|void
name|testRoundToDouble_maxDoublePlusOne
parameter_list|()
block|{
name|BigDecimal
name|maxDoubleAsBD
init|=
operator|new
name|BigDecimal
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|add
argument_list|(
name|BigDecimal
operator|.
name|ONE
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|maxDoubleAsBD
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|,
name|HALF_EVEN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|,
name|UP
argument_list|,
name|CEILING
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_wayTooBig ()
specifier|public
name|void
name|testRoundToDouble_wayTooBig
parameter_list|()
block|{
name|BigDecimal
name|bi
init|=
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|Double
operator|.
name|MAX_EXPONENT
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|bi
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|DOWN
argument_list|,
name|FLOOR
argument_list|,
name|HALF_EVEN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|,
name|UP
argument_list|,
name|CEILING
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_smallNegative ()
specifier|public
name|void
name|testRoundToDouble_smallNegative
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|-
literal|16
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
literal|16.0
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_minPreciselyRepresentable ()
specifier|public
name|void
name|testRoundToDouble_minPreciselyRepresentable
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|-
literal|1L
operator|<<
literal|53
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|53
argument_list|)
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_minPreciselyRepresentableMinusOne ()
specifier|public
name|void
name|testRoundToDouble_minPreciselyRepresentableMinusOne
parameter_list|()
block|{
comment|// the representable doubles are -2^53 and -2^53 - 2.
comment|// -2^53-1 is halfway between, so HALF_UP will go up and HALF_DOWN will go down.
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
operator|-
literal|1L
operator|<<
literal|53
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|53
argument_list|)
argument_list|,
name|DOWN
argument_list|,
name|CEILING
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|DoubleUtils
operator|.
name|nextDown
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|53
argument_list|)
argument_list|)
argument_list|,
name|FLOOR
argument_list|,
name|UP
argument_list|,
name|HALF_UP
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_negativeTwoToThe54MinusOne ()
specifier|public
name|void
name|testRoundToDouble_negativeTwoToThe54MinusOne
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
operator|-
literal|1L
operator|<<
literal|54
operator|)
operator|-
literal|1
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
argument_list|,
name|DOWN
argument_list|,
name|CEILING
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|DoubleUtils
operator|.
name|nextDown
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
argument_list|)
argument_list|,
name|FLOOR
argument_list|,
name|UP
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_negativeTwoToThe54MinusThree ()
specifier|public
name|void
name|testRoundToDouble_negativeTwoToThe54MinusThree
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
operator|-
literal|1L
operator|<<
literal|54
operator|)
operator|-
literal|3
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
argument_list|,
name|DOWN
argument_list|,
name|CEILING
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|DoubleUtils
operator|.
name|nextDown
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
argument_list|)
argument_list|,
name|FLOOR
argument_list|,
name|UP
argument_list|,
name|HALF_DOWN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_EVEN
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_negativeTwoToThe54MinusFour ()
specifier|public
name|void
name|testRoundToDouble_negativeTwoToThe54MinusFour
parameter_list|()
block|{
operator|new
name|RoundToDoubleTester
argument_list|(
name|BigDecimal
operator|.
name|valueOf
argument_list|(
operator|(
operator|-
literal|1L
operator|<<
literal|54
operator|)
operator|-
literal|4
argument_list|)
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
literal|54
argument_list|)
operator|-
literal|4
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_minDouble ()
specifier|public
name|void
name|testRoundToDouble_minDouble
parameter_list|()
block|{
name|BigDecimal
name|minDoubleAsBD
init|=
operator|new
name|BigDecimal
argument_list|(
operator|-
name|Double
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|minDoubleAsBD
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|values
argument_list|()
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_minDoubleMinusOne ()
specifier|public
name|void
name|testRoundToDouble_minDoubleMinusOne
parameter_list|()
block|{
name|BigDecimal
name|minDoubleAsBD
init|=
operator|new
name|BigDecimal
argument_list|(
operator|-
name|Double
operator|.
name|MAX_VALUE
argument_list|)
operator|.
name|subtract
argument_list|(
name|BigDecimal
operator|.
name|ONE
argument_list|)
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|minDoubleAsBD
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|DOWN
argument_list|,
name|CEILING
argument_list|,
name|HALF_EVEN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|NEGATIVE_INFINITY
argument_list|,
name|UP
argument_list|,
name|FLOOR
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testRoundToDouble_negativeWayTooBig ()
specifier|public
name|void
name|testRoundToDouble_negativeWayTooBig
parameter_list|()
block|{
name|BigDecimal
name|bi
init|=
name|BigDecimal
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|Double
operator|.
name|MAX_EXPONENT
argument_list|)
operator|.
name|negate
argument_list|()
decl_stmt|;
operator|new
name|RoundToDoubleTester
argument_list|(
name|bi
argument_list|)
operator|.
name|setExpectation
argument_list|(
operator|-
name|Double
operator|.
name|MAX_VALUE
argument_list|,
name|DOWN
argument_list|,
name|CEILING
argument_list|,
name|HALF_EVEN
argument_list|,
name|HALF_UP
argument_list|,
name|HALF_DOWN
argument_list|)
operator|.
name|setExpectation
argument_list|(
name|Double
operator|.
name|NEGATIVE_INFINITY
argument_list|,
name|UP
argument_list|,
name|FLOOR
argument_list|)
operator|.
name|roundUnnecessaryShouldThrow
argument_list|()
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

