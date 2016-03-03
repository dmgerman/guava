begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
operator|.
name|newArrayList
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
name|testers
operator|.
name|CollectionAddAllTester
operator|.
name|getAddAllUnsupportedNonePresentMethod
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
name|testers
operator|.
name|CollectionAddAllTester
operator|.
name|getAddAllUnsupportedSomePresentMethod
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
name|testers
operator|.
name|CollectionAddTester
operator|.
name|getAddUnsupportedNotPresentMethod
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
name|testers
operator|.
name|CollectionCreationTester
operator|.
name|getCreateWithNullUnsupportedMethod
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
name|testers
operator|.
name|MapCreationTester
operator|.
name|getCreateWithNullKeyUnsupportedMethod
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
name|testers
operator|.
name|MapEntrySetTester
operator|.
name|getContainsEntryWithIncomparableKeyMethod
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
name|testers
operator|.
name|MapEntrySetTester
operator|.
name|getContainsEntryWithIncomparableValueMethod
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
name|testers
operator|.
name|MapPutAllTester
operator|.
name|getPutAllNullKeyUnsupportedMethod
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
name|testers
operator|.
name|MapPutTester
operator|.
name|getPutNullKeyUnsupportedMethod
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Tests the {@link Map} implementations of {@link java.util}, suppressing  * tests that trip known bugs in OpenJDK 6 or higher.  *  * @author Kevin Bourrillion  */
end_comment

begin_comment
comment|/*  * TODO(cpovirk): consider renaming this class in light of our now running it  * under JDK7  */
end_comment

begin_class
DECL|class|OpenJdk6MapTests
specifier|public
class|class
name|OpenJdk6MapTests
extends|extends
name|TestsForMapsInJavaUtil
block|{
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|OpenJdk6MapTests
argument_list|()
operator|.
name|allTests
argument_list|()
return|;
block|}
DECL|method|suppressForTreeMapNatural ()
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForTreeMapNatural
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|getPutNullKeyUnsupportedMethod
argument_list|()
argument_list|,
name|getPutAllNullKeyUnsupportedMethod
argument_list|()
argument_list|,
name|getCreateWithNullKeyUnsupportedMethod
argument_list|()
argument_list|,
name|getCreateWithNullUnsupportedMethod
argument_list|()
argument_list|,
comment|// for keySet
name|getContainsEntryWithIncomparableKeyMethod
argument_list|()
argument_list|,
name|getContainsEntryWithIncomparableValueMethod
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|suppressForConcurrentHashMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForConcurrentHashMap
parameter_list|()
block|{
comment|/*      * The entrySet() of ConcurrentHashMap, unlike that of other Map      * implementations, supports add() under JDK8. This seems problematic, but I      * didn't see that discussed in the review, which included many other      * changes: http://goo.gl/okTTdr      *      * TODO(cpovirk): decide what the best long-term action here is: force users      * to suppress (as we do now), stop testing entrySet().add() at all, make      * entrySet().add() tests tolerant of either behavior, introduce a map      * feature for entrySet() that supports add(), or something else      */
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|getAddUnsupportedNotPresentMethod
argument_list|()
argument_list|,
name|getAddAllUnsupportedNonePresentMethod
argument_list|()
argument_list|,
name|getAddAllUnsupportedSomePresentMethod
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|suppressForConcurrentSkipListMap ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForConcurrentSkipListMap
parameter_list|()
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|methods
init|=
name|newArrayList
argument_list|()
decl_stmt|;
name|methods
operator|.
name|addAll
argument_list|(
name|super
operator|.
name|suppressForConcurrentSkipListMap
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|.
name|add
argument_list|(
name|getContainsEntryWithIncomparableKeyMethod
argument_list|()
argument_list|)
expr_stmt|;
name|methods
operator|.
name|add
argument_list|(
name|getContainsEntryWithIncomparableValueMethod
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|methods
return|;
block|}
block|}
end_class

end_unit

