begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * An iterator that supports a one-element lookahead while iterating.  *  * @author Mick Killianey  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|PeekingIterator
specifier|public
interface|interface
name|PeekingIterator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Iterator
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the next element in the iteration, without advancing the iteration.    *    *<p>Calls to {@code peek()} should not change the state of the iteration,    * except that it<i>may</i> prevent removal of the most recent element via    * {@link #remove()}.    *    * @throws NoSuchElementException if the iteration has no more elements    *     according to {@link #hasNext()}    */
DECL|method|peek ()
name|E
name|peek
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>The objects returned by consecutive calls to {@link #peek()} then {@link    * #next()} are guaranteed to be equal to each other.    */
DECL|method|next ()
name|E
name|next
parameter_list|()
function_decl|;
comment|/**    * {@inheritDoc}    *    *<p>Implementations may or may not support removal when a call to {@link    * #peek()} has occurred since the most recent call to {@link #next()}.    *    * @throws IllegalStateException if there has been a call to {@link #peek()}    *     since the most recent call to {@link #next()} and this implementation    *     does not support this sequence of calls (optional)    */
DECL|method|remove ()
name|void
name|remove
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

