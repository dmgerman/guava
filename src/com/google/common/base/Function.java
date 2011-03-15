begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * Determines an output value based on an input value.  *  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|Function
specifier|public
interface|interface
name|Function
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
block|{
comment|/**    * Returns the result of applying this function to {@code input}. This method is<i>generally    * expected</i>, but not absolutely required, to have the following properties:    *    *<ul>    *<li>Its execution does not cause any observable side effects.    *<li>The computation is<i>consistent with equals</i>; that is, {@link Objects#equal    *     Objects.equal}{@code (a, b)} implies that {@code Objects.equal(function.apply(a),    *     function.apply(b))}.    *</ul>    *    * @throws NullPointerException if {@code input} is null and this function does not accept null    *     arguments    */
DECL|method|apply (@ullable F input)
name|T
name|apply
parameter_list|(
annotation|@
name|Nullable
name|F
name|input
parameter_list|)
function_decl|;
comment|/**    * Indicates whether another object is equal to this function.    *    *<p>Most implementations will have no reason to override the behavior of {@link Object#equals}.    * However, an implementation may also choose to return {@code true} whenever {@code object} is a    * {@link Function} that it considers<i>interchangeable</i> with this one. "Interchangeable"    *<i>typically</i> means that {@code Objects.equal(this.apply(f), that.apply(f))} is true for all    * {@code f} of type {@code F}). Note that a {@code false} result from this method does not imply    * that the functions are known<i>not</i> to be interchangeable.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

