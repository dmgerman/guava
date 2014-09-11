begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|MoreObjects
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
name|Objects
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
name|Throwables
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
name|cache
operator|.
name|CacheBuilder
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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|HashMultimap
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
name|ImmutableSet
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
name|Iterators
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
name|reflect
operator|.
name|TypeToken
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
name|UncheckedExecutionException
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
name|Arrays
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Registry of subscribers to a single event bus.  *  * @author Colin Decker  */
end_comment

begin_class
DECL|class|SubscriberRegistry
specifier|final
class|class
name|SubscriberRegistry
block|{
comment|/**    * All registered subscribers, indexed by event type.    *    *<p>The {@link CopyOnWriteArraySet} values make it easy and relatively lightweight to get an    * immutable snapshot of all current subscribers to an event without any locking.    */
DECL|field|subscribers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
argument_list|>
name|subscribers
init|=
name|Maps
operator|.
name|newConcurrentMap
argument_list|()
decl_stmt|;
comment|/**    * The event bus this registry belongs to.    */
DECL|field|bus
specifier|private
specifier|final
name|EventBus
name|bus
decl_stmt|;
DECL|method|SubscriberRegistry (EventBus bus)
name|SubscriberRegistry
parameter_list|(
name|EventBus
name|bus
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|checkNotNull
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
comment|/**    * Registers all subscriber methods on the given listener object.    */
DECL|method|register (Object listener)
name|void
name|register
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Subscriber
argument_list|>
name|listenerMethods
init|=
name|findAllSubscribers
argument_list|(
name|listener
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Subscriber
argument_list|>
argument_list|>
name|entry
range|:
name|listenerMethods
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Subscriber
argument_list|>
name|eventMethodsInListener
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
name|eventSubscribers
init|=
name|subscribers
operator|.
name|get
argument_list|(
name|eventType
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventSubscribers
operator|==
literal|null
condition|)
block|{
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
name|newSet
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
argument_list|()
decl_stmt|;
name|eventSubscribers
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|subscribers
operator|.
name|putIfAbsent
argument_list|(
name|eventType
argument_list|,
name|newSet
argument_list|)
argument_list|,
name|newSet
argument_list|)
expr_stmt|;
block|}
name|eventSubscribers
operator|.
name|addAll
argument_list|(
name|eventMethodsInListener
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Unregisters all subscribers on the given listener object.    */
DECL|method|unregister (Object listener)
name|void
name|unregister
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Subscriber
argument_list|>
name|listenerMethods
init|=
name|findAllSubscribers
argument_list|(
name|listener
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Subscriber
argument_list|>
argument_list|>
name|entry
range|:
name|listenerMethods
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Subscriber
argument_list|>
name|listenerMethodsForType
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
name|currentSubscribers
init|=
name|subscribers
operator|.
name|get
argument_list|(
name|eventType
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentSubscribers
operator|==
literal|null
operator|||
operator|!
name|currentSubscribers
operator|.
name|removeAll
argument_list|(
name|listenerMethodsForType
argument_list|)
condition|)
block|{
comment|// if removeAll returns true, all we really know is that at least one subscriber was
comment|// removed... however, barring something very strange we can assume that if at least one
comment|// subscriber was removed, all subscribers on listener for that event type were... after
comment|// all, the definition of subscribers on a particular class is totally static
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"missing event subscriber for an annotated method. Is "
operator|+
name|listener
operator|+
literal|" registered?"
argument_list|)
throw|;
block|}
comment|// don't try to remove the set if it's empty; that can't be done safely without a lock
comment|// anyway, if the set is empty it'll just be wrapping an array of length 0
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|method|getSubscribersForTesting (Class<?> eventType)
name|Set
argument_list|<
name|Subscriber
argument_list|>
name|getSubscribersForTesting
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|subscribers
operator|.
name|get
argument_list|(
name|eventType
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
expr|<
name|Subscriber
operator|>
name|of
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Gets an iterator representing an immutable snapshot of all subscribers to the given event at    * the time this method is called.    */
DECL|method|getSubscribers (Object event)
name|Iterator
argument_list|<
name|Subscriber
argument_list|>
name|getSubscribers
parameter_list|(
name|Object
name|event
parameter_list|)
block|{
name|ImmutableSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|eventTypes
init|=
name|flattenHierarchy
argument_list|(
name|event
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Iterator
argument_list|<
name|Subscriber
argument_list|>
argument_list|>
name|subscriberIterators
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|eventTypes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
range|:
name|eventTypes
control|)
block|{
name|CopyOnWriteArraySet
argument_list|<
name|Subscriber
argument_list|>
name|eventSubscribers
init|=
name|subscribers
operator|.
name|get
argument_list|(
name|eventType
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventSubscribers
operator|!=
literal|null
condition|)
block|{
comment|// eager no-copy snapshot
name|subscriberIterators
operator|.
name|add
argument_list|(
name|eventSubscribers
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Iterators
operator|.
name|concat
argument_list|(
name|subscriberIterators
operator|.
name|iterator
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * A thread-safe cache that contains the mapping from each class to all methods in that class and    * all super-classes, that are annotated with {@code @Subscribe}. The cache is shared across all    * instances of this class; this greatly improves performance if multiple EventBus instances are    * created and objects of the same class are registered on all of them.    */
DECL|field|subscriberMethodsCache
specifier|private
specifier|static
specifier|final
name|LoadingCache
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ImmutableList
argument_list|<
name|Method
argument_list|>
argument_list|>
name|subscriberMethodsCache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|build
argument_list|(
operator|new
name|CacheLoader
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ImmutableList
argument_list|<
name|Method
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|load
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|concreteClass
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getAnnotatedMethodsNotCached
argument_list|(
name|concreteClass
argument_list|)
return|;
block|}
block|}
block|)
class|;
end_class

begin_comment
comment|/**    * Returns all subscribers for the given listener grouped by the type of event they subscribe to.    */
end_comment

begin_function
DECL|method|findAllSubscribers (Object listener)
specifier|private
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Subscriber
argument_list|>
name|findAllSubscribers
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Subscriber
argument_list|>
name|methodsInListener
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|listener
operator|.
name|getClass
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|getAnnotatedMethods
argument_list|(
name|clazz
argument_list|)
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
init|=
name|parameterTypes
index|[
literal|0
index|]
decl_stmt|;
name|methodsInListener
operator|.
name|put
argument_list|(
name|eventType
argument_list|,
name|Subscriber
operator|.
name|create
argument_list|(
name|bus
argument_list|,
name|listener
argument_list|,
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|methodsInListener
return|;
block|}
end_function

begin_function
DECL|method|getAnnotatedMethods (Class<?> clazz)
specifier|private
specifier|static
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|getAnnotatedMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|subscriberMethodsCache
operator|.
name|getUnchecked
argument_list|(
name|clazz
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|getAnnotatedMethodsNotCached (Class<?> clazz)
specifier|private
specifier|static
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|getAnnotatedMethodsNotCached
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|Set
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|supertypes
init|=
name|TypeToken
operator|.
name|of
argument_list|(
name|clazz
argument_list|)
operator|.
name|getTypes
argument_list|()
operator|.
name|rawTypes
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|MethodIdentifier
argument_list|,
name|Method
argument_list|>
name|identifiers
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|supertype
range|:
name|supertypes
control|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|supertype
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|method
operator|.
name|isAnnotationPresent
argument_list|(
name|Subscribe
operator|.
name|class
argument_list|)
operator|&&
operator|!
name|method
operator|.
name|isSynthetic
argument_list|()
condition|)
block|{
comment|// TODO(cgdecker): Should check for a generic parameter type and error out
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|parameterTypes
operator|.
name|length
operator|==
literal|1
argument_list|,
literal|"Method %s has @Subscribe annotation but has %s parameters."
operator|+
literal|"Subscriber methods must have exactly 1 parameter."
argument_list|,
name|method
argument_list|,
name|parameterTypes
operator|.
name|length
argument_list|)
expr_stmt|;
name|MethodIdentifier
name|ident
init|=
operator|new
name|MethodIdentifier
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|identifiers
operator|.
name|containsKey
argument_list|(
name|ident
argument_list|)
condition|)
block|{
name|identifiers
operator|.
name|put
argument_list|(
name|ident
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|identifiers
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**    * Global cache of classes to their flattened hierarchy of supertypes.    */
end_comment

begin_decl_stmt
DECL|field|flattenHierarchyCache
specifier|private
specifier|static
specifier|final
name|LoadingCache
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ImmutableSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|flattenHierarchyCache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|build
argument_list|(
operator|new
name|CacheLoader
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|ImmutableSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"RedundantTypeArguments"
argument_list|)
comment|//<Class<?>> is actually needed to compile
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|load
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|concreteClass
parameter_list|)
block|{
return|return
name|ImmutableSet
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|copyOf
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|concreteClass
argument_list|)
operator|.
name|getTypes
argument_list|()
operator|.
name|rawTypes
argument_list|()
argument_list|)
return|;
block|}
block|}
end_decl_stmt

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_comment
comment|/**    * Flattens a class's type hierarchy into a set of {@code Class} objects including all    * superclasses (transitively) and all interfaces implemented by these superclasses.    */
end_comment

begin_function
annotation|@
name|VisibleForTesting
DECL|method|flattenHierarchy (Class<?> concreteClass)
specifier|static
name|ImmutableSet
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
try|try
block|{
return|return
name|flattenHierarchyCache
operator|.
name|getUnchecked
argument_list|(
name|concreteClass
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UncheckedExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_function

begin_class
DECL|class|MethodIdentifier
specifier|private
specifier|static
specifier|final
class|class
name|MethodIdentifier
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|parameterTypes
specifier|private
specifier|final
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parameterTypes
decl_stmt|;
DECL|method|MethodIdentifier (Method method)
name|MethodIdentifier
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|method
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|parameterTypes
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|name
argument_list|,
name|parameterTypes
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|MethodIdentifier
condition|)
block|{
name|MethodIdentifier
name|ident
init|=
operator|(
name|MethodIdentifier
operator|)
name|o
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|ident
operator|.
name|name
argument_list|)
operator|&&
name|parameterTypes
operator|.
name|equals
argument_list|(
name|ident
operator|.
name|parameterTypes
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

unit|}
end_unit

