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
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertTrue
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
name|Beta
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|RelationshipTester
operator|.
name|ItemReporter
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
comment|/**  * Tester for {@link Equivalence} relationships between groups of objects.  *  *<p>To use, create a new {@link EquivalenceTester} and add equivalence groups where each group  * contains objects that are supposed to be equal to each other. Objects of different groups are  * expected to be unequal. For example:  *  *<pre>{@code  * EquivalenceTester.of(someStringEquivalence)  *     .addEquivalenceGroup("hello", "h" + "ello")  *     .addEquivalenceGroup("world", "wor" + "ld")  *     .test();  * }</pre>  *  *<p>Note that testing {@link Object#equals(Object)} is more simply done using the {@link  * EqualsTester}. It includes an extra test against an instance of an arbitrary class without having  * to explicitly add another equivalence group.  *  * @author Gregory Kick  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|EquivalenceTester
specifier|public
specifier|final
class|class
name|EquivalenceTester
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|REPETITIONS
specifier|private
specifier|static
specifier|final
name|int
name|REPETITIONS
init|=
literal|3
decl_stmt|;
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
DECL|field|delegate
specifier|private
specifier|final
name|RelationshipTester
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|items
specifier|private
specifier|final
name|List
argument_list|<
name|T
argument_list|>
name|items
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|EquivalenceTester (Equivalence<? super T> equivalence)
specifier|private
name|EquivalenceTester
parameter_list|(
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
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
name|delegate
operator|=
operator|new
name|RelationshipTester
argument_list|<
name|T
argument_list|>
argument_list|(
name|equivalence
argument_list|,
literal|"equivalent"
argument_list|,
literal|"hash"
argument_list|,
operator|new
name|ItemReporter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|of (Equivalence<? super T> equivalence)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|EquivalenceTester
argument_list|<
name|T
argument_list|>
name|of
parameter_list|(
name|Equivalence
argument_list|<
name|?
super|super
name|T
argument_list|>
name|equivalence
parameter_list|)
block|{
return|return
operator|new
name|EquivalenceTester
argument_list|<
name|T
argument_list|>
argument_list|(
name|equivalence
argument_list|)
return|;
block|}
comment|/**    * Adds a group of objects that are supposed to be equivalent to each other and not equivalent to    * objects in any other equivalence group added to this tester.    */
DECL|method|addEquivalenceGroup (T first, T... rest)
specifier|public
name|EquivalenceTester
argument_list|<
name|T
argument_list|>
name|addEquivalenceGroup
parameter_list|(
name|T
name|first
parameter_list|,
name|T
modifier|...
name|rest
parameter_list|)
block|{
name|addEquivalenceGroup
argument_list|(
name|Lists
operator|.
name|asList
argument_list|(
name|first
argument_list|,
name|rest
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addEquivalenceGroup (Iterable<T> group)
specifier|public
name|EquivalenceTester
argument_list|<
name|T
argument_list|>
name|addEquivalenceGroup
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|group
parameter_list|)
block|{
name|delegate
operator|.
name|addRelatedGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|items
operator|.
name|addAll
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
comment|/** Run tests on equivalence methods, throwing a failure on an invalid test */
DECL|method|test ()
specifier|public
name|EquivalenceTester
argument_list|<
name|T
argument_list|>
name|test
parameter_list|()
block|{
for|for
control|(
name|int
name|run
init|=
literal|0
init|;
name|run
operator|<
name|REPETITIONS
condition|;
name|run
operator|++
control|)
block|{
name|testItems
argument_list|()
expr_stmt|;
name|delegate
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|testItems ()
specifier|private
name|void
name|testItems
parameter_list|()
block|{
for|for
control|(
name|T
name|item
range|:
name|items
control|)
block|{
comment|/*        * TODO(cpovirk): consider no longer running these equivalent() tests on every Equivalence,        * since the Equivalence base type now implements this logic itself        */
name|assertTrue
argument_list|(
name|item
operator|+
literal|" must be inequivalent to null"
argument_list|,
operator|!
name|equivalence
operator|.
name|equivalent
argument_list|(
name|item
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"null must be inequivalent to "
operator|+
name|item
argument_list|,
operator|!
name|equivalence
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
name|item
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|item
operator|+
literal|" must be equivalent to itself"
argument_list|,
name|equivalence
operator|.
name|equivalent
argument_list|(
name|item
argument_list|,
name|item
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"the hash of "
operator|+
name|item
operator|+
literal|" must be consistent"
argument_list|,
name|equivalence
operator|.
name|hash
argument_list|(
name|item
argument_list|)
argument_list|,
name|equivalence
operator|.
name|hash
argument_list|(
name|item
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

