begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ONE
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
name|ArrayList
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
name|NavigableSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests operations on a NavigableSet. Can't be  * invoked directly; please see {@code NavigableSetTestSuiteBuilder}.  *  * @author Jesse Wilson  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|NavigableSetNavigationTester
specifier|public
class|class
name|NavigableSetNavigationTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|field|navigableSet
specifier|private
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|navigableSet
decl_stmt|;
DECL|field|values
specifier|private
name|List
argument_list|<
name|E
argument_list|>
name|values
decl_stmt|;
DECL|field|a
specifier|private
name|E
name|a
decl_stmt|;
DECL|field|b
specifier|private
name|E
name|b
decl_stmt|;
DECL|field|c
specifier|private
name|E
name|c
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
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
name|navigableSet
operator|=
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|getSet
argument_list|()
expr_stmt|;
name|values
operator|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|getSampleElements
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|getCollectionSize
argument_list|()
operator|.
name|getNumElements
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|values
argument_list|,
name|navigableSet
operator|.
name|comparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// some tests assume SEVERAL == 3
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|a
operator|=
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
name|b
operator|=
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|c
operator|=
name|values
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Resets the contents of navigableSet to have elements a, c, for the    * navigation tests.    */
DECL|method|resetWithHole ()
specifier|protected
name|void
name|resetWithHole
parameter_list|()
block|{
name|super
operator|.
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|a
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|navigableSet
operator|=
operator|(
name|NavigableSet
argument_list|<
name|E
argument_list|>
operator|)
name|getSet
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptySetPollFirst ()
specifier|public
name|void
name|testEmptySetPollFirst
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|pollFirst
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptySetNearby ()
specifier|public
name|void
name|testEmptySetNearby
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|lower
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|floor
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|higher
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptySetPollLast ()
specifier|public
name|void
name|testEmptySetPollLast
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|pollLast
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonSetPollFirst ()
specifier|public
name|void
name|testSingletonSetPollFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|pollFirst
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|navigableSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonSetNearby ()
specifier|public
name|void
name|testSingletonSetNearby
parameter_list|()
block|{
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|lower
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|navigableSet
operator|.
name|higher
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonSetPollLast ()
specifier|public
name|void
name|testSingletonSetPollLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|pollLast
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|navigableSet
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPollFirst ()
specifier|public
name|void
name|testPollFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|pollFirst
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|size
argument_list|()
argument_list|)
argument_list|,
name|Helpers
operator|.
name|copyToList
argument_list|(
name|navigableSet
argument_list|)
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testPollFirstUnsupported ()
specifier|public
name|void
name|testPollFirstUnsupported
parameter_list|()
block|{
try|try
block|{
name|navigableSet
operator|.
name|pollFirst
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
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
DECL|method|testLowerHole ()
specifier|public
name|void
name|testLowerHole
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|c
argument_list|)
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
DECL|method|testFloorHole ()
specifier|public
name|void
name|testFloorHole
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|c
argument_list|)
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
DECL|method|testCeilingHole ()
specifier|public
name|void
name|testCeilingHole
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|c
argument_list|)
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
DECL|method|testHigherHole ()
specifier|public
name|void
name|testHigherHole
parameter_list|()
block|{
name|resetWithHole
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*    * TODO(cpovirk): make "too small" and "too large" elements available for better navigation    * testing. At that point, we may be able to eliminate the "hole" tests, which would mean that    * ContiguousSet's tests would no longer need to suppress them.    */
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testLower ()
specifier|public
name|void
name|testLower
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|navigableSet
operator|.
name|lower
argument_list|(
name|c
argument_list|)
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
DECL|method|testFloor ()
specifier|public
name|void
name|testFloor
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|floor
argument_list|(
name|c
argument_list|)
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
DECL|method|testCeiling ()
specifier|public
name|void
name|testCeiling
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|b
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|ceiling
argument_list|(
name|c
argument_list|)
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
DECL|method|testHigher ()
specifier|public
name|void
name|testHigher
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|b
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|navigableSet
operator|.
name|higher
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPollLast ()
specifier|public
name|void
name|testPollLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|c
argument_list|,
name|navigableSet
operator|.
name|pollLast
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|values
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|,
name|Helpers
operator|.
name|copyToList
argument_list|(
name|navigableSet
argument_list|)
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testPollLastUnsupported ()
specifier|public
name|void
name|testPollLastUnsupported
parameter_list|()
block|{
try|try
block|{
name|navigableSet
operator|.
name|pollLast
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
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
DECL|method|testDescendingNavigation ()
specifier|public
name|void
name|testDescendingNavigation
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|descending
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
name|Iterator
argument_list|<
name|E
argument_list|>
name|i
init|=
name|navigableSet
operator|.
name|descendingIterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|descending
operator|.
name|add
argument_list|(
name|i
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|descending
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
argument_list|,
name|descending
argument_list|)
expr_stmt|;
block|}
DECL|method|testEmptySubSet ()
specifier|public
name|void
name|testEmptySubSet
parameter_list|()
block|{
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|empty
init|=
name|navigableSet
operator|.
name|subSet
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|false
argument_list|,
name|e0
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|TreeSet
argument_list|<
name|E
argument_list|>
argument_list|()
argument_list|,
name|empty
argument_list|)
expr_stmt|;
block|}
comment|/*    * TODO(cpovirk): more testing of subSet/headSet/tailSet/descendingSet? and/or generate derived    * suites?    */
comment|/**    * Returns the {@link Method} instances for the test methods in this class that create a set with    * a "hole" in it so that set tests of {@code ContiguousSet} can suppress them with {@code    * FeatureSpecificTestSuiteBuilder.suppressing()}.    */
comment|/*    * TODO(cpovirk): or we could make HOLES_FORBIDDEN a feature. Or we could declare that    * implementations are permitted to throw IAE if a hole is requested, and we could update    * test*Hole to permit IAE. (But might this ignore genuine bugs?) But see the TODO above    * testLower, which could make this all unnecessary    */
DECL|method|getHoleMethods ()
specifier|public
specifier|static
name|Method
index|[]
name|getHoleMethods
parameter_list|()
block|{
return|return
operator|new
name|Method
index|[]
block|{
name|Helpers
operator|.
name|getMethod
argument_list|(
name|NavigableSetNavigationTester
operator|.
name|class
argument_list|,
literal|"testLowerHole"
argument_list|)
block|,
name|Helpers
operator|.
name|getMethod
argument_list|(
name|NavigableSetNavigationTester
operator|.
name|class
argument_list|,
literal|"testFloorHole"
argument_list|)
block|,
name|Helpers
operator|.
name|getMethod
argument_list|(
name|NavigableSetNavigationTester
operator|.
name|class
argument_list|,
literal|"testCeilingHole"
argument_list|)
block|,
name|Helpers
operator|.
name|getMethod
argument_list|(
name|NavigableSetNavigationTester
operator|.
name|class
argument_list|,
literal|"testHigherHole"
argument_list|)
block|,     }
return|;
block|}
block|}
end_class

end_unit
