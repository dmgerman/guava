begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Helper functions that operate on any {@code Object}, and are not already provided in  * {@link java.util.Objects}.  *  *<p>See the Guava User Guide on<a  * href="https://github.com/google/guava/wiki/CommonObjectUtilitiesExplained">writing  * {@code Object} methods with {@code MoreObjects}</a>.  *  * @author Laurence Gonsalves  * @since 18.0 (since 2.0 as {@code Objects})  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MoreObjects
specifier|public
specifier|final
class|class
name|MoreObjects
block|{
comment|/**    * Returns the first of two given parameters that is not {@code null}, if either is, or otherwise    * throws a {@link NullPointerException}.    *    *<p><b>Note:</b> if {@code first} is represented as an {@link Optional}, this can be    * accomplished with {@link Optional#or(Object) first.or(second)}. That approach also allows for    * lazy evaluation of the fallback instance, using {@link Optional#or(Supplier)    * first.or(supplier)}.    *    * @return {@code first} if it is non-null; otherwise {@code second} if it is non-null    * @throws NullPointerException if both {@code first} and {@code second} are null    * @since 18.0 (since 3.0 as {@code Objects.firstNonNull()}).    */
annotation|@
name|CheckReturnValue
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
name|checkNotNull
argument_list|(
name|second
argument_list|)
return|;
block|}
comment|/**    * Creates an instance of {@link ToStringHelper}.    *    *<p>This is helpful for implementing {@link Object#toString()}.    * Specification by example:<pre>   {@code    *   // Returns "ClassName{}"    *   MoreObjects.toStringHelper(this)    *       .toString();    *    *   // Returns "ClassName{x=1}"    *   MoreObjects.toStringHelper(this)    *       .add("x", 1)    *       .toString();    *    *   // Returns "MyObject{x=1}"    *   MoreObjects.toStringHelper("MyObject")    *       .add("x", 1)    *       .toString();    *    *   // Returns "ClassName{x=1, y=foo}"    *   MoreObjects.toStringHelper(this)    *       .add("x", 1)    *       .add("y", "foo")    *       .toString();    *    *   // Returns "ClassName{x=1}"    *   MoreObjects.toStringHelper(this)    *       .omitNullValues()    *       .add("x", 1)    *       .add("y", null)    *       .toString();    *   }}</pre>    *    *<p>Note that in GWT, class names are often obfuscated.    *    * @param self the object to generate the string for (typically {@code this}), used only for its    *     class name    * @since 18.0 (since 2.0 as {@code Objects.toStringHelper()}).    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as {@link    * #toStringHelper(Object)}, but using the simple name of {@code clazz} instead of using an    * instance's {@link Object#getClass()}.    *    *<p>Note that in GWT, class names are often obfuscated.    *    * @param clazz the {@link Class} of the instance    * @since 18.0 (since 7.0 as {@code Objects.toStringHelper()}).    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Creates an instance of {@link ToStringHelper} in the same manner as {@link    * #toStringHelper(Object)}, but using {@code className} instead of using an instance's {@link    * Object#getClass()}.    *    * @param className the name of the instance type    * @since 18.0 (since 7.0 as {@code Objects.toStringHelper()}).    */
annotation|@
name|CheckReturnValue
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
comment|/**    * Support class for {@link MoreObjects#toStringHelper}.    *    * @author Jason Lee    * @since 18.0 (since 2.0 as {@code Objects.ToStringHelper}).    */
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
comment|/**      * Use {@link MoreObjects#toStringHelper(Object)} to create an instance.      */
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
comment|/**      * Configures the {@link ToStringHelper} so {@link #toString()} will ignore      * properties with null value. The order of calling this method, relative      * to the {@code add()}/{@code addValue()} methods, is not significant.      *      * @since 18.0 (since 12.0 as {@code Objects.ToStringHelper.omitNullValues()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds a name/value pair to the formatted output in {@code name=value}      * format.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.add()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, boolean)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, char)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, double)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, float)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, int)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Adds an unnamed value to the formatted output.      *      *<p>It is strongly encouraged to use {@link #add(String, long)} instead      * and give value a readable name.      *      * @since 18.0 (since 11.0 as {@code Objects.ToStringHelper.addValue()}).      */
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
comment|/**      * Returns a string in the format specified by      * {@link MoreObjects#toStringHelper(Object)}.      *      *<p>After calling this method, you can keep adding more properties to later      * call toString() again and get a more complete representation of the      * same object; but properties cannot be removed, so this only allows      * limited reuse of the helper instance. The helper allows duplication of      * properties (multiple name/value pairs with the same name can be added).      */
annotation|@
name|CheckReturnValue
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
name|Object
name|value
init|=
name|valueHolder
operator|.
name|value
decl_stmt|;
if|if
condition|(
operator|!
name|omitNullValuesSnapshot
operator|||
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
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Object
index|[]
name|objectArray
init|=
block|{
name|value
block|}
decl_stmt|;
name|String
name|arrayString
init|=
name|Arrays
operator|.
name|deepToString
argument_list|(
name|objectArray
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|arrayString
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|arrayString
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
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
DECL|method|MoreObjects ()
specifier|private
name|MoreObjects
parameter_list|()
block|{}
block|}
end_class

end_unit

