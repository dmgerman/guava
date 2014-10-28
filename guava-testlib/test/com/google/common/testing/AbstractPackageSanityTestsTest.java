begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Predicates
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
name|Arrays
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
comment|/**  * Unit tests for {@link AbstractPackageSanityTests}.  *  * @author Ben Yu  */
end_comment

begin_class
DECL|class|AbstractPackageSanityTestsTest
specifier|public
class|class
name|AbstractPackageSanityTestsTest
extends|extends
name|TestCase
block|{
DECL|field|sanityTests
specifier|private
specifier|final
name|AbstractPackageSanityTests
name|sanityTests
init|=
operator|new
name|AbstractPackageSanityTests
argument_list|()
block|{}
decl_stmt|;
DECL|method|testFindClassesToTest_testClass ()
specifier|public
name|void
name|testFindClassesToTest_testClass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|EmptyTest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|EmptyTests
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|EmptyTestCase
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|EmptyTestSuite
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_noCorrespondingTestClass ()
specifier|public
name|void
name|testFindClassesToTest_noCorrespondingTestClass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|Foo2Test
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_publicApiOnly ()
specifier|public
name|void
name|testFindClassesToTest_publicApiOnly
parameter_list|()
block|{
name|sanityTests
operator|.
name|publicApiOnly
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|PublicFoo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|PublicFoo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_ignoreClasses ()
specifier|public
name|void
name|testFindClassesToTest_ignoreClasses
parameter_list|()
block|{
name|sanityTests
operator|.
name|ignoreClasses
argument_list|(
name|Predicates
operator|.
expr|<
name|Object
operator|>
name|equalTo
argument_list|(
name|PublicFoo
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|PublicFoo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindClassesToTeset_ignoreUnderscores ()
specifier|public
name|void
name|testFindClassesToTeset_ignoreUnderscores
parameter_list|()
block|{
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|Foo_Bar
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|Foo_Bar
operator|.
name|class
argument_list|)
expr_stmt|;
name|sanityTests
operator|.
name|ignoreClasses
argument_list|(
name|AbstractPackageSanityTests
operator|.
name|UNDERSCORE_IN_NAME
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|Foo_Bar
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_withCorrespondingTestClassButNotExplicitlyTested ()
specifier|public
name|void
name|testFindClassesToTest_withCorrespondingTestClassButNotExplicitlyTested
parameter_list|()
block|{
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|FooTest
operator|.
name|class
argument_list|)
argument_list|,
literal|"testNotThere"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|FooTest
operator|.
name|class
argument_list|)
argument_list|,
literal|"testNotPublic"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_withCorrespondingTestClassAndExplicitlyTested ()
specifier|public
name|void
name|testFindClassesToTest_withCorrespondingTestClassAndExplicitlyTested
parameter_list|()
block|{
name|ImmutableList
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Object
argument_list|>
argument_list|>
name|classes
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|FooTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|classes
argument_list|,
literal|"testPublic"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|classes
argument_list|,
literal|"testNotThere"
argument_list|,
literal|"testPublic"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testFindClassesToTest_withCorrespondingTestClass_noTestName ()
specifier|public
name|void
name|testFindClassesToTest_withCorrespondingTestClass_noTestName
parameter_list|()
block|{
name|assertThat
argument_list|(
name|findClassesToTest
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
name|FooTest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|Foo
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|class|EmptyTestCase
specifier|static
class|class
name|EmptyTestCase
block|{}
DECL|class|EmptyTest
specifier|static
class|class
name|EmptyTest
block|{}
DECL|class|EmptyTests
specifier|static
class|class
name|EmptyTests
block|{}
DECL|class|EmptyTestSuite
specifier|static
class|class
name|EmptyTestSuite
block|{}
DECL|class|Foo
specifier|static
class|class
name|Foo
block|{}
DECL|class|Foo_Bar
specifier|static
class|class
name|Foo_Bar
block|{}
DECL|class|PublicFoo
specifier|public
specifier|static
class|class
name|PublicFoo
block|{}
DECL|class|FooTest
specifier|static
class|class
name|FooTest
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// accessed reflectively
DECL|method|testPublic ()
specifier|public
name|void
name|testPublic
parameter_list|()
block|{}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// accessed reflectively
DECL|method|testNotPublic ()
name|void
name|testNotPublic
parameter_list|()
block|{}
block|}
comment|// Shouldn't be mistaken as Foo's test
DECL|class|Foo2Test
specifier|static
class|class
name|Foo2Test
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// accessed reflectively
DECL|method|testPublic ()
specifier|public
name|void
name|testPublic
parameter_list|()
block|{}
block|}
DECL|method|findClassesToTest ( Iterable<? extends Class<?>> classes, String... explicitTestNames)
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|findClassesToTest
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|,
name|String
modifier|...
name|explicitTestNames
parameter_list|)
block|{
return|return
name|sanityTests
operator|.
name|findClassesToTest
argument_list|(
name|classes
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|explicitTestNames
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

