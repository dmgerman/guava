begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Comparator
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
comment|/**  * An implementation of {@link ImmutableTable} holding an arbitrary number of  * cells.  *  * @author Gregory Kick  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|RegularImmutableTable
specifier|abstract
class|class
name|RegularImmutableTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
DECL|method|RegularImmutableTable ()
name|RegularImmutableTable
parameter_list|()
block|{}
DECL|field|values
specifier|private
specifier|transient
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
decl_stmt|;
DECL|method|values ()
annotation|@
name|Override
specifier|public
specifier|final
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|()
block|{
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|result
init|=
name|values
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|values
operator|=
name|createValues
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createValues ()
specifier|abstract
name|ImmutableCollection
argument_list|<
name|V
argument_list|>
name|createValues
parameter_list|()
function_decl|;
DECL|method|size ()
annotation|@
name|Override
specifier|public
specifier|abstract
name|int
name|size
parameter_list|()
function_decl|;
DECL|field|cellSet
specifier|private
specifier|transient
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellSet
decl_stmt|;
annotation|@
name|Override
DECL|method|cellSet ()
specifier|public
specifier|final
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellSet
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|result
init|=
name|cellSet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|cellSet
operator|=
name|createCellSet
argument_list|()
else|:
name|result
return|;
block|}
DECL|method|createCellSet ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|createCellSet
parameter_list|()
function_decl|;
DECL|class|CellSet
specifier|abstract
class|class
name|CellSet
extends|extends
name|ImmutableSet
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|RegularImmutableTable
operator|.
name|this
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (@ullable Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Cell
condition|)
block|{
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|cell
init|=
operator|(
name|Cell
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
name|Object
name|value
init|=
name|get
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
operator|&&
name|value
operator|.
name|equals
argument_list|(
name|cell
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|forCells ( List<Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator)
specifier|static
specifier|final
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|forCells
parameter_list|(
name|List
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cells
parameter_list|,
annotation|@
name|Nullable
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|,
annotation|@
name|Nullable
specifier|final
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|cells
argument_list|)
expr_stmt|;
if|if
condition|(
name|rowComparator
operator|!=
literal|null
operator|||
name|columnComparator
operator|!=
literal|null
condition|)
block|{
comment|/*        * This sorting logic leads to a cellSet() ordering that may not be expected and that isn't        * documented in the Javadoc. If a row Comparator is provided, cellSet() iterates across the        * columns in the first row, the columns in the second row, etc. If a column Comparator is        * provided but a row Comparator isn't, cellSet() iterates across the rows in the first        * column, the rows in the second column, etc.        */
name|Comparator
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|comparator
init|=
operator|new
name|Comparator
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell1
parameter_list|,
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell2
parameter_list|)
block|{
name|int
name|rowCompare
init|=
operator|(
name|rowComparator
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|rowComparator
operator|.
name|compare
argument_list|(
name|cell1
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell2
operator|.
name|getRowKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|rowCompare
operator|!=
literal|0
condition|)
block|{
return|return
name|rowCompare
return|;
block|}
return|return
operator|(
name|columnComparator
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|columnComparator
operator|.
name|compare
argument_list|(
name|cell1
operator|.
name|getColumnKey
argument_list|()
argument_list|,
name|cell2
operator|.
name|getColumnKey
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|cells
argument_list|,
name|comparator
argument_list|)
expr_stmt|;
block|}
return|return
name|forCellsInternal
argument_list|(
name|cells
argument_list|,
name|rowComparator
argument_list|,
name|columnComparator
argument_list|)
return|;
block|}
DECL|method|forCells ( Iterable<Cell<R, C, V>> cells)
specifier|static
specifier|final
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|forCells
parameter_list|(
name|Iterable
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cells
parameter_list|)
block|{
return|return
name|forCellsInternal
argument_list|(
name|cells
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**    * A factory that chooses the most space-efficient representation of the    * table.    */
specifier|private
specifier|static
specifier|final
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
name|RegularImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
DECL|method|forCellsInternal (Iterable<Cell<R, C, V>> cells, @Nullable Comparator<? super R> rowComparator, @Nullable Comparator<? super C> columnComparator)
name|forCellsInternal
parameter_list|(
name|Iterable
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cells
parameter_list|,
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|rowComparator
parameter_list|,
annotation|@
name|Nullable
name|Comparator
argument_list|<
name|?
super|super
name|C
argument_list|>
name|columnComparator
parameter_list|)
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|R
argument_list|>
name|rowSpaceBuilder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|C
argument_list|>
name|columnSpaceBuilder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
name|ImmutableList
argument_list|<
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|cellList
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|cells
argument_list|)
decl_stmt|;
for|for
control|(
name|Cell
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
name|cell
range|:
name|cellList
control|)
block|{
name|rowSpaceBuilder
operator|.
name|add
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|)
expr_stmt|;
name|columnSpaceBuilder
operator|.
name|add
argument_list|(
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ImmutableSet
argument_list|<
name|R
argument_list|>
name|rowSpace
init|=
name|rowSpaceBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|rowComparator
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|R
argument_list|>
name|rowList
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|rowSpace
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|rowList
argument_list|,
name|rowComparator
argument_list|)
expr_stmt|;
name|rowSpace
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|rowList
argument_list|)
expr_stmt|;
block|}
name|ImmutableSet
argument_list|<
name|C
argument_list|>
name|columnSpace
init|=
name|columnSpaceBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|columnComparator
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|C
argument_list|>
name|columnList
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|columnSpace
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|columnList
argument_list|,
name|columnComparator
argument_list|)
expr_stmt|;
name|columnSpace
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|columnList
argument_list|)
expr_stmt|;
block|}
comment|// use a dense table if more than half of the cells have values
comment|// TODO(gak): tune this condition based on empirical evidence
return|return
operator|(
name|cellList
operator|.
name|size
argument_list|()
operator|>
operator|(
operator|(
name|rowSpace
operator|.
name|size
argument_list|()
operator|*
name|columnSpace
operator|.
name|size
argument_list|()
operator|)
operator|/
literal|2
operator|)
operator|)
condition|?
operator|new
name|DenseImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|cellList
argument_list|,
name|rowSpace
argument_list|,
name|columnSpace
argument_list|)
else|:
operator|new
name|SparseImmutableTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|cellList
argument_list|,
name|rowSpace
argument_list|,
name|columnSpace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

