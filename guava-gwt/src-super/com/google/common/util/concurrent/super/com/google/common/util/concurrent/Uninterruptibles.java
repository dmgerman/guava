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

begin_comment
comment|/**  * Emulation of Uninterruptibles in GWT.  */
end_comment

begin_class
DECL|class|Uninterruptibles
specifier|public
specifier|final
class|class
name|Uninterruptibles
block|{
DECL|method|Uninterruptibles ()
specifier|private
name|Uninterruptibles
parameter_list|()
block|{   }
DECL|method|getUninterruptibly (Future<V> future)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|V
name|getUninterruptibly
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|)
throws|throws
name|ExecutionException
block|{
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Should never be thrown in GWT but play it safe
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

