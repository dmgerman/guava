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
name|checkArgument
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
name|ObjectArrays
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|Callable
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
name|ExecutorService
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * A TimeLimiter that runs method calls in the background using an {@link ExecutorService}. If the  * time limit expires for a given method call, the thread running the call will be interrupted.  *  * @author Kevin Bourrillion  * @author Jens Nyman  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|SimpleTimeLimiter
specifier|public
specifier|final
class|class
name|SimpleTimeLimiter
implements|implements
name|TimeLimiter
block|{
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
DECL|method|SimpleTimeLimiter (ExecutorService executor)
specifier|private
name|SimpleTimeLimiter
parameter_list|(
name|ExecutorService
name|executor
parameter_list|)
block|{
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
comment|/**    * Creates a TimeLimiter instance using the given executor service to execute method calls.    *    *<p><b>Warning:</b> using a bounded executor may be counterproductive! If the thread pool fills    * up, any time callers spend waiting for a thread may count toward their time limit, and in this    * case the call may even time out before the target method is ever invoked.    *    * @param executor the ExecutorService that will execute the method calls on the target objects;    *     for example, a {@link Executors#newCachedThreadPool()}.    * @since 22.0    */
DECL|method|create (ExecutorService executor)
specifier|public
specifier|static
name|SimpleTimeLimiter
name|create
parameter_list|(
name|ExecutorService
name|executor
parameter_list|)
block|{
return|return
operator|new
name|SimpleTimeLimiter
argument_list|(
name|executor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newProxy ( T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|T
name|target
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|interfaceType
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|interfaceType
operator|.
name|isInterface
argument_list|()
argument_list|,
literal|"interfaceType must be an interface type"
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Method
argument_list|>
name|interruptibleMethods
init|=
name|findInterruptibleMethods
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|InvocationHandler
name|handler
init|=
operator|new
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|CheckForNull
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Method
name|method
parameter_list|,
annotation|@
name|CheckForNull
annotation|@
name|Nullable
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|Callable
argument_list|<
annotation|@
name|Nullable
name|Object
argument_list|>
name|callable
init|=
parameter_list|()
lambda|->
block|{
try|try
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|target
argument_list|,
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|throwCause
argument_list|(
name|e
argument_list|,
literal|false
comment|/* combineStackTraces */
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
return|return
name|callWithTimeout
argument_list|(
name|callable
argument_list|,
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|,
name|interruptibleMethods
operator|.
name|contains
argument_list|(
name|method
argument_list|)
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|newProxy
argument_list|(
name|interfaceType
argument_list|,
name|handler
argument_list|)
return|;
block|}
comment|// TODO: replace with version in common.reflect if and when it's open-sourced
DECL|method|newProxy (Class<T> interfaceType, InvocationHandler handler)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|InvocationHandler
name|handler
parameter_list|)
block|{
name|Object
name|object
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|interfaceType
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|interfaceType
block|}
operator|,
name|handler
block|)
function|;
return|return
name|interfaceType
operator|.
name|cast
argument_list|(
name|object
argument_list|)
return|;
block|}
end_class

begin_expr_stmt
DECL|method|callWithTimeout ( Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
specifier|private
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
name|callWithTimeout
argument_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
argument_list|,
name|long
name|timeoutDuration
argument_list|,
name|TimeUnit
name|timeoutUnit
argument_list|,
name|boolean
name|amInterruptible
argument_list|)
throws|throws
name|Exception
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|Future
argument_list|<
name|T
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|callable
argument_list|)
decl_stmt|;
end_decl_stmt

begin_try
try|try
block|{
if|if
condition|(
name|amInterruptible
condition|)
block|{
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
else|else
block|{
return|return
name|Uninterruptibles
operator|.
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|throwCause
argument_list|(
name|e
argument_list|,
literal|true
comment|/* combineStackTraces */
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UncheckedTimeoutException
argument_list|(
name|e
argument_list|)
throw|;
block|}
end_try

begin_expr_stmt
unit|}    @
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|callWithTimeout ( Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
name|callWithTimeout
argument_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
argument_list|,
name|long
name|timeoutDuration
argument_list|,
name|TimeUnit
name|timeoutUnit
argument_list|)
throws|throws
name|TimeoutException
throws|,
name|InterruptedException
throws|,
name|ExecutionException
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|Future
argument_list|<
name|T
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|callable
argument_list|)
decl_stmt|;
end_decl_stmt

begin_try
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|TimeoutException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
comment|/* mayInterruptIfRunning */
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowExecutionExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
end_try

begin_expr_stmt
unit|}    @
name|CanIgnoreReturnValue
expr|@
name|Override
DECL|method|callUninterruptiblyWithTimeout ( Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
name|T
name|callUninterruptiblyWithTimeout
argument_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
argument_list|,
name|long
name|timeoutDuration
argument_list|,
name|TimeUnit
name|timeoutUnit
argument_list|)
throws|throws
name|TimeoutException
throws|,
name|ExecutionException
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|Future
argument_list|<
name|T
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|callable
argument_list|)
decl_stmt|;
end_decl_stmt

begin_try
try|try
block|{
return|return
name|Uninterruptibles
operator|.
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
comment|/* mayInterruptIfRunning */
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowExecutionExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
end_try

begin_function
unit|}    @
name|Override
DECL|method|runWithTimeout (Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
name|void
name|runWithTimeout
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|)
throws|throws
name|TimeoutException
throws|,
name|InterruptedException
block|{
name|checkNotNull
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|(
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|TimeoutException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
comment|/* mayInterruptIfRunning */
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowRuntimeExecutionExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|runUninterruptiblyWithTimeout ( Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit)
specifier|public
name|void
name|runUninterruptiblyWithTimeout
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|)
throws|throws
name|TimeoutException
block|{
name|checkNotNull
argument_list|(
name|runnable
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
name|checkPositiveTimeout
argument_list|(
name|timeoutDuration
argument_list|)
expr_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
try|try
block|{
name|Uninterruptibles
operator|.
name|getUninterruptibly
argument_list|(
name|future
argument_list|,
name|timeoutDuration
argument_list|,
name|timeoutUnit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
comment|/* mayInterruptIfRunning */
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowRuntimeExecutionExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
end_function

begin_function
DECL|method|throwCause (Exception e, boolean combineStackTraces)
specifier|private
specifier|static
name|Exception
name|throwCause
parameter_list|(
name|Exception
name|e
parameter_list|,
name|boolean
name|combineStackTraces
parameter_list|)
throws|throws
name|Exception
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|==
literal|null
condition|)
block|{
throw|throw
name|e
throw|;
block|}
if|if
condition|(
name|combineStackTraces
condition|)
block|{
name|StackTraceElement
index|[]
name|combined
init|=
name|ObjectArrays
operator|.
name|concat
argument_list|(
name|cause
operator|.
name|getStackTrace
argument_list|()
argument_list|,
name|e
operator|.
name|getStackTrace
argument_list|()
argument_list|,
name|StackTraceElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|cause
operator|.
name|setStackTrace
argument_list|(
name|combined
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cause
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|cause
throw|;
block|}
if|if
condition|(
name|cause
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|(
name|Error
operator|)
name|cause
throw|;
block|}
comment|// The cause is a weird kind of Throwable, so throw the outer exception.
throw|throw
name|e
throw|;
block|}
end_function

begin_function
DECL|method|findInterruptibleMethods (Class<?> interfaceType)
specifier|private
specifier|static
name|Set
argument_list|<
name|Method
argument_list|>
name|findInterruptibleMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|interfaceType
parameter_list|)
block|{
name|Set
argument_list|<
name|Method
argument_list|>
name|set
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|m
range|:
name|interfaceType
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|declaresInterruptedEx
argument_list|(
name|m
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
return|;
block|}
end_function

begin_function
DECL|method|declaresInterruptedEx (Method method)
specifier|private
specifier|static
name|boolean
name|declaresInterruptedEx
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|exType
range|:
name|method
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
comment|// debate: == or isAssignableFrom?
if|if
condition|(
name|exType
operator|==
name|InterruptedException
operator|.
name|class
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
end_function

begin_function
DECL|method|wrapAndThrowExecutionExceptionOrError (Throwable cause)
specifier|private
name|void
name|wrapAndThrowExecutionExceptionOrError
parameter_list|(
name|Throwable
name|cause
parameter_list|)
throws|throws
name|ExecutionException
block|{
if|if
condition|(
name|cause
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|new
name|ExecutionError
argument_list|(
operator|(
name|Error
operator|)
name|cause
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|cause
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|new
name|UncheckedExecutionException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
else|else
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
end_function

begin_function
DECL|method|wrapAndThrowRuntimeExecutionExceptionOrError (Throwable cause)
specifier|private
name|void
name|wrapAndThrowRuntimeExecutionExceptionOrError
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
if|if
condition|(
name|cause
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|new
name|ExecutionError
argument_list|(
operator|(
name|Error
operator|)
name|cause
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|UncheckedExecutionException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
block|}
end_function

begin_function
DECL|method|checkPositiveTimeout (long timeoutDuration)
specifier|private
specifier|static
name|void
name|checkPositiveTimeout
parameter_list|(
name|long
name|timeoutDuration
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|timeoutDuration
operator|>
literal|0
argument_list|,
literal|"timeout must be positive: %s"
argument_list|,
name|timeoutDuration
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

