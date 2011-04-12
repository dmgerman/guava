begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Static methods for implementing hash-based collections.  *  * @author Kevin Bourrillion  * @author Jesse Wilson  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Hashing
specifier|final
class|class
name|Hashing
block|{
DECL|method|Hashing ()
specifier|private
name|Hashing
parameter_list|()
block|{}
comment|/*    * This method was written by Doug Lea with assistance from members of JCP    * JSR-166 Expert Group and released to the public domain, as explained at    * http://creativecommons.org/licenses/publicdomain    *     * As of 2010/06/11, this method is identical to the (package private) hash    * method in OpenJDK 7's java.util.HashMap class.    */
DECL|method|smear (int hashCode)
specifier|static
name|int
name|smear
parameter_list|(
name|int
name|hashCode
parameter_list|)
block|{
name|hashCode
operator|^=
operator|(
name|hashCode
operator|>>>
literal|20
operator|)
operator|^
operator|(
name|hashCode
operator|>>>
literal|12
operator|)
expr_stmt|;
return|return
name|hashCode
operator|^
operator|(
name|hashCode
operator|>>>
literal|7
operator|)
operator|^
operator|(
name|hashCode
operator|>>>
literal|4
operator|)
return|;
block|}
block|}
end_class

end_unit

