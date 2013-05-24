begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A sample enumerated type we use for testing.  *  * @author Kevin Bourrillion  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
DECL|enum|AnEnum
specifier|public
enum|enum
name|AnEnum
block|{
DECL|enumConstant|A
DECL|enumConstant|B
DECL|enumConstant|C
DECL|enumConstant|D
DECL|enumConstant|E
DECL|enumConstant|F
name|A
block|,
name|B
block|,
name|C
block|,
name|D
block|,
name|E
block|,
name|F
block|}
end_enum

end_unit

