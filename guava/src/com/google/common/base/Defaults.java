begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|HashMap
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
comment|/**  * This class provides default values for all Java types, as defined by the JLS.  *  * @author Ben Yu  * @since 1.0  */
end_comment

begin_class
DECL|class|Defaults
specifier|public
specifier|final
class|class
name|Defaults
block|{
DECL|method|Defaults ()
specifier|private
name|Defaults
parameter_list|()
block|{}
DECL|field|DEFAULTS
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
name|Object
argument_list|>
name|DEFAULTS
decl_stmt|;
static|static
block|{
comment|// Only add to this map via put(Map, Class<T>, T)
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|put
argument_list|(
name|map
argument_list|,
name|boolean
operator|.
name|class
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|map
argument_list|,
name|char
operator|.
name|class
argument_list|,
literal|'\0'
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|map
argument_list|,
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
name|put
argument_list|(
name|map
argument_list|,
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
name|put
argument_list|(
name|map
argument_list|,
name|int
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|map
argument_list|,
name|long
operator|.
name|class
argument_list|,
literal|0L
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|map
argument_list|,
name|float
operator|.
name|class
argument_list|,
literal|0f
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|map
argument_list|,
name|double
operator|.
name|class
argument_list|,
literal|0d
argument_list|)
expr_stmt|;
name|DEFAULTS
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|put (Map<Class<?>, Object> map, Class<T> type, T value)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
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
name|map
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the default value of {@code type} as defined by JLS --- {@code 0} for numbers, {@code    * false} for {@code boolean} and {@code '\0'} for {@code char}. For non-primitive types and    * {@code void}, null is returned.    */
annotation|@
name|Nullable
DECL|method|defaultValue (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|defaultValue
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
comment|// Primitives.wrap(type).cast(...) would avoid the warning, but we can't use that from here
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// the put method enforces this key-value relationship
name|T
name|t
init|=
operator|(
name|T
operator|)
name|DEFAULTS
operator|.
name|get
argument_list|(
name|checkNotNull
argument_list|(
name|type
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|t
return|;
block|}
block|}
end_class

end_unit

