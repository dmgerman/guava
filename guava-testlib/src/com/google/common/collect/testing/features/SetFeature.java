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
name|Set
import|;
end_import

begin_comment
comment|/**  * Optional features of classes derived from {@code Set}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
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
DECL|enum|SetFeature
specifier|public
enum|enum
name|SetFeature
implements|implements
name|Feature
argument_list|<
name|Set
argument_list|>
block|{
DECL|enumConstant|GENERAL_PURPOSE
name|GENERAL_PURPOSE
parameter_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
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
name|Set
argument_list|>
argument_list|>
name|implied
decl_stmt|;
DECL|method|SetFeature (Feature<? super Set> .... implied)
name|SetFeature
parameter_list|(
name|Feature
argument_list|<
name|?
super|super
name|Set
argument_list|>
modifier|...
name|implied
parameter_list|)
block|{
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
name|Set
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
specifier|public
annotation_defn|@interface
name|Require
block|{
DECL|method|value ()
specifier|public
specifier|abstract
name|SetFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
block|;
DECL|method|absent ()
specifier|public
specifier|abstract
name|SetFeature
index|[]
name|absent
argument_list|()
expr|default
block|{}
block|;   }
block|}
end_enum

end_unit

