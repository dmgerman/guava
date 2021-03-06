begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing.anotherpackage
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|anotherpackage
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Equivalence
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
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
name|Predicate
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
name|Ordering
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
name|primitives
operator|.
name|UnsignedInteger
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
name|primitives
operator|.
name|UnsignedLong
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
name|ForwardingWrapperTester
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|regex
operator|.
name|Pattern
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link ForwardingWrapperTester}. Live in a different package to detect reflection  * access issues, if any.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|ForwardingWrapperTesterTest
specifier|public
class|class
name|ForwardingWrapperTesterTest
extends|extends
name|TestCase
block|{
DECL|field|tester
specifier|private
specifier|final
name|ForwardingWrapperTester
name|tester
init|=
operator|new
name|ForwardingWrapperTester
argument_list|()
decl_stmt|;
DECL|method|testGoodForwarder ()
specifier|public
name|void
name|testGoodForwarder
parameter_list|()
block|{
name|tester
operator|.
name|testForwarding
argument_list|(
name|Arithmetic
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Arithmetic
argument_list|,
name|Arithmetic
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Arithmetic
name|apply
parameter_list|(
name|Arithmetic
name|arithmetic
parameter_list|)
block|{
return|return
operator|new
name|ForwardingArithmetic
argument_list|(
name|arithmetic
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testForwarding
argument_list|(
name|ParameterTypesDifferent
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|ParameterTypesDifferent
argument_list|,
name|ParameterTypesDifferent
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ParameterTypesDifferent
name|apply
parameter_list|(
name|ParameterTypesDifferent
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ParameterTypesDifferentForwarder
argument_list|(
name|delegate
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testVoidMethodForwarding ()
specifier|public
name|void
name|testVoidMethodForwarding
parameter_list|()
block|{
name|tester
operator|.
name|testForwarding
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringForwarding ()
specifier|public
name|void
name|testToStringForwarding
parameter_list|()
block|{
name|tester
operator|.
name|testForwarding
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|runnable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForwardToString ()
specifier|public
name|void
name|testFailsToForwardToString
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"toString()"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForwardHashCode ()
specifier|public
name|void
name|testFailsToForwardHashCode
parameter_list|()
block|{
name|tester
operator|.
name|includingEquals
argument_list|()
expr_stmt|;
name|assertFailure
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"EqualsHashCode"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|ForwardingRunnable
condition|)
block|{
name|ForwardingRunnable
name|that
init|=
operator|(
name|ForwardingRunnable
operator|)
name|o
decl_stmt|;
return|return
name|runnable
operator|.
name|equals
argument_list|(
name|that
operator|.
name|runnable
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"Runnable"
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualsAndHashCodeForwarded ()
specifier|public
name|void
name|testEqualsAndHashCodeForwarded
parameter_list|()
block|{
name|tester
operator|.
name|includingEquals
argument_list|()
expr_stmt|;
name|tester
operator|.
name|testForwarding
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|ForwardingRunnable
condition|)
block|{
name|ForwardingRunnable
name|that
init|=
operator|(
name|ForwardingRunnable
operator|)
name|o
decl_stmt|;
return|return
name|runnable
operator|.
name|equals
argument_list|(
name|that
operator|.
name|runnable
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|runnable
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForwardEquals ()
specifier|public
name|void
name|testFailsToForwardEquals
parameter_list|()
block|{
name|tester
operator|.
name|includingEquals
argument_list|()
expr_stmt|;
name|assertFailure
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|runnable
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"Runnable"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForward ()
specifier|public
name|void
name|testFailsToForward
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
operator|new
name|ForwardingRunnable
argument_list|(
name|runnable
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{}
block|}
return|;
block|}
block|}
argument_list|,
literal|"run()"
argument_list|,
literal|"Failed to forward"
argument_list|)
expr_stmt|;
block|}
DECL|method|testRedundantForwarding ()
specifier|public
name|void
name|testRedundantForwarding
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Runnable
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Runnable
argument_list|,
name|Runnable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Runnable
name|apply
parameter_list|(
specifier|final
name|Runnable
name|runnable
parameter_list|)
block|{
return|return
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
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
argument_list|,
literal|"run()"
argument_list|,
literal|"invoked more than once"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForwardParameters ()
specifier|public
name|void
name|testFailsToForwardParameters
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Adder
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Adder
argument_list|,
name|Adder
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Adder
name|apply
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
return|return
operator|new
name|FailsToForwardParameters
argument_list|(
name|adder
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|"add("
argument_list|,
literal|"Parameter #0"
argument_list|)
expr_stmt|;
block|}
DECL|method|testForwardsToTheWrongMethod ()
specifier|public
name|void
name|testForwardsToTheWrongMethod
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Arithmetic
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Arithmetic
argument_list|,
name|Arithmetic
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Arithmetic
name|apply
parameter_list|(
name|Arithmetic
name|adder
parameter_list|)
block|{
return|return
operator|new
name|ForwardsToTheWrongMethod
argument_list|(
name|adder
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|"minus"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToForwardReturnValue ()
specifier|public
name|void
name|testFailsToForwardReturnValue
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Adder
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Adder
argument_list|,
name|Adder
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Adder
name|apply
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
return|return
operator|new
name|FailsToForwardReturnValue
argument_list|(
name|adder
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|"add("
argument_list|,
literal|"Return value"
argument_list|)
expr_stmt|;
block|}
DECL|method|testFailsToPropagateException ()
specifier|public
name|void
name|testFailsToPropagateException
parameter_list|()
block|{
name|assertFailure
argument_list|(
name|Adder
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Adder
argument_list|,
name|Adder
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Adder
name|apply
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
return|return
operator|new
name|FailsToPropagageException
argument_list|(
name|adder
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|"add("
argument_list|,
literal|"exception"
argument_list|)
expr_stmt|;
block|}
DECL|method|testNotInterfaceType ()
specifier|public
name|void
name|testNotInterfaceType
parameter_list|()
block|{
try|try
block|{
operator|new
name|ForwardingWrapperTester
argument_list|()
operator|.
name|testForwarding
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Functions
operator|.
expr|<
name|String
operator|>
name|identity
argument_list|()
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
block|{     }
block|}
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|setDefault
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|Runnable
operator|.
name|class
argument_list|)
operator|.
name|testAllPublicInstanceMethods
argument_list|(
operator|new
name|ForwardingWrapperTester
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertFailure ( Class<T> interfaceType, Function<T, ? extends T> wrapperFunction, String... expectedMessages)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|assertFailure
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|,
name|String
modifier|...
name|expectedMessages
parameter_list|)
block|{
try|try
block|{
name|tester
operator|.
name|testForwarding
argument_list|(
name|interfaceType
argument_list|,
name|wrapperFunction
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
for|for
control|(
name|String
name|message
range|:
name|expectedMessages
control|)
block|{
name|assertThat
argument_list|(
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|fail
argument_list|(
literal|"expected failure not reported"
argument_list|)
expr_stmt|;
block|}
DECL|class|ForwardingRunnable
specifier|private
class|class
name|ForwardingRunnable
implements|implements
name|Runnable
block|{
DECL|field|runnable
specifier|private
specifier|final
name|Runnable
name|runnable
decl_stmt|;
DECL|method|ForwardingRunnable (Runnable runnable)
name|ForwardingRunnable
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|this
operator|.
name|runnable
operator|=
name|runnable
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
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
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
name|runnable
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|interface|Adder
specifier|private
interface|interface
name|Adder
block|{
DECL|method|add (int a, int b)
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
function_decl|;
block|}
DECL|class|ForwardingArithmetic
specifier|private
specifier|static
class|class
name|ForwardingArithmetic
implements|implements
name|Arithmetic
block|{
DECL|field|arithmetic
specifier|private
specifier|final
name|Arithmetic
name|arithmetic
decl_stmt|;
DECL|method|ForwardingArithmetic (Arithmetic arithmetic)
specifier|public
name|ForwardingArithmetic
parameter_list|(
name|Arithmetic
name|arithmetic
parameter_list|)
block|{
name|this
operator|.
name|arithmetic
operator|=
name|arithmetic
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (int a, int b)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|arithmetic
operator|.
name|add
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|minus (int a, int b)
specifier|public
name|int
name|minus
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|arithmetic
operator|.
name|minus
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
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
name|arithmetic
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|FailsToForwardParameters
specifier|private
specifier|static
class|class
name|FailsToForwardParameters
implements|implements
name|Adder
block|{
DECL|field|adder
specifier|private
specifier|final
name|Adder
name|adder
decl_stmt|;
DECL|method|FailsToForwardParameters (Adder adder)
name|FailsToForwardParameters
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
name|this
operator|.
name|adder
operator|=
name|adder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (int a, int b)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|adder
operator|.
name|add
argument_list|(
name|b
argument_list|,
name|a
argument_list|)
return|;
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
name|adder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|FailsToForwardReturnValue
specifier|private
specifier|static
class|class
name|FailsToForwardReturnValue
implements|implements
name|Adder
block|{
DECL|field|adder
specifier|private
specifier|final
name|Adder
name|adder
decl_stmt|;
DECL|method|FailsToForwardReturnValue (Adder adder)
name|FailsToForwardReturnValue
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
name|this
operator|.
name|adder
operator|=
name|adder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (int a, int b)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|adder
operator|.
name|add
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
operator|+
literal|1
return|;
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
name|adder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|class|FailsToPropagageException
specifier|private
specifier|static
class|class
name|FailsToPropagageException
implements|implements
name|Adder
block|{
DECL|field|adder
specifier|private
specifier|final
name|Adder
name|adder
decl_stmt|;
DECL|method|FailsToPropagageException (Adder adder)
name|FailsToPropagageException
parameter_list|(
name|Adder
name|adder
parameter_list|)
block|{
name|this
operator|.
name|adder
operator|=
name|adder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (int a, int b)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
try|try
block|{
return|return
name|adder
operator|.
name|add
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// swallow!
return|return
literal|0
return|;
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
name|adder
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|interface|Arithmetic
specifier|public
interface|interface
name|Arithmetic
extends|extends
name|Adder
block|{
DECL|method|minus (int a, int b)
name|int
name|minus
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
function_decl|;
block|}
DECL|class|ForwardsToTheWrongMethod
specifier|private
specifier|static
class|class
name|ForwardsToTheWrongMethod
implements|implements
name|Arithmetic
block|{
DECL|field|arithmetic
specifier|private
specifier|final
name|Arithmetic
name|arithmetic
decl_stmt|;
DECL|method|ForwardsToTheWrongMethod (Arithmetic arithmetic)
name|ForwardsToTheWrongMethod
parameter_list|(
name|Arithmetic
name|arithmetic
parameter_list|)
block|{
name|this
operator|.
name|arithmetic
operator|=
name|arithmetic
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|minus (int a, int b)
specifier|public
name|int
name|minus
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
comment|// bad!
return|return
name|arithmetic
operator|.
name|add
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|add (int a, int b)
specifier|public
name|int
name|add
parameter_list|(
name|int
name|a
parameter_list|,
name|int
name|b
parameter_list|)
block|{
return|return
name|arithmetic
operator|.
name|add
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
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
name|arithmetic
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|interface|ParameterTypesDifferent
specifier|private
interface|interface
name|ParameterTypesDifferent
block|{
DECL|method|foo ( String s, Runnable r, Number n, Iterable<?> it, boolean b, Equivalence<String> eq, Exception e, InputStream in, Comparable<?> c, Ordering<Integer> ord, Charset charset, TimeUnit unit, Class<?> cls, Joiner joiner, Pattern pattern, UnsignedInteger ui, UnsignedLong ul, StringBuilder sb, Predicate<?> pred, Function<?, ?> func, Object obj)
name|void
name|foo
parameter_list|(
name|String
name|s
parameter_list|,
name|Runnable
name|r
parameter_list|,
name|Number
name|n
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|it
parameter_list|,
name|boolean
name|b
parameter_list|,
name|Equivalence
argument_list|<
name|String
argument_list|>
name|eq
parameter_list|,
name|Exception
name|e
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|Comparable
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Ordering
argument_list|<
name|Integer
argument_list|>
name|ord
parameter_list|,
name|Charset
name|charset
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Joiner
name|joiner
parameter_list|,
name|Pattern
name|pattern
parameter_list|,
name|UnsignedInteger
name|ui
parameter_list|,
name|UnsignedLong
name|ul
parameter_list|,
name|StringBuilder
name|sb
parameter_list|,
name|Predicate
argument_list|<
name|?
argument_list|>
name|pred
parameter_list|,
name|Function
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|func
parameter_list|,
name|Object
name|obj
parameter_list|)
function_decl|;
block|}
DECL|class|ParameterTypesDifferentForwarder
specifier|private
specifier|static
class|class
name|ParameterTypesDifferentForwarder
implements|implements
name|ParameterTypesDifferent
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ParameterTypesDifferent
name|delegate
decl_stmt|;
DECL|method|ParameterTypesDifferentForwarder (ParameterTypesDifferent delegate)
specifier|public
name|ParameterTypesDifferentForwarder
parameter_list|(
name|ParameterTypesDifferent
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
annotation|@
name|Override
DECL|method|foo ( String s, Runnable r, Number n, Iterable<?> it, boolean b, Equivalence<String> eq, Exception e, InputStream in, Comparable<?> c, Ordering<Integer> ord, Charset charset, TimeUnit unit, Class<?> cls, Joiner joiner, Pattern pattern, UnsignedInteger ui, UnsignedLong ul, StringBuilder sb, Predicate<?> pred, Function<?, ?> func, Object obj)
specifier|public
name|void
name|foo
parameter_list|(
name|String
name|s
parameter_list|,
name|Runnable
name|r
parameter_list|,
name|Number
name|n
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|it
parameter_list|,
name|boolean
name|b
parameter_list|,
name|Equivalence
argument_list|<
name|String
argument_list|>
name|eq
parameter_list|,
name|Exception
name|e
parameter_list|,
name|InputStream
name|in
parameter_list|,
name|Comparable
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Ordering
argument_list|<
name|Integer
argument_list|>
name|ord
parameter_list|,
name|Charset
name|charset
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Joiner
name|joiner
parameter_list|,
name|Pattern
name|pattern
parameter_list|,
name|UnsignedInteger
name|ui
parameter_list|,
name|UnsignedLong
name|ul
parameter_list|,
name|StringBuilder
name|sb
parameter_list|,
name|Predicate
argument_list|<
name|?
argument_list|>
name|pred
parameter_list|,
name|Function
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|func
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
name|delegate
operator|.
name|foo
argument_list|(
name|s
argument_list|,
name|r
argument_list|,
name|n
argument_list|,
name|it
argument_list|,
name|b
argument_list|,
name|eq
argument_list|,
name|e
argument_list|,
name|in
argument_list|,
name|c
argument_list|,
name|ord
argument_list|,
name|charset
argument_list|,
name|unit
argument_list|,
name|cls
argument_list|,
name|joiner
argument_list|,
name|pattern
argument_list|,
name|ui
argument_list|,
name|ul
argument_list|,
name|sb
argument_list|,
name|pred
argument_list|,
name|func
argument_list|,
name|obj
argument_list|)
expr_stmt|;
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
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|method|testCovariantReturn ()
specifier|public
name|void
name|testCovariantReturn
parameter_list|()
block|{
operator|new
name|ForwardingWrapperTester
argument_list|()
operator|.
name|testForwarding
argument_list|(
name|Sub
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|Sub
argument_list|,
name|Sub
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Sub
name|apply
parameter_list|(
name|Sub
name|sub
parameter_list|)
block|{
return|return
operator|new
name|ForwardingSub
argument_list|(
name|sub
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|interface|Base
interface|interface
name|Base
block|{
DECL|method|getId ()
name|CharSequence
name|getId
parameter_list|()
function_decl|;
block|}
DECL|interface|Sub
interface|interface
name|Sub
extends|extends
name|Base
block|{
annotation|@
name|Override
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
block|}
DECL|class|ForwardingSub
specifier|private
specifier|static
class|class
name|ForwardingSub
implements|implements
name|Sub
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Sub
name|delegate
decl_stmt|;
DECL|method|ForwardingSub (Sub delegate)
name|ForwardingSub
parameter_list|(
name|Sub
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
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getId
argument_list|()
return|;
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
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|interface|Equals
specifier|private
interface|interface
name|Equals
block|{
annotation|@
name|Override
DECL|method|equals (Object obj)
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|hashCode ()
name|int
name|hashCode
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|toString ()
name|String
name|toString
parameter_list|()
function_decl|;
block|}
DECL|class|NoDelegateToEquals
specifier|private
specifier|static
class|class
name|NoDelegateToEquals
implements|implements
name|Equals
block|{
DECL|field|WRAPPER
specifier|private
specifier|static
name|Function
argument_list|<
name|Equals
argument_list|,
name|Equals
argument_list|>
name|WRAPPER
init|=
operator|new
name|Function
argument_list|<
name|Equals
argument_list|,
name|Equals
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|NoDelegateToEquals
name|apply
parameter_list|(
name|Equals
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|NoDelegateToEquals
argument_list|(
name|delegate
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|field|delegate
specifier|private
specifier|final
name|Equals
name|delegate
decl_stmt|;
DECL|method|NoDelegateToEquals (Equals delegate)
name|NoDelegateToEquals
parameter_list|(
name|Equals
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|method|testExplicitEqualsAndHashCodeNotDelegatedByDefault ()
specifier|public
name|void
name|testExplicitEqualsAndHashCodeNotDelegatedByDefault
parameter_list|()
block|{
operator|new
name|ForwardingWrapperTester
argument_list|()
operator|.
name|testForwarding
argument_list|(
name|Equals
operator|.
name|class
argument_list|,
name|NoDelegateToEquals
operator|.
name|WRAPPER
argument_list|)
expr_stmt|;
block|}
DECL|method|testExplicitEqualsAndHashCodeDelegatedWhenExplicitlyAsked ()
specifier|public
name|void
name|testExplicitEqualsAndHashCodeDelegatedWhenExplicitlyAsked
parameter_list|()
block|{
try|try
block|{
operator|new
name|ForwardingWrapperTester
argument_list|()
operator|.
name|includingEquals
argument_list|()
operator|.
name|testForwarding
argument_list|(
name|Equals
operator|.
name|class
argument_list|,
name|NoDelegateToEquals
operator|.
name|WRAPPER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|expected
parameter_list|)
block|{
return|return;
block|}
name|fail
argument_list|(
literal|"Should have failed"
argument_list|)
expr_stmt|;
block|}
comment|/** An interface for the 2 ways that a chaining call might be defined. */
DECL|interface|ChainingCalls
specifier|private
interface|interface
name|ChainingCalls
block|{
comment|// A method that is defined to 'return this'
DECL|method|chainingCall ()
name|ChainingCalls
name|chainingCall
parameter_list|()
function_decl|;
comment|// A method that just happens to return a ChainingCalls object
DECL|method|nonChainingCall ()
name|ChainingCalls
name|nonChainingCall
parameter_list|()
function_decl|;
block|}
DECL|class|ForwardingChainingCalls
specifier|private
specifier|static
class|class
name|ForwardingChainingCalls
implements|implements
name|ChainingCalls
block|{
DECL|field|delegate
specifier|final
name|ChainingCalls
name|delegate
decl_stmt|;
DECL|method|ForwardingChainingCalls (ChainingCalls delegate)
name|ForwardingChainingCalls
parameter_list|(
name|ChainingCalls
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
annotation|@
name|Override
DECL|method|chainingCall ()
specifier|public
name|ForwardingChainingCalls
name|chainingCall
parameter_list|()
block|{
name|delegate
operator|.
name|chainingCall
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|nonChainingCall ()
specifier|public
name|ChainingCalls
name|nonChainingCall
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|nonChainingCall
argument_list|()
return|;
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
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|method|testChainingCalls ()
specifier|public
name|void
name|testChainingCalls
parameter_list|()
block|{
name|tester
operator|.
name|testForwarding
argument_list|(
name|ChainingCalls
operator|.
name|class
argument_list|,
operator|new
name|Function
argument_list|<
name|ChainingCalls
argument_list|,
name|ChainingCalls
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChainingCalls
name|apply
parameter_list|(
name|ChainingCalls
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ForwardingChainingCalls
argument_list|(
name|delegate
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

