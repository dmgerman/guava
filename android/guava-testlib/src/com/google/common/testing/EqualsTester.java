begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Iterables
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

begin_comment
comment|/**  * Tester for equals() and hashCode() methods of a class.  *  *<p>The simplest use case is:  *  *<pre>  * new EqualsTester().addEqualityGroup(foo).testEquals();  *</pre>  *  *<p>This tests {@code foo.equals(foo)}, {@code foo.equals(null)}, and a few  * other operations.  *  *<p>For more extensive testing, add multiple equality groups. Each group  * should contain objects that are equal to each other but unequal to the  * objects in any other group. For example:  *<pre>  * new EqualsTester()  *     .addEqualityGroup(new User("page"), new User("page"))  *     .addEqualityGroup(new User("sergey"))  *     .testEquals();  *</pre>  *<p>This tests:  *<ul>  *<li>comparing each object against itself returns true  *<li>comparing each object against null returns false  *<li>comparing each object against an instance of an incompatible class  *     returns false  *<li>comparing each pair of objects within the same equality group returns  *     true  *<li>comparing each pair of objects from different equality groups returns  *     false  *<li>the hash codes of any two equal objects are equal  *</ul>  *  *<p>When a test fails, the error message labels the objects involved in  * the failed comparison as follows:  *<ul>  *<li>"{@code [group }<i>i</i>{@code , item }<i>j</i>{@code ]}" refers to the  *<i>j</i><sup>th</sup> item in the<i>i</i><sup>th</sup> equality group,  *       where both equality groups and the items within equality groups are  *       numbered starting from 1.  When either a constructor argument or an  *       equal object is provided, that becomes group 1.  *</ul>  *  * @author Jim McMaster  * @author Jige Yu  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|EqualsTester
specifier|public
specifier|final
class|class
name|EqualsTester
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
DECL|field|equalityGroups
specifier|private
specifier|final
name|List
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|equalityGroups
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|itemReporter
specifier|private
specifier|final
name|RelationshipTester
operator|.
name|ItemReporter
name|itemReporter
decl_stmt|;
comment|/**    * Constructs an empty EqualsTester instance    */
DECL|method|EqualsTester ()
specifier|public
name|EqualsTester
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|RelationshipTester
operator|.
name|ItemReporter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|EqualsTester (RelationshipTester.ItemReporter itemReporter)
name|EqualsTester
parameter_list|(
name|RelationshipTester
operator|.
name|ItemReporter
name|itemReporter
parameter_list|)
block|{
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
comment|/**    * Adds {@code equalityGroup} with objects that are supposed to be equal to    * each other and not equal to any other equality groups added to this tester.    */
DECL|method|addEqualityGroup (Object... equalityGroup)
specifier|public
name|EqualsTester
name|addEqualityGroup
parameter_list|(
name|Object
modifier|...
name|equalityGroup
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|equalityGroup
argument_list|)
expr_stmt|;
name|equalityGroups
operator|.
name|add
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|equalityGroup
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Run tests on equals method, throwing a failure on an invalid test    */
DECL|method|testEquals ()
specifier|public
name|EqualsTester
name|testEquals
parameter_list|()
block|{
name|RelationshipTester
argument_list|<
name|Object
argument_list|>
name|delegate
init|=
operator|new
name|RelationshipTester
argument_list|<>
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|,
literal|"Object#equals"
argument_list|,
literal|"Object#hashCode"
argument_list|,
name|itemReporter
argument_list|)
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|Object
argument_list|>
name|group
range|:
name|equalityGroups
control|)
block|{
name|delegate
operator|.
name|addRelatedGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
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
name|Object
name|item
range|:
name|Iterables
operator|.
name|concat
argument_list|(
name|equalityGroups
argument_list|)
control|)
block|{
name|assertTrue
argument_list|(
name|item
operator|+
literal|" must not be Object#equals to null"
argument_list|,
operator|!
name|item
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|item
operator|+
literal|" must not be Object#equals to an arbitrary object of another class"
argument_list|,
operator|!
name|item
operator|.
name|equals
argument_list|(
name|NotAnInstance
operator|.
name|EQUAL_TO_NOTHING
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|item
operator|+
literal|" must be Object#equals to itself"
argument_list|,
name|item
argument_list|,
name|item
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"the Object#hashCode of "
operator|+
name|item
operator|+
literal|" must be consistent"
argument_list|,
name|item
operator|.
name|hashCode
argument_list|()
argument_list|,
name|item
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Class used to test whether equals() correctly handles an instance    * of an incompatible class.  Since it is a private inner class, the    * invoker can never pass in an instance to the tester    */
DECL|enum|NotAnInstance
specifier|private
enum|enum
name|NotAnInstance
block|{
DECL|enumConstant|EQUAL_TO_NOTHING
name|EQUAL_TO_NOTHING
block|;   }
block|}
end_class

end_unit

