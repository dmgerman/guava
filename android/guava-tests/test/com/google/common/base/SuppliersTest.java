begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
operator|.
name|reserialize
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
name|ClassSanityTester
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
name|EqualsTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|TimeoutException
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
comment|/**  * Tests com.google.common.base.Suppliers.  *  * @author Laurence Gonsalves  * @author Harry Heymann  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SuppliersTest
specifier|public
class|class
name|SuppliersTest
extends|extends
name|TestCase
block|{
DECL|class|CountingSupplier
specifier|static
class|class
name|CountingSupplier
implements|implements
name|Supplier
argument_list|<
name|Integer
argument_list|>
block|{
DECL|field|calls
name|int
name|calls
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Integer
name|get
parameter_list|()
block|{
name|calls
operator|++
expr_stmt|;
return|return
name|calls
operator|*
literal|10
return|;
block|}
block|}
DECL|class|ThrowingSupplier
specifier|static
class|class
name|ThrowingSupplier
implements|implements
name|Supplier
argument_list|<
name|Integer
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Integer
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
block|}
DECL|class|SerializableCountingSupplier
specifier|static
class|class
name|SerializableCountingSupplier
extends|extends
name|CountingSupplier
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
DECL|class|SerializableThrowingSupplier
specifier|static
class|class
name|SerializableThrowingSupplier
extends|extends
name|ThrowingSupplier
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
DECL|method|checkMemoize (CountingSupplier countingSupplier, Supplier<Integer> memoizedSupplier)
specifier|static
name|void
name|checkMemoize
parameter_list|(
name|CountingSupplier
name|countingSupplier
parameter_list|,
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
parameter_list|)
block|{
comment|// the underlying supplier hasn't executed yet
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// now it has
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// it still should only have executed once due to memoization
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
block|}
DECL|method|testMemoize ()
specifier|public
name|void
name|testMemoize
parameter_list|()
block|{
name|memoizeTest
argument_list|(
operator|new
name|CountingSupplier
argument_list|()
argument_list|)
expr_stmt|;
name|memoizeTest
argument_list|(
operator|new
name|SerializableCountingSupplier
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|memoizeTest (CountingSupplier countingSupplier)
specifier|private
name|void
name|memoizeTest
parameter_list|(
name|CountingSupplier
name|countingSupplier
parameter_list|)
block|{
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoize
argument_list|(
name|countingSupplier
argument_list|)
decl_stmt|;
name|checkMemoize
argument_list|(
name|countingSupplier
argument_list|,
name|memoizedSupplier
argument_list|)
expr_stmt|;
block|}
DECL|method|testMemoize_redudantly ()
specifier|public
name|void
name|testMemoize_redudantly
parameter_list|()
block|{
name|memoize_redudantlyTest
argument_list|(
operator|new
name|CountingSupplier
argument_list|()
argument_list|)
expr_stmt|;
name|memoize_redudantlyTest
argument_list|(
operator|new
name|SerializableCountingSupplier
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|memoize_redudantlyTest (CountingSupplier countingSupplier)
specifier|private
name|void
name|memoize_redudantlyTest
parameter_list|(
name|CountingSupplier
name|countingSupplier
parameter_list|)
block|{
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoize
argument_list|(
name|countingSupplier
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|memoizedSupplier
argument_list|,
name|Suppliers
operator|.
name|memoize
argument_list|(
name|memoizedSupplier
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMemoizeExceptionThrown ()
specifier|public
name|void
name|testMemoizeExceptionThrown
parameter_list|()
block|{
name|memoizeExceptionThrownTest
argument_list|(
operator|new
name|ThrowingSupplier
argument_list|()
argument_list|)
expr_stmt|;
name|memoizeExceptionThrownTest
argument_list|(
operator|new
name|SerializableThrowingSupplier
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|memoizeExceptionThrownTest (ThrowingSupplier memoizedSupplier)
specifier|private
name|void
name|memoizeExceptionThrownTest
parameter_list|(
name|ThrowingSupplier
name|memoizedSupplier
parameter_list|)
block|{
comment|// call get() twice to make sure that memoization doesn't interfere
comment|// with throwing the exception
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"failed to throw NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// this is what should happen
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testMemoizeNonSerializable ()
specifier|public
name|void
name|testMemoizeNonSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|CountingSupplier
name|countingSupplier
init|=
operator|new
name|CountingSupplier
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoize
argument_list|(
name|countingSupplier
argument_list|)
decl_stmt|;
name|checkMemoize
argument_list|(
name|countingSupplier
argument_list|,
name|memoizedSupplier
argument_list|)
expr_stmt|;
comment|// Calls to the original memoized supplier shouldn't affect its copy.
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
comment|// Should get an exception when we try to serialize.
try|try
block|{
name|reserialize
argument_list|(
name|memoizedSupplier
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|ex
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|java
operator|.
name|io
operator|.
name|NotSerializableException
operator|.
name|class
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testMemoizeSerializable ()
specifier|public
name|void
name|testMemoizeSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|SerializableCountingSupplier
name|countingSupplier
init|=
operator|new
name|SerializableCountingSupplier
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoize
argument_list|(
name|countingSupplier
argument_list|)
decl_stmt|;
name|checkMemoize
argument_list|(
name|countingSupplier
argument_list|,
name|memoizedSupplier
argument_list|)
expr_stmt|;
comment|// Calls to the original memoized supplier shouldn't affect its copy.
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|copy
init|=
name|reserialize
argument_list|(
name|memoizedSupplier
argument_list|)
decl_stmt|;
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|CountingSupplier
name|countingCopy
init|=
call|(
name|CountingSupplier
call|)
argument_list|(
operator|(
name|Suppliers
operator|.
name|MemoizingSupplier
argument_list|<
name|Integer
argument_list|>
operator|)
name|copy
argument_list|)
operator|.
name|delegate
decl_stmt|;
name|checkMemoize
argument_list|(
name|countingCopy
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
DECL|method|testCompose ()
specifier|public
name|void
name|testCompose
parameter_list|()
block|{
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|fiveSupplier
init|=
operator|new
name|Supplier
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|get
parameter_list|()
block|{
return|return
literal|5
return|;
block|}
block|}
decl_stmt|;
name|Function
argument_list|<
name|Number
argument_list|,
name|Integer
argument_list|>
name|intValueFunction
init|=
operator|new
name|Function
argument_list|<
name|Number
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|Number
name|x
parameter_list|)
block|{
return|return
name|x
operator|.
name|intValue
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|squareSupplier
init|=
name|Suppliers
operator|.
name|compose
argument_list|(
name|intValueFunction
argument_list|,
name|fiveSupplier
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|squareSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testComposeWithLists ()
specifier|public
name|void
name|testComposeWithLists
parameter_list|()
block|{
name|Supplier
argument_list|<
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|listSupplier
init|=
operator|new
name|Supplier
argument_list|<
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ArrayList
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Function
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|addElementFunction
init|=
operator|new
name|Function
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|apply
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|list
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|list
argument_list|)
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
decl_stmt|;
name|Supplier
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|addSupplier
init|=
name|Suppliers
operator|.
name|compose
argument_list|(
name|addElementFunction
argument_list|,
name|listSupplier
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|result
init|=
name|addSupplier
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread.sleep
DECL|method|testMemoizeWithExpiration ()
specifier|public
name|void
name|testMemoizeWithExpiration
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|CountingSupplier
name|countingSupplier
init|=
operator|new
name|CountingSupplier
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoizeWithExpiration
argument_list|(
name|countingSupplier
argument_list|,
literal|75
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|checkExpiration
argument_list|(
name|countingSupplier
argument_list|,
name|memoizedSupplier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread.sleep, SerializationTester
DECL|method|testMemoizeWithExpirationSerialized ()
specifier|public
name|void
name|testMemoizeWithExpirationSerialized
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|SerializableCountingSupplier
name|countingSupplier
init|=
operator|new
name|SerializableCountingSupplier
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
init|=
name|Suppliers
operator|.
name|memoizeWithExpiration
argument_list|(
name|countingSupplier
argument_list|,
literal|75
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
comment|// Calls to the original memoized supplier shouldn't affect its copy.
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|copy
init|=
name|reserialize
argument_list|(
name|memoizedSupplier
argument_list|)
decl_stmt|;
name|memoizedSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
name|CountingSupplier
name|countingCopy
init|=
call|(
name|CountingSupplier
call|)
argument_list|(
operator|(
name|Suppliers
operator|.
name|ExpiringMemoizingSupplier
argument_list|<
name|Integer
argument_list|>
operator|)
name|copy
argument_list|)
operator|.
name|delegate
decl_stmt|;
name|checkExpiration
argument_list|(
name|countingCopy
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread.sleep
DECL|method|checkExpiration ( CountingSupplier countingSupplier, Supplier<Integer> memoizedSupplier)
specifier|private
name|void
name|checkExpiration
parameter_list|(
name|CountingSupplier
name|countingSupplier
parameter_list|,
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|memoizedSupplier
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// the underlying supplier hasn't executed yet
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// now it has
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// it still should only have executed once due to memoization
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|150
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// old value expired
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
operator|(
name|int
operator|)
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// it still should only have executed twice due to memoization
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|countingSupplier
operator|.
name|calls
argument_list|)
expr_stmt|;
block|}
DECL|method|testOfInstanceSuppliesSameInstance ()
specifier|public
name|void
name|testOfInstanceSuppliesSameInstance
parameter_list|()
block|{
name|Object
name|toBeSupplied
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|Supplier
argument_list|<
name|Object
argument_list|>
name|objectSupplier
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
name|toBeSupplied
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|toBeSupplied
argument_list|,
name|objectSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|toBeSupplied
argument_list|,
name|objectSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// idempotent
block|}
DECL|method|testOfInstanceSuppliesNull ()
specifier|public
name|void
name|testOfInstanceSuppliesNull
parameter_list|()
block|{
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|nullSupplier
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|nullSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread
DECL|method|testExpiringMemoizedSupplierThreadSafe ()
specifier|public
name|void
name|testExpiringMemoizedSupplierThreadSafe
parameter_list|()
throws|throws
name|Throwable
block|{
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|,
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|>
name|memoizer
init|=
operator|new
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|,
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|apply
parameter_list|(
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
name|Suppliers
operator|.
name|memoizeWithExpiration
argument_list|(
name|supplier
argument_list|,
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|testSupplierThreadSafe
argument_list|(
name|memoizer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread
DECL|method|testMemoizedSupplierThreadSafe ()
specifier|public
name|void
name|testMemoizedSupplierThreadSafe
parameter_list|()
throws|throws
name|Throwable
block|{
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|,
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|>
name|memoizer
init|=
operator|new
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|,
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|apply
parameter_list|(
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
name|Suppliers
operator|.
name|memoize
argument_list|(
name|supplier
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|testSupplierThreadSafe
argument_list|(
name|memoizer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread
DECL|method|testSupplierThreadSafe (Function<Supplier<Boolean>, Supplier<Boolean>> memoizer)
specifier|public
name|void
name|testSupplierThreadSafe
parameter_list|(
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|,
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|>
name|memoizer
parameter_list|)
throws|throws
name|Throwable
block|{
specifier|final
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
name|thrown
init|=
operator|new
name|AtomicReference
argument_list|<
name|Throwable
argument_list|>
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|int
name|numThreads
init|=
literal|3
decl_stmt|;
specifier|final
name|Thread
index|[]
name|threads
init|=
operator|new
name|Thread
index|[
name|numThreads
index|]
decl_stmt|;
specifier|final
name|long
name|timeout
init|=
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toNanos
argument_list|(
literal|60
argument_list|)
decl_stmt|;
specifier|final
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|supplier
init|=
operator|new
name|Supplier
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
name|boolean
name|isWaiting
parameter_list|(
name|Thread
name|thread
parameter_list|)
block|{
switch|switch
condition|(
name|thread
operator|.
name|getState
argument_list|()
condition|)
block|{
case|case
name|BLOCKED
case|:
case|case
name|WAITING
case|:
case|case
name|TIMED_WAITING
case|:
return|return
literal|true
return|;
default|default:
return|return
literal|false
return|;
block|}
block|}
name|int
name|waitingThreads
parameter_list|()
block|{
name|int
name|waitingThreads
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Thread
name|thread
range|:
name|threads
control|)
block|{
if|if
condition|(
name|isWaiting
argument_list|(
name|thread
argument_list|)
condition|)
block|{
name|waitingThreads
operator|++
expr_stmt|;
block|}
block|}
return|return
name|waitingThreads
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|get
parameter_list|()
block|{
comment|// Check that this method is called exactly once, by the first
comment|// thread to synchronize.
name|long
name|t0
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
while|while
condition|(
name|waitingThreads
argument_list|()
operator|!=
name|numThreads
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|t0
operator|>
name|timeout
condition|)
block|{
name|thrown
operator|.
name|set
argument_list|(
operator|new
name|TimeoutException
argument_list|(
literal|"timed out waiting for other threads to block"
operator|+
literal|" synchronizing on supplier"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
block|}
name|count
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|Supplier
argument_list|<
name|Boolean
argument_list|>
name|memoizedSupplier
init|=
name|memoizer
operator|.
name|apply
argument_list|(
name|supplier
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|assertSame
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|memoizedSupplier
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
for|for
control|(
name|Thread
name|t
range|:
name|threads
control|)
block|{
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Thread
name|t
range|:
name|threads
control|)
block|{
name|t
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|thrown
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|thrown
operator|.
name|get
argument_list|()
throw|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Thread
DECL|method|testSynchronizedSupplierThreadSafe ()
specifier|public
name|void
name|testSynchronizedSupplierThreadSafe
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|nonThreadSafe
init|=
operator|new
name|Supplier
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
name|int
name|counter
init|=
literal|0
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Integer
name|get
parameter_list|()
block|{
name|int
name|nextValue
init|=
name|counter
operator|+
literal|1
decl_stmt|;
name|Thread
operator|.
name|yield
argument_list|()
expr_stmt|;
name|counter
operator|=
name|nextValue
expr_stmt|;
return|return
name|counter
return|;
block|}
block|}
decl_stmt|;
specifier|final
name|int
name|numThreads
init|=
literal|10
decl_stmt|;
specifier|final
name|int
name|iterations
init|=
literal|1000
decl_stmt|;
name|Thread
index|[]
name|threads
init|=
operator|new
name|Thread
index|[
name|numThreads
index|]
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
name|numThreads
condition|;
name|i
operator|++
control|)
block|{
name|threads
index|[
name|i
index|]
operator|=
operator|new
name|Thread
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|iterations
condition|;
name|j
operator|++
control|)
block|{
name|Suppliers
operator|.
name|synchronizedSupplier
argument_list|(
name|nonThreadSafe
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
block|}
expr_stmt|;
block|}
for|for
control|(
name|Thread
name|t
range|:
name|threads
control|)
block|{
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Thread
name|t
range|:
name|threads
control|)
block|{
name|t
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|numThreads
operator|*
name|iterations
operator|+
literal|1
argument_list|,
operator|(
name|int
operator|)
name|nonThreadSafe
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSupplierFunction ()
specifier|public
name|void
name|testSupplierFunction
parameter_list|()
block|{
name|Supplier
argument_list|<
name|Integer
argument_list|>
name|supplier
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|14
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Supplier
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|supplierFunction
init|=
name|Suppliers
operator|.
name|supplierFunction
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
operator|(
name|int
operator|)
name|supplierFunction
operator|.
name|apply
argument_list|(
name|supplier
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializationTester
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reserialize
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reserialize
argument_list|(
name|Suppliers
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|identity
argument_list|()
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reserialize
argument_list|(
name|Suppliers
operator|.
name|memoize
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reserialize
argument_list|(
name|Suppliers
operator|.
name|memoizeWithExpiration
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|5
argument_list|)
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|5
argument_list|)
argument_list|,
name|reserialize
argument_list|(
name|Suppliers
operator|.
name|synchronizedSupplier
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|testSuppliersNullChecks ()
specifier|public
name|void
name|testSuppliersNullChecks
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|ClassSanityTester
argument_list|()
operator|.
name|forAllPublicStaticMethods
argument_list|(
name|Suppliers
operator|.
name|class
argument_list|)
operator|.
name|testNulls
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
annotation|@
name|AndroidIncompatible
comment|// TODO(cpovirk): ClassNotFoundException: com.google.common.base.Function
DECL|method|testSuppliersSerializable ()
specifier|public
name|void
name|testSuppliersSerializable
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|ClassSanityTester
argument_list|()
operator|.
name|forAllPublicStaticMethods
argument_list|(
name|Suppliers
operator|.
name|class
argument_list|)
operator|.
name|testSerializable
argument_list|()
expr_stmt|;
block|}
DECL|method|testOfInstance_equals ()
specifier|public
name|void
name|testOfInstance_equals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"foo"
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompose_equals ()
specifier|public
name|void
name|testCompose_equals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Suppliers
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Suppliers
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|2
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Suppliers
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|1
argument_list|)
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

