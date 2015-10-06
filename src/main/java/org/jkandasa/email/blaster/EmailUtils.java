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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.1
 */
public class EmailUtils {
    private static final Logger _logger = LoggerFactory.getLogger(EmailUtils.class);
    private static final String TO_NAME = "\\$user";
    private static final String ATTACHMENTS = "\\$attachments";
    public static final String EMAIL_SEPARATOR = ";";
    private static String subject = null;
    private static String message = null;

    private EmailUtils() {
        //Utils class
    }

    public static List<Address> getSenders(String sendersListFile) throws FileNotFoundException {
        BeanListProcessor<Address> rowProcessor = new BeanListProcessor<Address>(Address.class);

        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.setRowProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);

        CsvParser parser = new CsvParser(parserSettings);
        parser.parse(new FileReader(sendersListFile));

        // The BeanListProcessor provides a list of objects extracted from the input.
        return rowProcessor.getBeans();
    }

    public static void sendEmail(AppProperties appProperties, Address address) throws EmailException, IOException {
        HtmlEmail email = initializeEmail(appProperties);
        StringBuilder attachmentBuilder = new StringBuilder();

        String[] attachments = appProperties.getAttachments().split(",");
        for (String attachment : attachments) {
            File img = new File(attachment);
            if (appProperties.isEmbeddedAttachments()) {
                attachmentBuilder.append("<br><img src=cid:").append(email.embed(img)).append(">");
            } else {
                email.attach(img);
            }
        }

        email.setSubject(getSubject(appProperties, address));
        email.setHtmlMsg(getMessage(appProperties, address).replaceAll(ATTACHMENTS, attachmentBuilder.toString()));
        email.addTo(address.getEmails().split(EMAIL_SEPARATOR));
        String sendReturn = email.send();
        _logger.debug("Send Status:[{}]", sendReturn);
        _logger.trace("Email successfully sent to [{}]", address);
    }

    public static HtmlEmail initializeEmail(AppProperties appProperties) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(appProperties.getSmtpHost());
        email.setSmtpPort(Integer.valueOf(appProperties.getSmtpPort()));
        if (appProperties.getUsername() != null && appProperties.getUsername().length() > 0) {
            email.setAuthenticator(new DefaultAuthenticator(appProperties.getUsername(), appProperties.getPassword()));
        }
        email.setSSLOnConnect(appProperties.isEnableSSL());
        email.setFrom(appProperties.getFromAddress());
        return email;
    }

    private static String getSubject(AppProperties appProperties, Address address) throws IOException {
        if (subject == null) {
            subject = new String(Files.readAllBytes(Paths.get(appProperties.getEmailSubjectFile())),
                    StandardCharsets.UTF_8);
        }
        return subject.replaceAll(TO_NAME, address.getName());
    }

    private static String getMessage(AppProperties appProperties, Address address) throws IOException {
        if (message == null) {
            message = new String(Files.readAllBytes(Paths.get(appProperties.getEmailFormatTemplate())),
                    StandardCharsets.UTF_8);
        }
        return message.replaceAll(TO_NAME, address.getName());
    }

}
