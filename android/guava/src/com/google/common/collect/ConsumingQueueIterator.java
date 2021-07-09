begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|Queue
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
comment|/**  * An Iterator implementation which draws elements from a queue, removing them from the queue as it  * iterates.  */
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
DECL|class|ConsumingQueueIterator
name|final
name|class
name|ConsumingQueueIterator
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|AbstractIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|queue
specifier|private
name|final
name|Queue
argument_list|<
name|T
argument_list|>
name|queue
block|;
DECL|method|ConsumingQueueIterator (Queue<T> queue)
name|ConsumingQueueIterator
argument_list|(
name|Queue
argument_list|<
name|T
argument_list|>
name|queue
argument_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|checkNotNull
argument_list|(
name|queue
argument_list|)
block|;   }
expr|@
name|Override
expr|@
name|CheckForNull
DECL|method|computeNext ()
specifier|public
name|T
name|computeNext
argument_list|()
block|{
comment|// TODO(b/192579700): Use a ternary once it no longer confuses our nullness checker.
if|if
condition|(
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|endOfData
argument_list|()
return|;
block|}
return|return
name|queue
operator|.
name|remove
argument_list|()
return|;
block|}
end_expr_stmt

unit|}
end_unit

