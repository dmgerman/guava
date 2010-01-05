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
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * Helper functions that can operate on any {@code Object}.  *  * @author Laurence Gonsalves  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Objects
specifier|public
specifier|final
class|class
name|Objects
block|{
DECL|method|Objects ()
specifier|private
name|Objects
parameter_list|()
block|{}
comment|/**    * Determines whether two possibly-null objects are equal. Returns:    *    *<ul>    *<li>{@code true} if {@code a} and {@code b} are both null.    *<li>{@code true} if {@code a} and {@code b} are both non-null and they are    *     equal according to {@link Object#equals(Object)}.    *<li>{@code false} in all other situations.    *</ul>    *    *<p>This assumes that any non-null objects passed to this function conform    * to the {@code equals()} contract.    */
DECL|method|equal (@ullable Object a, @Nullable Object b)
specifier|public
specifier|static
name|boolean
name|equal
parameter_list|(
annotation|@
name|Nullable
name|Object
name|a
parameter_list|,
annotation|@
name|Nullable
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
operator|||
operator|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
operator|)
return|;
block|}
comment|/**    * Generates a hash code for multiple values. The hash code is generated by    * calling {@link Arrays#hashCode(Object[])}.    *    *<p>This is useful for implementing {@link Object#hashCode()}. For example,    * in an object that has three properties, {@code x}, {@code y}, and    * {@code z}, one could write:    *<pre>    * public int hashCode() {    *   return Objects.hashCode(getX(), getY(), getZ());    * }</pre>    *    *<b>Warning</b>: When a single object is supplied, the returned hash code    * does not equal the hash code of that object.    */
DECL|method|hashCode (Object... objects)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
name|Object
modifier|...
name|objects
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|hashCode
argument_list|(
name|objects
argument_list|)
return|;
block|}
block|}
end_class

end_unit

