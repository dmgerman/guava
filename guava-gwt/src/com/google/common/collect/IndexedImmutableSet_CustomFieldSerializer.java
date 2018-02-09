begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Dummy serializer. Otherwise, GWT, in processing JdkBackedImmutableSet -- even though that class  * has a custom field serializer -- would generate its own version of this class, implemented in  * terms of calls to ImmutableSet_CustomFieldSerializer, which is itself a dummy that we've  * provided. That produces GWT compilation errors, albeit ones that are non-fatal (even with -strict  * on, oddly).  */
end_comment

begin_class
DECL|class|IndexedImmutableSet_CustomFieldSerializer
specifier|public
specifier|final
class|class
name|IndexedImmutableSet_CustomFieldSerializer
block|{}
end_class

end_unit

