begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.annotations
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
package|;
end_package

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
name|ElementType
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
comment|/**  * The presence of this annotation on a method indicates that the method may  *<em>not</em> be used with the  *<a href="http://code.google.com/webtoolkit/">Google Web Toolkit</a> (GWT),  * even though its type is annotated as {@link GwtCompatible} and accessible in  * GWT.  They can cause GWT compilation errors or simply unexpected exceptions  * when used in GWT.  *  *<p>Note that this annotation should only be applied to methods, fields, or  * inner classes of types which are annotated as {@link GwtCompatible}.  *  * @author Charles Fry  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|CLASS
argument_list|)
annotation|@
name|Target
argument_list|(
block|{
name|ElementType
operator|.
name|TYPE
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|FIELD
block|}
argument_list|)
annotation|@
name|Documented
annotation|@
name|GwtCompatible
DECL|annotation|GwtIncompatible
specifier|public
annotation_defn|@interface
name|GwtIncompatible
block|{
comment|/**    * Describes why the annotated element is incompatible with GWT. Since this is    * generally due to a dependence on a type/method which GWT doesn't support,    * it is sufficient to simply reference the unsupported type/method. E.g.    * "Class.isInstance".    */
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
block|}
end_annotation_defn

end_unit

