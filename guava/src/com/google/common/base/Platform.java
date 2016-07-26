begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|WeakReference
argument_list|<
name|?
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
argument_list|>
name|ref
init|=
name|Enums
operator|.
name|getEnumConstants
argument_list|(
name|enumClass
argument_list|)
operator|.
name|get
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
name|ref
operator|==
literal|null
condition|?
name|Optional
operator|.
expr|<
name|T
operator|>
name|absent
argument_list|()
else|:
name|Optional
operator|.
name|of
argument_list|(
name|enumClass
operator|.
name|cast
argument_list|(
name|ref
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
return|;
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
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
literal|"%.4g"
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

