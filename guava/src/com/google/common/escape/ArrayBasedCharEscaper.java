begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.escape
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|escape
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
name|Beta
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A {@link CharEscaper} that uses an array to quickly look up replacement  * characters for a given {@code char} value. An additional safe range is  * provided that determines whether {@code char} values without specific  * replacements are to be considered safe and left unescaped or should be  * escaped in a general way.  *  *<p>A good example of usage of this class is for Java source code escaping  * where the replacement array contains information about special ASCII  * characters such as {@code \\t} and {@code \\n} while {@link #escapeUnsafe}  * is overridden to handle general escaping of the form {@code \\uxxxx}.  *  *<p>The size of the data structure used by {@link ArrayBasedCharEscaper} is  * proportional to the highest valued character that requires escaping.  * For example a replacement map containing the single character  * '{@code \}{@code u1000}' will require approximately 16K of memory. If you  * need to create multiple escaper instances that have the same character  * replacement mapping consider using {@link ArrayBasedEscaperMap}.  *  * @author Sven Mawson  * @author David Beaumont  * @since 15.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|ArrayBasedCharEscaper
specifier|public
specifier|abstract
class|class
name|ArrayBasedCharEscaper
extends|extends
name|CharEscaper
block|{
comment|// The replacement array (see ArrayBasedEscaperMap).
DECL|field|replacements
specifier|private
specifier|final
name|char
index|[]
index|[]
name|replacements
decl_stmt|;
comment|// The number of elements in the replacement array.
DECL|field|replacementsLength
specifier|private
specifier|final
name|int
name|replacementsLength
decl_stmt|;
comment|// The first character in the safe range.
DECL|field|safeMin
specifier|private
specifier|final
name|char
name|safeMin
decl_stmt|;
comment|// The last character in the safe range.
DECL|field|safeMax
specifier|private
specifier|final
name|char
name|safeMax
decl_stmt|;
comment|/**    * Creates a new ArrayBasedCharEscaper instance with the given replacement map    * and specified safe range. If {@code safeMax< safeMin} then no characters    * are considered safe.    *    *<p>If a character has no mapped replacement then it is checked against the    * safe range. If it lies outside that, then {@link #escapeUnsafe} is    * called, otherwise no escaping is performed.    *    * @param replacementMap a map of characters to their escaped representations    * @param safeMin the lowest character value in the safe range    * @param safeMax the highest character value in the safe range    */
DECL|method|ArrayBasedCharEscaper (Map<Character, String> replacementMap, char safeMin, char safeMax)
specifier|protected
name|ArrayBasedCharEscaper
parameter_list|(
name|Map
argument_list|<
name|Character
argument_list|,
name|String
argument_list|>
name|replacementMap
parameter_list|,
name|char
name|safeMin
parameter_list|,
name|char
name|safeMax
parameter_list|)
block|{
name|this
argument_list|(
name|ArrayBasedEscaperMap
operator|.
name|create
argument_list|(
name|replacementMap
argument_list|)
argument_list|,
name|safeMin
argument_list|,
name|safeMax
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new ArrayBasedCharEscaper instance with the given replacement map    * and specified safe range. If {@code safeMax< safeMin} then no characters    * are considered safe. This initializer is useful when explicit instances of    * ArrayBasedEscaperMap are used to allow the sharing of large replacement    * mappings.    *    *<p>If a character has no mapped replacement then it is checked against the    * safe range. If it lies outside that, then {@link #escapeUnsafe} is    * called, otherwise no escaping is performed.    *    * @param escaperMap the mapping of characters to be escaped    * @param safeMin the lowest character value in the safe range    * @param safeMax the highest character value in the safe range    */
DECL|method|ArrayBasedCharEscaper (ArrayBasedEscaperMap escaperMap, char safeMin, char safeMax)
specifier|protected
name|ArrayBasedCharEscaper
parameter_list|(
name|ArrayBasedEscaperMap
name|escaperMap
parameter_list|,
name|char
name|safeMin
parameter_list|,
name|char
name|safeMax
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|escaperMap
argument_list|)
expr_stmt|;
comment|// GWT specific check (do not optimize)
name|this
operator|.
name|replacements
operator|=
name|escaperMap
operator|.
name|getReplacementArray
argument_list|()
expr_stmt|;
name|this
operator|.
name|replacementsLength
operator|=
name|replacements
operator|.
name|length
expr_stmt|;
if|if
condition|(
name|safeMax
operator|<
name|safeMin
condition|)
block|{
comment|// If the safe range is empty, set the range limits to opposite extremes
comment|// to ensure the first test of either value will (almost certainly) fail.
name|safeMax
operator|=
name|Character
operator|.
name|MIN_VALUE
expr_stmt|;
name|safeMin
operator|=
name|Character
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
name|this
operator|.
name|safeMin
operator|=
name|safeMin
expr_stmt|;
name|this
operator|.
name|safeMax
operator|=
name|safeMax
expr_stmt|;
block|}
comment|/*    * This is overridden to improve performance. Rough benchmarking shows that    * this almost doubles the speed when processing strings that do not require    * any escaping.    */
annotation|@
name|Override
DECL|method|escape (String s)
specifier|public
specifier|final
name|String
name|escape
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
comment|// GWT specific check (do not optimize).
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|s
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|s
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|c
operator|<
name|replacementsLength
operator|&&
name|replacements
index|[
name|c
index|]
operator|!=
literal|null
operator|)
operator|||
name|c
operator|>
name|safeMax
operator|||
name|c
operator|<
name|safeMin
condition|)
block|{
return|return
name|escapeSlow
argument_list|(
name|s
argument_list|,
name|i
argument_list|)
return|;
block|}
block|}
return|return
name|s
return|;
block|}
comment|/**    * Escapes a single character using the replacement array and safe range    * values. If the given character does not have an explicit replacement and    * lies outside the safe range then {@link #escapeUnsafe} is called.    */
DECL|method|escape (char c)
annotation|@
name|Override
specifier|protected
specifier|final
name|char
index|[]
name|escape
parameter_list|(
name|char
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|<
name|replacementsLength
condition|)
block|{
name|char
index|[]
name|chars
init|=
name|replacements
index|[
name|c
index|]
decl_stmt|;
if|if
condition|(
name|chars
operator|!=
literal|null
condition|)
block|{
return|return
name|chars
return|;
block|}
block|}
if|if
condition|(
name|c
operator|>=
name|safeMin
operator|&&
name|c
operator|<=
name|safeMax
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|escapeUnsafe
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**    * Escapes a {@code char} value that has no direct explicit value in the    * replacement array and lies outside the stated safe range. Subclasses should    * override this method to provide generalized escaping for characters.    *    *<p>Note that arrays returned by this method must not be modified once they    * have been returned. However it is acceptable to return the same array    * multiple times (even for different input characters).    *    * @param c the character to escape    * @return the replacement characters, or {@code null} if no escaping was    *         required    */
comment|// TODO(user,cpovirk): Rename this something better once refactoring done
DECL|method|escapeUnsafe (char c)
specifier|protected
specifier|abstract
name|char
index|[]
name|escapeUnsafe
parameter_list|(
name|char
name|c
parameter_list|)
function_decl|;
block|}
end_class

end_unit

