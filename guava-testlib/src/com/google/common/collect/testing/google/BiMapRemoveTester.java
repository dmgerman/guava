begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|SUPPORTS_ITERATOR_REMOVE
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
name|MapFeature
operator|.
name|SUPPORTS_REMOVE
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
name|MapFeature
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

begin_comment
comment|/**  * Tester for {@code BiMap.remove}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BiMapRemoveTester
specifier|public
class|class
name|BiMapRemoveTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBiMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveKeyRemovesFromInverse ()
specifier|public
name|void
name|testRemoveKeyRemovesFromInverse
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveKeyFromKeySetRemovesFromInverse ()
specifier|public
name|void
name|testRemoveKeyFromKeySetRemovesFromInverse
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveFromValuesRemovesFromInverse ()
specifier|public
name|void
name|testRemoveFromValuesRemovesFromInverse
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveFromInverseRemovesFromForward ()
specifier|public
name|void
name|testRemoveFromInverseRemovesFromForward
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveFromInverseKeySetRemovesFromForward ()
specifier|public
name|void
name|testRemoveFromInverseKeySetRemovesFromForward
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getValue
argument_list|()
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|MapFeature
operator|.
name|Require
argument_list|(
name|SUPPORTS_REMOVE
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
DECL|method|testRemoveFromInverseValuesRemovesFromInverse ()
specifier|public
name|void
name|testRemoveFromInverseValuesRemovesFromInverse
parameter_list|()
block|{
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|remove
argument_list|(
name|samples
operator|.
name|e0
operator|.
name|getKey
argument_list|()
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
name|SUPPORTS_ITERATOR_REMOVE
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
DECL|method|testKeySetIteratorRemove ()
specifier|public
name|void
name|testKeySetIteratorRemove
parameter_list|()
block|{
name|int
name|initialSize
init|=
name|getNumElements
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|K
argument_list|>
name|iterator
init|=
name|getMap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|-
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|-
literal|1
argument_list|,
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

