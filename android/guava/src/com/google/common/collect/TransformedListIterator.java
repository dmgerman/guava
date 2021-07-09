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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
comment|/**  * An iterator that transforms a backing list iterator; for internal use. This avoids the object  * overhead of constructing a {@link Function} for internal methods.  *  * @author Louis Wasserman  */
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
DECL|class|TransformedListIterator
specifier|abstract
name|class
name|TransformedListIterator
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
name|TransformedIterator
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
expr|implements
name|ListIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|method|TransformedListIterator (ListIterator<? extends F> backingIterator)
name|TransformedListIterator
argument_list|(
name|ListIterator
argument_list|<
name|?
extends|extends
name|F
argument_list|>
name|backingIterator
argument_list|)
block|{
name|super
argument_list|(
name|backingIterator
argument_list|)
block|;   }
DECL|method|backingIterator ()
specifier|private
name|ListIterator
argument_list|<
name|?
extends|extends
name|F
argument_list|>
name|backingIterator
argument_list|()
block|{
return|return
name|Iterators
operator|.
name|cast
argument_list|(
name|backingIterator
argument_list|)
return|;
block|}
expr|@
name|Override
DECL|method|hasPrevious ()
specifier|public
name|final
name|boolean
name|hasPrevious
argument_list|()
block|{
return|return
name|backingIterator
argument_list|()
operator|.
name|hasPrevious
argument_list|()
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
annotation|@
name|ParametricNullness
DECL|method|previous ()
specifier|public
specifier|final
name|T
name|previous
parameter_list|()
block|{
return|return
name|transform
argument_list|(
name|backingIterator
argument_list|()
operator|.
name|previous
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|nextIndex ()
specifier|public
specifier|final
name|int
name|nextIndex
parameter_list|()
block|{
return|return
name|backingIterator
argument_list|()
operator|.
name|nextIndex
argument_list|()
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
name|backingIterator
argument_list|()
operator|.
name|previousIndex
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|set (@arametricNullness T element)
specifier|public
name|void
name|set
parameter_list|(
annotation|@
name|ParametricNullness
name|T
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|add (@arametricNullness T element)
specifier|public
name|void
name|add
parameter_list|(
annotation|@
name|ParametricNullness
name|T
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
end_function

unit|}
end_unit

