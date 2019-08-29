begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkNotNull
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
name|AggregateFuture
operator|.
name|ReleaseResourcesReason
operator|.
name|ALL_INPUT_FUTURES_PROCESSED
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
name|AggregateFuture
operator|.
name|ReleaseResourcesReason
operator|.
name|OUTPUT_FUTURE_DONE
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
name|Futures
operator|.
name|getDone
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
name|MoreExecutors
operator|.
name|directExecutor
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|SEVERE
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
name|errorprone
operator|.
name|annotations
operator|.
name|ForOverride
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|OverridingMethodsMustInvokeSuper
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
name|Future
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A future whose value is derived from a collection of input futures.  *  * @param<InputT> the type of the individual inputs  * @param<OutputT> the type of the output (i.e. this) future  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AggregateFuture
specifier|abstract
class|class
name|AggregateFuture
parameter_list|<
name|InputT
parameter_list|,
name|OutputT
parameter_list|>
extends|extends
name|AggregateFutureState
argument_list|<
name|OutputT
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
name|AggregateFuture
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * The input futures. After {@link #init}, this field is read only by {@link #afterDone()} (to    * propagate cancellation) and {@link #toString()}. To access the futures'<i>values</i>, {@code    * AggregateFuture} attaches listeners that hold references to one or more inputs. And in the case    * of {@link CombinedFuture}, the user-supplied callback usually has its own references to inputs.    */
comment|/*    * In certain circumstances, this field might theoretically not be visible to an afterDone() call    * triggered by cancel(). For details, see the comments on the fields of TimeoutFuture.    */
DECL|field|futures
specifier|private
annotation|@
name|Nullable
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|InputT
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
DECL|field|collectsValues
specifier|private
specifier|final
name|boolean
name|collectsValues
decl_stmt|;
DECL|method|AggregateFuture ( ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues)
name|AggregateFuture
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|,
name|boolean
name|collectsValues
parameter_list|)
block|{
name|super
argument_list|(
name|futures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|futures
operator|=
name|checkNotNull
argument_list|(
name|futures
argument_list|)
expr_stmt|;
name|this
operator|.
name|allMustSucceed
operator|=
name|allMustSucceed
expr_stmt|;
name|this
operator|.
name|collectsValues
operator|=
name|collectsValues
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterDone ()
specifier|protected
specifier|final
name|void
name|afterDone
parameter_list|()
block|{
name|super
operator|.
name|afterDone
argument_list|()
expr_stmt|;
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|localFutures
init|=
name|futures
decl_stmt|;
name|releaseResources
argument_list|(
name|OUTPUT_FUTURE_DONE
argument_list|)
expr_stmt|;
comment|// nulls out `futures`
if|if
condition|(
name|isCancelled
argument_list|()
operator|&
name|localFutures
operator|!=
literal|null
condition|)
block|{
name|boolean
name|wasInterrupted
init|=
name|wasInterrupted
argument_list|()
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|?
argument_list|>
name|future
range|:
name|localFutures
control|)
block|{
name|future
operator|.
name|cancel
argument_list|(
name|wasInterrupted
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * We don't call clearSeenExceptions() until processCompleted(). Prior to that, it may be needed      * again if some outstanding input fails.      */
block|}
annotation|@
name|Override
DECL|method|pendingToString ()
specifier|protected
specifier|final
name|String
name|pendingToString
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|Future
argument_list|<
name|?
argument_list|>
argument_list|>
name|localFutures
init|=
name|futures
decl_stmt|;
if|if
condition|(
name|localFutures
operator|!=
literal|null
condition|)
block|{
return|return
literal|"futures=["
operator|+
name|localFutures
operator|+
literal|"]"
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Must be called at the end of each subclass's constructor. This method performs the "real"    * initialization; we can't put this in the constructor because, in the case where futures are    * already complete, we would not initialize the subclass before calling {@link    * #collectValueFromNonCancelledFuture}. As this is called after the subclass is constructed,    * we're guaranteed to have properly initialized the subclass.    */
DECL|method|init ()
specifier|final
name|void
name|init
parameter_list|()
block|{
comment|// Corner case: List is empty.
if|if
condition|(
name|futures
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|handleAllCompleted
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// NOTE: If we ever want to use a custom executor here, have a look at CombinedFuture as we'll
comment|// need to handle RejectedExecutionException
if|if
condition|(
name|allMustSucceed
condition|)
block|{
comment|// We need fail fast, so we have to keep track of which future failed so we can propagate
comment|// the exception immediately
comment|// Register a listener on each Future in the list to update the state of this future.
comment|// Note that if all the futures on the list are done prior to completing this loop, the last
comment|// call to addListener() will callback to setOneValue(), transitively call our cleanup
comment|// listener, and set this.futures to null.
comment|// This is not actually a problem, since the foreach only needs this.futures to be non-null
comment|// at the beginning of the loop.
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
name|InputT
argument_list|>
name|future
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
name|future
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
try|try
block|{
if|if
condition|(
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
comment|// Clear futures prior to cancelling children. This sets our own state but lets
comment|// the input futures keep running, as some of them may be used elsewhere.
name|futures
operator|=
literal|null
expr_stmt|;
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|collectValueFromNonCancelledFuture
argument_list|(
name|index
argument_list|,
name|future
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|/*                    * "null" means: There is no need to access `futures` again during                    * `processCompleted` because we're reading each value during a call to                    * handleOneInputDone.                    */
name|decrementCountAndMaybeComplete
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|/*        * We'll call the user callback or collect the values only when all inputs complete,        * regardless of whether some failed. This lets us avoid calling expensive methods like        * Future.get() when we don't need to (specifically, for whenAllComplete().call*()), and it        * lets all futures share the same listener.        *        * We store `localFutures` inside the listener because `this.futures` might be nulled out by        * the time the listener runs for the final future -- at which point we need to check all        * inputs for exceptions *if* we're collecting values. If we're not, then the listener doesn't        * need access to the futures again, so we can just pass `null`.        *        * TODO(b/112550045): Allocating a single, cheaper listener is (I think) only an optimization.        * If we make some other optimizations, this one will no longer be necessary. The optimization        * could actually hurt in some cases, as it forces us to keep all inputs in memory until the        * final input completes.        */
specifier|final
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|Future
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
argument_list|>
name|localFutures
init|=
name|collectsValues
condition|?
name|futures
else|:
literal|null
decl_stmt|;
name|Runnable
name|listener
init|=
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
name|decrementCountAndMaybeComplete
argument_list|(
name|localFutures
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
for|for
control|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
name|future
range|:
name|futures
control|)
block|{
name|future
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Fails this future with the given Throwable if {@link #allMustSucceed} is true. Also, logs the    * throwable if it is an {@link Error} or if {@link #allMustSucceed} is {@code true}, the    * throwable did not cause this future to fail, and it is the first time we've seen that    * particular Throwable.    */
DECL|method|handleException (Throwable throwable)
specifier|private
name|void
name|handleException
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
if|if
condition|(
name|allMustSucceed
condition|)
block|{
comment|// As soon as the first one fails, make that failure the result of the output future.
comment|// The results of all other inputs are then ignored (except for logging any failures).
name|boolean
name|completedWithFailure
init|=
name|setException
argument_list|(
name|throwable
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|completedWithFailure
condition|)
block|{
comment|// Go up the causal chain to see if we've already seen this cause; if we have, even if
comment|// it's wrapped by a different exception, don't log it.
name|boolean
name|firstTimeSeeingThisException
init|=
name|addCausalChain
argument_list|(
name|getOrInitSeenExceptions
argument_list|()
argument_list|,
name|throwable
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstTimeSeeingThisException
condition|)
block|{
name|log
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
comment|/*      * TODO(cpovirk): Should whenAllComplete().call*() log errors, too? Currently, it doesn't call      * handleException() at all.      */
if|if
condition|(
name|throwable
operator|instanceof
name|Error
condition|)
block|{
comment|/*        * TODO(cpovirk): Do we really want to log this if we called setException(throwable) and it        * returned true? This was intentional (CL 46470009), but it seems odd compared to how we        * normally handle Error.        *        * Similarly, do we really want to log the same Error more than once?        */
name|log
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|log (Throwable throwable)
specifier|private
specifier|static
name|void
name|log
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|String
name|message
init|=
operator|(
name|throwable
operator|instanceof
name|Error
operator|)
condition|?
literal|"Input Future failed with Error"
else|:
literal|"Got more than one input Future failure. Logging failures after the first"
decl_stmt|;
name|logger
operator|.
name|log
argument_list|(
name|SEVERE
argument_list|,
name|message
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addInitialException (Set<Throwable> seen)
specifier|final
name|void
name|addInitialException
parameter_list|(
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seen
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|seen
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isCancelled
argument_list|()
condition|)
block|{
comment|// TODO(cpovirk): Think about whether we could/should use Verify to check this.
name|boolean
name|unused
init|=
name|addCausalChain
argument_list|(
name|seen
argument_list|,
name|tryInternalFastPathGetFailure
argument_list|()
argument_list|)
decl_stmt|;
block|}
block|}
comment|/**    * Collects the result (success or failure) of one input future. The input must not have been    * cancelled. For details on when this is called, see {@link #collectOneValue}.    */
DECL|method|collectValueFromNonCancelledFuture (int index, Future<? extends InputT> future)
specifier|private
name|void
name|collectValueFromNonCancelledFuture
parameter_list|(
name|int
name|index
parameter_list|,
name|Future
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
name|future
parameter_list|)
block|{
try|try
block|{
comment|// We get the result, even if collectOneValue is a no-op, so that we can fail fast.
name|collectOneValue
argument_list|(
name|index
argument_list|,
name|getDone
argument_list|(
name|future
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|handleException
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
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|decrementCountAndMaybeComplete ( @ullable ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion)
specifier|private
name|void
name|decrementCountAndMaybeComplete
parameter_list|(
annotation|@
name|Nullable
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|Future
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
argument_list|>
name|futuresIfNeedToCollectAtCompletion
parameter_list|)
block|{
name|int
name|newRemaining
init|=
name|decrementRemainingAndGet
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
name|processCompleted
argument_list|(
name|futuresIfNeedToCollectAtCompletion
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processCompleted ( @ullable ImmutableCollection<? extends Future<? extends InputT>> futuresIfNeedToCollectAtCompletion)
specifier|private
name|void
name|processCompleted
parameter_list|(
annotation|@
name|Nullable
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|Future
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
argument_list|>
name|futuresIfNeedToCollectAtCompletion
parameter_list|)
block|{
if|if
condition|(
name|futuresIfNeedToCollectAtCompletion
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
name|future
range|:
name|futuresIfNeedToCollectAtCompletion
control|)
block|{
if|if
condition|(
operator|!
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
name|collectValueFromNonCancelledFuture
argument_list|(
name|i
argument_list|,
name|future
argument_list|)
expr_stmt|;
block|}
name|i
operator|++
expr_stmt|;
block|}
block|}
name|clearSeenExceptions
argument_list|()
expr_stmt|;
name|handleAllCompleted
argument_list|()
expr_stmt|;
comment|/*      * Null out fields, including some used in handleAllCompleted() above (like      * `CollectionFuture.values`). This might be a no-op: If this future completed during      * handleAllCompleted(), they will already have been nulled out. But in the case of      * whenAll*().call*(), this future may be pending until the callback runs -- or even longer in      * the case of callAsync(), which waits for the callback's returned future to complete.      */
name|releaseResources
argument_list|(
name|ALL_INPUT_FUTURES_PROCESSED
argument_list|)
expr_stmt|;
block|}
comment|/**    * Clears fields that are no longer needed after this future has completed -- or at least all its    * inputs have completed (more precisely, after {@link #handleAllCompleted()} has been called).    * Often called multiple times (that is, both when the inputs complete and when the output    * completes).    *    *<p>This is similar to our proposed {@code afterCommit} method but not quite the same. See the    * description of CL 265462958.    */
comment|// TODO(user): Write more tests for memory retention.
annotation|@
name|ForOverride
annotation|@
name|OverridingMethodsMustInvokeSuper
DECL|method|releaseResources (ReleaseResourcesReason reason)
name|void
name|releaseResources
parameter_list|(
name|ReleaseResourcesReason
name|reason
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|reason
argument_list|)
expr_stmt|;
comment|/*      * All elements of `futures` are completed, or this future has already completed and read      * `futures` into a local variable (in preparation for propagating cancellation to them). In      * either case, no one needs to read `futures` for cancellation purposes later. (And      * cancellation purposes are the main reason to access `futures`, as discussed in its docs.)      */
name|this
operator|.
name|futures
operator|=
literal|null
expr_stmt|;
block|}
DECL|enum|ReleaseResourcesReason
enum|enum
name|ReleaseResourcesReason
block|{
DECL|enumConstant|OUTPUT_FUTURE_DONE
name|OUTPUT_FUTURE_DONE
block|,
DECL|enumConstant|ALL_INPUT_FUTURES_PROCESSED
name|ALL_INPUT_FUTURES_PROCESSED
block|,   }
comment|/**    * If {@code allMustSucceed} is true, called as each future completes; otherwise, if {@code    * collectsValues} is true, called for each future when all futures complete.    */
DECL|method|collectOneValue (int index, @Nullable InputT returnValue)
specifier|abstract
name|void
name|collectOneValue
parameter_list|(
name|int
name|index
parameter_list|,
annotation|@
name|Nullable
name|InputT
name|returnValue
parameter_list|)
function_decl|;
DECL|method|handleAllCompleted ()
specifier|abstract
name|void
name|handleAllCompleted
parameter_list|()
function_decl|;
comment|/** Adds the chain to the seen set, and returns whether all the chain was new to us. */
DECL|method|addCausalChain (Set<Throwable> seen, Throwable t)
specifier|private
specifier|static
name|boolean
name|addCausalChain
parameter_list|(
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seen
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
for|for
control|(
init|;
name|t
operator|!=
literal|null
condition|;
name|t
operator|=
name|t
operator|.
name|getCause
argument_list|()
control|)
block|{
name|boolean
name|firstTimeSeen
init|=
name|seen
operator|.
name|add
argument_list|(
name|t
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|firstTimeSeen
condition|)
block|{
comment|/*          * We've seen this, so we've seen its causes, too. No need to re-add them. (There's one case          * where this isn't true, but we ignore it: If we record an exception, then someone calls          * initCause() on it, and then we examine it again, we'll conclude that we've seen the whole          * chain before when it fact we haven't. But this should be rare.)          */
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

