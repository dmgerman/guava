begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|elemental2
operator|.
name|promise
operator|.
name|IThenable
import|;
end_import

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsPackage
import|;
end_import

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsType
import|;
end_import

begin_comment
comment|/**  * Subset of the elemental2 IThenable interface without the single-parameter overload, which allows  * us to implement it using a default implementation in J2cl ListenableFuture.  */
end_comment

begin_interface
annotation|@
name|JsType
argument_list|(
name|isNative
operator|=
literal|true
argument_list|,
name|namespace
operator|=
name|JsPackage
operator|.
name|GLOBAL
argument_list|)
DECL|interface|Thenable
interface|interface
name|Thenable
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|then ( IThenable.ThenOnFulfilledCallbackFn<? super T, ? extends V> onFulfilled, IThenable.ThenOnRejectedCallbackFn<? extends V> onRejected)
parameter_list|<
name|V
parameter_list|>
name|IThenable
argument_list|<
name|V
argument_list|>
name|then
parameter_list|(
name|IThenable
operator|.
name|ThenOnFulfilledCallbackFn
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|onFulfilled
parameter_list|,
name|IThenable
operator|.
name|ThenOnRejectedCallbackFn
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|onRejected
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

