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
name|annotations
operator|.
name|VisibleForTesting
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
name|NonTerminalImmutableMapEntry
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
name|Weak
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
name|function
operator|.
name|BiConsumer
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
comment|/**  * Implementation of {@link ImmutableMap} with two or more entries.  *  * @author Jesse Wilson  * @author Kevin Bourrillion  * @author Gregory Kick  */
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
DECL|class|RegularImmutableMap
specifier|final
class|class
name|RegularImmutableMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|field|EMPTY
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|EMPTY
init|=
operator|new
name|RegularImmutableMap
argument_list|<>
argument_list|(
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
literal|null
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|// entries in insertion order
DECL|field|entries
annotation|@
name|VisibleForTesting
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
comment|// array of linked lists of entries
DECL|field|table
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
name|table
decl_stmt|;
comment|// 'and' with an int to get a table index
DECL|field|mask
specifier|private
specifier|final
specifier|transient
name|int
name|mask
decl_stmt|;
DECL|method|fromEntries (Entry<K, V>.... entries)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableMap
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
comment|/**    * Creates a RegularImmutableMap from the first n entries in entryArray. This implementation may    * replace the entries in entryArray with its own entry objects (though they will have the same    * key/value contents), and may take ownership of entryArray.    */
DECL|method|fromEntryArray (int n, Entry<K, V>[] entryArray)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableMap
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
if|if
condition|(
name|n
operator|==
literal|0
condition|)
block|{
return|return
operator|(
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|EMPTY
return|;
block|}
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
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|table
init|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
decl_stmt|;
name|int
name|mask
init|=
name|tableSize
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|entryIndex
init|=
literal|0
init|;
name|entryIndex
operator|<
name|n
condition|;
name|entryIndex
operator|++
control|)
block|{
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
name|entryIndex
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
name|tableIndex
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|key
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|&
name|mask
decl_stmt|;
annotation|@
name|NullableDecl
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|existing
init|=
name|table
index|[
name|tableIndex
index|]
decl_stmt|;
comment|// prepend, not append, so the entries can be immutable
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
name|existing
operator|==
literal|null
condition|)
block|{
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
name|NonTerminalImmutableMapEntry
argument_list|<>
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
name|table
index|[
name|tableIndex
index|]
operator|=
name|newEntry
expr_stmt|;
name|entries
index|[
name|entryIndex
index|]
operator|=
name|newEntry
expr_stmt|;
name|checkNoConflictInKeyBucket
argument_list|(
name|key
argument_list|,
name|newEntry
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|RegularImmutableMap
argument_list|<>
argument_list|(
name|entries
argument_list|,
name|table
argument_list|,
name|mask
argument_list|)
return|;
block|}
DECL|method|RegularImmutableMap (Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask)
specifier|private
name|RegularImmutableMap
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
parameter_list|,
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|table
parameter_list|,
name|int
name|mask
parameter_list|)
block|{
name|this
operator|.
name|entries
operator|=
name|entries
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
block|}
DECL|method|checkNoConflictInKeyBucket ( Object key, Entry<?, ?> entry, @NullableDecl ImmutableMapEntry<?, ?> keyBucketHead)
specifier|static
name|void
name|checkNoConflictInKeyBucket
parameter_list|(
name|Object
name|key
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
name|NullableDecl
name|ImmutableMapEntry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|keyBucketHead
parameter_list|)
block|{
for|for
control|(
init|;
name|keyBucketHead
operator|!=
literal|null
condition|;
name|keyBucketHead
operator|=
name|keyBucketHead
operator|.
name|getNextInKeyBucket
argument_list|()
control|)
block|{
name|checkNoConflict
argument_list|(
operator|!
name|key
operator|.
name|equals
argument_list|(
name|keyBucketHead
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
literal|"key"
argument_list|,
name|entry
argument_list|,
name|keyBucketHead
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Closed addressing tends to perform well even with high load factors. Being conservative here    * ensures that the table is still likely to be relatively sparse (hence it misses fast) while    * saving space.    */
DECL|field|MAX_LOAD_FACTOR
specifier|private
specifier|static
specifier|final
name|double
name|MAX_LOAD_FACTOR
init|=
literal|1.2
decl_stmt|;
annotation|@
name|Override
DECL|method|get (@ullableDecl Object key)
specifier|public
name|V
name|get
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
argument_list|,
name|table
argument_list|,
name|mask
argument_list|)
return|;
block|}
annotation|@
name|NullableDecl
DECL|method|get ( @ullableDecl Object key, @NullableDecl ImmutableMapEntry<?, V>[] keyTable, int mask)
specifier|static
parameter_list|<
name|V
parameter_list|>
name|V
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|,
annotation|@
name|NullableDecl
name|ImmutableMapEntry
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
index|[]
name|keyTable
parameter_list|,
name|int
name|mask
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|keyTable
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|index
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|key
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
name|?
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|keyTable
index|[
name|index
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
name|getNextInKeyBucket
argument_list|()
control|)
block|{
name|Object
name|candidateKey
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
comment|/*        * Assume that equals uses the == optimization when appropriate, and that        * it would check hash codes as an optimization when appropriate. If we        * did these things, it would just make things worse for the most        * performance-conscious users.        */
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|candidateKey
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
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
operator|new
name|ImmutableMapEntrySet
operator|.
name|RegularEntrySet
argument_list|<>
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
name|KeySet
argument_list|<>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|KeySet
specifier|private
specifier|static
specifier|final
class|class
name|KeySet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableSet
operator|.
name|Indexed
argument_list|<
name|K
argument_list|>
block|{
DECL|field|map
annotation|@
name|Weak
specifier|private
specifier|final
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|KeySet (RegularImmutableMap<K, V> map)
name|KeySet
parameter_list|(
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
name|K
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|map
operator|.
name|entries
index|[
name|index
index|]
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|object
argument_list|)
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
literal|true
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
name|map
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|<
name|K
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
parameter_list|<
name|K
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|map
specifier|final
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
name|map
decl_stmt|;
DECL|method|SerializedForm (ImmutableMap<K, ?> map)
name|SerializedForm
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|map
operator|.
name|keySet
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
literal|0
decl_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createValues ()
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
block|{
return|return
operator|new
name|Values
argument_list|<>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Values
specifier|private
specifier|static
specifier|final
class|class
name|Values
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|V
argument_list|>
block|{
DECL|field|map
annotation|@
name|Weak
specifier|final
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|Values (RegularImmutableMap<K, V> map)
name|Values
parameter_list|(
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|V
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|map
operator|.
name|entries
index|[
name|index
index|]
operator|.
name|getValue
argument_list|()
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
name|map
operator|.
name|size
argument_list|()
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
literal|true
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|<
name|V
argument_list|>
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
DECL|class|SerializedForm
specifier|private
specifier|static
class|class
name|SerializedForm
parameter_list|<
name|V
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|map
specifier|final
name|ImmutableMap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|SerializedForm (ImmutableMap<?, V> map)
name|SerializedForm
parameter_list|(
name|ImmutableMap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|map
operator|.
name|values
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
literal|0
decl_stmt|;
block|}
block|}
comment|// This class is never actually serialized directly, but we have to make the
comment|// warning go away (and suppressing would suppress for all nested classes too)
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

