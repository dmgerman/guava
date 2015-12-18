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
comment|/**  * Tests for {@code Multiset#count}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MultisetCountTester
specifier|public
class|class
name|MultisetCountTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testCount_0 ()
specifier|public
name|void
name|testCount_0
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.count(missing) didn't return 0"
argument_list|,
literal|0
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
DECL|method|testCount_1 ()
specifier|public
name|void
name|testCount_1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.count(present) didn't return 1"
argument_list|,
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
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testCount_3 ()
specifier|public
name|void
name|testCount_3
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"multiset.count(thriceContained) didn't return 3"
argument_list|,
literal|3
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
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testCount_nullAbsent ()
specifier|public
name|void
name|testCount_nullAbsent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.count(null) didn't return 0"
argument_list|,
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
name|absent
operator|=
name|ALLOWS_NULL_QUERIES
argument_list|)
DECL|method|testCount_null_forbidden ()
specifier|public
name|void
name|testCount_null_forbidden
parameter_list|()
block|{
try|try
block|{
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
literal|null
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
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testCount_nullPresent ()
specifier|public
name|void
name|testCount_nullPresent
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
name|count
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCount_wrongType ()
specifier|public
name|void
name|testCount_wrongType
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.count(wrongType) didn't return 0"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|count
argument_list|(
name|WrongType
operator|.
name|VALUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns {@link Method} instances for the read tests that assume multisets    * support duplicates so that the test of {@code Multisets.forSet()} can    * suppress them.    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"reflection"
argument_list|)
DECL|method|getCountDuplicateInitializingMethods ()
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getCountDuplicateInitializingMethods
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
name|MultisetCountTester
operator|.
name|class
argument_list|,
literal|"testCount_3"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

