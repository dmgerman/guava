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
name|caliper
operator|.
name|Param
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Runner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|SimpleBenchmark
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

begin_comment
comment|/**  * Benchmarks for {@link CycleDetectingLockFactory}.  *  * @author Darick Tong (darick@google.com)  */
end_comment

begin_class
DECL|class|CycleDetectingLockFactoryBenchmark
specifier|public
class|class
name|CycleDetectingLockFactoryBenchmark
extends|extends
name|SimpleBenchmark
block|{
DECL|field|lockNestingDepth
annotation|@
name|Param
argument_list|(
block|{
literal|"2"
block|,
literal|"3"
block|,
literal|"4"
block|,
literal|"5"
block|,
literal|"10"
block|}
argument_list|)
name|int
name|lockNestingDepth
decl_stmt|;
DECL|field|factory
name|CycleDetectingLockFactory
name|factory
decl_stmt|;
DECL|field|plainLocks
specifier|private
name|Lock
index|[]
name|plainLocks
decl_stmt|;
DECL|field|detectingLocks
specifier|private
name|Lock
index|[]
name|detectingLocks
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
name|this
operator|.
name|factory
operator|=
name|CycleDetectingLockFactory
operator|.
name|newInstance
argument_list|(
name|CycleDetectingLockFactory
operator|.
name|Policies
operator|.
name|WARN
argument_list|)
expr_stmt|;
name|this
operator|.
name|plainLocks
operator|=
operator|new
name|Lock
index|[
name|lockNestingDepth
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
name|lockNestingDepth
condition|;
name|i
operator|++
control|)
block|{
name|plainLocks
index|[
name|i
index|]
operator|=
operator|new
name|ReentrantLock
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|detectingLocks
operator|=
operator|new
name|Lock
index|[
name|lockNestingDepth
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
name|lockNestingDepth
condition|;
name|i
operator|++
control|)
block|{
name|detectingLocks
index|[
name|i
index|]
operator|=
name|factory
operator|.
name|newReentrantLock
argument_list|(
literal|"Lock"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|timeUnorderedPlainLocks (int reps)
specifier|public
name|void
name|timeUnorderedPlainLocks
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|lockAndUnlock
argument_list|(
operator|new
name|ReentrantLock
argument_list|()
argument_list|,
name|reps
argument_list|)
expr_stmt|;
block|}
DECL|method|timeUnorderedCycleDetectingLocks (int reps)
specifier|public
name|void
name|timeUnorderedCycleDetectingLocks
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|lockAndUnlock
argument_list|(
name|factory
operator|.
name|newReentrantLock
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|reps
argument_list|)
expr_stmt|;
block|}
DECL|method|lockAndUnlock (Lock lock, int reps)
specifier|private
name|void
name|lockAndUnlock
parameter_list|(
name|Lock
name|lock
parameter_list|,
name|int
name|reps
parameter_list|)
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
name|reps
condition|;
name|i
operator|++
control|)
block|{
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|timeOrderedPlainLocks (int reps)
specifier|public
name|void
name|timeOrderedPlainLocks
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|lockAndUnlockNested
argument_list|(
name|plainLocks
argument_list|,
name|reps
argument_list|)
expr_stmt|;
block|}
DECL|method|timeOrderedCycleDetectingLocks (int reps)
specifier|public
name|void
name|timeOrderedCycleDetectingLocks
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|lockAndUnlockNested
argument_list|(
name|detectingLocks
argument_list|,
name|reps
argument_list|)
expr_stmt|;
block|}
DECL|method|lockAndUnlockNested (Lock[] locks, int reps)
specifier|private
name|void
name|lockAndUnlockNested
parameter_list|(
name|Lock
index|[]
name|locks
parameter_list|,
name|int
name|reps
parameter_list|)
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
name|reps
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
name|locks
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|locks
index|[
name|j
index|]
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|j
init|=
name|locks
operator|.
name|length
operator|-
literal|1
init|;
name|j
operator|>=
literal|0
condition|;
name|j
operator|--
control|)
block|{
name|locks
index|[
name|j
index|]
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Runner
operator|.
name|main
argument_list|(
name|CycleDetectingLockFactoryBenchmark
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

