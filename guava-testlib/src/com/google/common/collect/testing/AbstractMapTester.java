begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|GwtCompatible
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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

begin_comment
comment|/**  * Base class for map testers.  *  * TODO: see how much of this is actually needed once Map testers are written.  * (It was cloned from AbstractCollectionTester.)  *  * @param<K> the key type of the map to be tested.  * @param<V> the value type of the map to be tested.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMapTester
specifier|public
specifier|abstract
class|class
name|AbstractMapTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractContainerTester
argument_list|<
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|,
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|method|getMap ()
specifier|protected
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getMap
parameter_list|()
block|{
return|return
name|container
return|;
block|}
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|samples
operator|=
name|this
operator|.
name|getSubjectGenerator
argument_list|()
operator|.
name|samples
argument_list|()
expr_stmt|;
name|resetMap
argument_list|()
expr_stmt|;
block|}
DECL|method|actualContents ()
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|actualContents
parameter_list|()
block|{
return|return
name|getMap
argument_list|()
operator|.
name|entrySet
argument_list|()
return|;
block|}
comment|/** @see AbstractContainerTester#resetContainer() */
DECL|method|resetMap ()
specifier|protected
name|void
name|resetMap
parameter_list|()
block|{
name|resetContainer
argument_list|()
expr_stmt|;
block|}
DECL|method|expectMissingKeys (K... elements)
specifier|protected
name|void
name|expectMissingKeys
parameter_list|(
name|K
modifier|...
name|elements
parameter_list|)
block|{
for|for
control|(
name|K
name|element
range|:
name|elements
control|)
block|{
name|assertFalse
argument_list|(
literal|"Should not contain key "
operator|+
name|element
argument_list|,
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|expectMissingValues (V... elements)
specifier|protected
name|void
name|expectMissingValues
parameter_list|(
name|V
modifier|...
name|elements
parameter_list|)
block|{
for|for
control|(
name|V
name|element
range|:
name|elements
control|)
block|{
name|assertFalse
argument_list|(
literal|"Should not contain value "
operator|+
name|element
argument_list|,
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * @return an array of the proper size with {@code null} as the key of the    * middle element.    */
DECL|method|createArrayWithNullKey ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArrayWithNullKey
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nullKeyLocation
init|=
name|getNullLocation
argument_list|()
decl_stmt|;
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|oldEntry
init|=
name|array
index|[
name|nullKeyLocation
index|]
decl_stmt|;
name|array
index|[
name|nullKeyLocation
index|]
operator|=
name|entry
argument_list|(
literal|null
argument_list|,
name|oldEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|getValueForNullKey ()
specifier|protected
name|V
name|getValueForNullKey
parameter_list|()
block|{
return|return
name|getEntryNullReplaces
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getKeyForNullValue ()
specifier|protected
name|K
name|getKeyForNullValue
parameter_list|()
block|{
return|return
name|getEntryNullReplaces
argument_list|()
operator|.
name|getKey
argument_list|()
return|;
block|}
DECL|method|getEntryNullReplaces ()
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getEntryNullReplaces
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
init|=
name|getSampleElements
argument_list|()
operator|.
name|iterator
argument_list|()
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
name|getNullLocation
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|entries
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**    * @return an array of the proper size with {@code null} as the value of the    * middle element.    */
DECL|method|createArrayWithNullValue ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArrayWithNullValue
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nullValueLocation
init|=
name|getNullLocation
argument_list|()
decl_stmt|;
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|oldEntry
init|=
name|array
index|[
name|nullValueLocation
index|]
decl_stmt|;
name|array
index|[
name|nullValueLocation
index|]
operator|=
name|entry
argument_list|(
name|oldEntry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|initMapWithNullKey ()
specifier|protected
name|void
name|initMapWithNullKey
parameter_list|()
block|{
name|resetMap
argument_list|(
name|createArrayWithNullKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initMapWithNullValue ()
specifier|protected
name|void
name|initMapWithNullValue
parameter_list|()
block|{
name|resetMap
argument_list|(
name|createArrayWithNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Equivalent to {@link #expectMissingKeys(Object[]) expectMissingKeys}    * {@code (null)}    * except that the call to {@code contains(null)} is permitted to throw a    * {@code NullPointerException}.    * @param message message to use upon assertion failure    */
DECL|method|expectNullKeyMissingWhenNullKeysUnsupported (String message)
specifier|protected
name|void
name|expectNullKeyMissingWhenNullKeysUnsupported
parameter_list|(
name|String
name|message
parameter_list|)
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|message
argument_list|,
name|getMap
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|tolerated
parameter_list|)
block|{
comment|// Tolerated
block|}
block|}
comment|/**    * Equivalent to {@link #expectMissingValues(Object[]) expectMissingValues}    * {@code (null)}    * except that the call to {@code contains(null)} is permitted to throw a    * {@code NullPointerException}.    * @param message message to use upon assertion failure    */
DECL|method|expectNullValueMissingWhenNullValuesUnsupported ( String message)
specifier|protected
name|void
name|expectNullValueMissingWhenNullValuesUnsupported
parameter_list|(
name|String
name|message
parameter_list|)
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|message
argument_list|,
name|getMap
argument_list|()
operator|.
name|containsValue
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|tolerated
parameter_list|)
block|{
comment|// Tolerated
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|protected
name|MinimalCollection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
DECL|method|createDisjointCollection ()
name|createDisjointCollection
parameter_list|()
block|{
return|return
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e3
argument_list|()
argument_list|,
name|samples
operator|.
name|e4
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getNumEntries ()
specifier|protected
name|int
name|getNumEntries
parameter_list|()
block|{
return|return
name|getNumElements
argument_list|()
return|;
block|}
DECL|method|getSampleEntries (int howMany)
specifier|protected
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|getSampleEntries
parameter_list|(
name|int
name|howMany
parameter_list|)
block|{
return|return
name|getSampleElements
argument_list|(
name|howMany
argument_list|)
return|;
block|}
DECL|method|getSampleEntries ()
specifier|protected
name|Collection
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|getSampleEntries
parameter_list|()
block|{
return|return
name|getSampleElements
argument_list|()
return|;
block|}
DECL|method|expectMissing (Entry<K, V>.... entries)
annotation|@
name|Override
specifier|protected
name|void
name|expectMissing
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
name|assertFalse
argument_list|(
literal|"Should not contain entry "
operator|+
name|entry
argument_list|,
name|actualContents
argument_list|()
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not contain key "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" mapped to"
operator|+
literal|" value "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|equal
argument_list|(
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|equal (Object a, Object b)
specifier|private
specifier|static
name|boolean
name|equal
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
operator|||
operator|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
operator|)
return|;
block|}
comment|// This one-liner saves us from some ugly casts
DECL|method|entry (K key, V value)
specifier|protected
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|expectContents (Collection<Entry<K, V>> expected)
annotation|@
name|Override
specifier|protected
name|void
name|expectContents
parameter_list|(
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expected
parameter_list|)
block|{
comment|// TODO: move this to invariant checks once the appropriate hook exists?
name|super
operator|.
name|expectContents
argument_list|(
name|expected
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
name|expected
control|)
block|{
name|assertEquals
argument_list|(
literal|"Wrong value for key "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|expectReplacement (Entry<K, V> newEntry)
specifier|protected
specifier|final
name|void
name|expectReplacement
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
parameter_list|)
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|replaceValue
argument_list|(
name|expected
argument_list|,
name|newEntry
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|replaceValue (List<Entry<K, V>> expected, Entry<K, V> newEntry)
specifier|private
name|void
name|replaceValue
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|expected
parameter_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newEntry
parameter_list|)
block|{
for|for
control|(
name|ListIterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|i
init|=
name|expected
operator|.
name|listIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|Helpers
operator|.
name|equal
argument_list|(
name|i
operator|.
name|next
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|,
name|newEntry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|i
operator|.
name|set
argument_list|(
name|newEntry
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"key %s not found in entries %s"
argument_list|,
name|newEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|expected
argument_list|)
argument_list|)
throw|;
block|}
comment|/**    * Wrapper for {@link Map#get(Object)} that forces the caller to pass in a key    * of the same type as the map. Besides being slightly shorter than code that    * uses {@link #getMap()}, it also ensures that callers don't pass an    * {@link Entry} by mistake.    */
DECL|method|get (K key)
specifier|protected
name|V
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|getMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|resetMap (Entry<K, V>[] entries)
specifier|protected
name|void
name|resetMap
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entries
parameter_list|)
block|{
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|entries
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

