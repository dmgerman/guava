begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ScheduledExecutorService
import|;
end_import

begin_comment
comment|/**  * A {@link ScheduledExecutorService} that returns {@link ListenableFuture}  * instances from its {@code ExecutorService} methods.  Futures returned by the  * {@code schedule*} methods, by contrast, need not implement {@code  * ListenableFuture}.  (To create an instance from an existing {@link  * ScheduledExecutorService}, call {@link  * MoreExecutors#listeningDecorator(ScheduledExecutorService)}.  *  *<p>TODO(cpovirk): make at least the one-time schedule() methods return a  * ListenableFuture, too? But then we'll need ListenableScheduledFuture...  *  * @author Chris Povirk  * @since 10.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|ListeningScheduledExecutorService
specifier|public
interface|interface
name|ListeningScheduledExecutorService
extends|extends
name|ScheduledExecutorService
extends|,
name|ListeningExecutorService
block|{ }
end_interface

end_unit

