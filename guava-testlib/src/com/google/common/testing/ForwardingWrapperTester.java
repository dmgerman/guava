begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Throwables
operator|.
name|throwIfUnchecked
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|fail
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
name|Throwables
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
name|reflect
operator|.
name|AbstractInvocationHandler
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
name|Modifier
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_comment
comment|/**  * Tester to ensure forwarding wrapper works by delegating calls to the corresponding method with  * the same parameters forwarded and return value forwarded back or exception propagated as is.  *  *<p>For example:  *  *<pre>{@code  * new ForwardingWrapperTester().testForwarding(Foo.class, new Function<Foo, Foo>() {  *   public Foo apply(Foo foo) {  *     return new ForwardingFoo(foo);  *   }  * });  * }</pre>  *  * @author Ben Yu  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|ForwardingWrapperTester
specifier|public
specifier|final
class|class
name|ForwardingWrapperTester
block|{
DECL|field|testsEquals
specifier|private
name|boolean
name|testsEquals
init|=
literal|false
decl_stmt|;
comment|/**    * Asks for {@link Object#equals} and {@link Object#hashCode} to be tested. That is, forwarding    * wrappers of equal instances should be equal.    */
DECL|method|includingEquals ()
specifier|public
name|ForwardingWrapperTester
name|includingEquals
parameter_list|()
block|{
name|this
operator|.
name|testsEquals
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Tests that the forwarding wrapper returned by {@code wrapperFunction} properly forwards method    * calls with parameters passed as is, return value returned as is, and exceptions propagated as    * is.    */
DECL|method|testForwarding ( Class<T> interfaceType, Function<? super T, ? extends T> wrapperFunction)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|testForwarding
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|wrapperFunction
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|interfaceType
operator|.
name|isInterface
argument_list|()
argument_list|,
literal|"%s isn't an interface"
argument_list|,
name|interfaceType
argument_list|)
expr_stmt|;
name|Method
index|[]
name|methods
init|=
name|getMostConcreteMethods
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|AccessibleObject
operator|.
name|setAccessible
argument_list|(
name|methods
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
comment|// Under java 8, interfaces can have default methods that aren't abstract.
comment|// No need to verify them.
comment|// Can't check isDefault() for JDK 7 compatibility.
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isAbstract
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// The interface could be package-private or private.
comment|// filter out equals/hashCode/toString
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"equals"
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|==
name|Object
operator|.
name|class
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"hashCode"
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"toString"
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|testSuccessfulForwarding
argument_list|(
name|interfaceType
argument_list|,
name|method
argument_list|,
name|wrapperFunction
argument_list|)
expr_stmt|;
name|testExceptionPropagation
argument_list|(
name|interfaceType
argument_list|,
name|method
argument_list|,
name|wrapperFunction
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|testsEquals
condition|)
block|{
name|testEquals
argument_list|(
name|interfaceType
argument_list|,
name|wrapperFunction
argument_list|)
expr_stmt|;
block|}
name|testToString
argument_list|(
name|interfaceType
argument_list|,
name|wrapperFunction
argument_list|)
expr_stmt|;
block|}
comment|/** Returns the most concrete public methods from {@code type}. */
DECL|method|getMostConcreteMethods (Class<?> type)
specifier|private
specifier|static
name|Method
index|[]
name|getMostConcreteMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|Method
index|[]
name|methods
init|=
name|type
operator|.
name|getMethods
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
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|methods
index|[
name|i
index|]
operator|=
name|type
operator|.
name|getMethod
argument_list|(
name|methods
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|,
name|methods
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|methods
return|;
block|}
DECL|method|testSuccessfulForwarding ( Class<T> interfaceType, Method method, Function<? super T, ? extends T> wrapperFunction)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testSuccessfulForwarding
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Method
name|method
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
operator|new
name|InteractionTester
argument_list|<
name|T
argument_list|>
argument_list|(
name|interfaceType
argument_list|,
name|method
argument_list|)
operator|.
name|testInteraction
argument_list|(
name|wrapperFunction
argument_list|)
expr_stmt|;
block|}
DECL|method|testExceptionPropagation ( Class<T> interfaceType, Method method, Function<? super T, ? extends T> wrapperFunction)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testExceptionPropagation
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Method
name|method
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
name|RuntimeException
name|exception
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|T
name|proxy
init|=
name|Reflection
operator|.
name|newProxy
argument_list|(
name|interfaceType
argument_list|,
operator|new
name|AbstractInvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Object
name|handleInvocation
parameter_list|(
name|Object
name|p
parameter_list|,
name|Method
name|m
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
throw|throw
name|exception
throw|;
block|}
block|}
argument_list|)
decl_stmt|;
name|T
name|wrapper
init|=
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|proxy
argument_list|)
decl_stmt|;
try|try
block|{
name|method
operator|.
name|invoke
argument_list|(
name|wrapper
argument_list|,
name|getParameterValues
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
name|method
operator|+
literal|" failed to throw exception as is."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
if|if
condition|(
name|exception
operator|!=
name|e
operator|.
name|getCause
argument_list|()
condition|)
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
catch|catch
parameter_list|(
name|IllegalAccessException
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
DECL|method|testEquals ( Class<T> interfaceType, Function<? super T, ? extends T> wrapperFunction)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testEquals
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
name|FreshValueGenerator
name|generator
init|=
operator|new
name|FreshValueGenerator
argument_list|()
decl_stmt|;
name|T
name|instance
init|=
name|generator
operator|.
name|newFreshProxy
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|instance
argument_list|)
argument_list|,
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|instance
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|generator
operator|.
name|newFreshProxy
argument_list|(
name|interfaceType
argument_list|)
argument_list|)
argument_list|)
comment|// TODO: add an overload to EqualsTester to print custom error message?
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testToString ( Class<T> interfaceType, Function<? super T, ? extends T> wrapperFunction)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testToString
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
name|T
name|proxy
init|=
operator|new
name|FreshValueGenerator
argument_list|()
operator|.
name|newFreshProxy
argument_list|(
name|interfaceType
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"toString() isn't properly forwarded"
argument_list|,
name|proxy
operator|.
name|toString
argument_list|()
argument_list|,
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|proxy
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getParameterValues (Method method)
specifier|private
specifier|static
name|Object
index|[]
name|getParameterValues
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|FreshValueGenerator
name|paramValues
init|=
operator|new
name|FreshValueGenerator
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|passedArgs
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|paramType
range|:
name|method
operator|.
name|getParameterTypes
argument_list|()
control|)
block|{
name|passedArgs
operator|.
name|add
argument_list|(
name|paramValues
operator|.
name|generateFresh
argument_list|(
name|paramType
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|passedArgs
operator|.
name|toArray
argument_list|()
return|;
block|}
comment|/** Tests a single interaction against a method. */
DECL|class|InteractionTester
specifier|private
specifier|static
specifier|final
class|class
name|InteractionTester
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractInvocationHandler
block|{
DECL|field|interfaceType
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|passedArgs
specifier|private
specifier|final
name|Object
index|[]
name|passedArgs
decl_stmt|;
DECL|field|returnValue
specifier|private
specifier|final
name|Object
name|returnValue
decl_stmt|;
DECL|field|called
specifier|private
specifier|final
name|AtomicInteger
name|called
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|InteractionTester (Class<T> interfaceType, Method method)
name|InteractionTester
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|interfaceType
operator|=
name|interfaceType
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|passedArgs
operator|=
name|getParameterValues
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|this
operator|.
name|returnValue
operator|=
operator|new
name|FreshValueGenerator
argument_list|()
operator|.
name|generateFresh
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleInvocation (Object p, Method calledMethod, Object[] args)
specifier|protected
name|Object
name|handleInvocation
parameter_list|(
name|Object
name|p
parameter_list|,
name|Method
name|calledMethod
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
name|assertEquals
argument_list|(
name|method
argument_list|,
name|calledMethod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|method
operator|+
literal|" invoked more than once."
argument_list|,
literal|0
argument_list|,
name|called
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|passedArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Parameter #"
operator|+
name|i
operator|+
literal|" of "
operator|+
name|method
operator|+
literal|" not forwarded"
argument_list|,
name|passedArgs
index|[
name|i
index|]
argument_list|,
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|called
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
return|return
name|returnValue
return|;
block|}
DECL|method|testInteraction (Function<? super T, ? extends T> wrapperFunction)
name|void
name|testInteraction
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|wrapperFunction
parameter_list|)
block|{
name|T
name|proxy
init|=
name|Reflection
operator|.
name|newProxy
argument_list|(
name|interfaceType
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|T
name|wrapper
init|=
name|wrapperFunction
operator|.
name|apply
argument_list|(
name|proxy
argument_list|)
decl_stmt|;
name|boolean
name|isPossibleChainingCall
init|=
name|interfaceType
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Object
name|actualReturnValue
init|=
name|method
operator|.
name|invoke
argument_list|(
name|wrapper
argument_list|,
name|passedArgs
argument_list|)
decl_stmt|;
comment|// If we think this might be a 'chaining' call then we allow the return value to either
comment|// be the wrapper or the returnValue.
if|if
condition|(
operator|!
name|isPossibleChainingCall
operator|||
name|wrapper
operator|!=
name|actualReturnValue
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Return value of "
operator|+
name|method
operator|+
literal|" not forwarded"
argument_list|,
name|returnValue
argument_list|,
name|actualReturnValue
argument_list|)
expr_stmt|;
block|}
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
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|Throwables
operator|.
name|propagate
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
name|assertEquals
argument_list|(
literal|"Failed to forward to "
operator|+
name|method
argument_list|,
literal|1
argument_list|,
name|called
operator|.
name|get
argument_list|()
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
literal|"dummy "
operator|+
name|interfaceType
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

