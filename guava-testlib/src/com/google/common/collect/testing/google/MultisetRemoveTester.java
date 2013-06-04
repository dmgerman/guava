begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ALLOWS_NULL_QUERIES
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
name|WrongType
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
name|List
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Multiset#remove}.  *   * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MultisetRemoveTester
specifier|public
class|class
name|MultisetRemoveTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemoveZeroNoOp ()
specifier|public
name|void
name|testRemoveZeroNoOp
parameter_list|()
block|{
name|int
name|originalCount
init|=
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"old count"
argument_list|,
name|originalCount
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
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
DECL|method|testRemove_occurrences_present ()
specifier|public
name|void
name|testRemove_occurrences_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(present, 2) didn't return the old count"
argument_list|,
literal|1
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains present after multiset.remove(present, 2)"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
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
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_some_occurrences_present ()
specifier|public
name|void
name|testRemove_some_occurrences_present
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.remove(present, 2) didn't return the old count"
argument_list|,
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"multiset contains present after multiset.remove(present, 2)"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
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
DECL|method|testRemove_occurrences_absent ()
specifier|public
name|void
name|testRemove_occurrences_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(absent, 0) didn't return 0"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
argument_list|,
literal|2
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
DECL|method|testRemove_occurrences_unsupported_absent ()
specifier|public
name|void
name|testRemove_occurrences_unsupported_absent
parameter_list|()
block|{
comment|// notice: we don't care whether it succeeds, or fails with UOE
try|try
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(absent, 2) didn't return 0 or throw an exception"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ok
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_0 ()
specifier|public
name|void
name|testRemove_occurrences_0
parameter_list|()
block|{
name|int
name|oldCount
init|=
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.remove(E, 0) didn't return the old count"
argument_list|,
name|oldCount
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
literal|0
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
DECL|method|testRemove_occurrences_negative ()
specifier|public
name|void
name|testRemove_occurrences_negative
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"multiset.remove(E, -1) didn't throw an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|required
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRemove_occurrences_wrongType ()
specifier|public
name|void
name|testRemove_occurrences_wrongType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.remove(wrongType, 1) didn't return 0"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|,
literal|1
argument_list|)
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
block|{
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
DECL|method|testRemove_nullPresent ()
specifier|public
name|void
name|testRemove_nullPresent
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset contains present after multiset.remove(present, 2)"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|contains
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
literal|null
argument_list|)
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
name|ALLOWS_NULL_QUERIES
block|}
argument_list|)
DECL|method|testRemove_nullAbsent ()
specifier|public
name|void
name|testRemove_nullAbsent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|,
literal|2
argument_list|)
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
name|SUPPORTS_REMOVE
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testRemove_nullForbidden ()
specifier|public
name|void
name|testRemove_nullForbidden
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|remove
argument_list|(
literal|null
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{}
block|}
comment|/**    * Returns {@link Method} instances for the remove tests that assume multisets    * support duplicates so that the test of {@code Multisets.forSet()} can    * suppress them.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getRemoveDuplicateInitializingMethods ()
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getRemoveDuplicateInitializingMethods
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|Helpers
operator|.
name|getMethod
argument_list|(
name|MultisetRemoveTester
operator|.
name|class
argument_list|,
literal|"testRemove_some_occurrences_present"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

