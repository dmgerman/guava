begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtIncompatible
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
name|base
operator|.
name|Predicate
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
comment|/**  * Tests for Multimaps.filterEntries().asMap().  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
name|value
operator|=
literal|"untested"
argument_list|)
DECL|class|MultimapsFilterEntriesAsMapTest
specifier|public
class|class
name|MultimapsFilterEntriesAsMapTest
extends|extends
name|AbstractMultimapAsMapImplementsMapTest
block|{
DECL|field|PREDICATE
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|PREDICATE
init|=
operator|new
name|Predicate
argument_list|<
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
parameter_list|)
block|{
return|return
operator|!
literal|"badkey"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
literal|55556
operator|!=
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|MultimapsFilterEntriesAsMapTest ()
specifier|public
name|MultimapsFilterEntriesAsMapTest
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|createMultimap ()
specifier|private
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|createMultimap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|unfiltered
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|"zero"
argument_list|,
literal|55556
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|"one"
argument_list|,
literal|55556
argument_list|)
expr_stmt|;
name|unfiltered
operator|.
name|put
argument_list|(
literal|"badkey"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
name|Multimaps
operator|.
name|filterEntries
argument_list|(
name|unfiltered
argument_list|,
name|PREDICATE
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|makeEmptyMap ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|makeEmptyMap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createMultimap
argument_list|()
decl_stmt|;
return|return
name|multimap
operator|.
name|asMap
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|makePopulatedMap ()
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|makePopulatedMap
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|createMultimap
argument_list|()
decl_stmt|;
name|populate
argument_list|(
name|multimap
argument_list|)
expr_stmt|;
return|return
name|multimap
operator|.
name|asMap
argument_list|()
return|;
block|}
block|}
end_class

end_unit

