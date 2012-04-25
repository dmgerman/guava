begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2012 Google Inc. All Rights Reserved.
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

begin_comment
comment|/**  * An immutable small version of CharMatcher that uses an efficient hash table implementation, with  * non-power-of-2 sizing to try to use no reprobing, if possible.  *  * @author Christopher Swenson  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|SmallCharMatcher
specifier|final
class|class
name|SmallCharMatcher
extends|extends
name|CharMatcher
block|{
DECL|field|MAX_SIZE
specifier|static
specifier|final
name|int
name|MAX_SIZE
init|=
literal|63
decl_stmt|;
DECL|field|MAX_TABLE_SIZE
specifier|static
specifier|final
name|int
name|MAX_TABLE_SIZE
init|=
literal|128
decl_stmt|;
DECL|field|reprobe
specifier|private
specifier|final
name|boolean
name|reprobe
decl_stmt|;
DECL|field|table
specifier|private
specifier|final
name|char
index|[]
name|table
decl_stmt|;
DECL|field|containsZero
specifier|private
specifier|final
name|boolean
name|containsZero
decl_stmt|;
DECL|field|filter
specifier|final
name|long
name|filter
decl_stmt|;
DECL|method|SmallCharMatcher (char[] table, long filter, boolean containsZero, boolean reprobe)
specifier|private
name|SmallCharMatcher
parameter_list|(
name|char
index|[]
name|table
parameter_list|,
name|long
name|filter
parameter_list|,
name|boolean
name|containsZero
parameter_list|,
name|boolean
name|reprobe
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
name|this
operator|.
name|containsZero
operator|=
name|containsZero
expr_stmt|;
name|this
operator|.
name|reprobe
operator|=
name|reprobe
expr_stmt|;
block|}
DECL|method|checkFilter (int c)
specifier|private
name|boolean
name|checkFilter
parameter_list|(
name|int
name|c
parameter_list|)
block|{
return|return
literal|1
operator|==
operator|(
literal|1
operator|&
operator|(
name|filter
operator|>>
name|c
operator|)
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|precomputed ()
specifier|public
name|CharMatcher
name|precomputed
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|buildTable (int modulus, char[] allChars, boolean reprobe)
specifier|static
name|char
index|[]
name|buildTable
parameter_list|(
name|int
name|modulus
parameter_list|,
name|char
index|[]
name|allChars
parameter_list|,
name|boolean
name|reprobe
parameter_list|)
block|{
name|char
index|[]
name|table
init|=
operator|new
name|char
index|[
name|modulus
index|]
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
name|allChars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|allChars
index|[
name|i
index|]
decl_stmt|;
name|int
name|index
init|=
name|c
operator|%
name|modulus
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
name|index
operator|+=
name|modulus
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|table
index|[
name|index
index|]
operator|!=
literal|0
operator|)
operator|&&
operator|!
name|reprobe
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|reprobe
condition|)
block|{
while|while
condition|(
name|table
index|[
name|index
index|]
operator|!=
literal|0
condition|)
block|{
name|index
operator|=
operator|(
name|index
operator|+
literal|1
operator|)
operator|%
name|modulus
expr_stmt|;
block|}
block|}
name|table
index|[
name|index
index|]
operator|=
name|c
expr_stmt|;
block|}
return|return
name|table
return|;
block|}
DECL|method|from (char[] chars)
specifier|static
name|CharMatcher
name|from
parameter_list|(
name|char
index|[]
name|chars
parameter_list|)
block|{
name|long
name|filter
init|=
literal|0
decl_stmt|;
name|int
name|size
init|=
name|chars
operator|.
name|length
decl_stmt|;
name|boolean
name|containsZero
init|=
literal|false
decl_stmt|;
name|boolean
name|reprobe
init|=
literal|false
decl_stmt|;
name|containsZero
operator|=
name|chars
index|[
literal|0
index|]
operator|==
literal|0
expr_stmt|;
comment|// Compute the filter.
for|for
control|(
name|char
name|c
range|:
name|chars
control|)
block|{
name|filter
operator||=
literal|1L
operator|<<
name|c
expr_stmt|;
block|}
name|char
index|[]
name|table
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|size
init|;
name|i
operator|<
name|MAX_TABLE_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|table
operator|=
name|buildTable
argument_list|(
name|i
argument_list|,
name|chars
argument_list|,
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
comment|// Compute the hash table.
if|if
condition|(
name|table
operator|==
literal|null
condition|)
block|{
name|table
operator|=
name|buildTable
argument_list|(
name|MAX_TABLE_SIZE
argument_list|,
name|chars
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|reprobe
operator|=
literal|true
expr_stmt|;
block|}
return|return
operator|new
name|SmallCharMatcher
argument_list|(
name|table
argument_list|,
name|filter
argument_list|,
name|containsZero
argument_list|,
name|reprobe
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|matches (char c)
specifier|public
name|boolean
name|matches
parameter_list|(
name|char
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|0
condition|)
block|{
return|return
name|containsZero
return|;
block|}
if|if
condition|(
operator|!
name|checkFilter
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|index
init|=
name|c
operator|%
name|table
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|0
condition|)
block|{
name|index
operator|+=
name|table
operator|.
name|length
expr_stmt|;
block|}
while|while
condition|(
literal|true
condition|)
block|{
comment|// Check for empty.
if|if
condition|(
name|table
index|[
name|index
index|]
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|table
index|[
name|index
index|]
operator|==
name|c
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|reprobe
condition|)
block|{
comment|// Linear probing will terminate eventually.
name|index
operator|=
operator|(
name|index
operator|+
literal|1
operator|)
operator|%
name|table
operator|.
name|length
expr_stmt|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

