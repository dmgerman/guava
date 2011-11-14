begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2005 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
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
name|Beta
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
name|base
operator|.
name|Predicates
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterators
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
name|collect
operator|.
name|Maps
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|lang
operator|.
name|reflect
operator|.
name|Member
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
name|Method
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
name|Modifier
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Map
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
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A test utility that verifies that your methods throw {@link  * NullPointerException} or {@link UnsupportedOperationException} whenever any  * of their parameters are null. To use it, you must first provide valid default  * values for the parameter types used by the class.  *  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|NullPointerTester
specifier|public
specifier|final
class|class
name|NullPointerTester
block|{
DECL|field|defaults
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|defaults
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
DECL|field|ignoredMembers
specifier|private
specifier|final
name|List
argument_list|<
name|Member
argument_list|>
name|ignoredMembers
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|NullPointerTester ()
specifier|public
name|NullPointerTester
parameter_list|()
block|{
name|setCommonDefaults
argument_list|()
expr_stmt|;
block|}
DECL|method|setCommonDefaults ()
specifier|private
specifier|final
name|void
name|setCommonDefaults
parameter_list|()
block|{
name|setDefault
argument_list|(
name|Appendable
operator|.
name|class
argument_list|,
operator|new
name|StringBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|CharSequence
operator|.
name|class
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|Class
operator|.
name|class
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Collection
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Comparable
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Comparator
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|reverseOrder
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Function
operator|.
name|class
argument_list|,
name|Functions
operator|.
name|identity
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Iterable
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Iterator
operator|.
name|class
argument_list|,
name|Iterators
operator|.
name|emptyIterator
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Object
operator|.
name|class
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Pattern
operator|.
name|class
argument_list|,
name|Pattern
operator|.
name|compile
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Predicate
operator|.
name|class
argument_list|,
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Set
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|SortedSet
operator|.
name|class
argument_list|,
operator|new
name|TreeSet
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|String
operator|.
name|class
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Supplier
operator|.
name|class
argument_list|,
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|Throwable
operator|.
name|class
argument_list|,
operator|new
name|Exception
argument_list|()
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|TimeUnit
operator|.
name|class
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|long
operator|.
name|class
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|short
operator|.
name|class
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|char
operator|.
name|class
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|byte
operator|.
name|class
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|float
operator|.
name|class
argument_list|,
literal|0.0f
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|double
operator|.
name|class
argument_list|,
literal|0.0d
argument_list|)
expr_stmt|;
name|setDefault
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**    * Sets a default value that can be used for any parameter of type    * {@code type}. Returns this object.    */
DECL|method|setDefault (Class<T> type, T value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|NullPointerTester
name|setDefault
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|defaults
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Ignore a member (constructor or method) in testAllXxx methods. Returns    * this object.    */
DECL|method|ignore (Member member)
specifier|public
name|NullPointerTester
name|ignore
parameter_list|(
name|Member
name|member
parameter_list|)
block|{
name|ignoredMembers
operator|.
name|add
argument_list|(
name|member
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Runs {@link #testConstructor} on every public constructor in class {@code    * c}.    */
DECL|method|testAllPublicConstructors (Class<?> c)
specifier|public
name|void
name|testAllPublicConstructors
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
range|:
name|c
operator|.
name|getDeclaredConstructors
argument_list|()
control|)
block|{
if|if
condition|(
name|isPublic
argument_list|(
name|constructor
argument_list|)
operator|&&
operator|!
name|isStatic
argument_list|(
name|constructor
argument_list|)
operator|&&
operator|!
name|isIgnored
argument_list|(
name|constructor
argument_list|)
condition|)
block|{
name|testConstructor
argument_list|(
name|constructor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Runs {@link #testMethod} on every public static method in class    * {@code c}.    */
DECL|method|testAllPublicStaticMethods (Class<?> c)
specifier|public
name|void
name|testAllPublicStaticMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Method
name|method
range|:
name|c
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isPublic
argument_list|(
name|method
argument_list|)
operator|&&
name|isStatic
argument_list|(
name|method
argument_list|)
operator|&&
operator|!
name|isIgnored
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|testMethod
argument_list|(
literal|null
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Runs {@link #testMethod} on every public instance method of    * {@code instance}.    */
DECL|method|testAllPublicInstanceMethods (Object instance)
specifier|public
name|void
name|testAllPublicInstanceMethods
parameter_list|(
name|Object
name|instance
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|instance
operator|.
name|getClass
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|c
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|isPublic
argument_list|(
name|method
argument_list|)
operator|&&
operator|!
name|isStatic
argument_list|(
name|method
argument_list|)
operator|&&
operator|!
name|isIgnored
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|testMethod
argument_list|(
name|instance
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Verifies that {@code method} produces a {@link NullPointerException}    * or {@link UnsupportedOperationException} whenever<i>any</i> of its    * non-{@link Nullable} parameters are null.    *    * @param instance the instance to invoke {@code method} on, or null if    *     {@code method} is static    */
DECL|method|testMethod (Object instance, Method method)
specifier|public
name|void
name|testMethod
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Method
name|method
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|nullIndex
init|=
literal|0
init|;
name|nullIndex
operator|<
name|types
operator|.
name|length
condition|;
name|nullIndex
operator|++
control|)
block|{
name|testMethodParameter
argument_list|(
name|instance
argument_list|,
name|method
argument_list|,
name|nullIndex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Verifies that {@code ctor} produces a {@link NullPointerException} or    * {@link UnsupportedOperationException} whenever<i>any</i> of its    * non-{@link Nullable} parameters are null.    */
DECL|method|testConstructor (Constructor<?> ctor)
specifier|public
name|void
name|testConstructor
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|ctor
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|nullIndex
init|=
literal|0
init|;
name|nullIndex
operator|<
name|types
operator|.
name|length
condition|;
name|nullIndex
operator|++
control|)
block|{
name|testConstructorParameter
argument_list|(
name|ctor
argument_list|,
name|nullIndex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Verifies that {@code method} produces a {@link NullPointerException} or    * {@link UnsupportedOperationException} when the parameter in position {@code    * paramIndex} is null.  If this parameter is marked {@link Nullable}, this    * method does nothing.    *    * @param instance the instance to invoke {@code method} on, or null if    *     {@code method} is static    */
DECL|method|testMethodParameter (Object instance, final Method method, int paramIndex)
specifier|public
name|void
name|testMethodParameter
parameter_list|(
name|Object
name|instance
parameter_list|,
specifier|final
name|Method
name|method
parameter_list|,
name|int
name|paramIndex
parameter_list|)
throws|throws
name|Exception
block|{
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testFunctorParameter
argument_list|(
name|instance
argument_list|,
operator|new
name|Functor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|()
block|{
return|return
name|method
operator|.
name|getParameterTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Annotation
index|[]
index|[]
name|getParameterAnnotations
parameter_list|()
block|{
return|return
name|method
operator|.
name|getParameterAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
block|{
name|method
operator|.
name|invoke
argument_list|(
name|instance
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"("
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|getParameterTypes
argument_list|()
argument_list|)
operator|+
literal|")"
return|;
block|}
block|}
argument_list|,
name|paramIndex
argument_list|,
name|method
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that {@code ctor} produces a {@link NullPointerException} or    * {@link UnsupportedOperationException} when the parameter in position {@code    * paramIndex} is null.  If this parameter is marked {@link Nullable}, this    * method does nothing.    */
DECL|method|testConstructorParameter (final Constructor<?> ctor, int paramIndex)
specifier|public
name|void
name|testConstructorParameter
parameter_list|(
specifier|final
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
parameter_list|,
name|int
name|paramIndex
parameter_list|)
throws|throws
name|Exception
block|{
name|ctor
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testFunctorParameter
argument_list|(
literal|null
argument_list|,
operator|new
name|Functor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|()
block|{
return|return
name|ctor
operator|.
name|getParameterTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Annotation
index|[]
index|[]
name|getParameterAnnotations
parameter_list|()
block|{
return|return
name|ctor
operator|.
name|getParameterAnnotations
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|invoke
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
throws|,
name|InstantiationException
block|{
name|ctor
operator|.
name|newInstance
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
name|paramIndex
argument_list|,
name|ctor
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that {@code func} produces a {@link NullPointerException} or    * {@link UnsupportedOperationException} when the parameter in position {@code    * paramIndex} is null.  If this parameter is marked {@link Nullable}, this    * method does nothing.    *    * @param instance the instance to invoke {@code func} on, or null if    *     {@code func} is static    */
DECL|method|testFunctorParameter (Object instance, Functor func, int paramIndex, Class<?> testedClass)
specifier|private
name|void
name|testFunctorParameter
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Functor
name|func
parameter_list|,
name|int
name|paramIndex
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|testedClass
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|parameterIsPrimitiveOrNullable
argument_list|(
name|func
argument_list|,
name|paramIndex
argument_list|)
condition|)
block|{
return|return;
comment|// there's nothing to test
block|}
name|Object
index|[]
name|params
init|=
name|buildParamList
argument_list|(
name|func
argument_list|,
name|paramIndex
argument_list|)
decl_stmt|;
try|try
block|{
name|func
operator|.
name|invoke
argument_list|(
name|instance
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|GuavaAsserts
operator|.
name|fail
argument_list|(
literal|"No exception thrown from "
operator|+
name|func
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|params
argument_list|)
operator|+
literal|" for "
operator|+
name|testedClass
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|instanceof
name|NullPointerException
operator|||
name|cause
operator|instanceof
name|UnsupportedOperationException
condition|)
block|{
return|return;
block|}
name|AssertionFailedError
name|error
init|=
operator|new
name|AssertionFailedError
argument_list|(
literal|"wrong exception thrown from "
operator|+
name|func
operator|+
literal|": "
operator|+
name|cause
argument_list|)
decl_stmt|;
name|error
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
throw|throw
name|error
throw|;
block|}
block|}
DECL|method|parameterIsPrimitiveOrNullable ( Functor func, int paramIndex)
specifier|private
specifier|static
name|boolean
name|parameterIsPrimitiveOrNullable
parameter_list|(
name|Functor
name|func
parameter_list|,
name|int
name|paramIndex
parameter_list|)
block|{
if|if
condition|(
name|func
operator|.
name|getParameterTypes
argument_list|()
index|[
name|paramIndex
index|]
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Annotation
index|[]
name|annotations
init|=
name|func
operator|.
name|getParameterAnnotations
argument_list|()
index|[
name|paramIndex
index|]
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|Nullable
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|buildParamList (Functor func, int indexOfParamToSetToNull)
specifier|private
name|Object
index|[]
name|buildParamList
parameter_list|(
name|Functor
name|func
parameter_list|,
name|int
name|indexOfParamToSetToNull
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|types
init|=
name|func
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
name|types
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
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|!=
name|indexOfParamToSetToNull
condition|)
block|{
name|params
index|[
name|i
index|]
operator|=
name|defaults
operator|.
name|get
argument_list|(
name|types
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|parameterIsPrimitiveOrNullable
argument_list|(
name|func
argument_list|,
name|indexOfParamToSetToNull
argument_list|)
condition|)
block|{
name|GuavaAsserts
operator|.
name|assertTrue
argument_list|(
literal|"No default value found for "
operator|+
name|types
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|params
index|[
name|i
index|]
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|params
return|;
block|}
DECL|interface|Functor
specifier|private
interface|interface
name|Functor
block|{
DECL|method|getParameterTypes ()
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getParameterTypes
parameter_list|()
function_decl|;
DECL|method|getParameterAnnotations ()
name|Annotation
index|[]
index|[]
name|getParameterAnnotations
parameter_list|()
function_decl|;
DECL|method|invoke (Object o, Object[] params)
name|void
name|invoke
parameter_list|(
name|Object
name|o
parameter_list|,
name|Object
index|[]
name|params
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
DECL|method|isPublic (Member member)
specifier|private
specifier|static
name|boolean
name|isPublic
parameter_list|(
name|Member
name|member
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isPublic
argument_list|(
name|member
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isStatic (Member member)
specifier|private
specifier|static
name|boolean
name|isStatic
parameter_list|(
name|Member
name|member
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isStatic
argument_list|(
name|member
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|isIgnored (Member member)
specifier|private
name|boolean
name|isIgnored
parameter_list|(
name|Member
name|member
parameter_list|)
block|{
return|return
name|member
operator|.
name|isSynthetic
argument_list|()
operator|||
name|ignoredMembers
operator|.
name|contains
argument_list|(
name|member
argument_list|)
return|;
block|}
block|}
end_class

end_unit

