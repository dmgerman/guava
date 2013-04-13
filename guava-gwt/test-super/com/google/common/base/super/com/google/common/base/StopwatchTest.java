begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
package|;
end_package

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
name|MICROSECONDS
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|FakeTicker
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Stopwatch}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|StopwatchTest
specifier|public
class|class
name|StopwatchTest
extends|extends
name|TestCase
block|{
DECL|field|ticker
specifier|private
specifier|final
name|FakeTicker
name|ticker
init|=
operator|new
name|FakeTicker
argument_list|()
decl_stmt|;
DECL|field|stopwatch
specifier|private
specifier|final
name|Stopwatch
name|stopwatch
init|=
operator|new
name|Stopwatch
argument_list|(
name|ticker
argument_list|)
decl_stmt|;
DECL|method|testCreateStarted ()
specifier|public
name|void
name|testCreateStarted
parameter_list|()
block|{
name|Stopwatch
name|startedStopwatch
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|startedStopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateUnstarted ()
specifier|public
name|void
name|testCreateUnstarted
parameter_list|()
block|{
name|Stopwatch
name|unstartedStopwatch
init|=
name|Stopwatch
operator|.
name|createUnstarted
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|unstartedStopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|unstartedStopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInitialState ()
specifier|public
name|void
name|testInitialState
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testStart ()
specifier|public
name|void
name|testStart
parameter_list|()
block|{
name|assertSame
argument_list|(
name|stopwatch
argument_list|,
name|stopwatch
operator|.
name|start
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStart_whileRunning ()
specifier|public
name|void
name|testStart_whileRunning
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
name|assertTrue
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop ()
specifier|public
name|void
name|testStop
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|stopwatch
argument_list|,
name|stopwatch
operator|.
name|stop
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop_new ()
specifier|public
name|void
name|testStop_new
parameter_list|()
block|{
try|try
block|{
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testStop_alreadyStopped ()
specifier|public
name|void
name|testStop_alreadyStopped
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
try|try
block|{
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReset_new ()
specifier|public
name|void
name|testReset_new
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testReset_whileRunning ()
specifier|public
name|void
name|testReset_whileRunning
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|stopwatch
operator|.
name|isRunning
argument_list|()
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsed_whileRunning ()
specifier|public
name|void
name|testElapsed_whileRunning
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|78
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|345
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|345
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsed_notRunning ()
specifier|public
name|void
name|testElapsed_notRunning
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsed_multipleSegments ()
specifier|public
name|void
name|testElapsed_multipleSegments
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|36
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsed_micros ()
specifier|public
name|void
name|testElapsed_micros
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|999
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MICROSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MICROSECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsed_millis ()
specifier|public
name|void
name|testElapsed_millis
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|999999
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsedMillis ()
specifier|public
name|void
name|testElapsedMillis
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|999999
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsedMillis_whileRunning ()
specifier|public
name|void
name|testElapsedMillis_whileRunning
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|78000000
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|345000000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|345
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsedMillis_notRunning ()
specifier|public
name|void
name|testElapsedMillis_notRunning
parameter_list|()
block|{
name|ticker
operator|.
name|advance
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|4000000
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|9000000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testElapsedMillis_multipleSegments ()
specifier|public
name|void
name|testElapsedMillis_multipleSegments
parameter_list|()
block|{
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|9000000
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|16000000
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|9
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|25000000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|stopwatch
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ticker
operator|.
name|advance
argument_list|(
literal|36000000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|34
argument_list|,
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

