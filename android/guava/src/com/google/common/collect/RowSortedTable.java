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
name|Map
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
name|java
operator|.
name|util
operator|.
name|SortedSet
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
comment|/**  * Interface that extends {@code Table} and whose rows are sorted.  *  *<p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link #rowMap} method  * returns a {@link SortedMap}, instead of the {@link Set} and {@link Map} specified by the {@link  * Table} interface.  *  * @author Warren Dukes  * @since 8.0  */
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
DECL|interface|RowSortedTable
specifier|public
expr|interface
name|RowSortedTable
operator|<
name|R
expr|extends @
name|Nullable
name|Object
operator|,
name|C
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
name|Table
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
comment|/**    * {@inheritDoc}    *    *<p>This method returns a {@link SortedSet}, instead of the {@code Set} specified in the {@link    * Table} interface.    */
block|@
name|Override
DECL|method|rowKeySet ()
name|SortedSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
argument_list|()
block|;
comment|/**    * {@inheritDoc}    *    *<p>This method returns a {@link SortedMap}, instead of the {@code Map} specified in the {@link    * Table} interface.    */
block|@
name|Override
DECL|method|rowMap ()
name|SortedMap
argument_list|<
name|R
argument_list|,
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|rowMap
argument_list|()
block|; }
end_expr_stmt

end_unit

