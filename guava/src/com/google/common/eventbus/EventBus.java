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
name|Multimap
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|List
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
name|ConcurrentLinkedQueue
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
name|CopyOnWriteArraySet
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

begin_comment
comment|/**  * Dispatches events to listeners, and provides ways for listeners to register  * themselves.  *  *<p>The EventBus allows publish-subscribe-style communication between  * components without requiring the components to explicitly register with one  * another (and thus be aware of each other).  It is designed exclusively to  * replace traditional Java in-process event distribution using explicit  * registration. It is<em>not</em> a general-purpose publish-subscribe system,  * nor is it intended for interprocess communication.  *  *<h2>Receiving Events</h2>  * To receive events, an object should:<ol>  *<li>Expose a public method, known as the<i>event handler</i>, which accepts  *     a single argument of the type of event desired;</li>  *<li>Mark it with a {@link Subscribe} annotation;</li>  *<li>Pass itself to an EventBus instance's {@link #register(Object)} method.  *</li>  *</ol>  *  *<h2>Posting Events</h2>  * To post an event, simply provide the event object to the  * {@link #post(Object)} method.  The EventBus instance will determine the type  * of event and route it to all registered listeners.  *  *<p>Events are routed based on their type&mdash; an event will be delivered  * to any handler for any type to which the event is<em>assignable.</em>  This  * includes implemented interfaces, all superclasses, and all interfaces  * implemented by superclasses.  *  *<p>When {@code post} is called, all registered handlers for an event are run  * in sequence, so handlers should be reasonably quick.  If an event may trigger  * an extended process (such as a database load), spawn a thread or queue it for  * later.  (For a convenient way to do this, use an {@link AsyncEventBus}.)  *  *<h2>Handler Methods</h2>  * Event handler methods must accept only one argument: the event.  *  *<p>Handlers should not, in general, throw.  If they do, the EventBus will  * catch and log the exception.  This is rarely the right solution for error  * handling and should not be relied upon; it is intended solely to help find  * problems during development.  *  *<p>The EventBus guarantees that it will not call a handler method from  * multiple threads simultaneously, unless the method explicitly allows it by  * bearing the {@link AllowConcurrentEvents} annotation.  If this annotation is  * not present, handler methods need not worry about being reentrant, unless  * also called from outside the EventBus.  *  *<h2>Dead Events</h2>  * If an event is posted, but no registered handlers can accept it, it is  * considered "dead."  To give the system a second chance to handle dead events,  * they are wrapped in an instance of {@link DeadEvent} and reposted.  *  *<p>If a handler for a supertype of all events (such as Object) is registered,  * no event will ever be considered dead, and no DeadEvents will be generated.  * Accordingly, while DeadEvent extends {@link Object}, a handler registered to  * receive any Object will never receive a DeadEvent.  *  *<p>This class is safe for concurrent use.  *  * @author Cliff Biffle  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|EventBus
specifier|public
class|class
name|EventBus
block|{
comment|/**    * All registered event handlers, indexed by event type.    *    *<p>This is a concurrent map of sets, a structure that can't currently be    * built using {@link Multimap}.  Too bad, really.    */
DECL|field|handlersByType
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Set
argument_list|<
name|EventHandler
argument_list|>
argument_list|>
name|handlersByType
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Set
argument_list|<
name|EventHandler
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**    * Logger for event dispatch failures.  Named by the fully-qualified name of    * this class, followed by the identifier provided at construction.    */
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
decl_stmt|;
comment|/**    * Strategy for finding handler methods in registered objects.  Currently,    * only the {@link AnnotatedHandlerFinder} is supported, but this is    * encapsulated for future expansion.    */
DECL|field|finder
specifier|private
specifier|final
name|HandlerFindingStrategy
name|finder
init|=
operator|new
name|AnnotatedHandlerFinder
argument_list|()
decl_stmt|;
comment|/** queues of events for the current thread to dispatch */
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
argument_list|>
DECL|field|eventsToDispatch
name|eventsToDispatch
init|=
operator|new
name|ThreadLocal
argument_list|<
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
name|initialValue
parameter_list|()
block|{
return|return
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|EventWithHandler
argument_list|>
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/** true if the current thread is currently dispatching an event */
DECL|field|isDispatching
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
name|isDispatching
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Boolean
name|initialValue
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Creates a new EventBus named "default".    */
DECL|method|EventBus ()
specifier|public
name|EventBus
parameter_list|()
block|{
name|this
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new EventBus with the given {@code identifier}.    *    * @param identifier  a brief name for this bus, for logging purposes.  Should    *                    be a valid Java identifier.    */
DECL|method|EventBus (String identifier)
specifier|public
name|EventBus
parameter_list|(
name|String
name|identifier
parameter_list|)
block|{
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|EventBus
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|identifier
argument_list|)
expr_stmt|;
block|}
comment|/**    * Registers all handler methods on {@code object} to receive events.    * Handler methods are selected and classified using this EventBus's    * {@link HandlerFindingStrategy}; the default strategy is the    * {@link AnnotatedHandlerFinder}.    *    * @param object  object whose handler methods should be registered.    * @throws IllegalArgumentException if no public subscribe methods are found on object    */
DECL|method|register (Object object)
specifier|public
name|void
name|register
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|EventHandler
argument_list|>
name|methodsInListener
init|=
name|finder
operator|.
name|findAllHandlers
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodsInListener
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no subscribe method found on: "
operator|+
name|object
argument_list|)
throw|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
range|:
name|methodsInListener
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|currentHandlers
init|=
name|getOrCreateHandlersForEventType
argument_list|(
name|eventClass
argument_list|)
decl_stmt|;
name|currentHandlers
operator|.
name|addAll
argument_list|(
name|methodsInListener
operator|.
name|get
argument_list|(
name|eventClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Unregisters all handler methods on a registered {@code object}.    *    * @param object  object whose handler methods should be unregistered.    * @throws IllegalArgumentException if no public subscribe methods are found on object    *                                  or the object was not previously registered.    */
DECL|method|unregister (Object object)
specifier|public
name|void
name|unregister
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|EventHandler
argument_list|>
name|methodsInListener
init|=
name|finder
operator|.
name|findAllHandlers
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodsInListener
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no subscribe method found on: "
operator|+
name|object
argument_list|)
throw|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventClass
range|:
name|methodsInListener
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|currentHandlers
init|=
name|getHandlersForEventType
argument_list|(
name|eventClass
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|EventHandler
argument_list|>
name|eventMethodsInListener
init|=
name|methodsInListener
operator|.
name|get
argument_list|(
name|eventClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentHandlers
operator|==
literal|null
operator|||
operator|!
name|currentHandlers
operator|.
name|containsAll
argument_list|(
name|eventMethodsInListener
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"missing event handler for an annotated method. Is "
operator|+
name|object
operator|+
literal|" registered?"
argument_list|)
throw|;
block|}
name|currentHandlers
operator|.
name|removeAll
argument_list|(
name|eventMethodsInListener
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Posts an event to all registered handlers.  This method will return    * successfully after the event has been posted to all handlers, and    * regardless of any exceptions thrown by handlers.    *    *<p>If no handlers have been subscribed for {@code event}'s class, and    * {@code event} is not already a {@link DeadEvent}, it will be wrapped in a    * DeadEvent and reposted.    *    * @param event  event to post.    */
DECL|method|post (Object event)
specifier|public
name|void
name|post
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|dispatchTypes
init|=
name|flattenHierarchy
argument_list|(
name|event
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|dispatched
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
range|:
name|dispatchTypes
control|)
block|{
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|wrappers
init|=
name|getHandlersForEventType
argument_list|(
name|eventType
argument_list|)
decl_stmt|;
if|if
condition|(
name|wrappers
operator|!=
literal|null
operator|&&
operator|!
name|wrappers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|dispatched
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|EventHandler
name|wrapper
range|:
name|wrappers
control|)
block|{
name|enqueueEvent
argument_list|(
name|event
argument_list|,
name|wrapper
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|dispatched
operator|&&
operator|!
operator|(
name|event
operator|instanceof
name|DeadEvent
operator|)
condition|)
block|{
name|post
argument_list|(
operator|new
name|DeadEvent
argument_list|(
name|this
argument_list|,
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dispatchQueuedEvents
argument_list|()
expr_stmt|;
block|}
comment|/**    * Queue the {@code event} for dispatch during    * {@link #dispatchQueuedEvents()}. Events are queued in-order of occurrence    * so they can be dispatched in the same order.    */
DECL|method|enqueueEvent (Object event, EventHandler handler)
specifier|protected
name|void
name|enqueueEvent
parameter_list|(
name|Object
name|event
parameter_list|,
name|EventHandler
name|handler
parameter_list|)
block|{
name|eventsToDispatch
operator|.
name|get
argument_list|()
operator|.
name|offer
argument_list|(
operator|new
name|EventWithHandler
argument_list|(
name|event
argument_list|,
name|handler
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Drain the queue of events to be dispatched. As the queue is being drained,    * new events may be posted to the end of the queue.    */
DECL|method|dispatchQueuedEvents ()
specifier|protected
name|void
name|dispatchQueuedEvents
parameter_list|()
block|{
comment|// don't dispatch if we're already dispatching, that would allow reentrancy
comment|// and out-of-order events. Instead, leave the events to be dispatched
comment|// after the in-progress dispatch is complete.
if|if
condition|(
name|isDispatching
operator|.
name|get
argument_list|()
condition|)
block|{
return|return;
block|}
name|isDispatching
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|EventWithHandler
name|eventWithHandler
init|=
name|eventsToDispatch
operator|.
name|get
argument_list|()
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventWithHandler
operator|==
literal|null
condition|)
block|{
break|break;
block|}
name|dispatch
argument_list|(
name|eventWithHandler
operator|.
name|event
argument_list|,
name|eventWithHandler
operator|.
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|isDispatching
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Dispatches {@code event} to the handler in {@code wrapper}.  This method    * is an appropriate override point for subclasses that wish to make    * event delivery asynchronous.    *    * @param event  event to dispatch.    * @param wrapper  wrapper that will call the handler.    */
DECL|method|dispatch (Object event, EventHandler wrapper)
specifier|protected
name|void
name|dispatch
parameter_list|(
name|Object
name|event
parameter_list|,
name|EventHandler
name|wrapper
parameter_list|)
block|{
try|try
block|{
name|wrapper
operator|.
name|handleEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Could not dispatch event: "
operator|+
name|event
operator|+
literal|" to handler "
operator|+
name|wrapper
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Retrieves a mutable set containing all EventHandlers for {@code type},    * creating one if necessary.    *    * @param type  event type whose handlers are desired.    * @return a set of all handlers for {@code type}.    */
DECL|method|getOrCreateHandlersForEventType (Class<?> type)
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|getOrCreateHandlersForEventType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|handlers
init|=
name|getHandlersForEventType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlers
operator|==
literal|null
condition|)
block|{
name|handlersByType
operator|.
name|putIfAbsent
argument_list|(
name|type
argument_list|,
name|newHandlerSet
argument_list|()
argument_list|)
expr_stmt|;
name|handlers
operator|=
name|getHandlersForEventType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|handlers
return|;
block|}
comment|/**    * Retrieves a mutable set of the currently registered handlers for    * {@code type}.  If no handlers are currently registered for {@code type},    * this method may either return {@code null} or an empty set.    *    * @param type  type of handlers to retrieve.    * @return currently registered handlers, or {@code null}.    */
DECL|method|getHandlersForEventType (Class<?> type)
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|getHandlersForEventType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|handlersByType
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**    * Creates a new Set for insertion into the handler map.  This is provided    * as an override point for subclasses. The returned set should support    * concurrent access.    *    * @return a new, mutable set for handlers.    */
DECL|method|newHandlerSet ()
specifier|protected
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|newHandlerSet
parameter_list|()
block|{
return|return
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|EventHandler
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Flattens a class's type hierarchy into a set of Class objects.  The set    * will include all superclasses (transitively), and all interfaces    * implemented by these superclasses.    *    * @param concreteClass  class whose type hierarchy will be retrieved.    * @return {@code clazz}'s complete type hierarchy, flattened and uniqued.    */
annotation|@
name|VisibleForTesting
DECL|method|flattenHierarchy (Class<?> concreteClass)
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|flattenHierarchy
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|concreteClass
parameter_list|)
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parents
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|parents
operator|.
name|add
argument_list|(
name|concreteClass
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|parents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|parents
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|parent
init|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parents
operator|.
name|add
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|iface
range|:
name|clazz
operator|.
name|getInterfaces
argument_list|()
control|)
block|{
name|parents
operator|.
name|add
argument_list|(
name|iface
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|classes
return|;
block|}
comment|/** simple struct representing an event and it's handler */
DECL|class|EventWithHandler
specifier|static
class|class
name|EventWithHandler
block|{
DECL|field|event
specifier|final
name|Object
name|event
decl_stmt|;
DECL|field|handler
specifier|final
name|EventHandler
name|handler
decl_stmt|;
DECL|method|EventWithHandler (Object event, EventHandler handler)
specifier|public
name|EventWithHandler
parameter_list|(
name|Object
name|event
parameter_list|,
name|EventHandler
name|handler
parameter_list|)
block|{
name|this
operator|.
name|event
operator|=
name|event
expr_stmt|;
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

