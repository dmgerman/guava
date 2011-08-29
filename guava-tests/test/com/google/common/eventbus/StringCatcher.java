begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.eventbus
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
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
name|collect
operator|.
name|Lists
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  * A simple EventHandler mock that records Strings.  *  * For testing fun, also includes a landmine method that EventBus tests are  * required<em>not</em> to call ({@link #methodWithoutAnnotation(String)}).  *  * @author Cliff Biffle  */
end_comment

begin_class
DECL|class|StringCatcher
specifier|public
class|class
name|StringCatcher
block|{
DECL|field|events
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Subscribe
DECL|method|hereHaveAString (String string)
specifier|public
name|void
name|hereHaveAString
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
DECL|method|methodWithoutAnnotation (String string)
specifier|public
name|void
name|methodWithoutAnnotation
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Event bus must not call methods without @Subscribe!"
argument_list|)
expr_stmt|;
block|}
DECL|method|getEvents ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getEvents
parameter_list|()
block|{
return|return
name|events
return|;
block|}
block|}
end_class

end_unit

