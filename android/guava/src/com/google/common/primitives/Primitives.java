begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|GwtIncompatible
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
name|LinkedHashMap
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

begin_comment
comment|/**  * Contains static utility methods pertaining to primitive types and their corresponding wrapper  * types.  *  * @author Kevin Bourrillion  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Primitives
specifier|public
specifier|final
class|class
name|Primitives
block|{
DECL|method|Primitives ()
specifier|private
name|Primitives
parameter_list|()
block|{}
comment|/** A map from primitive types to their corresponding wrapper types. */
DECL|field|PRIMITIVE_TO_WRAPPER_TYPE
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|PRIMITIVE_TO_WRAPPER_TYPE
decl_stmt|;
comment|/** A map from wrapper types to their corresponding primitive types. */
DECL|field|WRAPPER_TO_PRIMITIVE_TYPE
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|WRAPPER_TO_PRIMITIVE_TYPE
decl_stmt|;
comment|// Sad that we can't use a BiMap. :(
static|static
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|primToWrap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|16
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|wrapToPrim
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|16
argument_list|)
decl_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|boolean
operator|.
name|class
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|byte
operator|.
name|class
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|char
operator|.
name|class
argument_list|,
name|Character
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|double
operator|.
name|class
argument_list|,
name|Double
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|float
operator|.
name|class
argument_list|,
name|Float
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|int
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|long
operator|.
name|class
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|short
operator|.
name|class
argument_list|,
name|Short
operator|.
name|class
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|primToWrap
argument_list|,
name|wrapToPrim
argument_list|,
name|void
operator|.
name|class
argument_list|,
name|Void
operator|.
name|class
argument_list|)
expr_stmt|;
name|PRIMITIVE_TO_WRAPPER_TYPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|primToWrap
argument_list|)
expr_stmt|;
name|WRAPPER_TO_PRIMITIVE_TYPE
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|wrapToPrim
argument_list|)
expr_stmt|;
block|}
DECL|method|add ( Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value)
specifier|private
specifier|static
name|void
name|add
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|forward
parameter_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|backward
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|key
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
name|forward
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|backward
operator|.
name|put
argument_list|(
name|value
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns an immutable set of all nine primitive types (including {@code void}). Note that a    * simpler way to test whether a {@code Class} instance is a member of this set is to call {@link    * Class#isPrimitive}.    *    * @since 3.0    */
DECL|method|allPrimitiveTypes ()
specifier|public
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allPrimitiveTypes
parameter_list|()
block|{
return|return
name|PRIMITIVE_TO_WRAPPER_TYPE
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * Returns an immutable set of all nine primitive-wrapper types (including {@link Void}).    *    * @since 3.0    */
DECL|method|allWrapperTypes ()
specifier|public
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allWrapperTypes
parameter_list|()
block|{
return|return
name|WRAPPER_TO_PRIMITIVE_TYPE
operator|.
name|keySet
argument_list|()
return|;
block|}
comment|/**    * Returns {@code true} if {@code type} is one of the nine primitive-wrapper types, such as {@link    * Integer}.    *    * @see Class#isPrimitive    */
DECL|method|isWrapperType (Class<?> type)
specifier|public
specifier|static
name|boolean
name|isWrapperType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|WRAPPER_TO_PRIMITIVE_TYPE
operator|.
name|containsKey
argument_list|(
name|checkNotNull
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the corresponding wrapper type of {@code type} if it is a primitive type; otherwise    * returns {@code type} itself. Idempotent.    *    *<pre>    *     wrap(int.class) == Integer.class    *     wrap(Integer.class) == Integer.class    *     wrap(String.class) == String.class    *</pre>    */
DECL|method|wrap (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|wrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// cast is safe: long.class and Long.class are both of type Class<Long>
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|T
argument_list|>
name|wrapped
init|=
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|PRIMITIVE_TO_WRAPPER_TYPE
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|(
name|wrapped
operator|==
literal|null
operator|)
condition|?
name|type
else|:
name|wrapped
return|;
block|}
comment|/**    * Returns the corresponding primitive type of {@code type} if it is a wrapper type; otherwise    * returns {@code type} itself. Idempotent.    *    *<pre>    *     unwrap(Integer.class) == int.class    *     unwrap(int.class) == int.class    *     unwrap(String.class) == String.class    *</pre>    */
DECL|method|unwrap (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// cast is safe: long.class and Long.class are both of type Class<Long>
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|T
argument_list|>
name|unwrapped
init|=
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|WRAPPER_TO_PRIMITIVE_TYPE
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
operator|(
name|unwrapped
operator|==
literal|null
operator|)
condition|?
name|type
else|:
name|unwrapped
return|;
block|}
block|}
end_class

end_unit

