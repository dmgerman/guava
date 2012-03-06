begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|math
operator|.
name|BigInteger
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
comment|/**  * Static utility methods pertaining to {@code long} primitives that interpret values as  *<i>unsigned</i> (that is, any negative value {@code x} is treated as the positive value  * {@code 2^64 + x}). The methods for which signedness is not an issue are in {@link Longs}, as  * well as signed versions of methods for which signedness is an issue.  *   *<p>In addition, this class provides several static methods for converting a {@code long} to a  * {@code String} and a {@code String} to a {@code long} that treat the {@code long} as an unsigned  * number.  *   *<p>Users of these utilities must be<i>extremely careful</i> not to mix up signed and unsigned  * {@code long} values. When possible, it is recommended that the {@link UnsignedLong} wrapper   * class be used, at a small efficiency penalty, to enforce the distinction in the type system.  *   *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/PrimitivesExplained#Unsigned_support">  * unsigned primitive utilities</a>.  *   * @author Louis Wasserman  * @author Brian Milch  * @author Colin Evans  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|UnsignedLongs
specifier|public
specifier|final
class|class
name|UnsignedLongs
block|{
DECL|method|UnsignedLongs ()
specifier|private
name|UnsignedLongs
parameter_list|()
block|{}
DECL|field|MAX_VALUE
specifier|public
specifier|static
specifier|final
name|long
name|MAX_VALUE
init|=
operator|-
literal|1L
decl_stmt|;
comment|// Equivalent to 2^64 - 1
comment|/**    * A (self-inverse) bijection which converts the ordering on unsigned longs to the ordering on    * longs, that is, {@code a<= b} as unsigned longs if and only if {@code rotate(a)<= rotate(b)}    * as signed longs.    */
DECL|method|flip (long a)
specifier|private
specifier|static
name|long
name|flip
parameter_list|(
name|long
name|a
parameter_list|)
block|{
return|return
name|a
operator|^
name|Long
operator|.
name|MIN_VALUE
return|;
block|}
comment|/**    * Compares the two specified {@code long} values, treating them as unsigned values between    * {@code 0} and {@code 2^64 - 1} inclusive.    *     * @param a the first unsigned {@code long} to compare    * @param b the second unsigned {@code long} to compare    * @return a negative value if {@code a} is less than {@code b}; a positive value if {@code a} is    *         greater than {@code b}; or zero if they are equal    */
DECL|method|compare (long a, long b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|long
name|a
parameter_list|,
name|long
name|b
parameter_list|)
block|{
return|return
name|Longs
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
comment|/**    * Returns the least value present in {@code array}, treating values as unsigned.    *     * @param array a<i>nonempty</i> array of unsigned {@code long} values    * @return the value present in {@code array} that is less than or equal to every other value in    *         the array according to {@link #compare}    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|min (long... array)
specifier|public
specifier|static
name|long
name|min
parameter_list|(
name|long
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
name|long
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
name|long
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
comment|/**    * Returns the greatest value present in {@code array}, treating values as unsigned.    *     * @param array a<i>nonempty</i> array of unsigned {@code long} values    * @return the value present in {@code array} that is greater than or equal to every other value    *         in the array according to {@link #compare}    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|max (long... array)
specifier|public
specifier|static
name|long
name|max
parameter_list|(
name|long
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
name|long
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
name|long
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
comment|/**    * Returns a string containing the supplied unsigned {@code long} values separated by    * {@code separator}. For example, {@code join("-", 1, 2, 3)} returns the string {@code "1-2-3"}.    *     * @param separator the text that should appear between consecutive values in the resulting    *        string (but not at the start or end)    * @param array an array of unsigned {@code long} values, possibly empty    */
DECL|method|join (String separator, long... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|long
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
name|array
index|[
literal|0
index|]
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
comment|/**    * Returns a comparator that compares two arrays of unsigned {@code long} values    * lexicographically. That is, it compares, using {@link #compare(long, long)}), the first pair of    * values that follow any common prefix, or when one array is a prefix of the other, treats the    * shorter array as the lesser. For example, {@code []< [1L]< [1L, 2L]< [2L]< [1L<< 63]}.    *     *<p>The returned comparator is inconsistent with {@link Object#equals(Object)} (since arrays    * support only identity equality), but it is consistent with    * {@link Arrays#equals(long[], long[])}.    *     * @see<a href="http://en.wikipedia.org/wiki/Lexicographical_order">Lexicographical order    *      article at Wikipedia</a>    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|long
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
name|long
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|compare (long[] left, long[] right)
specifier|public
name|int
name|compare
parameter_list|(
name|long
index|[]
name|left
parameter_list|,
name|long
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
name|UnsignedLongs
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
block|}
comment|/**    * Returns dividend / divisor, where the dividend and divisor are treated as unsigned 64-bit    * quantities.    *     * @param dividend the dividend (numerator)    * @param divisor the divisor (denominator)    * @throws ArithmeticException if divisor is 0    */
DECL|method|divide (long dividend, long divisor)
specifier|public
specifier|static
name|long
name|divide
parameter_list|(
name|long
name|dividend
parameter_list|,
name|long
name|divisor
parameter_list|)
block|{
if|if
condition|(
name|divisor
operator|<
literal|0
condition|)
block|{
comment|// i.e., divisor>= 2^63:
if|if
condition|(
name|compare
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
literal|0
return|;
comment|// dividend< divisor
block|}
else|else
block|{
return|return
literal|1
return|;
comment|// dividend>= divisor
block|}
block|}
comment|// Optimization - use signed division if dividend< 2^63
if|if
condition|(
name|dividend
operator|>=
literal|0
condition|)
block|{
return|return
name|dividend
operator|/
name|divisor
return|;
block|}
comment|/*      * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is      * guaranteed to be either exact or one less than the correct value. This follows from fact      * that floor(floor(x)/i) == floor(x/i) for any real x and integer i != 0. The proof is not      * quite trivial.      */
name|long
name|quotient
init|=
operator|(
operator|(
name|dividend
operator|>>>
literal|1
operator|)
operator|/
name|divisor
operator|)
operator|<<
literal|1
decl_stmt|;
name|long
name|rem
init|=
name|dividend
operator|-
name|quotient
operator|*
name|divisor
decl_stmt|;
return|return
name|quotient
operator|+
operator|(
name|compare
argument_list|(
name|rem
argument_list|,
name|divisor
argument_list|)
operator|>=
literal|0
condition|?
literal|1
else|:
literal|0
operator|)
return|;
block|}
comment|/**    * Returns dividend % divisor, where the dividend and divisor are treated as unsigned 64-bit    * quantities.    *     * @param dividend the dividend (numerator)    * @param divisor the divisor (denominator)    * @throws ArithmeticException if divisor is 0    * @since 11.0    */
DECL|method|remainder (long dividend, long divisor)
specifier|public
specifier|static
name|long
name|remainder
parameter_list|(
name|long
name|dividend
parameter_list|,
name|long
name|divisor
parameter_list|)
block|{
if|if
condition|(
name|divisor
operator|<
literal|0
condition|)
block|{
comment|// i.e., divisor>= 2^63:
if|if
condition|(
name|compare
argument_list|(
name|dividend
argument_list|,
name|divisor
argument_list|)
operator|<
literal|0
condition|)
block|{
return|return
name|dividend
return|;
comment|// dividend< divisor
block|}
else|else
block|{
return|return
name|dividend
operator|-
name|divisor
return|;
comment|// dividend>= divisor
block|}
block|}
comment|// Optimization - use signed modulus if dividend< 2^63
if|if
condition|(
name|dividend
operator|>=
literal|0
condition|)
block|{
return|return
name|dividend
operator|%
name|divisor
return|;
block|}
comment|/*      * Otherwise, approximate the quotient, check, and correct if necessary. Our approximation is      * guaranteed to be either exact or one less than the correct value. This follows from fact      * that floor(floor(x)/i) == floor(x/i) for any real x and integer i != 0. The proof is not      * quite trivial.      */
name|long
name|quotient
init|=
operator|(
operator|(
name|dividend
operator|>>>
literal|1
operator|)
operator|/
name|divisor
operator|)
operator|<<
literal|1
decl_stmt|;
name|long
name|rem
init|=
name|dividend
operator|-
name|quotient
operator|*
name|divisor
decl_stmt|;
return|return
name|rem
operator|-
operator|(
name|compare
argument_list|(
name|rem
argument_list|,
name|divisor
argument_list|)
operator|>=
literal|0
condition|?
name|divisor
else|:
literal|0
operator|)
return|;
block|}
comment|/**    * Returns the unsigned {@code long} value represented by the given decimal string.    *     * @throws NumberFormatException if the string does not contain a valid unsigned {@code long}    *         value    */
DECL|method|parseUnsignedLong (String s)
specifier|public
specifier|static
name|long
name|parseUnsignedLong
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|parseUnsignedLong
argument_list|(
name|s
argument_list|,
literal|10
argument_list|)
return|;
block|}
comment|/**    * Returns the unsigned {@code long} value represented by a string with the given radix.    *     * @param s the string containing the unsigned {@code long} representation to be parsed.    * @param radix the radix to use while parsing {@code s}    * @throws NumberFormatException if the string does not contain a valid unsigned {@code long}    *         with the given radix, or if {@code radix} is not between {@link Character#MIN_RADIX}    *         and {@link Character#MAX_RADIX}.    */
DECL|method|parseUnsignedLong (String s, int radix)
specifier|public
specifier|static
name|long
name|parseUnsignedLong
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"empty string"
argument_list|)
throw|;
block|}
if|if
condition|(
name|radix
argument_list|<
name|Character
operator|.
name|MIN_RADIX
operator|||
name|radix
argument_list|>
name|Character
operator|.
name|MAX_RADIX
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"illegal radix: "
operator|+
name|radix
argument_list|)
throw|;
block|}
name|int
name|max_safe_pos
init|=
name|maxSafeDigits
index|[
name|radix
index|]
operator|-
literal|1
decl_stmt|;
name|long
name|value
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|pos
init|=
literal|0
init|;
name|pos
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|pos
operator|++
control|)
block|{
name|int
name|digit
init|=
name|Character
operator|.
name|digit
argument_list|(
name|s
operator|.
name|charAt
argument_list|(
name|pos
argument_list|)
argument_list|,
name|radix
argument_list|)
decl_stmt|;
if|if
condition|(
name|digit
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
name|s
argument_list|)
throw|;
block|}
if|if
condition|(
name|pos
operator|>
name|max_safe_pos
operator|&&
name|overflowInParse
argument_list|(
name|value
argument_list|,
name|digit
argument_list|,
name|radix
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NumberFormatException
argument_list|(
literal|"Too large for unsigned long: "
operator|+
name|s
argument_list|)
throw|;
block|}
name|value
operator|=
operator|(
name|value
operator|*
name|radix
operator|)
operator|+
name|digit
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|/**    * Returns true if (current * radix) + digit is a number too large to be represented by an    * unsigned long. This is useful for detecting overflow while parsing a string representation of    * a number. Does not verify whether supplied radix is valid, passing an invalid radix will give    * undefined results or an ArrayIndexOutOfBoundsException.    */
DECL|method|overflowInParse (long current, int digit, int radix)
specifier|private
specifier|static
name|boolean
name|overflowInParse
parameter_list|(
name|long
name|current
parameter_list|,
name|int
name|digit
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
if|if
condition|(
name|current
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|current
operator|<
name|maxValueDivs
index|[
name|radix
index|]
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|current
operator|>
name|maxValueDivs
index|[
name|radix
index|]
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// current == maxValueDivs[radix]
return|return
operator|(
name|digit
operator|>
name|maxValueMods
index|[
name|radix
index|]
operator|)
return|;
block|}
comment|// current< 0: high bit is set
return|return
literal|true
return|;
block|}
comment|/**    * Returns a string representation of x, where x is treated as unsigned.    */
DECL|method|toString (long x)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|long
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
comment|/**    * Returns a string representation of {@code x} for the given radix, where {@code x} is treated    * as unsigned.    *     * @param x the value to convert to a string.    * @param radix the radix to use while working with {@code x}    * @throws IllegalArgumentException if {@code radix} is not between {@link Character#MIN_RADIX}    *         and {@link Character#MAX_RADIX}.    */
DECL|method|toString (long x, int radix)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|long
name|x
parameter_list|,
name|int
name|radix
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|radix
operator|>=
name|Character
operator|.
name|MIN_RADIX
operator|&&
name|radix
operator|<=
name|Character
operator|.
name|MAX_RADIX
argument_list|,
literal|"radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX"
argument_list|,
name|radix
argument_list|)
expr_stmt|;
if|if
condition|(
name|x
operator|==
literal|0
condition|)
block|{
comment|// Simply return "0"
return|return
literal|"0"
return|;
block|}
else|else
block|{
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|64
index|]
decl_stmt|;
name|int
name|i
init|=
name|buf
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|x
operator|<
literal|0
condition|)
block|{
comment|// Separate off the last digit using unsigned division. That will leave
comment|// a number that is nonnegative as a signed integer.
name|long
name|quotient
init|=
name|divide
argument_list|(
name|x
argument_list|,
name|radix
argument_list|)
decl_stmt|;
name|long
name|rem
init|=
name|x
operator|-
name|quotient
operator|*
name|radix
decl_stmt|;
name|buf
index|[
operator|--
name|i
index|]
operator|=
name|Character
operator|.
name|forDigit
argument_list|(
operator|(
name|int
operator|)
name|rem
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|x
operator|=
name|quotient
expr_stmt|;
block|}
comment|// Simple modulo/division approach
while|while
condition|(
name|x
operator|>
literal|0
condition|)
block|{
name|buf
index|[
operator|--
name|i
index|]
operator|=
name|Character
operator|.
name|forDigit
argument_list|(
call|(
name|int
call|)
argument_list|(
name|x
operator|%
name|radix
argument_list|)
argument_list|,
name|radix
argument_list|)
expr_stmt|;
name|x
operator|/=
name|radix
expr_stmt|;
block|}
comment|// Generate string
return|return
operator|new
name|String
argument_list|(
name|buf
argument_list|,
name|i
argument_list|,
name|buf
operator|.
name|length
operator|-
name|i
argument_list|)
return|;
block|}
block|}
comment|// calculated as 0xffffffffffffffff / radix
DECL|field|maxValueDivs
specifier|private
specifier|static
specifier|final
name|long
index|[]
name|maxValueDivs
init|=
operator|new
name|long
index|[
name|Character
operator|.
name|MAX_RADIX
operator|+
literal|1
index|]
decl_stmt|;
DECL|field|maxValueMods
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|maxValueMods
init|=
operator|new
name|int
index|[
name|Character
operator|.
name|MAX_RADIX
operator|+
literal|1
index|]
decl_stmt|;
DECL|field|maxSafeDigits
specifier|private
specifier|static
specifier|final
name|int
index|[]
name|maxSafeDigits
init|=
operator|new
name|int
index|[
name|Character
operator|.
name|MAX_RADIX
operator|+
literal|1
index|]
decl_stmt|;
static|static
block|{
name|BigInteger
name|overflow
init|=
operator|new
name|BigInteger
argument_list|(
literal|"10000000000000000"
argument_list|,
literal|16
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|Character
operator|.
name|MIN_RADIX
init|;
name|i
operator|<=
name|Character
operator|.
name|MAX_RADIX
condition|;
name|i
operator|++
control|)
block|{
name|maxValueDivs
index|[
name|i
index|]
operator|=
name|divide
argument_list|(
name|MAX_VALUE
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|maxValueMods
index|[
name|i
index|]
operator|=
operator|(
name|int
operator|)
name|remainder
argument_list|(
name|MAX_VALUE
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|maxSafeDigits
index|[
name|i
index|]
operator|=
name|overflow
operator|.
name|toString
argument_list|(
name|i
argument_list|)
operator|.
name|length
argument_list|()
operator|-
literal|1
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

