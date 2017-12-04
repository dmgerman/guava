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
name|collect
operator|.
name|Sets
operator|.
name|newHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Emulation of AggregateFutureState. */
end_comment

begin_class
DECL|class|AggregateFutureState
specifier|abstract
class|class
name|AggregateFutureState
block|{
comment|// Lazily initialized the first time we see an exception; not released until all the input futures
comment|//& this future completes. Released when the future releases the reference to the running state
DECL|field|seenExceptions
specifier|private
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seenExceptions
init|=
literal|null
decl_stmt|;
DECL|field|remaining
specifier|private
name|int
name|remaining
decl_stmt|;
DECL|method|AggregateFutureState (int remainingFutures)
name|AggregateFutureState
parameter_list|(
name|int
name|remainingFutures
parameter_list|)
block|{
name|this
operator|.
name|remaining
operator|=
name|remainingFutures
expr_stmt|;
block|}
DECL|method|getOrInitSeenExceptions ()
specifier|final
name|Set
argument_list|<
name|Throwable
argument_list|>
name|getOrInitSeenExceptions
parameter_list|()
block|{
if|if
condition|(
name|seenExceptions
operator|==
literal|null
condition|)
block|{
name|seenExceptions
operator|=
name|newHashSet
argument_list|()
expr_stmt|;
name|addInitialException
argument_list|(
name|seenExceptions
argument_list|)
expr_stmt|;
block|}
return|return
name|seenExceptions
return|;
block|}
DECL|method|addInitialException (Set<Throwable> seen)
specifier|abstract
name|void
name|addInitialException
parameter_list|(
name|Set
argument_list|<
name|Throwable
argument_list|>
name|seen
parameter_list|)
function_decl|;
DECL|method|decrementRemainingAndGet ()
specifier|final
name|int
name|decrementRemainingAndGet
parameter_list|()
block|{
return|return
operator|--
name|remaining
return|;
block|}
block|}
end_class

end_unit

