begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Preconditions}.  *  * @author Kevin Bourrillion  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|PreconditionsTest
specifier|public
class|class
name|PreconditionsTest
extends|extends
name|TestCase
block|{
DECL|method|testCheckArgument_simple_success ()
specifier|public
name|void
name|testCheckArgument_simple_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckArgument_simple_failure ()
specifier|public
name|void
name|testCheckArgument_simple_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCheckArgument_simpleMessage_success ()
specifier|public
name|void
name|testCheckArgument_simpleMessage_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|true
argument_list|,
name|IGNORE_ME
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckArgument_simpleMessage_failure ()
specifier|public
name|void
name|testCheckArgument_simpleMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|false
argument_list|,
operator|new
name|Message
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
name|verifySimpleMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckArgument_nullMessage_failure ()
specifier|public
name|void
name|testCheckArgument_nullMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"null"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckArgument_complexMessage_success ()
specifier|public
name|void
name|testCheckArgument_complexMessage_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|true
argument_list|,
literal|"%s"
argument_list|,
name|IGNORE_ME
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckArgument_complexMessage_failure ()
specifier|public
name|void
name|testCheckArgument_complexMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkArgument
argument_list|(
literal|false
argument_list|,
name|FORMAT
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
name|verifyComplexMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckState_simple_success ()
specifier|public
name|void
name|testCheckState_simple_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckState_simple_failure ()
specifier|public
name|void
name|testCheckState_simple_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCheckState_simpleMessage_success ()
specifier|public
name|void
name|testCheckState_simpleMessage_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|true
argument_list|,
name|IGNORE_ME
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckState_simpleMessage_failure ()
specifier|public
name|void
name|testCheckState_simpleMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|false
argument_list|,
operator|new
name|Message
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{
name|verifySimpleMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckState_nullMessage_failure ()
specifier|public
name|void
name|testCheckState_nullMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"null"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckState_complexMessage_success ()
specifier|public
name|void
name|testCheckState_complexMessage_success
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|true
argument_list|,
literal|"%s"
argument_list|,
name|IGNORE_ME
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckState_complexMessage_failure ()
specifier|public
name|void
name|testCheckState_complexMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
literal|false
argument_list|,
name|FORMAT
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{
name|verifyComplexMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|NON_NULL_STRING
specifier|private
specifier|static
specifier|final
name|String
name|NON_NULL_STRING
init|=
literal|"foo"
decl_stmt|;
DECL|method|testCheckNotNull_simple_success ()
specifier|public
name|void
name|testCheckNotNull_simple_success
parameter_list|()
block|{
name|String
name|result
init|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|NON_NULL_STRING
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|NON_NULL_STRING
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckNotNull_simple_failure ()
specifier|public
name|void
name|testCheckNotNull_simple_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCheckNotNull_simpleMessage_success ()
specifier|public
name|void
name|testCheckNotNull_simpleMessage_success
parameter_list|()
block|{
name|String
name|result
init|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|NON_NULL_STRING
argument_list|,
name|IGNORE_ME
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|NON_NULL_STRING
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckNotNull_simpleMessage_failure ()
specifier|public
name|void
name|testCheckNotNull_simpleMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
literal|null
argument_list|,
operator|new
name|Message
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{
name|verifySimpleMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckNotNull_complexMessage_success ()
specifier|public
name|void
name|testCheckNotNull_complexMessage_success
parameter_list|()
block|{
name|String
name|result
init|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|NON_NULL_STRING
argument_list|,
literal|"%s"
argument_list|,
name|IGNORE_ME
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|NON_NULL_STRING
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckNotNull_complexMessage_failure ()
specifier|public
name|void
name|testCheckNotNull_complexMessage_failure
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
literal|null
argument_list|,
name|FORMAT
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"no exception thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{
name|verifyComplexMessage
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckElementIndex_ok ()
specifier|public
name|void
name|testCheckElementIndex_ok
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckElementIndex_badSize ()
specifier|public
name|void
name|testCheckElementIndex_badSize
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|1
argument_list|,
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
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// don't care what the message text is, as this is an invalid usage of
comment|// the Preconditions class, unlike all the other exceptions it throws
block|}
block|}
DECL|method|testCheckElementIndex_negative ()
specifier|public
name|void
name|testCheckElementIndex_negative
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"index (-1) must not be negative"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckElementIndex_tooHigh ()
specifier|public
name|void
name|testCheckElementIndex_tooHigh
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|1
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"index (1) must be less than size (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckElementIndex_withDesc_negative ()
specifier|public
name|void
name|testCheckElementIndex_withDesc_negative
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|"foo"
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
block|{
name|assertEquals
argument_list|(
literal|"foo (-1) must not be negative"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckElementIndex_withDesc_tooHigh ()
specifier|public
name|void
name|testCheckElementIndex_withDesc_tooHigh
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkElementIndex
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|"foo"
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
block|{
name|assertEquals
argument_list|(
literal|"foo (1) must be less than size (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndex_ok ()
specifier|public
name|void
name|testCheckPositionIndex_ok
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckPositionIndex_badSize ()
specifier|public
name|void
name|testCheckPositionIndex_badSize
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|1
argument_list|,
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
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// don't care what the message text is, as this is an invalid usage of
comment|// the Preconditions class, unlike all the other exceptions it throws
block|}
block|}
DECL|method|testCheckPositionIndex_negative ()
specifier|public
name|void
name|testCheckPositionIndex_negative
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"index (-1) must not be negative"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndex_tooHigh ()
specifier|public
name|void
name|testCheckPositionIndex_tooHigh
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|2
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"index (2) must not be greater than size (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndex_withDesc_negative ()
specifier|public
name|void
name|testCheckPositionIndex_withDesc_negative
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|"foo"
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
block|{
name|assertEquals
argument_list|(
literal|"foo (-1) must not be negative"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndex_withDesc_tooHigh ()
specifier|public
name|void
name|testCheckPositionIndex_withDesc_tooHigh
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndex
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|"foo"
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
block|{
name|assertEquals
argument_list|(
literal|"foo (2) must not be greater than size (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndexes_ok ()
specifier|public
name|void
name|testCheckPositionIndexes_ok
parameter_list|()
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|testCheckPositionIndexes_badSize ()
specifier|public
name|void
name|testCheckPositionIndexes_badSize
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
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
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCheckPositionIndex_startNegative ()
specifier|public
name|void
name|testCheckPositionIndex_startNegative
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
operator|-
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"start index (-1) must not be negative"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndexes_endTooHigh ()
specifier|public
name|void
name|testCheckPositionIndexes_endTooHigh
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"end index (2) must not be greater than size (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testCheckPositionIndexes_reversed ()
specifier|public
name|void
name|testCheckPositionIndexes_reversed
parameter_list|()
block|{
try|try
block|{
name|Preconditions
operator|.
name|checkPositionIndexes
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
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
block|{
name|assertEquals
argument_list|(
literal|"end index (0) must not be less than start index (1)"
argument_list|,
name|expected
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testFormat ()
specifier|public
name|void
name|testFormat
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"%s"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo [5]"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo [5, 6, 7]"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"foo"
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"%s 1 2"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s %s %s"
argument_list|,
literal|"%s"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" [5, 6]"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|""
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s%s%s"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1%s%s"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s%s%s"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5 + 6 = 11"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s + 6 = 11"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5 + 6 = 11"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"5 + %s = 11"
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5 + 6 = 11"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"5 + 6 = %s"
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5 + 6 = 11"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s + %s = %s"
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|,
literal|11
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"null [null, null]"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|"%s"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"null [5, 6]"
argument_list|,
name|Preconditions
operator|.
name|format
argument_list|(
literal|null
argument_list|,
literal|5
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
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
name|testAllPublicStaticMethods
argument_list|(
name|Preconditions
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|field|IGNORE_ME
specifier|private
specifier|static
specifier|final
name|Object
name|IGNORE_ME
init|=
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|fail
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
DECL|class|Message
specifier|private
specifier|static
class|class
name|Message
block|{
DECL|field|invoked
name|boolean
name|invoked
decl_stmt|;
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|invoked
argument_list|)
expr_stmt|;
name|invoked
operator|=
literal|true
expr_stmt|;
return|return
literal|"A message"
return|;
block|}
block|}
DECL|field|FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|FORMAT
init|=
literal|"I ate %s pies."
decl_stmt|;
DECL|method|verifySimpleMessage (Exception e)
specifier|private
specifier|static
name|void
name|verifySimpleMessage
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"A message"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyComplexMessage (Exception e)
specifier|private
specifier|static
name|void
name|verifyComplexMessage
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"I ate 5 pies."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

