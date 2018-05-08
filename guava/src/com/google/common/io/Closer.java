begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
name|common
operator|.
name|base
operator|.
name|Throwables
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
name|Closeable
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
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|Level
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
name|MonotonicNonNullDecl
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
comment|/**  * A {@link Closeable} that collects {@code Closeable} resources and closes them all when it is  * {@linkplain #close closed}. This is intended to approximately emulate the behavior of Java 7's<a  * href="http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html"  *>try-with-resources</a> statement in JDK6-compatible code. Running on Java 7, code using this  * should be approximately equivalent in behavior to the same code written with try-with-resources.  * Running on Java 6, exceptions that cannot be thrown must be logged rather than being added to the  * thrown exception as a suppressed exception.  *  *<p>This class is intended to be used in the following pattern:  *  *<pre>{@code  * Closer closer = Closer.create();  * try {  *   InputStream in = closer.register(openInputStream());  *   OutputStream out = closer.register(openOutputStream());  *   // do stuff  * } catch (Throwable e) {  *   // ensure that any checked exception types other than IOException that could be thrown are  *   // provided here, e.g. throw closer.rethrow(e, CheckedException.class);  *   throw closer.rethrow(e);  * } finally {  *   closer.close();  * }  * }</pre>  *  *<p>Note that this try-catch-finally block is not equivalent to a try-catch-finally block using  * try-with-resources. To get the equivalent of that, you must wrap the above code in<i>another</i>  * try block in order to catch any exception that may be thrown (including from the call to {@code  * close()}).  *  *<p>This pattern ensures the following:  *  *<ul>  *<li>Each {@code Closeable} resource that is successfully registered will be closed later.  *<li>If a {@code Throwable} is thrown in the try block, no exceptions that occur when attempting  *       to close resources will be thrown from the finally block. The throwable from the try block  *       will be thrown.  *<li>If no exceptions or errors were thrown in the try block, the<i>first</i> exception thrown  *       by an attempt to close a resource will be thrown.  *<li>Any exception caught when attempting to close a resource that is<i>not</i> thrown (because  *       another exception is already being thrown) is<i>suppressed</i>.  *</ul>  *  *<p>An exception that is suppressed is not thrown. The method of suppression used depends on the  * version of Java the code is running on:  *  *<ul>  *<li><b>Java 7+:</b> Exceptions are suppressed by adding them to the exception that<i>will</i>  *       be thrown using {@code Throwable.addSuppressed(Throwable)}.  *<li><b>Java 6:</b> Exceptions are suppressed by logging them instead.  *</ul>  *  * @author Colin Decker  * @since 14.0  */
end_comment

begin_comment
comment|// Coffee's for {@link Closer closers} only.
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtIncompatible
DECL|class|Closer
specifier|public
specifier|final
class|class
name|Closer
implements|implements
name|Closeable
block|{
comment|/** The suppressor implementation to use for the current Java version. */
DECL|field|SUPPRESSOR
specifier|private
specifier|static
specifier|final
name|Suppressor
name|SUPPRESSOR
init|=
name|SuppressingSuppressor
operator|.
name|isAvailable
argument_list|()
condition|?
name|SuppressingSuppressor
operator|.
name|INSTANCE
else|:
name|LoggingSuppressor
operator|.
name|INSTANCE
decl_stmt|;
comment|/** Creates a new {@link Closer}. */
DECL|method|create ()
specifier|public
specifier|static
name|Closer
name|create
parameter_list|()
block|{
return|return
operator|new
name|Closer
argument_list|(
name|SUPPRESSOR
argument_list|)
return|;
block|}
DECL|field|suppressor
annotation|@
name|VisibleForTesting
specifier|final
name|Suppressor
name|suppressor
decl_stmt|;
comment|// only need space for 2 elements in most cases, so try to use the smallest array possible
DECL|field|stack
specifier|private
specifier|final
name|Deque
argument_list|<
name|Closeable
argument_list|>
name|stack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
DECL|field|thrown
annotation|@
name|MonotonicNonNullDecl
specifier|private
name|Throwable
name|thrown
decl_stmt|;
annotation|@
name|VisibleForTesting
DECL|method|Closer (Suppressor suppressor)
name|Closer
parameter_list|(
name|Suppressor
name|suppressor
parameter_list|)
block|{
name|this
operator|.
name|suppressor
operator|=
name|checkNotNull
argument_list|(
name|suppressor
argument_list|)
expr_stmt|;
comment|// checkNotNull to satisfy null tests
block|}
comment|/**    * Registers the given {@code closeable} to be closed when this {@code Closer} is {@linkplain    * #close closed}.    *    * @return the given {@code closeable}    */
comment|// close. this word no longer has any meaning to me.
annotation|@
name|CanIgnoreReturnValue
DECL|method|register (@ullable C closeable)
specifier|public
parameter_list|<
name|C
extends|extends
name|Closeable
parameter_list|>
name|C
name|register
parameter_list|(
annotation|@
name|Nullable
name|C
name|closeable
parameter_list|)
block|{
if|if
condition|(
name|closeable
operator|!=
literal|null
condition|)
block|{
name|stack
operator|.
name|addFirst
argument_list|(
name|closeable
argument_list|)
expr_stmt|;
block|}
return|return
name|closeable
return|;
block|}
comment|/**    * Stores the given throwable and rethrows it. It will be rethrown as is if it is an {@code    * IOException}, {@code RuntimeException} or {@code Error}. Otherwise, it will be rethrown wrapped    * in a {@code RuntimeException}.<b>Note:</b> Be sure to declare all of the checked exception    * types your try block can throw when calling an overload of this method so as to avoid losing    * the original exception type.    *    *<p>This method always throws, and as such should be called as {@code throw closer.rethrow(e);}    * to ensure the compiler knows that it will throw.    *    * @return this method does not return; it always throws    * @throws IOException when the given throwable is an IOException    */
DECL|method|rethrow (Throwable e)
specifier|public
name|RuntimeException
name|rethrow
parameter_list|(
name|Throwable
name|e
parameter_list|)
throws|throws
name|IOException
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|thrown
operator|=
name|e
expr_stmt|;
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|/**    * Stores the given throwable and rethrows it. It will be rethrown as is if it is an {@code    * IOException}, {@code RuntimeException}, {@code Error} or a checked exception of the given type.    * Otherwise, it will be rethrown wrapped in a {@code RuntimeException}.<b>Note:</b> Be sure to    * declare all of the checked exception types your try block can throw when calling an overload of    * this method so as to avoid losing the original exception type.    *    *<p>This method always throws, and as such should be called as {@code throw closer.rethrow(e,    * ...);} to ensure the compiler knows that it will throw.    *    * @return this method does not return; it always throws    * @throws IOException when the given throwable is an IOException    * @throws X when the given throwable is of the declared type X    */
DECL|method|rethrow (Throwable e, Class<X> declaredType)
specifier|public
parameter_list|<
name|X
extends|extends
name|Exception
parameter_list|>
name|RuntimeException
name|rethrow
parameter_list|(
name|Throwable
name|e
parameter_list|,
name|Class
argument_list|<
name|X
argument_list|>
name|declaredType
parameter_list|)
throws|throws
name|IOException
throws|,
name|X
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|thrown
operator|=
name|e
expr_stmt|;
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
argument_list|,
name|declaredType
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|/**    * Stores the given throwable and rethrows it. It will be rethrown as is if it is an {@code    * IOException}, {@code RuntimeException}, {@code Error} or a checked exception of either of the    * given types. Otherwise, it will be rethrown wrapped in a {@code RuntimeException}.<b>Note:</b>    * Be sure to declare all of the checked exception types your try block can throw when calling an    * overload of this method so as to avoid losing the original exception type.    *    *<p>This method always throws, and as such should be called as {@code throw closer.rethrow(e,    * ...);} to ensure the compiler knows that it will throw.    *    * @return this method does not return; it always throws    * @throws IOException when the given throwable is an IOException    * @throws X1 when the given throwable is of the declared type X1    * @throws X2 when the given throwable is of the declared type X2    */
DECL|method|rethrow ( Throwable e, Class<X1> declaredType1, Class<X2> declaredType2)
specifier|public
parameter_list|<
name|X1
extends|extends
name|Exception
parameter_list|,
name|X2
extends|extends
name|Exception
parameter_list|>
name|RuntimeException
name|rethrow
parameter_list|(
name|Throwable
name|e
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
name|IOException
throws|,
name|X1
throws|,
name|X2
block|{
name|checkNotNull
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|thrown
operator|=
name|e
expr_stmt|;
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|e
argument_list|,
name|declaredType1
argument_list|,
name|declaredType2
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|/**    * Closes all {@code Closeable} instances that have been added to this {@code Closer}. If an    * exception was thrown in the try block and passed to one of the {@code exceptionThrown} methods,    * any exceptions thrown when attempting to close a closeable will be suppressed. Otherwise, the    *<i>first</i> exception to be thrown from an attempt to close a closeable will be thrown and any    * additional exceptions that are thrown after that will be suppressed.    */
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|Throwable
name|throwable
init|=
name|thrown
decl_stmt|;
comment|// close closeables in LIFO order
while|while
condition|(
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Closeable
name|closeable
init|=
name|stack
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
try|try
block|{
name|closeable
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|throwable
operator|=
name|e
expr_stmt|;
block|}
else|else
block|{
name|suppressor
operator|.
name|suppress
argument_list|(
name|closeable
argument_list|,
name|throwable
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|thrown
operator|==
literal|null
operator|&&
name|throwable
operator|!=
literal|null
condition|)
block|{
name|Throwables
operator|.
name|propagateIfPossible
argument_list|(
name|throwable
argument_list|,
name|IOException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|AssertionError
argument_list|(
name|throwable
argument_list|)
throw|;
comment|// not possible
block|}
block|}
comment|/** Suppression strategy interface. */
annotation|@
name|VisibleForTesting
DECL|interface|Suppressor
interface|interface
name|Suppressor
block|{
comment|/**      * Suppresses the given exception ({@code suppressed}) which was thrown when attempting to close      * the given closeable. {@code thrown} is the exception that is actually being thrown from the      * method. Implementations of this method should not throw under any circumstances.      */
DECL|method|suppress (Closeable closeable, Throwable thrown, Throwable suppressed)
name|void
name|suppress
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|Throwable
name|thrown
parameter_list|,
name|Throwable
name|suppressed
parameter_list|)
function_decl|;
block|}
comment|/** Suppresses exceptions by logging them. */
annotation|@
name|VisibleForTesting
DECL|class|LoggingSuppressor
specifier|static
specifier|final
class|class
name|LoggingSuppressor
implements|implements
name|Suppressor
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|LoggingSuppressor
name|INSTANCE
init|=
operator|new
name|LoggingSuppressor
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|suppress (Closeable closeable, Throwable thrown, Throwable suppressed)
specifier|public
name|void
name|suppress
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|Throwable
name|thrown
parameter_list|,
name|Throwable
name|suppressed
parameter_list|)
block|{
comment|// log to the same place as Closeables
name|Closeables
operator|.
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Suppressing exception thrown when closing "
operator|+
name|closeable
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Suppresses exceptions by adding them to the exception that will be thrown using JDK7's    * addSuppressed(Throwable) mechanism.    */
annotation|@
name|VisibleForTesting
DECL|class|SuppressingSuppressor
specifier|static
specifier|final
class|class
name|SuppressingSuppressor
implements|implements
name|Suppressor
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|SuppressingSuppressor
name|INSTANCE
init|=
operator|new
name|SuppressingSuppressor
argument_list|()
decl_stmt|;
DECL|method|isAvailable ()
specifier|static
name|boolean
name|isAvailable
parameter_list|()
block|{
return|return
name|addSuppressed
operator|!=
literal|null
return|;
block|}
DECL|field|addSuppressed
specifier|static
specifier|final
name|Method
name|addSuppressed
init|=
name|getAddSuppressed
argument_list|()
decl_stmt|;
DECL|method|getAddSuppressed ()
specifier|private
specifier|static
name|Method
name|getAddSuppressed
parameter_list|()
block|{
try|try
block|{
return|return
name|Throwable
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"addSuppressed"
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|suppress (Closeable closeable, Throwable thrown, Throwable suppressed)
specifier|public
name|void
name|suppress
parameter_list|(
name|Closeable
name|closeable
parameter_list|,
name|Throwable
name|thrown
parameter_list|,
name|Throwable
name|suppressed
parameter_list|)
block|{
comment|// ensure no exceptions from addSuppressed
if|if
condition|(
name|thrown
operator|==
name|suppressed
condition|)
block|{
return|return;
block|}
try|try
block|{
name|addSuppressed
operator|.
name|invoke
argument_list|(
name|thrown
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// if, somehow, IllegalAccessException or another exception is thrown, fall back to logging
name|LoggingSuppressor
operator|.
name|INSTANCE
operator|.
name|suppress
argument_list|(
name|closeable
argument_list|,
name|thrown
argument_list|,
name|suppressed
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

