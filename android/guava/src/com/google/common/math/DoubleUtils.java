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
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|MAX_EXPONENT
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|MIN_EXPONENT
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|POSITIVE_INFINITY
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|doubleToRawLongBits
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|isNaN
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|longBitsToDouble
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

begin_comment
comment|/**  * Utilities for {@code double} primitives.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|DoubleUtils
specifier|final
class|class
name|DoubleUtils
block|{
DECL|method|DoubleUtils ()
specifier|private
name|DoubleUtils
parameter_list|()
block|{}
DECL|method|nextDown (double d)
specifier|static
name|double
name|nextDown
parameter_list|(
name|double
name|d
parameter_list|)
block|{
return|return
operator|-
name|Math
operator|.
name|nextUp
argument_list|(
operator|-
name|d
argument_list|)
return|;
block|}
comment|// The mask for the significand, according to the {@link
comment|// Double#doubleToRawLongBits(double)} spec.
DECL|field|SIGNIFICAND_MASK
specifier|static
specifier|final
name|long
name|SIGNIFICAND_MASK
init|=
literal|0x000fffffffffffffL
decl_stmt|;
comment|// The mask for the exponent, according to the {@link
comment|// Double#doubleToRawLongBits(double)} spec.
DECL|field|EXPONENT_MASK
specifier|static
specifier|final
name|long
name|EXPONENT_MASK
init|=
literal|0x7ff0000000000000L
decl_stmt|;
comment|// The mask for the sign, according to the {@link
comment|// Double#doubleToRawLongBits(double)} spec.
DECL|field|SIGN_MASK
specifier|static
specifier|final
name|long
name|SIGN_MASK
init|=
literal|0x8000000000000000L
decl_stmt|;
DECL|field|SIGNIFICAND_BITS
specifier|static
specifier|final
name|int
name|SIGNIFICAND_BITS
init|=
literal|52
decl_stmt|;
DECL|field|EXPONENT_BIAS
specifier|static
specifier|final
name|int
name|EXPONENT_BIAS
init|=
literal|1023
decl_stmt|;
comment|/** The implicit 1 bit that is omitted in significands of normal doubles. */
DECL|field|IMPLICIT_BIT
specifier|static
specifier|final
name|long
name|IMPLICIT_BIT
init|=
name|SIGNIFICAND_MASK
operator|+
literal|1
decl_stmt|;
DECL|method|getSignificand (double d)
specifier|static
name|long
name|getSignificand
parameter_list|(
name|double
name|d
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|isFinite
argument_list|(
name|d
argument_list|)
argument_list|,
literal|"not a normal value"
argument_list|)
expr_stmt|;
name|int
name|exponent
init|=
name|getExponent
argument_list|(
name|d
argument_list|)
decl_stmt|;
name|long
name|bits
init|=
name|doubleToRawLongBits
argument_list|(
name|d
argument_list|)
decl_stmt|;
name|bits
operator|&=
name|SIGNIFICAND_MASK
expr_stmt|;
return|return
operator|(
name|exponent
operator|==
name|MIN_EXPONENT
operator|-
literal|1
operator|)
condition|?
name|bits
operator|<<
literal|1
else|:
name|bits
operator||
name|IMPLICIT_BIT
return|;
block|}
DECL|method|isFinite (double d)
specifier|static
name|boolean
name|isFinite
parameter_list|(
name|double
name|d
parameter_list|)
block|{
return|return
name|getExponent
argument_list|(
name|d
argument_list|)
operator|<=
name|MAX_EXPONENT
return|;
block|}
DECL|method|isNormal (double d)
specifier|static
name|boolean
name|isNormal
parameter_list|(
name|double
name|d
parameter_list|)
block|{
return|return
name|getExponent
argument_list|(
name|d
argument_list|)
operator|>=
name|MIN_EXPONENT
return|;
block|}
comment|/*    * Returns x scaled by a power of 2 such that it is in the range [1, 2). Assumes x is positive,    * normal, and finite.    */
DECL|method|scaleNormalize (double x)
specifier|static
name|double
name|scaleNormalize
parameter_list|(
name|double
name|x
parameter_list|)
block|{
name|long
name|significand
init|=
name|doubleToRawLongBits
argument_list|(
name|x
argument_list|)
operator|&
name|SIGNIFICAND_MASK
decl_stmt|;
return|return
name|longBitsToDouble
argument_list|(
name|significand
operator||
name|ONE_BITS
argument_list|)
return|;
block|}
DECL|method|bigToDouble (BigInteger x)
specifier|static
name|double
name|bigToDouble
parameter_list|(
name|BigInteger
name|x
parameter_list|)
block|{
comment|// This is an extremely fast implementation of BigInteger.doubleValue(). JDK patch pending.
name|BigInteger
name|absX
init|=
name|x
operator|.
name|abs
argument_list|()
decl_stmt|;
name|int
name|exponent
init|=
name|absX
operator|.
name|bitLength
argument_list|()
operator|-
literal|1
decl_stmt|;
comment|// exponent == floor(log2(abs(x)))
if|if
condition|(
name|exponent
operator|<
name|Long
operator|.
name|SIZE
operator|-
literal|1
condition|)
block|{
return|return
name|x
operator|.
name|longValue
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|exponent
operator|>
name|MAX_EXPONENT
condition|)
block|{
return|return
name|x
operator|.
name|signum
argument_list|()
operator|*
name|POSITIVE_INFINITY
return|;
block|}
comment|/*      * We need the top SIGNIFICAND_BITS + 1 bits, including the "implicit" one bit. To make rounding      * easier, we pick out the top SIGNIFICAND_BITS + 2 bits, so we have one to help us round up or      * down. twiceSignifFloor will contain the top SIGNIFICAND_BITS + 2 bits, and signifFloor the      * top SIGNIFICAND_BITS + 1.      *      * It helps to consider the real number signif = absX * 2^(SIGNIFICAND_BITS - exponent).      */
name|int
name|shift
init|=
name|exponent
operator|-
name|SIGNIFICAND_BITS
operator|-
literal|1
decl_stmt|;
name|long
name|twiceSignifFloor
init|=
name|absX
operator|.
name|shiftRight
argument_list|(
name|shift
argument_list|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
name|long
name|signifFloor
init|=
name|twiceSignifFloor
operator|>>
literal|1
decl_stmt|;
name|signifFloor
operator|&=
name|SIGNIFICAND_MASK
expr_stmt|;
comment|// remove the implied bit
comment|/*      * We round up if either the fractional part of signif is strictly greater than 0.5 (which is      * true if the 0.5 bit is set and any lower bit is set), or if the fractional part of signif is      *>= 0.5 and signifFloor is odd (which is true if both the 0.5 bit and the 1 bit are set).      */
name|boolean
name|increment
init|=
operator|(
name|twiceSignifFloor
operator|&
literal|1
operator|)
operator|!=
literal|0
operator|&&
operator|(
operator|(
name|signifFloor
operator|&
literal|1
operator|)
operator|!=
literal|0
operator|||
name|absX
operator|.
name|getLowestSetBit
argument_list|()
operator|<
name|shift
operator|)
decl_stmt|;
name|long
name|signifRounded
init|=
name|increment
condition|?
name|signifFloor
operator|+
literal|1
else|:
name|signifFloor
decl_stmt|;
name|long
name|bits
init|=
call|(
name|long
call|)
argument_list|(
name|exponent
operator|+
name|EXPONENT_BIAS
argument_list|)
operator|<<
name|SIGNIFICAND_BITS
decl_stmt|;
name|bits
operator|+=
name|signifRounded
expr_stmt|;
comment|/*      * If signifRounded == 2^53, we'd need to set all of the significand bits to zero and add 1 to      * the exponent. This is exactly the behavior we get from just adding signifRounded to bits      * directly. If the exponent is MAX_DOUBLE_EXPONENT, we round up (correctly) to      * Double.POSITIVE_INFINITY.      */
name|bits
operator||=
name|x
operator|.
name|signum
argument_list|()
operator|&
name|SIGN_MASK
expr_stmt|;
return|return
name|longBitsToDouble
argument_list|(
name|bits
argument_list|)
return|;
block|}
comment|/** Returns its argument if it is non-negative, zero if it is negative. */
DECL|method|ensureNonNegative (double value)
specifier|static
name|double
name|ensureNonNegative
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|!
name|isNaN
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Math
operator|.
name|max
argument_list|(
name|value
argument_list|,
literal|0.0
argument_list|)
return|;
block|}
DECL|field|ONE_BITS
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
name|long
name|ONE_BITS
init|=
literal|0x3ff0000000000000L
decl_stmt|;
block|}
end_class

end_unit

