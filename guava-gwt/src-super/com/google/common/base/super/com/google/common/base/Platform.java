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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * @author Jesse Wilson  */
end_comment

begin_class
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|method|precomputeCharMatcher (CharMatcher matcher)
specifier|static
name|CharMatcher
name|precomputeCharMatcher
parameter_list|(
name|CharMatcher
name|matcher
parameter_list|)
block|{
comment|// CharMatcher.precomputed() produces CharMatchers that are maybe a little
comment|// faster (and that's debatable), but definitely more memory-hungry. We're
comment|// choosing to turn .precomputed() into a no-op in GWT, because it doesn't
comment|// seem to be a worthwhile tradeoff in a browser.
return|return
name|matcher
return|;
block|}
DECL|method|systemNanoTime ()
specifier|static
name|long
name|systemNanoTime
parameter_list|()
block|{
comment|// System.nanoTime() is not available in GWT, so we get milliseconds
comment|// and convert to nanos.
return|return
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toNanos
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getEnumIfPresent (Class<T> enumClass, String value)
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getEnumIfPresent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|,
name|String
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|value
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|iae
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

