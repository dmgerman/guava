begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FutureTask
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
comment|/**  * A {@link FutureTask} that also implements the {@link ListenableFuture}  * interface.  Unlike {@code FutureTask}, {@code ListenableFutureTask} does not  * provide an overrideable {@link FutureTask#done() done()} method.  For similar  * functionality, call {@link #addListener}.  *   *<p>  *  * @author Sven Mawson  * @since 1.0  */
end_comment

begin_class
DECL|class|ListenableFutureTask
specifier|public
specifier|final
class|class
name|ListenableFutureTask
parameter_list|<
name|V
parameter_list|>
extends|extends
name|FutureTask
argument_list|<
name|V
argument_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|// The execution list to hold our listeners.
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
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the    * given {@code Callable}.    *    * @param callable the callable task    * @since 10.0    */
DECL|method|create (Callable<V> callable)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
return|return
operator|new
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|callable
argument_list|)
return|;
block|}
comment|/**    * Creates a {@code ListenableFutureTask} that will upon running, execute the    * given {@code Runnable}, and arrange that {@code get} will return the    * given result on successful completion.    *    * @param runnable the runnable task    * @param result the result to return on successful completion. If you don't    *     need a particular result, consider using constructions of the form:    *     {@code ListenableFuture<?> f = ListenableFutureTask.create(runnable,    *     null)}    * @since 10.0    */
DECL|method|create ( Runnable runnable, @Nullable V result)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
annotation|@
name|Nullable
name|V
name|result
parameter_list|)
block|{
return|return
operator|new
name|ListenableFutureTask
argument_list|<
name|V
argument_list|>
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
return|;
block|}
DECL|method|ListenableFutureTask (Callable<V> callable)
specifier|private
name|ListenableFutureTask
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|)
block|{
name|super
argument_list|(
name|callable
argument_list|)
expr_stmt|;
block|}
DECL|method|ListenableFutureTask (Runnable runnable, @Nullable V result)
specifier|private
name|ListenableFutureTask
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
annotation|@
name|Nullable
name|V
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
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
comment|/**    * Internal implementation detail used to invoke the listeners.    */
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
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

