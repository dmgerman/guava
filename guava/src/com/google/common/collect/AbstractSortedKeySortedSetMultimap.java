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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * Basic implementation of a {@link SortedSetMultimap} with a sorted key set.  *   * This superclass allows {@code TreeMultimap} to override methods to return  * navigable set and map types in non-GWT only, while GWT code will inherit the  * SortedMap/SortedSet overrides.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractSortedKeySortedSetMultimap
specifier|abstract
class|class
name|AbstractSortedKeySortedSetMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|AbstractSortedKeySortedSetMultimap (SortedMap<K, Collection<V>> map)
name|AbstractSortedKeySortedSetMultimap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|super
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|asMap ()
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|asMap
parameter_list|()
block|{
return|return
operator|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
operator|)
name|super
operator|.
name|asMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|backingMap ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|backingMap
parameter_list|()
block|{
return|return
operator|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
operator|)
name|super
operator|.
name|backingMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|SortedSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
operator|(
name|SortedSet
argument_list|<
name|K
argument_list|>
operator|)
name|super
operator|.
name|keySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

