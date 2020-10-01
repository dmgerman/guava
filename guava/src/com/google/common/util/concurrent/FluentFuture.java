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
name|util
operator|.
name|concurrent
operator|.
name|Internal
operator|.
name|toNanosSaturated
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
name|base
operator|.
name|Function
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
name|CanIgnoreReturnValue
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
name|DoNotMock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|ScheduledExecutorService
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/**  * A {@link ListenableFuture} that supports fluent chains of operations. For example:  *  *<pre>{@code  * ListenableFuture<Boolean> adminIsLoggedIn =  *     FluentFuture.from(usersDatabase.getAdminUser())  *         .transform(User::getId, directExecutor())  *         .transform(ActivityService::isLoggedIn, threadPool)  *         .catching(RpcException.class, e -> false, directExecutor());  * }</pre>  *  *<h3>Alternatives</h3>  *  *<h4>Frameworks</h4>  *  *<p>When chaining together a graph of asynchronous operations, you will often find it easier to  * use a framework. Frameworks automate the process, often adding features like monitoring,  * debugging, and cancellation. Examples of frameworks include:  *  *<ul>  *<li><a href="https://dagger.dev/producers.html">Dagger Producers</a>  *</ul>  *  *<h4>{@link java.util.concurrent.CompletableFuture} / {@link java.util.concurrent.CompletionStage}  *</h4>  *  *<p>Users of {@code CompletableFuture} will likely want to continue using {@code  * CompletableFuture}. {@code FluentFuture} is targeted at people who use {@code ListenableFuture},  * who can't use Java 8, or who want an API more focused than {@code CompletableFuture}. (If you  * need to adapt between {@code CompletableFuture} and {@code ListenableFuture}, consider<a  * href="https://github.com/lukas-krecan/future-converter">Future Converter</a>.)  *  *<h3>Extension</h3>  *  * If you want a class like {@code FluentFuture} but with extra methods, we recommend declaring your  * own subclass of {@link ListenableFuture}, complete with a method like {@link #from} to adapt an  * existing {@code ListenableFuture}, implemented atop a {@link ForwardingListenableFuture} that  * forwards to that future and adds the desired methods.  *  * @since 23.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|DoNotMock
argument_list|(
literal|"Use FluentFuture.from(Futures.immediate*Future) or SettableFuture"
argument_list|)
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FluentFuture
specifier|public
specifier|abstract
class|class
name|FluentFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|GwtFluentFutureCatchingSpecialization
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * A less abstract subclass of AbstractFuture. This can be used to optimize setFuture by ensuring    * that {@link #get} calls exactly the implementation of {@link AbstractFuture#get}.    */
DECL|class|TrustedFuture
specifier|abstract
specifier|static
class|class
name|TrustedFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|FluentFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|AbstractFuture
operator|.
name|Trusted
argument_list|<
name|V
argument_list|>
block|{
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|final
name|V
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
return|return
name|super
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
specifier|final
name|V
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
block|{
return|return
name|super
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
specifier|final
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|super
operator|.
name|isDone
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isCancelled ()
specifier|public
specifier|final
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|super
operator|.
name|isCancelled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
specifier|final
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|super
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
specifier|final
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
return|return
name|super
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
return|;
block|}
block|}
DECL|method|FluentFuture ()
name|FluentFuture
parameter_list|()
block|{}
comment|/**    * Converts the given {@code ListenableFuture} to an equivalent {@code FluentFuture}.    *    *<p>If the given {@code ListenableFuture} is already a {@code FluentFuture}, it is returned    * directly. If not, it is wrapped in a {@code FluentFuture} that delegates all calls to the    * original {@code ListenableFuture}.    */
DECL|method|from (ListenableFuture<V> future)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|from
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
block|{
return|return
name|future
operator|instanceof
name|FluentFuture
condition|?
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|future
else|:
operator|new
name|ForwardingFluentFuture
argument_list|<
name|V
argument_list|>
argument_list|(
name|future
argument_list|)
return|;
block|}
comment|/**    * Simply returns its argument.    *    * @deprecated no need to use this    * @since 28.0    */
annotation|@
name|Deprecated
DECL|method|from (FluentFuture<V> future)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|from
parameter_list|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|future
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code Future} whose result is taken from this {@code Future} or, if this {@code    * Future} fails with the given {@code exceptionType}, from the result provided by the {@code    * fallback}. {@link Function#apply} is not invoked until the primary input has failed, so if the    * primary input succeeds, it is never invoked. If, during the invocation of {@code fallback}, an    * exception is thrown, this exception is used as the result of the output {@code Future}.    *    *<p>Usage example:    *    *<pre>{@code    * // Falling back to a zero counter in case an exception happens when processing the RPC to fetch    * // counters.    * ListenableFuture<Integer> faultTolerantFuture =    *     fetchCounters().catching(FetchException.class, x -> 0, directExecutor());    * }</pre>    *    *<p>When selecting an executor, note that {@code directExecutor} is dangerous in some cases. See    * the discussion in the {@link #addListener} documentation. All its warnings about heavyweight    * listeners are also applicable to heavyweight functions passed to this method.    *    *<p>This method is similar to {@link java.util.concurrent.CompletableFuture#exceptionally}. It    * can also serve some of the use cases of {@link java.util.concurrent.CompletableFuture#handle}    * and {@link java.util.concurrent.CompletableFuture#handleAsync} when used along with {@link    * #transform}.    *    * @param exceptionType the exception type that triggers use of {@code fallback}. The exception    *     type is matched against the input's exception. "The input's exception" means the cause of    *     the {@link ExecutionException} thrown by {@code input.get()} or, if {@code get()} throws a    *     different kind of exception, that exception itself. To avoid hiding bugs and other    *     unrecoverable errors, callers should prefer more specific types, avoiding {@code    *     Throwable.class} in particular.    * @param fallback the {@link Function} to be called if the input fails with the expected    *     exception type. The function's argument is the input's exception. "The input's exception"    *     means the cause of the {@link ExecutionException} thrown by {@code this.get()} or, if    *     {@code get()} throws a different kind of exception, that exception itself.    * @param executor the executor that runs {@code fallback} if the input fails    */
annotation|@
name|Partially
operator|.
name|GwtIncompatible
argument_list|(
literal|"AVAILABLE but requires exceptionType to be Throwable.class"
argument_list|)
DECL|method|catching ( Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor)
specifier|public
specifier|final
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|catching
parameter_list|(
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|Futures
operator|.
name|catching
argument_list|(
name|this
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code Future} whose result is taken from this {@code Future} or, if this {@code    * Future} fails with the given {@code exceptionType}, from the result provided by the {@code    * fallback}. {@link AsyncFunction#apply} is not invoked until the primary input has failed, so if    * the primary input succeeds, it is never invoked. If, during the invocation of {@code fallback},    * an exception is thrown, this exception is used as the result of the output {@code Future}.    *    *<p>Usage examples:    *    *<pre>{@code    * // Falling back to a zero counter in case an exception happens when processing the RPC to fetch    * // counters.    * ListenableFuture<Integer> faultTolerantFuture =    *     fetchCounters().catchingAsync(    *         FetchException.class, x -> immediateFuture(0), directExecutor());    * }</pre>    *    *<p>The fallback can also choose to propagate the original exception when desired:    *    *<pre>{@code    * // Falling back to a zero counter only in case the exception was a    * // TimeoutException.    * ListenableFuture<Integer> faultTolerantFuture =    *     fetchCounters().catchingAsync(    *         FetchException.class,    *         e -> {    *           if (omitDataOnFetchFailure) {    *             return immediateFuture(0);    *           }    *           throw e;    *         },    *         directExecutor());    * }</pre>    *    *<p>When selecting an executor, note that {@code directExecutor} is dangerous in some cases. See    * the discussion in the {@link #addListener} documentation. All its warnings about heavyweight    * listeners are also applicable to heavyweight functions passed to this method. (Specifically,    * {@code directExecutor} functions should avoid heavyweight operations inside {@code    * AsyncFunction.apply}. Any heavyweight operations should occur in other threads responsible for    * completing the returned {@code Future}.)    *    *<p>This method is similar to {@link java.util.concurrent.CompletableFuture#exceptionally}. It    * can also serve some of the use cases of {@link java.util.concurrent.CompletableFuture#handle}    * and {@link java.util.concurrent.CompletableFuture#handleAsync} when used along with {@link    * #transform}.    *    * @param exceptionType the exception type that triggers use of {@code fallback}. The exception    *     type is matched against the input's exception. "The input's exception" means the cause of    *     the {@link ExecutionException} thrown by {@code this.get()} or, if {@code get()} throws a    *     different kind of exception, that exception itself. To avoid hiding bugs and other    *     unrecoverable errors, callers should prefer more specific types, avoiding {@code    *     Throwable.class} in particular.    * @param fallback the {@link AsyncFunction} to be called if the input fails with the expected    *     exception type. The function's argument is the input's exception. "The input's exception"    *     means the cause of the {@link ExecutionException} thrown by {@code input.get()} or, if    *     {@code get()} throws a different kind of exception, that exception itself.    * @param executor the executor that runs {@code fallback} if the input fails    */
annotation|@
name|Partially
operator|.
name|GwtIncompatible
argument_list|(
literal|"AVAILABLE but requires exceptionType to be Throwable.class"
argument_list|)
DECL|method|catchingAsync ( Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor)
specifier|public
specifier|final
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|catchingAsync
parameter_list|(
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionType
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|X
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|Futures
operator|.
name|catchingAsync
argument_list|(
name|this
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * Returns a future that delegates to this future but will finish early (via a {@link    * TimeoutException} wrapped in an {@link ExecutionException}) if the specified timeout expires.    * If the timeout expires, not only will the output future finish, but also the input future    * ({@code this}) will be cancelled and interrupted.    *    * @param timeout when to time out the future    * @param scheduledExecutor The executor service to enforce the timeout.    * @since 28.0    */
annotation|@
name|GwtIncompatible
comment|// ScheduledExecutorService
DECL|method|withTimeout ( Duration timeout, ScheduledExecutorService scheduledExecutor)
specifier|public
specifier|final
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|withTimeout
parameter_list|(
name|Duration
name|timeout
parameter_list|,
name|ScheduledExecutorService
name|scheduledExecutor
parameter_list|)
block|{
return|return
name|withTimeout
argument_list|(
name|toNanosSaturated
argument_list|(
name|timeout
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|,
name|scheduledExecutor
argument_list|)
return|;
block|}
comment|/**    * Returns a future that delegates to this future but will finish early (via a {@link    * TimeoutException} wrapped in an {@link ExecutionException}) if the specified timeout expires.    * If the timeout expires, not only will the output future finish, but also the input future    * ({@code this}) will be cancelled and interrupted.    *    * @param timeout when to time out the future    * @param unit the time unit of the time parameter    * @param scheduledExecutor The executor service to enforce the timeout.    */
annotation|@
name|GwtIncompatible
comment|// ScheduledExecutorService
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// should accept a java.time.Duration
DECL|method|withTimeout ( long timeout, TimeUnit unit, ScheduledExecutorService scheduledExecutor)
specifier|public
specifier|final
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|withTimeout
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|ScheduledExecutorService
name|scheduledExecutor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|Futures
operator|.
name|withTimeout
argument_list|(
name|this
argument_list|,
name|timeout
argument_list|,
name|unit
argument_list|,
name|scheduledExecutor
argument_list|)
return|;
block|}
comment|/**    * Returns a new {@code Future} whose result is asynchronously derived from the result of this    * {@code Future}. If the input {@code Future} fails, the returned {@code Future} fails with the    * same exception (and the function is not invoked).    *    *<p>More precisely, the returned {@code Future} takes its result from a {@code Future} produced    * by applying the given {@code AsyncFunction} to the result of the original {@code Future}.    * Example usage:    *    *<pre>{@code    * FluentFuture<RowKey> rowKeyFuture = FluentFuture.from(indexService.lookUp(query));    * ListenableFuture<QueryResult> queryFuture =    *     rowKeyFuture.transformAsync(dataService::readFuture, executor);    * }</pre>    *    *<p>When selecting an executor, note that {@code directExecutor} is dangerous in some cases. See    * the discussion in the {@link #addListener} documentation. All its warnings about heavyweight    * listeners are also applicable to heavyweight functions passed to this method. (Specifically,    * {@code directExecutor} functions should avoid heavyweight operations inside {@code    * AsyncFunction.apply}. Any heavyweight operations should occur in other threads responsible for    * completing the returned {@code Future}.)    *    *<p>The returned {@code Future} attempts to keep its cancellation state in sync with that of the    * input future and that of the future returned by the chain function. That is, if the returned    * {@code Future} is cancelled, it will attempt to cancel the other two, and if either of the    * other two is cancelled, the returned {@code Future} will receive a callback in which it will    * attempt to cancel itself.    *    *<p>This method is similar to {@link java.util.concurrent.CompletableFuture#thenCompose} and    * {@link java.util.concurrent.CompletableFuture#thenComposeAsync}. It can also serve some of the    * use cases of {@link java.util.concurrent.CompletableFuture#handle} and {@link    * java.util.concurrent.CompletableFuture#handleAsync} when used along with {@link #catching}.    *    * @param function A function to transform the result of this future to the result of the output    *     future    * @param executor Executor to run the function in.    * @return A future that holds result of the function (if the input succeeded) or the original    *     input's failure (if not)    */
DECL|method|transformAsync ( AsyncFunction<? super V, T> function, Executor executor)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|FluentFuture
argument_list|<
name|T
argument_list|>
name|transformAsync
parameter_list|(
name|AsyncFunction
argument_list|<
name|?
super|super
name|V
argument_list|,
name|T
argument_list|>
name|function
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|T
argument_list|>
operator|)
name|Futures
operator|.
name|transformAsync
argument_list|(
name|this
argument_list|,
name|function
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * Returns a new {@code Future} whose result is derived from the result of this {@code Future}. If    * this input {@code Future} fails, the returned {@code Future} fails with the same exception (and    * the function is not invoked). Example usage:    *    *<pre>{@code    * ListenableFuture<List<Row>> rowsFuture =    *     queryFuture.transform(QueryResult::getRows, executor);    * }</pre>    *    *<p>When selecting an executor, note that {@code directExecutor} is dangerous in some cases. See    * the discussion in the {@link #addListener} documentation. All its warnings about heavyweight    * listeners are also applicable to heavyweight functions passed to this method.    *    *<p>The returned {@code Future} attempts to keep its cancellation state in sync with that of the    * input future. That is, if the returned {@code Future} is cancelled, it will attempt to cancel    * the input, and if the input is cancelled, the returned {@code Future} will receive a callback    * in which it will attempt to cancel itself.    *    *<p>An example use of this method is to convert a serializable object returned from an RPC into    * a POJO.    *    *<p>This method is similar to {@link java.util.concurrent.CompletableFuture#thenApply} and    * {@link java.util.concurrent.CompletableFuture#thenApplyAsync}. It can also serve some of the    * use cases of {@link java.util.concurrent.CompletableFuture#handle} and {@link    * java.util.concurrent.CompletableFuture#handleAsync} when used along with {@link #catching}.    *    * @param function A Function to transform the results of this future to the results of the    *     returned future.    * @param executor Executor to run the function in.    * @return A future that holds result of the transformation.    */
DECL|method|transform (Function<? super V, T> function, Executor executor)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|FluentFuture
argument_list|<
name|T
argument_list|>
name|transform
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|V
argument_list|,
name|T
argument_list|>
name|function
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|T
argument_list|>
operator|)
name|Futures
operator|.
name|transform
argument_list|(
name|this
argument_list|,
name|function
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * Registers separate success and failure callbacks to be run when this {@code Future}'s    * computation is {@linkplain java.util.concurrent.Future#isDone() complete} or, if the    * computation is already complete, immediately.    *    *<p>The callback is run on {@code executor}. There is no guaranteed ordering of execution of    * callbacks, but any callback added through this method is guaranteed to be called once the    * computation is complete.    *    *<p>Example:    *    *<pre>{@code    * future.addCallback(    *     new FutureCallback<QueryResult>() {    *       public void onSuccess(QueryResult result) {    *         storeInCache(result);    *       }    *       public void onFailure(Throwable t) {    *         reportError(t);    *       }    *     }, executor);    * }</pre>    *    *<p>When selecting an executor, note that {@code directExecutor} is dangerous in some cases. See    * the discussion in the {@link #addListener} documentation. All its warnings about heavyweight    * listeners are also applicable to heavyweight callbacks passed to this method.    *    *<p>For a more general interface to attach a completion listener, see {@link #addListener}.    *    *<p>This method is similar to {@link java.util.concurrent.CompletableFuture#whenComplete} and    * {@link java.util.concurrent.CompletableFuture#whenCompleteAsync}. It also serves the use case    * of {@link java.util.concurrent.CompletableFuture#thenAccept} and {@link    * java.util.concurrent.CompletableFuture#thenAcceptAsync}.    *    * @param callback The callback to invoke when this {@code Future} is completed.    * @param executor The executor to run {@code callback} when the future completes.    */
DECL|method|addCallback (FutureCallback<? super V> callback, Executor executor)
specifier|public
specifier|final
name|void
name|addCallback
parameter_list|(
name|FutureCallback
argument_list|<
name|?
super|super
name|V
argument_list|>
name|callback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|Futures
operator|.
name|addCallback
argument_list|(
name|this
argument_list|,
name|callback
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

