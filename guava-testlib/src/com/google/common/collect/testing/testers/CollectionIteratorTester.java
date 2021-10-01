begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
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
name|mapEntry
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
name|testing
operator|.
name|IteratorFeature
operator|.
name|MODIFIABLE
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
name|testing
operator|.
name|IteratorFeature
operator|.
name|UNMODIFIABLE
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
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|KNOWN_ORDER
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
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
operator|.
name|SUPPORTS_ITERATOR_REMOVE
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
name|testing
operator|.
name|features
operator|.
name|CollectionSize
operator|.
name|ZERO
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|testing
operator|.
name|AbstractCollectionTester
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
name|testing
operator|.
name|Helpers
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
name|testing
operator|.
name|IteratorFeature
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
name|testing
operator|.
name|IteratorTester
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
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
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
name|testing
operator|.
name|features
operator|.
name|CollectionSize
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
name|Arrays
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code iterator} operations on a collection. Can't be invoked  * directly; please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|CollectionIteratorTester
specifier|public
class|class
name|CollectionIteratorTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testIterator ()
specifier|public
name|void
name|testIterator
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|iteratorElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|collection
control|)
block|{
comment|// uses iterator()
name|iteratorElements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|)
argument_list|,
name|iteratorElements
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testIterationOrdering ()
specifier|public
name|void
name|testIterationOrdering
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|iteratorElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|collection
control|)
block|{
comment|// uses iterator()
name|iteratorElements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Different ordered iteration"
argument_list|,
name|expected
argument_list|,
name|iteratorElements
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testIterator_nullElement ()
specifier|public
name|void
name|testIterator_nullElement
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|iteratorElements
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|collection
control|)
block|{
comment|// uses iterator()
name|iteratorElements
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|asList
argument_list|(
name|createArrayWithNullElement
argument_list|()
argument_list|)
argument_list|,
name|iteratorElements
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testIterator_removeAffectsBackingCollection ()
specifier|public
name|void
name|testIterator_removeAffectsBackingCollection
parameter_list|()
block|{
name|int
name|originalSize
init|=
name|collection
operator|.
name|size
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Object
name|element
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// If it's an Entry, it may become invalid once it's removed from the Map. Copy it.
if|if
condition|(
name|element
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
name|element
decl_stmt|;
name|element
operator|=
name|mapEntry
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
name|assertTrue
argument_list|(
name|collection
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
comment|// sanity check
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|collection
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalSize
operator|-
literal|1
argument_list|,
name|collection
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|KNOWN_ORDER
block|,
name|SUPPORTS_ITERATOR_REMOVE
block|}
argument_list|)
DECL|method|testIterator_knownOrderRemoveSupported ()
specifier|public
name|void
name|testIterator_knownOrderRemoveSupported
parameter_list|()
block|{
name|runIteratorTest
argument_list|(
name|MODIFIABLE
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|,
name|getOrderedElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|KNOWN_ORDER
argument_list|,
name|absent
operator|=
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testIterator_knownOrderRemoveUnsupported ()
specifier|public
name|void
name|testIterator_knownOrderRemoveUnsupported
parameter_list|()
block|{
name|runIteratorTest
argument_list|(
name|UNMODIFIABLE
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|,
name|getOrderedElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|KNOWN_ORDER
argument_list|,
name|value
operator|=
name|SUPPORTS_ITERATOR_REMOVE
argument_list|)
DECL|method|testIterator_unknownOrderRemoveSupported ()
specifier|public
name|void
name|testIterator_unknownOrderRemoveSupported
parameter_list|()
block|{
name|runIteratorTest
argument_list|(
name|MODIFIABLE
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|,
name|getSampleElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|KNOWN_ORDER
block|,
name|SUPPORTS_ITERATOR_REMOVE
block|}
argument_list|)
DECL|method|testIterator_unknownOrderRemoveUnsupported ()
specifier|public
name|void
name|testIterator_unknownOrderRemoveUnsupported
parameter_list|()
block|{
name|runIteratorTest
argument_list|(
name|UNMODIFIABLE
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|,
name|getSampleElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|runIteratorTest ( Set<IteratorFeature> features, IteratorTester.KnownOrder knownOrder, Iterable<E> elements)
specifier|private
name|void
name|runIteratorTest
parameter_list|(
name|Set
argument_list|<
name|IteratorFeature
argument_list|>
name|features
parameter_list|,
name|IteratorTester
operator|.
name|KnownOrder
name|knownOrder
parameter_list|,
name|Iterable
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
operator|new
name|IteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
name|Platform
operator|.
name|collectionIteratorTesterNumIterations
argument_list|()
argument_list|,
name|features
argument_list|,
name|elements
argument_list|,
name|knownOrder
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|resetCollection
argument_list|()
expr_stmt|;
return|return
name|collection
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|verify
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|expectContents
argument_list|(
name|elements
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testIteratorNoSuchElementException ()
specifier|public
name|void
name|testIteratorNoSuchElementException
parameter_list|()
block|{
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
init|=
name|collection
operator|.
name|iterator
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
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"iterator.next() should throw NoSuchElementException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

