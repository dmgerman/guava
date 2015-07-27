begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is a modified version of  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/TimeUnit.java  * which contained the following notice:  *  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
end_comment

begin_package
DECL|package|java.util.concurrent
package|package
name|java
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_comment
comment|/**  * GWT emulation of TimeUnit, created by removing unsupported operations from  * Doug Lea's public domain version.  */
end_comment

begin_enum
DECL|enum|TimeUnit
specifier|public
enum|enum
name|TimeUnit
block|{
DECL|enumConstant|NANOSECONDS
name|NANOSECONDS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C1_C0
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C2_C0
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C3_C0
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C4_C0
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C5_C0
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C0
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toNanos
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|d
operator|-
operator|(
name|m
operator|*
name|C2
operator|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|MICROSECONDS
name|MICROSECONDS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C1_C0
argument_list|,
name|MAX_C1_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C2_C1
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C3_C1
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C4_C1
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C5_C1
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C1
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toMicros
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
operator|(
name|d
operator|*
name|C1
operator|)
operator|-
operator|(
name|m
operator|*
name|C2
operator|)
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|MILLISECONDS
name|MILLISECONDS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C2_C0
argument_list|,
name|MAX_C2_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C2_C1
argument_list|,
name|MAX_C2_C1
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C3_C2
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C4_C2
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C5_C2
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C2
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toMillis
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|,
DECL|enumConstant|SECONDS
name|SECONDS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C3_C0
argument_list|,
name|MAX_C3_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C3_C1
argument_list|,
name|MAX_C3_C1
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C3_C2
argument_list|,
name|MAX_C3_C2
argument_list|)
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C4_C3
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C5_C3
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C3
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toSeconds
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|,
DECL|enumConstant|MINUTES
name|MINUTES
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C4_C0
argument_list|,
name|MAX_C4_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C4_C1
argument_list|,
name|MAX_C4_C1
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C4_C2
argument_list|,
name|MAX_C4_C2
argument_list|)
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C4_C3
argument_list|,
name|MAX_C4_C3
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C5_C4
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C4
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toMinutes
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|,
DECL|enumConstant|HOURS
name|HOURS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C5_C0
argument_list|,
name|MAX_C5_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C5_C1
argument_list|,
name|MAX_C5_C1
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C5_C2
argument_list|,
name|MAX_C5_C2
argument_list|)
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C5_C3
argument_list|,
name|MAX_C5_C3
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C5_C4
argument_list|,
name|MAX_C5_C4
argument_list|)
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
operator|/
name|C6_C5
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toHours
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|,
DECL|enumConstant|DAYS
name|DAYS
block|{
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C0
argument_list|,
name|MAX_C6_C0
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C1
argument_list|,
name|MAX_C6_C1
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C2
argument_list|,
name|MAX_C6_C2
argument_list|)
return|;
block|}
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C3
argument_list|,
name|MAX_C6_C3
argument_list|)
return|;
block|}
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C4
argument_list|,
name|MAX_C6_C4
argument_list|)
return|;
block|}
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|x
argument_list|(
name|d
argument_list|,
name|C6_C5
argument_list|,
name|MAX_C6_C5
argument_list|)
return|;
block|}
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|d
parameter_list|)
block|{
return|return
name|d
return|;
block|}
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|d
parameter_list|,
name|TimeUnit
name|u
parameter_list|)
block|{
return|return
name|u
operator|.
name|toDays
argument_list|(
name|d
argument_list|)
return|;
block|}
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
block|{
return|return
literal|0
return|;
block|}
block|}
block|;
comment|// Handy constants for conversion methods
DECL|field|C0
specifier|static
specifier|final
name|long
name|C0
init|=
literal|1L
decl_stmt|;
DECL|field|C1
specifier|static
specifier|final
name|long
name|C1
init|=
name|C0
operator|*
literal|1000L
decl_stmt|;
DECL|field|C2
specifier|static
specifier|final
name|long
name|C2
init|=
name|C1
operator|*
literal|1000L
decl_stmt|;
DECL|field|C3
specifier|static
specifier|final
name|long
name|C3
init|=
name|C2
operator|*
literal|1000L
decl_stmt|;
DECL|field|C4
specifier|static
specifier|final
name|long
name|C4
init|=
name|C3
operator|*
literal|60L
decl_stmt|;
DECL|field|C5
specifier|static
specifier|final
name|long
name|C5
init|=
name|C4
operator|*
literal|60L
decl_stmt|;
DECL|field|C6
specifier|static
specifier|final
name|long
name|C6
init|=
name|C5
operator|*
literal|24L
decl_stmt|;
DECL|field|MAX
specifier|static
specifier|final
name|long
name|MAX
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|C6_C0
specifier|static
specifier|final
name|long
name|C6_C0
init|=
name|C6
operator|/
name|C0
decl_stmt|;
DECL|field|C6_C1
specifier|static
specifier|final
name|long
name|C6_C1
init|=
name|C6
operator|/
name|C1
decl_stmt|;
DECL|field|C6_C2
specifier|static
specifier|final
name|long
name|C6_C2
init|=
name|C6
operator|/
name|C2
decl_stmt|;
DECL|field|C6_C3
specifier|static
specifier|final
name|long
name|C6_C3
init|=
name|C6
operator|/
name|C3
decl_stmt|;
DECL|field|C6_C4
specifier|static
specifier|final
name|long
name|C6_C4
init|=
name|C6
operator|/
name|C4
decl_stmt|;
DECL|field|C6_C5
specifier|static
specifier|final
name|long
name|C6_C5
init|=
name|C6
operator|/
name|C5
decl_stmt|;
DECL|field|C5_C0
specifier|static
specifier|final
name|long
name|C5_C0
init|=
name|C5
operator|/
name|C0
decl_stmt|;
DECL|field|C5_C1
specifier|static
specifier|final
name|long
name|C5_C1
init|=
name|C5
operator|/
name|C1
decl_stmt|;
DECL|field|C5_C2
specifier|static
specifier|final
name|long
name|C5_C2
init|=
name|C5
operator|/
name|C2
decl_stmt|;
DECL|field|C5_C3
specifier|static
specifier|final
name|long
name|C5_C3
init|=
name|C5
operator|/
name|C3
decl_stmt|;
DECL|field|C5_C4
specifier|static
specifier|final
name|long
name|C5_C4
init|=
name|C5
operator|/
name|C4
decl_stmt|;
DECL|field|C4_C0
specifier|static
specifier|final
name|long
name|C4_C0
init|=
name|C4
operator|/
name|C0
decl_stmt|;
DECL|field|C4_C1
specifier|static
specifier|final
name|long
name|C4_C1
init|=
name|C4
operator|/
name|C1
decl_stmt|;
DECL|field|C4_C2
specifier|static
specifier|final
name|long
name|C4_C2
init|=
name|C4
operator|/
name|C2
decl_stmt|;
DECL|field|C4_C3
specifier|static
specifier|final
name|long
name|C4_C3
init|=
name|C4
operator|/
name|C3
decl_stmt|;
DECL|field|C3_C0
specifier|static
specifier|final
name|long
name|C3_C0
init|=
name|C3
operator|/
name|C0
decl_stmt|;
DECL|field|C3_C1
specifier|static
specifier|final
name|long
name|C3_C1
init|=
name|C3
operator|/
name|C1
decl_stmt|;
DECL|field|C3_C2
specifier|static
specifier|final
name|long
name|C3_C2
init|=
name|C3
operator|/
name|C2
decl_stmt|;
DECL|field|C2_C0
specifier|static
specifier|final
name|long
name|C2_C0
init|=
name|C2
operator|/
name|C0
decl_stmt|;
DECL|field|C2_C1
specifier|static
specifier|final
name|long
name|C2_C1
init|=
name|C2
operator|/
name|C1
decl_stmt|;
DECL|field|C1_C0
specifier|static
specifier|final
name|long
name|C1_C0
init|=
name|C1
operator|/
name|C0
decl_stmt|;
DECL|field|MAX_C6_C0
specifier|static
specifier|final
name|long
name|MAX_C6_C0
init|=
name|MAX
operator|/
name|C6_C0
decl_stmt|;
DECL|field|MAX_C6_C1
specifier|static
specifier|final
name|long
name|MAX_C6_C1
init|=
name|MAX
operator|/
name|C6_C1
decl_stmt|;
DECL|field|MAX_C6_C2
specifier|static
specifier|final
name|long
name|MAX_C6_C2
init|=
name|MAX
operator|/
name|C6_C2
decl_stmt|;
DECL|field|MAX_C6_C3
specifier|static
specifier|final
name|long
name|MAX_C6_C3
init|=
name|MAX
operator|/
name|C6_C3
decl_stmt|;
DECL|field|MAX_C6_C4
specifier|static
specifier|final
name|long
name|MAX_C6_C4
init|=
name|MAX
operator|/
name|C6_C4
decl_stmt|;
DECL|field|MAX_C6_C5
specifier|static
specifier|final
name|long
name|MAX_C6_C5
init|=
name|MAX
operator|/
name|C6_C5
decl_stmt|;
DECL|field|MAX_C5_C0
specifier|static
specifier|final
name|long
name|MAX_C5_C0
init|=
name|MAX
operator|/
name|C5_C0
decl_stmt|;
DECL|field|MAX_C5_C1
specifier|static
specifier|final
name|long
name|MAX_C5_C1
init|=
name|MAX
operator|/
name|C5_C1
decl_stmt|;
DECL|field|MAX_C5_C2
specifier|static
specifier|final
name|long
name|MAX_C5_C2
init|=
name|MAX
operator|/
name|C5_C2
decl_stmt|;
DECL|field|MAX_C5_C3
specifier|static
specifier|final
name|long
name|MAX_C5_C3
init|=
name|MAX
operator|/
name|C5_C3
decl_stmt|;
DECL|field|MAX_C5_C4
specifier|static
specifier|final
name|long
name|MAX_C5_C4
init|=
name|MAX
operator|/
name|C5_C4
decl_stmt|;
DECL|field|MAX_C4_C0
specifier|static
specifier|final
name|long
name|MAX_C4_C0
init|=
name|MAX
operator|/
name|C4_C0
decl_stmt|;
DECL|field|MAX_C4_C1
specifier|static
specifier|final
name|long
name|MAX_C4_C1
init|=
name|MAX
operator|/
name|C4_C1
decl_stmt|;
DECL|field|MAX_C4_C2
specifier|static
specifier|final
name|long
name|MAX_C4_C2
init|=
name|MAX
operator|/
name|C4_C2
decl_stmt|;
DECL|field|MAX_C4_C3
specifier|static
specifier|final
name|long
name|MAX_C4_C3
init|=
name|MAX
operator|/
name|C4_C3
decl_stmt|;
DECL|field|MAX_C3_C0
specifier|static
specifier|final
name|long
name|MAX_C3_C0
init|=
name|MAX
operator|/
name|C3_C0
decl_stmt|;
DECL|field|MAX_C3_C1
specifier|static
specifier|final
name|long
name|MAX_C3_C1
init|=
name|MAX
operator|/
name|C3_C1
decl_stmt|;
DECL|field|MAX_C3_C2
specifier|static
specifier|final
name|long
name|MAX_C3_C2
init|=
name|MAX
operator|/
name|C3_C2
decl_stmt|;
DECL|field|MAX_C2_C0
specifier|static
specifier|final
name|long
name|MAX_C2_C0
init|=
name|MAX
operator|/
name|C2_C0
decl_stmt|;
DECL|field|MAX_C2_C1
specifier|static
specifier|final
name|long
name|MAX_C2_C1
init|=
name|MAX
operator|/
name|C2_C1
decl_stmt|;
DECL|field|MAX_C1_C0
specifier|static
specifier|final
name|long
name|MAX_C1_C0
init|=
name|MAX
operator|/
name|C1_C0
decl_stmt|;
DECL|method|x (long d, long m, long over)
specifier|static
name|long
name|x
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|,
name|long
name|over
parameter_list|)
block|{
if|if
condition|(
name|d
operator|>
name|over
condition|)
return|return
name|Long
operator|.
name|MAX_VALUE
return|;
if|if
condition|(
name|d
operator|<
operator|-
name|over
condition|)
return|return
name|Long
operator|.
name|MIN_VALUE
return|;
return|return
name|d
operator|*
name|m
return|;
block|}
comment|// exceptions below changed from AbstractMethodError for GWT
DECL|method|convert (long sourceDuration, TimeUnit sourceUnit)
specifier|public
name|long
name|convert
parameter_list|(
name|long
name|sourceDuration
parameter_list|,
name|TimeUnit
name|sourceUnit
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toNanos (long duration)
specifier|public
name|long
name|toNanos
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toMicros (long duration)
specifier|public
name|long
name|toMicros
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toMillis (long duration)
specifier|public
name|long
name|toMillis
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toSeconds (long duration)
specifier|public
name|long
name|toSeconds
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toMinutes (long duration)
specifier|public
name|long
name|toMinutes
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toHours (long duration)
specifier|public
name|long
name|toHours
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|toDays (long duration)
specifier|public
name|long
name|toDays
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
DECL|method|excessNanos (long d, long m)
specifier|abstract
name|int
name|excessNanos
parameter_list|(
name|long
name|d
parameter_list|,
name|long
name|m
parameter_list|)
function_decl|;
block|}
end_enum

end_unit

