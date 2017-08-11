begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|ImmutableMap
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|AtomicInteger
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
comment|/**  * Unit tests for {@link CacheLoader}.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|CacheLoaderTest
specifier|public
class|class
name|CacheLoaderTest
extends|extends
name|TestCase
block|{
DECL|class|QueuingExecutor
specifier|private
specifier|static
class|class
name|QueuingExecutor
implements|implements
name|Executor
block|{
DECL|field|tasks
specifier|private
name|LinkedList
argument_list|<
name|Runnable
argument_list|>
name|tasks
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|execute (Runnable task)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|tasks
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
DECL|method|runNext ()
specifier|private
name|void
name|runNext
parameter_list|()
block|{
name|tasks
operator|.
name|removeFirst
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testAsyncReload ()
specifier|public
name|void
name|testAsyncReload
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|AtomicInteger
name|loadCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|AtomicInteger
name|reloadCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|AtomicInteger
name|loadAllCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|baseLoader
init|=
operator|new
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|load
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|loadCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
operator|new
name|Object
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Object
argument_list|>
name|reload
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|oldValue
parameter_list|)
block|{
name|reloadCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|loadAll
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|)
block|{
name|loadAllCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|loadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reloadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|loadAllCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|baseLoader
operator|.
name|load
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// go/futurereturn-lsc
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError
init|=
name|baseLoader
operator|.
name|reload
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|baseLoader
operator|.
name|loadAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reloadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|loadAllCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|QueuingExecutor
name|executor
init|=
operator|new
name|QueuingExecutor
argument_list|()
decl_stmt|;
name|CacheLoader
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|asyncReloader
init|=
name|CacheLoader
operator|.
name|asyncReloading
argument_list|(
name|baseLoader
argument_list|,
name|executor
argument_list|)
decl_stmt|;
name|asyncReloader
operator|.
name|load
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// go/futurereturn-lsc
name|Future
argument_list|<
name|?
argument_list|>
name|possiblyIgnoredError1
init|=
name|asyncReloader
operator|.
name|reload
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|asyncReloader
operator|.
name|loadAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|loadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reloadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|loadAllCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|runNext
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|loadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|reloadCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|loadAllCount
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

