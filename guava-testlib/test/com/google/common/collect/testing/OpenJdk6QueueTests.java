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
name|Queue
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

begin_comment
comment|/**  * Tests the {@link Queue} implementations of {@link java.util}, suppressing tests that trip known  * OpenJDK 6 bugs.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|OpenJdk6QueueTests
specifier|public
class|class
name|OpenJdk6QueueTests
extends|extends
name|TestsForQueuesInJavaUtil
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
name|OpenJdk6QueueTests
argument_list|()
operator|.
name|allTests
argument_list|()
return|;
block|}
DECL|field|PQ_SUPPRESS
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|Method
argument_list|>
name|PQ_SUPPRESS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|getCreateWithNullUnsupportedMethod
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|suppressForPriorityBlockingQueue ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForPriorityBlockingQueue
parameter_list|()
block|{
return|return
name|PQ_SUPPRESS
return|;
block|}
annotation|@
name|Override
DECL|method|suppressForPriorityQueue ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForPriorityQueue
parameter_list|()
block|{
return|return
name|PQ_SUPPRESS
return|;
block|}
block|}
end_class

end_unit

