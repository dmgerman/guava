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
name|collect
operator|.
name|Iterables
operator|.
name|concat
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
name|Functions
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
name|ImmutableList
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
name|Lists
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
name|Maps
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
name|Ordering
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
name|Sets
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
name|testing
operator|.
name|GcFinalization
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
name|testing
operator|.
name|NullPointerTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for Striped.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|StripedTest
specifier|public
class|class
name|StripedTest
extends|extends
name|TestCase
block|{
DECL|method|strongImplementations ()
specifier|private
specifier|static
name|List
argument_list|<
name|Striped
argument_list|<
name|?
argument_list|>
argument_list|>
name|strongImplementations
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|Striped
operator|.
name|readWriteLock
argument_list|(
literal|100
argument_list|)
argument_list|,
name|Striped
operator|.
name|readWriteLock
argument_list|(
literal|256
argument_list|)
argument_list|,
name|Striped
operator|.
name|lock
argument_list|(
literal|100
argument_list|)
argument_list|,
name|Striped
operator|.
name|lock
argument_list|(
literal|256
argument_list|)
argument_list|,
name|Striped
operator|.
name|semaphore
argument_list|(
literal|100
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Striped
operator|.
name|semaphore
argument_list|(
literal|256
argument_list|,
literal|1
argument_list|)
argument_list|)
return|;
block|}
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
annotation|@
name|Override
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
DECL|field|LOCK_SUPPLER
specifier|private
specifier|static
specifier|final
name|Supplier
argument_list|<
name|Lock
argument_list|>
name|LOCK_SUPPLER
init|=
operator|new
name|Supplier
argument_list|<
name|Lock
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Lock
name|get
parameter_list|()
block|{
return|return
operator|new
name|ReentrantLock
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|SEMAPHORE_SUPPLER
specifier|private
specifier|static
specifier|final
name|Supplier
argument_list|<
name|Semaphore
argument_list|>
name|SEMAPHORE_SUPPLER
init|=
operator|new
name|Supplier
argument_list|<
name|Semaphore
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Semaphore
name|get
parameter_list|()
block|{
return|return
operator|new
name|Semaphore
argument_list|(
literal|1
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|weakImplementations ()
specifier|private
specifier|static
name|List
argument_list|<
name|Striped
argument_list|<
name|?
argument_list|>
argument_list|>
name|weakImplementations
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
expr|<
name|Striped
argument_list|<
name|?
argument_list|>
operator|>
name|builder
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
literal|50
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
literal|64
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
literal|50
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|ReadWriteLock
argument_list|>
argument_list|(
literal|64
argument_list|,
name|READ_WRITE_LOCK_SUPPLIER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
literal|50
argument_list|,
name|LOCK_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
literal|64
argument_list|,
name|LOCK_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
literal|50
argument_list|,
name|LOCK_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|Lock
argument_list|>
argument_list|(
literal|64
argument_list|,
name|LOCK_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
literal|50
argument_list|,
name|SEMAPHORE_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|SmallLazyStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
literal|64
argument_list|,
name|SEMAPHORE_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
literal|50
argument_list|,
name|SEMAPHORE_SUPPLER
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|Striped
operator|.
name|LargeLazyStriped
argument_list|<
name|Semaphore
argument_list|>
argument_list|(
literal|64
argument_list|,
name|SEMAPHORE_SUPPLER
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|allImplementations ()
specifier|private
specifier|static
name|Iterable
argument_list|<
name|Striped
argument_list|<
name|?
argument_list|>
argument_list|>
name|allImplementations
parameter_list|()
block|{
return|return
name|concat
argument_list|(
name|strongImplementations
argument_list|()
argument_list|,
name|weakImplementations
argument_list|()
argument_list|)
return|;
block|}
DECL|method|testNull ()
specifier|public
name|void
name|testNull
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|allImplementations
argument_list|()
control|)
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|striped
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSizes ()
specifier|public
name|void
name|testSizes
parameter_list|()
block|{
comment|// not bothering testing all variations, since we know they share implementations
name|assertTrue
argument_list|(
name|Striped
operator|.
name|lock
argument_list|(
literal|100
argument_list|)
operator|.
name|size
argument_list|()
operator|>=
literal|100
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Striped
operator|.
name|lock
argument_list|(
literal|256
argument_list|)
operator|.
name|size
argument_list|()
operator|==
literal|256
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Striped
operator|.
name|lazyWeakLock
argument_list|(
literal|100
argument_list|)
operator|.
name|size
argument_list|()
operator|>=
literal|100
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Striped
operator|.
name|lazyWeakLock
argument_list|(
literal|256
argument_list|)
operator|.
name|size
argument_list|()
operator|==
literal|256
argument_list|)
expr_stmt|;
block|}
DECL|method|testWeakImplementations ()
specifier|public
name|void
name|testWeakImplementations
parameter_list|()
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|weakImplementations
argument_list|()
control|)
block|{
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|weakRef
init|=
operator|new
name|WeakReference
argument_list|<
name|Object
argument_list|>
argument_list|(
name|striped
operator|.
name|get
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|weakRef
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testStrongImplementations ()
specifier|public
name|void
name|testStrongImplementations
parameter_list|()
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|strongImplementations
argument_list|()
control|)
block|{
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|weakRef
init|=
operator|new
name|WeakReference
argument_list|<
name|Object
argument_list|>
argument_list|(
name|striped
operator|.
name|get
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|WeakReference
argument_list|<
name|Object
argument_list|>
name|garbage
init|=
operator|new
name|WeakReference
argument_list|<
name|Object
argument_list|>
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|GcFinalization
operator|.
name|awaitClear
argument_list|(
name|garbage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|weakRef
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMaximalWeakStripedLock ()
specifier|public
name|void
name|testMaximalWeakStripedLock
parameter_list|()
block|{
name|Striped
argument_list|<
name|Lock
argument_list|>
name|stripedLock
init|=
name|Striped
operator|.
name|lazyWeakLock
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
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
literal|10000
condition|;
name|i
operator|++
control|)
block|{
name|stripedLock
operator|.
name|get
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// nothing special (e.g. an exception) happens
block|}
block|}
DECL|method|testBulkGetReturnsSorted ()
specifier|public
name|void
name|testBulkGetReturnsSorted
parameter_list|()
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|allImplementations
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
name|indexByLock
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
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
name|striped
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|indexByLock
operator|.
name|put
argument_list|(
name|striped
operator|.
name|getAt
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|// ensure that bulkGet returns locks in monotonically increasing order
for|for
control|(
name|int
name|objectsNum
init|=
literal|1
init|;
name|objectsNum
operator|<=
name|striped
operator|.
name|size
argument_list|()
operator|*
literal|2
condition|;
name|objectsNum
operator|++
control|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|objects
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|objectsNum
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
name|objectsNum
condition|;
name|i
operator|++
control|)
block|{
name|objects
operator|.
name|add
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Iterable
argument_list|<
name|?
argument_list|>
name|locks
init|=
name|striped
operator|.
name|bulkGet
argument_list|(
name|objects
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|indexByLock
argument_list|)
argument_list|)
operator|.
name|isOrdered
argument_list|(
name|locks
argument_list|)
argument_list|)
expr_stmt|;
comment|// check idempotency
name|Iterable
argument_list|<
name|?
argument_list|>
name|locks2
init|=
name|striped
operator|.
name|bulkGet
argument_list|(
name|objects
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|locks
argument_list|)
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|locks2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Checks idempotency, and that we observe the promised number of stripes.    */
DECL|method|testBasicInvariants ()
specifier|public
name|void
name|testBasicInvariants
parameter_list|()
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|allImplementations
argument_list|()
control|)
block|{
name|assertBasicInvariants
argument_list|(
name|striped
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertBasicInvariants (Striped<?> striped)
specifier|private
specifier|static
name|void
name|assertBasicInvariants
parameter_list|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
parameter_list|)
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|observed
init|=
name|Sets
operator|.
name|newIdentityHashSet
argument_list|()
decl_stmt|;
comment|// for the sake of weakly referenced locks.
comment|// this gets the stripes with #getAt(index)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|striped
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|object
init|=
name|striped
operator|.
name|getAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|striped
operator|.
name|getAt
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
comment|// idempotent
name|observed
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"All stripes observed"
argument_list|,
name|observed
operator|.
name|size
argument_list|()
operator|==
name|striped
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// this uses #get(key), makes sure an already observed stripe is returned
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|striped
operator|.
name|size
argument_list|()
operator|*
literal|100
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|observed
operator|.
name|contains
argument_list|(
name|striped
operator|.
name|get
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|striped
operator|.
name|getAt
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|striped
operator|.
name|getAt
argument_list|(
name|striped
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testMaxSize ()
specifier|public
name|void
name|testMaxSize
parameter_list|()
block|{
for|for
control|(
name|Striped
argument_list|<
name|?
argument_list|>
name|striped
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|Striped
operator|.
name|lazyWeakLock
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|,
name|Striped
operator|.
name|lazyWeakSemaphore
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|,
name|Striped
operator|.
name|lazyWeakReadWriteLock
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
argument_list|)
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
comment|// doesn't throw exception
name|Object
name|unused
init|=
name|striped
operator|.
name|getAt
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|-
name|i
argument_list|)
decl_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

