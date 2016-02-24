begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|math
operator|.
name|MathPreconditions
operator|.
name|checkNonNegative
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
name|MathPreconditions
operator|.
name|checkPositive
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
name|MathPreconditions
operator|.
name|checkRoundingUnnecessary
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
name|HALF_EVEN
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
name|annotations
operator|.
name|VisibleForTesting
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_comment
comment|/**  * A class for arithmetic on values of type {@code BigInteger}.  *  *<p>The implementations of many methods in this class are based on material from Henry S. Warren,  * Jr.'s<i>Hacker's Delight</i>, (Addison Wesley, 2002).  *  *<p>Similar functionality for {@code int} and for {@code long} can be found in {@link IntMath} and  * {@link LongMath} respectively.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BigIntegerMath
specifier|public
specifier|final
class|class
name|BigIntegerMath
block|{
comment|/**    * Returns the smallest power of two greater than or equal to {@code x}.  This is equivalent to    * {@code BigInteger.valueOf(2).pow(log2(x, CEILING))}.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @since 20.0    */
DECL|method|ceilingPowerOfTwo (BigInteger x)
specifier|public
specifier|static
name|BigInteger
name|ceilingPowerOfTwo
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
return|return
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
name|log2
argument_list|(
name|x
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the largest power of two less than or equal to {@code x}.  This is equivalent to    * {@code BigInteger.valueOf(2).pow(log2(x, FLOOR))}.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @since 20.0    */
DECL|method|floorPowerOfTwo (BigInteger x)
specifier|public
specifier|static
name|BigInteger
name|floorPowerOfTwo
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
return|return
name|BigInteger
operator|.
name|ZERO
operator|.
name|setBit
argument_list|(
name|log2
argument_list|(
name|x
argument_list|,
name|RoundingMode
operator|.
name|FLOOR
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code x} represents a power of two.    */
DECL|method|isPowerOfTwo (BigInteger x)
specifier|public
specifier|static
name|boolean
name|isPowerOfTwo
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|x
argument_list|)
expr_stmt|;
return|return
name|x
operator|.
name|signum
argument_list|()
operator|>
literal|0
operator|&&
name|x
operator|.
name|getLowestSetBit
argument_list|()
operator|==
name|x
operator|.
name|bitLength
argument_list|()
operator|-
literal|1
return|;
block|}
comment|/**    * Returns the base-2 logarithm of {@code x}, rounded according to the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and {@code x}    *     is not a power of two    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
comment|// TODO(kevinb): remove after this warning is disabled globally
DECL|method|log2 (BigInteger x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log2
parameter_list|(
name|BigInteger
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|checkPositive
argument_list|(
literal|"x"
argument_list|,
name|checkNotNull
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|logFloor
init|=
name|x
operator|.
name|bitLength
argument_list|()
operator|-
literal|1
decl_stmt|;
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|UNNECESSARY
case|:
name|checkRoundingUnnecessary
argument_list|(
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
comment|// fall through
case|case
name|DOWN
case|:
case|case
name|FLOOR
case|:
return|return
name|logFloor
return|;
case|case
name|UP
case|:
case|case
name|CEILING
case|:
return|return
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
condition|?
name|logFloor
else|:
name|logFloor
operator|+
literal|1
return|;
case|case
name|HALF_DOWN
case|:
case|case
name|HALF_UP
case|:
case|case
name|HALF_EVEN
case|:
if|if
condition|(
name|logFloor
operator|<
name|SQRT2_PRECOMPUTE_THRESHOLD
condition|)
block|{
name|BigInteger
name|halfPower
init|=
name|SQRT2_PRECOMPUTED_BITS
operator|.
name|shiftRight
argument_list|(
name|SQRT2_PRECOMPUTE_THRESHOLD
operator|-
name|logFloor
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|.
name|compareTo
argument_list|(
name|halfPower
argument_list|)
operator|<=
literal|0
condition|)
block|{
return|return
name|logFloor
return|;
block|}
else|else
block|{
return|return
name|logFloor
operator|+
literal|1
return|;
block|}
block|}
comment|// Since sqrt(2) is irrational, log2(x) - logFloor cannot be exactly 0.5
comment|//
comment|// To determine which side of logFloor.5 the logarithm is,
comment|// we compare x^2 to 2^(2 * logFloor + 1).
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
name|int
name|logX2Floor
init|=
name|x2
operator|.
name|bitLength
argument_list|()
operator|-
literal|1
decl_stmt|;
return|return
operator|(
name|logX2Floor
operator|<
literal|2
operator|*
name|logFloor
operator|+
literal|1
operator|)
condition|?
name|logFloor
else|:
name|logFloor
operator|+
literal|1
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/*    * The maximum number of bits in a square root for which we'll precompute an explicit half power    * of two. This can be any value, but higher values incur more class load time and linearly    * increasing memory consumption.    */
DECL|field|SQRT2_PRECOMPUTE_THRESHOLD
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
name|SQRT2_PRECOMPUTE_THRESHOLD
init|=
literal|256
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|SQRT2_PRECOMPUTED_BITS
specifier|static
specifier|final
name|BigInteger
name|SQRT2_PRECOMPUTED_BITS
init|=
operator|new
name|BigInteger
argument_list|(
literal|"16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a"
argument_list|,
literal|16
argument_list|)
decl_stmt|;
comment|/**    * Returns the base-10 logarithm of {@code x}, rounded according to the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and {@code x}    *     is not a power of ten    */
annotation|@
name|GwtIncompatible
comment|// TODO
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|log10 (BigInteger x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log10
parameter_list|(
name|BigInteger
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|checkPositive
argument_list|(
literal|"x"
argument_list|,
name|x
argument_list|)
expr_stmt|;
if|if
condition|(
name|fitsInLong
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|LongMath
operator|.
name|log10
argument_list|(
name|x
operator|.
name|longValue
argument_list|()
argument_list|,
name|mode
argument_list|)
return|;
block|}
name|int
name|approxLog10
init|=
call|(
name|int
call|)
argument_list|(
name|log2
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
operator|*
name|LN_2
operator|/
name|LN_10
argument_list|)
decl_stmt|;
name|BigInteger
name|approxPow
init|=
name|BigInteger
operator|.
name|TEN
operator|.
name|pow
argument_list|(
name|approxLog10
argument_list|)
decl_stmt|;
name|int
name|approxCmp
init|=
name|approxPow
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
decl_stmt|;
comment|/*      * We adjust approxLog10 and approxPow until they're equal to floor(log10(x)) and      * 10^floor(log10(x)).      */
if|if
condition|(
name|approxCmp
operator|>
literal|0
condition|)
block|{
comment|/*        * The code is written so that even completely incorrect approximations will still yield the        * correct answer eventually, but in practice this branch should almost never be entered, and        * even then the loop should not run more than once.        */
do|do
block|{
name|approxLog10
operator|--
expr_stmt|;
name|approxPow
operator|=
name|approxPow
operator|.
name|divide
argument_list|(
name|BigInteger
operator|.
name|TEN
argument_list|)
expr_stmt|;
name|approxCmp
operator|=
name|approxPow
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|approxCmp
operator|>
literal|0
condition|)
do|;
block|}
else|else
block|{
name|BigInteger
name|nextPow
init|=
name|BigInteger
operator|.
name|TEN
operator|.
name|multiply
argument_list|(
name|approxPow
argument_list|)
decl_stmt|;
name|int
name|nextCmp
init|=
name|nextPow
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
decl_stmt|;
while|while
condition|(
name|nextCmp
operator|<=
literal|0
condition|)
block|{
name|approxLog10
operator|++
expr_stmt|;
name|approxPow
operator|=
name|nextPow
expr_stmt|;
name|approxCmp
operator|=
name|nextCmp
expr_stmt|;
name|nextPow
operator|=
name|BigInteger
operator|.
name|TEN
operator|.
name|multiply
argument_list|(
name|approxPow
argument_list|)
expr_stmt|;
name|nextCmp
operator|=
name|nextPow
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|floorLog
init|=
name|approxLog10
decl_stmt|;
name|BigInteger
name|floorPow
init|=
name|approxPow
decl_stmt|;
name|int
name|floorCmp
init|=
name|approxCmp
decl_stmt|;
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|UNNECESSARY
case|:
name|checkRoundingUnnecessary
argument_list|(
name|floorCmp
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// fall through
case|case
name|FLOOR
case|:
case|case
name|DOWN
case|:
return|return
name|floorLog
return|;
case|case
name|CEILING
case|:
case|case
name|UP
case|:
return|return
name|floorPow
operator|.
name|equals
argument_list|(
name|x
argument_list|)
condition|?
name|floorLog
else|:
name|floorLog
operator|+
literal|1
return|;
case|case
name|HALF_DOWN
case|:
case|case
name|HALF_UP
case|:
case|case
name|HALF_EVEN
case|:
comment|// Since sqrt(10) is irrational, log10(x) - floorLog can never be exactly 0.5
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
name|BigInteger
name|halfPowerSquared
init|=
name|floorPow
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|TEN
argument_list|)
decl_stmt|;
return|return
operator|(
name|x2
operator|.
name|compareTo
argument_list|(
name|halfPowerSquared
argument_list|)
operator|<=
literal|0
operator|)
condition|?
name|floorLog
else|:
name|floorLog
operator|+
literal|1
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
DECL|field|LN_10
specifier|private
specifier|static
specifier|final
name|double
name|LN_10
init|=
name|Math
operator|.
name|log
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|field|LN_2
specifier|private
specifier|static
specifier|final
name|double
name|LN_2
init|=
name|Math
operator|.
name|log
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|/**    * Returns the square root of {@code x}, rounded with the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x< 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and    *     {@code sqrt(x)} is not an integer    */
annotation|@
name|GwtIncompatible
comment|// TODO
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|sqrt (BigInteger x, RoundingMode mode)
specifier|public
specifier|static
name|BigInteger
name|sqrt
parameter_list|(
name|BigInteger
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|checkNonNegative
argument_list|(
literal|"x"
argument_list|,
name|x
argument_list|)
expr_stmt|;
if|if
condition|(
name|fitsInLong
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|LongMath
operator|.
name|sqrt
argument_list|(
name|x
operator|.
name|longValue
argument_list|()
argument_list|,
name|mode
argument_list|)
argument_list|)
return|;
block|}
name|BigInteger
name|sqrtFloor
init|=
name|sqrtFloor
argument_list|(
name|x
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|UNNECESSARY
case|:
name|checkRoundingUnnecessary
argument_list|(
name|sqrtFloor
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
argument_list|)
expr_stmt|;
comment|// fall through
case|case
name|FLOOR
case|:
case|case
name|DOWN
case|:
return|return
name|sqrtFloor
return|;
case|case
name|CEILING
case|:
case|case
name|UP
case|:
name|int
name|sqrtFloorInt
init|=
name|sqrtFloor
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|boolean
name|sqrtFloorIsExact
init|=
operator|(
name|sqrtFloorInt
operator|*
name|sqrtFloorInt
operator|==
name|x
operator|.
name|intValue
argument_list|()
operator|)
comment|// fast check mod 2^32
operator|&&
name|sqrtFloor
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
comment|// slow exact check
return|return
name|sqrtFloorIsExact
condition|?
name|sqrtFloor
else|:
name|sqrtFloor
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ONE
argument_list|)
return|;
case|case
name|HALF_DOWN
case|:
case|case
name|HALF_UP
case|:
case|case
name|HALF_EVEN
case|:
name|BigInteger
name|halfSquare
init|=
name|sqrtFloor
operator|.
name|pow
argument_list|(
literal|2
argument_list|)
operator|.
name|add
argument_list|(
name|sqrtFloor
argument_list|)
decl_stmt|;
comment|/*          * We wish to test whether or not x<= (sqrtFloor + 0.5)^2 = halfSquare + 0.25. Since both x          * and halfSquare are integers, this is equivalent to testing whether or not x<=          * halfSquare.          */
return|return
operator|(
name|halfSquare
operator|.
name|compareTo
argument_list|(
name|x
argument_list|)
operator|>=
literal|0
operator|)
condition|?
name|sqrtFloor
else|:
name|sqrtFloor
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|ONE
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|method|sqrtFloor (BigInteger x)
specifier|private
specifier|static
name|BigInteger
name|sqrtFloor
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
comment|/*      * Adapted from Hacker's Delight, Figure 11-1.      *      * Using DoubleUtils.bigToDouble, getting a double approximation of x is extremely fast, and      * then we can get a double approximation of the square root. Then, we iteratively improve this      * guess with an application of Newton's method, which sets guess := (guess + (x / guess)) / 2.      * This iteration has the following two properties:      *      * a) every iteration (except potentially the first) has guess>= floor(sqrt(x)). This is      * because guess' is the arithmetic mean of guess and x / guess, sqrt(x) is the geometric mean,      * and the arithmetic mean is always higher than the geometric mean.      *      * b) this iteration converges to floor(sqrt(x)). In fact, the number of correct digits doubles      * with each iteration, so this algorithm takes O(log(digits)) iterations.      *      * We start out with a double-precision approximation, which may be higher or lower than the      * true value. Therefore, we perform at least one Newton iteration to get a guess that's      * definitely>= floor(sqrt(x)), and then continue the iteration until we reach a fixed point.      */
name|BigInteger
name|sqrt0
decl_stmt|;
name|int
name|log2
init|=
name|log2
argument_list|(
name|x
argument_list|,
name|FLOOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|log2
operator|<
name|Double
operator|.
name|MAX_EXPONENT
condition|)
block|{
name|sqrt0
operator|=
name|sqrtApproxWithDoubles
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|shift
init|=
operator|(
name|log2
operator|-
name|DoubleUtils
operator|.
name|SIGNIFICAND_BITS
operator|)
operator|&
operator|~
literal|1
decl_stmt|;
comment|// even!
comment|/*        * We have that x / 2^shift< 2^54. Our initial approximation to sqrtFloor(x) will be        * 2^(shift/2) * sqrtApproxWithDoubles(x / 2^shift).        */
name|sqrt0
operator|=
name|sqrtApproxWithDoubles
argument_list|(
name|x
operator|.
name|shiftRight
argument_list|(
name|shift
argument_list|)
argument_list|)
operator|.
name|shiftLeft
argument_list|(
name|shift
operator|>>
literal|1
argument_list|)
expr_stmt|;
block|}
name|BigInteger
name|sqrt1
init|=
name|sqrt0
operator|.
name|add
argument_list|(
name|x
operator|.
name|divide
argument_list|(
name|sqrt0
argument_list|)
argument_list|)
operator|.
name|shiftRight
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|sqrt0
operator|.
name|equals
argument_list|(
name|sqrt1
argument_list|)
condition|)
block|{
return|return
name|sqrt0
return|;
block|}
do|do
block|{
name|sqrt0
operator|=
name|sqrt1
expr_stmt|;
name|sqrt1
operator|=
name|sqrt0
operator|.
name|add
argument_list|(
name|x
operator|.
name|divide
argument_list|(
name|sqrt0
argument_list|)
argument_list|)
operator|.
name|shiftRight
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|sqrt1
operator|.
name|compareTo
argument_list|(
name|sqrt0
argument_list|)
operator|<
literal|0
condition|)
do|;
return|return
name|sqrt0
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|method|sqrtApproxWithDoubles (BigInteger x)
specifier|private
specifier|static
name|BigInteger
name|sqrtApproxWithDoubles
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
return|return
name|DoubleMath
operator|.
name|roundToBigInteger
argument_list|(
name|Math
operator|.
name|sqrt
argument_list|(
name|DoubleUtils
operator|.
name|bigToDouble
argument_list|(
name|x
argument_list|)
argument_list|)
argument_list|,
name|HALF_EVEN
argument_list|)
return|;
block|}
comment|/**    * Returns the result of dividing {@code p} by {@code q}, rounding using the specified    * {@code RoundingMode}.    *    * @throws ArithmeticException if {@code q == 0}, or if {@code mode == UNNECESSARY} and {@code a}    *     is not an integer multiple of {@code b}    */
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|method|divide (BigInteger p, BigInteger q, RoundingMode mode)
specifier|public
specifier|static
name|BigInteger
name|divide
parameter_list|(
name|BigInteger
name|p
parameter_list|,
name|BigInteger
name|q
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|BigDecimal
name|pDec
init|=
operator|new
name|BigDecimal
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|BigDecimal
name|qDec
init|=
operator|new
name|BigDecimal
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
name|pDec
operator|.
name|divide
argument_list|(
name|qDec
argument_list|,
literal|0
argument_list|,
name|mode
argument_list|)
operator|.
name|toBigIntegerExact
argument_list|()
return|;
block|}
comment|/**    * Returns {@code n!}, that is, the product of the first {@code n} positive integers, or {@code 1}    * if {@code n == 0}.    *    *<p><b>Warning:</b> the result takes<i>O(n log n)</i> space, so use cautiously.    *    *<p>This uses an efficient binary recursive algorithm to compute the factorial with balanced    * multiplies. It also removes all the 2s from the intermediate products (shifting them back in at    * the end).    *    * @throws IllegalArgumentException if {@code n< 0}    */
DECL|method|factorial (int n)
specifier|public
specifier|static
name|BigInteger
name|factorial
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|checkNonNegative
argument_list|(
literal|"n"
argument_list|,
name|n
argument_list|)
expr_stmt|;
comment|// If the factorial is small enough, just use LongMath to do it.
if|if
condition|(
name|n
operator|<
name|LongMath
operator|.
name|factorials
operator|.
name|length
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|LongMath
operator|.
name|factorials
index|[
name|n
index|]
argument_list|)
return|;
block|}
comment|// Pre-allocate space for our list of intermediate BigIntegers.
name|int
name|approxSize
init|=
name|IntMath
operator|.
name|divide
argument_list|(
name|n
operator|*
name|IntMath
operator|.
name|log2
argument_list|(
name|n
argument_list|,
name|CEILING
argument_list|)
argument_list|,
name|Long
operator|.
name|SIZE
argument_list|,
name|CEILING
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|BigInteger
argument_list|>
name|bignums
init|=
operator|new
name|ArrayList
argument_list|<
name|BigInteger
argument_list|>
argument_list|(
name|approxSize
argument_list|)
decl_stmt|;
comment|// Start from the pre-computed maximum long factorial.
name|int
name|startingNumber
init|=
name|LongMath
operator|.
name|factorials
operator|.
name|length
decl_stmt|;
name|long
name|product
init|=
name|LongMath
operator|.
name|factorials
index|[
name|startingNumber
operator|-
literal|1
index|]
decl_stmt|;
comment|// Strip off 2s from this value.
name|int
name|shift
init|=
name|Long
operator|.
name|numberOfTrailingZeros
argument_list|(
name|product
argument_list|)
decl_stmt|;
name|product
operator|>>=
name|shift
expr_stmt|;
comment|// Use floor(log2(num)) + 1 to prevent overflow of multiplication.
name|int
name|productBits
init|=
name|LongMath
operator|.
name|log2
argument_list|(
name|product
argument_list|,
name|FLOOR
argument_list|)
operator|+
literal|1
decl_stmt|;
name|int
name|bits
init|=
name|LongMath
operator|.
name|log2
argument_list|(
name|startingNumber
argument_list|,
name|FLOOR
argument_list|)
operator|+
literal|1
decl_stmt|;
comment|// Check for the next power of two boundary, to save us a CLZ operation.
name|int
name|nextPowerOfTwo
init|=
literal|1
operator|<<
operator|(
name|bits
operator|-
literal|1
operator|)
decl_stmt|;
comment|// Iteratively multiply the longs as big as they can go.
for|for
control|(
name|long
name|num
init|=
name|startingNumber
init|;
name|num
operator|<=
name|n
condition|;
name|num
operator|++
control|)
block|{
comment|// Check to see if the floor(log2(num)) + 1 has changed.
if|if
condition|(
operator|(
name|num
operator|&
name|nextPowerOfTwo
operator|)
operator|!=
literal|0
condition|)
block|{
name|nextPowerOfTwo
operator|<<=
literal|1
expr_stmt|;
name|bits
operator|++
expr_stmt|;
block|}
comment|// Get rid of the 2s in num.
name|int
name|tz
init|=
name|Long
operator|.
name|numberOfTrailingZeros
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|long
name|normalizedNum
init|=
name|num
operator|>>
name|tz
decl_stmt|;
name|shift
operator|+=
name|tz
expr_stmt|;
comment|// Adjust floor(log2(num)) + 1.
name|int
name|normalizedBits
init|=
name|bits
operator|-
name|tz
decl_stmt|;
comment|// If it won't fit in a long, then we store off the intermediate product.
if|if
condition|(
name|normalizedBits
operator|+
name|productBits
operator|>=
name|Long
operator|.
name|SIZE
condition|)
block|{
name|bignums
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|product
argument_list|)
argument_list|)
expr_stmt|;
name|product
operator|=
literal|1
expr_stmt|;
name|productBits
operator|=
literal|0
expr_stmt|;
block|}
name|product
operator|*=
name|normalizedNum
expr_stmt|;
name|productBits
operator|=
name|LongMath
operator|.
name|log2
argument_list|(
name|product
argument_list|,
name|FLOOR
argument_list|)
operator|+
literal|1
expr_stmt|;
block|}
comment|// Check for leftovers.
if|if
condition|(
name|product
operator|>
literal|1
condition|)
block|{
name|bignums
operator|.
name|add
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|product
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Efficiently multiply all the intermediate products together.
return|return
name|listProduct
argument_list|(
name|bignums
argument_list|)
operator|.
name|shiftLeft
argument_list|(
name|shift
argument_list|)
return|;
block|}
DECL|method|listProduct (List<BigInteger> nums)
specifier|static
name|BigInteger
name|listProduct
parameter_list|(
name|List
argument_list|<
name|BigInteger
argument_list|>
name|nums
parameter_list|)
block|{
return|return
name|listProduct
argument_list|(
name|nums
argument_list|,
literal|0
argument_list|,
name|nums
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
DECL|method|listProduct (List<BigInteger> nums, int start, int end)
specifier|static
name|BigInteger
name|listProduct
parameter_list|(
name|List
argument_list|<
name|BigInteger
argument_list|>
name|nums
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
switch|switch
condition|(
name|end
operator|-
name|start
condition|)
block|{
case|case
literal|0
case|:
return|return
name|BigInteger
operator|.
name|ONE
return|;
case|case
literal|1
case|:
return|return
name|nums
operator|.
name|get
argument_list|(
name|start
argument_list|)
return|;
case|case
literal|2
case|:
return|return
name|nums
operator|.
name|get
argument_list|(
name|start
argument_list|)
operator|.
name|multiply
argument_list|(
name|nums
operator|.
name|get
argument_list|(
name|start
operator|+
literal|1
argument_list|)
argument_list|)
return|;
case|case
literal|3
case|:
return|return
name|nums
operator|.
name|get
argument_list|(
name|start
argument_list|)
operator|.
name|multiply
argument_list|(
name|nums
operator|.
name|get
argument_list|(
name|start
operator|+
literal|1
argument_list|)
argument_list|)
operator|.
name|multiply
argument_list|(
name|nums
operator|.
name|get
argument_list|(
name|start
operator|+
literal|2
argument_list|)
argument_list|)
return|;
default|default:
comment|// Otherwise, split the list in half and recursively do this.
name|int
name|m
init|=
operator|(
name|end
operator|+
name|start
operator|)
operator|>>>
literal|1
decl_stmt|;
return|return
name|listProduct
argument_list|(
name|nums
argument_list|,
name|start
argument_list|,
name|m
argument_list|)
operator|.
name|multiply
argument_list|(
name|listProduct
argument_list|(
name|nums
argument_list|,
name|m
argument_list|,
name|end
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns {@code n} choose {@code k}, also known as the binomial coefficient of {@code n} and    * {@code k}, that is, {@code n! / (k! (n - k)!)}.    *    *<p><b>Warning:</b> the result can take as much as<i>O(k log n)</i> space.    *    * @throws IllegalArgumentException if {@code n< 0}, {@code k< 0}, or {@code k> n}    */
DECL|method|binomial (int n, int k)
specifier|public
specifier|static
name|BigInteger
name|binomial
parameter_list|(
name|int
name|n
parameter_list|,
name|int
name|k
parameter_list|)
block|{
name|checkNonNegative
argument_list|(
literal|"n"
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|checkNonNegative
argument_list|(
literal|"k"
argument_list|,
name|k
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|k
operator|<=
name|n
argument_list|,
literal|"k (%s)> n (%s)"
argument_list|,
name|k
argument_list|,
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|>
operator|(
name|n
operator|>>
literal|1
operator|)
condition|)
block|{
name|k
operator|=
name|n
operator|-
name|k
expr_stmt|;
block|}
if|if
condition|(
name|k
operator|<
name|LongMath
operator|.
name|biggestBinomials
operator|.
name|length
operator|&&
name|n
operator|<=
name|LongMath
operator|.
name|biggestBinomials
index|[
name|k
index|]
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|LongMath
operator|.
name|binomial
argument_list|(
name|n
argument_list|,
name|k
argument_list|)
argument_list|)
return|;
block|}
name|BigInteger
name|accum
init|=
name|BigInteger
operator|.
name|ONE
decl_stmt|;
name|long
name|numeratorAccum
init|=
name|n
decl_stmt|;
name|long
name|denominatorAccum
init|=
literal|1
decl_stmt|;
name|int
name|bits
init|=
name|LongMath
operator|.
name|log2
argument_list|(
name|n
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
decl_stmt|;
name|int
name|numeratorBits
init|=
name|bits
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|k
condition|;
name|i
operator|++
control|)
block|{
name|int
name|p
init|=
name|n
operator|-
name|i
decl_stmt|;
name|int
name|q
init|=
name|i
operator|+
literal|1
decl_stmt|;
comment|// log2(p)>= bits - 1, because p>= n/2
if|if
condition|(
name|numeratorBits
operator|+
name|bits
operator|>=
name|Long
operator|.
name|SIZE
operator|-
literal|1
condition|)
block|{
comment|// The numerator is as big as it can get without risking overflow.
comment|// Multiply numeratorAccum / denominatorAccum into accum.
name|accum
operator|=
name|accum
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|numeratorAccum
argument_list|)
argument_list|)
operator|.
name|divide
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|denominatorAccum
argument_list|)
argument_list|)
expr_stmt|;
name|numeratorAccum
operator|=
name|p
expr_stmt|;
name|denominatorAccum
operator|=
name|q
expr_stmt|;
name|numeratorBits
operator|=
name|bits
expr_stmt|;
block|}
else|else
block|{
comment|// We can definitely multiply into the long accumulators without overflowing them.
name|numeratorAccum
operator|*=
name|p
expr_stmt|;
name|denominatorAccum
operator|*=
name|q
expr_stmt|;
name|numeratorBits
operator|+=
name|bits
expr_stmt|;
block|}
block|}
return|return
name|accum
operator|.
name|multiply
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|numeratorAccum
argument_list|)
argument_list|)
operator|.
name|divide
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|denominatorAccum
argument_list|)
argument_list|)
return|;
block|}
comment|// Returns true if BigInteger.valueOf(x.longValue()).equals(x).
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|method|fitsInLong (BigInteger x)
specifier|static
name|boolean
name|fitsInLong
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
return|return
name|x
operator|.
name|bitLength
argument_list|()
operator|<=
name|Long
operator|.
name|SIZE
operator|-
literal|1
return|;
block|}
DECL|method|BigIntegerMath ()
specifier|private
name|BigIntegerMath
parameter_list|()
block|{}
block|}
end_class

end_unit

