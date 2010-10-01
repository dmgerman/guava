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
comment|/**  * A simple collection of assertions used in testing - re-written to  * avoid junit or testNG dependencies.  *  * @author cgruber@google.com (Christian Edward Gruber)  * @since r08  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Assert
specifier|public
class|class
name|Assert
block|{
comment|/**    * Fail with an AssertionFailedError();    * @throws AssertionFailedError    */
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
comment|/**    * Fail with an AssertionFailedError and a message which may use String.format if any    * subsequent parameters are provided.    *     * @throws AssertionFailedError    */
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
comment|/**    * Test the condition and throw a failure exception if false with     * a stock message.    * @throws AssertionFailedError    */
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
literal|"Condition exepected to be true but was false."
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test the condition and throw a failure exception with a     * provided message.    * @throws AssertionFailedError    */
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
comment|/**    * Test the negation of the condition and throw a failure exception with a     * canned message.    * @throws AssertionFailedError    */
DECL|method|assertFalse (boolean condition)
specifier|public
specifier|static
name|void
name|assertFalse
parameter_list|(
name|boolean
name|condition
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Condition expected to be false but was true."
argument_list|,
operator|!
name|condition
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test the negation of the condition and throw a failure exception with a     * provided message.    * @throws AssertionFailedError    */
DECL|method|assertFalse (String message, boolean condition)
specifier|public
specifier|static
name|void
name|assertFalse
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|condition
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|message
argument_list|,
operator|!
name|condition
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the equality of two objects    */
DECL|method|assertEquals (Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertEquals
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|format
argument_list|(
literal|"Objects '%s' and '%s' are not equal"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|,
name|o1
argument_list|,
name|o2
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
comment|/**    * Assert the equality of two objects    */
DECL|method|assertNotEquals (Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertNotEquals
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|assertNotEquals
argument_list|(
name|format
argument_list|(
literal|"Objects '%s' and '%s' are equal but should not be"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the equality of two objects    */
DECL|method|assertNotEquals (String message, Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertNotEquals
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
name|fail
argument_list|(
name|format
argument_list|(
literal|"Objects '%s' and '%s' should not be equal but are identical"
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertFalse
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
comment|/**    * Assert the identity of the provided objects, else fail with a     * canned message.    */
DECL|method|assertSame (Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertSame
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|assertSame
argument_list|(
name|format
argument_list|(
literal|"Objects '%s' and '%s' are not the same."
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the identity of the provided objects, else fail with the     * provided message.    */
DECL|method|assertSame (String message, Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertSame
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
operator|!=
name|o2
condition|)
block|{
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Assert the identity of the provided objects, else fail with a     * canned message.    */
DECL|method|assertNotSame (Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertNotSame
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|assertNotSame
argument_list|(
name|format
argument_list|(
literal|"Objects '%s' and '%s' are not the same."
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
argument_list|,
name|o1
argument_list|,
name|o2
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert the identity of the provided objects, else fail with the     * provided message.    */
DECL|method|assertNotSame (String message, Object o1, Object o2)
specifier|public
specifier|static
name|void
name|assertNotSame
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
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Assert that the provided reference is null else throw an AssertionFailedError    * with a canned error message.    */
DECL|method|assertNull (Object object)
specifier|public
specifier|static
name|void
name|assertNull
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|assertNull
argument_list|(
name|format
argument_list|(
literal|"Expected null but found '%s'."
argument_list|,
name|object
argument_list|)
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert that the provided reference is null else throw an AssertionFailedError    * with a canned error message.    */
DECL|method|assertNull (String message, Object object)
specifier|public
specifier|static
name|void
name|assertNull
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Assert that the provided reference is null else throw an AssertionFailedError    * with a canned error message.    */
DECL|method|assertNotNull (Object object)
specifier|public
specifier|static
name|void
name|assertNotNull
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|format
argument_list|(
literal|"Null returned where expected non null result."
argument_list|)
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**    * Assert that the provided reference is null else throw an AssertionFailedError    * with a canned error message.    */
DECL|method|assertNotNull (String message, Object object)
specifier|public
specifier|static
name|void
name|assertNotNull
parameter_list|(
name|String
name|message
parameter_list|,
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|fail
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

