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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|List
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
comment|/**  * Helper functions that can operate on any {@code Object}.  *  * @author Laurence Gonsalves  * @since 2 (imported from Google Collections Library)  */
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
comment|/**    * Creates an instance of {@link ToStringHelper}.    *    *<p>This is helpful for implementing {@link Object#toString()}. For    * example, in an object that contains two member variables, {@code x},    * and {@code y}, one could write:<pre><tt>    *   public class ClassName {    *     public String toString() {    *       return Objects.toStringHelper(this)    *           .add("x", x)    *           .add("y", y)    *           .toString();    *     }    *   }</tt>    *</pre>    *    * Assuming the values of {@code x} and {@code y} are 1 and 2,    * this code snippet returns the string<tt>"ClassName{x=1, y=2}"</tt>.    *    * @param self the object to generate the string for (typically {@code this}),    *        used only for its class name    * @since 2    */
DECL|method|toStringHelper (Object self)
specifier|public
specifier|static
name|ToStringHelper
name|toStringHelper
parameter_list|(
name|Object
name|self
parameter_list|)
block|{
return|return
operator|new
name|ToStringHelper
argument_list|(
name|simpleName
argument_list|(
name|self
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as    * {@link Objects#toStringHelper(Object)}, but using the name of {@code clazz}    * instead of using an instance's {@link Object#getClass()}.    *    * @param clazz the {@link Class} of the instance    * @since 7 (source-compatible since 2)    */
DECL|method|toStringHelper (Class<?> clazz)
specifier|public
specifier|static
name|ToStringHelper
name|toStringHelper
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
operator|new
name|ToStringHelper
argument_list|(
name|simpleName
argument_list|(
name|clazz
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as    * {@link Objects#toStringHelper(Object)}, but using {@code className} instead    * of using an instance's {@link Object#getClass()}.    *    * @param className the name of the instance type    * @since 7 (source-compatible since 2)    */
DECL|method|toStringHelper (String className)
specifier|public
specifier|static
name|ToStringHelper
name|toStringHelper
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
operator|new
name|ToStringHelper
argument_list|(
name|className
argument_list|)
return|;
block|}
comment|/**    * {@link Class#getSimpleName()} is not GWT compatible yet, so we    * provide our own implementation.    */
DECL|method|simpleName (Class<?> clazz)
specifier|private
specifier|static
name|String
name|simpleName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// we want the name of the inner class all by its lonesome
name|int
name|start
init|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'$'
argument_list|)
decl_stmt|;
comment|// if this isn't an inner class, just find the start of the
comment|// top level class name.
if|if
condition|(
name|start
operator|==
operator|-
literal|1
condition|)
block|{
name|start
operator|=
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
return|return
name|name
operator|.
name|substring
argument_list|(
name|start
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/**    * Returns the first of two given parameters that is not {@code null}, if    * either is, or otherwise throws a {@link NullPointerException}.    *    * @return {@code first} if {@code first} is not {@code null}, or    *     {@code second} if {@code first} is {@code null} and {@code second} is    *     not {@code null}    * @throws NullPointerException if both {@code first} and {@code second} were    *     {@code null}    * @since 3    */
DECL|method|firstNonNull (@ullable T first, @Nullable T second)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|firstNonNull
parameter_list|(
annotation|@
name|Nullable
name|T
name|first
parameter_list|,
annotation|@
name|Nullable
name|T
name|second
parameter_list|)
block|{
return|return
name|first
operator|!=
literal|null
condition|?
name|first
else|:
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|second
argument_list|)
return|;
block|}
comment|/**    * Support class for {@link Objects#toStringHelper}.    *    * @author Jason Lee    * @since 2    */
DECL|class|ToStringHelper
specifier|public
specifier|static
class|class
name|ToStringHelper
block|{
comment|// TODO(kevinb): why are we not just appending directly to a StringBuilder?
DECL|field|fieldString
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|fieldString
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
comment|/**      * Use {@link Objects#toStringHelper(Object)} to create an instance.      */
DECL|method|ToStringHelper (String className)
specifier|private
name|ToStringHelper
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format. If {@code value} is {@code null}, the string {@code "null"}      * is used.      */
DECL|method|add (String name, @Nullable Object value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
return|return
name|addValue
argument_list|(
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|name
argument_list|)
operator|+
literal|"="
operator|+
name|value
argument_list|)
return|;
block|}
comment|/**      * Adds a value to the formatted output in {@code value} format.<p/>      *      * It is strongly encouraged to use {@link #add(String, Object)} instead and      * give value a readable name.      */
DECL|method|addValue (@ullable Object value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|fieldString
operator|.
name|add
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|field|JOINER
specifier|private
specifier|static
specifier|final
name|Joiner
name|JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
decl_stmt|;
comment|/**      * Returns the formatted string.      */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|100
argument_list|)
operator|.
name|append
argument_list|(
name|className
argument_list|)
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
decl_stmt|;
return|return
name|JOINER
operator|.
name|appendTo
argument_list|(
name|builder
argument_list|,
name|fieldString
argument_list|)
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

