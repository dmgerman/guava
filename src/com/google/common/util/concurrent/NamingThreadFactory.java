begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Executors
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
comment|/**  * A ThreadFactory which decorates another ThreadFactory to set a name on  * each thread created.  *  * @author Kevin Bourrillion  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
comment|// TODO: Deprecate this class.
DECL|class|NamingThreadFactory
specifier|public
class|class
name|NamingThreadFactory
implements|implements
name|ThreadFactory
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ThreadFactory
name|delegate
decl_stmt|;
DECL|field|DEFAULT_FACTORY
specifier|public
specifier|static
specifier|final
name|ThreadFactory
name|DEFAULT_FACTORY
init|=
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
decl_stmt|;
comment|/**    * Creates a new factory that delegates to the default thread factory for    * thread creation, then uses {@code format} to construct a name for the new    * thread.    *    * @param format a {@link String#format(String, Object...)}-compatible format    *     String, to which a unique integer (0, 1, etc.) will be supplied as the    *     single parameter. This integer will be unique to this instance of    *     NamingThreadFactory and will be assigned sequentially.    */
DECL|method|NamingThreadFactory (String format)
specifier|public
name|NamingThreadFactory
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|this
argument_list|(
name|format
argument_list|,
name|DEFAULT_FACTORY
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new factory that delegates to {@code backingFactory} for thread    * creation, then uses {@code format} to construct a name for the new thread.    *    * @param format a {@link String#format(String, Object...)}-compatible format    *     String, to which a unique integer (0, 1, etc.) will be supplied as the    *     single parameter    * @param backingFactory the factory that will actually create the threads    * @throws java.util.IllegalFormatException if {@code format} is invalid    */
DECL|method|NamingThreadFactory (String format, ThreadFactory backingFactory)
specifier|public
name|NamingThreadFactory
parameter_list|(
name|String
name|format
parameter_list|,
name|ThreadFactory
name|backingFactory
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
name|setNameFormat
argument_list|(
name|format
argument_list|)
operator|.
name|setThreadFactory
argument_list|(
name|backingFactory
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

