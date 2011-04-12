begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|collect
operator|.
name|ImmutableMap
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
name|Arrays
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
comment|/**  * Tests for {@link Objects#toStringHelper(Object)}.  *   * @author Jason Lee  */
end_comment

begin_class
DECL|class|ToStringHelperTest
specifier|public
class|class
name|ToStringHelperTest
extends|extends
name|TestCase
block|{
DECL|method|testConstructor_instance ()
specifier|public
name|void
name|testConstructor_instance
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ToStringHelperTest{}"
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructor_innerClass ()
specifier|public
name|void
name|testConstructor_innerClass
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestClass{}"
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructor_anonymousClass ()
specifier|public
name|void
name|testConstructor_anonymousClass
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|Object
argument_list|()
block|{}
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|toTest
operator|.
name|matches
argument_list|(
literal|"[0-9]+\\{\\}"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructor_classObject ()
specifier|public
name|void
name|testConstructor_classObject
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
name|TestClass
operator|.
name|class
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestClass{}"
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstructor_stringObject ()
specifier|public
name|void
name|testConstructor_stringObject
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
literal|"FooBar"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"FooBar{}"
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
comment|// all remaining test are on an inner class with various fields
DECL|method|testToString_oneField ()
specifier|public
name|void
name|testToString_oneField
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"field1"
argument_list|,
literal|"Hello"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestClass{field1=Hello}"
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_complexFields ()
specifier|public
name|void
name|testToString_complexFields
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|Integer
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"abc"
argument_list|,
literal|1
argument_list|)
decl|.
name|put
argument_list|(
literal|"def"
argument_list|,
literal|2
argument_list|)
decl|.
name|put
argument_list|(
literal|"ghi"
argument_list|,
literal|3
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"field1"
argument_list|,
literal|"This is string."
argument_list|)
operator|.
name|add
argument_list|(
literal|"field2"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"abc"
argument_list|,
literal|"def"
argument_list|,
literal|"ghi"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
literal|"field3"
argument_list|,
name|map
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|expected
init|=
literal|"TestClass{"
operator|+
literal|"field1=This is string., field2=[abc, def, ghi], field3={abc=1, def=2, ghi=3}}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_addWithNullName ()
specifier|public
name|void
name|testToString_addWithNullName
parameter_list|()
block|{
name|Objects
operator|.
name|ToStringHelper
name|helper
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|helper
operator|.
name|add
argument_list|(
literal|null
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No exception was thrown."
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
DECL|method|testToString_addWithNullValue ()
specifier|public
name|void
name|testToString_addWithNullValue
parameter_list|()
block|{
specifier|final
name|String
name|result
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"Hello"
argument_list|,
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"TestClass{Hello=null}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_addValue ()
specifier|public
name|void
name|testToString_addValue
parameter_list|()
block|{
name|String
name|toTest
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"field1"
argument_list|,
literal|1
argument_list|)
operator|.
name|addValue
argument_list|(
literal|"value1"
argument_list|)
operator|.
name|add
argument_list|(
literal|"field2"
argument_list|,
literal|"value2"
argument_list|)
operator|.
name|addValue
argument_list|(
literal|2
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|expected
init|=
literal|"TestClass{field1=1, value1, field2=value2, 2}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|toTest
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_addValueWithNullValue ()
specifier|public
name|void
name|testToString_addValueWithNullValue
parameter_list|()
block|{
specifier|final
name|String
name|result
init|=
name|Objects
operator|.
name|toStringHelper
argument_list|(
operator|new
name|TestClass
argument_list|()
argument_list|)
operator|.
name|addValue
argument_list|(
literal|null
argument_list|)
operator|.
name|addValue
argument_list|(
literal|"Hello"
argument_list|)
operator|.
name|addValue
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|String
name|expected
init|=
literal|"TestClass{null, Hello, null}"
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test class for testing formatting of inner classes.    */
DECL|class|TestClass
specifier|private
specifier|static
class|class
name|TestClass
block|{}
block|}
end_class

end_unit

