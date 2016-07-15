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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
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
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|PriorityQueue
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentLinkedQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|PriorityBlockingQueue
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

begin_comment
comment|/**  * Generates a test suite covering the {@link Queue} implementations in the  * {@link java.util} package. Can be subclassed to specify tests that should  * be suppressed.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|TestsForQueuesInJavaUtil
specifier|public
class|class
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
name|TestsForQueuesInJavaUtil
argument_list|()
operator|.
name|allTests
argument_list|()
return|;
block|}
DECL|method|allTests ()
specifier|public
name|Test
name|allTests
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForLinkedList
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForArrayBlockingQueue
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForConcurrentLinkedQueue
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForLinkedBlockingQueue
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForPriorityBlockingQueue
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|testsForPriorityQueue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|suppressForLinkedList ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForLinkedList
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForArrayBlockingQueue ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForArrayBlockingQueue
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForConcurrentLinkedQueue ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForConcurrentLinkedQueue
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|suppressForLinkedBlockingQueue ()
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|suppressForLinkedBlockingQueue
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
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
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
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
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
DECL|method|testsForLinkedList ()
specifier|public
name|Test
name|testsForLinkedList
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"LinkedList"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|skipCollectionTests
argument_list|()
comment|// already covered in TestsForListsInJavaUtil
operator|.
name|suppressing
argument_list|(
name|suppressForLinkedList
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForArrayBlockingQueue ()
specifier|public
name|Test
name|testsForArrayBlockingQueue
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|ArrayBlockingQueue
argument_list|<
name|String
argument_list|>
argument_list|(
literal|100
argument_list|,
literal|false
argument_list|,
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ArrayBlockingQueue"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForArrayBlockingQueue
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForConcurrentLinkedQueue ()
specifier|public
name|Test
name|testsForConcurrentLinkedQueue
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|String
argument_list|>
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ConcurrentLinkedQueue"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForConcurrentLinkedQueue
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForLinkedBlockingQueue ()
specifier|public
name|Test
name|testsForLinkedBlockingQueue
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|LinkedBlockingQueue
argument_list|<
name|String
argument_list|>
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"LinkedBlockingQueue"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForLinkedBlockingQueue
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
comment|// Not specifying KNOWN_ORDER for PriorityQueue and PriorityBlockingQueue
comment|// even though they do have it, because our tests interpret KNOWN_ORDER to
comment|// also mean that the iterator returns the head element first, which those
comment|// don't.
DECL|method|testsForPriorityBlockingQueue ()
specifier|public
name|Test
name|testsForPriorityBlockingQueue
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|PriorityBlockingQueue
argument_list|<
name|String
argument_list|>
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"PriorityBlockingQueue"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForPriorityBlockingQueue
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|method|testsForPriorityQueue ()
specifier|public
name|Test
name|testsForPriorityQueue
parameter_list|()
block|{
return|return
name|QueueTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringQueueGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Queue
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
operator|new
name|PriorityQueue
argument_list|<
name|String
argument_list|>
argument_list|(
name|MinimalCollection
operator|.
name|of
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"PriorityQueue"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|)
operator|.
name|suppressing
argument_list|(
name|suppressForPriorityQueue
argument_list|()
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
block|}
end_class

end_unit

