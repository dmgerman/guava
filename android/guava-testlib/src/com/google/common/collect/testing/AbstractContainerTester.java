begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
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
name|Arrays
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

begin_comment
comment|/**  * Base class for testers of classes (including {@link Collection}  * and {@link java.util.Map Map}) that contain elements.  *  * @param<C> the type of the container  * @param<E> the type of the container's contents  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractContainerTester
specifier|public
specifier|abstract
class|class
name|AbstractContainerTester
parameter_list|<
name|C
parameter_list|,
name|E
parameter_list|>
extends|extends
name|AbstractTester
argument_list|<
name|OneSizeTestContainerGenerator
argument_list|<
name|C
argument_list|,
name|E
argument_list|>
argument_list|>
block|{
DECL|field|samples
specifier|protected
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
decl_stmt|;
DECL|field|container
specifier|protected
name|C
name|container
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|samples
operator|=
name|this
operator|.
name|getSubjectGenerator
argument_list|()
operator|.
name|samples
argument_list|()
expr_stmt|;
name|resetContainer
argument_list|()
expr_stmt|;
block|}
comment|/**    * @return the contents of the container under test, for use by    * {@link #expectContents(Object[]) expectContents(E...)} and its friends.    */
DECL|method|actualContents ()
specifier|protected
specifier|abstract
name|Collection
argument_list|<
name|E
argument_list|>
name|actualContents
parameter_list|()
function_decl|;
comment|/**    * Replaces the existing container under test with a new container created    * by the subject generator.    *    * @see #resetContainer(Object) resetContainer(C)    *    * @return the new container instance.    */
DECL|method|resetContainer ()
specifier|protected
name|C
name|resetContainer
parameter_list|()
block|{
return|return
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|createTestSubject
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Replaces the existing container under test with a new container.    * This is useful when a single test method needs to create multiple    * containers while retaining the ability to use    * {@link #expectContents(Object[]) expectContents(E...)} and other    * convenience methods. The creation of multiple containers in a single    * method is discouraged in most cases, but it is vital to the iterator tests.    *    * @return the new container instance    * @param newValue the new container instance    */
DECL|method|resetContainer (C newValue)
specifier|protected
name|C
name|resetContainer
parameter_list|(
name|C
name|newValue
parameter_list|)
block|{
name|container
operator|=
name|newValue
expr_stmt|;
return|return
name|container
return|;
block|}
comment|/**    * @see #expectContents(java.util.Collection)    *    * @param elements expected contents of {@link #container}    */
DECL|method|expectContents (E... elements)
specifier|protected
specifier|final
name|void
name|expectContents
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|expectContents
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that the collection under test contains exactly the given elements,    * respecting cardinality but not order. Subclasses may override this method    * to provide stronger assertions, e.g., to check ordering in lists, but    * realize that<strong>unless a test extends    * {@link com.google.common.collect.testing.testers.AbstractListTester    * AbstractListTester}, a call to {@code expectContents()} invokes this    * version</strong>.    *    * @param expected expected value of {@link #container}    */
comment|/*    * TODO: improve this and other implementations and move out of this framework    * for wider use    *    * TODO: could we incorporate the overriding logic from AbstractListTester, by    * examining whether the features include KNOWN_ORDER?    */
DECL|method|expectContents (Collection<E> expected)
specifier|protected
name|void
name|expectContents
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|expected
parameter_list|)
block|{
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|expected
argument_list|,
name|actualContents
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|expectUnchanged ()
specifier|protected
name|void
name|expectUnchanged
parameter_list|()
block|{
name|expectContents
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that the collection under test contains exactly the elements it was    * initialized with plus the given elements, according to    * {@link #expectContents(java.util.Collection)}. In other words, for the    * default {@code expectContents()} implementation, the number of occurrences    * of each given element has increased by one since the test collection was    * created, and the number of occurrences of all other elements has not    * changed.    *    *<p>Note: This means that a test like the following will fail if    * {@code collection} is a {@code Set}:    *    *<pre>    * collection.add(existingElement);    * expectAdded(existingElement);</pre>    *    *<p>In this case, {@code collection} was not modified as a result of the    * {@code add()} call, and the test will fail because the number of    * occurrences of {@code existingElement} is unchanged.    *    * @param elements expected additional contents of {@link #container}    */
DECL|method|expectAdded (E... elements)
specifier|protected
specifier|final
name|void
name|expectAdded
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|expected
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|expectAdded (int index, E... elements)
specifier|protected
specifier|final
name|void
name|expectAdded
parameter_list|(
name|int
name|index
parameter_list|,
name|E
modifier|...
name|elements
parameter_list|)
block|{
name|expectAdded
argument_list|(
name|index
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|expectAdded (int index, Collection<E> elements)
specifier|protected
specifier|final
name|void
name|expectAdded
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|getSampleElements
argument_list|()
argument_list|)
decl_stmt|;
name|expected
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|elements
argument_list|)
expr_stmt|;
name|expectContents
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
comment|/*    * TODO: if we're testing a list, we could check indexOf(). (Doing it in    * AbstractListTester isn't enough because many tests that run on lists don't    * extends AbstractListTester.) We could also iterate over all elements to    * verify absence    */
DECL|method|expectMissing (E... elements)
specifier|protected
name|void
name|expectMissing
parameter_list|(
name|E
modifier|...
name|elements
parameter_list|)
block|{
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|assertFalse
argument_list|(
literal|"Should not contain "
operator|+
name|element
argument_list|,
name|actualContents
argument_list|()
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSamplesArray ()
specifier|protected
name|E
index|[]
name|createSamplesArray
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|createArray
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
decl_stmt|;
name|getSampleElements
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|createOrderedArray ()
specifier|protected
name|E
index|[]
name|createOrderedArray
parameter_list|()
block|{
name|E
index|[]
name|array
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|createArray
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
decl_stmt|;
name|getOrderedElements
argument_list|()
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|class|ArrayWithDuplicate
specifier|public
specifier|static
class|class
name|ArrayWithDuplicate
parameter_list|<
name|E
parameter_list|>
block|{
DECL|field|elements
specifier|public
specifier|final
name|E
index|[]
name|elements
decl_stmt|;
DECL|field|duplicate
specifier|public
specifier|final
name|E
name|duplicate
decl_stmt|;
DECL|method|ArrayWithDuplicate (E[] elements, E duplicate)
specifier|private
name|ArrayWithDuplicate
parameter_list|(
name|E
index|[]
name|elements
parameter_list|,
name|E
name|duplicate
parameter_list|)
block|{
name|this
operator|.
name|elements
operator|=
name|elements
expr_stmt|;
name|this
operator|.
name|duplicate
operator|=
name|duplicate
expr_stmt|;
block|}
block|}
comment|/**    * @return an array of the proper size with a duplicate element.    * The size must be at least three.    */
DECL|method|createArrayWithDuplicateElement ()
specifier|protected
name|ArrayWithDuplicate
argument_list|<
name|E
argument_list|>
name|createArrayWithDuplicateElement
parameter_list|()
block|{
name|E
index|[]
name|elements
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
name|E
name|duplicate
init|=
name|elements
index|[
operator|(
name|elements
operator|.
name|length
operator|/
literal|2
operator|)
operator|-
literal|1
index|]
decl_stmt|;
name|elements
index|[
operator|(
name|elements
operator|.
name|length
operator|/
literal|2
operator|)
operator|+
literal|1
index|]
operator|=
name|duplicate
expr_stmt|;
return|return
operator|new
name|ArrayWithDuplicate
argument_list|<
name|E
argument_list|>
argument_list|(
name|elements
argument_list|,
name|duplicate
argument_list|)
return|;
block|}
comment|// Helper methods to improve readability of derived classes
DECL|method|getNumElements ()
specifier|protected
name|int
name|getNumElements
parameter_list|()
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|getCollectionSize
argument_list|()
operator|.
name|getNumElements
argument_list|()
return|;
block|}
DECL|method|getSampleElements (int howMany)
specifier|protected
name|Collection
argument_list|<
name|E
argument_list|>
name|getSampleElements
parameter_list|(
name|int
name|howMany
parameter_list|)
block|{
return|return
name|getSubjectGenerator
argument_list|()
operator|.
name|getSampleElements
argument_list|(
name|howMany
argument_list|)
return|;
block|}
DECL|method|getSampleElements ()
specifier|protected
name|Collection
argument_list|<
name|E
argument_list|>
name|getSampleElements
parameter_list|()
block|{
return|return
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the {@linkplain #getSampleElements() sample elements} as ordered by    * {@link TestContainerGenerator#order(List)}. Tests should used this method    * only if they declare requirement {@link    * com.google.common.collect.testing.features.CollectionFeature#KNOWN_ORDER}.    */
DECL|method|getOrderedElements ()
specifier|protected
name|List
argument_list|<
name|E
argument_list|>
name|getOrderedElements
parameter_list|()
block|{
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|getSubjectGenerator
argument_list|()
operator|.
name|order
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
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|list
argument_list|)
return|;
block|}
comment|/**    * @return a suitable location for a null element, to use when initializing    * containers for tests that involve a null element being present.    */
DECL|method|getNullLocation ()
specifier|protected
name|int
name|getNullLocation
parameter_list|()
block|{
return|return
name|getNumElements
argument_list|()
operator|/
literal|2
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createDisjointCollection ()
specifier|protected
name|MinimalCollection
argument_list|<
name|E
argument_list|>
name|createDisjointCollection
parameter_list|()
block|{
return|return
name|MinimalCollection
operator|.
name|of
argument_list|(
name|e3
argument_list|()
argument_list|,
name|e4
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|emptyCollection ()
specifier|protected
name|MinimalCollection
argument_list|<
name|E
argument_list|>
name|emptyCollection
parameter_list|()
block|{
return|return
name|MinimalCollection
operator|.
expr|<
name|E
operator|>
name|of
argument_list|()
return|;
block|}
DECL|method|e0 ()
specifier|protected
specifier|final
name|E
name|e0
parameter_list|()
block|{
return|return
name|samples
operator|.
name|e0
argument_list|()
return|;
block|}
DECL|method|e1 ()
specifier|protected
specifier|final
name|E
name|e1
parameter_list|()
block|{
return|return
name|samples
operator|.
name|e1
argument_list|()
return|;
block|}
DECL|method|e2 ()
specifier|protected
specifier|final
name|E
name|e2
parameter_list|()
block|{
return|return
name|samples
operator|.
name|e2
argument_list|()
return|;
block|}
DECL|method|e3 ()
specifier|protected
specifier|final
name|E
name|e3
parameter_list|()
block|{
return|return
name|samples
operator|.
name|e3
argument_list|()
return|;
block|}
DECL|method|e4 ()
specifier|protected
specifier|final
name|E
name|e4
parameter_list|()
block|{
return|return
name|samples
operator|.
name|e4
argument_list|()
return|;
block|}
block|}
end_class

end_unit
