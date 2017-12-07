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
name|GwtIncompatible
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
name|IteratorBasedAbstractMap
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
name|NoSuchElementException
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Skeletal implementation of {@link NavigableMap}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|AbstractNavigableMap
specifier|abstract
class|class
name|AbstractNavigableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|IteratorBasedAbstractMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|get (@ullableDecl Object key)
specifier|public
specifier|abstract
name|V
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
function_decl|;
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|firstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|entryIterator
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|lastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lastEntry
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|getNext
argument_list|(
name|descendingEntryIterator
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|pollFirstEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollFirstEntry
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|pollNext
argument_list|(
name|entryIterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|pollLastEntry ()
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pollLastEntry
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|pollNext
argument_list|(
name|descendingEntryIterator
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|firstKey ()
specifier|public
name|K
name|firstKey
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|firstEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|lastKey ()
specifier|public
name|K
name|lastKey
parameter_list|()
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|lastEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|lowerEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|lowerEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
operator|.
name|lastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|floorEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|floorEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
operator|.
name|lastEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|ceilingEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ceilingEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|key
argument_list|,
literal|true
argument_list|)
operator|.
name|firstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|NullableDecl
DECL|method|higherEntry (K key)
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|higherEntry
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|key
argument_list|,
literal|false
argument_list|)
operator|.
name|firstEntry
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|lowerKey (K key)
specifier|public
name|K
name|lowerKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|keyOrNull
argument_list|(
name|lowerEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|floorKey (K key)
specifier|public
name|K
name|floorKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|keyOrNull
argument_list|(
name|floorEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|ceilingKey (K key)
specifier|public
name|K
name|ceilingKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|keyOrNull
argument_list|(
name|ceilingEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|higherKey (K key)
specifier|public
name|K
name|higherKey
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|keyOrNull
argument_list|(
name|higherEntry
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
DECL|method|descendingEntryIterator ()
specifier|abstract
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|descendingEntryIterator
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|subMap (K fromKey, K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
parameter_list|(
name|K
name|fromKey
parameter_list|,
name|K
name|toKey
parameter_list|)
block|{
return|return
name|subMap
argument_list|(
name|fromKey
argument_list|,
literal|true
argument_list|,
name|toKey
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|headMap (K toKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
parameter_list|(
name|K
name|toKey
parameter_list|)
block|{
return|return
name|headMap
argument_list|(
name|toKey
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailMap (K fromKey)
specifier|public
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
parameter_list|(
name|K
name|fromKey
parameter_list|)
block|{
return|return
name|tailMap
argument_list|(
name|fromKey
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|navigableKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|navigableKeySet
parameter_list|()
block|{
return|return
operator|new
name|Maps
operator|.
name|NavigableKeySet
argument_list|<>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|keySet ()
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
return|return
name|navigableKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingKeySet ()
specifier|public
name|NavigableSet
argument_list|<
name|K
argument_list|>
name|descendingKeySet
parameter_list|()
block|{
return|return
name|descendingMap
argument_list|()
operator|.
name|navigableKeySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingMap ()
specifier|public
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|descendingMap
parameter_list|()
block|{
return|return
operator|new
name|DescendingMap
argument_list|()
return|;
block|}
DECL|class|DescendingMap
specifier|private
specifier|final
class|class
name|DescendingMap
extends|extends
name|Maps
operator|.
name|DescendingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|forward ()
name|NavigableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
parameter_list|()
block|{
return|return
name|AbstractNavigableMap
operator|.
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|entryIterator ()
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryIterator
parameter_list|()
block|{
return|return
name|descendingEntryIterator
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

