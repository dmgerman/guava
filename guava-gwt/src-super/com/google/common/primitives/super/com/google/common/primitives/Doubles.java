begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkElementIndex
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
name|base
operator|.
name|Preconditions
operator|.
name|checkPositionIndexes
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
name|NEGATIVE_INFINITY
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
name|base
operator|.
name|Converter
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
name|util
operator|.
name|AbstractList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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
comment|/**  * Static utility methods pertaining to {@code double} primitives, that are not  * already found in either {@link Double} or {@link Arrays}.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/PrimitivesExplained">  * primitive utilities</a>.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Doubles
specifier|public
specifier|final
class|class
name|Doubles
block|{
DECL|method|Doubles ()
specifier|private
name|Doubles
parameter_list|()
block|{}
comment|/**    * The number of bytes required to represent a primitive {@code double}    * value.    *    * @since 10.0    */
DECL|field|BYTES
specifier|public
specifier|static
specifier|final
name|int
name|BYTES
init|=
name|Double
operator|.
name|SIZE
operator|/
name|Byte
operator|.
name|SIZE
decl_stmt|;
comment|/**    * Returns a hash code for {@code value}; equal to the result of invoking    * {@code ((Double) value).hashCode()}.    *    * @param value a primitive {@code double} value    * @return a hash code for the value    */
DECL|method|hashCode (double value)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|double
name|value
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Double
operator|)
name|value
operator|)
operator|.
name|hashCode
argument_list|()
return|;
comment|// TODO(kevinb): do it this way when we can (GWT problem):
comment|// long bits = Double.doubleToLongBits(value);
comment|// return (int) (bits ^ (bits>>> 32));
block|}
comment|/**    * Compares the two specified {@code double} values. The sign of the value    * returned is the same as that of<code>((Double) a).{@linkplain    * Double#compareTo compareTo}(b)</code>. As with that method, {@code NaN} is    * treated as greater than all other values, and {@code 0.0> -0.0}.    *    *<p><b>Note:</b> this method simply delegates to the JDK method {@link    * Double#compare}. It is provided for consistency with the other primitive    * types, whose compare methods were not added to the JDK until JDK 7.    *    * @param a the first {@code double} to compare    * @param b the second {@code double} to compare    * @return a negative value if {@code a} is less than {@code b}; a positive    *     value if {@code a} is greater than {@code b}; or zero if they are equal    */
DECL|method|compare (double a, double b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|double
name|a
parameter_list|,
name|double
name|b
parameter_list|)
block|{
return|return
name|Double
operator|.
name|compare
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code value} represents a real number. This is    * equivalent to, but not necessarily implemented as,    * {@code !(Double.isInfinite(value) || Double.isNaN(value))}.    *    * @since 10.0    */
DECL|method|isFinite (double value)
specifier|public
specifier|static
name|boolean
name|isFinite
parameter_list|(
name|double
name|value
parameter_list|)
block|{
return|return
name|NEGATIVE_INFINITY
operator|<
name|value
operator|&
name|value
operator|<
name|POSITIVE_INFINITY
return|;
block|}
comment|/**    * Returns {@code true} if {@code target} is present as an element anywhere in    * {@code array}. Note that this always returns {@code false} when {@code    * target} is {@code NaN}.    *    * @param array an array of {@code double} values, possibly empty    * @param target a primitive {@code double} value    * @return {@code true} if {@code array[i] == target} for some value of {@code    *     i}    */
DECL|method|contains (double[] array, double target)
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
name|target
parameter_list|)
block|{
for|for
control|(
name|double
name|value
range|:
name|array
control|)
block|{
if|if
condition|(
name|value
operator|==
name|target
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Returns the index of the first appearance of the value {@code target} in    * {@code array}. Note that this always returns {@code -1} when {@code target}    * is {@code NaN}.    *    * @param array an array of {@code double} values, possibly empty    * @param target a primitive {@code double} value    * @return the least index {@code i} for which {@code array[i] == target}, or    *     {@code -1} if no such index exists.    */
DECL|method|indexOf (double[] array, double target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
name|target
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|array
argument_list|,
name|target
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
return|;
block|}
comment|// TODO(kevinb): consider making this public
DECL|method|indexOf ( double[] array, double target, int start, int end)
specifier|private
specifier|static
name|int
name|indexOf
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
name|target
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
index|]
operator|==
name|target
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**    * Returns the start position of the first occurrence of the specified {@code    * target} within {@code array}, or {@code -1} if there is no such occurrence.    *    *<p>More formally, returns the lowest index {@code i} such that {@code    * java.util.Arrays.copyOfRange(array, i, i + target.length)} contains exactly    * the same elements as {@code target}.    *    *<p>Note that this always returns {@code -1} when {@code target} contains    * {@code NaN}.    *    * @param array the array to search for the sequence {@code target}    * @param target the array to search for as a sub-sequence of {@code array}    */
DECL|method|indexOf (double[] array, double[] target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
index|[]
name|target
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|array
argument_list|,
literal|"array"
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|target
argument_list|,
literal|"target"
argument_list|)
expr_stmt|;
if|if
condition|(
name|target
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
name|outer
label|:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
operator|-
name|target
operator|.
name|length
operator|+
literal|1
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|target
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
operator|+
name|j
index|]
operator|!=
name|target
index|[
name|j
index|]
condition|)
block|{
continue|continue
name|outer
continue|;
block|}
block|}
return|return
name|i
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**    * Returns the index of the last appearance of the value {@code target} in    * {@code array}. Note that this always returns {@code -1} when {@code target}    * is {@code NaN}.    *    * @param array an array of {@code double} values, possibly empty    * @param target a primitive {@code double} value    * @return the greatest index {@code i} for which {@code array[i] == target},    *     or {@code -1} if no such index exists.    */
DECL|method|lastIndexOf (double[] array, double target)
specifier|public
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
name|target
parameter_list|)
block|{
return|return
name|lastIndexOf
argument_list|(
name|array
argument_list|,
name|target
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
return|;
block|}
comment|// TODO(kevinb): consider making this public
DECL|method|lastIndexOf ( double[] array, double target, int start, int end)
specifier|private
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|double
name|target
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|end
operator|-
literal|1
init|;
name|i
operator|>=
name|start
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|array
index|[
name|i
index|]
operator|==
name|target
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
comment|/**    * Returns the least value present in {@code array}, using the same rules of    * comparison as {@link Math#min(double, double)}.    *    * @param array a<i>nonempty</i> array of {@code double} values    * @return the value present in {@code array} that is less than or equal to    *     every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|min (double... array)
specifier|public
specifier|static
name|double
name|min
parameter_list|(
name|double
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
name|double
name|min
init|=
name|array
index|[
literal|0
index|]
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
name|min
operator|=
name|Math
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|min
return|;
block|}
comment|/**    * Returns the greatest value present in {@code array}, using the same rules    * of comparison as {@link Math#max(double, double)}.    *    * @param array a<i>nonempty</i> array of {@code double} values    * @return the value present in {@code array} that is greater than or equal to    *     every other value in the array    * @throws IllegalArgumentException if {@code array} is empty    */
DECL|method|max (double... array)
specifier|public
specifier|static
name|double
name|max
parameter_list|(
name|double
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
name|double
name|max
init|=
name|array
index|[
literal|0
index|]
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
name|max
operator|=
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|max
return|;
block|}
comment|/**    * Returns the values from each provided array combined into a single array.    * For example, {@code concat(new double[] {a, b}, new double[] {}, new    * double[] {c}} returns the array {@code {a, b, c}}.    *    * @param arrays zero or more {@code double} arrays    * @return a single array containing all the values from the source arrays, in    *     order    */
DECL|method|concat (double[]... arrays)
specifier|public
specifier|static
name|double
index|[]
name|concat
parameter_list|(
name|double
index|[]
modifier|...
name|arrays
parameter_list|)
block|{
name|int
name|length
init|=
literal|0
decl_stmt|;
for|for
control|(
name|double
index|[]
name|array
range|:
name|arrays
control|)
block|{
name|length
operator|+=
name|array
operator|.
name|length
expr_stmt|;
block|}
name|double
index|[]
name|result
init|=
operator|new
name|double
index|[
name|length
index|]
decl_stmt|;
name|int
name|pos
init|=
literal|0
decl_stmt|;
for|for
control|(
name|double
index|[]
name|array
range|:
name|arrays
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|pos
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|pos
operator|+=
name|array
operator|.
name|length
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|class|DoubleConverter
specifier|private
specifier|static
specifier|final
class|class
name|DoubleConverter
extends|extends
name|Converter
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|DoubleConverter
name|INSTANCE
init|=
operator|new
name|DoubleConverter
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|doForward (String value)
specifier|protected
name|Double
name|doForward
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
name|Double
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doBackward (Double value)
specifier|protected
name|String
name|doBackward
parameter_list|(
name|Double
name|value
parameter_list|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
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
literal|"Doubles.stringConverter()"
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
block|}
comment|/**    * Returns a serializable converter object that converts between strings and    * doubles using {@link Double#valueOf} and {@link Double#toString()}.    *    * @since 16.0    */
annotation|@
name|Beta
DECL|method|stringConverter ()
specifier|public
specifier|static
name|Converter
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
name|stringConverter
parameter_list|()
block|{
return|return
name|DoubleConverter
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Returns an array containing the same values as {@code array}, but    * guaranteed to be of a specified minimum length. If {@code array} already    * has a length of at least {@code minLength}, it is returned directly.    * Otherwise, a new array of size {@code minLength + padding} is returned,    * containing the values of {@code array}, and zeroes in the remaining places.    *    * @param array the source array    * @param minLength the minimum length the returned array must guarantee    * @param padding an extra amount to "grow" the array by if growth is    *     necessary    * @throws IllegalArgumentException if {@code minLength} or {@code padding} is    *     negative    * @return an array containing the values of {@code array}, with guaranteed    *     minimum length {@code minLength}    */
DECL|method|ensureCapacity ( double[] array, int minLength, int padding)
specifier|public
specifier|static
name|double
index|[]
name|ensureCapacity
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|int
name|minLength
parameter_list|,
name|int
name|padding
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|minLength
operator|>=
literal|0
argument_list|,
literal|"Invalid minLength: %s"
argument_list|,
name|minLength
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|padding
operator|>=
literal|0
argument_list|,
literal|"Invalid padding: %s"
argument_list|,
name|padding
argument_list|)
expr_stmt|;
return|return
operator|(
name|array
operator|.
name|length
operator|<
name|minLength
operator|)
condition|?
name|copyOf
argument_list|(
name|array
argument_list|,
name|minLength
operator|+
name|padding
argument_list|)
else|:
name|array
return|;
block|}
comment|// Arrays.copyOf() requires Java 6
DECL|method|copyOf (double[] original, int length)
specifier|private
specifier|static
name|double
index|[]
name|copyOf
parameter_list|(
name|double
index|[]
name|original
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|double
index|[]
name|copy
init|=
operator|new
name|double
index|[
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|original
argument_list|,
literal|0
argument_list|,
name|copy
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|original
operator|.
name|length
argument_list|,
name|length
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**    * Returns a string containing the supplied {@code double} values, converted    * to strings as specified by {@link Double#toString(double)}, and separated    * by {@code separator}. For example, {@code join("-", 1.0, 2.0, 3.0)} returns    * the string {@code "1.0-2.0-3.0"}.    *    *<p>Note that {@link Double#toString(double)} formats {@code double}    * differently in GWT sometimes.  In the previous example, it returns the    * string {@code "1-2-3"}.    *    * @param separator the text that should appear between consecutive values in    *     the resulting string (but not at the start or end)    * @param array an array of {@code double} values, possibly empty    */
DECL|method|join (String separator, double... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|double
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
literal|12
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
name|array
index|[
name|i
index|]
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
comment|/**    * Returns a comparator that compares two {@code double} arrays    * lexicographically. That is, it compares, using {@link    * #compare(double, double)}), the first pair of values that follow any    * common prefix, or when one array is a prefix of the other, treats the    * shorter array as the lesser. For example,    * {@code []< [1.0]< [1.0, 2.0]< [2.0]}.    *    *<p>The returned comparator is inconsistent with {@link    * Object#equals(Object)} (since arrays support only identity equality), but    * it is consistent with {@link Arrays#equals(double[], double[])}.    *    * @see<a href="http://en.wikipedia.org/wiki/Lexicographical_order">    *     Lexicographical order article at Wikipedia</a>    * @since 2.0    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|double
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
specifier|private
enum|enum
name|LexicographicalComparator
implements|implements
name|Comparator
argument_list|<
name|double
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|compare (double[] left, double[] right)
specifier|public
name|int
name|compare
parameter_list|(
name|double
index|[]
name|left
parameter_list|,
name|double
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
name|Double
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
comment|/**    * Returns an array containing each value of {@code collection}, converted to    * a {@code double} value in the manner of {@link Number#doubleValue}.    *    *<p>Elements are copied from the argument collection as if by {@code    * collection.toArray()}.  Calling this method is as thread-safe as calling    * that method.    *    * @param collection a collection of {@code Number} instances    * @return an array containing the same values as {@code collection}, in the    *     same order, converted to primitives    * @throws NullPointerException if {@code collection} or any of its elements    *     is null    * @since 1.0 (parameter was {@code Collection<Double>} before 12.0)    */
DECL|method|toArray (Collection<? extends Number> collection)
specifier|public
specifier|static
name|double
index|[]
name|toArray
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|instanceof
name|DoubleArrayAsList
condition|)
block|{
return|return
operator|(
operator|(
name|DoubleArrayAsList
operator|)
name|collection
operator|)
operator|.
name|toDoubleArray
argument_list|()
return|;
block|}
name|Object
index|[]
name|boxedArray
init|=
name|collection
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|boxedArray
operator|.
name|length
decl_stmt|;
name|double
index|[]
name|array
init|=
operator|new
name|double
index|[
name|len
index|]
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
name|len
condition|;
name|i
operator|++
control|)
block|{
comment|// checkNotNull for GWT (do not optimize)
name|array
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|Number
operator|)
name|checkNotNull
argument_list|(
name|boxedArray
index|[
name|i
index|]
argument_list|)
operator|)
operator|.
name|doubleValue
argument_list|()
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**    * Returns a fixed-size list backed by the specified array, similar to {@link    * Arrays#asList(Object[])}. The list supports {@link List#set(int, Object)},    * but any attempt to set a value to {@code null} will result in a {@link    * NullPointerException}.    *    *<p>The returned list maintains the values, but not the identities, of    * {@code Double} objects written to or read from it.  For example, whether    * {@code list.get(0) == list.get(0)} is true for the returned list is    * unspecified.    *    *<p>The returned list may have unexpected behavior if it contains {@code    * NaN}, or if {@code NaN} is used as a parameter to any of its methods.    *    * @param backingArray the array to back the list    * @return a list view of the array    */
DECL|method|asList (double... backingArray)
specifier|public
specifier|static
name|List
argument_list|<
name|Double
argument_list|>
name|asList
parameter_list|(
name|double
modifier|...
name|backingArray
parameter_list|)
block|{
if|if
condition|(
name|backingArray
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
operator|new
name|DoubleArrayAsList
argument_list|(
name|backingArray
argument_list|)
return|;
block|}
annotation|@
name|GwtCompatible
DECL|class|DoubleArrayAsList
specifier|private
specifier|static
class|class
name|DoubleArrayAsList
extends|extends
name|AbstractList
argument_list|<
name|Double
argument_list|>
implements|implements
name|RandomAccess
implements|,
name|Serializable
block|{
DECL|field|array
specifier|final
name|double
index|[]
name|array
decl_stmt|;
DECL|field|start
specifier|final
name|int
name|start
decl_stmt|;
DECL|field|end
specifier|final
name|int
name|end
decl_stmt|;
DECL|method|DoubleArrayAsList (double[] array)
name|DoubleArrayAsList
parameter_list|(
name|double
index|[]
name|array
parameter_list|)
block|{
name|this
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|DoubleArrayAsList (double[] array, int start, int end)
name|DoubleArrayAsList
parameter_list|(
name|double
index|[]
name|array
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
name|this
operator|.
name|array
operator|=
name|array
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|start
expr_stmt|;
name|this
operator|.
name|end
operator|=
name|end
expr_stmt|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|end
operator|-
name|start
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|get (int index)
annotation|@
name|Override
specifier|public
name|Double
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
index|[
name|start
operator|+
name|index
index|]
return|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
comment|// Overridden to prevent a ton of boxing
return|return
operator|(
name|target
operator|instanceof
name|Double
operator|)
operator|&&
name|Doubles
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Double
operator|)
name|target
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
DECL|method|indexOf (Object target)
annotation|@
name|Override
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
comment|// Overridden to prevent a ton of boxing
if|if
condition|(
name|target
operator|instanceof
name|Double
condition|)
block|{
name|int
name|i
init|=
name|Doubles
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Double
operator|)
name|target
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>=
literal|0
condition|)
block|{
return|return
name|i
operator|-
name|start
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|method|lastIndexOf (Object target)
annotation|@
name|Override
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
comment|// Overridden to prevent a ton of boxing
if|if
condition|(
name|target
operator|instanceof
name|Double
condition|)
block|{
name|int
name|i
init|=
name|Doubles
operator|.
name|lastIndexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Double
operator|)
name|target
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>=
literal|0
condition|)
block|{
return|return
name|i
operator|-
name|start
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|method|set (int index, Double element)
annotation|@
name|Override
specifier|public
name|Double
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|Double
name|element
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|double
name|oldValue
init|=
name|array
index|[
name|start
operator|+
name|index
index|]
decl_stmt|;
comment|// checkNotNull for GWT (do not optimize)
name|array
index|[
name|start
operator|+
name|index
index|]
operator|=
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|oldValue
return|;
block|}
DECL|method|subList (int fromIndex, int toIndex)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Double
argument_list|>
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
name|checkPositionIndexes
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|,
name|size
argument_list|)
expr_stmt|;
if|if
condition|(
name|fromIndex
operator|==
name|toIndex
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
operator|new
name|DoubleArrayAsList
argument_list|(
name|array
argument_list|,
name|start
operator|+
name|fromIndex
argument_list|,
name|start
operator|+
name|toIndex
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|DoubleArrayAsList
condition|)
block|{
name|DoubleArrayAsList
name|that
init|=
operator|(
name|DoubleArrayAsList
operator|)
name|object
decl_stmt|;
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|that
operator|.
name|size
argument_list|()
operator|!=
name|size
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|array
index|[
name|start
operator|+
name|i
index|]
operator|!=
name|that
operator|.
name|array
index|[
name|that
operator|.
name|start
operator|+
name|i
index|]
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
return|return
name|super
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|Doubles
operator|.
name|hashCode
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|size
argument_list|()
operator|*
literal|12
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
operator|.
name|append
argument_list|(
name|array
index|[
name|start
index|]
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|start
operator|+
literal|1
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|toDoubleArray ()
name|double
index|[]
name|toDoubleArray
parameter_list|()
block|{
comment|// Arrays.copyOfRange() is not available under GWT
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
name|double
index|[]
name|result
init|=
operator|new
name|double
index|[
name|size
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
name|start
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
block|}
end_class

end_unit

