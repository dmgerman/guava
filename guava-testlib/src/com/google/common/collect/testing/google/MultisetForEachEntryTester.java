begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|KNOWN_ORDER
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
name|Multiset
operator|.
name|Entry
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
name|Multisets
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
name|CollectionFeature
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
name|ArrayList
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
comment|/**  * Tests for {@code Multiset#forEachEntry}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MultisetForEachEntryTester
specifier|public
class|class
name|MultisetForEachEntryTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testForEachEntry ()
specifier|public
name|void
name|testForEachEntry
parameter_list|()
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|actual
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getMultiset
argument_list|()
operator|.
name|forEachEntry
argument_list|(
parameter_list|(
name|element
parameter_list|,
name|count
parameter_list|)
lambda|->
name|actual
operator|.
name|add
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testForEachEntryOrdered ()
specifier|public
name|void
name|testForEachEntryOrdered
parameter_list|()
block|{
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|expected
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|actual
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getMultiset
argument_list|()
operator|.
name|forEachEntry
argument_list|(
parameter_list|(
name|element
parameter_list|,
name|count
parameter_list|)
lambda|->
name|actual
operator|.
name|add
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|testForEachEntryDuplicates ()
specifier|public
name|void
name|testForEachEntryDuplicates
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|expected
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|3
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|actual
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getMultiset
argument_list|()
operator|.
name|forEachEntry
argument_list|(
parameter_list|(
name|element
parameter_list|,
name|count
parameter_list|)
lambda|->
name|actual
operator|.
name|add
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns {@link Method} instances for the remove tests that assume multisets support duplicates    * so that the test of {@code Multisets.forSet()} can suppress them.    */
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getForEachEntryDuplicateInitializingMethods ()
specifier|public
specifier|static
name|List
argument_list|<
name|Method
argument_list|>
name|getForEachEntryDuplicateInitializingMethods
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
name|MultisetForEachEntryTester
operator|.
name|class
argument_list|,
literal|"testForEachEntryDuplicates"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

