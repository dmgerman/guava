begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|lang
operator|.
name|Thread
operator|.
name|currentThread
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|annotations
operator|.
name|VisibleForTesting
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|J2ObjCIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|Set
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
name|CopyOnWriteArraySet
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|mojo
operator|.
name|animal_sniffer
operator|.
name|IgnoreJRERequirement
import|;
end_import

begin_comment
comment|/**  * Static methods used to implement {@link Futures#getChecked(Future, Class)}.  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|FuturesGetChecked
specifier|final
class|class
name|FuturesGetChecked
block|{
annotation|@
name|CanIgnoreReturnValue
DECL|method|getChecked (Future<V> future, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|V
name|getChecked
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|)
throws|throws
name|X
block|{
return|return
name|getChecked
argument_list|(
name|bestGetCheckedTypeValidator
argument_list|()
argument_list|,
name|future
argument_list|,
name|exceptionClass
argument_list|)
return|;
block|}
comment|/**    * Implementation of {@link Futures#getChecked(Future, Class)}.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|VisibleForTesting
DECL|method|getChecked ( GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass)
specifier|static
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|V
name|getChecked
parameter_list|(
name|GetCheckedTypeValidator
name|validator
parameter_list|,
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|)
throws|throws
name|X
block|{
name|validator
operator|.
name|validateClass
argument_list|(
name|exceptionClass
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|newWithCause
argument_list|(
name|exceptionClass
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
comment|/**    * Implementation of {@link Futures#getChecked(Future, Class, long, TimeUnit)}.    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getChecked ( Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit)
specifier|static
parameter_list|<
name|V
parameter_list|,
name|X
extends|extends
name|Exception
parameter_list|>
name|V
name|getChecked
parameter_list|(
name|Future
argument_list|<
name|V
argument_list|>
name|future
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|X
block|{
comment|// TODO(cpovirk): benchmark a version of this method that accepts a GetCheckedTypeValidator
name|bestGetCheckedTypeValidator
argument_list|()
operator|.
name|validateClass
argument_list|(
name|exceptionClass
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|newWithCause
argument_list|(
name|exceptionClass
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
throw|throw
name|newWithCause
argument_list|(
name|exceptionClass
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|wrapAndThrowExceptionOrError
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|interface|GetCheckedTypeValidator
interface|interface
name|GetCheckedTypeValidator
block|{
DECL|method|validateClass (Class<? extends Exception> exceptionClass)
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
function_decl|;
block|}
DECL|method|bestGetCheckedTypeValidator ()
specifier|private
specifier|static
name|GetCheckedTypeValidator
name|bestGetCheckedTypeValidator
parameter_list|()
block|{
return|return
name|GetCheckedTypeValidatorHolder
operator|.
name|BEST_VALIDATOR
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|weakSetValidator ()
specifier|static
name|GetCheckedTypeValidator
name|weakSetValidator
parameter_list|()
block|{
return|return
name|GetCheckedTypeValidatorHolder
operator|.
name|WeakSetValidator
operator|.
name|INSTANCE
return|;
block|}
annotation|@
name|J2ObjCIncompatible
comment|// ClassValue
annotation|@
name|VisibleForTesting
DECL|method|classValueValidator ()
specifier|static
name|GetCheckedTypeValidator
name|classValueValidator
parameter_list|()
block|{
return|return
name|GetCheckedTypeValidatorHolder
operator|.
name|ClassValueValidator
operator|.
name|INSTANCE
return|;
block|}
comment|/**    * Provides a check of whether an exception type is valid for use with    * {@link FuturesGetChecked#getChecked(Future, Class)}, possibly using caching.    *    *<p>Uses reflection to gracefully fall back to when certain implementations aren't available.    */
annotation|@
name|VisibleForTesting
DECL|class|GetCheckedTypeValidatorHolder
specifier|static
class|class
name|GetCheckedTypeValidatorHolder
block|{
DECL|field|CLASS_VALUE_VALIDATOR_NAME
specifier|static
specifier|final
name|String
name|CLASS_VALUE_VALIDATOR_NAME
init|=
name|GetCheckedTypeValidatorHolder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"$ClassValueValidator"
decl_stmt|;
DECL|field|BEST_VALIDATOR
specifier|static
specifier|final
name|GetCheckedTypeValidator
name|BEST_VALIDATOR
init|=
name|getBestValidator
argument_list|()
decl_stmt|;
annotation|@
name|IgnoreJRERequirement
comment|// getChecked falls back to another implementation if necessary
annotation|@
name|J2ObjCIncompatible
comment|// ClassValue
DECL|enum|ClassValueValidator
enum|enum
name|ClassValueValidator
implements|implements
name|GetCheckedTypeValidator
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
comment|/*        * Static final fields are presumed to be fastest, based on our experience with        * UnsignedBytesBenchmark. TODO(cpovirk): benchmark this        */
DECL|field|isValidClass
specifier|private
specifier|static
specifier|final
name|ClassValue
argument_list|<
name|Boolean
argument_list|>
name|isValidClass
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
name|checkExceptionClassValidity
argument_list|(
name|type
operator|.
name|asSubclass
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
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
name|isValidClass
operator|.
name|get
argument_list|(
name|exceptionClass
argument_list|)
expr_stmt|;
comment|// throws if invalid; returns safely (and caches) if valid
block|}
block|}
DECL|enum|WeakSetValidator
enum|enum
name|WeakSetValidator
implements|implements
name|GetCheckedTypeValidator
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
comment|/*        * Static final fields are presumed to be fastest, based on our experience with        * UnsignedBytesBenchmark. TODO(cpovirk): benchmark this        */
comment|/*        * A CopyOnWriteArraySet<WeakReference> is faster than a newSetFromMap of a MapMaker map with        * weakKeys() and concurrencyLevel(1), even up to at least 12 cached exception types.        */
DECL|field|validClasses
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
argument_list|>
name|validClasses
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
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
for|for
control|(
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
name|knownGood
range|:
name|validClasses
control|)
block|{
if|if
condition|(
name|exceptionClass
operator|.
name|equals
argument_list|(
name|knownGood
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// TODO(cpovirk): if reference has been cleared, remove it?
block|}
name|checkExceptionClassValidity
argument_list|(
name|exceptionClass
argument_list|)
expr_stmt|;
comment|/*          * It's very unlikely that any loaded Futures class will see getChecked called with more          * than a handful of exceptions. But it seems prudent to set a cap on how many we'll cache.          * This avoids out-of-control memory consumption, and it keeps the cache from growing so          * large that doing the lookup is noticeably slower than redoing the work would be.          *          * Ideally we'd have a real eviction policy, but until we see a problem in practice, I hope          * that this will suffice. I have not even benchmarked with different size limits.          */
if|if
condition|(
name|validClasses
operator|.
name|size
argument_list|()
operator|>
literal|1000
condition|)
block|{
name|validClasses
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|validClasses
operator|.
name|add
argument_list|(
operator|new
name|WeakReference
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
argument_list|>
argument_list|(
name|exceptionClass
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the ClassValue-using validator, or falls back to the "weak Set" implementation if      * unable to do so.      */
DECL|method|getBestValidator ()
specifier|static
name|GetCheckedTypeValidator
name|getBestValidator
parameter_list|()
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|theClass
init|=
name|Class
operator|.
name|forName
argument_list|(
name|CLASS_VALUE_VALIDATOR_NAME
argument_list|)
decl_stmt|;
return|return
operator|(
name|GetCheckedTypeValidator
operator|)
name|theClass
operator|.
name|getEnumConstants
argument_list|()
index|[
literal|0
index|]
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ensure we really catch *everything*
return|return
name|weakSetValidator
argument_list|()
return|;
block|}
block|}
block|}
comment|// TODO(cpovirk): change parameter order to match other helper methods (Class, Throwable)?
DECL|method|wrapAndThrowExceptionOrError ( Throwable cause, Class<X> exceptionClass)
specifier|private
specifier|static
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|void
name|wrapAndThrowExceptionOrError
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|)
throws|throws
name|X
block|{
if|if
condition|(
name|cause
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|new
name|ExecutionError
argument_list|(
operator|(
name|Error
operator|)
name|cause
argument_list|)
throw|;
block|}
if|if
condition|(
name|cause
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|new
name|UncheckedExecutionException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
throw|throw
name|newWithCause
argument_list|(
name|exceptionClass
argument_list|,
name|cause
argument_list|)
throw|;
block|}
comment|/*    * TODO(user): FutureChecker interface for these to be static methods on? If so, refer to it in    * the (static-method) Futures.getChecked documentation    */
DECL|method|hasConstructorUsableByGetChecked ( Class<? extends Exception> exceptionClass)
specifier|private
specifier|static
name|boolean
name|hasConstructorUsableByGetChecked
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
try|try
block|{
name|Exception
name|unused
init|=
name|newWithCause
argument_list|(
name|exceptionClass
argument_list|,
operator|new
name|Exception
argument_list|()
argument_list|)
decl_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|newWithCause (Class<X> exceptionClass, Throwable cause)
specifier|private
specifier|static
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|X
name|newWithCause
parameter_list|(
name|Class
argument_list|<
name|X
argument_list|>
name|exceptionClass
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
comment|// getConstructors() guarantees this as long as we don't modify the array.
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|List
argument_list|<
name|Constructor
argument_list|<
name|X
argument_list|>
argument_list|>
name|constructors
init|=
operator|(
name|List
operator|)
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptionClass
operator|.
name|getConstructors
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Constructor
argument_list|<
name|X
argument_list|>
name|constructor
range|:
name|preferringStrings
argument_list|(
name|constructors
argument_list|)
control|)
block|{
annotation|@
name|Nullable
name|X
name|instance
init|=
name|newFromConstructor
argument_list|(
name|constructor
argument_list|,
name|cause
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|instance
operator|.
name|getCause
argument_list|()
operator|==
literal|null
condition|)
block|{
name|instance
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No appropriate constructor for exception of type "
operator|+
name|exceptionClass
operator|+
literal|" in response to chained exception"
argument_list|,
name|cause
argument_list|)
throw|;
block|}
DECL|method|preferringStrings ( List<Constructor<X>> constructors)
specifier|private
specifier|static
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|List
argument_list|<
name|Constructor
argument_list|<
name|X
argument_list|>
argument_list|>
name|preferringStrings
parameter_list|(
name|List
argument_list|<
name|Constructor
argument_list|<
name|X
argument_list|>
argument_list|>
name|constructors
parameter_list|)
block|{
return|return
name|WITH_STRING_PARAM_FIRST
operator|.
name|sortedCopy
argument_list|(
name|constructors
argument_list|)
return|;
block|}
DECL|field|WITH_STRING_PARAM_FIRST
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|Constructor
argument_list|<
name|?
argument_list|>
argument_list|>
name|WITH_STRING_PARAM_FIRST
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|Constructor
argument_list|<
name|?
argument_list|>
argument_list|,
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Boolean
name|apply
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|input
parameter_list|)
block|{
return|return
name|asList
argument_list|(
name|input
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
block|)
operator|.
name|reverse
argument_list|()
expr_stmt|;
end_class

begin_function
annotation|@
name|Nullable
DECL|method|newFromConstructor (Constructor<X> constructor, Throwable cause)
specifier|private
specifier|static
parameter_list|<
name|X
parameter_list|>
name|X
name|newFromConstructor
parameter_list|(
name|Constructor
argument_list|<
name|X
argument_list|>
name|constructor
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramTypes
init|=
name|constructor
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Object
index|[]
name|params
init|=
operator|new
name|Object
index|[
name|paramTypes
operator|.
name|length
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
name|paramTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
init|=
name|paramTypes
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|paramType
operator|.
name|equals
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
name|params
index|[
name|i
index|]
operator|=
name|cause
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|paramType
operator|.
name|equals
argument_list|(
name|Throwable
operator|.
name|class
argument_list|)
condition|)
block|{
name|params
index|[
name|i
index|]
operator|=
name|cause
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
try|try
block|{
return|return
name|constructor
operator|.
name|newInstance
argument_list|(
name|params
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_function

begin_function
annotation|@
name|VisibleForTesting
DECL|method|isCheckedException (Class<? extends Exception> type)
specifier|static
name|boolean
name|isCheckedException
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Exception
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|!
name|RuntimeException
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|VisibleForTesting
DECL|method|checkExceptionClassValidity (Class<? extends Exception> exceptionClass)
specifier|static
name|void
name|checkExceptionClassValidity
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
name|checkArgument
argument_list|(
name|hasConstructorUsableByGetChecked
argument_list|(
name|exceptionClass
argument_list|)
argument_list|,
literal|"Futures.getChecked exception type (%s) must be an accessible class with an accessible "
operator|+
literal|"constructor whose parameters (if any) must be of type String and/or Throwable"
argument_list|,
name|exceptionClass
argument_list|)
expr_stmt|;
block|}
end_function

begin_constructor
DECL|method|FuturesGetChecked ()
specifier|private
name|FuturesGetChecked
parameter_list|()
block|{}
end_constructor

unit|}
end_unit

