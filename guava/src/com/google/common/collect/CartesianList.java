begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkElementIndex
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
name|math
operator|.
name|IntMath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractList
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
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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
comment|/**  * Implementation of {@link Lists#cartesianProduct(List)}.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CartesianList
specifier|final
class|class
name|CartesianList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractList
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
implements|implements
name|RandomAccess
block|{
DECL|field|axes
specifier|private
specifier|transient
specifier|final
name|ImmutableList
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|axes
decl_stmt|;
DECL|field|axesSizeProduct
specifier|private
specifier|transient
specifier|final
name|int
index|[]
name|axesSizeProduct
decl_stmt|;
DECL|method|create (List<? extends List<? extends E>> lists)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|create
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
argument_list|>
name|lists
parameter_list|)
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|axesBuilder
init|=
operator|new
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|(
name|lists
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|list
range|:
name|lists
control|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|copy
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|list
argument_list|)
decl_stmt|;
if|if
condition|(
name|copy
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|axesBuilder
operator|.
name|add
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|CartesianList
argument_list|<
name|E
argument_list|>
argument_list|(
name|axesBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|CartesianList (ImmutableList<List<E>> axes)
name|CartesianList
parameter_list|(
name|ImmutableList
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|axes
parameter_list|)
block|{
name|this
operator|.
name|axes
operator|=
name|axes
expr_stmt|;
name|int
index|[]
name|axesSizeProduct
init|=
operator|new
name|int
index|[
name|axes
operator|.
name|size
argument_list|()
operator|+
literal|1
index|]
decl_stmt|;
name|axesSizeProduct
index|[
name|axes
operator|.
name|size
argument_list|()
index|]
operator|=
literal|1
expr_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
name|axes
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|axesSizeProduct
index|[
name|i
index|]
operator|=
name|IntMath
operator|.
name|checkedMultiply
argument_list|(
name|axesSizeProduct
index|[
name|i
operator|+
literal|1
index|]
argument_list|,
name|axes
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ArithmeticException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cartesian product too large; must have size at most Integer.MAX_VALUE"
argument_list|)
throw|;
block|}
name|this
operator|.
name|axesSizeProduct
operator|=
name|axesSizeProduct
expr_stmt|;
block|}
DECL|method|getAxisIndexForProductIndex (int index, int axis)
specifier|private
name|int
name|getAxisIndexForProductIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|axis
parameter_list|)
block|{
return|return
operator|(
name|index
operator|/
name|axesSizeProduct
index|[
name|axis
operator|+
literal|1
index|]
operator|)
operator|%
name|axes
operator|.
name|get
argument_list|(
name|axis
argument_list|)
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (final int index)
specifier|public
name|ImmutableList
argument_list|<
name|E
argument_list|>
name|get
parameter_list|(
specifier|final
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ImmutableList
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|axes
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|E
name|get
parameter_list|(
name|int
name|axis
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|axis
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|axisIndex
init|=
name|getAxisIndexForProductIndex
argument_list|(
name|index
argument_list|,
name|axis
argument_list|)
decl_stmt|;
return|return
name|axes
operator|.
name|get
argument_list|(
name|axis
argument_list|)
operator|.
name|get
argument_list|(
name|axisIndex
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|axesSizeProduct
index|[
literal|0
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object o)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|List
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|!=
name|axes
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ListIterator
argument_list|<
name|?
argument_list|>
name|itr
init|=
name|list
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|index
init|=
name|itr
operator|.
name|nextIndex
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|axes
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|contains
argument_list|(
name|itr
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

