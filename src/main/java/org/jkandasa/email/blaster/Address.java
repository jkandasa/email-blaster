/**
 * Copyright (C) 2015 Jeeva Kandasamy (jkandasa@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jkandasa.email.blaster;

import java.util.Arrays;

import com.univocity.parsers.annotations.Parsed;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.1
 */
public class Address {
    @Parsed
    private String name;
    @Parsed
    private String emails;

    public Address() {

    }

    public Address(String name, String emails) {
        this.name = name;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name:").append(this.name);
        builder.append(", Email(s):").append(Arrays.asList(this.emails));
        return builder.toString();
    }
}
