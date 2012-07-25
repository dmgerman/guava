begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *   * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|IteratorTester
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Tester to make sure the {@code iterator().remove()} implementation of {@code Multiset} works when  * there are multiple occurrences of elements.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|MultisetIteratorTester
specifier|public
class|class
name|MultisetIteratorTester
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
block|{
name|SUPPORTS_REMOVE
block|,
name|KNOWN_ORDER
block|}
argument_list|)
DECL|method|testRemovingIteratorKnownOrder ()
specifier|public
name|void
name|testRemovingIteratorKnownOrder
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
literal|4
argument_list|,
name|IteratorFeature
operator|.
name|MODIFIABLE
argument_list|,
name|getSubjectGenerator
argument_list|()
operator|.
name|order
argument_list|(
name|Arrays
operator|.
name|asList
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|create
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|SUPPORTS_REMOVE
argument_list|,
name|absent
operator|=
name|KNOWN_ORDER
argument_list|)
DECL|method|testRemovingIteratorUnknownOrder ()
specifier|public
name|void
name|testRemovingIteratorUnknownOrder
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
literal|4
argument_list|,
name|IteratorFeature
operator|.
name|MODIFIABLE
argument_list|,
name|Arrays
operator|.
name|asList
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|create
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|value
operator|=
name|KNOWN_ORDER
argument_list|,
name|absent
operator|=
name|SUPPORTS_REMOVE
argument_list|)
DECL|method|testIteratorKnownOrder ()
specifier|public
name|void
name|testIteratorKnownOrder
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
literal|4
argument_list|,
name|IteratorFeature
operator|.
name|UNMODIFIABLE
argument_list|,
name|getSubjectGenerator
argument_list|()
operator|.
name|order
argument_list|(
name|Arrays
operator|.
name|asList
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|create
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|SUPPORTS_REMOVE
block|,
name|KNOWN_ORDER
block|}
argument_list|)
DECL|method|testIteratorUnknownOrder ()
specifier|public
name|void
name|testIteratorUnknownOrder
parameter_list|()
block|{
operator|new
name|IteratorTester
argument_list|<
name|E
argument_list|>
argument_list|(
literal|4
argument_list|,
name|IteratorFeature
operator|.
name|UNMODIFIABLE
argument_list|,
name|Arrays
operator|.
name|asList
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
argument_list|,
name|IteratorTester
operator|.
name|KnownOrder
operator|.
name|UNKNOWN_ORDER
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|Iterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|create
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
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

