begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Predicate
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
name|collect
operator|.
name|Maps
operator|.
name|EntryTransformer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NavigableMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NavigableSet
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
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Platform
class|class
name|Platform
block|{
comment|/**    * Clone the given array using {@link Object#clone()}.  It is factored out so    * that it can be emulated in GWT.    */
DECL|method|clone (T[] array)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|clone
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|array
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**    * Returns a new array of the given length with the same type as a reference    * array.    *    * @param reference any array of the desired type    * @param length the length of the new array    */
DECL|method|newArray (T[] reference, int length)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|T
index|[]
name|reference
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|reference
operator|.
name|getClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
comment|// the cast is safe because
comment|// result.getClass() == reference.getClass().getComponentType()
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
index|[]
name|result
init|=
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|length
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Configures the given map maker to use weak keys, if possible; does nothing    * otherwise (i.e., in GWT). This is sometimes acceptable, when only    * server-side code could generate enough volume that reclamation becomes    * important.    */
DECL|method|tryWeakKeys (MapMaker mapMaker)
specifier|static
name|MapMaker
name|tryWeakKeys
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|)
block|{
return|return
name|mapMaker
operator|.
name|weakKeys
argument_list|()
return|;
block|}
DECL|method|mapsTransformEntriesSortedMap ( SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V1
parameter_list|,
name|V2
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V2
argument_list|>
name|mapsTransformEntriesSortedMap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V1
argument_list|>
name|fromMap
parameter_list|,
name|EntryTransformer
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V1
argument_list|,
name|V2
argument_list|>
name|transformer
parameter_list|)
block|{
return|return
operator|(
name|fromMap
operator|instanceof
name|NavigableMap
operator|)
condition|?
name|Maps
operator|.
name|transformEntries
argument_list|(
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V1
argument_list|>
operator|)
name|fromMap
argument_list|,
name|transformer
argument_list|)
else|:
name|Maps
operator|.
name|transformEntriesIgnoreNavigable
argument_list|(
name|fromMap
argument_list|,
name|transformer
argument_list|)
return|;
block|}
DECL|method|mapsAsMapSortedSet (SortedSet<K> set, Function<? super K, V> function)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mapsAsMapSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|K
argument_list|>
name|set
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|V
argument_list|>
name|function
parameter_list|)
block|{
return|return
operator|(
name|set
operator|instanceof
name|NavigableSet
operator|)
condition|?
name|Maps
operator|.
name|asMap
argument_list|(
operator|(
name|NavigableSet
argument_list|<
name|K
argument_list|>
operator|)
name|set
argument_list|,
name|function
argument_list|)
else|:
name|Maps
operator|.
name|asMapSortedIgnoreNavigable
argument_list|(
name|set
argument_list|,
name|function
argument_list|)
return|;
block|}
DECL|method|setsFilterSortedSet (SortedSet<E> set, Predicate<? super E> predicate)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|SortedSet
argument_list|<
name|E
argument_list|>
name|setsFilterSortedSet
parameter_list|(
name|SortedSet
argument_list|<
name|E
argument_list|>
name|set
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|E
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
operator|(
name|set
operator|instanceof
name|NavigableSet
operator|)
condition|?
name|Sets
operator|.
name|filter
argument_list|(
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|set
argument_list|,
name|predicate
argument_list|)
else|:
name|Sets
operator|.
name|filterSortedIgnoreNavigable
argument_list|(
name|set
argument_list|,
name|predicate
argument_list|)
return|;
block|}
DECL|method|mapsFilterSortedMap (SortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> predicate)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mapsFilterSortedMap
parameter_list|(
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
operator|(
name|map
operator|instanceof
name|NavigableMap
operator|)
condition|?
name|Maps
operator|.
name|filterEntries
argument_list|(
operator|(
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|map
argument_list|,
name|predicate
argument_list|)
else|:
name|Maps
operator|.
name|filterSortedIgnoreNavigable
argument_list|(
name|map
argument_list|,
name|predicate
argument_list|)
return|;
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

