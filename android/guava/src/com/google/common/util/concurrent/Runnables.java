begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Beta
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

begin_comment
comment|/**  * Static utility methods pertaining to the {@link Runnable} interface.  *  * @since 16.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Runnables
specifier|public
specifier|final
class|class
name|Runnables
block|{
DECL|field|EMPTY_RUNNABLE
specifier|private
specifier|static
specifier|final
name|Runnable
name|EMPTY_RUNNABLE
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{}
block|}
decl_stmt|;
comment|/** Returns a {@link Runnable} instance that does nothing when run. */
DECL|method|doNothing ()
specifier|public
specifier|static
name|Runnable
name|doNothing
parameter_list|()
block|{
return|return
name|EMPTY_RUNNABLE
return|;
block|}
DECL|method|Runnables ()
specifier|private
name|Runnables
parameter_list|()
block|{}
block|}
end_class

end_unit

