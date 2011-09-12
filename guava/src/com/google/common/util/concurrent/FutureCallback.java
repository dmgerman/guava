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
comment|/**  * A callback for accepting the results of a {@link java.util.concurrent.Future}  * computation asynchronously.  *  *<p>To attach to a {@link ListenableFuture} use {@link Futures#addCallback}.  *  * @author Anthony Zana  * @since 10.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|FutureCallback
specifier|public
interface|interface
name|FutureCallback
parameter_list|<
name|V
parameter_list|>
block|{
comment|/**    * Invoked with the result of the {@code Future} computation when it is    * successful.    */
DECL|method|onSuccess (V result)
name|void
name|onSuccess
parameter_list|(
name|V
name|result
parameter_list|)
function_decl|;
comment|/**    * Invoked when a {@code Future} computation fails or is canceled.    *    *<p>If the future's {@link Future#get() get} method throws an {@link    * ExecutionException}, then the cause is passed to this method. Any other    * thrown object is passed unaltered.    */
DECL|method|onFailure (Throwable t)
name|void
name|onFailure
parameter_list|(
name|Throwable
name|t
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

