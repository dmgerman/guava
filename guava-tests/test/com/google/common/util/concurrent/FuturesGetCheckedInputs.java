begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
operator|.
name|immediateFailedFuture
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

begin_comment
comment|/**  * Classes and futures used in {@link FuturesGetCheckedTest} and {@link FuturesGetUncheckedTest}.  */
end_comment

begin_class
DECL|class|FuturesGetCheckedInputs
specifier|final
class|class
name|FuturesGetCheckedInputs
block|{
DECL|field|CHECKED_EXCEPTION
specifier|static
specifier|final
name|Exception
name|CHECKED_EXCEPTION
init|=
operator|new
name|Exception
argument_list|(
literal|"mymessage"
argument_list|)
decl_stmt|;
DECL|field|FAILED_FUTURE_CHECKED_EXCEPTION
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|FAILED_FUTURE_CHECKED_EXCEPTION
init|=
name|immediateFailedFuture
argument_list|(
name|CHECKED_EXCEPTION
argument_list|)
decl_stmt|;
DECL|field|UNCHECKED_EXCEPTION
specifier|static
specifier|final
name|RuntimeException
name|UNCHECKED_EXCEPTION
init|=
operator|new
name|RuntimeException
argument_list|(
literal|"mymessage"
argument_list|)
decl_stmt|;
DECL|field|FAILED_FUTURE_UNCHECKED_EXCEPTION
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|FAILED_FUTURE_UNCHECKED_EXCEPTION
init|=
name|immediateFailedFuture
argument_list|(
name|UNCHECKED_EXCEPTION
argument_list|)
decl_stmt|;
DECL|field|RUNTIME_EXCEPTION
specifier|static
specifier|final
name|RuntimeException
name|RUNTIME_EXCEPTION
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
DECL|field|OTHER_THROWABLE
specifier|static
specifier|final
name|OtherThrowable
name|OTHER_THROWABLE
init|=
operator|new
name|OtherThrowable
argument_list|()
decl_stmt|;
DECL|field|FAILED_FUTURE_OTHER_THROWABLE
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|FAILED_FUTURE_OTHER_THROWABLE
init|=
name|immediateFailedFuture
argument_list|(
name|OTHER_THROWABLE
argument_list|)
decl_stmt|;
DECL|field|ERROR
specifier|static
specifier|final
name|Error
name|ERROR
init|=
operator|new
name|Error
argument_list|(
literal|"mymessage"
argument_list|)
decl_stmt|;
DECL|field|FAILED_FUTURE_ERROR
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|FAILED_FUTURE_ERROR
init|=
name|immediateFailedFuture
argument_list|(
name|ERROR
argument_list|)
decl_stmt|;
DECL|field|RUNTIME_EXCEPTION_FUTURE
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|RUNTIME_EXCEPTION_FUTURE
init|=
name|UncheckedThrowingFuture
operator|.
name|throwingRuntimeException
argument_list|(
name|RUNTIME_EXCEPTION
argument_list|)
decl_stmt|;
DECL|field|ERROR_FUTURE
specifier|static
specifier|final
name|Future
argument_list|<
name|String
argument_list|>
name|ERROR_FUTURE
init|=
name|UncheckedThrowingFuture
operator|.
name|throwingError
argument_list|(
name|ERROR
argument_list|)
decl_stmt|;
DECL|class|TwoArgConstructorException
specifier|public
specifier|static
specifier|final
class|class
name|TwoArgConstructorException
extends|extends
name|Exception
block|{
DECL|method|TwoArgConstructorException (String message, Throwable cause)
specifier|public
name|TwoArgConstructorException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|TwoArgConstructorRuntimeException
specifier|public
specifier|static
specifier|final
class|class
name|TwoArgConstructorRuntimeException
extends|extends
name|RuntimeException
block|{
DECL|method|TwoArgConstructorRuntimeException (String message, Throwable cause)
specifier|public
name|TwoArgConstructorRuntimeException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExceptionWithPrivateConstructor
specifier|public
specifier|static
specifier|final
class|class
name|ExceptionWithPrivateConstructor
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithPrivateConstructor (String message, Throwable cause)
specifier|private
name|ExceptionWithPrivateConstructor
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// we're testing that they're not used
DECL|class|ExceptionWithSomePrivateConstructors
specifier|public
specifier|static
specifier|final
class|class
name|ExceptionWithSomePrivateConstructors
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithSomePrivateConstructors (String a)
specifier|private
name|ExceptionWithSomePrivateConstructors
parameter_list|(
name|String
name|a
parameter_list|)
block|{}
DECL|method|ExceptionWithSomePrivateConstructors (String a, String b)
specifier|private
name|ExceptionWithSomePrivateConstructors
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{}
DECL|method|ExceptionWithSomePrivateConstructors (String a, String b, String c)
specifier|public
name|ExceptionWithSomePrivateConstructors
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|,
name|String
name|c
parameter_list|)
block|{}
DECL|method|ExceptionWithSomePrivateConstructors (String a, String b, String c, String d)
specifier|private
name|ExceptionWithSomePrivateConstructors
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|,
name|String
name|c
parameter_list|,
name|String
name|d
parameter_list|)
block|{}
DECL|method|ExceptionWithSomePrivateConstructors ( String a, String b, String c, String d, String e)
specifier|private
name|ExceptionWithSomePrivateConstructors
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|,
name|String
name|c
parameter_list|,
name|String
name|d
parameter_list|,
name|String
name|e
parameter_list|)
block|{}
block|}
DECL|class|ExceptionWithManyConstructors
specifier|public
specifier|static
specifier|final
class|class
name|ExceptionWithManyConstructors
extends|extends
name|Exception
block|{
DECL|field|usedExpectedConstructor
name|boolean
name|usedExpectedConstructor
decl_stmt|;
DECL|method|ExceptionWithManyConstructors ()
specifier|public
name|ExceptionWithManyConstructors
parameter_list|()
block|{}
DECL|method|ExceptionWithManyConstructors (Integer i)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Integer
name|i
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors (Throwable a)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors (Throwable a, Throwable b)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|,
name|Throwable
name|b
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors (String s, Throwable b)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|String
name|s
parameter_list|,
name|Throwable
name|b
parameter_list|)
block|{
name|usedExpectedConstructor
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|ExceptionWithManyConstructors (Throwable a, Throwable b, Throwable c)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|,
name|Throwable
name|b
parameter_list|,
name|Throwable
name|c
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors (Throwable a, Throwable b, Throwable c, Throwable d)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|,
name|Throwable
name|b
parameter_list|,
name|Throwable
name|c
parameter_list|,
name|Throwable
name|d
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors ( Throwable a, Throwable b, Throwable c, Throwable d, Throwable e)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|,
name|Throwable
name|b
parameter_list|,
name|Throwable
name|c
parameter_list|,
name|Throwable
name|d
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{}
DECL|method|ExceptionWithManyConstructors ( Throwable a, Throwable b, Throwable c, Throwable d, Throwable e, String s, Integer i)
specifier|public
name|ExceptionWithManyConstructors
parameter_list|(
name|Throwable
name|a
parameter_list|,
name|Throwable
name|b
parameter_list|,
name|Throwable
name|c
parameter_list|,
name|Throwable
name|d
parameter_list|,
name|Throwable
name|e
parameter_list|,
name|String
name|s
parameter_list|,
name|Integer
name|i
parameter_list|)
block|{}
block|}
DECL|class|ExceptionWithoutThrowableConstructor
specifier|public
specifier|static
specifier|final
class|class
name|ExceptionWithoutThrowableConstructor
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithoutThrowableConstructor (String s)
specifier|public
name|ExceptionWithoutThrowableConstructor
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExceptionWithWrongTypesConstructor
specifier|public
specifier|static
specifier|final
class|class
name|ExceptionWithWrongTypesConstructor
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithWrongTypesConstructor (Integer i, String s)
specifier|public
name|ExceptionWithWrongTypesConstructor
parameter_list|(
name|Integer
name|i
parameter_list|,
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExceptionWithGoodAndBadConstructor
specifier|static
specifier|final
class|class
name|ExceptionWithGoodAndBadConstructor
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithGoodAndBadConstructor (String message, Throwable cause)
specifier|public
name|ExceptionWithGoodAndBadConstructor
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"bad constructor"
argument_list|)
throw|;
block|}
DECL|method|ExceptionWithGoodAndBadConstructor (Throwable cause)
specifier|public
name|ExceptionWithGoodAndBadConstructor
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ExceptionWithBadConstructor
specifier|static
specifier|final
class|class
name|ExceptionWithBadConstructor
extends|extends
name|Exception
block|{
DECL|method|ExceptionWithBadConstructor (String message, Throwable cause)
specifier|public
name|ExceptionWithBadConstructor
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"bad constructor"
argument_list|)
throw|;
block|}
block|}
DECL|class|OtherThrowable
specifier|static
specifier|final
class|class
name|OtherThrowable
extends|extends
name|Throwable
block|{}
DECL|method|FuturesGetCheckedInputs ()
specifier|private
name|FuturesGetCheckedInputs
parameter_list|()
block|{}
block|}
end_class

end_unit

