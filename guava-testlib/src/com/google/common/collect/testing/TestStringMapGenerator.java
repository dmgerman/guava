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
name|annotations
operator|.
name|GwtCompatible
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
comment|/**  * Implementation helper for {@link TestMapGenerator} for use with maps of strings.  *  * @author Chris Povirk  * @author Jared Levy  * @author George van den Driessche  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|TestStringMapGenerator
specifier|public
specifier|abstract
class|class
name|TestStringMapGenerator
implements|implements
name|TestMapGenerator
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<>
argument_list|(
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"one"
argument_list|,
literal|"January"
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"two"
argument_list|,
literal|"February"
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"three"
argument_list|,
literal|"March"
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"four"
argument_list|,
literal|"April"
argument_list|)
argument_list|,
name|Helpers
operator|.
name|mapEntry
argument_list|(
literal|"five"
argument_list|,
literal|"May"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... entries)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|entries
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|array
init|=
operator|new
name|Entry
index|[
name|entries
operator|.
name|length
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|entries
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
init|=
operator|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|o
decl_stmt|;
name|array
index|[
name|i
operator|++
index|]
operator|=
name|e
expr_stmt|;
block|}
return|return
name|create
argument_list|(
name|array
argument_list|)
return|;
block|}
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|create
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|entries
parameter_list|)
function_decl|;
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createArray (int length)
specifier|public
specifier|final
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Entry
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|createKeyArray (int length)
specifier|public
specifier|final
name|String
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|String
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|createValueArray (int length)
specifier|public
specifier|final
name|String
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|String
index|[
name|length
index|]
return|;
block|}
comment|/** Returns the original element list, unchanged. */
annotation|@
name|Override
DECL|method|order (List<Entry<String, String>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
return|;
block|}
block|}
end_class

end_unit

