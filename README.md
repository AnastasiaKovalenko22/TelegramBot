# TelegramBot
Tabata Fitness Bot (телеграм бот для создания тренировок Табата)

телеграм бот, который при запуске рассказывает о том что он формирует план тренировки в зависимости от выбранного уровня сложности и целевых групп мышц,
также рассказывает о структуре этих тренировок (Табата)
Далее задаёт вопрос с выбором ответа про уровень сложности
Затем вопрос с выбором целевых групп мышц (выбрать можно не более двух)
Проводит тренировку

Пример работы бота:

Бот : Выберите уровень сложности(варианты ответа: новичок, любитель, продвинутый)
новичок : 1 раунд 6 циклов
любитель : 2 раунда по 8 циклов
продвинутый : 3 раунда по 8 циклов
Кнопки с вариантами ответа.

Пользователь: нажал новичок

Бот: Вы выбрали уровень новичок

Бот: Выберите целевую группу мышц(варианты ответа: ноги + ягодицы, руки + грудь + спина, пресс)
Кнопки с вариантами ответа

Пользователь: нажал пресс

Бот: Вы выбрали следующую группу мышц: пресс

Бот спрашивает: начать тренировку?
Кнопки (начать или отменить)
Пользователь: нажимает начать

Бот: начать первый раунд?
Кнопки: начать или завершить тренировку

Пользователь: нажимает начать

Бот: 1 упражнение: боковые скручивания. Начать?
Кнопки: начать или завершить тренировку

Пользователь: нажимает начать

Бот: начать 1 подход?
Кнопки: начать или завершить тренировку

Пользователь: нажимает начать

Бот: работаем!
Через 20с бот: 20 секунд прошло! Начать отдых между подходами 10 секунд?

Кнопки: начать или завершить тренировку?

Пользователь: нажимает начать

Бот: отдых! 
Через 10 секунд: 10 секунд прошло! Начать 2 подход?
Кнопки: начать или завершить тренировку?

Так 8 подходов для каждого упражнени.

Когда закончились все упражнения раунда, если есть еще раунды: 
Бот: начать 2 раунд?
Кнопки: начать или закончить тренировку?

Так для всех раундов до завершения тренировки.
