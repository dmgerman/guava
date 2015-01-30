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
comment|/**  * Emulation of CountDownLatch in GWT. Since GWT environment is single threaded, attempting to block  * on the latch by calling {@link #await()} or {@link #await(long, TimeUnit)} when it is not ready  * is considered illegal because it would lead to a deadlock. Both methods will throw {@link  * IllegalStateException} to avoid the deadlock.  */
end_comment

begin_class
DECL|class|CountDownLatch
specifier|public
class|class
name|CountDownLatch
block|{
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|method|CountDownLatch (int count)
specifier|public
name|CountDownLatch
parameter_list|(
name|int
name|count
parameter_list|)
block|{
if|if
condition|(
name|count
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"count< 0"
argument_list|)
throw|;
block|}
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
DECL|method|await ()
specifier|public
name|void
name|await
parameter_list|()
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"May not block. Count is "
operator|+
name|count
argument_list|)
throw|;
block|}
block|}
DECL|method|await (long timeout, TimeUnit unit)
specifier|public
name|boolean
name|await
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|await
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|countDown ()
specifier|public
name|void
name|countDown
parameter_list|()
block|{
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|count
operator|--
expr_stmt|;
block|}
block|}
DECL|method|getCount ()
specifier|public
name|long
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|"[Count = "
operator|+
name|count
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

