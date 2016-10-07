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
name|IteratorFeature
operator|.
name|MODIFIABLE
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
name|IteratorFeature
operator|.
name|UNMODIFIABLE
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
name|ListFeature
operator|.
name|SUPPORTS_ADD_WITH_INDEX
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
name|SUPPORTS_SET
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
name|testers
operator|.
name|Platform
operator|.
name|listListIteratorTesterNumIterations
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
name|singleton
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
name|IteratorFeature
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
name|ListIteratorTester
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
name|ListFeature
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code listIterator} operations on a list.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author Chris Povirk  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ListListIteratorTester
specifier|public
class|class
name|ListListIteratorTester
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|SUPPORTS_SET
block|,
name|SUPPORTS_ADD_WITH_INDEX
block|}
argument_list|)
DECL|method|testListIterator_unmodifiable ()
specifier|public
name|void
name|testListIterator_unmodifiable
parameter_list|()
block|{
name|runListIteratorTest
argument_list|(
name|UNMODIFIABLE
argument_list|)
expr_stmt|;
block|}
comment|/*    * For now, we don't cope with testing this when the list supports only some    * modification operations.    */
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|ListFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_SET
block|,
name|SUPPORTS_ADD_WITH_INDEX
block|}
argument_list|)
DECL|method|testListIterator_fullyModifiable ()
specifier|public
name|void
name|testListIterator_fullyModifiable
parameter_list|()
block|{
name|runListIteratorTest
argument_list|(
name|MODIFIABLE
argument_list|)
expr_stmt|;
block|}
DECL|method|runListIteratorTest (Set<IteratorFeature> features)
specifier|private
name|void
name|runListIteratorTest
parameter_list|(
name|Set
argument_list|<
name|IteratorFeature
argument_list|>
name|features
parameter_list|)
block|{
operator|new
name|ListIteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
name|listListIteratorTesterNumIterations
argument_list|()
argument_list|,
name|singleton
argument_list|(
name|e4
argument_list|()
argument_list|)
argument_list|,
name|features
argument_list|,
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|)
argument_list|,
literal|0
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|ListIterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
name|resetCollection
argument_list|()
expr_stmt|;
return|return
name|getList
argument_list|()
operator|.
name|listIterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|verify
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|expectContents
argument_list|(
name|elements
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testListIterator_tooLow ()
specifier|public
name|void
name|testListIterator_tooLow
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|listIterator
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testListIterator_tooHigh ()
specifier|public
name|void
name|testListIterator_tooHigh
parameter_list|()
block|{
try|try
block|{
name|getList
argument_list|()
operator|.
name|listIterator
argument_list|(
name|getNumElements
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testListIterator_atSize ()
specifier|public
name|void
name|testListIterator_atSize
parameter_list|()
block|{
name|getList
argument_list|()
operator|.
name|listIterator
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: run the iterator through ListIteratorTester
block|}
comment|/**    * Returns the {@link Method} instance for    * {@link #testListIterator_fullyModifiable()} so that tests of    * {@link CopyOnWriteArraySet} can suppress it with    * {@code FeatureSpecificTestSuiteBuilder.suppressing()} until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6570575">Sun bug    * 6570575</a> is fixed.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getListIteratorFullyModifiableMethod ()
specifier|public
specifier|static
name|Method
name|getListIteratorFullyModifiableMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|ListListIteratorTester
operator|.
name|class
argument_list|,
literal|"testListIterator_fullyModifiable"
argument_list|)
return|;
block|}
comment|/**    * Returns the {@link Method} instance for    * {@link #testListIterator_unmodifiable()} so that it can be suppressed in    * GWT tests.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getListIteratorUnmodifiableMethod ()
specifier|public
specifier|static
name|Method
name|getListIteratorUnmodifiableMethod
parameter_list|()
block|{
return|return
name|Helpers
operator|.
name|getMethod
argument_list|(
name|ListListIteratorTester
operator|.
name|class
argument_list|,
literal|"testListIterator_unmodifiable"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

