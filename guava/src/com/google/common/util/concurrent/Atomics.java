begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Beta
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReferenceArray
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Static utility methods pertaining to classes in the  * {@code java.util.concurrent.atomic} package.  *  * @author Kurt Alfred Kluever  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|Atomics
specifier|public
specifier|final
class|class
name|Atomics
block|{
DECL|method|Atomics ()
specifier|private
name|Atomics
parameter_list|()
block|{}
comment|/**    * Creates an {@code AtomicReference} instance with no initial value.    *    * @return a new {@code AtomicReference} with no initial value    */
DECL|method|newReference ()
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|AtomicReference
argument_list|<
name|V
argument_list|>
name|newReference
parameter_list|()
block|{
return|return
operator|new
name|AtomicReference
argument_list|<
name|V
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * Creates an {@code AtomicReference} instance with the given initial value.    *    * @param initialValue the initial value    * @return a new {@code AtomicReference} with the given initial value    */
DECL|method|newReference (@ullable V initialValue)
specifier|public
specifier|static
parameter_list|<
name|V
parameter_list|>
name|AtomicReference
argument_list|<
name|V
argument_list|>
name|newReference
parameter_list|(
annotation|@
name|Nullable
name|V
name|initialValue
parameter_list|)
block|{
return|return
operator|new
name|AtomicReference
argument_list|<
name|V
argument_list|>
argument_list|(
name|initialValue
argument_list|)
return|;
block|}
comment|/**    * Creates an {@code AtomicReferenceArray} instance of given length.    *    * @param length the length of the array    * @return a new {@code AtomicReferenceArray} with the given length    */
DECL|method|newReferenceArray (int length)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|AtomicReferenceArray
argument_list|<
name|E
argument_list|>
name|newReferenceArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|AtomicReferenceArray
argument_list|<
name|E
argument_list|>
argument_list|(
name|length
argument_list|)
return|;
block|}
comment|/**    * Creates an {@code AtomicReferenceArray} instance with the same length as,    * and all elements copied from, the given array.    *    * @param array the array to copy elements from    * @return a new {@code AtomicReferenceArray} copied from the given array    */
DECL|method|newReferenceArray (E[] array)
specifier|public
specifier|static
parameter_list|<
name|E
parameter_list|>
name|AtomicReferenceArray
argument_list|<
name|E
argument_list|>
name|newReferenceArray
parameter_list|(
name|E
index|[]
name|array
parameter_list|)
block|{
return|return
operator|new
name|AtomicReferenceArray
argument_list|<
name|E
argument_list|>
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
end_class

end_unit

