begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
name|org
operator|.
name|junit
operator|.
name|contrib
operator|.
name|truth
operator|.
name|Truth
operator|.
name|ASSERT
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
name|List
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
comment|/**  * Unit test for {@link Optional}.  *  * @author Kurt Alfred Kluever  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|OptionalTest
specifier|public
specifier|final
class|class
name|OptionalTest
extends|extends
name|TestCase
block|{
DECL|method|testAbsent ()
specifier|public
name|void
name|testAbsent
parameter_list|()
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|optionalName
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|optionalName
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOf ()
specifier|public
name|void
name|testOf
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"training"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOf_null ()
specifier|public
name|void
name|testOf_null
parameter_list|()
block|{
try|try
block|{
name|Optional
operator|.
name|of
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testFromNullable ()
specifier|public
name|void
name|testFromNullable
parameter_list|()
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|optionalName
init|=
name|Optional
operator|.
name|fromNullable
argument_list|(
literal|"bob"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bob"
argument_list|,
name|optionalName
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFromNullable_null ()
specifier|public
name|void
name|testFromNullable_null
parameter_list|()
block|{
comment|// not promised by spec, but easier to test
name|assertSame
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|,
name|Optional
operator|.
name|fromNullable
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsPresent_no ()
specifier|public
name|void
name|testIsPresent_no
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testIsPresent_yes ()
specifier|public
name|void
name|testIsPresent_yes
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testGet_absent ()
specifier|public
name|void
name|testGet_absent
parameter_list|()
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|optional
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
try|try
block|{
name|optional
operator|.
name|get
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testGet_present ()
specifier|public
name|void
name|testGet_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"training"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_T_present ()
specifier|public
name|void
name|testOr_T_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|or
argument_list|(
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_T_absent ()
specifier|public
name|void
name|testOr_T_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|or
argument_list|(
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_Supplier_present ()
specifier|public
name|void
name|testOr_Supplier_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|or
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"fallback"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_Supplier_absent ()
specifier|public
name|void
name|testOr_Supplier_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"fallback"
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|or
argument_list|(
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|"fallback"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_NullSupplier_absent ()
specifier|public
name|void
name|testOr_NullSupplier_absent
parameter_list|()
block|{
name|Supplier
argument_list|<
name|Object
argument_list|>
name|nullSupplier
init|=
name|Suppliers
operator|.
name|ofInstance
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Object
argument_list|>
name|absentOptional
init|=
name|Optional
operator|.
name|absent
argument_list|()
decl_stmt|;
try|try
block|{
name|absentOptional
operator|.
name|or
argument_list|(
name|nullSupplier
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testOr_Optional_present ()
specifier|public
name|void
name|testOr_Optional_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|or
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"fallback"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOr_Optional_absent ()
specifier|public
name|void
name|testOr_Optional_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"fallback"
argument_list|)
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|or
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"fallback"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrNull_present ()
specifier|public
name|void
name|testOrNull_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"a"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|orNull
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOrNull_absent ()
specifier|public
name|void
name|testOrNull_absent
parameter_list|()
block|{
name|assertNull
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|orNull
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsSet_present ()
specifier|public
name|void
name|testAsSet_present
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|expected
init|=
name|Collections
operator|.
name|singleton
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|asSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsSet_absent ()
specifier|public
name|void
name|testAsSet_absent
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"Returned set should be empty"
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|asSet
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testAsSet_presentIsImmutable ()
specifier|public
name|void
name|testAsSet_presentIsImmutable
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|presentAsSet
init|=
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|asSet
argument_list|()
decl_stmt|;
try|try
block|{
name|presentAsSet
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testAsSet_absentIsImmutable ()
specifier|public
name|void
name|testAsSet_absentIsImmutable
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|absentAsSet
init|=
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|asSet
argument_list|()
decl_stmt|;
try|try
block|{
name|absentAsSet
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|expected
parameter_list|)
block|{     }
block|}
comment|// TODO(kevinb): use EqualsTester
DECL|method|testEqualsAndHashCode_absent ()
specifier|public
name|void
name|testEqualsAndHashCode_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
expr|<
name|String
operator|>
name|absent
argument_list|()
argument_list|,
name|Optional
operator|.
expr|<
name|Integer
operator|>
name|absent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualsAndHashCode_present ()
specifier|public
name|void
name|testEqualsAndHashCode_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|equals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"b"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
operator|.
name|equals
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_absent ()
specifier|public
name|void
name|testToString_absent
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Optional.absent()"
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToString_present ()
specifier|public
name|void
name|testToString_present
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Optional.of(training)"
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testPresentInstances_allPresent ()
specifier|public
name|void
name|testPresentInstances_allPresent
parameter_list|()
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|String
argument_list|>
argument_list|>
name|optionals
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Optional
operator|.
name|presentInstances
argument_list|(
name|optionals
argument_list|)
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
expr_stmt|;
block|}
DECL|method|testPresentInstances_allAbsent ()
specifier|public
name|void
name|testPresentInstances_allAbsent
parameter_list|()
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|Object
argument_list|>
argument_list|>
name|optionals
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|,
name|Optional
operator|.
name|absent
argument_list|()
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Optional
operator|.
name|presentInstances
argument_list|(
name|optionals
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testPresentInstances_somePresent ()
specifier|public
name|void
name|testPresentInstances_somePresent
parameter_list|()
block|{
name|List
argument_list|<
name|Optional
argument_list|<
name|String
argument_list|>
argument_list|>
name|optionals
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|Optional
operator|.
expr|<
name|String
operator|>
name|absent
argument_list|()
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|ASSERT
operator|.
name|that
argument_list|(
name|Optional
operator|.
name|presentInstances
argument_list|(
name|optionals
argument_list|)
argument_list|)
operator|.
name|hasContentsInOrder
argument_list|(
literal|"a"
argument_list|,
literal|"c"
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
name|Optional
operator|.
name|absent
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"NullPointerTester"
argument_list|)
DECL|method|testNullPointers ()
specifier|public
name|void
name|testNullPointers
parameter_list|()
throws|throws
name|Exception
block|{
name|NullPointerTester
name|npTester
init|=
operator|new
name|NullPointerTester
argument_list|()
decl_stmt|;
name|npTester
operator|.
name|testAllPublicConstructors
argument_list|(
name|Optional
operator|.
name|class
argument_list|)
expr_stmt|;
name|npTester
operator|.
name|testAllPublicStaticMethods
argument_list|(
name|Optional
operator|.
name|class
argument_list|)
expr_stmt|;
name|npTester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|)
expr_stmt|;
name|npTester
operator|.
name|testAllPublicInstanceMethods
argument_list|(
name|Optional
operator|.
name|of
argument_list|(
literal|"training"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

