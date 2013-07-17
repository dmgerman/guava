begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|ArrayList
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|javax
operator|.
name|annotation
operator|.
name|CheckReturnValue
import|;
end_import

begin_comment
comment|/**  * Extracts non-overlapping substrings from an input string, typically by  * recognizing appearances of a<i>separator</i> sequence. This separator can be  * specified as a single {@linkplain #on(char) character}, fixed {@linkplain  * #on(String) string}, {@linkplain #onPattern regular expression} or {@link  * #on(CharMatcher) CharMatcher} instance. Or, instead of using a separator at  * all, a splitter can extract adjacent substrings of a given {@linkplain  * #fixedLength fixed length}.  *  *<p>For example, this expression:<pre>   {@code  *  *   Splitter.on(',').split("foo,bar,qux")}</pre>  *  * ... produces an {@code Iterable} containing {@code "foo"}, {@code "bar"} and  * {@code "qux"}, in that order.  *  *<p>By default, {@code Splitter}'s behavior is simplistic and unassuming. The  * following expression:<pre>   {@code  *  *   Splitter.on(',').split(" foo,,,  bar ,")}</pre>  *  * ... yields the substrings {@code [" foo", "", "", "  bar ", ""]}. If this  * is not the desired behavior, use configuration methods to obtain a<i>new</i>  * splitter instance with modified behavior:<pre>   {@code  *  *   private static final Splitter MY_SPLITTER = Splitter.on(',')  *       .trimResults()  *       .omitEmptyStrings();}</pre>  *  *<p>Now {@code MY_SPLITTER.split("foo,,,  bar ,")} returns just {@code ["foo",  * "bar"]}. Note that the order in which these configuration methods are called  * is never significant.  *  *<p><b>Warning:</b> Splitter instances are immutable. Invoking a configuration  * method has no effect on the receiving instance; you must store and use the  * new splitter instance it returns instead.<pre>   {@code  *  *   // Do NOT do this  *   Splitter splitter = Splitter.on('/');  *   splitter.trimResults(); // does nothing!  *   return splitter.split("wrong / wrong / wrong");}</pre>  *  *<p>For separator-based splitters that do not use {@code omitEmptyStrings}, an  * input string containing {@code n} occurrences of the separator naturally  * yields an iterable of size {@code n + 1}. So if the separator does not occur  * anywhere in the input, a single substring is returned containing the entire  * input. Consequently, all splitters split the empty string to {@code [""]}  * (note: even fixed-length splitters).  *  *<p>Splitter instances are thread-safe immutable, and are therefore safe to  * store as {@code static final} constants.  *  *<p>The {@link Joiner} class provides the inverse operation to splitting, but  * note that a round-trip between the two should be assumed to be lossy.  *  *<p>See the Guava User Guide article on<a href=  * "http://code.google.com/p/guava-libraries/wiki/StringsExplained#Splitter">  * {@code Splitter}</a>.  *  * @author Julien Silland  * @author Jesse Wilson  * @author Kevin Bourrillion  * @author Louis Wasserman  * @since 1.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Splitter
specifier|public
specifier|final
class|class
name|Splitter
block|{
DECL|field|trimmer
specifier|private
specifier|final
name|CharMatcher
name|trimmer
decl_stmt|;
DECL|field|omitEmptyStrings
specifier|private
specifier|final
name|boolean
name|omitEmptyStrings
decl_stmt|;
DECL|field|strategy
specifier|private
specifier|final
name|Strategy
name|strategy
decl_stmt|;
DECL|field|limit
specifier|private
specifier|final
name|int
name|limit
decl_stmt|;
DECL|method|Splitter (Strategy strategy)
specifier|private
name|Splitter
parameter_list|(
name|Strategy
name|strategy
parameter_list|)
block|{
name|this
argument_list|(
name|strategy
argument_list|,
literal|false
argument_list|,
name|CharMatcher
operator|.
name|NONE
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
DECL|method|Splitter (Strategy strategy, boolean omitEmptyStrings, CharMatcher trimmer, int limit)
specifier|private
name|Splitter
parameter_list|(
name|Strategy
name|strategy
parameter_list|,
name|boolean
name|omitEmptyStrings
parameter_list|,
name|CharMatcher
name|trimmer
parameter_list|,
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
name|this
operator|.
name|omitEmptyStrings
operator|=
name|omitEmptyStrings
expr_stmt|;
name|this
operator|.
name|trimmer
operator|=
name|trimmer
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
block|}
comment|/**    * Returns a splitter that uses the given single-character separator. For    * example, {@code Splitter.on(',').split("foo,,bar")} returns an iterable    * containing {@code ["foo", "", "bar"]}.    *    * @param separator the character to recognize as a separator    * @return a splitter, with default settings, that recognizes that separator    */
DECL|method|on (char separator)
specifier|public
specifier|static
name|Splitter
name|on
parameter_list|(
name|char
name|separator
parameter_list|)
block|{
return|return
name|on
argument_list|(
name|CharMatcher
operator|.
name|is
argument_list|(
name|separator
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that considers any single character matched by the    * given {@code CharMatcher} to be a separator. For example, {@code    * Splitter.on(CharMatcher.anyOf(";,")).split("foo,;bar,quux")} returns an    * iterable containing {@code ["foo", "", "bar", "quux"]}.    *    * @param separatorMatcher a {@link CharMatcher} that determines whether a    *     character is a separator    * @return a splitter, with default settings, that uses this matcher    */
DECL|method|on (final CharMatcher separatorMatcher)
specifier|public
specifier|static
name|Splitter
name|on
parameter_list|(
specifier|final
name|CharMatcher
name|separatorMatcher
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|separatorMatcher
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
operator|new
name|Strategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SplittingIterator
name|iterator
parameter_list|(
name|Splitter
name|splitter
parameter_list|,
specifier|final
name|CharSequence
name|toSplit
parameter_list|)
block|{
return|return
operator|new
name|SplittingIterator
argument_list|(
name|splitter
argument_list|,
name|toSplit
argument_list|)
block|{
annotation|@
name|Override
name|int
name|separatorStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
return|return
name|separatorMatcher
operator|.
name|indexIn
argument_list|(
name|toSplit
argument_list|,
name|start
argument_list|)
return|;
block|}
annotation|@
name|Override
name|int
name|separatorEnd
parameter_list|(
name|int
name|separatorPosition
parameter_list|)
block|{
return|return
name|separatorPosition
operator|+
literal|1
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that uses the given fixed string as a separator. For    * example, {@code Splitter.on(", ").split("foo, bar,baz")} returns an    * iterable containing {@code ["foo", "bar,baz"]}.    *    * @param separator the literal, nonempty string to recognize as a separator    * @return a splitter, with default settings, that recognizes that separator    */
DECL|method|on (final String separator)
specifier|public
specifier|static
name|Splitter
name|on
parameter_list|(
specifier|final
name|String
name|separator
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|separator
operator|.
name|length
argument_list|()
operator|!=
literal|0
argument_list|,
literal|"The separator may not be the empty string."
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
operator|new
name|Strategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SplittingIterator
name|iterator
parameter_list|(
name|Splitter
name|splitter
parameter_list|,
name|CharSequence
name|toSplit
parameter_list|)
block|{
return|return
operator|new
name|SplittingIterator
argument_list|(
name|splitter
argument_list|,
name|toSplit
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|separatorStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|int
name|delimeterLength
init|=
name|separator
operator|.
name|length
argument_list|()
decl_stmt|;
name|positions
label|:
for|for
control|(
name|int
name|p
init|=
name|start
init|,
name|last
init|=
name|toSplit
operator|.
name|length
argument_list|()
operator|-
name|delimeterLength
init|;
name|p
operator|<=
name|last
condition|;
name|p
operator|++
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|delimeterLength
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|toSplit
operator|.
name|charAt
argument_list|(
name|i
operator|+
name|p
argument_list|)
operator|!=
name|separator
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
condition|)
block|{
continue|continue
name|positions
continue|;
block|}
block|}
return|return
name|p
return|;
block|}
return|return
operator|-
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|separatorEnd
parameter_list|(
name|int
name|separatorPosition
parameter_list|)
block|{
return|return
name|separatorPosition
operator|+
name|separator
operator|.
name|length
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that divides strings into pieces of the given length.    * For example, {@code Splitter.fixedLength(2).split("abcde")} returns an    * iterable containing {@code ["ab", "cd", "e"]}. The last piece can be    * smaller than {@code length} but will never be empty.    *    *<p><b>Exception:</b> for consistency with separator-based splitters, {@code    * split("")} does not yield an empty iterable, but an iterable containing    * {@code ""}. This is the only case in which {@code    * Iterables.size(split(input))} does not equal {@code    * IntMath.divide(input.length(), length, CEILING)}. To avoid this behavior,    * use {@code omitEmptyStrings}.    *    * @param length the desired length of pieces after splitting, a positive    *     integer    * @return a splitter, with default settings, that can split into fixed sized    *     pieces    * @throws IllegalArgumentException if {@code length} is zero or negative    */
DECL|method|fixedLength (final int length)
specifier|public
specifier|static
name|Splitter
name|fixedLength
parameter_list|(
specifier|final
name|int
name|length
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|length
operator|>
literal|0
argument_list|,
literal|"The length may not be less than 1"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
operator|new
name|Strategy
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SplittingIterator
name|iterator
parameter_list|(
specifier|final
name|Splitter
name|splitter
parameter_list|,
name|CharSequence
name|toSplit
parameter_list|)
block|{
return|return
operator|new
name|SplittingIterator
argument_list|(
name|splitter
argument_list|,
name|toSplit
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|int
name|separatorStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
name|int
name|nextChunkStart
init|=
name|start
operator|+
name|length
decl_stmt|;
return|return
operator|(
name|nextChunkStart
operator|<
name|toSplit
operator|.
name|length
argument_list|()
condition|?
name|nextChunkStart
else|:
operator|-
literal|1
operator|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|separatorEnd
parameter_list|(
name|int
name|separatorPosition
parameter_list|)
block|{
return|return
name|separatorPosition
return|;
block|}
block|}
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that behaves equivalently to {@code this} splitter, but    * automatically omits empty strings from the results. For example, {@code    * Splitter.on(',').omitEmptyStrings().split(",a,,,b,c,,")} returns an    * iterable containing only {@code ["a", "b", "c"]}.    *    *<p>If either {@code trimResults} option is also specified when creating a    * splitter, that splitter always trims results first before checking for    * emptiness. So, for example, {@code    * Splitter.on(':').omitEmptyStrings().trimResults().split(": : : ")} returns    * an empty iterable.    *    *<p>Note that it is ordinarily not possible for {@link #split(CharSequence)}    * to return an empty iterable, but when using this option, it can (if the    * input sequence consists of nothing but separators).    *    * @return a splitter with the desired configuration    */
annotation|@
name|CheckReturnValue
DECL|method|omitEmptyStrings ()
specifier|public
name|Splitter
name|omitEmptyStrings
parameter_list|()
block|{
return|return
operator|new
name|Splitter
argument_list|(
name|strategy
argument_list|,
literal|true
argument_list|,
name|trimmer
argument_list|,
name|limit
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that behaves equivalently to {@code this} splitter but    * stops splitting after it reaches the limit.    * The limit defines the maximum number of items returned by the iterator.    *    *<p>For example,    * {@code Splitter.on(',').limit(3).split("a,b,c,d")} returns an iterable    * containing {@code ["a", "b", "c,d"]}.  When omitting empty strings, the    * omitted strings do no count.  Hence,    * {@code Splitter.on(',').limit(3).omitEmptyStrings().split("a,,,b,,,c,d")}    * returns an iterable containing {@code ["a", "b", "c,d"}.    * When trim is requested, all entries, including the last are trimmed.  Hence    * {@code Splitter.on(',').limit(3).trimResults().split(" a , b , c , d ")}    * results in @{code ["a", "b", "c , d"]}.    *    * @param limit the maximum number of items returns    * @return a splitter with the desired configuration    * @since 9.0    */
annotation|@
name|CheckReturnValue
DECL|method|limit (int limit)
specifier|public
name|Splitter
name|limit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|limit
operator|>
literal|0
argument_list|,
literal|"must be greater than zero: %s"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
name|strategy
argument_list|,
name|omitEmptyStrings
argument_list|,
name|trimmer
argument_list|,
name|limit
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that behaves equivalently to {@code this} splitter, but    * automatically removes leading and trailing {@linkplain    * CharMatcher#WHITESPACE whitespace} from each returned substring; equivalent    * to {@code trimResults(CharMatcher.WHITESPACE)}. For example, {@code    * Splitter.on(',').trimResults().split(" a, b ,c ")} returns an iterable    * containing {@code ["a", "b", "c"]}.    *    * @return a splitter with the desired configuration    */
annotation|@
name|CheckReturnValue
DECL|method|trimResults ()
specifier|public
name|Splitter
name|trimResults
parameter_list|()
block|{
return|return
name|trimResults
argument_list|(
name|CharMatcher
operator|.
name|WHITESPACE
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that behaves equivalently to {@code this} splitter, but    * removes all leading or trailing characters matching the given {@code    * CharMatcher} from each returned substring. For example, {@code    * Splitter.on(',').trimResults(CharMatcher.is('_')).split("_a ,_b_ ,c__")}    * returns an iterable containing {@code ["a ", "b_ ", "c"]}.    *    * @param trimmer a {@link CharMatcher} that determines whether a character    *     should be removed from the beginning/end of a subsequence    * @return a splitter with the desired configuration    */
comment|// TODO(kevinb): throw if a trimmer was already specified!
annotation|@
name|CheckReturnValue
DECL|method|trimResults (CharMatcher trimmer)
specifier|public
name|Splitter
name|trimResults
parameter_list|(
name|CharMatcher
name|trimmer
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|trimmer
argument_list|)
expr_stmt|;
return|return
operator|new
name|Splitter
argument_list|(
name|strategy
argument_list|,
name|omitEmptyStrings
argument_list|,
name|trimmer
argument_list|,
name|limit
argument_list|)
return|;
block|}
comment|/**    * Splits {@code sequence} into string components and makes them available    * through an {@link Iterator}, which may be lazily evaluated. If you want    * an eagerly computed {@link List}, use {@link #splitToList(CharSequence)}.    *    * @param sequence the sequence of characters to split    * @return an iteration over the segments split from the parameter.    */
DECL|method|split (final CharSequence sequence)
specifier|public
name|Iterable
argument_list|<
name|String
argument_list|>
name|split
parameter_list|(
specifier|final
name|CharSequence
name|sequence
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
return|return
operator|new
name|Iterable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|spliterator
argument_list|(
name|sequence
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|appendTo
argument_list|(
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
argument_list|,
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|spliterator (CharSequence sequence)
specifier|private
name|Iterator
argument_list|<
name|String
argument_list|>
name|spliterator
parameter_list|(
name|CharSequence
name|sequence
parameter_list|)
block|{
return|return
name|strategy
operator|.
name|iterator
argument_list|(
name|this
argument_list|,
name|sequence
argument_list|)
return|;
block|}
comment|/**    * Splits {@code sequence} into string components and returns them as    * an immutable list. If you want an {@link Iterable} which may be lazily    * evaluated, use {@link #split(CharSequence)}.    *    * @param sequence the sequence of characters to split    * @return an immutable list of the segments split from the parameter    * @since 15.0    */
annotation|@
name|Beta
DECL|method|splitToList (CharSequence sequence)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|splitToList
parameter_list|(
name|CharSequence
name|sequence
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
init|=
name|spliterator
argument_list|(
name|sequence
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code MapSplitter} which splits entries based on this splitter,    * and splits entries into keys and values using the specified separator.    *    * @since 10.0    */
annotation|@
name|CheckReturnValue
annotation|@
name|Beta
DECL|method|withKeyValueSeparator (String separator)
specifier|public
name|MapSplitter
name|withKeyValueSeparator
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
return|return
name|withKeyValueSeparator
argument_list|(
name|on
argument_list|(
name|separator
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code MapSplitter} which splits entries based on this splitter,    * and splits entries into keys and values using the specified separator.    *    * @since 14.0    */
annotation|@
name|CheckReturnValue
annotation|@
name|Beta
DECL|method|withKeyValueSeparator (char separator)
specifier|public
name|MapSplitter
name|withKeyValueSeparator
parameter_list|(
name|char
name|separator
parameter_list|)
block|{
return|return
name|withKeyValueSeparator
argument_list|(
name|on
argument_list|(
name|separator
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a {@code MapSplitter} which splits entries based on this splitter,    * and splits entries into keys and values using the specified key-value    * splitter.    *    * @since 10.0    */
annotation|@
name|CheckReturnValue
annotation|@
name|Beta
DECL|method|withKeyValueSeparator (Splitter keyValueSplitter)
specifier|public
name|MapSplitter
name|withKeyValueSeparator
parameter_list|(
name|Splitter
name|keyValueSplitter
parameter_list|)
block|{
return|return
operator|new
name|MapSplitter
argument_list|(
name|this
argument_list|,
name|keyValueSplitter
argument_list|)
return|;
block|}
comment|/**    * An object that splits strings into maps as {@code Splitter} splits    * iterables and lists. Like {@code Splitter}, it is thread-safe and    * immutable.    *    * @since 10.0    */
annotation|@
name|Beta
DECL|class|MapSplitter
specifier|public
specifier|static
specifier|final
class|class
name|MapSplitter
block|{
DECL|field|INVALID_ENTRY_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|INVALID_ENTRY_MESSAGE
init|=
literal|"Chunk [%s] is not a valid entry"
decl_stmt|;
DECL|field|outerSplitter
specifier|private
specifier|final
name|Splitter
name|outerSplitter
decl_stmt|;
DECL|field|entrySplitter
specifier|private
specifier|final
name|Splitter
name|entrySplitter
decl_stmt|;
DECL|method|MapSplitter (Splitter outerSplitter, Splitter entrySplitter)
specifier|private
name|MapSplitter
parameter_list|(
name|Splitter
name|outerSplitter
parameter_list|,
name|Splitter
name|entrySplitter
parameter_list|)
block|{
name|this
operator|.
name|outerSplitter
operator|=
name|outerSplitter
expr_stmt|;
comment|// only "this" is passed
name|this
operator|.
name|entrySplitter
operator|=
name|checkNotNull
argument_list|(
name|entrySplitter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Splits {@code sequence} into substrings, splits each substring into      * an entry, and returns an unmodifiable map with each of the entries. For      * example,<code>      * Splitter.on(';').trimResults().withKeyValueSeparator("=>")      * .split("a=>b ; c=>b")      *</code> will return a mapping from {@code "a"} to {@code "b"} and      * {@code "c"} to {@code b}.      *      *<p>The returned map preserves the order of the entries from      * {@code sequence}.      *      * @throws IllegalArgumentException if the specified sequence does not split      *         into valid map entries, or if there are duplicate keys      */
DECL|method|split (CharSequence sequence)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|split
parameter_list|(
name|CharSequence
name|sequence
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|entry
range|:
name|outerSplitter
operator|.
name|split
argument_list|(
name|sequence
argument_list|)
control|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|entryFields
init|=
name|entrySplitter
operator|.
name|spliterator
argument_list|(
name|entry
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|entryFields
operator|.
name|hasNext
argument_list|()
argument_list|,
name|INVALID_ENTRY_MESSAGE
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|entryFields
operator|.
name|next
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
argument_list|,
literal|"Duplicate key [%s] found."
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|entryFields
operator|.
name|hasNext
argument_list|()
argument_list|,
name|INVALID_ENTRY_MESSAGE
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|entryFields
operator|.
name|next
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|entryFields
operator|.
name|hasNext
argument_list|()
argument_list|,
name|INVALID_ENTRY_MESSAGE
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
DECL|interface|Strategy
specifier|private
interface|interface
name|Strategy
block|{
DECL|method|iterator (Splitter splitter, CharSequence toSplit)
name|Iterator
argument_list|<
name|String
argument_list|>
name|iterator
parameter_list|(
name|Splitter
name|splitter
parameter_list|,
name|CharSequence
name|toSplit
parameter_list|)
function_decl|;
block|}
DECL|class|SplittingIterator
specifier|private
specifier|abstract
specifier|static
class|class
name|SplittingIterator
extends|extends
name|AbstractIterator
argument_list|<
name|String
argument_list|>
block|{
DECL|field|toSplit
specifier|final
name|CharSequence
name|toSplit
decl_stmt|;
DECL|field|trimmer
specifier|final
name|CharMatcher
name|trimmer
decl_stmt|;
DECL|field|omitEmptyStrings
specifier|final
name|boolean
name|omitEmptyStrings
decl_stmt|;
comment|/**      * Returns the first index in {@code toSplit} at or after {@code start}      * that contains the separator.      */
DECL|method|separatorStart (int start)
specifier|abstract
name|int
name|separatorStart
parameter_list|(
name|int
name|start
parameter_list|)
function_decl|;
comment|/**      * Returns the first index in {@code toSplit} after {@code      * separatorPosition} that does not contain a separator. This method is only      * invoked after a call to {@code separatorStart}.      */
DECL|method|separatorEnd (int separatorPosition)
specifier|abstract
name|int
name|separatorEnd
parameter_list|(
name|int
name|separatorPosition
parameter_list|)
function_decl|;
DECL|field|offset
name|int
name|offset
init|=
literal|0
decl_stmt|;
DECL|field|limit
name|int
name|limit
decl_stmt|;
DECL|method|SplittingIterator (Splitter splitter, CharSequence toSplit)
specifier|protected
name|SplittingIterator
parameter_list|(
name|Splitter
name|splitter
parameter_list|,
name|CharSequence
name|toSplit
parameter_list|)
block|{
name|this
operator|.
name|trimmer
operator|=
name|splitter
operator|.
name|trimmer
expr_stmt|;
name|this
operator|.
name|omitEmptyStrings
operator|=
name|splitter
operator|.
name|omitEmptyStrings
expr_stmt|;
name|this
operator|.
name|limit
operator|=
name|splitter
operator|.
name|limit
expr_stmt|;
name|this
operator|.
name|toSplit
operator|=
name|toSplit
expr_stmt|;
block|}
DECL|method|computeNext ()
annotation|@
name|Override
specifier|protected
name|String
name|computeNext
parameter_list|()
block|{
comment|/*        * The returned string will be from the end of the last match to the        * beginning of the next one. nextStart is the start position of the        * returned substring, while offset is the place to start looking for a        * separator.        */
name|int
name|nextStart
init|=
name|offset
decl_stmt|;
while|while
condition|(
name|offset
operator|!=
operator|-
literal|1
condition|)
block|{
name|int
name|start
init|=
name|nextStart
decl_stmt|;
name|int
name|end
decl_stmt|;
name|int
name|separatorPosition
init|=
name|separatorStart
argument_list|(
name|offset
argument_list|)
decl_stmt|;
if|if
condition|(
name|separatorPosition
operator|==
operator|-
literal|1
condition|)
block|{
name|end
operator|=
name|toSplit
operator|.
name|length
argument_list|()
expr_stmt|;
name|offset
operator|=
operator|-
literal|1
expr_stmt|;
block|}
else|else
block|{
name|end
operator|=
name|separatorPosition
expr_stmt|;
name|offset
operator|=
name|separatorEnd
argument_list|(
name|separatorPosition
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|offset
operator|==
name|nextStart
condition|)
block|{
comment|/*            * This occurs when some pattern has an empty match, even if it            * doesn't match the empty string -- for example, if it requires            * lookahead or the like. The offset must be increased to look for            * separators beyond this point, without changing the start position            * of the next returned substring -- so nextStart stays the same.            */
name|offset
operator|++
expr_stmt|;
if|if
condition|(
name|offset
operator|>=
name|toSplit
operator|.
name|length
argument_list|()
condition|)
block|{
name|offset
operator|=
operator|-
literal|1
expr_stmt|;
block|}
continue|continue;
block|}
while|while
condition|(
name|start
operator|<
name|end
operator|&&
name|trimmer
operator|.
name|matches
argument_list|(
name|toSplit
operator|.
name|charAt
argument_list|(
name|start
argument_list|)
argument_list|)
condition|)
block|{
name|start
operator|++
expr_stmt|;
block|}
while|while
condition|(
name|end
operator|>
name|start
operator|&&
name|trimmer
operator|.
name|matches
argument_list|(
name|toSplit
operator|.
name|charAt
argument_list|(
name|end
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|end
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|omitEmptyStrings
operator|&&
name|start
operator|==
name|end
condition|)
block|{
comment|// Don't include the (unused) separator in next split string.
name|nextStart
operator|=
name|offset
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|limit
operator|==
literal|1
condition|)
block|{
comment|// The limit has been reached, return the rest of the string as the
comment|// final item.  This is tested after empty string removal so that
comment|// empty strings do not count towards the limit.
name|end
operator|=
name|toSplit
operator|.
name|length
argument_list|()
expr_stmt|;
name|offset
operator|=
operator|-
literal|1
expr_stmt|;
comment|// Since we may have changed the end, we need to trim it again.
while|while
condition|(
name|end
operator|>
name|start
operator|&&
name|trimmer
operator|.
name|matches
argument_list|(
name|toSplit
operator|.
name|charAt
argument_list|(
name|end
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|end
operator|--
expr_stmt|;
block|}
block|}
else|else
block|{
name|limit
operator|--
expr_stmt|;
block|}
return|return
name|toSplit
operator|.
name|subSequence
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|endOfData
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

