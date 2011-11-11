begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkNotNull
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_comment
comment|/**  * Tests serialization and deserialization of an object, optionally asserting  * that the resulting object is equal to the original.  *  *  * @author Mike Bostock  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|SerializableTester
specifier|public
specifier|final
class|class
name|SerializableTester
block|{
DECL|method|SerializableTester ()
specifier|private
name|SerializableTester
parameter_list|()
block|{}
comment|/**    * Serializes and deserializes the specified object.    *    *<p>Note that the specified object may not be known by the compiler to be a    * {@link java.io.Serializable} instance, and is thus declared an    * {@code Object}. For example, it might be declared as a {@code List}.    *    * @return the re-serialized object    * @throws RuntimeException if the specified object was not successfully    *     serialized or deserialized    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|reserialize (T object)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|reserialize
parameter_list|(
name|T
name|object
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|ObjectOutputStream
name|out
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|ObjectInputStream
name|in
init|=
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|T
operator|)
name|in
operator|.
name|readObject
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Serializes and deserializes the specified object and verifies that the    * re-serialized object is equal to the provided object, that the hashcodes    * are identical, and that the class of the re-serialized object is identical    * to that of the original.    *    *<p>Note that the specified object may not be known by the compiler to be a    * {@link java.io.Serializable} instance, and is thus declared an    * {@code Object}. For example, it might be declared as a {@code List}.    *    *<p>Note also that serialization is not in general required to return an    * object that is {@linkplain Object#equals equal} to the original, nor is it    * required to return even an object of the same class. For example, if    * sublists of {@code MyList} instances were serializable, those sublists    * might implement a private {@code MySubList} type but serialize as a plain    * {@code MyList} to save space. So long as {@code MyList} has all the public    * supertypes of {@code MySubList}, this is safe. For these cases, for which    * {@code reserializeAndAssert} is too strict, use {@link #reserialize}.    *    * @return the re-serialized object    * @throws RuntimeException if the specified object was not successfully    *     serialized or deserialized    * @throws GuavaAsserts.TestAssertionFailure if the re-serialized object is    *     not equal to the original object, or if the hashcodes are different.    */
DECL|method|reserializeAndAssert (T object)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|reserializeAndAssert
parameter_list|(
name|T
name|object
parameter_list|)
block|{
name|T
name|copy
init|=
name|reserialize
argument_list|(
name|object
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|object
argument_list|,
name|copy
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
name|GuavaAsserts
operator|.
name|assertEquals
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|,
name|copy
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
block|}
end_class

end_unit

