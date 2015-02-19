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
comment|/**  * Tests for {@link Objects}.  *  * @author Laurence Gonsalves  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ObjectsTest
specifier|public
class|class
name|ObjectsTest
extends|extends
name|TestCase
block|{
DECL|method|testEqual ()
specifier|public
name|void
name|testEqual
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
comment|// test distinct string objects
name|String
name|s1
init|=
literal|"foobar"
decl_stmt|;
name|String
name|s2
init|=
operator|new
name|String
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
name|s1
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
literal|null
argument_list|,
name|s1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Objects
operator|.
name|equal
argument_list|(
literal|"1"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|h1
init|=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|"two"
argument_list|,
literal|3.0
argument_list|)
decl_stmt|;
name|int
name|h2
init|=
name|Objects
operator|.
name|hashCode
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|String
argument_list|(
literal|"two"
argument_list|)
argument_list|,
operator|new
name|Double
argument_list|(
literal|3.0
argument_list|)
argument_list|)
decl_stmt|;
comment|// repeatable
name|assertEquals
argument_list|(
name|h1
argument_list|,
name|h2
argument_list|)
expr_stmt|;
comment|// These don't strictly need to be true, but they're nice properties.
name|assertTrue
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|null
argument_list|)
operator|!=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|null
argument_list|)
operator|!=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|2
argument_list|)
operator|!=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|!=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Objects
operator|.
name|hashCode
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
operator|!=
name|Objects
operator|.
name|hashCode
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFirstNonNull_withNonNull ()
specifier|public
name|void
name|testFirstNonNull_withNonNull
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|s1
init|=
literal|"foo"
decl_stmt|;
name|String
name|s2
init|=
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|s1
argument_list|,
literal|"bar"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|s1
argument_list|,
name|s2
argument_list|)
expr_stmt|;
name|Long
name|n1
init|=
operator|new
name|Long
argument_list|(
literal|42
argument_list|)
decl_stmt|;
name|Long
name|n2
init|=
name|Objects
operator|.
name|firstNonNull
argument_list|(
literal|null
argument_list|,
name|n1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"CheckReturnValue"
argument_list|)
DECL|method|testFirstNonNull_throwsNullPointerException ()
specifier|public
name|void
name|testFirstNonNull_throwsNullPointerException
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Objects
operator|.
name|firstNonNull
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected NullPointerException"
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
name|Objects
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

