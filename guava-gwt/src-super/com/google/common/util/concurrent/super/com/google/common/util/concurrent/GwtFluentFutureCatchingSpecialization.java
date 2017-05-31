begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Function
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
name|Executor
import|;
end_import

begin_comment
comment|/**  * Hidden superclass of {@link FluentFuture} that provides us a place to declare special GWT  * versions of the {@link FluentFuture#catching(Class, com.google.common.base.Function)  * FluentFuture.catching} family of methods. Those versions have slightly different signatures.  */
end_comment

begin_class
DECL|class|GwtFluentFutureCatchingSpecialization
specifier|abstract
class|class
name|GwtFluentFutureCatchingSpecialization
parameter_list|<
name|V
parameter_list|>
implements|implements
name|ListenableFuture
argument_list|<
name|V
argument_list|>
block|{
comment|/*    * In the GWT versions of the methods (below), every exceptionType parameter is required to be    * Class<Throwable>. To handle only certain kinds of exceptions under GWT, you'll need to write    * your own instanceof tests.    */
DECL|method|catching ( Class<Throwable> exceptionType, Function<? super Throwable, ? extends V> fallback, Executor executor)
specifier|public
specifier|final
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|catching
parameter_list|(
name|Class
argument_list|<
name|Throwable
argument_list|>
name|exceptionType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|Throwable
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|Futures
operator|.
name|catching
argument_list|(
name|this
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|,
name|executor
argument_list|)
return|;
block|}
DECL|method|catchingAsync ( Class<Throwable> exceptionType, AsyncFunction<? super Throwable, ? extends V> fallback, Executor executor)
specifier|public
specifier|final
name|FluentFuture
argument_list|<
name|V
argument_list|>
name|catchingAsync
parameter_list|(
name|Class
argument_list|<
name|Throwable
argument_list|>
name|exceptionType
parameter_list|,
name|AsyncFunction
argument_list|<
name|?
super|super
name|Throwable
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|fallback
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
return|return
operator|(
name|FluentFuture
argument_list|<
name|V
argument_list|>
operator|)
name|Futures
operator|.
name|catchingAsync
argument_list|(
name|this
argument_list|,
name|exceptionType
argument_list|,
name|fallback
argument_list|,
name|executor
argument_list|)
return|;
block|}
block|}
end_class

end_unit
