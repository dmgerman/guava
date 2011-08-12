begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|KNOWN_ORDER
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
name|collect
operator|.
name|testing
operator|.
name|AbstractCollectionTester
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
name|WrongType
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|List
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code toArray()} operations on a  * collection. Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  *<p>This class is GWT compatible.  *  * @author Kevin Bourrillion  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|CollectionToArrayTester
specifier|public
class|class
name|CollectionToArrayTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testToArray_noArgs ()
specifier|public
name|void
name|testToArray_noArgs
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
name|collection
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|expectArrayContentsAnyOrder
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@link Collection#toArray(Object[])} says: "Note that    *<tt>toArray(new Object[0])</tt> is identical in function to    *<tt>toArray()</tt>."    *    *<p>For maximum effect, the collection under test should be created from an    * element array of a type other than {@code Object[]}.    */
DECL|method|testToArray_isPlainObjectArray ()
specifier|public
name|void
name|testToArray_isPlainObjectArray
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
name|collection
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|array
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_emptyArray ()
specifier|public
name|void
name|testToArray_emptyArray
parameter_list|()
block|{
name|E
index|[]
name|empty
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|createArray
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|E
index|[]
name|array
init|=
name|collection
operator|.
name|toArray
argument_list|(
name|empty
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyT[]) should return an array of type T"
argument_list|,
name|empty
operator|.
name|getClass
argument_list|()
argument_list|,
name|array
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyT[]).length:"
argument_list|,
name|getNumElements
argument_list|()
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|expectArrayContentsAnyOrder
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testToArray_emptyArray_ordered ()
specifier|public
name|void
name|testToArray_emptyArray_ordered
parameter_list|()
block|{
name|E
index|[]
name|empty
init|=
name|getSubjectGenerator
argument_list|()
operator|.
name|createArray
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|E
index|[]
name|array
init|=
name|collection
operator|.
name|toArray
argument_list|(
name|empty
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyT[]) should return an array of type T"
argument_list|,
name|empty
operator|.
name|getClass
argument_list|()
argument_list|,
name|array
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyT[]).length:"
argument_list|,
name|getNumElements
argument_list|()
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|expectArrayContentsInOrder
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_emptyArrayOfObject ()
specifier|public
name|void
name|testToArray_emptyArrayOfObject
parameter_list|()
block|{
name|Object
index|[]
name|in
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
name|Object
index|[]
name|array
init|=
name|collection
operator|.
name|toArray
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyObject[]) should return an array of type Object"
argument_list|,
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|array
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"toArray(emptyObject[]).length"
argument_list|,
name|getNumElements
argument_list|()
argument_list|,
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|expectArrayContentsAnyOrder
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_rightSizedArray ()
specifier|public
name|void
name|testToArray_rightSizedArray
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
name|assertSame
argument_list|(
literal|"toArray(sameSizeE[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|expectArrayContentsAnyOrder
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testToArray_rightSizedArray_ordered ()
specifier|public
name|void
name|testToArray_rightSizedArray_ordered
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
name|assertSame
argument_list|(
literal|"toArray(sameSizeE[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|expectArrayContentsInOrder
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_rightSizedArrayOfObject ()
specifier|public
name|void
name|testToArray_rightSizedArrayOfObject
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
name|getNumElements
argument_list|()
index|]
decl_stmt|;
name|assertSame
argument_list|(
literal|"toArray(sameSizeObject[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|expectArrayContentsAnyOrder
argument_list|(
name|createSamplesArray
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testToArray_rightSizedArrayOfObject_ordered ()
specifier|public
name|void
name|testToArray_rightSizedArrayOfObject_ordered
parameter_list|()
block|{
name|Object
index|[]
name|array
init|=
operator|new
name|Object
index|[
name|getNumElements
argument_list|()
index|]
decl_stmt|;
name|assertSame
argument_list|(
literal|"toArray(sameSizeObject[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|expectArrayContentsInOrder
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
DECL|method|testToArray_oversizedArray ()
specifier|public
name|void
name|testToArray_oversizedArray
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
operator|+
literal|2
argument_list|)
decl_stmt|;
name|array
index|[
name|getNumElements
argument_list|()
index|]
operator|=
name|samples
operator|.
name|e3
expr_stmt|;
name|array
index|[
name|getNumElements
argument_list|()
operator|+
literal|1
index|]
operator|=
name|samples
operator|.
name|e3
expr_stmt|;
name|assertSame
argument_list|(
literal|"toArray(overSizedE[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|subArray
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|getNumElements
argument_list|()
argument_list|)
decl_stmt|;
name|E
index|[]
name|expectedSubArray
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getNumElements
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
literal|"toArray(overSizedE[]) should contain element "
operator|+
name|expectedSubArray
index|[
name|i
index|]
argument_list|,
name|subArray
operator|.
name|contains
argument_list|(
name|expectedSubArray
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertNull
argument_list|(
literal|"The array element "
operator|+
literal|"immediately following the end of the collection should be nulled"
argument_list|,
name|array
index|[
name|getNumElements
argument_list|()
index|]
argument_list|)
expr_stmt|;
comment|// array[getNumElements() + 1] might or might not have been nulled
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testToArray_oversizedArray_ordered ()
specifier|public
name|void
name|testToArray_oversizedArray_ordered
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
operator|+
literal|2
argument_list|)
decl_stmt|;
name|array
index|[
name|getNumElements
argument_list|()
index|]
operator|=
name|samples
operator|.
name|e3
expr_stmt|;
name|array
index|[
name|getNumElements
argument_list|()
operator|+
literal|1
index|]
operator|=
name|samples
operator|.
name|e3
expr_stmt|;
name|assertSame
argument_list|(
literal|"toArray(overSizedE[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|E
argument_list|>
name|expected
init|=
name|getOrderedElements
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getNumElements
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|assertNull
argument_list|(
literal|"The array element "
operator|+
literal|"immediately following the end of the collection should be nulled"
argument_list|,
name|array
index|[
name|getNumElements
argument_list|()
index|]
argument_list|)
expr_stmt|;
comment|// array[getNumElements() + 1] might or might not have been nulled
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|ZERO
argument_list|)
DECL|method|testToArray_emptyArrayOfWrongTypeForNonEmptyCollection ()
specifier|public
name|void
name|testToArray_emptyArrayOfWrongTypeForNonEmptyCollection
parameter_list|()
block|{
try|try
block|{
name|WrongType
index|[]
name|array
init|=
operator|new
name|WrongType
index|[
literal|0
index|]
decl_stmt|;
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"toArray(notAssignableTo[]) should throw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArrayStoreException
name|expected
parameter_list|)
block|{     }
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testToArray_emptyArrayOfWrongTypeForEmptyCollection ()
specifier|public
name|void
name|testToArray_emptyArrayOfWrongTypeForEmptyCollection
parameter_list|()
block|{
name|WrongType
index|[]
name|array
init|=
operator|new
name|WrongType
index|[
literal|0
index|]
decl_stmt|;
name|assertSame
argument_list|(
literal|"toArray(sameSizeNotAssignableTo[]) should return the given array"
argument_list|,
name|array
argument_list|,
name|collection
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|expectArrayContentsAnyOrder (Object[] expected, Object[] actual)
specifier|private
name|void
name|expectArrayContentsAnyOrder
parameter_list|(
name|Object
index|[]
name|expected
parameter_list|,
name|Object
index|[]
name|actual
parameter_list|)
block|{
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|actual
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|expectArrayContentsInOrder (List<E> expected, Object[] actual)
specifier|private
name|void
name|expectArrayContentsInOrder
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|expected
parameter_list|,
name|Object
index|[]
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"toArray() ordered contents: "
argument_list|,
name|expected
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|actual
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the {@link Method} instance for    * {@link #testToArray_isPlainObjectArray()} so that tests of    * {@link Arrays#asList(Object[])} can suppress it with {@code    * FeatureSpecificTestSuiteBuilder.suppressing()} until<a    * href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6260652">Sun bug    * 6260652</a> is fixed.    */
DECL|method|getToArrayIsPlainObjectArrayMethod ()
specifier|public
specifier|static
name|Method
name|getToArrayIsPlainObjectArrayMethod
parameter_list|()
block|{
return|return
name|Platform
operator|.
name|getMethod
argument_list|(
name|CollectionToArrayTester
operator|.
name|class
argument_list|,
literal|"testToArray_isPlainObjectArray"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

