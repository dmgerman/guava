begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * This package contains utility methods and classes for working with Java I/O,  * for example input streams, output streams, readers, writers, and files.  *  *<p>Many of the methods are based on the  * {@link com.google.common.io.InputSupplier} and  * {@link com.google.common.io.OutputSupplier} interfaces. They are used as  * factories for I/O objects that might throw {@link java.io.IOException} when  * being created. The advantage of using a factory is that the helper methods in  * this package can take care of closing the resource properly, even if an  * exception is thrown. The {@link com.google.common.io.ByteStreams},  * {@link com.google.common.io.CharStreams}, and  * {@link com.google.common.io.Files} classes all have static helper methods to  * create new factories and to work with them.  *  *<p>This package is a part of the open-source  *<a href="http://guava-libraries.googlecode.com">Guava libraries</a>.  *  * @author Chris Nokleberg  */
end_comment

begin_annotation
annotation|@
name|ParametersAreNonnullByDefault
end_annotation

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
package|;
end_package

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

