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
name|SUPPORTS_REMOVE_ALL
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
name|Collections
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code removeAll} operations on a  * collection. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// too many "unchecked generic array creations"
DECL|class|CollectionRemoveAllTester
specifier|public
class|class
name|CollectionRemoveAllTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testRemoveAll_emptyCollection ()
specifier|public
name|void
name|testRemoveAll_emptyCollection
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"removeAll(emptyCollection) should return false"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
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
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testRemoveAll_nonePresent ()
specifier|public
name|void
name|testRemoveAll_nonePresent
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"removeAll(disjointCollection) should return false"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
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
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_allPresent ()
specifier|public
name|void
name|testRemoveAll_allPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"removeAll(intersectingCollection) should return true"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_somePresent ()
specifier|public
name|void
name|testRemoveAll_somePresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"removeAll(intersectingCollection) should return true"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
block|}
comment|/**    * Trigger the other.size()>= this.size() case in AbstractSet.removeAll().    */
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_somePresentLargeCollectionToRemove ()
specifier|public
name|void
name|testRemoveAll_somePresentLargeCollectionToRemove
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"removeAll(largeIntersectingCollection) should return true"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e3
argument_list|,
name|samples
operator|.
name|e3
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e0
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
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testRemoveAll_unsupportedEmptyCollection ()
specifier|public
name|void
name|testRemoveAll_unsupportedEmptyCollection
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"removeAll(emptyCollection) should return false or throw "
operator|+
literal|"UnsupportedOperationException"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|()
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
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testRemoveAll_unsupportedNonePresent ()
specifier|public
name|void
name|testRemoveAll_unsupportedNonePresent
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"removeAll(disjointCollection) should return false or throw "
operator|+
literal|"UnsupportedOperationException"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
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
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_unsupportedPresent ()
specifier|public
name|void
name|testRemoveAll_unsupportedPresent
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"removeAll(intersectingCollection) should throw "
operator|+
literal|"UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|collection
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*    * AbstractCollection fails the removeAll(null) test when the subject    * collection is empty, but we'd still like to test removeAll(null) when we    * can. We split the test into empty and non-empty cases. This allows us to    * suppress only the former.    */
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testRemoveAll_nullCollectionReferenceEmptySubject ()
specifier|public
name|void
name|testRemoveAll_nullCollectionReferenceEmptySubject
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|removeAll
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
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_nullCollectionReferenceNonEmptySubject ()
specifier|public
name|void
name|testRemoveAll_nullCollectionReferenceNonEmptySubject
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|removeAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"removeAll(null) should throw NullPointerException"
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
name|value
operator|=
name|SUPPORTS_REMOVE_ALL
argument_list|,
name|absent
operator|=
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testRemoveAll_containsNullNo ()
specifier|public
name|void
name|testRemoveAll_containsNullNo
parameter_list|()
block|{
name|MinimalCollection
argument_list|<
name|?
argument_list|>
name|containsNull
init|=
name|MinimalCollection
operator|.
name|of
argument_list|(
operator|(
name|Object
operator|)
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|assertFalse
argument_list|(
literal|"removeAll(containsNull) should return false or throw"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|containsNull
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
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE_ALL
block|,
name|ALLOWS_NULL_QUERIES
block|}
argument_list|)
DECL|method|testRemoveAll_containsNullNoButAllowed ()
specifier|public
name|void
name|testRemoveAll_containsNullNoButAllowed
parameter_list|()
block|{
name|MinimalCollection
argument_list|<
name|?
argument_list|>
name|containsNull
init|=
name|MinimalCollection
operator|.
name|of
argument_list|(
operator|(
name|Object
operator|)
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"removeAll(containsNull) should return false"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|containsNull
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
block|{
name|SUPPORTS_REMOVE_ALL
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
DECL|method|testRemoveAll_containsNullYes ()
specifier|public
name|void
name|testRemoveAll_containsNullYes
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"removeAll(containsNull) should return true"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO: make this work with MinimalCollection
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE_ALL
argument_list|)
DECL|method|testRemoveAll_containsWrongType ()
specifier|public
name|void
name|testRemoveAll_containsWrongType
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"removeAll(containsWrongType) should return false or throw"
argument_list|,
name|collection
operator|.
name|removeAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|)
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
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

