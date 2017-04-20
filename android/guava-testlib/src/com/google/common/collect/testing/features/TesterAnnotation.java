begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
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
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Use this to meta-annotate {@code XxxFeature.Require} annotations, so that those annotations can  * be used to decide whether to apply a test to a given class-under-test.  *  *<p>This is needed because annotations can't implement interfaces, which is also why reflection is  * used to extract values from the properties of the various annotations.  *  * @author George van den Driessche  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
name|value
operator|=
block|{
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|ANNOTATION_TYPE
block|}
argument_list|)
annotation|@
name|Retention
argument_list|(
name|value
operator|=
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|GwtCompatible
DECL|annotation|TesterAnnotation
specifier|public
annotation_defn|@interface
name|TesterAnnotation
block|{}
end_annotation_defn

end_unit

