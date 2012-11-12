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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|UnsignedInts
operator|.
name|INT_MASK
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
name|primitives
operator|.
name|UnsignedInts
operator|.
name|compare
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
name|primitives
operator|.
name|UnsignedInts
operator|.
name|toLong
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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A wrapper class for unsigned {@code int} values, supporting arithmetic operations.  *  *<p>In some cases, when speed is more important than code readability, it may be faster simply to  * treat primitive {@code int} values as unsigned, using the methods from {@link UnsignedInts}.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/PrimitivesExplained#Unsigned_support">  * unsigned primitive utilities</a>.  *  * @author Louis Wasserman  * @since 11.0  */
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
DECL|class|UnsignedInteger
specifier|public
specifier|final
class|class
name|UnsignedInteger
extends|extends
name|Number
implements|implements
name|Comparable
argument_list|<
name|UnsignedInteger
argument_list|>
block|{
DECL|field|ZERO
specifier|public
specifier|static
specifier|final
name|UnsignedInteger
name|ZERO
init|=
name|asUnsigned
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|ONE
specifier|public
specifier|static
specifier|final
name|UnsignedInteger
name|ONE
init|=
name|asUnsigned
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|MAX_VALUE
specifier|public
specifier|static
specifier|final
name|UnsignedInteger
name|MAX_VALUE
init|=
name|asUnsigned
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|UnsignedInteger (int value)
specifier|private
name|UnsignedInteger
parameter_list|(
name|int
name|value
parameter_list|)
block|{
comment|// GWT doesn't consistently overflow values to make them 32-bit, so we need to force it.
name|this
operator|.
name|value
operator|=
name|value
operator|&
literal|0xffffffff
expr_stmt|;
block|}
comment|/**    * Returns an {@code UnsignedInteger} that, when treated as signed, is    * equal to {@code value}.    */
DECL|method|asUnsigned (int value)
specifier|public
specifier|static
name|UnsignedInteger
name|asUnsigned
parameter_list|(
name|int
name|value
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|fromIntBits
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedInteger} corresponding to a given bit representation.    * The argument is interpreted as an unsigned 32-bit value.    *    * @since 14.0    */
DECL|method|fromIntBits (int bits)
specifier|public
specifier|static
name|UnsignedInteger
name|fromIntBits
parameter_list|(
name|int
name|bits
parameter_list|)
block|{
return|return
operator|new
name|UnsignedInteger
argument_list|(
name|bits
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedInteger} that is equal to {@code value},    * if possible.  The inverse operation of {@link #longValue()}.    */
DECL|method|valueOf (long value)
specifier|public
specifier|static
name|UnsignedInteger
name|valueOf
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|(
name|value
operator|&
name|INT_MASK
operator|)
operator|==
name|value
argument_list|,
literal|"value (%s) is outside the range for an unsigned integer value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|fromIntBits
argument_list|(
operator|(
name|int
operator|)
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code UnsignedInteger} representing the same value as the specified    * {@link BigInteger}. This is the inverse operation of {@link #bigIntegerValue()}.    *    * @throws IllegalArgumentException if {@code value} is negative or {@code value>= 2^32}    */
DECL|method|valueOf (BigInteger value)
specifier|public
specifier|static
name|UnsignedInteger
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
name|Integer
operator|.
name|SIZE
argument_list|,
literal|"value (%s) is outside the range for an unsigned integer value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|fromIntBits
argument_list|(
name|value
operator|.
name|intValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns an {@code UnsignedInteger} holding the value of the specified {@code String}, parsed    * as an unsigned {@code int} value.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code int}    *         value    */
DECL|method|valueOf (String string)
specifier|public
specifier|static
name|UnsignedInteger
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
comment|/**    * Returns an {@code UnsignedInteger} holding the value of the specified {@code String}, parsed    * as an unsigned {@code int} value in the specified radix.    *    * @throws NumberFormatException if the string does not contain a parsable unsigned {@code int}    *         value    */
DECL|method|valueOf (String string, int radix)
specifier|public
specifier|static
name|UnsignedInteger
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
name|fromIntBits
argument_list|(
name|UnsignedInts
operator|.
name|parseUnsignedInt
argument_list|(
name|string
argument_list|,
name|radix
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the result of adding this and {@code val}. If the result would have more than 32 bits,    * returns the low 32 bits of the result.    */
DECL|method|add (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|add
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|plus
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of adding this and {@code val}. If the result would have more than 32 bits,    * returns the low 32 bits of the result.    *    * @since 14.0    */
DECL|method|plus (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|plus
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
return|return
name|fromIntBits
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
comment|/**    * Returns the result of subtracting this and {@code val}. If the result would be negative,    * returns the low 32 bits of the result.    */
DECL|method|subtract (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|subtract
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|minus
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of subtracting this and {@code val}. If the result would be negative,    * returns the low 32 bits of the result.    *    * @since 14.0    */
DECL|method|minus (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|minus
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
return|return
name|fromIntBits
argument_list|(
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
comment|/**    * Returns the result of multiplying this and {@code val}. If the result would have more than 32    * bits, returns the low 32 bits of the result.    */
DECL|method|multiply (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|multiply
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|times
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of multiplying this and {@code val}. If the result would have more than 32    * bits, returns the low 32 bits of the result.    *    * @since 14.0    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Does not truncate correctly"
argument_list|)
DECL|method|times (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|times
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): make this GWT-compatible
return|return
name|fromIntBits
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
comment|/**    * Returns the result of dividing this by {@code val}.    */
DECL|method|divide (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|divide
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|dividedBy
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns the result of dividing this by {@code val}.    *    * @throws ArithmeticException if {@code val} is zero    * @since 14.0    */
DECL|method|dividedBy (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|dividedBy
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
return|return
name|fromIntBits
argument_list|(
name|UnsignedInts
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
comment|/**    * Returns the remainder of dividing this by {@code val}.    */
DECL|method|remainder (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|remainder
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
comment|// TODO(user): deprecate this
return|return
name|mod
argument_list|(
name|val
argument_list|)
return|;
block|}
comment|/**    * Returns this mod {@code val}.    *    * @throws ArithmeticException if {@code val} is zero    * @since 14.0    */
DECL|method|mod (UnsignedInteger val)
specifier|public
name|UnsignedInteger
name|mod
parameter_list|(
name|UnsignedInteger
name|val
parameter_list|)
block|{
return|return
name|fromIntBits
argument_list|(
name|UnsignedInts
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
comment|/**    * Returns the value of this {@code UnsignedInteger} as an {@code int}. This is an inverse    * operation to {@link #fromIntBits}.    *    *<p>Note that if this {@code UnsignedInteger} holds a value {@code>= 2^31}, the returned value    * will be equal to {@code this - 2^32}.    */
annotation|@
name|Override
DECL|method|intValue ()
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedInteger} as a {@code long}.    */
annotation|@
name|Override
DECL|method|longValue ()
specifier|public
name|long
name|longValue
parameter_list|()
block|{
return|return
name|toLong
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedInteger} as a {@code float}, analogous to a widening    * primitive conversion from {@code int} to {@code float}, and correctly rounded.    */
annotation|@
name|Override
DECL|method|floatValue ()
specifier|public
name|float
name|floatValue
parameter_list|()
block|{
return|return
name|longValue
argument_list|()
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedInteger} as a {@code float}, analogous to a widening    * primitive conversion from {@code int} to {@code double}, and correctly rounded.    */
annotation|@
name|Override
DECL|method|doubleValue ()
specifier|public
name|double
name|doubleValue
parameter_list|()
block|{
return|return
name|longValue
argument_list|()
return|;
block|}
comment|/**    * Returns the value of this {@code UnsignedInteger} as a {@link BigInteger}.    */
DECL|method|bigIntegerValue ()
specifier|public
name|BigInteger
name|bigIntegerValue
parameter_list|()
block|{
return|return
name|BigInteger
operator|.
name|valueOf
argument_list|(
name|longValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Compares this unsigned integer to another unsigned integer.    * Returns {@code 0} if they are equal, a negative number if {@code this< other},    * and a positive number if {@code this> other}.    */
annotation|@
name|Override
DECL|method|compareTo (UnsignedInteger other)
specifier|public
name|int
name|compareTo
parameter_list|(
name|UnsignedInteger
name|other
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|other
argument_list|)
expr_stmt|;
return|return
name|compare
argument_list|(
name|value
argument_list|,
name|other
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
name|value
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
name|UnsignedInteger
condition|)
block|{
name|UnsignedInteger
name|other
init|=
operator|(
name|UnsignedInteger
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
comment|/**    * Returns a string representation of the {@code UnsignedInteger} value, in base 10.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toString
argument_list|(
literal|10
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of the {@code UnsignedInteger} value, in base {@code radix}.    * If {@code radix< Character.MIN_RADIX} or {@code radix> Character.MAX_RADIX}, the radix    * {@code 10} is used.    */
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
name|UnsignedInts
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

