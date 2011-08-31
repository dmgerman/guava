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
comment|/**  * Optional features of classes derived from {@code List}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
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
DECL|enum|ListFeature
specifier|public
enum|enum
name|ListFeature
implements|implements
name|Feature
argument_list|<
name|List
argument_list|>
block|{
DECL|enumConstant|SUPPORTS_SET
name|SUPPORTS_SET
block|,
DECL|enumConstant|SUPPORTS_ADD_WITH_INDEX
name|SUPPORTS_ADD_WITH_INDEX
block|,
DECL|enumConstant|SUPPORTS_ADD_ALL_WITH_INDEX
name|SUPPORTS_ADD_ALL_WITH_INDEX
block|,
DECL|enumConstant|SUPPORTS_REMOVE_WITH_INDEX
name|SUPPORTS_REMOVE_WITH_INDEX
block|,
DECL|enumConstant|GENERAL_PURPOSE
name|GENERAL_PURPOSE
parameter_list|(
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
parameter_list|,
name|SUPPORTS_SET
parameter_list|,
name|SUPPORTS_ADD_WITH_INDEX
parameter_list|,
name|SUPPORTS_ADD_ALL_WITH_INDEX
parameter_list|,
name|SUPPORTS_REMOVE_WITH_INDEX
parameter_list|)
operator|,
comment|/** Features supported by lists where only removal is allowed. */
DECL|enumConstant|REMOVE_OPERATIONS
constructor|REMOVE_OPERATIONS(       CollectionFeature.REMOVE_OPERATIONS
operator|,
constructor|SUPPORTS_REMOVE_WITH_INDEX
block|)
enum|;
end_enum

begin_decl_stmt
DECL|field|implied
specifier|private
specifier|final
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|List
argument_list|>
argument_list|>
name|implied
decl_stmt|;
end_decl_stmt

begin_expr_stmt
DECL|method|ListFeature (Feature<? super List> .... implied)
name|ListFeature
argument_list|(
name|Feature
argument_list|<
name|?
super|super
name|List
argument_list|>
operator|...
name|implied
argument_list|)
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
block|;   }
expr|@
name|Override
DECL|method|getImpliedFeatures ()
specifier|public
name|Set
argument_list|<
name|Feature
argument_list|<
name|?
super|super
name|List
argument_list|>
argument_list|>
name|getImpliedFeatures
argument_list|()
block|{
return|return
name|implied
return|;
block|}
end_expr_stmt

begin_annotation_defn
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
name|ListFeature
index|[]
name|value
argument_list|()
expr|default
block|{}
expr_stmt|;
DECL|method|absent ()
name|ListFeature
index|[]
name|absent
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

unit|}
end_unit

