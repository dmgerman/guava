begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|List
import|;
end_import

begin_comment
comment|/**  * Tests {@link List#equals}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|ListEqualsTester
specifier|public
class|class
name|ListEqualsTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractListTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testEquals_otherListWithSameElements ()
specifier|public
name|void
name|testEquals_otherListWithSameElements
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"A List should equal any other List containing the same elements."
argument_list|,
name|getList
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
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
DECL|method|testEquals_otherListWithDifferentElements ()
specifier|public
name|void
name|testEquals_otherListWithDifferentElements
parameter_list|()
block|{
name|ArrayList
argument_list|<
name|E
argument_list|>
name|other
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|other
operator|.
name|set
argument_list|(
name|other
operator|.
name|size
argument_list|()
operator|/
literal|2
argument_list|,
name|getSubjectGenerator
argument_list|()
operator|.
name|samples
argument_list|()
operator|.
name|e3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"A List should not equal another List containing different elements."
argument_list|,
name|getList
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
DECL|method|testEquals_otherListContainingNull ()
specifier|public
name|void
name|testEquals_otherListContainingNull
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|other
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|other
operator|.
name|set
argument_list|(
name|other
operator|.
name|size
argument_list|()
operator|/
literal|2
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Two Lists should not be equal if exactly one of them has "
operator|+
literal|"null at a given index."
argument_list|,
name|getList
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
name|ArrayList
argument_list|<
name|E
argument_list|>
name|elements
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|elements
operator|.
name|set
argument_list|(
name|elements
operator|.
name|size
argument_list|()
operator|/
literal|2
argument_list|,
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
name|List
argument_list|<
name|E
argument_list|>
name|other
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Two Lists should not be equal if exactly one of them has "
operator|+
literal|"null at a given index."
argument_list|,
name|getList
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
DECL|method|testEquals_shorterList ()
specifier|public
name|void
name|testEquals_shorterList
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
literal|"Lists of different sizes should not be equal."
argument_list|,
name|getList
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|fewerElements
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_longerList ()
specifier|public
name|void
name|testEquals_longerList
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
literal|"Lists of different sizes should not be equal."
argument_list|,
name|getList
argument_list|()
operator|.
name|equals
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|moreElements
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_set ()
specifier|public
name|void
name|testEquals_set
parameter_list|()
block|{
name|assertFalse
argument_list|(
literal|"A List should never equal a Set."
argument_list|,
name|getList
argument_list|()
operator|.
name|equals
argument_list|(
name|MinimalSet
operator|.
name|from
argument_list|(
name|getList
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

