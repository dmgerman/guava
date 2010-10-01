begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
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
name|Beta
import|;
end_import

begin_comment
comment|/**  * A simple assertion failure error used in testing to isolate Guava   * testing from JUnit or TestNG or any other framework.  It is a   * direct descendant of AssertionError.  *  * @author cgruber@google.com (Christian Edward Gruber)  * @since r08  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|AssertionFailedError
specifier|public
class|class
name|AssertionFailedError
extends|extends
name|AssertionError
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|221922462818431635L
decl_stmt|;
DECL|method|AssertionFailedError ()
specifier|public
name|AssertionFailedError
parameter_list|()
block|{ 	}
DECL|method|AssertionFailedError (String message)
specifier|public
name|AssertionFailedError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

