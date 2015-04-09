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
name|annotations
operator|.
name|GwtCompatible
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A map, each entry of which maps a Java  *<a href="http://tinyurl.com/2cmwkz">raw type</a> to an instance of that type.  * In addition to implementing {@code Map}, the additional type-safe operations  * {@link #putInstance} and {@link #getInstance} are available.  *  *<p>Like any other {@code Map<Class, Object>}, this map may contain entries  * for primitive types, and a primitive type and its corresponding wrapper type  * may map to different values.  *   *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/NewCollectionTypesExplained#ClassToInstanceMap">  * {@code ClassToInstanceMap}</a>.  *  *<p>To map a generic type to an instance of that type, use {@link  * com.google.common.reflect.TypeToInstanceMap} instead.  *  * @param<B> the common supertype that all entries must share; often this is  *     simply {@link Object}  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|ClassToInstanceMap
specifier|public
interface|interface
name|ClassToInstanceMap
parameter_list|<
name|B
parameter_list|>
extends|extends
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
block|{
comment|/**    * Returns the value the specified class is mapped to, or {@code null} if no    * entry for this class is present. This will only return a value that was    * bound to this specific class, not a value that may have been bound to a    * subtype.    */
DECL|method|getInstance (Class<T> type)
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**    * Maps the specified class to the specified value. Does<i>not</i> associate    * this value with any of the class's supertypes.    *    * @return the value previously associated with this class (possibly {@code    *     null}), or {@code null} if there was no previous entry.    */
DECL|method|putInstance (Class<T> type, @Nullable T value)
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|putInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
annotation|@
name|Nullable
name|T
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

