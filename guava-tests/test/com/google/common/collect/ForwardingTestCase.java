begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|Joiner
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
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
name|InvocationHandler
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
name|Proxy
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Base test case for testing the variety of forwarding classes.  *  * @author Robert Konigsberg  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|ForwardingTestCase
specifier|public
specifier|abstract
class|class
name|ForwardingTestCase
extends|extends
name|TestCase
block|{
DECL|field|calls
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|calls
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|called (String id)
specifier|private
name|void
name|called
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|calls
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
DECL|method|getCalls ()
specifier|protected
name|String
name|getCalls
parameter_list|()
block|{
return|return
name|calls
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isCalled ()
specifier|protected
name|boolean
name|isCalled
parameter_list|()
block|{
return|return
operator|!
name|calls
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createProxyInstance (Class<T> c)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxyInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
comment|/*      * This invocation handler only registers that a method was called,      * and then returns a bogus, but acceptable, value.      */
name|InvocationHandler
name|handler
init|=
operator|new
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|called
argument_list|(
name|asString
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|getDefaultValue
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|c
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|c
block|}
argument_list|,
name|handler
argument_list|)
return|;
block|}
DECL|field|COMMA_JOINER
specifier|private
specifier|static
specifier|final
name|Joiner
name|COMMA_JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|","
argument_list|)
decl_stmt|;
comment|/*    * Returns string representation of a method.    *    * If the method takes no parameters, it returns the name (e.g.    * "isEmpty". If the method takes parameters, it returns the simple names    * of the parameters (e.g. "put(Object,Object)".)    */
DECL|method|asString (Method method)
specifier|private
name|String
name|asString
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameterTypes
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|methodName
return|;
block|}
name|Iterable
argument_list|<
name|String
argument_list|>
name|parameterNames
init|=
name|Iterables
operator|.
name|transform
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|parameterTypes
argument_list|)
argument_list|,
operator|new
name|Function
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|from
parameter_list|)
block|{
return|return
name|from
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
block|)
function|;
return|return
name|methodName
operator|+
literal|"("
operator|+
name|COMMA_JOINER
operator|.
name|join
argument_list|(
name|parameterNames
argument_list|)
operator|+
literal|")"
return|;
block|}
end_class

begin_function
DECL|method|getDefaultValue (Class<?> returnType)
specifier|private
specifier|static
name|Object
name|getDefaultValue
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
parameter_list|)
block|{
if|if
condition|(
name|returnType
operator|==
name|boolean
operator|.
name|class
operator|||
name|returnType
operator|==
name|Boolean
operator|.
name|class
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
elseif|else
if|if
condition|(
name|returnType
operator|==
name|int
operator|.
name|class
operator|||
name|returnType
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
return|return
literal|0
return|;
block|}
elseif|else
if|if
condition|(
operator|(
name|returnType
operator|==
name|Set
operator|.
name|class
operator|)
operator|||
operator|(
name|returnType
operator|==
name|Collection
operator|.
name|class
operator|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|returnType
operator|==
name|Iterator
operator|.
name|class
condition|)
block|{
return|return
name|Iterators
operator|.
name|emptyModifiableIterator
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|returnType
operator|.
name|isArray
argument_list|()
condition|)
block|{
return|return
name|Array
operator|.
name|newInstance
argument_list|(
name|returnType
operator|.
name|getComponentType
argument_list|()
argument_list|,
literal|0
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
end_function

begin_function
DECL|method|callAllPublicMethods (Class<T> theClass, T object)
specifier|protected
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|callAllPublicMethods
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|theClass
parameter_list|,
name|T
name|object
parameter_list|)
throws|throws
name|InvocationTargetException
block|{
for|for
control|(
name|Method
name|method
range|:
name|theClass
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Object
index|[]
name|parameters
init|=
operator|new
name|Object
index|[
name|parameterTypes
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
name|parameterTypes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|parameters
index|[
name|i
index|]
operator|=
name|getDefaultValue
argument_list|(
name|parameterTypes
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
try|try
block|{
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|ex
parameter_list|)
block|{
try|try
block|{
throw|throw
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|unsupported
parameter_list|)
block|{
comment|// this is a legit exception
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InvocationTargetException
argument_list|(
name|cause
argument_list|,
name|method
operator|+
literal|" with args: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|parameters
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
end_function

unit|}
end_unit

