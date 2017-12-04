begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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

begin_comment
comment|/**  * Test cases for {@link HashBasedTable}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|HashBasedTableTest
specifier|public
class|class
name|HashBasedTableTest
extends|extends
name|AbstractTableTest
block|{
annotation|@
name|Override
DECL|method|create (Object... data)
specifier|protected
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|data
parameter_list|)
block|{
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|4
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|table
operator|.
name|put
argument_list|(
literal|"cat"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|)
expr_stmt|;
name|table
operator|.
name|clear
argument_list|()
expr_stmt|;
name|populate
argument_list|(
name|table
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return
name|table
return|;
block|}
DECL|method|testIterationOrder ()
specifier|public
name|void
name|testIterationOrder
parameter_list|()
block|{
name|Table
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|String
argument_list|>
name|table
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|table
operator|.
name|put
argument_list|(
literal|"r"
operator|+
name|i
argument_list|,
literal|"c"
operator|+
name|i
argument_list|,
literal|"v"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|table
operator|.
name|rowKeySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"r0"
argument_list|,
literal|"r1"
argument_list|,
literal|"r2"
argument_list|,
literal|"r3"
argument_list|,
literal|"r4"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|table
operator|.
name|columnKeySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"c0"
argument_list|,
literal|"c1"
argument_list|,
literal|"c2"
argument_list|,
literal|"c3"
argument_list|,
literal|"c4"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|table
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"v0"
argument_list|,
literal|"v1"
argument_list|,
literal|"v2"
argument_list|,
literal|"v3"
argument_list|,
literal|"v4"
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|testCreateWithValidSizes ()
specifier|public
name|void
name|testCreateWithValidSizes
parameter_list|()
block|{
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table1
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
literal|100
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|table1
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table1
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table2
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
literal|100
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|table2
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table2
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table3
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
literal|0
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|table3
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table3
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|table4
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|table4
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table4
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateWithInvalidSizes ()
specifier|public
name|void
name|testCreateWithInvalidSizes
parameter_list|()
block|{
try|try
block|{
name|HashBasedTable
operator|.
name|create
argument_list|(
literal|100
argument_list|,
operator|-
literal|5
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
try|try
block|{
name|HashBasedTable
operator|.
name|create
argument_list|(
operator|-
literal|5
argument_list|,
literal|20
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
DECL|method|testCreateCopy ()
specifier|public
name|void
name|testCreateCopy
parameter_list|()
block|{
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|original
init|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
decl_stmt|;
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|copy
init|=
name|HashBasedTable
operator|.
name|create
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|copy
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|table
operator|=
name|create
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'a'
argument_list|,
literal|"bar"
argument_list|,
literal|1
argument_list|,
literal|'b'
argument_list|,
literal|"foo"
argument_list|,
literal|3
argument_list|,
literal|'c'
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// NullPointerTester
DECL|method|testNullPointerStatic ()
specifier|public
name|void
name|testNullPointerStatic
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|HashBasedTable
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

