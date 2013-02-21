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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
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
name|util
operator|.
name|concurrent
operator|.
name|CycleDetectingLockFactory
operator|.
name|Policies
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
name|util
operator|.
name|concurrent
operator|.
name|CycleDetectingLockFactory
operator|.
name|Policy
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
name|util
operator|.
name|concurrent
operator|.
name|CycleDetectingLockFactory
operator|.
name|PotentialDeadlockException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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
name|TimeUnit
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Unittests for {@link CycleDetectingLockFactory}.  *  * @author Darick Tong  */
end_comment

begin_class
DECL|class|CycleDetectingLockFactoryTest
specifier|public
class|class
name|CycleDetectingLockFactoryTest
extends|extends
name|TestCase
block|{
DECL|field|lockA
specifier|private
name|ReentrantLock
name|lockA
decl_stmt|;
DECL|field|lockB
specifier|private
name|ReentrantLock
name|lockB
decl_stmt|;
DECL|field|lockC
specifier|private
name|ReentrantLock
name|lockC
decl_stmt|;
DECL|field|readLockA
specifier|private
name|ReentrantReadWriteLock
operator|.
name|ReadLock
name|readLockA
decl_stmt|;
DECL|field|readLockB
specifier|private
name|ReentrantReadWriteLock
operator|.
name|ReadLock
name|readLockB
decl_stmt|;
DECL|field|readLockC
specifier|private
name|ReentrantReadWriteLock
operator|.
name|ReadLock
name|readLockC
decl_stmt|;
DECL|field|writeLockA
specifier|private
name|ReentrantReadWriteLock
operator|.
name|WriteLock
name|writeLockA
decl_stmt|;
DECL|field|writeLockB
specifier|private
name|ReentrantReadWriteLock
operator|.
name|WriteLock
name|writeLockB
decl_stmt|;
DECL|field|writeLockC
specifier|private
name|ReentrantReadWriteLock
operator|.
name|WriteLock
name|writeLockC
decl_stmt|;
DECL|field|lock1
specifier|private
name|ReentrantLock
name|lock1
decl_stmt|;
DECL|field|lock2
specifier|private
name|ReentrantLock
name|lock2
decl_stmt|;
DECL|field|lock3
specifier|private
name|ReentrantLock
name|lock3
decl_stmt|;
DECL|field|lock01
specifier|private
name|ReentrantLock
name|lock01
decl_stmt|;
DECL|field|lock02
specifier|private
name|ReentrantLock
name|lock02
decl_stmt|;
DECL|field|lock03
specifier|private
name|ReentrantLock
name|lock03
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|CycleDetectingLockFactory
name|factory
init|=
name|CycleDetectingLockFactory
operator|.
name|newInstance
argument_list|(
name|Policies
operator|.
name|THROW
argument_list|)
decl_stmt|;
name|lockA
operator|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
literal|"LockA"
argument_list|)
expr_stmt|;
name|lockB
operator|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
literal|"LockB"
argument_list|)
expr_stmt|;
name|lockC
operator|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
literal|"LockC"
argument_list|)
expr_stmt|;
name|ReentrantReadWriteLock
name|readWriteLockA
init|=
name|factory
operator|.
name|newReentrantReadWriteLock
argument_list|(
literal|"ReadWriteA"
argument_list|)
decl_stmt|;
name|ReentrantReadWriteLock
name|readWriteLockB
init|=
name|factory
operator|.
name|newReentrantReadWriteLock
argument_list|(
literal|"ReadWriteB"
argument_list|)
decl_stmt|;
name|ReentrantReadWriteLock
name|readWriteLockC
init|=
name|factory
operator|.
name|newReentrantReadWriteLock
argument_list|(
literal|"ReadWriteC"
argument_list|)
decl_stmt|;
name|readLockA
operator|=
name|readWriteLockA
operator|.
name|readLock
argument_list|()
expr_stmt|;
name|readLockB
operator|=
name|readWriteLockB
operator|.
name|readLock
argument_list|()
expr_stmt|;
name|readLockC
operator|=
name|readWriteLockC
operator|.
name|readLock
argument_list|()
expr_stmt|;
name|writeLockA
operator|=
name|readWriteLockA
operator|.
name|writeLock
argument_list|()
expr_stmt|;
name|writeLockB
operator|=
name|readWriteLockB
operator|.
name|writeLock
argument_list|()
expr_stmt|;
name|writeLockC
operator|=
name|readWriteLockC
operator|.
name|writeLock
argument_list|()
expr_stmt|;
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|MyOrder
argument_list|>
name|factory2
init|=
name|newInstanceWithExplicitOrdering
argument_list|(
name|MyOrder
operator|.
name|class
argument_list|,
name|Policies
operator|.
name|THROW
argument_list|)
decl_stmt|;
name|lock1
operator|=
name|factory2
operator|.
name|newReentrantLock
argument_list|(
name|MyOrder
operator|.
name|FIRST
argument_list|)
expr_stmt|;
name|lock2
operator|=
name|factory2
operator|.
name|newReentrantLock
argument_list|(
name|MyOrder
operator|.
name|SECOND
argument_list|)
expr_stmt|;
name|lock3
operator|=
name|factory2
operator|.
name|newReentrantLock
argument_list|(
name|MyOrder
operator|.
name|THIRD
argument_list|)
expr_stmt|;
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|OtherOrder
argument_list|>
name|factory3
init|=
name|newInstanceWithExplicitOrdering
argument_list|(
name|OtherOrder
operator|.
name|class
argument_list|,
name|Policies
operator|.
name|THROW
argument_list|)
decl_stmt|;
name|lock01
operator|=
name|factory3
operator|.
name|newReentrantLock
argument_list|(
name|OtherOrder
operator|.
name|FIRST
argument_list|)
expr_stmt|;
name|lock02
operator|=
name|factory3
operator|.
name|newReentrantLock
argument_list|(
name|OtherOrder
operator|.
name|SECOND
argument_list|)
expr_stmt|;
name|lock03
operator|=
name|factory3
operator|.
name|newReentrantLock
argument_list|(
name|OtherOrder
operator|.
name|THIRD
argument_list|)
expr_stmt|;
block|}
comment|// In the unittest, create each ordered factory with its own set of lock
comment|// graph nodes (as opposed to using the static per-Enum map) to avoid
comment|// conflicts across different test runs.
specifier|private
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|E
argument_list|>
DECL|method|newInstanceWithExplicitOrdering (Class<E> enumClass, Policy policy)
name|newInstanceWithExplicitOrdering
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|enumClass
parameter_list|,
name|Policy
name|policy
parameter_list|)
block|{
return|return
operator|new
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|E
argument_list|>
argument_list|(
name|policy
argument_list|,
name|CycleDetectingLockFactory
operator|.
name|createNodes
argument_list|(
name|enumClass
argument_list|)
argument_list|)
return|;
block|}
DECL|method|testDeadlock_twoLocks ()
specifier|public
name|void
name|testDeadlock_twoLocks
parameter_list|()
block|{
comment|// Establish an acquisition order of lockA -> lockB.
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// The opposite order should fail (Policies.THROW).
name|PotentialDeadlockException
name|firstException
init|=
literal|null
decl_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> LockA"
argument_list|,
literal|"LockA -> LockB"
argument_list|)
expr_stmt|;
name|firstException
operator|=
name|expected
expr_stmt|;
block|}
comment|// Second time should also fail, with a cached causal chain.
try|try
block|{
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> LockA"
argument_list|,
literal|"LockA -> LockB"
argument_list|)
expr_stmt|;
comment|// The causal chain should be cached.
name|assertSame
argument_list|(
name|firstException
operator|.
name|getCause
argument_list|()
argument_list|,
name|expected
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// lockA should work after lockB is released.
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
comment|// Tests transitive deadlock detection.
DECL|method|testDeadlock_threeLocks ()
specifier|public
name|void
name|testDeadlock_threeLocks
parameter_list|()
block|{
comment|// Establish an ordering from lockA -> lockB.
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from lockB -> lockC.
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockC
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockC -> lockA should fail.
try|try
block|{
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockC -> LockA"
argument_list|,
literal|"LockB -> LockC"
argument_list|,
literal|"LockA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReentrancy_noDeadlock ()
specifier|public
name|void
name|testReentrancy_noDeadlock
parameter_list|()
block|{
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// Should not assert on lockB -> reentrant(lockA)
block|}
DECL|method|testExplicitOrdering_noViolations ()
specifier|public
name|void
name|testExplicitOrdering_noViolations
parameter_list|()
block|{
name|lock1
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock3
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lock2
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
DECL|method|testExplicitOrdering_violations ()
specifier|public
name|void
name|testExplicitOrdering_violations
parameter_list|()
block|{
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|lock2
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"MyOrder.THIRD -> MyOrder.SECOND"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|lock1
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"MyOrder.THIRD -> MyOrder.FIRST"
argument_list|)
expr_stmt|;
block|}
name|lock3
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lock2
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|lock1
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"MyOrder.SECOND -> MyOrder.FIRST"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDifferentOrderings_noViolations ()
specifier|public
name|void
name|testDifferentOrderings_noViolations
parameter_list|()
block|{
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// MyOrder, ordinal() == 3
name|lock01
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// OtherOrder, ordinal() == 1
block|}
DECL|method|testExplicitOrderings_generalCycleDetection ()
specifier|public
name|void
name|testExplicitOrderings_generalCycleDetection
parameter_list|()
block|{
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// MyOrder, ordinal() == 3
name|lock01
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// OtherOrder, ordinal() == 1
name|lock3
operator|.
name|unlock
argument_list|()
expr_stmt|;
try|try
block|{
name|lock3
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"OtherOrder.FIRST -> MyOrder.THIRD"
argument_list|,
literal|"MyOrder.THIRD -> OtherOrder.FIRST"
argument_list|)
expr_stmt|;
block|}
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock01
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
try|try
block|{
name|lock01
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> OtherOrder.FIRST"
argument_list|,
literal|"LockA -> LockB"
argument_list|,
literal|"OtherOrder.FIRST -> LockA"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testExplicitOrdering_cycleWithUnorderedLock ()
specifier|public
name|void
name|testExplicitOrdering_cycleWithUnorderedLock
parameter_list|()
block|{
name|Lock
name|myLock
init|=
name|CycleDetectingLockFactory
operator|.
name|newInstance
argument_list|(
name|Policies
operator|.
name|THROW
argument_list|)
operator|.
name|newReentrantLock
argument_list|(
literal|"MyLock"
argument_list|)
decl_stmt|;
name|lock03
operator|.
name|lock
argument_list|()
expr_stmt|;
name|myLock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock03
operator|.
name|unlock
argument_list|()
expr_stmt|;
try|try
block|{
name|lock01
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"MyLock -> OtherOrder.FIRST"
argument_list|,
literal|"OtherOrder.THIRD -> MyLock"
argument_list|,
literal|"OtherOrder.FIRST -> OtherOrder.THIRD"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testExplicitOrdering_reentrantAcquisition ()
specifier|public
name|void
name|testExplicitOrdering_reentrantAcquisition
parameter_list|()
block|{
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|OtherOrder
argument_list|>
name|factory
init|=
name|newInstanceWithExplicitOrdering
argument_list|(
name|OtherOrder
operator|.
name|class
argument_list|,
name|Policies
operator|.
name|THROW
argument_list|)
decl_stmt|;
name|Lock
name|lockA
init|=
name|factory
operator|.
name|newReentrantReadWriteLock
argument_list|(
name|OtherOrder
operator|.
name|FIRST
argument_list|)
operator|.
name|readLock
argument_list|()
decl_stmt|;
name|Lock
name|lockB
init|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
name|OtherOrder
operator|.
name|SECOND
argument_list|)
decl_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
DECL|method|testExplicitOrdering_acquiringMultipleLocksWithSameRank ()
specifier|public
name|void
name|testExplicitOrdering_acquiringMultipleLocksWithSameRank
parameter_list|()
block|{
name|CycleDetectingLockFactory
operator|.
name|WithExplicitOrdering
argument_list|<
name|OtherOrder
argument_list|>
name|factory
init|=
name|newInstanceWithExplicitOrdering
argument_list|(
name|OtherOrder
operator|.
name|class
argument_list|,
name|Policies
operator|.
name|THROW
argument_list|)
decl_stmt|;
name|Lock
name|lockA
init|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
name|OtherOrder
operator|.
name|FIRST
argument_list|)
decl_stmt|;
name|Lock
name|lockB
init|=
name|factory
operator|.
name|newReentrantReadWriteLock
argument_list|(
name|OtherOrder
operator|.
name|FIRST
argument_list|)
operator|.
name|readLock
argument_list|()
decl_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
DECL|method|testReadLock_deadlock ()
specifier|public
name|void
name|testReadLock_deadlock
parameter_list|()
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from readLockA -> lockB.
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> ReadWriteA"
argument_list|,
literal|"ReadWriteA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadLock_transitive ()
specifier|public
name|void
name|testReadLock_transitive
parameter_list|()
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from readLockA -> lockB.
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from lockB -> readLockC.
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|readLockC
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|readLockC
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// readLockC -> readLockA
name|readLockC
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"ReadWriteC -> ReadWriteA"
argument_list|,
literal|"LockB -> ReadWriteC"
argument_list|,
literal|"ReadWriteA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testWriteLock_threeLockDeadLock ()
specifier|public
name|void
name|testWriteLock_threeLockDeadLock
parameter_list|()
block|{
comment|// Establish an ordering from writeLockA -> writeLockB.
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|writeLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from writeLockB -> writeLockC.
name|writeLockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockC
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// writeLockC -> writeLockA should fail.
try|try
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"ReadWriteC -> ReadWriteA"
argument_list|,
literal|"ReadWriteB -> ReadWriteC"
argument_list|,
literal|"ReadWriteA -> ReadWriteB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testWriteToReadLockDowngrading ()
specifier|public
name|void
name|testWriteToReadLockDowngrading
parameter_list|()
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// writeLockA downgrades to readLockA
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// readLockA -> lockB
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockB -> writeLockA should fail
try|try
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> ReadWriteA"
argument_list|,
literal|"ReadWriteA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadWriteLockDeadlock ()
specifier|public
name|void
name|testReadWriteLockDeadlock
parameter_list|()
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from writeLockA -> lockB
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|writeLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockB -> readLockA should fail.
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockB -> ReadWriteA"
argument_list|,
literal|"ReadWriteA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadWriteLockDeadlock_transitive ()
specifier|public
name|void
name|testReadWriteLockDeadlock_transitive
parameter_list|()
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from readLockA -> lockB
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// Establish an ordering from lockB -> lockC
name|lockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockC
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockC
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockC -> writeLockA should fail.
name|lockC
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockC -> ReadWriteA"
argument_list|,
literal|"LockB -> LockC"
argument_list|,
literal|"ReadWriteA -> LockB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReadWriteLockDeadlock_treatedEquivalently ()
specifier|public
name|void
name|testReadWriteLockDeadlock_treatedEquivalently
parameter_list|()
block|{
name|readLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
comment|// readLockA -> writeLockB
name|writeLockB
operator|.
name|lock
argument_list|()
expr_stmt|;
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|writeLockB
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// readLockB -> writeLockA should fail.
name|readLockB
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|writeLockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"ReadWriteB -> ReadWriteA"
argument_list|,
literal|"ReadWriteA -> ReadWriteB"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDifferentLockFactories ()
specifier|public
name|void
name|testDifferentLockFactories
parameter_list|()
block|{
name|CycleDetectingLockFactory
name|otherFactory
init|=
name|CycleDetectingLockFactory
operator|.
name|newInstance
argument_list|(
name|Policies
operator|.
name|WARN
argument_list|)
decl_stmt|;
name|ReentrantLock
name|lockD
init|=
name|otherFactory
operator|.
name|newReentrantLock
argument_list|(
literal|"LockD"
argument_list|)
decl_stmt|;
comment|// lockA -> lockD
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockD
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockD
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockD -> lockA should fail even though lockD is from a different factory.
name|lockD
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected PotentialDeadlockException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PotentialDeadlockException
name|expected
parameter_list|)
block|{
name|checkMessage
argument_list|(
name|expected
argument_list|,
literal|"LockD -> LockA"
argument_list|,
literal|"LockA -> LockD"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDifferentLockFactories_policyExecution ()
specifier|public
name|void
name|testDifferentLockFactories_policyExecution
parameter_list|()
block|{
name|CycleDetectingLockFactory
name|otherFactory
init|=
name|CycleDetectingLockFactory
operator|.
name|newInstance
argument_list|(
name|Policies
operator|.
name|WARN
argument_list|)
decl_stmt|;
name|ReentrantLock
name|lockD
init|=
name|otherFactory
operator|.
name|newReentrantLock
argument_list|(
literal|"LockD"
argument_list|)
decl_stmt|;
comment|// lockD -> lockA
name|lockD
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|lockD
operator|.
name|unlock
argument_list|()
expr_stmt|;
comment|// lockA -> lockD should warn but otherwise succeed because lockD was
comment|// created by a factory with the WARN policy.
name|lockA
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lockD
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
DECL|method|testReentrantLock_tryLock ()
specifier|public
name|void
name|testReentrantLock_tryLock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockingThread
name|thread
init|=
operator|new
name|LockingThread
argument_list|(
name|lockA
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|thread
operator|.
name|waitUntilHoldingLock
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|lockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|thread
operator|.
name|releaseLockAndFinish
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|lockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReentrantWriteLock_tryLock ()
specifier|public
name|void
name|testReentrantWriteLock_tryLock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockingThread
name|thread
init|=
operator|new
name|LockingThread
argument_list|(
name|writeLockA
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|thread
operator|.
name|waitUntilHoldingLock
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|writeLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|readLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|thread
operator|.
name|releaseLockAndFinish
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|writeLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|readLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReentrantReadLock_tryLock ()
specifier|public
name|void
name|testReentrantReadLock_tryLock
parameter_list|()
throws|throws
name|Exception
block|{
name|LockingThread
name|thread
init|=
operator|new
name|LockingThread
argument_list|(
name|readLockA
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|thread
operator|.
name|waitUntilHoldingLock
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|writeLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|readLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|readLockA
operator|.
name|unlock
argument_list|()
expr_stmt|;
name|thread
operator|.
name|releaseLockAndFinish
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|writeLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|readLockA
operator|.
name|tryLock
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|LockingThread
specifier|private
specifier|static
class|class
name|LockingThread
extends|extends
name|Thread
block|{
DECL|field|locked
specifier|final
name|CountDownLatch
name|locked
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|finishLatch
specifier|final
name|CountDownLatch
name|finishLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|lock
specifier|final
name|Lock
name|lock
decl_stmt|;
DECL|method|LockingThread (Lock lock)
name|LockingThread
parameter_list|(
name|Lock
name|lock
parameter_list|)
block|{
name|this
operator|.
name|lock
operator|=
name|lock
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|locked
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|finishLatch
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|waitUntilHoldingLock ()
name|void
name|waitUntilHoldingLock
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|locked
operator|.
name|await
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
block|}
DECL|method|releaseLockAndFinish ()
name|void
name|releaseLockAndFinish
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|finishLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|this
operator|.
name|join
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|this
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testReentrantReadWriteLock_implDoesNotExposeShadowedLocks ()
specifier|public
name|void
name|testReentrantReadWriteLock_implDoesNotExposeShadowedLocks
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Unexpected number of public methods in ReentrantReadWriteLock. "
operator|+
literal|"The correctness of CycleDetectingReentrantReadWriteLock depends on "
operator|+
literal|"the fact that the shadowed ReadLock and WriteLock are never used or "
operator|+
literal|"exposed by the superclass implementation. If the implementation has "
operator|+
literal|"changed, the code must be re-inspected to ensure that the "
operator|+
literal|"assumption is still valid."
argument_list|,
literal|24
argument_list|,
name|ReentrantReadWriteLock
operator|.
name|class
operator|.
name|getMethods
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|enum|MyOrder
specifier|private
enum|enum
name|MyOrder
block|{
DECL|enumConstant|FIRST
DECL|enumConstant|SECOND
DECL|enumConstant|THIRD
name|FIRST
block|,
name|SECOND
block|,
name|THIRD
block|;   }
DECL|enum|OtherOrder
specifier|private
enum|enum
name|OtherOrder
block|{
DECL|enumConstant|FIRST
DECL|enumConstant|SECOND
DECL|enumConstant|THIRD
name|FIRST
block|,
name|SECOND
block|,
name|THIRD
block|;   }
comment|// Given a sequence of lock acquisition descriptions
comment|// (e.g. "LockA -> LockB", "LockB -> LockC", ...)
comment|// Checks that the exception.getMessage() matches a regex of the form:
comment|// "LockA -> LockB \b.*\b LockB -> LockC \b.*\b LockC -> LockA"
DECL|method|checkMessage ( IllegalStateException exception, String... expectedLockCycle)
specifier|private
name|void
name|checkMessage
parameter_list|(
name|IllegalStateException
name|exception
parameter_list|,
name|String
modifier|...
name|expectedLockCycle
parameter_list|)
block|{
name|String
name|regex
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|"\\b.*\\b"
argument_list|)
operator|.
name|join
argument_list|(
name|expectedLockCycle
argument_list|)
decl_stmt|;
name|assertContainsRegex
argument_list|(
name|regex
argument_list|,
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO(cpovirk): consider adding support for regex to Truth
DECL|method|assertContainsRegex (String expectedRegex, String actual)
specifier|private
specifier|static
name|void
name|assertContainsRegex
parameter_list|(
name|String
name|expectedRegex
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|expectedRegex
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|actual
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|actualDesc
init|=
operator|(
name|actual
operator|==
literal|null
operator|)
condition|?
literal|"null"
else|:
operator|(
literal|'<'
operator|+
name|actual
operator|+
literal|'>'
operator|)
decl_stmt|;
name|fail
argument_list|(
literal|"expected to contain regex:<"
operator|+
name|expectedRegex
operator|+
literal|"> but was:"
operator|+
name|actualDesc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

