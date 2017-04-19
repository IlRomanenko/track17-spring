package track.messenger.store;

import track.messenger.models.User;

import java.util.List;

public interface UserStore {
    /**
     * Добавить пользователя в хранилище
     * Вернуть его же
     */
    User addUser(String login, String password);

    /**
     * Обновить информацию о пользователе
     */
    User updateUser(User user);

    /**
     *
     * Получить пользователя по логину/паролю
     * return null if user not found
     */
    User getUser(String login, String pass);

    /**
     *
     * Получить пользователя по id, например запрос информации/профиля
     * return null if user not found
     */
    User getUserById(Long id);

    List<User> getUsers();
}
