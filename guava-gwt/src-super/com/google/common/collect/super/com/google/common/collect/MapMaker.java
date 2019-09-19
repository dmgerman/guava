begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkArgument
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * MapMaker emulation.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|MapMaker
specifier|public
specifier|final
class|class
name|MapMaker
block|{
DECL|field|initialCapacity
specifier|private
name|int
name|initialCapacity
init|=
literal|16
decl_stmt|;
DECL|method|MapMaker ()
specifier|public
name|MapMaker
parameter_list|()
block|{}
DECL|method|initialCapacity (int initialCapacity)
specifier|public
name|MapMaker
name|initialCapacity
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
if|if
condition|(
name|initialCapacity
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|concurrencyLevel (int concurrencyLevel)
specifier|public
name|MapMaker
name|concurrencyLevel
parameter_list|(
name|int
name|concurrencyLevel
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|concurrencyLevel
operator|>=
literal|1
argument_list|,
literal|"concurrency level (%s) must be at least 1"
argument_list|,
name|concurrencyLevel
argument_list|)
expr_stmt|;
comment|// GWT technically only supports concurrencyLevel == 1, but we silently
comment|// ignore other positive values.
return|return
name|this
return|;
block|}
DECL|method|makeMap ()
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeMap
parameter_list|()
block|{
return|return
operator|new
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|initialCapacity
argument_list|)
return|;
block|}
block|}
end_class

end_unit

