begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|testing
operator|.
name|Helpers
operator|.
name|nefariousMapEntry
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Supplier
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
name|AbstractMap
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
name|LinkedList
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
name|Queue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|RandomAccess
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@code MapConstraints}.  *  * @author Mike Bostock  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MapConstraintsTest
specifier|public
class|class
name|MapConstraintsTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"test"
decl_stmt|;
DECL|field|TEST_VALUE
specifier|private
specifier|static
specifier|final
name|Integer
name|TEST_VALUE
init|=
literal|42
decl_stmt|;
DECL|class|TestKeyException
specifier|static
specifier|final
class|class
name|TestKeyException
extends|extends
name|IllegalArgumentException
block|{
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
DECL|class|TestValueException
specifier|static
specifier|final
class|class
name|TestValueException
extends|extends
name|IllegalArgumentException
block|{
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
DECL|field|TEST_CONSTRAINT
specifier|static
specifier|final
name|MapConstraint
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|TEST_CONSTRAINT
init|=
operator|new
name|TestConstraint
argument_list|()
decl_stmt|;
DECL|class|TestConstraint
specifier|private
specifier|static
specifier|final
class|class
name|TestConstraint
implements|implements
name|MapConstraint
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
implements|,
name|Serializable
block|{
annotation|@
name|Override
DECL|method|checkKeyValue (String key, Integer value)
specifier|public
name|void
name|checkKeyValue
parameter_list|(
name|String
name|key
parameter_list|,
name|Integer
name|value
parameter_list|)
block|{
if|if
condition|(
name|TEST_KEY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TestKeyException
argument_list|()
throw|;
block|}
if|if
condition|(
name|TEST_VALUE
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TestValueException
argument_list|()
throw|;
block|}
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
DECL|method|testConstrainedMapLegal ()
specifier|public
name|void
name|testConstrainedMapLegal
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|constrained
init|=
name|MapConstraints
operator|.
name|constrainedMap
argument_list|(
name|map
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|constrained
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"baz"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|equals
argument_list|(
name|constrained
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|constrained
operator|.
name|equals
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|,
name|constrained
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|keySet
argument_list|()
argument_list|,
name|constrained
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HashMultiset
operator|.
name|create
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|HashMultiset
operator|.
name|create
argument_list|(
name|constrained
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|isNotInstanceOf
argument_list|(
name|Serializable
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|toString
argument_list|()
argument_list|,
name|constrained
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|hashCode
argument_list|()
argument_list|,
name|constrained
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
literal|"baz"
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testConstrainedMapIllegal ()
specifier|public
name|void
name|testConstrainedMapIllegal
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|constrained
init|=
name|MapConstraints
operator|.
name|constrainedMap
argument_list|(
name|map
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|constrained
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestKeyException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestKeyException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|put
argument_list|(
literal|"baz"
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestValueException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestValueException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestKeyException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestKeyException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|constrained
operator|.
name|putAll
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"baz"
argument_list|,
literal|3
argument_list|,
name|TEST_KEY
argument_list|,
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestKeyException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestKeyException
name|expected
parameter_list|)
block|{}
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|emptySet
argument_list|()
argument_list|,
name|constrained
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedTypePreservingList ()
specifier|public
name|void
name|testConstrainedTypePreservingList
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|MapConstraints
operator|.
name|constrainedListMultimap
argument_list|(
name|LinkedListMultimap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|entry
init|=
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|entries
argument_list|()
operator|instanceof
name|Set
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstrainedTypePreservingRandomAccessList ()
specifier|public
name|void
name|testConstrainedTypePreservingRandomAccessList
parameter_list|()
block|{
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|MapConstraints
operator|.
name|constrainedListMultimap
argument_list|(
name|ArrayListMultimap
operator|.
expr|<
name|String
argument_list|,
name|Integer
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|entry
init|=
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|List
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
operator|.
name|entries
argument_list|()
operator|instanceof
name|Set
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
operator|instanceof
name|RandomAccess
argument_list|)
expr_stmt|;
block|}
DECL|class|QueueSupplier
specifier|private
specifier|static
class|class
name|QueueSupplier
implements|implements
name|Supplier
argument_list|<
name|Queue
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Queue
argument_list|<
name|Integer
argument_list|>
name|get
parameter_list|()
block|{
return|return
operator|new
name|LinkedList
argument_list|<
name|Integer
argument_list|>
argument_list|()
return|;
block|}
block|}
DECL|method|testMapEntrySetToArray ()
specifier|public
name|void
name|testMapEntrySetToArray
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|constrained
init|=
name|MapConstraints
operator|.
name|constrainedMap
argument_list|(
name|map
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|constrained
operator|.
name|entrySet
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"TestValueException expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TestValueException
name|expected
parameter_list|)
block|{}
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testMapEntrySetContainsNefariousEntry ()
specifier|public
name|void
name|testMapEntrySetContainsNefariousEntry
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|constrained
init|=
name|MapConstraints
operator|.
name|constrainedMap
argument_list|(
name|map
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nefariousEntry
init|=
name|nefariousMapEntry
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|entries
init|=
name|constrained
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|contains
argument_list|(
name|nefariousEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entries
operator|.
name|containsAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|nefariousEntry
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
name|TEST_VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testNefariousMapPutAll ()
specifier|public
name|void
name|testNefariousMapPutAll
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|constrained
init|=
name|MapConstraints
operator|.
name|constrainedMap
argument_list|(
name|map
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|onceIterable
init|=
name|onceIterableMap
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|constrained
operator|.
name|putAll
argument_list|(
name|onceIterable
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|1
argument_list|,
name|constrained
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a "nefarious" map, which permits only one call to its views'    * iterator() methods. This verifies that the constrained map uses a    * defensive copy instead of potentially checking the elements in one snapshot    * and adding the elements from another.    *    * @param key the key to be contained in the map    * @param value the value to be contained in the map    */
DECL|method|onceIterableMap (K key, V value)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|onceIterableMap
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|AbstractMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
name|boolean
name|iteratorCalled
decl_stmt|;
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
comment|/*          * We could make the map empty, but that seems more likely to trigger          * special cases (so maybe we should test both empty and nonempty...).          */
return|return
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
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
return|return
operator|new
name|ForwardingSet
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
specifier|protected
name|Set
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
name|Collections
operator|.
name|singleton
argument_list|(
name|entry
argument_list|)
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
name|V
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"Expected only one call to iterator()"
argument_list|,
name|iteratorCalled
argument_list|)
expr_stmt|;
name|iteratorCalled
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|K
argument_list|>
name|keySet
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

