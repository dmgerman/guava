begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/** An ordering that uses the reverse of the natural order of the values. */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|)
DECL|class|UsingToStringOrdering
specifier|final
class|class
name|UsingToStringOrdering
extends|extends
name|Ordering
argument_list|<
name|Object
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|UsingToStringOrdering
name|INSTANCE
init|=
operator|new
name|UsingToStringOrdering
argument_list|()
decl_stmt|;
DECL|method|compare (Object left, Object right)
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|left
parameter_list|,
name|Object
name|right
parameter_list|)
block|{
return|return
name|left
operator|.
name|toString
argument_list|()
operator|.
name|compareTo
argument_list|(
name|right
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|// preserve singleton-ness, so equals() and hashCode() work correctly
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Ordering.usingToString()"
return|;
block|}
DECL|method|UsingToStringOrdering ()
specifier|private
name|UsingToStringOrdering
parameter_list|()
block|{}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
end_class

end_unit

