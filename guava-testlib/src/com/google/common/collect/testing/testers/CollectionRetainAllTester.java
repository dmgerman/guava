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
name|Arrays
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
name|List
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code retainAll} operations on a  * collection. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Chris Povirk  */
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
annotation|@
name|SuppressUnderAndroid
DECL|class|CollectionRetainAllTester
specifier|public
class|class
name|CollectionRetainAllTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * A collection of elements to retain, along with a description for use in    * failure messages.    */
DECL|class|Target
specifier|private
class|class
name|Target
block|{
DECL|field|toRetain
specifier|private
specifier|final
name|Collection
argument_list|<
name|E
argument_list|>
name|toRetain
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|Target (Collection<E> toRetain, String description)
specifier|private
name|Target
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|toRetain
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|toRetain
operator|=
name|toRetain
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|description
return|;
block|}
block|}
DECL|field|empty
specifier|private
name|Target
name|empty
decl_stmt|;
DECL|field|disjoint
specifier|private
name|Target
name|disjoint
decl_stmt|;
DECL|field|superset
specifier|private
name|Target
name|superset
decl_stmt|;
DECL|field|nonEmptyProperSubset
specifier|private
name|Target
name|nonEmptyProperSubset
decl_stmt|;
DECL|field|sameElements
specifier|private
name|Target
name|sameElements
decl_stmt|;
DECL|field|partialOverlap
specifier|private
name|Target
name|partialOverlap
decl_stmt|;
DECL|field|containsDuplicates
specifier|private
name|Target
name|containsDuplicates
decl_stmt|;
DECL|field|nullSingleton
specifier|private
name|Target
name|nullSingleton
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
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
name|empty
operator|=
operator|new
name|Target
argument_list|(
name|emptyCollection
argument_list|()
argument_list|,
literal|"empty"
argument_list|)
expr_stmt|;
comment|/*      * We test that nullSingleton.retainAll(disjointList) does NOT throw a      * NullPointerException when disjointList does not, so we can't use      * MinimalCollection, which throws NullPointerException on calls to      * contains(null).      */
name|List
argument_list|<
name|E
argument_list|>
name|disjointList
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|)
decl_stmt|;
name|disjoint
operator|=
operator|new
name|Target
argument_list|(
name|disjointList
argument_list|,
literal|"disjoint"
argument_list|)
expr_stmt|;
name|superset
operator|=
operator|new
name|Target
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|,
name|e2
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|)
argument_list|,
literal|"superset"
argument_list|)
expr_stmt|;
name|nonEmptyProperSubset
operator|=
operator|new
name|Target
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e1
argument_list|()
argument_list|)
argument_list|,
literal|"subset"
argument_list|)
expr_stmt|;
name|sameElements
operator|=
operator|new
name|Target
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|)
argument_list|,
literal|"sameElements"
argument_list|)
expr_stmt|;
name|containsDuplicates
operator|=
operator|new
name|Target
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
argument_list|,
literal|"containsDuplicates"
argument_list|)
expr_stmt|;
name|partialOverlap
operator|=
operator|new
name|Target
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e2
argument_list|()
argument_list|,
name|e3
argument_list|()
argument_list|)
argument_list|,
literal|"partialOverlap"
argument_list|)
expr_stmt|;
name|nullSingleton
operator|=
operator|new
name|Target
argument_list|(
name|Collections
operator|.
expr|<
name|E
operator|>
name|singleton
argument_list|(
literal|null
argument_list|)
argument_list|,
literal|"nullSingleton"
argument_list|)
expr_stmt|;
block|}
comment|// retainAll(empty)
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
DECL|method|testRetainAll_emptyPreviouslyEmpty ()
specifier|public
name|void
name|testRetainAll_emptyPreviouslyEmpty
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|empty
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
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testRetainAll_emptyPreviouslyEmptyUnsupported ()
specifier|public
name|void
name|testRetainAll_emptyPreviouslyEmptyUnsupported
parameter_list|()
block|{
name|expectReturnsFalseOrThrows
argument_list|(
name|empty
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_emptyPreviouslyNonEmpty ()
specifier|public
name|void
name|testRetainAll_emptyPreviouslyNonEmpty
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|expectContents
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|,
name|e2
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_emptyPreviouslyNonEmptyUnsupported ()
specifier|public
name|void
name|testRetainAll_emptyPreviouslyNonEmptyUnsupported
parameter_list|()
block|{
name|expectThrows
argument_list|(
name|empty
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(disjoint)
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
DECL|method|testRetainAll_disjointPreviouslyEmpty ()
specifier|public
name|void
name|testRetainAll_disjointPreviouslyEmpty
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|disjoint
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
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testRetainAll_disjointPreviouslyEmptyUnsupported ()
specifier|public
name|void
name|testRetainAll_disjointPreviouslyEmptyUnsupported
parameter_list|()
block|{
name|expectReturnsFalseOrThrows
argument_list|(
name|disjoint
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_disjointPreviouslyNonEmpty ()
specifier|public
name|void
name|testRetainAll_disjointPreviouslyNonEmpty
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|disjoint
argument_list|)
expr_stmt|;
name|expectContents
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e1
argument_list|()
argument_list|,
name|e2
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_disjointPreviouslyNonEmptyUnsupported ()
specifier|public
name|void
name|testRetainAll_disjointPreviouslyNonEmptyUnsupported
parameter_list|()
block|{
name|expectThrows
argument_list|(
name|disjoint
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(superset)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRetainAll_superset ()
specifier|public
name|void
name|testRetainAll_superset
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|superset
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
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRetainAll_supersetUnsupported ()
specifier|public
name|void
name|testRetainAll_supersetUnsupported
parameter_list|()
block|{
name|expectReturnsFalseOrThrows
argument_list|(
name|superset
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(subset)
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
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_subset ()
specifier|public
name|void
name|testRetainAll_subset
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|nonEmptyProperSubset
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|nonEmptyProperSubset
operator|.
name|toRetain
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_subsetUnsupported ()
specifier|public
name|void
name|testRetainAll_subsetUnsupported
parameter_list|()
block|{
name|expectThrows
argument_list|(
name|nonEmptyProperSubset
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(sameElements)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRetainAll_sameElements ()
specifier|public
name|void
name|testRetainAll_sameElements
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|sameElements
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
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testRetainAll_sameElementsUnsupported ()
specifier|public
name|void
name|testRetainAll_sameElementsUnsupported
parameter_list|()
block|{
name|expectReturnsFalseOrThrows
argument_list|(
name|sameElements
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(partialOverlap)
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
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_partialOverlap ()
specifier|public
name|void
name|testRetainAll_partialOverlap
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|partialOverlap
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|e2
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
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_partialOverlapUnsupported ()
specifier|public
name|void
name|testRetainAll_partialOverlapUnsupported
parameter_list|()
block|{
name|expectThrows
argument_list|(
name|partialOverlap
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(containsDuplicates)
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
DECL|method|testRetainAll_containsDuplicatesSizeOne ()
specifier|public
name|void
name|testRetainAll_containsDuplicatesSizeOne
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|containsDuplicates
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|e0
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
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_containsDuplicatesSizeSeveral ()
specifier|public
name|void
name|testRetainAll_containsDuplicatesSizeSeveral
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|containsDuplicates
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// retainAll(nullSingleton)
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
DECL|method|testRetainAll_nullSingletonPreviouslyEmpty ()
specifier|public
name|void
name|testRetainAll_nullSingletonPreviouslyEmpty
parameter_list|()
block|{
name|expectReturnsFalse
argument_list|(
name|nullSingleton
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_nullSingletonPreviouslyNonEmpty ()
specifier|public
name|void
name|testRetainAll_nullSingletonPreviouslyNonEmpty
parameter_list|()
block|{
name|expectReturnsTrue
argument_list|(
name|nullSingleton
argument_list|)
expr_stmt|;
name|expectContents
argument_list|()
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
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testRetainAll_nullSingletonPreviouslySingletonWithNull ()
specifier|public
name|void
name|testRetainAll_nullSingletonPreviouslySingletonWithNull
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|expectReturnsFalse
argument_list|(
name|nullSingleton
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|createArrayWithNullElement
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
name|SUPPORTS_REMOVE
block|,
name|ALLOWS_NULL_VALUES
block|}
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRetainAll_nullSingletonPreviouslySeveralWithNull ()
specifier|public
name|void
name|testRetainAll_nullSingletonPreviouslySeveralWithNull
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|expectReturnsTrue
argument_list|(
name|nullSingleton
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|nullSingleton
operator|.
name|toRetain
argument_list|)
expr_stmt|;
block|}
comment|// nullSingleton.retainAll()
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testRetainAll_containsNonNullWithNull ()
specifier|public
name|void
name|testRetainAll_containsNonNullWithNull
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|expectReturnsTrue
argument_list|(
name|disjoint
argument_list|)
expr_stmt|;
name|expectContents
argument_list|()
expr_stmt|;
block|}
comment|// retainAll(null)
comment|/*    * AbstractCollection fails the retainAll(null) test when the subject    * collection is empty, but we'd still like to test retainAll(null) when we    * can. We split the test into empty and non-empty cases. This allows us to    * suppress only the former.    */
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
DECL|method|testRetainAll_nullCollectionReferenceEmptySubject ()
specifier|public
name|void
name|testRetainAll_nullCollectionReferenceEmptySubject
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|retainAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Returning successfully is not ideal, but tolerated.
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
name|SUPPORTS_REMOVE
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
DECL|method|testRetainAll_nullCollectionReferenceNonEmptySubject ()
specifier|public
name|void
name|testRetainAll_nullCollectionReferenceNonEmptySubject
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|retainAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"retainAll(null) should throw NullPointerException"
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
DECL|method|expectReturnsTrue (Target target)
specifier|private
name|void
name|expectReturnsTrue
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
name|String
name|message
init|=
name|Platform
operator|.
name|format
argument_list|(
literal|"retainAll(%s) should return true"
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|message
argument_list|,
name|collection
operator|.
name|retainAll
argument_list|(
name|target
operator|.
name|toRetain
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|expectReturnsFalse (Target target)
specifier|private
name|void
name|expectReturnsFalse
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
name|String
name|message
init|=
name|Platform
operator|.
name|format
argument_list|(
literal|"retainAll(%s) should return false"
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|message
argument_list|,
name|collection
operator|.
name|retainAll
argument_list|(
name|target
operator|.
name|toRetain
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|expectThrows (Target target)
specifier|private
name|void
name|expectThrows
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
try|try
block|{
name|collection
operator|.
name|retainAll
argument_list|(
name|target
operator|.
name|toRetain
argument_list|)
expr_stmt|;
name|String
name|message
init|=
name|Platform
operator|.
name|format
argument_list|(
literal|"retainAll(%s) should throw"
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|fail
argument_list|(
name|message
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
DECL|method|expectReturnsFalseOrThrows (Target target)
specifier|private
name|void
name|expectReturnsFalseOrThrows
parameter_list|(
name|Target
name|target
parameter_list|)
block|{
name|String
name|message
init|=
name|Platform
operator|.
name|format
argument_list|(
literal|"retainAll(%s) should return false or throw"
argument_list|,
name|target
argument_list|)
decl_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
name|message
argument_list|,
name|collection
operator|.
name|retainAll
argument_list|(
name|target
operator|.
name|toRetain
argument_list|)
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
block|}
end_class

end_unit

