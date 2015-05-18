begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * This file is a modified version of  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/main/java/util/concurrent/CountDownLatch.java?revision=1.43  * which contained the following notice:  *  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
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
comment|/**  * Emulation of ScheduleFuture.  *  * @param<V> value type returned by the future.  */
end_comment

begin_interface
DECL|interface|ScheduledFuture
specifier|public
interface|interface
name|ScheduledFuture
parameter_list|<
name|V
parameter_list|>
extends|extends
name|Delayed
extends|,
name|Future
argument_list|<
name|V
argument_list|>
block|{ }
end_interface

end_unit

