begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|misc
operator|.
name|Unsafe
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|AccessController
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedAction
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
comment|/**  * Static utility methods pertaining to {@code byte} primitives that interpret  * values as<i>unsigned</i> (that is, any negative value {@code b} is treated  * as the positive value {@code 256 + b}). The corresponding methods that treat  * the values as signed are found in {@link SignedBytes}, and the methods for  * which signedness is not an issue are in {@link Bytes}.  *  * @author Kevin Bourrillion  * @author Martin Buchholz  * @author Hiroshi Yamauchi  * @since 1  */
end_comment

begin_class
DECL|class|UnsignedBytes
specifier|public
specifier|final
class|class
name|UnsignedBytes
block|{
DECL|method|UnsignedBytes ()
specifier|private
name|UnsignedBytes
parameter_list|()
block|{}
comment|/**    * Returns the value of the given byte as an integer, when treated as    * unsigned. That is, returns {@code value + 256} if {@code value} is    * negative; {@code value} itself otherwise.    *    * @since 6    */
DECL|method|toInt (byte value)
specifier|public
specifier|static
name|int
name|toInt
parameter_list|(
name|byte
name|value
parameter_list|)
block|{
return|return
name|value
operator|&
literal|0xFF
return|;
block|}
comment|/**    * Returns the {@code byte} value that, when treated as unsigned, is equal to    * {@code value}, if possible.    *    * @param value a value between 0 and 255 inclusive    * @return the {@code byte} value that, when treated as unsigned, equals    *     {@code value}    * @throws IllegalArgumentException if {@code value} is negative or greater    *     than 255    */
DECL|method|checkedCast (long value)
specifier|public
specifier|static
name|byte
name|checkedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|>>
literal|8
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
name|byte
operator|)
name|value
return|;
block|}
comment|/**    * Returns the {@code byte} value that, when treated as unsigned, is nearest    * in value to {@code value}.    *    * @param value any {@code long} value    * @return {@code (byte) 255} if {@code value>= 255}, {@code (byte) 0} if    *     {@code value<= 0}, and {@code value} cast to {@code byte} otherwise    */
DECL|method|saturatedCast (long value)
specifier|public
specifier|static
name|byte
name|saturatedCast
parameter_list|(
name|long
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|>
literal|255
condition|)
block|{
return|return
operator|(
name|byte
operator|)
literal|255
return|;
comment|// -1
block|}
if|if
condition|(
name|value
operator|<
literal|0
condition|)
block|{
return|return
operator|(
name|byte
operator|)
literal|0
return|;
block|}
return|return
operator|(
name|byte
operator|)
name|value
return|;
block|}
comment|/**    * Compares the two specified {@code byte} values, treating them as unsigned    * values between 0 and 255 inclusive. For example, {@code (byte) -127} is    * considered greater than {@code (byte) 127} because it is seen as having    * the value of positive {@code 129}.    *    * @param a the first {@code byte} to compare    * @param b the second {@code byte} to compare    * @return a negative value if {@code a} is less than {@code b}; a positive    *     value if {@code a} is greater than {@code b}; or zero if they are equal    */
DECL|method|compare (byte a, byte b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|byte
name|a
parameter_list|,
name|byte
name|b
parameter_list|)
block|{
return|return
name|toInt
argument_list|(
name|a
argument_list|)
operator|-
name|toInt
argument_list|(
name|b
argument_list|)
return|;
block|}
comment|/**    * Returns the least value present in {@code array}.    *    * @param array a<i>nonempty</i> array of {@code byte} values    * @return the value present in {@code array} that is less than or equal to    *     every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|min (byte... array)
specifier|public
specifier|static
name|byte
name|min
parameter_list|(
name|byte
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
name|toInt
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
name|toInt
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
operator|(
name|byte
operator|)
name|min
return|;
block|}
comment|/**    * Returns the greatest value present in {@code array}.    *    * @param array a<i>nonempty</i> array of {@code byte} values    * @return the value present in {@code array} that is greater than or equal    *     to every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|max (byte... array)
specifier|public
specifier|static
name|byte
name|max
parameter_list|(
name|byte
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
name|toInt
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
name|toInt
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
operator|(
name|byte
operator|)
name|max
return|;
block|}
comment|/**    * Returns a string containing the supplied {@code byte} values separated by    * {@code separator}. For example, {@code join(":", (byte) 1, (byte) 2,    * (byte) 255)} returns the string {@code "1:2:255"}.    *    * @param separator the text that should appear between consecutive values in    *     the resulting string (but not at the start or end)    * @param array an array of {@code byte} values, possibly empty    */
DECL|method|join (String separator, byte... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|byte
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
name|toInt
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
name|toInt
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
comment|/**    * Returns a comparator that compares two {@code byte} arrays    * lexicographically. That is, it compares, using {@link    * #compare(byte, byte)}), the first pair of values that follow any common    * prefix, or when one array is a prefix of the other, treats the shorter    * array as the lesser. For example, {@code []< [0x01]< [0x01, 0x7F]<    * [0x01, 0x80]< [0x02]}. Values are treated as unsigned.    *    *<p>The returned comparator is inconsistent with {@link    * Object#equals(Object)} (since arrays support only identity equality), but    * it is consistent with {@link java.util.Arrays#equals(byte[], byte[])}.    *    * @see<a href="http://en.wikipedia.org/wiki/Lexicographical_order">    *     Lexicographical order article at Wikipedia</a>    * @since 2    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|lexicographicalComparator
parameter_list|()
block|{
return|return
name|LexicographicalComparatorHolder
operator|.
name|BEST_COMPARATOR
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|lexicographicalComparatorJavaImpl ()
specifier|static
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|lexicographicalComparatorJavaImpl
parameter_list|()
block|{
return|return
name|LexicographicalComparatorHolder
operator|.
name|PureJavaComparator
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Provides a lexicographical comparator implementation; either a Java    * implementation or a faster implementation based on {@link Unsafe}.    *    *<p>Uses reflection to gracefully fall back to the Java implementation if    * {@code Unsafe} isn't available.    */
annotation|@
name|VisibleForTesting
DECL|class|LexicographicalComparatorHolder
specifier|static
class|class
name|LexicographicalComparatorHolder
block|{
DECL|field|UNSAFE_COMPARATOR_NAME
specifier|static
specifier|final
name|String
name|UNSAFE_COMPARATOR_NAME
init|=
name|LexicographicalComparatorHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"$UnsafeComparator"
decl_stmt|;
DECL|field|BEST_COMPARATOR
specifier|static
specifier|final
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|BEST_COMPARATOR
init|=
name|getBestComparator
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// only access this class via reflection!
DECL|enum|UnsafeComparator
enum|enum
name|UnsafeComparator
implements|implements
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|field|littleEndian
specifier|static
specifier|final
name|boolean
name|littleEndian
init|=
name|ByteOrder
operator|.
name|nativeOrder
argument_list|()
operator|.
name|equals
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
decl_stmt|;
comment|/*        * The following static final fields exist for performance reasons.        *        * In UnsignedBytesBenchmark, accessing the following objects via static        * final fields is the fastest (more than twice as fast as the Java        * implementation, vs ~1.5x with non-final static fields, on x86_32)        * under the Hotspot server compiler. The reason is obviously that the        * non-final fields need to be reloaded inside the loop.        *        * And, no, defining (final or not) local variables out of the loop still        * isn't as good because the null check on the theUnsafe object remains        * inside the loop and BYTE_ARRAY_BASE_OFFSET doesn't get        * constant-folded.        *        * The compiler can treat static final fields as compile-time constants        * and can constant-fold them while (final or not) local variables are        * run time values.        */
DECL|field|theUnsafe
specifier|static
specifier|final
name|Unsafe
name|theUnsafe
decl_stmt|;
comment|/** The offset to the first element in a byte array. */
DECL|field|BYTE_ARRAY_BASE_OFFSET
specifier|static
specifier|final
name|int
name|BYTE_ARRAY_BASE_OFFSET
decl_stmt|;
static|static
block|{
name|theUnsafe
operator|=
operator|(
name|Unsafe
operator|)
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|run
parameter_list|()
block|{
try|try
block|{
name|Field
name|f
init|=
name|Unsafe
operator|.
name|class
operator|.
name|getDeclaredField
argument_list|(
literal|"theUnsafe"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
comment|// It doesn't matter what we throw;
comment|// it's swallowed in getBestComparator().
throw|throw
operator|new
name|Error
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|()
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|BYTE_ARRAY_BASE_OFFSET
operator|=
name|theUnsafe
operator|.
name|arrayBaseOffset
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// sanity check - this should never fail
if|if
condition|(
name|theUnsafe
operator|.
name|arrayIndexScale
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/**        * Returns true if x1 is less than x2, when both values are treated as        * unsigned.        */
comment|// TODO(kevinb): Should be a common method in primitives.UnsignedLongs.
DECL|method|lessThanUnsigned (long x1, long x2)
specifier|static
name|boolean
name|lessThanUnsigned
parameter_list|(
name|long
name|x1
parameter_list|,
name|long
name|x2
parameter_list|)
block|{
return|return
operator|(
name|x1
operator|+
name|Long
operator|.
name|MIN_VALUE
operator|)
operator|<
operator|(
name|x2
operator|+
name|Long
operator|.
name|MIN_VALUE
operator|)
return|;
block|}
DECL|method|compare (byte[] left, byte[] right)
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|byte
index|[]
name|left
parameter_list|,
name|byte
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
name|int
name|minWords
init|=
name|minLength
operator|/
name|Longs
operator|.
name|BYTES
decl_stmt|;
comment|/*          * Compare 8 bytes at a time. Benchmarking shows comparing 8 bytes at a          * time is no slower than comparing 4 bytes at a time even on 32-bit.          * On the other hand, it is substantially faster on 64-bit.          */
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|minWords
operator|*
name|Longs
operator|.
name|BYTES
condition|;
name|i
operator|+=
name|Longs
operator|.
name|BYTES
control|)
block|{
name|long
name|lw
init|=
name|theUnsafe
operator|.
name|getLong
argument_list|(
name|left
argument_list|,
name|BYTE_ARRAY_BASE_OFFSET
operator|+
operator|(
name|long
operator|)
name|i
argument_list|)
decl_stmt|;
name|long
name|rw
init|=
name|theUnsafe
operator|.
name|getLong
argument_list|(
name|right
argument_list|,
name|BYTE_ARRAY_BASE_OFFSET
operator|+
operator|(
name|long
operator|)
name|i
argument_list|)
decl_stmt|;
name|long
name|diff
init|=
name|lw
operator|^
name|rw
decl_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
operator|!
name|littleEndian
condition|)
block|{
return|return
name|lessThanUnsigned
argument_list|(
name|lw
argument_list|,
name|rw
argument_list|)
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
comment|// Use binary search
name|int
name|n
init|=
literal|0
decl_stmt|;
name|int
name|y
decl_stmt|;
name|int
name|x
init|=
operator|(
name|int
operator|)
name|diff
decl_stmt|;
if|if
condition|(
name|x
operator|==
literal|0
condition|)
block|{
name|x
operator|=
call|(
name|int
call|)
argument_list|(
name|diff
operator|>>>
literal|32
argument_list|)
expr_stmt|;
name|n
operator|=
literal|32
expr_stmt|;
block|}
name|y
operator|=
name|x
operator|<<
literal|16
expr_stmt|;
if|if
condition|(
name|y
operator|==
literal|0
condition|)
block|{
name|n
operator|+=
literal|16
expr_stmt|;
block|}
else|else
block|{
name|x
operator|=
name|y
expr_stmt|;
block|}
name|y
operator|=
name|x
operator|<<
literal|8
expr_stmt|;
if|if
condition|(
name|y
operator|==
literal|0
condition|)
block|{
name|n
operator|+=
literal|8
expr_stmt|;
block|}
return|return
call|(
name|int
call|)
argument_list|(
operator|(
operator|(
name|lw
operator|>>>
name|n
operator|)
operator|&
literal|0xFFL
operator|)
operator|-
operator|(
operator|(
name|rw
operator|>>>
name|n
operator|)
operator|&
literal|0xFFL
operator|)
argument_list|)
return|;
block|}
block|}
comment|// The epilogue to cover the last (minLength % 8) elements.
for|for
control|(
name|int
name|i
init|=
name|minWords
operator|*
name|Longs
operator|.
name|BYTES
init|;
name|i
operator|<
name|minLength
condition|;
name|i
operator|++
control|)
block|{
name|int
name|result
init|=
name|UnsignedBytes
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
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
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
DECL|enum|PureJavaComparator
enum|enum
name|PureJavaComparator
implements|implements
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|method|compare (byte[] left, byte[] right)
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|byte
index|[]
name|left
parameter_list|,
name|byte
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
name|int
name|result
init|=
name|UnsignedBytes
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
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
condition|)
block|{
return|return
name|result
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
comment|/**      * Returns the Unsafe-using Comparator, or falls back to the pure-Java      * implementation if unable to do so.      */
DECL|method|getBestComparator ()
specifier|static
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|getBestComparator
parameter_list|()
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|theClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|UNSAFE_COMPARATOR_NAME
argument_list|)
decl_stmt|;
comment|// yes, UnsafeComparator does implement Comparator<byte[]>
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
name|comparator
init|=
operator|(
name|Comparator
argument_list|<
name|byte
index|[]
argument_list|>
operator|)
name|theClass
operator|.
name|getEnumConstants
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
return|return
name|comparator
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ensure we really catch *everything*
return|return
name|lexicographicalComparatorJavaImpl
argument_list|()
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

