begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|util
operator|.
name|concurrent
operator|.
name|Uninterruptibles
operator|.
name|getUninterruptibly
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
name|Optional
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
name|ImmutableCollection
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
name|Sets
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
name|ExecutionException
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
comment|/**  * Emulation of CollectionFuture.  */
end_comment

begin_class
DECL|class|CollectionFuture
specifier|public
class|class
name|CollectionFuture
parameter_list|<
name|V
parameter_list|,
name|C
parameter_list|>
extends|extends
name|AbstractFuture
operator|.
name|TrustedFuture
argument_list|<
name|C
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|CollectionFuture
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|futures
specifier|private
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|futures
decl_stmt|;
DECL|field|allMustSucceed
specifier|private
specifier|final
name|boolean
name|allMustSucceed
decl_stmt|;
DECL|field|remaining
specifier|private
specifier|final
name|AtomicInteger
name|remaining
decl_stmt|;
DECL|field|combiner
specifier|private
name|FutureCollector
argument_list|<
name|V
argument_list|,
name|C
argument_list|>
name|combiner
decl_stmt|;
DECL|field|values
specifier|private
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
decl_stmt|;
DECL|field|seenExceptions
specifier|private
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seenExceptions
decl_stmt|;
DECL|method|CollectionFuture ( ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed, Executor listenerExecutor, FutureCollector<V, C> combiner)
name|CollectionFuture
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|,
name|Executor
name|listenerExecutor
parameter_list|,
name|FutureCollector
argument_list|<
name|V
argument_list|,
name|C
argument_list|>
name|combiner
parameter_list|)
block|{
name|this
operator|.
name|futures
operator|=
name|futures
expr_stmt|;
name|this
operator|.
name|allMustSucceed
operator|=
name|allMustSucceed
expr_stmt|;
name|this
operator|.
name|remaining
operator|=
operator|new
name|AtomicInteger
argument_list|(
name|futures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|combiner
operator|=
name|combiner
expr_stmt|;
name|this
operator|.
name|values
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|futures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|seenExceptions
operator|=
literal|null
expr_stmt|;
comment|// Initialized once the first time we see an exception
name|init
argument_list|(
name|listenerExecutor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done ()
name|void
name|done
parameter_list|()
block|{
comment|// Let go of the memory held by other futures
name|this
operator|.
name|futures
operator|=
literal|null
expr_stmt|;
comment|// By now the values array has either been set as the Future's value,
comment|// or (in case of failure) is no longer useful.
name|this
operator|.
name|values
operator|=
literal|null
expr_stmt|;
comment|// The combiner may also hold state, so free that as well
name|this
operator|.
name|combiner
operator|=
literal|null
expr_stmt|;
block|}
comment|/**    * Must be called at the end of the constructor.    */
DECL|method|init (final Executor listenerExecutor)
specifier|protected
name|void
name|init
parameter_list|(
specifier|final
name|Executor
name|listenerExecutor
parameter_list|)
block|{
comment|// Now begin the "real" initialization.
comment|// Corner case: List is empty.
if|if
condition|(
name|futures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|set
argument_list|(
name|combiner
operator|.
name|combine
argument_list|(
name|ImmutableList
operator|.
expr|<
name|Optional
argument_list|<
name|V
argument_list|>
operator|>
name|of
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Populate the results list with null initially.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|futures
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|values
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// Register a listener on each Future in the list to update
comment|// the state of this future.
comment|// Note that if all the futures on the list are done prior to completing
comment|// this loop, the last call to addListener() will callback to
comment|// setOneValue(), transitively call our cleanup listener, and set
comment|// this.futures to null.
comment|// This is not actually a problem, since the foreach only needs
comment|// this.futures to be non-null at the beginning of the loop.
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|listenable
range|:
name|futures
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
operator|++
decl_stmt|;
name|listenable
operator|.
name|addListener
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
name|setOneValue
argument_list|(
name|index
argument_list|,
name|listenable
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|listenerExecutor
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
comment|// Cancel all the component futures.
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futuresToCancel
init|=
name|futures
decl_stmt|;
name|boolean
name|cancelled
init|=
name|super
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
decl_stmt|;
if|if
condition|(
name|cancelled
operator|&&
name|futuresToCancel
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
range|:
name|futuresToCancel
control|)
block|{
name|future
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cancelled
return|;
block|}
comment|/**    * Fails this future with the given Throwable if {@link #allMustSucceed} is true. Also, logs the    * throwable if it is an {@link Error} or if {@link #allMustSucceed} is {@code true}, the    * throwable did not cause this future to fail, and it is the first time we've seen that    * particular Throwable.    */
DECL|method|setExceptionAndMaybeLog (Throwable throwable)
specifier|private
name|void
name|setExceptionAndMaybeLog
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|boolean
name|visibleFromOutputFuture
init|=
literal|false
decl_stmt|;
name|boolean
name|firstTimeSeeingThisException
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|allMustSucceed
condition|)
block|{
comment|// As soon as the first one fails, throw the exception up.
comment|// The result of all other inputs is then ignored.
name|visibleFromOutputFuture
operator|=
name|super
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
comment|// seenExceptions is only set once; but we don't allocate it until we get a failure
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
comment|/*          * TODO(cpovirk): Find a way to share most code between GWT and non-GWT. The tricky line is          * this one:          */
name|seenExceptions
operator|=
name|Sets
operator|.
expr|<
name|Throwable
operator|>
name|newConcurrentHashSet
argument_list|()
expr_stmt|;
name|seenExceptionsLocal
operator|=
name|seenExceptions
expr_stmt|;
block|}
comment|// Go up the causal chain to see if we've already seen this cause; if we have,
comment|// even if it's wrapped by a different exception, don't log it.
name|Throwable
name|currentThrowable
init|=
name|throwable
decl_stmt|;
while|while
condition|(
name|currentThrowable
operator|!=
literal|null
condition|)
block|{
name|firstTimeSeeingThisException
operator|=
name|seenExceptionsLocal
operator|.
name|add
argument_list|(
name|currentThrowable
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|firstTimeSeeingThisException
condition|)
block|{
break|break;
block|}
name|currentThrowable
operator|=
name|currentThrowable
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|throwable
operator|instanceof
name|Error
operator|||
operator|(
name|allMustSucceed
operator|&&
operator|!
name|visibleFromOutputFuture
operator|&&
name|firstTimeSeeingThisException
operator|)
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"input future failed."
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Sets the value at the given index to that of the given future.    */
DECL|method|setOneValue (int index, Future<? extends V> future)
specifier|private
name|void
name|setOneValue
parameter_list|(
name|int
name|index
parameter_list|,
name|Future
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|future
parameter_list|)
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|localValues
init|=
name|values
decl_stmt|;
if|if
condition|(
name|isDone
argument_list|()
operator|||
name|localValues
operator|==
literal|null
condition|)
block|{
comment|// Some other future failed or has been cancelled, causing this one to
comment|// also be cancelled or have an exception set. This should only happen
comment|// if allMustSucceed is true or if the output itself has been
comment|// cancelled.
name|checkState
argument_list|(
name|allMustSucceed
operator|||
name|isCancelled
argument_list|()
argument_list|,
literal|"Future was done before all dependencies completed"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|checkState
argument_list|(
name|future
operator|.
name|isDone
argument_list|()
argument_list|,
literal|"Tried to set value from future which is not done"
argument_list|)
expr_stmt|;
if|if
condition|(
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
if|if
condition|(
name|allMustSucceed
condition|)
block|{
comment|// this.cancel propagates the cancellation to children; we use super.cancel
comment|// to set our own state but let the input futures keep running
comment|// as some of them may be used elsewhere.
name|super
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|V
name|returnValue
init|=
name|getUninterruptibly
argument_list|(
name|future
argument_list|)
decl_stmt|;
if|if
condition|(
name|localValues
operator|!=
literal|null
condition|)
block|{
name|localValues
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|Optional
operator|.
name|fromNullable
argument_list|(
name|returnValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|setExceptionAndMaybeLog
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|setExceptionAndMaybeLog
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|int
name|newRemaining
init|=
name|remaining
operator|.
name|decrementAndGet
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|newRemaining
operator|>=
literal|0
argument_list|,
literal|"Less than 0 remaining futures"
argument_list|)
expr_stmt|;
if|if
condition|(
name|newRemaining
operator|==
literal|0
condition|)
block|{
name|FutureCollector
argument_list|<
name|V
argument_list|,
name|C
argument_list|>
name|localCombiner
init|=
name|combiner
decl_stmt|;
if|if
condition|(
name|localCombiner
operator|!=
literal|null
operator|&&
name|localValues
operator|!=
literal|null
condition|)
block|{
name|set
argument_list|(
name|localCombiner
operator|.
name|combine
argument_list|(
name|localValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkState
argument_list|(
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|seenExceptions
operator|=
literal|null
expr_stmt|;
comment|// Done with tracking seen exceptions
block|}
block|}
block|}
DECL|interface|FutureCollector
interface|interface
name|FutureCollector
parameter_list|<
name|V
parameter_list|,
name|C
parameter_list|>
block|{
DECL|method|combine (List<Optional<V>> values)
name|C
name|combine
parameter_list|(
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

