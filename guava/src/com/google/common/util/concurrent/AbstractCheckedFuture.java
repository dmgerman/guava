begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|CancellationException
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
comment|/**  * A delegating wrapper around a {@link ListenableFuture} that adds support for the {@link  * #checkedGet()} and {@link #checkedGet(long, TimeUnit)} methods.  *  * @author Sven Mawson  * @since 1.0  * @deprecated {@link CheckedFuture} cannot properly support the chained operations that are the  *     primary goal of {@link ListenableFuture}. {@code CheckedFuture} also encourages users to  *     rethrow exceptions from one thread in another thread, producing misleading stack traces.  *     Additionally, it has a surprising policy about which exceptions to map and which to leave  *     untouched. Guava users who want a {@code CheckedFuture} can fork the classes for their own  *     use, possibly specializing them to the particular exception type they use. We recommend that  *     most people use {@code ListenableFuture} and perform any exception wrapping themselves. This  *     class is scheduled for removal from Guava in February 2018.  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|Deprecated
annotation|@
name|GwtIncompatible
DECL|class|AbstractCheckedFuture
specifier|public
specifier|abstract
class|class
name|AbstractCheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ForwardingListenableFuture
operator|.
name|SimpleForwardingListenableFuture
argument_list|<
name|V
argument_list|>
implements|implements
name|CheckedFuture
argument_list|<
name|V
argument_list|,
name|X
argument_list|>
block|{
comment|/** Constructs an {@code AbstractCheckedFuture} that wraps a delegate. */
DECL|method|AbstractCheckedFuture (ListenableFuture<V> delegate)
specifier|protected
name|AbstractCheckedFuture
parameter_list|(
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
comment|/**    * Translates from an {@link InterruptedException}, {@link CancellationException} or {@link    * ExecutionException} thrown by {@code get} to an exception of type {@code X} to be thrown by    * {@code checkedGet}. Subclasses must implement this method.    *    *<p>If {@code e} is an {@code InterruptedException}, the calling {@code checkedGet} method has    * already restored the interrupt after catching the exception. If an implementation of {@link    * #mapException(Exception)} wishes to swallow the interrupt, it can do so by calling {@link    * Thread#interrupted()}.    *    *<p>Subclasses may choose to throw, rather than return, a subclass of {@code RuntimeException}    * to allow creating a CheckedFuture that throws both checked and unchecked exceptions.    */
comment|// We might like @ForOverride here, but some subclasses invoke this from their get() methods.
DECL|method|mapException (Exception e)
specifier|protected
specifier|abstract
name|X
name|mapException
parameter_list|(
name|Exception
name|e
parameter_list|)
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>This implementation calls {@link #get()} and maps that method's standard exceptions to    * instances of type {@code X} using {@link #mapException}.    *    *<p>In addition, if {@code get} throws an {@link InterruptedException}, this implementation will    * set the current thread's interrupt status before calling {@code mapException}.    *    * @throws X if {@link #get()} throws an {@link InterruptedException}, {@link    *     CancellationException}, or {@link ExecutionException}    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|checkedGet ()
specifier|public
name|V
name|checkedGet
parameter_list|()
throws|throws
name|X
block|{
try|try
block|{
return|return
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|mapException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CancellationException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|mapException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * {@inheritDoc}    *    *<p>This implementation calls {@link #get(long, TimeUnit)} and maps that method's standard    * exceptions (excluding {@link TimeoutException}, which is propagated) to instances of type    * {@code X} using {@link #mapException}.    *    *<p>In addition, if {@code get} throws an {@link InterruptedException}, this implementation will    * set the current thread's interrupt status before calling {@code mapException}.    *    * @throws X if {@link #get()} throws an {@link InterruptedException}, {@link    *     CancellationException}, or {@link ExecutionException}    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|checkedGet (long timeout, TimeUnit unit)
specifier|public
name|V
name|checkedGet
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|TimeoutException
throws|,
name|X
block|{
try|try
block|{
return|return
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|mapException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CancellationException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
throw|throw
name|mapException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

