begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_comment
comment|/**  * Basic utility libraries and interfaces.  *  *<p>This package is a part of the open-source<a href="http://github.com/google/guava">Guava</a>  * library.  *  *<h2>Contents</h2>  *  *<h3>String-related utilities</h3>  *  *<ul>  *<li>{@link com.google.common.base.Ascii}  *<li>{@link com.google.common.base.CaseFormat}  *<li>{@link com.google.common.base.CharMatcher}  *<li>{@link com.google.common.base.Charsets}  *<li>{@link com.google.common.base.Joiner}  *<li>{@link com.google.common.base.Splitter}  *<li>{@link com.google.common.base.Strings}  *</ul>  *  *<h3>Function types</h3>  *  *<ul>  *<li>{@link com.google.common.base.Function}, {@link com.google.common.base.Functions}  *<li>{@link com.google.common.base.Predicate}, {@link com.google.common.base.Predicates}  *<li>{@link com.google.common.base.Equivalence}  *<li>{@link com.google.common.base.Converter}  *<li>{@link com.google.common.base.Supplier}, {@link com.google.common.base.Suppliers}  *</ul>  *  *<h3>Other</h3>  *  *<ul>  *<li>{@link com.google.common.base.Defaults}  *<li>{@link com.google.common.base.Enums}  *<li>{@link com.google.common.base.Objects}  *<li>{@link com.google.common.base.Optional}  *<li>{@link com.google.common.base.Preconditions}  *<li>{@link com.google.common.base.Stopwatch}  *<li>{@link com.google.common.base.Throwables}  *</ul>  *  */
end_comment

begin_annotation
annotation|@
name|CheckReturnValue
end_annotation

begin_annotation
annotation|@
name|ParametersAreNonnullByDefault
end_annotation

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
name|errorprone
operator|.
name|annotations
operator|.
name|CheckReturnValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|ParametersAreNonnullByDefault
import|;
end_import

end_unit

