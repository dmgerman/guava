begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2018 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Objects
operator|.
name|requireNonNull
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
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|concurrent
operator|.
name|LazyInit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|RetainedWith
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|j2objc
operator|.
name|annotations
operator|.
name|WeakOuter
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Implementation of ImmutableBiMap backed by a pair of JDK HashMaps, which have smartness  * protecting against hash flooding.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|JdkBackedImmutableBiMap
specifier|final
class|class
name|JdkBackedImmutableBiMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|VisibleForTesting
DECL|method|create (int n, @Nullable Entry<K, V>[] entryArray)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ImmutableBiMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|create
parameter_list|(
name|int
name|n
parameter_list|,
annotation|@
name|Nullable
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
index|[]
name|entryArray
parameter_list|)
block|{
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forwardDelegate
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backwardDelegate
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|n
argument_list|)
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
name|n
condition|;
name|i
operator|++
control|)
block|{
comment|// requireNonNull is safe because the first `n` elements have been filled in.
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|e
init|=
name|RegularImmutableMap
operator|.
name|makeImmutable
argument_list|(
name|requireNonNull
argument_list|(
name|entryArray
index|[
name|i
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|entryArray
index|[
name|i
index|]
operator|=
name|e
expr_stmt|;
name|V
name|oldValue
init|=
name|forwardDelegate
operator|.
name|putIfAbsent
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
throw|throw
name|conflictException
argument_list|(
literal|"key"
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|oldValue
argument_list|,
name|entryArray
index|[
name|i
index|]
argument_list|)
throw|;
block|}
name|K
name|oldKey
init|=
name|backwardDelegate
operator|.
name|putIfAbsent
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldKey
operator|!=
literal|null
condition|)
block|{
throw|throw
name|conflictException
argument_list|(
literal|"value"
argument_list|,
name|oldKey
operator|+
literal|"="
operator|+
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|entryArray
index|[
name|i
index|]
argument_list|)
throw|;
block|}
block|}
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entryList
init|=
name|ImmutableList
operator|.
name|asImmutableList
argument_list|(
name|entryArray
argument_list|,
name|n
argument_list|)
decl_stmt|;
return|return
operator|new
name|JdkBackedImmutableBiMap
argument_list|<>
argument_list|(
name|entryList
argument_list|,
name|forwardDelegate
argument_list|,
name|backwardDelegate
argument_list|)
return|;
block|}
DECL|field|entries
specifier|private
specifier|final
specifier|transient
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
decl_stmt|;
DECL|field|forwardDelegate
specifier|private
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forwardDelegate
decl_stmt|;
DECL|field|backwardDelegate
specifier|private
specifier|final
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backwardDelegate
decl_stmt|;
DECL|method|JdkBackedImmutableBiMap ( ImmutableList<Entry<K, V>> entries, Map<K, V> forwardDelegate, Map<V, K> backwardDelegate)
specifier|private
name|JdkBackedImmutableBiMap
parameter_list|(
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|entries
parameter_list|,
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|forwardDelegate
parameter_list|,
name|Map
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|backwardDelegate
parameter_list|)
block|{
name|this
operator|.
name|entries
operator|=
name|entries
expr_stmt|;
name|this
operator|.
name|forwardDelegate
operator|=
name|forwardDelegate
expr_stmt|;
name|this
operator|.
name|backwardDelegate
operator|=
name|backwardDelegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|entries
operator|.
name|size
argument_list|()
return|;
block|}
DECL|field|inverse
annotation|@
name|LazyInit
annotation|@
name|RetainedWith
annotation|@
name|CheckForNull
specifier|private
specifier|transient
name|JdkBackedImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
decl_stmt|;
annotation|@
name|Override
DECL|method|inverse ()
specifier|public
name|ImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|inverse
parameter_list|()
block|{
name|JdkBackedImmutableBiMap
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|result
init|=
name|inverse
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|inverse
operator|=
name|result
operator|=
operator|new
name|JdkBackedImmutableBiMap
argument_list|<>
argument_list|(
operator|new
name|InverseEntries
argument_list|()
argument_list|,
name|backwardDelegate
argument_list|,
name|forwardDelegate
argument_list|)
expr_stmt|;
name|result
operator|.
name|inverse
operator|=
name|this
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|WeakOuter
DECL|class|InverseEntries
specifier|private
specifier|final
class|class
name|InverseEntries
extends|extends
name|ImmutableList
argument_list|<
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get (int index)
specifier|public
name|Entry
argument_list|<
name|V
argument_list|,
name|K
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
init|=
name|entries
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|Maps
operator|.
name|immutableEntry
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|entries
operator|.
name|size
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|get (@heckForNull Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|key
parameter_list|)
block|{
return|return
name|forwardDelegate
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createEntrySet ()
name|ImmutableSet
argument_list|<
name|Entry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|createEntrySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapEntrySet
operator|.
name|RegularEntrySet
argument_list|<>
argument_list|(
name|this
argument_list|,
name|entries
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createKeySet ()
name|ImmutableSet
argument_list|<
name|K
argument_list|>
name|createKeySet
parameter_list|()
block|{
return|return
operator|new
name|ImmutableMapKeySet
argument_list|<>
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

