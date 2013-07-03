/*
   Copyright 2009-2013 Attila Szegedi

   Licensed under both the Apache License, Version 2.0 (the "Apache License")
   and the BSD License (the "BSD License"), with licensee being free to
   choose either of the two at their discretion.

   You may not use this file except in compliance with either the Apache
   License or the BSD License.

   If you choose to use this file in compliance with the Apache License, the
   following notice applies to you:

       You may obtain a copy of the Apache License at

           http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
       implied. See the License for the specific language governing
       permissions and limitations under the License.

   If you choose to use this file in compliance with the BSD License, the
   following notice applies to you:

       Redistribution and use in source and binary forms, with or without
       modification, are permitted provided that the following conditions are
       met:
       * Redistributions of source code must retain the above copyright
         notice, this list of conditions and the following disclaimer.
       * Redistributions in binary form must reproduce the above copyright
         notice, this list of conditions and the following disclaimer in the
         documentation and/or other materials provided with the distribution.
       * Neither the name of the copyright holder nor the names of
         contributors may be used to endorse or promote products derived from
         this software without specific prior written permission.

       THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
       IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
       TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
       PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER
       BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
       CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
       SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
       BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
       WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
       OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
       ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.dynalang.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.dynalang.dynalink.beans.sandbox.Unreflector;

/**
 * Provides lookup of unreflected method handles through delegation to an instance of {@link SafeUnreflectorImpl}. If
 * Dynalink is run as trusted code, the delegate class is loaded into an isolated zero-permissions protection domain,
 * serving as a firebreak against an accidental privilege escalation downstream.
 */
final class SafeUnreflector {
    private static final String UNREFLECTOR_IMPL_CLASS_NAME = "org.dynalang.dynalink.beans.SafeUnreflectorImpl";
    private static final Unreflector impl = createImpl();

    private SafeUnreflector() {
    }

    /**
     * Performs a {@link java.lang.invoke.MethodHandles.Lookup#unreflect(Method)}, converting any encountered
     * {@link IllegalAccessException} into an {@link IllegalAccessError}.
     *
     * @param m the method to unreflect
     * @return the unreflected method handle.
     */
    static MethodHandle unreflect(Method m) {
        return impl.unreflect(m);
    }

    /**
     * Performs a {@link java.lang.invoke.MethodHandles.Lookup#unreflectGetter(Field)}, converting any encountered
     * {@link IllegalAccessException} into an {@link IllegalAccessError}.
     *
     * @param f the field for which a getter is unreflected
     * @return the unreflected field getter handle.
     */
    static MethodHandle unreflectGetter(Field f) {
        return impl.unreflectGetter(f);
    }

    /**
     * Performs a {@link java.lang.invoke.MethodHandles.Lookup#unreflectSetter(Field)}, converting any encountered
     * {@link IllegalAccessException} into an {@link IllegalAccessError}.
     *
     * @param f the field for which a setter is unreflected
     * @return the unreflected field setter handle.
     */
    static MethodHandle unreflectSetter(Field f) {
        return impl.unreflectSetter(f);
    }

    static MethodHandle unreflectConstructor(Constructor<?> c) {
        return impl.unreflectConstructor(c);
    }

    private static Unreflector createImpl() {
        final Class<?> unreflectorImplClass = AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
            @Override
            public Class<?> run() {
                return SandboxClassLoader.loadClass(UNREFLECTOR_IMPL_CLASS_NAME);
            }
        });
        try {
            return (Unreflector)unreflectorImplClass.newInstance();
        } catch(InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}