begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
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
name|RESTRICTS_ELEMENTS
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
name|SUPPORTS_ADD
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
name|SUPPORTS_REMOVE
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
name|SEVERAL
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
name|GwtIncompatible
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|ConcurrentModificationException
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * Common superclass for {@link MultisetSetCountUnconditionallyTester} and {@link  * MultisetSetCountConditionallyTester}. It is used by those testers to test calls to the  * unconditional {@code setCount()} method and calls to the conditional {@code setCount()} method  * when the expected present count is correct.  *  * @author Chris Povirk  */
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
DECL|class|AbstractMultisetSetCountTester
specifier|public
specifier|abstract
class|class
name|AbstractMultisetSetCountTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
comment|/*    * TODO: consider adding MultisetFeatures.SUPPORTS_SET_COUNT. Currently we    * assume that using setCount() to increase the count is permitted iff add()    * is permitted and similarly for decrease/remove(). We assume that a    * setCount() no-op is permitted if either add() or remove() is permitted,    * though we also allow it to "succeed" if neither is permitted.    */
DECL|method|assertSetCount (E element, int count)
specifier|private
name|void
name|assertSetCount
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|setCountCheckReturnValue
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.count() should return the value passed to setCount()"
argument_list|,
name|count
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|size
init|=
literal|0
decl_stmt|;
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
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|size
operator|+=
name|entry
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"multiset.size() should be the sum of the counts of all entries"
argument_list|,
name|size
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Call the {@code setCount()} method under test, and check its return value. */
DECL|method|setCountCheckReturnValue (E element, int count)
specifier|abstract
name|void
name|setCountCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
function_decl|;
comment|/**    * Call the {@code setCount()} method under test, but do not check its return value. Callers    * should use this method over {@link #setCountCheckReturnValue(Object, int)} when they expect    * {@code setCount()} to throw an exception, as checking the return value could produce an    * incorrect error message like "setCount() should return the original count" instead of the    * message passed to a later invocation of {@code fail()}, like "setCount should throw    * UnsupportedOperationException."    */
DECL|method|setCountNoCheckReturnValue (E element, int count)
specifier|abstract
name|void
name|setCountNoCheckReturnValue
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|assertSetCountIncreasingFailure (E element, int count)
specifier|private
name|void
name|assertSetCountIncreasingFailure
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
try|try
block|{
name|setCountNoCheckReturnValue
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"a call to multiset.setCount() to increase an element's count should throw"
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
DECL|method|assertSetCountDecreasingFailure (E element, int count)
specifier|private
name|void
name|assertSetCountDecreasingFailure
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
try|try
block|{
name|setCountNoCheckReturnValue
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"a call to multiset.setCount() to decrease an element's count should throw"
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
comment|// Unconditional setCount no-ops.
DECL|method|assertZeroToZero ()
specifier|private
name|void
name|assertZeroToZero
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|assertOneToOne ()
specifier|private
name|void
name|assertOneToOne
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|assertThreeToThree ()
specifier|private
name|void
name|assertThreeToThree
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_zeroToZero_addSupported ()
specifier|public
name|void
name|testSetCount_zeroToZero_addSupported
parameter_list|()
block|{
name|assertZeroToZero
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_zeroToZero_removeSupported ()
specifier|public
name|void
name|testSetCount_zeroToZero_removeSupported
parameter_list|()
block|{
name|assertZeroToZero
argument_list|()
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
name|SUPPORTS_ADD
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testSetCount_zeroToZero_unsupported ()
specifier|public
name|void
name|testSetCount_zeroToZero_unsupported
parameter_list|()
block|{
try|try
block|{
name|assertZeroToZero
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_oneToOne_addSupported ()
specifier|public
name|void
name|testSetCount_oneToOne_addSupported
parameter_list|()
block|{
name|assertOneToOne
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_oneToOne_removeSupported ()
specifier|public
name|void
name|testSetCount_oneToOne_removeSupported
parameter_list|()
block|{
name|assertOneToOne
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|SUPPORTS_ADD
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testSetCount_oneToOne_unsupported ()
specifier|public
name|void
name|testSetCount_oneToOne_unsupported
parameter_list|()
block|{
try|try
block|{
name|assertOneToOne
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_threeToThree_addSupported ()
specifier|public
name|void
name|testSetCount_threeToThree_addSupported
parameter_list|()
block|{
name|assertThreeToThree
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_threeToThree_removeSupported ()
specifier|public
name|void
name|testSetCount_threeToThree_removeSupported
parameter_list|()
block|{
name|assertThreeToThree
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|SUPPORTS_ADD
block|,
name|SUPPORTS_REMOVE
block|}
argument_list|)
DECL|method|testSetCount_threeToThree_unsupported ()
specifier|public
name|void
name|testSetCount_threeToThree_unsupported
parameter_list|()
block|{
try|try
block|{
name|assertThreeToThree
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
comment|// Unconditional setCount size increases:
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_zeroToOne_supported ()
specifier|public
name|void
name|testSetCount_zeroToOne_supported
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_ADD
block|,
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|}
argument_list|)
DECL|method|testSetCountZeroToOneConcurrentWithIteration ()
specifier|public
name|void
name|testSetCountZeroToOneConcurrentWithIteration
parameter_list|()
block|{
try|try
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
name|assertSetCount
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_ADD
block|,
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|}
argument_list|)
DECL|method|testSetCountZeroToOneConcurrentWithEntrySetIteration ()
specifier|public
name|void
name|testSetCountZeroToOneConcurrentWithEntrySetIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
init|=
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertSetCount
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_zeroToThree_supported ()
specifier|public
name|void
name|testSetCount_zeroToThree_supported
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_oneToThree_supported ()
specifier|public
name|void
name|testSetCount_oneToThree_supported
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|3
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_zeroToOne_unsupported ()
specifier|public
name|void
name|testSetCount_zeroToOne_unsupported
parameter_list|()
block|{
name|assertSetCountIncreasingFailure
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|1
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_zeroToThree_unsupported ()
specifier|public
name|void
name|testSetCount_zeroToThree_unsupported
parameter_list|()
block|{
name|assertSetCountIncreasingFailure
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSetCount_oneToThree_unsupported ()
specifier|public
name|void
name|testSetCount_oneToThree_unsupported
parameter_list|()
block|{
name|assertSetCountIncreasingFailure
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
comment|// Unconditional setCount size decreases:
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_oneToZero_supported ()
specifier|public
name|void
name|testSetCount_oneToZero_supported
parameter_list|()
block|{
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE
block|,
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|}
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
DECL|method|testSetCountOneToZeroConcurrentWithIteration ()
specifier|public
name|void
name|testSetCountOneToZeroConcurrentWithIteration
parameter_list|()
block|{
try|try
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
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE
block|,
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
block|}
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
DECL|method|testSetCountOneToZeroConcurrentWithEntrySetIteration ()
specifier|public
name|void
name|testSetCountOneToZeroConcurrentWithEntrySetIteration
parameter_list|()
block|{
try|try
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|iterator
init|=
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConcurrentModificationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConcurrentModificationException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_threeToZero_supported ()
specifier|public
name|void
name|testSetCount_threeToZero_supported
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_threeToOne_supported ()
specifier|public
name|void
name|testSetCount_threeToOne_supported
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertSetCount
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_oneToZero_unsupported ()
specifier|public
name|void
name|testSetCount_oneToZero_unsupported
parameter_list|()
block|{
name|assertSetCountDecreasingFailure
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_threeToZero_unsupported ()
specifier|public
name|void
name|testSetCount_threeToZero_unsupported
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertSetCountDecreasingFailure
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_threeToOne_unsupported ()
specifier|public
name|void
name|testSetCount_threeToOne_unsupported
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertSetCountDecreasingFailure
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// setCount with nulls:
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testSetCount_removeNull_nullSupported ()
specifier|public
name|void
name|testSetCount_removeNull_nullSupported
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertSetCount
argument_list|(
literal|null
argument_list|,
literal|0
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
block|{
name|SUPPORTS_ADD
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|,
name|absent
operator|=
name|RESTRICTS_ELEMENTS
argument_list|)
DECL|method|testSetCount_addNull_nullSupported ()
specifier|public
name|void
name|testSetCount_addNull_nullSupported
parameter_list|()
block|{
name|assertSetCount
argument_list|(
literal|null
argument_list|,
literal|1
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
name|SUPPORTS_ADD
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testSetCount_addNull_nullUnsupported ()
specifier|public
name|void
name|testSetCount_addNull_nullUnsupported
parameter_list|()
block|{
try|try
block|{
name|setCountNoCheckReturnValue
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"adding null with setCount() should throw NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testSetCount_noOpNull_nullSupported ()
specifier|public
name|void
name|testSetCount_noOpNull_nullSupported
parameter_list|()
block|{
try|try
block|{
name|assertSetCount
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testSetCount_noOpNull_nullUnsupported ()
specifier|public
name|void
name|testSetCount_noOpNull_nullUnsupported
parameter_list|()
block|{
try|try
block|{
name|assertSetCount
argument_list|(
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
decl||
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testSetCount_existingNoNopNull_nullSupported ()
specifier|public
name|void
name|testSetCount_existingNoNopNull_nullSupported
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
try|try
block|{
name|assertSetCount
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|tolerated
parameter_list|)
block|{     }
block|}
comment|// Negative count.
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_negative_removeSupported ()
specifier|public
name|void
name|testSetCount_negative_removeSupported
parameter_list|()
block|{
try|try
block|{
name|setCountNoCheckReturnValue
argument_list|(
name|e3
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"calling setCount() with a negative count should throw IllegalArgumentException"
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
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSetCount_negative_removeUnsupported ()
specifier|public
name|void
name|testSetCount_negative_removeUnsupported
parameter_list|()
block|{
try|try
block|{
name|setCountNoCheckReturnValue
argument_list|(
name|e3
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"calling setCount() with a negative count should throw "
operator|+
literal|"IllegalArgumentException or UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
comment|// TODO: test adding element of wrong type
comment|/**    * Returns {@link Method} instances for the {@code setCount()} tests that assume multisets support    * duplicates so that the test of {@code Multisets.forSet()} can suppress them.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getSetCountDuplicateInitializingMethods ()
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getSetCountDuplicateInitializingMethods
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|getMethod
argument_list|(
literal|"testSetCount_threeToThree_removeSupported"
argument_list|)
argument_list|,
name|getMethod
argument_list|(
literal|"testSetCount_threeToZero_supported"
argument_list|)
argument_list|,
name|getMethod
argument_list|(
literal|"testSetCount_threeToOne_supported"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getMethod (String methodName)
specifier|private
specifier|static
name|Method
name|getMethod
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|AbstractMultisetSetCountTester
operator|.
name|class
argument_list|,
name|methodName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

