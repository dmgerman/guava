begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|format
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
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
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
comment|/**  * Static convenience methods that serve the same purpose as Java language<a  * href="http://docs.oracle.com/javase/7/docs/technotes/guides/language/assert.html">assertions</a>,  * except that they are always enabled. These methods should be used instead of Java assertions  * whenever there is a chance the check may fail "in real life". Example:  *  *<pre>{@code  * Bill bill = remoteService.getLastUnpaidBill();  *  * // In case bug 12345 happens again we'd rather just die  * Verify.verify(bill.status() == Status.UNPAID,  *     "Unexpected bill status: %s", bill.status());  * }</pre>  *  *<h3>Comparison to alternatives</h3>  *  *<p><b>Note:</b> In some cases the differences explained below can be subtle. When it's unclear  * which approach to use,<b>don't worry</b> too much about it; just pick something that seems  * reasonable and it will be fine.  *  *<ul>  *<li>If checking whether the<i>caller</i> has violated your method or constructor's contract  *       (such as by passing an invalid argument), use the utilities of the {@link Preconditions}  *       class instead.  *<li>If checking an<i>impossible</i> condition (which<i>cannot</i> happen unless your own  *       class or its<i>trusted</i> dependencies is badly broken), this is what ordinary Java  *       assertions are for. Note that assertions are not enabled by default; they are essentially  *       considered "compiled comments."  *<li>An explicit {@code if/throw} (as illustrated below) is always acceptable; we still  *       recommend using our {@link VerifyException} exception type. Throwing a plain {@link  *       RuntimeException} is frowned upon.  *<li>Use of {@link java.util.Objects#requireNonNull(Object)} is generally discouraged, since  *       {@link #verifyNotNull(Object)} and {@link Preconditions#checkNotNull(Object)} perform the  *       same function with more clarity.  *</ul>  *  *<h3>Warning about performance</h3>  *  *<p>Remember that parameter values for message construction must all be computed eagerly, and  * autoboxing and varargs array creation may happen as well, even when the verification succeeds and  * the message ends up unneeded. Performance-sensitive verification checks should continue to use  * usual form:  *  *<pre>{@code  * Bill bill = remoteService.getLastUnpaidBill();  * if (bill.status() != Status.UNPAID) {  *   throw new VerifyException("Unexpected bill status: " + bill.status());  * }  * }</pre>  *  *<h3>Only {@code %s} is supported</h3>  *  *<p>As with {@link Preconditions} error message template strings, only the {@code "%s"} specifier  * is supported, not the full range of {@link java.util.Formatter} specifiers. However, note that if  * the number of arguments does not match the number of occurrences of {@code "%s"} in the format  * string, {@code Verify} will still behave as expected, and will still include all argument values  * in the error message; the message will simply not be formatted exactly as intended.  *  *<h3>More information</h3>  *  * See<a href="https://github.com/google/guava/wiki/ConditionalFailuresExplained">Conditional  * failures explained</a> in the Guava User Guide for advice on when this class should be used.  *  * @since 17.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Verify
specifier|public
specifier|final
class|class
name|Verify
block|{
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with no    * message otherwise.    *    * @throws VerifyException if {@code expression} is {@code false}    * @see Preconditions#checkState Preconditions.checkState()    */
DECL|method|verify (boolean expression)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|()
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    * @param expression a boolean expression    * @param errorMessageTemplate a template for the exception message should the check fail. The    *     message is formed by replacing each {@code %s} placeholder in the template with an    *     argument. These are matched by position - the first {@code %s} gets {@code    *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in    *     square braces. Unmatched placeholders will be left as-is.    * @param errorMessageArgs the arguments to be substituted into the message template. Arguments    *     are converted to strings using {@link String#valueOf(Object)}.    * @throws VerifyException if {@code expression} is {@code false}    * @see Preconditions#checkState Preconditions.checkState()    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
modifier|...
name|errorMessageArgs
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|errorMessageArgs
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify (boolean expression, @Nullable String errorMessageTemplate, char p1)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|char
name|p1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify (boolean expression, @Nullable String errorMessageTemplate, int p1)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|int
name|p1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify (boolean expression, @Nullable String errorMessageTemplate, long p1)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|long
name|p1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, char p1, char p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|char
name|p1
parameter_list|,
name|char
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, int p1, char p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|int
name|p1
parameter_list|,
name|char
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, long p1, char p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|long
name|p1
parameter_list|,
name|char
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, char p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
name|char
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, char p1, int p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|char
name|p1
parameter_list|,
name|int
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, int p1, int p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|int
name|p1
parameter_list|,
name|int
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, long p1, int p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|long
name|p1
parameter_list|,
name|int
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, int p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
name|int
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, char p1, long p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|char
name|p1
parameter_list|,
name|long
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, int p1, long p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|int
name|p1
parameter_list|,
name|long
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, long p1, long p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|long
name|p1
parameter_list|,
name|long
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, long p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
name|long
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, char p1, @Nullable Object p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|char
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, int p1, @Nullable Object p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|int
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, long p1, @Nullable Object p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
name|long
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p3
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|,
name|p3
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code expression} is {@code true}, throwing a {@code VerifyException} with a    * custom message otherwise.    *    *<p>See {@link #verify(boolean, String, Object...)} for details.    *    * @since 23.1 (varargs overload since 17.0)    */
DECL|method|verify ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object p1, @Nullable Object p2, @Nullable Object p3, @Nullable Object p4)
specifier|public
specifier|static
name|void
name|verify
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p1
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p2
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p3
parameter_list|,
annotation|@
name|Nullable
name|Object
name|p4
parameter_list|)
block|{
if|if
condition|(
operator|!
name|expression
condition|)
block|{
throw|throw
operator|new
name|VerifyException
argument_list|(
name|format
argument_list|(
name|errorMessageTemplate
argument_list|,
name|p1
argument_list|,
name|p2
argument_list|,
name|p3
argument_list|,
name|p4
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures that {@code reference} is non-null, throwing a {@code VerifyException} with a default    * message otherwise.    *    * @return {@code reference}, guaranteed to be non-null, for convenience    * @throws VerifyException if {@code reference} is {@code null}    * @see Preconditions#checkNotNull Preconditions.checkNotNull()    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|verifyNotNull (@ullable T reference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|verifyNotNull
parameter_list|(
annotation|@
name|Nullable
name|T
name|reference
parameter_list|)
block|{
return|return
name|verifyNotNull
argument_list|(
name|reference
argument_list|,
literal|"expected a non-null reference"
argument_list|)
return|;
block|}
comment|/**    * Ensures that {@code reference} is non-null, throwing a {@code VerifyException} with a custom    * message otherwise.    *    * @param errorMessageTemplate a template for the exception message should the check fail. The    *     message is formed by replacing each {@code %s} placeholder in the template with an    *     argument. These are matched by position - the first {@code %s} gets {@code    *     errorMessageArgs[0]}, etc. Unmatched arguments will be appended to the formatted message in    *     square braces. Unmatched placeholders will be left as-is.    * @param errorMessageArgs the arguments to be substituted into the message template. Arguments    *     are converted to strings using {@link String#valueOf(Object)}.    * @return {@code reference}, guaranteed to be non-null, for convenience    * @throws VerifyException if {@code reference} is {@code null}    * @see Preconditions#checkNotNull Preconditions.checkNotNull()    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|verifyNotNull ( @ullable T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|verifyNotNull
parameter_list|(
annotation|@
name|Nullable
name|T
name|reference
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessageTemplate
parameter_list|,
annotation|@
name|Nullable
name|Object
modifier|...
name|errorMessageArgs
parameter_list|)
block|{
name|verify
argument_list|(
name|reference
operator|!=
literal|null
argument_list|,
name|errorMessageTemplate
argument_list|,
name|errorMessageArgs
argument_list|)
expr_stmt|;
return|return
name|reference
return|;
block|}
comment|// TODO(kevinb): consider<T> T verifySingleton(Iterable<T>) to take over for
comment|// Iterables.getOnlyElement()
DECL|method|Verify ()
specifier|private
name|Verify
parameter_list|()
block|{}
block|}
end_class

end_unit

