begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * Abstract interface for objects that can concurrently add longs.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|LongAddable
interface|interface
name|LongAddable
block|{
DECL|method|increment ()
name|void
name|increment
parameter_list|()
function_decl|;
DECL|method|add (long x)
name|void
name|add
parameter_list|(
name|long
name|x
parameter_list|)
function_decl|;
DECL|method|sum ()
name|long
name|sum
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

