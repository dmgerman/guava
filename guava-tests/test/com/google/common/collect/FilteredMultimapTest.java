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
name|Arrays
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link Multimaps} filtering methods.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
comment|// nottested
DECL|class|FilteredMultimapTest
specifier|public
class|class
name|FilteredMultimapTest
extends|extends
name|TestCase
block|{
DECL|field|ENTRY_PREDICATE
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
name|ENTRY_PREDICATE
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
operator|!
operator|(
operator|(
name|Integer
operator|)
literal|55556
operator|)
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|create ()
specifier|protected
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|create
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
literal|"foo"
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
name|ENTRY_PREDICATE
argument_list|)
return|;
block|}
DECL|field|KEY_PREDICATE
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|String
argument_list|>
name|KEY_PREDICATE
init|=
operator|new
name|Predicate
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|!
literal|"badkey"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|testFilterKeys ()
specifier|public
name|void
name|testFilterKeys
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
literal|"foo"
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
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|filtered
init|=
name|Multimaps
operator|.
name|filterKeys
argument_list|(
name|unfiltered
argument_list|,
name|KEY_PREDICATE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|filtered
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filtered
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|55556
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|field|VALUE_PREDICATE
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|Integer
argument_list|>
name|VALUE_PREDICATE
init|=
operator|new
name|Predicate
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
return|return
operator|!
operator|(
operator|(
name|Integer
operator|)
literal|55556
operator|)
operator|.
name|equals
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
decl_stmt|;
DECL|method|testFilterValues ()
specifier|public
name|void
name|testFilterValues
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
literal|"foo"
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
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|filtered
init|=
name|Multimaps
operator|.
name|filterValues
argument_list|(
name|unfiltered
argument_list|,
name|VALUE_PREDICATE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|filtered
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filtered
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|55556
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filtered
operator|.
name|containsEntry
argument_list|(
literal|"badkey"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testFilterFiltered ()
specifier|public
name|void
name|testFilterFiltered
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
literal|"foo"
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
name|unfiltered
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|keyFiltered
init|=
name|Multimaps
operator|.
name|filterKeys
argument_list|(
name|unfiltered
argument_list|,
name|KEY_PREDICATE
argument_list|)
decl_stmt|;
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|filtered
init|=
name|Multimaps
operator|.
name|filterValues
argument_list|(
name|keyFiltered
argument_list|,
name|VALUE_PREDICATE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|filtered
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filtered
operator|.
name|containsEntry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filtered
operator|.
name|keySet
argument_list|()
operator|.
name|retainAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"cat"
argument_list|,
literal|"dog"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|filtered
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO(jlevy): Many more tests needed.
block|}
end_class

end_unit

