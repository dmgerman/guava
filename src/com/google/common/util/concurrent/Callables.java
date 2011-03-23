begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
comment|/**  * Static utility methods pertaining to the {@link Callable} interface.  *  * @author Isaac Shum  * @since 1  */
end_comment

begin_class
DECL|class|Callables
specifier|public
specifier|final
class|class
name|Callables
block|{
DECL|method|Callables ()
specifier|private
name|Callables
parameter_list|()
block|{}
comment|/**    * Creates a {@code Callable} which immediately returns a preset value each    * time it is called.    */
DECL|method|returning (final @Nullable T value)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|returning
parameter_list|(
specifier|final
annotation|@
name|Nullable
name|T
name|value
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|call
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

