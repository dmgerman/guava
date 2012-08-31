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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Tests the {@link Map} implementations of {@link java.util}, suppressing  * tests that trip known OpenJDK 6 bugs.  *  * @author Kevin Bourrillion  */
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
argument_list|)
return|;
comment|// for keySet
block|}
DECL|method|testsForEnumMap ()
annotation|@
name|Override
specifier|public
name|Test
name|testsForEnumMap
parameter_list|()
block|{
comment|// Do nothing.
comment|// TODO: work around the reused-entry problem
comment|// http://bugs.sun.com/view_bug.do?bug_id=6312706
return|return
operator|new
name|TestSuite
argument_list|()
return|;
block|}
block|}
end_class

end_unit

