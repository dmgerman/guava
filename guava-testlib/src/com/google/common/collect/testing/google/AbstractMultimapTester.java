begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing.google
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
name|google
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|truth0
operator|.
name|Truth
operator|.
name|ASSERT
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
name|Multimap
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
name|AbstractContainerTester
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
name|SampleElements
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
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_comment
comment|/**  * Superclass for all {@code Multimap} testers.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractMultimapTester
specifier|public
specifier|abstract
class|class
name|AbstractMultimapTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|M
extends|extends
name|Multimap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
extends|extends
name|AbstractContainerTester
argument_list|<
name|M
argument_list|,
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
block|{
DECL|field|multimap
specifier|private
name|M
name|multimap
decl_stmt|;
DECL|method|multimap ()
specifier|protected
name|M
name|multimap
parameter_list|()
block|{
return|return
name|multimap
return|;
block|}
comment|/**    * @return an array of the proper size with {@code null} as the key of the    * middle element.    */
DECL|method|createArrayWithNullKey ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArrayWithNullKey
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nullKeyLocation
init|=
name|getNullLocation
argument_list|()
decl_stmt|;
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|oldEntry
init|=
name|array
index|[
name|nullKeyLocation
index|]
decl_stmt|;
name|array
index|[
name|nullKeyLocation
index|]
operator|=
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
name|oldEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
comment|/**    * @return an array of the proper size with {@code null} as the value of the    * middle element.    */
DECL|method|createArrayWithNullValue ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArrayWithNullValue
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nullValueLocation
init|=
name|getNullLocation
argument_list|()
decl_stmt|;
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|oldEntry
init|=
name|array
index|[
name|nullValueLocation
index|]
decl_stmt|;
name|array
index|[
name|nullValueLocation
index|]
operator|=
name|Helpers
operator|.
name|mapEntry
argument_list|(
name|oldEntry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
comment|/**    * @return an array of the proper size with {@code null} as the key and value of the    * middle element.    */
DECL|method|createArrayWithNullKeyAndValue ()
specifier|protected
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|createArrayWithNullKeyAndValue
parameter_list|()
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|array
init|=
name|createSamplesArray
argument_list|()
decl_stmt|;
specifier|final
name|int
name|nullValueLocation
init|=
name|getNullLocation
argument_list|()
decl_stmt|;
name|array
index|[
name|nullValueLocation
index|]
operator|=
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
DECL|method|getValueForNullKey ()
specifier|protected
name|V
name|getValueForNullKey
parameter_list|()
block|{
return|return
name|getEntryNullReplaces
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
DECL|method|getKeyForNullValue ()
specifier|protected
name|K
name|getKeyForNullValue
parameter_list|()
block|{
return|return
name|getEntryNullReplaces
argument_list|()
operator|.
name|getKey
argument_list|()
return|;
block|}
DECL|method|getEntryNullReplaces ()
specifier|private
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|getEntryNullReplaces
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
init|=
name|getSampleElements
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getNullLocation
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|entries
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|entries
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|initMultimapWithNullKey ()
specifier|protected
name|void
name|initMultimapWithNullKey
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|createArrayWithNullKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|initMultimapWithNullValue ()
specifier|protected
name|void
name|initMultimapWithNullValue
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|createArrayWithNullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|initMultimapWithNullKeyAndValue ()
specifier|protected
name|void
name|initMultimapWithNullKeyAndValue
parameter_list|()
block|{
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|createArrayWithNullKeyAndValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sampleKeys ()
specifier|protected
name|SampleElements
argument_list|<
name|K
argument_list|>
name|sampleKeys
parameter_list|()
block|{
return|return
operator|(
operator|(
name|TestMultimapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|?
extends|extends
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|getSubjectGenerator
argument_list|()
operator|.
name|getInnerGenerator
argument_list|()
operator|)
operator|.
name|sampleKeys
argument_list|()
return|;
block|}
DECL|method|sampleValues ()
specifier|protected
name|SampleElements
argument_list|<
name|V
argument_list|>
name|sampleValues
parameter_list|()
block|{
return|return
operator|(
operator|(
name|TestMultimapGenerator
argument_list|<
name|K
argument_list|,
name|V
argument_list|,
name|?
extends|extends
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|getSubjectGenerator
argument_list|()
operator|.
name|getInnerGenerator
argument_list|()
operator|)
operator|.
name|sampleValues
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|actualContents ()
specifier|protected
name|Collection
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|actualContents
parameter_list|()
block|{
return|return
name|multimap
operator|.
name|entries
argument_list|()
return|;
block|}
comment|// TODO: dispose of this once collection is encapsulated.
annotation|@
name|Override
DECL|method|resetContainer (M newContents)
specifier|protected
name|M
name|resetContainer
parameter_list|(
name|M
name|newContents
parameter_list|)
block|{
name|multimap
operator|=
name|super
operator|.
name|resetContainer
argument_list|(
name|newContents
argument_list|)
expr_stmt|;
return|return
name|multimap
return|;
block|}
DECL|method|resetContainer (Entry<K, V>.... newContents)
specifier|protected
name|Multimap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|resetContainer
parameter_list|(
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
modifier|...
name|newContents
parameter_list|)
block|{
name|multimap
operator|=
name|super
operator|.
name|resetContainer
argument_list|(
name|getSubjectGenerator
argument_list|()
operator|.
name|create
argument_list|(
name|newContents
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|multimap
return|;
block|}
comment|/** @see AbstractContainerTester#resetContainer() */
DECL|method|resetCollection ()
specifier|protected
name|void
name|resetCollection
parameter_list|()
block|{
name|resetContainer
argument_list|()
expr_stmt|;
block|}
DECL|method|assertGet (K key, V... values)
specifier|protected
name|void
name|assertGet
parameter_list|(
name|K
name|key
parameter_list|,
name|V
modifier|...
name|values
parameter_list|)
block|{
name|assertGet
argument_list|(
name|key
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertGet (K key, Collection<V> values)
specifier|protected
name|void
name|assertGet
parameter_list|(
name|K
name|key
parameter_list|,
name|Collection
argument_list|<
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allFrom
argument_list|(
name|values
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|has
argument_list|()
operator|.
name|allFrom
argument_list|(
name|values
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|multimap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
comment|// TODO(user): Add proper overrides to prevent autoboxing.
comment|// Truth+autoboxing == compile error. Cast int to long to fix:
name|ASSERT
operator|.
name|that
argument_list|(
name|multimap
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|is
argument_list|(
operator|(
name|long
operator|)
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|,
name|multimap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|,
name|multimap
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|,
name|multimap
argument_list|()
operator|.
name|keys
argument_list|()
operator|.
name|contains
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

