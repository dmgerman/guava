begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
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
name|ArrayList
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
name|List
import|;
end_import

begin_comment
comment|/**  * Generator for collection of a particular size.  *  *<p>This class is GWT compatible.  *  * @author George van den Driessche  */
end_comment

begin_class
DECL|class|OneSizeGenerator
specifier|public
specifier|final
class|class
name|OneSizeGenerator
parameter_list|<
name|T
parameter_list|,
name|E
parameter_list|>
implements|implements
name|OneSizeTestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
block|{
DECL|field|generator
specifier|private
specifier|final
name|TestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|generator
decl_stmt|;
DECL|field|collectionSize
specifier|private
specifier|final
name|CollectionSize
name|collectionSize
decl_stmt|;
DECL|method|OneSizeGenerator (TestContainerGenerator<T, E> generator, CollectionSize collectionSize)
specifier|public
name|OneSizeGenerator
parameter_list|(
name|TestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|generator
parameter_list|,
name|CollectionSize
name|collectionSize
parameter_list|)
block|{
name|this
operator|.
name|generator
operator|=
name|generator
expr_stmt|;
name|this
operator|.
name|collectionSize
operator|=
name|collectionSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getInnerGenerator ()
specifier|public
name|TestContainerGenerator
argument_list|<
name|T
argument_list|,
name|E
argument_list|>
name|getInnerGenerator
parameter_list|()
block|{
return|return
name|generator
return|;
block|}
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
parameter_list|()
block|{
return|return
name|generator
operator|.
name|samples
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|T
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
return|return
name|generator
operator|.
name|create
argument_list|(
name|elements
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|E
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
name|generator
operator|.
name|createArray
argument_list|(
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createTestSubject ()
specifier|public
name|T
name|createTestSubject
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
name|getCollectionSize
argument_list|()
operator|.
name|getNumElements
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|generator
operator|.
name|create
argument_list|(
name|elements
operator|.
name|toArray
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getSampleElements (int howMany)
specifier|public
name|Collection
argument_list|<
name|E
argument_list|>
name|getSampleElements
parameter_list|(
name|int
name|howMany
parameter_list|)
block|{
name|SampleElements
argument_list|<
name|E
argument_list|>
name|samples
init|=
name|samples
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|E
argument_list|>
name|allSampleElements
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|samples
operator|.
name|e0
argument_list|,
name|samples
operator|.
name|e1
argument_list|,
name|samples
operator|.
name|e2
argument_list|,
name|samples
operator|.
name|e3
argument_list|,
name|samples
operator|.
name|e4
argument_list|)
decl_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<
name|E
argument_list|>
argument_list|(
name|allSampleElements
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|howMany
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCollectionSize ()
specifier|public
name|CollectionSize
name|getCollectionSize
parameter_list|()
block|{
return|return
name|collectionSize
return|;
block|}
annotation|@
name|Override
DECL|method|order (List<E> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|E
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|generator
operator|.
name|order
argument_list|(
name|insertionOrder
argument_list|)
return|;
block|}
block|}
end_class

end_unit

