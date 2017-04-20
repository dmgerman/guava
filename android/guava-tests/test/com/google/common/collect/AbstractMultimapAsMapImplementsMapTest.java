begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|MapInterfaceTest
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Test {@link Multimap#asMap()} for an arbitrary multimap with  * {@link MapInterfaceTest}.  *  * @author George van den Driessche  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMultimapAsMapImplementsMapTest
specifier|public
specifier|abstract
class|class
name|AbstractMultimapAsMapImplementsMapTest
extends|extends
name|MapInterfaceTest
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
block|{
DECL|method|AbstractMultimapAsMapImplementsMapTest ( boolean modifiable, boolean allowsNulls, boolean supportsIteratorRemove)
specifier|public
name|AbstractMultimapAsMapImplementsMapTest
parameter_list|(
name|boolean
name|modifiable
parameter_list|,
name|boolean
name|allowsNulls
parameter_list|,
name|boolean
name|supportsIteratorRemove
parameter_list|)
block|{
name|super
argument_list|(
name|allowsNulls
argument_list|,
name|allowsNulls
argument_list|,
literal|false
argument_list|,
name|modifiable
argument_list|,
name|modifiable
argument_list|,
name|supportsIteratorRemove
argument_list|)
expr_stmt|;
block|}
DECL|method|populate (Multimap<String, Integer> multimap)
specifier|protected
name|void
name|populate
parameter_list|(
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
parameter_list|)
block|{
name|multimap
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"two"
argument_list|,
literal|22
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"three"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"three"
argument_list|,
literal|33
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"three"
argument_list|,
literal|333
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeyNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|String
name|getKeyNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
literal|"zero"
return|;
block|}
DECL|method|getValueNotInPopulatedMap ()
annotation|@
name|Override
specifier|protected
name|Collection
argument_list|<
name|Integer
argument_list|>
name|getValueNotInPopulatedMap
parameter_list|()
throws|throws
name|UnsupportedOperationException
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * The version of this test supplied by {@link MapInterfaceTest} fails for    * this particular Map implementation, because {@code map.get()} returns a    * view collection that changes in the course of a call to {@code remove()}.    * Thus, the expectation doesn't hold that {@code map.remove(x)} returns the    * same value which {@code map.get(x)} did immediately beforehand.    */
DECL|method|testRemove ()
annotation|@
name|Override
specifier|public
name|void
name|testRemove
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|map
decl_stmt|;
specifier|final
name|String
name|keyToRemove
decl_stmt|;
try|try
block|{
name|map
operator|=
name|makePopulatedMap
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
return|return;
block|}
name|keyToRemove
operator|=
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|supportsRemove
condition|)
block|{
name|int
name|initialSize
init|=
name|map
operator|.
name|size
argument_list|()
decl_stmt|;
name|map
operator|.
name|get
argument_list|(
name|keyToRemove
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
name|keyToRemove
argument_list|)
expr_stmt|;
comment|// This line doesn't hold - see the Javadoc comments above.
comment|// assertEquals(expectedValue, oldValue);
name|assertFalse
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
name|keyToRemove
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|initialSize
operator|-
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|map
operator|.
name|remove
argument_list|(
name|keyToRemove
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
name|assertInvariants
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

