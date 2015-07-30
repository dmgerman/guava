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
name|MoreExecutors
operator|.
name|directExecutor
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
name|annotations
operator|.
name|GwtIncompatible
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
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A future made up of a collection of sub-futures.  *  * @param<InputT> the type of the individual inputs  * @param<OutputT> the type of the output (i.e. this) future  */
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
name|AbstractFuture
operator|.
name|TrustedFuture
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
DECL|field|runningState
specifier|private
name|RunningState
name|runningState
decl_stmt|;
DECL|method|done ()
annotation|@
name|Override
specifier|final
name|void
name|done
parameter_list|()
block|{
name|super
operator|.
name|done
argument_list|()
expr_stmt|;
comment|// Let go of the memory held by the running state
name|this
operator|.
name|runningState
operator|=
literal|null
expr_stmt|;
block|}
comment|// TODO(cpovirk): Use maybePropagateCancellation() if the performance is OK and the code is clean.
DECL|method|cancel (boolean mayInterruptIfRunning)
annotation|@
name|Override
specifier|public
specifier|final
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
comment|// Must get a reference to the futures before we cancel, as they'll be cleared out.
name|RunningState
name|localRunningState
init|=
name|runningState
decl_stmt|;
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
init|=
operator|(
name|localRunningState
operator|!=
literal|null
operator|)
condition|?
name|localRunningState
operator|.
name|futures
else|:
literal|null
decl_stmt|;
comment|// Cancel all the component futures.
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
comment|//& is faster than the branch required for&&
if|if
condition|(
name|cancelled
operator|&
name|futures
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
name|futures
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Interruption not supported"
argument_list|)
DECL|method|interruptTask ()
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|interruptTask
parameter_list|()
block|{
name|RunningState
name|localRunningState
init|=
name|runningState
decl_stmt|;
if|if
condition|(
name|localRunningState
operator|!=
literal|null
condition|)
block|{
name|localRunningState
operator|.
name|interruptTask
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**    * Must be called at the end of each sub-class's constructor.    */
DECL|method|init (RunningState runningState)
specifier|final
name|void
name|init
parameter_list|(
name|RunningState
name|runningState
parameter_list|)
block|{
name|this
operator|.
name|runningState
operator|=
name|runningState
expr_stmt|;
name|runningState
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
annotation|@
name|WeakOuter
DECL|class|RunningState
specifier|abstract
class|class
name|RunningState
extends|extends
name|AggregateFutureState
implements|implements
name|Runnable
block|{
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
DECL|method|RunningState (ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues)
name|RunningState
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
comment|/* Used in the !allMustSucceed case so we don't have to instantiate a listener. */
DECL|method|run ()
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|run
parameter_list|()
block|{
name|decrementCountAndMaybeComplete
argument_list|()
expr_stmt|;
block|}
comment|/**      * The "real" initialization; we can't put this in the constructor because, in the case where      * futures are already complete, we would not initialize the subclass before calling      * {@link #handleOneInputDone}. As this is called after the subclass is constructed, we're      * guaranteed to have properly initialized the subclass.      */
DECL|method|init ()
specifier|private
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
comment|// NOTE: If we ever want to use a custom executor here, have a look at
comment|// CombinedFuture as we'll need to handle RejectedExecutionException
if|if
condition|(
name|allMustSucceed
condition|)
block|{
comment|// We need fail fast, so we have to keep track of which future failed so we can propagate
comment|// the exception immediately
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
name|InputT
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
try|try
block|{
name|handleOneInputDone
argument_list|(
name|index
argument_list|,
name|listenable
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|decrementCountAndMaybeComplete
argument_list|()
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
comment|// We'll only call the callback when all futures complete, regardless of whether some failed
comment|// Hold off on calling setOneValue until all complete, so we can share the same listener
for|for
control|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
name|listenable
range|:
name|futures
control|)
block|{
name|listenable
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Fails this future with the given Throwable if {@link #allMustSucceed} is      * true. Also, logs the throwable if it is an {@link Error} or if      * {@link #allMustSucceed} is {@code true}, the throwable did not cause      * this future to fail, and it is the first time we've seen that particular Throwable.      */
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
name|boolean
name|completedWithFailure
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
name|completedWithFailure
operator|=
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
if|if
condition|(
name|completedWithFailure
condition|)
block|{
name|releaseResourcesAfterFailure
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Go up the causal chain to see if we've already seen this cause; if we have,
comment|// even if it's wrapped by a different exception, don't log it.
name|firstTimeSeeingThisException
operator|=
name|addCausalChain
argument_list|(
name|getOrInitSeenExceptions
argument_list|()
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|// | and& used because it's faster than the branch required for || and&&
if|if
condition|(
name|throwable
operator|instanceof
name|Error
operator||
operator|(
name|allMustSucceed
operator|&
operator|!
name|completedWithFailure
operator|&
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
if|if
condition|(
operator|!
name|isCancelled
argument_list|()
condition|)
block|{
name|addCausalChain
argument_list|(
name|seen
argument_list|,
name|trustedGetException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Handles the input at the given index completing.      */
DECL|method|handleOneInputDone (int index, Future<? extends InputT> future)
specifier|private
name|void
name|handleOneInputDone
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
comment|// The only cases in which this Future should already be done are (a) if
comment|// it was cancelled or (b) if an input failed and we propagated that
comment|// immediately because of allMustSucceed.
name|checkState
argument_list|(
name|allMustSucceed
operator|||
operator|!
name|isDone
argument_list|()
operator|||
name|isCancelled
argument_list|()
argument_list|,
literal|"Future was done before all dependencies completed"
argument_list|)
expr_stmt|;
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
name|allMustSucceed
condition|)
block|{
if|if
condition|(
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
comment|// this.cancel propagates the cancellation to children; we use super.cancel
comment|// to set our own state but let the input futures keep running
comment|// as some of them may be used elsewhere.
name|AggregateFuture
operator|.
name|super
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// We always get the result so that we can have fail-fast, even if we don't collect
name|InputT
name|result
init|=
name|getUninterruptibly
argument_list|(
name|future
argument_list|)
decl_stmt|;
if|if
condition|(
name|collectsValues
condition|)
block|{
name|collectOneValue
argument_list|(
name|allMustSucceed
argument_list|,
name|index
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|collectsValues
operator|&&
operator|!
name|future
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
name|collectOneValue
argument_list|(
name|allMustSucceed
argument_list|,
name|index
argument_list|,
name|getUninterruptibly
argument_list|(
name|future
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|decrementCountAndMaybeComplete ()
specifier|private
name|void
name|decrementCountAndMaybeComplete
parameter_list|()
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
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|processCompleted ()
specifier|private
name|void
name|processCompleted
parameter_list|()
block|{
comment|// Collect the values if (a) our output requires collecting them and (b) we haven't been
comment|// collecting them as we go. (We've collected them as we go only if we needed to fail fast)
if|if
condition|(
name|collectsValues
operator|&
operator|!
name|allMustSucceed
condition|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|InputT
argument_list|>
name|listenable
range|:
name|futures
control|)
block|{
name|handleOneInputDone
argument_list|(
name|i
operator|++
argument_list|,
name|listenable
argument_list|)
expr_stmt|;
block|}
block|}
name|handleAllCompleted
argument_list|()
expr_stmt|;
block|}
comment|/**      * Listeners implicitly keep a reference to {@link RunningState} as they're inner classes,      * so we free resources here as well for the allMustSucceed=true case (i.e. when a future fails,      * we immediately release resources we no longer need); additionally, the future will release      * its reference to {@link RunningState}, which should free all associated memory when all the      * futures complete& the listeners are released.      *      * TODO(user): Write tests for memory retention      */
DECL|method|releaseResourcesAfterFailure ()
name|void
name|releaseResourcesAfterFailure
parameter_list|()
block|{
name|this
operator|.
name|futures
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Called only if {@code collectsValues} is true.      *      *<p>If {@code allMustSucceed} is true, called as each future completes; otherwise,      * called for each future when all futures complete.      */
DECL|method|collectOneValue (boolean allMustSucceed, int index, @Nullable InputT returnValue)
specifier|abstract
name|void
name|collectOneValue
parameter_list|(
name|boolean
name|allMustSucceed
parameter_list|,
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
DECL|method|interruptTask ()
name|void
name|interruptTask
parameter_list|()
block|{}
block|}
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

