begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|MapConstraintsTest
operator|.
name|TestKeyException
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
name|MapConstraintsTest
operator|.
name|TestValueException
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
name|collect
operator|.
name|testing
operator|.
name|google
operator|.
name|BiMapTestSuiteBuilder
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
name|TestStringBiMapGenerator
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
comment|/**  * Tests for {@link MapConstraints#constrainedBiMap}.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ConstrainedBiMapTest
specifier|public
class|class
name|ConstrainedBiMapTest
extends|extends
name|TestCase
block|{
DECL|field|TEST_KEY
specifier|private
specifier|static
specifier|final
name|String
name|TEST_KEY
init|=
literal|"42"
decl_stmt|;
DECL|field|TEST_VALUE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"test"
decl_stmt|;
DECL|field|TEST_CONSTRAINT
specifier|private
specifier|static
specifier|final
name|MapConstraint
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|TEST_CONSTRAINT
init|=
operator|new
name|TestConstraint
argument_list|()
decl_stmt|;
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
name|BiMapTestSuiteBuilder
operator|.
name|using
argument_list|(
operator|new
name|ConstrainedBiMapGenerator
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"Maps.constrainedBiMap[HashBiMap]"
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
name|SUPPORTS_ITERATOR_REMOVE
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_KEYS
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_NULL_VALUES
argument_list|,
name|MapFeature
operator|.
name|ALLOWS_ANY_NULL_QUERIES
argument_list|,
name|MapFeature
operator|.
name|GENERAL_PURPOSE
argument_list|,
name|MapFeature
operator|.
name|REJECTS_DUPLICATES_AT_CREATION
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
name|ConstrainedBiMapTest
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|suite
return|;
block|}
DECL|method|testPutWithForbiddenKeyForbiddenValue ()
specifier|public
name|void
name|testPutWithForbiddenKeyForbiddenValue
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|MapConstraints
operator|.
name|constrainedBiMap
argument_list|(
name|HashBiMap
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
DECL|method|testPutWithForbiddenKeyAllowedValue ()
specifier|public
name|void
name|testPutWithForbiddenKeyAllowedValue
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|MapConstraints
operator|.
name|constrainedBiMap
argument_list|(
name|HashBiMap
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|put
argument_list|(
name|TEST_KEY
argument_list|,
literal|"allowed"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
DECL|method|testPutWithAllowedKeyForbiddenValue ()
specifier|public
name|void
name|testPutWithAllowedKeyForbiddenValue
parameter_list|()
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|MapConstraints
operator|.
name|constrainedBiMap
argument_list|(
name|HashBiMap
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
try|try
block|{
name|map
operator|.
name|put
argument_list|(
literal|"allowed"
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{
comment|// success
block|}
block|}
DECL|class|ConstrainedBiMapGenerator
specifier|public
specifier|static
specifier|final
class|class
name|ConstrainedBiMapGenerator
extends|extends
name|TestStringBiMapGenerator
block|{
annotation|@
name|Override
DECL|method|create (Entry<String, String>[] entries)
specifier|protected
name|BiMap
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
block|{
name|BiMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|bimap
init|=
name|MapConstraints
operator|.
name|constrainedBiMap
argument_list|(
name|HashBiMap
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|create
argument_list|()
argument_list|,
name|TEST_CONSTRAINT
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|checkArgument
argument_list|(
operator|!
name|bimap
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bimap
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
name|bimap
return|;
block|}
block|}
DECL|class|TestConstraint
specifier|private
specifier|static
specifier|final
class|class
name|TestConstraint
implements|implements
name|MapConstraint
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|checkKeyValue (String key, String value)
specifier|public
name|void
name|checkKeyValue
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|TEST_KEY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TestKeyException
argument_list|()
throw|;
block|}
if|if
condition|(
name|TEST_VALUE
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TestValueException
argument_list|()
throw|;
block|}
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
block|}
end_class

end_unit

