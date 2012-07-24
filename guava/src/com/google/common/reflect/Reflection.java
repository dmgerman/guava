begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2005 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Proxy
import|;
end_import

begin_comment
comment|/**  * Static utilities relating to Java reflection.  *  * @since 12.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Reflection
specifier|public
specifier|final
class|class
name|Reflection
block|{
comment|/**    * Returns the package name of {@code clazz} according to the Java Language Specification (section    * 6.7). Unlike {@link Class#getPackage}, this method only parses the class name, without    * attempting to define the {@link Package} and hence load files.    */
DECL|method|getPackageName (Class<?> clazz)
specifier|public
specifier|static
name|String
name|getPackageName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|getPackageName
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the package name of {@code classFullName} according to the Java Language Specification    * (section 6.7). Unlike {@link Class#getPackage}, this method only parses the class name, without    * attempting to define the {@link Package} and hence load files.    */
DECL|method|getPackageName (String classFullName)
specifier|public
specifier|static
name|String
name|getPackageName
parameter_list|(
name|String
name|classFullName
parameter_list|)
block|{
name|int
name|lastDot
init|=
name|classFullName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
operator|(
name|lastDot
operator|<
literal|0
operator|)
condition|?
literal|""
else|:
name|classFullName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastDot
argument_list|)
return|;
block|}
comment|/**    * Ensures that the given classes are initialized, as described in    *<a href="http://java.sun.com/docs/books/jls/third_edition/html/execution.html#12.4.2">    * JLS Section 12.4.2</a>.    *    *<p>WARNING: Normally it's a smell if a class needs to be explicitly initialized, because static    * state hurts system maintainability and testability. In cases when you have no choice while    * inter-operating with a legacy framework, this method helps to keep the code less ugly.    *    * @throws ExceptionInInitializerError if an exception is thrown during    *   initialization of a class    */
DECL|method|initialize (Class<?>.... classes)
specifier|public
specifier|static
name|void
name|initialize
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|classes
parameter_list|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|clazz
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|,
name|clazz
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Returns a proxy instance that implements {@code interfaceType} by    * dispatching method invocations to {@code handler}. The class loader of    * {@code interfaceType} will be used to define the proxy class. To implement    * multiple interfaces or specify a class loader, use    * {@link Proxy#newProxyInstance}.    *    * @throws IllegalArgumentException if {@code interfaceType} does not specify    *     the type of a Java interface    */
DECL|method|newProxy ( Class<T> interfaceType, InvocationHandler handler)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|InvocationHandler
name|handler
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|interfaceType
operator|.
name|isInterface
argument_list|()
argument_list|,
literal|"%s is not an interface"
argument_list|,
name|interfaceType
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|interfaceType
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|interfaceType
block|}
operator|,
name|handler
block|)
function|;
return|return
name|interfaceType
operator|.
name|cast
argument_list|(
name|object
argument_list|)
return|;
block|}
end_class

begin_constructor
DECL|method|Reflection ()
specifier|private
name|Reflection
parameter_list|()
block|{}
end_constructor

unit|}
end_unit

