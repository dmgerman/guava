begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|j2objc
operator|.
name|annotations
operator|.
name|Weak
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Executor
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * A subscriber method on a specific object, plus the executor that should be used for dispatching  * events to it.  *  *<p>Two subscribers are equivalent when they refer to the same method on the same object (not  * class). This property is used to ensure that no subscriber method is registered more than once.  *  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Subscriber
class|class
name|Subscriber
block|{
comment|/** Creates a {@code Subscriber} for {@code method} on {@code listener}. */
DECL|method|create (EventBus bus, Object listener, Method method)
specifier|static
name|Subscriber
name|create
parameter_list|(
name|EventBus
name|bus
parameter_list|,
name|Object
name|listener
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
return|return
name|isDeclaredThreadSafe
argument_list|(
name|method
argument_list|)
condition|?
operator|new
name|Subscriber
argument_list|(
name|bus
argument_list|,
name|listener
argument_list|,
name|method
argument_list|)
else|:
operator|new
name|SynchronizedSubscriber
argument_list|(
name|bus
argument_list|,
name|listener
argument_list|,
name|method
argument_list|)
return|;
block|}
comment|/** The event bus this subscriber belongs to. */
DECL|field|bus
annotation|@
name|Weak
specifier|private
name|EventBus
name|bus
decl_stmt|;
comment|/** The object with the subscriber method. */
DECL|field|target
annotation|@
name|VisibleForTesting
specifier|final
name|Object
name|target
decl_stmt|;
comment|/** Subscriber method. */
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
comment|/** Executor to use for dispatching events to this subscriber. */
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|method|Subscriber (EventBus bus, Object target, Method method)
specifier|private
name|Subscriber
parameter_list|(
name|EventBus
name|bus
parameter_list|,
name|Object
name|target
parameter_list|,
name|Method
name|method
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
name|target
operator|=
name|checkNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|bus
operator|.
name|executor
argument_list|()
expr_stmt|;
block|}
comment|/** Dispatches {@code event} to this subscriber using the proper executor. */
DECL|method|dispatchEvent (final Object event)
specifier|final
name|void
name|dispatchEvent
parameter_list|(
specifier|final
name|Object
name|event
parameter_list|)
block|{
name|executor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
name|invokeSubscriberMethod
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
name|bus
operator|.
name|handleSubscriberException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|context
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invokes the subscriber method. This method can be overridden to make the invocation    * synchronized.    */
annotation|@
name|VisibleForTesting
DECL|method|invokeSubscriberMethod (Object event)
name|void
name|invokeSubscriberMethod
parameter_list|(
name|Object
name|event
parameter_list|)
throws|throws
name|InvocationTargetException
block|{
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|checkNotNull
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
literal|"Method rejected target/argument: "
operator|+
name|event
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Error
argument_list|(
literal|"Method became inaccessible: "
operator|+
name|event
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|(
name|Error
operator|)
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
comment|/** Gets the context for the given event. */
DECL|method|context (Object event)
specifier|private
name|SubscriberExceptionContext
name|context
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
return|return
operator|new
name|SubscriberExceptionContext
argument_list|(
name|bus
argument_list|,
name|event
argument_list|,
name|target
argument_list|,
name|method
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|(
literal|31
operator|+
name|method
operator|.
name|hashCode
argument_list|()
operator|)
operator|*
literal|31
operator|+
name|System
operator|.
name|identityHashCode
argument_list|(
name|target
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Subscriber
condition|)
block|{
name|Subscriber
name|that
init|=
operator|(
name|Subscriber
operator|)
name|obj
decl_stmt|;
comment|// Use == so that different equal instances will still receive events.
comment|// We only guard against the case that the same object is registered
comment|// multiple times
return|return
name|target
operator|==
name|that
operator|.
name|target
operator|&&
name|method
operator|.
name|equals
argument_list|(
name|that
operator|.
name|method
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**    * Checks whether {@code method} is thread-safe, as indicated by the presence of the {@link    * AllowConcurrentEvents} annotation.    */
DECL|method|isDeclaredThreadSafe (Method method)
specifier|private
specifier|static
name|boolean
name|isDeclaredThreadSafe
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|method
operator|.
name|getAnnotation
argument_list|(
name|AllowConcurrentEvents
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**    * Subscriber that synchronizes invocations of a method to ensure that only one thread may enter    * the method at a time.    */
annotation|@
name|VisibleForTesting
DECL|class|SynchronizedSubscriber
specifier|static
specifier|final
class|class
name|SynchronizedSubscriber
extends|extends
name|Subscriber
block|{
DECL|method|SynchronizedSubscriber (EventBus bus, Object target, Method method)
specifier|private
name|SynchronizedSubscriber
parameter_list|(
name|EventBus
name|bus
parameter_list|,
name|Object
name|target
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|target
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invokeSubscriberMethod (Object event)
name|void
name|invokeSubscriberMethod
parameter_list|(
name|Object
name|event
parameter_list|)
throws|throws
name|InvocationTargetException
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|super
operator|.
name|invokeSubscriberMethod
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

