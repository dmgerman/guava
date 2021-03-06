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
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * The subset of the {@link java.util.regex.Pattern} API which is used by this package, and also  * shared with the {@code re2j} library. For internal use only. Please refer to the {@code Pattern}  * javadoc for details.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|CommonPattern
specifier|abstract
class|class
name|CommonPattern
block|{
DECL|method|matcher (CharSequence t)
specifier|public
specifier|abstract
name|CommonMatcher
name|matcher
parameter_list|(
name|CharSequence
name|t
parameter_list|)
function_decl|;
DECL|method|pattern ()
specifier|public
specifier|abstract
name|String
name|pattern
parameter_list|()
function_decl|;
DECL|method|flags ()
specifier|public
specifier|abstract
name|int
name|flags
parameter_list|()
function_decl|;
comment|// Re-declare this as abstract to force subclasses to override.
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|abstract
name|String
name|toString
parameter_list|()
function_decl|;
DECL|method|compile (String pattern)
specifier|public
specifier|static
name|CommonPattern
name|compile
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|compilePattern
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|isPcreLike ()
specifier|public
specifier|static
name|boolean
name|isPcreLike
parameter_list|()
block|{
return|return
name|Platform
operator|.
name|patternCompilerIsPcreLike
argument_list|()
return|;
block|}
block|}
end_class

end_unit

