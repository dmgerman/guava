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

begin_comment
comment|/**  * A generic JUnit test which tests {@code remove(Object)} operations on a list.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.ListTestSuiteBuilder}.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListRemoveTester
specifier|public
class|class
name|ListRemoveTester
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
name|SUPPORTS_REMOVE
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ZERO
block|,
name|ONE
block|}
argument_list|)
DECL|method|testRemove_duplicate ()
specifier|public
name|void
name|testRemove_duplicate
parameter_list|()
block|{
name|ArrayWithDuplicate
argument_list|<
name|E
argument_list|>
name|arrayAndDuplicate
init|=
name|createArrayWithDuplicateElement
argument_list|()
decl_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|arrayAndDuplicate
operator|.
name|elements
argument_list|)
expr_stmt|;
name|E
name|duplicate
init|=
name|arrayAndDuplicate
operator|.
name|duplicate
decl_stmt|;
name|int
name|firstIndex
init|=
name|getList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|duplicate
argument_list|)
decl_stmt|;
name|int
name|initialSize
init|=
name|getList
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"remove(present) should return true"
argument_list|,
name|getList
argument_list|()
operator|.
name|remove
argument_list|(
name|duplicate
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"After remove(duplicate), a list should still contain "
operator|+
literal|"the duplicate element"
argument_list|,
name|getList
argument_list|()
operator|.
name|contains
argument_list|(
name|duplicate
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"remove(duplicate) should remove the first instance of the "
operator|+
literal|"duplicate element in the list"
argument_list|,
name|firstIndex
operator|==
name|getList
argument_list|()
operator|.
name|indexOf
argument_list|(
name|duplicate
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remove(present) should decrease the size of a list by one."
argument_list|,
name|initialSize
operator|-
literal|1
argument_list|,
name|getList
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

