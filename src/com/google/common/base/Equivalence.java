begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A strategy for determining whether two instances are considered equivalent. Examples of  * equivalences are the {@link Equivalences#identity() identity equivalence} and {@link  * Equivalences#equals equals equivalence}.  *  * @author Bob Lee  * @since Guava release 04  */
end_comment

begin_interface
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|interface|Equivalence
specifier|public
interface|interface
name|Equivalence
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns {@code true} if the given objects are considered equivalent.    *    *<p>The {@code equivalent} method implements an equivalence relation on object references:    *    *<ul>    *<li>It is<i>reflexive</i>: for any reference {@code x}, including null, {@code    *     equivalent(x, x)} should return {@code true}.    *<li>It is<i>symmetric</i>: for any references {@code x} and {@code y}, {@code    *     equivalent(x, y) == equivalent(y, x)}.    *<li>It is<i>transitive</i>: for any references {@code x}, {@code y}, and {@code z}, if    *     {@code equivalent(x, y)} returns {@code true} and {@code equivalent(y, z)} returns {@code    *     true}, then {@code equivalent(x, z)} should return {@code true}.    *<li>It is<i>consistent</i>: for any references {@code x} and {@code y}, multiple invocations    *     of {@code equivalent(x, y)} consistently return {@code true} or consistently return {@code    *     false} (provided that neither {@code x} nor {@code y} is modified).    *</ul>    */
DECL|method|equivalent (@ullable T a, @Nullable T b)
name|boolean
name|equivalent
parameter_list|(
annotation|@
name|Nullable
name|T
name|a
parameter_list|,
annotation|@
name|Nullable
name|T
name|b
parameter_list|)
function_decl|;
comment|/**    * Returns a hash code for {@code object}. This function<b>must</b> return the same value for    * any two references which are {@link #equivalent}, and should as often as possible return a    * distinct value for references which are not equivalent. It should support null references.    *    * @see Object#hashCode the same contractual obligations apply here    */
DECL|method|hash (@ullable T t)
name|int
name|hash
parameter_list|(
annotation|@
name|Nullable
name|T
name|t
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

