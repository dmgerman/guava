begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
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
comment|/**  * Utilities necessary for working with libraries that supply plain {@link Future} instances. Note  * that, whenever possible, it is strongly preferred to modify those libraries to return {@code  * ListenableFuture} directly.  *  * @author Sven Mawson  * @since 10.0 (replacing {@code Futures.makeListenable}, which existed in 1.0)  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|JdkFutureAdapters
specifier|public
specifier|final
class|class
name|JdkFutureAdapters
block|{
comment|/**    * Assigns a thread to the given {@link Future} to provide {@link ListenableFuture} functionality.    *    *<p><b>Warning:</b> If the input future does not already implement {@code ListenableFuture}, the    * returned future will emulate {@link ListenableFuture#addListener} by taking a thread from an    * internal, unbounded pool at the first call to {@code addListener} and holding it until the    * future is {@linkplain Future#isDone() done}.    *    *<p>Prefer to create {@code ListenableFuture} instances with {@link SettableFuture}, {@link    * MoreExecutors#listeningDecorator( java.util.concurrent.ExecutorService)}, {@link    * ListenableFutureTask}, {@link AbstractFuture}, and other utilities over creating plain {@code    * Future} instances to be upgraded to {@code ListenableFuture} after the fact.    */
DECL|method|listenInPoolThread ( Future<V> future)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|listenInPoolThread
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
argument_list|)
block|{
if|if
condition|(
name|future
operator|instanceof
name|ListenableFuture
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
argument_list|<>
argument_list|(
name|future
argument_list|)
return|;
block|}
end_class

begin_comment
comment|/**    * Submits a blocking task for the given {@link Future} to provide {@link ListenableFuture}    * functionality.    *    *<p><b>Warning:</b> If the input future does not already implement {@code ListenableFuture}, the    * returned future will emulate {@link ListenableFuture#addListener} by submitting a task to the    * given executor at the first call to {@code addListener}. The task must be started by the    * executor promptly, or else the returned {@code ListenableFuture} may fail to work. The task's    * execution consists of blocking until the input future is {@linkplain Future#isDone() done}, so    * each call to this method may claim and hold a thread for an arbitrary length of time. Use of    * bounded executors or other executors that may fail to execute a task promptly may result in    * deadlocks.    *    *<p>Prefer to create {@code ListenableFuture} instances with {@link SettableFuture}, {@link    * MoreExecutors#listeningDecorator( java.util.concurrent.ExecutorService)}, {@link    * ListenableFutureTask}, {@link AbstractFuture}, and other utilities over creating plain {@code    * Future} instances to be upgraded to {@code ListenableFuture} after the fact.    *    * @since 12.0    */
end_comment

begin_expr_stmt
DECL|method|listenInPoolThread ( Future<V> future, Executor executor)
specifier|public
specifier|static
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|listenInPoolThread
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
argument_list|,
name|Executor
name|executor
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|executor
argument_list|)
block|;
if|if
condition|(
name|future
operator|instanceof
name|ListenableFuture
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
end_expr_stmt

begin_return
return|return
operator|new
name|ListenableFutureAdapter
argument_list|<>
argument_list|(
name|future
argument_list|,
name|executor
argument_list|)
return|;
end_return

begin_comment
unit|}
comment|/**    * An adapter to turn a {@link Future} into a {@link ListenableFuture}. This will wait on the    * future to finish, and when it completes, run the listeners. This implementation will wait on    * the source future indefinitely, so if the source future never completes, the adapter will never    * complete either.    *    *<p>If the delegate future is interrupted or throws an unexpected unchecked exception, the    * listeners will not be invoked.    */
end_comment

begin_expr_stmt
DECL|class|ListenableFutureAdapter
unit|private
specifier|static
name|class
name|ListenableFutureAdapter
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingFuture
argument_list|<
name|V
argument_list|>
expr|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|threadFactory
specifier|private
specifier|static
name|final
name|ThreadFactory
name|threadFactory
operator|=
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|setNameFormat
argument_list|(
literal|"ListenableFutureAdapter-thread-%d"
argument_list|)
operator|.
name|build
argument_list|()
block|;
DECL|field|defaultAdapterExecutor
specifier|private
specifier|static
name|final
name|Executor
name|defaultAdapterExecutor
operator|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|(
name|threadFactory
argument_list|)
block|;
DECL|field|adapterExecutor
specifier|private
name|final
name|Executor
name|adapterExecutor
block|;
comment|// The execution list to hold our listeners.
DECL|field|executionList
specifier|private
name|final
name|ExecutionList
name|executionList
operator|=
operator|new
name|ExecutionList
argument_list|()
block|;
comment|// This allows us to only start up a thread waiting on the delegate future when the first
comment|// listener is added.
DECL|field|hasListeners
specifier|private
name|final
name|AtomicBoolean
name|hasListeners
operator|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
block|;
comment|// The delegate future.
DECL|field|delegate
specifier|private
name|final
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
block|;
DECL|method|ListenableFutureAdapter (Future<V> delegate)
name|ListenableFutureAdapter
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
argument_list|)
block|{
name|this
argument_list|(
name|delegate
argument_list|,
name|defaultAdapterExecutor
argument_list|)
block|;     }
DECL|method|ListenableFutureAdapter (Future<V> delegate, Executor adapterExecutor)
name|ListenableFutureAdapter
argument_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
argument_list|,
name|Executor
name|adapterExecutor
argument_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
block|;
name|this
operator|.
name|adapterExecutor
operator|=
name|checkNotNull
argument_list|(
name|adapterExecutor
argument_list|)
block|;     }
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Future
argument_list|<
name|V
argument_list|>
name|delegate
argument_list|()
block|{
return|return
name|delegate
return|;
block|}
expr|@
name|Override
DECL|method|addListener (Runnable listener, Executor exec)
specifier|public
name|void
name|addListener
argument_list|(
name|Runnable
name|listener
argument_list|,
name|Executor
name|exec
argument_list|)
block|{
name|executionList
operator|.
name|add
argument_list|(
name|listener
argument_list|,
name|exec
argument_list|)
block|;
comment|// When a listener is first added, we run a task that will wait for the delegate to finish,
comment|// and when it is done will run the listeners.
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
comment|// If the delegate is already done, run the execution list immediately on the current
comment|// thread.
name|executionList
operator|.
name|execute
argument_list|()
expr_stmt|;
return|return;
block|}
comment|// TODO(lukes): handle RejectedExecutionException
name|adapterExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
block|{
comment|/*                  * Threads from our private pool are never interrupted. Threads from a                  * user-supplied executor might be, but... what can we do? This is another reason                  * to return a proper ListenableFuture instead of using listenInPoolThread.                  */
name|getUninterruptibly
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ExecutionException / CancellationException / RuntimeException / Error
comment|// The task is presumably done, run the listeners.
block|}
name|executionList
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
unit|}     }   }    private
DECL|method|JdkFutureAdapters ()
name|JdkFutureAdapters
argument_list|()
block|{}
end_expr_stmt

unit|}
end_unit

