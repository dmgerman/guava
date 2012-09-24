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
name|testing
operator|.
name|AbstractPackageSanityTests
operator|.
name|Chopper
operator|.
name|suffix
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
name|Beta
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
name|VisibleForTesting
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
name|Optional
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
name|HashMultimap
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
name|Lists
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
name|collect
operator|.
name|Multimap
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
name|Sets
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
name|reflect
operator|.
name|ClassPath
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
operator|.
name|Visibility
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * Automatically runs sanity checks against the public classes in the same package of the class that  * extends {@code AbstractPackageSanityTests}. Currently sanity checks include {@link  * NullPointerTester}, {@link EqualsTester} and {@link SerializableTester}. For example:<pre>  * public class PackageSanityTests extends AbstractPackageSanityTests {}  *</pre>  *  *<p>Note that only the simplest type of classes are covered. That is, a public top-level class  * with either a public constructor or a public static factory method to construct instances of the  * class. For example:<pre>  * public class Address {  *   private final String city;  *   private final String state;  *   private final String zipcode;  *  *   public Address(String city, String state, String zipcode) {...}  *  *   {@literal @Override} public boolean equals(Object obj) {...}  *   {@literal @Override} public int hashCode() {...}  *   ...  * }  *</pre>  * No cascading checks are performed against the return values of methods unless the method is a  * static factory method. Neither are semantics of mutation methods such as {@code  * someList.add(obj)} checked. For more detailed discussion of supported and unsupported cases, see  * {@link #testEquals}, {@link #testNulls} and {@link #testSerializable}.  *  *<p>This class incurs IO because it scans the classpath and reads classpath resources.  *  * @author Ben Yu  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
comment|// TODO: Switch to JUnit 4 and use @Parameterized and @BeforeClass
DECL|class|AbstractPackageSanityTests
specifier|public
specifier|abstract
class|class
name|AbstractPackageSanityTests
extends|extends
name|TestCase
block|{
comment|/* The names of the expected method that tests null checks. */
DECL|field|NULL_TEST_METHOD_NAMES
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|NULL_TEST_METHOD_NAMES
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"testNulls"
argument_list|,
literal|"testNull"
argument_list|,
literal|"testNullPointers"
argument_list|,
literal|"testNullPointer"
argument_list|,
literal|"testNullPointerExceptions"
argument_list|,
literal|"testNullPointerException"
argument_list|)
decl_stmt|;
comment|/* The names of the expected method that tests serializable. */
DECL|field|SERIALIZABLE_TEST_METHOD_NAMES
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|SERIALIZABLE_TEST_METHOD_NAMES
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"testSerializable"
argument_list|,
literal|"testSerialization"
argument_list|,
literal|"testEqualsAndSerializable"
argument_list|,
literal|"testEqualsAndSerialization"
argument_list|)
decl_stmt|;
comment|/* The names of the expected method that tests equals. */
DECL|field|EQUALS_TEST_METHOD_NAMES
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|EQUALS_TEST_METHOD_NAMES
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"testEquals"
argument_list|,
literal|"testEqualsAndHashCode"
argument_list|,
literal|"testEqualsAndSerializable"
argument_list|,
literal|"testEqualsAndSerialization"
argument_list|)
decl_stmt|;
DECL|field|TEST_SUFFIX
specifier|private
specifier|static
specifier|final
name|Chopper
name|TEST_SUFFIX
init|=
name|suffix
argument_list|(
literal|"Test"
argument_list|)
operator|.
name|or
argument_list|(
name|suffix
argument_list|(
literal|"Tests"
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|suffix
argument_list|(
literal|"TestCase"
argument_list|)
argument_list|)
operator|.
name|or
argument_list|(
name|suffix
argument_list|(
literal|"TestSuite"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|tester
specifier|private
specifier|final
name|ClassSanityTester
name|tester
init|=
operator|new
name|ClassSanityTester
argument_list|()
decl_stmt|;
DECL|field|visibility
specifier|private
name|Visibility
name|visibility
init|=
name|Visibility
operator|.
name|PACKAGE
decl_stmt|;
comment|/**    * Restricts the sanity tests for public API only. By default, package-private API are also    * covered.    */
DECL|method|publicApiOnly ()
specifier|protected
specifier|final
name|void
name|publicApiOnly
parameter_list|()
block|{
name|visibility
operator|=
name|Visibility
operator|.
name|PUBLIC
expr_stmt|;
block|}
comment|/**    * Tests all top-level public {@link Serializable} classes in the package. For a serializable    * Class {@code C}:    *<ul>    *<li>If {@code C} explicitly implements {@link Object#equals}, the deserialized instance will be    *     checked to be equal to the instance before serialization.    *<li>If {@code C} doesn't explicitly implement {@code equals} but instead inherits it from a    *     superclass, no equality check is done on the deserialized instance because it's not clear    *     whether the author intended for the class to be a value type.    *<li>If a constructor or factory method takes a parameter whose type is interface, a dynamic    *     proxy will be passed to the method. It's possible that the method body expects an instance    *     method of the passed-in proxy to be of a certain value yet the proxy isn't aware of the    *     assumption, in which case the equality check before and after serialization will fail.    *<li>If the constructor or factory method takes a parameter that {@link    *     AbstractPackageSanityTests} doesn't know how to construct, the test will fail.    *<li>If there is no public constructor or public static factory method declared by {@code C},    *     {@code C} is skipped for serialization test, even if it implements {@link Serializable}.    *<li>Serialization test is not performed on method return values unless the method is a public    *     static factory method whose return type is {@code C} or {@code C}'s subtype.    *</ul>    *    * In all cases, if {@code C} needs custom logic for testing serialization, you can add an    * explicit {@code testSerializable()} test in the corresponding {@code CTest} class, and {@code    * C} will be excluded from automated serialization test performed by this method.    */
annotation|@
name|Test
DECL|method|testSerializable ()
specifier|public
name|void
name|testSerializable
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: when we use @BeforeClass, we can pay the cost of class path scanning only once.
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|classToTest
range|:
name|findClassesToTest
argument_list|(
name|loadClassesInPackage
argument_list|()
argument_list|,
name|SERIALIZABLE_TEST_METHOD_NAMES
argument_list|)
control|)
block|{
if|if
condition|(
name|Serializable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|classToTest
argument_list|)
condition|)
block|{
try|try
block|{
name|Object
name|instance
init|=
name|tester
operator|.
name|instantiate
argument_list|(
name|classToTest
argument_list|)
decl_stmt|;
if|if
condition|(
name|instance
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isEqualsDefined
argument_list|(
name|classToTest
argument_list|)
condition|)
block|{
name|SerializableTester
operator|.
name|reserializeAndAssert
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|sanityError
argument_list|(
name|classToTest
argument_list|,
name|SERIALIZABLE_TEST_METHOD_NAMES
argument_list|,
literal|"serializable test"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**    * Performs {@link NullPointerTester} checks for all top-level public classes in the package. For    * a class {@code C}    *<ul>    *<li>All public static methods are checked such that passing null for any parameter that's not    *     annotated with {@link javax.annotation.Nullable} should throw {@link NullPointerException}.    *<li>If there is any public constructor or public static factory method declared by the class,    *     all public instance methods will be checked too using the instance created by invoking the    *     constructor or static factory method.    *<li>If the constructor or factory method used to construct instance takes a parameter that    *     {@link AbstractPackageSanityTests} doesn't know how to construct, the test will fail.    *<li>If there is no public constructor or public static factory method declared by {@code C},    *     instance methods are skipped for nulls test.    *<li>Nulls test is not performed on method return values unless the method is a public static    *     factory method whose return type is {@code C} or {@code C}'s subtype.    *</ul>    *    * In all cases, if {@code C} needs custom logic for testing nulls, you can add an explicit {@code    * testNulls()} test in the corresponding {@code CTest} class, and {@code C} will be excluded from    * the automated null tests performed by this method.    */
annotation|@
name|Test
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|classToTest
range|:
name|findClassesToTest
argument_list|(
name|loadClassesInPackage
argument_list|()
argument_list|,
name|NULL_TEST_METHOD_NAMES
argument_list|)
control|)
block|{
try|try
block|{
name|tester
operator|.
name|doTestNulls
argument_list|(
name|classToTest
argument_list|,
name|visibility
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|sanityError
argument_list|(
name|classToTest
argument_list|,
name|NULL_TEST_METHOD_NAMES
argument_list|,
literal|"nulls test"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Tests {@code equals()} and {@code hashCode()} implementations for every top-level public class    * in the package, that explicitly implements {@link Object#equals}. For a class {@code C}:    *<ul>    *<li>The public constructor or public static factory method with the most parameters is used to    *     construct the sample instances. In case of tie, the candidate constructors or factories are    *     tried one after another until one can be used to construct sample instances.    *<li>For the constructor or static factory method used to construct instances, it's checked that    *     when equal parameters are passed, the result instance should also be equal; and vice versa.    *<li>Inequality check is not performed against state mutation methods such as {@link List#add},    *     or functional update methods such as {@link com.google.common.base.Joiner#skipNulls}.    *<li>If the constructor or factory method used to construct instance takes a parameter that    *     {@link AbstractPackageSanityTests} doesn't know how to construct, the test will fail.    *<li>If there is no public constructor or public static factory method declared by {@code C},    *     {@code C} is skipped for equality test.    *<li>Equality test is not performed on method return values unless the method is a public static    *     factory method whose return type is {@code C} or {@code C}'s subtype.    *</ul>    *    * In all cases, if {@code C} needs custom logic for testing {@code equals()}, you can add an    * explicit {@code testEquals()} test in the corresponding {@code CTest} class, and {@code C} will    * be excluded from the automated {@code equals} test performed by this method.    */
annotation|@
name|Test
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|classToTest
range|:
name|findClassesToTest
argument_list|(
name|loadClassesInPackage
argument_list|()
argument_list|,
name|EQUALS_TEST_METHOD_NAMES
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|classToTest
operator|.
name|isEnum
argument_list|()
operator|&&
name|isEqualsDefined
argument_list|(
name|classToTest
argument_list|)
condition|)
block|{
try|try
block|{
name|tester
operator|.
name|doTestEquals
argument_list|(
name|classToTest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
name|sanityError
argument_list|(
name|classToTest
argument_list|,
name|EQUALS_TEST_METHOD_NAMES
argument_list|,
literal|"equals test"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**    * Sets the default value for {@code type}, when dummy value for a parameter of the same type    * needs to be created in order to invoke a method or constructor. The default value isn't used in    * testing {@link Object#equals} because more than one sample instances are needed for testing    * inequality.    */
DECL|method|setDefault (Class<T> type, T value)
specifier|protected
specifier|final
parameter_list|<
name|T
parameter_list|>
name|void
name|setDefault
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|tester
operator|.
name|setDefault
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|sanityError ( Class<?> cls, List<String> explicitTestNames, String description, Throwable e)
specifier|private
specifier|static
name|AssertionFailedError
name|sanityError
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|explicitTestNames
parameter_list|,
name|String
name|description
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|String
name|message
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Error in automated %s of %s\n"
operator|+
literal|"If the class is better tested explicitly, you can add %s() to %sTest"
argument_list|,
name|description
argument_list|,
name|cls
argument_list|,
name|explicitTestNames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|AssertionFailedError
name|error
init|=
operator|new
name|AssertionFailedError
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|error
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
name|error
return|;
block|}
comment|/**    * Finds the classes not ending with a test suffix and not covered by an explicit test    * whose name is {@code explicitTestName}.    */
DECL|method|findClassesToTest ( Iterable<? extends Class<?>> classes, Iterable<String> explicitTestNames)
annotation|@
name|VisibleForTesting
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
name|Iterable
argument_list|<
name|String
argument_list|>
name|explicitTestNames
parameter_list|)
block|{
comment|// "a.b.Foo" -> a.b.Foo.class
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classMap
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|classes
control|)
block|{
name|classMap
operator|.
name|put
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
comment|// Foo.class -> [FooTest.class, FooTests.class, FooTestSuite.class, ...]
name|Multimap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|testClasses
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|LinkedHashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|nonTestClasses
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|classes
control|)
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|testedClassName
init|=
name|TEST_SUFFIX
operator|.
name|chop
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|testedClassName
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|testedClass
init|=
name|classMap
operator|.
name|get
argument_list|(
name|testedClassName
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|testedClass
operator|!=
literal|null
condition|)
block|{
name|testClasses
operator|.
name|put
argument_list|(
name|testedClass
argument_list|,
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|nonTestClasses
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classesToTest
init|=
name|Lists
operator|.
name|newArrayListWithExpectedSize
argument_list|(
name|nonTestClasses
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|NEXT_CANDIDATE
label|:
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
range|:
name|nonTestClasses
control|)
block|{
if|if
condition|(
operator|!
name|visibility
operator|.
name|isVisible
argument_list|(
name|cls
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
range|:
name|testClasses
operator|.
name|get
argument_list|(
name|cls
argument_list|)
control|)
block|{
if|if
condition|(
name|hasTest
argument_list|(
name|testClass
argument_list|,
name|explicitTestNames
argument_list|)
condition|)
block|{
comment|// covered by explicit test
continue|continue
name|NEXT_CANDIDATE
continue|;
block|}
block|}
name|classesToTest
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
return|return
name|classesToTest
return|;
block|}
DECL|method|loadClassesInPackage ()
specifier|private
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|loadClassesInPackage
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|String
name|packageName
init|=
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassPath
operator|.
name|ClassInfo
name|classInfo
range|:
name|ClassPath
operator|.
name|from
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|.
name|getTopLevelClasses
argument_list|(
name|packageName
argument_list|)
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
try|try
block|{
name|cls
operator|=
name|classInfo
operator|.
name|load
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoClassDefFoundError
name|e
parameter_list|)
block|{
comment|// In case there were linking problems, this is probably not a class we care to test anyway.
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"Cannot load class "
operator|+
name|classInfo
operator|+
literal|", skipping..."
argument_list|,
name|e
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
operator|!
name|cls
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|classes
operator|.
name|add
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|classes
return|;
block|}
DECL|method|hasTest (Class<?> testClass, Iterable<String> testNames)
specifier|private
specifier|static
name|boolean
name|hasTest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|,
name|Iterable
argument_list|<
name|String
argument_list|>
name|testNames
parameter_list|)
block|{
for|for
control|(
name|String
name|testName
range|:
name|testNames
control|)
block|{
try|try
block|{
name|testClass
operator|.
name|getMethod
argument_list|(
name|testName
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|isEqualsDefined (Class<?> cls)
specifier|private
specifier|static
name|boolean
name|isEqualsDefined
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
try|try
block|{
return|return
operator|!
name|cls
operator|.
name|getDeclaredMethod
argument_list|(
literal|"equals"
argument_list|,
name|Object
operator|.
name|class
argument_list|)
operator|.
name|isSynthetic
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|class|Chopper
specifier|static
specifier|abstract
class|class
name|Chopper
block|{
DECL|method|or (final Chopper you)
specifier|final
name|Chopper
name|or
parameter_list|(
specifier|final
name|Chopper
name|you
parameter_list|)
block|{
specifier|final
name|Chopper
name|i
init|=
name|this
decl_stmt|;
return|return
operator|new
name|Chopper
argument_list|()
block|{
annotation|@
name|Override
name|Optional
argument_list|<
name|String
argument_list|>
name|chop
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|i
operator|.
name|chop
argument_list|(
name|str
argument_list|)
operator|.
name|or
argument_list|(
name|you
operator|.
name|chop
argument_list|(
name|str
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|chop (String str)
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|chop
parameter_list|(
name|String
name|str
parameter_list|)
function_decl|;
DECL|method|suffix (final String suffix)
specifier|static
name|Chopper
name|suffix
parameter_list|(
specifier|final
name|String
name|suffix
parameter_list|)
block|{
return|return
operator|new
name|Chopper
argument_list|()
block|{
annotation|@
name|Override
name|Optional
argument_list|<
name|String
argument_list|>
name|chop
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|str
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|str
operator|.
name|length
argument_list|()
operator|-
name|suffix
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

