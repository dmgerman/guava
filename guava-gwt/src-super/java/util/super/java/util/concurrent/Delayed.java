begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is a modified version of  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/Delayed.java?revision=1.11  * which contained the following notice:  *  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
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
comment|/** Emulation of Delayed. */
end_comment

begin_interface
DECL|interface|Delayed
specifier|public
interface|interface
name|Delayed
extends|extends
name|Comparable
argument_list|<
name|Delayed
argument_list|>
block|{
DECL|method|getDelay (TimeUnit unit)
name|long
name|getDelay
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

