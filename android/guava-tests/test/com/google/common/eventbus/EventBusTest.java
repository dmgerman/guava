begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.eventbus
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
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
name|atomic
operator|.
name|AtomicInteger
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
comment|/**  * Test case for {@link EventBus}.  *  * @author Cliff Biffle  */
end_comment

begin_class
DECL|class|EventBusTest
specifier|public
class|class
name|EventBusTest
extends|extends
name|TestCase
block|{
DECL|field|EVENT
specifier|private
specifier|static
specifier|final
name|String
name|EVENT
init|=
literal|"Hello"
decl_stmt|;
DECL|field|BUS_IDENTIFIER
specifier|private
specifier|static
specifier|final
name|String
name|BUS_IDENTIFIER
init|=
literal|"test-bus"
decl_stmt|;
DECL|field|bus
specifier|private
name|EventBus
name|bus
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
name|bus
operator|=
operator|new
name|EventBus
argument_list|(
name|BUS_IDENTIFIER
argument_list|)
expr_stmt|;
block|}
DECL|method|testBasicCatcherDistribution ()
specifier|public
name|void
name|testBasicCatcherDistribution
parameter_list|()
block|{
name|StringCatcher
name|catcher
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|events
init|=
name|catcher
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only one event should be delivered."
argument_list|,
literal|1
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Correct string should be delivered."
argument_list|,
name|EVENT
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that events are distributed to any subscribers to their type or any supertype, including    * interfaces and superclasses.    *    *<p>Also checks delivery ordering in such cases.    */
DECL|method|testPolymorphicDistribution ()
specifier|public
name|void
name|testPolymorphicDistribution
parameter_list|()
block|{
comment|// Three catchers for related types String, Object, and Comparable<?>.
comment|// String isa Object
comment|// String isa Comparable<?>
comment|// Comparable<?> isa Object
name|StringCatcher
name|stringCatcher
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|objectEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Object
name|objCatcher
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Subscribe
specifier|public
name|void
name|eat
parameter_list|(
name|Object
name|food
parameter_list|)
block|{
name|objectEvents
operator|.
name|add
argument_list|(
name|food
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Comparable
argument_list|<
name|?
argument_list|>
argument_list|>
name|compEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Object
name|compCatcher
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Subscribe
specifier|public
name|void
name|eat
parameter_list|(
name|Comparable
argument_list|<
name|?
argument_list|>
name|food
parameter_list|)
block|{
name|compEvents
operator|.
name|add
argument_list|(
name|food
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|stringCatcher
argument_list|)
expr_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|objCatcher
argument_list|)
expr_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|compCatcher
argument_list|)
expr_stmt|;
comment|// Two additional event types: Object and Comparable<?> (played by Integer)
name|Object
name|objEvent
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Object
name|compEvent
init|=
operator|new
name|Integer
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|objEvent
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|compEvent
argument_list|)
expr_stmt|;
comment|// Check the StringCatcher...
name|List
argument_list|<
name|String
argument_list|>
name|stringEvents
init|=
name|stringCatcher
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Only one String should be delivered."
argument_list|,
literal|1
argument_list|,
name|stringEvents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Correct string should be delivered."
argument_list|,
name|EVENT
argument_list|,
name|stringEvents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check the Catcher<Object>...
name|assertEquals
argument_list|(
literal|"Three Objects should be delivered."
argument_list|,
literal|3
argument_list|,
name|objectEvents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String fixture must be first object delivered."
argument_list|,
name|EVENT
argument_list|,
name|objectEvents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Object fixture must be second object delivered."
argument_list|,
name|objEvent
argument_list|,
name|objectEvents
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Comparable fixture must be thirdobject delivered."
argument_list|,
name|compEvent
argument_list|,
name|objectEvents
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Check the Catcher<Comparable<?>>...
name|assertEquals
argument_list|(
literal|"Two Comparable<?>s should be delivered."
argument_list|,
literal|2
argument_list|,
name|compEvents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String fixture must be first comparable delivered."
argument_list|,
name|EVENT
argument_list|,
name|compEvents
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Comparable fixture must be second comparable delivered."
argument_list|,
name|compEvent
argument_list|,
name|compEvents
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubscriberThrowsException ()
specifier|public
name|void
name|testSubscriberThrowsException
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|RecordingSubscriberExceptionHandler
name|handler
init|=
operator|new
name|RecordingSubscriberExceptionHandler
argument_list|()
decl_stmt|;
specifier|final
name|EventBus
name|eventBus
init|=
operator|new
name|EventBus
argument_list|(
name|handler
argument_list|)
decl_stmt|;
specifier|final
name|RuntimeException
name|exception
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"but culottes have a tendancy to ride up!"
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|subscriber
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Subscribe
specifier|public
name|void
name|throwExceptionOn
parameter_list|(
name|String
name|message
parameter_list|)
block|{
throw|throw
name|exception
throw|;
block|}
block|}
decl_stmt|;
name|eventBus
operator|.
name|register
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|eventBus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Cause should be available."
argument_list|,
name|exception
argument_list|,
name|handler
operator|.
name|exception
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EventBus should be available."
argument_list|,
name|eventBus
argument_list|,
name|handler
operator|.
name|context
operator|.
name|getEventBus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Event should be available."
argument_list|,
name|EVENT
argument_list|,
name|handler
operator|.
name|context
operator|.
name|getEvent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Subscriber should be available."
argument_list|,
name|subscriber
argument_list|,
name|handler
operator|.
name|context
operator|.
name|getSubscriber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Method should be available."
argument_list|,
name|subscriber
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"throwExceptionOn"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|handler
operator|.
name|context
operator|.
name|getSubscriberMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubscriberThrowsExceptionHandlerThrowsException ()
specifier|public
name|void
name|testSubscriberThrowsExceptionHandlerThrowsException
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|EventBus
name|eventBus
init|=
operator|new
name|EventBus
argument_list|(
operator|new
name|SubscriberExceptionHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleException
parameter_list|(
name|Throwable
name|exception
parameter_list|,
name|SubscriberExceptionContext
name|context
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
argument_list|)
decl_stmt|;
specifier|final
name|Object
name|subscriber
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Subscribe
specifier|public
name|void
name|throwExceptionOn
parameter_list|(
name|String
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
name|eventBus
operator|.
name|register
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
try|try
block|{
name|eventBus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Exception should not be thrown."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testDeadEventForwarding ()
specifier|public
name|void
name|testDeadEventForwarding
parameter_list|()
block|{
name|GhostCatcher
name|catcher
init|=
operator|new
name|GhostCatcher
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
comment|// A String -- an event for which noone has registered.
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DeadEvent
argument_list|>
name|events
init|=
name|catcher
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"One dead event should be delivered."
argument_list|,
literal|1
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The dead event should wrap the original event."
argument_list|,
name|EVENT
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeadEventPosting ()
specifier|public
name|void
name|testDeadEventPosting
parameter_list|()
block|{
name|GhostCatcher
name|catcher
init|=
operator|new
name|GhostCatcher
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
operator|new
name|DeadEvent
argument_list|(
name|this
argument_list|,
name|EVENT
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DeadEvent
argument_list|>
name|events
init|=
name|catcher
operator|.
name|getEvents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The explicit DeadEvent should be delivered."
argument_list|,
literal|1
argument_list|,
name|events
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The dead event must not be re-wrapped."
argument_list|,
name|EVENT
argument_list|,
name|events
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testMissingSubscribe ()
specifier|public
name|void
name|testMissingSubscribe
parameter_list|()
block|{
name|bus
operator|.
name|register
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnregister ()
specifier|public
name|void
name|testUnregister
parameter_list|()
block|{
name|StringCatcher
name|catcher1
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
name|StringCatcher
name|catcher2
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
try|try
block|{
name|bus
operator|.
name|unregister
argument_list|(
name|catcher1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Attempting to unregister an unregistered object succeeded"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// OK.
block|}
name|bus
operator|.
name|register
argument_list|(
name|catcher1
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher2
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|expectedEvents
operator|.
name|add
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|expectedEvents
operator|.
name|add
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Two correct events should be delivered."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher1
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"One correct event should be delivered."
argument_list|,
name|Lists
operator|.
name|newArrayList
argument_list|(
name|EVENT
argument_list|)
argument_list|,
name|catcher2
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|unregister
argument_list|(
name|catcher1
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Shouldn't catch any more events when unregistered."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher1
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Two correct events should be delivered."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher2
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|bus
operator|.
name|unregister
argument_list|(
name|catcher1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Attempting to unregister an unregistered object succeeded"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// OK.
block|}
name|bus
operator|.
name|unregister
argument_list|(
name|catcher2
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Shouldn't catch any more events when unregistered."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher1
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Shouldn't catch any more events when unregistered."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher2
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// NOTE: This test will always pass if register() is thread-safe but may also
comment|// pass if it isn't, though this is unlikely.
DECL|method|testRegisterThreadSafety ()
specifier|public
name|void
name|testRegisterThreadSafety
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|StringCatcher
argument_list|>
name|catchers
init|=
name|Lists
operator|.
name|newCopyOnWriteArrayList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
name|int
name|numberOfCatchers
init|=
literal|10000
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
name|numberOfCatchers
condition|;
name|i
operator|++
control|)
block|{
name|futures
operator|.
name|add
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Registrator
argument_list|(
name|bus
argument_list|,
name|catchers
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfCatchers
condition|;
name|i
operator|++
control|)
block|{
name|futures
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Unexpected number of catchers in the list"
argument_list|,
name|numberOfCatchers
argument_list|,
name|catchers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedEvents
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|EVENT
argument_list|)
decl_stmt|;
for|for
control|(
name|StringCatcher
name|catcher
range|:
name|catchers
control|)
block|{
name|assertEquals
argument_list|(
literal|"One of the registered catchers did not receive an event."
argument_list|,
name|expectedEvents
argument_list|,
name|catcher
operator|.
name|getEvents
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
throws|throws
name|Exception
block|{
name|EventBus
name|eventBus
init|=
operator|new
name|EventBus
argument_list|(
literal|"a b ; - \"<> / \\ â¬"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"EventBus{a b ; - \"<> / \\ â¬}"
argument_list|,
name|eventBus
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that bridge methods are not subscribed to events. In Java 8, annotations are included on    * the bridge method in addition to the original method, which causes both the original and bridge    * methods to be subscribed (since both are annotated @Subscribe) without specifically checking    * for bridge methods.    */
DECL|method|testRegistrationWithBridgeMethod ()
specifier|public
name|void
name|testRegistrationWithBridgeMethod
parameter_list|()
block|{
specifier|final
name|AtomicInteger
name|calls
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
operator|new
name|Callback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Subscribe
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|calls
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|calls
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPrimitiveSubscribeFails ()
specifier|public
name|void
name|testPrimitiveSubscribeFails
parameter_list|()
block|{
class|class
name|SubscribesToPrimitive
block|{
annotation|@
name|Subscribe
specifier|public
name|void
name|toInt
parameter_list|(
name|int
name|i
parameter_list|)
block|{}
block|}
try|try
block|{
name|bus
operator|.
name|register
argument_list|(
operator|new
name|SubscribesToPrimitive
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"should have thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
comment|/** Records thrown exception information. */
DECL|class|RecordingSubscriberExceptionHandler
specifier|private
specifier|static
specifier|final
class|class
name|RecordingSubscriberExceptionHandler
implements|implements
name|SubscriberExceptionHandler
block|{
DECL|field|context
specifier|public
name|SubscriberExceptionContext
name|context
decl_stmt|;
DECL|field|exception
specifier|public
name|Throwable
name|exception
decl_stmt|;
annotation|@
name|Override
DECL|method|handleException (Throwable exception, SubscriberExceptionContext context)
specifier|public
name|void
name|handleException
parameter_list|(
name|Throwable
name|exception
parameter_list|,
name|SubscriberExceptionContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|exception
operator|=
name|exception
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
block|}
comment|/** Runnable which registers a StringCatcher on an event bus and adds it to a list. */
DECL|class|Registrator
specifier|private
specifier|static
class|class
name|Registrator
implements|implements
name|Runnable
block|{
DECL|field|bus
specifier|private
specifier|final
name|EventBus
name|bus
decl_stmt|;
DECL|field|catchers
specifier|private
specifier|final
name|List
argument_list|<
name|StringCatcher
argument_list|>
name|catchers
decl_stmt|;
DECL|method|Registrator (EventBus bus, List<StringCatcher> catchers)
name|Registrator
parameter_list|(
name|EventBus
name|bus
parameter_list|,
name|List
argument_list|<
name|StringCatcher
argument_list|>
name|catchers
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|bus
expr_stmt|;
name|this
operator|.
name|catchers
operator|=
name|catchers
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
name|StringCatcher
name|catcher
init|=
operator|new
name|StringCatcher
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
name|catchers
operator|.
name|add
argument_list|(
name|catcher
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * A collector for DeadEvents.    *    * @author cbiffle    */
DECL|class|GhostCatcher
specifier|public
specifier|static
class|class
name|GhostCatcher
block|{
DECL|field|events
specifier|private
name|List
argument_list|<
name|DeadEvent
argument_list|>
name|events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Subscribe
DECL|method|ohNoesIHaveDied (DeadEvent event)
specifier|public
name|void
name|ohNoesIHaveDied
parameter_list|(
name|DeadEvent
name|event
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|getEvents ()
specifier|public
name|List
argument_list|<
name|DeadEvent
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
block|}
DECL|interface|Callback
specifier|private
interface|interface
name|Callback
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|call (T t)
name|void
name|call
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

