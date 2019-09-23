begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
comment|/**  * Wraps an exception that occurred during a computation.  *  * @author Bob Lee  * @since 2.0  * @deprecated This exception is no longer thrown by {@code com.google.common}. Previously, it was  *     thrown by {@link MapMaker} computing maps. When support for computing maps was removed from  *     {@code MapMaker}, it was added to {@code CacheBuilder}, which throws {@code  *     ExecutionException}, {@code UncheckedExecutionException}, and {@code ExecutionError}. Any  *     code that is still catching {@code ComputationException} may need to be updated to catch some  *     of those types instead. (Note that this type, though deprecated, is not planned to be removed  *     from Guava.)  */
end_comment

begin_class
annotation|@
name|Deprecated
annotation|@
name|GwtCompatible
DECL|class|ComputationException
specifier|public
class|class
name|ComputationException
extends|extends
name|RuntimeException
block|{
comment|/** Creates a new instance with the given cause. */
DECL|method|ComputationException (@ullableDecl Throwable cause)
specifier|public
name|ComputationException
parameter_list|(
annotation|@
name|NullableDecl
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

