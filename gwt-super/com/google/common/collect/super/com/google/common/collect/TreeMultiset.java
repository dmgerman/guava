begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
comment|/**  * A multiset which maintains the ordering of its elements, according to either  * their natural order or an explicit {@link Comparator}. In all cases, this  * implementation uses {@link Comparable#compareTo} or {@link  * Comparator#compare} instead of {@link Object#equals} to determine  * equivalence of instances.  *  *<p><b>Warning:</b> The comparison must be<i>consistent with equals</i> as  * explained by the {@link Comparable} class specification. Otherwise, the  * resulting multiset will violate the {@link Collection} contract, which it is  * specified in terms of {@link Object#equals}.  *  * @author Neal Kanodia  * @author Jared Levy  * @since Guava release 02 (imported from Google Collections Library)  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
comment|// we're overriding default serialization
DECL|class|TreeMultiset
specifier|public
specifier|final
class|class
name|TreeMultiset
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractMapBasedMultiset
argument_list|<
name|E
argument_list|>
block|{
comment|/**    * Creates a new, empty multiset, sorted according to the elements' natural    * order. All elements inserted into the multiset must implement the    * {@code Comparable} interface. Furthermore, all such elements must be    *<i>mutually comparable</i>: {@code e1.compareTo(e2)} must not throw a    * {@code ClassCastException} for any elements {@code e1} and {@code e2} in    * the multiset. If the user attempts to add an element to the multiset that    * violates this constraint (for example, the user attempts to add a string    * element to a set whose elements are integers), the {@code add(Object)}    * call will throw a {@code ClassCastException}.    *    *<p>The type specification is {@code<E extends Comparable>}, instead of the    * more specific {@code<E extends Comparable<? super E>>}, to support    * classes defined without generics.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// eclipse doesn't like the raw Comparable
DECL|method|create ()
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|TreeMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|TreeMultiset
argument_list|<
name|E
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates a new, empty multiset, sorted according to the specified    * comparator. All elements inserted into the multiset must be<i>mutually    * comparable</i> by the specified comparator: {@code comparator.compare(e1,    * e2)} must not throw a {@code ClassCastException} for any elements {@code    * e1} and {@code e2} in the multiset. If the user attempts to add an element    * to the multiset that violates this constraint, the {@code add(Object)} call    * will throw a {@code ClassCastException}.    *    * @param comparator the comparator that will be used to sort this multiset. A    *     null value indicates that the elements'<i>natural ordering</i> should    *     be used.    */
DECL|method|create (Comparator<? super E> comparator)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|TreeMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
return|return
operator|new
name|TreeMultiset
argument_list|<
name|E
argument_list|>
argument_list|(
name|comparator
argument_list|)
return|;
block|}
comment|/**    * Creates an empty multiset containing the given initial elements, sorted    * according to the elements' natural order.    *    *<p>The type specification is {@code<E extends Comparable>}, instead of the    * more specific {@code<E extends Comparable<? super E>>}, to support    * classes defined without generics.    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// eclipse doesn't like the raw Comparable
DECL|method|create ( Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Comparable
parameter_list|>
name|TreeMultiset
argument_list|<
name|E
argument_list|>
name|create
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elements
parameter_list|)
block|{
name|TreeMultiset
argument_list|<
name|E
argument_list|>
name|multiset
init|=
name|create
argument_list|()
decl_stmt|;
name|Iterables
operator|.
name|addAll
argument_list|(
name|multiset
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|multiset
return|;
block|}
DECL|method|TreeMultiset ()
specifier|private
name|TreeMultiset
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|TreeMultiset (Comparator<? super E> comparator)
specifier|private
name|TreeMultiset
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|(
name|comparator
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * {@inheritDoc}    *    *<p>In {@code TreeMultiset}, the return type of this method is narrowed    * from {@link Set} to {@link SortedSet}.    */
DECL|method|elementSet ()
annotation|@
name|Override
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|elementSet
parameter_list|()
block|{
return|return
operator|(
name|SortedSet
argument_list|<
name|E
argument_list|>
operator|)
name|super
operator|.
name|elementSet
argument_list|()
return|;
block|}
DECL|method|count (@ullable Object element)
annotation|@
name|Override
specifier|public
name|int
name|count
parameter_list|(
annotation|@
name|Nullable
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|super
operator|.
name|count
argument_list|(
name|element
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|createElementSet ()
annotation|@
name|Override
name|Set
argument_list|<
name|E
argument_list|>
name|createElementSet
parameter_list|()
block|{
return|return
operator|new
name|SortedMapBasedElementSet
argument_list|(
operator|(
name|SortedMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
operator|)
name|backingMap
argument_list|()
argument_list|)
return|;
block|}
DECL|class|SortedMapBasedElementSet
specifier|private
class|class
name|SortedMapBasedElementSet
extends|extends
name|MapBasedElementSet
implements|implements
name|SortedSet
argument_list|<
name|E
argument_list|>
block|{
DECL|method|SortedMapBasedElementSet (SortedMap<E, AtomicInteger> map)
name|SortedMapBasedElementSet
parameter_list|(
name|SortedMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
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
DECL|method|sortedMap ()
name|SortedMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
name|sortedMap
parameter_list|()
block|{
return|return
operator|(
name|SortedMap
argument_list|<
name|E
argument_list|,
name|AtomicInteger
argument_list|>
operator|)
name|getMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|comparator ()
specifier|public
name|Comparator
argument_list|<
name|?
super|super
name|E
argument_list|>
name|comparator
parameter_list|()
block|{
return|return
name|sortedMap
argument_list|()
operator|.
name|comparator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|first ()
specifier|public
name|E
name|first
parameter_list|()
block|{
return|return
name|sortedMap
argument_list|()
operator|.
name|firstKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|last ()
specifier|public
name|E
name|last
parameter_list|()
block|{
return|return
name|sortedMap
argument_list|()
operator|.
name|lastKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|headSet (E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|headSet
parameter_list|(
name|E
name|toElement
parameter_list|)
block|{
return|return
operator|new
name|SortedMapBasedElementSet
argument_list|(
name|sortedMap
argument_list|()
operator|.
name|headMap
argument_list|(
name|toElement
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|subSet (E fromElement, E toElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|subSet
parameter_list|(
name|E
name|fromElement
parameter_list|,
name|E
name|toElement
parameter_list|)
block|{
return|return
operator|new
name|SortedMapBasedElementSet
argument_list|(
name|sortedMap
argument_list|()
operator|.
name|subMap
argument_list|(
name|fromElement
argument_list|,
name|toElement
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|tailSet (E fromElement)
specifier|public
name|SortedSet
argument_list|<
name|E
argument_list|>
name|tailSet
parameter_list|(
name|E
name|fromElement
parameter_list|)
block|{
return|return
operator|new
name|SortedMapBasedElementSet
argument_list|(
name|sortedMap
argument_list|()
operator|.
name|tailMap
argument_list|(
name|fromElement
argument_list|)
argument_list|)
return|;
block|}
DECL|method|remove (Object element)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|element
parameter_list|)
block|{
try|try
block|{
return|return
name|super
operator|.
name|remove
argument_list|(
name|element
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
comment|/*    * TODO(jlevy): Decide whether entrySet() should return entries with an    * equals() method that calls the comparator to compare the two keys. If that    * change is made, AbstractMultiset.equals() can simply check whether two    * multisets have equal entry sets.    */
block|}
end_class

end_unit

