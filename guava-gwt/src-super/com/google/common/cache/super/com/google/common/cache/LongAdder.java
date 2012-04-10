begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
package|;
end_package

begin_comment
comment|/**  * GWT emulated version of LongAdder.  *  * @author fry@google.com (Charles Fry)  */
end_comment

begin_class
DECL|class|LongAdder
class|class
name|LongAdder
block|{
DECL|field|value
specifier|private
name|long
name|value
decl_stmt|;
DECL|method|increment ()
specifier|public
name|void
name|increment
parameter_list|()
block|{
name|value
operator|++
expr_stmt|;
block|}
DECL|method|add (long x)
specifier|public
name|void
name|add
parameter_list|(
name|long
name|x
parameter_list|)
block|{
name|value
operator|+=
name|x
expr_stmt|;
block|}
DECL|method|sum ()
specifier|public
name|long
name|sum
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

