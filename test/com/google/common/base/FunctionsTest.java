begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2005 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|collect
operator|.
name|ImmutableMap
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
name|Maps
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

begin_comment
comment|/**  * Tests for {@link Functions}.  *  * @author Mike Bostock  * @author Vlad Patryshev  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|FunctionsTest
specifier|public
class|class
name|FunctionsTest
extends|extends
name|TestCase
block|{
DECL|method|testIdentity_same ()
specifier|public
name|void
name|testIdentity_same
parameter_list|()
block|{
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|identity
init|=
name|Functions
operator|.
name|identity
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|identity
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
literal|"foo"
argument_list|,
name|identity
operator|.
name|apply
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIdentity_notSame ()
specifier|public
name|void
name|testIdentity_notSame
parameter_list|()
block|{
name|Function
argument_list|<
name|Long
argument_list|,
name|Long
argument_list|>
name|identity
init|=
name|Functions
operator|.
name|identity
argument_list|()
decl_stmt|;
name|assertNotSame
argument_list|(
operator|new
name|Long
argument_list|(
literal|135135L
argument_list|)
argument_list|,
name|identity
operator|.
name|apply
argument_list|(
operator|new
name|Long
argument_list|(
literal|135135L
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testIdentitySerializable ()
specifier|public
name|void
name|testIdentitySerializable
parameter_list|()
block|{
name|checkCanReserializeSingleton
argument_list|(
name|Functions
operator|.
name|identity
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringFunction_apply ()
specifier|public
name|void
name|testToStringFunction_apply
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"3"
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
operator|.
name|apply
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hiya"
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
operator|.
name|apply
argument_list|(
literal|"hiya"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"I'm a string"
argument_list|,
name|Functions
operator|.
name|toStringFunction
argument_list|()
operator|.
name|apply
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"I'm a string"
return|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Functions
operator|.
name|toStringFunction
argument_list|()
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected NullPointerException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testToStringFunctionSerializable ()
specifier|public
name|void
name|testToStringFunctionSerializable
parameter_list|()
block|{
name|checkCanReserializeSingleton
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullPointerExceptions ()
specifier|public
name|void
name|testNullPointerExceptions
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|tester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|tester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Functions
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testForMapWithoutDefault ()
specifier|public
name|void
name|testForMapWithoutDefault
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Three"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
operator|new
name|EqualsTester
argument_list|(
name|function
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|42
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testForMapWithoutDefaultSerializable ()
specifier|public
name|void
name|testForMapWithoutDefaultSerializable
parameter_list|()
block|{
name|checkCanReserialize
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testForMapWithDefault ()
specifier|public
name|void
name|testForMapWithDefault
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|42
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Three"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|()
operator|.
name|addEqualityGroup
argument_list|(
name|function
argument_list|,
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|42
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|43
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testForMapWithDefault_includeSerializable ()
specifier|public
name|void
name|testForMapWithDefault_includeSerializable
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|42
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Three"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|(
name|function
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|42
argument_list|)
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|function
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|null
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|43
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testForMapWithDefaultSerializable ()
specifier|public
name|void
name|testForMapWithDefaultSerializable
parameter_list|()
block|{
name|checkCanReserialize
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testForMapWithDefault_null ()
specifier|public
name|void
name|testForMapWithDefault_null
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check basic sanity of equals and hashCode
operator|new
name|EqualsTester
argument_list|(
name|function
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testForMapWithDefault_null_compareWithSerializable ()
specifier|public
name|void
name|testForMapWithDefault_null_compareWithSerializable
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|Integer
operator|)
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
comment|// check basic sanity of equals and hashCode
operator|new
name|EqualsTester
argument_list|(
name|function
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|function
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testForMapWildCardWithDefault ()
specifier|public
name|void
name|testForMapWildCardWithDefault
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"One"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"Three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Number
name|number
init|=
name|Double
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Number
argument_list|>
name|function
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|map
argument_list|,
name|number
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"One"
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|number
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|function
operator|.
name|apply
argument_list|(
literal|"Three"
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testComposition ()
specifier|public
name|void
name|testComposition
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mJapaneseToInteger
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"Ichi"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"Ni"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"San"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|japaneseToInteger
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|mJapaneseToInteger
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|mIntegerToSpanish
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"Uno"
argument_list|)
expr_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"Tres"
argument_list|)
expr_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"Cuatro"
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|integerToSpanish
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|mIntegerToSpanish
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|japaneseToSpanish
init|=
name|Functions
operator|.
name|compose
argument_list|(
name|integerToSpanish
argument_list|,
name|japaneseToInteger
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Uno"
argument_list|,
name|japaneseToSpanish
operator|.
name|apply
argument_list|(
literal|"Ichi"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|japaneseToSpanish
operator|.
name|apply
argument_list|(
literal|"Ni"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{     }
name|assertEquals
argument_list|(
literal|"Tres"
argument_list|,
name|japaneseToSpanish
operator|.
name|apply
argument_list|(
literal|"San"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|japaneseToSpanish
operator|.
name|apply
argument_list|(
literal|"Shi"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{     }
operator|new
name|EqualsTester
argument_list|(
name|japaneseToSpanish
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|compose
argument_list|(
name|integerToSpanish
argument_list|,
name|japaneseToInteger
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|japaneseToInteger
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|integerToSpanish
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|compose
argument_list|(
name|japaneseToInteger
argument_list|,
name|integerToSpanish
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testComposition_includeReserializabled ()
specifier|public
name|void
name|testComposition_includeReserializabled
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mJapaneseToInteger
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"Ichi"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"Ni"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|mJapaneseToInteger
operator|.
name|put
argument_list|(
literal|"San"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|japaneseToInteger
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|mJapaneseToInteger
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|mIntegerToSpanish
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|1
argument_list|,
literal|"Uno"
argument_list|)
expr_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|3
argument_list|,
literal|"Tres"
argument_list|)
expr_stmt|;
name|mIntegerToSpanish
operator|.
name|put
argument_list|(
literal|4
argument_list|,
literal|"Cuatro"
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|integerToSpanish
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|mIntegerToSpanish
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|japaneseToSpanish
init|=
name|Functions
operator|.
name|compose
argument_list|(
name|integerToSpanish
argument_list|,
name|japaneseToInteger
argument_list|)
decl_stmt|;
operator|new
name|EqualsTester
argument_list|(
name|japaneseToSpanish
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|compose
argument_list|(
name|integerToSpanish
argument_list|,
name|japaneseToInteger
argument_list|)
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|japaneseToSpanish
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|japaneseToInteger
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|integerToSpanish
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|compose
argument_list|(
name|japaneseToInteger
argument_list|,
name|integerToSpanish
argument_list|)
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
DECL|method|testCompositionWildcard ()
specifier|public
name|void
name|testCompositionWildcard
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|mapJapaneseToInteger
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|japaneseToInteger
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|mapJapaneseToInteger
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|numberToSpanish
init|=
name|Functions
operator|.
name|constant
argument_list|(
literal|"Yo no se"
argument_list|)
decl_stmt|;
name|Functions
operator|.
name|compose
argument_list|(
name|numberToSpanish
argument_list|,
name|japaneseToInteger
argument_list|)
expr_stmt|;
block|}
DECL|class|HashCodeFunction
specifier|private
specifier|static
class|class
name|HashCodeFunction
implements|implements
name|Function
argument_list|<
name|Object
argument_list|,
name|Integer
argument_list|>
block|{
DECL|method|apply (Object o)
specifier|public
name|Integer
name|apply
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
operator|(
name|o
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|o
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
DECL|method|testComposeOfFunctionsIsAssociative ()
specifier|public
name|void
name|testComposeOfFunctionsIsAssociative
parameter_list|()
block|{
name|Map
argument_list|<
name|Float
argument_list|,
name|String
argument_list|>
name|m
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|4.0f
argument_list|,
literal|"A"
argument_list|,
literal|3.0f
argument_list|,
literal|"B"
argument_list|,
literal|2.0f
argument_list|,
literal|"C"
argument_list|,
literal|1.0f
argument_list|,
literal|"D"
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|?
super|super
name|Integer
argument_list|,
name|Boolean
argument_list|>
name|h
init|=
name|Functions
operator|.
name|constant
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|?
super|super
name|String
argument_list|,
name|Integer
argument_list|>
name|g
init|=
operator|new
name|HashCodeFunction
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|Float
argument_list|,
name|String
argument_list|>
name|f
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|m
argument_list|,
literal|"F"
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Float
argument_list|,
name|Boolean
argument_list|>
name|c1
init|=
name|Functions
operator|.
name|compose
argument_list|(
name|Functions
operator|.
name|compose
argument_list|(
name|h
argument_list|,
name|g
argument_list|)
argument_list|,
name|f
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Float
argument_list|,
name|Boolean
argument_list|>
name|c2
init|=
name|Functions
operator|.
name|compose
argument_list|(
name|h
argument_list|,
name|Functions
operator|.
name|compose
argument_list|(
name|g
argument_list|,
name|f
argument_list|)
argument_list|)
decl_stmt|;
comment|// Might be nice (eventually) to have:
comment|//     assertEquals(c1, c2);
comment|// But for now, settle for this:
name|assertEquals
argument_list|(
name|c1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|c2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c1
operator|.
name|apply
argument_list|(
literal|1.0f
argument_list|)
argument_list|,
name|c2
operator|.
name|apply
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|c1
operator|.
name|apply
argument_list|(
literal|5.0f
argument_list|)
argument_list|,
name|c2
operator|.
name|apply
argument_list|(
literal|5.0f
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testComposeOfPredicateAndFunctionIsAssociative ()
specifier|public
name|void
name|testComposeOfPredicateAndFunctionIsAssociative
parameter_list|()
block|{
name|Map
argument_list|<
name|Float
argument_list|,
name|String
argument_list|>
name|m
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|4.0f
argument_list|,
literal|"A"
argument_list|,
literal|3.0f
argument_list|,
literal|"B"
argument_list|,
literal|2.0f
argument_list|,
literal|"C"
argument_list|,
literal|1.0f
argument_list|,
literal|"D"
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|?
super|super
name|Integer
argument_list|>
name|h
init|=
name|Predicates
operator|.
name|equalTo
argument_list|(
literal|42
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|?
super|super
name|String
argument_list|,
name|Integer
argument_list|>
name|g
init|=
operator|new
name|HashCodeFunction
argument_list|()
decl_stmt|;
name|Function
argument_list|<
name|Float
argument_list|,
name|String
argument_list|>
name|f
init|=
name|Functions
operator|.
name|forMap
argument_list|(
name|m
argument_list|,
literal|"F"
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|Float
argument_list|>
name|p1
init|=
name|Predicates
operator|.
name|compose
argument_list|(
name|Predicates
operator|.
name|compose
argument_list|(
name|h
argument_list|,
name|g
argument_list|)
argument_list|,
name|f
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|Float
argument_list|>
name|p2
init|=
name|Predicates
operator|.
name|compose
argument_list|(
name|h
argument_list|,
name|Functions
operator|.
name|compose
argument_list|(
name|g
argument_list|,
name|f
argument_list|)
argument_list|)
decl_stmt|;
comment|// Might be nice (eventually) to have:
comment|//     assertEquals(p1, p2);
comment|// But for now, settle for this:
name|assertEquals
argument_list|(
name|p1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|p2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p1
operator|.
name|apply
argument_list|(
literal|1.0f
argument_list|)
argument_list|,
name|p2
operator|.
name|apply
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p1
operator|.
name|apply
argument_list|(
literal|5.0f
argument_list|)
argument_list|,
name|p2
operator|.
name|apply
argument_list|(
literal|5.0f
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testForPredicate ()
specifier|public
name|void
name|testForPredicate
parameter_list|()
block|{
name|Function
argument_list|<
name|Object
argument_list|,
name|Boolean
argument_list|>
name|alwaysTrue
init|=
name|Functions
operator|.
name|forPredicate
argument_list|(
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
decl_stmt|;
name|Function
argument_list|<
name|Object
argument_list|,
name|Boolean
argument_list|>
name|alwaysFalse
init|=
name|Functions
operator|.
name|forPredicate
argument_list|(
name|Predicates
operator|.
name|alwaysFalse
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|alwaysTrue
operator|.
name|apply
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|alwaysFalse
operator|.
name|apply
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|(
name|alwaysTrue
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|forPredicate
argument_list|(
name|Predicates
operator|.
name|alwaysTrue
argument_list|()
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|alwaysFalse
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
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
literal|"SerializableTester"
argument_list|)
DECL|method|testForPredicateSerializable ()
specifier|public
name|void
name|testForPredicateSerializable
parameter_list|()
block|{
name|checkCanReserialize
argument_list|(
name|Functions
operator|.
name|forPredicate
argument_list|(
name|Predicates
operator|.
name|equalTo
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testConstant ()
specifier|public
name|void
name|testConstant
parameter_list|()
block|{
name|Function
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|f
init|=
name|Functions
operator|.
expr|<
name|Object
operator|>
name|constant
argument_list|(
literal|"correct"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"correct"
argument_list|,
name|f
operator|.
name|apply
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"correct"
argument_list|,
name|f
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|g
init|=
name|Functions
operator|.
name|constant
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|g
operator|.
name|apply
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|g
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
operator|new
name|EqualsTester
argument_list|(
name|f
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|"correct"
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|"incorrect"
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|g
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
operator|new
name|EqualsTester
argument_list|(
name|g
argument_list|)
operator|.
name|addEqualObject
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|null
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|"incorrect"
argument_list|)
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|Functions
operator|.
name|toStringFunction
argument_list|()
argument_list|)
operator|.
name|addNotEqualObject
argument_list|(
name|f
argument_list|)
operator|.
name|testEquals
argument_list|()
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|testConstantSerializable ()
specifier|public
name|void
name|testConstantSerializable
parameter_list|()
block|{
name|checkCanReserialize
argument_list|(
name|Functions
operator|.
name|constant
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|checkCanReserialize (Function<? super Integer, Y> f)
specifier|private
parameter_list|<
name|Y
parameter_list|>
name|void
name|checkCanReserialize
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|Integer
argument_list|,
name|Y
argument_list|>
name|f
parameter_list|)
block|{
name|Function
argument_list|<
name|?
super|super
name|Integer
argument_list|,
name|Y
argument_list|>
name|g
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|f
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
comment|// convoluted way to check that the same result happens from each
name|Y
name|expected
init|=
literal|null
decl_stmt|;
try|try
block|{
name|expected
operator|=
name|f
operator|.
name|apply
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
try|try
block|{
name|g
operator|.
name|apply
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ok
parameter_list|)
block|{
continue|continue;
block|}
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|g
operator|.
name|apply
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|checkCanReserializeSingleton (Function<? super String, Y> f)
specifier|private
parameter_list|<
name|Y
parameter_list|>
name|void
name|checkCanReserializeSingleton
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|String
argument_list|,
name|Y
argument_list|>
name|f
parameter_list|)
block|{
name|Function
argument_list|<
name|?
super|super
name|String
argument_list|,
name|Y
argument_list|>
name|g
init|=
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|f
argument_list|,
name|g
argument_list|)
expr_stmt|;
for|for
control|(
name|Integer
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|f
operator|.
name|apply
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
name|g
operator|.
name|apply
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

