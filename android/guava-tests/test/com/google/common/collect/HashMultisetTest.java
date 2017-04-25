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
name|google
operator|.
name|MultisetFeature
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
name|google
operator|.
name|MultisetTestSuiteBuilder
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
name|google
operator|.
name|TestStringMultisetGenerator
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
name|testing
operator|.
name|SerializableTester
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_comment
comment|/**  * Unit test for {@link HashMultiset}.  *  * @author Kevin Bourrillion  * @author Jared Levy  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|HashMultisetTest
specifier|public
class|class
name|HashMultisetTest
extends|extends
name|TestCase
block|{
annotation|@
name|GwtIncompatible
comment|// suite
DECL|method|suite ()
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
name|TestSuite
name|suite
init|=
operator|new
name|TestSuite
argument_list|()
decl_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MultisetTestSuiteBuilder
operator|.
name|using
argument_list|(
name|hashMultisetGenerator
argument_list|()
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|CollectionFeature
operator|.
name|FAILS_FAST_ON_CONCURRENT_MODIFICATION
argument_list|,
name|CollectionFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|,
name|CollectionFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MultisetFeature
operator|.
name|ENTRIES_ARE_VIEWS
argument_list|)
operator|.
name|named
argument_list|(
literal|"HashMultiset"
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTestSuite
argument_list|(
name|HashMultisetTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|hashMultisetGenerator ()
specifier|private
specifier|static
name|TestStringMultisetGenerator
name|hashMultisetGenerator
parameter_list|()
block|{
return|return
operator|new
name|TestStringMultisetGenerator
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|Multiset
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
name|HashMultiset
operator|.
name|create
argument_list|(
name|asList
argument_list|(
name|elements
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|testCreate ()
specifier|public
name|void
name|testCreate
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateWithSize ()
specifier|public
name|void
name|testCreateWithSize
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
literal|50
argument_list|)
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|multiset
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCreateFromIterable ()
specifier|public
name|void
name|testCreateFromIterable
parameter_list|()
block|{
name|Multiset
argument_list|<
name|String
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|multiset
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|multiset
operator|.
name|count
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testSerializationContainingSelf ()
specifier|public
name|void
name|testSerializationContainingSelf
parameter_list|()
block|{
name|Multiset
argument_list|<
name|Multiset
argument_list|<
name|?
argument_list|>
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|multiset
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|Multiset
argument_list|<
name|?
argument_list|>
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|multiset
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|copy
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|copy
argument_list|,
name|copy
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// Only used by @GwtIncompatible code
DECL|class|MultisetHolder
specifier|private
specifier|static
class|class
name|MultisetHolder
implements|implements
name|Serializable
block|{
DECL|field|member
specifier|public
name|Multiset
argument_list|<
name|?
argument_list|>
name|member
decl_stmt|;
DECL|method|MultisetHolder (Multiset<?> multiset)
name|MultisetHolder
parameter_list|(
name|Multiset
argument_list|<
name|?
argument_list|>
name|multiset
parameter_list|)
block|{
name|this
operator|.
name|member
operator|=
name|multiset
expr_stmt|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
block|}
annotation|@
name|GwtIncompatible
comment|// SerializableTester
DECL|method|testSerializationIndirectSelfReference ()
specifier|public
name|void
name|testSerializationIndirectSelfReference
parameter_list|()
block|{
name|Multiset
argument_list|<
name|MultisetHolder
argument_list|>
name|multiset
init|=
name|HashMultiset
operator|.
name|create
argument_list|()
decl_stmt|;
name|MultisetHolder
name|holder
init|=
operator|new
name|MultisetHolder
argument_list|(
name|multiset
argument_list|)
decl_stmt|;
name|multiset
operator|.
name|add
argument_list|(
name|holder
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Multiset
argument_list|<
name|MultisetHolder
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|multiset
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|copy
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|copy
argument_list|,
name|copy
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|member
argument_list|)
expr_stmt|;
block|}
comment|/*    * The behavior of toString() and iteration is tested by LinkedHashMultiset,    * which shares a lot of code with HashMultiset and has deterministic    * iteration order.    */
block|}
end_class

end_unit
