begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|directExecutor
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
name|ConcurrentHashMultiset
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
name|ImmutableMap
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
name|ImmutableMultiset
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
name|Multiset
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
name|TestLogHandler
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
name|Map
operator|.
name|Entry
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
comment|/** Tests for {@link ListenerCallQueue}. */
end_comment

begin_class
DECL|class|ListenerCallQueueTest
specifier|public
class|class
name|ListenerCallQueueTest
extends|extends
name|TestCase
block|{
DECL|field|THROWING_EVENT
specifier|private
specifier|static
specifier|final
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
name|THROWING_EVENT
init|=
operator|new
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"throwing()"
return|;
block|}
block|}
decl_stmt|;
DECL|method|testEnqueueAndDispatch ()
specifier|public
name|void
name|testEnqueueAndDispatch
parameter_list|()
block|{
name|Object
name|listener
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|(
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|,
name|counters
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnqueueAndDispatch_multipleListeners ()
specifier|public
name|void
name|testEnqueueAndDispatch_multipleListeners
parameter_list|()
block|{
name|Object
name|listener1
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|addListener
argument_list|(
name|listener1
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|listener2
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|queue
operator|.
name|addListener
argument_list|(
name|listener2
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|multiset
argument_list|(
name|listener1
argument_list|,
literal|3
argument_list|,
name|listener2
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|multiset
argument_list|(
name|listener1
argument_list|,
literal|4
argument_list|,
name|listener2
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|(
name|listener1
argument_list|,
literal|4
argument_list|,
name|listener2
argument_list|,
literal|2
argument_list|)
argument_list|,
name|counters
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnqueueAndDispatch_withExceptions ()
specifier|public
name|void
name|testEnqueueAndDispatch_withExceptions
parameter_list|()
block|{
name|Object
name|listener
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|(
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|,
name|counters
argument_list|)
expr_stmt|;
block|}
DECL|class|MyListener
specifier|static
specifier|final
class|class
name|MyListener
block|{
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MyListener"
return|;
block|}
block|}
DECL|method|testEnqueueAndDispatch_withLabeledExceptions ()
specifier|public
name|void
name|testEnqueueAndDispatch_withLabeledExceptions
parameter_list|()
block|{
name|Object
name|listener
init|=
operator|new
name|MyListener
argument_list|()
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
name|queue
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|,
literal|"custom-label"
argument_list|)
expr_stmt|;
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|ListenerCallQueue
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|logger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|)
expr_stmt|;
name|TestLogHandler
name|logHandler
init|=
operator|new
name|TestLogHandler
argument_list|()
decl_stmt|;
name|logger
operator|.
name|addHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|logger
operator|.
name|removeHandler
argument_list|(
name|logHandler
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Exception while executing callback: MyListener custom-label"
argument_list|,
name|logHandler
operator|.
name|getStoredLogRecords
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEnqueueAndDispatch_multithreaded ()
specifier|public
name|void
name|testEnqueueAndDispatch_multithreaded
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Object
name|listener
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|queue
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|countDownEvent
argument_list|(
name|latch
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|(
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|,
name|counters
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testEnqueueAndDispatch_multithreaded_withThrowingRunnable ()
specifier|public
name|void
name|testEnqueueAndDispatch_multithreaded_withThrowingRunnable
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Object
name|listener
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ExecutorService
name|service
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|ListenerCallQueue
argument_list|<
name|Object
argument_list|>
name|queue
init|=
operator|new
name|ListenerCallQueue
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|queue
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|service
argument_list|)
expr_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
init|=
name|ConcurrentHashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|THROWING_EVENT
argument_list|)
expr_stmt|;
name|queue
operator|.
name|enqueue
argument_list|(
name|countDownEvent
argument_list|(
name|latch
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|counters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|dispatch
argument_list|()
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|multiset
argument_list|(
name|listener
argument_list|,
literal|4
argument_list|)
argument_list|,
name|counters
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|incrementingEvent ( Multiset<Object> counters, Object expectedListener, int expectedCount)
specifier|private
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
name|incrementingEvent
parameter_list|(
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
parameter_list|,
name|Object
name|expectedListener
parameter_list|,
name|int
name|expectedCount
parameter_list|)
block|{
return|return
name|incrementingEvent
argument_list|(
name|counters
argument_list|,
name|multiset
argument_list|(
name|expectedListener
argument_list|,
name|expectedCount
argument_list|)
argument_list|)
return|;
block|}
DECL|method|incrementingEvent ( final Multiset<Object> counters, final Multiset<Object> expected)
specifier|private
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
name|incrementingEvent
parameter_list|(
specifier|final
name|Multiset
argument_list|<
name|Object
argument_list|>
name|counters
parameter_list|,
specifier|final
name|Multiset
argument_list|<
name|Object
argument_list|>
name|expected
parameter_list|)
block|{
return|return
operator|new
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|counters
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
operator|.
name|count
argument_list|(
name|listener
argument_list|)
argument_list|,
name|counters
operator|.
name|count
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"incrementing"
return|;
block|}
block|}
return|;
block|}
DECL|method|multiset (T value, int count)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|T
argument_list|>
name|multiset
parameter_list|(
name|T
name|value
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|multiset
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|value
argument_list|,
name|count
argument_list|)
argument_list|)
return|;
block|}
DECL|method|multiset (T value1, int count1, T value2, int count2)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|T
argument_list|>
name|multiset
parameter_list|(
name|T
name|value1
parameter_list|,
name|int
name|count1
parameter_list|,
name|T
name|value2
parameter_list|,
name|int
name|count2
parameter_list|)
block|{
return|return
name|multiset
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
name|value1
argument_list|,
name|count1
argument_list|,
name|value2
argument_list|,
name|count2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|multiset (Map<T, Integer> counts)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ImmutableMultiset
argument_list|<
name|T
argument_list|>
name|multiset
parameter_list|(
name|Map
argument_list|<
name|T
argument_list|,
name|Integer
argument_list|>
name|counts
parameter_list|)
block|{
name|ImmutableMultiset
operator|.
name|Builder
argument_list|<
name|T
argument_list|>
name|builder
init|=
name|ImmutableMultiset
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|T
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|counts
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|builder
operator|.
name|addCopies
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|countDownEvent (final CountDownLatch latch)
specifier|private
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
name|countDownEvent
parameter_list|(
specifier|final
name|CountDownLatch
name|latch
parameter_list|)
block|{
return|return
operator|new
name|ListenerCallQueue
operator|.
name|Event
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"countDown"
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

