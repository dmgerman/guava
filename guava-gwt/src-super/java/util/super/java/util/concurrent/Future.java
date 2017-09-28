begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.util.concurrent
package|package
name|java
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_comment
comment|/**  * Emulation of Future. Since GWT environment is single threaded, attempting to block on the future  * by calling {@link #get()} or {@link #get(long, TimeUnit)} when the it is not yet done is  * considered illegal because it would lead to a deadlock. Future implementations must throw {@link  * IllegalStateException} to avoid a deadlock.  *  * @param<V> value type returned by the future.  */
end_comment

begin_interface
DECL|interface|Future
specifier|public
interface|interface
name|Future
parameter_list|<
name|V
parameter_list|>
block|{
DECL|method|cancel (boolean mayInterruptIfRunning)
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
function_decl|;
DECL|method|isCancelled ()
name|boolean
name|isCancelled
parameter_list|()
function_decl|;
DECL|method|isDone ()
name|boolean
name|isDone
parameter_list|()
function_decl|;
comment|// Even though the 'get' methods below are blocking, they are the only built-in APIs to get the
comment|// result of the {@code Future}, hence they are not removed. The implementation must throw {@link
comment|// IllegalStateException} if the {@code Future} is not done yet (see the class javadoc).
DECL|method|get ()
name|V
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
function_decl|;
DECL|method|get (long timeout, TimeUnit unit)
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
name|InterruptedException
throws|,
name|ExecutionException
throws|,
name|TimeoutException
function_decl|;
block|}
end_interface

end_unit

