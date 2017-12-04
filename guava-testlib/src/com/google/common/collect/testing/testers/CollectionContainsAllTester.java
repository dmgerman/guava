begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MinimalCollection
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
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code containsAll()} operations on a collection. Can't be  * invoked directly; please see {@link  * com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
annotation|@
name|GwtCompatible
DECL|class|CollectionContainsAllTester
specifier|public
class|class
name|CollectionContainsAllTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testContainsAll_empty ()
specifier|public
name|void
name|testContainsAll_empty
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"containsAll(empty) should return true"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|()
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
DECL|method|testContainsAll_subset ()
specifier|public
name|void
name|testContainsAll_subset
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"containsAll(subset) should return true"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsAll_sameElements ()
specifier|public
name|void
name|testContainsAll_sameElements
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"containsAll(sameElements) should return true"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"ModifyingCollectionWithItself"
argument_list|)
DECL|method|testContainsAll_self ()
specifier|public
name|void
name|testContainsAll_self
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"containsAll(this) should return true"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsAll_partialOverlap ()
specifier|public
name|void
name|testContainsAll_partialOverlap
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"containsAll(partialOverlap) should return false"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsAll_disjoint ()
specifier|public
name|void
name|testContainsAll_disjoint
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"containsAll(disjoint) should return false"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|)
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
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testContainsAll_nullNotAllowed ()
specifier|public
name|void
name|testContainsAll_nullNotAllowed
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
operator|(
name|E
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|tolerated
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testContainsAll_nullAllowed ()
specifier|public
name|void
name|testContainsAll_nullAllowed
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
operator|(
name|E
operator|)
literal|null
argument_list|)
argument_list|)
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
DECL|method|testContainsAll_nullPresent ()
specifier|public
name|void
name|testContainsAll_nullPresent
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|collection
operator|.
name|containsAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
operator|(
name|E
operator|)
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testContainsAll_wrongType ()
specifier|public
name|void
name|testContainsAll_wrongType
parameter_list|()
block|{
name|Collection
argument_list|<
name|WrongType
argument_list|>
name|wrong
init|=
name|MinimalCollection
operator|.
name|of
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|)
decl_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
literal|"containsAll(wrongType) should return false or throw"
argument_list|,
name|collection
operator|.
name|containsAll
argument_list|(
name|wrong
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|tolerated
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

