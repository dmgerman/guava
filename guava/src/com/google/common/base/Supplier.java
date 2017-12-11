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

begin_comment
comment|/**  * Legacy version of {@link java.util.function.Supplier java.util.function.Supplier}. Semantically,  * this could be a factory, generator, builder, closure, or something else entirely. No guarantees  * are implied by this interface.  *  *<p>The {@link Suppliers} class provides common suppliers and related utilities.  *  *<p>As this interface extends {@code java.util.function.Supplier}, an instance of this type can be  * used as a {@code java.util.function.Supplier} directly. To use a {@code  * java.util.function.Supplier} in a context where a {@code com.google.common.base.Supplier} is  * needed, use {@code supplier::get}.  *  *<p>See the Guava User Guide article on<a  * href="https://github.com/google/guava/wiki/FunctionalExplained">the use of {@code Function}</a>.  *  * @author Harry Heymann  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
annotation|@
name|FunctionalInterface
DECL|interface|Supplier
specifier|public
interface|interface
name|Supplier
parameter_list|<
name|T
parameter_list|>
extends|extends
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
argument_list|<
name|T
argument_list|>
block|{
comment|/**    * Retrieves an instance of the appropriate type. The returned object may or may not be a new    * instance, depending on the implementation.    *    * @return an instance of the appropriate type    */
annotation|@
name|CanIgnoreReturnValue
annotation|@
name|Override
DECL|method|get ()
name|T
name|get
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

