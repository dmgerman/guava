begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|CheckReturnValue
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
comment|/**  * Helper functions that can operate on any {@code Object}.  *  *<p>See the Guava User Guide on<a  * href="https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained">writing  * {@code Object} methods with {@code Objects}</a>.  *  * @author Laurence Gonsalves  * @since 2.0  */
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
comment|/**    * Determines whether two possibly-null objects are equal. Returns:    *    *<ul>    *<li>{@code true} if {@code a} and {@code b} are both null.    *<li>{@code true} if {@code a} and {@code b} are both non-null and they are    *     equal according to {@link Object#equals(Object)}.    *<li>{@code false} in all other situations.    *</ul>    *    *<p>This assumes that any non-null objects passed to this function conform    * to the {@code equals()} contract.    *    *<p><b>Note for Java 7 and later:</b> This method should be treated as    * deprecated; use {@link java.util.Objects#equals} instead.    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Generates a hash code for multiple values. The hash code is generated by    * calling {@link Arrays#hashCode(Object[])}. Note that array arguments to    * this method, with the exception of a single Object array, do not get any    * special handling; their hash codes are based on identity and not contents.    *    *<p>This is useful for implementing {@link Object#hashCode()}. For example,    * in an object that has three properties, {@code x}, {@code y}, and    * {@code z}, one could write:    *<pre>   {@code    *   public int hashCode() {    *     return Objects.hashCode(getX(), getY(), getZ());    *   }}</pre>    *    *<p><b>Warning:</b> When a single object is supplied, the returned hash code    * does not equal the hash code of that object.    *    *<p><b>Note for Java 7 and later:</b> This method should be treated as    * deprecated; use {@link java.util.Objects#hash} instead.    */
annotation|@
name|CheckReturnValue
DECL|method|hashCode (@ullable Object... objects)
specifier|public
specifier|static
name|int
name|hashCode
parameter_list|(
annotation|@
name|Nullable
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
comment|/**    * Creates an instance of {@link ToStringHelper}.    *    *<p>This is helpful for implementing {@link Object#toString()}.    * Specification by example:<pre>   {@code    *   // Returns "ClassName{}"    *   Objects.toStringHelper(this)    *       .toString();    *    *   // Returns "ClassName{x=1}"    *   Objects.toStringHelper(this)    *       .add("x", 1)    *       .toString();    *    *   // Returns "MyObject{x=1}"    *   Objects.toStringHelper("MyObject")    *       .add("x", 1)    *       .toString();    *    *   // Returns "ClassName{x=1, y=foo}"    *   Objects.toStringHelper(this)    *       .add("x", 1)    *       .add("y", "foo")    *       .toString();    *    *   // Returns "ClassName{x=1}"    *   Objects.toStringHelper(this)    *       .omitNullValues()    *       .add("x", 1)    *       .add("y", null)    *       .toString();    *   }}</pre>    *    *<p>Note that in GWT, class names are often obfuscated.    *    * @param self the object to generate the string for (typically {@code this}),    *        used only for its class name    * @since 2.0    * @deprecated Use {@link MoreObjects#toStringHelper(Object)} instead. This    *     method is scheduled for removal in August 2016.    */
annotation|@
name|CheckReturnValue
annotation|@
name|Deprecated
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
name|self
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as    * {@link Objects#toStringHelper(Object)}, but using the name of {@code clazz}    * instead of using an instance's {@link Object#getClass()}.    *    *<p>Note that in GWT, class names are often obfuscated.    *    * @param clazz the {@link Class} of the instance    * @since 7.0 (source-compatible since 2.0)    * @deprecated Use {@link MoreObjects#toStringHelper(Class)} instead. This    *     method is scheduled for removal in August 2016.    */
annotation|@
name|CheckReturnValue
annotation|@
name|Deprecated
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
name|clazz
operator|.
name|getSimpleName
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as    * {@link Objects#toStringHelper(Object)}, but using {@code className} instead    * of using an instance's {@link Object#getClass()}.    *    * @param className the name of the instance type    * @since 7.0 (source-compatible since 2.0)    * @deprecated Use {@link MoreObjects#toStringHelper(String)} instead. This    *     method is scheduled for removal in August 2016.    */
annotation|@
name|CheckReturnValue
annotation|@
name|Deprecated
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
comment|/**    * Returns the first of two given parameters that is not {@code null}, if    * either is, or otherwise throws a {@link NullPointerException}.    *    *<p><b>Note:</b> if {@code first} is represented as an {@link Optional},    * this can be accomplished with    * {@linkplain Optional#or(Object) first.or(second)}.    * That approach also allows for lazy evaluation of the fallback instance,    * using {@linkplain Optional#or(Supplier) first.or(Supplier)}.    *    * @return {@code first} if {@code first} is not {@code null}, or    *     {@code second} if {@code first} is {@code null} and {@code second} is    *     not {@code null}    * @throws NullPointerException if both {@code first} and {@code second} were    *     {@code null}    * @since 3.0    * @deprecated Use {@link MoreObjects#firstNonNull} instead. This method is    *      scheduled for removal in August 2016.    */
annotation|@
name|CheckReturnValue
annotation|@
name|Deprecated
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
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
return|;
block|}
comment|/**    * Support class for {@link Objects#toStringHelper}.    *    * @author Jason Lee    * @since 2.0    * @deprecated Use {@link MoreObjects.ToStringHelper} instead. This class is    *      scheduled for removal in August 2016.    */
annotation|@
name|Deprecated
DECL|class|ToStringHelper
specifier|public
specifier|static
specifier|final
class|class
name|ToStringHelper
block|{
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
DECL|field|holderHead
specifier|private
name|ValueHolder
name|holderHead
init|=
operator|new
name|ValueHolder
argument_list|()
decl_stmt|;
DECL|field|holderTail
specifier|private
name|ValueHolder
name|holderTail
init|=
name|holderHead
decl_stmt|;
DECL|field|omitNullValues
specifier|private
name|boolean
name|omitNullValues
init|=
literal|false
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
name|checkNotNull
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
comment|/**      * Configures the {@link ToStringHelper} so {@link #toString()} will ignore      * properties with null value. The order of calling this method, relative      * to the {@code add()}/{@code addValue()} methods, is not significant.      *      * @since 12.0      */
DECL|method|omitNullValues ()
specifier|public
name|ToStringHelper
name|omitNullValues
parameter_list|()
block|{
name|omitNullValues
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format. If {@code value} is {@code null}, the string {@code "null"}      * is used, unless {@link #omitNullValues()} is called, in which case this      * name/value pair will not be added.      */
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
name|addHolder
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, boolean value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, char value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|char
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, double value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|double
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, float value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|float
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, int value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|add (String name, long value)
specifier|public
name|ToStringHelper
name|add
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|name
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, Object)} instead      * and give value a readable name.      */
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
return|return
name|addHolder
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, boolean)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (boolean value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, char)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (char value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|char
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, double)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (double value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|double
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, float)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (float value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|float
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, int)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (int value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, long)} instead      * and give value a readable name.      *      * @since 11.0 (source-compatible since 2.0)      */
DECL|method|addValue (long value)
specifier|public
name|ToStringHelper
name|addValue
parameter_list|(
name|long
name|value
parameter_list|)
block|{
return|return
name|addHolder
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a string in the format specified by {@link      * Objects#toStringHelper(Object)}.      *      *<p>After calling this method, you can keep adding more properties to later      * call toString() again and get a more complete representation of the      * same object; but properties cannot be removed, so this only allows      * limited reuse of the helper instance. The helper allows duplication of      * properties (multiple name/value pairs with the same name can be added).      */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// create a copy to keep it consistent in case value changes
name|boolean
name|omitNullValuesSnapshot
init|=
name|omitNullValues
decl_stmt|;
name|String
name|nextSeparator
init|=
literal|""
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|32
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
for|for
control|(
name|ValueHolder
name|valueHolder
init|=
name|holderHead
operator|.
name|next
init|;
name|valueHolder
operator|!=
literal|null
condition|;
name|valueHolder
operator|=
name|valueHolder
operator|.
name|next
control|)
block|{
if|if
condition|(
operator|!
name|omitNullValuesSnapshot
operator|||
name|valueHolder
operator|.
name|value
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|nextSeparator
argument_list|)
expr_stmt|;
name|nextSeparator
operator|=
literal|", "
expr_stmt|;
if|if
condition|(
name|valueHolder
operator|.
name|name
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|valueHolder
operator|.
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|valueHolder
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
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
DECL|method|addHolder ()
specifier|private
name|ValueHolder
name|addHolder
parameter_list|()
block|{
name|ValueHolder
name|valueHolder
init|=
operator|new
name|ValueHolder
argument_list|()
decl_stmt|;
name|holderTail
operator|=
name|holderTail
operator|.
name|next
operator|=
name|valueHolder
expr_stmt|;
return|return
name|valueHolder
return|;
block|}
DECL|method|addHolder (@ullable Object value)
specifier|private
name|ToStringHelper
name|addHolder
parameter_list|(
annotation|@
name|Nullable
name|Object
name|value
parameter_list|)
block|{
name|ValueHolder
name|valueHolder
init|=
name|addHolder
argument_list|()
decl_stmt|;
name|valueHolder
operator|.
name|value
operator|=
name|value
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|addHolder (String name, @Nullable Object value)
specifier|private
name|ToStringHelper
name|addHolder
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
name|ValueHolder
name|valueHolder
init|=
name|addHolder
argument_list|()
decl_stmt|;
name|valueHolder
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|valueHolder
operator|.
name|name
operator|=
name|checkNotNull
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|class|ValueHolder
specifier|private
specifier|static
specifier|final
class|class
name|ValueHolder
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|value
name|Object
name|value
decl_stmt|;
DECL|field|next
name|ValueHolder
name|next
decl_stmt|;
block|}
block|}
block|}
end_class

end_unit

