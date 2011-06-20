begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|Beta
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
name|NoSuchElementException
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
comment|/**  * A {@link Multiset} which maintains the ordering of its elements, according to  * either their natural order or an explicit {@link Comparator}. In all cases,  * this implementation uses {@link Comparable#compareTo} or  * {@link Comparator#compare} instead of {@link Object#equals} to determine  * equivalence of instances.  *  *<p><b>Warning:</b> The comparison must be<i>consistent with equals</i> as  * explained by the {@link Comparable} class specification. Otherwise, the  * resulting multiset will violate the {@link Collection} contract, which it is  * specified in terms of {@link Object#equals}.  *  * @author Louis Wasserman  */
end_comment

begin_interface
annotation|@
name|Beta
DECL|interface|SortedMultiset
interface|interface
name|SortedMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Multiset
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Returns the comparator that orders this multiset, or    * {@link Ordering#natural()} if the natural ordering of the elements is used.    */
DECL|method|comparator ()
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
function_decl|;
comment|/**    * Returns the greatest element in this multiset that is strictly less than    * the specified element, or {@code null} if there is no such element.    */
DECL|method|lower (E e)
name|E
name|lower
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the greatest element in this multiset that is less than or equal to    * the specified element, or {@code null} if there is no such element.    */
DECL|method|floor (E e)
name|E
name|floor
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the lowest element in this multiset that is greater than or equal    * to the specified element, or {@code null} if there is no such element.    */
DECL|method|ceiling (E e)
name|E
name|ceiling
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the lowest element in this multiset that is strictly greater than    * the specified element, or {@code null} if there is no such element.    */
DECL|method|higher (E e)
name|E
name|higher
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the entry of the greatest element in this multiset that is strictly    * less than the specified element, or {@code null} if there is no such    * element.    *    *<p>Equivalent to {@code headMultiset(e, false).lastEntry()}.    */
DECL|method|lowerEntry (E e)
name|Entry
argument_list|<
name|E
argument_list|>
name|lowerEntry
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the entry of the greatest element in this multiset that is less    * than or equal to the specified element, or {@code null} if there is no such    * element.    *<p>Equivalent to {@code headMultiset(e, true).lastEntry()}.    */
DECL|method|floorEntry (E e)
name|Entry
argument_list|<
name|E
argument_list|>
name|floorEntry
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the entry of the lowest element in this multiset that is greater    * than or equal to the specified element, or {@code null} if there is no such    * element.    *<p>Equivalent to {@code tailMultiset(e, true).firstEntry()}.    */
DECL|method|ceilingEntry (E e)
name|Entry
argument_list|<
name|E
argument_list|>
name|ceilingEntry
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the entry of the lowest element in this multiset that is strictly    * greater than the specified element, or {@code null} if there is no such    * element.    *<p>Equivalent to {@code tailMultiset(e, false).firstEntry()}.    */
DECL|method|higherEntry (E e)
name|Entry
argument_list|<
name|E
argument_list|>
name|higherEntry
parameter_list|(
name|E
name|e
parameter_list|)
function_decl|;
comment|/**    * Returns the first element in this multiset. Equivalent to {@code    * elementSet().first()}.    *    * @throws NoSuchElementException if this multiset is empty    */
DECL|method|first ()
name|E
name|first
parameter_list|()
function_decl|;
comment|/**    * Returns the last element in this multiset. Equivalent to {@code    * elementSet().last()}.    *    * @throws NoSuchElementException if this multiset is empty    */
DECL|method|last ()
name|E
name|last
parameter_list|()
function_decl|;
comment|/**    * Returns the entry of the first element in this multiset, or {@code null} if    * this multiset is empty.    */
DECL|method|firstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|firstEntry
parameter_list|()
function_decl|;
comment|/**    * Returns the entry of the last element in this multiset, or {@code null} if    * this multiset is empty.    */
DECL|method|lastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|lastEntry
parameter_list|()
function_decl|;
comment|/**    * Returns and removes the entry associated with the lowest element in this    * multiset, or returns {@code null} if this multiset is empty.    */
DECL|method|pollFirstEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollFirstEntry
parameter_list|()
function_decl|;
comment|/**    * Returns and removes the entry associated with the greatest element in this    * multiset, or returns {@code null} if this multiset is empty.    */
DECL|method|pollLastEntry ()
name|Entry
argument_list|<
name|E
argument_list|>
name|pollLastEntry
parameter_list|()
function_decl|;
comment|/**    * Returns a {@link SortedSet} view of the distinct elements in this multiset.    */
DECL|method|elementSet ()
annotation|@
name|Override
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
function_decl|;
comment|/**    * Returns a descending view of the distinct elements in this multiset.    */
DECL|method|descendingElementSet ()
name|SortedSet
argument_list|<
name|E
argument_list|>
name|descendingElementSet
parameter_list|()
function_decl|;
comment|/**    * Returns an iterator over this multiset in descending order.    */
DECL|method|descendingIterator ()
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
function_decl|;
comment|/**    * Returns a descending view of this multiset. Modifications made to either    * map will be reflected in the other.    */
DECL|method|descendingMultiset ()
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|descendingMultiset
parameter_list|()
function_decl|;
comment|/**    * Equivalent to {@code headMultiset(toElement, false)}.    */
DECL|method|headMultiset (E toElement)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|headMultiset
parameter_list|(
name|E
name|toElement
parameter_list|)
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the elements less than (or    * equal to, if {@code inclusive}) {@code toElement}. The returned multiset is    * a view of this multiset, so changes to one will be reflected in the other.    * The returned multiset supports all operations that this multiset supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    */
DECL|method|headMultiset (E toElement, boolean inclusive)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|headMultiset
parameter_list|(
name|E
name|toElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the range between {@code    * fromElement} and {@code toElement}. The returned multiset is a view of this    * multiset, so changes to one will be reflected in the other. The returned    * multiset supports all operations that this multiset supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    *     * @throws IllegalArgumentException if {@code fromElement} is greater than    *         {@code toElement}    */
DECL|method|subMultiset ( E fromElement, boolean fromInclusive, E toElement, boolean toInclusive)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|fromInclusive
parameter_list|,
name|E
name|toElement
parameter_list|,
name|boolean
name|toInclusive
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code subMultiset(fromElement, true, toElement, false)}.    */
DECL|method|subMultiset (E fromElement, E toElement)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|subMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
function_decl|;
comment|/**    * Equivalent to {@code tailMultiset(fromElement, true)}.    */
DECL|method|tailMultiset (E fromElement)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|tailMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|)
function_decl|;
comment|/**    * Returns a view of this multiset restricted to the elements greater than (or    * equal to, if {@code inclusive}) {@code fromElement}. The returned multiset    * is a view of this multiset, so changes to one will be reflected in the    * other. The returned multiset supports all operations that this multiset    * supports.    *    *<p>The returned multiset will throw an {@link IllegalArgumentException} on    * attempts to add elements outside its range.    */
DECL|method|tailMultiset (E fromElement, boolean inclusive)
name|SortedMultiset
argument_list|<
name|E
argument_list|>
name|tailMultiset
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

