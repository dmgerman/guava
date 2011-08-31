begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * The presence of this annotation on a type indicates that the type may be  * used with the  *<a href="http://code.google.com/webtoolkit/">Google Web Toolkit</a> (GWT).  * When applied to a method, the return type of the method is GWT compatible.  * It's useful to indicate that an instance created by factory methods has a GWT  * serializable type.  In the following example,  *  *<pre style="code">  * {@literal @}GwtCompatible  * class Lists {  *   ...  *   {@literal @}GwtCompatible(serializable = true)  *   static&lt;E> List&lt;E> newArrayList(E... elements) {  *     ...  *   }  * }  *</pre>  * The return value of {@code Lists.newArrayList(E[])} has GWT  * serializable type.  It is also useful in specifying contracts of interface  * methods.  In the following example,  *  *<pre style="code">  * {@literal @}GwtCompatible  * interface ListFactory {  *   ...  *   {@literal @}GwtCompatible(serializable = true)  *&lt;E> List&lt;E> newArrayList(E... elements);  * }  *</pre>  * The {@code newArrayList(E[])} method of all implementations of {@code  * ListFactory} is expected to return a value with a GWT serializable type.  *  *<p>Note that a {@code GwtCompatible} type may have some {@link  * GwtIncompatible} methods.  *  * @author Charles Fry  * @author Hayward Chan  */
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
block|}
argument_list|)
annotation|@
name|Documented
annotation|@
name|GwtCompatible
DECL|annotation|GwtCompatible
specifier|public
annotation_defn|@interface
name|GwtCompatible
block|{
comment|/**    * When {@code true}, the annotated type or the type of the method return    * value is GWT serializable.    *    * @see<a href="http://code.google.com/webtoolkit/doc/latest/DevGuideServerCommunication.html#DevGuideSerializableTypes">    *     Documentation about GWT serialization</a>    */
DECL|method|serializable ()
DECL|field|false
name|boolean
name|serializable
parameter_list|()
default|default
literal|false
function_decl|;
comment|/**    * When {@code true}, the annotated type is emulated in GWT. The emulated    * source (also known as super-source) is different from the implementation    * used by the JVM.    *    * @see<a href="http://code.google.com/webtoolkit/doc/latest/DevGuideOrganizingProjects.html#DevGuideModules">    *     Documentation about GWT emulated source</a>    */
DECL|method|emulated ()
DECL|field|false
name|boolean
name|emulated
parameter_list|()
default|default
literal|false
function_decl|;
block|}
end_annotation_defn

end_unit

