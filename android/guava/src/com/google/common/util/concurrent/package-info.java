begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_comment
comment|/**  * Concurrency utilities.  *  *<p>Commonly used types include {@link com.google.common.util.concurrent.ListenableFuture} and  * {@link com.google.common.util.concurrent.Service}.  *  *<p>Commonly used utilities include {@link com.google.common.util.concurrent.Futures},  * {@link com.google.common.util.concurrent.MoreExecutors}, and  * {@link com.google.common.util.concurrent.ThreadFactoryBuilder}.  *  *<p>This package is a part of the open-source<a href="http://github.com/google/guava">Guava</a>  * library.  */
end_comment

begin_annotation
annotation|@
name|CheckReturnValue
end_annotation

begin_annotation
annotation|@
name|ParametersAreNonnullByDefault
end_annotation

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|ParametersAreNonnullByDefault
import|;
end_import

end_unit

