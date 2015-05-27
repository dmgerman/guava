begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Predicate
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of {@link Multimaps#filterKeys(ListMultimap, Predicate)}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|FilteredKeyListMultimap
specifier|final
class|class
name|FilteredKeyListMultimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|FilteredKeyMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|FilteredKeyListMultimap (ListMultimap<K, V> unfiltered, Predicate<? super K> keyPredicate)
name|FilteredKeyListMultimap
parameter_list|(
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unfiltered
parameter_list|,
name|Predicate
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyPredicate
parameter_list|)
block|{
name|super
argument_list|(
name|unfiltered
argument_list|,
name|keyPredicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unfiltered ()
specifier|public
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|unfiltered
parameter_list|()
block|{
return|return
operator|(
name|ListMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|unfiltered
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (K key)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (@ullable Object key)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|removeAll
parameter_list|(
annotation|@
name|Nullable
name|Object
name|key
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|replaceValues (K key, Iterable<? extends V> values)
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
name|K
name|key
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|values
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|V
argument_list|>
operator|)
name|super
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
block|}
end_class

end_unit

