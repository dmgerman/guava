begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|checkArgument
import|;
end_import

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
import|import static
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|WARNING
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
comment|/**  * Static utility methods pertaining to {@code String} or {@code CharSequence} instances.  *  * @author Kevin Bourrillion  * @since 3.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Strings
specifier|public
specifier|final
class|class
name|Strings
block|{
DECL|method|Strings ()
specifier|private
name|Strings
parameter_list|()
block|{}
comment|/**    * Returns the given string if it is non-null; the empty string otherwise.    *    * @param string the string to test and possibly return    * @return {@code string} itself if it is non-null; {@code ""} if it is null    */
DECL|method|nullToEmpty (@ullable String string)
specifier|public
specifier|static
name|String
name|nullToEmpty
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|nullToEmpty
argument_list|(
name|string
argument_list|)
return|;
block|}
comment|/**    * Returns the given string if it is nonempty; {@code null} otherwise.    *    * @param string the string to test and possibly return    * @return {@code string} itself if it is nonempty; {@code null} if it is empty or null    */
DECL|method|emptyToNull (@ullable String string)
specifier|public
specifier|static
annotation|@
name|Nullable
name|String
name|emptyToNull
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|emptyToNull
argument_list|(
name|string
argument_list|)
return|;
block|}
comment|/**    * Returns {@code true} if the given string is null or is the empty string.    *    *<p>Consider normalizing your string references with {@link #nullToEmpty}. If you do, you can    * use {@link String#isEmpty()} instead of this method, and you won't need special null-safe forms    * of methods like {@link String#toUpperCase} either. Or, if you'd like to normalize "in the other    * direction," converting empty strings to {@code null}, you can use {@link #emptyToNull}.    *    * @param string a string reference to check    * @return {@code true} if the string is null or is the empty string    */
DECL|method|isNullOrEmpty (@ullable String string)
specifier|public
specifier|static
name|boolean
name|isNullOrEmpty
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|stringIsNullOrEmpty
argument_list|(
name|string
argument_list|)
return|;
block|}
comment|/**    * Returns a string, of length at least {@code minLength}, consisting of {@code string} prepended    * with as many copies of {@code padChar} as are necessary to reach that length. For example,    *    *<ul>    *<li>{@code padStart("7", 3, '0')} returns {@code "007"}    *<li>{@code padStart("2010", 3, '0')} returns {@code "2010"}    *</ul>    *    *<p>See {@link java.util.Formatter} for a richer set of formatting capabilities.    *    * @param string the string which should appear at the end of the result    * @param minLength the minimum length the resulting string must have. Can be zero or negative, in    *     which case the input string is always returned.    * @param padChar the character to insert at the beginning of the result until the minimum length    *     is reached    * @return the padded string    */
DECL|method|padStart (String string, int minLength, char padChar)
specifier|public
specifier|static
name|String
name|padStart
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|minLength
parameter_list|,
name|char
name|padChar
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// eager for GWT.
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|>=
name|minLength
condition|)
block|{
return|return
name|string
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|minLength
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|string
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|minLength
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|padChar
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|string
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns a string, of length at least {@code minLength}, consisting of {@code string} appended    * with as many copies of {@code padChar} as are necessary to reach that length. For example,    *    *<ul>    *<li>{@code padEnd("4.", 5, '0')} returns {@code "4.000"}    *<li>{@code padEnd("2010", 3, '!')} returns {@code "2010"}    *</ul>    *    *<p>See {@link java.util.Formatter} for a richer set of formatting capabilities.    *    * @param string the string which should appear at the beginning of the result    * @param minLength the minimum length the resulting string must have. Can be zero or negative, in    *     which case the input string is always returned.    * @param padChar the character to append to the end of the result until the minimum length is    *     reached    * @return the padded string    */
DECL|method|padEnd (String string, int minLength, char padChar)
specifier|public
specifier|static
name|String
name|padEnd
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|minLength
parameter_list|,
name|char
name|padChar
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// eager for GWT.
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|>=
name|minLength
condition|)
block|{
return|return
name|string
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|minLength
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|string
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
name|string
operator|.
name|length
argument_list|()
init|;
name|i
operator|<
name|minLength
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|padChar
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns a string consisting of a specific number of concatenated copies of an input string. For    * example, {@code repeat("hey", 3)} returns the string {@code "heyheyhey"}.    *    * @param string any non-null string    * @param count the number of times to repeat it; a nonnegative integer    * @return a string containing {@code string} repeated {@code count} times (the empty string if    *     {@code count} is zero)    * @throws IllegalArgumentException if {@code count} is negative    */
DECL|method|repeat (String string, int count)
specifier|public
specifier|static
name|String
name|repeat
parameter_list|(
name|String
name|string
parameter_list|,
name|int
name|count
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
comment|// eager for GWT.
if|if
condition|(
name|count
operator|<=
literal|1
condition|)
block|{
name|checkArgument
argument_list|(
name|count
operator|>=
literal|0
argument_list|,
literal|"invalid count: %s"
argument_list|,
name|count
argument_list|)
expr_stmt|;
return|return
operator|(
name|count
operator|==
literal|0
operator|)
condition|?
literal|""
else|:
name|string
return|;
block|}
comment|// IF YOU MODIFY THE CODE HERE, you must update StringsRepeatBenchmark
specifier|final
name|int
name|len
init|=
name|string
operator|.
name|length
argument_list|()
decl_stmt|;
specifier|final
name|long
name|longSize
init|=
operator|(
name|long
operator|)
name|len
operator|*
operator|(
name|long
operator|)
name|count
decl_stmt|;
specifier|final
name|int
name|size
init|=
operator|(
name|int
operator|)
name|longSize
decl_stmt|;
if|if
condition|(
name|size
operator|!=
name|longSize
condition|)
block|{
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
literal|"Required array size too large: "
operator|+
name|longSize
argument_list|)
throw|;
block|}
specifier|final
name|char
index|[]
name|array
init|=
operator|new
name|char
index|[
name|size
index|]
decl_stmt|;
name|string
operator|.
name|getChars
argument_list|(
literal|0
argument_list|,
name|len
argument_list|,
name|array
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|int
name|n
decl_stmt|;
for|for
control|(
name|n
operator|=
name|len
init|;
name|n
operator|<
name|size
operator|-
name|n
condition|;
name|n
operator|<<=
literal|1
control|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|n
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|array
argument_list|,
name|n
argument_list|,
name|size
operator|-
name|n
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|array
argument_list|)
return|;
block|}
comment|/**    * Returns the longest string {@code prefix} such that {@code a.toString().startsWith(prefix)&&    * b.toString().startsWith(prefix)}, taking care not to split surrogate pairs. If {@code a} and    * {@code b} have no common prefix, returns the empty string.    *    * @since 11.0    */
DECL|method|commonPrefix (CharSequence a, CharSequence b)
specifier|public
specifier|static
name|String
name|commonPrefix
parameter_list|(
name|CharSequence
name|a
parameter_list|,
name|CharSequence
name|b
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|int
name|maxPrefixLength
init|=
name|Math
operator|.
name|min
argument_list|(
name|a
operator|.
name|length
argument_list|()
argument_list|,
name|b
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|p
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|p
operator|<
name|maxPrefixLength
operator|&&
name|a
operator|.
name|charAt
argument_list|(
name|p
argument_list|)
operator|==
name|b
operator|.
name|charAt
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|p
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|validSurrogatePairAt
argument_list|(
name|a
argument_list|,
name|p
operator|-
literal|1
argument_list|)
operator|||
name|validSurrogatePairAt
argument_list|(
name|b
argument_list|,
name|p
operator|-
literal|1
argument_list|)
condition|)
block|{
name|p
operator|--
expr_stmt|;
block|}
return|return
name|a
operator|.
name|subSequence
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Returns the longest string {@code suffix} such that {@code a.toString().endsWith(suffix)&&    * b.toString().endsWith(suffix)}, taking care not to split surrogate pairs. If {@code a} and    * {@code b} have no common suffix, returns the empty string.    *    * @since 11.0    */
DECL|method|commonSuffix (CharSequence a, CharSequence b)
specifier|public
specifier|static
name|String
name|commonSuffix
parameter_list|(
name|CharSequence
name|a
parameter_list|,
name|CharSequence
name|b
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|int
name|maxSuffixLength
init|=
name|Math
operator|.
name|min
argument_list|(
name|a
operator|.
name|length
argument_list|()
argument_list|,
name|b
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|s
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|s
operator|<
name|maxSuffixLength
operator|&&
name|a
operator|.
name|charAt
argument_list|(
name|a
operator|.
name|length
argument_list|()
operator|-
name|s
operator|-
literal|1
argument_list|)
operator|==
name|b
operator|.
name|charAt
argument_list|(
name|b
operator|.
name|length
argument_list|()
operator|-
name|s
operator|-
literal|1
argument_list|)
condition|)
block|{
name|s
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|validSurrogatePairAt
argument_list|(
name|a
argument_list|,
name|a
operator|.
name|length
argument_list|()
operator|-
name|s
operator|-
literal|1
argument_list|)
operator|||
name|validSurrogatePairAt
argument_list|(
name|b
argument_list|,
name|b
operator|.
name|length
argument_list|()
operator|-
name|s
operator|-
literal|1
argument_list|)
condition|)
block|{
name|s
operator|--
expr_stmt|;
block|}
return|return
name|a
operator|.
name|subSequence
argument_list|(
name|a
operator|.
name|length
argument_list|()
operator|-
name|s
argument_list|,
name|a
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * True when a valid surrogate pair starts at the given {@code index} in the given {@code string}.    * Out-of-range indexes return false.    */
annotation|@
name|VisibleForTesting
DECL|method|validSurrogatePairAt (CharSequence string, int index)
specifier|static
name|boolean
name|validSurrogatePairAt
parameter_list|(
name|CharSequence
name|string
parameter_list|,
name|int
name|index
parameter_list|)
block|{
return|return
name|index
operator|>=
literal|0
operator|&&
name|index
operator|<=
operator|(
name|string
operator|.
name|length
argument_list|()
operator|-
literal|2
operator|)
operator|&&
name|Character
operator|.
name|isHighSurrogate
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|index
argument_list|)
argument_list|)
operator|&&
name|Character
operator|.
name|isLowSurrogate
argument_list|(
name|string
operator|.
name|charAt
argument_list|(
name|index
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the given {@code template} string with each occurrence of {@code "%s"} replaced with    * the corresponding argument value from {@code args}; or, if the placeholder and argument counts    * do not match, returns a best-effort form of that string. Will not throw an exception under    * normal conditions.    *    *<p><b>Note:</b> For most string-formatting needs, use {@link String#format String.format},    * {@link java.io.PrintWriter#format PrintWriter.format}, and related methods. These support the    * full range of<a    * href="https://docs.oracle.com/javase/9/docs/api/java/util/Formatter.html#syntax">format    * specifiers</a>, and alert you to usage errors by throwing {@link    * java.util.IllegalFormatException}.    *    *<p>In certain cases, such as outputting debugging information or constructing a message to be    * used for another unchecked exception, an exception during string formatting would serve little    * purpose except to supplant the real information you were trying to provide. These are the cases    * this method is made for; it instead generates a best-effort string with all supplied argument    * values present. This method is also useful in environments such as GWT where {@code    * String.format} is not available. As an example, method implementations of the {@link    * Preconditions} class use this formatter, for both of the reasons just discussed.    *    *<p><b>Warning:</b> Only the exact two-character placeholder sequence {@code "%s"} is    * recognized.    *    * @param template a string containing zero or more {@code "%s"} placeholder sequences. {@code    *     null} is treated as the four-character string {@code "null"}.    * @param args the arguments to be substituted into the message template. The first argument    *     specified is substituted for the first occurrence of {@code "%s"} in the template, and so    *     forth. A {@code null} argument is converted to the four-character string {@code "null"};    *     non-null values are converted to strings using {@link Object#toString()}.    * @since 25.1    */
comment|// TODO(diamondm) consider using Arrays.toString() for array parameters
DECL|method|lenientFormat ( @ullable String template, @Nullable Object @Nullable ... args)
specifier|public
specifier|static
name|String
name|lenientFormat
parameter_list|(
annotation|@
name|Nullable
name|String
name|template
parameter_list|,
annotation|@
name|Nullable
name|Object
annotation|@
name|Nullable
modifier|...
name|args
parameter_list|)
block|{
name|template
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|template
argument_list|)
expr_stmt|;
comment|// null -> "null"
if|if
condition|(
name|args
operator|==
literal|null
condition|)
block|{
name|args
operator|=
operator|new
name|Object
index|[]
block|{
literal|"(Object[])null"
block|}
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|lenientToString
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|// start substituting the arguments into the '%s' placeholders
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|template
operator|.
name|length
argument_list|()
operator|+
literal|16
operator|*
name|args
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|templateStart
init|=
literal|0
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|int
name|placeholderStart
init|=
name|template
operator|.
name|indexOf
argument_list|(
literal|"%s"
argument_list|,
name|templateStart
argument_list|)
decl_stmt|;
if|if
condition|(
name|placeholderStart
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|builder
operator|.
name|append
argument_list|(
name|template
argument_list|,
name|templateStart
argument_list|,
name|placeholderStart
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
name|templateStart
operator|=
name|placeholderStart
operator|+
literal|2
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|template
argument_list|,
name|templateStart
argument_list|,
name|template
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
comment|// if we run out of placeholders, append the extra args in square braces
if|if
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|" ["
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
while|while
condition|(
name|i
operator|<
name|args
operator|.
name|length
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|args
index|[
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|lenientToString (@ullable Object o)
specifier|private
specifier|static
name|String
name|lenientToString
parameter_list|(
annotation|@
name|Nullable
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
try|try
block|{
return|return
name|o
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Default toString() behavior - see Object.toString()
name|String
name|objectToString
init|=
name|o
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|'@'
operator|+
name|Integer
operator|.
name|toHexString
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|o
argument_list|)
argument_list|)
decl_stmt|;
comment|// Logger is created inline with fixed name to avoid forcing Proguard to create another class.
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"com.google.common.base.Strings"
argument_list|)
operator|.
name|log
argument_list|(
name|WARNING
argument_list|,
literal|"Exception during lenientFormat for "
operator|+
name|objectToString
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|"<"
operator|+
name|objectToString
operator|+
literal|" threw "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|">"
return|;
block|}
block|}
block|}
end_class

end_unit

