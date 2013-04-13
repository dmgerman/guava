begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Unit tests for {@link Sets#union}, {@link Sets#intersection} and  * {@link Sets#difference}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SetOperationsTest
specifier|public
class|class
name|SetOperationsTest
extends|extends
name|TestCase
block|{
DECL|class|MoreTests
specifier|public
specifier|static
class|class
name|MoreTests
extends|extends
name|TestCase
block|{
DECL|field|friends
name|Set
argument_list|<
name|String
argument_list|>
name|friends
decl_stmt|;
DECL|field|enemies
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
decl_stmt|;
DECL|method|setUp ()
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|friends
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
expr_stmt|;
name|enemies
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
expr_stmt|;
block|}
DECL|method|testUnion ()
specifier|public
name|void
name|testUnion
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|all
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|all
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|union
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Buck"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|all
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIntersection ()
specifier|public
name|void
name|testIntersection
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|frenemies
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|frenemies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|intersection
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Joe"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|frenemies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifference ()
specifier|public
name|void
name|testDifference
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|goodFriends
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|goodFriends
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|goodFriends
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSymmetricDifference ()
specifier|public
name|void
name|testSymmetricDifference
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|friends
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Tom"
argument_list|,
literal|"Joe"
argument_list|,
literal|"Dave"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|enemies
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
literal|"Dick"
argument_list|,
literal|"Harry"
argument_list|,
literal|"Tom"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|symmetricDifferenceFriendsFirst
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|symmetricDifferenceFriendsFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|symmetricDifferenceEnemiesFirst
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|symmetricDifferenceEnemiesFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|symmetricDifferenceFriendsFirst
argument_list|,
name|symmetricDifferenceEnemiesFirst
argument_list|)
expr_stmt|;
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|immut
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|immutableCopy
argument_list|()
decl_stmt|;
name|HashSet
argument_list|<
name|String
argument_list|>
name|mut
init|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|friends
argument_list|,
name|enemies
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|enemies
operator|.
name|add
argument_list|(
literal|"Dave"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|symmetricDifferenceFriendsFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|immut
operator|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
operator|.
name|immutableCopy
argument_list|()
expr_stmt|;
name|mut
operator|=
name|Sets
operator|.
name|symmetricDifference
argument_list|(
name|enemies
argument_list|,
name|friends
argument_list|)
operator|.
name|copyInto
argument_list|(
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|friends
operator|.
name|add
argument_list|(
literal|"Harry"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|symmetricDifferenceEnemiesFirst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|immut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mut
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

