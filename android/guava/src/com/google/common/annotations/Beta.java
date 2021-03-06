begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
comment|/**  * Signifies that a public API (public class, method or field) is subject to incompatible changes,  * or even removal, in a future release. An API bearing this annotation is exempt from any  * compatibility guarantees made by its containing library. Note that the presence of this  * annotation implies nothing about the quality or performance of the API in question, only the fact  * that it is not "API-frozen."  *  *<p>It is generally safe for<i>applications</i> to depend on beta APIs, at the cost of some extra  * work during upgrades. However it is generally inadvisable for<i>libraries</i> (which get  * included on users' CLASSPATHs, outside the library developers' control) to do so.  *  * @author Kevin Bourrillion  */
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
name|ANNOTATION_TYPE
block|,
name|ElementType
operator|.
name|CONSTRUCTOR
block|,
name|ElementType
operator|.
name|FIELD
block|,
name|ElementType
operator|.
name|METHOD
block|,
name|ElementType
operator|.
name|TYPE
block|}
argument_list|)
annotation|@
name|Documented
annotation|@
name|GwtCompatible
DECL|annotation|Beta
specifier|public
annotation_defn|@interface
name|Beta
block|{}
end_annotation_defn

end_unit

