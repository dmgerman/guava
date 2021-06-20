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
name|Function
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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
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
comment|/**  * An ordering that orders elements by applying an order to the result of a function on those  * elements.  */
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
DECL|class|ByFunctionOrdering
name|final
name|class
name|ByFunctionOrdering
operator|<
name|F
expr|extends @
name|Nullable
name|Object
operator|,
name|T
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|Ordering
argument_list|<
name|F
argument_list|>
expr|implements
name|Serializable
block|{
DECL|field|function
name|final
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
block|;
DECL|field|ordering
name|final
name|Ordering
argument_list|<
name|T
argument_list|>
name|ordering
block|;
DECL|method|ByFunctionOrdering (Function<F, ? extends T> function, Ordering<T> ordering)
name|ByFunctionOrdering
argument_list|(
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
argument_list|,
name|Ordering
argument_list|<
name|T
argument_list|>
name|ordering
argument_list|)
block|{
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
block|;
name|this
operator|.
name|ordering
operator|=
name|checkNotNull
argument_list|(
name|ordering
argument_list|)
block|;   }
expr|@
name|Override
DECL|method|compare (@arametricNullness F left, @ParametricNullness F right)
specifier|public
name|int
name|compare
argument_list|(
annotation|@
name|ParametricNullness
name|F
name|left
argument_list|,
annotation|@
name|ParametricNullness
name|F
name|right
argument_list|)
block|{
return|return
name|ordering
operator|.
name|compare
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|left
argument_list|)
argument_list|,
name|function
operator|.
name|apply
argument_list|(
name|right
argument_list|)
argument_list|)
return|;
block|}
expr|@
name|Override
DECL|method|equals (@heckForNull Object object)
specifier|public
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|object
argument_list|)
block|{
if|if
condition|(
name|object
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
end_expr_stmt

begin_if
if|if
condition|(
name|object
operator|instanceof
name|ByFunctionOrdering
condition|)
block|{
name|ByFunctionOrdering
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|ByFunctionOrdering
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|function
operator|.
name|equals
argument_list|(
name|that
operator|.
name|function
argument_list|)
operator|&&
name|this
operator|.
name|ordering
operator|.
name|equals
argument_list|(
name|that
operator|.
name|ordering
argument_list|)
return|;
block|}
end_if

begin_return
return|return
literal|false
return|;
end_return

begin_function
unit|}    @
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|function
argument_list|,
name|ordering
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ordering
operator|+
literal|".onResultOf("
operator|+
name|function
operator|+
literal|")"
return|;
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

