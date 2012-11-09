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
name|Collection
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
comment|/**  * Bimap with two or more mappings.  *  * @author Louis Wasserman  */
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
DECL|class|BiMapEntry
specifier|private
specifier|static
class|class
name|BiMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|BiMapEntry (K key, V value)
name|BiMapEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Nullable
DECL|method|getNextInKToVBucket ()
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInKToVBucket
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Nullable
DECL|method|getNextInVToKBucket ()
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInVToKBucket
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|NonTerminalBiMapEntry
specifier|private
specifier|static
class|class
name|NonTerminalBiMapEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|nextInKToVBucket
annotation|@
name|Nullable
specifier|private
specifier|final
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKToVBucket
decl_stmt|;
DECL|field|nextInVToKBucket
annotation|@
name|Nullable
specifier|private
specifier|final
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInVToKBucket
decl_stmt|;
DECL|method|NonTerminalBiMapEntry (K key, V value, @Nullable BiMapEntry<K, V> nextInKToVBucket, @Nullable BiMapEntry<K, V> nextInVToKBucket)
name|NonTerminalBiMapEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
annotation|@
name|Nullable
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKToVBucket
parameter_list|,
annotation|@
name|Nullable
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInVToKBucket
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
name|nextInKToVBucket
operator|=
name|nextInKToVBucket
expr_stmt|;
name|this
operator|.
name|nextInVToKBucket
operator|=
name|nextInVToKBucket
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getNextInKToVBucket ()
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInKToVBucket
parameter_list|()
block|{
return|return
name|nextInKToVBucket
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getNextInVToKBucket ()
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getNextInVToKBucket
parameter_list|()
block|{
return|return
name|nextInVToKBucket
return|;
block|}
block|}
DECL|field|kToVTable
specifier|private
specifier|transient
specifier|final
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|kToVTable
decl_stmt|;
DECL|field|vToKTable
specifier|private
specifier|transient
specifier|final
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|vToKTable
decl_stmt|;
DECL|field|entries
specifier|private
specifier|transient
specifier|final
name|BiMapEntry
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
specifier|transient
specifier|final
name|int
name|mask
decl_stmt|;
DECL|field|hashCode
specifier|private
specifier|transient
specifier|final
name|int
name|hashCode
decl_stmt|;
DECL|method|RegularImmutableBiMap (Collection<? extends Entry<? extends K, ? extends V>> entriesToAdd)
name|RegularImmutableBiMap
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
argument_list|>
name|entriesToAdd
parameter_list|)
block|{
name|int
name|n
init|=
name|entriesToAdd
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|tableSize
init|=
name|RegularImmutableMap
operator|.
name|chooseTableSize
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|this
operator|.
name|mask
operator|=
name|tableSize
operator|-
literal|1
expr_stmt|;
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|kToVTable
init|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
decl_stmt|;
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|vToKTable
init|=
name|createEntryArray
argument_list|(
name|tableSize
argument_list|)
decl_stmt|;
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
init|=
name|createEntryArray
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|hashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|entry
range|:
name|entriesToAdd
control|)
block|{
name|K
name|key
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|V
name|value
init|=
name|checkNotNull
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
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
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInKToVBucket
init|=
name|kToVTable
index|[
name|keyBucket
index|]
decl_stmt|;
for|for
control|(
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|kToVEntry
init|=
name|nextInKToVBucket
init|;
name|kToVEntry
operator|!=
literal|null
condition|;
name|kToVEntry
operator|=
name|kToVEntry
operator|.
name|getNextInKToVBucket
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|kToVEntry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple entries with same key: "
operator|+
name|entry
operator|+
literal|" and "
operator|+
name|kToVEntry
argument_list|)
throw|;
block|}
block|}
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nextInVToKBucket
init|=
name|vToKTable
index|[
name|valueBucket
index|]
decl_stmt|;
for|for
control|(
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|vToKEntry
init|=
name|nextInVToKBucket
init|;
name|vToKEntry
operator|!=
literal|null
condition|;
name|vToKEntry
operator|=
name|vToKEntry
operator|.
name|getNextInVToKBucket
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|.
name|equals
argument_list|(
name|vToKEntry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Multiple entries with same value: "
operator|+
name|entry
operator|+
literal|" and "
operator|+
name|vToKEntry
argument_list|)
throw|;
block|}
block|}
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
init|=
operator|(
name|nextInKToVBucket
operator|==
literal|null
operator|&&
name|nextInVToKBucket
operator|==
literal|null
operator|)
condition|?
operator|new
name|BiMapEntry
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
name|NonTerminalBiMapEntry
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
name|nextInKToVBucket
argument_list|,
name|nextInVToKBucket
argument_list|)
decl_stmt|;
name|kToVTable
index|[
name|keyBucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|vToKTable
index|[
name|valueBucket
index|]
operator|=
name|newEntry
expr_stmt|;
name|entries
index|[
name|i
operator|++
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
name|this
operator|.
name|kToVTable
operator|=
name|kToVTable
expr_stmt|;
name|this
operator|.
name|vToKTable
operator|=
name|vToKTable
expr_stmt|;
name|this
operator|.
name|entries
operator|=
name|entries
expr_stmt|;
name|this
operator|.
name|hashCode
operator|=
name|hashCode
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createEntryArray (int length)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createEntryArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|BiMapEntry
index|[
name|length
index|]
return|;
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
name|bucket
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
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|kToVTable
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
name|getNextInKToVBucket
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
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
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
name|ImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
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
name|RegularImmutableAsList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
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
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
block|}
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
name|BiMapEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|vToKTable
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
name|getNextInVToKBucket
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

