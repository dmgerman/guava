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
name|util
operator|.
name|concurrent
operator|.
name|AbstractFuture
operator|.
name|TrustedFuture
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
comment|/** Implementation of {@link Futures#immediateFuture}. */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_comment
comment|// TODO(cpovirk): Make this final (but that may break Mockito spy calls).
end_comment

begin_expr_stmt
DECL|class|ImmediateFuture
name|class
name|ImmediateFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|field|NULL
specifier|static
name|final
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|NULL
operator|=
operator|new
name|ImmediateFuture
argument_list|<
annotation|@
name|Nullable
name|Object
argument_list|>
argument_list|(
literal|null
argument_list|)
block|;
DECL|field|log
specifier|private
specifier|static
name|final
name|Logger
name|log
operator|=
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
block|;    @
DECL|field|value
name|ParametricNullness
specifier|private
name|final
name|V
name|value
block|;
DECL|method|ImmediateFuture (@arametricNullness V value)
name|ImmediateFuture
argument_list|(
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
block|;   }
expr|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
name|void
name|addListener
argument_list|(
name|Runnable
name|listener
argument_list|,
name|Executor
name|executor
argument_list|)
block|{
name|checkNotNull
argument_list|(
name|listener
argument_list|,
literal|"Runnable was null."
argument_list|)
block|;
name|checkNotNull
argument_list|(
name|executor
argument_list|,
literal|"Executor was null."
argument_list|)
block|;
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
end_expr_stmt

begin_function
unit|}    @
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
end_function

begin_comment
comment|// TODO(lukes): Consider throwing InterruptedException when appropriate.
end_comment

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
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
end_function

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
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
end_function

begin_function
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
end_function

begin_function
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
end_function

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// Behaviour analogous to AbstractFuture#toString().
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|"[status=SUCCESS, result=["
operator|+
name|value
operator|+
literal|"]]"
return|;
block|}
end_function

begin_expr_stmt
DECL|class|ImmediateFailedFuture
specifier|static
name|final
name|class
name|ImmediateFailedFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|TrustedFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|method|ImmediateFailedFuture (Throwable thrown)
name|ImmediateFailedFuture
argument_list|(
name|Throwable
name|thrown
argument_list|)
block|{
name|setException
argument_list|(
name|thrown
argument_list|)
block|;     }
block|}
DECL|class|ImmediateCancelledFuture
specifier|static
name|final
name|class
name|ImmediateCancelledFuture
operator|<
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|TrustedFuture
argument_list|<
name|V
argument_list|>
block|{
DECL|method|ImmediateCancelledFuture ()
name|ImmediateCancelledFuture
argument_list|()
block|{
name|cancel
argument_list|(
literal|false
argument_list|)
block|;     }
block|}
end_expr_stmt

unit|}
end_unit

