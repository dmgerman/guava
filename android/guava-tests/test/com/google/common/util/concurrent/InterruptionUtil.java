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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|NANOSECONDS
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|fail
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
name|testing
operator|.
name|TearDown
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
name|testing
operator|.
name|TearDownAccepter
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Utilities for performing thread interruption in tests  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|InterruptionUtil
specifier|final
class|class
name|InterruptionUtil
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|InterruptionUtil
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Runnable which will interrupt the target thread repeatedly when run.    */
DECL|class|Interruptenator
specifier|private
specifier|static
specifier|final
class|class
name|Interruptenator
implements|implements
name|Runnable
block|{
DECL|field|everyMillis
specifier|private
specifier|final
name|long
name|everyMillis
decl_stmt|;
DECL|field|interruptee
specifier|private
specifier|final
name|Thread
name|interruptee
decl_stmt|;
DECL|field|shouldStop
specifier|private
specifier|volatile
name|boolean
name|shouldStop
init|=
literal|false
decl_stmt|;
DECL|method|Interruptenator (Thread interruptee, long everyMillis)
name|Interruptenator
parameter_list|(
name|Thread
name|interruptee
parameter_list|,
name|long
name|everyMillis
parameter_list|)
block|{
name|this
operator|.
name|everyMillis
operator|=
name|everyMillis
expr_stmt|;
name|this
operator|.
name|interruptee
operator|=
name|interruptee
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|everyMillis
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ok. just stop sleeping.
block|}
if|if
condition|(
name|shouldStop
condition|)
block|{
break|break;
block|}
name|interruptee
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|stopInterrupting ()
name|void
name|stopInterrupting
parameter_list|()
block|{
name|shouldStop
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/**    * Interrupts the current thread after sleeping for the specified delay.    */
DECL|method|requestInterruptIn (final long time, final TimeUnit unit)
specifier|static
name|void
name|requestInterruptIn
parameter_list|(
specifier|final
name|long
name|time
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|unit
argument_list|)
expr_stmt|;
specifier|final
name|Thread
name|interruptee
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
decl_stmt|;
operator|new
name|Thread
argument_list|(
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
block|{
try|try
block|{
name|unit
operator|.
name|sleep
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|wontHappen
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|wontHappen
argument_list|)
throw|;
block|}
name|interruptee
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|repeatedlyInterruptTestThread ( long interruptPeriodMillis, TearDownAccepter tearDownAccepter)
specifier|static
name|void
name|repeatedlyInterruptTestThread
parameter_list|(
name|long
name|interruptPeriodMillis
parameter_list|,
name|TearDownAccepter
name|tearDownAccepter
parameter_list|)
block|{
specifier|final
name|Interruptenator
name|interruptingTask
init|=
operator|new
name|Interruptenator
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|,
name|interruptPeriodMillis
argument_list|)
decl_stmt|;
specifier|final
name|Thread
name|interruptingThread
init|=
operator|new
name|Thread
argument_list|(
name|interruptingTask
argument_list|)
decl_stmt|;
name|interruptingThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|tearDownAccepter
operator|.
name|addTearDown
argument_list|(
operator|new
name|TearDown
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|interruptingTask
operator|.
name|stopInterrupting
argument_list|()
expr_stmt|;
name|interruptingThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
name|joinUninterruptibly
argument_list|(
name|interruptingThread
argument_list|,
literal|2500
argument_list|,
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|interrupted
argument_list|()
expr_stmt|;
if|if
condition|(
name|interruptingThread
operator|.
name|isAlive
argument_list|()
condition|)
block|{
comment|// This will be hidden by test-output redirection:
name|logger
operator|.
name|severe
argument_list|(
literal|"InterruptenatorTask did not exit; future tests may be affected"
argument_list|)
expr_stmt|;
comment|/*            * This won't do any good under JUnit 3, but I'll leave it around in            * case we ever switch to JUnit 4:            */
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// TODO(cpovirk): promote to Uninterruptibles, and add untimed version
DECL|method|joinUninterruptibly ( Thread thread, long timeout, TimeUnit unit)
specifier|private
specifier|static
name|void
name|joinUninterruptibly
parameter_list|(
name|Thread
name|thread
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|boolean
name|interrupted
init|=
literal|false
decl_stmt|;
try|try
block|{
name|long
name|remainingNanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|long
name|end
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|+
name|remainingNanos
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// TimeUnit.timedJoin() treats negative timeouts just like zero.
name|NANOSECONDS
operator|.
name|timedJoin
argument_list|(
name|thread
argument_list|,
name|remainingNanos
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|interrupted
operator|=
literal|true
expr_stmt|;
name|remainingNanos
operator|=
name|end
operator|-
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|interrupted
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
