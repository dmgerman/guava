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
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * Hidden superclass of {@link Futures} that provides us a place to declare special GWT versions of  * the {@link Futures#catching(ListenableFuture, Class, com.google.common.base.Function)  * Futures.catching} family of methods. Those versions have slightly different signatures.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|GwtFuturesCatchingSpecialization
specifier|abstract
class|class
name|GwtFuturesCatchingSpecialization
block|{
comment|/*    * This server copy of the class is empty. The corresponding GWT copy contains alternative    * versions of catching() and catchingAsync() with slightly different signatures from the ones    * found in Futures.java.    */
block|}
end_class

end_unit

