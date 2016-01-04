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
name|TimeUnit
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
comment|/**  * Implementations of {@code Futures.immediate*}.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ImmediateFuture
specifier|abstract
class|class
name|ImmediateFuture
parameter_list|<
name|V
parameter_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/*    * TODO(lukes): Use AbstractFuture.TrustedFuture instead of special classes so that get() throws    * InterruptedException when appropriate, and, more importantly for failed/cancelled Futures, we    * can take advantage of the TrustedFuture optimizations.    */
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
name|ImmediateFuture
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
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
name|checkNotNull
argument_list|(
name|listener
argument_list|,
literal|"Runnable was null."
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|executor
argument_list|,
literal|"Executor was null."
argument_list|)
expr_stmt|;
try|try
block|{
name|executor
operator|.
name|execute
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// ListenableFuture's contract is that it will not throw unchecked exceptions, so log the bad
comment|// runnable and/or executor and swallow it.
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
name|listener
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
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
specifier|abstract
name|V
name|get
parameter_list|()
throws|throws
name|ExecutionException
function_decl|;
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
name|ExecutionException
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
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
literal|false
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
literal|true
return|;
block|}
DECL|class|ImmediateSuccessfulFuture
specifier|static
class|class
name|ImmediateSuccessfulFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmediateFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|NULL
specifier|static
specifier|final
name|ImmediateSuccessfulFuture
argument_list|<
name|Object
argument_list|>
name|NULL
init|=
operator|new
name|ImmediateSuccessfulFuture
argument_list|<
name|Object
argument_list|>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
DECL|field|value
annotation|@
name|Nullable
specifier|private
specifier|final
name|V
name|value
decl_stmt|;
DECL|method|ImmediateSuccessfulFuture (@ullable V value)
name|ImmediateSuccessfulFuture
parameter_list|(
annotation|@
name|Nullable
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
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|class|ImmediateSuccessfulCheckedFuture
specifier|static
class|class
name|ImmediateSuccessfulCheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ImmediateFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
block|{
DECL|field|value
annotation|@
name|Nullable
specifier|private
specifier|final
name|V
name|value
decl_stmt|;
DECL|method|ImmediateSuccessfulCheckedFuture (@ullable V value)
name|ImmediateSuccessfulCheckedFuture
parameter_list|(
annotation|@
name|Nullable
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
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|checkedGet ()
specifier|public
name|V
name|checkedGet
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|checkedGet (long timeout, TimeUnit unit)
specifier|public
name|V
name|checkedGet
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|unit
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
DECL|class|ImmediateFailedFuture
specifier|static
class|class
name|ImmediateFailedFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmediateFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|thrown
specifier|private
specifier|final
name|Throwable
name|thrown
decl_stmt|;
DECL|method|ImmediateFailedFuture (Throwable thrown)
name|ImmediateFailedFuture
parameter_list|(
name|Throwable
name|thrown
parameter_list|)
block|{
name|this
operator|.
name|thrown
operator|=
name|thrown
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|thrown
argument_list|)
throw|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|class|ImmediateCancelledFuture
specifier|static
class|class
name|ImmediateCancelledFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmediateFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|thrown
specifier|private
specifier|final
name|CancellationException
name|thrown
decl_stmt|;
DECL|method|ImmediateCancelledFuture ()
name|ImmediateCancelledFuture
parameter_list|()
block|{
name|this
operator|.
name|thrown
operator|=
operator|new
name|CancellationException
argument_list|(
literal|"Immediate cancelled future."
argument_list|)
expr_stmt|;
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
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
block|{
throw|throw
name|AbstractFuture
operator|.
name|cancellationExceptionWithCause
argument_list|(
literal|"Task was cancelled."
argument_list|,
name|thrown
argument_list|)
throw|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// TODO
DECL|class|ImmediateFailedCheckedFuture
specifier|static
class|class
name|ImmediateFailedCheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ImmediateFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
block|{
DECL|field|thrown
specifier|private
specifier|final
name|X
name|thrown
decl_stmt|;
DECL|method|ImmediateFailedCheckedFuture (X thrown)
name|ImmediateFailedCheckedFuture
parameter_list|(
name|X
name|thrown
parameter_list|)
block|{
name|this
operator|.
name|thrown
operator|=
name|thrown
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|V
name|get
parameter_list|()
throws|throws
name|ExecutionException
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|thrown
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|checkedGet ()
specifier|public
name|V
name|checkedGet
parameter_list|()
throws|throws
name|X
block|{
throw|throw
name|thrown
throw|;
block|}
annotation|@
name|Override
DECL|method|checkedGet (long timeout, TimeUnit unit)
specifier|public
name|V
name|checkedGet
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|X
block|{
name|checkNotNull
argument_list|(
name|unit
argument_list|)
expr_stmt|;
throw|throw
name|thrown
throw|;
block|}
block|}
block|}
end_class

end_unit

