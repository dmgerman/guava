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
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractSet
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
name|Iterator
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
name|Set
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
comment|/**  * Minimal GWT emulation of {@code com.google.common.collect.Platform}.  *  *<p><strong>This .java file should never be consumed by javac.</strong>  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
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
return|return
name|GwtPlatform
operator|.
name|newArray
argument_list|(
name|reference
argument_list|,
name|length
argument_list|)
return|;
block|}
comment|/*    * Regarding newSetForMap() and SetFromMap:    *    * Written by Doug Lea with assistance from members of JCP JSR-166    * Expert Group and released to the public domain, as explained at    * http://creativecommons.org/licenses/publicdomain    */
DECL|method|newSetFromMap (Map<E, Boolean> map)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|newSetFromMap
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|Boolean
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|SetFromMap
argument_list|<
name|E
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
DECL|class|SetFromMap
specifier|private
specifier|static
class|class
name|SetFromMap
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSet
argument_list|<
name|E
argument_list|>
implements|implements
name|Set
argument_list|<
name|E
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|m
specifier|private
specifier|final
name|Map
argument_list|<
name|E
argument_list|,
name|Boolean
argument_list|>
name|m
decl_stmt|;
comment|// The backing map
DECL|field|s
specifier|private
specifier|transient
name|Set
argument_list|<
name|E
argument_list|>
name|s
decl_stmt|;
comment|// Its keySet
DECL|method|SetFromMap (Map<E, Boolean> map)
name|SetFromMap
parameter_list|(
name|Map
argument_list|<
name|E
argument_list|,
name|Boolean
argument_list|>
name|map
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"Map is non-empty"
argument_list|)
expr_stmt|;
name|m
operator|=
name|map
expr_stmt|;
name|s
operator|=
name|map
operator|.
name|keySet
argument_list|()
expr_stmt|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|m
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|m
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|m
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|contains (Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|m
operator|.
name|containsKey
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|remove (Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|m
operator|.
name|remove
argument_list|(
name|o
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|add (E e)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|e
parameter_list|)
block|{
return|return
name|m
operator|.
name|put
argument_list|(
name|e
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
operator|==
literal|null
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|s
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
return|return
name|s
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] a)
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|a
parameter_list|)
block|{
return|return
name|s
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|s
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|equals (@ullable Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|this
operator|==
name|object
operator|||
name|this
operator|.
name|s
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|s
operator|.
name|containsAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|removeAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|s
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|retainAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
parameter_list|)
block|{
return|return
name|s
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
return|;
block|}
block|}
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
DECL|method|mapsAsMapSortedSet ( SortedSet<K> set, Function<? super K, V> function)
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
DECL|method|setsFilterSortedSet ( SortedSet<E> unfiltered, Predicate<? super E> predicate)
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
name|unfiltered
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
name|Sets
operator|.
name|filterSortedIgnoreNavigable
argument_list|(
name|unfiltered
argument_list|,
name|predicate
argument_list|)
return|;
block|}
DECL|method|mapsFilterSortedMap ( SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate)
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
name|unfiltered
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
name|Maps
operator|.
name|filterSortedIgnoreNavigable
argument_list|(
name|unfiltered
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

