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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to {@code int} primitives that interpret values as  *<i>unsigned</i> (that is, any negative value {@code x} is treated as the positive value  * {@code 2^32 + x}). The methods for which signedness is not an issue are in {@link Ints}, as well  * as signed versions of methods for which signedness is an issue.  *  *<p>In addition, this class provides several static methods for converting an {@code int} to a  * {@code String} and a {@code String} to an {@code int} that treat the {@code int} as an unsigned  * number.  *  *<p>Users of these utilities must be<i>extremely careful</i> not to mix up signed and unsigned  * {@code int} values. When possible, it is recommended that the {@link UnsignedInteger} wrapper  * class be used, at a small efficiency penalty, to enforce the distinction in the type system.  *  *<p>See the Guava User Guide article on  *<a href="https://github.com/google/guava/wiki/PrimitivesExplained#unsigned-support">unsigned  * primitive utilities</a>.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|UnsignedInts
specifier|public
specifier|final
class|class
name|UnsignedInts
block|{
DECL|field|INT_MASK
specifier|static
specifier|final
name|long
name|INT_MASK
init|=
literal|0xffffffffL
decl_stmt|;
DECL|method|UnsignedInts ()
specifier|private
name|UnsignedInts
parameter_list|()
block|{}
DECL|method|flip (int value)
specifier|static
name|int
name|flip
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|value
operator|^
name|Integer
operator|.
name|MIN_VALUE
return|;
block|}
comment|/**    * Compares the two specified {@code int} values, treating them as unsigned values between    * {@code 0} and {@code 2^32 - 1} inclusive.    *    * @param a the first unsigned {@code int} to compare    * @param b the second unsigned {@code int} to compare    * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is    *     greater than {@code b}; or zero if they are equal    */
DECL|method|compare (int a, int b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|Ints
operator|.
name|compare
argument_list|(
name|flip
argument_list|(
name|a
argument_list|)
argument_list|,
name|flip
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the value of the given {@code int} as a {@code long}, when treated as unsigned.    */
DECL|method|toLong (int value)
specifier|public
specifier|static
name|long
name|toLong
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|value
operator|&
name|INT_MASK
return|;
block|}
comment|/**    * Returns the {@code int} value that, when treated as unsigned, is equal to {@code value}, if    * possible.    *    * @param value a value between 0 and 2<sup>32</sup>-1 inclusive    * @return the {@code int} value that, when treated as unsigned, equals {@code value}    * @throws IllegalArgumentException if {@code value} is negative or greater than or equal to    *     2<sup>32</sup>    * @since 21.0    */
DECL|method|checkedCast (long value)
specifier|public
specifier|static
name|int
name|checkedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
operator|(
name|value
operator|>>
name|Integer
operator|.
name|SIZE
operator|)
operator|==
literal|0
argument_list|,
literal|"out of range: %s"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
operator|(
name|int
operator|)
name|value
return|;
block|}
comment|/**    * Returns the {@code int} value that, when treated as unsigned, is nearest in value to    * {@code value}.    *    * @param value any {@code long} value    * @return {@code 2^32 - 1} if {@code value>= 2^32}, {@code 0} if {@code value<= 0}, and    *     {@code value} cast to {@code int} otherwise    * @since 21.0    */
DECL|method|saturatedCast (long value)
specifier|public
specifier|static
name|int
name|saturatedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|<=
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|>=
operator|(
literal|1L
operator|<<
literal|32
operator|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
operator|(
name|int
operator|)
name|value
return|;
block|}
block|}
comment|/**    * Returns the least value present in {@code array}, treating values as unsigned.    *    * @param array a<i>nonempty</i> array of unsigned {@code int} values    * @return the value present in {@code array} that is less than or equal to every other value in    *     the array according to {@link #compare}    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|min (int... array)
specifier|public
specifier|static
name|int
name|min
parameter_list|(
name|int
modifier|...
name|array
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|array
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|min
init|=
name|flip
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
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
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|next
init|=
name|flip
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|<
name|min
condition|)
block|{
name|min
operator|=
name|next
expr_stmt|;
block|}
block|}
return|return
name|flip
argument_list|(
name|min
argument_list|)
return|;
block|}
comment|/**    * Returns the greatest value present in {@code array}, treating values as unsigned.    *    * @param array a<i>nonempty</i> array of unsigned {@code int} values    * @return the value present in {@code array} that is greater than or equal to every other value    *     in the array according to {@link #compare}    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|max (int... array)
specifier|public
specifier|static
name|int
name|max
parameter_list|(
name|int
modifier|...
name|array
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|array
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|int
name|max
init|=
name|flip
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
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
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|next
init|=
name|flip
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|>
name|max
condition|)
block|{
name|max
operator|=
name|next
expr_stmt|;
block|}
block|}
return|return
name|flip
argument_list|(
name|max
argument_list|)
return|;
block|}
comment|/**    * Returns a string containing the supplied unsigned {@code int} values separated by    * {@code separator}. For example, {@code join("-", 1, 2, 3)} returns the string {@code "1-2-3"}.    *    * @param separator the text that should appear between consecutive values in the resulting string    *     (but not at the start or end)    * @param array an array of unsigned {@code int} values, possibly empty    */
DECL|method|join (String separator, int... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|int
modifier|...
name|array
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|separator
argument_list|)
expr_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|""
return|;
block|}
comment|// For pre-sizing a builder, just get the right order of magnitude
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|array
operator|.
name|length
operator|*
literal|5
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|toString
argument_list|(
name|array
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|separator
argument_list|)
operator|.
name|append
argument_list|(
name|toString
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns a comparator that compares two arrays of unsigned {@code int} values<a    * href="http://en.wikipedia.org/wiki/Lexicographical_order">lexicographically</a>. That is, it    * compares, using {@link #compare(int, int)}), the first pair of values that follow any common    * prefix, or when one array is a prefix of the other, treats the shorter array as the lesser. For    * example, {@code []< [1]< [1, 2]< [2]< [1<< 31]}.    *    *<p>The returned comparator is inconsistent with {@link Object#equals(Object)} (since arrays    * support only identity equality), but it is consistent with {@link Arrays#equals(int[], int[])}.    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|int
index|[]
argument_list|>
name|lexicographicalComparator
parameter_list|()
block|{
return|return
name|LexicographicalComparator
operator|.
name|INSTANCE
return|;
block|}
DECL|enum|LexicographicalComparator
enum|enum
name|LexicographicalComparator
implements|implements
name|Comparator
argument_list|<
name|int
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|compare (int[] left, int[] right)
specifier|public
name|int
name|compare
parameter_list|(
name|int
index|[]
name|left
parameter_list|,
name|int
index|[]
name|right
parameter_list|)
block|{
name|int
name|minLength
init|=
name|Math
operator|.
name|min
argument_list|(
name|left
operator|.
name|length
argument_list|,
name|right
operator|.
name|length
argument_list|)
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
name|minLength
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|left
index|[
name|i
index|]
operator|!=
name|right
index|[
name|i
index|]
condition|)
block|{
return|return
name|UnsignedInts
operator|.
name|compare
argument_list|(
name|left
index|[
name|i
index|]
argument_list|,
name|right
index|[
name|i
index|]
argument_list|)
return|;
block|}
block|}
return|return
name|left
operator|.
name|length
operator|-
name|right
operator|.
name|length
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"UnsignedInts.lexicographicalComparator()"
return|;
block|}
block|}
comment|/**    * Returns dividend / divisor, where the dividend and divisor are treated as unsigned 32-bit    * quantities.    *    * @param dividend the dividend (numerator)    * @param divisor the divisor (denominator)    * @throws ArithmeticException if divisor is 0    */
DECL|method|divide (int dividend, int divisor)
specifier|public
specifier|static
name|int
name|divide
parameter_list|(
name|int
name|dividend
parameter_list|,
name|int
name|divisor
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|toLong
argument_list|(
name|dividend
argument_list|)
operator|/
name|toLong
argument_list|(
name|divisor
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns dividend % divisor, where the dividend and divisor are treated as unsigned 32-bit    * quantities.    *    * @param dividend the dividend (numerator)    * @param divisor the divisor (denominator)    * @throws ArithmeticException if divisor is 0    */
DECL|method|remainder (int dividend, int divisor)
specifier|public
specifier|static
name|int
name|remainder
parameter_list|(
name|int
name|dividend
parameter_list|,
name|int
name|divisor
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|toLong
argument_list|(
name|dividend
argument_list|)
operator|%
name|toLong
argument_list|(
name|divisor
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the unsigned {@code int} value represented by the given string.    *    * Accepts a decimal, hexadecimal, or octal number given by specifying the following prefix:    *    *<ul>    *<li>{@code 0x}<i>HexDigits</i>    *<li>{@code 0X}<i>HexDigits</i>    *<li>{@code #}<i>HexDigits</i>    *<li>{@code 0}<i>OctalDigits</i>    *</ul>    *    * @throws NumberFormatException if the string does not contain a valid unsigned {@code int} value    * @since 13.0    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|decode (String stringValue)
specifier|public
specifier|static
name|int
name|decode
parameter_list|(
name|String
name|stringValue
parameter_list|)
block|{
name|ParseRequest
name|request
init|=
name|ParseRequest
operator|.
name|fromString
argument_list|(
name|stringValue
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|parseUnsignedInt
argument_list|(
name|request
operator|.
name|rawValue
argument_list|,
name|request
operator|.
name|radix
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|NumberFormatException
name|decodeException
init|=
operator|new
name|NumberFormatException
argument_list|(
literal|"Error parsing value: "
operator|+
name|stringValue
argument_list|)
decl_stmt|;
name|decodeException
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|decodeException
throw|;
block|}
block|}
comment|/**    * Returns the unsigned {@code int} value represented by the given decimal string.    *    * @throws NumberFormatException if the string does not contain a valid unsigned {@code int} value    * @throws NullPointerException if {@code s} is null (in contrast to    *     {@link Integer#parseInt(String)})    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|parseUnsignedInt (String s)
specifier|public
specifier|static
name|int
name|parseUnsignedInt
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|parseUnsignedInt
argument_list|(
name|s
argument_list|,
literal|10
argument_list|)
return|;
block|}
comment|/**    * Returns the unsigned {@code int} value represented by a string with the given radix.    *    * @param string the string containing the unsigned integer representation to be parsed.    * @param radix the radix to use while parsing {@code s}; must be between    *     {@link Character#MIN_RADIX} and {@link Character#MAX_RADIX}.    * @throws NumberFormatException if the string does not contain a valid unsigned {@code int}, or    *     if supplied radix is invalid.    * @throws NullPointerException if {@code s} is null (in contrast to    *     {@link Integer#parseInt(String)})    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|parseUnsignedInt (String string, int radix)
specifier|public
specifier|static
name|int
name|parseUnsignedInt
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|long
name|result
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|string
argument_list|,
name|radix
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|result
operator|&
name|INT_MASK
operator|)
operator|!=
name|result
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"Input "
operator|+
name|string
operator|+
literal|" in base "
operator|+
name|radix
operator|+
literal|" is not in the range of an unsigned integer"
argument_list|)
throw|;
block|}
return|return
operator|(
name|int
operator|)
name|result
return|;
block|}
comment|/**    * Returns a string representation of x, where x is treated as unsigned.    */
DECL|method|toString (int x)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|int
name|x
parameter_list|)
block|{
return|return
name|toString
argument_list|(
name|x
argument_list|,
literal|10
argument_list|)
return|;
block|}
comment|/**    * Returns a string representation of {@code x} for the given radix, where {@code x} is treated as    * unsigned.    *    * @param x the value to convert to a string.    * @param radix the radix to use while working with {@code x}    * @throws IllegalArgumentException if {@code radix} is not between {@link Character#MIN_RADIX}    *     and {@link Character#MAX_RADIX}.    */
DECL|method|toString (int x, int radix)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|int
name|x
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
name|long
name|asLong
init|=
name|x
operator|&
name|INT_MASK
decl_stmt|;
return|return
name|Long
operator|.
name|toString
argument_list|(
name|asLong
argument_list|,
name|radix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

