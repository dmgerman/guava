begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Beta
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|Thread
operator|.
name|UncaughtExceptionHandler
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * A ThreadFactory builder, providing any combination of these features:  *<ul>  *<li> whether threads should be marked as {@linkplain Thread#setDaemon daemon}  * threads  *<li> a {@linkplain ThreadFactoryBuilder#setNameFormat naming format}  *<li> a {@linkplain Thread#setPriority thread priority}  *<li> an {@linkplain Thread#setUncaughtExceptionHandler uncaught exception  * handler}  *<li> a {@linkplain ThreadFactory#newThread backing thread factory}  *</ul>  * If no backing thread factory is provided, a default backing thread factory is  * used as if by calling {@code setThreadFactory(}{@link  * Executors#defaultThreadFactory()}{@code )}.  *  * @author Kurt Alfred Kluever  * @since 4  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ThreadFactoryBuilder
specifier|public
specifier|final
class|class
name|ThreadFactoryBuilder
block|{
DECL|field|nameFormat
specifier|private
name|String
name|nameFormat
init|=
literal|null
decl_stmt|;
DECL|field|daemon
specifier|private
name|Boolean
name|daemon
init|=
literal|null
decl_stmt|;
DECL|field|priority
specifier|private
name|Integer
name|priority
init|=
literal|null
decl_stmt|;
DECL|field|uncaughtExceptionHandler
specifier|private
name|UncaughtExceptionHandler
name|uncaughtExceptionHandler
init|=
literal|null
decl_stmt|;
DECL|field|backingThreadFactory
specifier|private
name|ThreadFactory
name|backingThreadFactory
init|=
literal|null
decl_stmt|;
comment|/**    * Creates a new {@link ThreadFactory} builder.    */
DECL|method|ThreadFactoryBuilder ()
specifier|public
name|ThreadFactoryBuilder
parameter_list|()
block|{}
comment|/**    * Sets the naming format to use when naming threads ({@link Thread#setName})    * which are created with this ThreadFactory.    *    * @param nameFormat a {@link String#format(String, Object...)}-compatible    *     format String, to which a unique integer (0, 1, etc.) will be supplied    *     as the single parameter. This integer will be unique to the built    *     instance of the ThreadFactory and will be assigned sequentially.    * @return this for the builder pattern    */
DECL|method|setNameFormat (String nameFormat)
specifier|public
name|ThreadFactoryBuilder
name|setNameFormat
parameter_list|(
name|String
name|nameFormat
parameter_list|)
block|{
name|String
operator|.
name|format
argument_list|(
name|nameFormat
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// fail fast if the format is bad or null
name|this
operator|.
name|nameFormat
operator|=
name|nameFormat
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets daemon or not for new threads created with this ThreadFactory.    *    * @param daemon whether or not new Threads created with this ThreadFactory    *     will be daemon threads    * @return this for the builder pattern    */
DECL|method|setDaemon (boolean daemon)
specifier|public
name|ThreadFactoryBuilder
name|setDaemon
parameter_list|(
name|boolean
name|daemon
parameter_list|)
block|{
name|this
operator|.
name|daemon
operator|=
name|daemon
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the priority for new threads created with this ThreadFactory.    *    * @param priority the priority for new Threads created with this    *     ThreadFactory    * @return this for the builder pattern    */
DECL|method|setPriority (int priority)
specifier|public
name|ThreadFactoryBuilder
name|setPriority
parameter_list|(
name|int
name|priority
parameter_list|)
block|{
comment|// Thread#setPriority() already checks for validity. These error messages
comment|// are nicer though and will fail-fast.
name|checkArgument
argument_list|(
name|priority
operator|>=
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|,
literal|"Thread priority (%s) must be>= %s"
argument_list|,
name|priority
argument_list|,
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|priority
operator|<=
name|Thread
operator|.
name|MAX_PRIORITY
argument_list|,
literal|"Thread priority (%s) must be<= %s"
argument_list|,
name|priority
argument_list|,
name|Thread
operator|.
name|MAX_PRIORITY
argument_list|)
expr_stmt|;
name|this
operator|.
name|priority
operator|=
name|priority
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the {@link UncaughtExceptionHandler} for new threads created with this    * ThreadFactory.    *    * @param uncaughtExceptionHandler the uncaught exception handler for new    *     Threads created with this ThreadFactory    * @return this for the builder pattern    */
DECL|method|setUncaughtExceptionHandler ( UncaughtExceptionHandler uncaughtExceptionHandler)
specifier|public
name|ThreadFactoryBuilder
name|setUncaughtExceptionHandler
parameter_list|(
name|UncaughtExceptionHandler
name|uncaughtExceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|uncaughtExceptionHandler
operator|=
name|checkNotNull
argument_list|(
name|uncaughtExceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the backing {@link ThreadFactory} for new threads created with this    * ThreadFactory. Threads will be created by invoking #newThread(Runnable) on    * this backing {@link ThreadFactory}.    *    * @param backingThreadFactory the backing {@link ThreadFactory} which will    *     be delegated to during thread creation.    * @return this for the builder pattern    *    * @see MoreExecutors    */
DECL|method|setThreadFactory ( ThreadFactory backingThreadFactory)
specifier|public
name|ThreadFactoryBuilder
name|setThreadFactory
parameter_list|(
name|ThreadFactory
name|backingThreadFactory
parameter_list|)
block|{
name|this
operator|.
name|backingThreadFactory
operator|=
name|checkNotNull
argument_list|(
name|backingThreadFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Returns a new thread factory using the options supplied during the building    * process. After building, it is still possible to change the options used to    * build the ThreadFactory and/or build again. State is not shared amongst    * built instances.    *    * @return the fully constructed {@link ThreadFactory}    */
DECL|method|build ()
specifier|public
name|ThreadFactory
name|build
parameter_list|()
block|{
return|return
name|build
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|build (ThreadFactoryBuilder builder)
specifier|private
specifier|static
name|ThreadFactory
name|build
parameter_list|(
name|ThreadFactoryBuilder
name|builder
parameter_list|)
block|{
specifier|final
name|String
name|nameFormat
init|=
name|builder
operator|.
name|nameFormat
decl_stmt|;
specifier|final
name|Boolean
name|daemon
init|=
name|builder
operator|.
name|daemon
decl_stmt|;
specifier|final
name|Integer
name|priority
init|=
name|builder
operator|.
name|priority
decl_stmt|;
specifier|final
name|UncaughtExceptionHandler
name|uncaughtExceptionHandler
init|=
name|builder
operator|.
name|uncaughtExceptionHandler
decl_stmt|;
specifier|final
name|ThreadFactory
name|backingThreadFactory
init|=
operator|(
name|builder
operator|.
name|backingThreadFactory
operator|!=
literal|null
operator|)
condition|?
name|builder
operator|.
name|backingThreadFactory
else|:
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
decl_stmt|;
specifier|final
name|AtomicLong
name|count
init|=
operator|(
name|nameFormat
operator|!=
literal|null
operator|)
condition|?
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
operator|new
name|ThreadFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|Thread
name|thread
init|=
name|backingThreadFactory
operator|.
name|newThread
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameFormat
operator|!=
literal|null
condition|)
block|{
name|thread
operator|.
name|setName
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|nameFormat
argument_list|,
name|count
operator|.
name|getAndIncrement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|daemon
operator|!=
literal|null
condition|)
block|{
name|thread
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|priority
operator|!=
literal|null
condition|)
block|{
name|thread
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uncaughtExceptionHandler
operator|!=
literal|null
condition|)
block|{
name|thread
operator|.
name|setUncaughtExceptionHandler
argument_list|(
name|uncaughtExceptionHandler
argument_list|)
expr_stmt|;
block|}
return|return
name|thread
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

