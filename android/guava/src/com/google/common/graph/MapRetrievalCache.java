begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.graph
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|graph
package|;
end_package

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
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|compatqual
operator|.
name|NullableDecl
import|;
end_import

begin_comment
comment|/**  * A {@link MapIteratorCache} that adds additional caching. In addition to the caching provided by  * {@link MapIteratorCache}, this structure caches values for the two most recently retrieved keys.  *  * @author James Sexton  */
end_comment

begin_class
DECL|class|MapRetrievalCache
class|class
name|MapRetrievalCache
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|MapIteratorCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
comment|// See the note about volatile in the superclass.
DECL|field|cacheEntry1
annotation|@
name|NullableDecl
specifier|private
specifier|transient
specifier|volatile
name|CacheEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cacheEntry1
decl_stmt|;
DECL|field|cacheEntry2
annotation|@
name|NullableDecl
specifier|private
specifier|transient
specifier|volatile
name|CacheEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cacheEntry2
decl_stmt|;
DECL|method|MapRetrievalCache (Map<K, V> backingMap)
name|MapRetrievalCache
parameter_list|(
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|backingMap
parameter_list|)
block|{
name|super
argument_list|(
name|backingMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Safe because we only cast if key is found in map.
annotation|@
name|Override
DECL|method|get (@ullableDecl Object key)
specifier|public
name|V
name|get
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|V
name|value
init|=
name|getIfCached
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
name|value
operator|=
name|getWithoutCaching
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|addToCache
argument_list|(
operator|(
name|K
operator|)
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
comment|// Internal methods (package-visible, but treat as only subclass-visible)
annotation|@
name|Override
DECL|method|getIfCached (@ullableDecl Object key)
name|V
name|getIfCached
parameter_list|(
annotation|@
name|NullableDecl
name|Object
name|key
parameter_list|)
block|{
name|V
name|value
init|=
name|super
operator|.
name|getIfCached
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
return|return
name|value
return|;
block|}
comment|// Store a local reference to the cache entry. If the backing map is immutable, this,
comment|// in combination with immutable cache entries, will ensure a thread-safe cache.
name|CacheEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
decl_stmt|;
comment|// Check cache. We use == on purpose because it's cheaper and a cache miss is ok.
name|entry
operator|=
name|cacheEntry1
expr_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
name|entry
operator|.
name|key
operator|==
name|key
condition|)
block|{
return|return
name|entry
operator|.
name|value
return|;
block|}
name|entry
operator|=
name|cacheEntry2
expr_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
operator|&&
name|entry
operator|.
name|key
operator|==
name|key
condition|)
block|{
comment|// Promote second cache entry to first so the access pattern
comment|// [K1, K2, K1, K3, K1, K4...] still hits the cache half the time.
name|addToCache
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
name|entry
operator|.
name|value
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|clearCache ()
name|void
name|clearCache
parameter_list|()
block|{
name|super
operator|.
name|clearCache
argument_list|()
expr_stmt|;
name|cacheEntry1
operator|=
literal|null
expr_stmt|;
name|cacheEntry2
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|addToCache (K key, V value)
specifier|private
name|void
name|addToCache
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|addToCache
argument_list|(
operator|new
name|CacheEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addToCache (CacheEntry<K, V> entry)
specifier|private
name|void
name|addToCache
parameter_list|(
name|CacheEntry
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|entry
parameter_list|)
block|{
comment|// Slide new entry into first cache position. Drop previous entry in second cache position.
name|cacheEntry2
operator|=
name|cacheEntry1
expr_stmt|;
name|cacheEntry1
operator|=
name|entry
expr_stmt|;
block|}
DECL|class|CacheEntry
specifier|private
specifier|static
specifier|final
class|class
name|CacheEntry
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
DECL|field|key
specifier|final
name|K
name|key
decl_stmt|;
DECL|field|value
specifier|final
name|V
name|value
decl_stmt|;
DECL|method|CacheEntry (K key, V value)
name|CacheEntry
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

