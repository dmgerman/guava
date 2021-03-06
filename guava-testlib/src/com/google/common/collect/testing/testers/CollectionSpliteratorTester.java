begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SpliteratorTester
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
name|Spliterator
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
comment|/**  * A generic JUnit test which tests {@code spliterator} operations on a collection. Can't be invoked  * directly; please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Louis Wasserman  */
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
DECL|class|CollectionSpliteratorTester
specifier|public
class|class
name|CollectionSpliteratorTester
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
name|absent
operator|=
name|KNOWN_ORDER
argument_list|)
DECL|method|testSpliteratorUnknownOrder ()
specifier|public
name|void
name|testSpliteratorUnknownOrder
parameter_list|()
block|{
synchronized|synchronized
init|(
name|collection
init|)
block|{
name|SpliteratorTester
operator|.
name|of
argument_list|(
name|collection
operator|::
name|spliterator
argument_list|)
operator|.
name|expect
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testSpliteratorKnownOrder ()
specifier|public
name|void
name|testSpliteratorKnownOrder
parameter_list|()
block|{
synchronized|synchronized
init|(
name|collection
init|)
block|{
name|SpliteratorTester
operator|.
name|of
argument_list|(
name|collection
operator|::
name|spliterator
argument_list|)
operator|.
name|expect
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
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
DECL|method|testSpliteratorNullable ()
specifier|public
name|void
name|testSpliteratorNullable
parameter_list|()
block|{
name|initCollectionWithNullElement
argument_list|()
expr_stmt|;
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// for Collections.synchronized
name|assertFalse
argument_list|(
name|collection
operator|.
name|spliterator
argument_list|()
operator|.
name|hasCharacteristics
argument_list|(
name|Spliterator
operator|.
name|NONNULL
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testSpliteratorNotImmutable_CollectionAllowsAdd ()
specifier|public
name|void
name|testSpliteratorNotImmutable_CollectionAllowsAdd
parameter_list|()
block|{
comment|// If add is supported, verify that IMMUTABLE is not reported.
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// for Collections.synchronized
name|assertFalse
argument_list|(
name|collection
operator|.
name|spliterator
argument_list|()
operator|.
name|hasCharacteristics
argument_list|(
name|Spliterator
operator|.
name|IMMUTABLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testSpliteratorNotImmutable_CollectionAllowsRemove ()
specifier|public
name|void
name|testSpliteratorNotImmutable_CollectionAllowsRemove
parameter_list|()
block|{
comment|// If remove is supported, verify that IMMUTABLE is not reported.
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// for Collections.synchronized
name|assertFalse
argument_list|(
name|collection
operator|.
name|spliterator
argument_list|()
operator|.
name|hasCharacteristics
argument_list|(
name|Spliterator
operator|.
name|IMMUTABLE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getSpliteratorNotImmutableCollectionAllowsAddMethod ()
specifier|public
specifier|static
name|Method
name|getSpliteratorNotImmutableCollectionAllowsAddMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionSpliteratorTester
operator|.
name|class
argument_list|,
literal|"testSpliteratorNotImmutable_CollectionAllowsAdd"
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getSpliteratorNotImmutableCollectionAllowsRemoveMethod ()
specifier|public
specifier|static
name|Method
name|getSpliteratorNotImmutableCollectionAllowsRemoveMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|CollectionSpliteratorTester
operator|.
name|class
argument_list|,
literal|"testSpliteratorNotImmutable_CollectionAllowsRemove"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

