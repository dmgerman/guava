begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
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
comment|/**  * Outer class that exists solely to let us write {@code Partially.GwtIncompatible} instead of plain  * {@code GwtIncompatible}. This is more accurate for {@link Futures#catching}, which is available  * under GWT but with a slightly different signature.  *  *<p>We can't use {@code PartiallyGwtIncompatible} because then the GWT compiler wouldn't recognize  * it as a {@code GwtIncompatible} annotation. And for {@code Futures.catching}, we need the GWT  * compiler to autostrip the normal server method in order to expose the special, inherited GWT  * version.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Partially
specifier|final
class|class
name|Partially
block|{
comment|/**    * The presence of this annotation on an API indicates that the method<i>may</i> be used with the    *<a href="http://www.gwtproject.org/">Google Web Toolkit</a> (GWT) but that it has<i>some    * restrictions</i>.    */
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
name|CONSTRUCTOR
block|,
name|ElementType
operator|.
name|FIELD
block|}
argument_list|)
annotation|@
name|Documented
DECL|annotation|GwtIncompatible
annotation_defn|@interface
name|GwtIncompatible
block|{
DECL|method|value ()
name|String
name|value
parameter_list|()
function_decl|;
block|}
DECL|method|Partially ()
specifier|private
name|Partially
parameter_list|()
block|{}
block|}
end_class

end_unit

