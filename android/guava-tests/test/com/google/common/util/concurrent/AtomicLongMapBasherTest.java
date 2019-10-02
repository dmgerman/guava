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
name|Random
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
name|ExecutorService
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
name|Executors
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
name|Future
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
name|atomic
operator|.
name|AtomicLong
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
comment|/**  * Basher test for {@link AtomicLongMap}.  *  * @author mike nonemacher  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// threads
DECL|class|AtomicLongMapBasherTest
specifier|public
class|class
name|AtomicLongMapBasherTest
extends|extends
name|TestCase
block|{
DECL|field|random
specifier|private
specifier|final
name|Random
name|random
init|=
operator|new
name|Random
argument_list|(
literal|301
argument_list|)
decl_stmt|;
DECL|method|testModify_basher ()
specifier|public
name|void
name|testModify_basher
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|int
name|nTasks
init|=
literal|3000
decl_stmt|;
name|int
name|nThreads
init|=
literal|100
decl_stmt|;
specifier|final
name|int
name|getsPerTask
init|=
literal|1000
decl_stmt|;
specifier|final
name|int
name|deltaRange
init|=
literal|10000
decl_stmt|;
specifier|final
name|String
name|key
init|=
literal|"key"
decl_stmt|;
specifier|final
name|AtomicLong
name|sum
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
specifier|final
name|AtomicLongMap
argument_list|<
name|String
argument_list|>
name|map
init|=
name|AtomicLongMap
operator|.
name|create
argument_list|()
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|nThreads
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
name|nTasks
condition|;
name|i
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// go/futurereturn-lsc
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|threadPool
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|threadSum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|getsPerTask
condition|;
name|j
operator|++
control|)
block|{
name|long
name|delta
init|=
name|random
operator|.
name|nextInt
argument_list|(
name|deltaRange
argument_list|)
decl_stmt|;
name|int
name|behavior
init|=
name|random
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|behavior
condition|)
block|{
case|case
literal|0
case|:
name|map
operator|.
name|incrementAndGet
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|threadSum
operator|++
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|map
operator|.
name|decrementAndGet
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|threadSum
operator|--
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|map
operator|.
name|addAndGet
argument_list|(
name|key
argument_list|,
name|delta
argument_list|)
expr_stmt|;
name|threadSum
operator|+=
name|delta
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|map
operator|.
name|getAndIncrement
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|threadSum
operator|++
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|map
operator|.
name|getAndDecrement
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|threadSum
operator|--
expr_stmt|;
break|break;
case|case
literal|5
case|:
name|map
operator|.
name|getAndAdd
argument_list|(
name|key
argument_list|,
name|delta
argument_list|)
expr_stmt|;
name|threadSum
operator|+=
name|delta
expr_stmt|;
break|break;
case|case
literal|6
case|:
name|long
name|oldValue
init|=
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|delta
argument_list|)
decl_stmt|;
name|threadSum
operator|+=
name|delta
operator|-
name|oldValue
expr_stmt|;
break|break;
case|case
literal|7
case|:
name|oldValue
operator|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|replace
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|,
name|delta
argument_list|)
condition|)
block|{
name|threadSum
operator|+=
name|delta
operator|-
name|oldValue
expr_stmt|;
block|}
break|break;
case|case
literal|8
case|:
name|oldValue
operator|=
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|threadSum
operator|-=
name|oldValue
expr_stmt|;
break|break;
case|case
literal|9
case|:
name|oldValue
operator|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|,
name|oldValue
argument_list|)
condition|)
block|{
name|threadSum
operator|-=
name|oldValue
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
name|sum
operator|.
name|addAndGet
argument_list|(
name|threadSum
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
block|}
name|threadPool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|threadPool
operator|.
name|awaitTermination
argument_list|(
literal|300
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|sum
operator|.
name|get
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
