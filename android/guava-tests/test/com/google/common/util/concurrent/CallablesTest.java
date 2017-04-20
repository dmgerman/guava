begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|Supplier
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
name|Suppliers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Permission
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
name|Callable
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
name|ExecutionException
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
comment|/**  * Unit tests for {@link Callables}.  *  * @author Isaac Shum  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CallablesTest
specifier|public
class|class
name|CallablesTest
extends|extends
name|TestCase
block|{
DECL|method|testReturning ()
specifier|public
name|void
name|testReturning
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|Callables
operator|.
name|returning
argument_list|(
literal|null
argument_list|)
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Callable
argument_list|<
name|Object
argument_list|>
name|callable
init|=
name|Callables
operator|.
name|returning
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|value
argument_list|,
name|callable
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
comment|// Expect the same value on subsequent calls
name|assertSame
argument_list|(
name|value
argument_list|,
name|callable
operator|.
name|call
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
DECL|method|testAsAsyncCallable ()
specifier|public
name|void
name|testAsAsyncCallable
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|expected
init|=
literal|"MyCallableString"
decl_stmt|;
name|Callable
argument_list|<
name|String
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|expected
return|;
block|}
block|}
decl_stmt|;
name|AsyncCallable
argument_list|<
name|String
argument_list|>
name|asyncCallable
init|=
name|Callables
operator|.
name|asAsyncCallable
argument_list|(
name|callable
argument_list|,
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|asyncCallable
operator|.
name|call
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|expected
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
DECL|method|testAsAsyncCallable_exception ()
specifier|public
name|void
name|testAsAsyncCallable_exception
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Exception
name|expected
init|=
operator|new
name|IllegalArgumentException
argument_list|()
decl_stmt|;
name|Callable
argument_list|<
name|String
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
name|expected
throw|;
block|}
block|}
decl_stmt|;
name|AsyncCallable
argument_list|<
name|String
argument_list|>
name|asyncCallable
init|=
name|Callables
operator|.
name|asAsyncCallable
argument_list|(
name|callable
argument_list|,
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|asyncCallable
operator|.
name|call
argument_list|()
decl_stmt|;
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected exception to be thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|expected
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|testRenaming ()
specifier|public
name|void
name|testRenaming
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|oldName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|Supplier
argument_list|<
name|String
argument_list|>
name|newName
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"MyCrazyThreadName"
argument_list|)
decl_stmt|;
name|Callable
argument_list|<
name|Void
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|newName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Callables
operator|.
name|threadRenaming
argument_list|(
name|callable
argument_list|,
name|newName
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|oldName
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|testRenaming_exceptionalReturn ()
specifier|public
name|void
name|testRenaming_exceptionalReturn
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|oldName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|Supplier
argument_list|<
name|String
argument_list|>
name|newName
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"MyCrazyThreadName"
argument_list|)
decl_stmt|;
class|class
name|MyException
extends|extends
name|Exception
block|{}
name|Callable
argument_list|<
name|Void
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|newName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MyException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|Callables
operator|.
name|threadRenaming
argument_list|(
name|callable
argument_list|,
name|newName
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MyException
name|expected
parameter_list|)
block|{}
name|assertEquals
argument_list|(
name|oldName
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// threads
DECL|method|testRenaming_noPermissions ()
specifier|public
name|void
name|testRenaming_noPermissions
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
operator|new
name|SecurityManager
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|checkAccess
parameter_list|(
name|Thread
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|SecurityException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|checkPermission
parameter_list|(
name|Permission
name|perm
parameter_list|)
block|{
comment|// Do nothing so we can clear the security manager at the end
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|String
name|oldName
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|String
argument_list|>
name|newName
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"MyCrazyThreadName"
argument_list|)
decl_stmt|;
name|Callable
argument_list|<
name|Void
argument_list|>
name|callable
init|=
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|oldName
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Callables
operator|.
name|threadRenaming
argument_list|(
name|callable
argument_list|,
name|newName
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|oldName
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|setSecurityManager
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

