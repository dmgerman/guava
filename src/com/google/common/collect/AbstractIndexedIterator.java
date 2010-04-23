begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * This class provides a skeletal implementation of an iterator across a fixed  * number of elements that may be retrieved by position.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractIndexedIterator
specifier|abstract
class|class
name|AbstractIndexedIterator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO: Make public? If public, constructor should verify that size>= 0.
DECL|field|size
specifier|private
specifier|final
name|int
name|size
decl_stmt|;
DECL|field|position
specifier|private
name|int
name|position
decl_stmt|;
comment|/**    * Returns the element with the specified index. This method is called by    * {@link #next()}.    */
DECL|method|get (int index)
specifier|protected
specifier|abstract
name|E
name|get
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|// TODO: Add constructor taking an offset.
DECL|method|AbstractIndexedIterator (int size)
specifier|protected
name|AbstractIndexedIterator
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|position
operator|<
name|size
return|;
block|}
DECL|method|next ()
specifier|public
specifier|final
name|E
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
return|return
name|get
argument_list|(
name|position
operator|++
argument_list|)
return|;
block|}
block|}
end_class

end_unit

