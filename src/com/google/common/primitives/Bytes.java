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

begin_comment
comment|/**  * Static utility methods pertaining to {@code byte} primitives, that are not  * already found in either {@link Byte} or {@link Arrays},<i>and interpret  * bytes as neither signed nor unsigned</i>. The methods which specifically  * treat bytes as signed or unsigned are found in {@link SignedBytes} and {@link  * UnsignedBytes}.  *  * @author Kevin Bourrillion  * @since 1  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Bytes
specifier|public
specifier|final
class|class
name|Bytes
block|{
DECL|method|Bytes ()
specifier|private
name|Bytes
parameter_list|()
block|{}
comment|/**    * Returns a hash code for {@code value}; equal to the result of invoking    * {@code ((Byte) value).hashCode()}.    *    * @param value a primitive {@code byte} value    * @return a hash code for the value    */
DECL|method|hashCode (byte value)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|byte
name|value
parameter_list|)
block|{
return|return
name|value
return|;
block|}
comment|/**    * Returns {@code true} if {@code target} is present as an element anywhere in    * {@code array}.    *    * @param array an array of {@code byte} values, possibly empty    * @param target a primitive {@code byte} value    * @return {@code true} if {@code array[i] == target} for some value of {@code    *     i}    */
DECL|method|contains (byte[] array, byte target)
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
name|target
parameter_list|)
block|{
for|for
control|(
name|byte
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
comment|/**    * Returns the index of the first appearance of the value {@code target} in    * {@code array}.    *    * @param array an array of {@code byte} values, possibly empty    * @param target a primitive {@code byte} value    * @return the least index {@code i} for which {@code array[i] == target}, or    *     {@code -1} if no such index exists.    */
DECL|method|indexOf (byte[] array, byte target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
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
DECL|method|indexOf ( byte[] array, byte target, int start, int end)
specifier|private
specifier|static
name|int
name|indexOf
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
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
comment|/**    * Returns the start position of the first occurrence of the specified {@code    * target} within {@code array}, or {@code -1} if there is no such occurrence.    *    *<p>More formally, returns the lowest index {@code i} such that {@code    * java.util.Arrays.copyOfRange(array, i, i + target.length)} contains exactly    * the same elements as {@code target}.    *    * @param array the array to search for the sequence {@code target}    * @param target the array to search for as a sub-sequence of {@code array}    */
DECL|method|indexOf (byte[] array, byte[] target)
specifier|public
specifier|static
name|int
name|indexOf
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
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
comment|/**    * Returns the index of the last appearance of the value {@code target} in    * {@code array}.    *    * @param array an array of {@code byte} values, possibly empty    * @param target a primitive {@code byte} value    * @return the greatest index {@code i} for which {@code array[i] == target},    *     or {@code -1} if no such index exists.    */
DECL|method|lastIndexOf (byte[] array, byte target)
specifier|public
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
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
DECL|method|lastIndexOf ( byte[] array, byte target, int start, int end)
specifier|private
specifier|static
name|int
name|lastIndexOf
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|byte
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
comment|/**    * Returns the values from each provided array combined into a single array.    * For example, {@code concat(new byte[] {a, b}, new byte[] {}, new    * byte[] {c}} returns the array {@code {a, b, c}}.    *    * @param arrays zero or more {@code byte} arrays    * @return a single array containing all the values from the source arrays, in    *     order    */
DECL|method|concat (byte[]... arrays)
specifier|public
specifier|static
name|byte
index|[]
name|concat
parameter_list|(
name|byte
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
name|byte
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
name|byte
index|[]
name|result
init|=
operator|new
name|byte
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
name|byte
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
comment|/**    * Returns an array containing the same values as {@code array}, but    * guaranteed to be of a specified minimum length. If {@code array} already    * has a length of at least {@code minLength}, it is returned directly.    * Otherwise, a new array of size {@code minLength + padding} is returned,    * containing the values of {@code array}, and zeroes in the remaining places.    *    * @param array the source array    * @param minLength the minimum length the returned array must guarantee    * @param padding an extra amount to "grow" the array by if growth is    *     necessary    * @throws IllegalArgumentException if {@code minLength} or {@code padding} is    *     negative    * @return an array containing the values of {@code array}, with guaranteed    *     minimum length {@code minLength}    */
DECL|method|ensureCapacity ( byte[] array, int minLength, int padding)
specifier|public
specifier|static
name|byte
index|[]
name|ensureCapacity
parameter_list|(
name|byte
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
DECL|method|copyOf (byte[] original, int length)
specifier|private
specifier|static
name|byte
index|[]
name|copyOf
parameter_list|(
name|byte
index|[]
name|original
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|byte
index|[]
name|copy
init|=
operator|new
name|byte
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
comment|/**    * Copies a collection of {@code Byte} instances into a new array of    * primitive {@code byte} values.    *    *<p>Elements are copied from the argument collection as if by {@code    * collection.toArray()}.  Calling this method is as thread-safe as calling    * that method.    *    * @param collection a collection of {@code Byte} objects    * @return an array containing the same values as {@code collection}, in the    *     same order, converted to primitives    * @throws NullPointerException if {@code collection} or any of its elements    *     is null    */
DECL|method|toArray (Collection<Byte> collection)
specifier|public
specifier|static
name|byte
index|[]
name|toArray
parameter_list|(
name|Collection
argument_list|<
name|Byte
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
name|collection
operator|instanceof
name|ByteArrayAsList
condition|)
block|{
return|return
operator|(
operator|(
name|ByteArrayAsList
operator|)
name|collection
operator|)
operator|.
name|toByteArray
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
name|byte
index|[]
name|array
init|=
operator|new
name|byte
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
name|array
index|[
name|i
index|]
operator|=
operator|(
name|Byte
operator|)
name|boxedArray
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**    * Returns a fixed-size list backed by the specified array, similar to {@link    * Arrays#asList(Object[])}. The list supports {@link List#set(int, Object)},    * but any attempt to set a value to {@code null} will result in a {@link    * NullPointerException}.    *    *<p>The returned list maintains the values, but not the identities, of    * {@code Byte} objects written to or read from it.  For example, whether    * {@code list.get(0) == list.get(0)} is true for the returned list is    * unspecified.    *    * @param backingArray the array to back the list    * @return a list view of the array    */
DECL|method|asList (byte... backingArray)
specifier|public
specifier|static
name|List
argument_list|<
name|Byte
argument_list|>
name|asList
parameter_list|(
name|byte
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
name|ByteArrayAsList
argument_list|(
name|backingArray
argument_list|)
return|;
block|}
annotation|@
name|GwtCompatible
DECL|class|ByteArrayAsList
specifier|private
specifier|static
class|class
name|ByteArrayAsList
extends|extends
name|AbstractList
argument_list|<
name|Byte
argument_list|>
implements|implements
name|RandomAccess
implements|,
name|Serializable
block|{
DECL|field|array
specifier|final
name|byte
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
DECL|method|ByteArrayAsList (byte[] array)
name|ByteArrayAsList
parameter_list|(
name|byte
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
DECL|method|ByteArrayAsList (byte[] array, int start, int end)
name|ByteArrayAsList
parameter_list|(
name|byte
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
name|Byte
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
name|Byte
operator|)
operator|&&
name|Bytes
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Byte
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
name|Byte
condition|)
block|{
name|int
name|i
init|=
name|Bytes
operator|.
name|indexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Byte
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
name|Byte
condition|)
block|{
name|int
name|i
init|=
name|Bytes
operator|.
name|lastIndexOf
argument_list|(
name|array
argument_list|,
operator|(
name|Byte
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
DECL|method|set (int index, Byte element)
annotation|@
name|Override
specifier|public
name|Byte
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|Byte
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
name|byte
name|oldValue
init|=
name|array
index|[
name|start
operator|+
name|index
index|]
decl_stmt|;
name|array
index|[
name|start
operator|+
name|index
index|]
operator|=
name|element
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
name|Byte
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
name|ByteArrayAsList
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
DECL|method|equals (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
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
name|ByteArrayAsList
condition|)
block|{
name|ByteArrayAsList
name|that
init|=
operator|(
name|ByteArrayAsList
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
name|Bytes
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
literal|5
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
DECL|method|toByteArray ()
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
comment|// Arrays.copyOfRange() requires Java 6
name|int
name|size
init|=
name|size
argument_list|()
decl_stmt|;
name|byte
index|[]
name|result
init|=
operator|new
name|byte
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

