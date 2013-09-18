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
name|Multimap
import|;
end_import

begin_comment
comment|/**  * A method for finding event subscriber methods in objects, for use by  * {@link EventBus}.  *  * @author Cliff Biffle  */
end_comment

begin_interface
DECL|interface|SubscriberFindingStrategy
interface|interface
name|SubscriberFindingStrategy
block|{
comment|/**    * Finds all suitable event subscriber methods in {@code source}, organizes them    * by the type of event they handle, and wraps them in {@link EventSubscriber} instances.    *    * @param source  object whose subscribers are desired.    * @return EventSubscriber objects for each subscriber method, organized by event    *         type.    *    * @throws IllegalArgumentException if {@code source} is not appropriate for    *         this strategy (in ways that this interface does not define).    */
DECL|method|findAllSubscribers (Object source)
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|EventSubscriber
argument_list|>
name|findAllSubscribers
parameter_list|(
name|Object
name|source
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

