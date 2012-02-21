begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtCompatible
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
name|Field
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
comment|/**  * Utility methods for working with {@link Enum} instances.  *  * @author Steve McKay  *  * @since 9.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|Beta
DECL|class|Enums
specifier|public
specifier|final
class|class
name|Enums
block|{
DECL|method|Enums ()
specifier|private
name|Enums
parameter_list|()
block|{}
comment|/**    * Returns the {@link Field} in which {@code enumValue} is defined.    * For example, to get the {@code Description} annotation on the {@code GOLF}    * constant of enum {@code Sport}, use    * {@code Enums.getField(Sport.GOLF).getAnnotation(Description.class)}.    *    * @since 12.0    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getField (Enum<?> enumValue)
specifier|public
specifier|static
name|Field
name|getField
parameter_list|(
name|Enum
argument_list|<
name|?
argument_list|>
name|enumValue
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|enumValue
operator|.
name|getDeclaringClass
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|clazz
operator|.
name|getDeclaredField
argument_list|(
name|enumValue
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|impossible
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|impossible
argument_list|)
throw|;
block|}
block|}
comment|/**    * Returns a {@link Function} that maps an {@link Enum} name to the associated    * {@code Enum} constant. The {@code Function} will return {@code null} if the    * {@code Enum} constant does not exist.    *    * @param enumClass the {@link Class} of the {@code Enum} declaring the    *     constant values.    */
DECL|method|valueOfFunction (Class<T> enumClass)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|Function
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|valueOfFunction
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
return|return
operator|new
name|ValueOfFunction
argument_list|<
name|T
argument_list|>
argument_list|(
name|enumClass
argument_list|)
return|;
block|}
comment|/**    * A {@link Function} that maps an {@link Enum} name to the associated    * constant, or {@code null} if the constant does not exist.    */
DECL|class|ValueOfFunction
specifier|private
specifier|static
specifier|final
class|class
name|ValueOfFunction
parameter_list|<
name|T
extends|extends
name|Enum
parameter_list|<
name|T
parameter_list|>
parameter_list|>
implements|implements
name|Function
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|enumClass
specifier|private
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
decl_stmt|;
DECL|method|ValueOfFunction (Class<T> enumClass)
specifier|private
name|ValueOfFunction
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
name|this
operator|.
name|enumClass
operator|=
name|checkNotNull
argument_list|(
name|enumClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (String value)
specifier|public
name|T
name|apply
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|ValueOfFunction
operator|&&
name|enumClass
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ValueOfFunction
operator|)
name|obj
operator|)
operator|.
name|enumClass
argument_list|)
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
name|enumClass
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Enums.valueOf("
operator|+
name|enumClass
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns an optional enum constant for the given type, using {@link Enum#valueOf}. If the    * constant does not exist, {@link Optional#absent} is returned. A common use case is for parsing    * user input or falling back to a default enum constant. For example,    * {@code Enums.getIfPresent(Country.class, countryInput).or(Country.DEFAULT);}    *    * @since 12.0    */
DECL|method|getIfPresent (Class<T> enumClass, String value)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getIfPresent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|enumClass
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|value
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

