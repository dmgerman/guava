begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Arrays
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
comment|/**  * Static utility methods pertaining to {@code boolean} primitives, that are not already found in  * either {@link Boolean} or {@link Arrays}.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/PrimitivesExplained">primitive utilities</a>.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Booleans
specifier|public
specifier|final
class|class
name|Booleans
block|{
DECL|method|Booleans ()
specifier|private
name|Booleans
parameter_list|()
block|{}
comment|/** Comparators for {@code Boolean} values. */
DECL|enum|BooleanComparator
specifier|private
enum|enum
name|BooleanComparator
implements|implements
name|Comparator
argument_list|<
name|Boolean
argument_list|>
block|{
DECL|enumConstant|TRUE_FIRST
name|TRUE_FIRST
argument_list|(
literal|1
argument_list|,
literal|"Booleans.trueFirst()"
argument_list|)
block|,
DECL|enumConstant|FALSE_FIRST
name|FALSE_FIRST
argument_list|(
operator|-
literal|1
argument_list|,
literal|"Booleans.falseFirst()"
argument_list|)
block|;
DECL|field|trueValue
specifier|private
specifier|final
name|int
name|trueValue
decl_stmt|;
DECL|field|toString
specifier|private
specifier|final
name|String
name|toString
decl_stmt|;
DECL|method|BooleanComparator (int trueValue, String toString)
name|BooleanComparator
parameter_list|(
name|int
name|trueValue
parameter_list|,
name|String
name|toString
parameter_list|)
block|{
name|this
operator|.
name|trueValue
operator|=
name|trueValue
expr_stmt|;
name|this
operator|.
name|toString
operator|=
name|toString
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compare (Boolean a, Boolean b)
specifier|public
name|int
name|compare
parameter_list|(
name|Boolean
name|a
parameter_list|,
name|Boolean
name|b
parameter_list|)
block|{
name|int
name|aVal
init|=
name|a
condition|?
name|trueValue
else|:
literal|0
decl_stmt|;
name|int
name|bVal
init|=
name|b
condition|?
name|trueValue
else|:
literal|0
decl_stmt|;
return|return
name|bVal
operator|-
name|aVal
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
name|toString
return|;
block|}
block|}
comment|/**    * Returns a {@code Comparator<Boolean>} that sorts {@code true} before {@code false}.    *    *<p>This is particularly useful in Java 8+ in combination with {@code Comparators.comparing},    * e.g. {@code Comparators.comparing(Foo::hasBar, trueFirst())}.    *    * @since 21.0    */
annotation|@
name|Beta
DECL|method|trueFirst ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|Boolean
argument_list|>
name|trueFirst
parameter_list|()
block|{
return|return
name|BooleanComparator
operator|.
name|TRUE_FIRST
return|;
block|}
comment|/**    * Returns a {@code Comparator<Boolean>} that sorts {@code false} before {@code true}.    *    *<p>This is particularly useful in Java 8+ in combination with {@code Comparators.comparing},    * e.g. {@code Comparators.comparing(Foo::hasBar, falseFirst())}.    *    * @since 21.0    */
annotation|@
name|Beta
DECL|method|falseFirst ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|Boolean
argument_list|>
name|falseFirst
parameter_list|()
block|{
return|return
name|BooleanComparator
operator|.
name|FALSE_FIRST
return|;
block|}
comment|/**    * Returns a hash code for {@code value}; equal to the result of invoking {@code ((Boolean)    * value).hashCode()}.    *    *<p><b>Java 8 users:</b> use {@link Boolean#hashCode(boolean)} instead.    *    * @param value a primitive {@code boolean} value    * @return a hash code for the value    */
DECL|method|hashCode (boolean value)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
name|value
condition|?
literal|1231
else|:
literal|1237
return|;
block|}
comment|/**    * Compares the two specified {@code boolean} values in the standard way ({@code false} is    * considered less than {@code true}). The sign of the value returned is the same as that of    * {@code ((Boolean) a).compareTo(b)}.    *    *<p><b>Note for Java 7 and later:</b> this method should be treated as deprecated; use the    * equivalent {@link Boolean#compare} method instead.    *    * @param a the first {@code boolean} to compare    * @param b the second {@code boolean} to compare    * @return a positive number if only {@code a} is {@code true}, a negative number if only {@code    *     b} is true, or zero if {@code a == b}    */
DECL|method|compare (boolean a, boolean b)
specifier|public
specifier|static
name|int
name|compare
parameter_list|(
name|boolean
name|a
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
return|return
operator|(
name|a
operator|==
name|b
operator|)
condition|?
literal|0
else|:
operator|(
name|a
condition|?
literal|1
else|:
operator|-
literal|1
operator|)
return|;
block|}
comment|/**    * Returns {@code true} if {@code target} is present as an element anywhere in {@code array}.    *    *<p><b>Note:</b> consider representing the array as a {@link java.util.BitSet} instead,    * replacing {@code Booleans.contains(array, true)} with {@code !bitSet.isEmpty()} and {@code    * Booleans.contains(array, false)} with {@code bitSet.nextClearBit(0) == sizeOfBitSet}.    *    * @param array an array of {@code boolean} values, possibly empty    * @param target a primitive {@code boolean} value    * @return {@code true} if {@code array[i] == target} for some value of {@code i}    */
DECL|method|contains (boolean[] array, boolean target)
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
name|target
parameter_list|)
block|{
for|for
control|(
name|boolean
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
comment|/**    * Returns the index of the first appearance of the value {@code target} in {@code array}.    *    *<p><b>Note:</b> consider representing the array as a {@link java.util.BitSet} instead, and    * using {@link java.util.BitSet#nextSetBit(int)} or {@link java.util.BitSet#nextClearBit(int)}.    *    * @param array an array of {@code boolean} values, possibly empty    * @param target a primitive {@code boolean} value    * @return the least index {@code i} for which {@code array[i] == target}, or {@code -1} if no    *     such index exists.    */
DECL|method|indexOf (boolean[] array, boolean target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
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
DECL|method|indexOf (boolean[] array, boolean target, int start, int end)
specifier|private
specifier|static
name|int
name|indexOf
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
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
comment|/**    * Returns the start position of the first occurrence of the specified {@code target} within    * {@code array}, or {@code -1} if there is no such occurrence.    *    *<p>More formally, returns the lowest index {@code i} such that {@code Arrays.copyOfRange(array,    * i, i + target.length)} contains exactly the same elements as {@code target}.    *    * @param array the array to search for the sequence {@code target}    * @param target the array to search for as a sub-sequence of {@code array}    */
DECL|method|indexOf (boolean[] array, boolean[] target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
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
comment|/**    * Returns the index of the last appearance of the value {@code target} in {@code array}.    *    * @param array an array of {@code boolean} values, possibly empty    * @param target a primitive {@code boolean} value    * @return the greatest index {@code i} for which {@code array[i] == target}, or {@code -1} if no    *     such index exists.    */
DECL|method|lastIndexOf (boolean[] array, boolean target)
specifier|public
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
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
DECL|method|lastIndexOf (boolean[] array, boolean target, int start, int end)
specifier|private
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|boolean
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
comment|/**    * Returns the values from each provided array combined into a single array. For example, {@code    * concat(new boolean[] {a, b}, new boolean[] {}, new boolean[] {c}} returns the array {@code {a,    * b, c}}.    *    * @param arrays zero or more {@code boolean} arrays    * @return a single array containing all the values from the source arrays, in order    */
DECL|method|concat (boolean[]... arrays)
specifier|public
specifier|static
name|boolean
index|[]
name|concat
parameter_list|(
name|boolean
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
name|boolean
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
name|boolean
index|[]
name|result
init|=
operator|new
name|boolean
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
name|boolean
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
comment|/**    * Returns an array containing the same values as {@code array}, but guaranteed to be of a    * specified minimum length. If {@code array} already has a length of at least {@code minLength},    * it is returned directly. Otherwise, a new array of size {@code minLength + padding} is    * returned, containing the values of {@code array}, and zeroes in the remaining places.    *    * @param array the source array    * @param minLength the minimum length the returned array must guarantee    * @param padding an extra amount to "grow" the array by if growth is necessary    * @throws IllegalArgumentException if {@code minLength} or {@code padding} is negative    * @return an array containing the values of {@code array}, with guaranteed minimum length {@code    *     minLength}    */
DECL|method|ensureCapacity (boolean[] array, int minLength, int padding)
specifier|public
specifier|static
name|boolean
index|[]
name|ensureCapacity
parameter_list|(
name|boolean
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
name|Arrays
operator|.
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
comment|/**    * Returns a string containing the supplied {@code boolean} values separated by {@code separator}.    * For example, {@code join("-", false, true, false)} returns the string {@code    * "false-true-false"}.    *    * @param separator the text that should appear between consecutive values in the resulting string    *     (but not at the start or end)    * @param array an array of {@code boolean} values, possibly empty    */
DECL|method|join (String separator, boolean... array)
specifier|public
specifier|static
name|String
name|join
parameter_list|(
name|String
name|separator
parameter_list|,
name|boolean
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
literal|7
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
comment|/**    * Returns a comparator that compares two {@code boolean} arrays<a    * href="http://en.wikipedia.org/wiki/Lexicographical_order">lexicographically</a>. That is, it    * compares, using {@link #compare(boolean, boolean)}), the first pair of values that follow any    * common prefix, or when one array is a prefix of the other, treats the shorter array as the    * lesser. For example, {@code []< [false]< [false, true]< [true]}.    *    *<p>The returned comparator is inconsistent with {@link Object#equals(Object)} (since arrays    * support only identity equality), but it is consistent with {@link Arrays#equals(boolean[],    * boolean[])}.    *    * @since 2.0    */
DECL|method|lexicographicalComparator ()
specifier|public
specifier|static
name|Comparator
argument_list|<
name|boolean
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
name|boolean
index|[]
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|compare (boolean[] left, boolean[] right)
specifier|public
name|int
name|compare
parameter_list|(
name|boolean
index|[]
name|left
parameter_list|,
name|boolean
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
name|Booleans
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Booleans.lexicographicalComparator()"
return|;
block|}
block|}
comment|/**    * Copies a collection of {@code Boolean} instances into a new array of primitive {@code boolean}    * values.    *    *<p>Elements are copied from the argument collection as if by {@code collection.toArray()}.    * Calling this method is as thread-safe as calling that method.    *    *<p><b>Note:</b> consider representing the collection as a {@link java.util.BitSet} instead.    *    * @param collection a collection of {@code Boolean} objects    * @return an array containing the same values as {@code collection}, in the same order, converted    *     to primitives    * @throws NullPointerException if {@code collection} or any of its elements is null    */
DECL|method|toArray (Collection<Boolean> collection)
specifier|public
specifier|static
name|boolean
index|[]
name|toArray
parameter_list|(
name|Collection
argument_list|<
name|Boolean
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|instanceof
name|BooleanArrayAsList
condition|)
block|{
return|return
operator|(
operator|(
name|BooleanArrayAsList
operator|)
name|collection
operator|)
operator|.
name|toBooleanArray
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
name|boolean
index|[]
name|array
init|=
operator|new
name|boolean
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
name|Boolean
operator|)
name|checkNotNull
argument_list|(
name|boxedArray
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**    * Returns a fixed-size list backed by the specified array, similar to {@link    * Arrays#asList(Object[])}. The list supports {@link List#set(int, Object)}, but any attempt to    * set a value to {@code null} will result in a {@link NullPointerException}.    *    *<p>The returned list maintains the values, but not the identities, of {@code Boolean} objects    * written to or read from it. For example, whether {@code list.get(0) == list.get(0)} is true for    * the returned list is unspecified.    *    * @param backingArray the array to back the list    * @return a list view of the array    */
DECL|method|asList (boolean... backingArray)
specifier|public
specifier|static
name|List
argument_list|<
name|Boolean
argument_list|>
name|asList
parameter_list|(
name|boolean
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
name|BooleanArrayAsList
argument_list|(
name|backingArray
argument_list|)
return|;
block|}
annotation|@
name|GwtCompatible
DECL|class|BooleanArrayAsList
specifier|private
specifier|static
class|class
name|BooleanArrayAsList
extends|extends
name|AbstractList
argument_list|<
name|Boolean
argument_list|>
implements|implements
name|RandomAccess
implements|,
name|Serializable
block|{
DECL|field|array
specifier|final
name|boolean
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
DECL|method|BooleanArrayAsList (boolean[] array)
name|BooleanArrayAsList
parameter_list|(
name|boolean
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
DECL|method|BooleanArrayAsList (boolean[] array, int start, int end)
name|BooleanArrayAsList
parameter_list|(
name|boolean
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
annotation|@
name|Override
DECL|method|size ()
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
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|Boolean
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
annotation|@
name|Override
DECL|method|contains (Object target)
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
name|Boolean
operator|)
operator|&&
name|Booleans
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Boolean
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
annotation|@
name|Override
DECL|method|indexOf (Object target)
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
name|Boolean
condition|)
block|{
name|int
name|i
init|=
name|Booleans
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Boolean
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
annotation|@
name|Override
DECL|method|lastIndexOf (Object target)
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
name|Boolean
condition|)
block|{
name|int
name|i
init|=
name|Booleans
operator|.
name|lastIndexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Boolean
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
annotation|@
name|Override
DECL|method|set (int index, Boolean element)
specifier|public
name|Boolean
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|Boolean
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
name|boolean
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
annotation|@
name|Override
DECL|method|subList (int fromIndex, int toIndex)
specifier|public
name|List
argument_list|<
name|Boolean
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
name|BooleanArrayAsList
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
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
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
name|BooleanArrayAsList
condition|)
block|{
name|BooleanArrayAsList
name|that
init|=
operator|(
name|BooleanArrayAsList
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
annotation|@
name|Override
DECL|method|hashCode ()
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
name|Booleans
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
annotation|@
name|Override
DECL|method|toString ()
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
literal|7
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|array
index|[
name|start
index|]
condition|?
literal|"[true"
else|:
literal|"[false"
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
name|array
index|[
name|i
index|]
condition|?
literal|", true"
else|:
literal|", false"
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
DECL|method|toBooleanArray ()
name|boolean
index|[]
name|toBooleanArray
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|array
argument_list|,
name|start
argument_list|,
name|end
argument_list|)
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
comment|/**    * Returns the number of {@code values} that are {@code true}.    *    * @since 16.0    */
annotation|@
name|Beta
DECL|method|countTrue (boolean... values)
specifier|public
specifier|static
name|int
name|countTrue
parameter_list|(
name|boolean
modifier|...
name|values
parameter_list|)
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|boolean
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|value
condition|)
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
return|return
name|count
return|;
block|}
comment|/**    * Reverses the elements of {@code array}. This is equivalent to {@code    * Collections.reverse(Booleans.asList(array))}, but is likely to be more efficient.    *    * @since 23.1    */
DECL|method|reverse (boolean[] array)
specifier|public
specifier|static
name|void
name|reverse
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|reverse
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
comment|/**    * Reverses the elements of {@code array} between {@code fromIndex} inclusive and {@code toIndex}    * exclusive. This is equivalent to {@code    * Collections.reverse(Booleans.asList(array).subList(fromIndex, toIndex))}, but is likely to be    * more efficient.    *    * @throws IndexOutOfBoundsException if {@code fromIndex< 0}, {@code toIndex> array.length}, or    *     {@code toIndex> fromIndex}    * @since 23.1    */
DECL|method|reverse (boolean[] array, int fromIndex, int toIndex)
specifier|public
specifier|static
name|void
name|reverse
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|,
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|checkPositionIndexes
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|,
name|j
init|=
name|toIndex
operator|-
literal|1
init|;
name|i
operator|<
name|j
condition|;
name|i
operator|++
operator|,
name|j
operator|--
control|)
block|{
name|boolean
name|tmp
init|=
name|array
index|[
name|i
index|]
decl_stmt|;
name|array
index|[
name|i
index|]
operator|=
name|array
index|[
name|j
index|]
expr_stmt|;
name|array
index|[
name|j
index|]
operator|=
name|tmp
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

