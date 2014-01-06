begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
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
name|testing
operator|.
name|AbstractPackageSanityTests
import|;
end_import

begin_comment
comment|/**  * Basic sanity tests for the entire package.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|PackageSanityTests
specifier|public
class|class
name|PackageSanityTests
extends|extends
name|AbstractPackageSanityTests
block|{
DECL|method|PackageSanityTests ()
specifier|public
name|PackageSanityTests
parameter_list|()
block|{
name|publicApiOnly
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

