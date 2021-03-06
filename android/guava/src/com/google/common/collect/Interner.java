begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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

begin_comment
comment|/**  * Provides similar behavior to {@link String#intern} for any immutable type. Common implementations  * are available from the {@link Interners} class.  *  *<p>Note that {@code String.intern()} has some well-known performance limitations, and should  * generally be avoided. Prefer {@link Interners#newWeakInterner} or another {@code Interner}  * implementation even for {@code String} interning.  *  * @author Kevin Bourrillion  * @since 3.0  */
end_comment

begin_interface
annotation|@
name|DoNotMock
argument_list|(
literal|"Use Interners.new*Interner"
argument_list|)
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|interface|Interner
specifier|public
interface|interface
name|Interner
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**    * Chooses and returns the representative instance for any of a collection of instances that are    * equal to each other. If two {@linkplain Object#equals equal} inputs are given to this method,    * both calls will return the same instance. That is, {@code intern(a).equals(a)} always holds,    * and {@code intern(a) == intern(b)} if and only if {@code a.equals(b)}. Note that {@code    * intern(a)} is permitted to return one instance now and a different instance later if the    * original interned instance was garbage-collected.    *    *<p><b>Warning:</b> do not use with mutable objects.    *    * @throws NullPointerException if {@code sample} is null    */
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(cpovirk): Consider removing this?
DECL|method|intern (E sample)
name|E
name|intern
parameter_list|(
name|E
name|sample
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

