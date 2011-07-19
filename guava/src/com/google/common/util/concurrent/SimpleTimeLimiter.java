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
name|base
operator|.
name|Throwables
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

begin_comment
comment|/**  * A TimeLimiter that runs method calls in the background using an  * {@link ExecutorService}.  If the time limit expires for a given method call,  * the thread running the call will be interrupted.  *  * @author Kevin Bourrillion  * @since Guava release 01  */
end_comment

begin_class
annotation|@
name|Beta
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
comment|/**    * Constructs a TimeLimiter instance using the given executor service to    * execute proxied method calls.    *<p>    *<b>Warning:</b> using a bounded executor    * may be counterproductive!  If the thread pool fills up, any time callers    * spend waiting for a thread may count toward their time limit, and in    * this case the call may even time out before the target method is ever    * invoked.    *    * @param executor the ExecutorService that will execute the method calls on    *     the target objects; for example, a {@link    *     Executors#newCachedThreadPool()}.    */
DECL|method|SimpleTimeLimiter (ExecutorService executor)
specifier|public
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
comment|/**    * Constructs a TimeLimiter instance using a {@link    * Executors#newCachedThreadPool()} to execute proxied method calls.    *    *<p><b>Warning:</b> using a bounded executor may be counterproductive! If    * the thread pool fills up, any time callers spend waiting for a thread may    * count toward their time limit, and in this case the call may even time out    * before the target method is ever invoked.    */
DECL|method|SimpleTimeLimiter ()
specifier|public
name|SimpleTimeLimiter
parameter_list|()
block|{
name|this
argument_list|(
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newProxy (final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
specifier|final
name|T
name|target
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
specifier|final
name|long
name|timeoutDuration
parameter_list|,
specifier|final
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
name|checkArgument
argument_list|(
name|timeoutDuration
operator|>
literal|0
argument_list|,
literal|"bad timeout: "
operator|+
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
specifier|final
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
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|obj
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
specifier|final
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|Callable
argument_list|<
name|Object
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
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
name|Throwables
operator|.
name|throwCause
argument_list|(
name|e
argument_list|,
literal|false
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"can't get here"
argument_list|)
throw|;
block|}
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
comment|// TODO: should this actually throw only ExecutionException?
annotation|@
name|Override
DECL|method|callWithTimeout (Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|callWithTimeout
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
name|long
name|timeoutDuration
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|,
name|boolean
name|amInterruptible
parameter_list|)
throws|throws
name|Exception
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|timeoutUnit
argument_list|)
expr_stmt|;
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
name|Throwables
operator|.
name|throwCause
argument_list|(
name|e
argument_list|,
literal|true
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
block|}
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
comment|// TODO: replace with version in common.reflect if and when it's open-sourced
DECL|method|newProxy ( Class<T> interfaceType, InvocationHandler handler)
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

unit|}
end_unit

