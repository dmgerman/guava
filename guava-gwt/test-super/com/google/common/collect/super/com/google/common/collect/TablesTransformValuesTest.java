begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|base
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Test cases for {@link Tables#transformValues}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|TablesTransformValuesTest
specifier|public
class|class
name|TablesTransformValuesTest
extends|extends
name|AbstractTableTest
block|{
DECL|field|FIRST_CHARACTER
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|String
argument_list|,
name|Character
argument_list|>
name|FIRST_CHARACTER
init|=
operator|new
name|Function
argument_list|<
name|String
argument_list|,
name|Character
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Character
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|==
literal|null
condition|?
literal|null
else|:
name|input
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|create ( Object... data)
annotation|@
name|Override
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
name|String
argument_list|>
name|table
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|data
operator|.
name|length
operator|%
literal|3
operator|==
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data
operator|.
name|length
condition|;
name|i
operator|+=
literal|3
control|)
block|{
name|String
name|value
init|=
operator|(
name|data
index|[
name|i
operator|+
literal|2
index|]
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|data
index|[
name|i
operator|+
literal|2
index|]
operator|.
name|toString
argument_list|()
operator|+
literal|"transformed"
decl_stmt|;
name|table
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|data
index|[
name|i
index|]
argument_list|,
operator|(
name|Integer
operator|)
name|data
index|[
name|i
operator|+
literal|1
index|]
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|Tables
operator|.
name|transformValues
argument_list|(
name|table
argument_list|,
name|FIRST_CHARACTER
argument_list|)
return|;
block|}
comment|// Null support depends on the underlying table and function.
comment|// put() and putAll() aren't supported.
DECL|method|testPut ()
annotation|@
name|Override
specifier|public
name|void
name|testPut
parameter_list|()
block|{
try|try
block|{
name|table
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
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
name|assertSize
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutAllTable ()
annotation|@
name|Override
specifier|public
name|void
name|testPutAllTable
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
name|Table
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|,
name|Character
argument_list|>
name|other
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|,
literal|'d'
argument_list|)
expr_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|,
literal|'e'
argument_list|)
expr_stmt|;
name|other
operator|.
name|put
argument_list|(
literal|"cat"
argument_list|,
literal|2
argument_list|,
literal|'f'
argument_list|)
expr_stmt|;
try|try
block|{
name|table
operator|.
name|putAll
argument_list|(
name|other
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{}
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'a'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'b'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Character
operator|)
literal|'c'
argument_list|,
name|table
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
DECL|method|testPutNull ()
annotation|@
name|Override
specifier|public
name|void
name|testPutNull
parameter_list|()
block|{}
DECL|method|testPutNullReplace ()
annotation|@
name|Override
specifier|public
name|void
name|testPutNullReplace
parameter_list|()
block|{}
DECL|method|testRowClearAndPut ()
annotation|@
name|Override
specifier|public
name|void
name|testRowClearAndPut
parameter_list|()
block|{}
block|}
end_class

end_unit

