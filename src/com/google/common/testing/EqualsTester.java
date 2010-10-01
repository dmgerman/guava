begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|Assert
operator|.
name|assertEquals
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
name|testing
operator|.
name|Assert
operator|.
name|assertNotEquals
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
name|testing
operator|.
name|Assert
operator|.
name|assertTrue
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
name|Collections
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

begin_comment
comment|/**  * Tester for equals() and hashCode() methods of a class.  *  *<p>To use, create a new EqualsTester and add equality groups where each group  * contains objects that are supposed to be equal to each other, and objects of  * different groups are expected to be unequal. For example:  *<pre>  * new EqualsTester()  *     .addEqualityGroup("hello", "h" + "ello")  *     .addEqualityGroup("world", "wor" + "ld")  *     .addEqualityGroup(2, 1 + 1)  *     .testEquals();  *</pre>  * This tests:  *<ul>  *<li>comparing each object against itself returns true  *<li>comparing each object against null returns false  *<li>comparing each object an instance of an incompatible class returns false  *<li>comparing each pair of objects within the same equality group returns  *     true  *<li>comparing each pair of objects from different equality groups returns  *     false  *<li>the hash code of any two equal objects are equal  *</ul>  * For backward compatibility, the following usage pattern is also supported:  *<ol>  *<li>Create a reference instance of the class under test and use to create a  * new EqualsTester.  *  *<li>Create one or more new instances of the class that should be equal to the  * reference instance and pass to addEqualObject(). Multiple instances can be  * used to test subclasses.  *  *<li>Create one or more new instances that should not be equal to the  * reference instance and pass to addNotEqualObject. For complete testing,  * you should add one instance that varies in each aspect checked by equals().  *  *<li>Invoke {@link #testEquals} on the EqualsTester.  *</ol>  *  * @author jmcmaster@google.com (Jim McMaster)  * @author benyu@google.com (Jige Yu)  */
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
DECL|field|defaultEqualObjects
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|defaultEqualObjects
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|defaultNotEqualObjects
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|defaultNotEqualObjects
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
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
comment|/**    * Constructs an empty EqualsTester instance    */
DECL|method|EqualsTester ()
specifier|public
name|EqualsTester
parameter_list|()
block|{
name|equalityGroups
operator|.
name|add
argument_list|(
name|defaultEqualObjects
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a new EqualsTester for a given reference object    *    * @param reference reference object for comparison    */
DECL|method|EqualsTester (Object reference)
specifier|public
name|EqualsTester
parameter_list|(
name|Object
name|reference
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|checkNotNull
argument_list|(
name|reference
argument_list|,
literal|"Reference object cannot be null"
argument_list|)
expr_stmt|;
name|defaultEqualObjects
operator|.
name|add
argument_list|(
name|reference
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
comment|/**    * Add one or more objects that should be equal to the reference object    */
DECL|method|addEqualObject (Object... equalObjects)
specifier|public
name|EqualsTester
name|addEqualObject
parameter_list|(
name|Object
modifier|...
name|equalObjects
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|equalObjects
argument_list|)
expr_stmt|;
name|defaultEqualObjects
operator|.
name|addAll
argument_list|(
name|list
argument_list|(
name|equalObjects
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Add one or more objects that should not be equal to the reference object.    */
DECL|method|addNotEqualObject (Object... notEqualObjects)
specifier|public
name|EqualsTester
name|addNotEqualObject
parameter_list|(
name|Object
modifier|...
name|notEqualObjects
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|notEqualObjects
argument_list|)
expr_stmt|;
name|defaultNotEqualObjects
operator|.
name|addAll
argument_list|(
name|list
argument_list|(
name|notEqualObjects
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
name|assertEquality
argument_list|()
expr_stmt|;
name|assertInequality
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|assertEquality ()
specifier|private
name|void
name|assertEquality
parameter_list|()
block|{
comment|// Objects in defaultNotEqualObjects don't have to be equal to each other
comment|// for backward compatibility
for|for
control|(
name|Iterable
argument_list|<
name|Object
argument_list|>
name|group
range|:
name|equalityGroups
control|)
block|{
for|for
control|(
name|Object
name|reference
range|:
name|group
control|)
block|{
name|assertNotEquals
argument_list|(
name|reference
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reference
operator|+
literal|" is expected to be equal to itself"
argument_list|,
name|reference
operator|.
name|equals
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|reference
argument_list|,
name|NotAnInstance
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|right
range|:
name|group
control|)
block|{
if|if
condition|(
name|reference
operator|!=
name|right
condition|)
block|{
name|assertEquals
argument_list|(
name|reference
operator|+
literal|" is expected to be equal to "
operator|+
name|right
argument_list|,
name|reference
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|reference
operator|+
literal|" hash code is expected to be equal to "
operator|+
name|right
operator|+
literal|" hash code"
argument_list|,
name|reference
operator|.
name|hashCode
argument_list|()
argument_list|,
name|right
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|assertInequality ()
specifier|private
name|void
name|assertInequality
parameter_list|()
block|{
comment|// defaultNotEqualObjects should participate in inequality test with other
comment|// equality groups.
name|Iterable
argument_list|<
name|List
argument_list|<
name|Object
argument_list|>
argument_list|>
name|inequalityGroups
init|=
name|Iterables
operator|.
name|concat
argument_list|(
name|equalityGroups
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|defaultNotEqualObjects
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterable
argument_list|<
name|Object
argument_list|>
name|group
range|:
name|inequalityGroups
control|)
block|{
for|for
control|(
name|Iterable
argument_list|<
name|Object
argument_list|>
name|anotherGroup
range|:
name|inequalityGroups
control|)
block|{
comment|// compare every two equality groups
if|if
condition|(
name|group
operator|==
name|anotherGroup
condition|)
block|{
comment|// same group, ignore
continue|continue;
block|}
for|for
control|(
name|Object
name|left
range|:
name|group
control|)
block|{
for|for
control|(
name|Object
name|right
range|:
name|anotherGroup
control|)
block|{
comment|// No two objects from different equality group can be equal
name|assertNotEquals
argument_list|(
name|left
argument_list|,
name|right
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|list (Object... objects)
specifier|private
specifier|static
name|List
argument_list|<
name|Object
argument_list|>
name|list
parameter_list|(
name|Object
modifier|...
name|objects
parameter_list|)
block|{
comment|//return Preconditions.checkContentsNotNull(Arrays.asList(objects));
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|objects
argument_list|)
return|;
block|}
comment|/**    * Class used to test whether equals() correctly handles an instance    * of an incompatible class.  Since it is a private inner class, the    * invoker can never pass in an instance to the tester    */
DECL|class|NotAnInstance
specifier|private
specifier|static
specifier|final
class|class
name|NotAnInstance
block|{
DECL|field|SINGLETON
specifier|static
specifier|final
name|NotAnInstance
name|SINGLETON
init|=
operator|new
name|NotAnInstance
argument_list|()
decl_stmt|;
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"equal_to_nothing"
return|;
block|}
block|}
block|}
end_class

end_unit

