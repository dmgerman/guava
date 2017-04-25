begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
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
name|testers
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
name|AbstractCollectionTester
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
name|testing
operator|.
name|Helpers
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Base class for list testers.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractListTester
specifier|public
class|class
name|AbstractListTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
comment|/*    * Previously we had a field named list that was initialized to the value of    * collection in setUp(), but that caused problems when a tester changed the    * value of list or collection but not both.    */
DECL|method|getList ()
specifier|protected
specifier|final
name|List
argument_list|<
name|E
argument_list|>
name|getList
parameter_list|()
block|{
return|return
operator|(
name|List
argument_list|<
name|E
argument_list|>
operator|)
name|collection
return|;
block|}
comment|/**    * {@inheritDoc}    *<p>    * The {@code AbstractListTester} implementation overrides    * {@link AbstractCollectionTester#expectContents(Collection)} to verify that    * the order of the elements in the list under test matches what is expected.    */
annotation|@
name|Override
DECL|method|expectContents (Collection<E> expectedCollection)
specifier|protected
name|void
name|expectContents
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|expectedCollection
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|expectedList
init|=
name|Helpers
operator|.
name|copyToList
argument_list|(
name|expectedCollection
argument_list|)
decl_stmt|;
comment|// Avoid expectEquals() here to delay reason manufacture until necessary.
if|if
condition|(
name|getList
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
name|expectedList
operator|.
name|size
argument_list|()
condition|)
block|{
name|fail
argument_list|(
literal|"size mismatch: "
operator|+
name|reportContext
argument_list|(
name|expectedList
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|E
name|expected
init|=
name|expectedList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|E
name|actual
init|=
name|getList
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|expected
operator|!=
name|actual
operator|&&
operator|(
name|expected
operator|==
literal|null
operator|||
operator|!
name|expected
operator|.
name|equals
argument_list|(
name|actual
argument_list|)
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"mismatch at index "
operator|+
name|i
operator|+
literal|": "
operator|+
name|reportContext
argument_list|(
name|expectedList
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Used to delay string formatting until actually required, as it    * otherwise shows up in the test execution profile when running an    * extremely large numbers of tests.    */
DECL|method|reportContext (List<E> expected)
specifier|private
name|String
name|reportContext
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|expected
parameter_list|)
block|{
return|return
name|Platform
operator|.
name|format
argument_list|(
literal|"expected collection %s; actual collection %s"
argument_list|,
name|expected
argument_list|,
name|this
operator|.
name|collection
argument_list|)
return|;
block|}
block|}
end_class

end_unit
