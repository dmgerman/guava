begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ONE
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
name|SEVERAL
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

begin_comment
comment|/**  * A generic JUnit test which tests {@code peek()} operations on a queue. Can't be invoked directly;  * please see {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|QueuePeekTester
specifier|public
class|class
name|QueuePeekTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractQueueTester
argument_list|<
name|E
argument_list|>
block|{
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ZERO
argument_list|)
DECL|method|testPeek_empty ()
specifier|public
name|void
name|testPeek_empty
parameter_list|()
block|{
name|assertNull
argument_list|(
literal|"emptyQueue.peek() should return null"
argument_list|,
name|getQueue
argument_list|()
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|ONE
argument_list|)
DECL|method|testPeek_size1 ()
specifier|public
name|void
name|testPeek_size1
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"size1Queue.peek() should return first element"
argument_list|,
name|e0
argument_list|()
argument_list|,
name|getQueue
argument_list|()
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|SEVERAL
argument_list|)
DECL|method|testPeek_sizeMany ()
specifier|public
name|void
name|testPeek_sizeMany
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"sizeManyQueue.peek() should return first element"
argument_list|,
name|e0
argument_list|()
argument_list|,
name|getQueue
argument_list|()
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|expectUnchanged
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

