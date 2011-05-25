begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2011 Google Inc. All Rights Reserved.
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
name|io
operator|.
name|Serializable
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
comment|/**  * Equivalence applied on functional result.  *  * @author Bob Lee  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|FunctionalEquivalence
specifier|final
class|class
name|FunctionalEquivalence
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
extends|extends
name|AbstractEquivalence
argument_list|<
name|F
argument_list|>
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
DECL|field|function
specifier|private
specifier|final
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
decl_stmt|;
DECL|field|resultEquivalence
specifier|private
specifier|final
name|Equivalence
argument_list|<
name|T
argument_list|>
name|resultEquivalence
decl_stmt|;
DECL|method|FunctionalEquivalence ( Function<F, ? extends T> function, Equivalence<T> resultEquivalence)
name|FunctionalEquivalence
parameter_list|(
name|Function
argument_list|<
name|F
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|function
parameter_list|,
name|Equivalence
argument_list|<
name|T
argument_list|>
name|resultEquivalence
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultEquivalence
operator|=
name|checkNotNull
argument_list|(
name|resultEquivalence
argument_list|)
expr_stmt|;
block|}
DECL|method|equivalentNonNull (F a, F b)
annotation|@
name|Override
specifier|protected
name|boolean
name|equivalentNonNull
parameter_list|(
name|F
name|a
parameter_list|,
name|F
name|b
parameter_list|)
block|{
return|return
name|resultEquivalence
operator|.
name|equivalent
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|a
argument_list|)
argument_list|,
name|function
operator|.
name|apply
argument_list|(
name|b
argument_list|)
argument_list|)
return|;
block|}
DECL|method|hashNonNull (F a)
annotation|@
name|Override
specifier|protected
name|int
name|hashNonNull
parameter_list|(
name|F
name|a
parameter_list|)
block|{
return|return
name|resultEquivalence
operator|.
name|hash
argument_list|(
name|function
operator|.
name|apply
argument_list|(
name|a
argument_list|)
argument_list|)
return|;
block|}
DECL|method|equals (@ullable Object obj)
annotation|@
name|Override
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
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|FunctionalEquivalence
condition|)
block|{
name|FunctionalEquivalence
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|FunctionalEquivalence
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|function
operator|.
name|equals
argument_list|(
name|that
operator|.
name|function
argument_list|)
operator|&&
name|resultEquivalence
operator|.
name|equals
argument_list|(
name|that
operator|.
name|resultEquivalence
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
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
name|function
argument_list|,
name|resultEquivalence
argument_list|)
return|;
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|resultEquivalence
operator|+
literal|".onResultOf("
operator|+
name|function
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

