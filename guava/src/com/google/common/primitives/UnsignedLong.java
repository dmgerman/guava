begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A wrapper class for unsigned {@code long} values, supporting arithmetic operations.  *  *<p>In some cases, when speed is more important than code readability, it may be faster simply to  * treat primitive {@code long} values as unsigned, using the methods from {@link UnsignedLongs}.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/PrimitivesExplained#Unsigned_support">  * unsigned primitive utilities</a>.  *  * @author Louis Wasserman  * @author Colin Evans  * @since 11.0  */
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
comment|/**    * Returns an {@code UnsignedLong} that, when treated as signed, is equal to {@code value}. The    * inverse operation is {@link #longValue()}.    *    *<p>Put another way, if {@code value} is negative, the returned result will be equal to    * {@code 2^64 + value}; otherwise, the returned result will be equal to {@code value}.    *    * @deprecated Use {@link #fromLongBits(long)}. This method is scheduled to be removed in Guava    *             release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|asUnsigned (long value)
specifier|public
specifier|static
name|UnsignedLong
name|asUnsigned
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
name|fromLongBits
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedLong} corresponding to a given bit representation.    * The argument is interpreted as an unsigned 64-bit value. Specifically, the sign bit    * of {@code bits} is interpreted as a normal bit, and all other bits are treated as usual.    *    *<p>If the argument is nonnegative, the returned result will be equal to {@code bits},    * otherwise, the result will be equal to {@code 2^64 + bits}.    *    *<p>To represent decimal constants less than {@code 2^63}, consider {@link #valueOf(long)}    * instead.    *    * @since 14.0    */
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
comment|// TODO(user): consider caching small values, like Long.valueOf
return|return
operator|new
name|UnsignedLong
argument_list|(
name|bits
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedLong} representing the same value as the specified {@code long}.    *    * @throws IllegalArgumentException if {@code value} is negative    * @since 14.0    */
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
comment|/**    * Returns a {@code UnsignedLong} representing the same value as the specified    * {@code BigInteger}. This is the inverse operation of {@link #bigIntegerValue()}.    *    * @throws IllegalArgumentException if {@code value} is negative or {@code value>= 2^64}    */
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
comment|/**    * Returns an {@code UnsignedLong} holding the value of the specified {@code String}, parsed as    * an unsigned {@code long} value.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code long}    *         value    */
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
comment|/**    * Returns an {@code UnsignedLong} holding the value of the specified {@code String}, parsed as    * an unsigned {@code long} value in the specified radix.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code long}    *         value, or {@code radix} is not between {@link Character#MIN_RADIX} and    *         {@link Character#MAX_RADIX}    */
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
comment|/**    * Returns the result of adding this and {@code val}. If the result would have more than 64 bits,    * returns the low 64 bits of the result.    *    * @deprecated Use {@link #plus(UnsignedLong)}.  This method is scheduled to be removed in Guava    *             release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|add (UnsignedLong val)
specifier|public
name|UnsignedLong
name|add
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|plus
argument_list|(
name|val
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
comment|/**    * Returns the result of subtracting this and {@code val}. If the result would be negative,    * returns the low 64 bits of the result.    *    * @deprecated Use {@link #minus(UnsignedLong)}.  This method is scheduled to be removed in Guava    *             release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|subtract (UnsignedLong val)
specifier|public
name|UnsignedLong
name|subtract
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|minus
argument_list|(
name|val
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
comment|/**    * Returns the result of multiplying this and {@code val}. If the result would have more than 64    * bits, returns the low 64 bits of the result.    *    * @deprecated Use {@link #times(UnsignedLong)}.  This method is scheduled to be removed in Guava    *             release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|multiply (UnsignedLong val)
specifier|public
name|UnsignedLong
name|multiply
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|times
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of multiplying this and {@code val}. If the result would have more than 64    * bits, returns the low 64 bits of the result.    *    * @since 14.0    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Returns the result of dividing this by {@code val}.    *    * @deprecated Use {@link #dividedBy(UnsignedLong)}.  This method is scheduled to be removed in    *             Guava release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|divide (UnsignedLong val)
specifier|public
name|UnsignedLong
name|divide
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|dividedBy
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of dividing this by {@code val}.    *    * @since 14.0    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Returns the remainder of dividing this by {@code val}.    *    * @deprecated Use {@link #mod(UnsignedLong)}.  This method is scheduled to be removed in Guava    *             release 15.0.    */
annotation|@
name|Deprecated
annotation|@
name|Beta
DECL|method|remainder (UnsignedLong val)
specifier|public
name|UnsignedLong
name|remainder
parameter_list|(
name|UnsignedLong
name|val
parameter_list|)
block|{
return|return
name|mod
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns this modulo {@code val}.    *    * @since 14.0    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Returns the value of this {@code UnsignedLong} as an {@code int}.    */
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
comment|/**    * Returns the value of this {@code UnsignedLong} as a {@code long}. This is an inverse operation    * to {@link #asUnsigned}.    *    *<p>Note that if this {@code UnsignedLong} holds a value {@code>= 2^63}, the returned value    * will be equal to {@code this - 2^64}.    */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"cast"
argument_list|)
name|float
name|fValue
init|=
call|(
name|float
call|)
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
name|fValue
operator|+=
literal|0x1
literal|.0p63f
expr_stmt|;
block|}
return|return
name|fValue
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"cast"
argument_list|)
name|double
name|dValue
init|=
call|(
name|double
call|)
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
name|dValue
operator|+=
literal|0x1
literal|.0p63
expr_stmt|;
block|}
return|return
name|dValue
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedLong} as a {@link BigInteger}.    */
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
comment|/**    * Returns a string representation of the {@code UnsignedLong} value, in base 10.    */
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
comment|/**    * Returns a string representation of the {@code UnsignedLong} value, in base {@code radix}. If    * {@code radix< Character.MIN_RADIX} or {@code radix> Character.MAX_RADIX}, the radix    * {@code 10} is used.    */
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

