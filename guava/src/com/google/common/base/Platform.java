begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  * @author Jesse Wilson  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
comment|/** Calls {@link System#nanoTime()}. */
DECL|method|systemNanoTime ()
specifier|static
name|long
name|systemNanoTime
parameter_list|()
block|{
return|return
name|System
operator|.
name|nanoTime
argument_list|()
return|;
block|}
DECL|method|precomputeCharMatcher (CharMatcher matcher)
specifier|static
name|CharMatcher
name|precomputeCharMatcher
parameter_list|(
name|CharMatcher
name|matcher
parameter_list|)
block|{
return|return
name|matcher
operator|.
name|precomputedInternal
argument_list|()
return|;
block|}
block|}
end_class

end_unit

