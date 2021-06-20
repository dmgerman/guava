begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Transforms a value, possibly asynchronously. For an example usage and more information, see  * {@link Futures#transformAsync(ListenableFuture, AsyncFunction, Executor)}.  *  * @author Chris Povirk  * @since 11.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|AsyncFunction
specifier|public
expr|interface
name|AsyncFunction
operator|<
name|I
expr|extends @
name|Nullable
name|Object
operator|,
name|O
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/**    * Returns an output {@code Future} to use in place of the given {@code input}. The output {@code    * Future} need not be {@linkplain Future#isDone done}, making {@code AsyncFunction} suitable for    * asynchronous derivations.    *    *<p>Throwing an exception from this method is equivalent to returning a failing {@code Future}.    */
DECL|method|apply (@arametricNullness I input)
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|apply
argument_list|(
annotation|@
name|ParametricNullness
name|I
name|input
argument_list|)
throws|throws
name|Exception
block|; }
end_expr_stmt

end_unit

