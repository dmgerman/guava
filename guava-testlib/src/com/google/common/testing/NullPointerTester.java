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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Objects
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
name|ClassToInstanceMap
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|MutableClassToInstanceMap
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
name|reflect
operator|.
name|Invokable
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
name|reflect
operator|.
name|Parameter
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
name|reflect
operator|.
name|Reflection
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
name|reflect
operator|.
name|TypeToken
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
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
name|Type
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
name|concurrent
operator|.
name|ConcurrentMap
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
comment|/**  * A test utility that verifies that your methods and constructors throw {@link  * NullPointerException} or {@link UnsupportedOperationException} whenever null  * is passed to a parameter that isn't annotated with {@link Nullable}.  *  *<p>The tested methods and constructors are invoked -- each time with one  * parameter being null and the rest not null -- and the test fails if no  * expected exception is thrown. {@code NullPointerTester} uses best effort to  * pick non-null default values for many common JDK and Guava types, and also  * for interfaces and public classes that have public parameter-less  * constructors. When the non-null default value for a particular parameter type  * cannot be provided by {@code NullPointerTester}, the caller can provide a  * custom non-null default value for the parameter type via {@link #setDefault}.  *  * @author Kevin Bourrillion  * @since 10.0  */
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
name|ClassToInstanceMap
argument_list|<
name|Object
argument_list|>
name|defaults
init|=
name|MutableClassToInstanceMap
operator|.
name|create
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
DECL|field|policy
specifier|private
name|ExceptionTypePolicy
name|policy
init|=
name|ExceptionTypePolicy
operator|.
name|NPE_OR_UOE
decl_stmt|;
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
name|putInstance
argument_list|(
name|type
argument_list|,
name|checkNotNull
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Ignore {@code method} in the tests that follow. Returns this object.    *    * @since 13.0    */
DECL|method|ignore (Method method)
specifier|public
name|NullPointerTester
name|ignore
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|ignoredMembers
operator|.
name|add
argument_list|(
name|checkNotNull
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Runs {@link #testConstructor} on every constructor in class {@code c} that    * has at least {@code minimalVisibility}.    */
DECL|method|testConstructors (Class<?> c, Visibility minimalVisibility)
specifier|public
name|void
name|testConstructors
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Visibility
name|minimalVisibility
parameter_list|)
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
name|minimalVisibility
operator|.
name|isVisible
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
block|{
name|testConstructors
argument_list|(
name|c
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|)
expr_stmt|;
block|}
comment|/**    * Runs {@link #testMethod} on every static method of class {@code c} that has    * at least {@code minimalVisibility}, including those "inherited" from    * superclasses of the same package.    */
DECL|method|testStaticMethods (Class<?> c, Visibility minimalVisibility)
specifier|public
name|void
name|testStaticMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Visibility
name|minimalVisibility
parameter_list|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|minimalVisibility
operator|.
name|getStaticMethods
argument_list|(
name|c
argument_list|)
control|)
block|{
if|if
condition|(
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
comment|/**    * Runs {@link #testMethod} on every public static method of class {@code c},    * including those "inherited" from superclasses of the same package.    */
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
block|{
name|testStaticMethods
argument_list|(
name|c
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|)
expr_stmt|;
block|}
comment|/**    * Runs {@link #testMethod} on every instance method of the class of    * {@code instance} with at least {@code minimalVisibility}, including those    * inherited from superclasses of the same package.    */
DECL|method|testInstanceMethods (Object instance, Visibility minimalVisibility)
specifier|public
name|void
name|testInstanceMethods
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Visibility
name|minimalVisibility
parameter_list|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|getInstanceMethodsToTest
argument_list|(
name|instance
operator|.
name|getClass
argument_list|()
argument_list|,
name|minimalVisibility
argument_list|)
control|)
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
DECL|method|getInstanceMethodsToTest (Class<?> c, Visibility minimalVisibility)
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|getInstanceMethodsToTest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Visibility
name|minimalVisibility
parameter_list|)
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Method
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|minimalVisibility
operator|.
name|getInstanceMethods
argument_list|(
name|c
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|isIgnored
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Runs {@link #testMethod} on every public instance method of the class of    * {@code instance}, including those inherited from superclasses of the same    * package.    */
DECL|method|testAllPublicInstanceMethods (Object instance)
specifier|public
name|void
name|testAllPublicInstanceMethods
parameter_list|(
name|Object
name|instance
parameter_list|)
block|{
name|testInstanceMethods
argument_list|(
name|instance
argument_list|,
name|Visibility
operator|.
name|PUBLIC
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that {@code method} produces a {@link NullPointerException}    * or {@link UnsupportedOperationException} whenever<i>any</i> of its    * non-{@link Nullable} parameters are null.    *    * @param instance the instance to invoke {@code method} on, or null if    *     {@code method} is static    */
DECL|method|testMethod (@ullable Object instance, Method method)
specifier|public
name|void
name|testMethod
parameter_list|(
annotation|@
name|Nullable
name|Object
name|instance
parameter_list|,
name|Method
name|method
parameter_list|)
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
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
init|=
name|ctor
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|declaringClass
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|||
name|declaringClass
operator|.
name|getEnclosingClass
argument_list|()
operator|==
literal|null
argument_list|,
literal|"Cannot test constructor of non-static inner class: %s"
argument_list|,
name|declaringClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
DECL|method|testMethodParameter ( @ullable final Object instance, final Method method, int paramIndex)
specifier|public
name|void
name|testMethodParameter
parameter_list|(
annotation|@
name|Nullable
specifier|final
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
block|{
name|method
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testParameter
argument_list|(
name|instance
argument_list|,
name|invokable
argument_list|(
name|instance
argument_list|,
name|method
argument_list|)
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
DECL|method|testConstructorParameter (Constructor<?> ctor, int paramIndex)
specifier|public
name|void
name|testConstructorParameter
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
parameter_list|,
name|int
name|paramIndex
parameter_list|)
block|{
name|ctor
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testParameter
argument_list|(
literal|null
argument_list|,
name|Invokable
operator|.
name|from
argument_list|(
name|ctor
argument_list|)
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
comment|/** Visibility of any method or constructor. */
DECL|enum|Visibility
specifier|public
enum|enum
name|Visibility
block|{
DECL|enumConstant|PACKAGE
name|PACKAGE
block|{
annotation|@
name|Override
name|boolean
name|isVisible
parameter_list|(
name|int
name|modifiers
parameter_list|)
block|{
return|return
operator|!
name|Modifier
operator|.
name|isPrivate
argument_list|(
name|modifiers
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|PROTECTED
name|PROTECTED
block|{
annotation|@
name|Override
name|boolean
name|isVisible
parameter_list|(
name|int
name|modifiers
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
operator|||
name|Modifier
operator|.
name|isProtected
argument_list|(
name|modifiers
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|PUBLIC
name|PUBLIC
block|{
annotation|@
name|Override
name|boolean
name|isVisible
parameter_list|(
name|int
name|modifiers
parameter_list|)
block|{
return|return
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
return|;
block|}
block|}
block|;
DECL|method|isVisible (int modifiers)
specifier|abstract
name|boolean
name|isVisible
parameter_list|(
name|int
name|modifiers
parameter_list|)
function_decl|;
comment|/**      * Returns {@code true} if {@code member} is visible under {@code this}      * visibility.      */
DECL|method|isVisible (Member member)
specifier|final
name|boolean
name|isVisible
parameter_list|(
name|Member
name|member
parameter_list|)
block|{
return|return
name|isVisible
argument_list|(
name|member
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getStaticMethods (Class<?> cls)
specifier|final
name|Iterable
argument_list|<
name|Method
argument_list|>
name|getStaticMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Method
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|getVisibleMethods
argument_list|(
name|cls
argument_list|)
control|)
block|{
if|if
condition|(
name|Invokable
operator|.
name|from
argument_list|(
name|method
argument_list|)
operator|.
name|isStatic
argument_list|()
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|getInstanceMethods (Class<?> cls)
specifier|final
name|Iterable
argument_list|<
name|Method
argument_list|>
name|getInstanceMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|ConcurrentMap
argument_list|<
name|Signature
argument_list|,
name|Method
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newConcurrentMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|getVisibleMethods
argument_list|(
name|cls
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|Invokable
operator|.
name|from
argument_list|(
name|method
argument_list|)
operator|.
name|isStatic
argument_list|()
condition|)
block|{
name|map
operator|.
name|putIfAbsent
argument_list|(
operator|new
name|Signature
argument_list|(
name|method
argument_list|)
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
operator|.
name|values
argument_list|()
return|;
block|}
DECL|method|getVisibleMethods (Class<?> cls)
specifier|private
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|getVisibleMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
comment|// Don't use cls.getPackage() because it does nasty things like reading
comment|// a file.
name|String
name|visiblePackage
init|=
name|Reflection
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
decl_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Method
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|TypeToken
operator|.
name|of
argument_list|(
name|cls
argument_list|)
operator|.
name|getTypes
argument_list|()
operator|.
name|classes
argument_list|()
operator|.
name|rawTypes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Reflection
operator|.
name|getPackageName
argument_list|(
name|type
argument_list|)
operator|.
name|equals
argument_list|(
name|visiblePackage
argument_list|)
condition|)
block|{
break|break;
block|}
for|for
control|(
name|Method
name|method
range|:
name|type
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|method
operator|.
name|isSynthetic
argument_list|()
operator|&&
name|isVisible
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
comment|// TODO(benyu): Use labs/reflect/Signature if it graduates.
DECL|class|Signature
specifier|private
specifier|static
specifier|final
class|class
name|Signature
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|parameterTypes
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parameterTypes
decl_stmt|;
DECL|method|Signature (Method method)
name|Signature
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|this
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|method
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Signature (String name, ImmutableList<Class<?>> parameterTypes)
name|Signature
parameter_list|(
name|String
name|name
parameter_list|,
name|ImmutableList
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parameterTypes
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|parameterTypes
operator|=
name|parameterTypes
expr_stmt|;
block|}
DECL|method|equals (Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Signature
condition|)
block|{
name|Signature
name|that
init|=
operator|(
name|Signature
operator|)
name|obj
decl_stmt|;
return|return
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
operator|&&
name|parameterTypes
operator|.
name|equals
argument_list|(
name|that
operator|.
name|parameterTypes
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|name
argument_list|,
name|parameterTypes
argument_list|)
return|;
block|}
block|}
comment|/**    * Verifies that {@code invokable} produces a {@link NullPointerException} or    * {@link UnsupportedOperationException} when the parameter in position {@code    * paramIndex} is null.  If this parameter is marked {@link Nullable}, this    * method does nothing.    *    * @param instance the instance to invoke {@code invokable} on, or null if    *     {@code invokable} is static    */
DECL|method|testParameter (Object instance, Invokable<?, ?> invokable, int paramIndex, Class<?> testedClass)
specifier|private
name|void
name|testParameter
parameter_list|(
name|Object
name|instance
parameter_list|,
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|invokable
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
block|{
if|if
condition|(
name|isPrimitiveOrNullable
argument_list|(
name|invokable
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
name|paramIndex
argument_list|)
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
name|invokable
argument_list|,
name|paramIndex
argument_list|)
decl_stmt|;
try|try
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// We'll get a runtime exception if the type is wrong.
name|Invokable
argument_list|<
name|Object
argument_list|,
name|?
argument_list|>
name|unsafe
init|=
operator|(
name|Invokable
argument_list|<
name|Object
argument_list|,
name|?
argument_list|>
operator|)
name|invokable
decl_stmt|;
name|unsafe
operator|.
name|invoke
argument_list|(
name|instance
argument_list|,
name|params
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"No exception thrown from "
operator|+
name|invokable
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
name|policy
operator|.
name|isExpectedType
argument_list|(
name|cause
argument_list|)
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
name|invokable
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
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|buildParamList (Invokable<?, ?> invokable, int indexOfParamToSetToNull)
specifier|private
name|Object
index|[]
name|buildParamList
parameter_list|(
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|invokable
parameter_list|,
name|int
name|indexOfParamToSetToNull
parameter_list|)
block|{
name|ImmutableList
argument_list|<
name|Parameter
argument_list|>
name|params
init|=
name|invokable
operator|.
name|getParameters
argument_list|()
decl_stmt|;
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
name|params
operator|.
name|size
argument_list|()
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Parameter
name|param
init|=
name|params
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
name|indexOfParamToSetToNull
condition|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|getDefaultValue
argument_list|(
name|param
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isPrimitiveOrNullable
argument_list|(
name|param
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"No default value found for "
operator|+
name|param
operator|+
literal|" of "
operator|+
name|invokable
argument_list|,
name|args
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
name|args
return|;
block|}
DECL|method|getDefaultValue (TypeToken<T> type)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|getDefaultValue
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
comment|// We assume that all defaults are generics-safe, even if they aren't,
comment|// we take the risk.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|defaultValue
init|=
operator|(
name|T
operator|)
name|defaults
operator|.
name|getInstance
argument_list|(
name|type
operator|.
name|getRawType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultValue
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultValue
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// All null values are generics-safe
name|T
name|nullValue
init|=
operator|(
name|T
operator|)
name|ArbitraryInstances
operator|.
name|get
argument_list|(
name|type
operator|.
name|getRawType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|nullValue
operator|!=
literal|null
condition|)
block|{
return|return
name|nullValue
return|;
block|}
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|==
name|Class
operator|.
name|class
condition|)
block|{
comment|// If parameter is Class<? extends Foo>, we return Foo.class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|defaultClass
init|=
operator|(
name|T
operator|)
name|getFirstTypeParameter
argument_list|(
name|type
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|getRawType
argument_list|()
decl_stmt|;
return|return
name|defaultClass
return|;
block|}
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|==
name|TypeToken
operator|.
name|class
condition|)
block|{
comment|// If parameter is TypeToken<? extends Foo>, we return TypeToken<Foo>.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|defaultType
init|=
operator|(
name|T
operator|)
name|getFirstTypeParameter
argument_list|(
name|type
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|defaultType
return|;
block|}
if|if
condition|(
name|type
operator|.
name|getRawType
argument_list|()
operator|.
name|isInterface
argument_list|()
condition|)
block|{
return|return
name|newDefaultReturningProxy
argument_list|(
name|type
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getFirstTypeParameter (Type type)
specifier|private
specifier|static
name|TypeToken
argument_list|<
name|?
argument_list|>
name|getFirstTypeParameter
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|TypeToken
operator|.
name|of
argument_list|(
operator|(
operator|(
name|ParameterizedType
operator|)
name|type
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|TypeToken
operator|.
name|of
argument_list|(
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
block|}
DECL|method|newDefaultReturningProxy (final TypeToken<T> type)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|newDefaultReturningProxy
parameter_list|(
specifier|final
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|DummyProxy
argument_list|()
block|{
annotation|@
name|Override
argument_list|<
name|R
argument_list|>
name|R
name|dummyReturnValue
parameter_list|(
name|TypeToken
argument_list|<
name|R
argument_list|>
name|returnType
parameter_list|)
block|{
return|return
name|getDefaultValue
argument_list|(
name|returnType
argument_list|)
return|;
block|}
block|}
operator|.
name|newProxy
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|invokable (@ullable Object instance, Method method)
specifier|private
specifier|static
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|invokable
parameter_list|(
annotation|@
name|Nullable
name|Object
name|instance
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
return|return
name|Invokable
operator|.
name|from
argument_list|(
name|method
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|TypeToken
operator|.
name|of
argument_list|(
name|instance
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|method
argument_list|(
name|method
argument_list|)
return|;
block|}
block|}
DECL|method|isPrimitiveOrNullable (Parameter param)
specifier|static
name|boolean
name|isPrimitiveOrNullable
parameter_list|(
name|Parameter
name|param
parameter_list|)
block|{
return|return
name|param
operator|.
name|getType
argument_list|()
operator|.
name|getRawType
argument_list|()
operator|.
name|isPrimitive
argument_list|()
operator|||
name|param
operator|.
name|isAnnotationPresent
argument_list|(
name|Nullable
operator|.
name|class
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
comment|/**    * Strategy for exception type matching used by {@link NullPointerTester}.    */
DECL|enum|ExceptionTypePolicy
specifier|private
enum|enum
name|ExceptionTypePolicy
block|{
comment|/**      * Exceptions should be {@link NullPointerException} or      * {@link UnsupportedOperationException}.      */
DECL|enumConstant|NPE_OR_UOE
DECL|method|NPE_OR_UOE ()
name|NPE_OR_UOE
parameter_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isExpectedType
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
return|return
name|cause
operator|instanceof
name|NullPointerException
operator|||
name|cause
operator|instanceof
name|UnsupportedOperationException
return|;
block|}
block|}
block|,
comment|/**      * Exceptions should be {@link NullPointerException},      * {@link IllegalArgumentException}, or      * {@link UnsupportedOperationException}.      */
DECL|enumConstant|NPE_IAE_OR_UOE
DECL|method|NPE_IAE_OR_UOE ()
name|NPE_IAE_OR_UOE
parameter_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isExpectedType
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
return|return
name|cause
operator|instanceof
name|NullPointerException
operator|||
name|cause
operator|instanceof
name|IllegalArgumentException
operator|||
name|cause
operator|instanceof
name|UnsupportedOperationException
return|;
block|}
block|}
block|;
DECL|method|isExpectedType (Throwable cause)
specifier|public
specifier|abstract
name|boolean
name|isExpectedType
parameter_list|(
name|Throwable
name|cause
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

