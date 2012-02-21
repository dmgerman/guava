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

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * A {@link UnicodeEscaper} that uses an array to quickly look up replacement  * characters for a given code point. An additional safe range is provided that  * determines whether code points without specific replacements are to be  * considered safe and left unescaped or should be escaped in a general way.  *  *<p>A good example of usage of this class is for HTML escaping where the  * replacement array contains information about the named HTML entities  * such as {@code&amp;} and {@code&quot;} while {@link #escapeUnsafe} is  * overridden to handle general escaping of the form {@code&#NNNNN;}.  *  *<p>The size of the data structure used by {@link ArrayBasedUnicodeEscaper} is  * proportional to the highest valued code point that requires escaping.  * For example a replacement map containing the single character  * '{@code \}{@code u1000}' will require approximately 16K of memory. If you  * need to create multiple escaper instances that have the same character  * replacement mapping consider using {@link ArrayBasedEscaperMap}.  *  * @author David Beaumont  * @since 12.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|ArrayBasedUnicodeEscaper
specifier|public
specifier|abstract
class|class
name|ArrayBasedUnicodeEscaper
extends|extends
name|UnicodeEscaper
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
comment|// The first code point in the safe range.
DECL|field|safeMin
specifier|private
specifier|final
name|int
name|safeMin
decl_stmt|;
comment|// The last code point in the safe range.
DECL|field|safeMax
specifier|private
specifier|final
name|int
name|safeMax
decl_stmt|;
comment|// Cropped values used in the fast path range checks.
DECL|field|safeMinChar
specifier|private
specifier|final
name|char
name|safeMinChar
decl_stmt|;
DECL|field|safeMaxChar
specifier|private
specifier|final
name|char
name|safeMaxChar
decl_stmt|;
comment|/**    * Creates a new ArrayBasedUnicodeEscaper instance with the given replacement    * map and specified safe range. If {@code safeMax< safeMin} then no code    * points are considered safe.    *    *<p>If a code point has no mapped replacement then it is checked against the    * safe range. If it lies outside that, then {@link #escapeUnsafe} is    * called, otherwise no escaping is performed.    *    * @param replacementMap a map of characters to their escaped representations    * @param safeMin the lowest character value in the safe range    * @param safeMax the highest character value in the safe range    * @param unsafeReplacement the default replacement for unsafe characters or    *     null if no default replacement is required    */
DECL|method|ArrayBasedUnicodeEscaper (Map<Character, String> replacementMap, int safeMin, int safeMax, @Nullable String unsafeReplacement)
specifier|protected
name|ArrayBasedUnicodeEscaper
parameter_list|(
name|Map
argument_list|<
name|Character
argument_list|,
name|String
argument_list|>
name|replacementMap
parameter_list|,
name|int
name|safeMin
parameter_list|,
name|int
name|safeMax
parameter_list|,
annotation|@
name|Nullable
name|String
name|unsafeReplacement
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
argument_list|,
name|unsafeReplacement
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new ArrayBasedUnicodeEscaper instance with the given replacement    * map and specified safe range. If {@code safeMax< safeMin} then no code    * points are considered safe. This initializer is useful when explicit    * instances of ArrayBasedEscaperMap are used to allow the sharing of large    * replacement mappings.    *    *<p>If a code point has no mapped replacement then it is checked against the    * safe range. If it lies outside that, then {@link #escapeUnsafe} is    * called, otherwise no escaping is performed.    *    * @param escaperMap the map of replacements    * @param safeMin the lowest character value in the safe range    * @param safeMax the highest character value in the safe range    * @param unsafeReplacement the default replacement for unsafe characters or    *     null if no default replacement is required    */
DECL|method|ArrayBasedUnicodeEscaper (ArrayBasedEscaperMap escaperMap, int safeMin, int safeMax, @Nullable String unsafeReplacement)
specifier|protected
name|ArrayBasedUnicodeEscaper
parameter_list|(
name|ArrayBasedEscaperMap
name|escaperMap
parameter_list|,
name|int
name|safeMin
parameter_list|,
name|int
name|safeMax
parameter_list|,
annotation|@
name|Nullable
name|String
name|unsafeReplacement
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
comment|// to ensure the first test of either value will fail.
name|safeMax
operator|=
operator|-
literal|1
expr_stmt|;
name|safeMin
operator|=
name|Integer
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
comment|// This is a bit of a hack but lets us do quicker per-character checks in
comment|// the fast path code. The safe min/max values are very unlikely to extend
comment|// into the range of surrogate characters, but if they do we must not test
comment|// any values in that range. To see why, consider the case where:
comment|//   safeMin<= {hi,lo}<= safeMax
comment|// where {hi,lo} are characters forming a surrogate pair such that:
comment|//   codePointOf(hi, lo)> safeMax
comment|// which would result in the surrogate pair being (wrongly) considered safe.
comment|// If we clip the safe range used during the per-character tests so it is
comment|// below the values of characters in surrogate pairs, this cannot occur.
comment|// This approach does mean that we break out of the fast path code in cases
comment|// where we don't strictly need to, but this situation will almost never
comment|// occur in practice.
if|if
condition|(
name|safeMin
operator|>=
name|Character
operator|.
name|MIN_HIGH_SURROGATE
condition|)
block|{
comment|// The safe range is empty or the all safe code points lie in or above the
comment|// surrogate range. Either way the character range is empty.
name|this
operator|.
name|safeMinChar
operator|=
name|Character
operator|.
name|MAX_VALUE
expr_stmt|;
name|this
operator|.
name|safeMaxChar
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
comment|// The safe range is non empty and contains values below the surrogate
comment|// range but may extend above it. We may need to clip the maximum value.
name|this
operator|.
name|safeMinChar
operator|=
operator|(
name|char
operator|)
name|safeMin
expr_stmt|;
name|this
operator|.
name|safeMaxChar
operator|=
operator|(
name|char
operator|)
name|Math
operator|.
name|min
argument_list|(
name|safeMax
argument_list|,
name|Character
operator|.
name|MIN_HIGH_SURROGATE
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
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
comment|// GWT specific check (do not optimize)
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
name|safeMaxChar
operator|||
name|c
operator|<
name|safeMinChar
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
comment|/* Overridden for performance. */
annotation|@
name|Override
DECL|method|nextEscapeIndex (CharSequence csq, int index, int end)
specifier|protected
specifier|final
name|int
name|nextEscapeIndex
parameter_list|(
name|CharSequence
name|csq
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|end
parameter_list|)
block|{
while|while
condition|(
name|index
operator|<
name|end
condition|)
block|{
name|char
name|c
init|=
name|csq
operator|.
name|charAt
argument_list|(
name|index
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
name|safeMaxChar
operator|||
name|c
operator|<
name|safeMinChar
condition|)
block|{
break|break;
block|}
name|index
operator|++
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
comment|/**    * Escapes a single Unicode code point using the replacement array and safe    * range values. If the given character does not have an explicit replacement    * and lies outside the safe range then {@link #escapeUnsafe} is called.    */
annotation|@
name|Override
DECL|method|escape (int cp)
specifier|protected
specifier|final
name|char
index|[]
name|escape
parameter_list|(
name|int
name|cp
parameter_list|)
block|{
if|if
condition|(
name|cp
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
name|cp
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
name|cp
operator|>=
name|safeMin
operator|&&
name|cp
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
name|cp
argument_list|)
return|;
block|}
comment|/**    * Escapes a code point that has no direct explicit value in the replacement    * array and lies outside the stated safe range. Subclasses should override    * this method to provide generalized escaping for code points if required.    *    *<p>Note that arrays returned by this method must not be modified once they    * have been returned. However it is acceptable to return the same array    * multiple times (even for different input characters).    *    * @param cp the Unicode code point to escape    * @return the replacement characters, or {@code null} if no escaping was    *         required    */
DECL|method|escapeUnsafe (int cp)
specifier|protected
specifier|abstract
name|char
index|[]
name|escapeUnsafe
parameter_list|(
name|int
name|cp
parameter_list|)
function_decl|;
block|}
end_class

end_unit

