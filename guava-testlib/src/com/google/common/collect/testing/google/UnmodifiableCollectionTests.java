begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
operator|.
name|google
package|;
end_package

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|fail
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
name|ArrayListMultimap
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
name|Iterators
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
name|LinkedHashMultiset
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
name|Lists
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
name|Maps
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
name|Multimap
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
name|Multiset
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
name|Set
import|;
end_import

begin_comment
comment|/**  * A series of tests that support asserting that collections cannot be  * modified, either through direct or indirect means.  *  * @author Robert Konigsberg  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|UnmodifiableCollectionTests
specifier|public
class|class
name|UnmodifiableCollectionTests
block|{
DECL|method|assertMapEntryIsUnmodifiable (Entry<?, ?> entry)
specifier|public
specifier|static
name|void
name|assertMapEntryIsUnmodifiable
parameter_list|(
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
parameter_list|)
block|{
try|try
block|{
name|entry
operator|.
name|setValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"setValue on unmodifiable Map.Entry succeeded"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
comment|/**    * Verifies that an Iterator is unmodifiable.    *    *<p>This test only works with iterators that iterate over a finite set.    */
DECL|method|assertIteratorIsUnmodifiable (Iterator<?> iterator)
specifier|public
specifier|static
name|void
name|assertIteratorIsUnmodifiable
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
parameter_list|)
block|{
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
try|try
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Remove on unmodifiable iterator succeeded"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
block|}
block|}
comment|/**    * Asserts that two iterators contain elements in tandem.    *    *<p>This test only works with iterators that iterate over a finite set.    */
DECL|method|assertIteratorsInOrder ( Iterator<?> expectedIterator, Iterator<?> actualIterator)
specifier|public
specifier|static
name|void
name|assertIteratorsInOrder
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|expectedIterator
parameter_list|,
name|Iterator
argument_list|<
name|?
argument_list|>
name|actualIterator
parameter_list|)
block|{
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|expectedIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|expected
init|=
name|expectedIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"index "
operator|+
name|i
operator|+
literal|" expected<"
operator|+
name|expected
operator|+
literal|"., actual is exhausted"
argument_list|,
name|actualIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|actual
init|=
name|actualIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"index "
operator|+
name|i
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|actualIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"index "
operator|+
name|i
operator|+
literal|", expected is exhausted, actual<"
operator|+
name|actualIterator
operator|.
name|next
argument_list|()
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Verifies that a collection is immutable.    *    *<p>A collection is considered immutable if:    *<ol>    *<li>All its mutation methods result in UnsupportedOperationException, and    * do not change the underlying contents.    *<li>All methods that return objects that can indirectly mutate the    * collection throw UnsupportedOperationException when those mutators    * are called.    *</ol>    *    * @param collection the presumed-immutable collection    * @param sampleElement an element of the same type as that contained by    * {@code collection}. {@code collection} may or may not have {@code    * sampleElement} as a member.    */
DECL|method|assertCollectionIsUnmodifiable (Collection<E> collection, E sampleElement)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|assertCollectionIsUnmodifiable
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|collection
parameter_list|,
name|E
name|sampleElement
parameter_list|)
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|siblingCollection
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|siblingCollection
operator|.
name|add
argument_list|(
name|sampleElement
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|E
argument_list|>
name|copy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Avoid copy.addAll(collection), which runs afoul of an Android bug in older versions:
comment|// http://b.android.com/72073 http://r.android.com/98929
name|Iterators
operator|.
name|addAll
argument_list|(
name|copy
argument_list|,
name|collection
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|add
argument_list|(
name|sampleElement
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|addAll
argument_list|(
name|siblingCollection
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"clear succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
name|assertIteratorIsUnmodifiable
argument_list|(
name|collection
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|remove
argument_list|(
name|sampleElement
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|removeAll
argument_list|(
name|siblingCollection
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"removeAll succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
try|try
block|{
name|collection
operator|.
name|retainAll
argument_list|(
name|siblingCollection
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"retainAll succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|copy
argument_list|,
name|collection
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that a set is immutable.    *    *<p>A set is considered immutable if:    *<ol>    *<li>All its mutation methods result in UnsupportedOperationException, and    * do not change the underlying contents.    *<li>All methods that return objects that can indirectly mutate the    * set throw UnsupportedOperationException when those mutators    * are called.    *</ol>    *    * @param set the presumed-immutable set    * @param sampleElement an element of the same type as that contained by    * {@code set}. {@code set} may or may not have {@code sampleElement} as a    * member.    */
DECL|method|assertSetIsUnmodifiable (Set<E> set, E sampleElement)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|assertSetIsUnmodifiable
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|set
parameter_list|,
name|E
name|sampleElement
parameter_list|)
block|{
name|assertCollectionIsUnmodifiable
argument_list|(
name|set
argument_list|,
name|sampleElement
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that a multiset is immutable.    *    *<p>A multiset is considered immutable if:    *<ol>    *<li>All its mutation methods result in UnsupportedOperationException, and    * do not change the underlying contents.    *<li>All methods that return objects that can indirectly mutate the    * multiset throw UnsupportedOperationException when those mutators    * are called.    *</ol>    *    * @param multiset the presumed-immutable multiset    * @param sampleElement an element of the same type as that contained by    * {@code multiset}. {@code multiset} may or may not have {@code    * sampleElement} as a member.    */
DECL|method|assertMultisetIsUnmodifiable (Multiset<E> multiset, final E sampleElement)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|assertMultisetIsUnmodifiable
parameter_list|(
name|Multiset
argument_list|<
name|E
argument_list|>
name|multiset
parameter_list|,
specifier|final
name|E
name|sampleElement
parameter_list|)
block|{
name|Multiset
argument_list|<
name|E
argument_list|>
name|copy
init|=
name|LinkedHashMultiset
operator|.
name|create
argument_list|(
name|multiset
argument_list|)
decl_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
comment|// Multiset is a collection, so we can use all those tests.
name|assertCollectionIsUnmodifiable
argument_list|(
name|multiset
argument_list|,
name|sampleElement
argument_list|)
expr_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
try|try
block|{
name|multiset
operator|.
name|add
argument_list|(
name|sampleElement
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"add(Object, int) succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
try|try
block|{
name|multiset
operator|.
name|remove
argument_list|(
name|sampleElement
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove(Object, int) succeeded on unmodifiable collection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertSetIsUnmodifiable
argument_list|(
name|multiset
operator|.
name|elementSet
argument_list|()
argument_list|,
name|sampleElement
argument_list|)
expr_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertSetIsUnmodifiable
argument_list|(
name|multiset
operator|.
name|entrySet
argument_list|()
argument_list|,
operator|new
name|Multiset
operator|.
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|getCount
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|E
name|getElement
parameter_list|()
block|{
return|return
name|sampleElement
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertCollectionsAreEquivalent
argument_list|(
name|multiset
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
comment|/**    * Verifies that a multimap is immutable.    *    *<p>A multimap is considered immutable if:    *<ol>    *<li>All its mutation methods result in UnsupportedOperationException, and    * do not change the underlying contents.    *<li>All methods that return objects that can indirectly mutate the    * multimap throw UnsupportedOperationException when those mutators    *</ol>    *    * @param multimap the presumed-immutable multimap    * @param sampleKey a key of the same type as that contained by    * {@code multimap}. {@code multimap} may or may not have {@code sampleKey} as    * a key.    * @param sampleValue a key of the same type as that contained by    * {@code multimap}. {@code multimap} may or may not have {@code sampleValue}    * as a key.    */
DECL|method|assertMultimapIsUnmodifiable ( Multimap<K, V> multimap, final K sampleKey, final V sampleValue)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|assertMultimapIsUnmodifiable
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap
parameter_list|,
specifier|final
name|K
name|sampleKey
parameter_list|,
specifier|final
name|V
name|sampleValue
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
name|originalEntries
init|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|multimap
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|V
argument_list|>
name|sampleValueAsCollection
init|=
name|Collections
operator|.
name|singleton
argument_list|(
name|sampleValue
argument_list|)
decl_stmt|;
comment|// Test #clear()
try|try
block|{
name|multimap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"clear succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test asMap().entrySet()
name|assertSetIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|sampleKey
argument_list|,
name|sampleValueAsCollection
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test #values()
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|multimap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|V
argument_list|>
name|values
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
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertCollectionIsUnmodifiable
argument_list|(
name|values
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
block|}
comment|// Test #entries()
name|assertCollectionIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|entries
argument_list|()
argument_list|,
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|sampleKey
argument_list|,
name|sampleValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Iterate over every element in the entry set
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
name|multimap
operator|.
name|entries
argument_list|()
control|)
block|{
name|assertMapEntryIsUnmodifiable
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #keys()
name|assertMultisetIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|keys
argument_list|()
argument_list|,
name|sampleKey
argument_list|)
expr_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #keySet()
name|assertSetIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|keySet
argument_list|()
argument_list|,
name|sampleKey
argument_list|)
expr_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #get()
if|if
condition|(
operator|!
name|multimap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|K
name|key
init|=
name|multimap
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
name|assertCollectionIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
block|}
comment|// Test #put()
try|try
block|{
name|multimap
operator|.
name|put
argument_list|(
name|sampleKey
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"put succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #putAll(K, Collection<V>)
try|try
block|{
name|multimap
operator|.
name|putAll
argument_list|(
name|sampleKey
argument_list|,
name|sampleValueAsCollection
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(K, Iterable) succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #putAll(Multimap<K, V>)
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|multimap2
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|multimap2
operator|.
name|put
argument_list|(
name|sampleKey
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
try|try
block|{
name|multimap
operator|.
name|putAll
argument_list|(
name|multimap2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"putAll(Multimap<K, V>) succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #remove()
try|try
block|{
name|multimap
operator|.
name|remove
argument_list|(
name|sampleKey
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"remove succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #removeAll()
try|try
block|{
name|multimap
operator|.
name|removeAll
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"removeAll succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #replaceValues()
try|try
block|{
name|multimap
operator|.
name|replaceValues
argument_list|(
name|sampleKey
argument_list|,
name|sampleValueAsCollection
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"replaceValues succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
comment|// Test #asMap()
try|try
block|{
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|remove
argument_list|(
name|sampleKey
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().remove() succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|multimap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|K
name|presentKey
init|=
name|multimap
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
try|try
block|{
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|presentKey
argument_list|)
operator|.
name|remove
argument_list|(
name|sampleValue
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().get().remove() succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
try|try
block|{
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|remove
argument_list|(
name|sampleValue
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().values().iterator().next().remove() succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
try|try
block|{
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|multimap
operator|.
name|asMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
operator|)
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"asMap().values().toArray()[0].clear() succeeded on unmodifiable multimap"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{       }
block|}
name|assertCollectionIsUnmodifiable
argument_list|(
name|multimap
operator|.
name|values
argument_list|()
argument_list|,
name|sampleValue
argument_list|)
expr_stmt|;
name|assertMultimapRemainsUnmodified
argument_list|(
name|multimap
argument_list|,
name|originalEntries
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCollectionsAreEquivalent ( Collection<E> expected, Collection<E> actual)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|assertCollectionsAreEquivalent
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|expected
parameter_list|,
name|Collection
argument_list|<
name|E
argument_list|>
name|actual
parameter_list|)
block|{
name|assertIteratorsInOrder
argument_list|(
name|expected
operator|.
name|iterator
argument_list|()
argument_list|,
name|actual
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMultimapRemainsUnmodified ( Multimap<K, V> expected, List<Entry<K, V>> actual)
specifier|private
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|void
name|assertMultimapRemainsUnmodified
parameter_list|(
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|expected
parameter_list|,
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|actual
parameter_list|)
block|{
name|assertIteratorsInOrder
argument_list|(
name|expected
operator|.
name|entries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|actual
operator|.
name|iterator
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

