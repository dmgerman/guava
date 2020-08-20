begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|Buffer
import|;
end_import

begin_comment
comment|/**  * Wrappers around {@link Buffer} methods that are covariantly overridden in Java 9+. See  * https://github.com/google/guava/issues/3990  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|Java8Compatibility
specifier|final
class|class
name|Java8Compatibility
block|{
DECL|method|clear (Buffer b)
specifier|static
name|void
name|clear
parameter_list|(
name|Buffer
name|b
parameter_list|)
block|{
name|b
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|flip (Buffer b)
specifier|static
name|void
name|flip
parameter_list|(
name|Buffer
name|b
parameter_list|)
block|{
name|b
operator|.
name|flip
argument_list|()
expr_stmt|;
block|}
DECL|method|limit (Buffer b, int limit)
specifier|static
name|void
name|limit
parameter_list|(
name|Buffer
name|b
parameter_list|,
name|int
name|limit
parameter_list|)
block|{
name|b
operator|.
name|limit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
DECL|method|position (Buffer b, int position)
specifier|static
name|void
name|position
parameter_list|(
name|Buffer
name|b
parameter_list|,
name|int
name|position
parameter_list|)
block|{
name|b
operator|.
name|position
argument_list|(
name|position
argument_list|)
expr_stmt|;
block|}
DECL|method|Java8Compatibility ()
specifier|private
name|Java8Compatibility
parameter_list|()
block|{}
block|}
end_class

end_unit

