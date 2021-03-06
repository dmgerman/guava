begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.features
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
operator|.
name|features
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
name|Sets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
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
name|Collections
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/** @author George van den Driessche */
end_comment

begin_comment
comment|// Enum values use constructors with generic varargs.
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|FeatureUtilTest
specifier|public
class|class
name|FeatureUtilTest
extends|extends
name|TestCase
block|{
DECL|interface|ExampleBaseInterface
interface|interface
name|ExampleBaseInterface
block|{
DECL|method|behave ()
name|void
name|behave
parameter_list|()
function_decl|;
block|}
DECL|interface|ExampleDerivedInterface
interface|interface
name|ExampleDerivedInterface
extends|extends
name|ExampleBaseInterface
block|{
DECL|method|misbehave ()
name|void
name|misbehave
parameter_list|()
function_decl|;
block|}
DECL|enum|ExampleBaseFeature
enum|enum
name|ExampleBaseFeature
implements|implements
name|Feature
argument_list|<
name|ExampleBaseInterface
argument_list|>
block|{
DECL|enumConstant|BASE_FEATURE_1
name|BASE_FEATURE_1
block|,
DECL|enumConstant|BASE_FEATURE_2
name|BASE_FEATURE_2
block|;
annotation|@
name|Override
DECL|method|getImpliedFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|ExampleBaseInterface
argument_list|>
argument_list|>
name|getImpliedFeatures
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Inherited
annotation|@
name|TesterAnnotation
DECL|annotation|Require
annotation_defn|@interface
name|Require
block|{
DECL|method|value ()
name|ExampleBaseFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
block|;
DECL|method|absent ()
name|ExampleBaseFeature
index|[]
name|absent
argument_list|()
expr|default
block|{}
block|;     }
block|}
DECL|enum|ExampleDerivedFeature
enum|enum
name|ExampleDerivedFeature
implements|implements
name|Feature
argument_list|<
name|ExampleDerivedInterface
argument_list|>
block|{
DECL|enumConstant|DERIVED_FEATURE_1
name|DERIVED_FEATURE_1
block|,
DECL|enumConstant|DERIVED_FEATURE_2
name|DERIVED_FEATURE_2
parameter_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
parameter_list|)
operator|,
DECL|enumConstant|DERIVED_FEATURE_3
constructor|DERIVED_FEATURE_3
operator|,
DECL|enumConstant|COMPOUND_DERIVED_FEATURE
constructor|COMPOUND_DERIVED_FEATURE(         DERIVED_FEATURE_1
operator|,
constructor|DERIVED_FEATURE_2
operator|,
constructor|ExampleBaseFeature.BASE_FEATURE_2
block|)
enum|;
DECL|field|implied
specifier|private
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|ExampleDerivedInterface
argument_list|>
argument_list|>
name|implied
decl_stmt|;
DECL|method|ExampleDerivedFeature (Feature<? super ExampleDerivedInterface>.... implied)
name|ExampleDerivedFeature
parameter_list|(
name|Feature
argument_list|<
name|?
super|super
name|ExampleDerivedInterface
argument_list|>
modifier|...
name|implied
parameter_list|)
block|{
name|this
operator|.
name|implied
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|implied
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getImpliedFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|ExampleDerivedInterface
argument_list|>
argument_list|>
name|getImpliedFeatures
parameter_list|()
block|{
return|return
name|implied
return|;
block|}
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Inherited
annotation|@
name|TesterAnnotation
DECL|annotation|Require
annotation_defn|@interface
name|Require
block|{
DECL|method|value ()
name|ExampleDerivedFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
expr_stmt|;
DECL|method|absent ()
name|ExampleDerivedFeature
index|[]
name|absent
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
block|}
end_class

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
DECL|annotation|NonTesterAnnotation
annotation_defn|@interface
name|NonTesterAnnotation
block|{}
end_annotation_defn

begin_class
annotation|@
name|ExampleBaseFeature
operator|.
name|Require
argument_list|(
block|{
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
block|}
argument_list|)
DECL|class|ExampleBaseInterfaceTester
specifier|private
specifier|abstract
specifier|static
class|class
name|ExampleBaseInterfaceTester
extends|extends
name|TestCase
block|{
DECL|method|doNotActuallyRunThis ()
specifier|protected
specifier|final
name|void
name|doNotActuallyRunThis
parameter_list|()
block|{
name|fail
argument_list|(
literal|"Nobody's meant to actually run this!"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_class
annotation|@
name|AndroidIncompatible
comment|// Android attempts to run directly
annotation|@
name|NonTesterAnnotation
annotation|@
name|ExampleDerivedFeature
operator|.
name|Require
argument_list|(
block|{
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
block|}
argument_list|)
DECL|class|ExampleDerivedInterfaceTester
specifier|private
specifier|static
class|class
name|ExampleDerivedInterfaceTester
extends|extends
name|ExampleBaseInterfaceTester
block|{
comment|// Exists to test that our framework doesn't run it:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|ExampleDerivedFeature
operator|.
name|Require
argument_list|(
block|{
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
block|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
block|}
argument_list|)
DECL|method|testRequiringTwoExplicitDerivedFeatures ()
specifier|public
name|void
name|testRequiringTwoExplicitDerivedFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|doNotActuallyRunThis
argument_list|()
expr_stmt|;
block|}
comment|// Exists to test that our framework doesn't run it:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|ExampleDerivedFeature
operator|.
name|Require
argument_list|(
block|{
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
block|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_3
block|}
argument_list|)
DECL|method|testRequiringAllThreeDerivedFeatures ()
specifier|public
name|void
name|testRequiringAllThreeDerivedFeatures
parameter_list|()
block|{
name|doNotActuallyRunThis
argument_list|()
expr_stmt|;
block|}
comment|// Exists to test that our framework doesn't run it:
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|ExampleBaseFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
block|}
argument_list|)
DECL|method|testRequiringConflictingFeatures ()
specifier|public
name|void
name|testRequiringConflictingFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|doNotActuallyRunThis
argument_list|()
expr_stmt|;
block|}
block|}
end_class

begin_class
annotation|@
name|ExampleDerivedFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
block|{
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
block|}
argument_list|)
DECL|class|ConflictingRequirementsExampleDerivedInterfaceTester
specifier|private
specifier|static
class|class
name|ConflictingRequirementsExampleDerivedInterfaceTester
extends|extends
name|ExampleBaseInterfaceTester
block|{}
end_class

begin_function
DECL|method|testTestFeatureEnums ()
specifier|public
name|void
name|testTestFeatureEnums
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Haha! Let's test our own test rig!
name|FeatureEnumTest
operator|.
name|assertGoodFeatureEnum
argument_list|(
name|FeatureUtilTest
operator|.
name|ExampleBaseFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|FeatureEnumTest
operator|.
name|assertGoodFeatureEnum
argument_list|(
name|FeatureUtilTest
operator|.
name|ExampleDerivedFeature
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|testAddImpliedFeatures_returnsSameSetInstance ()
specifier|public
name|void
name|testAddImpliedFeatures_returnsSameSetInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|features
argument_list|,
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|testAddImpliedFeatures_addsImpliedFeatures ()
specifier|public
name|void
name|testAddImpliedFeatures_addsImpliedFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
decl_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|)
expr_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|,
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
expr_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|COMPOUND_DERIVED_FEATURE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|ExampleDerivedFeature
operator|.
name|COMPOUND_DERIVED_FEATURE
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|,
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|,
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_2
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|testImpliedFeatures_returnsNewSetInstance ()
specifier|public
name|void
name|testImpliedFeatures_returnsNewSetInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
init|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|features
argument_list|,
name|FeatureUtil
operator|.
name|impliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|testImpliedFeatures_returnsImpliedFeatures ()
specifier|public
name|void
name|testImpliedFeatures_returnsImpliedFeatures
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
decl_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|FeatureUtil
operator|.
name|impliedFeatures
argument_list|(
name|features
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FeatureUtil
operator|.
name|impliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
expr_stmt|;
name|features
operator|=
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|COMPOUND_DERIVED_FEATURE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|FeatureUtil
operator|.
name|impliedFeatures
argument_list|(
name|features
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|,
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|,
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_2
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|AndroidIncompatible
comment|// Android runs ExampleDerivedInterfaceTester directly if it exists
DECL|method|testBuildTesterRequirements_class ()
specifier|public
name|void
name|testBuildTesterRequirements_class
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|ExampleBaseInterfaceTester
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|TesterRequirements
argument_list|(
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
operator|,
name|Collections
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|ExampleDerivedInterfaceTester
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|TesterRequirements
argument_list|(
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|)
operator|,
name|Collections
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|AndroidIncompatible
comment|// Android runs ExampleDerivedInterfaceTester directly if it exists
DECL|method|testBuildTesterRequirements_method ()
specifier|public
name|void
name|testBuildTesterRequirements_method
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|ExampleDerivedInterfaceTester
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testRequiringTwoExplicitDerivedFeatures"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TesterRequirements
argument_list|(
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|)
operator|,
name|Collections
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|ExampleDerivedInterfaceTester
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testRequiringAllThreeDerivedFeatures"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TesterRequirements
argument_list|(
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_3
argument_list|)
operator|,
name|Collections
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|AndroidIncompatible
comment|// Android runs ExampleDerivedInterfaceTester directly if it exists
DECL|method|testBuildTesterRequirements_classClassConflict ()
specifier|public
name|void
name|testBuildTesterRequirements_classClassConflict
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|ConflictingRequirementsExampleDerivedInterfaceTester
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConflictingRequirementsException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConflictingRequirementsException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getConflicts
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConflictingRequirementsExampleDerivedInterfaceTester
operator|.
name|class
argument_list|,
name|e
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|AndroidIncompatible
comment|// Android runs ExampleDerivedInterfaceTester directly if it exists
DECL|method|testBuildTesterRequirements_methodClassConflict ()
specifier|public
name|void
name|testBuildTesterRequirements_methodClassConflict
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Method
name|method
init|=
name|ExampleDerivedInterfaceTester
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testRequiringConflictingFeatures"
argument_list|)
decl_stmt|;
try|try
block|{
name|FeatureUtil
operator|.
name|buildTesterRequirements
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ConflictingRequirementsException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConflictingRequirementsException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getConflicts
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
name|ExampleBaseFeature
operator|.
name|BASE_FEATURE_1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|method
argument_list|,
name|e
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|AndroidIncompatible
comment|// Android runs ExampleDerivedInterfaceTester directly if it exists
DECL|method|testBuildDeclaredTesterRequirements ()
specifier|public
name|void
name|testBuildDeclaredTesterRequirements
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|FeatureUtil
operator|.
name|buildDeclaredTesterRequirements
argument_list|(
name|ExampleDerivedInterfaceTester
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"testRequiringTwoExplicitDerivedFeatures"
argument_list|)
argument_list|)
argument_list|,
operator|new
name|TesterRequirements
argument_list|(
name|FeatureUtil
operator|.
name|addImpliedFeatures
argument_list|(
name|Sets
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|newHashSet
argument_list|(
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_1
argument_list|,
name|ExampleDerivedFeature
operator|.
name|DERIVED_FEATURE_2
argument_list|)
argument_list|)
argument_list|,
name|Collections
operator|.
expr|<
name|Feature
argument_list|<
name|?
argument_list|>
operator|>
name|emptySet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

