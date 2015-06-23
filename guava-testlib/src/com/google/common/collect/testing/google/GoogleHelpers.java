begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
operator|.
name|google
package|;
end_package

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|fail
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
name|annotations
operator|.
name|GwtCompatible
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
name|Multimap
import|;
end_import

begin_comment
comment|/**  * Helper methods/assertions for use with {@code com.google.common.collect} types.  *  * @author Colin Decker  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|GoogleHelpers
specifier|final
class|class
name|GoogleHelpers
block|{
DECL|method|GoogleHelpers ()
specifier|private
name|GoogleHelpers
parameter_list|()
block|{}
DECL|method|assertEmpty (Multimap<?, ?> multimap)
specifier|static
name|void
name|assertEmpty
parameter_list|(
name|Multimap
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|multimap
parameter_list|)
block|{
if|if
condition|(
operator|!
name|multimap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"Not true that "
operator|+
name|multimap
operator|+
literal|" is empty"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

