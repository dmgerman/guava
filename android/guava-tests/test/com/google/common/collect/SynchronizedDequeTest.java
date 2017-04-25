begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Deque
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link Synchronized#deque} and {@link Queues#synchronizedDeque}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
DECL|class|SynchronizedDequeTest
specifier|public
class|class
name|SynchronizedDequeTest
extends|extends
name|TestCase
block|{
DECL|method|create ()
specifier|protected
name|Deque
argument_list|<
name|String
argument_list|>
name|create
parameter_list|()
block|{
name|TestDeque
argument_list|<
name|String
argument_list|>
name|inner
init|=
operator|new
name|TestDeque
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|String
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|deque
argument_list|(
name|inner
argument_list|,
name|inner
operator|.
name|mutex
argument_list|)
decl_stmt|;
name|outer
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|// necessary because we try to remove elements later on
return|return
name|outer
return|;
block|}
DECL|class|TestDeque
specifier|private
specifier|static
specifier|final
class|class
name|TestDeque
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Deque
argument_list|<
name|E
argument_list|>
block|{
DECL|field|delegate
specifier|private
specifier|final
name|Deque
argument_list|<
name|E
argument_list|>
name|delegate
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// something Serializable
annotation|@
name|Override
DECL|method|offer (E o)
specifier|public
name|boolean
name|offer
parameter_list|(
name|E
name|o
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|offer
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|public
name|E
name|poll
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|poll
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|E
name|remove
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|remove
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|peek ()
specifier|public
name|E
name|peek
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|peek
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|element ()
specifier|public
name|E
name|element
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|element
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|iterator
parameter_list|()
block|{
comment|// We explicitly don't lock for iterator()
name|assertFalse
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|removeAll (Collection<?> collection)
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|removeAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|contains (Object object)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|contains
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|add (E element)
specifier|public
name|boolean
name|add
parameter_list|(
name|E
name|element
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|add
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|remove (Object object)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|remove
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAll (Collection<?> collection)
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|containsAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addAll (Collection<? extends E> collection)
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|addAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|retainAll (Collection<?> collection)
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|retainAll
argument_list|(
name|collection
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toArray ()
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|toArray
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toArray (T[] array)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|toArray
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addFirst (E e)
specifier|public
name|void
name|addFirst
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addFirst
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addLast (E e)
specifier|public
name|void
name|addLast
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|addLast
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|offerFirst (E e)
specifier|public
name|boolean
name|offerFirst
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|offerFirst
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|offerLast (E e)
specifier|public
name|boolean
name|offerLast
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|offerLast
argument_list|(
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeFirst ()
specifier|public
name|E
name|removeFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|removeFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|removeLast ()
specifier|public
name|E
name|removeLast
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|removeLast
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollFirst ()
specifier|public
name|E
name|pollFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|pollFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|pollLast ()
specifier|public
name|E
name|pollLast
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|pollLast
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getFirst ()
specifier|public
name|E
name|getFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|getFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLast ()
specifier|public
name|E
name|getLast
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|getLast
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|peekFirst ()
specifier|public
name|E
name|peekFirst
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|peekFirst
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|peekLast ()
specifier|public
name|E
name|peekLast
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|peekLast
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|removeFirstOccurrence (Object o)
specifier|public
name|boolean
name|removeFirstOccurrence
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|removeFirstOccurrence
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|removeLastOccurrence (Object o)
specifier|public
name|boolean
name|removeLastOccurrence
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|removeLastOccurrence
argument_list|(
name|o
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|push (E e)
specifier|public
name|void
name|push
parameter_list|(
name|E
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|push
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pop ()
specifier|public
name|E
name|pop
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|pop
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|descendingIterator ()
specifier|public
name|Iterator
argument_list|<
name|E
argument_list|>
name|descendingIterator
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Thread
operator|.
name|holdsLock
argument_list|(
name|mutex
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|delegate
operator|.
name|descendingIterator
argument_list|()
return|;
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
DECL|method|testHoldsLockOnAllOperations ()
specifier|public
name|void
name|testHoldsLockOnAllOperations
parameter_list|()
block|{
name|create
argument_list|()
operator|.
name|element
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|offer
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|peek
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|poll
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|remove
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|addAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|contains
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|containsAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|equals
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|remove
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|removeAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|retainAll
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|toArray
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"foo"
block|}
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|addFirst
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|offerFirst
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|offerLast
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|removeFirst
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|removeLast
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|pollFirst
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|pollLast
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|getFirst
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|getLast
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|peekFirst
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|peekLast
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|removeFirstOccurrence
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|removeLastOccurrence
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|push
argument_list|(
literal|"e"
argument_list|)
expr_stmt|;
name|create
argument_list|()
operator|.
name|pop
argument_list|()
expr_stmt|;
name|create
argument_list|()
operator|.
name|descendingIterator
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
