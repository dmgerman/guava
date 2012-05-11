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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests operations on a SortedSet. Can't be  * invoked directly; please see {@code SortedSetTestSuiteBuilder}.  *  * @author Jesse Wilson  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SortedSetNavigationTester
specifier|public
class|class
name|SortedSetNavigationTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|field|sortedSet
specifier|private
name|SortedSet
argument_list|<
name|E
argument_list|>
name|sortedSet
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
name|sortedSet
operator|=
operator|(
name|SortedSet
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
name|sortedSet
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptySetFirst ()
specifier|public
name|void
name|testEmptySetFirst
parameter_list|()
block|{
try|try
block|{
name|sortedSet
operator|.
name|first
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEmptySetLast ()
specifier|public
name|void
name|testEmptySetLast
parameter_list|()
block|{
try|try
block|{
name|sortedSet
operator|.
name|last
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testSingletonSetFirst ()
specifier|public
name|void
name|testSingletonSetFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|sortedSet
operator|.
name|first
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
DECL|method|testSingletonSetLast ()
specifier|public
name|void
name|testSingletonSetLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|sortedSet
operator|.
name|last
argument_list|()
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
DECL|method|testFirst ()
specifier|public
name|void
name|testFirst
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|a
argument_list|,
name|sortedSet
operator|.
name|first
argument_list|()
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
DECL|method|testLast ()
specifier|public
name|void
name|testLast
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|c
argument_list|,
name|sortedSet
operator|.
name|last
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

