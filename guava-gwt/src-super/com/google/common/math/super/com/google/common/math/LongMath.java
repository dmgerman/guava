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
name|VisibleForTesting
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
comment|/**  * A class for arithmetic on values of type {@code long}. Where possible, methods are defined and  * named analogously to their {@code BigInteger} counterparts.  *  *<p>The implementations of many methods in this class are based on material from Henry S. Warren,  * Jr.'s<i>Hacker's Delight</i>, (Addison Wesley, 2002).  *  *<p>Similar functionality for {@code int} and for {@link BigInteger} can be found in  * {@link IntMath} and {@link BigIntegerMath} respectively.  For other common operations on  * {@code long} values, see {@link com.google.common.primitives.Longs}.  *  * @author Louis Wasserman  * @since 11.0  */
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
DECL|class|LongMath
specifier|public
specifier|final
class|class
name|LongMath
block|{
comment|// NOTE: Whenever both tests are cheap and functional, it's faster to use&, | instead of&&, ||
comment|/**    * Returns {@code true} if {@code x} represents a power of two.    *    *<p>This differs from {@code Long.bitCount(x) == 1}, because    * {@code Long.bitCount(Long.MIN_VALUE) == 1}, but {@link Long#MIN_VALUE} is not a power of two.    */
DECL|method|isPowerOfTwo (long x)
specifier|public
specifier|static
name|boolean
name|isPowerOfTwo
parameter_list|(
name|long
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
DECL|method|log2 (long x, RoundingMode mode)
specifier|public
specifier|static
name|int
name|log2
parameter_list|(
name|long
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
name|Long
operator|.
name|SIZE
operator|-
literal|1
operator|)
operator|-
name|Long
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
name|Long
operator|.
name|SIZE
operator|-
name|Long
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
name|Long
operator|.
name|numberOfLeadingZeros
argument_list|(
name|x
argument_list|)
decl_stmt|;
name|long
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
name|Long
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
argument_list|(
literal|"impossible"
argument_list|)
throw|;
block|}
block|}
comment|/** The biggest half power of two that fits into an unsigned long */
DECL|field|MAX_POWER_OF_SQRT2_UNSIGNED
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|long
name|MAX_POWER_OF_SQRT2_UNSIGNED
init|=
literal|0xB504F333F9DE6484L
decl_stmt|;
comment|// HALF_POWERS_OF_10[i] = largest long less than 10^(i + 0.5)
DECL|field|FACTORIALS
specifier|static
specifier|final
name|long
index|[]
name|FACTORIALS
init|=
block|{
literal|1L
block|,
literal|1L
block|,
literal|1L
operator|*
literal|2
block|,
literal|1L
operator|*
literal|2
operator|*
literal|3
block|,
literal|1L
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
block|,
literal|1L
operator|*
literal|2
operator|*
literal|3
operator|*
literal|4
operator|*
literal|5
block|,
literal|1L
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
literal|1L
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
literal|1L
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
literal|1L
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
literal|1L
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
literal|1L
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
literal|1L
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
block|,
literal|1L
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
operator|*
literal|13
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
operator|*
literal|16
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
operator|*
literal|16
operator|*
literal|17
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
operator|*
literal|16
operator|*
literal|17
operator|*
literal|18
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
operator|*
literal|16
operator|*
literal|17
operator|*
literal|18
operator|*
literal|19
block|,
literal|1L
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
operator|*
literal|13
operator|*
literal|14
operator|*
literal|15
operator|*
literal|16
operator|*
literal|17
operator|*
literal|18
operator|*
literal|19
operator|*
literal|20
block|}
decl_stmt|;
comment|/**    * Returns {@code n} choose {@code k}, also known as the binomial coefficient of {@code n} and    * {@code k}, or {@link Long#MAX_VALUE} if the result does not fit in a {@code long}.    *    * @throws IllegalArgumentException if {@code n< 0}, {@code k< 0}, or {@code k> n}    */
DECL|method|binomial (int n, int k)
specifier|public
specifier|static
name|long
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
name|Long
operator|.
name|MAX_VALUE
return|;
block|}
name|long
name|result
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|k
operator|<
name|BIGGEST_SIMPLE_BINOMIALS
operator|.
name|length
operator|&&
name|n
operator|<=
name|BIGGEST_SIMPLE_BINOMIALS
index|[
name|k
index|]
condition|)
block|{
comment|// guaranteed not to overflow
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
block|}
else|else
block|{
comment|// We want to do this in long math for speed, but want to avoid overflow.
comment|// Dividing by the GCD suffices to avoid overflow in all the remaining cases.
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|k
condition|;
name|i
operator|++
operator|,
name|n
operator|--
control|)
block|{
name|int
name|d
init|=
name|IntMath
operator|.
name|gcd
argument_list|(
name|n
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|result
operator|/=
name|i
operator|/
name|d
expr_stmt|;
comment|// (i/d) is guaranteed to divide result
name|result
operator|*=
name|n
operator|/
name|d
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/*    * binomial(BIGGEST_BINOMIALS[k], k) fits in a long, but not    * binomial(BIGGEST_BINOMIALS[k] + 1, k).    */
DECL|field|BIGGEST_BINOMIALS
specifier|static
specifier|final
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
name|Integer
operator|.
name|MAX_VALUE
block|,
literal|3810779
block|,
literal|121977
block|,
literal|16175
block|,
literal|4337
block|,
literal|1733
block|,
literal|887
block|,
literal|534
block|,
literal|361
block|,
literal|265
block|,
literal|206
block|,
literal|169
block|,
literal|143
block|,
literal|125
block|,
literal|111
block|,
literal|101
block|,
literal|94
block|,
literal|88
block|,
literal|83
block|,
literal|79
block|,
literal|76
block|,
literal|74
block|,
literal|72
block|,
literal|70
block|,
literal|69
block|,
literal|68
block|,
literal|67
block|,
literal|67
block|,
literal|66
block|,
literal|66
block|,
literal|66
block|,
literal|66
block|}
decl_stmt|;
comment|/*    * binomial(BIGGEST_SIMPLE_BINOMIALS[k], k) doesn't need to use the slower GCD-based impl,    * but binomial(BIGGEST_SIMPLE_BINOMIALS[k] + 1, k) does.    */
DECL|field|BIGGEST_SIMPLE_BINOMIALS
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|int
index|[]
name|BIGGEST_SIMPLE_BINOMIALS
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
name|Integer
operator|.
name|MAX_VALUE
block|,
literal|2642246
block|,
literal|86251
block|,
literal|11724
block|,
literal|3218
block|,
literal|1313
block|,
literal|684
block|,
literal|419
block|,
literal|287
block|,
literal|214
block|,
literal|169
block|,
literal|139
block|,
literal|119
block|,
literal|105
block|,
literal|95
block|,
literal|87
block|,
literal|81
block|,
literal|76
block|,
literal|73
block|,
literal|70
block|,
literal|68
block|,
literal|66
block|,
literal|64
block|,
literal|63
block|,
literal|62
block|,
literal|62
block|,
literal|61
block|,
literal|61
block|,
literal|61
block|}
decl_stmt|;
comment|// These values were generated by using checkedMultiply to see when the simple multiply/divide
comment|// algorithm would lead to an overflow.
DECL|method|LongMath ()
specifier|private
name|LongMath
parameter_list|()
block|{}
block|}
end_class

end_unit

