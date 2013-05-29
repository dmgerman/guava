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
name|CollectionAddAllTester
operator|.
name|getAddAllNullUnsupportedMethod
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
name|getAddNullSupportedMethod
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
name|getAddNullUnsupportedMethod
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
name|SetAddTester
operator|.
name|getAddSupportedNullPresentMethod
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests the {@link Set} implementations of {@link java.util}, suppressing  * tests that trip known OpenJDK 6 bugs.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|OpenJdk6SetTests
specifier|public
class|class
name|OpenJdk6SetTests
extends|extends
name|TestsForSetsInJavaUtil
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
name|OpenJdk6SetTests
argument_list|()
operator|.
name|allTests
argument_list|()
return|;
block|}
DECL|method|suppressForTreeSetNatural ()
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForTreeSetNatural
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|getAddNullUnsupportedMethod
argument_list|()
argument_list|,
name|getAddAllNullUnsupportedMethod
argument_list|()
argument_list|,
name|getCreateWithNullUnsupportedMethod
argument_list|()
argument_list|)
return|;
block|}
DECL|method|suppressForCheckedSet ()
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForCheckedSet
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|getAddNullSupportedMethod
argument_list|()
argument_list|,
name|getAddSupportedNullPresentMethod
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

