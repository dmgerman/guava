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
name|HashMultimap
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
name|collect
operator|.
name|Multimap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * A {@link HandlerFindingStrategy} for collecting all event handler methods  * that are marked with the {@link Subscribe} annotation.  *  * @author Cliff Biffle  */
end_comment

begin_class
DECL|class|AnnotatedHandlerFinder
class|class
name|AnnotatedHandlerFinder
implements|implements
name|HandlerFindingStrategy
block|{
comment|/**    * {@inheritDoc}    *    * This implementation finds all methods marked with a {@link Subscribe}    * annotation.    */
annotation|@
name|Override
DECL|method|findAllHandlers (Object listener)
specifier|public
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|EventHandler
argument_list|>
name|findAllHandlers
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|EventHandler
argument_list|>
name|methodsInListener
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|Class
name|clazz
init|=
name|listener
operator|.
name|getClass
argument_list|()
decl_stmt|;
while|while
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Method
name|method
range|:
name|clazz
operator|.
name|getMethods
argument_list|()
control|)
block|{
name|Subscribe
name|annotation
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|Subscribe
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameterTypes
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Method "
operator|+
name|method
operator|+
literal|" has @Subscribe annotation, but requires "
operator|+
name|parameterTypes
operator|.
name|length
operator|+
literal|" arguments.  Event handler methods "
operator|+
literal|"must require a single argument."
argument_list|)
throw|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|eventType
init|=
name|parameterTypes
index|[
literal|0
index|]
decl_stmt|;
name|EventHandler
name|handler
init|=
name|makeHandler
argument_list|(
name|listener
argument_list|,
name|method
argument_list|)
decl_stmt|;
name|methodsInListener
operator|.
name|put
argument_list|(
name|eventType
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
name|methodsInListener
return|;
block|}
comment|/**    * Creates an {@code EventHandler} for subsequently calling {@code method} on    * {@code listener}.    * Selects an EventHandler implementation based on the annotations on    * {@code method}.    *    * @param listener  object bearing the event handler method.    * @param method  the event handler method to wrap in an EventHandler.    * @return an EventHandler that will call {@code method} on {@code listener}    *         when invoked.    */
DECL|method|makeHandler (Object listener, Method method)
specifier|private
specifier|static
name|EventHandler
name|makeHandler
parameter_list|(
name|Object
name|listener
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|EventHandler
name|wrapper
decl_stmt|;
if|if
condition|(
name|methodIsDeclaredThreadSafe
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|wrapper
operator|=
operator|new
name|EventHandler
argument_list|(
name|listener
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wrapper
operator|=
operator|new
name|SynchronizedEventHandler
argument_list|(
name|listener
argument_list|,
name|method
argument_list|)
expr_stmt|;
block|}
return|return
name|wrapper
return|;
block|}
comment|/**    * Checks whether {@code method} is thread-safe, as indicated by the    * {@link AllowConcurrentEvents} annotation.    *    * @param method  handler method to check.    * @return {@code true} if {@code handler} is marked as thread-safe,    *         {@code false} otherwise.    */
DECL|method|methodIsDeclaredThreadSafe (Method method)
specifier|private
specifier|static
name|boolean
name|methodIsDeclaredThreadSafe
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
return|return
name|method
operator|.
name|getAnnotation
argument_list|(
name|AllowConcurrentEvents
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

