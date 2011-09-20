begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkArgument
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
name|EnumMap
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
comment|/**  * Multiset implementation backed by an {@link EnumMap}.  *  * @author Jared Levy  * @since 2.0 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EnumMultiset
specifier|public
specifier|final
class|class
name|EnumMultiset
parameter_list|<
name|E
extends|extends
name|Enum
parameter_list|<
name|E
parameter_list|>
parameter_list|>
extends|extends
name|AbstractMapBasedMultiset
argument_list|<
name|E
argument_list|>
block|{
comment|/** Creates an empty {@code EnumMultiset}. */
DECL|method|create (Class<E> type)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|EnumMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**    * Creates a new {@code EnumMultiset} containing the specified elements.    *    *<p>This implementation is highly efficient when {@code elements} is itself a {@link    * Multiset}.    *    * @param elements the elements that the multiset should contain    * @throws IllegalArgumentException if {@code elements} is empty    */
DECL|method|create (Iterable<E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Enum
argument_list|<
name|E
argument_list|>
parameter_list|>
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|elements
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|iterator
operator|.
name|hasNext
argument_list|()
argument_list|,
literal|"EnumMultiset constructor passed empty Iterable"
argument_list|)
expr_stmt|;
name|EnumMultiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
operator|new
name|EnumMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|multiset
return|;
block|}
DECL|field|type
specifier|private
specifier|transient
name|Class
argument_list|<
name|E
argument_list|>
name|type
decl_stmt|;
comment|/** Creates an empty {@code EnumMultiset}. */
DECL|method|EnumMultiset (Class<E> type)
specifier|private
name|EnumMultiset
parameter_list|(
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|WellBehavedMap
operator|.
name|wrap
argument_list|(
operator|new
name|EnumMap
argument_list|<
name|E
argument_list|,
name|Count
argument_list|>
argument_list|(
name|type
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

