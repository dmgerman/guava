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
name|checkElementIndex
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
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * A hash-based implementation of {@link ImmutableMap}.  *  * @author Louis Wasserman  */
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
DECL|field|ABSENT
specifier|private
specifier|static
specifier|final
name|int
name|ABSENT
init|=
operator|-
literal|1
decl_stmt|;
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
literal|null
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|/*    * This is an implementation of ImmutableMap optimized especially for Android, which does not like    * objects per entry.  Instead we use an open-addressed hash table.  This design is basically    * equivalent to RegularImmutableSet, save that instead of having a hash table containing the    * elements directly and null for empty positions, we store indices of the keys in the hash table,    * and ABSENT for empty positions.  We then look up the keys in alternatingKeysAndValues.    *    * (The index actually stored is the index of the key in alternatingKeysAndValues, which is    * double the index of the entry in entrySet.asList.)    *    * The basic data structure is described in https://en.wikipedia.org/wiki/Open_addressing.    * The pointer to a key is stored in hashTable[Hashing.smear(key.hashCode())] % table.length,    * save that if that location is already full, we try the next index, and the next, until we    * find an empty table position.  Since the table has a power-of-two size, we use    *& (table.length - 1) instead of % table.length, though.    */
DECL|field|hashTable
specifier|private
specifier|final
specifier|transient
name|int
index|[]
name|hashTable
decl_stmt|;
DECL|field|alternatingKeysAndValues
annotation|@
name|VisibleForTesting
specifier|final
specifier|transient
name|Object
index|[]
name|alternatingKeysAndValues
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|create (int n, Object[] alternatingKeysAndValues)
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
name|create
parameter_list|(
name|int
name|n
parameter_list|,
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|)
block|{
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
elseif|else
if|if
condition|(
name|n
operator|==
literal|1
condition|)
block|{
name|checkEntryNotNull
argument_list|(
name|alternatingKeysAndValues
index|[
literal|0
index|]
argument_list|,
name|alternatingKeysAndValues
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
literal|null
argument_list|,
name|alternatingKeysAndValues
argument_list|,
literal|1
argument_list|)
return|;
block|}
name|checkPositionIndex
argument_list|(
name|n
argument_list|,
name|alternatingKeysAndValues
operator|.
name|length
operator|>>
literal|1
argument_list|)
expr_stmt|;
name|int
name|tableSize
init|=
name|ImmutableSet
operator|.
name|chooseTableSize
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|int
index|[]
name|hashTable
init|=
name|createHashTable
argument_list|(
name|alternatingKeysAndValues
argument_list|,
name|n
argument_list|,
name|tableSize
argument_list|,
literal|0
argument_list|)
decl_stmt|;
return|return
operator|new
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|hashTable
argument_list|,
name|alternatingKeysAndValues
argument_list|,
name|n
argument_list|)
return|;
block|}
comment|/**    * Returns a hash table for the specified keys and values, and ensures that neither keys nor    * values are null.    */
DECL|method|createHashTable ( Object[] alternatingKeysAndValues, int n, int tableSize, int keyOffset)
specifier|static
name|int
index|[]
name|createHashTable
parameter_list|(
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|,
name|int
name|n
parameter_list|,
name|int
name|tableSize
parameter_list|,
name|int
name|keyOffset
parameter_list|)
block|{
if|if
condition|(
name|n
operator|==
literal|1
condition|)
block|{
comment|// for n=1 we don't create a hash table, but we need to do the checkEntryNotNull check!
name|checkEntryNotNull
argument_list|(
name|alternatingKeysAndValues
index|[
name|keyOffset
index|]
argument_list|,
name|alternatingKeysAndValues
index|[
name|keyOffset
operator|^
literal|1
index|]
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|int
name|mask
init|=
name|tableSize
operator|-
literal|1
decl_stmt|;
name|int
index|[]
name|hashTable
init|=
operator|new
name|int
index|[
name|tableSize
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|hashTable
argument_list|,
name|ABSENT
argument_list|)
expr_stmt|;
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
name|Object
name|key
init|=
name|alternatingKeysAndValues
index|[
literal|2
operator|*
name|i
operator|+
name|keyOffset
index|]
decl_stmt|;
name|Object
name|value
init|=
name|alternatingKeysAndValues
index|[
literal|2
operator|*
name|i
operator|+
operator|(
name|keyOffset
operator|^
literal|1
operator|)
index|]
decl_stmt|;
name|checkEntryNotNull
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|h
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
init|;
condition|;
name|h
operator|++
control|)
block|{
name|h
operator|&=
name|mask
expr_stmt|;
name|int
name|previous
init|=
name|hashTable
index|[
name|h
index|]
decl_stmt|;
if|if
condition|(
name|previous
operator|==
name|ABSENT
condition|)
block|{
name|hashTable
index|[
name|h
index|]
operator|=
literal|2
operator|*
name|i
operator|+
name|keyOffset
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|alternatingKeysAndValues
index|[
name|previous
index|]
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple entries with same key: "
operator|+
name|key
operator|+
literal|"="
operator|+
name|value
operator|+
literal|" and "
operator|+
name|alternatingKeysAndValues
index|[
name|previous
index|]
operator|+
literal|"="
operator|+
name|alternatingKeysAndValues
index|[
name|previous
operator|^
literal|1
index|]
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|hashTable
return|;
block|}
DECL|method|RegularImmutableMap (int[] hashTable, Object[] alternatingKeysAndValues, int size)
specifier|private
name|RegularImmutableMap
parameter_list|(
name|int
index|[]
name|hashTable
parameter_list|,
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|hashTable
operator|=
name|hashTable
expr_stmt|;
name|this
operator|.
name|alternatingKeysAndValues
operator|=
name|alternatingKeysAndValues
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
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
name|size
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
annotation|@
name|NullableDecl
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
operator|(
name|V
operator|)
name|get
argument_list|(
name|hashTable
argument_list|,
name|alternatingKeysAndValues
argument_list|,
name|size
argument_list|,
literal|0
argument_list|,
name|key
argument_list|)
return|;
block|}
DECL|method|get ( @ullableDecl int[] hashTable, @NullableDecl Object[] alternatingKeysAndValues, int size, int keyOffset, @NullableDecl Object key)
specifier|static
name|Object
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|int
index|[]
name|hashTable
parameter_list|,
annotation|@
name|NullableDecl
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|,
name|int
name|size
parameter_list|,
name|int
name|keyOffset
parameter_list|,
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
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
elseif|else
if|if
condition|(
name|size
operator|==
literal|1
condition|)
block|{
return|return
name|alternatingKeysAndValues
index|[
name|keyOffset
index|]
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|?
name|alternatingKeysAndValues
index|[
name|keyOffset
operator|^
literal|1
index|]
else|:
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|hashTable
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|mask
init|=
name|hashTable
operator|.
name|length
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|h
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
init|;
condition|;
name|h
operator|++
control|)
block|{
name|h
operator|&=
name|mask
expr_stmt|;
name|int
name|index
init|=
name|hashTable
index|[
name|h
index|]
decl_stmt|;
if|if
condition|(
name|index
operator|==
name|ABSENT
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|alternatingKeysAndValues
index|[
name|index
index|]
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|alternatingKeysAndValues
index|[
name|index
operator|^
literal|1
index|]
return|;
block|}
block|}
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
name|EntrySet
argument_list|<>
argument_list|(
name|this
argument_list|,
name|alternatingKeysAndValues
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
return|;
block|}
DECL|class|EntrySet
specifier|static
class|class
name|EntrySet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|field|map
specifier|private
specifier|final
specifier|transient
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|field|alternatingKeysAndValues
specifier|private
specifier|final
specifier|transient
name|Object
index|[]
name|alternatingKeysAndValues
decl_stmt|;
DECL|field|keyOffset
specifier|private
specifier|final
specifier|transient
name|int
name|keyOffset
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
DECL|method|EntrySet (ImmutableMap<K, V> map, Object[] alternatingKeysAndValues, int keyOffset, int size)
name|EntrySet
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|,
name|int
name|keyOffset
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|alternatingKeysAndValues
operator|=
name|alternatingKeysAndValues
expr_stmt|;
name|this
operator|.
name|keyOffset
operator|=
name|keyOffset
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
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
DECL|method|copyIntoArray (Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|asList
argument_list|()
operator|.
name|copyIntoArray
argument_list|(
name|dst
argument_list|,
name|offset
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createAsList ()
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createAsList
parameter_list|()
block|{
return|return
operator|new
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|K
name|key
init|=
operator|(
name|K
operator|)
name|alternatingKeysAndValues
index|[
literal|2
operator|*
name|index
operator|+
name|keyOffset
index|]
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|V
name|value
init|=
operator|(
name|V
operator|)
name|alternatingKeysAndValues
index|[
literal|2
operator|*
name|index
operator|+
operator|(
name|keyOffset
operator|^
literal|1
operator|)
index|]
decl_stmt|;
return|return
operator|new
name|AbstractMap
operator|.
name|SimpleImmutableEntry
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
name|size
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
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
if|if
condition|(
name|object
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
name|object
decl_stmt|;
name|Object
name|k
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|v
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|v
operator|!=
literal|null
operator|&&
name|v
operator|.
name|equals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|k
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|false
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
name|size
return|;
block|}
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|ImmutableList
argument_list|<
name|K
argument_list|>
name|keyList
init|=
operator|(
name|ImmutableList
argument_list|<
name|K
argument_list|>
operator|)
operator|new
name|KeysOrValuesAsList
argument_list|(
name|alternatingKeysAndValues
argument_list|,
literal|0
argument_list|,
name|size
argument_list|)
decl_stmt|;
return|return
operator|new
name|KeySet
argument_list|<
name|K
argument_list|>
argument_list|(
name|this
argument_list|,
name|keyList
argument_list|)
return|;
block|}
DECL|class|KeysOrValuesAsList
specifier|static
specifier|final
class|class
name|KeysOrValuesAsList
extends|extends
name|ImmutableList
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|alternatingKeysAndValues
specifier|private
specifier|final
specifier|transient
name|Object
index|[]
name|alternatingKeysAndValues
decl_stmt|;
DECL|field|offset
specifier|private
specifier|final
specifier|transient
name|int
name|offset
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
specifier|transient
name|int
name|size
decl_stmt|;
DECL|method|KeysOrValuesAsList (Object[] alternatingKeysAndValues, int offset, int size)
name|KeysOrValuesAsList
parameter_list|(
name|Object
index|[]
name|alternatingKeysAndValues
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|alternatingKeysAndValues
operator|=
name|alternatingKeysAndValues
expr_stmt|;
name|this
operator|.
name|offset
operator|=
name|offset
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|Object
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|alternatingKeysAndValues
index|[
literal|2
operator|*
name|index
operator|+
name|offset
index|]
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
name|size
return|;
block|}
block|}
DECL|class|KeySet
specifier|static
specifier|final
class|class
name|KeySet
parameter_list|<
name|K
parameter_list|>
extends|extends
name|ImmutableSet
argument_list|<
name|K
argument_list|>
block|{
DECL|field|map
specifier|private
specifier|final
specifier|transient
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
name|map
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|K
argument_list|>
name|list
decl_stmt|;
DECL|method|KeySet (ImmutableMap<K, ?> map, ImmutableList<K> list)
name|KeySet
parameter_list|(
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|?
argument_list|>
name|map
parameter_list|,
name|ImmutableList
argument_list|<
name|K
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|UnmodifiableIterator
argument_list|<
name|K
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
DECL|method|copyIntoArray (Object[] dst, int offset)
name|int
name|copyIntoArray
parameter_list|(
name|Object
index|[]
name|dst
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|asList
argument_list|()
operator|.
name|copyIntoArray
argument_list|(
name|dst
argument_list|,
name|offset
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|asList ()
specifier|public
name|ImmutableList
argument_list|<
name|K
argument_list|>
name|asList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullableDecl Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|object
parameter_list|)
block|{
return|return
name|map
operator|.
name|get
argument_list|(
name|object
argument_list|)
operator|!=
literal|null
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
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
operator|(
name|ImmutableList
argument_list|<
name|V
argument_list|>
operator|)
operator|new
name|KeysOrValuesAsList
argument_list|(
name|alternatingKeysAndValues
argument_list|,
literal|1
argument_list|,
name|size
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
literal|false
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

