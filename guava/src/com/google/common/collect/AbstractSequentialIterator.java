begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|NoSuchElementException
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
comment|/**  * This class provides a skeletal implementation of the {@code Iterator}  * interface for sequences whose next element can always be derived from the  * previous element. Null elements are not supported, nor is the  * {@link #remove()} method.  *  *<p>Example:<pre>   {@code  *  *   Iterator<Integer> powersOfTwo =  *       new AbstractSequentialIterator<Integer>(1) {  *         protected Integer computeNext(Integer previous) {  *           return (previous == 1<< 30) ? null : previous * 2;  *         }  *       };}</pre>  *  * @author Chris Povirk  * @since 12.0 (in Guava as {@code AbstractLinkedIterator} since 8.0)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractSequentialIterator
specifier|public
specifier|abstract
class|class
name|AbstractSequentialIterator
parameter_list|<
name|T
parameter_list|>
extends|extends
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|nextOrNull
specifier|private
name|T
name|nextOrNull
decl_stmt|;
comment|/**    * Creates a new iterator with the given first element, or, if {@code    * firstOrNull} is null, creates a new empty iterator.    */
DECL|method|AbstractSequentialIterator (@ullable T firstOrNull)
specifier|protected
name|AbstractSequentialIterator
parameter_list|(
annotation|@
name|Nullable
name|T
name|firstOrNull
parameter_list|)
block|{
name|this
operator|.
name|nextOrNull
operator|=
name|firstOrNull
expr_stmt|;
block|}
comment|/**    * Returns the element that follows {@code previous}, or returns {@code null}    * if no elements remain. This method is invoked during each call to    * {@link #next()} in order to compute the result of a<i>future</i> call to    * {@code next()}.    */
DECL|method|computeNext (T previous)
specifier|protected
specifier|abstract
name|T
name|computeNext
parameter_list|(
name|T
name|previous
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextOrNull
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
specifier|final
name|T
name|next
parameter_list|()
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
try|try
block|{
return|return
name|nextOrNull
return|;
block|}
finally|finally
block|{
name|nextOrNull
operator|=
name|computeNext
argument_list|(
name|nextOrNull
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

