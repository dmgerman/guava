begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
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
name|errorprone
operator|.
name|annotations
operator|.
name|DoNotMock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * An object which can send data from an object of type {@code T} into a {@code PrimitiveSink}.  * Implementations for common types can be found in {@link Funnels}.  *  *<p>Note that serialization of {@linkplain BloomFilter bloom filters} requires the proper  * serialization of funnels. When possible, it is recommended that funnels be implemented as a  * single-element enum to maintain serialization guarantees. See Effective Java (2nd Edition), Item  * 3: "Enforce the singleton property with a private constructor or an enum type". For example:  *  *<pre>{@code  * public enum PersonFunnel implements Funnel<Person> {  *   INSTANCE;  *   public void funnel(Person person, PrimitiveSink into) {  *     into.putUnencodedChars(person.getFirstName())  *         .putUnencodedChars(person.getLastName())  *         .putInt(person.getAge());  *   }  * }  * }</pre>  *  * @author Dimitris Andreou  * @since 11.0  */
end_comment

begin_annotation
annotation|@
name|Beta
end_annotation

begin_annotation
annotation|@
name|DoNotMock
argument_list|(
literal|"Implement with a lambda"
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|Funnel
specifier|public
expr|interface
name|Funnel
operator|<
name|T
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|Serializable
block|{
comment|/**    * Sends a stream of data from the {@code from} object into the sink {@code into}. There is no    * requirement that this data be complete enough to fully reconstitute the object later.    *    * @since 12.0 (in Guava 11.0, {@code PrimitiveSink} was named {@code Sink})    */
DECL|method|funnel (@arametricNullness T from, PrimitiveSink into)
name|void
name|funnel
argument_list|(
annotation|@
name|ParametricNullness
name|T
name|from
argument_list|,
name|PrimitiveSink
name|into
argument_list|)
block|; }
end_expr_stmt

end_unit

