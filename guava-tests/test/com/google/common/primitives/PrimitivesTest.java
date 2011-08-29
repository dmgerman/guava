begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|collect
operator|.
name|ImmutableSet
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
comment|/**  * Unit test for {@link Primitives}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|PrimitivesTest
specifier|public
class|class
name|PrimitivesTest
extends|extends
name|TestCase
block|{
DECL|method|testIsWrapperType ()
specifier|public
name|void
name|testIsWrapperType
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Primitives
operator|.
name|isWrapperType
argument_list|(
name|Void
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Primitives
operator|.
name|isWrapperType
argument_list|(
name|void
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testWrap ()
specifier|public
name|void
name|testWrap
parameter_list|()
block|{
name|assertSame
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|wrap
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|wrap
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|wrap
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnwrap ()
specifier|public
name|void
name|testUnwrap
parameter_list|()
block|{
name|assertSame
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|unwrap
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|unwrap
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|Primitives
operator|.
name|unwrap
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAllPrimitiveTypes ()
specifier|public
name|void
name|testAllPrimitiveTypes
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|primitives
init|=
name|Primitives
operator|.
name|allPrimitiveTypes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
expr|<
name|Object
operator|>
name|of
argument_list|(
name|boolean
operator|.
name|class
argument_list|,
name|byte
operator|.
name|class
argument_list|,
name|char
operator|.
name|class
argument_list|,
name|double
operator|.
name|class
argument_list|,
name|float
operator|.
name|class
argument_list|,
name|int
operator|.
name|class
argument_list|,
name|long
operator|.
name|class
argument_list|,
name|short
operator|.
name|class
argument_list|,
name|void
operator|.
name|class
argument_list|)
argument_list|,
name|primitives
argument_list|)
expr_stmt|;
try|try
block|{
name|primitives
operator|.
name|remove
argument_list|(
name|boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testAllWrapperTypes ()
specifier|public
name|void
name|testAllWrapperTypes
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|wrappers
init|=
name|Primitives
operator|.
name|allWrapperTypes
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
expr|<
name|Object
operator|>
name|of
argument_list|(
name|Boolean
operator|.
name|class
argument_list|,
name|Byte
operator|.
name|class
argument_list|,
name|Character
operator|.
name|class
argument_list|,
name|Double
operator|.
name|class
argument_list|,
name|Float
operator|.
name|class
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|Long
operator|.
name|class
argument_list|,
name|Short
operator|.
name|class
argument_list|,
name|Void
operator|.
name|class
argument_list|)
argument_list|,
name|wrappers
argument_list|)
expr_stmt|;
try|try
block|{
name|wrappers
operator|.
name|remove
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testNullPointerExceptions ()
specifier|public
name|void
name|testNullPointerExceptions
parameter_list|()
throws|throws
name|Exception
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
name|Primitives
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

