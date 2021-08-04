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
name|SortedMap
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
comment|/**  * An object representing the differences between two sorted maps.  *  * @author Louis Wasserman  * @since 8.0  */
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
DECL|interface|SortedMapDifference
specifier|public
expr|interface
name|SortedMapDifference
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
name|MapDifference
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{    @
name|Override
DECL|method|entriesOnlyOnLeft ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnLeft
argument_list|()
block|;    @
name|Override
DECL|method|entriesOnlyOnRight ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesOnlyOnRight
argument_list|()
block|;    @
name|Override
DECL|method|entriesInCommon ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entriesInCommon
argument_list|()
block|;    @
name|Override
DECL|method|entriesDiffering ()
name|SortedMap
argument_list|<
name|K
argument_list|,
name|ValueDifference
argument_list|<
name|V
argument_list|>
argument_list|>
name|entriesDiffering
argument_list|()
block|; }
end_expr_stmt

end_unit

