begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|math
operator|.
name|MathTesting
operator|.
name|ALL_BIGINTEGER_CANDIDATES
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
name|math
operator|.
name|MathTesting
operator|.
name|ALL_ROUNDING_MODES
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
name|math
operator|.
name|MathTesting
operator|.
name|ALL_SAFE_ROUNDING_MODES
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
name|math
operator|.
name|MathTesting
operator|.
name|NEGATIVE_BIGINTEGER_CANDIDATES
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
name|math
operator|.
name|MathTesting
operator|.
name|NEGATIVE_INTEGER_CANDIDATES
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
name|math
operator|.
name|MathTesting
operator|.
name|NONZERO_BIGINTEGER_CANDIDATES
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
name|math
operator|.
name|MathTesting
operator|.
name|POSITIVE_BIGINTEGER_CANDIDATES
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|.
name|ONE
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|.
name|TEN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|math
operator|.
name|BigInteger
operator|.
name|ZERO
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
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|BigInteger
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

begin_comment
comment|/**  * Tests for BigIntegerMath.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|BigIntegerMathTest
specifier|public
class|class
name|BigIntegerMathTest
extends|extends
name|TestCase
block|{
DECL|method|testConstantSqrt2PrecomputedBits ()
specifier|public
name|void
name|testConstantSqrt2PrecomputedBits
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
literal|2
operator|*
name|BigIntegerMath
operator|.
name|SQRT2_PRECOMPUTE_THRESHOLD
operator|+
literal|1
argument_list|)
argument_list|,
name|FLOOR
argument_list|)
argument_list|,
name|BigIntegerMath
operator|.
name|SQRT2_PRECOMPUTED_BITS
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsPowerOfTwo ()
specifier|public
name|void
name|testIsPowerOfTwo
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|ALL_BIGINTEGER_CANDIDATES
control|)
block|{
comment|// Checks for a single bit set.
name|boolean
name|expected
init|=
name|x
operator|.
name|signum
argument_list|()
operator|>
literal|0
operator|&
name|x
operator|.
name|and
argument_list|(
name|x
operator|.
name|subtract
argument_list|(
name|ONE
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|ZERO
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|BigIntegerMath
operator|.
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLog2ZeroAlwaysThrows ()
specifier|public
name|void
name|testLog2ZeroAlwaysThrows
parameter_list|()
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|ZERO
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
DECL|method|testLog2NegativeAlwaysThrows ()
specifier|public
name|void
name|testLog2NegativeAlwaysThrows
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
operator|.
name|negate
argument_list|()
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
block|}
DECL|method|testLog2Floor ()
specifier|public
name|void
name|testLog2Floor
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|FLOOR
argument_list|,
name|DOWN
argument_list|)
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ZERO
operator|.
name|setBit
argument_list|(
name|result
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ZERO
operator|.
name|setBit
argument_list|(
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testLog2Ceiling ()
specifier|public
name|void
name|testLog2Ceiling
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|CEILING
argument_list|,
name|UP
argument_list|)
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ZERO
operator|.
name|setBit
argument_list|(
name|result
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|ZERO
operator|.
name|setBit
argument_list|(
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Relies on the correctness of isPowerOfTwo(BigInteger).
DECL|method|testLog2Exact ()
specifier|public
name|void
name|testLog2Exact
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
comment|// We only expect an exception if x was not a power of 2.
name|boolean
name|isPowerOf2
init|=
name|BigIntegerMath
operator|.
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|x
argument_list|,
name|ZERO
operator|.
name|setBit
argument_list|(
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|UNNECESSARY
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|isPowerOf2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|isPowerOf2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testLog2HalfUp ()
specifier|public
name|void
name|testLog2HalfUp
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|HALF_UP
argument_list|)
decl_stmt|;
name|BigInteger
name|x2
init|=
name|x
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// x^2< 2^(2 * result + 1), or else we would have rounded up
name|assertTrue
argument_list|(
name|ZERO
operator|.
name|setBit
argument_list|(
literal|2
operator|*
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// x^2>= 2^(2 * result - 1), or else we would have rounded down
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|ZERO
operator|.
name|setBit
argument_list|(
literal|2
operator|*
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLog2HalfDown ()
specifier|public
name|void
name|testLog2HalfDown
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|HALF_DOWN
argument_list|)
decl_stmt|;
name|BigInteger
name|x2
init|=
name|x
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// x^2<= 2^(2 * result + 1), or else we would have rounded up
name|assertTrue
argument_list|(
name|ZERO
operator|.
name|setBit
argument_list|(
literal|2
operator|*
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// x^2> 2^(2 * result - 1), or else we would have rounded down
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|ZERO
operator|.
name|setBit
argument_list|(
literal|2
operator|*
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Relies on the correctness of log2(BigInteger, {HALF_UP,HALF_DOWN}).
DECL|method|testLog2HalfEven ()
specifier|public
name|void
name|testLog2HalfEven
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|halfEven
init|=
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|HALF_EVEN
argument_list|)
decl_stmt|;
comment|// Now figure out what rounding mode we should behave like (it depends if FLOOR was
comment|// odd/even).
name|boolean
name|floorWasEven
init|=
operator|(
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
operator|&
literal|1
operator|)
operator|==
literal|0
decl_stmt|;
name|assertEquals
argument_list|(
name|BigIntegerMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|floorWasEven
condition|?
name|HALF_DOWN
else|:
name|HALF_UP
argument_list|)
argument_list|,
name|halfEven
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLog10ZeroAlwaysThrows ()
specifier|public
name|void
name|testLog10ZeroAlwaysThrows
parameter_list|()
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|ZERO
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
DECL|method|testLog10NegativeAlwaysThrows ()
specifier|public
name|void
name|testLog10NegativeAlwaysThrows
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
operator|.
name|negate
argument_list|()
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
block|}
DECL|method|testLog10Floor ()
specifier|public
name|void
name|testLog10Floor
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|FLOOR
argument_list|,
name|DOWN
argument_list|)
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|TEN
operator|.
name|pow
argument_list|(
name|result
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|TEN
operator|.
name|pow
argument_list|(
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testLog10Ceiling ()
specifier|public
name|void
name|testLog10Ceiling
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|CEILING
argument_list|,
name|UP
argument_list|)
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|TEN
operator|.
name|pow
argument_list|(
name|result
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|TEN
operator|.
name|pow
argument_list|(
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Relies on the correctness of log10(BigInteger, FLOOR).
DECL|method|testLog10Exact ()
specifier|public
name|void
name|testLog10Exact
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|logFloor
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
decl_stmt|;
name|boolean
name|expectSuccess
init|=
name|TEN
operator|.
name|pow
argument_list|(
name|logFloor
argument_list|)
operator|.
name|equals
argument_list|(
name|x
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|logFloor
argument_list|,
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|UNNECESSARY
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|expectSuccess
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testLog10HalfUp ()
specifier|public
name|void
name|testLog10HalfUp
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|HALF_UP
argument_list|)
decl_stmt|;
name|BigInteger
name|x2
init|=
name|x
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// x^2< 10^(2 * result + 1), or else we would have rounded up
name|assertTrue
argument_list|(
name|TEN
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// x^2>= 10^(2 * result - 1), or else we would have rounded down
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|TEN
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLog10HalfDown ()
specifier|public
name|void
name|testLog10HalfDown
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|result
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|HALF_DOWN
argument_list|)
decl_stmt|;
name|BigInteger
name|x2
init|=
name|x
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// x^2<= 10^(2 * result + 1), or else we would have rounded up
name|assertTrue
argument_list|(
name|TEN
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|result
operator|+
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
comment|// x^2> 10^(2 * result - 1), or else we would have rounded down
name|assertTrue
argument_list|(
name|result
operator|==
literal|0
operator|||
name|TEN
operator|.
name|pow
argument_list|(
literal|2
operator|*
name|result
operator|-
literal|1
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x2
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Relies on the correctness of log10(BigInteger, {HALF_UP,HALF_DOWN}).
DECL|method|testLog10HalfEven ()
specifier|public
name|void
name|testLog10HalfEven
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|int
name|halfEven
init|=
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|HALF_EVEN
argument_list|)
decl_stmt|;
comment|// Now figure out what rounding mode we should behave like (it depends if FLOOR was
comment|// odd/even).
name|boolean
name|floorWasEven
init|=
operator|(
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
operator|&
literal|1
operator|)
operator|==
literal|0
decl_stmt|;
name|assertEquals
argument_list|(
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|floorWasEven
condition|?
name|HALF_DOWN
else|:
name|HALF_UP
argument_list|)
argument_list|,
name|halfEven
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testLog10TrivialOnPowerOf10 ()
specifier|public
name|void
name|testLog10TrivialOnPowerOf10
parameter_list|()
block|{
name|BigInteger
name|x
init|=
name|BigInteger
operator|.
name|TEN
operator|.
name|pow
argument_list|(
literal|100
argument_list|)
decl_stmt|;
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|BigIntegerMath
operator|.
name|log10
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSqrtZeroAlwaysZero ()
specifier|public
name|void
name|testSqrtZeroAlwaysZero
parameter_list|()
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
name|assertEquals
argument_list|(
name|ZERO
argument_list|,
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|ZERO
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSqrtNegativeAlwaysThrows ()
specifier|public
name|void
name|testSqrtNegativeAlwaysThrows
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|NEGATIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
block|}
DECL|method|testSqrtFloor ()
specifier|public
name|void
name|testSqrtFloor
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|FLOOR
argument_list|,
name|DOWN
argument_list|)
control|)
block|{
name|BigInteger
name|result
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|compareTo
argument_list|(
name|ZERO
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testSqrtCeiling ()
specifier|public
name|void
name|testSqrtCeiling
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|asList
argument_list|(
name|CEILING
argument_list|,
name|UP
argument_list|)
control|)
block|{
name|BigInteger
name|result
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|compareTo
argument_list|(
name|ZERO
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|signum
argument_list|()
operator|==
literal|0
operator|||
name|result
operator|.
name|subtract
argument_list|(
name|ONE
argument_list|)
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Relies on the correctness of sqrt(BigInteger, FLOOR).
DECL|method|testSqrtExact ()
specifier|public
name|void
name|testSqrtExact
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|BigInteger
name|floor
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
decl_stmt|;
comment|// We only expect an exception if x was not a perfect square.
name|boolean
name|isPerfectSquare
init|=
name|floor
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|equals
argument_list|(
name|x
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|floor
argument_list|,
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|UNNECESSARY
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|isPerfectSquare
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|isPerfectSquare
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testSqrtHalfUp ()
specifier|public
name|void
name|testSqrtHalfUp
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|BigInteger
name|result
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|HALF_UP
argument_list|)
decl_stmt|;
name|BigInteger
name|plusHalfSquared
init|=
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|result
argument_list|)
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
name|BigInteger
name|x4
init|=
name|x
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// sqrt(x)< result + 0.5, so 4 * x< (result + 0.5)^2 * 4
comment|// (result + 0.5)^2 * 4 = (result^2 + result)*4 + 1
name|assertTrue
argument_list|(
name|x4
operator|.
name|compareTo
argument_list|(
name|plusHalfSquared
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|BigInteger
name|minusHalfSquared
init|=
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|subtract
argument_list|(
name|result
argument_list|)
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
comment|// sqrt(x)> result - 0.5, so 4 * x> (result - 0.5)^2 * 4
comment|// (result - 0.5)^2 * 4 = (result^2 - result)*4 + 1
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|ZERO
argument_list|)
operator|||
name|x4
operator|.
name|compareTo
argument_list|(
name|minusHalfSquared
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSqrtHalfDown ()
specifier|public
name|void
name|testSqrtHalfDown
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|BigInteger
name|result
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|HALF_DOWN
argument_list|)
decl_stmt|;
name|BigInteger
name|plusHalfSquared
init|=
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|result
argument_list|)
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
name|BigInteger
name|x4
init|=
name|x
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// sqrt(x)<= result + 0.5, so 4 * x<= (result + 0.5)^2 * 4
comment|// (result + 0.5)^2 * 4 = (result^2 + result)*4 + 1
name|assertTrue
argument_list|(
name|x4
operator|.
name|compareTo
argument_list|(
name|plusHalfSquared
argument_list|)
operator|<=
literal|0
argument_list|)
expr_stmt|;
name|BigInteger
name|minusHalfSquared
init|=
name|result
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|subtract
argument_list|(
name|result
argument_list|)
operator|.
name|shiftLeft
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|ONE
argument_list|)
decl_stmt|;
comment|// sqrt(x)> result - 0.5, so 4 * x> (result - 0.5)^2 * 4
comment|// (result - 0.5)^2 * 4 = (result^2 - result)*4 + 1
name|assertTrue
argument_list|(
name|result
operator|.
name|equals
argument_list|(
name|ZERO
argument_list|)
operator|||
name|x4
operator|.
name|compareTo
argument_list|(
name|minusHalfSquared
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Relies on the correctness of sqrt(BigInteger, {HALF_UP,HALF_DOWN}).
DECL|method|testSqrtHalfEven ()
specifier|public
name|void
name|testSqrtHalfEven
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|x
range|:
name|POSITIVE_BIGINTEGER_CANDIDATES
control|)
block|{
name|BigInteger
name|halfEven
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|HALF_EVEN
argument_list|)
decl_stmt|;
comment|// Now figure out what rounding mode we should behave like (it depends if FLOOR was
comment|// odd/even).
name|boolean
name|floorWasOdd
init|=
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
operator|.
name|testBit
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|BigIntegerMath
operator|.
name|sqrt
argument_list|(
name|x
argument_list|,
name|floorWasOdd
condition|?
name|HALF_UP
else|:
name|HALF_DOWN
argument_list|)
argument_list|,
name|halfEven
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDivNonZero ()
specifier|public
name|void
name|testDivNonZero
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|p
range|:
name|NONZERO_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|BigInteger
name|q
range|:
name|NONZERO_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_SAFE_ROUNDING_MODES
control|)
block|{
name|BigInteger
name|expected
init|=
operator|new
name|BigDecimal
argument_list|(
name|p
argument_list|)
operator|.
name|divide
argument_list|(
operator|new
name|BigDecimal
argument_list|(
name|q
argument_list|)
argument_list|,
literal|0
argument_list|,
name|mode
argument_list|)
operator|.
name|toBigIntegerExact
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|BigIntegerMath
operator|.
name|divide
argument_list|(
name|p
argument_list|,
name|q
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testDivNonZeroExact ()
specifier|public
name|void
name|testDivNonZeroExact
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|p
range|:
name|NONZERO_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|BigInteger
name|q
range|:
name|NONZERO_BIGINTEGER_CANDIDATES
control|)
block|{
name|boolean
name|dividesEvenly
init|=
name|p
operator|.
name|remainder
argument_list|(
name|q
argument_list|)
operator|.
name|equals
argument_list|(
name|ZERO
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
name|p
argument_list|,
name|BigIntegerMath
operator|.
name|divide
argument_list|(
name|p
argument_list|,
name|q
argument_list|,
name|UNNECESSARY
argument_list|)
operator|.
name|multiply
argument_list|(
name|q
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dividesEvenly
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
name|assertFalse
argument_list|(
name|dividesEvenly
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testZeroDivIsAlwaysZero ()
specifier|public
name|void
name|testZeroDivIsAlwaysZero
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|q
range|:
name|NONZERO_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
name|assertEquals
argument_list|(
name|ZERO
argument_list|,
name|BigIntegerMath
operator|.
name|divide
argument_list|(
name|ZERO
argument_list|,
name|q
argument_list|,
name|mode
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testDivByZeroAlwaysFails ()
specifier|public
name|void
name|testDivByZeroAlwaysFails
parameter_list|()
block|{
for|for
control|(
name|BigInteger
name|p
range|:
name|ALL_BIGINTEGER_CANDIDATES
control|)
block|{
for|for
control|(
name|RoundingMode
name|mode
range|:
name|ALL_ROUNDING_MODES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|divide
argument_list|(
name|p
argument_list|,
name|ZERO
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ArithmeticException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|expected
parameter_list|)
block|{}
block|}
block|}
block|}
DECL|method|testFactorial ()
specifier|public
name|void
name|testFactorial
parameter_list|()
block|{
name|BigInteger
name|expected
init|=
name|BigInteger
operator|.
name|ONE
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|300
condition|;
name|i
operator|++
control|)
block|{
name|expected
operator|=
name|expected
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testFactorial0 ()
specifier|public
name|void
name|testFactorial0
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|BigInteger
operator|.
name|ONE
argument_list|,
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFactorialNegative ()
specifier|public
name|void
name|testFactorialNegative
parameter_list|()
block|{
for|for
control|(
name|int
name|n
range|:
name|NEGATIVE_INTEGER_CANDIDATES
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
comment|// Depends on the correctness of BigIntegerMath.factorial
DECL|method|testBinomial ()
specifier|public
name|void
name|testBinomial
parameter_list|()
block|{
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<=
literal|50
condition|;
name|n
operator|++
control|)
block|{
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<=
name|n
condition|;
name|k
operator|++
control|)
block|{
name|BigInteger
name|expected
init|=
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|n
argument_list|)
operator|.
name|divide
argument_list|(
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|k
argument_list|)
argument_list|)
operator|.
name|divide
argument_list|(
name|BigIntegerMath
operator|.
name|factorial
argument_list|(
name|n
operator|-
name|k
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|BigIntegerMath
operator|.
name|binomial
argument_list|(
name|n
argument_list|,
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testBinomialOutside ()
specifier|public
name|void
name|testBinomialOutside
parameter_list|()
block|{
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<=
literal|50
condition|;
name|n
operator|++
control|)
block|{
try|try
block|{
name|BigIntegerMath
operator|.
name|binomial
argument_list|(
name|n
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|BigIntegerMath
operator|.
name|binomial
argument_list|(
name|n
argument_list|,
name|n
operator|+
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
block|}
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|BigInteger
operator|.
name|class
argument_list|,
name|ONE
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|RoundingMode
operator|.
name|class
argument_list|,
name|FLOOR
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tester
operator|.
name|setDefault
argument_list|(
name|long
operator|.
name|class
argument_list|,
literal|1L
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|BigIntegerMath
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

