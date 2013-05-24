begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|Comparator
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
name|NoSuchElementException
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
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_comment
comment|/**  * Tests representing the contract of {@link SortedMap}. Concrete subclasses of  * this base class test conformance of concrete {@link SortedMap} subclasses to  * that contract.  *  * @author Jared Levy  */
end_comment

begin_comment
comment|// TODO: Use this class to test classes besides ImmutableSortedMap.
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SortedMapInterfaceTest
specifier|public
specifier|abstract
class|class
name|SortedMapInterfaceTest
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MapInterfaceTest
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** A key type that is not assignable to any classes but Object. */
DECL|class|IncompatibleComparableKeyType
specifier|private
specifier|static
specifier|final
class|class
name|IncompatibleComparableKeyType
implements|implements
name|Comparable
argument_list|<
name|IncompatibleComparableKeyType
argument_list|>
block|{
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IncompatibleComparableKeyType"
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (IncompatibleComparableKeyType o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|IncompatibleComparableKeyType
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|()
throw|;
block|}
block|}
DECL|method|SortedMapInterfaceTest (boolean allowsNullKeys, boolean allowsNullValues, boolean supportsPut, boolean supportsRemove, boolean supportsClear)
specifier|protected
name|SortedMapInterfaceTest
parameter_list|(
name|boolean
name|allowsNullKeys
parameter_list|,
name|boolean
name|allowsNullValues
parameter_list|,
name|boolean
name|supportsPut
parameter_list|,
name|boolean
name|supportsRemove
parameter_list|,
name|boolean
name|supportsClear
parameter_list|)
block|{
name|super
argument_list|(
name|allowsNullKeys
argument_list|,
name|allowsNullValues
argument_list|,
name|supportsPut
argument_list|,
name|supportsRemove
argument_list|,
name|supportsClear
argument_list|)
expr_stmt|;
block|}
DECL|method|makeEmptyMap ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeEmptyMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
function_decl|;
DECL|method|makePopulatedMap ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makePopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
function_decl|;
DECL|method|makeEitherMap ()
annotation|@
name|Override
specifier|protected
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeEitherMap
parameter_list|()
block|{
try|try
block|{
return|return
name|makePopulatedMap
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return
name|makeEmptyMap
argument_list|()
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Needed for null comparator
DECL|method|testOrdering ()
specifier|public
name|void
name|testOrdering
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|K
name|prior
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|comparator
init|=
name|map
operator|.
name|comparator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|K
name|current
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|comparator
operator|==
literal|null
condition|)
block|{
name|Comparable
name|comparable
init|=
operator|(
name|Comparable
operator|)
name|prior
decl_stmt|;
name|assertTrue
argument_list|(
name|comparable
operator|.
name|compareTo
argument_list|(
name|current
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|map
operator|.
name|comparator
argument_list|()
operator|.
name|compare
argument_list|(
name|prior
argument_list|,
name|current
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
name|current
operator|=
name|prior
expr_stmt|;
block|}
block|}
DECL|method|testEntrySetContainsEntryIncompatibleComparableKey ()
specifier|public
name|void
name|testEntrySetContainsEntryIncompatibleComparableKey
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
specifier|final
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
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEitherMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|entrySet
operator|=
name|map
operator|.
name|entrySet
argument_list|()
expr_stmt|;
specifier|final
name|V
name|unmappedValue
decl_stmt|;
try|try
block|{
name|unmappedValue
operator|=
name|getValueNotInPopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|Entry
argument_list|<
name|IncompatibleComparableKeyType
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|mapEntry
argument_list|(
operator|new
name|IncompatibleComparableKeyType
argument_list|()
argument_list|,
name|unmappedValue
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|entrySet
operator|.
name|contains
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstKeyEmpty ()
specifier|public
name|void
name|testFirstKeyEmpty
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEmptyMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
try|try
block|{
name|map
operator|.
name|firstKey
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NoSuchElementException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{}
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstKeyNonEmpty ()
specifier|public
name|void
name|testFirstKeyNonEmpty
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|K
name|expected
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|map
operator|.
name|firstKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastKeyEmpty ()
specifier|public
name|void
name|testLastKeyEmpty
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEmptyMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
try|try
block|{
name|map
operator|.
name|lastKey
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NoSuchElementException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{}
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|testLastKeyNonEmpty ()
specifier|public
name|void
name|testLastKeyNonEmpty
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|K
name|expected
init|=
literal|null
decl_stmt|;
for|for
control|(
name|K
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|expected
operator|=
name|key
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|map
operator|.
name|lastKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
DECL|method|toList (Collection<E> collection)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|toList
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|subListSnapshot ( List<E> list, int fromIndex, int toIndex)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|subListSnapshot
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|subList
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|;
name|i
operator|<
name|toIndex
condition|;
name|i
operator|++
control|)
block|{
name|subList
operator|.
name|add
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|subList
argument_list|)
return|;
block|}
DECL|method|testHeadMap ()
specifier|public
name|void
name|testHeadMap
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEitherMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|list
init|=
name|toList
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
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
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
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
name|subListSnapshot
argument_list|(
name|list
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|headMap
init|=
name|map
operator|.
name|headMap
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|toList
argument_list|(
name|headMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testTailMap ()
specifier|public
name|void
name|testTailMap
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEitherMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|list
init|=
name|toList
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
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
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
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
name|subListSnapshot
argument_list|(
name|list
argument_list|,
name|i
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tailMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|toList
argument_list|(
name|tailMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSubMap ()
specifier|public
name|void
name|testSubMap
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makeEitherMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|list
init|=
name|toList
argument_list|(
name|map
operator|.
name|entrySet
argument_list|()
argument_list|)
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
name|list
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
name|i
init|;
name|j
operator|<
name|list
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
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
name|subListSnapshot
argument_list|(
name|list
argument_list|,
name|i
argument_list|,
name|j
argument_list|)
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|subMap
argument_list|(
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|j
argument_list|)
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|toList
argument_list|(
name|subMap
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|testSubMapIllegal ()
specifier|public
name|void
name|testSubMapIllegal
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
init|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|K
name|first
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|second
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|map
operator|.
name|subMap
argument_list|(
name|second
argument_list|,
name|first
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testTailMapEntrySet ()
specifier|public
name|void
name|testTailMapEntrySet
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|3
condition|)
block|{
return|return;
block|}
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
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|thirdEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|tail
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|secondEntry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|tailEntrySet
init|=
name|tail
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|tailEntrySet
operator|.
name|contains
argument_list|(
name|thirdEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|tailEntrySet
operator|.
name|contains
argument_list|(
name|secondEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|tailEntrySet
operator|.
name|contains
argument_list|(
name|firstEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tail
operator|.
name|firstKey
argument_list|()
argument_list|,
name|secondEntry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHeadMapEntrySet ()
specifier|public
name|void
name|testHeadMapEntrySet
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|3
condition|)
block|{
return|return;
block|}
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
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|thirdEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|head
init|=
name|map
operator|.
name|headMap
argument_list|(
name|secondEntry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|headEntrySet
init|=
name|head
operator|.
name|entrySet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|headEntrySet
operator|.
name|contains
argument_list|(
name|thirdEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|headEntrySet
operator|.
name|contains
argument_list|(
name|secondEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|headEntrySet
operator|.
name|contains
argument_list|(
name|firstEntry
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|head
operator|.
name|firstKey
argument_list|()
argument_list|,
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|head
operator|.
name|lastKey
argument_list|()
argument_list|,
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMapWriteThrough ()
specifier|public
name|void
name|testTailMapWriteThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsPut
condition|)
block|{
return|return;
block|}
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
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|V
name|value
init|=
name|getValueNotInPopulatedMap
argument_list|()
decl_stmt|;
name|subMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|secondEntry
operator|.
name|getValue
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
try|try
block|{
name|subMap
operator|.
name|put
argument_list|(
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testTailMapRemoveThrough ()
specifier|public
name|void
name|testTailMapRemoveThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|int
name|oldSize
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsRemove
condition|)
block|{
return|return;
block|}
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
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|subMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|subMap
operator|.
name|remove
argument_list|(
name|firstEntry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|subMap
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
DECL|method|testTailMapClearThrough ()
specifier|public
name|void
name|testTailMapClearThrough
parameter_list|()
block|{
specifier|final
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|map
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|int
name|oldSize
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|<
literal|2
operator|||
operator|!
name|supportsClear
condition|)
block|{
return|return;
block|}
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
init|=
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|firstEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|secondEntry
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|K
name|key
init|=
name|secondEntry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|subMap
init|=
name|map
operator|.
name|tailMap
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|int
name|subMapSize
init|=
name|subMap
operator|.
name|size
argument_list|()
decl_stmt|;
name|subMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|,
name|oldSize
operator|-
name|subMapSize
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|subMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

