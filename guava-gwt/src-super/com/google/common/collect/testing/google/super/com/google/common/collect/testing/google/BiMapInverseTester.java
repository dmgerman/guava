begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
operator|.
name|SERIALIZABLE
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
name|BiMap
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

begin_comment
comment|/**  * Tests for the {@code inverse} view of a BiMap.  *   *<p>This assumes that {@code bimap.inverse().inverse() == bimap}, which is not technically  * required but is fulfilled by all current implementations.  *   * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|BiMapInverseTester
specifier|public
class|class
name|BiMapInverseTester
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|AbstractBiMapTester
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|method|testInverseSame ()
specifier|public
name|void
name|testInverseSame
parameter_list|()
block|{
name|assertSame
argument_list|(
name|getMap
argument_list|()
argument_list|,
name|getMap
argument_list|()
operator|.
name|inverse
argument_list|()
operator|.
name|inverse
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|CollectionFeature
operator|.
name|Require
argument_list|(
name|SERIALIZABLE
argument_list|)
DECL|method|testInverseSerialization ()
specifier|public
name|void
name|testInverseSerialization
parameter_list|()
block|{
name|BiMapPair
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|pair
init|=
operator|new
name|BiMapPair
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|getMap
argument_list|()
argument_list|)
decl_stmt|;
name|BiMapPair
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|copy
init|=
name|SerializableTester
operator|.
name|reserialize
argument_list|(
name|pair
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|pair
operator|.
name|forward
argument_list|,
name|copy
operator|.
name|forward
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|pair
operator|.
name|backward
argument_list|,
name|copy
operator|.
name|backward
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|copy
operator|.
name|backward
argument_list|,
name|copy
operator|.
name|forward
operator|.
name|inverse
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|copy
operator|.
name|forward
argument_list|,
name|copy
operator|.
name|backward
operator|.
name|inverse
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|BiMapPair
specifier|private
specifier|static
class|class
name|BiMapPair
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Serializable
block|{
DECL|field|forward
specifier|final
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forward
decl_stmt|;
DECL|field|backward
specifier|final
name|BiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backward
decl_stmt|;
DECL|method|BiMapPair (BiMap<K, V> original)
name|BiMapPair
parameter_list|(
name|BiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|original
parameter_list|)
block|{
name|this
operator|.
name|forward
operator|=
name|original
expr_stmt|;
name|this
operator|.
name|backward
operator|=
name|original
operator|.
name|inverse
argument_list|()
expr_stmt|;
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

