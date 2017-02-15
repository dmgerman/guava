begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A dummy superclass to support GWT serialization of the element types of a {@link  * LinkedHashMultimap}. The GWT supersource for this class contains a field for each type.  *  *<p>For details about this hack, see {@link GwtSerializationDependencies}, which takes the same  * approach but with a subclass rather than a superclass.  *  *<p>TODO(cpovirk): Consider applying this subclass approach to our other types.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|LinkedHashMultimapGwtSerializationDependencies
specifier|abstract
class|class
name|LinkedHashMultimapGwtSerializationDependencies
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|LinkedHashMultimapGwtSerializationDependencies (Map<K, Collection<V>> map)
name|LinkedHashMultimapGwtSerializationDependencies
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|Collection
argument_list|<
name|V
argument_list|>
argument_list|>
name|map
parameter_list|)
block|{
name|super
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

