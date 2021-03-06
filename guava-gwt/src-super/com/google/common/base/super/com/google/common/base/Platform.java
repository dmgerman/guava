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
import|import static
name|jsinterop
operator|.
name|annotations
operator|.
name|JsPackage
operator|.
name|GLOBAL
import|;
end_import

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

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsMethod
import|;
end_import

begin_import
import|import
name|jsinterop
operator|.
name|annotations
operator|.
name|JsType
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
comment|/** @author Jesse Wilson */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"GoodTime"
argument_list|)
comment|// reading system time without TimeSource
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
DECL|method|formatCompact4Digits (double value)
specifier|static
name|String
name|formatCompact4Digits
parameter_list|(
name|double
name|value
parameter_list|)
block|{
return|return
literal|""
operator|+
operator|(
call|(
name|Number
call|)
argument_list|(
name|Object
argument_list|)
name|value
operator|)
operator|.
name|toPrecision
argument_list|(
literal|4
argument_list|)
return|;
block|}
annotation|@
name|JsMethod
DECL|method|stringIsNullOrEmpty (@ullable String string)
specifier|static
specifier|native
name|boolean
name|stringIsNullOrEmpty
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
comment|/*-{     return !string;   }-*/
function_decl|;
annotation|@
name|JsMethod
DECL|method|nullToEmpty (@ullable String string)
specifier|static
specifier|native
name|String
name|nullToEmpty
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
comment|/*-{     return string || "";   }-*/
function_decl|;
annotation|@
name|JsMethod
DECL|method|emptyToNull (@ullable String string)
specifier|static
specifier|native
name|String
name|emptyToNull
parameter_list|(
annotation|@
name|Nullable
name|String
name|string
parameter_list|)
comment|/*-{     return string || null;   }-*/
function_decl|;
annotation|@
name|JsType
argument_list|(
name|isNative
operator|=
literal|true
argument_list|,
name|name
operator|=
literal|"number"
argument_list|,
name|namespace
operator|=
name|GLOBAL
argument_list|)
DECL|interface|Number
specifier|private
interface|interface
name|Number
block|{
DECL|method|toPrecision (int precision)
name|double
name|toPrecision
parameter_list|(
name|int
name|precision
parameter_list|)
function_decl|;
block|}
DECL|method|compilePattern (String pattern)
specifier|static
name|CommonPattern
name|compilePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|patternCompilerIsPcreLike ()
specifier|static
name|boolean
name|patternCompilerIsPcreLike
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|/*    * We will eventually disable GWT-RPC on the server side, but we'll leave it nominally enabled on    * the client side. There's little practical difference: If it's disabled on the server, it won't    * work. It's just a matter of how quickly it fails. I'm not sure if failing on the client would    * be better or not, but it's harder: GWT's System.getProperty reads from a different property    * list than Java's, so anyone who needs to reenable GWT-RPC in an emergency would have to figure    * out how to set both properties. It's easier to have to set only one, and it might as well be    * the Java property, since Guava already reads another Java property.    */
DECL|method|checkGwtRpcEnabled ()
specifier|static
name|void
name|checkGwtRpcEnabled
parameter_list|()
block|{}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

