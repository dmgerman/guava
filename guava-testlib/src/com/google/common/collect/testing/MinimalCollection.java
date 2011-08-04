begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|AbstractCollection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A simplistic collection which implements only the bare minimum allowed by the  * spec, and throws exceptions whenever it can.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|MinimalCollection
specifier|public
class|class
name|MinimalCollection
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollection
argument_list|<
name|E
argument_list|>
block|{
comment|// TODO: expose allow nulls parameter?
DECL|method|of (E... contents)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|MinimalCollection
argument_list|<
name|E
argument_list|>
name|of
parameter_list|(
name|E
modifier|...
name|contents
parameter_list|)
block|{
return|return
operator|new
name|MinimalCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|Object
operator|.
name|class
argument_list|,
literal|true
argument_list|,
name|contents
argument_list|)
return|;
block|}
comment|// TODO: use this
DECL|method|ofClassAndContents ( Class<? super E> type, E... contents)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|MinimalCollection
argument_list|<
name|E
argument_list|>
name|ofClassAndContents
parameter_list|(
name|Class
argument_list|<
name|?
super|super
name|E
argument_list|>
name|type
parameter_list|,
name|E
modifier|...
name|contents
parameter_list|)
block|{
return|return
operator|new
name|MinimalCollection
argument_list|<
name|E
argument_list|>
argument_list|(
name|type
argument_list|,
literal|true
argument_list|,
name|contents
argument_list|)
return|;
block|}
DECL|field|contents
specifier|private
specifier|final
name|E
index|[]
name|contents
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Class
argument_list|<
name|?
super|super
name|E
argument_list|>
name|type
decl_stmt|;
DECL|field|allowNulls
specifier|private
specifier|final
name|boolean
name|allowNulls
decl_stmt|;
comment|// Package-private so that it can be extended.
DECL|method|MinimalCollection (Class<? super E> type, boolean allowNulls, E... contents)
name|MinimalCollection
parameter_list|(
name|Class
argument_list|<
name|?
super|super
name|E
argument_list|>
name|type
parameter_list|,
name|boolean
name|allowNulls
parameter_list|,
name|E
modifier|...
name|contents
parameter_list|)
block|{
comment|// TODO: consider making it shuffle the contents to test iteration order.
name|this
operator|.
name|contents
operator|=
name|Platform
operator|.
name|clone
argument_list|(
name|contents
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|allowNulls
operator|=
name|allowNulls
expr_stmt|;
if|if
condition|(
operator|!
name|allowNulls
condition|)
block|{
for|for
control|(
name|Object
name|element
range|:
name|contents
control|)
block|{
name|Helpers
operator|.
name|checkNotNull
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|size ()
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|contents
operator|.
name|length
return|;
block|}
DECL|method|contains (Object object)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
name|allowNulls
condition|)
block|{
name|Helpers
operator|.
name|checkNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// behave badly
block|}
name|Platform
operator|.
name|checkCast
argument_list|(
name|type
argument_list|,
name|object
argument_list|)
expr_stmt|;
comment|// behave badly
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|contents
argument_list|)
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> collection)
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
if|if
condition|(
operator|!
name|allowNulls
condition|)
block|{
for|for
control|(
name|Object
name|object
range|:
name|collection
control|)
block|{
name|Helpers
operator|.
name|checkNotNull
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// behave badly
block|}
block|}
return|return
name|super
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
DECL|method|iterator ()
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|contents
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|toArray ()
annotation|@
name|Override
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|Object
index|[]
name|result
init|=
operator|new
name|Object
index|[
name|contents
operator|.
name|length
index|]
decl_stmt|;
name|Platform
operator|.
name|unsafeArrayCopy
argument_list|(
name|contents
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|contents
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/*    * a "type A" unmodifiable collection freaks out proactively, even if there    * wasn't going to be any actual work to do anyway    */
DECL|method|addAll (Collection<? extends E> elementsToAdd)
annotation|@
name|Override
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elementsToAdd
parameter_list|)
block|{
throw|throw
name|up
argument_list|()
throw|;
block|}
DECL|method|removeAll (Collection<?> elementsToRemove)
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elementsToRemove
parameter_list|)
block|{
throw|throw
name|up
argument_list|()
throw|;
block|}
DECL|method|retainAll (Collection<?> elementsToRetain)
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|elementsToRetain
parameter_list|)
block|{
throw|throw
name|up
argument_list|()
throw|;
block|}
DECL|method|clear ()
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
throw|throw
name|up
argument_list|()
throw|;
block|}
DECL|method|up ()
specifier|private
specifier|static
name|UnsupportedOperationException
name|up
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

