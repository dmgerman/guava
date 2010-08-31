begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A {@code CheckedFuture} is an extension of {@link Future} that includes  * versions of the {@code get} methods that can throw a checked exception and  * allows listeners to be attached to the future.  This makes it easier to  * create a future that executes logic which can throw an exception.  *  *<p>Implementations of this interface must adapt the exceptions thrown by  * {@code Future#get()}: {@link CancellationException},  * {@link ExecutionException} and {@link InterruptedException} into the type  * specified by the {@code E} type parameter.  *  *<p>This interface also extends the ListenableFuture interface to allow  * listeners to be added. This allows the future to be used as a normal  * {@link Future} or as an asynchronous callback mechanism as needed. This  * allows multiple callbacks to be registered for a particular task, and the  * future will guarantee execution of all listeners when the task completes.  *  * @author Sven Mawson  * @since 1  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|CheckedFuture
specifier|public
interface|interface
name|CheckedFuture
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
extends|extends
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Exception checking version of {@link Future#get()} that will translate    * {@link InterruptedException}, {@link CancellationException} and    * {@link ExecutionException} into application-specific exceptions.    *    * @return the result of executing the future.    * @throws X on interruption, cancellation or execution exceptions.    */
DECL|method|checkedGet ()
name|V
name|checkedGet
parameter_list|()
throws|throws
name|X
function_decl|;
comment|/**    * Exception checking version of {@link Future#get(long, TimeUnit)} that will    * translate {@link InterruptedException}, {@link CancellationException} and    * {@link ExecutionException} into application-specific exceptions.  On    * timeout this method throws a normal {@link TimeoutException}.    *    * @return the result of executing the future.    * @throws TimeoutException if retrieving the result timed out.    * @throws X on interruption, cancellation or execution exceptions.    */
DECL|method|checkedGet (long timeout, TimeUnit unit)
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
function_decl|;
block|}
end_interface

end_unit

