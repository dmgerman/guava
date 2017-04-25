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
comment|/** An ordering that treats {@code null} as greater than all other values. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|NullsLastOrdering
specifier|final
class|class
name|NullsLastOrdering
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Ordering
argument_list|<
name|T
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|ordering
specifier|final
name|Ordering
argument_list|<
name|?
super|super
name|T
argument_list|>
name|ordering
decl_stmt|;
DECL|method|NullsLastOrdering (Ordering<? super T> ordering)
name|NullsLastOrdering
parameter_list|(
name|Ordering
argument_list|<
name|?
super|super
name|T
argument_list|>
name|ordering
parameter_list|)
block|{
name|this
operator|.
name|ordering
operator|=
name|ordering
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compare (@ullable T left, @Nullable T right)
specifier|public
name|int
name|compare
parameter_list|(
annotation|@
name|Nullable
name|T
name|left
parameter_list|,
annotation|@
name|Nullable
name|T
name|right
parameter_list|)
block|{
if|if
condition|(
name|left
operator|==
name|right
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|left
operator|==
literal|null
condition|)
block|{
return|return
name|LEFT_IS_GREATER
return|;
block|}
if|if
condition|(
name|right
operator|==
literal|null
condition|)
block|{
return|return
name|RIGHT_IS_GREATER
return|;
block|}
return|return
name|ordering
operator|.
name|compare
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|reverse ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|reverse
parameter_list|()
block|{
comment|// ordering.reverse() might be optimized, so let it do its thing
return|return
name|ordering
operator|.
name|reverse
argument_list|()
operator|.
name|nullsFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|nullsFirst ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|nullsFirst
parameter_list|()
block|{
return|return
name|ordering
operator|.
name|nullsFirst
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// still need the right way to explain this
annotation|@
name|Override
DECL|method|nullsLast ()
specifier|public
parameter_list|<
name|S
extends|extends
name|T
parameter_list|>
name|Ordering
argument_list|<
name|S
argument_list|>
name|nullsLast
parameter_list|()
block|{
return|return
operator|(
name|Ordering
argument_list|<
name|S
argument_list|>
operator|)
name|this
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
name|NullsLastOrdering
condition|)
block|{
name|NullsLastOrdering
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|NullsLastOrdering
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
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
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|ordering
operator|.
name|hashCode
argument_list|()
operator|^
operator|-
literal|921210296
return|;
comment|// meaningless
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
name|ordering
operator|+
literal|".nullsLast()"
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
