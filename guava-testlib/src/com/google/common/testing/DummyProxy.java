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
name|testing
operator|.
name|NullPointerTester
operator|.
name|isNullable
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
name|Sets
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
name|TypeToken
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Generates a dummy interface proxy that simply returns a dummy value for each method.  *  * @author Ben Yu  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|DummyProxy
specifier|abstract
class|class
name|DummyProxy
block|{
comment|/**    * Returns a new proxy for {@code interfaceType}. Proxies of the same interface are equal to each    * other if the {@link DummyProxy} instance that created the proxies are equal.    */
DECL|method|newProxy (TypeToken<T> interfaceType)
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|interfaceClasses
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
decl_stmt|;
name|interfaceClasses
operator|.
name|addAll
argument_list|(
name|interfaceType
operator|.
name|getTypes
argument_list|()
operator|.
name|interfaces
argument_list|()
operator|.
name|rawTypes
argument_list|()
argument_list|)
expr_stmt|;
comment|// Make the proxy serializable to work with SerializableTester
name|interfaceClasses
operator|.
name|add
argument_list|(
name|Serializable
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|dummy
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|interfaceClasses
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|interfaceClasses
operator|.
name|toArray
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|interfaceClasses
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|,
operator|new
name|DummyHandler
argument_list|(
name|interfaceType
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// interfaceType is T
name|T
name|result
init|=
operator|(
name|T
operator|)
name|dummy
decl_stmt|;
return|return
name|result
return|;
block|}
comment|/** Returns the dummy return value for {@code returnType}. */
DECL|method|dummyReturnValue (TypeToken<R> returnType)
specifier|abstract
parameter_list|<
name|R
parameter_list|>
name|R
name|dummyReturnValue
parameter_list|(
name|TypeToken
argument_list|<
name|R
argument_list|>
name|returnType
parameter_list|)
function_decl|;
DECL|class|DummyHandler
specifier|private
class|class
name|DummyHandler
extends|extends
name|AbstractInvocationHandler
implements|implements
name|Serializable
block|{
DECL|field|interfaceType
specifier|private
specifier|final
name|TypeToken
argument_list|<
name|?
argument_list|>
name|interfaceType
decl_stmt|;
DECL|method|DummyHandler (TypeToken<?> interfaceType)
name|DummyHandler
parameter_list|(
name|TypeToken
argument_list|<
name|?
argument_list|>
name|interfaceType
parameter_list|)
block|{
name|this
operator|.
name|interfaceType
operator|=
name|interfaceType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleInvocation (Object proxy, Method method, Object[] args)
specifier|protected
name|Object
name|handleInvocation
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
block|{
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|invokable
init|=
name|interfaceType
operator|.
name|method
argument_list|(
name|method
argument_list|)
decl_stmt|;
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
operator|!
name|isNullable
argument_list|(
name|param
argument_list|)
condition|)
block|{
name|checkNotNull
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dummyReturnValue
argument_list|(
name|interfaceType
operator|.
name|resolveType
argument_list|(
name|method
operator|.
name|getGenericReturnType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|identity
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
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
name|DummyHandler
condition|)
block|{
name|DummyHandler
name|that
init|=
operator|(
name|DummyHandler
operator|)
name|obj
decl_stmt|;
return|return
name|identity
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|identity
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|identity ()
specifier|private
name|DummyProxy
name|identity
parameter_list|()
block|{
return|return
name|DummyProxy
operator|.
name|this
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
literal|"Dummy proxy for "
operator|+
name|interfaceType
return|;
block|}
comment|// Since type variables aren't serializable, reduce the type down to raw type before
comment|// serialization.
DECL|method|writeReplace ()
specifier|private
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|DummyHandler
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|interfaceType
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

