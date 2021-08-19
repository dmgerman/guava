begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
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
name|collect
operator|.
name|Lists
operator|.
name|newArrayList
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
name|immediateFailedFuture
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
name|FuturesGetChecked
operator|.
name|checkExceptionClassValidity
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
name|FuturesGetChecked
operator|.
name|getChecked
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
name|FuturesGetChecked
operator|.
name|isCheckedException
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
name|FuturesGetChecked
operator|.
name|weakSetValidator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|BeforeExperiment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Benchmark
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|caliper
operator|.
name|Param
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
name|ImmutableSet
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
name|FuturesGetChecked
operator|.
name|GetCheckedTypeValidator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|acl
operator|.
name|NotOwnerException
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
name|TooManyListenersException
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
name|BrokenBarrierException
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|BackingStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|prefs
operator|.
name|InvalidPreferencesFormatException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DataFormatException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|RefreshFailedException
import|;
end_import

begin_comment
comment|/** Microbenchmark for {@link Futures#getChecked}. */
end_comment

begin_class
DECL|class|FuturesGetCheckedBenchmark
specifier|public
class|class
name|FuturesGetCheckedBenchmark
block|{
DECL|enum|Validator
specifier|private
enum|enum
name|Validator
block|{
DECL|enumConstant|NON_CACHING_WITH_CONSTRUCTOR_CHECK
DECL|enumConstant|nonCachingWithConstructorCheckValidator
name|NON_CACHING_WITH_CONSTRUCTOR_CHECK
argument_list|(
name|nonCachingWithConstructorCheckValidator
argument_list|()
argument_list|)
block|,
DECL|enumConstant|NON_CACHING_WITHOUT_CONSTRUCTOR_CHECK
DECL|enumConstant|nonCachingWithoutConstructorCheckValidator
name|NON_CACHING_WITHOUT_CONSTRUCTOR_CHECK
argument_list|(
name|nonCachingWithoutConstructorCheckValidator
argument_list|()
argument_list|)
block|,
DECL|enumConstant|WEAK_SET
DECL|enumConstant|weakSetValidator
name|WEAK_SET
argument_list|(
name|weakSetValidator
argument_list|()
argument_list|)
block|,     ;
DECL|field|validator
specifier|final
name|GetCheckedTypeValidator
name|validator
decl_stmt|;
DECL|method|Validator (GetCheckedTypeValidator validator)
name|Validator
parameter_list|(
name|GetCheckedTypeValidator
name|validator
parameter_list|)
block|{
name|this
operator|.
name|validator
operator|=
name|validator
expr_stmt|;
block|}
block|}
DECL|enum|Result
specifier|private
enum|enum
name|Result
block|{
DECL|enumConstant|SUCCESS
DECL|enumConstant|immediateFuture
name|SUCCESS
argument_list|(
name|immediateFuture
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
block|,
DECL|enumConstant|FAILURE
DECL|enumConstant|immediateFailedFuture
name|FAILURE
argument_list|(
name|immediateFailedFuture
argument_list|(
operator|new
name|Exception
argument_list|()
argument_list|)
argument_list|)
block|;
DECL|field|future
specifier|final
name|Future
argument_list|<
name|Object
argument_list|>
name|future
decl_stmt|;
DECL|method|Result (Future<Object> result)
name|Result
parameter_list|(
name|Future
argument_list|<
name|Object
argument_list|>
name|result
parameter_list|)
block|{
name|this
operator|.
name|future
operator|=
name|result
expr_stmt|;
block|}
block|}
DECL|enum|ExceptionType
specifier|private
enum|enum
name|ExceptionType
block|{
DECL|enumConstant|CHECKED
name|CHECKED
parameter_list|(
name|IOException
operator|.
name|class
parameter_list|)
operator|,
DECL|enumConstant|UNCHECKED
constructor|UNCHECKED(RuntimeException.class
block|)
enum|;
DECL|field|exceptionType
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionType
decl_stmt|;
DECL|method|ExceptionType (Class<? extends Exception> exceptionType)
name|ExceptionType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionType
parameter_list|)
block|{
name|this
operator|.
name|exceptionType
operator|=
name|exceptionType
expr_stmt|;
block|}
block|}
end_class

begin_decl_stmt
DECL|field|OTHER_EXCEPTION_TYPES
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
name|OTHER_EXCEPTION_TYPES
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|BackingStoreException
operator|.
name|class
argument_list|,
name|BrokenBarrierException
operator|.
name|class
argument_list|,
name|CloneNotSupportedException
operator|.
name|class
argument_list|,
name|DataFormatException
operator|.
name|class
argument_list|,
name|ExecutionException
operator|.
name|class
argument_list|,
name|GeneralSecurityException
operator|.
name|class
argument_list|,
name|InvalidPreferencesFormatException
operator|.
name|class
argument_list|,
name|NotOwnerException
operator|.
name|class
argument_list|,
name|RefreshFailedException
operator|.
name|class
argument_list|,
name|TimeoutException
operator|.
name|class
argument_list|,
name|TooManyListenersException
operator|.
name|class
argument_list|,
name|URISyntaxException
operator|.
name|class
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|validator
annotation|@
name|Param
name|Validator
name|validator
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|result
annotation|@
name|Param
name|Result
name|result
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|exceptionType
annotation|@
name|Param
name|ExceptionType
name|exceptionType
decl_stmt|;
end_decl_stmt

begin_comment
comment|/**    * The number of other exception types in the cache of known-good exceptions and the number of    * other {@code ClassValue} entries for the exception type to be tested. This lets us evaluate    * whether our solution scales to use with multiple exception types and to whether it is affected    * by other {@code ClassValue} users. Some of the benchmarked implementations don't use one or    * both of these mechanisms, so they will be unaffected.    */
end_comment

begin_decl_stmt
annotation|@
name|Param
argument_list|(
block|{
literal|"0"
block|,
literal|"1"
block|,
literal|"12"
block|}
argument_list|)
DECL|field|otherEntriesInDataStructure
name|int
name|otherEntriesInDataStructure
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|retainedReferencesToOtherClassValues
specifier|final
name|List
argument_list|<
name|ClassValue
argument_list|<
name|?
argument_list|>
argument_list|>
name|retainedReferencesToOtherClassValues
init|=
name|newArrayList
argument_list|()
decl_stmt|;
end_decl_stmt

begin_function
annotation|@
name|BeforeExperiment
DECL|method|addOtherEntries ()
name|void
name|addOtherEntries
parameter_list|()
throws|throws
name|Exception
block|{
name|GetCheckedTypeValidator
name|validator
init|=
name|this
operator|.
name|validator
operator|.
name|validator
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionType
init|=
name|this
operator|.
name|exceptionType
operator|.
name|exceptionType
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionClass
range|:
name|OTHER_EXCEPTION_TYPES
operator|.
name|asList
argument_list|()
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|otherEntriesInDataStructure
argument_list|)
control|)
block|{
name|getChecked
argument_list|(
name|validator
argument_list|,
name|immediateFuture
argument_list|(
literal|""
argument_list|)
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|otherEntriesInDataStructure
condition|;
name|i
operator|++
control|)
block|{
name|ClassValue
argument_list|<
name|Boolean
argument_list|>
name|classValue
init|=
operator|new
name|ClassValue
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Boolean
name|computeValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
name|classValue
operator|.
name|get
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
name|retainedReferencesToOtherClassValues
operator|.
name|add
argument_list|(
name|classValue
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Benchmark
DECL|method|benchmarkGetChecked (int reps)
name|int
name|benchmarkGetChecked
parameter_list|(
name|int
name|reps
parameter_list|)
block|{
name|int
name|tmp
init|=
literal|0
decl_stmt|;
name|GetCheckedTypeValidator
name|validator
init|=
name|this
operator|.
name|validator
operator|.
name|validator
decl_stmt|;
name|Future
argument_list|<
name|Object
argument_list|>
name|future
init|=
name|this
operator|.
name|result
operator|.
name|future
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionType
init|=
name|this
operator|.
name|exceptionType
operator|.
name|exceptionType
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
name|reps
condition|;
operator|++
name|i
control|)
block|{
try|try
block|{
name|tmp
operator|+=
name|getChecked
argument_list|(
name|validator
argument_list|,
name|future
argument_list|,
name|exceptionType
argument_list|)
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|tmp
operator|+=
name|e
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|tmp
return|;
block|}
end_function

begin_function
DECL|method|nonCachingWithoutConstructorCheckValidator ()
specifier|private
specifier|static
name|GetCheckedTypeValidator
name|nonCachingWithoutConstructorCheckValidator
parameter_list|()
block|{
return|return
name|NonCachingWithoutConstructorCheckValidator
operator|.
name|INSTANCE
return|;
block|}
end_function

begin_enum
DECL|enum|NonCachingWithoutConstructorCheckValidator
specifier|private
enum|enum
name|NonCachingWithoutConstructorCheckValidator
implements|implements
name|GetCheckedTypeValidator
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|validateClass (Class<? extends Exception> exceptionClass)
specifier|public
name|void
name|validateClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionClass
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|isCheckedException
argument_list|(
name|exceptionClass
argument_list|)
argument_list|,
literal|"Futures.getChecked exception type (%s) must not be a RuntimeException"
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
block|}
end_enum

begin_function
DECL|method|nonCachingWithConstructorCheckValidator ()
specifier|private
specifier|static
name|GetCheckedTypeValidator
name|nonCachingWithConstructorCheckValidator
parameter_list|()
block|{
return|return
name|NonCachingWithConstructorCheckValidator
operator|.
name|INSTANCE
return|;
block|}
end_function

begin_enum
DECL|enum|NonCachingWithConstructorCheckValidator
specifier|private
enum|enum
name|NonCachingWithConstructorCheckValidator
implements|implements
name|GetCheckedTypeValidator
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|validateClass (Class<? extends Exception> exceptionClass)
specifier|public
name|void
name|validateClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|exceptionClass
parameter_list|)
block|{
name|checkExceptionClassValidity
argument_list|(
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
block|}
end_enum

unit|}
end_unit

