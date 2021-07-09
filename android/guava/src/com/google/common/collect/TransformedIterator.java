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
name|util
operator|.
name|Iterator
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
comment|/**  * An iterator that transforms a backing iterator; for internal use. This avoids the object overhead  * of constructing a {@link com.google.common.base.Function Function} for internal methods.  *  * @author Louis Wasserman  */
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
DECL|class|TransformedIterator
specifier|abstract
name|class
name|TransformedIterator
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
expr|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|backingIterator
name|final
name|Iterator
argument_list|<
name|?
extends|extends
name|F
argument_list|>
name|backingIterator
block|;
DECL|method|TransformedIterator (Iterator<? extends F> backingIterator)
name|TransformedIterator
argument_list|(
name|Iterator
argument_list|<
name|?
extends|extends
name|F
argument_list|>
name|backingIterator
argument_list|)
block|{
name|this
operator|.
name|backingIterator
operator|=
name|checkNotNull
argument_list|(
name|backingIterator
argument_list|)
block|;   }
expr|@
name|ParametricNullness
DECL|method|transform (@arametricNullness F from)
specifier|abstract
name|T
name|transform
argument_list|(
annotation|@
name|ParametricNullness
name|F
name|from
argument_list|)
block|;    @
name|Override
DECL|method|hasNext ()
specifier|public
name|final
name|boolean
name|hasNext
argument_list|()
block|{
return|return
name|backingIterator
operator|.
name|hasNext
argument_list|()
return|;
block|}
expr|@
name|Override
expr|@
name|ParametricNullness
DECL|method|next ()
specifier|public
name|final
name|T
name|next
argument_list|()
block|{
return|return
name|transform
argument_list|(
name|backingIterator
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|remove ()
specifier|public
specifier|final
name|void
name|remove
parameter_list|()
block|{
name|backingIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
end_function

unit|}
end_unit

