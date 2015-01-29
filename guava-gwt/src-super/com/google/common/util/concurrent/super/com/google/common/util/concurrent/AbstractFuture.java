begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|CancellationException
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
comment|/**  * Emulation for AbstractFuture in GWT.  */
end_comment

begin_class
DECL|class|AbstractFuture
specifier|public
specifier|abstract
class|class
name|AbstractFuture
parameter_list|<
name|V
parameter_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|class|TrustedFuture
specifier|abstract
specifier|static
class|class
name|TrustedFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|method|get ()
annotation|@
name|Override
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
DECL|method|get (long timeout, TimeUnit unit)
annotation|@
name|Override
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
DECL|method|isDone ()
annotation|@
name|Override
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
DECL|method|isCancelled ()
annotation|@
name|Override
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
DECL|method|addListener (Runnable listener, Executor executor)
annotation|@
name|Override
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
block|}
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
name|AbstractFuture
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|state
specifier|private
name|State
name|state
decl_stmt|;
DECL|field|value
specifier|private
name|V
name|value
decl_stmt|;
DECL|field|delegate
specifier|private
name|Future
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|field|throwable
specifier|private
name|Throwable
name|throwable
decl_stmt|;
DECL|field|mayInterruptIfRunning
specifier|private
name|boolean
name|mayInterruptIfRunning
decl_stmt|;
DECL|field|listeners
specifier|private
name|List
argument_list|<
name|Listener
argument_list|>
name|listeners
decl_stmt|;
DECL|method|AbstractFuture ()
specifier|protected
name|AbstractFuture
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|PENDING
expr_stmt|;
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|Listener
argument_list|>
argument_list|()
expr_stmt|;
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
if|if
condition|(
operator|!
name|state
operator|.
name|permitsPublicUserToTransitionTo
argument_list|(
name|State
operator|.
name|CANCELLED
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|this
operator|.
name|mayInterruptIfRunning
operator|=
name|mayInterruptIfRunning
expr_stmt|;
name|state
operator|=
name|State
operator|.
name|CANCELLED
expr_stmt|;
name|notifyAndClearListeners
argument_list|()
expr_stmt|;
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
name|state
operator|.
name|isCancelled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
return|return
name|state
operator|.
name|isDone
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|state
operator|.
name|maybeThrowOnGet
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
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
name|checkNotNull
argument_list|(
name|unit
argument_list|)
expr_stmt|;
return|return
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addListener (Runnable runnable, Executor executor)
specifier|public
name|void
name|addListener
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|Listener
name|listener
init|=
operator|new
name|Listener
argument_list|(
name|runnable
argument_list|,
name|executor
argument_list|)
decl_stmt|;
if|if
condition|(
name|isDone
argument_list|()
condition|)
block|{
name|listener
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setException (Throwable throwable)
specifier|protected
name|boolean
name|setException
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
operator|!
name|state
operator|.
name|permitsPublicUserToTransitionTo
argument_list|(
name|State
operator|.
name|FAILURE
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|forceSetException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|forceSetException (Throwable throwable)
specifier|private
name|void
name|forceSetException
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|this
operator|.
name|throwable
operator|=
name|throwable
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|State
operator|.
name|FAILURE
expr_stmt|;
name|notifyAndClearListeners
argument_list|()
expr_stmt|;
block|}
DECL|method|set (V value)
specifier|protected
name|boolean
name|set
parameter_list|(
name|V
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|permitsPublicUserToTransitionTo
argument_list|(
name|State
operator|.
name|VALUE
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|forceSet
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|forceSet (V value)
specifier|private
name|void
name|forceSet
parameter_list|(
name|V
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|State
operator|.
name|VALUE
expr_stmt|;
name|notifyAndClearListeners
argument_list|()
expr_stmt|;
block|}
DECL|method|setFuture (ListenableFuture<? extends V> future)
specifier|protected
name|boolean
name|setFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|future
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|future
argument_list|)
expr_stmt|;
comment|// If this future is already cancelled, cancel the delegate.
if|if
condition|(
name|isCancelled
argument_list|()
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|state
operator|.
name|permitsPublicUserToTransitionTo
argument_list|(
name|State
operator|.
name|DELEGATED
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|state
operator|=
name|State
operator|.
name|DELEGATED
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|future
expr_stmt|;
name|future
operator|.
name|addListener
argument_list|(
operator|new
name|SetFuture
argument_list|(
name|future
argument_list|)
argument_list|,
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|wasInterrupted ()
specifier|protected
specifier|final
name|boolean
name|wasInterrupted
parameter_list|()
block|{
return|return
name|mayInterruptIfRunning
return|;
block|}
DECL|method|notifyAndClearListeners ()
specifier|private
name|void
name|notifyAndClearListeners
parameter_list|()
block|{
comment|// TODO(cpovirk): consider clearing this.delegate
for|for
control|(
name|Listener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|listeners
operator|=
literal|null
expr_stmt|;
name|done
argument_list|()
expr_stmt|;
block|}
DECL|method|done ()
name|void
name|done
parameter_list|()
block|{}
DECL|enum|State
specifier|private
enum|enum
name|State
block|{
DECL|enumConstant|PENDING
name|PENDING
block|{
annotation|@
name|Override
name|boolean
name|isDone
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
name|void
name|maybeThrowOnGet
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot get() on a pending future."
argument_list|)
throw|;
block|}
annotation|@
name|Override
name|boolean
name|permitsPublicUserToTransitionTo
parameter_list|(
name|State
name|state
parameter_list|)
block|{
return|return
operator|!
name|state
operator|.
name|equals
argument_list|(
name|PENDING
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|DELEGATED
name|DELEGATED
block|{
annotation|@
name|Override
name|boolean
name|isDone
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
name|void
name|maybeThrowOnGet
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot get() on a pending future."
argument_list|)
throw|;
block|}
name|boolean
name|permitsPublicUserToTransitionTo
parameter_list|(
name|State
name|state
parameter_list|)
block|{
return|return
name|state
operator|.
name|equals
argument_list|(
name|CANCELLED
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|VALUE
name|VALUE
block|,
DECL|enumConstant|FAILURE
name|FAILURE
block|{
annotation|@
name|Override
name|void
name|maybeThrowOnGet
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
block|}
block|,
DECL|enumConstant|CANCELLED
name|CANCELLED
block|{
annotation|@
name|Override
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
name|void
name|maybeThrowOnGet
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{
comment|// TODO(cpovirk): chain in a CancellationException created at the cancel() call?
throw|throw
operator|new
name|CancellationException
argument_list|()
throw|;
block|}
block|}
block|;
DECL|method|isDone ()
name|boolean
name|isDone
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isCancelled ()
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|maybeThrowOnGet (Throwable cause)
name|void
name|maybeThrowOnGet
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{}
DECL|method|permitsPublicUserToTransitionTo (State state)
name|boolean
name|permitsPublicUserToTransitionTo
parameter_list|(
name|State
name|state
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|Listener
specifier|private
specifier|static
specifier|final
class|class
name|Listener
block|{
DECL|field|command
specifier|final
name|Runnable
name|command
decl_stmt|;
DECL|field|executor
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|method|Listener (Runnable command, Executor executor)
name|Listener
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|checkNotNull
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|checkNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
DECL|method|execute ()
name|void
name|execute
parameter_list|()
block|{
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"RuntimeException while executing runnable "
operator|+
name|command
operator|+
literal|" with executor "
operator|+
name|executor
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|SetFuture
specifier|private
specifier|final
class|class
name|SetFuture
implements|implements
name|Runnable
block|{
DECL|field|delegate
specifier|final
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|delegate
decl_stmt|;
DECL|method|SetFuture (ListenableFuture<? extends V> delegate)
name|SetFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|isCancelled
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delegate
operator|instanceof
name|TrustedFuture
condition|)
block|{
name|AbstractFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|other
init|=
operator|(
name|AbstractFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
operator|)
name|delegate
decl_stmt|;
name|value
operator|=
name|other
operator|.
name|value
expr_stmt|;
name|throwable
operator|=
name|other
operator|.
name|throwable
expr_stmt|;
name|mayInterruptIfRunning
operator|=
name|other
operator|.
name|mayInterruptIfRunning
expr_stmt|;
name|state
operator|=
name|other
operator|.
name|state
expr_stmt|;
name|notifyAndClearListeners
argument_list|()
expr_stmt|;
return|return;
block|}
try|try
block|{
name|forceSet
argument_list|(
name|getUninterruptibly
argument_list|(
name|delegate
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|exception
parameter_list|)
block|{
name|forceSetException
argument_list|(
name|exception
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|cancellation
parameter_list|)
block|{
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|forceSetException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

