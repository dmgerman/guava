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
name|util
operator|.
name|concurrent
operator|.
name|Uninterruptibles
operator|.
name|getUninterruptibly
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
name|base
operator|.
name|Preconditions
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

begin_comment
comment|/**  * Static utility methods pertaining to the {@link Future} interface.  *  *<p>Many of these methods use the {@link ListenableFuture} API; consult the  * Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/ListenableFutureExplained">  * {@code ListenableFuture}</a>.  *  * @author Kevin Bourrillion  * @author Nishant Thakkar  * @author Sven Mawson  * @since 1.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Futures
specifier|public
specifier|final
class|class
name|Futures
block|{
DECL|method|Futures ()
specifier|private
name|Futures
parameter_list|()
block|{}
comment|/**    * Registers separate success and failure callbacks to be run when the {@code    * Future}'s computation is {@linkplain java.util.concurrent.Future#isDone()    * complete} or, if the computation is already complete, immediately.    *    *<p>The callback is run in {@code executor}.    * There is no guaranteed ordering of execution of callbacks, but any    * callback added through this method is guaranteed to be called once the    * computation is complete.    *    * Example:<pre> {@code    * ListenableFuture<QueryResult> future = ...;    * Executor e = ...    * addCallback(future,    *     new FutureCallback<QueryResult> {    *       public void onSuccess(QueryResult result) {    *         storeInCache(result);    *       }    *       public void onFailure(Throwable t) {    *         reportError(t);    *       }    *     }, e);}</pre>    *    *<p>When the callback is fast and lightweight, consider {@linkplain    * #addCallback(ListenableFuture, FutureCallback) omitting the executor} or    * explicitly specifying {@code directExecutor}. However, be aware of the    * caveats documented in the link above.    *    *<p>For a more general interface to attach a completion listener to a    * {@code Future}, see {@link ListenableFuture#addListener addListener}.    *    * @param future The future attach the callback to.    * @param callback The callback to invoke when {@code future} is completed.    * @param executor The executor to run {@code callback} when the future    *    completes.    * @since 10.0    */
DECL|method|addCallback (final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|void
name|addCallback
parameter_list|(
specifier|final
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
specifier|final
name|FutureCallback
argument_list|<
name|?
super|super
name|V
argument_list|>
name|callback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|Runnable
name|callbackListener
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
specifier|final
name|V
name|value
decl_stmt|;
try|try
block|{
comment|// TODO(user): (Before Guava release), validate that this
comment|// is the thing for IE.
name|value
operator|=
name|getUninterruptibly
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
name|callback
operator|.
name|onSuccess
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|future
operator|.
name|addListener
argument_list|(
name|callbackListener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
comment|/*    * TODO(user): FutureChecker interface for these to be static methods on? If    * so, refer to it in the (static-method) Futures.get documentation    */
comment|/*    * Arguably we don't need a timed getUnchecked because any operation slow    * enough to require a timeout is heavyweight enough to throw a checked    * exception and therefore be inappropriate to use with getUnchecked. Further,    * it's not clear that converting the checked TimeoutException to a    * RuntimeException -- especially to an UncheckedExecutionException, since it    * wasn't thrown by the computation -- makes sense, and if we don't convert    * it, the user still has to write a try-catch block.    *    * If you think you would use this method, let us know.    */
block|}
end_class

end_unit

