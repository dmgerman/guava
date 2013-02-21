begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|Objects
operator|.
name|firstNonNull
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
name|base
operator|.
name|Preconditions
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
name|Supplier
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
name|Iterables
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
name|MapMaker
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
name|math
operator|.
name|RoundingMode
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
name|Semaphore
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReadWriteLock
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
name|locks
operator|.
name|ReentrantLock
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
name|locks
operator|.
name|ReentrantReadWriteLock
import|;
end_import

begin_comment
comment|/**  * A striped {@code Lock/Semaphore/ReadWriteLock}. This offers the underlying lock striping  * similar to that of {@code ConcurrentHashMap} in a reusable form, and extends it for  * semaphores and read-write locks. Conceptually, lock striping is the technique of dividing a lock  * into many<i>stripes</i>, increasing the granularity of a single lock and allowing independent  * operations to lock different stripes and proceed concurrently, instead of creating contention  * for a single lock.  *  *<p>The guarantee provided by this class is that equal keys lead to the same lock (or semaphore),  * i.e. {@code if (key1.equals(key2))} then {@code striped.get(key1) == striped.get(key2)}  * (assuming {@link Object#hashCode()} is correctly implemented for the keys). Note  * that if {@code key1} is<strong>not</strong> equal to {@code key2}, it is<strong>not</strong>  * guaranteed that {@code striped.get(key1) != striped.get(key2)}; the elements might nevertheless  * be mapped to the same lock. The lower the number of stripes, the higher the probability of this  * happening.  *  *<p>There are three flavors of this class: {@code Striped<Lock>}, {@code Striped<Semaphore>},  * and {@code Striped<ReadWriteLock>}. For each type, two implementations are offered:  * {@linkplain #lock(int) strong} and {@linkplain #lazyWeakLock(int) weak}  * {@code Striped<Lock>}, {@linkplain #semaphore(int, int) strong} and {@linkplain  * #lazyWeakSemaphore(int, int) weak} {@code Striped<Semaphore>}, and {@linkplain  * #readWriteLock(int) strong} and {@linkplain #lazyWeakReadWriteLock(int) weak}  * {@code Striped<ReadWriteLock>}.<i>Strong</i> means that all stripes (locks/semaphores) are  * initialized eagerly, and are not reclaimed unless {@code Striped} itself is reclaimable.  *<i>Weak</i> means that locks/semaphores are created lazily, and they are allowed to be reclaimed  * if nobody is holding on to them. This is useful, for example, if one wants to create a {@code  * Striped<Lock>} of many locks, but worries that in most cases only a small portion of these  * would be in use.  *  *<p>Prior to this class, one might be tempted to use {@code Map<K, Lock>}, where {@code K}  * represents the task. This maximizes concurrency by having each unique key mapped to a unique  * lock, but also maximizes memory footprint. On the other extreme, one could use a single lock  * for all tasks, which minimizes memory footprint but also minimizes concurrency. Instead of  * choosing either of these extremes, {@code Striped} allows the user to trade between required  * concurrency and memory footprint. For example, if a set of tasks are CPU-bound, one could easily  * create a very compact {@code Striped<Lock>} of {@code availableProcessors() * 4} stripes,  * instead of possibly thousands of locks which could be created in a {@code Map<K, Lock>}  * structure.  *  * @author Dimitris Andreou  * @since 13.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Striped
specifier|public
specifier|abstract
class|class
name|Striped
parameter_list|<
name|L
parameter_list|>
block|{
DECL|method|Striped ()
specifier|private
name|Striped
parameter_list|()
block|{}
comment|/**    * Returns the stripe that corresponds to the passed key. It is always guaranteed that if    * {@code key1.equals(key2)}, then {@code get(key1) == get(key2)}.    *    * @param key an arbitrary, non-null key    * @return the stripe that the passed key corresponds to    */
DECL|method|get (Object key)
specifier|public
specifier|abstract
name|L
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the stripe at the specified index. Valid indexes are 0, inclusively, to    * {@code size()}, exclusively.    *    * @param index the index of the stripe to return; must be in {@code [0...size())}    * @return the stripe at the specified index    */
DECL|method|getAt (int index)
specifier|public
specifier|abstract
name|L
name|getAt
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**    * Returns the index to which the given key is mapped, so that getAt(indexFor(key)) == get(key).    */
DECL|method|indexFor (Object key)
specifier|abstract
name|int
name|indexFor
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**    * Returns the total number of stripes in this instance.    */
DECL|method|size ()
specifier|public
specifier|abstract
name|int
name|size
parameter_list|()
function_decl|;
comment|/**    * Returns the stripes that correspond to the passed objects, in ascending (as per    * {@link #getAt(int)}) order. Thus, threads that use the stripes in the order returned    * by this method are guaranteed to not deadlock each other.    *    *<p>It should be noted that using a {@code Striped<L>} with relatively few stripes, and    * {@code bulkGet(keys)} with a relative large number of keys can cause an excessive number    * of shared stripes (much like the birthday paradox, where much fewer than anticipated birthdays    * are needed for a pair of them to match). Please consider carefully the implications of the    * number of stripes, the intended concurrency level, and the typical number of keys used in a    * {@code bulkGet(keys)} operation. See<a href="http://www.mathpages.com/home/kmath199.htm">Balls    * in Bins model</a> for mathematical formulas that can be used to estimate the probability of    * collisions.    *    * @param keys arbitrary non-null keys    * @return the stripes corresponding to the objects (one per each object, derived by delegating    *         to {@link #get(Object)}; may contain duplicates), in an increasing index order.    */
DECL|method|bulkGet (Iterable<?> keys)
specifier|public
name|Iterable
argument_list|<
name|L
argument_list|>
name|bulkGet
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
block|{
comment|// Initially using the array to store the keys, then reusing it to store the respective L's
specifier|final
name|Object
index|[]
name|array
init|=
name|Iterables
operator|.
name|toArray
argument_list|(
name|keys
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
index|[]
name|stripes
init|=
operator|new
name|int
index|[
name|array
operator|.
name|length
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
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|stripes
index|[
name|i
index|]
operator|=
name|indexFor
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|Arrays
operator|.
name|sort
argument_list|(
name|stripes
argument_list|)
expr_stmt|;
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
name|getAt
argument_list|(
name|stripes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
comment|/*      * Note that the returned Iterable holds references to the returned stripes, to avoid      * error-prone code like:      *      * Striped<Lock> stripedLock = Striped.lazyWeakXXX(...)'      * Iterable<Lock> locks = stripedLock.bulkGet(keys);      * for (Lock lock : locks) {      *   lock.lock();      * }      * operation();      * for (Lock lock : locks) {      *   lock.unlock();      * }      *      * If we only held the int[] stripes, translating it on the fly to L's, the original locks      * might be garbage collected after locking them, ending up in a huge mess.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we carefully replaced all keys with their respective L's
name|List
argument_list|<
name|L
argument_list|>
name|asList
init|=
operator|(
name|List
argument_list|<
name|L
argument_list|>
operator|)
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|asList
argument_list|)
return|;
block|}
comment|// Static factories
comment|/**    * Creates a {@code Striped<Lock>} with eagerly initialized, strongly referenced locks, with the    * specified fairness. Every lock is reentrant.    *    * @param stripes the minimum number of stripes (locks) required    * @return a new {@code Striped<Lock>}    */
DECL|method|lock (int stripes)
specifier|public
specifier|static
name|Striped
argument_list|<
name|Lock
argument_list|>
name|lock
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|CompactStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
name|stripes
argument_list|,
operator|new
name|Supplier
argument_list|<
name|Lock
argument_list|>
argument_list|()
block|{
specifier|public
name|Lock
name|get
parameter_list|()
block|{
return|return
operator|new
name|PaddedLock
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code Striped<Lock>} with lazily initialized, weakly referenced locks, with the    * specified fairness. Every lock is reentrant.    *    * @param stripes the minimum number of stripes (locks) required    * @return a new {@code Striped<Lock>}    */
DECL|method|lazyWeakLock (int stripes)
specifier|public
specifier|static
name|Striped
argument_list|<
name|Lock
argument_list|>
name|lazyWeakLock
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|LazyStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
name|stripes
argument_list|,
operator|new
name|Supplier
argument_list|<
name|Lock
argument_list|>
argument_list|()
block|{
specifier|public
name|Lock
name|get
parameter_list|()
block|{
return|return
operator|new
name|ReentrantLock
argument_list|(
literal|false
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code Striped<Semaphore>} with eagerly initialized, strongly referenced semaphores,    * with the specified number of permits and fairness.    *    * @param stripes the minimum number of stripes (semaphores) required    * @param permits the number of permits in each semaphore    * @return a new {@code Striped<Semaphore>}    */
DECL|method|semaphore (int stripes, final int permits)
specifier|public
specifier|static
name|Striped
argument_list|<
name|Semaphore
argument_list|>
name|semaphore
parameter_list|(
name|int
name|stripes
parameter_list|,
specifier|final
name|int
name|permits
parameter_list|)
block|{
return|return
operator|new
name|CompactStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
name|stripes
argument_list|,
operator|new
name|Supplier
argument_list|<
name|Semaphore
argument_list|>
argument_list|()
block|{
specifier|public
name|Semaphore
name|get
parameter_list|()
block|{
return|return
operator|new
name|PaddedSemaphore
argument_list|(
name|permits
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code Striped<Semaphore>} with lazily initialized, weakly referenced semaphores,    * with the specified number of permits and fairness.    *    * @param stripes the minimum number of stripes (semaphores) required    * @param permits the number of permits in each semaphore    * @return a new {@code Striped<Semaphore>}    */
DECL|method|lazyWeakSemaphore (int stripes, final int permits)
specifier|public
specifier|static
name|Striped
argument_list|<
name|Semaphore
argument_list|>
name|lazyWeakSemaphore
parameter_list|(
name|int
name|stripes
parameter_list|,
specifier|final
name|int
name|permits
parameter_list|)
block|{
return|return
operator|new
name|LazyStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
name|stripes
argument_list|,
operator|new
name|Supplier
argument_list|<
name|Semaphore
argument_list|>
argument_list|()
block|{
specifier|public
name|Semaphore
name|get
parameter_list|()
block|{
return|return
operator|new
name|Semaphore
argument_list|(
name|permits
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code Striped<ReadWriteLock>} with eagerly initialized, strongly referenced    * read-write locks, with the specified fairness. Every lock is reentrant.    *    * @param stripes the minimum number of stripes (locks) required    * @return a new {@code Striped<ReadWriteLock>}    */
DECL|method|readWriteLock (int stripes)
specifier|public
specifier|static
name|Striped
argument_list|<
name|ReadWriteLock
argument_list|>
name|readWriteLock
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|CompactStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
name|stripes
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code Striped<ReadWriteLock>} with lazily initialized, weakly referenced    * read-write locks, with the specified fairness. Every lock is reentrant.    *    * @param stripes the minimum number of stripes (locks) required    * @return a new {@code Striped<ReadWriteLock>}    */
DECL|method|lazyWeakReadWriteLock (int stripes)
specifier|public
specifier|static
name|Striped
argument_list|<
name|ReadWriteLock
argument_list|>
name|lazyWeakReadWriteLock
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
return|return
operator|new
name|LazyStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
name|stripes
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
return|;
block|}
comment|// ReentrantReadWriteLock is large enough to make padding probably unnecessary
DECL|field|READ_WRITE_LOCK_SUPPLIER
specifier|private
specifier|static
specifier|final
name|Supplier
argument_list|<
name|ReadWriteLock
argument_list|>
name|READ_WRITE_LOCK_SUPPLIER
init|=
operator|new
name|Supplier
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|()
block|{
specifier|public
name|ReadWriteLock
name|get
parameter_list|()
block|{
return|return
operator|new
name|ReentrantReadWriteLock
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|class|PowerOfTwoStriped
specifier|private
specifier|abstract
specifier|static
class|class
name|PowerOfTwoStriped
parameter_list|<
name|L
parameter_list|>
extends|extends
name|Striped
argument_list|<
name|L
argument_list|>
block|{
comment|/** Capacity (power of two) minus one, for fast mod evaluation */
DECL|field|mask
specifier|final
name|int
name|mask
decl_stmt|;
DECL|method|PowerOfTwoStriped (int stripes)
name|PowerOfTwoStriped
parameter_list|(
name|int
name|stripes
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|stripes
operator|>
literal|0
argument_list|,
literal|"Stripes must be positive"
argument_list|)
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|stripes
operator|>
name|Ints
operator|.
name|MAX_POWER_OF_TWO
condition|?
name|ALL_SET
else|:
name|ceilToPowerOfTwo
argument_list|(
name|stripes
argument_list|)
operator|-
literal|1
expr_stmt|;
block|}
DECL|method|indexFor (Object key)
annotation|@
name|Override
specifier|final
name|int
name|indexFor
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|int
name|hash
init|=
name|smear
argument_list|(
name|key
operator|.
name|hashCode
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|hash
operator|&
name|mask
return|;
block|}
DECL|method|get (Object key)
annotation|@
name|Override
specifier|public
specifier|final
name|L
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|getAt
argument_list|(
name|indexFor
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**    * Implementation of Striped where 2^k stripes are represented as an array of the same length,    * eagerly initialized.    */
DECL|class|CompactStriped
specifier|private
specifier|static
class|class
name|CompactStriped
parameter_list|<
name|L
parameter_list|>
extends|extends
name|PowerOfTwoStriped
argument_list|<
name|L
argument_list|>
block|{
comment|/** Size is a power of two. */
DECL|field|array
specifier|private
specifier|final
name|Object
index|[]
name|array
decl_stmt|;
DECL|method|CompactStriped (int stripes, Supplier<L> supplier)
specifier|private
name|CompactStriped
parameter_list|(
name|int
name|stripes
parameter_list|,
name|Supplier
argument_list|<
name|L
argument_list|>
name|supplier
parameter_list|)
block|{
name|super
argument_list|(
name|stripes
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|stripes
operator|<=
name|Ints
operator|.
name|MAX_POWER_OF_TWO
argument_list|,
literal|"Stripes must be<= 2^30)"
argument_list|)
expr_stmt|;
name|this
operator|.
name|array
operator|=
operator|new
name|Object
index|[
name|mask
operator|+
literal|1
index|]
expr_stmt|;
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
name|supplier
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// we only put L's in the array
DECL|method|getAt (int index)
annotation|@
name|Override
specifier|public
name|L
name|getAt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
operator|(
name|L
operator|)
name|array
index|[
name|index
index|]
return|;
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
name|array
operator|.
name|length
return|;
block|}
block|}
comment|/**    * Implementation of Striped where up to 2^k stripes can be represented, using a Cache    * where the key domain is [0..2^k). To map a user key into a stripe, we take a k-bit slice of the    * user key's (smeared) hashCode(). The stripes are lazily initialized and are weakly referenced.    */
DECL|class|LazyStriped
specifier|private
specifier|static
class|class
name|LazyStriped
parameter_list|<
name|L
parameter_list|>
extends|extends
name|PowerOfTwoStriped
argument_list|<
name|L
argument_list|>
block|{
DECL|field|locks
specifier|final
name|ConcurrentMap
argument_list|<
name|Integer
argument_list|,
name|L
argument_list|>
name|locks
decl_stmt|;
DECL|field|supplier
specifier|final
name|Supplier
argument_list|<
name|L
argument_list|>
name|supplier
decl_stmt|;
DECL|field|size
specifier|final
name|int
name|size
decl_stmt|;
DECL|method|LazyStriped (int stripes, Supplier<L> supplier)
name|LazyStriped
parameter_list|(
name|int
name|stripes
parameter_list|,
name|Supplier
argument_list|<
name|L
argument_list|>
name|supplier
parameter_list|)
block|{
name|super
argument_list|(
name|stripes
argument_list|)
expr_stmt|;
name|this
operator|.
name|size
operator|=
operator|(
name|mask
operator|==
name|ALL_SET
operator|)
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|mask
operator|+
literal|1
expr_stmt|;
name|this
operator|.
name|supplier
operator|=
name|supplier
expr_stmt|;
name|this
operator|.
name|locks
operator|=
operator|new
name|MapMaker
argument_list|()
operator|.
name|weakValues
argument_list|()
operator|.
name|makeMap
argument_list|()
expr_stmt|;
block|}
DECL|method|getAt (int index)
annotation|@
name|Override
specifier|public
name|L
name|getAt
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|L
name|existing
init|=
name|locks
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
return|return
name|existing
return|;
block|}
name|L
name|created
init|=
name|supplier
operator|.
name|get
argument_list|()
decl_stmt|;
name|existing
operator|=
name|locks
operator|.
name|putIfAbsent
argument_list|(
name|index
argument_list|,
name|created
argument_list|)
expr_stmt|;
return|return
name|firstNonNull
argument_list|(
name|existing
argument_list|,
name|created
argument_list|)
return|;
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
name|size
return|;
block|}
block|}
comment|/**    * A bit mask were all bits are set.    */
DECL|field|ALL_SET
specifier|private
specifier|static
specifier|final
name|int
name|ALL_SET
init|=
operator|~
literal|0
decl_stmt|;
DECL|method|ceilToPowerOfTwo (int x)
specifier|private
specifier|static
name|int
name|ceilToPowerOfTwo
parameter_list|(
name|int
name|x
parameter_list|)
block|{
return|return
literal|1
operator|<<
name|IntMath
operator|.
name|log2
argument_list|(
name|x
argument_list|,
name|RoundingMode
operator|.
name|CEILING
argument_list|)
return|;
block|}
comment|/*    * This method was written by Doug Lea with assistance from members of JCP    * JSR-166 Expert Group and released to the public domain, as explained at    * http://creativecommons.org/licenses/publicdomain    *    * As of 2010/06/11, this method is identical to the (package private) hash    * method in OpenJDK 7's java.util.HashMap class.    */
comment|// Copied from java/com/google/common/collect/Hashing.java
DECL|method|smear (int hashCode)
specifier|private
specifier|static
name|int
name|smear
parameter_list|(
name|int
name|hashCode
parameter_list|)
block|{
name|hashCode
operator|^=
operator|(
name|hashCode
operator|>>>
literal|20
operator|)
operator|^
operator|(
name|hashCode
operator|>>>
literal|12
operator|)
expr_stmt|;
return|return
name|hashCode
operator|^
operator|(
name|hashCode
operator|>>>
literal|7
operator|)
operator|^
operator|(
name|hashCode
operator|>>>
literal|4
operator|)
return|;
block|}
DECL|class|PaddedLock
specifier|private
specifier|static
class|class
name|PaddedLock
extends|extends
name|ReentrantLock
block|{
comment|/*      * Padding from 40 into 64 bytes, same size as cache line. Might be beneficial to add      * a fourth long here, to minimize chance of interference between consecutive locks,      * but I couldn't observe any benefit from that.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|q1
DECL|field|q2
DECL|field|q3
name|long
name|q1
decl_stmt|,
name|q2
decl_stmt|,
name|q3
decl_stmt|;
DECL|method|PaddedLock ()
name|PaddedLock
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PaddedSemaphore
specifier|private
specifier|static
class|class
name|PaddedSemaphore
extends|extends
name|Semaphore
block|{
comment|// See PaddedReentrantLock comment
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|q1
DECL|field|q2
DECL|field|q3
name|long
name|q1
decl_stmt|,
name|q2
decl_stmt|,
name|q3
decl_stmt|;
DECL|method|PaddedSemaphore (int permits)
name|PaddedSemaphore
parameter_list|(
name|int
name|permits
parameter_list|)
block|{
name|super
argument_list|(
name|permits
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

