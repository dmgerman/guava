begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * Static convenience methods that help a method or constructor check whether it was invoked  * correctly (whether its<i>preconditions</i> have been met). These methods generally accept a  * {@code boolean} expression which is expected to be {@code true} (or in the case of {@code  * checkNotNull}, an object reference which is expected to be non-null). When {@code false} (or  * {@code null}) is passed instead, the {@code Preconditions} method throws an unchecked exception,  * which helps the calling method communicate to<i>its</i> caller that<i>that</i> caller has made  * a mistake. Example:<pre>   {@code  *  *   /**  *    * Returns the positive square root of the given value.  *    *  *    * @throws IllegalArgumentException if the value is negative  *    *}{@code /  *   public static double sqrt(double value) {  *     Preconditions.checkArgument(value>= 0.0, "negative value: %s", value);  *     // calculate the square root  *   }  *  *   void exampleBadCaller() {  *     double d = sqrt(-1.0);  *   }}</pre>  *  * In this example, {@code checkArgument} throws an {@code IllegalArgumentException} to indicate  * that {@code exampleBadCaller} made an error in<i>its</i> call to {@code sqrt}.  *  *<h3>Warning about performance</h3>  *  *<p>The goal of this class is to improve readability of code, but in some circumstances this may  * come at a significant performance cost. Remember that parameter values for message construction  * must all be computed eagerly, and autoboxing and varargs array creation may happen as well, even  * when the precondition check then succeeds (as it should almost always do in production). In some  * circumstances these wasted CPU cycles and allocations can add up to a real problem.  * Performance-sensitive precondition checks can always be converted to the customary form:  *<pre>   {@code  *  *   if (value< 0.0) {  *     throw new IllegalArgumentException("negative value: " + value);  *   }}</pre>  *  *<h3>Other types of preconditions</h3>  *  *<p>Not every type of precondition failure is supported by these methods. Continue to throw  * standard JDK exceptions such as {@link java.util.NoSuchElementException} or {@link  * UnsupportedOperationException} in the situations they are intended for.  *  *<h3>Non-preconditions</h3>  *  *<p>It is of course possible to use the methods of this class to check for invalid conditions  * which are<i>not the caller's fault</i>. Doing so is<b>not recommended</b> because it is  * misleading to future readers of the code and of stack traces. See  *<a href="https://github.com/google/guava/wiki/ConditionalFailuresExplained">Conditional  * failures explained</a> in the Guava User Guide for more advice.  *  *<h3>{@code java.util.Objects.requireNonNull()}</h3>  *  *<p>Projects which use {@code com.google.common} should generally avoid the use of {@link  * java.util.Objects#requireNonNull(Object)}. Instead, use whichever of {@link  * #checkNotNull(Object)} or {@link Verify#verifyNotNull(Object)} is appropriate to the situation.  * (The same goes for the message-accepting overloads.)  *  *<h3>Only {@code %s} is supported</h3>  *  *<p>In {@code Preconditions} error message template strings, only the {@code "%s"} specifier is  * supported, not the full range of {@link java.util.Formatter} specifiers.  *  *<h3>More information</h3>  *  *<p>See the Guava User Guide on  *<a href="https://github.com/google/guava/wiki/PreconditionsExplained">using {@code  * Preconditions}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Preconditions
specifier|public
specifier|final
class|class
name|Preconditions
block|{
DECL|method|Preconditions ()
specifier|private
name|Preconditions
parameter_list|()
block|{}
comment|/**    * Ensures the truth of an expression involving one or more parameters to the calling method.    *    * @param expression a boolean expression    * @throws IllegalArgumentException if {@code expression} is false    */
DECL|method|checkArgument (boolean expression)
specifier|public
specifier|static
name|void
name|checkArgument
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
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
comment|/**    * Ensures the truth of an expression involving one or more parameters to the calling method.    *    * @param expression a boolean expression    * @param errorMessage the exception message to use if the check fails; will be converted to a    *     string using {@link String#valueOf(Object)}    * @throws IllegalArgumentException if {@code expression} is false    */
DECL|method|checkArgument (boolean expression, @Nullable Object errorMessage)
specifier|public
specifier|static
name|void
name|checkArgument
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|Object
name|errorMessage
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
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|errorMessage
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures the truth of an expression involving one or more parameters to the calling method.    *    * @param expression a boolean expression    * @param errorMessageTemplate a template for the exception message should the check fail. The    *     message is formed by replacing each {@code %s} placeholder in the template with an    *     argument. These are matched by position - the first {@code %s} gets {@code    *     errorMessageArgs[0]}, etc.  Unmatched arguments will be appended to the formatted message    *     in square braces. Unmatched placeholders will be left as-is.    * @param errorMessageArgs the arguments to be substituted into the message template. Arguments    *     are converted to strings using {@link String#valueOf(Object)}.    * @throws IllegalArgumentException if {@code expression} is false    * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or    *     {@code errorMessageArgs} is null (don't let this happen)    */
DECL|method|checkArgument ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
specifier|public
specifier|static
name|void
name|checkArgument
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
name|IllegalArgumentException
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
comment|/**    * Ensures the truth of an expression involving the state of the calling instance, but not    * involving any parameters to the calling method.    *    * @param expression a boolean expression    * @throws IllegalStateException if {@code expression} is false    */
DECL|method|checkState (boolean expression)
specifier|public
specifier|static
name|void
name|checkState
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
name|IllegalStateException
argument_list|()
throw|;
block|}
block|}
comment|/**    * Ensures the truth of an expression involving the state of the calling instance, but not    * involving any parameters to the calling method.    *    * @param expression a boolean expression    * @param errorMessage the exception message to use if the check fails; will be converted to a    *     string using {@link String#valueOf(Object)}    * @throws IllegalStateException if {@code expression} is false    */
DECL|method|checkState (boolean expression, @Nullable Object errorMessage)
specifier|public
specifier|static
name|void
name|checkState
parameter_list|(
name|boolean
name|expression
parameter_list|,
annotation|@
name|Nullable
name|Object
name|errorMessage
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
name|IllegalStateException
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|errorMessage
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**    * Ensures the truth of an expression involving the state of the calling instance, but not    * involving any parameters to the calling method.    *    * @param expression a boolean expression    * @param errorMessageTemplate a template for the exception message should the check fail. The    *     message is formed by replacing each {@code %s} placeholder in the template with an    *     argument. These are matched by position - the first {@code %s} gets {@code    *     errorMessageArgs[0]}, etc.  Unmatched arguments will be appended to the formatted message    *     in square braces. Unmatched placeholders will be left as-is.    * @param errorMessageArgs the arguments to be substituted into the message template. Arguments    *     are converted to strings using {@link String#valueOf(Object)}.    * @throws IllegalStateException if {@code expression} is false    * @throws NullPointerException if the check fails and either {@code errorMessageTemplate} or    *     {@code errorMessageArgs} is null (don't let this happen)    */
DECL|method|checkState ( boolean expression, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
specifier|public
specifier|static
name|void
name|checkState
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
name|IllegalStateException
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
comment|/**    * Ensures that an object reference passed as a parameter to the calling method is not null.    *    * @param reference an object reference    * @return the non-null reference that was validated    * @throws NullPointerException if {@code reference} is null    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkNotNull (T reference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|checkNotNull
parameter_list|(
name|T
name|reference
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
return|return
name|reference
return|;
block|}
comment|/**    * Ensures that an object reference passed as a parameter to the calling method is not null.    *    * @param reference an object reference    * @param errorMessage the exception message to use if the check fails; will be converted to a    *     string using {@link String#valueOf(Object)}    * @return the non-null reference that was validated    * @throws NullPointerException if {@code reference} is null    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkNotNull (T reference, @Nullable Object errorMessage)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|checkNotNull
parameter_list|(
name|T
name|reference
parameter_list|,
annotation|@
name|Nullable
name|Object
name|errorMessage
parameter_list|)
block|{
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|errorMessage
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|reference
return|;
block|}
comment|/**    * Ensures that an object reference passed as a parameter to the calling method is not null.    *    * @param reference an object reference    * @param errorMessageTemplate a template for the exception message should the check fail. The    *     message is formed by replacing each {@code %s} placeholder in the template with an    *     argument. These are matched by position - the first {@code %s} gets {@code    *     errorMessageArgs[0]}, etc.  Unmatched arguments will be appended to the formatted message    *     in square braces. Unmatched placeholders will be left as-is.    * @param errorMessageArgs the arguments to be substituted into the message template. Arguments    *     are converted to strings using {@link String#valueOf(Object)}.    * @return the non-null reference that was validated    * @throws NullPointerException if {@code reference} is null    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkNotNull ( T reference, @Nullable String errorMessageTemplate, @Nullable Object... errorMessageArgs)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|checkNotNull
parameter_list|(
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
if|if
condition|(
name|reference
operator|==
literal|null
condition|)
block|{
comment|// If either of these parameters is null, the right thing happens anyway
throw|throw
operator|new
name|NullPointerException
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
return|return
name|reference
return|;
block|}
comment|/*    * All recent hotspots (as of 2009) *really* like to have the natural code    *    * if (guardExpression) {    *    throw new BadException(messageExpression);    * }    *    * refactored so that messageExpression is moved to a separate String-returning method.    *    * if (guardExpression) {    *    throw new BadException(badMsg(...));    * }    *    * The alternative natural refactorings into void or Exception-returning methods are much slower.    * This is a big deal - we're talking factors of 2-8 in microbenchmarks, not just 10-20%.  (This    * is a hotspot optimizer bug, which should be fixed, but that's a separate, big project).    *    * The coding pattern above is heavily used in java.util, e.g. in ArrayList.  There is a    * RangeCheckMicroBenchmark in the JDK that was used to test this.    *    * But the methods in this class want to throw different exceptions, depending on the args, so it    * appears that this pattern is not directly applicable.  But we can use the ridiculous, devious    * trick of throwing an exception in the middle of the construction of another exception.  Hotspot    * is fine with that.    */
comment|/**    * Ensures that {@code index} specifies a valid<i>element</i> in an array, list or string of size    * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.    *    * @param index a user-supplied index identifying an element of an array, list or string    * @param size the size of that array, list or string    * @return the value of {@code index}    * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}    * @throws IllegalArgumentException if {@code size} is negative    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkElementIndex (int index, int size)
specifier|public
specifier|static
name|int
name|checkElementIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|checkElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|,
literal|"index"
argument_list|)
return|;
block|}
comment|/**    * Ensures that {@code index} specifies a valid<i>element</i> in an array, list or string of size    * {@code size}. An element index may range from zero, inclusive, to {@code size}, exclusive.    *    * @param index a user-supplied index identifying an element of an array, list or string    * @param size the size of that array, list or string    * @param desc the text to use to describe this index in an error message    * @return the value of {@code index}    * @throws IndexOutOfBoundsException if {@code index} is negative or is not less than {@code size}    * @throws IllegalArgumentException if {@code size} is negative    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkElementIndex (int index, int size, @Nullable String desc)
specifier|public
specifier|static
name|int
name|checkElementIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|,
annotation|@
name|Nullable
name|String
name|desc
parameter_list|)
block|{
comment|// Carefully optimized for execution by hotspot (explanatory comment above)
if|if
condition|(
name|index
operator|<
literal|0
operator|||
name|index
operator|>=
name|size
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
name|badElementIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|,
name|desc
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|index
return|;
block|}
DECL|method|badElementIndex (int index, int size, String desc)
specifier|private
specifier|static
name|String
name|badElementIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|,
name|String
name|desc
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
return|return
name|format
argument_list|(
literal|"%s (%s) must not be negative"
argument_list|,
name|desc
argument_list|,
name|index
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|size
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"negative size: "
operator|+
name|size
argument_list|)
throw|;
block|}
else|else
block|{
comment|// index>= size
return|return
name|format
argument_list|(
literal|"%s (%s) must be less than size (%s)"
argument_list|,
name|desc
argument_list|,
name|index
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
comment|/**    * Ensures that {@code index} specifies a valid<i>position</i> in an array, list or string of    * size {@code size}. A position index may range from zero to {@code size}, inclusive.    *    * @param index a user-supplied index identifying a position in an array, list or string    * @param size the size of that array, list or string    * @return the value of {@code index}    * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}    * @throws IllegalArgumentException if {@code size} is negative    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkPositionIndex (int index, int size)
specifier|public
specifier|static
name|int
name|checkPositionIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|)
block|{
return|return
name|checkPositionIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|,
literal|"index"
argument_list|)
return|;
block|}
comment|/**    * Ensures that {@code index} specifies a valid<i>position</i> in an array, list or string of    * size {@code size}. A position index may range from zero to {@code size}, inclusive.    *    * @param index a user-supplied index identifying a position in an array, list or string    * @param size the size of that array, list or string    * @param desc the text to use to describe this index in an error message    * @return the value of {@code index}    * @throws IndexOutOfBoundsException if {@code index} is negative or is greater than {@code size}    * @throws IllegalArgumentException if {@code size} is negative    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|checkPositionIndex (int index, int size, @Nullable String desc)
specifier|public
specifier|static
name|int
name|checkPositionIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|,
annotation|@
name|Nullable
name|String
name|desc
parameter_list|)
block|{
comment|// Carefully optimized for execution by hotspot (explanatory comment above)
if|if
condition|(
name|index
argument_list|<
literal|0
operator|||
name|index
argument_list|>
name|size
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
name|badPositionIndex
argument_list|(
name|index
argument_list|,
name|size
argument_list|,
name|desc
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|index
return|;
block|}
DECL|method|badPositionIndex (int index, int size, String desc)
specifier|private
specifier|static
name|String
name|badPositionIndex
parameter_list|(
name|int
name|index
parameter_list|,
name|int
name|size
parameter_list|,
name|String
name|desc
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
return|return
name|format
argument_list|(
literal|"%s (%s) must not be negative"
argument_list|,
name|desc
argument_list|,
name|index
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|size
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"negative size: "
operator|+
name|size
argument_list|)
throw|;
block|}
else|else
block|{
comment|// index> size
return|return
name|format
argument_list|(
literal|"%s (%s) must not be greater than size (%s)"
argument_list|,
name|desc
argument_list|,
name|index
argument_list|,
name|size
argument_list|)
return|;
block|}
block|}
comment|/**    * Ensures that {@code start} and {@code end} specify a valid<i>positions</i> in an array, list    * or string of size {@code size}, and are in order. A position index may range from zero to    * {@code size}, inclusive.    *    * @param start a user-supplied index identifying a starting position in an array, list or string    * @param end a user-supplied index identifying a ending position in an array, list or string    * @param size the size of that array, list or string    * @throws IndexOutOfBoundsException if either index is negative or is greater than {@code size},    *     or if {@code end} is less than {@code start}    * @throws IllegalArgumentException if {@code size} is negative    */
DECL|method|checkPositionIndexes (int start, int end, int size)
specifier|public
specifier|static
name|void
name|checkPositionIndexes
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|int
name|size
parameter_list|)
block|{
comment|// Carefully optimized for execution by hotspot (explanatory comment above)
if|if
condition|(
name|start
operator|<
literal|0
operator|||
name|end
argument_list|<
name|start
operator|||
name|end
argument_list|>
name|size
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
name|badPositionIndexes
argument_list|(
name|start
argument_list|,
name|end
argument_list|,
name|size
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|badPositionIndexes (int start, int end, int size)
specifier|private
specifier|static
name|String
name|badPositionIndexes
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|start
argument_list|<
literal|0
operator|||
name|start
argument_list|>
name|size
condition|)
block|{
return|return
name|badPositionIndex
argument_list|(
name|start
argument_list|,
name|size
argument_list|,
literal|"start index"
argument_list|)
return|;
block|}
if|if
condition|(
name|end
argument_list|<
literal|0
operator|||
name|end
argument_list|>
name|size
condition|)
block|{
return|return
name|badPositionIndex
argument_list|(
name|end
argument_list|,
name|size
argument_list|,
literal|"end index"
argument_list|)
return|;
block|}
comment|// end< start
return|return
name|format
argument_list|(
literal|"end index (%s) must not be less than start index (%s)"
argument_list|,
name|end
argument_list|,
name|start
argument_list|)
return|;
block|}
comment|/**    * Substitutes each {@code %s} in {@code template} with an argument. These are matched by    * position: the first {@code %s} gets {@code args[0]}, etc.  If there are more arguments than    * placeholders, the unmatched arguments will be appended to the end of the formatted message in    * square braces.    *    * @param template a non-null string containing 0 or more {@code %s} placeholders.    * @param args the arguments to be substituted into the message template. Arguments are converted    *     to strings using {@link String#valueOf(Object)}. Arguments can be null.    */
comment|// Note that this is somewhat-improperly used from Verify.java as well.
DECL|method|format (String template, @Nullable Object... args)
specifier|static
name|String
name|format
parameter_list|(
name|String
name|template
parameter_list|,
annotation|@
name|Nullable
name|Object
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
operator|.
name|substring
argument_list|(
name|templateStart
argument_list|,
name|placeholderStart
argument_list|)
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
operator|.
name|substring
argument_list|(
name|templateStart
argument_list|)
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
block|}
end_class

end_unit

