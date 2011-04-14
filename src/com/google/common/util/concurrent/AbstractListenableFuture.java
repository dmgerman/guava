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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_comment
comment|/**  *<p>An abstract base implementation of the listener support provided by  * {@link ListenableFuture}. This class uses an {@link ExecutionList} to  * guarantee that all registered listeners will be executed. Listener/Executor  * pairs are stored in the execution list and executed in the order in which  * they were added, but because of thread scheduling issues there is no  * guarantee that the JVM will execute them in order. In addition, listeners  * added after the task is complete will be executed immediately, even if some  * previously added listeners have not yet been executed.  *   *<p>This class uses the {@link AbstractFuture} class to implement the  * {@code ListenableFuture} interface and simply delegates the  * {@link #addListener(Runnable, Executor)} and {@link #done()} methods to it.  *   * @author Sven Mawson  * @since Guava release 01  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AbstractListenableFuture
specifier|public
specifier|abstract
class|class
name|AbstractListenableFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|AbstractFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|// The execution list to hold our executors.
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
comment|/*    * Adds a listener/executor pair to execution list to execute when this task    * is completed.    */
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
block|}
comment|/*    * Override the done method to execute the execution list.    */
annotation|@
name|Override
DECL|method|done ()
specifier|protected
name|void
name|done
parameter_list|()
block|{
name|executionList
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

