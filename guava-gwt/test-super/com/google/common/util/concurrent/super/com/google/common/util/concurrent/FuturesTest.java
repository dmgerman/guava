begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
operator|.
name|immediateFuture
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
operator|.
name|directExecutor
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
name|Function
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
name|Functions
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Futures}.  *  * @author Nishant Thakkar  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FuturesTest
specifier|public
class|class
name|FuturesTest
extends|extends
name|EmptySetUpAndTearDown
block|{
DECL|field|DATA1
specifier|private
specifier|static
specifier|final
name|String
name|DATA1
init|=
literal|"data"
decl_stmt|;
DECL|field|DATA2
specifier|private
specifier|static
specifier|final
name|String
name|DATA2
init|=
literal|"more data"
decl_stmt|;
DECL|field|DATA3
specifier|private
specifier|static
specifier|final
name|String
name|DATA3
init|=
literal|"most data"
decl_stmt|;
DECL|method|testImmediateFuture ()
specifier|public
name|void
name|testImmediateFuture
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|future
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|DATA1
argument_list|)
decl_stmt|;
comment|// Verify that the proper object is returned without waiting
name|assertSame
argument_list|(
name|DATA1
argument_list|,
name|future
operator|.
name|get
argument_list|(
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMultipleImmediateFutures ()
specifier|public
name|void
name|testMultipleImmediateFutures
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|future1
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|DATA1
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|String
argument_list|>
name|future2
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|DATA2
argument_list|)
decl_stmt|;
comment|// Verify that the proper objects are returned without waiting
name|assertSame
argument_list|(
name|DATA1
argument_list|,
name|future1
operator|.
name|get
argument_list|(
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|DATA2
argument_list|,
name|future2
operator|.
name|get
argument_list|(
literal|0L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|MyException
specifier|private
specifier|static
class|class
name|MyException
extends|extends
name|Exception
block|{}
comment|// Class hierarchy for generics sanity checks
DECL|class|Foo
specifier|private
specifier|static
class|class
name|Foo
block|{}
DECL|class|FooChild
specifier|private
specifier|static
class|class
name|FooChild
extends|extends
name|Foo
block|{}
DECL|class|Bar
specifier|private
specifier|static
class|class
name|Bar
block|{}
DECL|class|BarChild
specifier|private
specifier|static
class|class
name|BarChild
extends|extends
name|Bar
block|{}
DECL|method|testTransform_genericsNull ()
specifier|public
name|void
name|testTransform_genericsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|nullFuture
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|transformedFuture
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|nullFuture
argument_list|,
name|Functions
operator|.
name|constant
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|transformedFuture
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_genericsHierarchy ()
specifier|public
name|void
name|testTransform_genericsHierarchy
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|FooChild
argument_list|>
name|future
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|BarChild
name|barChild
init|=
operator|new
name|BarChild
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|Foo
argument_list|,
name|BarChild
argument_list|>
name|function
init|=
operator|new
name|Function
argument_list|<
name|Foo
argument_list|,
name|BarChild
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|BarChild
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
return|return
name|barChild
return|;
block|}
block|}
decl_stmt|;
name|Bar
name|bar
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|future
argument_list|,
name|function
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|barChild
argument_list|,
name|bar
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_cancelPropagatesToInput ()
specifier|public
name|void
name|testTransform_cancelPropagatesToInput
parameter_list|()
throws|throws
name|Exception
block|{
name|SettableFuture
argument_list|<
name|Foo
argument_list|>
name|input
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Bar
argument_list|>
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
literal|"Unexpeted call to apply."
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|Futures
operator|.
name|transform
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|input
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_interruptPropagatesToInput ()
specifier|public
name|void
name|testTransform_interruptPropagatesToInput
parameter_list|()
throws|throws
name|Exception
block|{
name|SettableFuture
argument_list|<
name|Foo
argument_list|>
name|input
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Bar
argument_list|>
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
literal|"Unexpeted call to apply."
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|Futures
operator|.
name|transform
argument_list|(
name|input
argument_list|,
name|function
argument_list|)
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|input
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_cancelPropagatesToAsyncOutput ()
specifier|public
name|void
name|testTransform_cancelPropagatesToAsyncOutput
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|Foo
argument_list|>
name|immediate
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
operator|new
name|Foo
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SettableFuture
argument_list|<
name|Bar
argument_list|>
name|secondary
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Bar
argument_list|>
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
return|return
name|secondary
return|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|Futures
operator|.
name|transform
argument_list|(
name|immediate
argument_list|,
name|function
argument_list|)
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|secondary
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|secondary
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_interruptPropagatesToAsyncOutput ()
specifier|public
name|void
name|testTransform_interruptPropagatesToAsyncOutput
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|Foo
argument_list|>
name|immediate
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
operator|new
name|Foo
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SettableFuture
argument_list|<
name|Bar
argument_list|>
name|secondary
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|Bar
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Bar
argument_list|>
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
return|return
name|secondary
return|;
block|}
block|}
decl_stmt|;
name|assertTrue
argument_list|(
name|Futures
operator|.
name|transform
argument_list|(
name|immediate
argument_list|,
name|function
argument_list|)
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|secondary
operator|.
name|isCancelled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|secondary
operator|.
name|wasInterrupted
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Tests that the function is invoked only once, even if it throws an    * exception.    */
DECL|method|testTransformValueRemainsMemoized ()
specifier|public
name|void
name|testTransformValueRemainsMemoized
parameter_list|()
throws|throws
name|Exception
block|{
class|class
name|Holder
block|{
name|int
name|value
init|=
literal|2
decl_stmt|;
block|}
specifier|final
name|Holder
name|holder
init|=
operator|new
name|Holder
argument_list|()
decl_stmt|;
comment|// This function adds the holder's value to the input value.
name|Function
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|adder
init|=
operator|new
name|Function
argument_list|<
name|Integer
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
name|Integer
name|from
parameter_list|)
block|{
return|return
name|from
operator|+
name|holder
operator|.
name|value
return|;
block|}
block|}
decl_stmt|;
comment|// Since holder.value is 2, applying 4 should yield 6.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|adder
operator|.
name|apply
argument_list|(
literal|4
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|immediateFuture
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|Integer
argument_list|>
name|transformedFuture
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|immediateFuture
argument_list|,
name|adder
argument_list|)
decl_stmt|;
comment|// The composed future also yields 6.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|transformedFuture
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Repeated calls yield the same value even though the function's behavior
comment|// changes
name|holder
operator|.
name|value
operator|=
literal|3
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|transformedFuture
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|adder
operator|.
name|apply
argument_list|(
literal|4
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Once more, with feeling.
name|holder
operator|.
name|value
operator|=
literal|4
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|transformedFuture
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|adder
operator|.
name|apply
argument_list|(
literal|4
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Memoized get also retains the value.
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|transformedFuture
operator|.
name|get
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Unsurprisingly, recomposing the future will return an updated value.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|Futures
operator|.
name|transform
argument_list|(
name|immediateFuture
argument_list|,
name|adder
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Repeating, with the timeout version
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|Futures
operator|.
name|transform
argument_list|(
name|immediateFuture
argument_list|,
name|adder
argument_list|)
operator|.
name|get
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyError
specifier|static
class|class
name|MyError
extends|extends
name|Error
block|{}
DECL|class|MyRuntimeException
specifier|static
class|class
name|MyRuntimeException
extends|extends
name|RuntimeException
block|{}
comment|// TODO(cpovirk): top-level class?
DECL|class|ExecutorSpy
specifier|static
class|class
name|ExecutorSpy
implements|implements
name|Executor
block|{
DECL|field|delegate
name|Executor
name|delegate
decl_stmt|;
DECL|field|wasExecuted
name|boolean
name|wasExecuted
decl_stmt|;
DECL|method|ExecutorSpy (Executor delegate)
specifier|public
name|ExecutorSpy
parameter_list|(
name|Executor
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
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
name|delegate
operator|.
name|execute
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|wasExecuted
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|testTransform_Executor ()
specifier|public
name|void
name|testTransform_Executor
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|ExecutorSpy
name|spy
init|=
operator|new
name|ExecutorSpy
argument_list|(
name|directExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|spy
operator|.
name|wasExecuted
argument_list|)
expr_stmt|;
name|ListenableFuture
argument_list|<
name|Object
argument_list|>
name|future
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|Futures
operator|.
name|immediateFuture
argument_list|(
name|value
argument_list|)
argument_list|,
name|Functions
operator|.
name|identity
argument_list|()
argument_list|,
name|spy
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|value
argument_list|,
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|spy
operator|.
name|wasExecuted
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_genericsWildcard_AsyncFunction ()
specifier|public
name|void
name|testTransform_genericsWildcard_AsyncFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|nullFuture
init|=
name|immediateFuture
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|chainedFuture
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|nullFuture
argument_list|,
name|constantAsyncFunction
argument_list|(
name|nullFuture
argument_list|)
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|chainedFuture
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|constantAsyncFunction ( final ListenableFuture<O> output)
specifier|private
specifier|static
parameter_list|<
name|I
parameter_list|,
name|O
parameter_list|>
name|AsyncFunction
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
name|constantAsyncFunction
parameter_list|(
specifier|final
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|output
parameter_list|)
block|{
return|return
operator|new
name|AsyncFunction
argument_list|<
name|I
argument_list|,
name|O
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|O
argument_list|>
name|apply
parameter_list|(
name|I
name|input
parameter_list|)
block|{
return|return
name|output
return|;
block|}
block|}
return|;
block|}
DECL|method|testTransform_genericsHierarchy_AsyncFunction ()
specifier|public
name|void
name|testTransform_genericsHierarchy_AsyncFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|FooChild
argument_list|>
name|future
init|=
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
decl_stmt|;
specifier|final
name|BarChild
name|barChild
init|=
operator|new
name|BarChild
argument_list|()
decl_stmt|;
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|BarChild
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|Foo
argument_list|,
name|BarChild
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AbstractFuture
argument_list|<
name|BarChild
argument_list|>
name|apply
parameter_list|(
name|Foo
name|unused
parameter_list|)
block|{
name|AbstractFuture
argument_list|<
name|BarChild
argument_list|>
name|future
init|=
operator|new
name|AbstractFuture
argument_list|<
name|BarChild
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|future
operator|.
name|set
argument_list|(
name|barChild
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
block|}
decl_stmt|;
name|Bar
name|bar
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|future
argument_list|,
name|function
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|barChild
argument_list|,
name|bar
argument_list|)
expr_stmt|;
block|}
DECL|method|testTransform_asyncFunction_error ()
specifier|public
name|void
name|testTransform_asyncFunction_error
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|Error
name|error
init|=
operator|new
name|Error
argument_list|(
literal|"deliberate"
argument_list|)
decl_stmt|;
name|AsyncFunction
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
operator|new
name|AsyncFunction
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
throw|throw
name|error
throw|;
block|}
block|}
decl_stmt|;
name|SettableFuture
argument_list|<
name|String
argument_list|>
name|inputFuture
init|=
name|SettableFuture
operator|.
name|create
argument_list|()
decl_stmt|;
name|ListenableFuture
argument_list|<
name|Integer
argument_list|>
name|outputFuture
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|inputFuture
argument_list|,
name|function
argument_list|)
decl_stmt|;
name|inputFuture
operator|.
name|set
argument_list|(
literal|"value"
argument_list|)
expr_stmt|;
try|try
block|{
name|outputFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"should have thrown error"
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
name|error
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testTransform_asyncFunction_nullInsteadOfFuture ()
specifier|public
name|void
name|testTransform_asyncFunction_nullInsteadOfFuture
parameter_list|()
throws|throws
name|Exception
block|{
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|inputFuture
init|=
name|immediateFuture
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|chainedFuture
init|=
name|Futures
operator|.
name|transform
argument_list|(
name|inputFuture
argument_list|,
name|constantAsyncFunction
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|chainedFuture
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|expected
parameter_list|)
block|{
name|NullPointerException
name|cause
init|=
operator|(
name|NullPointerException
operator|)
name|expected
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|cause
argument_list|)
operator|.
name|hasMessage
argument_list|(
literal|"AsyncFunction.apply returned null instead of a Future. "
operator|+
literal|"Did you mean to return immediateFuture(null)?"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    * TODO(cpovirk): maybe pass around TestFuture instances instead of    * ListenableFuture instances    */
DECL|class|OtherThrowable
specifier|private
specifier|static
specifier|final
class|class
name|OtherThrowable
extends|extends
name|Throwable
block|{}
comment|// Boring untimed-get tests:
comment|// Boring timed-get tests:
comment|// Boring getUnchecked tests:
comment|// Edge case tests of the exception-construction code through untimed get():
comment|// Mostly an example of how it would look like to use a list of mixed types
DECL|method|failureWithCause (Throwable cause, String message)
specifier|static
name|AssertionFailedError
name|failureWithCause
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|AssertionFailedError
name|failure
init|=
operator|new
name|AssertionFailedError
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|failure
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
return|return
name|failure
return|;
block|}
comment|/** A future that throws a runtime exception from get. */
DECL|class|BuggyFuture
specifier|static
class|class
name|BuggyFuture
extends|extends
name|AbstractFuture
argument_list|<
name|String
argument_list|>
block|{
DECL|method|get ()
annotation|@
name|Override
specifier|public
name|String
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
DECL|method|set (String v)
annotation|@
name|Override
specifier|public
name|boolean
name|set
parameter_list|(
name|String
name|v
parameter_list|)
block|{
return|return
name|super
operator|.
name|set
argument_list|(
name|v
argument_list|)
return|;
block|}
block|}
comment|// This test covers a bug where an Error thrown from a callback could cause the TimeoutFuture to
comment|// never complete when timing out.  Notably, nothing would get logged since the Error would get
comment|// stuck in the ScheduledFuture inside of TimeoutFuture and nothing ever calls get on it.
comment|// Simulate a timeout that fires before the call the SES.schedule returns but the future is
comment|// already completed.
block|}
end_class

end_unit

