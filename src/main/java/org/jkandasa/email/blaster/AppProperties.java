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

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.1
 */
public class AppProperties {
    private static final Logger _logger = LoggerFactory.getLogger(AppProperties.class.getName());

    public static final String APPLICATION_NAME = "Email Blaster";
    public static final String APPLICATION_VERSION = "0.0.1";

    private String username;
    private String password;
    private String fromAddress;
    private String smtpHost;
    private String smtpPort;
    private boolean enableSSL;
    private String recipientsFile;
    private String emailSubjectFile;
    private String emailFormatTemplate;
    private String attachments;
    private boolean embeddedAttachments = false;

    public AppProperties() {
    }

    public AppProperties(Properties properties) {
        this.loadProperties(properties);
    }

    public void loadProperties(Properties properties) {
        this.smtpHost = getValue(properties, "eb.email.smpt.host");
        this.smtpPort = getValue(properties, "eb.email.smpt.port");
        this.enableSSL = Boolean.valueOf(getValue(properties, "eb.email.enable.ssl"));
        this.fromAddress = getValue(properties, "eb.email.from.address");
        this.username = getValue(properties, "eb.email.username");
        this.password = getValue(properties, "eb.email.password");
        this.recipientsFile = getValue(properties, "eb.email.recipients.file");
        this.emailFormatTemplate = getValue(properties, "eb.email.html.template");
        this.emailSubjectFile = getValue(properties, "eb.email.subject.file");
        this.attachments = getValue(properties, "eb.email.attachments");
        this.embeddedAttachments = Boolean.valueOf(getValue(properties, "eb.email.attachments.embedded"));
    }

    private String getValue(Properties properties, String key) {
        String value = properties.getProperty(key);
        _logger.debug("Key:{}, Value:{}", key, key.contains("password") ? "*****" : value);

        if (value != null) {
            return key.contains("password") ? value : value.trim();
        } else {
            return null;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public String getRecipientsFile() {
        return recipientsFile;
    }

    public String getEmailFormatTemplate() {
        return emailFormatTemplate;
    }

    public String getAttachments() {
        return attachments;
    }

    public boolean isEmbeddedAttachments() {
        return embeddedAttachments;
    }

    public String getEmailSubjectFile() {
        return emailSubjectFile;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SMTP Host:").append(this.getSmtpHost());
        builder.append("SMTP Port:").append(this.getSmtpPort());
        builder.append(", Is SSL Enabled?:").append(this.isEnableSSL());
        builder.append(", From Address:").append(this.getFromAddress());
        builder.append(", Username:").append(this.getUsername());
        builder.append(", Senders File Location:").append(this.getRecipientsFile());
        builder.append(", Email Subject File:").append(this.getEmailSubjectFile());
        builder.append(", Email Template File:").append(this.getEmailFormatTemplate());
        builder.append(", Attachements:").append(this.getAttachments());
        builder.append(", Is Attachements Embedded:").append(this.isEmbeddedAttachments());
        return builder.toString();
    }
}
