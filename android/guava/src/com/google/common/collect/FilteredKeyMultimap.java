begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkNotNull
import|;
end_import

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
name|checkPositionIndex
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
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
name|Collections
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
name|List
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
name|Map
operator|.
name|Entry
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link Multimaps#filterKeys(Multimap, Predicate)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|FilteredKeyMultimap
class|class
name|FilteredKeyMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|FilteredMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|unfiltered
specifier|final
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unfiltered
decl_stmt|;
DECL|field|keyPredicate
specifier|final
name|Predicate
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyPredicate
decl_stmt|;
DECL|method|FilteredKeyMultimap (Multimap<K, V> unfiltered, Predicate<? super K> keyPredicate)
name|FilteredKeyMultimap
parameter_list|(
name|Multimap
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
name|K
argument_list|>
name|keyPredicate
parameter_list|)
block|{
name|this
operator|.
name|unfiltered
operator|=
name|checkNotNull
argument_list|(
name|unfiltered
argument_list|)
expr_stmt|;
name|this
operator|.
name|keyPredicate
operator|=
name|checkNotNull
argument_list|(
name|keyPredicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unfiltered ()
specifier|public
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unfiltered
parameter_list|()
block|{
return|return
name|unfiltered
return|;
block|}
annotation|@
name|Override
DECL|method|entryPredicate ()
specifier|public
name|Predicate
argument_list|<
name|?
super|super
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryPredicate
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|keyPredicateOnEntries
argument_list|(
name|keyPredicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
range|:
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|size
operator|+=
name|collection
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
annotation|@
name|Override
DECL|method|containsKey (@ullable Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
if|if
condition|(
name|unfiltered
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// k is equal to a K, if not one itself
name|K
name|k
init|=
operator|(
name|K
operator|)
name|key
decl_stmt|;
return|return
name|keyPredicate
operator|.
name|apply
argument_list|(
name|k
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Object key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|containsKey
argument_list|(
name|key
argument_list|)
condition|?
name|unfiltered
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
else|:
name|unmodifiableEmptyCollection
argument_list|()
return|;
block|}
DECL|method|unmodifiableEmptyCollection ()
name|Collection
argument_list|<
name|V
argument_list|>
name|unmodifiableEmptyCollection
parameter_list|()
block|{
if|if
condition|(
name|unfiltered
operator|instanceof
name|SetMultimap
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|keySet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
name|Set
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|filter
argument_list|(
name|unfiltered
operator|.
name|keySet
argument_list|()
argument_list|,
name|keyPredicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (K key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
if|if
condition|(
name|keyPredicate
operator|.
name|apply
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|unfiltered
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|unfiltered
operator|instanceof
name|SetMultimap
condition|)
block|{
return|return
operator|new
name|AddRejectingSet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|AddRejectingList
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
DECL|class|AddRejectingSet
specifier|static
class|class
name|AddRejectingSet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingSet
argument_list|<
name|V
argument_list|>
block|{
DECL|field|key
specifier|final
name|K
name|key
decl_stmt|;
DECL|method|AddRejectingSet (K key)
name|AddRejectingSet
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (V element)
specifier|public
name|boolean
name|add
parameter_list|(
name|V
name|element
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key does not satisfy predicate: "
operator|+
name|key
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends V> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|collection
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|collection
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key does not satisfy predicate: "
operator|+
name|key
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Set
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
block|}
DECL|class|AddRejectingList
specifier|static
class|class
name|AddRejectingList
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ForwardingList
argument_list|<
name|V
argument_list|>
block|{
DECL|field|key
specifier|final
name|K
name|key
decl_stmt|;
DECL|method|AddRejectingList (K key)
name|AddRejectingList
parameter_list|(
name|K
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (V v)
specifier|public
name|boolean
name|add
parameter_list|(
name|V
name|v
parameter_list|)
block|{
name|add
argument_list|(
literal|0
argument_list|,
name|v
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends V> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|collection
parameter_list|)
block|{
name|addAll
argument_list|(
literal|0
argument_list|,
name|collection
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|add (int index, V element)
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|V
name|element
parameter_list|)
block|{
name|checkPositionIndex
argument_list|(
name|index
argument_list|,
literal|0
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key does not satisfy predicate: "
operator|+
name|key
argument_list|)
throw|;
block|}
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|addAll (int index, Collection<? extends V> elements)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|elements
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|elements
argument_list|)
expr_stmt|;
name|checkPositionIndex
argument_list|(
name|index
argument_list|,
literal|0
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Key does not satisfy predicate: "
operator|+
name|key
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|List
argument_list|<
name|V
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
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
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"should never be called"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createEntries ()
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntries
parameter_list|()
block|{
return|return
operator|new
name|Entries
argument_list|()
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|Entries
class|class
name|Entries
extends|extends
name|ForwardingCollection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|Collections2
operator|.
name|filter
argument_list|(
name|unfiltered
operator|.
name|entries
argument_list|()
argument_list|,
name|entryPredicate
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|remove (@ullable Object o)
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|unfiltered
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
comment|// if this holds, then we know entry.getKey() is a K
operator|&&
name|keyPredicate
operator|.
name|apply
argument_list|(
operator|(
name|K
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|unfiltered
operator|.
name|remove
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createValues ()
name|Collection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|FilteredMultimapValues
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createAsMap ()
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createAsMap
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|filterKeys
argument_list|(
name|unfiltered
operator|.
name|asMap
argument_list|()
argument_list|,
name|keyPredicate
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeys ()
name|Multiset
argument_list|<
name|K
argument_list|>
name|createKeys
parameter_list|()
block|{
return|return
name|Multisets
operator|.
name|filter
argument_list|(
name|unfiltered
operator|.
name|keys
argument_list|()
argument_list|,
name|keyPredicate
argument_list|)
return|;
block|}
block|}
end_class

end_unit
