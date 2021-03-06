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
name|base
operator|.
name|Equivalence
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
name|AssertionFailedError
import|;
end_import

begin_comment
comment|/**  * Implementation helper for {@link EqualsTester} and {@link EquivalenceTester} that tests for  * equivalence classes.  *  * @author Gregory Kick  */
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
DECL|method|reportItem (Item<?> item)
name|String
name|reportItem
parameter_list|(
name|Item
argument_list|<
name|?
argument_list|>
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
comment|/**    * A word about using {@link Equivalence}, which automatically checks for {@code null} and    * identical inputs: This sounds like it ought to be a problem here, since the goals of this class    * include testing that {@code equals()} is reflexive and is tolerant of {@code null}. However,    * there's no problem. The reason: {@link EqualsTester} tests {@code null} and identical inputs    * directly against {@code equals()} rather than through the {@code Equivalence}.    */
DECL|field|equivalence
specifier|private
specifier|final
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
decl_stmt|;
DECL|field|relationshipName
specifier|private
specifier|final
name|String
name|relationshipName
decl_stmt|;
DECL|field|hashName
specifier|private
specifier|final
name|String
name|hashName
decl_stmt|;
DECL|field|itemReporter
specifier|private
specifier|final
name|ItemReporter
name|itemReporter
decl_stmt|;
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
DECL|method|RelationshipTester ( Equivalence<? super T> equivalence, String relationshipName, String hashName, ItemReporter itemReporter)
name|RelationshipTester
parameter_list|(
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
parameter_list|,
name|String
name|relationshipName
parameter_list|,
name|String
name|hashName
parameter_list|,
name|ItemReporter
name|itemReporter
parameter_list|)
block|{
name|this
operator|.
name|equivalence
operator|=
name|checkNotNull
argument_list|(
name|equivalence
argument_list|)
expr_stmt|;
name|this
operator|.
name|relationshipName
operator|=
name|checkNotNull
argument_list|(
name|relationshipName
argument_list|)
expr_stmt|;
name|this
operator|.
name|hashName
operator|=
name|checkNotNull
argument_list|(
name|hashName
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
comment|// TODO(cpovirk): should we reject null items, since the tests already check null automatically?
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
name|Item
argument_list|<
name|T
argument_list|>
name|itemInfo
init|=
name|getItem
argument_list|(
name|groupNumber
argument_list|,
name|itemNumber
argument_list|)
decl_stmt|;
name|Item
argument_list|<
name|T
argument_list|>
name|relatedInfo
init|=
name|getItem
argument_list|(
name|groupNumber
argument_list|,
name|relatedItemNumber
argument_list|)
decl_stmt|;
name|T
name|item
init|=
name|itemInfo
operator|.
name|value
decl_stmt|;
name|T
name|related
init|=
name|relatedInfo
operator|.
name|value
decl_stmt|;
name|assertWithTemplate
argument_list|(
literal|"$ITEM must be $RELATIONSHIP to $OTHER"
argument_list|,
name|itemInfo
argument_list|,
name|relatedInfo
argument_list|,
name|equivalence
operator|.
name|equivalent
argument_list|(
name|item
argument_list|,
name|related
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|itemHash
init|=
name|equivalence
operator|.
name|hash
argument_list|(
name|item
argument_list|)
decl_stmt|;
name|int
name|relatedHash
init|=
name|equivalence
operator|.
name|hash
argument_list|(
name|related
argument_list|)
decl_stmt|;
name|assertWithTemplate
argument_list|(
literal|"the $HASH ("
operator|+
name|itemHash
operator|+
literal|") of $ITEM must be equal to the $HASH ("
operator|+
name|relatedHash
operator|+
literal|") of $OTHER"
argument_list|,
name|itemInfo
argument_list|,
name|relatedInfo
argument_list|,
name|itemHash
operator|==
name|relatedHash
argument_list|)
expr_stmt|;
block|}
DECL|method|assertUnrelated ( int groupNumber, int itemNumber, int unrelatedGroupNumber, int unrelatedItemNumber)
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
name|Item
argument_list|<
name|T
argument_list|>
name|itemInfo
init|=
name|getItem
argument_list|(
name|groupNumber
argument_list|,
name|itemNumber
argument_list|)
decl_stmt|;
name|Item
argument_list|<
name|T
argument_list|>
name|unrelatedInfo
init|=
name|getItem
argument_list|(
name|unrelatedGroupNumber
argument_list|,
name|unrelatedItemNumber
argument_list|)
decl_stmt|;
name|assertWithTemplate
argument_list|(
literal|"$ITEM must not be $RELATIONSHIP to $OTHER"
argument_list|,
name|itemInfo
argument_list|,
name|unrelatedInfo
argument_list|,
operator|!
name|equivalence
operator|.
name|equivalent
argument_list|(
name|itemInfo
operator|.
name|value
argument_list|,
name|unrelatedInfo
operator|.
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertWithTemplate (String template, Item<T> item, Item<T> other, boolean condition)
specifier|private
name|void
name|assertWithTemplate
parameter_list|(
name|String
name|template
parameter_list|,
name|Item
argument_list|<
name|T
argument_list|>
name|item
parameter_list|,
name|Item
argument_list|<
name|T
argument_list|>
name|other
parameter_list|,
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
block|{
throw|throw
operator|new
name|AssertionFailedError
argument_list|(
name|template
operator|.
name|replace
argument_list|(
literal|"$RELATIONSHIP"
argument_list|,
name|relationshipName
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$HASH"
argument_list|,
name|hashName
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$ITEM"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
name|item
argument_list|)
argument_list|)
operator|.
name|replace
argument_list|(
literal|"$OTHER"
argument_list|,
name|itemReporter
operator|.
name|reportItem
argument_list|(
name|other
argument_list|)
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getItem (int groupNumber, int itemNumber)
specifier|private
name|Item
argument_list|<
name|T
argument_list|>
name|getItem
parameter_list|(
name|int
name|groupNumber
parameter_list|,
name|int
name|itemNumber
parameter_list|)
block|{
return|return
operator|new
name|Item
argument_list|<>
argument_list|(
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
argument_list|,
name|groupNumber
argument_list|,
name|itemNumber
argument_list|)
return|;
block|}
DECL|class|Item
specifier|static
specifier|final
class|class
name|Item
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|value
specifier|final
name|T
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
DECL|method|Item (T value, int groupNumber, int itemNumber)
name|Item
parameter_list|(
name|T
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|value
operator|+
literal|" [group "
operator|+
operator|(
name|groupNumber
operator|+
literal|1
operator|)
operator|+
literal|", item "
operator|+
operator|(
name|itemNumber
operator|+
literal|1
operator|)
operator|+
literal|']'
return|;
block|}
block|}
block|}
end_class

end_unit

