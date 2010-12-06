begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Supplier
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

begin_comment
comment|/**  * Implementation of {@code Table} whose iteration ordering across row keys is  * sorted by their natural ordering or by a supplied comparator. Note that  * iterations across the columns keys for a single row key may or may not be  * ordered, depending on the implementation. When rows and columns are both  * sorted, it's easier to use the {@link TreeBasedTable} subclass.  *  *<p>The {@link #rowKeySet} method returns a {@link SortedSet} and the {@link  * #rowMap} method returns a {@link SortedMap}, instead of the {@link Set} and  * {@link Map} specified by the {@link Table} interface.  *  *<p>Null keys and values are not supported.  *  *<p>See the {@link StandardTable} superclass for more information about the  * behavior of this class.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|StandardRowSortedTable
class|class
name|StandardRowSortedTable
parameter_list|<
name|R
parameter_list|,
name|C
parameter_list|,
name|V
parameter_list|>
extends|extends
name|StandardTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
implements|implements
name|RowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
block|{
comment|/*    * TODO(jlevy): Consider adding headTable, tailTable, and subTable methods,    * which return a Table view with rows keys in a given range. Create a    * RowSortedTable subinterface with the revised methods?    */
DECL|method|StandardRowSortedTable (SortedMap<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory)
name|StandardRowSortedTable
parameter_list|(
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
name|backingMap
parameter_list|,
name|Supplier
argument_list|<
name|?
extends|extends
name|Map
argument_list|<
name|C
argument_list|,
name|V
argument_list|>
argument_list|>
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|backingMap
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
DECL|method|sortedBackingMap ()
specifier|private
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
name|sortedBackingMap
parameter_list|()
block|{
return|return
operator|(
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
operator|)
name|backingMap
return|;
block|}
DECL|field|rowKeySet
specifier|private
specifier|transient
name|SortedSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
decl_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>This method returns a {@link SortedSet}, instead of the {@code Set}    * specified in the {@link Table} interface.    */
DECL|method|rowKeySet ()
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|R
argument_list|>
name|rowKeySet
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|R
argument_list|>
name|result
init|=
name|rowKeySet
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|rowKeySet
operator|=
operator|new
name|RowKeySortedSet
argument_list|()
else|:
name|result
return|;
block|}
DECL|class|RowKeySortedSet
specifier|private
class|class
name|RowKeySortedSet
extends|extends
name|RowKeySet
implements|implements
name|SortedSet
argument_list|<
name|R
argument_list|>
block|{
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
DECL|method|first ()
specifier|public
name|R
name|first
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
DECL|method|last ()
specifier|public
name|R
name|last
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
DECL|method|headSet (R toElement)
specifier|public
name|SortedSet
argument_list|<
name|R
argument_list|>
name|headSet
parameter_list|(
name|R
name|toElement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|headMap
argument_list|(
name|toElement
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
DECL|method|subSet (R fromElement, R toElement)
specifier|public
name|SortedSet
argument_list|<
name|R
argument_list|>
name|subSet
parameter_list|(
name|R
name|fromElement
parameter_list|,
name|R
name|toElement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toElement
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
DECL|method|tailSet (R fromElement)
specifier|public
name|SortedSet
argument_list|<
name|R
argument_list|>
name|tailSet
parameter_list|(
name|R
name|fromElement
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromElement
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromElement
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowKeySet
argument_list|()
return|;
block|}
block|}
DECL|field|rowMap
specifier|private
specifier|transient
name|RowSortedMap
name|rowMap
decl_stmt|;
comment|/**    * {@inheritDoc}    *    *<p>This method returns a {@link SortedMap}, instead of the {@code Map}    * specified in the {@link Table} interface.    */
DECL|method|rowMap ()
annotation|@
name|Override
specifier|public
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
parameter_list|()
block|{
name|RowSortedMap
name|result
init|=
name|rowMap
decl_stmt|;
return|return
operator|(
name|result
operator|==
literal|null
operator|)
condition|?
name|rowMap
operator|=
operator|new
name|RowSortedMap
argument_list|()
else|:
name|result
return|;
block|}
DECL|class|RowSortedMap
specifier|private
class|class
name|RowSortedMap
extends|extends
name|RowMap
implements|implements
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
block|{
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|R
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
DECL|method|firstKey ()
specifier|public
name|R
name|firstKey
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
DECL|method|lastKey ()
specifier|public
name|R
name|lastKey
parameter_list|()
block|{
return|return
name|sortedBackingMap
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
DECL|method|headMap (R toKey)
specifier|public
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
name|headMap
parameter_list|(
name|R
name|toKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|headMap
argument_list|(
name|toKey
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowMap
argument_list|()
return|;
block|}
DECL|method|subMap (R fromKey, R toKey)
specifier|public
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
name|subMap
parameter_list|(
name|R
name|fromKey
parameter_list|,
name|R
name|toKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
name|checkNotNull
argument_list|(
name|toKey
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromKey
argument_list|,
name|toKey
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowMap
argument_list|()
return|;
block|}
DECL|method|tailMap (R fromKey)
specifier|public
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
name|tailMap
parameter_list|(
name|R
name|fromKey
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|fromKey
argument_list|)
expr_stmt|;
return|return
operator|new
name|StandardRowSortedTable
argument_list|<
name|R
argument_list|,
name|C
argument_list|,
name|V
argument_list|>
argument_list|(
name|sortedBackingMap
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromKey
argument_list|)
argument_list|,
name|factory
argument_list|)
operator|.
name|rowMap
argument_list|()
return|;
block|}
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

