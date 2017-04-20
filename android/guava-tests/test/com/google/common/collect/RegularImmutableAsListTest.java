begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link RegularImmutableAsList}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|RegularImmutableAsListTest
specifier|public
class|class
name|RegularImmutableAsListTest
extends|extends
name|TestCase
block|{
comment|/**    * RegularImmutableAsList should assume its input is null-free without checking, because it only    * gets invoked from other immutable collections.    */
DECL|method|testDoesntCheckForNull ()
specifier|public
name|void
name|testDoesntCheckForNull
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|set
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
operator|new
name|RegularImmutableAsList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|set
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|null
block|,
literal|null
block|,
literal|null
block|}
argument_list|)
expr_stmt|;
comment|// shouldn't throw!
block|}
block|}
end_class

end_unit

