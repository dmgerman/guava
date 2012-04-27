begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Minimal GWT emulation of {@code com.google.common.collect.Platform}.  *  *<p><strong>This .java file should never be consumed by javac.</strong>  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|Platform
class|class
name|Platform
block|{
DECL|method|clone (T[] array)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|clone
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|GwtPlatform
operator|.
name|clone
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|newArray (T[] reference, int length)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|T
index|[]
name|reference
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|GwtPlatform
operator|.
name|newArray
argument_list|(
name|reference
argument_list|,
name|length
argument_list|)
return|;
block|}
DECL|method|tryWeakKeys (MapMaker mapMaker)
specifier|static
name|MapMaker
name|tryWeakKeys
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|)
block|{
return|return
name|mapMaker
return|;
block|}
block|}
end_class

end_unit

