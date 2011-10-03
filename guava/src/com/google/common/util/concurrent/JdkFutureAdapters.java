begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ThreadFactory
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
name|AtomicBoolean
import|;
end_import

begin_comment
comment|/**  * Utilities necessary for working with libraries that supply plain {@link  * Future} instances. Note that, whenver possible, it is strongly preferred to  * modify those libraries to return {@code ListenableFuture} directly.  *  * @author Sven Mawson  * @since 10.0 (replacing {@code Futures.makeListenable}, which  *     existed in 1.0)  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|JdkFutureAdapters
specifier|public
specifier|final
class|class
name|JdkFutureAdapters
block|{
comment|/**    * Assigns a thread to the given {@link Future} to provide {@link    * ListenableFuture} functionality.    *    *<p><b>Warning:</b> If the input future does not already implement {@link    * ListenableFuture}, the returned future will emulate {@link    * ListenableFuture#addListener} by taking a thread from an internal,    * unbounded pool at the first call to {@code addListener} and holding it    * until the future is {@linkplain Future#isDone() done}.    *    *<p>Prefer to create {@code ListenableFuture} instances with {@link    * SettableFuture}, {@link MoreExecutors#listeningDecorator(    * java.util.concurrent.ExecutorService)}, {@link ListenableFutureTask},    * {@link AbstractFuture}, and other utilities over creating plain {@code    * Future} instances to be upgraded to {@code ListenableFuture} after the    * fact.    */
DECL|method|listenInPoolThread ( Future<V> future)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|listenInPoolThread
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
block|{
if|if
condition|(
name|future
operator|instanceof
name|ListenableFuture
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
operator|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
operator|)
name|future
return|;
block|}
return|return
operator|new
name|ListenableFutureAdapter
argument_list|<
name|V
argument_list|>
argument_list|(
name|future
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|listenInPoolThread ( Future<V> future, Executor executor)
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|listenInPoolThread
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
if|if
condition|(
name|future
operator|instanceof
name|ListenableFuture
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
operator|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
operator|)
name|future
return|;
block|}
return|return
operator|new
name|ListenableFutureAdapter
argument_list|<
name|V
argument_list|>
argument_list|(
name|future
argument_list|,
name|executor
argument_list|)
return|;
block|}
comment|/**    * An adapter to turn a {@link Future} into a {@link ListenableFuture}.  This    * will wait on the future to finish, and when it completes, run the    * listeners.  This implementation will wait on the source future    * indefinitely, so if the source future never completes, the adapter will    * never complete either.    *    *<p>If the delegate future is interrupted or throws an unexpected unchecked    * exception, the listeners will not be invoked.    */
DECL|class|ListenableFutureAdapter
specifier|private
specifier|static
class|class
name|ListenableFutureAdapter
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ForwardingFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|threadFactory
specifier|private
specifier|static
specifier|final
name|ThreadFactory
name|threadFactory
init|=
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
literal|"ListenableFutureAdapter-thread-%d"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
DECL|field|defaultAdapterExecutor
specifier|private
specifier|static
specifier|final
name|Executor
name|defaultAdapterExecutor
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|(
name|threadFactory
argument_list|)
decl_stmt|;
DECL|field|adapterExecutor
specifier|private
specifier|final
name|Executor
name|adapterExecutor
decl_stmt|;
comment|// The execution list to hold our listeners.
DECL|field|executionList
specifier|private
specifier|final
name|ExecutionList
name|executionList
init|=
operator|new
name|ExecutionList
argument_list|()
decl_stmt|;
comment|// This allows us to only start up a thread waiting on the delegate future
comment|// when the first listener is added.
DECL|field|hasListeners
specifier|private
specifier|final
name|AtomicBoolean
name|hasListeners
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// The delegate future.
DECL|field|delegate
specifier|private
specifier|final
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ListenableFutureAdapter (Future<V> delegate)
name|ListenableFutureAdapter
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
argument_list|(
name|delegate
argument_list|,
name|defaultAdapterExecutor
argument_list|)
expr_stmt|;
block|}
DECL|method|ListenableFutureAdapter (Future<V> delegate, Executor adapterExecutor)
name|ListenableFutureAdapter
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|,
name|Executor
name|adapterExecutor
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|adapterExecutor
operator|=
name|checkNotNull
argument_list|(
name|adapterExecutor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor exec)
specifier|public
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|exec
parameter_list|)
block|{
name|executionList
operator|.
name|add
argument_list|(
name|listener
argument_list|,
name|exec
argument_list|)
expr_stmt|;
comment|// When a listener is first added, we run a task that will wait for
comment|// the delegate to finish, and when it is done will run the listeners.
if|if
condition|(
name|hasListeners
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
if|if
condition|(
name|delegate
operator|.
name|isDone
argument_list|()
condition|)
block|{
comment|// If the delegate is already done, run the execution list
comment|// immediately on the current thread.
name|executionList
operator|.
name|execute
argument_list|()
expr_stmt|;
return|return;
block|}
name|adapterExecutor
operator|.
name|execute
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
name|delegate
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
comment|// Threads from our private pool are never interrupted.
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ExecutionException / CancellationException / RuntimeException
comment|// The task is done, run the listeners.
block|}
name|executionList
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|JdkFutureAdapters ()
specifier|private
name|JdkFutureAdapters
parameter_list|()
block|{}
block|}
end_class

end_unit

