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
name|ref
operator|.
name|WeakReference
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
name|java
operator|.
name|util
operator|.
name|EnumSet
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
name|java
operator|.
name|util
operator|.
name|WeakHashMap
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
comment|/**    * Returns the {@link Field} in which {@code enumValue} is defined. For example, to get the    * {@code Description} annotation on the {@code GOLF} constant of enum {@code Sport}, use    * {@code Enums.getField(Sport.GOLF).getAnnotation(Description.class)}.    *    * @since 12.0    */
annotation|@
name|GwtIncompatible
comment|// reflection
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
return|return
name|Platform
operator|.
name|getEnumIfPresent
argument_list|(
name|enumClass
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|>
DECL|field|enumConstantCache
name|enumConstantCache
init|=
operator|new
name|WeakHashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|populateCache ( Class<T> enumClass)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|populateCache
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|enumInstance
range|:
name|EnumSet
operator|.
name|allOf
argument_list|(
name|enumClass
argument_list|)
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|enumInstance
operator|.
name|name
argument_list|()
argument_list|,
operator|new
name|WeakReference
argument_list|<
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|enumInstance
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|enumConstantCache
operator|.
name|put
argument_list|(
name|enumClass
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.lang.ref.WeakReference
DECL|method|getEnumConstants ( Class<T> enumClass)
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|getEnumConstants
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
synchronized|synchronized
init|(
name|enumConstantCache
init|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|constants
init|=
name|enumConstantCache
operator|.
name|get
argument_list|(
name|enumClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|constants
operator|==
literal|null
condition|)
block|{
name|constants
operator|=
name|populateCache
argument_list|(
name|enumClass
argument_list|)
expr_stmt|;
block|}
return|return
name|constants
return|;
block|}
block|}
comment|/**    * Returns a converter that converts between strings and {@code enum} values of type    * {@code enumClass} using {@link Enum#valueOf(Class, String)} and {@link Enum#name()}. The    * converter will throw an {@code IllegalArgumentException} if the argument is not the name of    * any enum constant in the specified enum.    *    * @since 16.0    */
DECL|method|stringConverter (final Class<T> enumClass)
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
name|Converter
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|stringConverter
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
return|return
operator|new
name|StringConverter
argument_list|<
name|T
argument_list|>
argument_list|(
name|enumClass
argument_list|)
return|;
block|}
DECL|class|StringConverter
specifier|private
specifier|static
specifier|final
class|class
name|StringConverter
parameter_list|<
name|T
extends|extends
name|Enum
parameter_list|<
name|T
parameter_list|>
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
implements|implements
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
DECL|method|StringConverter (Class<T> enumClass)
name|StringConverter
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
DECL|method|doForward (String value)
specifier|protected
name|T
name|doForward
parameter_list|(
name|String
name|value
parameter_list|)
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
annotation|@
name|Override
DECL|method|doBackward (T enumValue)
specifier|protected
name|String
name|doBackward
parameter_list|(
name|T
name|enumValue
parameter_list|)
block|{
return|return
name|enumValue
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|StringConverter
condition|)
block|{
name|StringConverter
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|StringConverter
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|enumClass
operator|.
name|equals
argument_list|(
name|that
operator|.
name|enumClass
argument_list|)
return|;
block|}
return|return
literal|false
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
name|enumClass
operator|.
name|hashCode
argument_list|()
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
literal|"Enums.stringConverter("
operator|+
name|enumClass
operator|.
name|getName
argument_list|()
operator|+
literal|".class)"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
block|}
block|}
end_class

end_unit

