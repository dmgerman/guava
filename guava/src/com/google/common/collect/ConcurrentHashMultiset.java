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
name|collect
operator|.
name|Multisets
operator|.
name|checkNonnegative
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
name|VisibleForTesting
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
name|collect
operator|.
name|Serialization
operator|.
name|FieldSetter
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
name|primitives
operator|.
name|Ints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|AbstractSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
comment|/**  * A multiset that supports concurrent modifications and that provides atomic  * versions of most {@code Multiset} operations (exceptions where noted). Null  * elements are not supported.  *  * @author Cliff L. Biffle  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
DECL|class|ConcurrentHashMultiset
specifier|public
specifier|final
class|class
name|ConcurrentHashMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
comment|/*    * The ConcurrentHashMultiset's atomic operations are implemented in terms of    * ConcurrentMap's atomic operations. Many of them, such as add(E, int), are    * read-modify-write sequences, and so are implemented as loops that wrap    * ConcurrentMap's compare-and-set operations (like putIfAbsent).    *    * Invariant: all entries have a positive value. In particular, there are no    * entries with zero value. Some operations would fail if this was not the    * case.    */
comment|/** The number of occurrences of each element. */
DECL|field|countMap
specifier|private
specifier|final
specifier|transient
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|countMap
decl_stmt|;
comment|// This constant allows the deserialization code to set a final field. This
comment|// holder class makes sure it is not initialized unless an instance is
comment|// deserialized.
DECL|class|FieldSettersHolder
specifier|private
specifier|static
class|class
name|FieldSettersHolder
block|{
DECL|field|COUNT_MAP_FIELD_SETTER
specifier|static
specifier|final
name|FieldSetter
argument_list|<
name|ConcurrentHashMultiset
argument_list|>
name|COUNT_MAP_FIELD_SETTER
init|=
name|Serialization
operator|.
name|getFieldSetter
argument_list|(
name|ConcurrentHashMultiset
operator|.
name|class
argument_list|,
literal|"countMap"
argument_list|)
decl_stmt|;
block|}
comment|/**    * Creates a new, empty {@code ConcurrentHashMultiset} using the default    * initial capacity, load factor, and concurrency settings.    */
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
operator|new
name|ConcurrentHashMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a new {@code ConcurrentHashMultiset} containing the specified    * elements, using the default initial capacity, load factor, and concurrency    * settings.    *    * @param elements the elements that the multiset should contain    */
DECL|method|create ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|multiset
return|;
block|}
comment|/**    * Creates a new, empty {@code ConcurrentHashMultiset} using {@code mapMaker}    * to construct the internal backing map.    *    *<p>If this {@link MapMaker} is configured to use entry eviction of any    * kind, this eviction applies to all occurrences of a given element as a    * single unit.    *    *<p>The returned multiset is serializable but any serialization caveats    * given in {@code MapMaker} apply.    *    *<p>Finally, soft/weak values can be used but are not very useful.    * Soft/weak keys on the other hand can be useful in some scenarios.    *     * @since Guava release 07    */
annotation|@
name|Beta
DECL|method|create ( GenericMapMaker<? super E, ? super Number> mapMaker)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|GenericMapMaker
argument_list|<
name|?
super|super
name|E
argument_list|,
name|?
super|super
name|Number
argument_list|>
name|mapMaker
parameter_list|)
block|{
return|return
operator|new
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|mapMaker
operator|.
expr|<
name|E
argument_list|,
name|Integer
operator|>
name|makeMap
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an instance using {@code countMap} to store elements and their    * counts.    *    *<p>This instance will assume ownership of {@code countMap}, and other code    * should not maintain references to the map or modify it in any way.    *    * @param countMap backing map for storing the elements in the multiset and    *     their counts. It must be empty.    * @throws IllegalArgumentException if {@code countMap} is not empty    */
DECL|method|ConcurrentHashMultiset ( ConcurrentMap<E, Integer> countMap)
annotation|@
name|VisibleForTesting
name|ConcurrentHashMultiset
parameter_list|(
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|countMap
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|countMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|countMap
operator|=
name|countMap
expr_stmt|;
block|}
comment|// Query Operations
comment|/**    * Returns the number of occurrences of {@code element} in this multiset.    *    * @param element the element to look for    * @return the nonnegative number of occurrences of the element    */
DECL|method|count (@ullable Object element)
annotation|@
name|Override
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|unbox
argument_list|(
name|countMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/**    * {@inheritDoc}    *    *<p>If the data in the multiset is modified by any other threads during this    * method, it is undefined which (if any) of these modifications will be    * reflected in the result.    */
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
name|long
name|sum
init|=
literal|0L
decl_stmt|;
for|for
control|(
name|Integer
name|value
range|:
name|countMap
operator|.
name|values
argument_list|()
control|)
block|{
name|sum
operator|+=
name|value
expr_stmt|;
block|}
return|return
name|Ints
operator|.
name|saturatedCast
argument_list|(
name|sum
argument_list|)
return|;
block|}
comment|/*    * Note: the superclass toArray() methods assume that size() gives a correct    * answer, which ours does not.    */
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] array)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/*    * We'd love to use 'new ArrayList(this)' or 'list.addAll(this)', but    * either of these would recurse back to us again!    */
DECL|method|snapshot ()
specifier|private
name|List
argument_list|<
name|E
argument_list|>
name|snapshot
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|entrySet
argument_list|()
control|)
block|{
name|E
name|element
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|entry
operator|.
name|getCount
argument_list|()
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
comment|// Modification Operations
comment|/**    * Adds a number of occurrences of the specified element to this multiset.    *    * @param element the element to add    * @param occurrences the number of occurrences to add    * @return the previous count of the element before the operation; possibly    *     zero    * @throws IllegalArgumentException if {@code occurrences} is negative, or if    *     the resulting amount would exceed {@link Integer#MAX_VALUE}    */
DECL|method|add (E element, int occurrences)
annotation|@
name|Override
specifier|public
name|int
name|add
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
block|}
else|else
block|{
name|checkArgument
argument_list|(
name|occurrences
operator|<=
name|Integer
operator|.
name|MAX_VALUE
operator|-
name|current
argument_list|,
literal|"Overflow adding %s occurrences to a count of %s"
argument_list|,
name|occurrences
argument_list|,
name|current
argument_list|)
expr_stmt|;
name|int
name|next
init|=
name|current
operator|+
name|occurrences
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|current
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**    * Removes a number of occurrences of the specified element from this    * multiset. If the multiset contains fewer than this number of occurrences to    * begin with, all occurrences will be removed.    *    * @param element the element whose occurrences should be removed    * @param occurrences the number of occurrences of the element to remove    * @return the count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code occurrences} is negative    */
DECL|method|remove (@ullable Object element, int occurrences)
annotation|@
name|Override
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|occurrences
operator|>=
name|current
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|current
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
else|else
block|{
comment|// We know it's an "E" because it already exists in the map.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|E
name|casted
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|casted
argument_list|,
name|current
argument_list|,
name|current
operator|-
name|occurrences
argument_list|)
condition|)
block|{
return|return
name|current
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**    * Removes<b>all</b> occurrences of the specified element from this multiset.    * This method complements {@link Multiset#remove(Object)}, which removes only    * one occurrence at a time.    *    * @param element the element whose occurrences should all be removed    * @return the number of occurrences successfully removed, possibly zero    */
DECL|method|removeAllOccurrences (@ullable Object element)
specifier|private
name|int
name|removeAllOccurrences
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|unbox
argument_list|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
comment|/**    * Removes exactly the specified number of occurrences of {@code element}, or    * makes no change if this is not possible.    *    *<p>This method, in contrast to {@link #remove(Object, int)}, has no effect    * when the element count is smaller than {@code occurrences}.    *    * @param element the element to remove    * @param occurrences the number of occurrences of {@code element} to remove    * @return {@code true} if the removal was possible (including if {@code    *     occurrences} is zero)    */
DECL|method|removeExactly (@ullable Object element, int occurrences)
specifier|public
name|boolean
name|removeExactly
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
name|checkArgument
argument_list|(
name|occurrences
operator|>
literal|0
argument_list|,
literal|"Invalid occurrences: %s"
argument_list|,
name|occurrences
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|current
init|=
name|count
argument_list|(
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|occurrences
operator|>
name|current
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|occurrences
operator|==
name|current
condition|)
block|{
if|if
condition|(
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|occurrences
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// it's in the map, must be an "E"
name|E
name|casted
init|=
operator|(
name|E
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|countMap
operator|.
name|replace
argument_list|(
name|casted
argument_list|,
name|current
argument_list|,
name|current
operator|-
name|occurrences
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**    * Adds or removes occurrences of {@code element} such that the {@link #count}    * of the element becomes {@code count}.    *    * @return the count of {@code element} in the multiset before this call    * @throws IllegalArgumentException if {@code count} is negative    */
DECL|method|setCount (E element, int count)
annotation|@
name|Override
specifier|public
name|int
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|count
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
return|return
operator|(
name|count
operator|==
literal|0
operator|)
condition|?
name|removeAllOccurrences
argument_list|(
name|element
argument_list|)
else|:
name|unbox
argument_list|(
name|countMap
operator|.
name|put
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Sets the number of occurrences of {@code element} to {@code newCount}, but    * only if the count is currently {@code oldCount}. If {@code element} does    * not appear in the multiset exactly {@code oldCount} times, no changes will    * be made.    *    * @return {@code true} if the change was successful. This usually indicates    *     that the multiset has been modified, but not always: in the case that    *     {@code oldCount == newCount}, the method will return {@code true} if    *     the condition was met.    * @throws IllegalArgumentException if {@code oldCount} or {@code newCount} is    *     negative    */
DECL|method|setCount (E element, int oldCount, int newCount)
annotation|@
name|Override
specifier|public
name|boolean
name|setCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|oldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|oldCount
argument_list|,
literal|"oldCount"
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|newCount
argument_list|,
literal|"newCount"
argument_list|)
expr_stmt|;
if|if
condition|(
name|newCount
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
comment|// No change to make, but must return true if the element is not present
return|return
operator|!
name|countMap
operator|.
name|containsKey
argument_list|(
name|element
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|oldCount
operator|==
literal|0
condition|)
block|{
return|return
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|newCount
argument_list|)
operator|==
literal|null
return|;
block|}
return|return
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|oldCount
argument_list|,
name|newCount
argument_list|)
return|;
block|}
comment|// Views
DECL|method|createElementSet ()
annotation|@
name|Override
name|Set
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
init|=
name|countMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
return|return
operator|new
name|ForwardingSet
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
try|try
block|{
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|;
block|}
DECL|field|entrySet
specifier|private
specifier|transient
name|EntrySet
name|entrySet
decl_stmt|;
DECL|method|entrySet ()
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|EntrySet
name|result
init|=
name|entrySet
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|entrySet
operator|=
name|result
operator|=
operator|new
name|EntrySet
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|AbstractSet
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|countMap
operator|.
name|size
argument_list|()
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
name|countMap
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Multiset
operator|.
name|Entry
condition|)
block|{
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
init|=
operator|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|Object
name|element
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
return|return
name|entryCount
operator|>
literal|0
operator|&&
name|count
argument_list|(
name|element
argument_list|)
operator|==
name|entryCount
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|backingIterator
init|=
name|countMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|backingIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|backingEntry
init|=
name|backingIterator
operator|.
name|next
argument_list|()
decl_stmt|;
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|backingEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|backingEntry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|backingIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/*      * Note: the superclass toArray() methods assume that size() gives a correct      * answer, which ours does not.      */
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] array)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|snapshot
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/*      * We'd love to use 'new ArrayList(this)' or 'list.addAll(this)', but      * either of these would recurse back to us again!      */
DECL|method|snapshot ()
specifier|private
name|List
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|snapshot
parameter_list|()
block|{
name|List
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|list
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|this
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|remove (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Multiset
operator|.
name|Entry
condition|)
block|{
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
name|entry
init|=
operator|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|Object
name|element
init|=
name|entry
operator|.
name|getElement
argument_list|()
decl_stmt|;
name|int
name|entryCount
init|=
name|entry
operator|.
name|getCount
argument_list|()
decl_stmt|;
return|return
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|entryCount
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|countMap
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * The hash code is the same as countMap's, though the objects aren't equal.      */
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|countMap
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
comment|/**    * We use a special form of unboxing that treats null as zero.    */
DECL|method|unbox (@ullable Integer i)
specifier|private
specifier|static
name|int
name|unbox
parameter_list|(
annotation|@
name|Nullable
name|Integer
name|i
parameter_list|)
block|{
return|return
operator|(
name|i
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|i
return|;
block|}
comment|/**    * @serialData the ConcurrentMap of elements and their counts.    */
DECL|method|writeObject (ObjectOutputStream stream)
specifier|private
name|void
name|writeObject
parameter_list|(
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|countMap
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|stream
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeObject
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
name|deserializedCountMap
init|=
operator|(
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|Integer
argument_list|>
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|FieldSettersHolder
operator|.
name|COUNT_MAP_FIELD_SETTER
operator|.
name|set
argument_list|(
name|this
argument_list|,
name|deserializedCountMap
argument_list|)
expr_stmt|;
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
end_class

end_unit

