begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|HashMultiset
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
name|features
operator|.
name|CollectionSize
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests multiset-specific read operations.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.SetTestSuiteBuilder}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|SuppressUnderAndroid
DECL|class|MultisetReadsTester
specifier|public
class|class
name|MultisetReadsTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMultisetTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testElementSet_contains ()
specifier|public
name|void
name|testElementSet_contains
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset.elementSet().contains(present) returned false"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
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
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testEntrySet_contains ()
specifier|public
name|void
name|testEntrySet_contains
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset.entrySet() didn't contain [present, 1]"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e0
argument_list|()
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet_contains_count0 ()
specifier|public
name|void
name|testEntrySet_contains_count0
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"multiset.entrySet() contains [missing, 0]"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|e3
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet_contains_nonentry ()
specifier|public
name|void
name|testEntrySet_contains_nonentry
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"multiset.entrySet() contains a non-entry"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|contains
argument_list|(
name|e0
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEntrySet_twice ()
specifier|public
name|void
name|testEntrySet_twice
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"calling multiset.entrySet() twice returned unequal sets"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testEntrySet_hashCode_size0 ()
specifier|public
name|void
name|testEntrySet_hashCode_size0
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.entrySet() has incorrect hash code"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|hashCode
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
DECL|method|testEntrySet_hashCode_size1 ()
specifier|public
name|void
name|testEntrySet_hashCode_size1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset.entrySet() has incorrect hash code"
argument_list|,
literal|1
operator|^
name|e0
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_yes ()
specifier|public
name|void
name|testEquals_yes
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"multiset doesn't equal a multiset with the same elements"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|equals
argument_list|(
name|HashMultiset
operator|.
name|create
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_differentSize ()
specifier|public
name|void
name|testEquals_differentSize
parameter_list|()
block|{
name|Multiset
argument_list|<
name|E
argument_list|>
name|other
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|other
operator|.
name|add
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset equals a multiset with a different size"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|equals
argument_list|(
name|other
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
DECL|method|testEquals_differentElements ()
specifier|public
name|void
name|testEquals_differentElements
parameter_list|()
block|{
name|Multiset
argument_list|<
name|E
argument_list|>
name|other
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|other
operator|.
name|remove
argument_list|(
name|e0
argument_list|()
argument_list|)
expr_stmt|;
name|other
operator|.
name|add
argument_list|(
name|e3
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"multiset equals a multiset with different elements"
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|equals
argument_list|(
name|other
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testHashCode_size0 ()
specifier|public
name|void
name|testHashCode_size0
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset has incorrect hash code"
argument_list|,
literal|0
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|hashCode
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
DECL|method|testHashCode_size1 ()
specifier|public
name|void
name|testHashCode_size1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"multiset has incorrect hash code"
argument_list|,
literal|1
operator|^
name|e0
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|,
name|getMultiset
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

