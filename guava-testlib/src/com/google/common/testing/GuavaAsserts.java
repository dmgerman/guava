begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Objects
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
import|;
end_import

begin_comment
comment|/**  * Contains additional assertion methods not found in JUnit.  *  * @author Kevin Bourillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|GuavaAsserts
specifier|public
specifier|final
class|class
name|GuavaAsserts
block|{
DECL|method|GuavaAsserts ()
specifier|private
name|GuavaAsserts
parameter_list|()
block|{ }
comment|/**    * Replacement of {@link GuavaAsserts#assertEquals} which provides the same error    * message in GWT and java.    */
DECL|method|assertEqualsImpl ( String message, Object expected, Object actual)
specifier|private
specifier|static
name|void
name|assertEqualsImpl
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|expected
parameter_list|,
name|Object
name|actual
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Objects
operator|.
name|equal
argument_list|(
name|expected
argument_list|,
name|actual
argument_list|)
condition|)
block|{
name|failWithMessage
argument_list|(
name|message
argument_list|,
literal|"expected:<"
operator|+
name|expected
operator|+
literal|"> but was:<"
operator|+
name|actual
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|failWithMessage (String userMessage, String ourMessage)
specifier|private
specifier|static
name|void
name|failWithMessage
parameter_list|(
name|String
name|userMessage
parameter_list|,
name|String
name|ourMessage
parameter_list|)
block|{
name|fail
argument_list|(
operator|(
name|userMessage
operator|==
literal|null
operator|)
condition|?
name|ourMessage
else|:
name|userMessage
operator|+
literal|' '
operator|+
name|ourMessage
argument_list|)
expr_stmt|;
block|}
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
name|AssertionFailedError
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
name|AssertionFailedError
argument_list|(
name|message
argument_list|)
throw|;
block|}
comment|/**    * Test the condition and throw a failure exception if false with    * a stock message.    */
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
block|{
name|fail
argument_list|(
literal|"Condition expected to be true but was false."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Test the condition and throw a failure exception if false with    * a stock message.    */
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
block|{
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
comment|// String.format is not GwtCompatible.
name|assertEquals
argument_list|(
literal|"Expected '"
operator|+
name|expected
operator|+
literal|"' but got '"
operator|+
name|actual
operator|+
literal|"'"
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
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
name|message
argument_list|,
name|o2
operator|==
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
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
end_class

end_unit

