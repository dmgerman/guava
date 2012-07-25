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
name|checkNoOverflow
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
name|lang
operator|.
name|Math
operator|.
name|abs
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Math
operator|.
name|min
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|Beta
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
comment|/**  * A class for arithmetic on values of type {@code int}. Where possible, methods are defined and  * named analogously to their {@code BigInteger} counterparts.  *  *<p>The implementations of many methods in this class are based on material from Henry S. Warren,  * Jr.'s<i>Hacker's Delight</i>, (Addison Wesley, 2002).  *  *<p>Similar functionality for {@code long} and for {@link BigInteger} can be found in  * {@link LongMath} and {@link BigIntegerMath} respectively.  For other common operations on  * {@code int} values, see {@link com.google.common.primitives.Ints}.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|IntMath
specifier|public
specifier|final
class|class
name|IntMath
block|{
comment|// NOTE: Whenever both tests are cheap and functional, it's faster to use&, | instead of&&, ||
comment|/**    * Returns {@code true} if {@code x} represents a power of two.    *    *<p>This differs from {@code Integer.bitCount(x) == 1}, because    * {@code Integer.bitCount(Integer.MIN_VALUE) == 1}, but {@link Integer#MIN_VALUE} is not a power    * of two.    */
DECL|method|isPowerOfTwo (int x)
specifier|public
specifier|static
name|boolean
name|isPowerOfTwo
parameter_list|(
name|int
name|x
parameter_list|)
block|{
return|return
name|x
operator|>
literal|0
operator|&
operator|(
name|x
operator|&
operator|(
name|x
operator|-
literal|1
operator|)
operator|)
operator|==
literal|0
return|;
block|}
comment|/**    * Returns the base-2 logarithm of {@code x}, rounded according to the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and {@code x}    *         is not a power of two    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|log2 (int x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log2
parameter_list|(
name|int
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
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|-
name|Integer
operator|.
name|numberOfLeadingZeros
argument_list|(
name|x
argument_list|)
return|;
case|case
name|UP
case|:
case|case
name|CEILING
case|:
return|return
name|Integer
operator|.
name|SIZE
operator|-
name|Integer
operator|.
name|numberOfLeadingZeros
argument_list|(
name|x
operator|-
literal|1
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
comment|// Since sqrt(2) is irrational, log2(x) - logFloor cannot be exactly 0.5
name|int
name|leadingZeros
init|=
name|Integer
operator|.
name|numberOfLeadingZeros
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|int
name|cmp
init|=
name|MAX_POWER_OF_SQRT2_UNSIGNED
operator|>>>
name|leadingZeros
decl_stmt|;
comment|// floor(2^(logFloor + 0.5))
name|int
name|logFloor
init|=
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|-
name|leadingZeros
decl_stmt|;
return|return
operator|(
name|x
operator|<=
name|cmp
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
comment|/** The biggest half power of two that can fit in an unsigned int. */
DECL|field|MAX_POWER_OF_SQRT2_UNSIGNED
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
name|MAX_POWER_OF_SQRT2_UNSIGNED
init|=
literal|0xB504F333
decl_stmt|;
comment|/**    * Returns the base-10 logarithm of {@code x}, rounded according to the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x<= 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and {@code x}    *         is not a power of ten    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"need BigIntegerMath to adequately test"
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|log10 (int x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log10
parameter_list|(
name|int
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
name|int
name|logFloor
init|=
name|log10Floor
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|int
name|floorPow
init|=
name|POWERS_OF_10
index|[
name|logFloor
index|]
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
name|x
operator|==
name|floorPow
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
name|logFloor
return|;
case|case
name|CEILING
case|:
case|case
name|UP
case|:
return|return
operator|(
name|x
operator|==
name|floorPow
operator|)
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
comment|// sqrt(10) is irrational, so log10(x) - logFloor is never exactly 0.5
return|return
operator|(
name|x
operator|<=
name|HALF_POWERS_OF_10
index|[
name|logFloor
index|]
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
DECL|method|log10Floor (int x)
specifier|private
specifier|static
name|int
name|log10Floor
parameter_list|(
name|int
name|x
parameter_list|)
block|{
comment|/*      * Based on Hacker's Delight Fig. 11-5, the two-table-lookup, branch-free implementation.      *      * The key idea is that based on the number of leading zeros (equivalently, floor(log2(x))),      * we can narrow the possible floor(log10(x)) values to two.  For example, if floor(log2(x))      * is 6, then 64<= x< 128, so floor(log10(x)) is either 1 or 2.      */
name|int
name|y
init|=
name|MAX_LOG10_FOR_LEADING_ZEROS
index|[
name|Integer
operator|.
name|numberOfLeadingZeros
argument_list|(
name|x
argument_list|)
index|]
decl_stmt|;
comment|// y is the higher of the two possible values of floor(log10(x))
name|int
name|sgn
init|=
operator|(
name|x
operator|-
name|POWERS_OF_10
index|[
name|y
index|]
operator|)
operator|>>>
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
decl_stmt|;
comment|/*      * sgn is the sign bit of x - 10^y; it is 1 if x< 10^y, and 0 otherwise. If x< 10^y, then we      * want the lower of the two possible values, or y - 1, otherwise, we want y.      */
return|return
name|y
operator|-
name|sgn
return|;
block|}
comment|// MAX_LOG10_FOR_LEADING_ZEROS[i] == floor(log10(2^(Long.SIZE - i)))
DECL|field|MAX_LOG10_FOR_LEADING_ZEROS
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|byte
index|[]
name|MAX_LOG10_FOR_LEADING_ZEROS
init|=
block|{
literal|9
block|,
literal|9
block|,
literal|9
block|,
literal|8
block|,
literal|8
block|,
literal|8
block|,
literal|7
block|,
literal|7
block|,
literal|7
block|,
literal|6
block|,
literal|6
block|,
literal|6
block|,
literal|6
block|,
literal|5
block|,
literal|5
block|,
literal|5
block|,
literal|4
block|,
literal|4
block|,
literal|4
block|,
literal|3
block|,
literal|3
block|,
literal|3
block|,
literal|3
block|,
literal|2
block|,
literal|2
block|,
literal|2
block|,
literal|1
block|,
literal|1
block|,
literal|1
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
decl_stmt|;
DECL|field|POWERS_OF_10
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
index|[]
name|POWERS_OF_10
init|=
block|{
literal|1
block|,
literal|10
block|,
literal|100
block|,
literal|1000
block|,
literal|10000
block|,
literal|100000
block|,
literal|1000000
block|,
literal|10000000
block|,
literal|100000000
block|,
literal|1000000000
block|}
decl_stmt|;
comment|// HALF_POWERS_OF_10[i] = largest int less than 10^(i + 0.5)
DECL|field|HALF_POWERS_OF_10
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
index|[]
name|HALF_POWERS_OF_10
init|=
block|{
literal|3
block|,
literal|31
block|,
literal|316
block|,
literal|3162
block|,
literal|31622
block|,
literal|316227
block|,
literal|3162277
block|,
literal|31622776
block|,
literal|316227766
block|,
name|Integer
operator|.
name|MAX_VALUE
block|}
decl_stmt|;
comment|/**    * Returns {@code b} to the {@code k}th power. Even if the result overflows, it will be equal to    * {@code BigInteger.valueOf(b).pow(k).intValue()}. This implementation runs in {@code O(log k)}    * time.    *    *<p>Compare {@link #checkedPow}, which throws an {@link ArithmeticException} upon overflow.    *    * @throws IllegalArgumentException if {@code k< 0}    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"failing tests"
argument_list|)
DECL|method|pow (int b, int k)
specifier|public
specifier|static
name|int
name|pow
parameter_list|(
name|int
name|b
parameter_list|,
name|int
name|k
parameter_list|)
block|{
name|checkNonNegative
argument_list|(
literal|"exponent"
argument_list|,
name|k
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|b
condition|)
block|{
case|case
literal|0
case|:
return|return
operator|(
name|k
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
literal|0
return|;
case|case
literal|1
case|:
return|return
literal|1
return|;
case|case
operator|(
operator|-
literal|1
operator|)
case|:
return|return
operator|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
operator|-
literal|1
return|;
case|case
literal|2
case|:
return|return
operator|(
name|k
operator|<
name|Integer
operator|.
name|SIZE
operator|)
condition|?
operator|(
literal|1
operator|<<
name|k
operator|)
else|:
literal|0
return|;
case|case
operator|(
operator|-
literal|2
operator|)
case|:
if|if
condition|(
name|k
operator|<
name|Integer
operator|.
name|SIZE
condition|)
block|{
return|return
operator|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|==
literal|0
operator|)
condition|?
operator|(
literal|1
operator|<<
name|k
operator|)
else|:
operator|-
operator|(
literal|1
operator|<<
name|k
operator|)
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
for|for
control|(
name|int
name|accum
init|=
literal|1
init|;
condition|;
name|k
operator|>>=
literal|1
control|)
block|{
switch|switch
condition|(
name|k
condition|)
block|{
case|case
literal|0
case|:
return|return
name|accum
return|;
case|case
literal|1
case|:
return|return
name|b
operator|*
name|accum
return|;
default|default:
name|accum
operator|*=
operator|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
name|b
expr_stmt|;
name|b
operator|*=
name|b
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Returns the square root of {@code x}, rounded with the specified rounding mode.    *    * @throws IllegalArgumentException if {@code x< 0}    * @throws ArithmeticException if {@code mode} is {@link RoundingMode#UNNECESSARY} and    *         {@code sqrt(x)} is not an integer    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"need BigIntegerMath to adequately test"
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|sqrt (int x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|sqrt
parameter_list|(
name|int
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
name|int
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
operator|*
name|sqrtFloor
operator|==
name|x
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
return|return
operator|(
name|sqrtFloor
operator|*
name|sqrtFloor
operator|==
name|x
operator|)
condition|?
name|sqrtFloor
else|:
name|sqrtFloor
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
name|int
name|halfSquare
init|=
name|sqrtFloor
operator|*
name|sqrtFloor
operator|+
name|sqrtFloor
decl_stmt|;
comment|/*          * We wish to test whether or not x<= (sqrtFloor + 0.5)^2 = halfSquare + 0.25.          * Since both x and halfSquare are integers, this is equivalent to testing whether or not          * x<= halfSquare.  (We have to deal with overflow, though.)          */
return|return
operator|(
name|x
operator|<=
name|halfSquare
operator||
name|halfSquare
operator|<
literal|0
operator|)
condition|?
name|sqrtFloor
else|:
name|sqrtFloor
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
DECL|method|sqrtFloor (int x)
specifier|private
specifier|static
name|int
name|sqrtFloor
parameter_list|(
name|int
name|x
parameter_list|)
block|{
comment|// There is no loss of precision in converting an int to a double, according to
comment|// http://java.sun.com/docs/books/jls/third_edition/html/conversions.html#5.1.2
return|return
operator|(
name|int
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
name|x
argument_list|)
return|;
block|}
comment|/**    * Returns the result of dividing {@code p} by {@code q}, rounding using the specified    * {@code RoundingMode}.    *    * @throws ArithmeticException if {@code q == 0}, or if {@code mode == UNNECESSARY} and {@code a}    *         is not an integer multiple of {@code b}    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|divide (int p, int q, RoundingMode mode)
specifier|public
specifier|static
name|int
name|divide
parameter_list|(
name|int
name|p
parameter_list|,
name|int
name|q
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|mode
argument_list|)
expr_stmt|;
if|if
condition|(
name|q
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"/ by zero"
argument_list|)
throw|;
comment|// for GWT
block|}
name|int
name|div
init|=
name|p
operator|/
name|q
decl_stmt|;
name|int
name|rem
init|=
name|p
operator|-
name|q
operator|*
name|div
decl_stmt|;
comment|// equal to p % q
if|if
condition|(
name|rem
operator|==
literal|0
condition|)
block|{
return|return
name|div
return|;
block|}
comment|/*      * Normal Java division rounds towards 0, consistently with RoundingMode.DOWN. We just have to      * deal with the cases where rounding towards 0 is wrong, which typically depends on the sign of      * p / q.      *      * signum is 1 if p and q are both nonnegative or both negative, and -1 otherwise.      */
name|int
name|signum
init|=
literal|1
operator||
operator|(
operator|(
name|p
operator|^
name|q
operator|)
operator|>>
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|)
decl_stmt|;
name|boolean
name|increment
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
name|rem
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// fall through
case|case
name|DOWN
case|:
name|increment
operator|=
literal|false
expr_stmt|;
break|break;
case|case
name|UP
case|:
name|increment
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|CEILING
case|:
name|increment
operator|=
name|signum
operator|>
literal|0
expr_stmt|;
break|break;
case|case
name|FLOOR
case|:
name|increment
operator|=
name|signum
operator|<
literal|0
expr_stmt|;
break|break;
case|case
name|HALF_EVEN
case|:
case|case
name|HALF_DOWN
case|:
case|case
name|HALF_UP
case|:
name|int
name|absRem
init|=
name|abs
argument_list|(
name|rem
argument_list|)
decl_stmt|;
name|int
name|cmpRemToHalfDivisor
init|=
name|absRem
operator|-
operator|(
name|abs
argument_list|(
name|q
argument_list|)
operator|-
name|absRem
operator|)
decl_stmt|;
comment|// subtracting two nonnegative ints can't overflow
comment|// cmpRemToHalfDivisor has the same sign as compare(abs(rem), abs(q) / 2).
if|if
condition|(
name|cmpRemToHalfDivisor
operator|==
literal|0
condition|)
block|{
comment|// exactly on the half mark
name|increment
operator|=
operator|(
name|mode
operator|==
name|HALF_UP
operator|||
operator|(
name|mode
operator|==
name|HALF_EVEN
operator|&
operator|(
name|div
operator|&
literal|1
operator|)
operator|!=
literal|0
operator|)
operator|)
expr_stmt|;
block|}
else|else
block|{
name|increment
operator|=
name|cmpRemToHalfDivisor
operator|>
literal|0
expr_stmt|;
comment|// closer to the UP value
block|}
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
return|return
name|increment
condition|?
name|div
operator|+
name|signum
else|:
name|div
return|;
block|}
comment|/**    * Returns {@code x mod m}. This differs from {@code x % m} in that it always returns a    * non-negative result.    *    *<p>For example:<pre> {@code    *    * mod(7, 4) == 3    * mod(-7, 4) == 1    * mod(-1, 4) == 3    * mod(-8, 4) == 0    * mod(8, 4) == 0}</pre>    *    * @throws ArithmeticException if {@code m<= 0}    */
DECL|method|mod (int x, int m)
specifier|public
specifier|static
name|int
name|mod
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|m
parameter_list|)
block|{
if|if
condition|(
name|m
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"Modulus "
operator|+
name|m
operator|+
literal|" must be> 0"
argument_list|)
throw|;
block|}
name|int
name|result
init|=
name|x
operator|%
name|m
decl_stmt|;
return|return
operator|(
name|result
operator|>=
literal|0
operator|)
condition|?
name|result
else|:
name|result
operator|+
name|m
return|;
block|}
comment|/**    * Returns the greatest common divisor of {@code a, b}. Returns {@code 0} if    * {@code a == 0&& b == 0}.    *    * @throws IllegalArgumentException if {@code a< 0} or {@code b< 0}    */
DECL|method|gcd (int a, int b)
specifier|public
specifier|static
name|int
name|gcd
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
comment|/*      * The reason we require both arguments to be>= 0 is because otherwise, what do you return on      * gcd(0, Integer.MIN_VALUE)? BigInteger.gcd would return positive 2^31, but positive 2^31      * isn't an int.      */
name|checkNonNegative
argument_list|(
literal|"a"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|checkNonNegative
argument_list|(
literal|"b"
argument_list|,
name|b
argument_list|)
expr_stmt|;
if|if
condition|(
name|a
operator|==
literal|0
condition|)
block|{
comment|// 0 % b == 0, so b divides a, but the converse doesn't hold.
comment|// BigInteger.gcd is consistent with this decision.
return|return
name|b
return|;
block|}
elseif|else
if|if
condition|(
name|b
operator|==
literal|0
condition|)
block|{
return|return
name|a
return|;
comment|// similar logic
block|}
comment|/*      * Uses the binary GCD algorithm; see http://en.wikipedia.org/wiki/Binary_GCD_algorithm.      * This is>40% faster than the Euclidean algorithm in benchmarks.      */
name|int
name|aTwos
init|=
name|Integer
operator|.
name|numberOfTrailingZeros
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|a
operator|>>=
name|aTwos
expr_stmt|;
comment|// divide out all 2s
name|int
name|bTwos
init|=
name|Integer
operator|.
name|numberOfTrailingZeros
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|b
operator|>>=
name|bTwos
expr_stmt|;
comment|// divide out all 2s
while|while
condition|(
name|a
operator|!=
name|b
condition|)
block|{
comment|// both a, b are odd
comment|// The key to the binary GCD algorithm is as follows:
comment|// Both a and b are odd.  Assume a> b; then gcd(a - b, b) = gcd(a, b).
comment|// But in gcd(a - b, b), a - b is even and b is odd, so we can divide out powers of two.
comment|// We bend over backwards to avoid branching, adapting a technique from
comment|// http://graphics.stanford.edu/~seander/bithacks.html#IntegerMinOrMax
name|int
name|delta
init|=
name|a
operator|-
name|b
decl_stmt|;
comment|// can't overflow, since a and b are nonnegative
name|int
name|minDeltaOrZero
init|=
name|delta
operator|&
operator|(
name|delta
operator|>>
operator|(
name|Integer
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|)
decl_stmt|;
comment|// equivalent to Math.min(delta, 0)
name|a
operator|=
name|delta
operator|-
name|minDeltaOrZero
operator|-
name|minDeltaOrZero
expr_stmt|;
comment|// sets a to Math.abs(a - b)
comment|// a is now nonnegative and even
name|b
operator|+=
name|minDeltaOrZero
expr_stmt|;
comment|// sets b to min(old a, b)
name|a
operator|>>=
name|Integer
operator|.
name|numberOfTrailingZeros
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// divide out all 2s, since 2 doesn't divide b
block|}
return|return
name|a
operator|<<
name|min
argument_list|(
name|aTwos
argument_list|,
name|bTwos
argument_list|)
return|;
block|}
comment|/**    * Returns the sum of {@code a} and {@code b}, provided it does not overflow.    *    * @throws ArithmeticException if {@code a + b} overflows in signed {@code int} arithmetic    */
DECL|method|checkedAdd (int a, int b)
specifier|public
specifier|static
name|int
name|checkedAdd
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|long
name|result
init|=
operator|(
name|long
operator|)
name|a
operator|+
name|b
decl_stmt|;
name|checkNoOverflow
argument_list|(
name|result
operator|==
operator|(
name|int
operator|)
name|result
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|result
return|;
block|}
comment|/**    * Returns the difference of {@code a} and {@code b}, provided it does not overflow.    *    * @throws ArithmeticException if {@code a - b} overflows in signed {@code int} arithmetic    */
DECL|method|checkedSubtract (int a, int b)
specifier|public
specifier|static
name|int
name|checkedSubtract
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|long
name|result
init|=
operator|(
name|long
operator|)
name|a
operator|-
name|b
decl_stmt|;
name|checkNoOverflow
argument_list|(
name|result
operator|==
operator|(
name|int
operator|)
name|result
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|result
return|;
block|}
comment|/**    * Returns the product of {@code a} and {@code b}, provided it does not overflow.    *    * @throws ArithmeticException if {@code a * b} overflows in signed {@code int} arithmetic    */
DECL|method|checkedMultiply (int a, int b)
specifier|public
specifier|static
name|int
name|checkedMultiply
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
name|long
name|result
init|=
operator|(
name|long
operator|)
name|a
operator|*
name|b
decl_stmt|;
name|checkNoOverflow
argument_list|(
name|result
operator|==
operator|(
name|int
operator|)
name|result
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|result
return|;
block|}
comment|/**    * Returns the {@code b} to the {@code k}th power, provided it does not overflow.    *    *<p>{@link #pow} may be faster, but does not check for overflow.    *    * @throws ArithmeticException if {@code b} to the {@code k}th power overflows in signed    *         {@code int} arithmetic    */
DECL|method|checkedPow (int b, int k)
specifier|public
specifier|static
name|int
name|checkedPow
parameter_list|(
name|int
name|b
parameter_list|,
name|int
name|k
parameter_list|)
block|{
name|checkNonNegative
argument_list|(
literal|"exponent"
argument_list|,
name|k
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|b
condition|)
block|{
case|case
literal|0
case|:
return|return
operator|(
name|k
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
literal|0
return|;
case|case
literal|1
case|:
return|return
literal|1
return|;
case|case
operator|(
operator|-
literal|1
operator|)
case|:
return|return
operator|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
operator|-
literal|1
return|;
case|case
literal|2
case|:
name|checkNoOverflow
argument_list|(
name|k
operator|<
name|Integer
operator|.
name|SIZE
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
literal|1
operator|<<
name|k
return|;
case|case
operator|(
operator|-
literal|2
operator|)
case|:
name|checkNoOverflow
argument_list|(
name|k
operator|<
name|Integer
operator|.
name|SIZE
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|==
literal|0
operator|)
condition|?
literal|1
operator|<<
name|k
else|:
operator|-
literal|1
operator|<<
name|k
return|;
block|}
name|int
name|accum
init|=
literal|1
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
switch|switch
condition|(
name|k
condition|)
block|{
case|case
literal|0
case|:
return|return
name|accum
return|;
case|case
literal|1
case|:
return|return
name|checkedMultiply
argument_list|(
name|accum
argument_list|,
name|b
argument_list|)
return|;
default|default:
if|if
condition|(
operator|(
name|k
operator|&
literal|1
operator|)
operator|!=
literal|0
condition|)
block|{
name|accum
operator|=
name|checkedMultiply
argument_list|(
name|accum
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
name|k
operator|>>=
literal|1
expr_stmt|;
if|if
condition|(
name|k
operator|>
literal|0
condition|)
block|{
name|checkNoOverflow
argument_list|(
operator|-
name|FLOOR_SQRT_MAX_INT
operator|<=
name|b
operator|&
name|b
operator|<=
name|FLOOR_SQRT_MAX_INT
argument_list|)
expr_stmt|;
name|b
operator|*=
name|b
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|field|FLOOR_SQRT_MAX_INT
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
name|FLOOR_SQRT_MAX_INT
init|=
literal|46340
decl_stmt|;
comment|/**    * Returns {@code n!}, that is, the product of the first {@code n} positive    * integers, {@code 1} if {@code n == 0}, or {@link Integer#MAX_VALUE} if the    * result does not fit in a {@code int}.    *    * @throws IllegalArgumentException if {@code n< 0}    */
DECL|method|factorial (int n)
specifier|public
specifier|static
name|int
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
return|return
operator|(
name|n
operator|<
name|FACTORIALS
operator|.
name|length
operator|)
condition|?
name|FACTORIALS
index|[
name|n
index|]
else|:
name|Integer
operator|.
name|MAX_VALUE
return|;
block|}
DECL|field|FACTORIALS
specifier|static
specifier|final
name|int
index|[]
name|FACTORIALS
init|=
block|{
literal|1
block|,
literal|1
block|,
literal|1
operator|*
literal|2
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
operator|*
literal|8
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
operator|*
literal|8
operator|*
literal|9
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
operator|*
literal|8
operator|*
literal|9
operator|*
literal|10
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
operator|*
literal|8
operator|*
literal|9
operator|*
literal|10
operator|*
literal|11
block|,
literal|1
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
operator|*
literal|6
operator|*
literal|7
operator|*
literal|8
operator|*
literal|9
operator|*
literal|10
operator|*
literal|11
operator|*
literal|12
block|}
decl_stmt|;
comment|/**    * Returns {@code n} choose {@code k}, also known as the binomial coefficient of {@code n} and    * {@code k}, or {@link Integer#MAX_VALUE} if the result does not fit in an {@code int}.    *    * @throws IllegalArgumentException if {@code n< 0}, {@code k< 0} or {@code k> n}    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"need BigIntegerMath to adequately test"
argument_list|)
DECL|method|binomial (int n, int k)
specifier|public
specifier|static
name|int
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
operator|>=
name|BIGGEST_BINOMIALS
operator|.
name|length
operator|||
name|n
operator|>
name|BIGGEST_BINOMIALS
index|[
name|k
index|]
condition|)
block|{
return|return
name|Integer
operator|.
name|MAX_VALUE
return|;
block|}
switch|switch
condition|(
name|k
condition|)
block|{
case|case
literal|0
case|:
return|return
literal|1
return|;
case|case
literal|1
case|:
return|return
name|n
return|;
default|default:
name|long
name|result
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|k
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|*=
name|n
operator|-
name|i
expr_stmt|;
name|result
operator|/=
name|i
operator|+
literal|1
expr_stmt|;
block|}
return|return
operator|(
name|int
operator|)
name|result
return|;
block|}
block|}
comment|// binomial(BIGGEST_BINOMIALS[k], k) fits in an int, but not binomial(BIGGEST_BINOMIALS[k]+1,k).
DECL|field|BIGGEST_BINOMIALS
annotation|@
name|VisibleForTesting
specifier|static
name|int
index|[]
name|BIGGEST_BINOMIALS
init|=
block|{
name|Integer
operator|.
name|MAX_VALUE
block|,
name|Integer
operator|.
name|MAX_VALUE
block|,
literal|65536
block|,
literal|2345
block|,
literal|477
block|,
literal|193
block|,
literal|110
block|,
literal|75
block|,
literal|58
block|,
literal|49
block|,
literal|43
block|,
literal|39
block|,
literal|37
block|,
literal|35
block|,
literal|34
block|,
literal|34
block|,
literal|33
block|}
decl_stmt|;
comment|/**    * Returns the arithmetic mean of {@code x} and {@code y}, rounded towards    * negative infinity. This method is overflow resilient.    *    * @since 14.0    */
DECL|method|mean (int x, int y)
specifier|public
specifier|static
name|int
name|mean
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|)
block|{
comment|// Efficient method for computing the arithmetic mean.
comment|// The alternative (x + y) / 2 fails for large values.
comment|// The alternative (x + y)>>> 1 fails for negative values.
return|return
operator|(
name|x
operator|&
name|y
operator|)
operator|+
operator|(
operator|(
name|x
operator|^
name|y
operator|)
operator|>>
literal|1
operator|)
return|;
block|}
DECL|method|IntMath ()
specifier|private
name|IntMath
parameter_list|()
block|{}
block|}
end_class

end_unit

