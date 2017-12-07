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
name|Predicates
operator|.
name|in
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
name|Predicates
operator|.
name|not
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
name|collect
operator|.
name|CollectPreconditions
operator|.
name|checkNonnegative
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
name|MoreObjects
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
name|ViewCachingAbstractMap
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
comment|/**  * Implementation of {@link Multimaps#filterEntries(Multimap, Predicate)}.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|FilteredEntryMultimap
class|class
name|FilteredEntryMultimap
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
DECL|field|predicate
specifier|final
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
name|predicate
decl_stmt|;
DECL|method|FilteredEntryMultimap (Multimap<K, V> unfiltered, Predicate<? super Entry<K, V>> predicate)
name|FilteredEntryMultimap
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
name|predicate
operator|=
name|checkNotNull
argument_list|(
name|predicate
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
name|predicate
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
return|return
name|entries
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|satisfies (K key, V value)
specifier|private
name|boolean
name|satisfies
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|class|ValuePredicate
specifier|final
class|class
name|ValuePredicate
implements|implements
name|Predicate
argument_list|<
name|V
argument_list|>
block|{
DECL|field|key
specifier|private
specifier|final
name|K
name|key
decl_stmt|;
DECL|method|ValuePredicate (K key)
name|ValuePredicate
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
DECL|method|apply (@ullableDecl V value)
specifier|public
name|boolean
name|apply
parameter_list|(
annotation|@
name|NullableDecl
name|V
name|value
parameter_list|)
block|{
return|return
name|satisfies
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
DECL|method|filterCollection ( Collection<E> collection, Predicate<? super E> predicate)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Collection
argument_list|<
name|E
argument_list|>
name|filterCollection
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
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
if|if
condition|(
name|collection
operator|instanceof
name|Set
condition|)
block|{
return|return
name|Sets
operator|.
name|filter
argument_list|(
operator|(
name|Set
argument_list|<
name|E
argument_list|>
operator|)
name|collection
argument_list|,
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections2
operator|.
name|filter
argument_list|(
name|collection
argument_list|,
name|predicate
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|containsKey (@ullableDecl Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
return|return
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (@ullableDecl Object key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
argument_list|,
name|unmodifiableEmptyCollection
argument_list|()
argument_list|)
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
comment|// These return false, rather than throwing a UOE, on remove calls.
return|return
operator|(
name|unfiltered
operator|instanceof
name|SetMultimap
operator|)
condition|?
name|Collections
operator|.
expr|<
name|V
operator|>
name|emptySet
argument_list|()
else|:
name|Collections
operator|.
expr|<
name|V
operator|>
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|entries
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (final K key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
specifier|final
name|K
name|key
parameter_list|)
block|{
return|return
name|filterCollection
argument_list|(
name|unfiltered
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
operator|new
name|ValuePredicate
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
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
name|filterCollection
argument_list|(
name|unfiltered
operator|.
name|entries
argument_list|()
argument_list|,
name|predicate
argument_list|)
return|;
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
argument_list|<>
argument_list|(
name|this
argument_list|)
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
operator|new
name|AsMap
argument_list|()
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
name|asMap
argument_list|()
operator|.
name|keySet
argument_list|()
return|;
block|}
DECL|method|removeEntriesIf (Predicate<? super Entry<K, Collection<V>>> predicate)
name|boolean
name|removeEntriesIf
parameter_list|(
name|Predicate
argument_list|<
name|?
super|super
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|predicate
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|entryIterator
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|changed
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|filterCollection
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
operator|new
name|ValuePredicate
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|collection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|predicate
operator|.
name|apply
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|collection
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|collection
operator|.
name|size
argument_list|()
operator|==
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|changed
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|changed
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|AsMap
class|class
name|AsMap
extends|extends
name|ViewCachingAbstractMap
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|containsKey (@ullableDecl Object key)
specifier|public
name|boolean
name|containsKey
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
return|return
name|get
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|FilteredEntryMultimap
operator|.
name|this
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (@ullableDecl Object key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// key is equal to a K, if not a K itself
name|K
name|k
init|=
operator|(
name|K
operator|)
name|key
decl_stmt|;
name|result
operator|=
name|filterCollection
argument_list|(
name|result
argument_list|,
operator|new
name|ValuePredicate
argument_list|(
name|k
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullableDecl Object key)
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|collection
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// it's definitely equal to a K
name|K
name|k
init|=
operator|(
name|K
operator|)
name|key
decl_stmt|;
name|List
argument_list|<
name|V
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|V
argument_list|>
name|itr
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|V
name|v
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|satisfies
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
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
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|result
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
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
annotation|@
name|WeakOuter
class|class
name|KeySetImpl
extends|extends
name|Maps
operator|.
name|KeySet
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
name|KeySetImpl
parameter_list|()
block|{
name|super
argument_list|(
name|AsMap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
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
name|removeEntriesIf
argument_list|(
name|Maps
operator|.
expr|<
name|K
operator|>
name|keyPredicateOnEntries
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|Maps
operator|.
expr|<
name|K
operator|>
name|keyPredicateOnEntries
argument_list|(
name|not
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|o
parameter_list|)
block|{
return|return
name|AsMap
operator|.
name|this
operator|.
name|remove
argument_list|(
name|o
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
return|return
operator|new
name|KeySetImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createEntrySet ()
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
annotation|@
name|WeakOuter
class|class
name|EntrySetImpl
extends|extends
name|Maps
operator|.
name|EntrySet
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|AsMap
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|final
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|backingIterator
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|computeNext
parameter_list|()
block|{
while|while
condition|(
name|backingIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|backingIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|filterCollection
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
operator|new
name|ValuePredicate
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|collection
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|collection
argument_list|)
return|;
block|}
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|not
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Iterators
operator|.
name|size
argument_list|(
name|iterator
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|EntrySetImpl
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createValues ()
name|Collection
argument_list|<
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|createValues
parameter_list|()
block|{
annotation|@
name|WeakOuter
class|class
name|ValuesImpl
extends|extends
name|Maps
operator|.
name|Values
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
block|{
name|ValuesImpl
parameter_list|()
block|{
name|super
argument_list|(
name|AsMap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
name|entryIterator
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entryIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
init|=
name|entryIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|filterCollection
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
operator|new
name|ValuePredicate
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|collection
operator|.
name|isEmpty
argument_list|()
operator|&&
name|c
operator|.
name|equals
argument_list|(
name|collection
argument_list|)
condition|)
block|{
if|if
condition|(
name|collection
operator|.
name|size
argument_list|()
operator|==
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
condition|)
block|{
name|entryIterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|Maps
operator|.
expr|<
name|Collection
argument_list|<
name|V
argument_list|>
operator|>
name|valuePredicateOnEntries
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|Maps
operator|.
expr|<
name|Collection
argument_list|<
name|V
argument_list|>
operator|>
name|valuePredicateOnEntries
argument_list|(
name|not
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|ValuesImpl
argument_list|()
return|;
block|}
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
operator|new
name|Keys
argument_list|()
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|Keys
class|class
name|Keys
extends|extends
name|Multimaps
operator|.
name|Keys
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|Keys ()
name|Keys
parameter_list|()
block|{
name|super
argument_list|(
name|FilteredEntryMultimap
operator|.
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove (@ullableDecl Object key, int occurrences)
specifier|public
name|int
name|remove
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|,
name|int
name|occurrences
parameter_list|)
block|{
name|checkNonnegative
argument_list|(
name|occurrences
argument_list|,
literal|"occurrences"
argument_list|)
expr_stmt|;
if|if
condition|(
name|occurrences
operator|==
literal|0
condition|)
block|{
return|return
name|count
argument_list|(
name|key
argument_list|)
return|;
block|}
name|Collection
argument_list|<
name|V
argument_list|>
name|collection
init|=
name|unfiltered
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|collection
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// key is equal to a K, if not a K itself
name|K
name|k
init|=
operator|(
name|K
operator|)
name|key
decl_stmt|;
name|int
name|oldCount
init|=
literal|0
decl_stmt|;
name|Iterator
argument_list|<
name|V
argument_list|>
name|itr
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|V
name|v
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|satisfies
argument_list|(
name|k
argument_list|,
name|v
argument_list|)
condition|)
block|{
name|oldCount
operator|++
expr_stmt|;
if|if
condition|(
name|oldCount
operator|<=
name|occurrences
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|oldCount
return|;
block|}
annotation|@
name|Override
DECL|method|entrySet ()
specifier|public
name|Set
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|K
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
return|return
operator|new
name|Multisets
operator|.
name|EntrySet
argument_list|<
name|K
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|Multiset
argument_list|<
name|K
argument_list|>
name|multiset
parameter_list|()
block|{
return|return
name|Keys
operator|.
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|Multiset
operator|.
name|Entry
argument_list|<
name|K
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Keys
operator|.
name|this
operator|.
name|entryIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|FilteredEntryMultimap
operator|.
name|this
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|removeEntriesIf
parameter_list|(
specifier|final
name|Predicate
argument_list|<
name|?
super|super
name|Multiset
operator|.
name|Entry
argument_list|<
name|K
argument_list|>
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|FilteredEntryMultimap
operator|.
name|this
operator|.
name|removeEntriesIf
argument_list|(
operator|new
name|Predicate
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
parameter_list|)
block|{
return|return
name|predicate
operator|.
name|apply
argument_list|(
name|Multisets
operator|.
name|immutableEntry
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
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
return|;
block|}
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
name|removeEntriesIf
argument_list|(
name|not
argument_list|(
name|in
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

