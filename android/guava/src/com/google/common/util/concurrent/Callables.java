begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Supplier
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to the {@link Callable} interface.  *  * @author Isaac Shum  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Callables
specifier|public
specifier|final
class|class
name|Callables
block|{
DECL|method|Callables ()
specifier|private
name|Callables
parameter_list|()
block|{}
comment|/** Creates a {@code Callable} which immediately returns a preset value each time it is called. */
DECL|method|returning (@ullable final T value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|returning
parameter_list|(
annotation|@
name|Nullable
specifier|final
name|T
name|value
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|call
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
return|;
block|}
comment|/**    * Creates an {@link AsyncCallable} from a {@link Callable}.    *    *<p>The {@link AsyncCallable} returns the {@link ListenableFuture} resulting from {@link    * ListeningExecutorService#submit(Callable)}.    *    * @since 20.0    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|method|asAsyncCallable ( final Callable<T> callable, final ListeningExecutorService listeningExecutorService)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|AsyncCallable
argument_list|<
name|T
argument_list|>
name|asAsyncCallable
parameter_list|(
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
specifier|final
name|ListeningExecutorService
name|listeningExecutorService
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|listeningExecutorService
argument_list|)
expr_stmt|;
return|return
operator|new
name|AsyncCallable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|T
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|listeningExecutorService
operator|.
name|submit
argument_list|(
name|callable
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Wraps the given callable such that for the duration of {@link Callable#call} the thread that is    * running will have the given name.    *    *    * @param callable The callable to wrap    * @param nameSupplier The supplier of thread names, {@link Supplier#get get} will be called once    *     for each invocation of the wrapped callable.    */
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|threadRenaming ( final Callable<T> callable, final Supplier<String> nameSupplier)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|threadRenaming
parameter_list|(
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
specifier|final
name|Supplier
argument_list|<
name|String
argument_list|>
name|nameSupplier
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nameSupplier
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|callable
argument_list|)
expr_stmt|;
return|return
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Thread
name|currentThread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
name|String
name|oldName
init|=
name|currentThread
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|restoreName
init|=
name|trySetName
argument_list|(
name|nameSupplier
operator|.
name|get
argument_list|()
argument_list|,
name|currentThread
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|restoreName
condition|)
block|{
name|boolean
name|unused
init|=
name|trySetName
argument_list|(
name|oldName
argument_list|,
name|currentThread
argument_list|)
decl_stmt|;
block|}
block|}
block|}
block|}
return|;
block|}
comment|/**    * Wraps the given runnable such that for the duration of {@link Runnable#run} the thread that is    * running with have the given name.    *    *    * @param task The Runnable to wrap    * @param nameSupplier The supplier of thread names, {@link Supplier#get get} will be called once    *     for each invocation of the wrapped callable.    */
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|threadRenaming (final Runnable task, final Supplier<String> nameSupplier)
specifier|static
name|Runnable
name|threadRenaming
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|,
specifier|final
name|Supplier
argument_list|<
name|String
argument_list|>
name|nameSupplier
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|nameSupplier
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|task
argument_list|)
expr_stmt|;
return|return
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
name|Thread
name|currentThread
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
name|String
name|oldName
init|=
name|currentThread
operator|.
name|getName
argument_list|()
decl_stmt|;
name|boolean
name|restoreName
init|=
name|trySetName
argument_list|(
name|nameSupplier
operator|.
name|get
argument_list|()
argument_list|,
name|currentThread
argument_list|)
decl_stmt|;
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|restoreName
condition|)
block|{
name|boolean
name|unused
init|=
name|trySetName
argument_list|(
name|oldName
argument_list|,
name|currentThread
argument_list|)
decl_stmt|;
block|}
block|}
block|}
block|}
return|;
block|}
comment|/** Tries to set name of the given {@link Thread}, returns true if successful. */
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|trySetName (final String threadName, Thread currentThread)
specifier|private
specifier|static
name|boolean
name|trySetName
parameter_list|(
specifier|final
name|String
name|threadName
parameter_list|,
name|Thread
name|currentThread
parameter_list|)
block|{
comment|// In AppEngine, this will always fail. Should we test for that explicitly using
comment|// MoreExecutors.isAppEngine? More generally, is there a way to see if we have the modifyThread
comment|// permission without catching an exception?
try|try
block|{
name|currentThread
operator|.
name|setName
argument_list|(
name|threadName
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

