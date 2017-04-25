begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
name|testing
operator|.
name|NullPointerTester
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
name|atomic
operator|.
name|AtomicReferenceArray
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

begin_comment
comment|/**  * Unit test for {@link Atomics}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|AtomicsTest
specifier|public
class|class
name|AtomicsTest
extends|extends
name|TestCase
block|{
DECL|field|OBJECT
specifier|private
specifier|static
specifier|final
name|Object
name|OBJECT
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|method|testNewReference ()
specifier|public
name|void
name|testNewReference
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|Atomics
operator|.
name|newReference
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewReference_withInitialValue ()
specifier|public
name|void
name|testNewReference_withInitialValue
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|Atomics
operator|.
name|newReference
argument_list|(
literal|null
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|OBJECT
argument_list|,
name|Atomics
operator|.
name|newReference
argument_list|(
name|OBJECT
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNewReferenceArray_withLength ()
specifier|public
name|void
name|testNewReferenceArray_withLength
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|length
init|=
literal|42
decl_stmt|;
name|AtomicReferenceArray
argument_list|<
name|String
argument_list|>
name|refArray
init|=
name|Atomics
operator|.
name|newReferenceArray
argument_list|(
name|length
argument_list|)
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
name|length
condition|;
operator|++
name|i
control|)
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|refArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|refArray
operator|.
name|get
argument_list|(
name|length
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNewReferenceArray_withNegativeLength ()
specifier|public
name|void
name|testNewReferenceArray_withNegativeLength
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Atomics
operator|.
name|newReferenceArray
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NegativeArraySizeException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNewReferenceArray_withStringArray ()
specifier|public
name|void
name|testNewReferenceArray_withStringArray
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|array
init|=
block|{
literal|"foo"
block|,
literal|"bar"
block|,
literal|"baz"
block|}
decl_stmt|;
name|AtomicReferenceArray
argument_list|<
name|String
argument_list|>
name|refArray
init|=
name|Atomics
operator|.
name|newReferenceArray
argument_list|(
name|array
argument_list|)
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
name|array
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|assertEquals
argument_list|(
name|array
index|[
name|i
index|]
argument_list|,
name|refArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|refArray
operator|.
name|get
argument_list|(
name|array
operator|.
name|length
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNewReferenceArray_withNullArray ()
specifier|public
name|void
name|testNewReferenceArray_withNullArray
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Atomics
operator|.
name|newReferenceArray
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
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
name|testAllPublicConstructors
argument_list|(
name|Atomics
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// there aren't any
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Atomics
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
