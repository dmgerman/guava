begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A method supported by implementations of the {@link Iterator} or  * {@link ListIterator} interface.  *  *<p>This enum is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_enum
annotation|@
name|GwtCompatible
DECL|enum|IteratorFeature
specifier|public
enum|enum
name|IteratorFeature
block|{
comment|/**    * Support for {@link Iterator#remove()}.    */
DECL|enumConstant|SUPPORTS_REMOVE
name|SUPPORTS_REMOVE
block|,
comment|/**    * Support for {@link ListIterator#add(Object)}; ignored for plain    * {@link Iterator} implementations.    */
DECL|enumConstant|SUPPORTS_ADD
name|SUPPORTS_ADD
block|,
comment|/**    * Support for {@link ListIterator#set(Object)}; ignored for plain    * {@link Iterator} implementations.    */
DECL|enumConstant|SUPPORTS_SET
name|SUPPORTS_SET
block|;
comment|/**    * A set containing none of the optional features of the {@link Iterator} or    * {@link ListIterator} interfaces.    */
DECL|field|UNMODIFIABLE
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|IteratorFeature
argument_list|>
name|UNMODIFIABLE
init|=
name|Collections
operator|.
name|emptySet
argument_list|()
decl_stmt|;
comment|/**    * A set containing all of the optional features of the {@link Iterator} and    * {@link ListIterator} interfaces.    */
DECL|field|MODIFIABLE
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|IteratorFeature
argument_list|>
name|MODIFIABLE
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|EnumSet
operator|.
name|allOf
argument_list|(
name|IteratorFeature
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
block|}
end_enum

end_unit

