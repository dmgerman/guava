begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import static
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
name|features
operator|.
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionFeature
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
name|collect
operator|.
name|testing
operator|.
name|features
operator|.
name|CollectionSize
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Tests {@link java.util.Set#hashCode}.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|SetHashCodeTester
specifier|public
class|class
name|SetHashCodeTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractSetTester
argument_list|<
name|E
argument_list|>
block|{
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|int
name|expectedHashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|getSampleElements
argument_list|()
control|)
block|{
name|expectedHashCode
operator|+=
operator|(
operator|(
name|element
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|element
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"A Set's hashCode() should be the sum of those of its elements."
argument_list|,
name|expectedHashCode
argument_list|,
name|getSet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionSize
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|CollectionSize
operator|.
name|ZERO
argument_list|)
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|ALLOWS_NULL_VALUES
argument_list|)
DECL|method|testHashCode_containingNull ()
specifier|public
name|void
name|testHashCode_containingNull
parameter_list|()
block|{
name|Collection
argument_list|<
name|E
argument_list|>
name|elements
init|=
name|getSampleElements
argument_list|(
name|getNumElements
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|expectedHashCode
init|=
literal|0
decl_stmt|;
for|for
control|(
name|E
name|element
range|:
name|elements
control|)
block|{
name|expectedHashCode
operator|+=
operator|(
operator|(
name|element
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|element
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
block|}
name|elements
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|collection
operator|=
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|elements
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A Set's hashCode() should be the sum of those of its elements (with "
operator|+
literal|"a null element counting as having a hash of zero)."
argument_list|,
name|expectedHashCode
argument_list|,
name|getSet
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

