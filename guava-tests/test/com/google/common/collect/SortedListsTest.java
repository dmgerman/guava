begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the  * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  * express or implied. See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

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
name|SortedLists
operator|.
name|KeyAbsentBehavior
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
name|SortedLists
operator|.
name|KeyPresentBehavior
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
name|testing
operator|.
name|NullPointerTester
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for SortedLists.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SortedListsTest
specifier|public
class|class
name|SortedListsTest
extends|extends
name|TestCase
block|{
DECL|field|LIST_WITH_DUPS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|LIST_WITH_DUPS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|4
argument_list|,
literal|4
argument_list|,
literal|8
argument_list|)
decl_stmt|;
DECL|field|LIST_WITHOUT_DUPS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|Integer
argument_list|>
name|LIST_WITHOUT_DUPS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|,
literal|8
argument_list|)
decl_stmt|;
DECL|method|assertModelAgrees ( List<Integer> list, Integer key, int answer, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
name|void
name|assertModelAgrees
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|list
parameter_list|,
name|Integer
name|key
parameter_list|,
name|int
name|answer
parameter_list|,
name|KeyPresentBehavior
name|presentBehavior
parameter_list|,
name|KeyAbsentBehavior
name|absentBehavior
parameter_list|)
block|{
switch|switch
condition|(
name|presentBehavior
condition|)
block|{
case|case
name|FIRST_PRESENT
case|:
if|if
condition|(
name|list
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|list
operator|.
name|indexOf
argument_list|(
name|key
argument_list|)
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
case|case
name|LAST_PRESENT
case|:
if|if
condition|(
name|list
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|list
operator|.
name|lastIndexOf
argument_list|(
name|key
argument_list|)
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
case|case
name|ANY_PRESENT
case|:
if|if
condition|(
name|list
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|key
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|answer
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
case|case
name|FIRST_AFTER
case|:
if|if
condition|(
name|list
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|list
operator|.
name|lastIndexOf
argument_list|(
name|key
argument_list|)
operator|+
literal|1
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
case|case
name|LAST_BEFORE
case|:
if|if
condition|(
name|list
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|list
operator|.
name|indexOf
argument_list|(
name|key
argument_list|)
operator|-
literal|1
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
comment|// key is not present
name|int
name|nextHigherIndex
init|=
name|list
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
operator|&&
name|list
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|>
name|key
condition|;
name|i
operator|--
control|)
block|{
name|nextHigherIndex
operator|=
name|i
expr_stmt|;
block|}
switch|switch
condition|(
name|absentBehavior
condition|)
block|{
case|case
name|NEXT_LOWER
case|:
name|assertEquals
argument_list|(
name|nextHigherIndex
operator|-
literal|1
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
case|case
name|NEXT_HIGHER
case|:
name|assertEquals
argument_list|(
name|nextHigherIndex
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
case|case
name|INVERTED_INSERTION_INDEX
case|:
name|assertEquals
argument_list|(
operator|-
literal|1
operator|-
name|nextHigherIndex
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
DECL|method|testWithoutDups ()
specifier|public
name|void
name|testWithoutDups
parameter_list|()
block|{
for|for
control|(
name|KeyPresentBehavior
name|presentBehavior
range|:
name|KeyPresentBehavior
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|KeyAbsentBehavior
name|absentBehavior
range|:
name|KeyAbsentBehavior
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|int
name|key
init|=
literal|0
init|;
name|key
operator|<=
literal|10
condition|;
name|key
operator|++
control|)
block|{
name|assertModelAgrees
argument_list|(
name|LIST_WITHOUT_DUPS
argument_list|,
name|key
argument_list|,
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|LIST_WITHOUT_DUPS
argument_list|,
name|key
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|testWithDups ()
specifier|public
name|void
name|testWithDups
parameter_list|()
block|{
for|for
control|(
name|KeyPresentBehavior
name|presentBehavior
range|:
name|KeyPresentBehavior
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|KeyAbsentBehavior
name|absentBehavior
range|:
name|KeyAbsentBehavior
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|int
name|key
init|=
literal|0
init|;
name|key
operator|<=
literal|10
condition|;
name|key
operator|++
control|)
block|{
name|assertModelAgrees
argument_list|(
name|LIST_WITH_DUPS
argument_list|,
name|key
argument_list|,
name|SortedLists
operator|.
name|binarySearch
argument_list|(
name|LIST_WITH_DUPS
argument_list|,
name|key
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
argument_list|,
name|presentBehavior
argument_list|,
name|absentBehavior
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|SortedLists
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

