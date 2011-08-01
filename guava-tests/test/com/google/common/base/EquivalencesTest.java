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
name|testing
operator|.
name|SerializableTester
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
name|com
operator|.
name|google
operator|.
name|testing
operator|.
name|util
operator|.
name|EquivalenceTester
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
comment|/**  * Unit test for {@link Equivalences}.  *  * @author Kurt Alfred Kluever  * @author Jige Yu  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|EquivalencesTest
specifier|public
class|class
name|EquivalencesTest
extends|extends
name|TestCase
block|{
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
name|Equivalences
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
name|Equivalences
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
DECL|method|testEquality ()
specifier|public
name|void
name|testEquality
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
name|equals
argument_list|()
argument_list|,
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
operator|.
name|addEqualityGroup
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
argument_list|,
name|Equivalences
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
name|Equivalences
operator|.
name|equals
argument_list|()
argument_list|)
expr_stmt|;
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|Equivalences
operator|.
name|identity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

