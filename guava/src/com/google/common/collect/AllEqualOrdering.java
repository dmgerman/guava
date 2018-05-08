begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * An ordering that treats all references as equals, even nulls.  *  * @author Emily Soldal  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|AllEqualOrdering
specifier|final
class|class
name|AllEqualOrdering
extends|extends
name|Ordering
argument_list|<
name|Object
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|AllEqualOrdering
name|INSTANCE
init|=
operator|new
name|AllEqualOrdering
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|compare (@ullable Object left, @Nullable Object right)
specifier|public
name|int
name|compare
parameter_list|(
annotation|@
name|Nullable
name|Object
name|left
parameter_list|,
annotation|@
name|Nullable
name|Object
name|right
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|sortedCopy (Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|sortedCopy
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|iterable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|immutableSortedCopy (Iterable<E> iterable)
specifier|public
parameter_list|<
name|E
parameter_list|>
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|immutableSortedCopy
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|iterable
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|reverse ()
specifier|public
parameter_list|<
name|S
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
argument_list|<
name|S
argument_list|>
operator|)
name|this
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
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
literal|"Ordering.allEqual()"
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

