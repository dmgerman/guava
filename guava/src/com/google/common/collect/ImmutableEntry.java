begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|Serializable
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
comment|/** @see com.google.common.collect.Maps#immutableEntry(Object, Object) */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ImmutableEntry
name|class
name|ImmutableEntry
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AbstractMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
expr|implements
name|Serializable
block|{   @
DECL|field|key
name|ParametricNullness
name|final
name|K
name|key
block|;   @
DECL|field|value
name|ParametricNullness
name|final
name|V
name|value
block|;
DECL|method|ImmutableEntry (@arametricNullness K key, @ParametricNullness V value)
name|ImmutableEntry
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|,
annotation|@
name|ParametricNullness
name|V
name|value
argument_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
block|;
name|this
operator|.
name|value
operator|=
name|value
block|;   }
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|getKey ()
specifier|public
name|final
name|K
name|getKey
argument_list|()
block|{
return|return
name|key
return|;
block|}
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|getValue ()
specifier|public
name|final
name|V
name|getValue
argument_list|()
block|{
return|return
name|value
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|setValue (@arametricNullness V value)
specifier|public
specifier|final
name|V
name|setValue
parameter_list|(
annotation|@
name|ParametricNullness
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
end_function

begin_decl_stmt
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
end_decl_stmt

unit|}
end_unit

