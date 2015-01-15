begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_comment
comment|/**  * Factory and utility methods for {@link java.util.concurrent.Executor}, {@link  * ExecutorService}, and {@link ThreadFactory}.  *  * @author Eric Fellheimer  * @author Kyle Littlefield  * @author Justin Mahoney  * @since 3.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MoreExecutors
specifier|public
specifier|final
class|class
name|MoreExecutors
block|{
DECL|method|MoreExecutors ()
specifier|private
name|MoreExecutors
parameter_list|()
block|{}
comment|// See sameThreadExecutor javadoc for behavioral notes.
comment|/**    * Returns an {@link Executor} that runs each task in the thread that invokes    * {@link Executor#execute execute}, as in {@link CallerRunsPolicy}.    *    *<p>This instance is equivalent to:<pre>   {@code    *   final class DirectExecutor implements Executor {    *     public void execute(Runnable r) {    *       r.run();    *     }    *   }}</pre>    *    *<p>This should be preferred to {@link #newDirectExecutorService()} because the implementing the    * {@link ExecutorService} subinterface necessitates significant performance overhead.    *    * @since 18.0    */
DECL|method|directExecutor ()
specifier|public
specifier|static
name|Executor
name|directExecutor
parameter_list|()
block|{
return|return
name|DirectExecutor
operator|.
name|INSTANCE
return|;
block|}
comment|/** See {@link #directExecutor} for behavioral notes. */
DECL|enum|DirectExecutor
specifier|private
enum|enum
name|DirectExecutor
implements|implements
name|Executor
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|method|execute (Runnable command)
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|command
parameter_list|)
block|{
name|command
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*    * This following method is a modified version of one found in    * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/test/tck/AbstractExecutorServiceTest.java?revision=1.30    * which contained the following notice:    *    * Written by Doug Lea with assistance from members of JCP JSR-166    * Expert Group and released to the public domain, as explained at    * http://creativecommons.org/publicdomain/zero/1.0/    * Other contributors include Andrew Wright, Jeffrey Hayes,    * Pat Fisher, Mike Judd.    */
comment|// TODO(lukes): provide overloads for ListeningExecutorService? ListeningScheduledExecutorService?
comment|// TODO(lukes): provide overloads that take constant strings? Function<Runnable, String>s to
comment|// calculate names?
block|}
end_class

end_unit

