begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A non-empty tester for {@link java.util.Iterator}.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ExampleIteratorTester
specifier|public
specifier|final
class|class
name|ExampleIteratorTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractTester
argument_list|<
name|TestIteratorGenerator
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
DECL|method|testSomethingAboutIterators ()
specifier|public
name|void
name|testSomethingAboutIterators
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

