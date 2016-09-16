package org.tkalenko.utils.password;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class FilePasswordKeeper implements PasswordKeeper {
    private static final Logger log = LoggerFactory.getLogger(FilePasswordKeeper.class);
    private static final String SYSTEM_PROPERTY_FILE_PATH = "passwordFilePath";
    private static final FilePasswordKeeper INSTANCE = new FilePasswordKeeper();

    private Map<String, String> passwords = new HashMap<>();
    /**
     * файл
     */
    private File file = null;
    /**
     * Время последней модификации
     */
    private long lastDate = -1L;
    /**
     * Последняя контрольная сумма
     */
    private String lastCheckSum = null;

    /**
     * Объект для синхранизации
     */
    private Object mutex = new Object();

    private FilePasswordKeeper() {
        // TODO: 15.09.2016 установка системных свойств в приложение! при поможи слушателя web xml сделаешь дома!
        file = new File(System.getProperty(SYSTEM_PROPERTY_FILE_PATH));
    }

    public static FilePasswordKeeper getInstance() {
        return INSTANCE;
    }

    @Override
    public String getPassword(final String key) {
        if (StringUtils.isBlank(key)) {
            log.error(String.format("cant check key=[%s] password=[%s]"));
            return null;
        }
        if (!file.exists()) {
            log.error(String.format("file don't exist by path=[%s]", file.getPath()));
            return null;
        }
        if (file.lastModified() > lastDate) {
            synchronized (mutex) {
                if (file.lastModified() > lastDate) {
                    log.debug(String.format("file change by time"));
                    String checkSum = checkSum();
                    if (!StringUtils.equals(checkSum, lastCheckSum)) {
                        log.debug(String.format("file change by check sum"));
                        passwords.clear();
                        readFile();
                        lastCheckSum = checkSum;
                    }
                    lastDate = file.lastModified();
                }
            }
        }
        return passwords.get(key);
    }

    /**
     * Подсчет контротльной суммы для файла на текущий момент
     *
     * @return контрольная сумма файла
     */
    private String checkSum() {
        if (!file.exists()) {
            log.error(String.format("file don't exist by path=[%s]", file.getPath()));
            return null;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return DigestUtils.md5Hex(fileInputStream);
        } catch (Throwable t) {
            log.error(String.format("can't get check sum file"), t);
            return null;
        }
    }

    /**
     * Обновление паролей из файла
     */
    private void readFile() {
        if (!file.exists()) {
            log.error(String.format("file don't exist by path=[%s]", file.getPath()));
            return;
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                log.debug(String.format("read line=[%s]", s));
                StringTokenizer token = new StringTokenizer(s, "\t", false);
                if (token.countTokens() != 2)
                    continue;
                String userId = token.nextToken();
                String password = token.nextToken();
                log.debug(String.format("put userId=[%s] password=[%s]", userId, password));
                passwords.put(userId, password);
            }
        } catch (Throwable t) {
            log.error(String.format("error while read password file=[%s]", file.getPath()), t);
        }
    }
}
