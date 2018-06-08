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
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|unmodifiableList
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
name|Beta
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
name|GwtIncompatible
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractList
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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to instances of {@link Throwable}.  *  *<p>See the Guava User Guide entry on<a  * href="https://github.com/google/guava/wiki/ThrowablesExplained">Throwables</a>.  *  * @author Kevin Bourrillion  * @author Ben Yu  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
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
comment|/**    * Throws {@code throwable} if it is an instance of {@code declaredType}. Example usage:    *    *<pre>    * for (Foo foo : foos) {    *   try {    *     foo.bar();    *   } catch (BarException | RuntimeException | Error t) {    *     failure = t;    *   }    * }    * if (failure != null) {    *   throwIfInstanceOf(failure, BarException.class);    *   throwIfUnchecked(failure);    *   throw new AssertionError(failure);    * }    *</pre>    *    * @since 20.0    */
annotation|@
name|GwtIncompatible
comment|// Class.cast, Class.isInstance
DECL|method|throwIfInstanceOf ( Throwable throwable, Class<X> declaredType)
specifier|public
specifier|static
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|void
name|throwIfInstanceOf
parameter_list|(
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
name|checkNotNull
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
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
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an instance of {@code    * declaredType}. Example usage:    *    *<pre>    * try {    *   someMethodThatCouldThrowAnything();    * } catch (IKnowWhatToDoWithThisException e) {    *   handle(e);    * } catch (Throwable t) {    *   Throwables.propagateIfInstanceOf(t, IOException.class);    *   Throwables.propagateIfInstanceOf(t, SQLException.class);    *   throw Throwables.propagate(t);    * }    *</pre>    *    * @deprecated Use {@link #throwIfInstanceOf}, which has the same behavior but rejects {@code    *     null}.    */
annotation|@
name|Deprecated
annotation|@
name|GwtIncompatible
comment|// throwIfInstanceOf
DECL|method|propagateIfInstanceOf ( @ullableDecl Throwable throwable, Class<X> declaredType)
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
name|NullableDecl
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
name|throwable
operator|!=
literal|null
condition|)
block|{
name|throwIfInstanceOf
argument_list|(
name|throwable
argument_list|,
name|declaredType
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Throws {@code throwable} if it is a {@link RuntimeException} or {@link Error}. Example usage:    *    *<pre>    * for (Foo foo : foos) {    *   try {    *     foo.bar();    *   } catch (RuntimeException | Error t) {    *     failure = t;    *   }    * }    * if (failure != null) {    *   throwIfUnchecked(failure);    *   throw new AssertionError(failure);    * }    *</pre>    *    * @since 20.0    */
DECL|method|throwIfUnchecked (Throwable throwable)
specifier|public
specifier|static
name|void
name|throwIfUnchecked
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
if|if
condition|(
name|throwable
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|throwable
throw|;
block|}
if|if
condition|(
name|throwable
operator|instanceof
name|Error
condition|)
block|{
throw|throw
operator|(
name|Error
operator|)
name|throwable
throw|;
block|}
block|}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an instance of {@link    * RuntimeException} or {@link Error}. Example usage:    *    *<pre>    * try {    *   someMethodThatCouldThrowAnything();    * } catch (IKnowWhatToDoWithThisException e) {    *   handle(e);    * } catch (Throwable t) {    *   Throwables.propagateIfPossible(t);    *   throw new RuntimeException("unexpected", t);    * }    *</pre>    *    * @deprecated Use {@link #throwIfUnchecked}, which has the same behavior but rejects {@code    *     null}.    */
annotation|@
name|Deprecated
annotation|@
name|GwtIncompatible
DECL|method|propagateIfPossible (@ullableDecl Throwable throwable)
specifier|public
specifier|static
name|void
name|propagateIfPossible
parameter_list|(
annotation|@
name|NullableDecl
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|throwable
operator|!=
literal|null
condition|)
block|{
name|throwIfUnchecked
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an instance of {@link    * RuntimeException}, {@link Error}, or {@code declaredType}. Example usage:    *    *<pre>    * try {    *   someMethodThatCouldThrowAnything();    * } catch (IKnowWhatToDoWithThisException e) {    *   handle(e);    * } catch (Throwable t) {    *   Throwables.propagateIfPossible(t, OtherException.class);    *   throw new RuntimeException("unexpected", t);    * }    *</pre>    *    * @param throwable the Throwable to possibly propagate    * @param declaredType the single checked exception type declared by the calling method    */
annotation|@
name|GwtIncompatible
comment|// propagateIfInstanceOf
DECL|method|propagateIfPossible ( @ullableDecl Throwable throwable, Class<X> declaredType)
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
name|NullableDecl
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
comment|/**    * Propagates {@code throwable} exactly as-is, if and only if it is an instance of {@link    * RuntimeException}, {@link Error}, {@code declaredType1}, or {@code declaredType2}. In the    * unlikely case that you have three or more declared checked exception types, you can handle them    * all by invoking these methods repeatedly. See usage example in {@link    * #propagateIfPossible(Throwable, Class)}.    *    * @param throwable the Throwable to possibly propagate    * @param declaredType1 any checked exception type declared by the calling method    * @param declaredType2 any other checked exception type declared by the calling method    */
annotation|@
name|GwtIncompatible
comment|// propagateIfInstanceOf
DECL|method|propagateIfPossible ( @ullableDecl Throwable throwable, Class<X1> declaredType1, Class<X2> declaredType2)
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
name|void
name|propagateIfPossible
parameter_list|(
annotation|@
name|NullableDecl
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
comment|/**    * Propagates {@code throwable} as-is if it is an instance of {@link RuntimeException} or {@link    * Error}, or else as a last resort, wraps it in a {@code RuntimeException} and then propagates.    *    *<p>This method always throws an exception. The {@code RuntimeException} return type allows    * client code to signal to the compiler that statements after the call are unreachable. Example    * usage:    *    *<pre>    * T doSomething() {    *   try {    *     return someMethodThatCouldThrowAnything();    *   } catch (IKnowWhatToDoWithThisException e) {    *     return handle(e);    *   } catch (Throwable t) {    *     throw Throwables.propagate(t);    *   }    * }    *</pre>    *    * @param throwable the Throwable to propagate    * @return nothing will ever be returned; this return type is only for your convenience, as    *     illustrated in the example above    * @deprecated Use {@code throw e} or {@code throw new RuntimeException(e)} directly, or use a    *     combination of {@link #throwIfUnchecked} and {@code throw new RuntimeException(e)}. For    *     background on the deprecation, read<a href="https://goo.gl/Ivn2kc">Why we deprecated    *     {@code Throwables.propagate}</a>.    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|GwtIncompatible
annotation|@
name|Deprecated
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
name|throwIfUnchecked
argument_list|(
name|throwable
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
comment|/**    * Returns the innermost cause of {@code throwable}. The first throwable in a chain provides    * context from when the error or exception was initially detected. Example usage:    *    *<pre>    * assertEquals("Unable to assign a customer id", Throwables.getRootCause(e).getMessage());    *</pre>    *    * @throws IllegalArgumentException if there is a loop in the causal chain    */
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
comment|// Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
comment|// the slower pointer, then there's a loop.
name|Throwable
name|slowPointer
init|=
name|throwable
decl_stmt|;
name|boolean
name|advanceSlowPointer
init|=
literal|false
decl_stmt|;
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
if|if
condition|(
name|throwable
operator|==
name|slowPointer
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Loop in causal chain detected."
argument_list|,
name|throwable
argument_list|)
throw|;
block|}
if|if
condition|(
name|advanceSlowPointer
condition|)
block|{
name|slowPointer
operator|=
name|slowPointer
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
name|advanceSlowPointer
operator|=
operator|!
name|advanceSlowPointer
expr_stmt|;
comment|// only advance every other iteration
block|}
return|return
name|throwable
return|;
block|}
comment|/**    * Gets a {@code Throwable} cause chain as a list. The first entry in the list will be {@code    * throwable} followed by its cause hierarchy. Note that this is a snapshot of the cause chain and    * will not reflect any subsequent changes to the cause chain.    *    *<p>Here's an example of how it can be used to find specific types of exceptions in the cause    * chain:    *    *<pre>    * Iterables.filter(Throwables.getCausalChain(e), IOException.class));    *</pre>    *    * @param throwable the non-null {@code Throwable} to extract causes from    * @return an unmodifiable list containing the cause chain starting with {@code throwable}    * @throws IllegalArgumentException if there is a loop in the causal chain    */
annotation|@
name|Beta
comment|// TODO(kevinb): decide best return type
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
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|causes
operator|.
name|add
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
comment|// Keep a second pointer that slowly walks the causal chain. If the fast pointer ever catches
comment|// the slower pointer, then there's a loop.
name|Throwable
name|slowPointer
init|=
name|throwable
decl_stmt|;
name|boolean
name|advanceSlowPointer
init|=
literal|false
decl_stmt|;
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
name|causes
operator|.
name|add
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
if|if
condition|(
name|throwable
operator|==
name|slowPointer
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Loop in causal chain detected."
argument_list|,
name|throwable
argument_list|)
throw|;
block|}
if|if
condition|(
name|advanceSlowPointer
condition|)
block|{
name|slowPointer
operator|=
name|slowPointer
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
name|advanceSlowPointer
operator|=
operator|!
name|advanceSlowPointer
expr_stmt|;
comment|// only advance every other iteration
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
comment|/**    * Returns {@code throwable}'s cause, cast to {@code expectedCauseType}.    *    *<p>Prefer this method instead of manually casting an exception's cause. For example, {@code    * (IOException) e.getCause()} throws a {@link ClassCastException} that discards the original    * exception {@code e} if the cause is not an {@link IOException}, but {@code    * Throwables.getCauseAs(e, IOException.class)} keeps {@code e} as the {@link    * ClassCastException}'s cause.    *    * @throws ClassCastException if the cause cannot be cast to the expected type. The {@code    *     ClassCastException}'s cause is {@code throwable}.    * @since 22.0    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// Class.cast(Object)
DECL|method|getCauseAs ( Throwable throwable, Class<X> expectedCauseType)
specifier|public
specifier|static
parameter_list|<
name|X
extends|extends
name|Throwable
parameter_list|>
name|X
name|getCauseAs
parameter_list|(
name|Throwable
name|throwable
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|expectedCauseType
parameter_list|)
block|{
try|try
block|{
return|return
name|expectedCauseType
operator|.
name|cast
argument_list|(
name|throwable
operator|.
name|getCause
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
name|e
operator|.
name|initCause
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
comment|/**    * Returns a string containing the result of {@link Throwable#toString() toString()}, followed by    * the full, recursive stack trace of {@code throwable}. Note that you probably should not be    * parsing the resulting string; if you need programmatic access to the stack frames, you can call    * {@link Throwable#getStackTrace()}.    */
annotation|@
name|GwtIncompatible
comment|// java.io.PrintWriter, java.io.StringWriter
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
comment|/**    * Returns the stack trace of {@code throwable}, possibly providing slower iteration over the full    * trace but faster iteration over parts of the trace. Here, "slower" and "faster" are defined in    * comparison to the normal way to access the stack trace, {@link Throwable#getStackTrace()    * throwable.getStackTrace()}. Note, however, that this method's special implementation is not    * available for all platforms and configurations. If that implementation is unavailable, this    * method falls back to {@code getStackTrace}. Callers that require the special implementation can    * check its availability with {@link #lazyStackTraceIsLazy()}.    *    *<p>The expected (but not guaranteed) performance of the special implementation differs from    * {@code getStackTrace} in one main way: The {@code lazyStackTrace} call itself returns quickly    * by delaying the per-stack-frame work until each element is accessed. Roughly speaking:    *    *<ul>    *<li>{@code getStackTrace} takes {@code stackSize} time to return but then negligible time to    *       retrieve each element of the returned list.    *<li>{@code lazyStackTrace} takes negligible time to return but then {@code 1/stackSize} time    *       to retrieve each element of the returned list (probably slightly more than {@code    *       1/stackSize}).    *</ul>    *    *<p>Note: The special implementation does not respect calls to {@link Throwable#setStackTrace    * throwable.setStackTrace}. Instead, it always reflects the original stack trace from the    * exception's creation.    *    * @since 19.0    */
comment|// TODO(cpovirk): Say something about the possibility that List access could fail at runtime?
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// lazyStackTraceIsLazy, jlaStackTrace
comment|// TODO(cpovirk): Consider making this available under GWT (slow implementation only).
DECL|method|lazyStackTrace (Throwable throwable)
specifier|public
specifier|static
name|List
argument_list|<
name|StackTraceElement
argument_list|>
name|lazyStackTrace
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|lazyStackTraceIsLazy
argument_list|()
condition|?
name|jlaStackTrace
argument_list|(
name|throwable
argument_list|)
else|:
name|unmodifiableList
argument_list|(
name|asList
argument_list|(
name|throwable
operator|.
name|getStackTrace
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns whether {@link #lazyStackTrace} will use the special implementation described in its    * documentation.    *    * @since 19.0    */
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
comment|// getStackTraceElementMethod
DECL|method|lazyStackTraceIsLazy ()
specifier|public
specifier|static
name|boolean
name|lazyStackTraceIsLazy
parameter_list|()
block|{
return|return
name|getStackTraceElementMethod
operator|!=
literal|null
operator|&&
name|getStackTraceDepthMethod
operator|!=
literal|null
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// invokeAccessibleNonThrowingMethod
DECL|method|jlaStackTrace (final Throwable t)
specifier|private
specifier|static
name|List
argument_list|<
name|StackTraceElement
argument_list|>
name|jlaStackTrace
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
comment|/*      * TODO(cpovirk): Consider optimizing iterator() to catch IOOBE instead of doing bounds checks.      *      * TODO(cpovirk): Consider the UnsignedBytes pattern if it performs faster and doesn't cause      * AOSP grief.      */
return|return
operator|new
name|AbstractList
argument_list|<
name|StackTraceElement
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|StackTraceElement
name|get
parameter_list|(
name|int
name|n
parameter_list|)
block|{
return|return
operator|(
name|StackTraceElement
operator|)
name|invokeAccessibleNonThrowingMethod
argument_list|(
name|getStackTraceElementMethod
argument_list|,
name|jla
argument_list|,
name|t
argument_list|,
name|n
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|invokeAccessibleNonThrowingMethod
argument_list|(
name|getStackTraceDepthMethod
argument_list|,
name|jla
argument_list|,
name|t
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
DECL|method|invokeAccessibleNonThrowingMethod ( Method method, Object receiver, Object... params)
specifier|private
specifier|static
name|Object
name|invokeAccessibleNonThrowingMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
name|receiver
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
try|try
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|receiver
argument_list|,
name|params
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
name|propagate
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/** JavaLangAccess class name to load using reflection */
annotation|@
name|GwtIncompatible
comment|// not used by GWT emulation
DECL|field|JAVA_LANG_ACCESS_CLASSNAME
specifier|private
specifier|static
specifier|final
name|String
name|JAVA_LANG_ACCESS_CLASSNAME
init|=
literal|"sun.misc.JavaLangAccess"
decl_stmt|;
comment|/** SharedSecrets class name to load using reflection */
annotation|@
name|GwtIncompatible
comment|// not used by GWT emulation
annotation|@
name|VisibleForTesting
DECL|field|SHARED_SECRETS_CLASSNAME
specifier|static
specifier|final
name|String
name|SHARED_SECRETS_CLASSNAME
init|=
literal|"sun.misc.SharedSecrets"
decl_stmt|;
comment|/** Access to some fancy internal JVM internals. */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|field|jla
specifier|private
specifier|static
specifier|final
name|Object
name|jla
init|=
name|getJLA
argument_list|()
decl_stmt|;
comment|/**    * The "getStackTraceElementMethod" method, only available on some JDKs so we use reflection to    * find it when available. When this is null, use the slow way.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|field|getStackTraceElementMethod
specifier|private
specifier|static
specifier|final
name|Method
name|getStackTraceElementMethod
init|=
operator|(
name|jla
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|getGetMethod
argument_list|()
decl_stmt|;
comment|/**    * The "getStackTraceDepth" method, only available on some JDKs so we use reflection to find it    * when available. When this is null, use the slow way.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|field|getStackTraceDepthMethod
specifier|private
specifier|static
specifier|final
name|Method
name|getStackTraceDepthMethod
init|=
operator|(
name|jla
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|getSizeMethod
argument_list|()
decl_stmt|;
comment|/**    * Returns the JavaLangAccess class that is present in all Sun JDKs. It is not allowed in    * AppEngine, and not present in non-Sun JDKs.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|method|getJLA ()
specifier|private
specifier|static
name|Object
name|getJLA
parameter_list|()
block|{
try|try
block|{
comment|/*        * We load sun.misc.* classes using reflection since Android doesn't support these classes and        * would result in compilation failure if we directly refer to these classes.        */
name|Class
argument_list|<
name|?
argument_list|>
name|sharedSecrets
init|=
name|Class
operator|.
name|forName
argument_list|(
name|SHARED_SECRETS_CLASSNAME
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Method
name|langAccess
init|=
name|sharedSecrets
operator|.
name|getMethod
argument_list|(
literal|"getJavaLangAccess"
argument_list|)
decl_stmt|;
return|return
name|langAccess
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ThreadDeath
name|death
parameter_list|)
block|{
throw|throw
name|death
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|/*        * This is not one of AppEngine's allowed classes, so even in Sun JDKs, this can fail with        * a NoClassDefFoundError. Other apps might deny access to sun.misc packages.        */
return|return
literal|null
return|;
block|}
block|}
comment|/**    * Returns the Method that can be used to resolve an individual StackTraceElement, or null if that    * method cannot be found (it is only to be found in fairly recent JDKs).    */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|method|getGetMethod ()
specifier|private
specifier|static
name|Method
name|getGetMethod
parameter_list|()
block|{
return|return
name|getJlaMethod
argument_list|(
literal|"getStackTraceElement"
argument_list|,
name|Throwable
operator|.
name|class
argument_list|,
name|int
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**    * Returns the Method that can be used to return the size of a stack, or null if that method    * cannot be found (it is only to be found in fairly recent JDKs). Tries to test method {@link    * sun.misc.JavaLangAccess#getStackTraceDepth(Throwable)} getStackTraceDepth} prior to return it    * (might fail some JDKs).    *    *<p>See<a href="https://github.com/google/guava/issues/2887">Throwables#lazyStackTrace throws    * UnsupportedOperationException</a>.    */
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|method|getSizeMethod ()
specifier|private
specifier|static
name|Method
name|getSizeMethod
parameter_list|()
block|{
try|try
block|{
name|Method
name|getStackTraceDepth
init|=
name|getJlaMethod
argument_list|(
literal|"getStackTraceDepth"
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|getStackTraceDepth
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|getStackTraceDepth
operator|.
name|invoke
argument_list|(
name|getJLA
argument_list|()
argument_list|,
operator|new
name|Throwable
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getStackTraceDepth
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// java.lang.reflect
annotation|@
name|NullableDecl
DECL|method|getJlaMethod (String name, Class<?>... parameterTypes)
specifier|private
specifier|static
name|Method
name|getJlaMethod
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|parameterTypes
parameter_list|)
throws|throws
name|ThreadDeath
block|{
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|JAVA_LANG_ACCESS_CLASSNAME
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
operator|.
name|getMethod
argument_list|(
name|name
argument_list|,
name|parameterTypes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ThreadDeath
name|death
parameter_list|)
block|{
throw|throw
name|death
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|/*        * Either the JavaLangAccess class itself is not found, or the method is not supported on the        * JVM.        */
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

