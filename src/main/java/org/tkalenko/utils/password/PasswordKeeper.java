package org.tkalenko.utils.password;

public interface PasswordKeeper {

    /**
     * Получение пароля
     * @param key ключ для поиска пароля
     * @return пароль, null если не нашел
     */
    String getPassword(final String key);
}
