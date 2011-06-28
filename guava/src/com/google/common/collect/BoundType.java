begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied.  See the License for the specific language governing permissions and limitations  * under the License.  */
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
name|Beta
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
name|annotations
operator|.
name|GwtCompatible
import|;
end_import

begin_comment
comment|/**  * Indicates whether an endpoint of some range is contained in the range itself ("closed") or not  * ("open"). If a range is unbounded on a side, it is neither open nor closed on that side; the  * bound simply does not exist.  *  * @since Guava release 10  */
end_comment

begin_enum
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|enum|BoundType
specifier|public
enum|enum
name|BoundType
block|{
comment|/**    * The endpoint value<i>is not</i> considered part of the set ("exclusive").    */
DECL|enumConstant|OPEN
name|OPEN
block|,
comment|/**    * The endpoint value<i>is</i> considered part of the set ("inclusive").    */
DECL|enumConstant|CLOSED
name|CLOSED
block|}
end_enum

end_unit

