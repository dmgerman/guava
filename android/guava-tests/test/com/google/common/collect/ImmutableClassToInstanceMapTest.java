begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Maps
operator|.
name|immutableEntry
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
name|MapTestSuiteBuilder
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
name|TestMapGenerator
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
name|MapFeature
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
comment|/**  * Unit test for {@link ImmutableClassToInstanceMap}.  *  * @author Kevin Bourrillion  */
end_comment

begin_class
DECL|class|ImmutableClassToInstanceMapTest
specifier|public
class|class
name|ImmutableClassToInstanceMapTest
extends|extends
name|TestCase
block|{
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
name|addTestSuite
argument_list|(
name|ImmutableClassToInstanceMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|suite
operator|.
name|addTest
argument_list|(
name|MapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|TestClassToInstanceMapGenerator
argument_list|()
block|{
comment|// Other tests will verify what real, warning-free usage looks like
comment|// but here we have to do some serious fudging
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|Map
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
name|create
parameter_list|(
name|Object
modifier|...
name|elements
parameter_list|)
block|{
name|ImmutableClassToInstanceMap
operator|.
name|Builder
argument_list|<
name|Impl
argument_list|>
name|builder
init|=
name|ImmutableClassToInstanceMap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|elements
control|)
block|{
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
name|entry
init|=
operator|(
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
operator|)
name|object
decl_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|Map
operator|)
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
argument_list|)
operator|.
name|named
argument_list|(
literal|"ImmutableClassToInstanceMap"
argument_list|)
operator|.
name|withFeatures
argument_list|(
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
argument_list|,
name|MapFeature
operator|.
name|RESTRICTS_KEYS
argument_list|,
name|CollectionFeature
operator|.
name|KNOWN_ORDER
argument_list|,
name|CollectionSize
operator|.
name|ANY
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|CollectionFeature
operator|.
name|SERIALIZABLE
argument_list|)
operator|.
name|createTestSuite
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testSerialization_empty ()
specifier|public
name|void
name|testSerialization_empty
parameter_list|()
block|{
name|assertSame
argument_list|(
name|ImmutableClassToInstanceMap
operator|.
name|of
argument_list|()
argument_list|,
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|ImmutableClassToInstanceMap
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyOf_map_empty ()
specifier|public
name|void
name|testCopyOf_map_empty
parameter_list|()
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|in
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
name|ClassToInstanceMap
argument_list|<
name|Object
argument_list|>
name|map
init|=
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|ImmutableClassToInstanceMap
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testOf_zero ()
specifier|public
name|void
name|testOf_zero
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|ImmutableClassToInstanceMap
operator|.
name|of
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOf_one ()
specifier|public
name|void
name|testOf_one
parameter_list|()
block|{
name|ImmutableClassToInstanceMap
argument_list|<
name|Number
argument_list|>
name|map
init|=
name|ImmutableClassToInstanceMap
operator|.
name|of
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyOf_map_valid ()
specifier|public
name|void
name|testCopyOf_map_valid
parameter_list|()
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
argument_list|,
name|Number
argument_list|>
name|in
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|in
operator|.
name|put
argument_list|(
name|Number
operator|.
name|class
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|in
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
argument_list|,
name|Math
operator|.
name|PI
argument_list|)
expr_stmt|;
name|ClassToInstanceMap
argument_list|<
name|Number
argument_list|>
name|map
init|=
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|zero
init|=
name|map
operator|.
name|getInstance
argument_list|(
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|zero
argument_list|)
expr_stmt|;
name|Double
name|pi
init|=
name|map
operator|.
name|getInstance
argument_list|(
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Math
operator|.
name|PI
argument_list|,
name|pi
argument_list|,
literal|0.0
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|map
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyOf_map_nulls ()
specifier|public
name|void
name|testCopyOf_map_nulls
parameter_list|()
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
argument_list|,
name|Number
argument_list|>
name|nullKey
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|null
argument_list|,
operator|(
name|Number
operator|)
literal|1.0
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|nullKey
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
name|Map
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
argument_list|,
name|Number
argument_list|>
name|nullValue
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|Number
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|nullValue
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testCopyOf_imap_empty ()
specifier|public
name|void
name|testCopyOf_imap_empty
parameter_list|()
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|in
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
name|ClassToInstanceMap
argument_list|<
name|Object
argument_list|>
name|map
init|=
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCopyOf_imap_valid ()
specifier|public
name|void
name|testCopyOf_imap_valid
parameter_list|()
block|{
name|ImmutableMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Number
argument_list|>
argument_list|,
name|?
extends|extends
name|Number
argument_list|>
name|in
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
name|Number
operator|.
name|class
argument_list|,
literal|0
argument_list|,
name|Double
operator|.
name|class
argument_list|,
name|Math
operator|.
name|PI
argument_list|)
decl_stmt|;
name|ClassToInstanceMap
argument_list|<
name|Number
argument_list|>
name|map
init|=
name|ImmutableClassToInstanceMap
operator|.
name|copyOf
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Number
name|zero
init|=
name|map
operator|.
name|getInstance
argument_list|(
name|Number
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|zero
argument_list|)
expr_stmt|;
name|Double
name|pi
init|=
name|map
operator|.
name|getInstance
argument_list|(
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Math
operator|.
name|PI
argument_list|,
name|pi
argument_list|,
literal|0.0
argument_list|)
expr_stmt|;
block|}
DECL|method|testPrimitiveAndWrapper ()
specifier|public
name|void
name|testPrimitiveAndWrapper
parameter_list|()
block|{
name|ImmutableClassToInstanceMap
argument_list|<
name|Number
argument_list|>
name|ictim
init|=
operator|new
name|ImmutableClassToInstanceMap
operator|.
name|Builder
argument_list|<
name|Number
argument_list|>
argument_list|()
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
literal|0
argument_list|)
operator|.
name|put
argument_list|(
name|int
operator|.
name|class
argument_list|,
literal|1
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ictim
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
operator|(
name|int
operator|)
name|ictim
operator|.
name|getInstance
argument_list|(
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
name|int
operator|)
name|ictim
operator|.
name|getInstance
argument_list|(
name|int
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|TestClassToInstanceMapGenerator
specifier|abstract
specifier|static
class|class
name|TestClassToInstanceMapGenerator
implements|implements
name|TestMapGenerator
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
block|{
annotation|@
name|Override
DECL|method|createKeyArray (int length)
specifier|public
name|Class
index|[]
name|createKeyArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Class
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|createValueArray (int length)
specifier|public
name|Impl
index|[]
name|createValueArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
return|return
operator|new
name|Impl
index|[
name|length
index|]
return|;
block|}
annotation|@
name|Override
DECL|method|samples ()
specifier|public
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
argument_list|>
name|samples
parameter_list|()
block|{
return|return
operator|new
name|SampleElements
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
argument_list|>
argument_list|(
name|immutableEntry
argument_list|(
operator|(
name|Class
operator|)
name|One
operator|.
name|class
argument_list|,
operator|new
name|Impl
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|,
name|immutableEntry
argument_list|(
operator|(
name|Class
operator|)
name|Two
operator|.
name|class
argument_list|,
operator|new
name|Impl
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|,
name|immutableEntry
argument_list|(
operator|(
name|Class
operator|)
name|Three
operator|.
name|class
argument_list|,
operator|new
name|Impl
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|,
name|immutableEntry
argument_list|(
operator|(
name|Class
operator|)
name|Four
operator|.
name|class
argument_list|,
operator|new
name|Impl
argument_list|(
literal|4
argument_list|)
argument_list|)
argument_list|,
name|immutableEntry
argument_list|(
operator|(
name|Class
operator|)
name|Five
operator|.
name|class
argument_list|,
operator|new
name|Impl
argument_list|(
literal|5
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createArray (int length)
specifier|public
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
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
DECL|method|order (List<Entry<Class, Impl>> insertionOrder)
specifier|public
name|Iterable
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
argument_list|>
argument_list|>
name|order
parameter_list|(
name|List
argument_list|<
name|Entry
argument_list|<
name|Class
argument_list|,
name|Impl
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
DECL|interface|One
specifier|private
interface|interface
name|One
block|{}
DECL|interface|Two
specifier|private
interface|interface
name|Two
block|{}
DECL|interface|Three
specifier|private
interface|interface
name|Three
block|{}
DECL|interface|Four
specifier|private
interface|interface
name|Four
block|{}
DECL|interface|Five
specifier|private
interface|interface
name|Five
block|{}
DECL|class|Impl
specifier|static
specifier|final
class|class
name|Impl
implements|implements
name|One
implements|,
name|Two
implements|,
name|Three
implements|,
name|Four
implements|,
name|Five
implements|,
name|Serializable
block|{
DECL|field|value
specifier|final
name|int
name|value
decl_stmt|;
DECL|method|Impl (int value)
name|Impl
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|Impl
operator|&&
name|value
operator|==
operator|(
operator|(
name|Impl
operator|)
name|obj
operator|)
operator|.
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Integer
operator|.
name|toString
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

