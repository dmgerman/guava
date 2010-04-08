begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExecutorService
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
name|ScheduledExecutorService
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
name|ScheduledThreadPoolExecutor
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolExecutor
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Old location of {@link MoreExecutors}.  */
end_comment

begin_class
annotation|@
name|Beta
comment|// TODO: delete after Guava release 3
DECL|class|Executors
specifier|public
specifier|final
class|class
name|Executors
block|{
DECL|method|Executors ()
specifier|private
name|Executors
parameter_list|()
block|{}
comment|/**    * Old location of {@link MoreExecutors#getExitingExecutorService(    * ThreadPoolExecutor, long, TimeUnit)}.    */
DECL|method|getExitingExecutorService ( ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
specifier|public
specifier|static
name|ExecutorService
name|getExitingExecutorService
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|,
name|long
name|terminationTimeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
return|return
name|MoreExecutors
operator|.
name|getExitingExecutorService
argument_list|(
name|executor
argument_list|,
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#getExitingScheduledExecutorService(    * ScheduledThreadPoolExecutor, long, TimeUnit)}.    */
DECL|method|getExitingScheduledExecutorService ( ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)
specifier|public
specifier|static
name|ScheduledExecutorService
name|getExitingScheduledExecutorService
parameter_list|(
name|ScheduledThreadPoolExecutor
name|executor
parameter_list|,
name|long
name|terminationTimeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
return|return
name|MoreExecutors
operator|.
name|getExitingScheduledExecutorService
argument_list|(
name|executor
argument_list|,
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#addDelayedShutdownHook(    * ExecutorService, long, TimeUnit)}.    */
DECL|method|addDelayedShutdownHook ( final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit)
specifier|public
specifier|static
name|void
name|addDelayedShutdownHook
parameter_list|(
specifier|final
name|ExecutorService
name|service
parameter_list|,
specifier|final
name|long
name|terminationTimeout
parameter_list|,
specifier|final
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|MoreExecutors
operator|.
name|addDelayedShutdownHook
argument_list|(
name|service
argument_list|,
name|terminationTimeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Old location of {@link MoreExecutors#getExitingExecutorService(    * ThreadPoolExecutor)}.    */
DECL|method|getExitingExecutorService ( ThreadPoolExecutor executor)
specifier|public
specifier|static
name|ExecutorService
name|getExitingExecutorService
parameter_list|(
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
return|return
name|MoreExecutors
operator|.
name|getExitingExecutorService
argument_list|(
name|executor
argument_list|)
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#getExitingScheduledExecutorService(    * ScheduledThreadPoolExecutor)}.    */
DECL|method|getExitingScheduledExecutorService ( ScheduledThreadPoolExecutor executor)
specifier|public
specifier|static
name|ScheduledExecutorService
name|getExitingScheduledExecutorService
parameter_list|(
name|ScheduledThreadPoolExecutor
name|executor
parameter_list|)
block|{
return|return
name|MoreExecutors
operator|.
name|getExitingScheduledExecutorService
argument_list|(
name|executor
argument_list|)
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#daemonThreadFactory()}.    */
DECL|method|daemonThreadFactory ()
specifier|public
specifier|static
name|ThreadFactory
name|daemonThreadFactory
parameter_list|()
block|{
return|return
name|MoreExecutors
operator|.
name|daemonThreadFactory
argument_list|()
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#daemonThreadFactory(ThreadFactory)}.    */
DECL|method|daemonThreadFactory (ThreadFactory factory)
specifier|public
specifier|static
name|ThreadFactory
name|daemonThreadFactory
parameter_list|(
name|ThreadFactory
name|factory
parameter_list|)
block|{
return|return
name|MoreExecutors
operator|.
name|daemonThreadFactory
argument_list|(
name|factory
argument_list|)
return|;
block|}
comment|/**    * Old location of {@link MoreExecutors#sameThreadExecutor()}.    */
DECL|method|sameThreadExecutor ()
specifier|public
specifier|static
name|ExecutorService
name|sameThreadExecutor
parameter_list|()
block|{
return|return
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
return|;
block|}
block|}
end_class

end_unit

