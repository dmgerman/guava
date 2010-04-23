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
name|Beta
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Collections
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

begin_comment
comment|/**  * Static utility methods pertaining to instances of {@link Throwable}.  *  * @author Kevin Bourrillion  * @author Ben Yu  * @since 1  */
end_comment

begin_class
DECL|class|Throwables
specifier|public
specifier|final
class|class
name|Throwables
block|{
DECL|method|Throwables ()
specifier|private
name|Throwables
parameter_list|()
block|{}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an    * instance of {@code declaredType}.  Example usage:    *<pre>    *   try {    *     someMethodThatCouldThrowAnything();    *   } catch (IKnowWhatToDoWithThisException e) {    *     handle(e);    *   } catch (Throwable t) {    *     Throwables.propagateIfInstanceOf(t, IOException.class);    *     Throwables.propagateIfInstanceOf(t, SQLException.class);    *     throw Throwables.propagate(t);    *   }    *</pre>    */
DECL|method|propagateIfInstanceOf ( @ullable Throwable throwable, Class<X> declaredType)
specifier|public
specifier|static
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|void
name|propagateIfInstanceOf
parameter_list|(
annotation|@
name|Nullable
name|Throwable
name|throwable
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|declaredType
parameter_list|)
throws|throws
name|X
block|{
if|if
condition|(
name|declaredType
operator|.
name|isInstance
argument_list|(
name|throwable
argument_list|)
condition|)
block|{
throw|throw
name|declaredType
operator|.
name|cast
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
block|}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an    * instance of {@link RuntimeException} or {@link Error}.  Example usage:    *<pre>    *   try {    *     someMethodThatCouldThrowAnything();    *   } catch (IKnowWhatToDoWithThisException e) {    *     handle(e);    *   } catch (Throwable t) {    *     Throwables.propagateIfPossible(t);    *     throw new RuntimeException("unexpected", t);    *   }    *</pre>    */
DECL|method|propagateIfPossible (@ullable Throwable throwable)
specifier|public
specifier|static
name|void
name|propagateIfPossible
parameter_list|(
annotation|@
name|Nullable
name|Throwable
name|throwable
parameter_list|)
block|{
name|propagateIfInstanceOf
argument_list|(
name|throwable
argument_list|,
name|Error
operator|.
name|class
argument_list|)
expr_stmt|;
name|propagateIfInstanceOf
argument_list|(
name|throwable
argument_list|,
name|RuntimeException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an    * instance of {@link RuntimeException}, {@link Error}, or    * {@code declaredType}. Example usage:    *<pre>    *   try {    *     someMethodThatCouldThrowAnything();    *   } catch (IKnowWhatToDoWithThisException e) {    *     handle(e);    *   } catch (Throwable t) {    *     Throwables.propagateIfPossible(t, OtherException.class);    *     throw new RuntimeException("unexpected", t);    *   }    *</pre>    *    * @param throwable the Throwable to possibly propagate    * @param declaredType the single checked exception type declared by the    *     calling method    */
DECL|method|propagateIfPossible ( @ullable Throwable throwable, Class<X> declaredType)
specifier|public
specifier|static
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|void
name|propagateIfPossible
parameter_list|(
annotation|@
name|Nullable
name|Throwable
name|throwable
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|declaredType
parameter_list|)
throws|throws
name|X
block|{
name|propagateIfInstanceOf
argument_list|(
name|throwable
argument_list|,
name|declaredType
argument_list|)
expr_stmt|;
name|propagateIfPossible
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an    * instance of {@link RuntimeException}, {@link Error}, {@code declaredType1},    * or {@code declaredType2}.  In the unlikely case that you have three or more    * declared checked exception types, you can handle them all by invoking these    * methods repeatedly. See usage example in {@link    * #propagateIfPossible(Throwable, Class)}.    *    * @param throwable the Throwable to possibly propagate    * @param declaredType1 any checked exception type declared by the calling    *     method    * @param declaredType2 any other checked exception type declared by the    *     calling method    */
specifier|public
specifier|static
parameter_list|<
name|X1
extends|extends
name|Throwable
parameter_list|,
name|X2
extends|extends
name|Throwable
parameter_list|>
DECL|method|propagateIfPossible (@ullable Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2)
name|void
name|propagateIfPossible
parameter_list|(
annotation|@
name|Nullable
name|Throwable
name|throwable
parameter_list|,
name|Class
argument_list|<
name|X1
argument_list|>
name|declaredType1
parameter_list|,
name|Class
argument_list|<
name|X2
argument_list|>
name|declaredType2
parameter_list|)
throws|throws
name|X1
throws|,
name|X2
block|{
name|checkNotNull
argument_list|(
name|declaredType2
argument_list|)
expr_stmt|;
name|propagateIfInstanceOf
argument_list|(
name|throwable
argument_list|,
name|declaredType1
argument_list|)
expr_stmt|;
name|propagateIfPossible
argument_list|(
name|throwable
argument_list|,
name|declaredType2
argument_list|)
expr_stmt|;
block|}
comment|/**    * Propagates {@code throwable} as-is if it is an instance of    * {@link RuntimeException} or {@link Error}, or else as a last resort, wraps    * it in a {@code RuntimeException} then propagates.    *<p>    * This method always throws an exception. The {@code RuntimeException} return    * type is only for client code to make Java type system happy in case a    * return value is required by the enclosing method. Example usage:    *<pre>    *   T doSomething() {    *     try {    *       return someMethodThatCouldThrowAnything();    *     } catch (IKnowWhatToDoWithThisException e) {    *       return handle(e);    *     } catch (Throwable t) {    *       throw Throwables.propagate(t);    *     }    *   }    *</pre>    *    * @param throwable the Throwable to propagate    * @return nothing will ever be returned; this return type is only for your    *     convenience, as illustrated in the example above    */
DECL|method|propagate (Throwable throwable)
specifier|public
specifier|static
name|RuntimeException
name|propagate
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|propagateIfPossible
argument_list|(
name|checkNotNull
argument_list|(
name|throwable
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
comment|/**    * Returns the innermost cause of {@code throwable}. The first throwable in a    * chain provides context from when the error or exception was initially    * detected. Example usage:    *<pre>    *   assertEquals("Unable to assign a customer id",    *       Throwables.getRootCause(e).getMessage());    *</pre>    */
DECL|method|getRootCause (Throwable throwable)
specifier|public
specifier|static
name|Throwable
name|getRootCause
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|Throwable
name|cause
decl_stmt|;
while|while
condition|(
operator|(
name|cause
operator|=
name|throwable
operator|.
name|getCause
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|throwable
operator|=
name|cause
expr_stmt|;
block|}
return|return
name|throwable
return|;
block|}
comment|/**    * Gets a {@code Throwable} cause chain as a list.  The first entry in the    * list will be {@code throwable} followed by its cause hierarchy.  Note    * that this is a snapshot of the cause chain and will not reflect    * any subsequent changes to the cause chain.    *    *<p>Here's an example of how it can be used to find specific types    * of exceptions in the cause chain:    *    *<pre>    * Iterables.filter(Throwables.getCausalChain(e), IOException.class));    *</pre>    *    * @param throwable the non-null {@code Throwable} to extract causes from    * @return an unmodifiable list containing the cause chain starting with    *     {@code throwable}    */
annotation|@
name|Beta
DECL|method|getCausalChain (Throwable throwable)
specifier|public
specifier|static
name|List
argument_list|<
name|Throwable
argument_list|>
name|getCausalChain
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Throwable
argument_list|>
name|causes
init|=
operator|new
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
while|while
condition|(
name|throwable
operator|!=
literal|null
condition|)
block|{
name|causes
operator|.
name|add
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
name|throwable
operator|=
name|throwable
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|causes
argument_list|)
return|;
block|}
comment|/**    * Returns a string containing the result of    * {@link Throwable#toString() toString()}, followed by the full, recursive    * stack trace of {@code throwable}. Note that you probably should not be    * parsing the resulting string; if you need programmatic access to the stack    * frames, you can call {@link Throwable#getStackTrace()}.    */
DECL|method|getStackTraceAsString (Throwable throwable)
specifier|public
specifier|static
name|String
name|getStackTraceAsString
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|StringWriter
name|stringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|throwable
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|stringWriter
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|stringWriter
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Rethrows the cause exception of a given throwable, discarding the original    * throwable. Optionally, the stack frames of the cause and the outer    * exception are combined and the stack trace of the cause is set to this    * combined trace. If there is no cause the original exception is rethrown    * unchanged in all cases.    *    * @param exception the exception from which to extract the cause    * @param combineStackTraces if true the stack trace of the cause will be    *     replaced by the concatenation of the trace from the exception and the    *     trace from the cause.    */
annotation|@
name|Beta
DECL|method|throwCause (Exception exception, boolean combineStackTraces)
specifier|public
specifier|static
name|Exception
name|throwCause
parameter_list|(
name|Exception
name|exception
parameter_list|,
name|boolean
name|combineStackTraces
parameter_list|)
throws|throws
name|Exception
block|{
name|Throwable
name|cause
init|=
name|exception
operator|.
name|getCause
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|==
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
if|if
condition|(
name|combineStackTraces
condition|)
block|{
name|StackTraceElement
index|[]
name|causeTrace
init|=
name|cause
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
name|StackTraceElement
index|[]
name|outerTrace
init|=
name|exception
operator|.
name|getStackTrace
argument_list|()
decl_stmt|;
name|StackTraceElement
index|[]
name|combined
init|=
operator|new
name|StackTraceElement
index|[
name|causeTrace
operator|.
name|length
operator|+
name|outerTrace
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|causeTrace
argument_list|,
literal|0
argument_list|,
name|combined
argument_list|,
literal|0
argument_list|,
name|causeTrace
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|outerTrace
argument_list|,
literal|0
argument_list|,
name|combined
argument_list|,
name|causeTrace
operator|.
name|length
argument_list|,
name|outerTrace
operator|.
name|length
argument_list|)
expr_stmt|;
name|cause
operator|.
name|setStackTrace
argument_list|(
name|combined
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cause
operator|instanceof
name|Exception
condition|)
block|{
throw|throw
operator|(
name|Exception
operator|)
name|cause
throw|;
block|}
if|if
condition|(
name|cause
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|(
name|Error
operator|)
name|cause
throw|;
block|}
comment|// The cause is a weird kind of Throwable, so throw the outer exception
throw|throw
name|exception
throw|;
block|}
block|}
end_class

end_unit

