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
name|checkState
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
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * An object that divides strings (or other instances of {@code CharSequence})  * into substrings, by recognizing a<i>separator</i> (a.k.a. "delimiter")  * which can be expressed as a single character, literal string, regular  * expression, {@code CharMatcher}, or by using a fixed substring length. This  * class provides the complementary functionality to {@link Joiner}.  *  *<p>Here is the most basic example of {@code Splitter} usage:<pre>   {@code  *  *   Splitter.on(',').split("foo,bar")}</pre>  *  * This invocation returns an {@code Iterable<String>} containing {@code "foo"}  * and {@code "bar"}, in that order.  *  *<p>By default {@code Splitter}'s behavior is very simplistic:<pre>   {@code  *  *   Splitter.on(',').split("foo,,bar, quux")}</pre>  *  * This returns an iterable containing {@code ["foo", "", "bar", " quux"]}.  * Notice that the splitter does not assume that you want empty strings removed,  * or that you wish to trim whitespace. If you want features like these, simply  * ask for them:<pre> {@code  *  *   private static final Splitter MY_SPLITTER = Splitter.on(',')  *       .trimResults()  *       .omitEmptyStrings();}</pre>  *  * Now {@code MY_SPLITTER.split("foo, ,bar, quux,")} returns an iterable  * containing just {@code ["foo", "bar", "quux"]}. Note that the order in which  * the configuration methods are called is never significant; for instance,  * trimming is always applied first before checking for an empty result,  * regardless of the order in which the {@link #trimResults()} and  * {@link #omitEmptyStrings()} methods were invoked.  *  *<p><b>Warning: splitter instances are always immutable</b>; a configuration  * method such as {@code omitEmptyStrings} has no effect on the instance it  * is invoked on! You must store and use the new splitter instance returned by  * the method. This makes splitters thread-safe, and safe to store as {@code  * static final} constants (as illustrated above).<pre>   {@code  *  *   // Bad! Do not do this!  *   Splitter splitter = Splitter.on('/');  *   splitter.trimResults(); // does nothing!  *   return splitter.split("wrong / wrong / wrong");}</pre>  *  * The separator recognized by the splitter does not have to be a single  * literal character as in the examples above. See the methods {@link  * #on(String)}, {@link #on(Pattern)} and {@link #on(CharMatcher)} for examples  * of other ways to specify separators.  *  *<p><b>Note:</b> this class does not mimic any of the quirky behaviors of  * similar JDK methods; for instance, it does not silently discard trailing  * separators, as does {@link String#split(String)}, nor does it have a default  * behavior of using five particular whitespace characters as separators, like  * {@link java.util.StringTokenizer}.  *  * @author Julien Silland  * @author Jesse Wilson  * @author Kevin Bourrillion  * @since 1  */
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
comment|/**    * Returns a splitter that uses the given fixed string as a separator. For    * example, {@code Splitter.on(", ").split("foo, bar, baz,qux")} returns an    * iterable containing {@code ["foo", "bar", "baz,qux"]}.    *    * @param separator the literal, nonempty string to recognize as a separator    * @return a splitter, with default settings, that recognizes that separator    */
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
comment|/**    * Returns a splitter that considers any subsequence matching {@code    * pattern} to be a separator. For example, {@code    * Splitter.on(Pattern.compile("\r?\n")).split(entireFile)} splits a string    * into lines whether it uses DOS-style or UNIX-style line terminators.    *    * @param separatorPattern the pattern that determines whether a subsequence    *     is a separator. This pattern may not match the empty string.    * @return a splitter, with default settings, that uses this pattern    * @throws IllegalArgumentException if {@code separatorPattern} matches the    *     empty string    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.regex"
argument_list|)
DECL|method|on (final Pattern separatorPattern)
specifier|public
specifier|static
name|Splitter
name|on
parameter_list|(
specifier|final
name|Pattern
name|separatorPattern
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|separatorPattern
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|separatorPattern
operator|.
name|matcher
argument_list|(
literal|""
argument_list|)
operator|.
name|matches
argument_list|()
argument_list|,
literal|"The pattern may not match the empty string: %s"
argument_list|,
name|separatorPattern
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
specifier|final
name|Matcher
name|matcher
init|=
name|separatorPattern
operator|.
name|matcher
argument_list|(
name|toSplit
argument_list|)
decl_stmt|;
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
return|return
name|matcher
operator|.
name|find
argument_list|(
name|start
argument_list|)
condition|?
name|matcher
operator|.
name|start
argument_list|()
else|:
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
name|matcher
operator|.
name|end
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
comment|/**    * Returns a splitter that considers any subsequence matching a given    * pattern (regular expression) to be a separator. For example, {@code    * Splitter.onPattern("\r?\n").split(entireFile)} splits a string into lines    * whether it uses DOS-style or UNIX-style line terminators. This is    * equivalent to {@code Splitter.on(Pattern.compile(pattern))}.    *    * @param separatorPattern the pattern that determines whether a subsequence    *     is a separator. This pattern may not match the empty string.    * @return a splitter, with default settings, that uses this pattern    * @throws java.util.regex.PatternSyntaxException if {@code separatorPattern}    *     is a malformed expression    * @throws IllegalArgumentException if {@code separatorPattern} matches the    *     empty string    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"java.util.regex"
argument_list|)
DECL|method|onPattern (String separatorPattern)
specifier|public
specifier|static
name|Splitter
name|onPattern
parameter_list|(
name|String
name|separatorPattern
parameter_list|)
block|{
return|return
name|on
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|separatorPattern
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns a splitter that divides strings into pieces of the given length.    * For example, {@code Splitter.fixedLength(2).split("abcde")} returns an    * iterable containing {@code ["ab", "cd", "e"]}. The last piece can be    * smaller than {@code length} but will never be empty.    *    * @param length the desired length of pieces after splitting    * @return a splitter, with default settings, that can split into fixed sized    *     pieces    */
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
comment|/**    * Returns a splitter that behaves equivalently to {@code this} splitter, but    * automatically removes leading and trailing {@linkplain    * CharMatcher#WHITESPACE whitespace} from each returned substring; equivalent    * to {@code trimResults(CharMatcher.WHITESPACE)}. For example, {@code    * Splitter.on(',').trimResults().split(" a, b ,c ")} returns an iterable    * containing {@code ["a", "b", "c"]}.    *    * @return a splitter with the desired configuration    */
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
comment|/**    * Splits {@code sequence} into string components and makes them available    * through an {@link Iterator}, which may be lazily evaluated.    *    * @param sequence the sequence of characters to split    * @return an iteration over the segments split from the parameter.    */
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
name|strategy
operator|.
name|iterator
argument_list|(
name|Splitter
operator|.
name|this
argument_list|,
name|sequence
argument_list|)
return|;
block|}
block|}
return|;
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
name|offset
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
comment|/*    * Copied from common.collect.AbstractIterator. TODO(kevinb): un-fork if these    * packages are ever combined into a single library.    */
DECL|class|AbstractIterator
specifier|private
specifier|abstract
specifier|static
class|class
name|AbstractIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|state
name|State
name|state
init|=
name|State
operator|.
name|NOT_READY
decl_stmt|;
DECL|enum|State
enum|enum
name|State
block|{
DECL|enumConstant|READY
DECL|enumConstant|NOT_READY
DECL|enumConstant|DONE
DECL|enumConstant|FAILED
name|READY
block|,
name|NOT_READY
block|,
name|DONE
block|,
name|FAILED
block|,     }
DECL|field|next
name|T
name|next
decl_stmt|;
DECL|method|computeNext ()
specifier|protected
specifier|abstract
name|T
name|computeNext
parameter_list|()
function_decl|;
DECL|method|endOfData ()
specifier|protected
specifier|final
name|T
name|endOfData
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|DONE
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
name|checkState
argument_list|(
name|state
operator|!=
name|State
operator|.
name|FAILED
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|DONE
case|:
return|return
literal|false
return|;
case|case
name|READY
case|:
return|return
literal|true
return|;
default|default:
block|}
return|return
name|tryToComputeNext
argument_list|()
return|;
block|}
DECL|method|tryToComputeNext ()
name|boolean
name|tryToComputeNext
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|FAILED
expr_stmt|;
comment|// temporary pessimism
name|next
operator|=
name|computeNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|DONE
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|READY
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
specifier|final
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|state
operator|=
name|State
operator|.
name|NOT_READY
expr_stmt|;
return|return
name|next
return|;
block|}
DECL|method|remove ()
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

