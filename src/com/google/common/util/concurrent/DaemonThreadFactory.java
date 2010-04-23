begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_comment
comment|/**  * Wraps another {@link ThreadFactory}, making all new threads daemon threads.  *  * @author Charles Fry  * @author Harendra Verma  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
comment|// TODO: Deprecate this class.
DECL|class|DaemonThreadFactory
specifier|public
class|class
name|DaemonThreadFactory
implements|implements
name|ThreadFactory
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ThreadFactory
name|delegate
decl_stmt|;
DECL|method|DaemonThreadFactory (ThreadFactory factory)
specifier|public
name|DaemonThreadFactory
parameter_list|(
name|ThreadFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setThreadFactory
argument_list|(
name|factory
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
DECL|method|newThread (Runnable r)
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|newThread
argument_list|(
name|r
argument_list|)
return|;
block|}
block|}
end_class

end_unit

