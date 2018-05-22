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
comment|/**  * Legacy version of {@link java.util.function.Function java.util.function.Function}.  *  *<p>The {@link Functions} class provides common functions and related utilities.  *  *<p>As this interface extends {@code java.util.function.Function}, an instance of this type can be  * used as a {@code java.util.function.Function} directly. To use a {@code  * java.util.function.Function} in a context where a {@code com.google.common.base.Function} is  * needed, use {@code function::apply}.  *  *<p>This interface is now a legacy type. Use {@code java.util.function.Function} (or the  * appropriate primitive specialization such as {@code ToIntFunction}) instead whenever possible.  * Otherwise, at least reduce<i>explicit</i> dependencies on this type by using lambda expressions  * or method references instead of classes, leaving your code easier to migrate in the future.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/FunctionalExplained">the use of {@code Function}</a>.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
annotation|@
name|FunctionalInterface
DECL|interface|Function
specifier|public
interface|interface
name|Function
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
block|{
annotation|@
name|Override
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(kevinb): remove this
annotation|@
name|Nullable
DECL|method|apply (@ullable F input)
name|T
name|apply
parameter_list|(
annotation|@
name|Nullable
name|F
name|input
parameter_list|)
function_decl|;
comment|/**    *<i>May</i> return {@code true} if {@code object} is a {@code Function} that behaves identically    * to this function.    *    *<p><b>Warning: do not depend</b> on the behavior of this method.    *    *<p>Historically, {@code Function} instances in this library have implemented this method to    * recognize certain cases where distinct {@code Function} instances would in fact behave    * identically. However, as code migrates to {@code java.util.function}, that behavior will    * disappear. It is best not to depend on it.    */
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

