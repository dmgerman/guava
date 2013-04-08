begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
package|;
end_package

begin_comment
comment|/**  * Options controlling the behavior of sources/sinks/streams for testing.  *  * @author Colin Decker  */
end_comment

begin_enum
DECL|enum|TestOption
specifier|public
enum|enum
name|TestOption
block|{
DECL|enumConstant|OPEN_THROWS
name|OPEN_THROWS
block|,
DECL|enumConstant|SKIP_THROWS
name|SKIP_THROWS
block|,
DECL|enumConstant|READ_THROWS
name|READ_THROWS
block|,
DECL|enumConstant|WRITE_THROWS
name|WRITE_THROWS
block|,
DECL|enumConstant|CLOSE_THROWS
name|CLOSE_THROWS
block|,
DECL|enumConstant|AVAILABLE_ALWAYS_ZERO
name|AVAILABLE_ALWAYS_ZERO
block|}
end_enum

end_unit

