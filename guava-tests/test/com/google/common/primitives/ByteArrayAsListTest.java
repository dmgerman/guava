begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.primitives
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|testing
operator|.
name|ListTestSuiteBuilder
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
name|ListFeature
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
comment|/**  * Test suite covering {@link Bytes#asList(byte[])}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ByteArrayAsListTest
specifier|public
class|class
name|ByteArrayAsListTest
extends|extends
name|TestCase
block|{
DECL|method|asList (Byte[] values)
specifier|private
specifier|static
name|List
argument_list|<
name|Byte
argument_list|>
name|asList
parameter_list|(
name|Byte
index|[]
name|values
parameter_list|)
block|{
name|byte
index|[]
name|temp
init|=
operator|new
name|byte
index|[
name|values
operator|.
name|length
index|]
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
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|temp
index|[
name|i
index|]
operator|=
name|checkNotNull
argument_list|(
name|values
index|[
name|i
index|]
argument_list|)
expr_stmt|;
comment|// checkNotNull for GWT (do not optimize).
block|}
return|return
name|Bytes
operator|.
name|asList
argument_list|(
name|temp
argument_list|)
return|;
block|}
annotation|@
name|GwtIncompatible
argument_list|(
literal|"suite"
argument_list|)
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|List
argument_list|<
name|ListTestSuiteBuilder
argument_list|<
name|Byte
argument_list|>
argument_list|>
name|builders
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|BytesAsListGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Bytes.asList"
argument_list|)
argument_list|,
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|BytesAsListHeadSubListGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Bytes.asList, head subList"
argument_list|)
argument_list|,
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|BytesAsListTailSubListGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Bytes.asList, tail subList"
argument_list|)
argument_list|,
name|ListTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|BytesAsListMiddleSubListGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Bytes.asList, middle subList"
argument_list|)
argument_list|)
decl_stmt|;
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
for|for
control|(
name|ListTestSuiteBuilder
argument_list|<
name|Byte
argument_list|>
name|builder
range|:
name|builders
control|)
block|{
name|suite
operator|.
name|addTest
argument_list|(
name|builder
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ONE
argument_list|,
name|CollectionSize
operator|.
name|SEVERAL
argument_list|,
name|CollectionFeature
operator|.
name|RESTRICTS_ELEMENTS
argument_list|,
name|ListFeature
operator|.
name|SUPPORTS_SET
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|suite
return|;
block|}
comment|// Test generators.  To let the GWT test suite generator access them, they need to be
comment|// public named classes with a public default constructor.
DECL|class|BytesAsListGenerator
specifier|public
specifier|static
specifier|final
class|class
name|BytesAsListGenerator
extends|extends
name|TestByteListGenerator
block|{
DECL|method|create (Byte[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Byte
index|[]
name|elements
parameter_list|)
block|{
return|return
name|asList
argument_list|(
name|elements
argument_list|)
return|;
block|}
block|}
DECL|class|BytesAsListHeadSubListGenerator
specifier|public
specifier|static
specifier|final
class|class
name|BytesAsListHeadSubListGenerator
extends|extends
name|TestByteListGenerator
block|{
DECL|method|create (Byte[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Byte
index|[]
name|elements
parameter_list|)
block|{
name|Byte
index|[]
name|suffix
init|=
block|{
name|Byte
operator|.
name|MIN_VALUE
block|,
name|Byte
operator|.
name|MAX_VALUE
block|}
decl_stmt|;
name|Byte
index|[]
name|all
init|=
name|concat
argument_list|(
name|elements
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
return|return
name|asList
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
DECL|class|BytesAsListTailSubListGenerator
specifier|public
specifier|static
specifier|final
class|class
name|BytesAsListTailSubListGenerator
extends|extends
name|TestByteListGenerator
block|{
DECL|method|create (Byte[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Byte
index|[]
name|elements
parameter_list|)
block|{
name|Byte
index|[]
name|prefix
init|=
block|{
operator|(
name|byte
operator|)
literal|86
block|,
operator|(
name|byte
operator|)
literal|99
block|}
decl_stmt|;
name|Byte
index|[]
name|all
init|=
name|concat
argument_list|(
name|prefix
argument_list|,
name|elements
argument_list|)
decl_stmt|;
return|return
name|asList
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
DECL|class|BytesAsListMiddleSubListGenerator
specifier|public
specifier|static
specifier|final
class|class
name|BytesAsListMiddleSubListGenerator
extends|extends
name|TestByteListGenerator
block|{
DECL|method|create (Byte[] elements)
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Byte
index|[]
name|elements
parameter_list|)
block|{
name|Byte
index|[]
name|prefix
init|=
block|{
name|Byte
operator|.
name|MIN_VALUE
block|,
name|Byte
operator|.
name|MAX_VALUE
block|}
decl_stmt|;
name|Byte
index|[]
name|suffix
init|=
block|{
operator|(
name|byte
operator|)
literal|86
block|,
operator|(
name|byte
operator|)
literal|99
block|}
decl_stmt|;
name|Byte
index|[]
name|all
init|=
name|concat
argument_list|(
name|concat
argument_list|(
name|prefix
argument_list|,
name|elements
argument_list|)
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
return|return
name|asList
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
DECL|method|concat (Byte[] left, Byte[] right)
specifier|private
specifier|static
name|Byte
index|[]
name|concat
parameter_list|(
name|Byte
index|[]
name|left
parameter_list|,
name|Byte
index|[]
name|right
parameter_list|)
block|{
name|Byte
index|[]
name|result
init|=
operator|new
name|Byte
index|[
name|left
operator|.
name|length
operator|+
name|right
operator|.
name|length
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|left
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
literal|0
argument_list|,
name|left
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|right
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|left
operator|.
name|length
argument_list|,
name|right
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|class|TestByteListGenerator
specifier|public
specifier|static
specifier|abstract
class|class
name|TestByteListGenerator
implements|implements
name|TestListGenerator
argument_list|<
name|Byte
argument_list|>
block|{
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Byte
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleBytes
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create (Object... elements)
specifier|public
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|Byte
index|[]
name|array
init|=
operator|new
name|Byte
index|[
name|elements
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
name|e
range|:
name|elements
control|)
block|{
name|array
index|[
name|i
operator|++
index|]
operator|=
operator|(
name|Byte
operator|)
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
comment|/**      * Creates a new collection containing the given elements; implement this      * method instead of {@link #create(Object...)}.      */
DECL|method|create (Byte[] elements)
specifier|protected
specifier|abstract
name|List
argument_list|<
name|Byte
argument_list|>
name|create
parameter_list|(
name|Byte
index|[]
name|elements
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|createArray (int length)
specifier|public
name|Byte
index|[]
name|createArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Byte
index|[
name|length
index|]
return|;
block|}
comment|/** Returns the original element list, unchanged. */
annotation|@
name|Override
DECL|method|order (List<Byte> insertionOrder)
specifier|public
name|List
argument_list|<
name|Byte
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Byte
argument_list|>
name|insertionOrder
parameter_list|)
block|{
return|return
name|insertionOrder
return|;
block|}
block|}
DECL|class|SampleBytes
specifier|public
specifier|static
class|class
name|SampleBytes
extends|extends
name|SampleElements
argument_list|<
name|Byte
argument_list|>
block|{
DECL|method|SampleBytes ()
specifier|public
name|SampleBytes
parameter_list|()
block|{
name|super
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
operator|(
name|byte
operator|)
literal|1
argument_list|,
operator|(
name|byte
operator|)
literal|2
argument_list|,
operator|(
name|byte
operator|)
literal|3
argument_list|,
operator|(
name|byte
operator|)
literal|4
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

