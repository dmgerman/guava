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
name|CheckForNull
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
comment|/**  * Determines an output value based on an input value; a pre-Java-8 version of {@link  * java.util.function.Function java.util.function.Function}.  *  *<p>The {@link Functions} class provides common functions and related utilities.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/FunctionalExplained">the use of {@code Function}</a>.  *  *<h3>For Java 8+ users</h3>  *  *<p>This interface is now a legacy type. Use {@code java.util.function.Function} (or the  * appropriate primitive specialization such as {@code ToIntFunction}) instead whenever possible.  * Otherwise, at least reduce<i>explicit</i> dependencies on this type by using lambda expressions  * or method references instead of classes, leaving your code easier to migrate in the future.  *  *<p>To use an existing function (say, named {@code function}) in a context where the<i>other  * type</i> of function is expected, use the method reference {@code function::apply}. A future  * version of {@code com.google.common.base.Function} will be made to<i>extend</i> {@code  * java.util.function.Function}, making conversion code necessary only in one direction. At that  * time, this interface will be officially discouraged.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|Function
specifier|public
expr|interface
name|Function
operator|<
name|F
expr|extends @
name|Nullable
name|Object
operator|,
name|T
expr|extends @
name|Nullable
name|Object
operator|>
block|{
comment|/**    * Returns the result of applying this function to {@code input}. This method is<i>generally    * expected</i>, but not absolutely required, to have the following properties:    *    *<ul>    *<li>Its execution does not cause any observable side effects.    *<li>The computation is<i>consistent with equals</i>; that is, {@link Objects#equal    *       Objects.equal}{@code (a, b)} implies that {@code Objects.equal(function.apply(a),    *       function.apply(b))}.    *</ul>    *    * @throws NullPointerException if {@code input} is null and this function does not accept null    *     arguments    */
block|@
name|CanIgnoreReturnValue
comment|// TODO(kevinb): remove this
expr|@
name|ParametricNullness
DECL|method|apply (@arametricNullness F input)
name|T
name|apply
argument_list|(
annotation|@
name|ParametricNullness
name|F
name|input
argument_list|)
block|;
comment|/**    *<i>May</i> return {@code true} if {@code object} is a {@code Function} that behaves identically    * to this function.    *    *<p><b>Warning: do not depend</b> on the behavior of this method.    *    *<p>Historically, {@code Function} instances in this library have implemented this method to    * recognize certain cases where distinct {@code Function} instances would in fact behave    * identically. However, as code migrates to {@code java.util.function}, that behavior will    * disappear. It is best not to depend on it.    */
block|@
name|Override
DECL|method|equals (@heckForNull Object object)
name|boolean
name|equals
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|object
argument_list|)
block|; }
end_expr_stmt

end_unit

