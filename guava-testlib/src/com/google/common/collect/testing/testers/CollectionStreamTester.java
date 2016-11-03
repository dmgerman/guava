begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2013 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|KNOWN_ORDER
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
name|AbstractCollectionTester
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
name|Helpers
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * A generic JUnit test which tests {@code stream} operations on a collection.  * Can't be invoked directly; please see  * {@link com.google.common.collect.testing.CollectionTestSuiteBuilder}.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|CollectionStreamTester
specifier|public
class|class
name|CollectionStreamTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractCollectionTester
argument_list|<
name|E
argument_list|>
block|{
comment|/*    * We're not really testing the implementation of Stream, only that we're getting a Stream    * that corresponds to the expected elements.    */
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|absent
operator|=
name|KNOWN_ORDER
argument_list|)
DECL|method|testStreamToArrayUnknownOrder ()
specifier|public
name|void
name|testStreamToArrayUnknownOrder
parameter_list|()
block|{
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// to allow Collections.synchronized* tests to pass
name|Helpers
operator|.
name|assertEqualIgnoringOrder
argument_list|(
name|getSampleElements
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|collection
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|KNOWN_ORDER
argument_list|)
DECL|method|testStreamToArrayKnownOrder ()
specifier|public
name|void
name|testStreamToArrayKnownOrder
parameter_list|()
block|{
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// to allow Collections.synchronized* tests to pass
name|assertEquals
argument_list|(
name|getOrderedElements
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|collection
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testStreamCount ()
specifier|public
name|void
name|testStreamCount
parameter_list|()
block|{
synchronized|synchronized
init|(
name|collection
init|)
block|{
comment|// to allow Collections.synchronized* tests to pass
name|assertEquals
argument_list|(
name|getNumElements
argument_list|()
argument_list|,
name|collection
operator|.
name|stream
argument_list|()
operator|.
name|count
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

