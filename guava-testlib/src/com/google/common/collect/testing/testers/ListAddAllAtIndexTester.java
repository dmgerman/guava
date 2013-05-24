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
name|ListFeature
operator|.
name|SUPPORTS_ADD_WITH_INDEX
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|singletonList
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
name|ListFeature
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
comment|/**  * A generic JUnit test which tests {@code addAll(int, Collection)} operations  * on a list. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Chris Povirk  */
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
DECL|class|ListAddAllAtIndexTester
specifier|public
class|class
name|ListAddAllAtIndexTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_supportedAllPresent ()
specifier|public
name|void
name|testAddAllAtIndex_supportedAllPresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(n, allPresent) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
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
name|expectAdded
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_unsupportedAllPresent ()
specifier|public
name|void
name|testAddAllAtIndex_unsupportedAllPresent
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
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
literal|"addAll(n, allPresent) should throw"
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
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_supportedSomePresent ()
specifier|public
name|void
name|testAddAllAtIndex_supportedSomePresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(n, allPresent) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
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
name|expectAdded
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_unsupportedSomePresent ()
specifier|public
name|void
name|testAddAllAtIndex_unsupportedSomePresent
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
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
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(n, allPresent) should throw"
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
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_supportedNothing ()
specifier|public
name|void
name|testAddAllAtIndex_supportedNothing
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"addAll(n, nothing) should return false"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|emptyCollection
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_unsupportedNothing ()
specifier|public
name|void
name|testAddAllAtIndex_unsupportedNothing
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"addAll(n, nothing) should return false or throw"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|emptyCollection
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
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_withDuplicates ()
specifier|public
name|void
name|testAddAllAtIndex_withDuplicates
parameter_list|()
block|{
name|MinimalCollection
argument_list|<
name|E
argument_list|>
name|elementsToAdd
init|=
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
name|e1
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"addAll(n, hasDuplicates) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|elementsToAdd
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
literal|0
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e1
argument_list|,
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAddAllAtIndex_nullSupported ()
specifier|public
name|void
name|testAddAllAtIndex_nullSupported
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|containsNull
init|=
name|singletonList
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"addAll(n, containsNull) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|containsNull
argument_list|)
argument_list|)
expr_stmt|;
comment|/*      * We need (E) to force interpretation of null as the single element of a      * varargs array, not the array itself      */
name|expectAdded
argument_list|(
literal|0
argument_list|,
operator|(
name|E
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testAddAllAtIndex_nullUnsupported ()
specifier|public
name|void
name|testAddAllAtIndex_nullUnsupported
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|containsNull
init|=
name|singletonList
argument_list|(
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
name|containsNull
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(n, containsNull) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectNullMissingWhenNullUnsupported
argument_list|(
literal|"Should not contain null after unsupported addAll(n, containsNull)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_middle ()
specifier|public
name|void
name|testAddAllAtIndex_middle
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(middle, disjoint) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|,
name|createDisjointCollection
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|getNumElements
argument_list|()
operator|/
literal|2
argument_list|,
name|createDisjointCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
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
DECL|method|testAddAllAtIndex_end ()
specifier|public
name|void
name|testAddAllAtIndex_end
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(end, disjoint) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|createDisjointCollection
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|expectAdded
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|createDisjointCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_nullCollectionReference ()
specifier|public
name|void
name|testAddAllAtIndex_nullCollectionReference
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(n, null) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_negative ()
specifier|public
name|void
name|testAddAllAtIndex_negative
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
operator|-
literal|1
argument_list|,
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(-1, e) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD_WITH_INDEX
argument_list|)
DECL|method|testAddAllAtIndex_tooLarge ()
specifier|public
name|void
name|testAddAllAtIndex_tooLarge
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|addAll
argument_list|(
name|getNumElements
argument_list|()
operator|+
literal|1
argument_list|,
name|MinimalCollection
operator|.
name|of
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(size + 1, e) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
name|expectUnchanged
argument_list|()
expr_stmt|;
name|expectMissing
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

