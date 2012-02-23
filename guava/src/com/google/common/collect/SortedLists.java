begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|common
operator|.
name|base
operator|.
name|Function
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
comment|/**  * Static methods pertaining to sorted {@link List} instances.  *  * In this documentation, the terms<i>greatest</i>,<i>greater</i>,<i>least</i>, and  *<i>lesser</i> are considered to refer to the comparator on the elements, and the terms  *<i>first</i> and<i>last</i> are considered to refer to the elements' ordering in a  * list.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SortedLists
annotation|@
name|Beta
specifier|final
class|class
name|SortedLists
block|{
DECL|method|SortedLists ()
specifier|private
name|SortedLists
parameter_list|()
block|{}
comment|/**    * A specification for which index to return if the list contains at least one element that    * compares as equal to the key.    */
DECL|enum|KeyPresentBehavior
specifier|public
enum|enum
name|KeyPresentBehavior
block|{
comment|/**      * Return the index of any list element that compares as equal to the key. No guarantees are      * made as to which index is returned, if more than one element compares as equal to the key.      */
DECL|enumConstant|ANY_PRESENT
name|ANY_PRESENT
block|{
annotation|@
name|Override
argument_list|<
name|E
argument_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
block|{
return|return
name|foundIndex
return|;
block|}
block|}
block|,
comment|/**      * Return the index of the last list element that compares as equal to the key.      */
DECL|enumConstant|LAST_PRESENT
name|LAST_PRESENT
block|{
annotation|@
name|Override
argument_list|<
name|E
argument_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
block|{
comment|// Of course, we have to use binary search to find the precise
comment|// breakpoint...
name|int
name|lower
init|=
name|foundIndex
decl_stmt|;
name|int
name|upper
init|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
comment|// Everything between lower and upper inclusive compares at>= 0.
while|while
condition|(
name|lower
operator|<
name|upper
condition|)
block|{
name|int
name|middle
init|=
operator|(
name|lower
operator|+
name|upper
operator|+
literal|1
operator|)
operator|>>>
literal|1
decl_stmt|;
name|int
name|c
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|middle
argument_list|)
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|>
literal|0
condition|)
block|{
name|upper
operator|=
name|middle
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
comment|// c == 0
name|lower
operator|=
name|middle
expr_stmt|;
block|}
block|}
return|return
name|lower
return|;
block|}
block|}
block|,
comment|/**      * Return the index of the first list element that compares as equal to the key.      */
DECL|enumConstant|FIRST_PRESENT
name|FIRST_PRESENT
block|{
annotation|@
name|Override
argument_list|<
name|E
argument_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
block|{
comment|// Of course, we have to use binary search to find the precise
comment|// breakpoint...
name|int
name|lower
init|=
literal|0
decl_stmt|;
name|int
name|upper
init|=
name|foundIndex
decl_stmt|;
comment|// Of course, we have to use binary search to find the precise breakpoint...
comment|// Everything between lower and upper inclusive compares at<= 0.
while|while
condition|(
name|lower
operator|<
name|upper
condition|)
block|{
name|int
name|middle
init|=
operator|(
name|lower
operator|+
name|upper
operator|)
operator|>>>
literal|1
decl_stmt|;
name|int
name|c
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|middle
argument_list|)
argument_list|,
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
name|lower
operator|=
name|middle
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
comment|// c == 0
name|upper
operator|=
name|middle
expr_stmt|;
block|}
block|}
return|return
name|lower
return|;
block|}
block|}
block|,
comment|/**      * Return the index of the first list element that compares as greater than the key, or {@code      * list.size()} if there is no such element.      */
DECL|enumConstant|FIRST_AFTER
name|FIRST_AFTER
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
block|{
return|return
name|LAST_PRESENT
operator|.
name|resultIndex
argument_list|(
name|comparator
argument_list|,
name|key
argument_list|,
name|list
argument_list|,
name|foundIndex
argument_list|)
operator|+
literal|1
return|;
block|}
block|}
block|,
comment|/**      * Return the index of the last list element that compares as less than the key, or {@code -1}      * if there is no such element.      */
DECL|enumConstant|LAST_BEFORE
name|LAST_BEFORE
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
block|{
return|return
name|FIRST_PRESENT
operator|.
name|resultIndex
argument_list|(
name|comparator
argument_list|,
name|key
argument_list|,
name|list
argument_list|,
name|foundIndex
argument_list|)
operator|-
literal|1
return|;
block|}
block|}
block|;
DECL|method|resultIndex ( Comparator<? super E> comparator, E key, List<? extends E> list, int foundIndex)
specifier|abstract
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|E
name|key
parameter_list|,
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|foundIndex
parameter_list|)
function_decl|;
block|}
comment|/**    * A specification for which index to return if the list contains no elements that compare as    * equal to the key.    */
DECL|enum|KeyAbsentBehavior
specifier|public
enum|enum
name|KeyAbsentBehavior
block|{
comment|/**      * Return the index of the next lower element in the list, or {@code -1} if there is no such      * element.      */
DECL|enumConstant|NEXT_LOWER
name|NEXT_LOWER
block|{
annotation|@
name|Override
argument_list|<
name|E
argument_list|>
name|int
name|resultIndex
parameter_list|(
name|int
name|higherIndex
parameter_list|)
block|{
return|return
name|higherIndex
operator|-
literal|1
return|;
block|}
block|}
block|,
comment|/**      * Return the index of the next higher element in the list, or {@code list.size()} if there is      * no such element.      */
DECL|enumConstant|NEXT_HIGHER
name|NEXT_HIGHER
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|int
name|higherIndex
parameter_list|)
block|{
return|return
name|higherIndex
return|;
block|}
block|}
block|,
comment|/**      * Return {@code ~insertionIndex}, where {@code insertionIndex} is defined as the point at      * which the key would be inserted into the list: the index of the next higher element in the      * list, or {@code list.size()} if there is no such element.      *      *<p>Note that the return value will be {@code>= 0} if and only if there is an element of the      * list that compares as equal to the key.      *      *<p>This is equivalent to the behavior of      * {@link java.util.Collections#binarySearch(List, Object)} when the key isn't present, since      * {@code ~insertionIndex} is equal to {@code -1 - insertionIndex}.      */
DECL|enumConstant|INVERTED_INSERTION_INDEX
name|INVERTED_INSERTION_INDEX
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|int
name|higherIndex
parameter_list|)
block|{
return|return
operator|~
name|higherIndex
return|;
block|}
block|}
block|;
DECL|method|resultIndex (int higherIndex)
specifier|abstract
parameter_list|<
name|E
parameter_list|>
name|int
name|resultIndex
parameter_list|(
name|int
name|higherIndex
parameter_list|)
function_decl|;
block|}
comment|/**    * Searches the specified naturally ordered list for the specified object using the binary search    * algorithm.    *    *<p>Equivalent to {@link #binarySearch(List, Function, Object, Comparator, KeyPresentBehavior,    * KeyAbsentBehavior)} using {@link Ordering#natural}.    */
DECL|method|binarySearch (List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
name|E
name|e
parameter_list|,
name|KeyPresentBehavior
name|presentBehavior
parameter_list|,
name|KeyAbsentBehavior
name|absentBehavior
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|binarySearch
argument_list|(
name|list
argument_list|,
name|checkNotNull
argument_list|(
name|e
argument_list|)
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
return|;
block|}
comment|/**    * Binary searches the list for the specified key, using the specified key function.    *    *<p>Equivalent to {@link #binarySearch(List, Function, Object, Comparator, KeyPresentBehavior,    * KeyAbsentBehavior)} using {@link Ordering#natural}.    */
DECL|method|binarySearch (List<E> list, Function<? super E, K> keyFunction, @Nullable K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|,
name|K
extends|extends
name|Comparable
parameter_list|>
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|K
argument_list|>
name|keyFunction
parameter_list|,
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
name|KeyPresentBehavior
name|presentBehavior
parameter_list|,
name|KeyAbsentBehavior
name|absentBehavior
parameter_list|)
block|{
return|return
name|binarySearch
argument_list|(
name|list
argument_list|,
name|keyFunction
argument_list|,
name|key
argument_list|,
name|Ordering
operator|.
name|natural
argument_list|()
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
return|;
block|}
comment|/**    * Binary searches the list for the specified key, using the specified key function.    *    *<p>Equivalent to    * {@link #binarySearch(List, Object, Comparator, KeyPresentBehavior, KeyAbsentBehavior)} using    * {@link Lists#transform(List, Function) Lists.transform(list, keyFunction)}.    */
DECL|method|binarySearch ( List<E> list, Function<? super E, K> keyFunction, @Nullable K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|,
name|K
parameter_list|>
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|E
argument_list|,
name|K
argument_list|>
name|keyFunction
parameter_list|,
annotation|@
name|Nullable
name|K
name|key
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|,
name|KeyPresentBehavior
name|presentBehavior
parameter_list|,
name|KeyAbsentBehavior
name|absentBehavior
parameter_list|)
block|{
return|return
name|binarySearch
argument_list|(
name|Lists
operator|.
name|transform
argument_list|(
name|list
argument_list|,
name|keyFunction
argument_list|)
argument_list|,
name|key
argument_list|,
name|keyComparator
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
return|;
block|}
comment|/**    * Searches the specified list for the specified object using the binary search algorithm. The    * list must be sorted into ascending order according to the specified comparator (as by the    * {@link Collections#sort(List, Comparator) Collections.sort(List, Comparator)} method), prior    * to making this call. If it is not sorted, the results are undefined.    *    *<p>If there are elements in the list which compare as equal to the key, the choice of    * {@link KeyPresentBehavior} decides which index is returned. If no elements compare as equal to    * the key, the choice of {@link KeyAbsentBehavior} decides which index is returned.    *    *<p>This method runs in log(n) time on random-access lists, which offer near-constant-time    * access to each list element.    *    * @param list the list to be searched.    * @param key the value to be searched for.    * @param comparator the comparator by which the list is ordered.    * @param presentBehavior the specification for what to do if at least one element of the list    *        compares as equal to the key.    * @param absentBehavior the specification for what to do if no elements of the list compare as    *        equal to the key.    * @return the index determined by the {@code KeyPresentBehavior}, if the key is in the list;    *         otherwise the index determined by the {@code KeyAbsentBehavior}.    */
DECL|method|binarySearch (List<? extends E> list, @Nullable E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|int
name|binarySearch
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
parameter_list|,
annotation|@
name|Nullable
name|E
name|key
parameter_list|,
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|,
name|KeyPresentBehavior
name|presentBehavior
parameter_list|,
name|KeyAbsentBehavior
name|absentBehavior
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|comparator
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|presentBehavior
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|absentBehavior
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|list
operator|instanceof
name|RandomAccess
operator|)
condition|)
block|{
name|list
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
comment|// TODO(user): benchmark when it's best to do a linear search
name|int
name|lower
init|=
literal|0
decl_stmt|;
name|int
name|upper
init|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|lower
operator|<=
name|upper
condition|)
block|{
name|int
name|middle
init|=
operator|(
name|lower
operator|+
name|upper
operator|)
operator|>>>
literal|1
decl_stmt|;
name|int
name|c
init|=
name|comparator
operator|.
name|compare
argument_list|(
name|key
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|middle
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|0
condition|)
block|{
name|upper
operator|=
name|middle
operator|-
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|>
literal|0
condition|)
block|{
name|lower
operator|=
name|middle
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
return|return
name|lower
operator|+
name|presentBehavior
operator|.
name|resultIndex
argument_list|(
name|comparator
argument_list|,
name|key
argument_list|,
name|list
operator|.
name|subList
argument_list|(
name|lower
argument_list|,
name|upper
operator|+
literal|1
argument_list|)
argument_list|,
name|middle
operator|-
name|lower
argument_list|)
return|;
block|}
block|}
return|return
name|absentBehavior
operator|.
name|resultIndex
argument_list|(
name|lower
argument_list|)
return|;
block|}
block|}
end_class

end_unit

