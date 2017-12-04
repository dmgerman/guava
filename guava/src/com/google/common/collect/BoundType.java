begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_comment
comment|/**  * Indicates whether an endpoint of some range is contained in the range itself ("closed") or not  * ("open"). If a range is unbounded on a side, it is neither open nor closed on that side; the  * bound simply does not exist.  *  * @since 10.0  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
DECL|enum|BoundType
specifier|public
enum|enum
name|BoundType
block|{
comment|/** The endpoint value<i>is not</i> considered part of the set ("exclusive"). */
DECL|enumConstant|OPEN
name|OPEN
argument_list|(
literal|false
argument_list|)
block|,
DECL|enumConstant|CLOSED
name|CLOSED
argument_list|(
literal|true
argument_list|)
block|;
DECL|field|inclusive
specifier|final
name|boolean
name|inclusive
decl_stmt|;
DECL|method|BoundType (boolean inclusive)
name|BoundType
parameter_list|(
name|boolean
name|inclusive
parameter_list|)
block|{
name|this
operator|.
name|inclusive
operator|=
name|inclusive
expr_stmt|;
block|}
comment|/** Returns the bound type corresponding to a boolean value for inclusivity. */
DECL|method|forBoolean (boolean inclusive)
specifier|static
name|BoundType
name|forBoolean
parameter_list|(
name|boolean
name|inclusive
parameter_list|)
block|{
return|return
name|inclusive
condition|?
name|CLOSED
else|:
name|OPEN
return|;
block|}
DECL|method|flip ()
name|BoundType
name|flip
parameter_list|()
block|{
return|return
name|forBoolean
argument_list|(
operator|!
name|inclusive
argument_list|)
return|;
block|}
block|}
end_enum

end_unit

