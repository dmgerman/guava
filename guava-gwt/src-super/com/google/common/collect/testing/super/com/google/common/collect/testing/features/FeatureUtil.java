begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Minimal stubs to keep the collection test suite builder compilable in GWT.  *  * @author Hayward Chan  */
end_comment

begin_class
DECL|class|FeatureUtil
specifier|public
class|class
name|FeatureUtil
block|{
DECL|method|addImpliedFeatures (Set<Feature<?>> features)
specifier|public
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|addImpliedFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called in GWT."
argument_list|)
throw|;
block|}
DECL|method|impliedFeatures (Set<Feature<?>> features)
specifier|public
specifier|static
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|impliedFeatures
parameter_list|(
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
argument_list|>
argument_list|>
name|features
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called in GWT."
argument_list|)
throw|;
block|}
DECL|method|getTesterRequirements (Class<?> testerClass)
specifier|public
specifier|static
name|TesterRequirements
name|getTesterRequirements
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testerClass
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called in GWT."
argument_list|)
throw|;
block|}
DECL|method|getTesterRequirements (Method testerMethod)
specifier|public
specifier|static
name|TesterRequirements
name|getTesterRequirements
parameter_list|(
name|Method
name|testerMethod
parameter_list|)
throws|throws
name|ConflictingRequirementsException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Should not be called in GWT."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

