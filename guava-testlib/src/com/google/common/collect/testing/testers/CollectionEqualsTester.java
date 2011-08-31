begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.testers
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
operator|.
name|testers
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
name|collect
operator|.
name|testing
operator|.
name|AbstractCollectionTester
import|;
end_import

begin_comment
comment|/**  * Tests {@link java.util.Collection#equals}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|CollectionEqualsTester
specifier|public
class|class
name|CollectionEqualsTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testEquals_self ()
specifier|public
name|void
name|testEquals_self
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|"An Object should be equal to itself."
argument_list|,
name|collection
operator|.
name|equals
argument_list|(
name|collection
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_null ()
specifier|public
name|void
name|testEquals_null
parameter_list|()
block|{
comment|//noinspection ObjectEqualsNull
name|assertFalse
argument_list|(
literal|"An object should not be equal to null."
argument_list|,
name|collection
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals_notACollection ()
specifier|public
name|void
name|testEquals_notACollection
parameter_list|()
block|{
comment|//noinspection EqualsBetweenInconvertibleTypes
name|assertFalse
argument_list|(
literal|"A Collection should never equal an "
operator|+
literal|"object that is not a Collection."
argument_list|,
name|collection
operator|.
name|equals
argument_list|(
literal|"huh?"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

