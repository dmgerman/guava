begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_comment
comment|/**  * Pluggable interface for compiling a regex pattern. By default this package uses the {@code  * java.util.regex} library, but an alternate implementation can be supplied using the {@link  * java.util.ServiceLoader} mechanism.  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|interface|PatternCompiler
interface|interface
name|PatternCompiler
block|{
comment|/**    * Compiles the given pattern.    *    * @throws IllegalArgumentException if the pattern is invalid    */
DECL|method|compile (String pattern)
name|CommonPattern
name|compile
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
comment|/**    * Returns {@code true} if the regex implementation behaves like Perl -- notably, by supporting    * possessive quantifiers but also being susceptible to catastrophic backtracking.    */
DECL|method|isPcreLike ()
name|boolean
name|isPcreLike
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

