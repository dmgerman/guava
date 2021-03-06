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
name|math
operator|.
name|DoubleUtils
operator|.
name|IMPLICIT_BIT
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
name|DoubleUtils
operator|.
name|SIGNIFICAND_BITS
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
name|DoubleUtils
operator|.
name|getSignificand
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
name|DoubleUtils
operator|.
name|isFinite
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
name|DoubleUtils
operator|.
name|isNormal
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
name|DoubleUtils
operator|.
name|scaleNormalize
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
name|checkInRangeForRoundingInputs
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
name|copySign
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
name|getExponent
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
name|log
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
name|rint
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Booleans
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A class for arithmetic on doubles that is not covered by {@link java.lang.Math}.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|DoubleMath
specifier|public
specifier|final
class|class
name|DoubleMath
block|{
comment|/*    * This method returns a value y such that rounding y DOWN (towards zero) gives the same result as    * rounding x according to the specified mode.    */
annotation|@
name|GwtIncompatible
comment|// #isMathematicalInteger, com.google.common.math.DoubleUtils
DECL|method|roundIntermediate (double x, RoundingMode mode)
specifier|static
name|double
name|roundIntermediate
parameter_list|(
name|double
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isFinite
argument_list|(
name|x
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArithmeticException
argument_list|(
literal|"input is infinite or NaN"
argument_list|)
throw|;
block|}
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
name|isMathematicalInteger
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|x
return|;
case|case
name|FLOOR
case|:
if|if
condition|(
name|x
operator|>=
literal|0.0
operator|||
name|isMathematicalInteger
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|x
return|;
block|}
else|else
block|{
return|return
operator|(
name|long
operator|)
name|x
operator|-
literal|1
return|;
block|}
case|case
name|CEILING
case|:
if|if
condition|(
name|x
operator|<=
literal|0.0
operator|||
name|isMathematicalInteger
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|x
return|;
block|}
else|else
block|{
return|return
operator|(
name|long
operator|)
name|x
operator|+
literal|1
return|;
block|}
case|case
name|DOWN
case|:
return|return
name|x
return|;
case|case
name|UP
case|:
if|if
condition|(
name|isMathematicalInteger
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|x
return|;
block|}
else|else
block|{
return|return
operator|(
name|long
operator|)
name|x
operator|+
operator|(
name|x
operator|>
literal|0
condition|?
literal|1
else|:
operator|-
literal|1
operator|)
return|;
block|}
case|case
name|HALF_EVEN
case|:
return|return
name|rint
argument_list|(
name|x
argument_list|)
return|;
case|case
name|HALF_UP
case|:
block|{
name|double
name|z
init|=
name|rint
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|abs
argument_list|(
name|x
operator|-
name|z
argument_list|)
operator|==
literal|0.5
condition|)
block|{
return|return
name|x
operator|+
name|copySign
argument_list|(
literal|0.5
argument_list|,
name|x
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|z
return|;
block|}
block|}
case|case
name|HALF_DOWN
case|:
block|{
name|double
name|z
init|=
name|rint
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|abs
argument_list|(
name|x
operator|-
name|z
argument_list|)
operator|==
literal|0.5
condition|)
block|{
return|return
name|x
return|;
block|}
else|else
block|{
return|return
name|z
return|;
block|}
block|}
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/**    * Returns the {@code int} value that is equal to {@code x} rounded with the specified rounding    * mode, if possible.    *    * @throws ArithmeticException if    *<ul>    *<li>{@code x} is infinite or NaN    *<li>{@code x}, after being rounded to a mathematical integer using the specified rounding    *           mode, is either less than {@code Integer.MIN_VALUE} or greater than {@code    *           Integer.MAX_VALUE}    *<li>{@code x} is not a mathematical integer and {@code mode} is {@link    *           RoundingMode#UNNECESSARY}    *</ul>    */
annotation|@
name|GwtIncompatible
comment|// #roundIntermediate
DECL|method|roundToInt (double x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|roundToInt
parameter_list|(
name|double
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|double
name|z
init|=
name|roundIntermediate
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|checkInRangeForRoundingInputs
argument_list|(
name|z
operator|>
name|MIN_INT_AS_DOUBLE
operator|-
literal|1.0
operator|&
name|z
operator|<
name|MAX_INT_AS_DOUBLE
operator|+
literal|1.0
argument_list|,
name|x
argument_list|,
name|mode
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|z
return|;
block|}
DECL|field|MIN_INT_AS_DOUBLE
specifier|private
specifier|static
specifier|final
name|double
name|MIN_INT_AS_DOUBLE
init|=
operator|-
literal|0x1p31
decl_stmt|;
DECL|field|MAX_INT_AS_DOUBLE
specifier|private
specifier|static
specifier|final
name|double
name|MAX_INT_AS_DOUBLE
init|=
literal|0x1p31
operator|-
literal|1.0
decl_stmt|;
comment|/**    * Returns the {@code long} value that is equal to {@code x} rounded with the specified rounding    * mode, if possible.    *    * @throws ArithmeticException if    *<ul>    *<li>{@code x} is infinite or NaN    *<li>{@code x}, after being rounded to a mathematical integer using the specified rounding    *           mode, is either less than {@code Long.MIN_VALUE} or greater than {@code    *           Long.MAX_VALUE}    *<li>{@code x} is not a mathematical integer and {@code mode} is {@link    *           RoundingMode#UNNECESSARY}    *</ul>    */
annotation|@
name|GwtIncompatible
comment|// #roundIntermediate
DECL|method|roundToLong (double x, RoundingMode mode)
specifier|public
specifier|static
name|long
name|roundToLong
parameter_list|(
name|double
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|double
name|z
init|=
name|roundIntermediate
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
decl_stmt|;
name|checkInRangeForRoundingInputs
argument_list|(
name|MIN_LONG_AS_DOUBLE
operator|-
name|z
operator|<
literal|1.0
operator|&
name|z
operator|<
name|MAX_LONG_AS_DOUBLE_PLUS_ONE
argument_list|,
name|x
argument_list|,
name|mode
argument_list|)
expr_stmt|;
return|return
operator|(
name|long
operator|)
name|z
return|;
block|}
DECL|field|MIN_LONG_AS_DOUBLE
specifier|private
specifier|static
specifier|final
name|double
name|MIN_LONG_AS_DOUBLE
init|=
operator|-
literal|0x1p63
decl_stmt|;
comment|/*    * We cannot store Long.MAX_VALUE as a double without losing precision. Instead, we store    * Long.MAX_VALUE + 1 == -Long.MIN_VALUE, and then offset all comparisons by 1.    */
DECL|field|MAX_LONG_AS_DOUBLE_PLUS_ONE
specifier|private
specifier|static
specifier|final
name|double
name|MAX_LONG_AS_DOUBLE_PLUS_ONE
init|=
literal|0x1p63
decl_stmt|;
comment|/**    * Returns the {@code BigInteger} value that is equal to {@code x} rounded with the specified    * rounding mode, if possible.    *    * @throws ArithmeticException if    *<ul>    *<li>{@code x} is infinite or NaN    *<li>{@code x} is not a mathematical integer and {@code mode} is {@link    *           RoundingMode#UNNECESSARY}    *</ul>    */
comment|// #roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils
annotation|@
name|GwtIncompatible
DECL|method|roundToBigInteger (double x, RoundingMode mode)
specifier|public
specifier|static
name|BigInteger
name|roundToBigInteger
parameter_list|(
name|double
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|x
operator|=
name|roundIntermediate
argument_list|(
name|x
argument_list|,
name|mode
argument_list|)
expr_stmt|;
if|if
condition|(
name|MIN_LONG_AS_DOUBLE
operator|-
name|x
operator|<
literal|1.0
operator|&
name|x
operator|<
name|MAX_LONG_AS_DOUBLE_PLUS_ONE
condition|)
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
operator|(
name|long
operator|)
name|x
argument_list|)
return|;
block|}
name|int
name|exponent
init|=
name|getExponent
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|long
name|significand
init|=
name|getSignificand
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|BigInteger
name|result
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|significand
argument_list|)
operator|.
name|shiftLeft
argument_list|(
name|exponent
operator|-
name|SIGNIFICAND_BITS
argument_list|)
decl_stmt|;
return|return
operator|(
name|x
operator|<
literal|0
operator|)
condition|?
name|result
operator|.
name|negate
argument_list|()
else|:
name|result
return|;
block|}
comment|/**    * Returns {@code true} if {@code x} is exactly equal to {@code 2^k} for some finite integer    * {@code k}.    */
annotation|@
name|GwtIncompatible
comment|// com.google.common.math.DoubleUtils
DECL|method|isPowerOfTwo (double x)
specifier|public
specifier|static
name|boolean
name|isPowerOfTwo
parameter_list|(
name|double
name|x
parameter_list|)
block|{
if|if
condition|(
name|x
operator|>
literal|0.0
operator|&&
name|isFinite
argument_list|(
name|x
argument_list|)
condition|)
block|{
name|long
name|significand
init|=
name|getSignificand
argument_list|(
name|x
argument_list|)
decl_stmt|;
return|return
operator|(
name|significand
operator|&
operator|(
name|significand
operator|-
literal|1
operator|)
operator|)
operator|==
literal|0
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns the base 2 logarithm of a double value.    *    *<p>Special cases:    *    *<ul>    *<li>If {@code x} is NaN or less than zero, the result is NaN.    *<li>If {@code x} is positive infinity, the result is positive infinity.    *<li>If {@code x} is positive or negative zero, the result is negative infinity.    *</ul>    *    *<p>The computed result is within 1 ulp of the exact result.    *    *<p>If the result of this method will be immediately rounded to an {@code int}, {@link    * #log2(double, RoundingMode)} is faster.    */
DECL|method|log2 (double x)
specifier|public
specifier|static
name|double
name|log2
parameter_list|(
name|double
name|x
parameter_list|)
block|{
return|return
name|log
argument_list|(
name|x
argument_list|)
operator|/
name|LN_2
return|;
comment|// surprisingly within 1 ulp according to tests
block|}
comment|/**    * Returns the base 2 logarithm of a double value, rounded with the specified rounding mode to an    * {@code int}.    *    *<p>Regardless of the rounding mode, this is faster than {@code (int) log2(x)}.    *    * @throws IllegalArgumentException if {@code x<= 0.0}, {@code x} is NaN, or {@code x} is    *     infinite    */
annotation|@
name|GwtIncompatible
comment|// java.lang.Math.getExponent, com.google.common.math.DoubleUtils
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|log2 (double x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log2
parameter_list|(
name|double
name|x
parameter_list|,
name|RoundingMode
name|mode
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|x
operator|>
literal|0.0
operator|&&
name|isFinite
argument_list|(
name|x
argument_list|)
argument_list|,
literal|"x must be positive and finite"
argument_list|)
expr_stmt|;
name|int
name|exponent
init|=
name|getExponent
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNormal
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|log2
argument_list|(
name|x
operator|*
name|IMPLICIT_BIT
argument_list|,
name|mode
argument_list|)
operator|-
name|SIGNIFICAND_BITS
return|;
comment|// Do the calculation on a normal value.
block|}
comment|// x is positive, finite, and normal
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
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
comment|// fall through
case|case
name|FLOOR
case|:
name|increment
operator|=
literal|false
expr_stmt|;
break|break;
case|case
name|CEILING
case|:
name|increment
operator|=
operator|!
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
expr_stmt|;
break|break;
case|case
name|DOWN
case|:
name|increment
operator|=
name|exponent
operator|<
literal|0
operator|&
operator|!
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
expr_stmt|;
break|break;
case|case
name|UP
case|:
name|increment
operator|=
name|exponent
operator|>=
literal|0
operator|&
operator|!
name|isPowerOfTwo
argument_list|(
name|x
argument_list|)
expr_stmt|;
break|break;
case|case
name|HALF_DOWN
case|:
case|case
name|HALF_EVEN
case|:
case|case
name|HALF_UP
case|:
name|double
name|xScaled
init|=
name|scaleNormalize
argument_list|(
name|x
argument_list|)
decl_stmt|;
comment|// sqrt(2) is irrational, and the spec is relative to the "exact numerical result,"
comment|// so log2(x) is never exactly exponent + 0.5.
name|increment
operator|=
operator|(
name|xScaled
operator|*
name|xScaled
operator|)
operator|>
literal|2.0
expr_stmt|;
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
name|exponent
operator|+
literal|1
else|:
name|exponent
return|;
block|}
DECL|field|LN_2
specifier|private
specifier|static
specifier|final
name|double
name|LN_2
init|=
name|log
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|/**    * Returns {@code true} if {@code x} represents a mathematical integer.    *    *<p>This is equivalent to, but not necessarily implemented as, the expression {@code    * !Double.isNaN(x)&& !Double.isInfinite(x)&& x == Math.rint(x)}.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.Math.getExponent, com.google.common.math.DoubleUtils
DECL|method|isMathematicalInteger (double x)
specifier|public
specifier|static
name|boolean
name|isMathematicalInteger
parameter_list|(
name|double
name|x
parameter_list|)
block|{
return|return
name|isFinite
argument_list|(
name|x
argument_list|)
operator|&&
operator|(
name|x
operator|==
literal|0.0
operator|||
name|SIGNIFICAND_BITS
operator|-
name|Long
operator|.
name|numberOfTrailingZeros
argument_list|(
name|getSignificand
argument_list|(
name|x
argument_list|)
argument_list|)
operator|<=
name|getExponent
argument_list|(
name|x
argument_list|)
operator|)
return|;
block|}
comment|/**    * Returns {@code n!}, that is, the product of the first {@code n} positive integers, {@code 1} if    * {@code n == 0}, or {@code n!}, or {@link Double#POSITIVE_INFINITY} if {@code n!>    * Double.MAX_VALUE}.    *    *<p>The result is within 1 ulp of the true value.    *    * @throws IllegalArgumentException if {@code n< 0}    */
DECL|method|factorial (int n)
specifier|public
specifier|static
name|double
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
if|if
condition|(
name|n
operator|>
name|MAX_FACTORIAL
condition|)
block|{
return|return
name|Double
operator|.
name|POSITIVE_INFINITY
return|;
block|}
else|else
block|{
comment|// Multiplying the last (n& 0xf) values into their own accumulator gives a more accurate
comment|// result than multiplying by everySixteenthFactorial[n>> 4] directly.
name|double
name|accum
init|=
literal|1.0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
operator|+
operator|(
name|n
operator|&
operator|~
literal|0xf
operator|)
init|;
name|i
operator|<=
name|n
condition|;
name|i
operator|++
control|)
block|{
name|accum
operator|*=
name|i
expr_stmt|;
block|}
return|return
name|accum
operator|*
name|everySixteenthFactorial
index|[
name|n
operator|>>
literal|4
index|]
return|;
block|}
block|}
DECL|field|MAX_FACTORIAL
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
name|MAX_FACTORIAL
init|=
literal|170
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|field|everySixteenthFactorial
specifier|static
specifier|final
name|double
index|[]
name|everySixteenthFactorial
init|=
block|{
literal|0x1
literal|.0p0
block|,
literal|0x1
literal|.30777758p44
block|,
literal|0x1
literal|.956ad0aae33a4p117
block|,
literal|0x1
operator|.
name|ee69a78d72cb6p202
block|,
literal|0x1
operator|.
name|fe478ee34844ap295
block|,
literal|0x1
operator|.
name|c619094edabffp394
block|,
literal|0x1
literal|.3638dd7bd6347p498
block|,
literal|0x1
literal|.7cac197cfe503p605
block|,
literal|0x1
literal|.1e5dfc140e1e5p716
block|,
literal|0x1
literal|.8ce85fadb707ep829
block|,
literal|0x1
literal|.95d5f3d928edep945
block|}
decl_stmt|;
comment|/**    * Returns {@code true} if {@code a} and {@code b} are within {@code tolerance} of each other.    *    *<p>Technically speaking, this is equivalent to {@code Math.abs(a - b)<= tolerance ||    * Double.valueOf(a).equals(Double.valueOf(b))}.    *    *<p>Notable special cases include:    *    *<ul>    *<li>All NaNs are fuzzily equal.    *<li>If {@code a == b}, then {@code a} and {@code b} are always fuzzily equal.    *<li>Positive and negative zero are always fuzzily equal.    *<li>If {@code tolerance} is zero, and neither {@code a} nor {@code b} is NaN, then {@code a}    *       and {@code b} are fuzzily equal if and only if {@code a == b}.    *<li>With {@link Double#POSITIVE_INFINITY} tolerance, all non-NaN values are fuzzily equal.    *<li>With finite tolerance, {@code Double.POSITIVE_INFINITY} and {@code    *       Double.NEGATIVE_INFINITY} are fuzzily equal only to themselves.    *</ul>    *    *<p>This is reflexive and symmetric, but<em>not</em> transitive, so it is<em>not</em> an    * equivalence relation and<em>not</em> suitable for use in {@link Object#equals}    * implementations.    *    * @throws IllegalArgumentException if {@code tolerance} is {@code< 0} or NaN    * @since 13.0    */
DECL|method|fuzzyEquals (double a, double b, double tolerance)
specifier|public
specifier|static
name|boolean
name|fuzzyEquals
parameter_list|(
name|double
name|a
parameter_list|,
name|double
name|b
parameter_list|,
name|double
name|tolerance
parameter_list|)
block|{
name|MathPreconditions
operator|.
name|checkNonNegative
argument_list|(
literal|"tolerance"
argument_list|,
name|tolerance
argument_list|)
expr_stmt|;
return|return
name|Math
operator|.
name|copySign
argument_list|(
name|a
operator|-
name|b
argument_list|,
literal|1.0
argument_list|)
operator|<=
name|tolerance
comment|// copySign(x, 1.0) is a branch-free version of abs(x), but with different NaN semantics
operator|||
operator|(
name|a
operator|==
name|b
operator|)
comment|// needed to ensure that infinities equal themselves
operator|||
operator|(
name|Double
operator|.
name|isNaN
argument_list|(
name|a
argument_list|)
operator|&&
name|Double
operator|.
name|isNaN
argument_list|(
name|b
argument_list|)
operator|)
return|;
block|}
comment|/**    * Compares {@code a} and {@code b} "fuzzily," with a tolerance for nearly-equal values.    *    *<p>This method is equivalent to {@code fuzzyEquals(a, b, tolerance) ? 0 : Double.compare(a,    * b)}. In particular, like {@link Double#compare(double, double)}, it treats all NaN values as    * equal and greater than all other values (including {@link Double#POSITIVE_INFINITY}).    *    *<p>This is<em>not</em> a total ordering and is<em>not</em> suitable for use in {@link    * Comparable#compareTo} implementations. In particular, it is not transitive.    *    * @throws IllegalArgumentException if {@code tolerance} is {@code< 0} or NaN    * @since 13.0    */
DECL|method|fuzzyCompare (double a, double b, double tolerance)
specifier|public
specifier|static
name|int
name|fuzzyCompare
parameter_list|(
name|double
name|a
parameter_list|,
name|double
name|b
parameter_list|,
name|double
name|tolerance
parameter_list|)
block|{
if|if
condition|(
name|fuzzyEquals
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|tolerance
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
elseif|else
if|if
condition|(
name|a
operator|<
name|b
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
elseif|else
if|if
condition|(
name|a
operator|>
name|b
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|Booleans
operator|.
name|compare
argument_list|(
name|Double
operator|.
name|isNaN
argument_list|(
name|a
argument_list|)
argument_list|,
name|Double
operator|.
name|isNaN
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of    * {@code values}.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    * @param values a nonempty series of values    * @throws IllegalArgumentException if {@code values} is empty or contains any non-finite value    * @deprecated Use {@link Stats#meanOf} instead, noting the less strict handling of non-finite    *     values.    */
annotation|@
name|Deprecated
comment|// com.google.common.math.DoubleUtils
annotation|@
name|GwtIncompatible
DECL|method|mean (double... values)
specifier|public
specifier|static
name|double
name|mean
parameter_list|(
name|double
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"Cannot take mean of 0 values"
argument_list|)
expr_stmt|;
name|long
name|count
init|=
literal|1
decl_stmt|;
name|double
name|mean
init|=
name|checkFinite
argument_list|(
name|values
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|1
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
operator|++
name|index
control|)
block|{
name|checkFinite
argument_list|(
name|values
index|[
name|index
index|]
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|values
index|[
name|index
index|]
operator|-
name|mean
operator|)
operator|/
name|count
expr_stmt|;
block|}
return|return
name|mean
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of    * {@code values}.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    * @param values a nonempty series of values    * @throws IllegalArgumentException if {@code values} is empty    * @deprecated Use {@link Stats#meanOf} instead, noting the less strict handling of non-finite    *     values.    */
annotation|@
name|Deprecated
DECL|method|mean (int... values)
specifier|public
specifier|static
name|double
name|mean
parameter_list|(
name|int
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"Cannot take mean of 0 values"
argument_list|)
expr_stmt|;
comment|// The upper bound on the length of an array and the bounds on the int values mean that, in
comment|// this case only, we can compute the sum as a long without risking overflow or loss of
comment|// precision. So we do that, as it's slightly quicker than the Knuth algorithm.
name|long
name|sum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
operator|++
name|index
control|)
block|{
name|sum
operator|+=
name|values
index|[
name|index
index|]
expr_stmt|;
block|}
return|return
operator|(
name|double
operator|)
name|sum
operator|/
name|values
operator|.
name|length
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of    * {@code values}.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    * @param values a nonempty series of values, which will be converted to {@code double} values    *     (this may cause loss of precision for longs of magnitude over 2^53 (slightly over 9e15))    * @throws IllegalArgumentException if {@code values} is empty    * @deprecated Use {@link Stats#meanOf} instead, noting the less strict handling of non-finite    *     values.    */
annotation|@
name|Deprecated
DECL|method|mean (long... values)
specifier|public
specifier|static
name|double
name|mean
parameter_list|(
name|long
modifier|...
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"Cannot take mean of 0 values"
argument_list|)
expr_stmt|;
name|long
name|count
init|=
literal|1
decl_stmt|;
name|double
name|mean
init|=
name|values
index|[
literal|0
index|]
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|1
init|;
name|index
operator|<
name|values
operator|.
name|length
condition|;
operator|++
name|index
control|)
block|{
name|count
operator|++
expr_stmt|;
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|values
index|[
name|index
index|]
operator|-
name|mean
operator|)
operator|/
name|count
expr_stmt|;
block|}
return|return
name|mean
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of    * {@code values}.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    * @param values a nonempty series of values, which will be converted to {@code double} values    *     (this may cause loss of precision)    * @throws IllegalArgumentException if {@code values} is empty or contains any non-finite value    * @deprecated Use {@link Stats#meanOf} instead, noting the less strict handling of non-finite    *     values.    */
annotation|@
name|Deprecated
comment|// com.google.common.math.DoubleUtils
annotation|@
name|GwtIncompatible
DECL|method|mean (Iterable<? extends Number> values)
specifier|public
specifier|static
name|double
name|mean
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|mean
argument_list|(
name|values
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the<a href="http://en.wikipedia.org/wiki/Arithmetic_mean">arithmetic mean</a> of    * {@code values}.    *    *<p>If these values are a sample drawn from a population, this is also an unbiased estimator of    * the arithmetic mean of the population.    *    * @param values a nonempty series of values, which will be converted to {@code double} values    *     (this may cause loss of precision)    * @throws IllegalArgumentException if {@code values} is empty or contains any non-finite value    * @deprecated Use {@link Stats#meanOf} instead, noting the less strict handling of non-finite    *     values.    */
annotation|@
name|Deprecated
comment|// com.google.common.math.DoubleUtils
annotation|@
name|GwtIncompatible
DECL|method|mean (Iterator<? extends Number> values)
specifier|public
specifier|static
name|double
name|mean
parameter_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|values
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|values
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"Cannot take mean of 0 values"
argument_list|)
expr_stmt|;
name|long
name|count
init|=
literal|1
decl_stmt|;
name|double
name|mean
init|=
name|checkFinite
argument_list|(
name|values
operator|.
name|next
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|values
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|double
name|value
init|=
name|checkFinite
argument_list|(
name|values
operator|.
name|next
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|)
decl_stmt|;
name|count
operator|++
expr_stmt|;
comment|// Art of Computer Programming vol. 2, Knuth, 4.2.2, (15)
name|mean
operator|+=
operator|(
name|value
operator|-
name|mean
operator|)
operator|/
name|count
expr_stmt|;
block|}
return|return
name|mean
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// com.google.common.math.DoubleUtils
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkFinite (double argument)
specifier|private
specifier|static
name|double
name|checkFinite
parameter_list|(
name|double
name|argument
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|isFinite
argument_list|(
name|argument
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|argument
return|;
block|}
DECL|method|DoubleMath ()
specifier|private
name|DoubleMath
parameter_list|()
block|{}
block|}
end_class

end_unit

