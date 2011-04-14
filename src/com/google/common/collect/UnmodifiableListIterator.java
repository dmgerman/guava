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
name|Beta
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

begin_comment
comment|/**  * A list iterator that does not support {@link #remove}, {@link #add}, or  * {@link #set}.  *  * @since Guava release 07  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|UnmodifiableListIterator
specifier|public
specifier|abstract
class|class
name|UnmodifiableListIterator
parameter_list|<
name|E
parameter_list|>
extends|extends
name|UnmodifiableIterator
argument_list|<
name|E
argument_list|>
implements|implements
name|ListIterator
argument_list|<
name|E
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|UnmodifiableListIterator ()
specifier|protected
name|UnmodifiableListIterator
parameter_list|()
block|{}
comment|/**    * Guaranteed to throw an exception and leave the underlying data unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|add (E e)
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|add
parameter_list|(
name|E
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the underlying data unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|set (E e)
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|set
parameter_list|(
name|E
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

