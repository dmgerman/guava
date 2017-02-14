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
name|checkEntryNotNull
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
name|ImmutableMapEntry
operator|.
name|createEntryArray
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
name|RegularImmutableMap
operator|.
name|checkNoConflictInKeyBucket
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
name|collect
operator|.
name|ImmutableMapEntry
operator|.
name|NonTerminalImmutableBiMapEntry
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
name|concurrent
operator|.
name|LazyInit
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
name|RetainedWith
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
name|function
operator|.
name|BiConsumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
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
comment|/**  * Bimap with zero or more mappings.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|RegularImmutableBiMap
class|class
name|RegularImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|EMPTY
specifier|static
specifier|final
name|RegularImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|RegularImmutableBiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
operator|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
index|[]
operator|)
name|ImmutableMap
operator|.
name|EMPTY_ENTRY_ARRAY
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
DECL|field|MAX_LOAD_FACTOR
specifier|static
specifier|final
name|double
name|MAX_LOAD_FACTOR
init|=
literal|1.2
decl_stmt|;
DECL|field|keyTable
specifier|private
specifier|final
specifier|transient
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|keyTable
decl_stmt|;
DECL|field|valueTable
specifier|private
specifier|final
specifier|transient
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|valueTable
decl_stmt|;
DECL|field|entries
specifier|private
specifier|final
specifier|transient
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
decl_stmt|;
DECL|field|mask
specifier|private
specifier|final
specifier|transient
name|int
name|mask
decl_stmt|;
DECL|field|hashCode
specifier|private
specifier|final
specifier|transient
name|int
name|hashCode
decl_stmt|;
DECL|method|fromEntries (Entry<K, V>.... entries)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|fromEntries
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
modifier|...
name|entries
parameter_list|)
block|{
return|return
name|fromEntryArray
argument_list|(
name|entries
operator|.
name|length
argument_list|,
name|entries
argument_list|)
return|;
block|}
DECL|method|fromEntryArray (int n, Entry<K, V>[] entryArray)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|fromEntryArray
parameter_list|(
name|int
name|n
parameter_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entryArray
parameter_list|)
block|{
name|checkPositionIndex
argument_list|(
name|n
argument_list|,
name|entryArray
operator|.
name|length
argument_list|)
expr_stmt|;
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|n
argument_list|,
name|MAX_LOAD_FACTOR
argument_list|)
decl_stmt|;
name|int
name|mask
init|=
name|tableSize
operator|-
literal|1
decl_stmt|;
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|keyTable
init|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
decl_stmt|;
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|valueTable
init|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
decl_stmt|;
if|if
condition|(
name|n
operator|==
name|entryArray
operator|.
name|length
condition|)
block|{
name|entries
operator|=
name|entryArray
expr_stmt|;
block|}
else|else
block|{
name|entries
operator|=
name|createEntryArray
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n
condition|;
name|i
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|entryArray
index|[
name|i
index|]
decl_stmt|;
name|K
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|V
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|checkEntryNotNull
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|int
name|keyHash
init|=
name|key
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|int
name|valueHash
init|=
name|value
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|int
name|keyBucket
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|keyHash
argument_list|)
operator|&
name|mask
decl_stmt|;
name|int
name|valueBucket
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|valueHash
argument_list|)
operator|&
name|mask
decl_stmt|;
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKeyBucket
init|=
name|keyTable
index|[
name|keyBucket
index|]
decl_stmt|;
name|checkNoConflictInKeyBucket
argument_list|(
name|key
argument_list|,
name|entry
argument_list|,
name|nextInKeyBucket
argument_list|)
expr_stmt|;
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInValueBucket
init|=
name|valueTable
index|[
name|valueBucket
index|]
decl_stmt|;
name|checkNoConflictInValueBucket
argument_list|(
name|value
argument_list|,
name|entry
argument_list|,
name|nextInValueBucket
argument_list|)
expr_stmt|;
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
decl_stmt|;
if|if
condition|(
name|nextInValueBucket
operator|==
literal|null
operator|&&
name|nextInKeyBucket
operator|==
literal|null
condition|)
block|{
comment|/*          * TODO(lowasser): consider using a NonTerminalImmutableMapEntry when nextInKeyBucket is          * nonnull but nextInValueBucket is null.  This may save a few bytes on some platforms, but          * 2-morphic call sites are often optimized much better than 3-morphic, so it'd require          * benchmarking.          */
name|boolean
name|reusable
init|=
name|entry
operator|instanceof
name|ImmutableMapEntry
operator|&&
operator|(
operator|(
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
operator|)
operator|.
name|isReusable
argument_list|()
decl_stmt|;
name|newEntry
operator|=
name|reusable
condition|?
operator|(
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|entry
else|:
operator|new
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newEntry
operator|=
operator|new
name|NonTerminalImmutableBiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|nextInKeyBucket
argument_list|,
name|nextInValueBucket
argument_list|)
expr_stmt|;
block|}
name|keyTable
index|[
name|keyBucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|valueTable
index|[
name|valueBucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|entries
index|[
name|i
index|]
operator|=
name|newEntry
expr_stmt|;
name|hashCode
operator|+=
name|keyHash
operator|^
name|valueHash
expr_stmt|;
block|}
return|return
operator|new
name|RegularImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|keyTable
argument_list|,
name|valueTable
argument_list|,
name|entries
argument_list|,
name|mask
argument_list|,
name|hashCode
argument_list|)
return|;
block|}
DECL|method|RegularImmutableBiMap ( ImmutableMapEntry<K, V>[] keyTable, ImmutableMapEntry<K, V>[] valueTable, Entry<K, V>[] entries, int mask, int hashCode)
specifier|private
name|RegularImmutableBiMap
parameter_list|(
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|keyTable
parameter_list|,
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|valueTable
parameter_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
parameter_list|,
name|int
name|mask
parameter_list|,
name|int
name|hashCode
parameter_list|)
block|{
name|this
operator|.
name|keyTable
operator|=
name|keyTable
expr_stmt|;
name|this
operator|.
name|valueTable
operator|=
name|valueTable
expr_stmt|;
name|this
operator|.
name|entries
operator|=
name|entries
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
expr_stmt|;
block|}
comment|// checkNoConflictInKeyBucket is static imported from RegularImmutableMap
DECL|method|checkNoConflictInValueBucket ( Object value, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> valueBucketHead)
specifier|private
specifier|static
name|void
name|checkNoConflictInValueBucket
parameter_list|(
name|Object
name|value
parameter_list|,
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
parameter_list|,
annotation|@
name|Nullable
name|ImmutableMapEntry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|valueBucketHead
parameter_list|)
block|{
for|for
control|(
init|;
name|valueBucketHead
operator|!=
literal|null
condition|;
name|valueBucketHead
operator|=
name|valueBucketHead
operator|.
name|getNextInValueBucket
argument_list|()
control|)
block|{
name|checkNoConflict
argument_list|(
operator|!
name|value
operator|.
name|equals
argument_list|(
name|valueBucketHead
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|,
literal|"value"
argument_list|,
name|entry
argument_list|,
name|valueBucketHead
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|get (@ullable Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|keyTable
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|RegularImmutableMap
operator|.
name|get
argument_list|(
name|key
argument_list|,
name|keyTable
argument_list|,
name|mask
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEntrySet ()
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
name|isEmpty
argument_list|()
condition|?
name|ImmutableSet
operator|.
expr|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|>
name|of
argument_list|()
else|:
operator|new
name|ImmutableMapEntrySet
operator|.
name|RegularEntrySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|,
name|entries
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapKeySet
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
DECL|method|forEach (BiConsumer<? super K, ? super V> action)
specifier|public
name|void
name|forEach
parameter_list|(
name|BiConsumer
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
super|super
name|V
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|action
operator|.
name|accept
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
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isHashCodeFast ()
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
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
operator|.
name|length
return|;
block|}
annotation|@
name|LazyInit
annotation|@
name|RetainedWith
DECL|field|inverse
specifier|private
specifier|transient
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableBiMap
operator|.
name|of
argument_list|()
return|;
block|}
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|result
init|=
name|inverse
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|inverse
operator|=
operator|new
name|Inverse
argument_list|()
else|:
name|result
return|;
block|}
DECL|class|Inverse
specifier|private
specifier|final
class|class
name|Inverse
extends|extends
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
block|{
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|inverse
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|inverse
parameter_list|()
block|{
return|return
name|RegularImmutableBiMap
operator|.
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|forEach (BiConsumer<? super V, ? super K> action)
specifier|public
name|void
name|forEach
parameter_list|(
name|BiConsumer
argument_list|<
name|?
super|super
name|V
argument_list|,
name|?
super|super
name|K
argument_list|>
name|action
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|RegularImmutableBiMap
operator|.
name|this
operator|.
name|forEach
argument_list|(
parameter_list|(
name|k
parameter_list|,
name|v
parameter_list|)
lambda|->
name|action
operator|.
name|accept
argument_list|(
name|v
argument_list|,
name|k
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (@ullable Object value)
specifier|public
name|K
name|get
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|valueTable
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|bucket
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|value
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|&
name|mask
decl_stmt|;
for|for
control|(
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|valueTable
index|[
name|bucket
index|]
init|;
name|entry
operator|!=
literal|null
condition|;
name|entry
operator|=
name|entry
operator|.
name|getNextInValueBucket
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|V
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapKeySet
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEntrySet ()
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|InverseEntrySet
argument_list|()
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|InverseEntrySet
specifier|final
class|class
name|InverseEntrySet
extends|extends
name|ImmutableMapEntrySet
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
block|{
annotation|@
name|Override
DECL|method|map ()
name|ImmutableMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|map
parameter_list|()
block|{
return|return
name|Inverse
operator|.
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|isHashCodeFast ()
name|boolean
name|isHashCodeFast
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|asList
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|forEach (Consumer<? super Entry<V, K>> action)
specifier|public
name|void
name|forEach
parameter_list|(
name|Consumer
argument_list|<
name|?
super|super
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|action
parameter_list|)
block|{
name|asList
argument_list|()
operator|.
name|forEach
argument_list|(
name|action
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|new
name|ImmutableAsList
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|entries
index|[
name|index
index|]
decl_stmt|;
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
name|ImmutableCollection
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
name|delegateCollection
parameter_list|()
block|{
return|return
name|InverseEntrySet
operator|.
name|this
return|;
block|}
block|}
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|InverseSerializedForm
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|RegularImmutableBiMap
operator|.
name|this
argument_list|)
return|;
block|}
block|}
DECL|class|InverseSerializedForm
specifier|private
specifier|static
class|class
name|InverseSerializedForm
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|forward
specifier|private
specifier|final
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
decl_stmt|;
DECL|method|InverseSerializedForm (ImmutableBiMap<K, V> forward)
name|InverseSerializedForm
parameter_list|(
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
parameter_list|)
block|{
name|this
operator|.
name|forward
operator|=
name|forward
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|forward
operator|.
name|inverse
argument_list|()
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1
decl_stmt|;
block|}
block|}
end_class

end_unit

