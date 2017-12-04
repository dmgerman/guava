begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.cache
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|cache
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
name|common
operator|.
name|base
operator|.
name|MoreObjects
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
name|Objects
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
name|Splitter
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
name|cache
operator|.
name|LocalCache
operator|.
name|Strength
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
name|ImmutableList
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
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
comment|/**  * A specification of a {@link CacheBuilder} configuration.  *  *<p>{@code CacheBuilderSpec} supports parsing configuration off of a string, which makes it  * especially useful for command-line configuration of a {@code CacheBuilder}.  *  *<p>The string syntax is a series of comma-separated keys or key-value pairs, each corresponding  * to a {@code CacheBuilder} method.  *  *<ul>  *<li>{@code concurrencyLevel=[integer]}: sets {@link CacheBuilder#concurrencyLevel}.  *<li>{@code initialCapacity=[integer]}: sets {@link CacheBuilder#initialCapacity}.  *<li>{@code maximumSize=[long]}: sets {@link CacheBuilder#maximumSize}.  *<li>{@code maximumWeight=[long]}: sets {@link CacheBuilder#maximumWeight}.  *<li>{@code expireAfterAccess=[duration]}: sets {@link CacheBuilder#expireAfterAccess}.  *<li>{@code expireAfterWrite=[duration]}: sets {@link CacheBuilder#expireAfterWrite}.  *<li>{@code refreshAfterWrite=[duration]}: sets {@link CacheBuilder#refreshAfterWrite}.  *<li>{@code weakKeys}: sets {@link CacheBuilder#weakKeys}.  *<li>{@code softValues}: sets {@link CacheBuilder#softValues}.  *<li>{@code weakValues}: sets {@link CacheBuilder#weakValues}.  *<li>{@code recordStats}: sets {@link CacheBuilder#recordStats}.  *</ul>  *  *<p>The set of supported keys will grow as {@code CacheBuilder} evolves, but existing keys will  * never be removed.  *  *<p>Durations are represented by an integer, followed by one of "d", "h", "m", or "s",  * representing days, hours, minutes, or seconds respectively. (There is currently no syntax to  * request expiration in milliseconds, microseconds, or nanoseconds.)  *  *<p>Whitespace before and after commas and equal signs is ignored. Keys may not be repeated; it is  * also illegal to use the following pairs of keys in a single value:  *  *<ul>  *<li>{@code maximumSize} and {@code maximumWeight}  *<li>{@code softValues} and {@code weakValues}  *</ul>  *  *<p>{@code CacheBuilderSpec} does not support configuring {@code CacheBuilder} methods with  * non-value parameters. These must be configured in code.  *  *<p>A new {@code CacheBuilder} can be instantiated from a {@code CacheBuilderSpec} using {@link  * CacheBuilder#from(CacheBuilderSpec)} or {@link CacheBuilder#from(String)}.  *  * @author Adam Winer  * @since 12.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|CacheBuilderSpec
specifier|public
specifier|final
class|class
name|CacheBuilderSpec
block|{
comment|/** Parses a single value. */
DECL|interface|ValueParser
specifier|private
interface|interface
name|ValueParser
block|{
DECL|method|parse (CacheBuilderSpec spec, String key, @Nullable String value)
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
function_decl|;
block|}
comment|/** Splits each key-value pair. */
DECL|field|KEYS_SPLITTER
specifier|private
specifier|static
specifier|final
name|Splitter
name|KEYS_SPLITTER
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|trimResults
argument_list|()
decl_stmt|;
comment|/** Splits the key from the value. */
DECL|field|KEY_VALUE_SPLITTER
specifier|private
specifier|static
specifier|final
name|Splitter
name|KEY_VALUE_SPLITTER
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'='
argument_list|)
operator|.
name|trimResults
argument_list|()
decl_stmt|;
comment|/** Map of names to ValueParser. */
DECL|field|VALUE_PARSERS
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|ValueParser
argument_list|>
name|VALUE_PARSERS
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|ValueParser
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"initialCapacity"
argument_list|,
operator|new
name|InitialCapacityParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"maximumSize"
argument_list|,
operator|new
name|MaximumSizeParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"maximumWeight"
argument_list|,
operator|new
name|MaximumWeightParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"concurrencyLevel"
argument_list|,
operator|new
name|ConcurrencyLevelParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"weakKeys"
argument_list|,
operator|new
name|KeyStrengthParser
argument_list|(
name|Strength
operator|.
name|WEAK
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"softValues"
argument_list|,
operator|new
name|ValueStrengthParser
argument_list|(
name|Strength
operator|.
name|SOFT
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"weakValues"
argument_list|,
operator|new
name|ValueStrengthParser
argument_list|(
name|Strength
operator|.
name|WEAK
argument_list|)
argument_list|)
decl|.
name|put
argument_list|(
literal|"recordStats"
argument_list|,
operator|new
name|RecordStatsParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"expireAfterAccess"
argument_list|,
operator|new
name|AccessDurationParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"expireAfterWrite"
argument_list|,
operator|new
name|WriteDurationParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"refreshAfterWrite"
argument_list|,
operator|new
name|RefreshDurationParser
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"refreshInterval"
argument_list|,
operator|new
name|RefreshDurationParser
argument_list|()
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|field|initialCapacity
annotation|@
name|VisibleForTesting
name|Integer
name|initialCapacity
decl_stmt|;
DECL|field|maximumSize
annotation|@
name|VisibleForTesting
name|Long
name|maximumSize
decl_stmt|;
DECL|field|maximumWeight
annotation|@
name|VisibleForTesting
name|Long
name|maximumWeight
decl_stmt|;
DECL|field|concurrencyLevel
annotation|@
name|VisibleForTesting
name|Integer
name|concurrencyLevel
decl_stmt|;
DECL|field|keyStrength
annotation|@
name|VisibleForTesting
name|Strength
name|keyStrength
decl_stmt|;
DECL|field|valueStrength
annotation|@
name|VisibleForTesting
name|Strength
name|valueStrength
decl_stmt|;
DECL|field|recordStats
annotation|@
name|VisibleForTesting
name|Boolean
name|recordStats
decl_stmt|;
DECL|field|writeExpirationDuration
annotation|@
name|VisibleForTesting
name|long
name|writeExpirationDuration
decl_stmt|;
DECL|field|writeExpirationTimeUnit
annotation|@
name|VisibleForTesting
name|TimeUnit
name|writeExpirationTimeUnit
decl_stmt|;
DECL|field|accessExpirationDuration
annotation|@
name|VisibleForTesting
name|long
name|accessExpirationDuration
decl_stmt|;
DECL|field|accessExpirationTimeUnit
annotation|@
name|VisibleForTesting
name|TimeUnit
name|accessExpirationTimeUnit
decl_stmt|;
DECL|field|refreshDuration
annotation|@
name|VisibleForTesting
name|long
name|refreshDuration
decl_stmt|;
DECL|field|refreshTimeUnit
annotation|@
name|VisibleForTesting
name|TimeUnit
name|refreshTimeUnit
decl_stmt|;
comment|/** Specification; used for toParseableString(). */
DECL|field|specification
specifier|private
specifier|final
name|String
name|specification
decl_stmt|;
DECL|method|CacheBuilderSpec (String specification)
specifier|private
name|CacheBuilderSpec
parameter_list|(
name|String
name|specification
parameter_list|)
block|{
name|this
operator|.
name|specification
operator|=
name|specification
expr_stmt|;
block|}
comment|/**    * Creates a CacheBuilderSpec from a string.    *    * @param cacheBuilderSpecification the string form    */
DECL|method|parse (String cacheBuilderSpecification)
specifier|public
specifier|static
name|CacheBuilderSpec
name|parse
parameter_list|(
name|String
name|cacheBuilderSpecification
parameter_list|)
block|{
name|CacheBuilderSpec
name|spec
init|=
operator|new
name|CacheBuilderSpec
argument_list|(
name|cacheBuilderSpecification
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cacheBuilderSpecification
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|keyValuePair
range|:
name|KEYS_SPLITTER
operator|.
name|split
argument_list|(
name|cacheBuilderSpecification
argument_list|)
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keyAndValue
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|KEY_VALUE_SPLITTER
operator|.
name|split
argument_list|(
name|keyValuePair
argument_list|)
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|keyAndValue
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"blank key-value pair"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|keyAndValue
operator|.
name|size
argument_list|()
operator|<=
literal|2
argument_list|,
literal|"key-value pair %s with more than one equals sign"
argument_list|,
name|keyValuePair
argument_list|)
expr_stmt|;
comment|// Find the ValueParser for the current key.
name|String
name|key
init|=
name|keyAndValue
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ValueParser
name|valueParser
init|=
name|VALUE_PARSERS
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|checkArgument
argument_list|(
name|valueParser
operator|!=
literal|null
argument_list|,
literal|"unknown key %s"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|keyAndValue
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|?
literal|null
else|:
name|keyAndValue
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|valueParser
operator|.
name|parse
argument_list|(
name|spec
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|spec
return|;
block|}
comment|/** Returns a CacheBuilderSpec that will prevent caching. */
DECL|method|disableCaching ()
specifier|public
specifier|static
name|CacheBuilderSpec
name|disableCaching
parameter_list|()
block|{
comment|// Maximum size of zero is one way to block caching
return|return
name|CacheBuilderSpec
operator|.
name|parse
argument_list|(
literal|"maximumSize=0"
argument_list|)
return|;
block|}
comment|/** Returns a CacheBuilder configured according to this instance's specification. */
DECL|method|toCacheBuilder ()
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|toCacheBuilder
parameter_list|()
block|{
name|CacheBuilder
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|builder
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|initialCapacity
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|initialCapacity
argument_list|(
name|initialCapacity
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumSize
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|maximumSize
argument_list|(
name|maximumSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maximumWeight
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|maximumWeight
argument_list|(
name|maximumWeight
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|concurrencyLevel
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|concurrencyLevel
argument_list|(
name|concurrencyLevel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|keyStrength
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|keyStrength
condition|)
block|{
case|case
name|WEAK
case|:
name|builder
operator|.
name|weakKeys
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
if|if
condition|(
name|valueStrength
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|valueStrength
condition|)
block|{
case|case
name|SOFT
case|:
name|builder
operator|.
name|softValues
argument_list|()
expr_stmt|;
break|break;
case|case
name|WEAK
case|:
name|builder
operator|.
name|weakValues
argument_list|()
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
if|if
condition|(
name|recordStats
operator|!=
literal|null
operator|&&
name|recordStats
condition|)
block|{
name|builder
operator|.
name|recordStats
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|writeExpirationTimeUnit
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|expireAfterWrite
argument_list|(
name|writeExpirationDuration
argument_list|,
name|writeExpirationTimeUnit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|accessExpirationTimeUnit
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|expireAfterAccess
argument_list|(
name|accessExpirationDuration
argument_list|,
name|accessExpirationTimeUnit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|refreshTimeUnit
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|refreshAfterWrite
argument_list|(
name|refreshDuration
argument_list|,
name|refreshTimeUnit
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
return|;
block|}
comment|/**    * Returns a string that can be used to parse an equivalent {@code CacheBuilderSpec}. The order    * and form of this representation is not guaranteed, except that reparsing its output will    * produce a {@code CacheBuilderSpec} equal to this instance.    */
DECL|method|toParsableString ()
specifier|public
name|String
name|toParsableString
parameter_list|()
block|{
return|return
name|specification
return|;
block|}
comment|/**    * Returns a string representation for this CacheBuilderSpec instance. The form of this    * representation is not guaranteed.    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|addValue
argument_list|(
name|toParsableString
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
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
name|Objects
operator|.
name|hashCode
argument_list|(
name|initialCapacity
argument_list|,
name|maximumSize
argument_list|,
name|maximumWeight
argument_list|,
name|concurrencyLevel
argument_list|,
name|keyStrength
argument_list|,
name|valueStrength
argument_list|,
name|recordStats
argument_list|,
name|durationInNanos
argument_list|(
name|writeExpirationDuration
argument_list|,
name|writeExpirationTimeUnit
argument_list|)
argument_list|,
name|durationInNanos
argument_list|(
name|accessExpirationDuration
argument_list|,
name|accessExpirationTimeUnit
argument_list|)
argument_list|,
name|durationInNanos
argument_list|(
name|refreshDuration
argument_list|,
name|refreshTimeUnit
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|CacheBuilderSpec
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|CacheBuilderSpec
name|that
init|=
operator|(
name|CacheBuilderSpec
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|initialCapacity
argument_list|,
name|that
operator|.
name|initialCapacity
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|maximumSize
argument_list|,
name|that
operator|.
name|maximumSize
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|maximumWeight
argument_list|,
name|that
operator|.
name|maximumWeight
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|concurrencyLevel
argument_list|,
name|that
operator|.
name|concurrencyLevel
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|keyStrength
argument_list|,
name|that
operator|.
name|keyStrength
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|valueStrength
argument_list|,
name|that
operator|.
name|valueStrength
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|recordStats
argument_list|,
name|that
operator|.
name|recordStats
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|durationInNanos
argument_list|(
name|writeExpirationDuration
argument_list|,
name|writeExpirationTimeUnit
argument_list|)
argument_list|,
name|durationInNanos
argument_list|(
name|that
operator|.
name|writeExpirationDuration
argument_list|,
name|that
operator|.
name|writeExpirationTimeUnit
argument_list|)
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|durationInNanos
argument_list|(
name|accessExpirationDuration
argument_list|,
name|accessExpirationTimeUnit
argument_list|)
argument_list|,
name|durationInNanos
argument_list|(
name|that
operator|.
name|accessExpirationDuration
argument_list|,
name|that
operator|.
name|accessExpirationTimeUnit
argument_list|)
argument_list|)
operator|&&
name|Objects
operator|.
name|equal
argument_list|(
name|durationInNanos
argument_list|(
name|refreshDuration
argument_list|,
name|refreshTimeUnit
argument_list|)
argument_list|,
name|durationInNanos
argument_list|(
name|that
operator|.
name|refreshDuration
argument_list|,
name|that
operator|.
name|refreshTimeUnit
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Converts an expiration duration/unit pair into a single Long for hashing and equality. Uses    * nanos to match CacheBuilder implementation.    */
annotation|@
name|Nullable
DECL|method|durationInNanos (long duration, @Nullable TimeUnit unit)
specifier|private
specifier|static
name|Long
name|durationInNanos
parameter_list|(
name|long
name|duration
parameter_list|,
annotation|@
name|Nullable
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|(
name|unit
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|unit
operator|.
name|toNanos
argument_list|(
name|duration
argument_list|)
return|;
block|}
comment|/** Base class for parsing integers. */
DECL|class|IntegerParser
specifier|abstract
specifier|static
class|class
name|IntegerParser
implements|implements
name|ValueParser
block|{
DECL|method|parseInteger (CacheBuilderSpec spec, int value)
specifier|protected
specifier|abstract
name|void
name|parseInteger
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|int
name|value
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"value of key %s omitted"
argument_list|,
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|parseInteger
argument_list|(
name|spec
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|format
argument_list|(
literal|"key %s value set to %s, must be integer"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/** Base class for parsing integers. */
DECL|class|LongParser
specifier|abstract
specifier|static
class|class
name|LongParser
implements|implements
name|ValueParser
block|{
DECL|method|parseLong (CacheBuilderSpec spec, long value)
specifier|protected
specifier|abstract
name|void
name|parseLong
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|value
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"value of key %s omitted"
argument_list|,
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|parseLong
argument_list|(
name|spec
argument_list|,
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|format
argument_list|(
literal|"key %s value set to %s, must be integer"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/** Parse initialCapacity */
DECL|class|InitialCapacityParser
specifier|static
class|class
name|InitialCapacityParser
extends|extends
name|IntegerParser
block|{
annotation|@
name|Override
DECL|method|parseInteger (CacheBuilderSpec spec, int value)
specifier|protected
name|void
name|parseInteger
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|initialCapacity
operator|==
literal|null
argument_list|,
literal|"initial capacity was already set to "
argument_list|,
name|spec
operator|.
name|initialCapacity
argument_list|)
expr_stmt|;
name|spec
operator|.
name|initialCapacity
operator|=
name|value
expr_stmt|;
block|}
block|}
comment|/** Parse maximumSize */
DECL|class|MaximumSizeParser
specifier|static
class|class
name|MaximumSizeParser
extends|extends
name|LongParser
block|{
annotation|@
name|Override
DECL|method|parseLong (CacheBuilderSpec spec, long value)
specifier|protected
name|void
name|parseLong
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|maximumSize
operator|==
literal|null
argument_list|,
literal|"maximum size was already set to "
argument_list|,
name|spec
operator|.
name|maximumSize
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|spec
operator|.
name|maximumWeight
operator|==
literal|null
argument_list|,
literal|"maximum weight was already set to "
argument_list|,
name|spec
operator|.
name|maximumWeight
argument_list|)
expr_stmt|;
name|spec
operator|.
name|maximumSize
operator|=
name|value
expr_stmt|;
block|}
block|}
comment|/** Parse maximumWeight */
DECL|class|MaximumWeightParser
specifier|static
class|class
name|MaximumWeightParser
extends|extends
name|LongParser
block|{
annotation|@
name|Override
DECL|method|parseLong (CacheBuilderSpec spec, long value)
specifier|protected
name|void
name|parseLong
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|maximumWeight
operator|==
literal|null
argument_list|,
literal|"maximum weight was already set to "
argument_list|,
name|spec
operator|.
name|maximumWeight
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|spec
operator|.
name|maximumSize
operator|==
literal|null
argument_list|,
literal|"maximum size was already set to "
argument_list|,
name|spec
operator|.
name|maximumSize
argument_list|)
expr_stmt|;
name|spec
operator|.
name|maximumWeight
operator|=
name|value
expr_stmt|;
block|}
block|}
comment|/** Parse concurrencyLevel */
DECL|class|ConcurrencyLevelParser
specifier|static
class|class
name|ConcurrencyLevelParser
extends|extends
name|IntegerParser
block|{
annotation|@
name|Override
DECL|method|parseInteger (CacheBuilderSpec spec, int value)
specifier|protected
name|void
name|parseInteger
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|concurrencyLevel
operator|==
literal|null
argument_list|,
literal|"concurrency level was already set to "
argument_list|,
name|spec
operator|.
name|concurrencyLevel
argument_list|)
expr_stmt|;
name|spec
operator|.
name|concurrencyLevel
operator|=
name|value
expr_stmt|;
block|}
block|}
comment|/** Parse weakKeys */
DECL|class|KeyStrengthParser
specifier|static
class|class
name|KeyStrengthParser
implements|implements
name|ValueParser
block|{
DECL|field|strength
specifier|private
specifier|final
name|Strength
name|strength
decl_stmt|;
DECL|method|KeyStrengthParser (Strength strength)
specifier|public
name|KeyStrengthParser
parameter_list|(
name|Strength
name|strength
parameter_list|)
block|{
name|this
operator|.
name|strength
operator|=
name|strength
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, @Nullable String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|==
literal|null
argument_list|,
literal|"key %s does not take values"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|spec
operator|.
name|keyStrength
operator|==
literal|null
argument_list|,
literal|"%s was already set to %s"
argument_list|,
name|key
argument_list|,
name|spec
operator|.
name|keyStrength
argument_list|)
expr_stmt|;
name|spec
operator|.
name|keyStrength
operator|=
name|strength
expr_stmt|;
block|}
block|}
comment|/** Parse weakValues and softValues */
DECL|class|ValueStrengthParser
specifier|static
class|class
name|ValueStrengthParser
implements|implements
name|ValueParser
block|{
DECL|field|strength
specifier|private
specifier|final
name|Strength
name|strength
decl_stmt|;
DECL|method|ValueStrengthParser (Strength strength)
specifier|public
name|ValueStrengthParser
parameter_list|(
name|Strength
name|strength
parameter_list|)
block|{
name|this
operator|.
name|strength
operator|=
name|strength
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, @Nullable String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|==
literal|null
argument_list|,
literal|"key %s does not take values"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|spec
operator|.
name|valueStrength
operator|==
literal|null
argument_list|,
literal|"%s was already set to %s"
argument_list|,
name|key
argument_list|,
name|spec
operator|.
name|valueStrength
argument_list|)
expr_stmt|;
name|spec
operator|.
name|valueStrength
operator|=
name|strength
expr_stmt|;
block|}
block|}
comment|/** Parse recordStats */
DECL|class|RecordStatsParser
specifier|static
class|class
name|RecordStatsParser
implements|implements
name|ValueParser
block|{
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, @Nullable String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|==
literal|null
argument_list|,
literal|"recordStats does not take values"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|spec
operator|.
name|recordStats
operator|==
literal|null
argument_list|,
literal|"recordStats already set"
argument_list|)
expr_stmt|;
name|spec
operator|.
name|recordStats
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/** Base class for parsing times with durations */
DECL|class|DurationParser
specifier|abstract
specifier|static
class|class
name|DurationParser
implements|implements
name|ValueParser
block|{
DECL|method|parseDuration (CacheBuilderSpec spec, long duration, TimeUnit unit)
specifier|protected
specifier|abstract
name|void
name|parseDuration
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|parse (CacheBuilderSpec spec, String key, String value)
specifier|public
name|void
name|parse
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"value of key %s omitted"
argument_list|,
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|char
name|lastChar
init|=
name|value
operator|.
name|charAt
argument_list|(
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|TimeUnit
name|timeUnit
decl_stmt|;
switch|switch
condition|(
name|lastChar
condition|)
block|{
case|case
literal|'d'
case|:
name|timeUnit
operator|=
name|TimeUnit
operator|.
name|DAYS
expr_stmt|;
break|break;
case|case
literal|'h'
case|:
name|timeUnit
operator|=
name|TimeUnit
operator|.
name|HOURS
expr_stmt|;
break|break;
case|case
literal|'m'
case|:
name|timeUnit
operator|=
name|TimeUnit
operator|.
name|MINUTES
expr_stmt|;
break|break;
case|case
literal|'s'
case|:
name|timeUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|format
argument_list|(
literal|"key %s invalid format.  was %s, must end with one of [dDhHmMsS]"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
throw|;
block|}
name|long
name|duration
init|=
name|Long
operator|.
name|parseLong
argument_list|(
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|parseDuration
argument_list|(
name|spec
argument_list|,
name|duration
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|format
argument_list|(
literal|"key %s value set to %s, must be integer"
argument_list|,
name|key
argument_list|,
name|value
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
comment|/** Parse expireAfterAccess */
DECL|class|AccessDurationParser
specifier|static
class|class
name|AccessDurationParser
extends|extends
name|DurationParser
block|{
annotation|@
name|Override
DECL|method|parseDuration (CacheBuilderSpec spec, long duration, TimeUnit unit)
specifier|protected
name|void
name|parseDuration
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|accessExpirationTimeUnit
operator|==
literal|null
argument_list|,
literal|"expireAfterAccess already set"
argument_list|)
expr_stmt|;
name|spec
operator|.
name|accessExpirationDuration
operator|=
name|duration
expr_stmt|;
name|spec
operator|.
name|accessExpirationTimeUnit
operator|=
name|unit
expr_stmt|;
block|}
block|}
comment|/** Parse expireAfterWrite */
DECL|class|WriteDurationParser
specifier|static
class|class
name|WriteDurationParser
extends|extends
name|DurationParser
block|{
annotation|@
name|Override
DECL|method|parseDuration (CacheBuilderSpec spec, long duration, TimeUnit unit)
specifier|protected
name|void
name|parseDuration
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|writeExpirationTimeUnit
operator|==
literal|null
argument_list|,
literal|"expireAfterWrite already set"
argument_list|)
expr_stmt|;
name|spec
operator|.
name|writeExpirationDuration
operator|=
name|duration
expr_stmt|;
name|spec
operator|.
name|writeExpirationTimeUnit
operator|=
name|unit
expr_stmt|;
block|}
block|}
comment|/** Parse refreshAfterWrite */
DECL|class|RefreshDurationParser
specifier|static
class|class
name|RefreshDurationParser
extends|extends
name|DurationParser
block|{
annotation|@
name|Override
DECL|method|parseDuration (CacheBuilderSpec spec, long duration, TimeUnit unit)
specifier|protected
name|void
name|parseDuration
parameter_list|(
name|CacheBuilderSpec
name|spec
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|spec
operator|.
name|refreshTimeUnit
operator|==
literal|null
argument_list|,
literal|"refreshAfterWrite already set"
argument_list|)
expr_stmt|;
name|spec
operator|.
name|refreshDuration
operator|=
name|duration
expr_stmt|;
name|spec
operator|.
name|refreshTimeUnit
operator|=
name|unit
expr_stmt|;
block|}
block|}
DECL|method|format (String format, Object... args)
specifier|private
specifier|static
name|String
name|format
parameter_list|(
name|String
name|format
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|,
name|format
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
end_class

end_unit

