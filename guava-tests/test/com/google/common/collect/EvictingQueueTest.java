begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|testing
operator|.
name|NullPointerTester
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
name|testing
operator|.
name|SerializableTester
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * Tests for {@link EvictingQueue}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EvictingQueueTest
specifier|public
class|class
name|EvictingQueueTest
extends|extends
name|TestCase
block|{
DECL|method|testCreateWithNegativeSize ()
specifier|public
name|void
name|testCreateWithNegativeSize
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|EvictingQueue
argument_list|<
name|Object
argument_list|>
name|unused
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCreateWithZeroSize ()
specifier|public
name|void
name|testCreateWithZeroSize
parameter_list|()
throws|throws
name|Exception
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|offer
argument_list|(
literal|"hi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|queue
operator|.
name|remove
argument_list|(
literal|"hi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|element
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{}
name|assertNull
argument_list|(
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|queue
operator|.
name|poll
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|remove
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testRemainingCapacity_maxSize0 ()
specifier|public
name|void
name|testRemainingCapacity_maxSize0
parameter_list|()
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemainingCapacity_maxSize1 ()
specifier|public
name|void
name|testRemainingCapacity_maxSize1
parameter_list|()
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemainingCapacity_maxSize3 ()
specifier|public
name|void
name|testRemainingCapacity_maxSize3
parameter_list|()
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEvictingAfterOne ()
specifier|public
name|void
name|testEvictingAfterOne
parameter_list|()
throws|throws
name|Exception
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"hi"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hi"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hi"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"there"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"there"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"there"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"there"
argument_list|,
name|queue
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEvictingAfterThree ()
specifier|public
name|void
name|testEvictingAfterThree
parameter_list|()
throws|throws
name|Exception
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"one"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"three"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|add
argument_list|(
literal|"four"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAddAll ()
specifier|public
name|void
name|testAddAll
parameter_list|()
throws|throws
name|Exception
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"one"
argument_list|,
literal|"two"
argument_list|,
literal|"three"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queue
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
literal|"four"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|element
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|queue
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNullPointerExceptions ()
specifier|public
name|void
name|testNullPointerExceptions
parameter_list|()
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|EvictingQueue
operator|.
name|class
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicConstructors
argument_list|(
name|EvictingQueue
operator|.
name|class
argument_list|)
expr_stmt|;
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|queue
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|5
argument_list|)
decl_stmt|;
comment|// The queue must be non-empty so it throws a NPE correctly
name|queue
operator|.
name|add
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|tester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|queue
argument_list|)
expr_stmt|;
block|}
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|original
init|=
name|EvictingQueue
operator|.
name|create
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|original
operator|.
name|add
argument_list|(
literal|"one"
argument_list|)
expr_stmt|;
name|original
operator|.
name|add
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
name|original
operator|.
name|add
argument_list|(
literal|"three"
argument_list|)
expr_stmt|;
name|EvictingQueue
argument_list|<
name|String
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|copy
operator|.
name|maxSize
argument_list|,
name|original
operator|.
name|maxSize
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"one"
argument_list|,
name|copy
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"two"
argument_list|,
name|copy
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"three"
argument_list|,
name|copy
operator|.
name|remove
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copy
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

