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
import|import static
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Multiset.elementSet()} not covered by the derived {@code SetTestSuiteBuilder}.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetElementSetTester
specifier|public
class|class
name|MultisetElementSetTester
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
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_ADD
argument_list|)
DECL|method|testElementSetReflectsAddAbsent ()
specifier|public
name|void
name|testElementSetReflectsAddAbsent
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|elementSet
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e3
argument_list|)
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|add
argument_list|(
name|samples
operator|.
name|e3
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|elementSet
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e3
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
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testElementSetReflectsRemove ()
specifier|public
name|void
name|testElementSetReflectsRemove
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elementSet
operator|.
name|contains
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|getMultiset
argument_list|()
operator|.
name|removeAll
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|elementSet
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testElementSetRemovePropagatesToMultiset ()
specifier|public
name|void
name|testElementSetRemovePropagatesToMultiset
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elementSet
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|getMultiset
argument_list|()
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
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testElementSetRemoveDuplicatePropagatesToMultiset ()
specifier|public
name|void
name|testElementSetRemoveDuplicatePropagatesToMultiset
parameter_list|()
block|{
name|initThreeCopies
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|elementSet
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
argument_list|)
argument_list|)
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|getMultiset
argument_list|()
argument_list|)
operator|.
name|isEmpty
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
DECL|method|testElementSetRemoveAbsent ()
specifier|public
name|void
name|testElementSetRemoveAbsent
parameter_list|()
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|elementSet
init|=
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|elementSet
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e3
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
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testElementSetClear ()
specifier|public
name|void
name|testElementSetClear
parameter_list|()
block|{
name|getMultiset
argument_list|()
operator|.
name|elementSet
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|getMultiset
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

