begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not  * use this file except in compliance with the License. You may obtain a copy of  * the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the  * License for the specific language governing permissions and limitations under  * the License.  */
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
name|Comparator
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
name|NavigableSet
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
comment|/**  * A {@link Multiset} which maintains the ordering of its elements, according to either their  * natural order or an explicit {@link Comparator}. This order is reflected when iterating over the  * sorted multiset, either directly, or through its {@code elementSet} or {@code entrySet} views. In  * all cases, this implementation uses {@link Comparable#compareTo} or {@link Comparator#compare}  * instead of {@link Object#equals} to determine equivalence of instances.  *  *<p><b>Warning:</b> The comparison must be<i>consistent with equals</i> as explained by the  * {@link Comparable} class specification. Otherwise, the resulting multiset will violate the {@link  * Collection} contract, which is specified in terms of {@link Object#equals}.  *  *<p>See the Guava User Guide article on<a href=  * "https://github.com/google/guava/wiki/NewCollectionTypesExplained#multiset"> {@code  * Multiset}</a>.  *  * @author Louis Wasserman  * @since 11.0  */
end_comment

begin_annotation
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
end_annotation

begin_annotation
annotation|@
name|ElementTypesAreNonnullByDefault
end_annotation

begin_expr_stmt
DECL|interface|SortedMultiset
specifier|public
expr|interface
name|SortedMultiset
operator|<
name|E
expr|extends @
name|Nullable
name|Object
operator|>
expr|extends
name|SortedMultisetBridge
argument_list|<
name|E
argument_list|>
operator|,
name|SortedIterable
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the comparator that orders this multiset, or {@link Ordering#natural()} if the natural    * ordering of the elements is used.    */
block|@
name|Override
DECL|method|comparator ()
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
argument_list|()
block|;
comment|/**    * Returns the entry of the first element in this multiset, or {@code null} if this multiset is    * empty.    */
block|@
name|CheckForNull
DECL|method|firstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
argument_list|()
block|;
comment|/**    * Returns the entry of the last element in this multiset, or {@code null} if this multiset is    * empty.    */
block|@
name|CheckForNull
DECL|method|lastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
argument_list|()
block|;
comment|/**    * Returns and removes the entry associated with the lowest element in this multiset, or returns    * {@code null} if this multiset is empty.    */
block|@
name|CheckForNull
DECL|method|pollFirstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
argument_list|()
block|;
comment|/**    * Returns and removes the entry associated with the greatest element in this multiset, or returns    * {@code null} if this multiset is empty.    */
block|@
name|CheckForNull
DECL|method|pollLastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
argument_list|()
block|;
comment|/**    * Returns a {@link NavigableSet} view of the distinct elements in this multiset.    *    * @since 14.0 (present with return type {@code SortedSet} since 11.0)    */
block|@
name|Override
DECL|method|elementSet ()
name|NavigableSet
argument_list|<
name|E
argument_list|>
name|elementSet
argument_list|()
block|;
comment|/**    * {@inheritDoc}    *    *<p>The {@code entrySet}'s iterator returns entries in ascending element order according to the    * this multiset's comparator.    */
block|@
name|Override
DECL|method|entrySet ()
name|Set
argument_list|<
name|Entry
argument_list|<
name|E
argument_list|>
argument_list|>
name|entrySet
argument_list|()
block|;
comment|/**    * {@inheritDoc}    *    *<p>The iterator returns the elements in ascending order according to this multiset's    * comparator.    */
block|@
name|Override
DECL|method|iterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
argument_list|()
block|;
comment|/**    * Returns a descending view of this multiset. Modifications made to either map will be reflected    * in the other.    */
DECL|method|descendingMultiset ()
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
argument_list|()
block|;
comment|/**    * Returns a view of this multiset restricted to the elements less than {@code upperBound},    * optionally including {@code upperBound} itself. The returned multiset is a view of this    * multiset, so changes to one will be reflected in the other. The returned multiset supports all    * operations that this multiset supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on attempts to add    * elements outside its range.    */
DECL|method|headMultiset (@arametricNullness E upperBound, BoundType boundType)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|headMultiset
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|upperBound
argument_list|,
name|BoundType
name|boundType
argument_list|)
block|;
comment|/**    * Returns a view of this multiset restricted to the range between {@code lowerBound} and {@code    * upperBound}. The returned multiset is a view of this multiset, so changes to one will be    * reflected in the other. The returned multiset supports all operations that this multiset    * supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on attempts to add    * elements outside its range.    *    *<p>This method is equivalent to {@code tailMultiset(lowerBound,    * lowerBoundType).headMultiset(upperBound, upperBoundType)}.    */
DECL|method|subMultiset ( @arametricNullness E lowerBound, BoundType lowerBoundType, @ParametricNullness E upperBound, BoundType upperBoundType)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|lowerBound
argument_list|,
name|BoundType
name|lowerBoundType
argument_list|,
annotation|@
name|ParametricNullness
name|E
name|upperBound
argument_list|,
name|BoundType
name|upperBoundType
argument_list|)
block|;
comment|/**    * Returns a view of this multiset restricted to the elements greater than {@code lowerBound},    * optionally including {@code lowerBound} itself. The returned multiset is a view of this    * multiset, so changes to one will be reflected in the other. The returned multiset supports all    * operations that this multiset supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on attempts to add    * elements outside its range.    */
DECL|method|tailMultiset (@arametricNullness E lowerBound, BoundType boundType)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|tailMultiset
argument_list|(
annotation|@
name|ParametricNullness
name|E
name|lowerBound
argument_list|,
name|BoundType
name|boundType
argument_list|)
block|; }
end_expr_stmt

end_unit

