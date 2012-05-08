begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests {@link ImmutableTable}  *  * @author Gregory Kick  */
end_comment

begin_class
DECL|class|AbstractImmutableTableTest
specifier|public
specifier|abstract
class|class
name|AbstractImmutableTableTest
extends|extends
name|TestCase
block|{
specifier|abstract
name|Iterable
argument_list|<
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|>
DECL|method|getTestInstances ()
name|getTestInstances
parameter_list|()
function_decl|;
DECL|method|testClear ()
specifier|public
specifier|final
name|void
name|testClear
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
try|try
block|{
name|testInstance
operator|.
name|clear
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// success
block|}
block|}
block|}
DECL|method|testPut ()
specifier|public
specifier|final
name|void
name|testPut
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
try|try
block|{
name|testInstance
operator|.
name|put
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|,
literal|"blah"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// success
block|}
block|}
block|}
DECL|method|testPutAll ()
specifier|public
specifier|final
name|void
name|testPutAll
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
try|try
block|{
name|testInstance
operator|.
name|putAll
argument_list|(
name|ImmutableTable
operator|.
name|of
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|,
literal|"blah"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// success
block|}
block|}
block|}
DECL|method|testRemove ()
specifier|public
specifier|final
name|void
name|testRemove
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
try|try
block|{
name|testInstance
operator|.
name|remove
argument_list|(
literal|'a'
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// success
block|}
block|}
block|}
DECL|method|testConsistentToString ()
specifier|public
specifier|final
name|void
name|testConsistentToString
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|testInstance
operator|.
name|rowMap
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|testInstance
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testConsistentHashCode ()
specifier|public
specifier|final
name|void
name|testConsistentHashCode
parameter_list|()
block|{
for|for
control|(
name|ImmutableTable
argument_list|<
name|Character
argument_list|,
name|Integer
argument_list|,
name|String
argument_list|>
name|testInstance
range|:
name|getTestInstances
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|testInstance
operator|.
name|cellSet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|,
name|testInstance
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

