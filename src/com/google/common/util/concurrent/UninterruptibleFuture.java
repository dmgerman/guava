begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A {@code Future} whose {@code get} calls cannot be interrupted. If a thread  * is interrupted during such a call, the call continues to block until the  * result is available or the timeout elapses, and only then re-interrupts the  * thread. Obtain an instance of this type using {@link  * Futures#makeUninterruptible(Future)}.  *  * @author Kevin Bourrillion  * @since 1  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|UninterruptibleFuture
specifier|public
interface|interface
name|UninterruptibleFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|Future
argument_list|<
name|V
argument_list|>
block|{
DECL|method|get ()
annotation|@
name|Override
name|V
name|get
parameter_list|()
throws|throws
name|ExecutionException
function_decl|;
DECL|method|get (long timeout, TimeUnit unit)
annotation|@
name|Override
name|V
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|ExecutionException
throws|,
name|TimeoutException
function_decl|;
block|}
end_interface

end_unit

