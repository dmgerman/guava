begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|io
operator|.
name|Serializable
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A wrapper class for unsigned {@code long} values, supporting arithmetic operations.  *  *<p>In some cases, when speed is more important than code readability, it may be faster simply to  * treat primitive {@code long} values as unsigned, using the methods from {@link UnsignedLongs}.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/PrimitivesExplained#unsigned-support">unsigned  * primitive utilities</a>.  *  * @author Louis Wasserman  * @author Colin Evans  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|UnsignedLong
specifier|public
specifier|final
class|class
name|UnsignedLong
extends|extends
name|Number
implements|implements
name|Comparable
argument_list|<
name|UnsignedLong
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|UNSIGNED_MASK
specifier|private
specifier|static
specifier|final
name|long
name|UNSIGNED_MASK
init|=
literal|0x7fffffffffffffffL
decl_stmt|;
DECL|field|ZERO
specifier|public
specifier|static
specifier|final
name|UnsignedLong
name|ZERO
init|=
operator|new
name|UnsignedLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|ONE
specifier|public
specifier|static
specifier|final
name|UnsignedLong
name|ONE
init|=
operator|new
name|UnsignedLong
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|MAX_VALUE
specifier|public
specifier|static
specifier|final
name|UnsignedLong
name|MAX_VALUE
init|=
operator|new
name|UnsignedLong
argument_list|(
operator|-
literal|1L
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|long
name|value
decl_stmt|;
DECL|method|UnsignedLong (long value)
specifier|private
name|UnsignedLong
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**    * Returns an {@code UnsignedLong} corresponding to a given bit representation. The argument is    * interpreted as an unsigned 64-bit value. Specifically, the sign bit of {@code bits} is    * interpreted as a normal bit, and all other bits are treated as usual.    *    *<p>If the argument is nonnegative, the returned result will be equal to {@code bits},    * otherwise, the result will be equal to {@code 2^64 + bits}.    *    *<p>To represent decimal constants less than {@code 2^63}, consider {@link #valueOf(long)}    * instead.    *    * @since 14.0    */
DECL|method|fromLongBits (long bits)
specifier|public
specifier|static
name|UnsignedLong
name|fromLongBits
parameter_list|(
name|long
name|bits
parameter_list|)
block|{
comment|// TODO(lowasser): consider caching small values, like Long.valueOf
return|return
operator|new
name|UnsignedLong
argument_list|(
name|bits
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedLong} representing the same value as the specified {@code long}.    *    * @throws IllegalArgumentException if {@code value} is negative    * @since 14.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|valueOf (long value)
specifier|public
specifier|static
name|UnsignedLong
name|valueOf
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|>=
literal|0
argument_list|,
literal|"value (%s) is outside the range for an unsigned long value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|fromLongBits
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code UnsignedLong} representing the same value as the specified {@code BigInteger}.    * This is the inverse operation of {@link #bigIntegerValue()}.    *    * @throws IllegalArgumentException if {@code value} is negative or {@code value>= 2^64}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|valueOf (BigInteger value)
specifier|public
specifier|static
name|UnsignedLong
name|valueOf
parameter_list|(
name|BigInteger
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|value
operator|.
name|signum
argument_list|()
operator|>=
literal|0
operator|&&
name|value
operator|.
name|bitLength
argument_list|()
operator|<=
name|Long
operator|.
name|SIZE
argument_list|,
literal|"value (%s) is outside the range for an unsigned long value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|fromLongBits
argument_list|(
name|value
operator|.
name|longValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedLong} holding the value of the specified {@code String}, parsed as an    * unsigned {@code long} value.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code long}    *     value    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|valueOf (String string)
specifier|public
specifier|static
name|UnsignedLong
name|valueOf
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|valueOf
argument_list|(
name|string
argument_list|,
literal|10
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedLong} holding the value of the specified {@code String}, parsed as an    * unsigned {@code long} value in the specified radix.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code long}    *     value, or {@code radix} is not between {@link Character#MIN_RADIX} and {@link    *     Character#MAX_RADIX}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|valueOf (String string, int radix)
specifier|public
specifier|static
name|UnsignedLong
name|valueOf
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|UnsignedLongs
operator|.
name|parseUnsignedLong
argument_list|(
name|string
argument_list|,
name|radix
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the result of adding this and {@code val}. If the result would have more than 64 bits,    * returns the low 64 bits of the result.    *    * @since 14.0    */
DECL|method|plus (UnsignedLong val)
specifier|public
name|UnsignedLong
name|plus
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|this
operator|.
name|value
operator|+
name|checkNotNull
argument_list|(
name|val
argument_list|)
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns the result of subtracting this and {@code val}. If the result would have more than 64    * bits, returns the low 64 bits of the result.    *    * @since 14.0    */
DECL|method|minus (UnsignedLong val)
specifier|public
name|UnsignedLong
name|minus
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|this
operator|.
name|value
operator|-
name|checkNotNull
argument_list|(
name|val
argument_list|)
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns the result of multiplying this and {@code val}. If the result would have more than 64    * bits, returns the low 64 bits of the result.    *    * @since 14.0    */
DECL|method|times (UnsignedLong val)
specifier|public
name|UnsignedLong
name|times
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|value
operator|*
name|checkNotNull
argument_list|(
name|val
argument_list|)
operator|.
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns the result of dividing this by {@code val}.    *    * @since 14.0    */
DECL|method|dividedBy (UnsignedLong val)
specifier|public
name|UnsignedLong
name|dividedBy
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|UnsignedLongs
operator|.
name|divide
argument_list|(
name|value
argument_list|,
name|checkNotNull
argument_list|(
name|val
argument_list|)
operator|.
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns this modulo {@code val}.    *    * @since 14.0    */
DECL|method|mod (UnsignedLong val)
specifier|public
name|UnsignedLong
name|mod
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|UnsignedLongs
operator|.
name|remainder
argument_list|(
name|value
argument_list|,
name|checkNotNull
argument_list|(
name|val
argument_list|)
operator|.
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/** Returns the value of this {@code UnsignedLong} as an {@code int}. */
annotation|@
name|Override
DECL|method|intValue ()
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
operator|(
name|int
operator|)
name|value
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedLong} as a {@code long}. This is an inverse operation    * to {@link #fromLongBits}.    *    *<p>Note that if this {@code UnsignedLong} holds a value {@code>= 2^63}, the returned value    * will be equal to {@code this - 2^64}.    */
annotation|@
name|Override
DECL|method|longValue ()
specifier|public
name|long
name|longValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedLong} as a {@code float}, analogous to a widening    * primitive conversion from {@code long} to {@code float}, and correctly rounded.    */
annotation|@
name|Override
DECL|method|floatValue ()
specifier|public
name|float
name|floatValue
parameter_list|()
block|{
if|if
condition|(
name|value
operator|>=
literal|0
condition|)
block|{
return|return
operator|(
name|float
operator|)
name|value
return|;
block|}
comment|// The top bit is set, which means that the float value is going to come from the top 24 bits.
comment|// So we can ignore the bottom 8, except for rounding. See doubleValue() for more.
return|return
call|(
name|float
call|)
argument_list|(
operator|(
name|value
operator|>>>
literal|1
operator|)
operator||
operator|(
name|value
operator|&
literal|1
operator|)
argument_list|)
operator|*
literal|2f
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedLong} as a {@code double}, analogous to a widening    * primitive conversion from {@code long} to {@code double}, and correctly rounded.    */
annotation|@
name|Override
DECL|method|doubleValue ()
specifier|public
name|double
name|doubleValue
parameter_list|()
block|{
if|if
condition|(
name|value
operator|>=
literal|0
condition|)
block|{
return|return
operator|(
name|double
operator|)
name|value
return|;
block|}
comment|// The top bit is set, which means that the double value is going to come from the top 53 bits.
comment|// So we can ignore the bottom 11, except for rounding. We can unsigned-shift right 1, aka
comment|// unsigned-divide by 2, and convert that. Then we'll get exactly half of the desired double
comment|// value. But in the specific case where the bottom two bits of the original number are 01, we
comment|// want to replace that with 1 in the shifted value for correct rounding.
return|return
call|(
name|double
call|)
argument_list|(
operator|(
name|value
operator|>>>
literal|1
operator|)
operator||
operator|(
name|value
operator|&
literal|1
operator|)
argument_list|)
operator|*
literal|2.0
return|;
block|}
comment|/** Returns the value of this {@code UnsignedLong} as a {@link BigInteger}. */
DECL|method|bigIntegerValue ()
specifier|public
name|BigInteger
name|bigIntegerValue
parameter_list|()
block|{
name|BigInteger
name|bigInt
init|=
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|value
operator|&
name|UNSIGNED_MASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|<
literal|0
condition|)
block|{
name|bigInt
operator|=
name|bigInt
operator|.
name|setBit
argument_list|(
name|Long
operator|.
name|SIZE
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|bigInt
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (UnsignedLong o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|UnsignedLong
name|o
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
return|return
name|UnsignedLongs
operator|.
name|compare
argument_list|(
name|value
argument_list|,
name|o
operator|.
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Longs
operator|.
name|hashCode
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|UnsignedLong
condition|)
block|{
name|UnsignedLong
name|other
init|=
operator|(
name|UnsignedLong
operator|)
name|obj
decl_stmt|;
return|return
name|value
operator|==
name|other
operator|.
name|value
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/** Returns a string representation of the {@code UnsignedLong} value, in base 10. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|UnsignedLongs
operator|.
name|toString
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of the {@code UnsignedLong} value, in base {@code radix}. If    * {@code radix< Character.MIN_RADIX} or {@code radix> Character.MAX_RADIX}, the radix {@code    * 10} is used.    */
DECL|method|toString (int radix)
specifier|public
name|String
name|toString
parameter_list|(
name|int
name|radix
parameter_list|)
block|{
return|return
name|UnsignedLongs
operator|.
name|toString
argument_list|(
name|value
argument_list|,
name|radix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

