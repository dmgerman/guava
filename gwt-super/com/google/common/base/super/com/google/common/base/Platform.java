begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
class|class
name|Platform
block|{
DECL|field|CHAR_BUFFER
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|CHAR_BUFFER
init|=
operator|new
name|char
index|[
literal|1024
index|]
decl_stmt|;
DECL|method|isInstance (Class<?> clazz, Object obj)
specifier|static
name|boolean
name|isInstance
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Object
name|obj
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Class.isInstance is not supported in GWT yet."
argument_list|)
throw|;
block|}
DECL|method|charBufferFromThreadLocal ()
specifier|static
name|char
index|[]
name|charBufferFromThreadLocal
parameter_list|()
block|{
comment|// ThreadLocal is not available to GWT, so we always reuse the same
comment|// instance.  It is always safe to return the same instance because
comment|// javascript is single-threaded, and only used by blocks that doesn't
comment|// involve async callbacks.
return|return
name|CHAR_BUFFER
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
block|}
end_class

end_unit

