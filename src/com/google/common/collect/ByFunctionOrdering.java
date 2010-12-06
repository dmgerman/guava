begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An ordering that orders elements by applying an order to the result of a  * function on those elements.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|ByFunctionOrdering
specifier|final
class|class
name|ByFunctionOrdering
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|Ordering
argument_list|<
name|F
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|function
specifier|final
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
decl_stmt|;
DECL|field|ordering
specifier|final
name|Ordering
argument_list|<
name|T
argument_list|>
name|ordering
decl_stmt|;
DECL|method|ByFunctionOrdering ( Function<F, ? extends T> function, Ordering<T> ordering)
name|ByFunctionOrdering
parameter_list|(
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|,
name|Ordering
argument_list|<
name|T
argument_list|>
name|ordering
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|this
operator|.
name|ordering
operator|=
name|checkNotNull
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
DECL|method|compare (F left, F right)
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|F
name|left
parameter_list|,
name|F
name|right
parameter_list|)
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
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
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
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
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
return|return
literal|false
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
DECL|method|toString ()
annotation|@
name|Override
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
end_class

end_unit

