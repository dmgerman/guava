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
name|util
operator|.
name|Map
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
comment|/**  * A map, each entry of which maps a {@link TypeToken} to an instance of that type. In addition to  * implementing {@code Map}, the additional type-safe operations {@link #putInstance} and {@link  * #getInstance} are available.  *  *<p>Generally, implementations don't support {@link #put} and {@link #putAll} because there is no  * way to check an object at runtime to be an instance of a {@link TypeToken}. Instead, caller  * should use the type safe {@link #putInstance}.  *  *<p>Also, if caller suppresses unchecked warnings and passes in an {@code Iterable<String>} for  * type {@code Iterable<Integer>}, the map won't be able to detect and throw type error.  *  *<p>Like any other {@code Map<Class, Object>}, this map may contain entries for primitive types,  * and a primitive type and its corresponding wrapper type may map to different values.  *  * @param<B> the common supertype that all entries must share; often this is simply {@link Object}  * @author Ben Yu  * @since 13.0  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|TypeToInstanceMap
specifier|public
interface|interface
name|TypeToInstanceMap
parameter_list|<
name|B
parameter_list|>
extends|extends
name|Map
argument_list|<
name|TypeToken
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
block|{
comment|/**    * Returns the value the specified class is mapped to, or {@code null} if no entry for this class    * is present. This will only return a value that was bound to this specific class, not a value    * that may have been bound to a subtype.    *    *<p>{@code getInstance(Foo.class)} is equivalent to {@code    * getInstance(TypeToken.of(Foo.class))}.    */
annotation|@
name|NullableDecl
DECL|method|getInstance (Class<T> type)
argument_list|<
name|T
extends|extends
name|B
argument_list|>
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
comment|/**    * Returns the value the specified type is mapped to, or {@code null} if no entry for this type is    * present. This will only return a value that was bound to this specific type, not a value that    * may have been bound to a subtype.    */
annotation|@
name|NullableDecl
DECL|method|getInstance (TypeToken<T> type)
argument_list|<
name|T
extends|extends
name|B
argument_list|>
name|T
name|getInstance
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**    * Maps the specified class to the specified value. Does<i>not</i> associate this value with any    * of the class's supertypes.    *    *<p>{@code putInstance(Foo.class, foo)} is equivalent to {@code    * putInstance(TypeToken.of(Foo.class), foo)}.    *    * @return the value previously associated with this class (possibly {@code null}), or {@code    *     null} if there was no previous entry.    */
annotation|@
name|NullableDecl
annotation|@
name|CanIgnoreReturnValue
DECL|method|putInstance (Class<T> type, @NullableDecl T value)
argument_list|<
name|T
extends|extends
name|B
argument_list|>
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
name|NullableDecl
name|T
name|value
parameter_list|)
function_decl|;
comment|/**    * Maps the specified type to the specified value. Does<i>not</i> associate this value with any    * of the type's supertypes.    *    * @return the value previously associated with this type (possibly {@code null}), or {@code null}    *     if there was no previous entry.    */
annotation|@
name|NullableDecl
annotation|@
name|CanIgnoreReturnValue
DECL|method|putInstance (TypeToken<T> type, @NullableDecl T value)
argument_list|<
name|T
extends|extends
name|B
argument_list|>
name|T
name|putInstance
parameter_list|(
name|TypeToken
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
annotation|@
name|NullableDecl
name|T
name|value
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

