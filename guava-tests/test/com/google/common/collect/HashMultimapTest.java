begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Unit tests for {@link HashMultimap}.  *  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|HashMultimapTest
specifier|public
class|class
name|HashMultimapTest
extends|extends
name|AbstractSetMultimapTest
block|{
DECL|method|create ()
annotation|@
name|Override
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
return|return
name|HashMultimap
operator|.
name|create
argument_list|()
return|;
block|}
comment|/*    * The behavior of toString() is tested by TreeMultimap, which shares a    * lot of code with HashMultimap and has deterministic iteration order.    */
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
name|HashMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|,
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromMultimap ()
specifier|public
name|void
name|testCreateFromMultimap
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
name|createSample
argument_list|()
decl_stmt|;
name|HashMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|copy
init|=
name|HashMultimap
operator|.
name|create
argument_list|(
name|multimap
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|multimap
argument_list|,
name|copy
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|copy
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromSizes ()
specifier|public
name|void
name|testCreateFromSizes
parameter_list|()
block|{
name|HashMultimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|multimap
init|=
name|HashMultimap
operator|.
name|create
argument_list|(
literal|20
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multimap
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
argument_list|,
name|multimap
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|multimap
operator|.
name|expectedValuesPerKey
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIllegalSizes ()
specifier|public
name|void
name|testCreateFromIllegalSizes
parameter_list|()
block|{
try|try
block|{
name|HashMultimap
operator|.
name|create
argument_list|(
operator|-
literal|20
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
try|try
block|{
name|HashMultimap
operator|.
name|create
argument_list|(
literal|20
argument_list|,
operator|-
literal|15
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{}
block|}
DECL|method|testEmptyMultimapsEqual ()
specifier|public
name|void
name|testEmptyMultimapsEqual
parameter_list|()
block|{
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|setMultimap
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|Multimap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|listMultimap
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|setMultimap
operator|.
name|equals
argument_list|(
name|listMultimap
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listMultimap
operator|.
name|equals
argument_list|(
name|setMultimap
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

