begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.thirdparty.publicsuffix
package|package
name|com
operator|.
name|google
operator|.
name|thirdparty
operator|.
name|publicsuffix
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
name|Joiner
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
name|ImmutableMap
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Parser for a map of reversed domain names stored as a serialized radix tree.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|TrieParser
specifier|final
class|class
name|TrieParser
block|{
DECL|field|PREFIX_JOINER
specifier|private
specifier|static
specifier|final
name|Joiner
name|PREFIX_JOINER
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|""
argument_list|)
decl_stmt|;
comment|/**    * Parses a serialized trie representation of a map of reversed public suffixes into an immutable    * map of public suffixes.    */
DECL|method|parseTrie (CharSequence encoded)
specifier|static
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|PublicSuffixType
argument_list|>
name|parseTrie
parameter_list|(
name|CharSequence
name|encoded
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|PublicSuffixType
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|int
name|encodedLen
init|=
name|encoded
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|idx
operator|<
name|encodedLen
condition|)
block|{
name|idx
operator|+=
name|doParseTrieToBuilder
argument_list|(
name|Lists
operator|.
expr|<
name|CharSequence
operator|>
name|newLinkedList
argument_list|()
argument_list|,
name|encoded
operator|.
name|subSequence
argument_list|(
name|idx
argument_list|,
name|encodedLen
argument_list|)
argument_list|,
name|builder
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
comment|/**    * Parses a trie node and returns the number of characters consumed.    *    * @param stack The prefixes that precede the characters represented by this node. Each entry of    *     the stack is in reverse order.    * @param encoded The serialized trie.    * @param builder A map builder to which all entries will be added.    * @return The number of characters consumed from {@code encoded}.    */
DECL|method|doParseTrieToBuilder ( List<CharSequence> stack, CharSequence encoded, ImmutableMap.Builder<String, PublicSuffixType> builder)
specifier|private
specifier|static
name|int
name|doParseTrieToBuilder
parameter_list|(
name|List
argument_list|<
name|CharSequence
argument_list|>
name|stack
parameter_list|,
name|CharSequence
name|encoded
parameter_list|,
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|PublicSuffixType
argument_list|>
name|builder
parameter_list|)
block|{
name|int
name|encodedLen
init|=
name|encoded
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
name|char
name|c
init|=
literal|'\0'
decl_stmt|;
comment|// Read all of the characters for this node.
for|for
control|(
init|;
name|idx
operator|<
name|encodedLen
condition|;
name|idx
operator|++
control|)
block|{
name|c
operator|=
name|encoded
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'&'
operator|||
name|c
operator|==
literal|'?'
operator|||
name|c
operator|==
literal|'!'
operator|||
name|c
operator|==
literal|':'
operator|||
name|c
operator|==
literal|','
condition|)
block|{
break|break;
block|}
block|}
name|stack
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|reverse
argument_list|(
name|encoded
operator|.
name|subSequence
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|'!'
operator|||
name|c
operator|==
literal|'?'
operator|||
name|c
operator|==
literal|':'
operator|||
name|c
operator|==
literal|','
condition|)
block|{
comment|// '!' represents an interior node that represents an ICANN entry in the map.
comment|// '?' represents a leaf node, which represents an ICANN entry in map.
comment|// ':' represents an interior node that represents a private entry in the map
comment|// ',' represents a leaf node, which represents a private entry in the map.
name|String
name|domain
init|=
name|PREFIX_JOINER
operator|.
name|join
argument_list|(
name|stack
argument_list|)
decl_stmt|;
if|if
condition|(
name|domain
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|put
argument_list|(
name|domain
argument_list|,
name|PublicSuffixType
operator|.
name|fromCode
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|idx
operator|++
expr_stmt|;
if|if
condition|(
name|c
operator|!=
literal|'?'
operator|&&
name|c
operator|!=
literal|','
condition|)
block|{
while|while
condition|(
name|idx
operator|<
name|encodedLen
condition|)
block|{
comment|// Read all the children
name|idx
operator|+=
name|doParseTrieToBuilder
argument_list|(
name|stack
argument_list|,
name|encoded
operator|.
name|subSequence
argument_list|(
name|idx
argument_list|,
name|encodedLen
argument_list|)
argument_list|,
name|builder
argument_list|)
expr_stmt|;
if|if
condition|(
name|encoded
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
operator|==
literal|'?'
operator|||
name|encoded
operator|.
name|charAt
argument_list|(
name|idx
argument_list|)
operator|==
literal|','
condition|)
block|{
comment|// An extra '?' or ',' after a child node indicates the end of all children of this node.
name|idx
operator|++
expr_stmt|;
break|break;
block|}
block|}
block|}
name|stack
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|idx
return|;
block|}
DECL|method|reverse (CharSequence s)
specifier|private
specifier|static
name|CharSequence
name|reverse
parameter_list|(
name|CharSequence
name|s
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|(
name|s
argument_list|)
operator|.
name|reverse
argument_list|()
return|;
block|}
block|}
end_class

end_unit

