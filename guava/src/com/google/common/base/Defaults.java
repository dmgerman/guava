begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
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
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * This class provides default values for all Java types, as defined by the JLS.  *  * @author Ben Yu  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
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
DECL|field|DOUBLE_DEFAULT
specifier|private
specifier|static
specifier|final
name|Double
name|DOUBLE_DEFAULT
init|=
name|Double
operator|.
name|valueOf
argument_list|(
literal|0d
argument_list|)
decl_stmt|;
DECL|field|FLOAT_DEFAULT
specifier|private
specifier|static
specifier|final
name|Float
name|FLOAT_DEFAULT
init|=
name|Float
operator|.
name|valueOf
argument_list|(
literal|0f
argument_list|)
decl_stmt|;
comment|/**    * Returns the default value of {@code type} as defined by JLS --- {@code 0} for numbers, {@code    * false} for {@code boolean} and {@code '\0'} for {@code char}. For non-primitive types and    * {@code void}, {@code null} is returned.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|defaultValue (Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
annotation|@
name|Nullable
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
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Boolean
operator|.
name|FALSE
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|char
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Character
operator|.
name|valueOf
argument_list|(
literal|'\0'
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|byte
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Byte
operator|.
name|valueOf
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|short
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Short
operator|.
name|valueOf
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|int
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|long
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|Long
operator|.
name|valueOf
argument_list|(
literal|0L
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|float
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|FLOAT_DEFAULT
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|double
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|DOUBLE_DEFAULT
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

