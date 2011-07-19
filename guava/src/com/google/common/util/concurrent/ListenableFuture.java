begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RejectedExecutionException
import|;
end_import

begin_comment
comment|/**  * A {@link Future} that accepts completion listeners.  Each listener has an  * associated executor, and is invoked using this executor once the future's  * computation is {@linkplain Future#isDone() complete}.  If the computation has  * already completed when the listener is added, the listener will execute  * immediately.  *  *<p>Common {@code ListenableFuture} implementations include {@link  * SettableFuture} and the futures returned by a {@link  * ListeningExecutorService} (typically {@link ListenableFutureTask}  * instances).  *  *<p>Usage:  *<pre>   {@code  *   final ListenableFuture<?> future = myService.async(myRequest);  *   future.addListener(new Runnable() {  *     public void run() {  *       System.out.println("Operation Complete.");  *       try {  *         System.out.println("Result: " + future.get());  *       } catch (Exception e) {  *         System.out.println("Error: " + e.message());  *       }  *     }  *   }, executor);}</pre>  *  * @author Sven Mawson  * @author Nishant Thakkar  * @since Guava release 01  */
end_comment

begin_interface
DECL|interface|ListenableFuture
specifier|public
interface|interface
name|ListenableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|Future
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Registers a listener to be {@linkplain Executor#execute(Runnable) run} on    * the given executor.  The listener will run when the {@code Future}'s    * computation is {@linkplain Future#isDone() complete} or, if the computation    * is already complete, immediately.    *    *<p>There is no guaranteed ordering of execution of listeners, but any    * listener added through this method is guaranteed to be called once the    * computation is complete.    *    *<p>Exceptions thrown by a listener will be propagated up to the executor.    * Any exception thrown during {@code Executor.execute} (e.g., a {@code    * RejectedExecutionException} or an exception thrown by {@linkplain    * MoreExecutors#sameThreadExecutor inline execution}) will be caught and    * logged.    *    *<p>Note: For fast, lightweight listeners that would be safe to execute in    * any thread, consider {@link MoreExecutors#sameThreadExecutor}. For heavier    * listeners, {@code sameThreadExecutor()} carries some caveats: First, the    * thread that the listener runs in depends on whether the {@code Future} is    * done at the time it is added. In particular, if added late, listeners will    * run in the thread that calls {@code addListener}. Second, listeners may    * run in an internal thread of the system responsible for the input {@code    * Future}, such as an RPC network thread. Finally, during the execution of a    * listener, the thread cannot submit any additional listeners for execution,    * even if those listeners are to run in other executors.    *    *<p>This is the most general listener interface.    * For common operations performed using listeners,    * see {@link com.google.common.util.concurrent.Futures}    *    * @param listener the listener to run when the computation is complete    * @param executor the executor to run the listener in    * @throws NullPointerException if the executor or listener was null    * @throws RejectedExecutionException if we tried to execute the listener    *         immediately but the executor rejected it.    */
DECL|method|addListener (Runnable listener, Executor executor)
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

