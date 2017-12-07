begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
name|AccessibleObject
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
name|GenericDeclaration
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
name|Type
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
name|TypeVariable
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Wrapper around either a {@link Method} or a {@link Constructor}. Convenience API is provided to  * make common reflective operation easier to deal with, such as {@link #isPublic}, {@link  * #getParameters} etc.  *  *<p>In addition to convenience methods, {@link TypeToken#method} and {@link TypeToken#constructor}  * will resolve the type parameters of the method or constructor in the context of the owner type,  * which may be a subtype of the declaring class. For example:  *  *<pre>{@code  * Method getMethod = List.class.getMethod("get", int.class);  * Invokable<List<String>, ?> invokable = new TypeToken<List<String>>() {}.method(getMethod);  * assertEquals(TypeToken.of(String.class), invokable.getReturnType()); // Not Object.class!  * assertEquals(new TypeToken<List<String>>() {}, invokable.getOwnerType());  * }</pre>  *  * @param<T> the type that owns this method or constructor.  * @param<R> the return type of (or supertype thereof) the method or the declaring type of the  *     constructor.  * @author Ben Yu  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Invokable
specifier|public
specifier|abstract
class|class
name|Invokable
parameter_list|<
name|T
parameter_list|,
name|R
parameter_list|>
extends|extends
name|Element
implements|implements
name|GenericDeclaration
block|{
DECL|method|Invokable (M member)
parameter_list|<
name|M
extends|extends
name|AccessibleObject
operator|&
name|Member
parameter_list|>
name|Invokable
parameter_list|(
name|M
name|member
parameter_list|)
block|{
name|super
argument_list|(
name|member
argument_list|)
expr_stmt|;
block|}
comment|/** Returns {@link Invokable} of {@code method}. */
DECL|method|from (Method method)
specifier|public
specifier|static
name|Invokable
argument_list|<
name|?
argument_list|,
name|Object
argument_list|>
name|from
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
operator|new
name|MethodInvokable
argument_list|<>
argument_list|(
name|method
argument_list|)
return|;
block|}
comment|/** Returns {@link Invokable} of {@code constructor}. */
DECL|method|from (Constructor<T> constructor)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Invokable
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
name|from
parameter_list|(
name|Constructor
argument_list|<
name|T
argument_list|>
name|constructor
parameter_list|)
block|{
return|return
operator|new
name|ConstructorInvokable
argument_list|<
name|T
argument_list|>
argument_list|(
name|constructor
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if this is an overridable method. Constructors, private, static or final    * methods, or methods declared by final classes are not overridable.    */
DECL|method|isOverridable ()
specifier|public
specifier|abstract
name|boolean
name|isOverridable
parameter_list|()
function_decl|;
comment|/** Returns {@code true} if this was declared to take a variable number of arguments. */
DECL|method|isVarArgs ()
specifier|public
specifier|abstract
name|boolean
name|isVarArgs
parameter_list|()
function_decl|;
comment|/**    * Invokes with {@code receiver} as 'this' and {@code args} passed to the underlying method and    * returns the return value; or calls the underlying constructor with {@code args} and returns the    * constructed instance.    *    * @throws IllegalAccessException if this {@code Constructor} object enforces Java language access    *     control and the underlying method or constructor is inaccessible.    * @throws IllegalArgumentException if the number of actual and formal parameters differ; if an    *     unwrapping conversion for primitive arguments fails; or if, after possible unwrapping, a    *     parameter value cannot be converted to the corresponding formal parameter type by a method    *     invocation conversion.    * @throws InvocationTargetException if the underlying method or constructor throws an exception.    */
comment|// All subclasses are owned by us and we'll make sure to get the R type right.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CanIgnoreReturnValue
DECL|method|invoke (@ullableDecl T receiver, Object... args)
specifier|public
specifier|final
name|R
name|invoke
parameter_list|(
annotation|@
name|NullableDecl
name|T
name|receiver
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
block|{
return|return
operator|(
name|R
operator|)
name|invokeInternal
argument_list|(
name|receiver
argument_list|,
name|checkNotNull
argument_list|(
name|args
argument_list|)
argument_list|)
return|;
block|}
comment|/** Returns the return type of this {@code Invokable}. */
comment|// All subclasses are owned by us and we'll make sure to get the R type right.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getReturnType ()
specifier|public
specifier|final
name|TypeToken
argument_list|<
name|?
extends|extends
name|R
argument_list|>
name|getReturnType
parameter_list|()
block|{
return|return
operator|(
name|TypeToken
argument_list|<
name|?
extends|extends
name|R
argument_list|>
operator|)
name|TypeToken
operator|.
name|of
argument_list|(
name|getGenericReturnType
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns all declared parameters of this {@code Invokable}. Note that if this is a constructor    * of a non-static inner class, unlike {@link Constructor#getParameterTypes}, the hidden {@code    * this} parameter of the enclosing class is excluded from the returned parameters.    */
DECL|method|getParameters ()
specifier|public
specifier|final
name|ImmutableList
argument_list|<
name|Parameter
argument_list|>
name|getParameters
parameter_list|()
block|{
name|Type
index|[]
name|parameterTypes
init|=
name|getGenericParameterTypes
argument_list|()
decl_stmt|;
name|Annotation
index|[]
index|[]
name|annotations
init|=
name|getParameterAnnotations
argument_list|()
decl_stmt|;
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|Parameter
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parameterTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
operator|new
name|Parameter
argument_list|(
name|this
argument_list|,
name|i
argument_list|,
name|TypeToken
operator|.
name|of
argument_list|(
name|parameterTypes
index|[
name|i
index|]
argument_list|)
argument_list|,
name|annotations
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/** Returns all declared exception types of this {@code Invokable}. */
DECL|method|getExceptionTypes ()
specifier|public
specifier|final
name|ImmutableList
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|getExceptionTypes
parameter_list|()
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
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
name|Type
name|type
range|:
name|getGenericExceptionTypes
argument_list|()
control|)
block|{
comment|// getGenericExceptionTypes() will never return a type that's not exception
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|TypeToken
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionType
init|=
operator|(
name|TypeToken
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
operator|)
name|TypeToken
operator|.
name|of
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|builder
operator|.
name|add
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Explicitly specifies the return type of this {@code Invokable}. For example:    *    *<pre>{@code    * Method factoryMethod = Person.class.getMethod("create");    * Invokable<?, Person> factory = Invokable.of(getNameMethod).returning(Person.class);    * }</pre>    */
DECL|method|returning (Class<R1> returnType)
specifier|public
specifier|final
parameter_list|<
name|R1
extends|extends
name|R
parameter_list|>
name|Invokable
argument_list|<
name|T
argument_list|,
name|R1
argument_list|>
name|returning
parameter_list|(
name|Class
argument_list|<
name|R1
argument_list|>
name|returnType
parameter_list|)
block|{
return|return
name|returning
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|returnType
argument_list|)
argument_list|)
return|;
block|}
comment|/** Explicitly specifies the return type of this {@code Invokable}. */
DECL|method|returning (TypeToken<R1> returnType)
specifier|public
specifier|final
parameter_list|<
name|R1
extends|extends
name|R
parameter_list|>
name|Invokable
argument_list|<
name|T
argument_list|,
name|R1
argument_list|>
name|returning
parameter_list|(
name|TypeToken
argument_list|<
name|R1
argument_list|>
name|returnType
parameter_list|)
block|{
if|if
condition|(
operator|!
name|returnType
operator|.
name|isSupertypeOf
argument_list|(
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invokable is known to return "
operator|+
name|getReturnType
argument_list|()
operator|+
literal|", not "
operator|+
name|returnType
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// guarded by previous check
name|Invokable
argument_list|<
name|T
argument_list|,
name|R1
argument_list|>
name|specialized
init|=
operator|(
name|Invokable
argument_list|<
name|T
argument_list|,
name|R1
argument_list|>
operator|)
name|this
decl_stmt|;
return|return
name|specialized
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// The declaring class is T's raw class, or one of its supertypes.
annotation|@
name|Override
DECL|method|getDeclaringClass ()
specifier|public
specifier|final
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getDeclaringClass
parameter_list|()
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
operator|)
name|super
operator|.
name|getDeclaringClass
argument_list|()
return|;
block|}
comment|/** Returns the type of {@code T}. */
comment|// Overridden in TypeToken#method() and TypeToken#constructor()
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// The declaring class is T.
annotation|@
name|Override
DECL|method|getOwnerType ()
specifier|public
name|TypeToken
argument_list|<
name|T
argument_list|>
name|getOwnerType
parameter_list|()
block|{
return|return
operator|(
name|TypeToken
argument_list|<
name|T
argument_list|>
operator|)
name|TypeToken
operator|.
name|of
argument_list|(
name|getDeclaringClass
argument_list|()
argument_list|)
return|;
block|}
DECL|method|invokeInternal (@ullableDecl Object receiver, Object[] args)
specifier|abstract
name|Object
name|invokeInternal
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|receiver
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
function_decl|;
DECL|method|getGenericParameterTypes ()
specifier|abstract
name|Type
index|[]
name|getGenericParameterTypes
parameter_list|()
function_decl|;
comment|/** This should never return a type that's not a subtype of Throwable. */
DECL|method|getGenericExceptionTypes ()
specifier|abstract
name|Type
index|[]
name|getGenericExceptionTypes
parameter_list|()
function_decl|;
DECL|method|getParameterAnnotations ()
specifier|abstract
name|Annotation
index|[]
index|[]
name|getParameterAnnotations
parameter_list|()
function_decl|;
DECL|method|getGenericReturnType ()
specifier|abstract
name|Type
name|getGenericReturnType
parameter_list|()
function_decl|;
DECL|class|MethodInvokable
specifier|static
class|class
name|MethodInvokable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Invokable
argument_list|<
name|T
argument_list|,
name|Object
argument_list|>
block|{
DECL|field|method
specifier|final
name|Method
name|method
decl_stmt|;
DECL|method|MethodInvokable (Method method)
name|MethodInvokable
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invokeInternal (@ullableDecl Object receiver, Object[] args)
specifier|final
name|Object
name|invokeInternal
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|receiver
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|receiver
argument_list|,
name|args
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getGenericReturnType ()
name|Type
name|getGenericReturnType
parameter_list|()
block|{
return|return
name|method
operator|.
name|getGenericReturnType
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getGenericParameterTypes ()
name|Type
index|[]
name|getGenericParameterTypes
parameter_list|()
block|{
return|return
name|method
operator|.
name|getGenericParameterTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getGenericExceptionTypes ()
name|Type
index|[]
name|getGenericExceptionTypes
parameter_list|()
block|{
return|return
name|method
operator|.
name|getGenericExceptionTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getParameterAnnotations ()
specifier|final
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
DECL|method|getTypeParameters ()
specifier|public
specifier|final
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|getTypeParameters
parameter_list|()
block|{
return|return
name|method
operator|.
name|getTypeParameters
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isOverridable ()
specifier|public
specifier|final
name|boolean
name|isOverridable
parameter_list|()
block|{
return|return
operator|!
operator|(
name|isFinal
argument_list|()
operator|||
name|isPrivate
argument_list|()
operator|||
name|isStatic
argument_list|()
operator|||
name|Modifier
operator|.
name|isFinal
argument_list|(
name|getDeclaringClass
argument_list|()
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|isVarArgs ()
specifier|public
specifier|final
name|boolean
name|isVarArgs
parameter_list|()
block|{
return|return
name|method
operator|.
name|isVarArgs
argument_list|()
return|;
block|}
block|}
DECL|class|ConstructorInvokable
specifier|static
class|class
name|ConstructorInvokable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Invokable
argument_list|<
name|T
argument_list|,
name|T
argument_list|>
block|{
DECL|field|constructor
specifier|final
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
decl_stmt|;
DECL|method|ConstructorInvokable (Constructor<?> constructor)
name|ConstructorInvokable
parameter_list|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
parameter_list|)
block|{
name|super
argument_list|(
name|constructor
argument_list|)
expr_stmt|;
name|this
operator|.
name|constructor
operator|=
name|constructor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|invokeInternal (@ullableDecl Object receiver, Object[] args)
specifier|final
name|Object
name|invokeInternal
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|receiver
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
block|{
try|try
block|{
return|return
name|constructor
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|constructor
operator|+
literal|" failed."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * If the class is parameterized, such as {@link java.util.ArrayList ArrayList}, this returns      * {@code ArrayList<E>}.      */
annotation|@
name|Override
DECL|method|getGenericReturnType ()
name|Type
name|getGenericReturnType
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
init|=
name|getDeclaringClass
argument_list|()
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|typeParams
init|=
name|declaringClass
operator|.
name|getTypeParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeParams
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|declaringClass
argument_list|,
name|typeParams
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|declaringClass
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getGenericParameterTypes ()
name|Type
index|[]
name|getGenericParameterTypes
parameter_list|()
block|{
name|Type
index|[]
name|types
init|=
name|constructor
operator|.
name|getGenericParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|length
operator|>
literal|0
operator|&&
name|mayNeedHiddenThis
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|rawParamTypes
init|=
name|constructor
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|types
operator|.
name|length
operator|==
name|rawParamTypes
operator|.
name|length
operator|&&
name|rawParamTypes
index|[
literal|0
index|]
operator|==
name|getDeclaringClass
argument_list|()
operator|.
name|getEnclosingClass
argument_list|()
condition|)
block|{
comment|// first parameter is the hidden 'this'
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|types
argument_list|,
literal|1
argument_list|,
name|types
operator|.
name|length
argument_list|)
return|;
block|}
block|}
return|return
name|types
return|;
block|}
annotation|@
name|Override
DECL|method|getGenericExceptionTypes ()
name|Type
index|[]
name|getGenericExceptionTypes
parameter_list|()
block|{
return|return
name|constructor
operator|.
name|getGenericExceptionTypes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getParameterAnnotations ()
specifier|final
name|Annotation
index|[]
index|[]
name|getParameterAnnotations
parameter_list|()
block|{
return|return
name|constructor
operator|.
name|getParameterAnnotations
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      *      *<p>{@code [<E>]} will be returned for ArrayList's constructor. When both the class and the      * constructor have type parameters, the class parameters are prepended before those of the      * constructor's. This is an arbitrary rule since no existing language spec mandates one way or      * the other. From the declaration syntax, the class type parameter appears first, but the call      * syntax may show up in opposite order such as {@code new<A>Foo<B>()}.      */
annotation|@
name|Override
DECL|method|getTypeParameters ()
specifier|public
specifier|final
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|getTypeParameters
parameter_list|()
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|declaredByClass
init|=
name|getDeclaringClass
argument_list|()
operator|.
name|getTypeParameters
argument_list|()
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|declaredByConstructor
init|=
name|constructor
operator|.
name|getTypeParameters
argument_list|()
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|result
init|=
operator|new
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[
name|declaredByClass
operator|.
name|length
operator|+
name|declaredByConstructor
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|declaredByClass
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|declaredByClass
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|declaredByConstructor
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|declaredByClass
operator|.
name|length
argument_list|,
name|declaredByConstructor
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|isOverridable ()
specifier|public
specifier|final
name|boolean
name|isOverridable
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isVarArgs ()
specifier|public
specifier|final
name|boolean
name|isVarArgs
parameter_list|()
block|{
return|return
name|constructor
operator|.
name|isVarArgs
argument_list|()
return|;
block|}
DECL|method|mayNeedHiddenThis ()
specifier|private
name|boolean
name|mayNeedHiddenThis
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|declaringClass
init|=
name|constructor
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|declaringClass
operator|.
name|getEnclosingConstructor
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Enclosed in a constructor, needs hidden this
return|return
literal|true
return|;
block|}
name|Method
name|enclosingMethod
init|=
name|declaringClass
operator|.
name|getEnclosingMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|enclosingMethod
operator|!=
literal|null
condition|)
block|{
comment|// Enclosed in a method, if it's not static, must need hidden this.
return|return
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|enclosingMethod
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// Strictly, this doesn't necessarily indicate a hidden 'this' in the case of
comment|// static initializer. But there seems no way to tell in that case. :(
comment|// This may cause issues when an anonymous class is created inside a static initializer,
comment|// and the class's constructor's first parameter happens to be the enclosing class.
comment|// In such case, we may mistakenly think that the class is within a non-static context
comment|// and the first parameter is the hidden 'this'.
return|return
name|declaringClass
operator|.
name|getEnclosingClass
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|declaringClass
operator|.
name|getModifiers
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

