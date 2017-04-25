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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Provides static methods for serializing collection classes.  *  *<p>This class assists the implementation of collection classes. Do not use  * this class to serialize collections that are defined elsewhere.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|Serialization
specifier|final
class|class
name|Serialization
block|{
DECL|method|Serialization ()
specifier|private
name|Serialization
parameter_list|()
block|{}
comment|/**    * Reads a count corresponding to a serialized map, multiset, or multimap. It    * returns the size of a map serialized by {@link    * #writeMap(Map, ObjectOutputStream)}, the number of distinct elements in a    * multiset serialized by {@link    * #writeMultiset(Multiset, ObjectOutputStream)}, or the number of distinct    * keys in a multimap serialized by {@link    * #writeMultimap(Multimap, ObjectOutputStream)}.    */
DECL|method|readCount (ObjectInputStream stream)
specifier|static
name|int
name|readCount
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|stream
operator|.
name|readInt
argument_list|()
return|;
block|}
comment|/**    * Stores the contents of a map in an output stream, as part of serialization.    * It does not support concurrent maps whose content may change while the    * method is running.    *    *<p>The serialized output consists of the number of entries, first key,    * first value, second key, second value, and so on.    */
DECL|method|writeMap (Map<K, V> map, ObjectOutputStream stream)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|writeMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|writeInt
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Populates a map by reading an input stream, as part of deserialization.    * See {@link #writeMap} for the data format.    */
DECL|method|populateMap (Map<K, V> map, ObjectInputStream stream)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|populateMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|int
name|size
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|populateMap
argument_list|(
name|map
argument_list|,
name|stream
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**    * Populates a map by reading an input stream, as part of deserialization.    * See {@link #writeMap} for the data format. The size is determined by a    * prior call to {@link #readCount}.    */
DECL|method|populateMap (Map<K, V> map, ObjectInputStream stream, int size)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|populateMap
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|,
name|int
name|size
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
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
comment|// reading data stored by writeMap
name|K
name|key
init|=
operator|(
name|K
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeMap
name|V
name|value
init|=
operator|(
name|V
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Stores the contents of a multiset in an output stream, as part of    * serialization. It does not support concurrent multisets whose content may    * change while the method is running.    *    *<p>The serialized output consists of the number of distinct elements, the    * first element, its count, the second element, its count, and so on.    */
DECL|method|writeMultiset (Multiset<E> multiset, ObjectOutputStream stream)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|writeMultiset
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|,
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|entryCount
init|=
name|multiset
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|stream
operator|.
name|writeInt
argument_list|(
name|entryCount
argument_list|)
expr_stmt|;
for|for
control|(
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
range|:
name|multiset
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|writeInt
argument_list|(
name|entry
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Populates a multiset by reading an input stream, as part of    * deserialization. See {@link #writeMultiset} for the data format.    */
DECL|method|populateMultiset (Multiset<E> multiset, ObjectInputStream stream)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|populateMultiset
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|int
name|distinctElements
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|populateMultiset
argument_list|(
name|multiset
argument_list|,
name|stream
argument_list|,
name|distinctElements
argument_list|)
expr_stmt|;
block|}
comment|/**    * Populates a multiset by reading an input stream, as part of    * deserialization. See {@link #writeMultiset} for the data format. The number    * of distinct elements is determined by a prior call to {@link #readCount}.    */
DECL|method|populateMultiset ( Multiset<E> multiset, ObjectInputStream stream, int distinctElements)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|populateMultiset
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|,
name|int
name|distinctElements
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|distinctElements
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
comment|// reading data stored by writeMultiset
name|E
name|element
init|=
operator|(
name|E
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Stores the contents of a multimap in an output stream, as part of    * serialization. It does not support concurrent multimaps whose content may    * change while the method is running. The {@link Multimap#asMap} view    * determines the ordering in which data is written to the stream.    *    *<p>The serialized output consists of the number of distinct keys, and then    * for each distinct key: the key, the number of values for that key, and the    * key's values.    */
DECL|method|writeMultimap (Multimap<K, V> multimap, ObjectOutputStream stream)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|writeMultimap
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|,
name|ObjectOutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|stream
operator|.
name|writeInt
argument_list|(
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
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
range|:
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|writeInt
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|V
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|stream
operator|.
name|writeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Populates a multimap by reading an input stream, as part of    * deserialization. See {@link #writeMultimap} for the data format.    */
DECL|method|populateMultimap (Multimap<K, V> multimap, ObjectInputStream stream)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|populateMultimap
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|int
name|distinctKeys
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|populateMultimap
argument_list|(
name|multimap
argument_list|,
name|stream
argument_list|,
name|distinctKeys
argument_list|)
expr_stmt|;
block|}
comment|/**    * Populates a multimap by reading an input stream, as part of    * deserialization. See {@link #writeMultimap} for the data format. The number    * of distinct keys is determined by a prior call to {@link #readCount}.    */
DECL|method|populateMultimap ( Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|populateMultimap
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|,
name|ObjectInputStream
name|stream
parameter_list|,
name|int
name|distinctKeys
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|distinctKeys
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
comment|// reading data stored by writeMultimap
name|K
name|key
init|=
operator|(
name|K
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|values
init|=
name|multimap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|valueCount
init|=
name|stream
operator|.
name|readInt
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|valueCount
condition|;
name|j
operator|++
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// reading data stored by writeMultimap
name|V
name|value
init|=
operator|(
name|V
operator|)
name|stream
operator|.
name|readObject
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Secret sauce for setting final fields; don't make it public.
DECL|method|getFieldSetter (final Class<T> clazz, String fieldName)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|FieldSetter
argument_list|<
name|T
argument_list|>
name|getFieldSetter
parameter_list|(
specifier|final
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
try|try
block|{
name|Field
name|field
init|=
name|clazz
operator|.
name|getDeclaredField
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
return|return
operator|new
name|FieldSetter
argument_list|<
name|T
argument_list|>
argument_list|(
name|field
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
comment|// programmer error
block|}
block|}
comment|// Secret sauce for setting final fields; don't make it public.
DECL|class|FieldSetter
specifier|static
specifier|final
class|class
name|FieldSetter
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|field
specifier|private
specifier|final
name|Field
name|field
decl_stmt|;
DECL|method|FieldSetter (Field field)
specifier|private
name|FieldSetter
parameter_list|(
name|Field
name|field
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|set (T instance, Object value)
name|void
name|set
parameter_list|(
name|T
name|instance
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
name|field
operator|.
name|set
argument_list|(
name|instance
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|impossible
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|impossible
argument_list|)
throw|;
block|}
block|}
DECL|method|set (T instance, int value)
name|void
name|set
parameter_list|(
name|T
name|instance
parameter_list|,
name|int
name|value
parameter_list|)
block|{
try|try
block|{
name|field
operator|.
name|set
argument_list|(
name|instance
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|impossible
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|impossible
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit
