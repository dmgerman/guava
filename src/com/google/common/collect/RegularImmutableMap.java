begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ImmutableSet
operator|.
name|ArrayImmutableSet
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
name|ImmutableSet
operator|.
name|TransformedImmutableSet
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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|concurrent
operator|.
name|Immutable
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
name|LinkedEntry
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
name|LinkedEntry
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
DECL|field|keySetHashCode
specifier|private
specifier|final
specifier|transient
name|int
name|keySetHashCode
decl_stmt|;
comment|// TODO(gak): investigate avoiding the creation of ImmutableEntries since we
comment|// re-copy them anyway.
DECL|method|RegularImmutableMap (Entry<?, ?>.... immutableEntries)
name|RegularImmutableMap
parameter_list|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
modifier|...
name|immutableEntries
parameter_list|)
block|{
name|int
name|size
init|=
name|immutableEntries
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
name|chooseTableSize
argument_list|(
name|size
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
name|int
name|keySetHashCodeMutable
init|=
literal|0
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
name|size
condition|;
name|entryIndex
operator|++
control|)
block|{
comment|// each of our 6 callers carefully put only Entry<K, V>s into the array!
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
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|immutableEntries
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
name|keyHashCode
init|=
name|key
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|keySetHashCodeMutable
operator|+=
name|keyHashCode
expr_stmt|;
name|int
name|tableIndex
init|=
name|Hashing
operator|.
name|smear
argument_list|(
name|keyHashCode
argument_list|)
operator|&
name|mask
decl_stmt|;
annotation|@
name|Nullable
name|LinkedEntry
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
name|LinkedEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|linkedEntry
init|=
operator|new
name|LinkedEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|existing
argument_list|)
decl_stmt|;
name|table
index|[
name|tableIndex
index|]
operator|=
name|linkedEntry
expr_stmt|;
name|entries
index|[
name|entryIndex
index|]
operator|=
name|linkedEntry
expr_stmt|;
while|while
condition|(
name|existing
operator|!=
literal|null
condition|)
block|{
name|checkArgument
argument_list|(
operator|!
name|key
operator|.
name|equals
argument_list|(
name|existing
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
literal|"duplicate key: %s"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|existing
operator|=
name|existing
operator|.
name|next
expr_stmt|;
block|}
block|}
name|keySetHashCode
operator|=
name|keySetHashCodeMutable
expr_stmt|;
block|}
DECL|method|chooseTableSize (int size)
specifier|private
specifier|static
name|int
name|chooseTableSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
comment|// least power of 2 greater than size
name|int
name|tableSize
init|=
name|Integer
operator|.
name|highestOneBit
argument_list|(
name|size
argument_list|)
operator|<<
literal|1
decl_stmt|;
name|checkArgument
argument_list|(
name|tableSize
operator|>
literal|0
argument_list|,
literal|"table too large: %s"
argument_list|,
name|size
argument_list|)
expr_stmt|;
return|return
name|tableSize
return|;
block|}
comment|/**    * Creates a {@link LinkedEntry} array to hold parameterized entries. The    * result must never be upcast back to LinkedEntry[] (or Object[], etc.), or    * allowed to escape the class.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Safe as long as the javadocs are followed
DECL|method|createEntryArray (int size)
specifier|private
name|LinkedEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createEntryArray
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
operator|new
name|LinkedEntry
index|[
name|size
index|]
return|;
block|}
annotation|@
name|Immutable
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// this class is never serialized
DECL|class|LinkedEntry
specifier|private
specifier|static
specifier|final
class|class
name|LinkedEntry
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
DECL|field|next
annotation|@
name|Nullable
specifier|final
name|LinkedEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
decl_stmt|;
DECL|method|LinkedEntry (K key, V value, @Nullable LinkedEntry<K, V> next)
name|LinkedEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|,
annotation|@
name|Nullable
name|LinkedEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|next
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
name|next
operator|=
name|next
expr_stmt|;
block|}
block|}
DECL|method|get (Object key)
annotation|@
name|Override
specifier|public
name|V
name|get
parameter_list|(
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
name|LinkedEntry
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
name|next
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
DECL|method|isEmpty ()
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|containsValue (Object value)
annotation|@
name|Override
specifier|public
name|boolean
name|containsValue
parameter_list|(
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
literal|false
return|;
block|}
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
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
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
DECL|field|entrySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
decl_stmt|;
DECL|method|entrySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entrySet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|es
init|=
name|entrySet
decl_stmt|;
return|return
operator|(
name|es
operator|==
literal|null
operator|)
condition|?
operator|(
name|entrySet
operator|=
operator|new
name|EntrySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|es
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|EntrySet
specifier|private
specifier|static
class|class
name|EntrySet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ArrayImmutableSet
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
specifier|final
specifier|transient
name|RegularImmutableMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|EntrySet (RegularImmutableMap<K, V> map)
name|EntrySet
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
name|super
argument_list|(
name|map
operator|.
name|entries
argument_list|)
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
if|if
condition|(
name|target
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
name|target
decl_stmt|;
name|V
name|mappedValue
init|=
name|map
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|mappedValue
operator|!=
literal|null
operator|&&
name|mappedValue
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
DECL|field|keySet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
decl_stmt|;
DECL|method|keySet ()
annotation|@
name|Override
specifier|public
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|ks
init|=
name|keySet
decl_stmt|;
return|return
operator|(
name|ks
operator|==
literal|null
operator|)
condition|?
operator|(
name|keySet
operator|=
operator|new
name|KeySet
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|ks
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|KeySet
specifier|private
specifier|static
class|class
name|KeySet
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|TransformedImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|K
argument_list|>
block|{
DECL|field|map
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
name|super
argument_list|(
name|map
operator|.
name|entries
argument_list|,
name|map
operator|.
name|keySetHashCode
argument_list|)
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
DECL|method|transform (Entry<K, V> element)
annotation|@
name|Override
name|K
name|transform
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|element
parameter_list|)
block|{
return|return
name|element
operator|.
name|getKey
argument_list|()
return|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsKey
argument_list|(
name|target
argument_list|)
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
literal|true
return|;
block|}
block|}
DECL|field|values
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
DECL|method|values ()
annotation|@
name|Override
specifier|public
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|v
init|=
name|values
decl_stmt|;
return|return
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
operator|(
name|values
operator|=
operator|new
name|Values
argument_list|<
name|V
argument_list|>
argument_list|(
name|this
argument_list|)
operator|)
else|:
name|v
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// uses writeReplace(), not default serialization
DECL|class|Values
specifier|private
specifier|static
class|class
name|Values
parameter_list|<
name|V
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
block|{
DECL|field|map
specifier|final
name|RegularImmutableMap
argument_list|<
name|?
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
DECL|method|Values (RegularImmutableMap<?, V> map)
name|Values
parameter_list|(
name|RegularImmutableMap
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
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
operator|.
name|entries
operator|.
name|length
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|UnmodifiableIterator
argument_list|<
name|V
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|AbstractIndexedListIterator
argument_list|<
name|V
argument_list|>
argument_list|(
name|map
operator|.
name|entries
operator|.
name|length
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
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
block|}
return|;
block|}
DECL|method|contains (Object target)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|target
parameter_list|)
block|{
return|return
name|map
operator|.
name|containsValue
argument_list|(
name|target
argument_list|)
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
literal|true
return|;
block|}
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|(
name|size
argument_list|()
operator|*
literal|16
argument_list|)
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
decl_stmt|;
name|Collections2
operator|.
name|STANDARD_JOINER
operator|.
name|appendTo
argument_list|(
name|result
argument_list|,
name|entries
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
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

