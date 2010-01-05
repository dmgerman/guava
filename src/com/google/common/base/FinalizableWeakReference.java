begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|lang
operator|.
name|ref
operator|.
name|WeakReference
import|;
end_import

begin_comment
comment|/**  * Weak reference with a {@code finalizeReferent()} method which a background  * thread invokes after the garbage collector reclaims the referent. This is a  * simpler alternative to using a {@link java.lang.ref.ReferenceQueue}.  *  * @author Bob Lee  */
end_comment

begin_class
DECL|class|FinalizableWeakReference
specifier|public
specifier|abstract
class|class
name|FinalizableWeakReference
parameter_list|<
name|T
parameter_list|>
extends|extends
name|WeakReference
argument_list|<
name|T
argument_list|>
implements|implements
name|FinalizableReference
block|{
comment|/**    * Constructs a new finalizable weak reference.    *    * @param referent to weakly reference    * @param queue that should finalize the referent    */
DECL|method|FinalizableWeakReference (T referent, FinalizableReferenceQueue queue)
specifier|protected
name|FinalizableWeakReference
parameter_list|(
name|T
name|referent
parameter_list|,
name|FinalizableReferenceQueue
name|queue
parameter_list|)
block|{
name|super
argument_list|(
name|referent
argument_list|,
name|queue
operator|.
name|queue
argument_list|)
expr_stmt|;
name|queue
operator|.
name|cleanUp
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

