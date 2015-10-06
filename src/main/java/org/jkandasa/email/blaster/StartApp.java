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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jeeva Kandasamy (jkandasa)
 * @since 0.0.1
 */
public class StartApp {
    private static final Logger _logger = LoggerFactory.getLogger(StartApp.class.getName());
    static long start;
    static AppProperties appProperties = null;

    public static void main(String[] args) {
        start = System.currentTimeMillis();
        loadInitialProperties();
        _logger.info("Application:{}, Version:{}",
                AppProperties.APPLICATION_NAME, AppProperties.APPLICATION_VERSION);
        _logger.debug("App Properties: {}", appProperties.toString());
        try {
            List<Address> addresses = EmailUtils.getRecipients(appProperties.getRecipientsFile());
            _logger.info("Total number of senders:{}", addresses.size());
            int counter = 0;
            for (Address address : addresses) {
                try {
                    EmailUtils.sendEmail(appProperties, address);
                    counter++;
                    if (counter % 25 == 0) {
                        _logger.info("Sent {} of {} email(s) successfully..", counter, addresses.size());
                    } else {
                        _logger.debug("Sent {} of {} email(s)successfully..", counter, addresses.size());
                    }
                } catch (EmailException ex) {
                    _logger.error("Unable to send for the Address:[{}]", address);
                }
            }
            _logger.info("Email blaster completed successfully for {} recipients in [{}] Seconds",
                    counter, new DecimalFormat("#.###").format((System.currentTimeMillis() - start) / 1000.0));
        } catch (FileNotFoundException ex) {
            _logger.error("File not found!", ex);
        } catch (IOException ex) {
            _logger.error("IO Exception!", ex);
        } catch (Exception ex) {
            _logger.error("Exception,", ex);
        }
    }

    private static boolean loadInitialProperties() {
        String propertiesFile = System.getProperty("eb.conf.file");
        try {
            Properties properties = new Properties();
            if (propertiesFile != null) {
                properties.load(new FileReader(propertiesFile));
            } else {
                throw new RuntimeException("Supply eb.properties file via 'eb.conf.file'!");
            }
            appProperties = new AppProperties(properties);
            _logger.debug("Properties are loaded successfuly...");
            return true;
        } catch (IOException ex) {
            _logger.error("Exception while loading properties file, ", ex);
            return false;
        }
    }
}
