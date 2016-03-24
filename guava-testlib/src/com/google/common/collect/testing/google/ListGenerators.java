begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|ImmutableList
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
name|Lists
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
name|TestCharacterListGenerator
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
name|TestListGenerator
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
name|TestStringListGenerator
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
name|TestUnhashableCollectionGenerator
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
name|UnhashableObject
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
name|primitives
operator|.
name|Chars
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
name|Collections
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
comment|/**  * Common generators of different types of lists.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListGenerators
specifier|public
specifier|final
class|class
name|ListGenerators
block|{
DECL|method|ListGenerators ()
specifier|private
name|ListGenerators
parameter_list|()
block|{}
DECL|class|ImmutableListOfGenerator
specifier|public
specifier|static
class|class
name|ImmutableListOfGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|BuilderAddListGenerator
specifier|public
specifier|static
class|class
name|BuilderAddListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|ImmutableList
operator|.
name|Builder
argument_list|<
name|String
argument_list|>
name|builder
init|=
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|element
range|:
name|elements
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
DECL|class|BuilderAddAllListGenerator
specifier|public
specifier|static
class|class
name|BuilderAddAllListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|builder
argument_list|()
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
DECL|class|BuilderReversedListGenerator
specifier|public
specifier|static
class|class
name|BuilderReversedListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|asList
argument_list|(
name|elements
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|list
argument_list|)
operator|.
name|reverse
argument_list|()
return|;
block|}
block|}
DECL|class|ImmutableListHeadSubListGenerator
specifier|public
specifier|static
class|class
name|ImmutableListHeadSubListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|String
index|[]
name|suffix
init|=
block|{
literal|"f"
block|,
literal|"g"
block|}
decl_stmt|;
name|String
index|[]
name|all
init|=
operator|new
name|String
index|[
name|elements
operator|.
name|length
operator|+
name|suffix
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|0
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|suffix
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
name|elements
operator|.
name|length
argument_list|,
name|suffix
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|all
argument_list|)
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|elements
operator|.
name|length
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableListTailSubListGenerator
specifier|public
specifier|static
class|class
name|ImmutableListTailSubListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|String
index|[]
name|prefix
init|=
block|{
literal|"f"
block|,
literal|"g"
block|}
decl_stmt|;
name|String
index|[]
name|all
init|=
operator|new
name|String
index|[
name|elements
operator|.
name|length
operator|+
name|prefix
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|prefix
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|2
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|all
argument_list|)
operator|.
name|subList
argument_list|(
literal|2
argument_list|,
name|elements
operator|.
name|length
operator|+
literal|2
argument_list|)
return|;
block|}
block|}
DECL|class|ImmutableListMiddleSubListGenerator
specifier|public
specifier|static
class|class
name|ImmutableListMiddleSubListGenerator
extends|extends
name|TestStringListGenerator
block|{
annotation|@
name|Override
DECL|method|create (String[] elements)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|create
parameter_list|(
name|String
index|[]
name|elements
parameter_list|)
block|{
name|String
index|[]
name|prefix
init|=
block|{
literal|"f"
block|,
literal|"g"
block|}
decl_stmt|;
name|String
index|[]
name|suffix
init|=
block|{
literal|"h"
block|,
literal|"i"
block|}
decl_stmt|;
name|String
index|[]
name|all
init|=
operator|new
name|String
index|[
literal|2
operator|+
name|elements
operator|.
name|length
operator|+
literal|2
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|prefix
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|elements
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|2
argument_list|,
name|elements
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|suffix
argument_list|,
literal|0
argument_list|,
name|all
argument_list|,
literal|2
operator|+
name|elements
operator|.
name|length
argument_list|,
literal|2
argument_list|)
expr_stmt|;
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|all
argument_list|)
operator|.
name|subList
argument_list|(
literal|2
argument_list|,
name|elements
operator|.
name|length
operator|+
literal|2
argument_list|)
return|;
block|}
block|}
DECL|class|CharactersOfStringGenerator
specifier|public
specifier|static
class|class
name|CharactersOfStringGenerator
extends|extends
name|TestCharacterListGenerator
block|{
annotation|@
name|Override
DECL|method|create (Character[] elements)
specifier|public
name|List
argument_list|<
name|Character
argument_list|>
name|create
parameter_list|(
name|Character
index|[]
name|elements
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
name|Chars
operator|.
name|toArray
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Lists
operator|.
name|charactersOf
argument_list|(
name|String
operator|.
name|copyValueOf
argument_list|(
name|chars
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|class|CharactersOfCharSequenceGenerator
specifier|public
specifier|static
class|class
name|CharactersOfCharSequenceGenerator
extends|extends
name|TestCharacterListGenerator
block|{
annotation|@
name|Override
DECL|method|create (Character[] elements)
specifier|public
name|List
argument_list|<
name|Character
argument_list|>
name|create
parameter_list|(
name|Character
index|[]
name|elements
parameter_list|)
block|{
name|char
index|[]
name|chars
init|=
name|Chars
operator|.
name|toArray
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|str
operator|.
name|append
argument_list|(
name|chars
argument_list|)
expr_stmt|;
return|return
name|Lists
operator|.
name|charactersOf
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
DECL|class|TestUnhashableListGenerator
specifier|private
specifier|abstract
specifier|static
class|class
name|TestUnhashableListGenerator
extends|extends
name|TestUnhashableCollectionGenerator
argument_list|<
name|List
argument_list|<
name|UnhashableObject
argument_list|>
argument_list|>
implements|implements
name|TestListGenerator
argument_list|<
name|UnhashableObject
argument_list|>
block|{}
DECL|class|UnhashableElementsImmutableListGenerator
specifier|public
specifier|static
class|class
name|UnhashableElementsImmutableListGenerator
extends|extends
name|TestUnhashableListGenerator
block|{
annotation|@
name|Override
DECL|method|create (UnhashableObject[] elements)
specifier|public
name|List
argument_list|<
name|UnhashableObject
argument_list|>
name|create
parameter_list|(
name|UnhashableObject
index|[]
name|elements
parameter_list|)
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

