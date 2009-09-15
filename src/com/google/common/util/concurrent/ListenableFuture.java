begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  *<p>This interface defines a future that has listeners attached to it, which  * is useful for asynchronous workflows.  Each listener has an associated  * executor, and is invoked using this executor once the {@code Future}'s  * computation is {@linkplain Future#isDone() complete}.  The listener will be  * executed even if it is added after the computation is complete.  *  *<p>Usage:  *<pre>   {@code  *   final ListenableFuture<?> future = myService.async(myRequest);  *   future.addListener(new Runnable() {  *     public void run() {  *       System.out.println("Operation Complete.");  *       try {  *         System.out.println("Result: " + future.get());  *       } catch (Exception e) {  *         System.out.println("Error: " + e.message());  *       }  *     }  *   }, exec);}</pre>  *  * @param<V> The type returned by {@link Future#get()}.  *  * @author Sven Mawson  * @author Nishant Thakkar  * @since 9.09.15<b>tentative</b>  */
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
comment|/**    *<p>Adds a listener and executor to the ListenableFuture.    * The listener will be {@linkplain Executor#execute(Runnable) passed    * to the executor} for execution when the {@code Future}'s computation is    * {@linkplain Future#isDone() complete}.    *    *<p>There is no guaranteed ordering of execution of listeners, they may get    * called in the order they were added and they may get called out of order,    * but any listener added through this method is guaranteed to be called once    * the computation is complete.    *    * @param listener the listener to run when the computation is complete.    * @param exec the executor to run the listener in.    * @throws NullPointerException if the executor or listener was null.    * @throws RejectedExecutionException if we tried to execute the listener    * immediately but the executor rejected it.    */
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
function_decl|;
block|}
end_interface

end_unit

