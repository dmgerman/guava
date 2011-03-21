begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * diOBJECTibuted under the License is diOBJECTibuted on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|ImmutableSet
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
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|testing
operator|.
name|util
operator|.
name|EqualsTester
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
name|List
import|;
end_import

begin_comment
comment|/**  * Unit test for {@link Equivalences}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|EquivalencesTest
specifier|public
class|class
name|EquivalencesTest
extends|extends
name|TestCase
block|{
DECL|field|OBJECT
specifier|private
specifier|static
specifier|final
name|Object
name|OBJECT
init|=
literal|42
decl_stmt|;
DECL|method|testEqualsEquivalent ()
specifier|public
name|void
name|testEqualsEquivalent
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
name|OBJECT
argument_list|,
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
operator|(
literal|42
operator|)
argument_list|,
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
name|OBJECT
argument_list|,
operator|(
literal|42
operator|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
name|OBJECT
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualsHash ()
specifier|public
name|void
name|testEqualsHash
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|OBJECT
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|hash
argument_list|(
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Equivalences
operator|.
name|equals
argument_list|()
operator|.
name|hash
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIdentityEquivalent ()
specifier|public
name|void
name|testIdentityEquivalent
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
name|OBJECT
argument_list|,
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|12L
argument_list|,
literal|12L
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
name|OBJECT
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
literal|12L
argument_list|,
operator|new
name|Long
argument_list|(
literal|12L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|equivalent
argument_list|(
operator|new
name|Long
argument_list|(
literal|12L
argument_list|)
argument_list|,
literal|12L
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIdentityHash ()
specifier|public
name|void
name|testIdentityHash
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|OBJECT
argument_list|)
argument_list|,
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|hash
argument_list|(
name|OBJECT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|Equivalences
operator|.
name|identity
argument_list|()
operator|.
name|hash
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPairwiseEquivalent_equivalent ()
specifier|public
name|void
name|testPairwiseEquivalent_equivalent
parameter_list|()
block|{
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|pairwise
init|=
name|Equivalences
operator|.
name|pairwise
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|empty
init|=
name|ImmutableList
operator|.
name|of
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|a
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|b
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ab
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|empty
argument_list|,
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|a
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|b
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|ab
argument_list|,
name|ab
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPairwiseEquivalent_nonEquivalent ()
specifier|public
name|void
name|testPairwiseEquivalent_nonEquivalent
parameter_list|()
block|{
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|pairwise
init|=
name|Equivalences
operator|.
name|pairwise
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|empty
init|=
name|ImmutableList
operator|.
name|of
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|a
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|b
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|ab
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|empty
argument_list|,
name|ab
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|a
argument_list|,
name|ab
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|b
argument_list|,
name|ab
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|b
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|ab
argument_list|,
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|ab
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|ab
argument_list|,
name|b
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPairwiseEquivalent_null ()
specifier|public
name|void
name|testPairwiseEquivalent_null
parameter_list|()
block|{
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|pairwise
init|=
name|Equivalences
operator|.
name|pairwise
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|empty
init|=
name|ImmutableList
operator|.
name|of
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|a
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
name|empty
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|empty
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
literal|null
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|pairwise
operator|.
name|equivalent
argument_list|(
name|a
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testPairwiseHash ()
specifier|public
name|void
name|testPairwiseHash
parameter_list|()
block|{
name|Equivalence
argument_list|<
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|>
name|pairwise
init|=
name|Equivalences
operator|.
name|pairwise
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|pairwise
operator|.
name|hash
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pairwise
operator|.
name|hash
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|,
name|pairwise
operator|.
name|hash
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pairwise
operator|.
name|hash
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
argument_list|,
name|pairwise
operator|.
name|hash
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|field|LENGTH_EQUIVALENCE
specifier|private
specifier|static
specifier|final
name|Equivalence
argument_list|<
name|String
argument_list|>
name|LENGTH_EQUIVALENCE
init|=
operator|new
name|Equivalence
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|equivalent
parameter_list|(
name|String
name|a
parameter_list|,
name|String
name|b
parameter_list|)
block|{
return|return
operator|(
name|a
operator|==
literal|null
operator|)
condition|?
operator|(
name|b
operator|==
literal|null
operator|)
else|:
operator|(
name|b
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|a
operator|.
name|length
argument_list|()
operator|==
name|b
operator|.
name|length
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hash
parameter_list|(
name|String
name|t
parameter_list|)
block|{
return|return
operator|(
name|t
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|t
operator|.
name|length
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|testWrap ()
specifier|public
name|void
name|testWrap
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|"hello"
argument_list|)
argument_list|,
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|"hello"
argument_list|)
argument_list|,
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|"world"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|"hi"
argument_list|)
argument_list|,
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|"yo"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Equivalences
operator|.
name|wrap
argument_list|(
name|LENGTH_EQUIVALENCE
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|wrap
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|,
literal|"hello"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|wrap
argument_list|(
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

