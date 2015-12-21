begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Future
import|;
end_import

begin_comment
comment|/**  * Computes a value, possibly asynchronously. For an example usage and more information, see {@link  * Futures.FutureCombiner#callAsync(AsyncCallable)}.  *  *<p>Much like {@link java.util.concurrent.Callable}, but returning a {@link ListenableFuture}  * result.  *  * @since 20.0  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|interface|AsyncCallable
specifier|public
interface|interface
name|AsyncCallable
parameter_list|<
name|V
parameter_list|>
block|{
comment|/**    * Computes a result {@code Future}. The output {@code Future} need not be {@linkplain    * Future#isDone done}, making {@code AsyncCallable} suitable for asynchronous derivations.    *    *<p>Throwing an exception from this method is equivalent to returning a failing {@link    * ListenableFuture}.    */
DECL|method|call ()
name|ListenableFuture
argument_list|<
name|V
argument_list|>
name|call
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

