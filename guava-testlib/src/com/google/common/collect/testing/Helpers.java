begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|sort
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertTrue
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
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|Map
operator|.
name|Entry
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
name|junit
operator|.
name|framework
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|AssertionFailedError
import|;
end_import

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Helpers
specifier|public
class|class
name|Helpers
block|{
comment|// Clone of Objects.equal
DECL|method|equal (Object a, Object b)
specifier|static
name|boolean
name|equal
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
operator|||
operator|(
name|a
operator|!=
literal|null
operator|&&
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
operator|)
return|;
block|}
comment|// Clone of Lists.newArrayList
DECL|method|copyToList (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|copyToList
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
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|addAll
argument_list|(
name|list
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
DECL|method|copyToList (E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|copyToList
parameter_list|(
name|E
index|[]
name|elements
parameter_list|)
block|{
return|return
name|copyToList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
comment|// Clone of Sets.newLinkedHashSet
DECL|method|copyToSet (Iterable<? extends E> elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyToSet
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
name|Set
argument_list|<
name|E
argument_list|>
name|set
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|addAll
argument_list|(
name|set
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
DECL|method|copyToSet (E[] elements)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|copyToSet
parameter_list|(
name|E
index|[]
name|elements
parameter_list|)
block|{
return|return
name|copyToSet
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
comment|// Would use Maps.immutableEntry
DECL|method|mapEntry (K key, V value)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|mapEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|singletonMap
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|isEmpty (Iterable<?> iterable)
specifier|private
specifier|static
name|boolean
name|isEmpty
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|iterable
operator|instanceof
name|Collection
condition|?
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|iterable
operator|)
operator|.
name|isEmpty
argument_list|()
else|:
operator|!
name|iterable
operator|.
name|iterator
argument_list|()
operator|.
name|hasNext
argument_list|()
return|;
block|}
DECL|method|assertEmpty (Iterable<?> iterable)
specifier|public
specifier|static
name|void
name|assertEmpty
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|iterable
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isEmpty
argument_list|(
name|iterable
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Not true that "
operator|+
name|iterable
operator|+
literal|" is empty"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertEmpty (Map<?, ?> map)
specifier|public
specifier|static
name|void
name|assertEmpty
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Not true that "
operator|+
name|map
operator|+
literal|" is empty"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertEqualInOrder (Iterable<?> expected, Iterable<?> actual)
specifier|public
specifier|static
name|void
name|assertEqualInOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|expected
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|expectedIter
init|=
name|expected
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|actualIter
init|=
name|actual
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|expectedIter
operator|.
name|hasNext
argument_list|()
operator|&&
name|actualIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|equal
argument_list|(
name|expectedIter
operator|.
name|next
argument_list|()
argument_list|,
name|actualIter
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"contents were not equal and in the same order: "
operator|+
literal|"expected = "
operator|+
name|expected
operator|+
literal|", actual = "
operator|+
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|expectedIter
operator|.
name|hasNext
argument_list|()
operator|||
name|actualIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// actual either had too few or too many elements
name|Assert
operator|.
name|fail
argument_list|(
literal|"contents were not equal and in the same order: "
operator|+
literal|"expected = "
operator|+
name|expected
operator|+
literal|", actual = "
operator|+
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertContentsInOrder (Iterable<?> actual, Object... expected)
specifier|public
specifier|static
name|void
name|assertContentsInOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|,
name|Object
modifier|...
name|expected
parameter_list|)
block|{
name|assertEqualInOrder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEqualIgnoringOrder (Iterable<?> expected, Iterable<?> actual)
specifier|public
specifier|static
name|void
name|assertEqualIgnoringOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|expected
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|exp
init|=
name|copyToList
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|act
init|=
name|copyToList
argument_list|(
name|actual
argument_list|)
decl_stmt|;
name|String
name|actString
init|=
name|act
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// Of course we could take pains to give the complete description of the
comment|// problem on any failure.
comment|// Yeah it's n^2.
for|for
control|(
name|Object
name|object
range|:
name|exp
control|)
block|{
if|if
condition|(
operator|!
name|act
operator|.
name|remove
argument_list|(
name|object
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"did not contain expected element "
operator|+
name|object
operator|+
literal|", "
operator|+
literal|"expected = "
operator|+
name|exp
operator|+
literal|", actual = "
operator|+
name|actString
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"unexpected elements: "
operator|+
name|act
argument_list|,
name|act
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertContentsAnyOrder (Iterable<?> actual, Object... expected)
specifier|public
specifier|static
name|void
name|assertContentsAnyOrder
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|,
name|Object
modifier|...
name|expected
parameter_list|)
block|{
name|assertEqualIgnoringOrder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|assertContains (Iterable<?> actual, Object expected)
specifier|public
specifier|static
name|void
name|assertContains
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|,
name|Object
name|expected
parameter_list|)
block|{
name|boolean
name|contained
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|actual
operator|instanceof
name|Collection
condition|)
block|{
name|contained
operator|=
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|actual
operator|)
operator|.
name|contains
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|Object
name|o
range|:
name|actual
control|)
block|{
if|if
condition|(
name|equal
argument_list|(
name|o
argument_list|,
name|expected
argument_list|)
condition|)
block|{
name|contained
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|contained
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Not true that "
operator|+
name|actual
operator|+
literal|" contains "
operator|+
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertContainsAllOf (Iterable<?> actual, Object... expected)
specifier|public
specifier|static
name|void
name|assertContainsAllOf
parameter_list|(
name|Iterable
argument_list|<
name|?
argument_list|>
name|actual
parameter_list|,
name|Object
modifier|...
name|expected
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|expectedList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|actual
control|)
block|{
name|expectedList
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|expectedList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Assert
operator|.
name|fail
argument_list|(
literal|"Not true that "
operator|+
name|actual
operator|+
literal|" contains all of "
operator|+
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addAll (Collection<E> addTo, Iterable<? extends E> elementsToAdd)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|addTo
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|elementsToAdd
parameter_list|)
block|{
name|boolean
name|modified
init|=
literal|false
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|elementsToAdd
control|)
block|{
name|modified
operator||=
name|addTo
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|modified
return|;
block|}
DECL|method|reverse (List<T> list)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|T
argument_list|>
name|reverse
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{
return|return
operator|new
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
block|{
name|ListIterator
argument_list|<
name|T
argument_list|>
name|listIter
init|=
name|list
operator|.
name|listIterator
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|listIter
operator|.
name|hasPrevious
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|next
parameter_list|()
block|{
return|return
name|listIter
operator|.
name|previous
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|listIter
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
return|;
block|}
DECL|method|cycle (Iterable<T> iterable)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|cycle
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
init|=
name|Collections
operator|.
expr|<
name|T
operator|>
name|emptySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|iterator
operator|=
name|iterable
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
return|return
name|iterator
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
return|;
block|}
DECL|method|get (Iterator<T> iterator, int position)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|,
name|int
name|position
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|position
condition|;
name|i
operator|++
control|)
block|{
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|iterator
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|fail (Throwable cause, Object message)
specifier|static
name|void
name|fail
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Object
name|message
parameter_list|)
block|{
name|AssertionFailedError
name|assertionFailedError
init|=
operator|new
name|AssertionFailedError
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|assertionFailedError
operator|.
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
throw|throw
name|assertionFailedError
throw|;
block|}
DECL|method|entryComparator ( Comparator<? super K> keyComparator)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Comparator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryComparator
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|K
argument_list|>
name|keyComparator
parameter_list|)
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// no less safe than putting it in the map!
specifier|public
name|int
name|compare
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|a
parameter_list|,
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|b
parameter_list|)
block|{
return|return
operator|(
name|keyComparator
operator|==
literal|null
operator|)
condition|?
operator|(
operator|(
name|Comparable
operator|)
name|a
operator|.
name|getKey
argument_list|()
operator|)
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
else|:
name|keyComparator
operator|.
name|compare
argument_list|(
name|a
operator|.
name|getKey
argument_list|()
argument_list|,
name|b
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Asserts that all pairs of {@code T} values within {@code valuesInExpectedOrder} are ordered    * consistently between their order within {@code valuesInExpectedOrder} and the order implied by    * the given {@code comparator}.    *    * @see #testComparator(Comparator, List)    */
DECL|method|testComparator ( Comparator<? super T> comparator, T... valuesInExpectedOrder)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testComparator
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
name|T
modifier|...
name|valuesInExpectedOrder
parameter_list|)
block|{
name|testComparator
argument_list|(
name|comparator
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|valuesInExpectedOrder
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that all pairs of {@code T} values within {@code valuesInExpectedOrder} are ordered    * consistently between their order within {@code valuesInExpectedOrder} and the order implied by    * the given {@code comparator}.    *    *<p>In detail, this method asserts    *    *<ul>    *<li><i>reflexivity</i>: {@code comparator.compare(t, t) = 0} for all {@code t} in {@code    *       valuesInExpectedOrder}; and    *<li><i>consistency</i>: {@code comparator.compare(ti, tj)< 0} and {@code    *       comparator.compare(tj, ti)> 0} for {@code i< j}, where {@code ti =    *       valuesInExpectedOrder.get(i)} and {@code tj = valuesInExpectedOrder.get(j)}.    *</ul>    */
DECL|method|testComparator ( Comparator<? super T> comparator, List<T> valuesInExpectedOrder)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|testComparator
parameter_list|(
name|Comparator
argument_list|<
name|?
super|super
name|T
argument_list|>
name|comparator
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|valuesInExpectedOrder
parameter_list|)
block|{
comment|// This does an O(n^2) test of all pairs of values in both orders
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|valuesInExpectedOrder
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|T
name|t
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
name|j
operator|++
control|)
block|{
name|T
name|lesser
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|comparator
operator|+
literal|".compare("
operator|+
name|lesser
operator|+
literal|", "
operator|+
name|t
operator|+
literal|")"
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|lesser
argument_list|,
name|t
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|comparator
operator|+
literal|".compare("
operator|+
name|t
operator|+
literal|", "
operator|+
name|t
operator|+
literal|")"
argument_list|,
literal|0
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|t
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<
name|valuesInExpectedOrder
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|T
name|greater
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|comparator
operator|+
literal|".compare("
operator|+
name|greater
operator|+
literal|", "
operator|+
name|t
operator|+
literal|")"
argument_list|,
name|comparator
operator|.
name|compare
argument_list|(
name|greater
argument_list|,
name|t
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"SelfComparison"
block|,
literal|"SelfEquals"
block|}
argument_list|)
DECL|method|testCompareToAndEquals ( List<T> valuesInExpectedOrder)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|T
argument_list|>
parameter_list|>
name|void
name|testCompareToAndEquals
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|valuesInExpectedOrder
parameter_list|)
block|{
comment|// This does an O(n^2) test of all pairs of values in both orders
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|valuesInExpectedOrder
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|T
name|t
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|i
condition|;
name|j
operator|++
control|)
block|{
name|T
name|lesser
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|lesser
operator|+
literal|".compareTo("
operator|+
name|t
operator|+
literal|')'
argument_list|,
name|lesser
operator|.
name|compareTo
argument_list|(
name|t
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|lesser
operator|.
name|equals
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|t
operator|+
literal|".compareTo("
operator|+
name|t
operator|+
literal|')'
argument_list|,
literal|0
argument_list|,
name|t
operator|.
name|compareTo
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t
operator|.
name|equals
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
name|i
operator|+
literal|1
init|;
name|j
operator|<
name|valuesInExpectedOrder
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|T
name|greater
init|=
name|valuesInExpectedOrder
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|greater
operator|+
literal|".compareTo("
operator|+
name|t
operator|+
literal|')'
argument_list|,
name|greater
operator|.
name|compareTo
argument_list|(
name|t
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|greater
operator|.
name|equals
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Returns a collection that simulates concurrent modification by having its size method return    * incorrect values. This is useful for testing methods that must treat the return value from    * size() as a hint only.    *    * @param delta the difference between the true size of the collection and the values returned by    *     the size method    */
DECL|method|misleadingSizeCollection (int delta)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|misleadingSizeCollection
parameter_list|(
name|int
name|delta
parameter_list|)
block|{
comment|// It would be nice to be able to return a real concurrent
comment|// collection like ConcurrentLinkedQueue, so that e.g. concurrent
comment|// iteration would work, but that would not be GWT-compatible.
return|return
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|super
operator|.
name|size
argument_list|()
operator|+
name|delta
argument_list|)
return|;
block|}
block|}
return|;
block|}
comment|/**    * Returns a "nefarious" map entry with the specified key and value, meaning an entry that is    * suitable for testing that map entries cannot be modified via a nefarious implementation of    * equals. This is used for testing unmodifiable collections of map entries; for example, it    * should not be possible to access the raw (modifiable) map entry via a nefarious equals method.    */
DECL|method|nefariousMapEntry (K key, V value)
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|nefariousMapEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
return|return
operator|new
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|K
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
specifier|public
name|V
name|setValue
parameter_list|(
name|V
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|Entry
condition|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|e
init|=
operator|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|o
decl_stmt|;
name|e
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// muhahaha!
return|return
name|equal
argument_list|(
name|this
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|equal
argument_list|(
name|this
operator|.
name|getValue
argument_list|()
argument_list|,
name|e
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
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|K
name|k
init|=
name|getKey
argument_list|()
decl_stmt|;
name|V
name|v
init|=
name|getValue
argument_list|()
decl_stmt|;
return|return
operator|(
operator|(
name|k
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|k
operator|.
name|hashCode
argument_list|()
operator|)
operator|^
operator|(
operator|(
name|v
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|v
operator|.
name|hashCode
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|getValue
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|castOrCopyToList (Iterable<E> iterable)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|E
argument_list|>
name|castOrCopyToList
parameter_list|(
name|Iterable
argument_list|<
name|E
argument_list|>
name|iterable
parameter_list|)
block|{
if|if
condition|(
name|iterable
operator|instanceof
name|List
condition|)
block|{
return|return
operator|(
name|List
argument_list|<
name|E
argument_list|>
operator|)
name|iterable
return|;
block|}
name|List
argument_list|<
name|E
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|E
name|e
range|:
name|iterable
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|field|NATURAL_ORDER
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|Comparable
argument_list|>
name|NATURAL_ORDER
init|=
operator|new
name|Comparator
argument_list|<
name|Comparable
argument_list|>
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// assume any Comparable is Comparable<Self>
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|Comparable
name|left
parameter_list|,
name|Comparable
name|right
parameter_list|)
block|{
return|return
name|left
operator|.
name|compareTo
argument_list|(
name|right
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|orderEntriesByKey ( List<Entry<K, V>> insertionOrder)
specifier|public
specifier|static
parameter_list|<
name|K
extends|extends
name|Comparable
parameter_list|,
name|V
parameter_list|>
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|orderEntriesByKey
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
name|sort
argument_list|(
name|insertionOrder
argument_list|,
name|Helpers
operator|.
expr|<
name|K
argument_list|,
name|V
operator|>
name|entryComparator
argument_list|(
name|NATURAL_ORDER
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|insertionOrder
return|;
block|}
comment|/**    * Private replacement for {@link com.google.gwt.user.client.rpc.GwtTransient} to work around    * build-system quirks.    */
DECL|annotation|GwtTransient
specifier|private
annotation_defn|@interface
name|GwtTransient
block|{}
comment|/**    * Compares strings in natural order except that null comes immediately before a given value. This    * works better than Ordering.natural().nullsFirst() because, if null comes before all other    * values, it lies outside the submap/submultiset ranges we test, and the variety of tests that    * exercise null handling fail on those subcollections.    */
DECL|class|NullsBefore
specifier|public
specifier|abstract
specifier|static
class|class
name|NullsBefore
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
implements|,
name|Serializable
block|{
comment|/*      * We don't serialize this class in GWT, so we don't care about whether GWT will serialize this      * field.      */
DECL|field|justAfterNull
annotation|@
name|GwtTransient
specifier|private
specifier|final
name|String
name|justAfterNull
decl_stmt|;
DECL|method|NullsBefore (String justAfterNull)
specifier|protected
name|NullsBefore
parameter_list|(
name|String
name|justAfterNull
parameter_list|)
block|{
if|if
condition|(
name|justAfterNull
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
block|}
name|this
operator|.
name|justAfterNull
operator|=
name|justAfterNull
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|compare (String lhs, String rhs)
specifier|public
name|int
name|compare
parameter_list|(
name|String
name|lhs
parameter_list|,
name|String
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|lhs
operator|==
name|rhs
condition|)
block|{
return|return
literal|0
return|;
block|}
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
comment|// lhs (null) comes just before justAfterNull.
comment|// If rhs is b, lhs comes first.
if|if
condition|(
name|rhs
operator|.
name|equals
argument_list|(
name|justAfterNull
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|justAfterNull
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
return|;
block|}
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
comment|// rhs (null) comes just before justAfterNull.
comment|// If lhs is b, rhs comes first.
if|if
condition|(
name|lhs
operator|.
name|equals
argument_list|(
name|justAfterNull
argument_list|)
condition|)
block|{
return|return
literal|1
return|;
block|}
return|return
name|lhs
operator|.
name|compareTo
argument_list|(
name|justAfterNull
argument_list|)
return|;
block|}
return|return
name|lhs
operator|.
name|compareTo
argument_list|(
name|rhs
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|NullsBefore
condition|)
block|{
name|NullsBefore
name|other
init|=
operator|(
name|NullsBefore
operator|)
name|obj
decl_stmt|;
return|return
name|justAfterNull
operator|.
name|equals
argument_list|(
name|other
operator|.
name|justAfterNull
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|justAfterNull
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
DECL|class|NullsBeforeB
specifier|public
specifier|static
specifier|final
class|class
name|NullsBeforeB
extends|extends
name|NullsBefore
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|NullsBeforeB
name|INSTANCE
init|=
operator|new
name|NullsBeforeB
argument_list|()
decl_stmt|;
DECL|method|NullsBeforeB ()
specifier|private
name|NullsBeforeB
parameter_list|()
block|{
name|super
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|NullsBeforeTwo
specifier|public
specifier|static
specifier|final
class|class
name|NullsBeforeTwo
extends|extends
name|NullsBefore
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|NullsBeforeTwo
name|INSTANCE
init|=
operator|new
name|NullsBeforeTwo
argument_list|()
decl_stmt|;
DECL|method|NullsBeforeTwo ()
specifier|private
name|NullsBeforeTwo
parameter_list|()
block|{
name|super
argument_list|(
literal|"two"
argument_list|)
expr_stmt|;
comment|// from TestStringSortedMapGenerator's sample keys
block|}
block|}
annotation|@
name|GwtIncompatible
comment|// reflection
DECL|method|getMethod (Class<?> clazz, String name)
specifier|public
specifier|static
name|Method
name|getMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|clazz
operator|.
name|getMethod
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

