begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MinimalSet
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
name|Collection
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
comment|/**  * Tests {@link java.util.Set#equals}.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|SetEqualsTester
specifier|public
class|class
name|SetEqualsTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testEquals_otherSetWithSameElements ()
specifier|public
name|void
name|testEquals_otherSetWithSameElements
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"A Set should equal any other Set containing the same elements."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
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
name|CollectionSize
operator|.
name|ZERO
argument_list|)
DECL|method|testEquals_otherSetWithDifferentElements ()
specifier|public
name|void
name|testEquals_otherSetWithDifferentElements
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|samples
argument_list|()
operator|.
name|e3
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"A Set should not equal another Set containing different elements."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|elements
argument_list|)
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
name|CollectionSize
operator|.
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testEquals_containingNull ()
specifier|public
name|void
name|testEquals_containingNull
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|elements
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"A Set should equal any other Set containing the same elements,"
operator|+
literal|" even if some elements are null."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|elements
argument_list|)
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
name|CollectionSize
operator|.
name|ZERO
argument_list|)
DECL|method|testEquals_otherContainsNull ()
specifier|public
name|void
name|testEquals_otherContainsNull
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|elements
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|E
argument_list|>
name|other
init|=
name|MinimalSet
operator|.
name|from
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Two Sets should not be equal if exactly one of them contains null."
argument_list|,
name|getSet
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
name|CollectionSize
operator|.
name|ZERO
argument_list|)
DECL|method|testEquals_smallerSet ()
specifier|public
name|void
name|testEquals_smallerSet
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|fewerElements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Sets of different sizes should not be equal."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|fewerElements
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_largerSet ()
specifier|public
name|void
name|testEquals_largerSet
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|moreElements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|+
literal|1
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Sets of different sizes should not be equal."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|moreElements
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_list ()
specifier|public
name|void
name|testEquals_list
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"A List should never equal a Set."
argument_list|,
name|getSet
argument_list|()
operator|.
name|equals
argument_list|(
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSet
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

