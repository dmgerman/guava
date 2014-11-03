begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|cache
operator|.
name|TestingCacheLoaders
operator|.
name|constantLoader
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
name|cache
operator|.
name|TestingCacheLoaders
operator|.
name|identityLoader
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
name|cache
operator|.
name|TestingRemovalListeners
operator|.
name|countingRemovalListener
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
name|cache
operator|.
name|TestingRemovalListeners
operator|.
name|nullRemovalListener
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|base
operator|.
name|Ticker
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Unit tests for CacheBuilder.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CacheBuilderTest
specifier|public
class|class
name|CacheBuilderTest
extends|extends
name|TestCase
block|{
DECL|method|testNewBuilder ()
specifier|public
name|void
name|testNewBuilder
parameter_list|()
block|{
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
name|loader
init|=
name|constantLoader
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|LoadingCache
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|cache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|removalListener
argument_list|(
name|countingRemovalListener
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|loader
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|cache
operator|.
name|getUnchecked
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testInitialCapacity_negative ()
specifier|public
name|void
name|testInitialCapacity_negative
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|initialCapacity
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testInitialCapacity_setTwice ()
specifier|public
name|void
name|testInitialCapacity_setTwice
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|initialCapacity
argument_list|(
literal|16
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|builder
operator|.
name|initialCapacity
argument_list|(
literal|16
argument_list|)
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
block|{}
block|}
DECL|method|testInitialCapacity_large ()
specifier|public
name|void
name|testInitialCapacity_large
parameter_list|()
block|{
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|initialCapacity
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
comment|// that the builder didn't blow up is enough;
comment|// don't actually create this monster!
block|}
DECL|method|testConcurrencyLevel_zero ()
specifier|public
name|void
name|testConcurrencyLevel_zero
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|concurrencyLevel
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testConcurrencyLevel_setTwice ()
specifier|public
name|void
name|testConcurrencyLevel_setTwice
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
literal|16
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|builder
operator|.
name|concurrencyLevel
argument_list|(
literal|16
argument_list|)
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
block|{}
block|}
DECL|method|testConcurrencyLevel_large ()
specifier|public
name|void
name|testConcurrencyLevel_large
parameter_list|()
block|{
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|concurrencyLevel
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
comment|// don't actually build this beast
block|}
DECL|method|testMaximumSize_negative ()
specifier|public
name|void
name|testMaximumSize_negative
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|maximumSize
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testMaximumSize_setTwice ()
specifier|public
name|void
name|testMaximumSize_setTwice
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|maximumSize
argument_list|(
literal|16
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|builder
operator|.
name|maximumSize
argument_list|(
literal|16
argument_list|)
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
block|{}
block|}
DECL|method|testTimeToLive_negative ()
specifier|public
name|void
name|testTimeToLive_negative
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|expireAfterWrite
argument_list|(
operator|-
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testTimeToLive_small ()
specifier|public
name|void
name|testTimeToLive_small
parameter_list|()
block|{
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|NANOSECONDS
argument_list|)
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// well, it didn't blow up.
block|}
DECL|method|testTimeToLive_setTwice ()
specifier|public
name|void
name|testTimeToLive_setTwice
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|expireAfterWrite
argument_list|(
literal|3600
argument_list|,
name|SECONDS
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|builder
operator|.
name|expireAfterWrite
argument_list|(
literal|3600
argument_list|,
name|SECONDS
argument_list|)
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
block|{}
block|}
DECL|method|testTimeToIdle_negative ()
specifier|public
name|void
name|testTimeToIdle_negative
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
name|builder
operator|.
name|expireAfterAccess
argument_list|(
operator|-
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testTimeToIdle_small ()
specifier|public
name|void
name|testTimeToIdle_small
parameter_list|()
block|{
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|expireAfterAccess
argument_list|(
literal|1
argument_list|,
name|NANOSECONDS
argument_list|)
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// well, it didn't blow up.
block|}
DECL|method|testTimeToIdle_setTwice ()
specifier|public
name|void
name|testTimeToIdle_setTwice
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|expireAfterAccess
argument_list|(
literal|3600
argument_list|,
name|SECONDS
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same value is not allowed
name|builder
operator|.
name|expireAfterAccess
argument_list|(
literal|3600
argument_list|,
name|SECONDS
argument_list|)
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
block|{}
block|}
DECL|method|testTimeToIdleAndToLive ()
specifier|public
name|void
name|testTimeToIdleAndToLive
parameter_list|()
block|{
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|expireAfterWrite
argument_list|(
literal|1
argument_list|,
name|NANOSECONDS
argument_list|)
operator|.
name|expireAfterAccess
argument_list|(
literal|1
argument_list|,
name|NANOSECONDS
argument_list|)
operator|.
name|build
argument_list|(
name|identityLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// well, it didn't blow up.
block|}
DECL|method|testTicker_setTwice ()
specifier|public
name|void
name|testTicker_setTwice
parameter_list|()
block|{
name|Ticker
name|testTicker
init|=
name|Ticker
operator|.
name|systemTicker
argument_list|()
decl_stmt|;
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|ticker
argument_list|(
name|testTicker
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same instance is not allowed
name|builder
operator|.
name|ticker
argument_list|(
name|testTicker
argument_list|)
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
block|{}
block|}
DECL|method|testRemovalListener_setTwice ()
specifier|public
name|void
name|testRemovalListener_setTwice
parameter_list|()
block|{
name|RemovalListener
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|testListener
init|=
name|nullRemovalListener
argument_list|()
decl_stmt|;
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|removalListener
argument_list|(
name|testListener
argument_list|)
decl_stmt|;
try|try
block|{
comment|// even to the same instance is not allowed
name|builder
operator|=
name|builder
operator|.
name|removalListener
argument_list|(
name|testListener
argument_list|)
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
block|{}
block|}
DECL|method|testValuesIsNotASet ()
specifier|public
name|void
name|testValuesIsNotASet
parameter_list|()
block|{
name|assertThat
argument_list|(
operator|new
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
operator|.
name|build
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|isNotInstanceOf
argument_list|(
name|Set
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// "Basher tests", where we throw a bunch of stuff at a LoadingCache and check basic invariants.
block|}
end_class

end_unit

