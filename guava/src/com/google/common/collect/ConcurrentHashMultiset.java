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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|CollectPreconditions
operator|.
name|checkRemove
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
name|math
operator|.
name|IntMath
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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
comment|/**  * A multiset that supports concurrent modifications and that provides atomic versions of most  * {@code Multiset} operations (exceptions where noted). Null elements are not supported.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#Multiset">  * {@code Multiset}</a>.  *  * @author Cliff L. Biffle  * @author mike nonemacher  * @since 2.0 (imported from Google Collections Library)  */
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
comment|/*    * The ConcurrentHashMultiset's atomic operations are implemented primarily in terms of    * AtomicInteger's atomic operations, with some help from ConcurrentMap's atomic operations on    * creation and removal (including automatic removal of zeroes). If the modification of an    * AtomicInteger results in zero, we compareAndSet the value to zero; if that succeeds, we remove    * the entry from the Map. If another operation sees a zero in the map, it knows that the entry is    * about to be removed, so this operation may remove it (often by replacing it with a new    * AtomicInteger).    */
comment|/** The number of occurrences of each element. */
DECL|field|countMap
specifier|private
specifier|final
specifier|transient
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
name|countMap
decl_stmt|;
comment|// This constant allows the deserialization code to set a final field. This holder class
comment|// makes sure it is not initialized unless an instance is deserialized.
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
comment|// TODO(schmoe): provide a way to use this class with other (possibly arbitrary)
comment|// ConcurrentMap implementors. One possibility is to extract most of this class into
comment|// an AbstractConcurrentMapMultiset.
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
name|AtomicInteger
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates a new {@code ConcurrentHashMultiset} containing the specified elements, using    * the default initial capacity, load factor, and concurrency settings.    *    *<p>This implementation is highly efficient when {@code elements} is itself a {@link Multiset}.    *    * @param elements the elements that the multiset should contain    */
DECL|method|create (Iterable<? extends E> elements)
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
comment|/**    * Creates a new, empty {@code ConcurrentHashMultiset} using {@code mapMaker}    * to construct the internal backing map.    *    *<p>If this {@link MapMaker} is configured to use entry eviction of any kind, this eviction    * applies to all occurrences of a given element as a single unit. However, most updates to the    * multiset do not count as map updates at all, since we're usually just mutating the value    * stored in the map, so {@link MapMaker#expireAfterAccess} makes sense (evict the entry that    * was queried or updated longest ago), but {@link MapMaker#expireAfterWrite} doesn't, because    * the eviction time is measured from when we saw the first occurrence of the object.    *    *<p>The returned multiset is serializable but any serialization caveats    * given in {@code MapMaker} apply.    *    *<p>Finally, soft/weak values can be used but are not very useful: the values are created    * internally and not exposed externally, so no one else will have a strong reference to the    * values. Weak keys on the other hand can be useful in some scenarios.    *    * @since 15.0 (source compatible (accepting the since removed {@code GenericMapMaker} class)    *     since 7.0)    */
annotation|@
name|Beta
DECL|method|create (MapMaker mapMaker)
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
name|MapMaker
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
name|AtomicInteger
operator|>
name|makeMap
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an instance using {@code countMap} to store elements and their counts.    *    *<p>This instance will assume ownership of {@code countMap}, and other code    * should not maintain references to the map or modify it in any way.    *    * @param countMap backing map for storing the elements in the multiset and    *     their counts. It must be empty.    * @throws IllegalArgumentException if {@code countMap} is not empty    */
DECL|method|ConcurrentHashMultiset (ConcurrentMap<E, AtomicInteger> countMap)
annotation|@
name|VisibleForTesting
name|ConcurrentHashMultiset
parameter_list|(
name|ConcurrentMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
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
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
return|return
operator|(
name|existingCounter
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|existingCounter
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>If the data in the multiset is modified by any other threads during this method,    * it is undefined which (if any) of these modifications will be reflected in the result.    */
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
name|AtomicInteger
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
operator|.
name|get
argument_list|()
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
comment|/**    * Adds a number of occurrences of the specified element to this multiset.    *    * @param element the element to add    * @param occurrences the number of occurrences to add    * @return the previous count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code occurrences} is negative, or if    *     the resulting amount would exceed {@link Integer#MAX_VALUE}    */
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
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
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
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
name|existingCounter
operator|=
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
name|occurrences
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// existingCounter != null: fall through to operate against the existing AtomicInteger
block|}
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|oldValue
init|=
name|existingCounter
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|0
condition|)
block|{
try|try
block|{
name|int
name|newValue
init|=
name|IntMath
operator|.
name|checkedAdd
argument_list|(
name|oldValue
argument_list|,
name|occurrences
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
comment|// newValue can't == 0, so no need to check& remove
return|return
name|oldValue
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|overflow
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Overflow adding "
operator|+
name|occurrences
operator|+
literal|" occurrences to a count of "
operator|+
name|oldValue
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// In the case of a concurrent remove, we might observe a zero value, which means another
comment|// thread is about to remove (element, existingCounter) from the map. Rather than wait,
comment|// we can just do that work here.
name|AtomicInteger
name|newCounter
init|=
operator|new
name|AtomicInteger
argument_list|(
name|occurrences
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|newCounter
argument_list|)
operator|==
literal|null
operator|)
operator|||
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|,
name|newCounter
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
break|break;
block|}
block|}
comment|// If we're still here, there was a race, so just try again.
block|}
block|}
comment|/**    * Removes a number of occurrences of the specified element from this multiset. If the multiset    * contains fewer than this number of occurrences to begin with, all occurrences will be removed.    *    * @param element the element whose occurrences should be removed    * @param occurrences the number of occurrences of the element to remove    * @return the count of the element before the operation; possibly zero    * @throws IllegalArgumentException if {@code occurrences} is negative    */
comment|/*    * TODO(cpovirk): remove and removeExactly currently accept null inputs only    * if occurrences == 0. This satisfies both NullPointerTester and    * CollectionRemoveTester.testRemove_nullAllowed, but it's not clear that it's    * a good policy, especially because, in order for the test to pass, the    * parameter must be misleadingly annotated as @Nullable. I suspect that    * we'll want to remove @Nullable, add an eager checkNotNull, and loosen up    * testRemove_nullAllowed.    */
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
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|oldValue
init|=
name|existingCounter
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|0
condition|)
block|{
name|int
name|newValue
init|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|oldValue
operator|-
name|occurrences
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
if|if
condition|(
name|newValue
operator|==
literal|0
condition|)
block|{
comment|// Just CASed to 0; remove the entry to clean up the map. If the removal fails,
comment|// another thread has already replaced it with a new counter, which is fine.
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|)
expr_stmt|;
block|}
return|return
name|oldValue
return|;
block|}
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
block|}
comment|/**    * Removes exactly the specified number of occurrences of {@code element}, or makes no    * change if this is not possible.    *    *<p>This method, in contrast to {@link #remove(Object, int)}, has no effect when the    * element count is smaller than {@code occurrences}.    *    * @param element the element to remove    * @param occurrences the number of occurrences of {@code element} to remove    * @return {@code true} if the removal was possible (including if {@code occurrences} is zero)    */
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
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|oldValue
init|=
name|existingCounter
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|<
name|occurrences
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|newValue
init|=
name|oldValue
operator|-
name|occurrences
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
condition|)
block|{
if|if
condition|(
name|newValue
operator|==
literal|0
condition|)
block|{
comment|// Just CASed to 0; remove the entry to clean up the map. If the removal fails,
comment|// another thread has already replaced it with a new counter, which is fine.
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
comment|/**    * Adds or removes occurrences of {@code element} such that the {@link #count} of the    * element becomes {@code count}.    *    * @return the count of {@code element} in the multiset before this call    * @throws IllegalArgumentException if {@code count} is negative    */
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
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|count
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
name|existingCounter
operator|=
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
name|count
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// existingCounter != null: fall through
block|}
block|}
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|oldValue
init|=
name|existingCounter
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
return|return
literal|0
return|;
block|}
else|else
block|{
name|AtomicInteger
name|newCounter
init|=
operator|new
name|AtomicInteger
argument_list|(
name|count
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|newCounter
argument_list|)
operator|==
literal|null
operator|)
operator|||
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|,
name|newCounter
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
block|}
break|break;
block|}
else|else
block|{
if|if
condition|(
name|existingCounter
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|count
argument_list|)
condition|)
block|{
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
comment|// Just CASed to 0; remove the entry to clean up the map. If the removal fails,
comment|// another thread has already replaced it with a new counter, which is fine.
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|)
expr_stmt|;
block|}
return|return
name|oldValue
return|;
block|}
block|}
block|}
block|}
block|}
comment|/**    * Sets the number of occurrences of {@code element} to {@code newCount}, but only if    * the count is currently {@code expectedOldCount}. If {@code element} does not appear    * in the multiset exactly {@code expectedOldCount} times, no changes will be made.    *    * @return {@code true} if the change was successful. This usually indicates    *     that the multiset has been modified, but not always: in the case that    *     {@code expectedOldCount == newCount}, the method will return {@code true} if    *     the condition was met.    * @throws IllegalArgumentException if {@code expectedOldCount} or {@code newCount} is negative    */
DECL|method|setCount (E element, int expectedOldCount, int newCount)
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
name|expectedOldCount
parameter_list|,
name|int
name|newCount
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|checkNonnegative
argument_list|(
name|expectedOldCount
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
name|AtomicInteger
name|existingCounter
init|=
name|Maps
operator|.
name|safeGet
argument_list|(
name|countMap
argument_list|,
name|element
argument_list|)
decl_stmt|;
if|if
condition|(
name|existingCounter
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|expectedOldCount
operator|!=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|newCount
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// if our write lost the race, it must have lost to a nonzero value, so we can stop
return|return
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
operator|new
name|AtomicInteger
argument_list|(
name|newCount
argument_list|)
argument_list|)
operator|==
literal|null
return|;
block|}
block|}
name|int
name|oldValue
init|=
name|existingCounter
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|oldValue
operator|==
name|expectedOldCount
condition|)
block|{
if|if
condition|(
name|oldValue
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|newCount
operator|==
literal|0
condition|)
block|{
comment|// Just observed a 0; try to remove the entry to clean up the map
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|AtomicInteger
name|newCounter
init|=
operator|new
name|AtomicInteger
argument_list|(
name|newCount
argument_list|)
decl_stmt|;
return|return
operator|(
name|countMap
operator|.
name|putIfAbsent
argument_list|(
name|element
argument_list|,
name|newCounter
argument_list|)
operator|==
literal|null
operator|)
operator|||
name|countMap
operator|.
name|replace
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|,
name|newCounter
argument_list|)
return|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|existingCounter
operator|.
name|compareAndSet
argument_list|(
name|oldValue
argument_list|,
name|newCount
argument_list|)
condition|)
block|{
if|if
condition|(
name|newCount
operator|==
literal|0
condition|)
block|{
comment|// Just CASed to 0; remove the entry to clean up the map. If the removal fails,
comment|// another thread has already replaced it with a new counter, which is fine.
name|countMap
operator|.
name|remove
argument_list|(
name|element
argument_list|,
name|existingCounter
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
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
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
operator|&&
name|Collections2
operator|.
name|safeContains
argument_list|(
name|delegate
argument_list|,
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
return|return
name|standardContainsAll
argument_list|(
name|collection
argument_list|)
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
return|return
name|object
operator|!=
literal|null
operator|&&
name|Collections2
operator|.
name|safeRemove
argument_list|(
name|delegate
argument_list|,
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|standardRemoveAll
argument_list|(
name|c
argument_list|)
return|;
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
DECL|method|distinctElements ()
annotation|@
name|Override
name|int
name|distinctElements
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
DECL|method|entryIterator ()
annotation|@
name|Override
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
comment|// AbstractIterator makes this fairly clean, but it doesn't support remove(). To support
comment|// remove(), we create an AbstractIterator, and then use ForwardingIterator to delegate to it.
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|readOnlyIterator
init|=
operator|new
name|AbstractIterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|private
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|>
name|mapEntries
init|=
name|countMap
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|E
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
if|if
condition|(
operator|!
name|mapEntries
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
name|endOfData
argument_list|()
return|;
block|}
name|Map
operator|.
name|Entry
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
name|mapEntry
init|=
name|mapEntries
operator|.
name|next
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|mapEntry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|!=
literal|0
condition|)
block|{
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|mapEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|count
argument_list|)
return|;
block|}
block|}
block|}
block|}
decl_stmt|;
return|return
operator|new
name|ForwardingIterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|private
name|Entry
argument_list|<
name|E
argument_list|>
name|last
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|readOnlyIterator
return|;
block|}
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|E
argument_list|>
name|next
parameter_list|()
block|{
name|last
operator|=
name|super
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|last
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|checkRemove
argument_list|(
name|last
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|ConcurrentHashMultiset
operator|.
name|this
operator|.
name|setCount
argument_list|(
name|last
operator|.
name|getElement
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|last
operator|=
literal|null
expr_stmt|;
block|}
block|}
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
DECL|class|EntrySet
specifier|private
class|class
name|EntrySet
extends|extends
name|AbstractMultiset
argument_list|<
name|E
argument_list|>
operator|.
name|EntrySet
block|{
DECL|method|multiset ()
annotation|@
name|Override
name|ConcurrentHashMultiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|ConcurrentHashMultiset
operator|.
name|this
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
comment|// Not Iterables.addAll(list, this), because that'll forward right back here.
name|Iterators
operator|.
name|addAll
argument_list|(
name|list
argument_list|,
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
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

