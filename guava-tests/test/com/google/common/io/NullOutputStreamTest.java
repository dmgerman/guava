begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2002 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
package|;
end_package

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
comment|/**  * Unit tests for {@link NullOutputStream}.  *  * @author Spencer Kimball  */
end_comment

begin_class
DECL|class|NullOutputStreamTest
specifier|public
class|class
name|NullOutputStreamTest
extends|extends
name|TestCase
block|{
DECL|method|testBasicOperation ()
specifier|public
name|void
name|testBasicOperation
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create a null output stream
name|NullOutputStream
name|nos
init|=
operator|new
name|NullOutputStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|nos
argument_list|)
expr_stmt|;
comment|// write to the output stream
name|nos
operator|.
name|write
argument_list|(
literal|'n'
argument_list|)
expr_stmt|;
name|String
name|test
init|=
literal|"Test string for NullOutputStream"
decl_stmt|;
name|nos
operator|.
name|write
argument_list|(
name|test
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|nos
operator|.
name|write
argument_list|(
name|test
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|2
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

