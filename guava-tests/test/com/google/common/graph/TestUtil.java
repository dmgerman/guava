begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Iterators
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Utility methods used in various common.graph tests.  */
end_comment

begin_class
DECL|class|TestUtil
class|class
name|TestUtil
block|{
DECL|method|TestUtil ()
specifier|private
name|TestUtil
parameter_list|()
block|{}
comment|/**    * In some cases our graph implementations return custom collections that define their own size()    * and contains(). Verify that those methods are consistent with the elements of the iterator.    */
DECL|method|sanityCheckCollection (Collection<?> collection)
specifier|static
name|void
name|sanityCheckCollection
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|hasSize
argument_list|(
name|Iterators
operator|.
name|size
argument_list|(
name|collection
operator|.
name|iterator
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|element
range|:
name|collection
control|)
block|{
name|assertThat
argument_list|(
name|collection
argument_list|)
operator|.
name|contains
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

