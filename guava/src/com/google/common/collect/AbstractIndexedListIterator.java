begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkPositionIndex
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
name|NoSuchElementException
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
comment|/**  * This class provides a skeletal implementation of the {@link ListIterator} interface across a  * fixed number of elements that may be retrieved by position. It does not support {@link #remove},  * {@link #set}, or {@link #add}.  *  * @author Jared Levy  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|AbstractIndexedListIterator
specifier|abstract
name|class
name|AbstractIndexedListIterator
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|UnmodifiableListIterator
argument_list|<
name|E
argument_list|>
block|{
DECL|field|size
specifier|private
name|final
name|int
name|size
block|;
DECL|field|position
specifier|private
name|int
name|position
block|;
comment|/** Returns the element with the specified index. This method is called by {@link #next()}. */
block|@
name|ParametricNullness
DECL|method|get (int index)
specifier|protected
specifier|abstract
name|E
name|get
argument_list|(
name|int
name|index
argument_list|)
block|;
comment|/**    * Constructs an iterator across a sequence of the given size whose initial position is 0. That    * is, the first call to {@link #next()} will return the first element (or throw {@link    * NoSuchElementException} if {@code size} is zero).    *    * @throws IllegalArgumentException if {@code size} is negative    */
DECL|method|AbstractIndexedListIterator (int size)
specifier|protected
name|AbstractIndexedListIterator
argument_list|(
name|int
name|size
argument_list|)
block|{
name|this
argument_list|(
name|size
argument_list|,
literal|0
argument_list|)
block|;   }
comment|/**    * Constructs an iterator across a sequence of the given size with the given initial position.    * That is, the first call to {@link #nextIndex()} will return {@code position}, and the first    * call to {@link #next()} will return the element at that index, if available. Calls to {@link    * #previous()} can retrieve the preceding {@code position} elements.    *    * @throws IndexOutOfBoundsException if {@code position} is negative or is greater than {@code    *     size}    * @throws IllegalArgumentException if {@code size} is negative    */
DECL|method|AbstractIndexedListIterator (int size, int position)
specifier|protected
name|AbstractIndexedListIterator
argument_list|(
name|int
name|size
argument_list|,
name|int
name|position
argument_list|)
block|{
name|checkPositionIndex
argument_list|(
name|position
argument_list|,
name|size
argument_list|)
block|;
name|this
operator|.
name|size
operator|=
name|size
block|;
name|this
operator|.
name|position
operator|=
name|position
block|;   }
expr|@
name|Override
DECL|method|hasNext ()
specifier|public
name|final
name|boolean
name|hasNext
argument_list|()
block|{
return|return
name|position
operator|<
name|size
return|;
block|}
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|next ()
specifier|public
name|final
name|E
name|next
argument_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
end_expr_stmt

begin_return
return|return
name|get
argument_list|(
name|position
operator|++
argument_list|)
return|;
end_return

begin_function
unit|}    @
name|Override
DECL|method|nextIndex ()
specifier|public
specifier|final
name|int
name|nextIndex
parameter_list|()
block|{
return|return
name|position
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|hasPrevious ()
specifier|public
specifier|final
name|boolean
name|hasPrevious
parameter_list|()
block|{
return|return
name|position
operator|>
literal|0
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|previous ()
specifier|public
specifier|final
name|E
name|previous
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasPrevious
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|get
argument_list|(
operator|--
name|position
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|previousIndex ()
specifier|public
specifier|final
name|int
name|previousIndex
parameter_list|()
block|{
return|return
name|position
operator|-
literal|1
return|;
block|}
end_function

unit|}
end_unit

