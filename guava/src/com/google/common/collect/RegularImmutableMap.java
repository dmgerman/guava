begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/* * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|ImmutableMapEntry
operator|.
name|TerminalEntry
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
comment|// entries in insertion order
DECL|field|entries
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
DECL|method|RegularImmutableMap (TerminalEntry<?, ?>.... theEntries)
name|RegularImmutableMap
parameter_list|(
name|TerminalEntry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
modifier|...
name|theEntries
parameter_list|)
block|{
name|this
argument_list|(
name|theEntries
operator|.
name|length
argument_list|,
name|theEntries
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructor for RegularImmutableMap that takes as input an array of {@code TerminalEntry}    * entries.  Assumes that these entries have already been checked for null.    *    *<p>This allows reuse of the entry objects from the array in the actual implementation.    */
DECL|method|RegularImmutableMap (int size, TerminalEntry<?, ?>[] theEntries)
name|RegularImmutableMap
parameter_list|(
name|int
name|size
parameter_list|,
name|TerminalEntry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
index|[]
name|theEntries
parameter_list|)
block|{
name|entries
operator|=
name|createEntryArray
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|size
argument_list|,
name|MAX_LOAD_FACTOR
argument_list|)
decl_stmt|;
name|table
operator|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
expr_stmt|;
name|mask
operator|=
name|tableSize
operator|-
literal|1
expr_stmt|;
for|for
control|(
name|int
name|entryIndex
init|=
literal|0
init|;
name|entryIndex
operator|<
name|size
condition|;
name|entryIndex
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|TerminalEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
operator|(
name|TerminalEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|theEntries
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
name|Nullable
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
init|=
operator|(
name|existing
operator|==
literal|null
operator|)
condition|?
name|entry
else|:
operator|new
name|NonTerminalMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|entry
argument_list|,
name|existing
argument_list|)
decl_stmt|;
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
name|checkNoConflictInBucket
argument_list|(
name|key
argument_list|,
name|newEntry
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Constructor for RegularImmutableMap that makes no assumptions about the input entries.    */
DECL|method|RegularImmutableMap (Entry<?, ?>[] theEntries)
name|RegularImmutableMap
parameter_list|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
index|[]
name|theEntries
parameter_list|)
block|{
name|int
name|size
init|=
name|theEntries
operator|.
name|length
decl_stmt|;
name|entries
operator|=
name|createEntryArray
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|int
name|tableSize
init|=
name|Hashing
operator|.
name|closedTableSize
argument_list|(
name|size
argument_list|,
name|MAX_LOAD_FACTOR
argument_list|)
decl_stmt|;
name|table
operator|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
expr_stmt|;
name|mask
operator|=
name|tableSize
operator|-
literal|1
expr_stmt|;
for|for
control|(
name|int
name|entryIndex
init|=
literal|0
init|;
name|entryIndex
operator|<
name|size
condition|;
name|entryIndex
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// all our callers carefully put in only Entry<K, V>s
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|theEntries
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
name|Nullable
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
init|=
operator|(
name|existing
operator|==
literal|null
operator|)
condition|?
operator|new
name|TerminalEntry
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
else|:
operator|new
name|NonTerminalMapEntry
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
name|existing
argument_list|)
decl_stmt|;
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
name|checkNoConflictInBucket
argument_list|(
name|key
argument_list|,
name|newEntry
argument_list|,
name|existing
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO(user): consider sharing this code with RegularImmutableBiMap
DECL|method|checkNoConflictInBucket ( Object key, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> bucketHead)
specifier|private
specifier|static
name|void
name|checkNoConflictInBucket
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
name|Nullable
name|ImmutableMapEntry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bucketHead
parameter_list|)
block|{
for|for
control|(
init|;
name|bucketHead
operator|!=
literal|null
condition|;
name|bucketHead
operator|=
name|bucketHead
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
name|bucketHead
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
literal|"key"
argument_list|,
name|entry
argument_list|,
name|bucketHead
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|NonTerminalMapEntry
specifier|private
specifier|static
specifier|final
class|class
name|NonTerminalMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|nextInKeyBucket
specifier|private
specifier|final
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKeyBucket
decl_stmt|;
DECL|method|NonTerminalMapEntry (K key, V value, ImmutableMapEntry<K, V> nextInKeyBucket)
name|NonTerminalMapEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKeyBucket
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|nextInKeyBucket
operator|=
name|nextInKeyBucket
expr_stmt|;
block|}
DECL|method|NonTerminalMapEntry (ImmutableMapEntry<K, V> contents, ImmutableMapEntry<K, V> nextInKeyBucket)
name|NonTerminalMapEntry
parameter_list|(
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|contents
parameter_list|,
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKeyBucket
parameter_list|)
block|{
name|super
argument_list|(
name|contents
argument_list|)
expr_stmt|;
name|this
operator|.
name|nextInKeyBucket
operator|=
name|nextInKeyBucket
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNextInKeyBucket ()
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInKeyBucket
parameter_list|()
block|{
return|return
name|nextInKeyBucket
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getNextInValueBucket ()
name|ImmutableMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInValueBucket
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**    * Closed addressing tends to perform well even with high load factors.    * Being conservative here ensures that the table is still likely to be    * relatively sparse (hence it misses fast) while saving space.    */
DECL|field|MAX_LOAD_FACTOR
specifier|private
specifier|static
specifier|final
name|double
name|MAX_LOAD_FACTOR
init|=
literal|1.2
decl_stmt|;
DECL|method|get (@ullable Object key)
annotation|@
name|Override
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
comment|// TODO(user): consider sharing this code with RegularImmutableBiMap
if|if
condition|(
name|key
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
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|table
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
name|K
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
DECL|method|isPartialView ()
annotation|@
name|Override
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

