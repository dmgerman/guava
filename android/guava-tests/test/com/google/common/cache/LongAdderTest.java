begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/** Unit tests for {@link LongAdder}. */
end_comment

begin_class
DECL|class|LongAdderTest
specifier|public
class|class
name|LongAdderTest
extends|extends
name|TestCase
block|{
comment|/**    * No-op null-pointer test for {@link LongAdder} to override the {@link PackageSanityTests}    * version, which checks package-private methods that we don't want to have to annotate as {@code    * Nullable} because we don't want diffs from jsr166e.    */
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{}
DECL|method|testOverflows ()
specifier|public
name|void
name|testOverflows
parameter_list|()
block|{
name|LongAdder
name|longAdder
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
name|longAdder
operator|.
name|add
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|longAdder
operator|.
name|sum
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
name|longAdder
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// silently overflows; is this a bug?
comment|// See https://github.com/google/guava/issues/3503
name|assertThat
argument_list|(
name|longAdder
operator|.
name|sum
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|-
literal|9223372036854775808L
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

