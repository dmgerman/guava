begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Comparator
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

begin_comment
comment|/**  * An {@code Iterable} whose elements are sorted relative to a {@code Comparator}, typically  * provided at creation time.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|SortedIterable
interface|interface
name|SortedIterable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Iterable
argument_list|<
name|T
argument_list|>
block|{
comment|/**    * Returns the {@code Comparator} by which the elements of this iterable are ordered, or {@code    * Ordering.natural()} if the elements are ordered by their natural ordering.    */
DECL|method|comparator ()
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|()
function_decl|;
comment|/**    * Returns an iterator over elements of type {@code T}. The elements are returned in nondecreasing    * order according to the associated {@link #comparator}.    */
annotation|@
name|Override
DECL|method|iterator ()
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

