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
name|testing
operator|.
name|Helpers
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
name|Set
import|;
end_import

begin_comment
comment|/**  * When describing the features of the collection produced by a given generator  * (i.e. in a call to {@link  * com.google.common.collect.testing.FeatureSpecificTestSuiteBuilder#withFeatures(Feature...)}),  * this annotation specifies each of the different sizes for which a test suite  * should be built. (In a typical case, the features should include {@link  * CollectionSize#ANY}.) These semantics are thus a little different  * from those of other Collection-related features such as {@link  * CollectionFeature} or {@link SetFeature}.  *<p>  * However, when {@link CollectionSize.Require} is used to annotate a test it  * behaves normally (i.e. it requires the collection instance under test to be  * a certain size for the test to run). Note that this means a test should not  * require more than one CollectionSize, since a particular collection instance  * can only be one size at once.  *  * @author George van den Driessche  */
end_comment

begin_comment
comment|// Enum values use constructors with generic varargs.
end_comment

begin_enum
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|GwtCompatible
DECL|enum|CollectionSize
specifier|public
enum|enum
name|CollectionSize
implements|implements
name|Feature
argument_list|<
name|Collection
argument_list|>
implements|,
name|Comparable
argument_list|<
name|CollectionSize
argument_list|>
block|{
comment|/** Test an empty collection. */
DECL|enumConstant|ZERO
name|ZERO
argument_list|(
literal|0
argument_list|)
block|,
comment|/** Test a one-element collection. */
DECL|enumConstant|ONE
name|ONE
argument_list|(
literal|1
argument_list|)
block|,
comment|/** Test a three-element collection. */
DECL|enumConstant|SEVERAL
name|SEVERAL
argument_list|(
literal|3
argument_list|)
block|,
comment|/*    * TODO: add VERY_LARGE, noting that we currently assume that the fourth    * sample element is not in any collection    */
DECL|enumConstant|ANY
name|ANY
parameter_list|(
name|ZERO
parameter_list|,
name|ONE
parameter_list|,
name|SEVERAL
parameter_list|)
constructor_decl|;
DECL|field|implied
specifier|private
specifier|final
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|Collection
argument_list|>
argument_list|>
name|implied
decl_stmt|;
DECL|field|numElements
specifier|private
specifier|final
name|Integer
name|numElements
decl_stmt|;
DECL|method|CollectionSize (int numElements)
name|CollectionSize
parameter_list|(
name|int
name|numElements
parameter_list|)
block|{
name|this
operator|.
name|implied
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
name|this
operator|.
name|numElements
operator|=
name|numElements
expr_stmt|;
block|}
DECL|method|CollectionSize (Feature<? super Collection>.... implied)
name|CollectionSize
parameter_list|(
name|Feature
argument_list|<
name|?
super|super
name|Collection
argument_list|>
modifier|...
name|implied
parameter_list|)
block|{
comment|// Keep the order here, so that PerCollectionSizeTestSuiteBuilder
comment|// gives a predictable order of test suites.
name|this
operator|.
name|implied
operator|=
name|Helpers
operator|.
name|copyToSet
argument_list|(
name|implied
argument_list|)
expr_stmt|;
name|this
operator|.
name|numElements
operator|=
literal|null
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
name|Collection
argument_list|>
argument_list|>
name|getImpliedFeatures
parameter_list|()
block|{
return|return
name|implied
return|;
block|}
DECL|method|getNumElements ()
specifier|public
name|int
name|getNumElements
parameter_list|()
block|{
if|if
condition|(
name|numElements
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"A compound CollectionSize doesn't specify a number of elements."
argument_list|)
throw|;
block|}
return|return
name|numElements
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
specifier|public
annotation_defn|@interface
name|Require
block|{
DECL|method|value ()
name|CollectionSize
index|[]
name|value
argument_list|()
expr|default
block|{}
block|;
DECL|method|absent ()
name|CollectionSize
index|[]
name|absent
argument_list|()
expr|default
block|{}
block|;   }
block|}
end_enum

end_unit

