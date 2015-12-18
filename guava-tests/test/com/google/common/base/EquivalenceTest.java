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
name|annotations
operator|.
name|GwtIncompatible
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
name|Equivalence
operator|.
name|Wrapper
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
name|testing
operator|.
name|EqualsTester
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
name|testing
operator|.
name|EquivalenceTester
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
name|testing
operator|.
name|NullPointerTester
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
name|testing
operator|.
name|SerializableTester
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
comment|/**  * Unit test for {@link Equivalence}.  *  * @author Jige Yu  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EquivalenceTest
specifier|public
class|class
name|EquivalenceTest
extends|extends
name|TestCase
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// varargs
DECL|method|testPairwiseEquivalent ()
specifier|public
name|void
name|testPairwiseEquivalent
parameter_list|()
block|{
name|EquivalenceTester
operator|.
name|of
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
operator|<
name|String
operator|>
name|pairwise
argument_list|()
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"b"
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testPairwiseEquivalent_equals ()
specifier|public
name|void
name|testPairwiseEquivalent_equals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|pairwise
argument_list|()
argument_list|,
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|pairwise
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
operator|.
name|pairwise
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|enum|LengthFunction
specifier|private
enum|enum
name|LengthFunction
implements|implements
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
DECL|method|apply (String input)
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|length
argument_list|()
return|;
block|}
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
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|LengthFunction
operator|.
name|INSTANCE
argument_list|)
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
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"hello"
argument_list|)
argument_list|,
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"hello"
argument_list|)
argument_list|,
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"world"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"hi"
argument_list|)
argument_list|,
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"yo"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|null
argument_list|)
argument_list|,
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|wrap
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|wrap
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testWrap_get ()
specifier|public
name|void
name|testWrap_get
parameter_list|()
block|{
name|String
name|test
init|=
literal|"test"
decl_stmt|;
name|Wrapper
argument_list|<
name|String
argument_list|>
name|wrapper
init|=
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
name|test
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|test
argument_list|,
name|wrapper
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testSerialization ()
specifier|public
name|void
name|testSerialization
parameter_list|()
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|LENGTH_EQUIVALENCE
operator|.
name|wrap
argument_list|(
literal|"hello"
argument_list|)
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|IntValue
specifier|private
specifier|static
class|class
name|IntValue
block|{
DECL|field|value
specifier|private
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|IntValue (int value)
name|IntValue
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"value = "
operator|+
name|value
return|;
block|}
block|}
DECL|method|testOnResultOf ()
specifier|public
name|void
name|testOnResultOf
parameter_list|()
block|{
name|EquivalenceTester
operator|.
name|of
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
operator|new
name|IntValue
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|IntValue
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
operator|new
name|IntValue
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testOnResultOf_equals ()
specifier|public
name|void
name|testOnResultOf_equals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
argument_list|,
name|Equivalence
operator|.
name|identity
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
operator|.
name|onResultOf
argument_list|(
name|Functions
operator|.
name|identity
argument_list|()
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquivalentTo ()
specifier|public
name|void
name|testEquivalentTo
parameter_list|()
block|{
name|Predicate
argument_list|<
name|Object
argument_list|>
name|equalTo1
init|=
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|equivalentTo
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|equalTo1
operator|.
name|apply
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|equalTo1
operator|.
name|apply
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|equalTo1
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Predicate
argument_list|<
name|Object
argument_list|>
name|isNull
init|=
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|equivalentTo
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|isNull
operator|.
name|apply
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|isNull
operator|.
name|apply
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|isNull
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|equalTo1
argument_list|,
name|Equivalence
operator|.
name|equals
argument_list|()
operator|.
name|equivalentTo
argument_list|(
literal|"1"
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|isNull
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
operator|.
name|equivalentTo
argument_list|(
literal|"1"
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testEqualsEquivalent ()
specifier|public
name|void
name|testEqualsEquivalent
parameter_list|()
block|{
name|EquivalenceTester
operator|.
name|of
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|,
literal|42
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
literal|"a"
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testIdentityEquivalent ()
specifier|public
name|void
name|testIdentityEquivalent
parameter_list|()
block|{
name|EquivalenceTester
operator|.
name|of
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
operator|new
name|Integer
argument_list|(
literal|42
argument_list|)
argument_list|)
operator|.
name|addEquivalenceGroup
argument_list|(
literal|"a"
argument_list|)
operator|.
name|test
argument_list|()
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|,
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
argument_list|,
name|Equivalence
operator|.
name|identity
argument_list|()
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Equivalence
operator|.
name|class
argument_list|)
expr_stmt|;
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|Equivalence
operator|.
name|equals
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|NullPointerTester
argument_list|()
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|Equivalence
operator|.
name|identity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

