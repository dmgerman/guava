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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|Helpers
operator|.
name|orderEntriesByKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * Implementation helper for {@link TestMapGenerator} for use with sorted maps of strings.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_class
DECL|class|TestStringSortedMapGenerator
specifier|public
specifier|abstract
class|class
name|TestStringSortedMapGenerator
extends|extends
name|TestStringMapGenerator
block|{
comment|/** Sorts the entries by natural order. */
annotation|@
name|Override
DECL|method|order (List<Entry<String, String>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|orderEntriesByKey
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
end_class

end_unit

