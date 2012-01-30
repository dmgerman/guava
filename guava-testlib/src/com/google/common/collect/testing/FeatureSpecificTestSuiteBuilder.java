begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Collections
operator|.
name|disjoint
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|FINER
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
name|ConflictingRequirementsException
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
name|Feature
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
name|FeatureUtil
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
name|TesterRequirements
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|List
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
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
comment|/**  * Creates, based on your criteria, a JUnit test suite that exhaustively tests  * the object generated by a G, selecting appropriate tests by matching them  * against specified features.  *  * @param<B> The concrete type of this builder (the 'self-type'). All the  * Builder methods of this class (such as {@link #named}) return this type, so  * that Builder methods of more derived classes can be chained onto them without  * casting.  * @param<G> The type of the generator to be passed to testers in the  * generated test suite. An instance of G should somehow provide an  * instance of the class under test, plus any other information required  * to parameterize the test.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|FeatureSpecificTestSuiteBuilder
specifier|public
specifier|abstract
class|class
name|FeatureSpecificTestSuiteBuilder
parameter_list|<
name|B
extends|extends
name|FeatureSpecificTestSuiteBuilder
parameter_list|<
name|B
parameter_list|,
name|G
parameter_list|>
parameter_list|,
name|G
parameter_list|>
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|self ()
specifier|protected
name|B
name|self
parameter_list|()
block|{
return|return
operator|(
name|B
operator|)
name|this
return|;
block|}
comment|// Test Data
DECL|field|subjectGenerator
specifier|private
name|G
name|subjectGenerator
decl_stmt|;
comment|// Gets run before every test.
DECL|field|setUp
specifier|private
name|Runnable
name|setUp
decl_stmt|;
comment|// Gets run at the conclusion of every test.
DECL|field|tearDown
specifier|private
name|Runnable
name|tearDown
decl_stmt|;
DECL|method|usingGenerator (G subjectGenerator)
specifier|protected
name|B
name|usingGenerator
parameter_list|(
name|G
name|subjectGenerator
parameter_list|)
block|{
name|this
operator|.
name|subjectGenerator
operator|=
name|subjectGenerator
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getSubjectGenerator ()
specifier|public
name|G
name|getSubjectGenerator
parameter_list|()
block|{
return|return
name|subjectGenerator
return|;
block|}
DECL|method|withSetUp (Runnable setUp)
specifier|public
name|B
name|withSetUp
parameter_list|(
name|Runnable
name|setUp
parameter_list|)
block|{
name|this
operator|.
name|setUp
operator|=
name|setUp
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getSetUp ()
specifier|protected
name|Runnable
name|getSetUp
parameter_list|()
block|{
return|return
name|setUp
return|;
block|}
DECL|method|withTearDown (Runnable tearDown)
specifier|public
name|B
name|withTearDown
parameter_list|(
name|Runnable
name|tearDown
parameter_list|)
block|{
name|this
operator|.
name|tearDown
operator|=
name|tearDown
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getTearDown ()
specifier|protected
name|Runnable
name|getTearDown
parameter_list|()
block|{
return|return
name|tearDown
return|;
block|}
comment|// Features
DECL|field|features
specifier|private
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
decl_stmt|;
comment|/**    * Configures this builder to produce tests appropriate for the given    * features.    */
DECL|method|withFeatures (Feature<?>.... features)
specifier|public
name|B
name|withFeatures
parameter_list|(
name|Feature
argument_list|<
name|?
argument_list|>
modifier|...
name|features
parameter_list|)
block|{
return|return
name|withFeatures
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|features
argument_list|)
argument_list|)
return|;
block|}
DECL|method|withFeatures (Iterable<? extends Feature<?>> features)
specifier|public
name|B
name|withFeatures
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
parameter_list|)
block|{
name|this
operator|.
name|features
operator|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|features
argument_list|)
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|getFeatures
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|features
argument_list|)
return|;
block|}
comment|// Name
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/** Configures this builder produce a TestSuite with the given name. */
DECL|method|named (String name)
specifier|public
name|B
name|named
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"("
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Eclipse hides all characters after "
operator|+
literal|"'('; please use '[]' or other characters instead of parentheses"
argument_list|)
throw|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|// Test suppression
DECL|field|suppressedTests
specifier|private
name|Set
argument_list|<
name|Method
argument_list|>
name|suppressedTests
init|=
operator|new
name|HashSet
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
comment|/**    * Prevents the given methods from being run as part of the test suite.    *    *<em>Note:</em> in principle this should never need to be used, but it    * might be useful if the semantics of an implementation disagree in    * unforeseen ways with the semantics expected by a test, or to keep dependent    * builds clean in spite of an erroneous test.    */
DECL|method|suppressing (Method... methods)
specifier|public
name|B
name|suppressing
parameter_list|(
name|Method
modifier|...
name|methods
parameter_list|)
block|{
return|return
name|suppressing
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|methods
argument_list|)
argument_list|)
return|;
block|}
DECL|method|suppressing (Collection<Method> methods)
specifier|public
name|B
name|suppressing
parameter_list|(
name|Collection
argument_list|<
name|Method
argument_list|>
name|methods
parameter_list|)
block|{
name|suppressedTests
operator|.
name|addAll
argument_list|(
name|methods
argument_list|)
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|getSuppressedTests ()
specifier|public
name|Set
argument_list|<
name|Method
argument_list|>
name|getSuppressedTests
parameter_list|()
block|{
return|return
name|suppressedTests
return|;
block|}
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|FeatureSpecificTestSuiteBuilder
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Creates a runnable JUnit test suite based on the criteria already given.    */
comment|/*    * Class parameters must be raw. This annotation should go on testerClass in    * the for loop, but the 1.5 javac crashes on annotations in for loops:    *<http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6294589>    */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createTestSuite ()
specifier|public
name|TestSuite
name|createTestSuite
parameter_list|()
block|{
name|checkCanCreate
argument_list|()
expr_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|" Testing: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|"Features: "
operator|+
name|formatFeatureSet
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|logger
operator|.
name|fine
argument_list|(
literal|"Expanded: "
operator|+
name|formatFeatureSet
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
comment|// Class parameters must be raw.
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
name|testers
init|=
name|getTesters
argument_list|()
decl_stmt|;
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|(
name|name
argument_list|)
decl_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
name|testerClass
range|:
name|testers
control|)
block|{
specifier|final
name|TestSuite
name|testerSuite
init|=
name|makeSuiteForTesterClass
argument_list|(
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|<
name|?
argument_list|>
argument_list|>
operator|)
name|testerClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|testerSuite
operator|.
name|countTestCases
argument_list|()
operator|>
literal|0
condition|)
block|{
name|suite
operator|.
name|addTest
argument_list|(
name|testerSuite
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|suite
return|;
block|}
comment|/**    * Throw {@link IllegalStateException} if {@link #createTestSuite()} can't    * be called yet.    */
DECL|method|checkCanCreate ()
specifier|protected
name|void
name|checkCanCreate
parameter_list|()
block|{
if|if
condition|(
name|subjectGenerator
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call using() before createTestSuite()."
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call named() before createTestSuite()."
argument_list|)
throw|;
block|}
if|if
condition|(
name|features
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call withFeatures() before createTestSuite()."
argument_list|)
throw|;
block|}
block|}
comment|// Class parameters must be raw.
specifier|protected
specifier|abstract
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|>
argument_list|>
DECL|method|getTesters ()
name|getTesters
parameter_list|()
function_decl|;
DECL|method|matches (Test test)
specifier|private
name|boolean
name|matches
parameter_list|(
name|Test
name|test
parameter_list|)
block|{
specifier|final
name|Method
name|method
decl_stmt|;
try|try
block|{
name|method
operator|=
name|extractMethod
argument_list|(
name|test
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|finer
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"%s: including by default: %s"
argument_list|,
name|test
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|suppressedTests
operator|.
name|contains
argument_list|(
name|method
argument_list|)
condition|)
block|{
name|logger
operator|.
name|finer
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"%s: excluding because it was explicitly suppressed."
argument_list|,
name|test
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|final
name|TesterRequirements
name|requirements
decl_stmt|;
try|try
block|{
name|requirements
operator|=
name|FeatureUtil
operator|.
name|getTesterRequirements
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConflictingRequirementsException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|features
operator|.
name|containsAll
argument_list|(
name|requirements
operator|.
name|getPresentFeatures
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|FINER
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|missingFeatures
init|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|requirements
operator|.
name|getPresentFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|missingFeatures
operator|.
name|removeAll
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|logger
operator|.
name|finer
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"%s: skipping because these features are absent: %s"
argument_list|,
name|method
argument_list|,
name|missingFeatures
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
if|if
condition|(
name|intersect
argument_list|(
name|features
argument_list|,
name|requirements
operator|.
name|getAbsentFeatures
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|logger
operator|.
name|isLoggable
argument_list|(
name|FINER
argument_list|)
condition|)
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|unwantedFeatures
init|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|requirements
operator|.
name|getAbsentFeatures
argument_list|()
argument_list|)
decl_stmt|;
name|unwantedFeatures
operator|.
name|retainAll
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|logger
operator|.
name|finer
argument_list|(
name|Platform
operator|.
name|format
argument_list|(
literal|"%s: skipping because these features are present: %s"
argument_list|,
name|method
argument_list|,
name|unwantedFeatures
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|intersect (Set<?> a, Set<?> b)
specifier|private
specifier|static
name|boolean
name|intersect
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|a
parameter_list|,
name|Set
argument_list|<
name|?
argument_list|>
name|b
parameter_list|)
block|{
return|return
operator|!
name|disjoint
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
DECL|method|extractMethod (Test test)
specifier|private
specifier|static
name|Method
name|extractMethod
parameter_list|(
name|Test
name|test
parameter_list|)
block|{
if|if
condition|(
name|test
operator|instanceof
name|AbstractTester
condition|)
block|{
name|AbstractTester
argument_list|<
name|?
argument_list|>
name|tester
init|=
operator|(
name|AbstractTester
argument_list|<
name|?
argument_list|>
operator|)
name|test
decl_stmt|;
return|return
name|Platform
operator|.
name|getMethod
argument_list|(
name|tester
operator|.
name|getClass
argument_list|()
argument_list|,
name|tester
operator|.
name|getTestMethodName
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|test
operator|instanceof
name|TestCase
condition|)
block|{
name|TestCase
name|testCase
init|=
operator|(
name|TestCase
operator|)
name|test
decl_stmt|;
return|return
name|Platform
operator|.
name|getMethod
argument_list|(
name|testCase
operator|.
name|getClass
argument_list|()
argument_list|,
name|testCase
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unable to extract method from test: not a TestCase."
argument_list|)
throw|;
block|}
block|}
DECL|method|makeSuiteForTesterClass ( Class<? extends AbstractTester<?>> testerClass)
specifier|protected
name|TestSuite
name|makeSuiteForTesterClass
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|<
name|?
argument_list|>
argument_list|>
name|testerClass
parameter_list|)
block|{
specifier|final
name|TestSuite
name|candidateTests
init|=
name|getTemplateSuite
argument_list|(
name|testerClass
argument_list|)
decl_stmt|;
specifier|final
name|TestSuite
name|suite
init|=
name|filterSuite
argument_list|(
name|candidateTests
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|?
argument_list|>
name|allTests
init|=
name|suite
operator|.
name|tests
argument_list|()
decl_stmt|;
while|while
condition|(
name|allTests
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Object
name|test
init|=
name|allTests
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|test
operator|instanceof
name|AbstractTester
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|AbstractTester
argument_list|<
name|?
super|super
name|G
argument_list|>
name|tester
init|=
operator|(
name|AbstractTester
argument_list|<
name|?
super|super
name|G
argument_list|>
operator|)
name|test
decl_stmt|;
name|tester
operator|.
name|init
argument_list|(
name|subjectGenerator
argument_list|,
name|name
argument_list|,
name|setUp
argument_list|,
name|tearDown
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|suite
return|;
block|}
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|TestSuite
argument_list|>
DECL|field|templateSuiteForClass
name|templateSuiteForClass
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|,
name|TestSuite
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|getTemplateSuite ( Class<? extends AbstractTester<?>> testerClass)
specifier|private
specifier|static
name|TestSuite
name|getTemplateSuite
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTester
argument_list|<
name|?
argument_list|>
argument_list|>
name|testerClass
parameter_list|)
block|{
synchronized|synchronized
init|(
name|templateSuiteForClass
init|)
block|{
name|TestSuite
name|suite
init|=
name|templateSuiteForClass
operator|.
name|get
argument_list|(
name|testerClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|suite
operator|==
literal|null
condition|)
block|{
name|suite
operator|=
operator|new
name|TestSuite
argument_list|(
name|testerClass
argument_list|)
expr_stmt|;
name|templateSuiteForClass
operator|.
name|put
argument_list|(
name|testerClass
argument_list|,
name|suite
argument_list|)
expr_stmt|;
block|}
return|return
name|suite
return|;
block|}
block|}
DECL|method|filterSuite (TestSuite suite)
specifier|private
name|TestSuite
name|filterSuite
parameter_list|(
name|TestSuite
name|suite
parameter_list|)
block|{
name|TestSuite
name|filtered
init|=
operator|new
name|TestSuite
argument_list|(
name|suite
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Enumeration
argument_list|<
name|?
argument_list|>
name|tests
init|=
name|suite
operator|.
name|tests
argument_list|()
decl_stmt|;
while|while
condition|(
name|tests
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Test
name|test
init|=
operator|(
name|Test
operator|)
name|tests
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|matches
argument_list|(
name|test
argument_list|)
condition|)
block|{
name|filtered
operator|.
name|addTest
argument_list|(
name|test
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filtered
return|;
block|}
DECL|method|formatFeatureSet (Set<? extends Feature<?>> features)
specifier|protected
specifier|static
name|String
name|formatFeatureSet
parameter_list|(
name|Set
argument_list|<
name|?
extends|extends
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|temp
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Feature
argument_list|<
name|?
argument_list|>
name|feature
range|:
name|features
control|)
block|{
name|Object
name|featureAsObject
init|=
name|feature
decl_stmt|;
comment|// to work around bogus JDK warning
if|if
condition|(
name|featureAsObject
operator|instanceof
name|Enum
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|f
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|featureAsObject
decl_stmt|;
name|temp
operator|.
name|add
argument_list|(
name|Platform
operator|.
name|classGetSimpleName
argument_list|(
name|f
operator|.
name|getDeclaringClass
argument_list|()
argument_list|)
operator|+
literal|"."
operator|+
name|feature
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|temp
operator|.
name|add
argument_list|(
name|feature
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|temp
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

