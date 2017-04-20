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

begin_comment
comment|/**  * No-op null-pointer test for {@link LongAdder} to override the {@link PackageSanityTests} version,  * which checks package-private methods that we don't want to have to annotate as {@code Nullable}  * because we don't want diffs from jsr166e.  */
end_comment

begin_class
DECL|class|LongAdderTest
specifier|public
class|class
name|LongAdderTest
block|{
DECL|method|testNulls ()
specifier|public
name|void
name|testNulls
parameter_list|()
block|{}
block|}
end_class

end_unit

