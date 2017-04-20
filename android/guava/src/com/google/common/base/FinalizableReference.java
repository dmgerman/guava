begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|GwtIncompatible
import|;
end_import

begin_comment
comment|/**  * Implemented by references that have code to run after garbage collection of their referents.  *  * @see FinalizableReferenceQueue  * @author Bob Lee  * @since 2.0  */
end_comment

begin_interface
annotation|@
name|GwtIncompatible
DECL|interface|FinalizableReference
specifier|public
interface|interface
name|FinalizableReference
block|{
comment|/**    * Invoked on a background thread after the referent has been garbage collected unless security    * restrictions prevented starting a background thread, in which case this method is invoked when    * new references are created.    */
DECL|method|finalizeReferent ()
name|void
name|finalizeReferent
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

