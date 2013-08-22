begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
comment|/**  * Precondition checks useful in collection implementations.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CollectPreconditions
specifier|final
class|class
name|CollectPreconditions
block|{
DECL|method|checkEntryNotNull (Object key, Object value)
specifier|static
name|void
name|checkEntryNotNull
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null key in entry: null="
operator|+
name|value
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"null value in entry: "
operator|+
name|key
operator|+
literal|"=null"
argument_list|)
throw|;
block|}
block|}
DECL|method|checkNonnegative (int value, String name)
specifier|static
name|int
name|checkNonnegative
parameter_list|(
name|int
name|value
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|value
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|name
operator|+
literal|" cannot be negative but was: "
operator|+
name|value
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
comment|/**    * Precondition tester for {@code Iterator.remove()} that throws an exception with a consistent    * error message.    */
DECL|method|checkRemove (boolean canRemove)
specifier|static
name|void
name|checkRemove
parameter_list|(
name|boolean
name|canRemove
parameter_list|)
block|{
name|checkState
argument_list|(
name|canRemove
argument_list|,
literal|"no calls to next() since the last call to remove()"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

