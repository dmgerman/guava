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
name|util
operator|.
name|concurrent
operator|.
name|AbstractFuture
operator|.
name|TrustedFuture
import|;
end_import

begin_comment
comment|/**  * Tests for {@link AbstractFuture} that use a {@link TrustedFuture} for {@link  * AbstractFuture#setFuture} calls.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|TrustedInputFutureTest
specifier|public
class|class
name|TrustedInputFutureTest
extends|extends
name|AbstractAbstractFutureTest
block|{
annotation|@
name|Override
DECL|method|newDelegate ()
name|AbstractFuture
argument_list|<
name|Integer
argument_list|>
name|newDelegate
parameter_list|()
block|{
name|AbstractFuture
argument_list|<
name|Integer
argument_list|>
name|future
init|=
operator|new
name|TrustedFuture
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|assertTrue
argument_list|(
name|future
operator|instanceof
name|TrustedFuture
argument_list|)
expr_stmt|;
comment|// sanity check
return|return
name|future
return|;
block|}
block|}
end_class

end_unit

