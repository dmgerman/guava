begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Defaults}.  *  * @author Jige Yu  */
end_comment

begin_class
DECL|class|DefaultsTest
specifier|public
class|class
name|DefaultsTest
extends|extends
name|TestCase
block|{
DECL|method|testGetDefaultValue ()
specifier|public
name|void
name|testGetDefaultValue
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|boolean
operator|.
name|class
argument_list|)
operator|.
name|booleanValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'\0'
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|char
operator|.
name|class
argument_list|)
operator|.
name|charValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|byte
operator|.
name|class
argument_list|)
operator|.
name|byteValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|short
operator|.
name|class
argument_list|)
operator|.
name|shortValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|int
operator|.
name|class
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|long
operator|.
name|class
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0f
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|float
operator|.
name|class
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.0d
argument_list|,
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|double
operator|.
name|class
argument_list|)
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|void
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|Defaults
operator|.
name|defaultValue
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
