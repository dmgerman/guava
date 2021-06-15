begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A sorted set multimap which forwards all its method calls to another sorted set multimap.  * Subclasses should override one or more methods to modify the behavior of the backing multimap as  * desired per the<a href="http://en.wikipedia.org/wiki/Decorator_pattern">decorator pattern</a>.  *  *<p><b>{@code default} method warning:</b> This class does<i>not</i> forward calls to {@code  * default} methods. Instead, it inherits their default implementations. When those implementations  * invoke methods, they invoke methods on the {@code ForwardingSortedSetMultimap}.  *  * @author Kurt Alfred Kluever  * @since 3.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|class|ForwardingSortedSetMultimap
specifier|public
specifier|abstract
name|class
name|ForwardingSortedSetMultimap
operator|<
name|K
expr|extends @
name|Nullable
name|Object
operator|,
name|V
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|ForwardingSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
expr|implements
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|/** Constructor for use by subclasses. */
DECL|method|ForwardingSortedSetMultimap ()
specifier|protected
name|ForwardingSortedSetMultimap
argument_list|()
block|{}
expr|@
name|Override
DECL|method|delegate ()
specifier|protected
specifier|abstract
name|SortedSetMultimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|delegate
argument_list|()
block|;    @
name|Override
DECL|method|get (@arametricNullness K key)
specifier|public
name|SortedSet
argument_list|<
name|V
argument_list|>
name|get
argument_list|(
annotation|@
name|ParametricNullness
name|K
name|key
argument_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
expr|@
name|Override
DECL|method|removeAll (@heckForNull Object key)
specifier|public
name|SortedSet
argument_list|<
name|V
argument_list|>
name|removeAll
argument_list|(
annotation|@
name|CheckForNull
name|Object
name|key
argument_list|)
block|{
return|return
name|delegate
argument_list|()
operator|.
name|removeAll
argument_list|(
name|key
argument_list|)
return|;
block|}
end_expr_stmt

begin_function
annotation|@
name|Override
DECL|method|replaceValues (@arametricNullness K key, Iterable<? extends V> values)
specifier|public
name|SortedSet
argument_list|<
name|V
argument_list|>
name|replaceValues
parameter_list|(
annotation|@
name|ParametricNullness
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
name|delegate
argument_list|()
operator|.
name|replaceValues
argument_list|(
name|key
argument_list|,
name|values
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|valueComparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|V
argument_list|>
name|valueComparator
parameter_list|()
block|{
return|return
name|delegate
argument_list|()
operator|.
name|valueComparator
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

