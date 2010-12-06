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
comment|/** An ordering that uses the reverse of a given order. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|ReverseOrdering
specifier|final
class|class
name|ReverseOrdering
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
DECL|field|forwardOrder
specifier|final
name|Ordering
argument_list|<
name|?
super|super
name|T
argument_list|>
name|forwardOrder
decl_stmt|;
DECL|method|ReverseOrdering (Ordering<? super T> forwardOrder)
name|ReverseOrdering
parameter_list|(
name|Ordering
argument_list|<
name|?
super|super
name|T
argument_list|>
name|forwardOrder
parameter_list|)
block|{
name|this
operator|.
name|forwardOrder
operator|=
name|checkNotNull
argument_list|(
name|forwardOrder
argument_list|)
expr_stmt|;
block|}
DECL|method|compare (T a, T b)
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|T
name|a
parameter_list|,
name|T
name|b
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|compare
argument_list|(
name|b
argument_list|,
name|a
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// how to explain?
DECL|method|reverse ()
annotation|@
name|Override
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
return|return
operator|(
name|Ordering
operator|)
name|forwardOrder
return|;
block|}
comment|// Override the six min/max methods to "hoist" delegation outside loops
DECL|method|min (E a, E b)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|max
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
DECL|method|min (E a, E b, E c, E... rest)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|,
name|E
name|c
parameter_list|,
name|E
modifier|...
name|rest
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|max
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|c
argument_list|,
name|rest
argument_list|)
return|;
block|}
DECL|method|min (Iterable<E> iterable)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|min
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|max
argument_list|(
name|iterable
argument_list|)
return|;
block|}
DECL|method|max (E a, E b)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|min
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
DECL|method|max (E a, E b, E c, E... rest)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|E
name|a
parameter_list|,
name|E
name|b
parameter_list|,
name|E
name|c
parameter_list|,
name|E
modifier|...
name|rest
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|min
argument_list|(
name|a
argument_list|,
name|b
argument_list|,
name|c
argument_list|,
name|rest
argument_list|)
return|;
block|}
DECL|method|max (Iterable<E> iterable)
annotation|@
name|Override
specifier|public
parameter_list|<
name|E
extends|extends
name|T
parameter_list|>
name|E
name|max
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|forwardOrder
operator|.
name|min
argument_list|(
name|iterable
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
operator|-
name|forwardOrder
operator|.
name|hashCode
argument_list|()
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
name|ReverseOrdering
condition|)
block|{
name|ReverseOrdering
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|ReverseOrdering
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
return|return
name|this
operator|.
name|forwardOrder
operator|.
name|equals
argument_list|(
name|that
operator|.
name|forwardOrder
argument_list|)
return|;
block|}
return|return
literal|false
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
name|forwardOrder
operator|+
literal|".reverse()"
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

