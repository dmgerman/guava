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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

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
name|Lists
operator|.
name|newArrayListWithCapacity
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|unmodifiableList
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
name|base
operator|.
name|Optional
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
name|collect
operator|.
name|ImmutableCollection
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
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Aggregate future that collects (stores) results of each future.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CollectionFuture
specifier|abstract
class|class
name|CollectionFuture
parameter_list|<
name|V
parameter_list|,
name|C
parameter_list|>
extends|extends
name|AggregateFuture
argument_list|<
name|V
argument_list|,
name|C
argument_list|>
block|{
annotation|@
name|WeakOuter
DECL|class|CollectionFutureRunningState
specifier|abstract
class|class
name|CollectionFutureRunningState
extends|extends
name|RunningState
block|{
DECL|field|values
specifier|private
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
decl_stmt|;
DECL|method|CollectionFutureRunningState ( ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed)
name|CollectionFutureRunningState
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|)
block|{
name|super
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|values
operator|=
name|futures
operator|.
name|isEmpty
argument_list|()
condition|?
name|ImmutableList
operator|.
expr|<
name|Optional
argument_list|<
name|V
argument_list|>
operator|>
name|of
argument_list|()
else|:
name|Lists
operator|.
expr|<
name|Optional
argument_list|<
name|V
argument_list|>
operator|>
name|newArrayListWithCapacity
argument_list|(
name|futures
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Populate the results list with null initially.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|futures
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|values
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|collectOneValue (boolean allMustSucceed, int index, @Nullable V returnValue)
specifier|final
name|void
name|collectOneValue
parameter_list|(
name|boolean
name|allMustSucceed
parameter_list|,
name|int
name|index
parameter_list|,
annotation|@
name|Nullable
name|V
name|returnValue
parameter_list|)
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|localValues
init|=
name|values
decl_stmt|;
if|if
condition|(
name|localValues
operator|!=
literal|null
condition|)
block|{
name|localValues
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|Optional
operator|.
name|fromNullable
argument_list|(
name|returnValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Some other future failed or has been cancelled, causing this one to
comment|// also be cancelled or have an exception set. This should only happen
comment|// if allMustSucceed is true or if the output itself has been
comment|// cancelled.
name|checkState
argument_list|(
name|allMustSucceed
operator|||
name|isCancelled
argument_list|()
argument_list|,
literal|"Future was done before all dependencies completed"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|handleAllCompleted ()
specifier|final
name|void
name|handleAllCompleted
parameter_list|()
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|localValues
init|=
name|values
decl_stmt|;
if|if
condition|(
name|localValues
operator|!=
literal|null
condition|)
block|{
name|set
argument_list|(
name|combine
argument_list|(
name|localValues
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|checkState
argument_list|(
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|releaseResourcesAfterFailure ()
name|void
name|releaseResourcesAfterFailure
parameter_list|()
block|{
name|super
operator|.
name|releaseResourcesAfterFailure
argument_list|()
expr_stmt|;
name|this
operator|.
name|values
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|combine (List<Optional<V>> values)
specifier|abstract
name|C
name|combine
parameter_list|(
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
parameter_list|)
function_decl|;
block|}
comment|/** Used for {@link Futures#allAsList} and {@link Futures#successfulAsList}. */
DECL|class|ListFuture
specifier|static
specifier|final
class|class
name|ListFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|CollectionFuture
argument_list|<
name|V
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
DECL|method|ListFuture ( ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed)
name|ListFuture
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|)
block|{
name|init
argument_list|(
operator|new
name|ListFutureRunningState
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|ListFutureRunningState
specifier|private
specifier|final
class|class
name|ListFutureRunningState
extends|extends
name|CollectionFutureRunningState
block|{
DECL|method|ListFutureRunningState ( ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed)
name|ListFutureRunningState
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
extends|extends
name|ListenableFuture
argument_list|<
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|futures
parameter_list|,
name|boolean
name|allMustSucceed
parameter_list|)
block|{
name|super
argument_list|(
name|futures
argument_list|,
name|allMustSucceed
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|combine (List<Optional<V>> values)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|combine
parameter_list|(
name|List
argument_list|<
name|Optional
argument_list|<
name|V
argument_list|>
argument_list|>
name|values
parameter_list|)
block|{
name|List
argument_list|<
name|V
argument_list|>
name|result
init|=
name|newArrayListWithCapacity
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Optional
argument_list|<
name|V
argument_list|>
name|element
range|:
name|values
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|element
operator|!=
literal|null
condition|?
name|element
operator|.
name|orNull
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

