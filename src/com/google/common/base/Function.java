begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A transformation from one object to another. For example, a  * {@code StringToIntegerFunction} may implement  *<code>Function&lt;String,Integer&gt;</code> and transform integers in  * {@code String} format to {@code Integer} format.  *  *<p>The transformation on the source object does not necessarily result in  * an object of a different type.  For example, a  * {@code FarenheitToCelsiusFunction} may implement  *<code>Function&lt;Float,Float&gt;</code>.  *  *<p>Implementations which may cause side effects upon evaluation are strongly  * encouraged to state this fact clearly in their API documentation.  *  * @param<F> the type of the function input  * @param<T> the type of the function output  * @author Kevin Bourrillion  * @author Scott Bonneau  * @since 2010.01.04<b>stable</b> (imported from Google Collections Library)  */
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
comment|/**    * Applies the function to an object of type {@code F}, resulting in an object    * of type {@code T}.  Note that types {@code F} and {@code T} may or may not    * be the same.    *    * @param from the source object    * @return the resulting object    */
DECL|method|apply (@ullable F from)
name|T
name|apply
parameter_list|(
annotation|@
name|Nullable
name|F
name|from
parameter_list|)
function_decl|;
comment|/**    * Indicates whether some other object is equal to this {@code Function}.    * This method can return {@code true}<i>only</i> if the specified object is    * also a {@code Function} and, for every input object {@code o}, it returns    * exactly the same value.  Thus, {@code function1.equals(function2)} implies    * that either {@code function1.apply(o)} and {@code function2.apply(o)} are    * both null, or {@code function1.apply(o).equals(function2.apply(o))}.    *    *<p>Note that it is always safe<em>not</em> to override    * {@link Object#equals}.    */
DECL|method|equals (@ullable Object obj)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

