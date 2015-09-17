begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtCompatible
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

begin_comment
comment|/**  * Provides a backup {@code Future} to replace an earlier failed {@code Future}. An implementation  * of this interface can be applied to an input {@code Future} with {@link Futures#withFallback}.  *  * @param<V> the result type of the provided backup {@code Future}  *  * @author Bruno Diniz  * @since 14.0  * @deprecated This interface's main user, {@link Futures#withFallback(ListenableFuture,  *     FutureFallback) Futures.withFallback}, has been replaced with {@link Futures#catchingAsync  *     catchingAsync}, which uses {@link AsyncFunction} instead of {@code FutureFallback}. We  *     recommend that other APIs be updated in the same way. This interface will be removed in Guava  *     release 20.0.  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|Deprecated
annotation|@
name|GwtCompatible
DECL|interface|FutureFallback
specifier|public
interface|interface
name|FutureFallback
parameter_list|<
name|V
parameter_list|>
block|{
comment|/**    * Returns a {@code Future} to be used in place of the {@code Future} that failed with the given    * exception. The exception is provided so that the {@code Fallback} implementation can    * conditionally determine whether to propagate the exception or to attempt to recover.    *    * @param t the exception that made the future fail. If the future's {@link Future#get() get}    *     method throws an {@link ExecutionException}, then the cause is passed to this method. Any    *     other thrown object is passed unaltered.    */
DECL|method|create (Throwable t)
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|create
parameter_list|(
name|Throwable
name|t
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

