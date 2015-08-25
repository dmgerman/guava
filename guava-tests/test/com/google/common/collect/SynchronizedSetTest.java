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
name|collect
operator|.
name|testing
operator|.
name|SetTestSuiteBuilder
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
name|collect
operator|.
name|testing
operator|.
name|TestStringSetGenerator
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|HashSet
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
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Tests for {@code Synchronized#set}.  *  * @author Mike Bostock  */
end_comment

begin_class
DECL|class|SynchronizedSetTest
specifier|public
class|class
name|SynchronizedSetTest
extends|extends
name|TestCase
block|{
DECL|field|MUTEX
specifier|public
specifier|static
specifier|final
name|Object
name|MUTEX
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// something Serializable
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
name|SetTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestStringSetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|TestSet
argument_list|<
name|String
argument_list|>
name|inner
init|=
operator|new
name|TestSet
argument_list|<
name|String
argument_list|>
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|,
name|MUTEX
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|outer
init|=
name|Synchronized
operator|.
name|set
argument_list|(
name|inner
argument_list|,
name|inner
operator|.
name|mutex
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|addAll
argument_list|(
name|outer
argument_list|,
name|elements
argument_list|)
expr_stmt|;
return|return
name|outer
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"Synchronized.set"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
operator|.
name|createTestSuite
argument_list|()
return|;
block|}
DECL|class|TestSet
specifier|static
class|class
name|TestSet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ForwardingSet
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
decl_stmt|;
DECL|field|mutex
specifier|public
specifier|final
name|Object
name|mutex
decl_stmt|;
DECL|method|TestSet (Set<E> delegate, Object mutex)
specifier|public
name|TestSet
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|,
name|Object
name|mutex
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|mutex
argument_list|)
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|mutex
operator|=
name|mutex
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Set
argument_list|<
name|E
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
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
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|equals (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
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
name|super
operator|.
name|equals
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
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
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|add (@ullable E o)
annotation|@
name|Override
specifier|public
name|boolean
name|add
parameter_list|(
annotation|@
name|Nullable
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
name|super
operator|.
name|add
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|addAll (Collection<? extends E> c)
annotation|@
name|Override
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
name|c
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
name|super
operator|.
name|addAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|clear ()
annotation|@
name|Override
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
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|contains (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|Nullable
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
name|super
operator|.
name|contains
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|containsAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
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
name|super
operator|.
name|containsAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
annotation|@
name|Override
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
name|super
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/* Don't test iterator(); it may or may not hold the mutex. */
DECL|method|remove (@ullable Object o)
annotation|@
name|Override
specifier|public
name|boolean
name|remove
parameter_list|(
annotation|@
name|Nullable
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
name|super
operator|.
name|remove
argument_list|(
name|o
argument_list|)
return|;
block|}
DECL|method|removeAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
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
name|super
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|retainAll (Collection<?> c)
annotation|@
name|Override
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|c
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
name|super
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
return|;
block|}
DECL|method|size ()
annotation|@
name|Override
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
name|super
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|toArray ()
annotation|@
name|Override
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
name|super
operator|.
name|toArray
argument_list|()
return|;
block|}
DECL|method|toArray (T[] a)
annotation|@
name|Override
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
name|a
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
name|super
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
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
block|}
end_class

end_unit

