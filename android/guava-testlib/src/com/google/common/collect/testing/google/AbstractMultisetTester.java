begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Multiset
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
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/**  * Base class for multiset collection tests.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
annotation|@
name|Ignore
comment|// Affects only Android test runner, which respects JUnit 4 annotations on JUnit 3 tests.
DECL|class|AbstractMultisetTester
specifier|public
class|class
name|AbstractMultisetTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|getMultiset ()
specifier|protected
specifier|final
name|Multiset
argument_list|<
name|E
argument_list|>
name|getMultiset
parameter_list|()
block|{
return|return
operator|(
name|Multiset
argument_list|<
name|E
argument_list|>
operator|)
name|collection
return|;
block|}
DECL|method|initThreeCopies ()
specifier|protected
name|void
name|initThreeCopies
parameter_list|()
block|{
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|e0
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|,
name|e0
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

