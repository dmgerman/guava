begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2014 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.math
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|math
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
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
name|ImmutableSet
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
name|Sets
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Tests that the different algorithms benchmarked in {@link QuantilesBenchmark} are actually all  * returning more-or-less the same answers.  */
end_comment

begin_class
DECL|class|QuantilesAlgorithmTest
specifier|public
class|class
name|QuantilesAlgorithmTest
extends|extends
name|TestCase
block|{
DECL|field|RNG
specifier|private
specifier|static
specifier|final
name|Random
name|RNG
init|=
operator|new
name|Random
argument_list|(
literal|82674067L
argument_list|)
decl_stmt|;
DECL|field|DATASET_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DATASET_SIZE
init|=
literal|1000
decl_stmt|;
DECL|field|ALLOWED_ERROR
specifier|private
specifier|static
specifier|final
name|double
name|ALLOWED_ERROR
init|=
literal|1.0e-10
decl_stmt|;
DECL|field|REFERENCE_ALGORITHM
specifier|private
specifier|static
specifier|final
name|QuantilesAlgorithm
name|REFERENCE_ALGORITHM
init|=
name|QuantilesAlgorithm
operator|.
name|SORTING
decl_stmt|;
DECL|field|NON_REFERENCE_ALGORITHMS
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|QuantilesAlgorithm
argument_list|>
name|NON_REFERENCE_ALGORITHMS
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|QuantilesAlgorithm
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|REFERENCE_ALGORITHM
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|dataset
specifier|private
name|double
index|[]
name|dataset
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
block|{
name|dataset
operator|=
operator|new
name|double
index|[
name|DATASET_SIZE
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|DATASET_SIZE
condition|;
name|i
operator|++
control|)
block|{
name|dataset
index|[
name|i
index|]
operator|=
name|RNG
operator|.
name|nextDouble
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testSingleQuantile_median ()
specifier|public
name|void
name|testSingleQuantile_median
parameter_list|()
block|{
name|double
name|referenceValue
init|=
name|REFERENCE_ALGORITHM
operator|.
name|singleQuantile
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|QuantilesAlgorithm
name|algorithm
range|:
name|NON_REFERENCE_ALGORITHMS
control|)
block|{
name|assertWithMessage
argument_list|(
literal|"Mismatch between %s and %s"
argument_list|,
name|algorithm
argument_list|,
name|REFERENCE_ALGORITHM
argument_list|)
operator|.
name|that
argument_list|(
name|algorithm
operator|.
name|singleQuantile
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isWithin
argument_list|(
name|ALLOWED_ERROR
argument_list|)
operator|.
name|of
argument_list|(
name|referenceValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testSingleQuantile_percentile99 ()
specifier|public
name|void
name|testSingleQuantile_percentile99
parameter_list|()
block|{
name|double
name|referenceValue
init|=
name|REFERENCE_ALGORITHM
operator|.
name|singleQuantile
argument_list|(
literal|99
argument_list|,
literal|100
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|QuantilesAlgorithm
name|algorithm
range|:
name|NON_REFERENCE_ALGORITHMS
control|)
block|{
name|assertWithMessage
argument_list|(
literal|"Mismatch between %s and %s"
argument_list|,
name|algorithm
argument_list|,
name|REFERENCE_ALGORITHM
argument_list|)
operator|.
name|that
argument_list|(
name|algorithm
operator|.
name|singleQuantile
argument_list|(
literal|99
argument_list|,
literal|100
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isWithin
argument_list|(
name|ALLOWED_ERROR
argument_list|)
operator|.
name|of
argument_list|(
name|referenceValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMultipleQuantile ()
specifier|public
name|void
name|testMultipleQuantile
parameter_list|()
block|{
name|ImmutableSet
argument_list|<
name|Integer
argument_list|>
name|indexes
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|50
argument_list|,
literal|90
argument_list|,
literal|99
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|Double
argument_list|>
name|referenceQuantiles
init|=
name|REFERENCE_ALGORITHM
operator|.
name|multipleQuantiles
argument_list|(
name|indexes
argument_list|,
literal|100
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|referenceQuantiles
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|indexes
argument_list|)
expr_stmt|;
for|for
control|(
name|QuantilesAlgorithm
name|algorithm
range|:
name|NON_REFERENCE_ALGORITHMS
control|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|Double
argument_list|>
name|quantiles
init|=
name|algorithm
operator|.
name|multipleQuantiles
argument_list|(
name|indexes
argument_list|,
literal|100
argument_list|,
name|dataset
operator|.
name|clone
argument_list|()
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"Wrong keys from "
operator|+
name|algorithm
argument_list|)
operator|.
name|that
argument_list|(
name|quantiles
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|indexes
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
range|:
name|indexes
control|)
block|{
name|assertWithMessage
argument_list|(
literal|"Mismatch between %s and %s at %s"
argument_list|,
name|algorithm
argument_list|,
name|REFERENCE_ALGORITHM
argument_list|,
name|i
argument_list|)
operator|.
name|that
argument_list|(
name|quantiles
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|isWithin
argument_list|(
name|ALLOWED_ERROR
argument_list|)
operator|.
name|of
argument_list|(
name|referenceQuantiles
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
