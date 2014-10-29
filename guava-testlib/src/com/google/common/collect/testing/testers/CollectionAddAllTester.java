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

begin_comment
comment|/**  * A generic JUnit test which tests addAll operations on a collection. Can't be  * invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
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
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|CollectionAddAllTester
specifier|public
class|class
name|CollectionAddAllTester
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
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_supportedNothing ()
specifier|public
name|void
name|testAddAll_supportedNothing
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"addAll(nothing) should return false"
argument_list|,
name|collection
operator|.
name|addAll
argument_list|(
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_unsupportedNothing ()
specifier|public
name|void
name|testAddAll_unsupportedNothing
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"addAll(nothing) should return false or throw"
argument_list|,
name|collection
operator|.
name|addAll
argument_list|(
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testAddAll_supportedNonePresent ()
specifier|public
name|void
name|testAddAll_supportedNonePresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(nonePresent) should return true"
argument_list|,
name|collection
operator|.
name|addAll
argument_list|(
name|createDisjointCollection
argument_list|()
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
DECL|method|testAddAll_unsupportedNonePresent ()
specifier|public
name|void
name|testAddAll_unsupportedNonePresent
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|addAll
argument_list|(
name|createDisjointCollection
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(nonePresent) should throw"
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
name|e3
argument_list|()
argument_list|,
name|e4
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testAddAll_supportedSomePresent ()
specifier|public
name|void
name|testAddAll_supportedSomePresent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"addAll(somePresent) should return true"
argument_list|,
name|collection
operator|.
name|addAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"should contain "
operator|+
name|e3
argument_list|()
argument_list|,
name|collection
operator|.
name|contains
argument_list|(
name|e3
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"should contain "
operator|+
name|e0
argument_list|()
argument_list|,
name|collection
operator|.
name|contains
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testAddAll_unsupportedSomePresent ()
specifier|public
name|void
name|testAddAll_unsupportedSomePresent
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|addAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(somePresent) should throw"
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testAddAllConcurrentWithIteration ()
specifier|public
name|void
name|testAddAllConcurrentWithIteration
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
name|assertTrue
argument_list|(
name|collection
operator|.
name|addAll
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
argument_list|)
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
name|absent
operator|=
name|SUPPORTS_ADD
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
DECL|method|testAddAll_unsupportedAllPresent ()
specifier|public
name|void
name|testAddAll_unsupportedAllPresent
parameter_list|()
block|{
try|try
block|{
name|assertFalse
argument_list|(
literal|"addAll(allPresent) should return false or throw"
argument_list|,
name|collection
operator|.
name|addAll
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
DECL|method|testAddAll_nullSupported ()
specifier|public
name|void
name|testAddAll_nullSupported
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
literal|"addAll(containsNull) should return true"
argument_list|,
name|collection
operator|.
name|addAll
argument_list|(
name|containsNull
argument_list|)
argument_list|)
expr_stmt|;
comment|/*      * We need (E) to force interpretation of null as the single element of a      * varargs array, not the array itself      */
name|expectAdded
argument_list|(
operator|(
name|E
operator|)
literal|null
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
DECL|method|testAddAll_nullUnsupported ()
specifier|public
name|void
name|testAddAll_nullUnsupported
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
name|collection
operator|.
name|addAll
argument_list|(
name|containsNull
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(containsNull) should throw"
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
literal|"Should not contain null after unsupported addAll(containsNull)"
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
DECL|method|testAddAll_nullCollectionReference ()
specifier|public
name|void
name|testAddAll_nullCollectionReference
parameter_list|()
block|{
try|try
block|{
name|collection
operator|.
name|addAll
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"addAll(null) should throw NullPointerException"
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
comment|/**    * Returns the {@link Method} instance for {@link    * #testAddAll_nullUnsupported()} so that tests can suppress it with {@code    * FeatureSpecificTestSuiteBuilder.suppressing()} until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5045147">Sun    * bug 5045147</a> is fixed.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getAddAllNullUnsupportedMethod ()
specifier|public
specifier|static
name|Method
name|getAddAllNullUnsupportedMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionAddAllTester
operator|.
name|class
argument_list|,
literal|"testAddAll_nullUnsupported"
argument_list|)
return|;
block|}
comment|/**    * Returns the {@link Method} instance for {@link    * #testAddAll_unsupportedNonePresent()} so that tests can suppress it with    * {@code FeatureSpecificTestSuiteBuilder.suppressing()} while we figure out    * what to do with<a href="http://goo.gl/qJBruX">{@code ConcurrentHashMap}    * support for {@code entrySet().add()}</a>.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getAddAllUnsupportedNonePresentMethod ()
specifier|public
specifier|static
name|Method
name|getAddAllUnsupportedNonePresentMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionAddAllTester
operator|.
name|class
argument_list|,
literal|"testAddAll_unsupportedNonePresent"
argument_list|)
return|;
block|}
comment|/**    * Returns the {@link Method} instance for {@link    * #testAddAll_unsupportedSomePresent()} so that tests can suppress it with    * {@code FeatureSpecificTestSuiteBuilder.suppressing()} while we figure out    * what to do with<a href="http://goo.gl/qJBruX">{@code ConcurrentHashMap}    * support for {@code entrySet().add()}</a>.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getAddAllUnsupportedSomePresentMethod ()
specifier|public
specifier|static
name|Method
name|getAddAllUnsupportedSomePresentMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionAddAllTester
operator|.
name|class
argument_list|,
literal|"testAddAll_unsupportedSomePresent"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

