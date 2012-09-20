begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|ImmutableList
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
name|Lists
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
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
comment|/**  * Tests a collection of objects according to the rules specified in a  * {@link RelationshipAssertion}.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|RelationshipTester
specifier|final
class|class
name|RelationshipTester
parameter_list|<
name|T
parameter_list|>
block|{
DECL|class|ItemReporter
specifier|static
class|class
name|ItemReporter
block|{
DECL|method|reportItem (Item item)
name|String
name|reportItem
parameter_list|(
name|Item
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|field|groups
specifier|private
specifier|final
name|List
argument_list|<
name|ImmutableList
argument_list|<
name|T
argument_list|>
argument_list|>
name|groups
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|assertion
specifier|private
specifier|final
name|RelationshipAssertion
argument_list|<
name|T
argument_list|>
name|assertion
decl_stmt|;
DECL|field|itemReporter
specifier|private
specifier|final
name|ItemReporter
name|itemReporter
decl_stmt|;
DECL|method|RelationshipTester (RelationshipAssertion<T> assertion, ItemReporter itemReporter)
name|RelationshipTester
parameter_list|(
name|RelationshipAssertion
argument_list|<
name|T
argument_list|>
name|assertion
parameter_list|,
name|ItemReporter
name|itemReporter
parameter_list|)
block|{
name|this
operator|.
name|assertion
operator|=
name|checkNotNull
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
name|this
operator|.
name|itemReporter
operator|=
name|checkNotNull
argument_list|(
name|itemReporter
argument_list|)
expr_stmt|;
block|}
DECL|method|RelationshipTester (RelationshipAssertion<T> assertion)
name|RelationshipTester
parameter_list|(
name|RelationshipAssertion
argument_list|<
name|T
argument_list|>
name|assertion
parameter_list|)
block|{
name|this
argument_list|(
name|assertion
argument_list|,
operator|new
name|ItemReporter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|addRelatedGroup (Iterable<? extends T> group)
specifier|public
name|RelationshipTester
argument_list|<
name|T
argument_list|>
name|addRelatedGroup
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|group
parameter_list|)
block|{
name|groups
operator|.
name|add
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{
for|for
control|(
name|int
name|groupNumber
init|=
literal|0
init|;
name|groupNumber
operator|<
name|groups
operator|.
name|size
argument_list|()
condition|;
name|groupNumber
operator|++
control|)
block|{
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|group
init|=
name|groups
operator|.
name|get
argument_list|(
name|groupNumber
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|itemNumber
init|=
literal|0
init|;
name|itemNumber
operator|<
name|group
operator|.
name|size
argument_list|()
condition|;
name|itemNumber
operator|++
control|)
block|{
comment|// check related items in same group
for|for
control|(
name|int
name|relatedItemNumber
init|=
literal|0
init|;
name|relatedItemNumber
operator|<
name|group
operator|.
name|size
argument_list|()
condition|;
name|relatedItemNumber
operator|++
control|)
block|{
if|if
condition|(
name|itemNumber
operator|!=
name|relatedItemNumber
condition|)
block|{
name|assertRelated
argument_list|(
name|groupNumber
argument_list|,
name|itemNumber
argument_list|,
name|relatedItemNumber
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check unrelated items in all other groups
for|for
control|(
name|int
name|unrelatedGroupNumber
init|=
literal|0
init|;
name|unrelatedGroupNumber
operator|<
name|groups
operator|.
name|size
argument_list|()
condition|;
name|unrelatedGroupNumber
operator|++
control|)
block|{
if|if
condition|(
name|groupNumber
operator|!=
name|unrelatedGroupNumber
condition|)
block|{
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|unrelatedGroup
init|=
name|groups
operator|.
name|get
argument_list|(
name|unrelatedGroupNumber
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|unrelatedItemNumber
init|=
literal|0
init|;
name|unrelatedItemNumber
operator|<
name|unrelatedGroup
operator|.
name|size
argument_list|()
condition|;
name|unrelatedItemNumber
operator|++
control|)
block|{
name|assertUnrelated
argument_list|(
name|groupNumber
argument_list|,
name|itemNumber
argument_list|,
name|unrelatedGroupNumber
argument_list|,
name|unrelatedItemNumber
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
DECL|method|assertRelated (int groupNumber, int itemNumber, int relatedItemNumber)
specifier|private
name|void
name|assertRelated
parameter_list|(
name|int
name|groupNumber
parameter_list|,
name|int
name|itemNumber
parameter_list|,
name|int
name|relatedItemNumber
parameter_list|)
block|{
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|group
init|=
name|groups
operator|.
name|get
argument_list|(
name|groupNumber
argument_list|)
decl_stmt|;
name|T
name|item
init|=
name|group
operator|.
name|get
argument_list|(
name|itemNumber
argument_list|)
decl_stmt|;
name|T
name|related
init|=
name|group
operator|.
name|get
argument_list|(
name|relatedItemNumber
argument_list|)
decl_stmt|;
try|try
block|{
name|assertion
operator|.
name|assertRelated
argument_list|(
name|item
argument_list|,
name|related
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
comment|// TODO(gak): special handling for ComparisonFailure?
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|replace
argument_list|(
literal|"$ITEM"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
operator|new
name|Item
argument_list|(
name|item
argument_list|,
name|groupNumber
argument_list|,
name|itemNumber
argument_list|)
argument_list|)
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$RELATED"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
operator|new
name|Item
argument_list|(
name|related
argument_list|,
name|groupNumber
argument_list|,
name|relatedItemNumber
argument_list|)
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|assertUnrelated (int groupNumber, int itemNumber, int unrelatedGroupNumber, int unrelatedItemNumber)
specifier|private
name|void
name|assertUnrelated
parameter_list|(
name|int
name|groupNumber
parameter_list|,
name|int
name|itemNumber
parameter_list|,
name|int
name|unrelatedGroupNumber
parameter_list|,
name|int
name|unrelatedItemNumber
parameter_list|)
block|{
name|T
name|item
init|=
name|groups
operator|.
name|get
argument_list|(
name|groupNumber
argument_list|)
operator|.
name|get
argument_list|(
name|itemNumber
argument_list|)
decl_stmt|;
name|T
name|unrelated
init|=
name|groups
operator|.
name|get
argument_list|(
name|unrelatedGroupNumber
argument_list|)
operator|.
name|get
argument_list|(
name|unrelatedItemNumber
argument_list|)
decl_stmt|;
try|try
block|{
name|assertion
operator|.
name|assertUnrelated
argument_list|(
name|item
argument_list|,
name|unrelated
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AssertionFailedError
name|e
parameter_list|)
block|{
comment|// TODO(gak): special handling for ComparisonFailure?
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|replace
argument_list|(
literal|"$ITEM"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
operator|new
name|Item
argument_list|(
name|item
argument_list|,
name|groupNumber
argument_list|,
name|itemNumber
argument_list|)
argument_list|)
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$UNRELATED"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
operator|new
name|Item
argument_list|(
name|unrelated
argument_list|,
name|unrelatedGroupNumber
argument_list|,
name|unrelatedItemNumber
argument_list|)
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|class|Item
specifier|static
specifier|final
class|class
name|Item
block|{
DECL|field|value
specifier|final
name|Object
name|value
decl_stmt|;
DECL|field|groupNumber
specifier|final
name|int
name|groupNumber
decl_stmt|;
DECL|field|itemNumber
specifier|final
name|int
name|itemNumber
decl_stmt|;
DECL|method|Item (Object value, int groupNumber, int itemNumber)
name|Item
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|groupNumber
parameter_list|,
name|int
name|itemNumber
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|groupNumber
operator|=
name|groupNumber
expr_stmt|;
name|this
operator|.
name|itemNumber
operator|=
name|itemNumber
expr_stmt|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|" [group "
argument_list|)
operator|.
name|append
argument_list|(
name|groupNumber
operator|+
literal|1
argument_list|)
operator|.
name|append
argument_list|(
literal|", item "
argument_list|)
operator|.
name|append
argument_list|(
name|itemNumber
operator|+
literal|1
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**    * A strategy for testing the relationship between objects.  Methods are expected to throw    * {@link AssertionFailedError} whenever the relationship is violated.    *    *<p>As a convenience, any occurrence of {@code $ITEM}, {@code $RELATED} or {@code $UNRELATED} in    * the error message will be replaced with a string that combines the {@link Object#toString()},    * item number and group number of the respective item.    *    */
DECL|class|RelationshipAssertion
specifier|static
specifier|abstract
class|class
name|RelationshipAssertion
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|assertRelated (T item, T related)
specifier|abstract
name|void
name|assertRelated
parameter_list|(
name|T
name|item
parameter_list|,
name|T
name|related
parameter_list|)
function_decl|;
DECL|method|assertUnrelated (T item, T unrelated)
specifier|abstract
name|void
name|assertUnrelated
parameter_list|(
name|T
name|item
parameter_list|,
name|T
name|unrelated
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

