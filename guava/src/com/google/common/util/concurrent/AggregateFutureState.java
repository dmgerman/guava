begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Sets
operator|.
name|newConcurrentHashSet
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicIntegerFieldUpdater
operator|.
name|newUpdater
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReferenceFieldUpdater
operator|.
name|newUpdater
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
name|GwtCompatible
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
name|ReflectionSupport
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
name|atomic
operator|.
name|AtomicIntegerFieldUpdater
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
name|AtomicReferenceFieldUpdater
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
comment|/**  * A helper which does some thread-safe operations for aggregate futures, which must be implemented  * differently in GWT. Namely:  *<ul>  *<li>Lazily initializes a set of seen exceptions  *<li>Decrements a counter atomically  *</ul>  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ReflectionSupport
argument_list|(
name|value
operator|=
name|ReflectionSupport
operator|.
name|Level
operator|.
name|FULL
argument_list|)
DECL|class|AggregateFutureState
specifier|abstract
class|class
name|AggregateFutureState
block|{
comment|// Lazily initialized the first time we see an exception; not released until all the input futures
comment|//& this future completes. Released when the future releases the reference to the running state
DECL|field|seenExceptions
specifier|private
specifier|volatile
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seenExceptions
init|=
literal|null
decl_stmt|;
DECL|field|remaining
specifier|private
specifier|volatile
name|int
name|remaining
decl_stmt|;
DECL|field|ATOMIC_HELPER
specifier|private
specifier|static
specifier|final
name|AtomicHelper
name|ATOMIC_HELPER
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|AggregateFutureState
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
static|static
block|{
name|AtomicHelper
name|helper
decl_stmt|;
name|Throwable
name|thrownReflectionFailure
init|=
literal|null
decl_stmt|;
try|try
block|{
name|helper
operator|=
operator|new
name|SafeAtomicHelper
argument_list|(
name|newUpdater
argument_list|(
name|AggregateFutureState
operator|.
name|class
argument_list|,
operator|(
name|Class
operator|)
name|Set
operator|.
name|class
argument_list|,
literal|"seenExceptions"
argument_list|)
argument_list|,
name|newUpdater
argument_list|(
name|AggregateFutureState
operator|.
name|class
argument_list|,
literal|"remaining"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|reflectionFailure
parameter_list|)
block|{
comment|// Some Android 5.0.x Samsung devices have bugs in JDK reflection APIs that cause
comment|// getDeclaredField to throw a NoSuchFieldException when the field is definitely there.
comment|// For these users fallback to a suboptimal implementation, based on synchronized. This will
comment|// be a definite performance hit to those users.
name|thrownReflectionFailure
operator|=
name|reflectionFailure
expr_stmt|;
name|helper
operator|=
operator|new
name|SynchronizedAtomicHelper
argument_list|()
expr_stmt|;
block|}
name|ATOMIC_HELPER
operator|=
name|helper
expr_stmt|;
comment|// Log after all static init is finished; if an installed logger uses any Futures methods, it
comment|// shouldn't break in cases where reflection is missing/broken.
if|if
condition|(
name|thrownReflectionFailure
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"SafeAtomicHelper is broken!"
argument_list|,
name|thrownReflectionFailure
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|AggregateFutureState (int remainingFutures)
name|AggregateFutureState
parameter_list|(
name|int
name|remainingFutures
parameter_list|)
block|{
name|this
operator|.
name|remaining
operator|=
name|remainingFutures
expr_stmt|;
block|}
DECL|method|getOrInitSeenExceptions ()
specifier|final
name|Set
argument_list|<
name|Throwable
argument_list|>
name|getOrInitSeenExceptions
parameter_list|()
block|{
comment|/*      * The initialization of seenExceptions has to be more complicated than we'd like. The simple      * approach would be for each caller CAS it from null to a Set populated with its exception. But      * there's another race: If the first thread fails with an exception and a second thread      * immediately fails with the same exception:      *      * Thread1: calls setException(), which returns true, context switch before it can CAS      * seenExceptions to its exception      *      * Thread2: calls setException(), which returns false, CASes seenExceptions to its exception,      * and wrongly believes that its exception is new (leading it to logging it when it shouldn't)      *      * Our solution is for threads to CAS seenExceptions from null to a Set population with _the      * initial exception_, no matter which thread does the work. This ensures that seenExceptions      * always contains not just the current thread's exception but also the initial thread's.      */
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seenExceptionsLocal
init|=
name|seenExceptions
decl_stmt|;
if|if
condition|(
name|seenExceptionsLocal
operator|==
literal|null
condition|)
block|{
name|seenExceptionsLocal
operator|=
name|newConcurrentHashSet
argument_list|()
expr_stmt|;
comment|/*        * Other handleException() callers may see this as soon as we publish it. We need to populate        * it with the initial failure before we do, or else they may think that the initial failure        * has never been seen before.        */
name|addInitialException
argument_list|(
name|seenExceptionsLocal
argument_list|)
expr_stmt|;
name|ATOMIC_HELPER
operator|.
name|compareAndSetSeenExceptions
argument_list|(
name|this
argument_list|,
literal|null
argument_list|,
name|seenExceptionsLocal
argument_list|)
expr_stmt|;
comment|/*        * If another handleException() caller created the set, we need to use that copy in case yet        * other callers have added to it.        *        * This read is guaranteed to get us the right value because we only set this once (here).        */
name|seenExceptionsLocal
operator|=
name|seenExceptions
expr_stmt|;
block|}
return|return
name|seenExceptionsLocal
return|;
block|}
comment|/** Populates {@code seen} with the exception that was passed to {@code setException}. */
DECL|method|addInitialException (Set<Throwable> seen)
specifier|abstract
name|void
name|addInitialException
parameter_list|(
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seen
parameter_list|)
function_decl|;
DECL|method|decrementRemainingAndGet ()
specifier|final
name|int
name|decrementRemainingAndGet
parameter_list|()
block|{
return|return
name|ATOMIC_HELPER
operator|.
name|decrementAndGetRemainingCount
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|class|AtomicHelper
specifier|private
specifier|abstract
specifier|static
class|class
name|AtomicHelper
block|{
comment|/** Atomic compare-and-set of the {@link AggregateFutureState#seenExceptions} field. */
DECL|method|compareAndSetSeenExceptions ( AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
specifier|abstract
name|void
name|compareAndSetSeenExceptions
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|expect
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|update
parameter_list|)
function_decl|;
comment|/** Atomic decrement-and-get of the {@link AggregateFutureState#remaining} field. */
DECL|method|decrementAndGetRemainingCount (AggregateFutureState state)
specifier|abstract
name|int
name|decrementAndGetRemainingCount
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|)
function_decl|;
block|}
DECL|class|SafeAtomicHelper
specifier|private
specifier|static
specifier|final
class|class
name|SafeAtomicHelper
extends|extends
name|AtomicHelper
block|{
DECL|field|seenExceptionsUpdater
specifier|final
name|AtomicReferenceFieldUpdater
argument_list|<
name|AggregateFutureState
argument_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
argument_list|>
name|seenExceptionsUpdater
decl_stmt|;
DECL|field|remainingCountUpdater
specifier|final
name|AtomicIntegerFieldUpdater
argument_list|<
name|AggregateFutureState
argument_list|>
name|remainingCountUpdater
decl_stmt|;
DECL|method|SafeAtomicHelper ( AtomicReferenceFieldUpdater seenExceptionsUpdater, AtomicIntegerFieldUpdater remainingCountUpdater)
name|SafeAtomicHelper
parameter_list|(
name|AtomicReferenceFieldUpdater
name|seenExceptionsUpdater
parameter_list|,
name|AtomicIntegerFieldUpdater
name|remainingCountUpdater
parameter_list|)
block|{
name|this
operator|.
name|seenExceptionsUpdater
operator|=
name|seenExceptionsUpdater
expr_stmt|;
name|this
operator|.
name|remainingCountUpdater
operator|=
name|remainingCountUpdater
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compareAndSetSeenExceptions ( AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
name|void
name|compareAndSetSeenExceptions
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|expect
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|update
parameter_list|)
block|{
name|seenExceptionsUpdater
operator|.
name|compareAndSet
argument_list|(
name|state
argument_list|,
name|expect
argument_list|,
name|update
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|decrementAndGetRemainingCount (AggregateFutureState state)
name|int
name|decrementAndGetRemainingCount
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|)
block|{
return|return
name|remainingCountUpdater
operator|.
name|decrementAndGet
argument_list|(
name|state
argument_list|)
return|;
block|}
block|}
DECL|class|SynchronizedAtomicHelper
specifier|private
specifier|static
specifier|final
class|class
name|SynchronizedAtomicHelper
extends|extends
name|AtomicHelper
block|{
annotation|@
name|Override
DECL|method|compareAndSetSeenExceptions ( AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update)
name|void
name|compareAndSetSeenExceptions
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|expect
parameter_list|,
name|Set
argument_list|<
name|Throwable
argument_list|>
name|update
parameter_list|)
block|{
synchronized|synchronized
init|(
name|state
init|)
block|{
if|if
condition|(
name|state
operator|.
name|seenExceptions
operator|==
name|expect
condition|)
block|{
name|state
operator|.
name|seenExceptions
operator|=
name|update
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|decrementAndGetRemainingCount (AggregateFutureState state)
name|int
name|decrementAndGetRemainingCount
parameter_list|(
name|AggregateFutureState
name|state
parameter_list|)
block|{
synchronized|synchronized
init|(
name|state
init|)
block|{
name|state
operator|.
name|remaining
operator|--
expr_stmt|;
return|return
name|state
operator|.
name|remaining
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

