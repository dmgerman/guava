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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
operator|.
name|reserialize
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|junit
operator|.
name|framework
operator|.
name|Assert
operator|.
name|assertTrue
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|SerializableTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
annotation|@
name|GwtIncompatible
argument_list|(
literal|"SerializableTester"
argument_list|)
DECL|method|reserializeAndAssertLenient (Set<E> original)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|reserializeAndAssertLenient
parameter_list|(
name|Set
argument_list|<
name|E
argument_list|>
name|original
parameter_list|)
block|{
name|Set
argument_list|<
name|E
argument_list|>
name|copy
init|=
name|reserialize
argument_list|(
name|original
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|original
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|copy
operator|instanceof
name|ImmutableSet
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|LenientSerializableTester ()
specifier|private
name|LenientSerializableTester
parameter_list|()
block|{}
block|}
end_class

end_unit

