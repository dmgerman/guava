begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

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
comment|/**  * Common interface for {@link Holder} and {@link Optional}; most users should have no  * need to refer to this type directly.  *  * @author Kevin Bourrillion  */
end_comment

begin_interface
annotation|@
name|GwtCompatible
DECL|interface|BaseHolder
interface|interface
name|BaseHolder
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns {@code true} if this holder contains a (non-null) instance.    */
DECL|method|isPresent ()
name|boolean
name|isPresent
parameter_list|()
function_decl|;
comment|// TODO(kevinb): isAbsent too?
comment|/**    * Returns the contained instance, which must be present. If the instance might be    * absent, use {@link #or(Object)} or {@link #orNull} instead.    *    * @throws IllegalStateException if the instance is absent ({@link #isPresent} returns    *     {@code false})    */
DECL|method|get ()
name|T
name|get
parameter_list|()
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code defaultValue} otherwise. If    * no default value should be required because the instance is known to be present, use    * {@link #get()} instead. For a default value of {@code null}, use {@link #orNull}.    */
DECL|method|or (T defaultValue)
name|T
name|or
parameter_list|(
name|T
name|defaultValue
parameter_list|)
function_decl|;
comment|/**    * Returns the contained instance if it is present; {@code null} otherwise. If the    * instance is known to be present, use {@link #get()} instead.    */
DECL|method|orNull ()
annotation|@
name|Nullable
name|T
name|orNull
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

