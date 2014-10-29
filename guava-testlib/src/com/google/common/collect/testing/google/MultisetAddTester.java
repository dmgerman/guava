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
name|SUPPORTS_ADD
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
name|features
operator|.
name|CollectionFeature
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
name|Collections
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Multiset.add}.  *   * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetAddTester
specifier|public
class|class
name|MultisetAddTester
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
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddUnsupported ()
specifier|public
name|void
name|testAddUnsupported
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddMeansAddOne ()
specifier|public
name|void
name|testAddMeansAddOne
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
name|e0
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalCount
operator|+
literal|1
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrencesZero ()
specifier|public
name|void
name|testAddOccurrencesZero
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
name|e0
argument_list|()
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
name|add
argument_list|(
name|e0
argument_list|()
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrences ()
specifier|public
name|void
name|testAddOccurrences
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
name|e0
argument_list|()
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
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"old count"
argument_list|,
name|originalCount
operator|+
literal|2
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddSeveralTimes ()
specifier|public
name|void
name|testAddSeveralTimes
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
name|e0
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|originalCount
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalCount
operator|+
literal|3
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|originalCount
operator|+
literal|4
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
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
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrences_unsupported ()
specifier|public
name|void
name|testAddOccurrences_unsupported
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"unsupported multiset.add(E, int) didn't throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|required
parameter_list|)
block|{}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddOccurrencesNegative ()
specifier|public
name|void
name|testAddOccurrencesNegative
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"multiset.add(E, -1) didn't throw an exception"
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddTooMany ()
specifier|public
name|void
name|testAddTooMany
parameter_list|()
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e3
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|e3
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|e3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|getMultiset
argument_list|()
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_emptySet ()
specifier|public
name|void
name|testAddAll_emptySet
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|addAll
argument_list|(
name|Collections
operator|.
expr|<
name|E
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_emptyMultiset ()
specifier|public
name|void
name|testAddAll_emptyMultiset
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|addAll
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_nonEmptyList ()
specifier|public
name|void
name|testAddAll_nonEmptyList
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|,
name|e3
argument_list|()
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
DECL|method|testAddAll_nonEmptyMultiset ()
specifier|public
name|void
name|testAddAll_nonEmptyMultiset
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|addAll
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

