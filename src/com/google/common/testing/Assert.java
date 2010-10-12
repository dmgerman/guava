begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|String
operator|.
name|format
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
name|Beta
import|;
end_import

begin_comment
comment|/**  * A simple collection of assertions used in testing - rewritten to  * avoid test framework dependencies.  *  * @author Christian Edward Gruber  * @since 8  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Assert
specifier|public
class|class
name|Assert
block|{
comment|/**    * Fail with an RuntimeException.    *    * @throws RuntimeException always    */
DECL|method|fail ()
specifier|public
specifier|static
name|void
name|fail
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
comment|/**    * Fail with an RuntimeException and a message.    *    * @throws RuntimeException always    */
DECL|method|fail (String message)
specifier|public
specifier|static
name|void
name|fail
parameter_list|(
name|String
name|message
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|message
argument_list|)
throw|;
block|}
comment|/**    * Test the condition and throw a failure exception if false with     * a stock message.    *    * @throws RuntimeException    */
DECL|method|assertTrue (boolean condition)
specifier|public
specifier|static
name|void
name|assertTrue
parameter_list|(
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
name|fail
argument_list|(
literal|"Condition expected to be true but was false."
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test the condition and throw a failure exception if false with     * a stock message.    *    * @throws RuntimeException    */
DECL|method|assertTrue (String message, boolean condition)
specifier|public
specifier|static
name|void
name|assertTrue
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|condition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|condition
condition|)
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the equality of two objects    */
DECL|method|assertEquals (Object expected, Object actual)
specifier|public
specifier|static
name|void
name|assertEquals
parameter_list|(
name|Object
name|expected
parameter_list|,
name|Object
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|format
argument_list|(
literal|"Expected '%s' but got '%s'"
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the equality of two objects    */
DECL|method|assertEquals (String message, Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertEquals
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
if|if
condition|(
name|o1
operator|==
name|o2
condition|)
block|{
return|return;
block|}
else|else
block|{
name|assertTrue
argument_list|(
name|message
argument_list|,
name|o1
operator|.
name|equals
argument_list|(
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

