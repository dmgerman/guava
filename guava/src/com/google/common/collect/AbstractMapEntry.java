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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
comment|/**  * Implementation of the {@code equals}, {@code hashCode}, and {@code toString} methods of {@code  * Entry}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMapEntry
specifier|abstract
class|class
name|AbstractMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|getKey ()
specifier|public
specifier|abstract
name|K
name|getKey
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
specifier|abstract
name|V
name|getValue
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|setValue (V value)
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getKey
argument_list|()
argument_list|,
name|that
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getValue
argument_list|()
argument_list|,
name|that
operator|.
name|getValue
argument_list|()
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
name|K
name|k
init|=
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|getValue
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|k
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|k
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
operator|(
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|v
operator|.
name|hashCode
argument_list|()
operator|)
return|;
block|}
comment|/** Returns a string representation of the form {@code {key}={value}}. */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

