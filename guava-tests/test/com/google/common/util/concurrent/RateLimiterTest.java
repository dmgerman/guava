begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|Lists
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
name|NullPointerTester
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
name|NullPointerTester
operator|.
name|Visibility
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
comment|/**  * Tests for RateLimiter.  *  * @author Dimitris Andreou  */
end_comment

begin_class
DECL|class|RateLimiterTest
specifier|public
class|class
name|RateLimiterTest
extends|extends
name|TestCase
block|{
comment|/**    * The ticker gathers events and presents them as strings.    * R0.6 means a delay of 0.6 seconds caused by the (R)ateLimiter    * U1.0 means the (U)ser caused the ticker to sleep for a second.    */
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
DECL|method|testSimple ()
specifier|public
name|void
name|testSimple
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|5.0
argument_list|)
decl_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.00, since it's the first request
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.20
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.20
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R0.20"
argument_list|,
literal|"R0.20"
argument_list|)
expr_stmt|;
block|}
DECL|method|testImmediateTryAcquire ()
specifier|public
name|void
name|testImmediateTryAcquire
parameter_list|()
block|{
name|RateLimiter
name|r
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unable to acquire initial permit"
argument_list|,
name|r
operator|.
name|tryAcquire
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Capable of acquiring secondary permit"
argument_list|,
name|r
operator|.
name|tryAcquire
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleRateUpdate ()
specifier|public
name|void
name|testSimpleRateUpdate
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
literal|5.0
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5.0
argument_list|,
name|limiter
operator|.
name|getRate
argument_list|()
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|setRate
argument_list|(
literal|10.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10.0
argument_list|,
name|limiter
operator|.
name|getRate
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|limiter
operator|.
name|setRate
argument_list|(
literal|0.0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ok
parameter_list|)
block|{}
try|try
block|{
name|limiter
operator|.
name|setRate
argument_list|(
operator|-
literal|10.0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ok
parameter_list|)
block|{}
block|}
DECL|method|testSimpleWithWait ()
specifier|public
name|void
name|testSimpleWithWait
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|5.0
argument_list|)
decl_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.00
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|200
argument_list|)
expr_stmt|;
comment|// U0.20, we are ready for the next request...
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.00, ...which is granted immediately
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.20
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"U0.20"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.20"
argument_list|)
expr_stmt|;
block|}
DECL|method|testOneSecondBurst ()
specifier|public
name|void
name|testOneSecondBurst
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|5.0
argument_list|)
decl_stmt|;
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// max capacity reached
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
comment|// this makes no difference
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R0.00, since it's the first request
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R0.00, from capacity
name|limiter
operator|.
name|acquire
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// R0.00, from capacity
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R0.00, concluding a burst of 5 permits
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// R0.20, capacity exhausted
name|assertEvents
argument_list|(
literal|"U1.00"
argument_list|,
literal|"U1.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|,
comment|// first request and burst
literal|"R0.20"
argument_list|)
expr_stmt|;
block|}
DECL|method|testWarmUp ()
specifier|public
name|void
name|testWarmUp
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|2.0
argument_list|,
literal|4000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #1
block|}
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// #2: to repay for the last acquire
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|4000
argument_list|)
expr_stmt|;
comment|// #3: becomes cold again
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// // #4
block|}
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// #5: to repay for the last acquire
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// #6: didn't get cold! It would take another 2 seconds to go cold
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #7
block|}
name|assertEvents
argument_list|(
literal|"R0.00, R1.38, R1.13, R0.88, R0.63, R0.50, R0.50, R0.50"
argument_list|,
comment|// #1
literal|"U0.50"
argument_list|,
comment|// #2
literal|"U4.00"
argument_list|,
comment|// #3
literal|"R0.00, R1.38, R1.13, R0.88, R0.63, R0.50, R0.50, R0.50"
argument_list|,
comment|// #4
literal|"U0.50"
argument_list|,
comment|// #5
literal|"U2.00"
argument_list|,
comment|// #6
literal|"R0.00, R0.50, R0.50, R0.50, R0.50, R0.50, R0.50, R0.50"
argument_list|)
expr_stmt|;
comment|// #7
block|}
DECL|method|testWarmUpAndUpdate ()
specifier|public
name|void
name|testWarmUpAndUpdate
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|2.0
argument_list|,
literal|4000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// // #1
block|}
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|4500
argument_list|)
expr_stmt|;
comment|// #2: back to cold state (warmup period + repay last acquire)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|3
condition|;
name|i
operator|++
control|)
block|{
comment|// only three steps, we're somewhere in the warmup period
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #3
block|}
name|limiter
operator|.
name|setRate
argument_list|(
literal|4.0
argument_list|)
expr_stmt|;
comment|// double the rate!
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #4, we repay the debt of the last acquire (imposed by the old rate)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #5
block|}
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|4250
argument_list|)
expr_stmt|;
comment|// #6, back to cold state (warmup period + repay last acquire)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|11
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
comment|// #7, showing off the warmup starting from totally cold
block|}
comment|// make sure the areas (times) remain the same, while permits are different
name|assertEvents
argument_list|(
literal|"R0.00, R1.38, R1.13, R0.88, R0.63, R0.50, R0.50, R0.50"
argument_list|,
comment|// #1
literal|"U4.50"
argument_list|,
comment|// #2
literal|"R0.00, R1.38, R1.13"
argument_list|,
comment|// #3, after that the rate changes
literal|"R0.88"
argument_list|,
comment|// #4, this is what the throttling would be with the old rate
literal|"R0.34, R0.28, R0.25, R0.25"
argument_list|,
comment|// #5
literal|"U4.25"
argument_list|,
comment|// #6
literal|"R0.00, R0.72, R0.66, R0.59, R0.53, R0.47, R0.41"
argument_list|,
comment|// #7
literal|"R0.34, R0.28, R0.25, R0.25"
argument_list|)
expr_stmt|;
comment|// #7 (cont.), note, this matches #5
block|}
DECL|method|testBursty ()
specifier|public
name|void
name|testBursty
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|createWithCapacity
argument_list|(
name|ticker
argument_list|,
literal|1.0
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
comment|// reach full capacity
name|limiter
operator|.
name|acquire
argument_list|(
literal|11
argument_list|)
expr_stmt|;
comment|// all these are served in a burst (10 + 1 by borrowing from the future)
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// out of capacity, we have to wait
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// and wait
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// fill up 3 permits
name|limiter
operator|.
name|acquire
argument_list|(
literal|5
argument_list|)
expr_stmt|;
comment|// we had 3 ready, thus we borrow 2 permits
name|limiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// this acquire() will also repay for the previous acquire()
name|assertEvents
argument_list|(
literal|"U10.00"
argument_list|,
literal|"R0.00"
argument_list|,
comment|// 10 permits grabbed
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|,
comment|// 1 and 1
literal|"U3.00"
argument_list|,
literal|"R0.00"
argument_list|,
comment|// 5 grabbed
literal|"R3.00"
comment|// 1 grabbed
argument_list|)
expr_stmt|;
block|}
DECL|method|testBurstyAndUpdate ()
specifier|public
name|void
name|testBurstyAndUpdate
parameter_list|()
block|{
name|RateLimiter
name|rateLimiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// no wait
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R1.00, to repay previous
name|rateLimiter
operator|.
name|setRate
argument_list|(
literal|2.0
argument_list|)
expr_stmt|;
comment|// update the rate!
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R1.00, to repay previous (the previous was under the old rate!)
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// R0.50, to repay previous (now the rate takes effect)
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|4
argument_list|)
expr_stmt|;
comment|// R1.00, to repay previous
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R2.00, to repay previous
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R0.50"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R2.00"
argument_list|)
expr_stmt|;
block|}
DECL|method|testTimeWrapping ()
specifier|public
name|void
name|testTimeWrapping
parameter_list|()
block|{
name|ticker
operator|.
name|instant
operator|=
name|Long
operator|.
name|MAX_VALUE
operator|-
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toNanos
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// 1 second before max value
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|4
condition|;
name|i
operator|++
control|)
block|{
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
block|}
comment|// Without protection from overflow, the last wait value would have been huge,
comment|// because "now" would have wrapped into a value near MIN_VALUE, and the limiter would think
comment|// that the next request should be admitted far into the future
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|)
expr_stmt|;
block|}
DECL|method|testTryGate ()
specifier|public
name|void
name|testTryGate
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|5.0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|limiter
operator|.
name|tryAcquire
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|limiter
operator|.
name|tryAcquire
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|limiter
operator|.
name|tryAcquire
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|ticker
operator|.
name|sleepMillis
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|limiter
operator|.
name|tryAcquire
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSimpleWeights ()
specifier|public
name|void
name|testSimpleWeights
parameter_list|()
block|{
name|RateLimiter
name|rateLimiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// no wait
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R1.00, to repay previous
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|2
argument_list|)
expr_stmt|;
comment|// R1.00, to repay previous
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|4
argument_list|)
expr_stmt|;
comment|// R2.00, to repay previous
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|8
argument_list|)
expr_stmt|;
comment|// R4.00, to repay previous
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// R8.00, to repay previous
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R2.00"
argument_list|,
literal|"R4.00"
argument_list|,
literal|"R8.00"
argument_list|)
expr_stmt|;
block|}
DECL|method|testInfinity_Bursty ()
specifier|public
name|void
name|testInfinity_Bursty
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|)
decl_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|/
literal|4
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|/
literal|2
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|)
expr_stmt|;
comment|// no wait, infinite rate!
name|limiter
operator|.
name|setRate
argument_list|(
literal|1.0
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|)
expr_stmt|;
comment|// we repay the last request (but that had no cost)
comment|// and then we go to 1 second per request mode
name|limiter
operator|.
name|setRate
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R1.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|)
expr_stmt|;
comment|// we repay the last request (1sec), then back to +oo
block|}
DECL|method|testInfinity_WarmUp ()
specifier|public
name|void
name|testInfinity_WarmUp
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|/
literal|4
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
operator|/
literal|2
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|setRate
argument_list|(
literal|1.0
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R0.00"
argument_list|,
literal|"R1.00"
argument_list|,
literal|"R1.00"
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|setRate
argument_list|(
name|Double
operator|.
name|POSITIVE_INFINITY
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|limiter
operator|.
name|acquire
argument_list|()
expr_stmt|;
name|assertEvents
argument_list|(
literal|"R1.00"
argument_list|,
literal|"R0.00"
argument_list|,
literal|"R0.00"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Make sure that bursts can never go above 1-second-worth-of-work for the current    * rate, even when we change the rate.    */
DECL|method|testWeNeverGetABurstMoreThanOneSec ()
specifier|public
name|void
name|testWeNeverGetABurstMoreThanOneSec
parameter_list|()
block|{
name|RateLimiter
name|limiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
name|int
index|[]
name|rates
init|=
block|{
literal|1000
block|,
literal|1
block|,
literal|10
block|,
literal|1000000
block|,
literal|10
block|,
literal|1
block|}
decl_stmt|;
for|for
control|(
name|int
name|rate
range|:
name|rates
control|)
block|{
name|int
name|oneSecWorthOfWork
init|=
name|rate
decl_stmt|;
name|ticker
operator|.
name|sleepMillis
argument_list|(
name|rate
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|limiter
operator|.
name|setRate
argument_list|(
name|rate
argument_list|)
expr_stmt|;
name|long
name|burst
init|=
name|measureTotalTimeMillis
argument_list|(
name|limiter
argument_list|,
name|oneSecWorthOfWork
argument_list|,
operator|new
name|Random
argument_list|()
argument_list|)
decl_stmt|;
comment|// we allow one second worth of work to go in a burst (i.e. take less than a second)
name|assertTrue
argument_list|(
name|burst
operator|<=
literal|1000
argument_list|)
expr_stmt|;
name|long
name|afterBurst
init|=
name|measureTotalTimeMillis
argument_list|(
name|limiter
argument_list|,
name|oneSecWorthOfWork
argument_list|,
operator|new
name|Random
argument_list|()
argument_list|)
decl_stmt|;
comment|// but work beyond that must take at least one second
name|assertTrue
argument_list|(
name|afterBurst
operator|>=
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * This neat test shows that no matter what weights we use in our requests, if we push X    * amount of permits in a cool state, where X = rate * timeToCoolDown, and we have    * specified a timeToWarmUp() period, it will cost as the prescribed amount of time. E.g.,    * calling [acquire(5), acquire(1)] takes exactly the same time as    * [acquire(2), acquire(3), acquire(1)].    */
DECL|method|testTimeToWarmUpIsHonouredEvenWithWeights ()
specifier|public
name|void
name|testTimeToWarmUpIsHonouredEvenWithWeights
parameter_list|()
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|maxPermits
init|=
literal|10
decl_stmt|;
name|double
index|[]
name|qpsToTest
init|=
block|{
literal|4.0
block|,
literal|2.0
block|,
literal|1.0
block|,
literal|0.5
block|,
literal|0.1
block|}
decl_stmt|;
for|for
control|(
name|int
name|trial
init|=
literal|0
init|;
name|trial
operator|<
literal|100
condition|;
name|trial
operator|++
control|)
block|{
for|for
control|(
name|double
name|qps
range|:
name|qpsToTest
control|)
block|{
comment|// Since we know that: maxPermits = 0.5 * warmup / stableInterval;
comment|// then if maxPermits == 10, we have:
comment|// warmupSeconds = 20 / qps
name|long
name|warmupMillis
init|=
call|(
name|long
call|)
argument_list|(
operator|(
literal|2
operator|*
name|maxPermits
operator|/
name|qps
operator|)
operator|*
literal|1000.0
argument_list|)
decl_stmt|;
name|RateLimiter
name|rateLimiter
init|=
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
name|qps
argument_list|,
name|warmupMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|warmupMillis
argument_list|,
name|measureTotalTimeMillis
argument_list|(
name|rateLimiter
argument_list|,
name|maxPermits
argument_list|,
name|random
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|RateLimiter
operator|.
name|SleepingTicker
operator|.
name|class
argument_list|,
name|ticker
argument_list|)
decl_stmt|;
name|tester
operator|.
name|testStaticMethods
argument_list|(
name|RateLimiter
operator|.
name|class
argument_list|,
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testInstanceMethods
argument_list|(
name|RateLimiter
operator|.
name|create
argument_list|(
name|ticker
argument_list|,
literal|5.0
argument_list|)
argument_list|,
name|Visibility
operator|.
name|PACKAGE
argument_list|)
expr_stmt|;
block|}
DECL|method|measureTotalTimeMillis (RateLimiter rateLimiter, int permits, Random random)
specifier|private
name|long
name|measureTotalTimeMillis
parameter_list|(
name|RateLimiter
name|rateLimiter
parameter_list|,
name|int
name|permits
parameter_list|,
name|Random
name|random
parameter_list|)
block|{
name|long
name|startTime
init|=
name|ticker
operator|.
name|instant
decl_stmt|;
while|while
condition|(
name|permits
operator|>
literal|0
condition|)
block|{
name|int
name|nextPermitsToAcquire
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|random
operator|.
name|nextInt
argument_list|(
name|permits
argument_list|)
argument_list|)
decl_stmt|;
name|permits
operator|-=
name|nextPermitsToAcquire
expr_stmt|;
name|rateLimiter
operator|.
name|acquire
argument_list|(
name|nextPermitsToAcquire
argument_list|)
expr_stmt|;
block|}
name|rateLimiter
operator|.
name|acquire
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// to repay for any pending debt
return|return
name|TimeUnit
operator|.
name|NANOSECONDS
operator|.
name|toMillis
argument_list|(
name|ticker
operator|.
name|instant
operator|-
name|startTime
argument_list|)
return|;
block|}
DECL|method|assertEvents (String... events)
specifier|private
name|void
name|assertEvents
parameter_list|(
name|String
modifier|...
name|events
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|events
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|ticker
operator|.
name|readEventsAndClear
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|FakeTicker
specifier|private
specifier|static
class|class
name|FakeTicker
extends|extends
name|RateLimiter
operator|.
name|SleepingTicker
block|{
DECL|field|instant
name|long
name|instant
init|=
literal|0L
decl_stmt|;
DECL|field|events
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|long
name|read
parameter_list|()
block|{
return|return
name|instant
return|;
block|}
DECL|method|sleepMillis (int millis)
name|void
name|sleepMillis
parameter_list|(
name|int
name|millis
parameter_list|)
block|{
name|sleepMicros
argument_list|(
literal|"U"
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMicros
argument_list|(
name|millis
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sleepMicros (String caption, long micros)
name|void
name|sleepMicros
parameter_list|(
name|String
name|caption
parameter_list|,
name|long
name|micros
parameter_list|)
block|{
name|instant
operator|+=
name|TimeUnit
operator|.
name|MICROSECONDS
operator|.
name|toNanos
argument_list|(
name|micros
argument_list|)
expr_stmt|;
name|events
operator|.
name|add
argument_list|(
name|caption
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%3.2f"
argument_list|,
operator|(
name|micros
operator|/
literal|1000000.0
operator|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sleepMicrosUninterruptibly (long micros)
name|void
name|sleepMicrosUninterruptibly
parameter_list|(
name|long
name|micros
parameter_list|)
block|{
name|sleepMicros
argument_list|(
literal|"R"
argument_list|,
name|micros
argument_list|)
expr_stmt|;
block|}
DECL|method|readEventsAndClear ()
name|String
name|readEventsAndClear
parameter_list|()
block|{
try|try
block|{
return|return
name|events
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|events
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|events
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

