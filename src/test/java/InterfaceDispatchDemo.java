/*
   Copyright 2009-2013 Attila Szegedi

   Licensed under either the Apache License, Version 2.0 (the "Apache
   License") or the BSD License (the "BSD License"), with licensee
   being free to choose either of the two at their discretion.

   You may not use this file except in compliance with either the Apache
   License or the BSD License.

   A copy of the BSD License is available in the root directory of the
   source distribution of the project under the file name
   "Dynalink-License-BSD.txt".

   A copy of the Apache License is available in the root directory of the
   source distribution of the project under the file name
   "Dynalink-License-Apache-2.0.txt". Alternatively, you may obtain a
   copy of the Apache License at <http://www.apache.org/licenses/LICENSE-2.0>

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See your chosen License for the specific language governing permissions
   and limitations under that License.
*/

public class InterfaceDispatchDemo {
    interface Greeter {
        public void sayHello();
    }

    static class English implements Greeter {
        @Override
        public void sayHello() {
            System.out.println("Hello!");
        }
    }

    static class Spanish implements Greeter {
        @Override
        public void sayHello() {
            System.out.println("Hola!");
        }
    }

    public static void main(String[] args) {
        final Greeter[] greeters =
                new Greeter[] { new English(), new Spanish(), new English(), new Spanish(), new Spanish(),
                        new English(), new English() };
        for(Greeter greeter: greeters) {
            greeter.sayHello();
        }
    }
}