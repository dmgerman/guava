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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Produces proxies that impose a time limit on method calls to the proxied object. For example, to  * return the value of {@code target.someMethod()}, but substitute {@code DEFAULT_VALUE} if this  * method call takes over 50 ms, you can use this code:  *  *<pre>  *   TimeLimiter limiter = . . .;  *   TargetType proxy = limiter.newProxy(  *       target, TargetType.class, 50, TimeUnit.MILLISECONDS);  *   try {  *     return proxy.someMethod();  *   } catch (UncheckedTimeoutException e) {  *     return DEFAULT_VALUE;  *   }  *</pre>  *  *<p>Please see {@code SimpleTimeLimiterTest} for more usage examples.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|interface|TimeLimiter
specifier|public
interface|interface
name|TimeLimiter
block|{
comment|/**    * Returns an instance of {@code interfaceType} that delegates all method calls to the    * {@code target} object, enforcing the specified time limit on each call. This time-limited    * delegation is also performed for calls to {@link Object#equals}, {@link Object#hashCode}, and    * {@link Object#toString}.    *    *<p>If the target method call finishes before the limit is reached, the return value or    * exception is propagated to the caller exactly as-is. If, on the other hand, the time limit is    * reached, the proxy will attempt to abort the call to the target, and will throw an    * {@link UncheckedTimeoutException} to the caller.    *    *<p>It is important to note that the primary purpose of the proxy object is to return control to    * the caller when the timeout elapses; aborting the target method call is of secondary concern.    * The particular nature and strength of the guarantees made by the proxy is    * implementation-dependent. However, it is important that each of the methods on the target    * object behaves appropriately when its thread is interrupted.    *    * @param target the object to proxy    * @param interfaceType the interface you wish the returned proxy to implement    * @param timeoutDuration with timeoutUnit, the maximum length of time that callers are willing to    *     wait on each method call to the proxy    * @param timeoutUnit with timeoutDuration, the maximum length of time that callers are willing to    *     wait on each method call to the proxy    * @return a time-limiting proxy    * @throws IllegalArgumentException if {@code interfaceType} is a regular class, enum, or    *     annotation type, rather than an interface    */
DECL|method|newProxy (T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit)
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
function_decl|;
comment|/**    * Invokes a specified Callable, timing out after the specified time limit. If the target method    * call finished before the limit is reached, the return value or exception is propagated to the    * caller exactly as-is. If, on the other hand, the time limit is reached, we attempt to abort the    * call to the target, and throw an {@link UncheckedTimeoutException} to the caller.    *    * @param callable the Callable to execute    * @param timeoutDuration with timeoutUnit, the maximum length of time to wait    * @param timeoutUnit with timeoutDuration, the maximum length of time to wait    * @param interruptible whether to respond to thread interruption by aborting the operation and    *     throwing InterruptedException; if false, the operation is allowed to complete or time out,    *     and the current thread's interrupt status is re-asserted.    * @return the result returned by the Callable    * @throws InterruptedException if {@code interruptible} is true and our thread is interrupted    *     during execution    * @throws UncheckedTimeoutException if the time limit is reached    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|callWithTimeout ( Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean interruptible)
argument_list|<
name|T
argument_list|>
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
name|interruptible
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

