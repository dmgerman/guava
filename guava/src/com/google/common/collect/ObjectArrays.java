begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

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
name|util
operator|.
name|Collection
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
comment|/**  * Static utility methods pertaining to object arrays.  *  * @author Kevin Bourrillion  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ObjectArrays
specifier|public
specifier|final
class|class
name|ObjectArrays
block|{
DECL|method|ObjectArrays ()
specifier|private
name|ObjectArrays
parameter_list|()
block|{}
comment|/**    * Returns a new array of the given length with the specified component type.    *    * @param type the component type    * @param length the length of the new array    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Array.newInstance(Class, int)"
argument_list|)
DECL|method|newArray (Class<T> type, int length)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|newArray
argument_list|(
name|type
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/**    * Returns a new array of the given length with the same type as a reference    * array.    *    * @param reference any array of the desired type    * @param length the length of the new array    */
DECL|method|newArray (T[] reference, int length)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|T
index|[]
name|reference
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|newArray
argument_list|(
name|reference
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/**    * Returns a new array that contains the concatenated contents of two arrays.    *    * @param first the first array of elements to concatenate    * @param second the second array of elements to concatenate    * @param type the component type of the returned array    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Array.newInstance(Class, int)"
argument_list|)
DECL|method|concat (T[] first, T[] second, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|concat
parameter_list|(
name|T
index|[]
name|first
parameter_list|,
name|T
index|[]
name|second
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|T
index|[]
name|result
init|=
name|newArray
argument_list|(
name|type
argument_list|,
name|first
operator|.
name|length
operator|+
name|second
operator|.
name|length
argument_list|)
decl_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|first
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|first
operator|.
name|length
argument_list|)
expr_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|second
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|first
operator|.
name|length
argument_list|,
name|second
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Returns a new array that prepends {@code element} to {@code array}.    *    * @param element the element to prepend to the front of {@code array}    * @param array the array of elements to append    * @return an array whose size is one larger than {@code array}, with    *     {@code element} occupying the first position, and the    *     elements of {@code array} occupying the remaining elements.    */
DECL|method|concat (@ullable T element, T[] array)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|concat
parameter_list|(
annotation|@
name|Nullable
name|T
name|element
parameter_list|,
name|T
index|[]
name|array
parameter_list|)
block|{
name|T
index|[]
name|result
init|=
name|newArray
argument_list|(
name|array
argument_list|,
name|array
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|result
index|[
literal|0
index|]
operator|=
name|element
expr_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|1
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Returns a new array that appends {@code element} to {@code array}.    *    * @param array the array of elements to prepend    * @param element the element to append to the end    * @return an array whose size is one larger than {@code array}, with    *     the same contents as {@code array}, plus {@code element} occupying the    *     last position.    */
DECL|method|concat (T[] array, @Nullable T element)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|concat
parameter_list|(
name|T
index|[]
name|array
parameter_list|,
annotation|@
name|Nullable
name|T
name|element
parameter_list|)
block|{
name|T
index|[]
name|result
init|=
name|arraysCopyOf
argument_list|(
name|array
argument_list|,
name|array
operator|.
name|length
operator|+
literal|1
argument_list|)
decl_stmt|;
name|result
index|[
name|array
operator|.
name|length
index|]
operator|=
name|element
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/** GWT safe version of Arrays.copyOf. */
DECL|method|arraysCopyOf (T[] original, int newLength)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|arraysCopyOf
parameter_list|(
name|T
index|[]
name|original
parameter_list|,
name|int
name|newLength
parameter_list|)
block|{
name|T
index|[]
name|copy
init|=
name|newArray
argument_list|(
name|original
argument_list|,
name|newLength
argument_list|)
decl_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
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
name|newLength
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/**    * Returns an array containing all of the elements in the specified    * collection; the runtime type of the returned array is that of the specified    * array. If the collection fits in the specified array, it is returned    * therein. Otherwise, a new array is allocated with the runtime type of the    * specified array and the size of the specified collection.    *    *<p>If the collection fits in the specified array with room to spare (i.e.,    * the array has more elements than the collection), the element in the array    * immediately following the end of the collection is set to null. This is    * useful in determining the length of the collection<i>only</i> if the    * caller knows that the collection does not contain any null elements.    *    *<p>This method returns the elements in the order they are returned by the    * collection's iterator.    *    *<p>TODO(kevinb): support concurrently modified collections?    *    * @param c the collection for which to return an array of elements    * @param array the array in which to place the collection elements    * @throws ArrayStoreException if the runtime type of the specified array is    *     not a supertype of the runtime type of every element in the specified    *     collection    */
DECL|method|toArrayImpl (Collection<?> c, T[] array)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArrayImpl
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|T
index|[]
name|array
parameter_list|)
block|{
name|int
name|size
init|=
name|c
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|<
name|size
condition|)
block|{
name|array
operator|=
name|newArray
argument_list|(
name|array
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
name|fillArray
argument_list|(
name|c
argument_list|,
name|array
argument_list|)
expr_stmt|;
if|if
condition|(
name|array
operator|.
name|length
operator|>
name|size
condition|)
block|{
name|array
index|[
name|size
index|]
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**    * Returns an array containing all of the elements in the specified    * collection. This method returns the elements in the order they are returned    * by the collection's iterator. The returned array is "safe" in that no    * references to it are maintained by the collection. The caller is thus free    * to modify the returned array.    *    *<p>This method assumes that the collection size doesn't change while the    * method is running.    *    *<p>TODO(kevinb): support concurrently modified collections?    *    * @param c the collection for which to return an array of elements    */
DECL|method|toArrayImpl (Collection<?> c)
specifier|static
name|Object
index|[]
name|toArrayImpl
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|fillArray
argument_list|(
name|c
argument_list|,
operator|new
name|Object
index|[
name|c
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
DECL|method|fillArray (Iterable<?> elements, Object[] array)
specifier|private
specifier|static
name|Object
index|[]
name|fillArray
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|elements
parameter_list|,
name|Object
index|[]
name|array
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|element
range|:
name|elements
control|)
block|{
name|array
index|[
name|i
operator|++
index|]
operator|=
name|element
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
comment|/**    * Swaps {@code array[i]} with {@code array[j]}.    */
DECL|method|swap (Object[] array, int i, int j)
specifier|static
name|void
name|swap
parameter_list|(
name|Object
index|[]
name|array
parameter_list|,
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
name|Object
name|temp
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
name|temp
expr_stmt|;
block|}
block|}
end_class

end_unit

