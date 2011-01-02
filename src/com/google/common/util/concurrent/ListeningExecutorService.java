begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2010 Google Inc. All Rights Reserved.
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
name|Collection
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

begin_comment
comment|/**  * An {@link ExecutorService} that returns {@link ListenableFuture} instances.  * To create an instance from an existing {@link ExecutorService}, call  * {@link MoreExecutors#listeningDecorator(ExecutorService)}.  *  * @author Chris Povirk  * @since 8  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|ListeningExecutorService
specifier|public
interface|interface
name|ListeningExecutorService
extends|extends
name|ExecutorService
block|{
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the    *         task    */
comment|// @Override
DECL|method|submit (Callable<T> task)
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
function_decl|;
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the    *         task    */
comment|// @Override
DECL|method|submit (Runnable task)
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
function_decl|;
comment|/**    * @return a {@code ListenableFuture} representing pending completion of the    *         task    */
comment|// @Override
DECL|method|submit (Runnable task, T result)
parameter_list|<
name|T
parameter_list|>
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|,
name|T
name|result
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>    * Though the return type does not express this, all elements in the returned    * list must be {@link ListenableFuture} instances.    *    * @return A list of {@code ListenableFuture} instances representing the    *         tasks, in the same sequential order as produced by the iterator for    *         the given task list, each of which has completed.    */
comment|// @Override
DECL|method|invokeAll (Collection<? extends Callable<T>> tasks)
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|tasks
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>    * Though the return type does not express this, all elements in the returned    * list must be {@link ListenableFuture} instances.    *    * @return a list of {@code ListenableFuture} instances representing the    *         tasks, in the same sequential order as produced by the iterator for    *         the given task list. If the operation did not time out, each task    *         will have completed. If it did time out, some of these tasks will    *         not have completed.    */
comment|// @Override
DECL|method|invokeAll ( Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|Future
argument_list|<
name|T
argument_list|>
argument_list|>
name|invokeAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|>
name|tasks
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
block|}
end_interface

end_unit

