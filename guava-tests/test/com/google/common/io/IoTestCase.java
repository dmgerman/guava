begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Base test case class for I/O tests.  *  * @author Chris Nokleberg  */
end_comment

begin_class
DECL|class|IoTestCase
specifier|public
specifier|abstract
class|class
name|IoTestCase
extends|extends
name|TestCase
block|{
DECL|field|I18N
specifier|static
specifier|final
name|String
name|I18N
init|=
literal|"\u00CE\u00F1\u0163\u00E9\u0072\u00F1\u00E5\u0163\u00EE\u00F6"
operator|+
literal|"\u00F1\u00E5\u013C\u00EE\u017E\u00E5\u0163\u00EE\u00F6\u00F1"
decl_stmt|;
DECL|field|ASCII
specifier|static
specifier|final
name|String
name|ASCII
init|=
literal|" !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ"
operator|+
literal|"[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~"
decl_stmt|;
comment|/** Returns a byte array of length size that has values 0 .. size - 1. */
DECL|method|newPreFilledByteArray (int size)
specifier|protected
specifier|static
name|byte
index|[]
name|newPreFilledByteArray
parameter_list|(
name|int
name|size
parameter_list|)
block|{
return|return
name|newPreFilledByteArray
argument_list|(
literal|0
argument_list|,
name|size
argument_list|)
return|;
block|}
comment|/**    * Returns a byte array of length size that has values    *    offset .. offset + size - 1.    */
DECL|method|newPreFilledByteArray (int offset, int size)
specifier|protected
specifier|static
name|byte
index|[]
name|newPreFilledByteArray
parameter_list|(
name|int
name|offset
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|byte
index|[]
name|array
init|=
operator|new
name|byte
index|[
name|size
index|]
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|array
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|offset
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|array
return|;
block|}
block|}
end_class

end_unit

