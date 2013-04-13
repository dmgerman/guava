begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Variant of {@link SerializableTester} that does not require the reserialized object's class to be  * identical to the original.  *  * @author Chris Povirk  */
end_comment

begin_comment
comment|/*  * The whole thing is really @GwtIncompatible, but GwtJUnitConvertedTestModule doesn't have a  * parameter for non-GWT, non-test files, and it didn't seem worth adding one for this unusual case.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|LenientSerializableTester
specifier|final
class|class
name|LenientSerializableTester
block|{
comment|/*    * TODO(cpovirk): move this to c.g.c.testing if we allow for c.g.c.annotations dependencies so    * that it can be GWTified?    */
DECL|method|LenientSerializableTester ()
specifier|private
name|LenientSerializableTester
parameter_list|()
block|{}
block|}
end_class

end_unit

