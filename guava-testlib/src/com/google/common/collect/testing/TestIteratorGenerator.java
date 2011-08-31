begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * Creates iterators to be tested.  *  * @param<E> the element type of the iterator.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_interface
DECL|interface|TestIteratorGenerator
specifier|public
interface|interface
name|TestIteratorGenerator
parameter_list|<
name|E
parameter_list|>
block|{
DECL|method|get ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|get
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

