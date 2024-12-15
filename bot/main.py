import configparser
import json
from multiprocessing import Process

import requests
import telebot
from kafka.consumer import KafkaConsumer
from telebot import types
from telegram import ParseMode

from constants import URL, AUTH_HEADER
from helpers import emoji, get_default_reply_keyboard, UNKNOWN_TEXT_HANDLER

config = configparser.ConfigParser()
config.read("settings.ini")

token = config["Telegram"]["token"]
bot = telebot.TeleBot(token)

IS_USER_LOGGED_IN = False
USER_TOKEN = 'default token'
# USER_TOKEN = 'eyJhbGciOiJIUzUxMiJ9.eyJpc191c2VyX3JvbGUiOnRydWUsImlzX2FkbWluX3JvbGUiOmZhbHNlLCJpc19tb2RlcmF0b3Jfcm9sZSI6ZmFsc2UsImlkIjozLCJlbWFpbCI6ImFsZXhrb3JpdHNhQGdtYWlsLmNvbSIsImlhdCI6MTY4MjQ0NTg3NiwiZXhwIjoyNjgyNDQ1Njc3fQ.84NJPWDhEGERJw94VN4DjL2m7uMMTSzEWUc7Jq_7V1HOfKHiFAfTHYLbCip8cIb4_Ohg-0ylExAByYEW_OccbA'
USER = {
    'id': 0,
    'name': 'Name',
    'surname': 'Surname',
    'email': 'example@a.com',
    'chat_id': 0
}


@bot.message_handler(commands=['start'])
def start_message(message):
    message_text = f"Привет! Я - бот, который поможет не терять задачи в teamsync " \
                   f"и смотреть статистику по Вашим организациям!" \
                   f"Приятно познакомиться! {emoji['hello']}\n " \
                   f"Для старта мне необходим Ваш токен авторизации, чтобы отображать корректные данные." \
                   f"Этот токен можно создать в личном кабинете! " \
                   f"Для авторизации необходимо ввести /auth и после этого ввести свой токен. \n" \
                   f"После авторизации Вы сможете пользоваться ботом в полной мере! {emoji['love']}"

    bot.send_message(
        message.chat.id,
        text=message_text
    )


@bot.message_handler(commands=['auth'])
def auth_message(message):
    msg = bot.send_message(
        message.chat.id,
        text="Пожалуйста, введите свой токен авторизации и отправьте его следующим сообщением"
    )
    bot.register_next_step_handler(msg, auth_user_function)


def auth_user_function(message):
    global USER_TOKEN
    global USER

    url = f'{URL}/api/auth/v1/bot/validate/user'
    headers = {
        AUTH_HEADER: message.text
    }
    response = requests.get(url, headers=headers)

    if response.status_code == 200:
        payload = response.json()['payload']
        USER = {
            'id': payload['id'],
            'name': payload['firstName'],
            'surname': payload['lastName'],
            'email': payload['email'],
            'chat_id': message.chat.id
        }
        USER_TOKEN = message.text

        # Вызов метода для сохранения данных о телеграм-акке в нашей БД
        request_data = {
            "firstName": message.from_user.first_name,
            "lastName": message.from_user.last_name,
            "chatId": message.from_user.id,
            "username": message.from_user.username
        }

        url_for_saving_tg_data = f'{URL}/api/auth/v1/bot/save/tg/data'
        headers_for_saving_data = {
            AUTH_HEADER: USER_TOKEN,
            "Content-Type": "application/json"
        }
        response_from_saving_tg_data = requests.post(url_for_saving_tg_data,
                                                     json=request_data,
                                                     headers=headers_for_saving_data)

        if response_from_saving_tg_data.status_code != 200:
            message_text = f"Что-то пошло не так {emoji['cry']} \n\n" \
                           f"Давайте попробуем еще раз - нажимайте на команду /auth!"
            bot.send_message(message.chat.id, message_text)
            return

        telebot.types.ReplyKeyboardMarkup(resize_keyboard=True)
        keyboard = get_default_reply_keyboard()
        message_text = f"Авторизация прошла успешно! {emoji['accept']} \n\n" \
                       f"{USER['name']}, теперь Вы можете пользоваться всеми возможностями бота. " \
                       f"Нажимайте на кнопки ниже, чтобы попробовать все! \n\n" \
                       f"Не забывайте, что если вдруг понадобится помощь, то я всегда рядом - просто нажмите кнопку " \
                       f"\"Помощь\" на клавиатуре."
        bot.send_message(
            message.chat.id,
            text=message_text, reply_markup=keyboard
        )
    else:
        message_text = f"Что-то пошло не так {emoji['cry']} \n\n" \
                       f"Давайте попробуем еще раз - нажимайте на команду /auth!"
        bot.send_message(message.chat.id, message_text)


@bot.message_handler(content_types=['text'])
def button_choice(message):
    if message.text == 'Моя организация':

        url = f'{URL}/api/tracker/v1/organization/my'
        headers = {
            AUTH_HEADER: USER_TOKEN
        }
        response = requests.get(url, headers=headers)

        organizations = response.json()['payload']
        if organizations['count'] == 0:
            message_text = "К сожалению, у Вас пока нет организаций."
        else:
            keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
            message_text = f"Список Ваших организаций {emoji['cool']} \n\n"
            for i in range(len(organizations['organizations'])):
                message_text += f"#{i + 1}. \n" \
                                f"*{organizations['organizations'][i]['name']}* \n" \
                                f"*Адрес*: {organizations['organizations'][i]['address']} \n" \
                                f"*Ссылка*: {organizations['organizations'][i]['link']} \n\n"

                keyboard.add(telebot.types.InlineKeyboardButton(
                    text=f"На страницу \"{organizations['organizations'][i]['name']}\"",
                    callback_data=f"each_organization|{organizations['organizations'][i]['id']}"))

        bot.send_message(message.chat.id,
                         text=message_text, parse_mode=ParseMode.MARKDOWN,
                         reply_markup=keyboard)
    elif message.text == "Мои задачи":
        url = f'{URL}/api/tracker/v1/task/all/my'
        headers = {
            AUTH_HEADER: USER_TOKEN
        }
        response = requests.get(url, headers=headers)

        tasks = response.json()['payload']
        message_text = f"Вот список Ваших задач {emoji['info']} \n\n"
        for i in range(len(tasks)):
            message_text += f"#{i + 1}. {tasks[i]['name']} \n" \
                            f"Описание: {tasks[i]['description']} \n" \
                            f"Текущий статус: {tasks[i]['status']} \n\n"
        bot.send_message(message.chat.id, text=message_text, reply_markup=get_default_reply_keyboard())
    else:
        bot.send_message(message.chat.id,
                         text=UNKNOWN_TEXT_HANDLER, parse_mode=ParseMode.MARKDOWN,
                         reply_markup=get_default_reply_keyboard())


@bot.callback_query_handler(func=lambda call: call.data.startswith("each_organization"))
def each_organization(call: types.CallbackQuery):
    """
    Принимается [each_organization|<organization_id>]
    """
    context = call.data.split("|")
    organization_id = context[1]

    url = f'{URL}/api/tracker/v1/organization/getById/{organization_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response_organization = requests.get(url, headers=headers)
    organization = response_organization.json()['payload']
    message_text = f"Мы все узнали про Вашу организацию {emoji['cute']} \n\n" \
                   f"Названиe - {organization['name']} \n" \
                   f"Адрес - {organization['address']} \n" \
                   f"Ссылка - {organization['link']} \n\n" \
                   f"Количество сотрудников - {len(organization['workers'])} \n" \
                   f"Количество должностей - {len(organization['functions'])} \n" \
                   f"Количество досок задач - {len(organization['taskBoards'])} \n\n" \
                   f"С моей помощью Вы можете посмотреть, какие задачи есть в Вашей организации. " \
                   f"Для этого Вам надо выбрать одну из досок задач. Более того, Вы можете создать новые задачи!"

    keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
    if len(organization['taskBoards']) > 0:
        for i in range(len(organization['taskBoards'])):
            keyboard.add(telebot.types.InlineKeyboardButton(
                text=f"Доска задач \"{organization['taskBoards'][i]['name']}\"",
                callback_data=f"each_task_board|{organization['id']}|{organization['taskBoards'][i]['id']}"))
    else:
        message_text += f"\n\n" \
                        f"К сожалению, в Вашей организации пока нет ни одной доски задач. Может быть создадим?"

    keyboard.add(telebot.types.InlineKeyboardButton(
        text="Создать новую доску задач",
        callback_data=f"create_task_board|{organization['id']}"))
    keyboard.add(telebot.types.InlineKeyboardButton(
        text="Статистика по сотрудникам",
        callback_data=f"get_stats_for_worker|{organization['id']}"))

    bot.send_message(call.message.chat.id, text=message_text, reply_markup=keyboard)


@bot.callback_query_handler(func=lambda call: call.data.startswith("get_stats_for_worker"))
def get_stats_for_worker(call: types.CallbackQuery):
    """
    Принимается [get_stats_for_worker|<organization_id>]
    """
    context = call.data.split("|")
    organization_id = context[1]

    url = f'{URL}/api/tracker/v1/organization/stats/workers/{organization_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response_organization = requests.get(url, headers=headers)
    stat_workers = response_organization.json()['payload']

    message_text = f"Итак, вот статистика по всем сотрудника! {emoji['cool']} \n\n"
    for worker in stat_workers:
        message_text += f"{worker['name']} {worker['surname']} ({worker['email']}) \n" \
                        f"Сумма заработанных денег: {worker['totalCash']} \n" \
                        f"Общее количество задач на сотруднике: {worker['totalTaskOnWorker']} \n\n"

    keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
    keyboard.add(telebot.types.InlineKeyboardButton(
        text="На страницу организации",
        callback_data=f"each_organization|{organization_id}"))

    bot.send_message(
        call.message.chat.id, text=message_text, reply_markup=keyboard
    )


@bot.callback_query_handler(func=lambda call: call.data.startswith("each_task_board"))
def each_task_board(call: types.CallbackQuery):
    """
    Принимается [each_task_board|<organization_id>|<task_board_id>]
    """
    context = call.data.split("|")
    organization_id, task_board_id = context[1], context[2]

    url = f'{URL}/api/tracker/v1/taskBoard/get/{task_board_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response = requests.get(url, headers=headers)
    task_board = response.json()['payload']

    message_text = f"Доска \"{task_board['name']}\" \n\n" \
                   f"Выберите колонку, которую хотите посмотреть. Нажмите на одну из кнопок ниже {emoji['info']}"

    keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
    for i in range(len(task_board['boardColumns'])):
        keyboard.add(telebot.types.InlineKeyboardButton(
            text=f"\"{task_board['boardColumns'][i]['name']}\"",
            callback_data=f"each_board_column|{task_board['id']}|{task_board['boardColumns'][i]['id']}"))
    bot.send_message(call.message.chat.id, text=message_text, reply_markup=keyboard)


@bot.callback_query_handler(func=lambda call: call.data.startswith("each_board_column"))
def each_board_column(call: types.CallbackQuery):
    """
    Принимается [each_board_column|<task_board_id>|<board_column_id>]
    """
    context = call.data.split("|")
    task_board_id, board_column_id = context[1], context[2]

    url = f'{URL}/api/tracker/v1/boardColumn/get/{board_column_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response = requests.get(url, headers=headers)
    board_column = response.json()['payload']

    message_text = f"Колонка \"{board_column['name']} {emoji['rate']}\" \n\n"

    keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
    if len(board_column['tasks']) != 0:
        for i in range(len(board_column['tasks'])):
            message_text += f"#{i + 1}. {board_column['tasks'][i]['name']} \n" \
                            f"Описание: {board_column['tasks'][i]['description']} \n"

            if board_column['tasks'][i]['worker']:
                message_text += f"Исполнитель: {board_column['tasks'][i]['worker']['firstName']} {board_column['tasks'][i]['worker']['lastName']} \n"

            if board_column['tasks'][i]['taskTagList']:
                message_text += f"Теги для задачи:\n"
                for t in range(len(board_column['tasks'][i]['taskTagList'])):
                    message_text += f"#{t + 1}. {board_column['tasks'][i]['taskTagList'][t]['tagValue']}\n"
            message_text += "\n\n"
            keyboard.add(telebot.types.InlineKeyboardButton(
                text=f"Задача {board_column['tasks'][i]['name']}",
                callback_data=f"each_task|{board_column['tasks'][i]['id']}|{task_board_id}"))
        message_text += "Более того, Вы можете создать новую задачу в этой колонке!"
    else:
        message_text += f"В этой колонке нет задач. Давайте создадим?"

    keyboard.add(telebot.types.InlineKeyboardButton(
        text="Создать новую задачу",
        callback_data=f"create_task|{board_column['id']}"))

    bot.send_message(call.message.chat.id, text=message_text, reply_markup=keyboard)


@bot.callback_query_handler(func=lambda call: call.data.startswith("each_task"))
def each_task(call: types.CallbackQuery):
    """
    Принимается [each_task|<task_id>|<task_board_id>]
    """
    context = call.data.split("|")
    task_id, task_board_id = context[1], context[2]

    url_for_task_board = f'{URL}/api/tracker/v1/taskBoard/get/{task_board_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response_task_board = requests.get(url_for_task_board, headers=headers)
    task_board = response_task_board.json()['payload']

    url_for_task = f'{URL}/api/tracker/v1/task/get/{task_id}'
    response_task = requests.get(url_for_task, headers=headers)
    task = response_task.json()['payload']

    message_text = f"Информация по задаче {emoji['info']} \n\n" \
                   f"Название: {task['name']} \n" \
                   f"Описание: {task['description']} \n" \
                   f"Текущий статус: {task['status']} \n\n" \
                   f"Если хотите поменять статус, то нажмите на любой из них ниже!"

    keyboard = telebot.types.InlineKeyboardMarkup(row_width=1)
    for column in task_board['boardColumns']:
        if column['name'] != task['status']:
            keyboard.add(telebot.types.InlineKeyboardButton(
                text=f"\"{column['name']}\"",
                callback_data=f"change_status_task|{column['id']}|{task['id']}"))

    bot.send_message(call.message.chat.id, text=message_text, reply_markup=keyboard)


@bot.callback_query_handler(func=lambda call: call.data.startswith("change_status_task"))
def change_task_status(call: types.CallbackQuery):
    """
    Принимается [change_status_task|<column_id>|<task_id>]
    """
    context = call.data.split("|")
    board_column_id, task_id = context[1], context[2]

    url = f'{URL}/api/tracker/v1/task/updateStatus/{board_column_id}/{task_id}'
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response = requests.put(url, headers=headers)
    updated_task = response.json()['payload']

    message_text = f"Обновленная информация по задаче {emoji['info']} \n\n" \
                   f"Название: {updated_task['name']} \n" \
                   f"Описание: {updated_task['description']} \n" \
                   f"Текущий статус: {updated_task['status']}"
    bot.send_message(call.message.chat.id, text=message_text)


@bot.callback_query_handler(func=lambda call: call.data.startswith("create_task_board"))
def create_task_board(call: types.CallbackQuery):
    """
    Принимается [create_task_board|<organization_id>]
    """
    context = call.data.split("|")
    organization_id = context[1]
    msg = bot.send_message(
        call.message.chat.id,
        text="Пожалуйста, введите название новой доски задач и отправьте его следующим сообщением"
    )
    bot.register_next_step_handler(msg, create_task_board_function, organization_id)


@bot.callback_query_handler(func=lambda call: call.data.startswith("create_task"))
def create_task(call: types.CallbackQuery):
    """
    Принимается [create_task|<board_column_id>]
    """
    context = call.data.split("|")
    board_column_id = context[1]

    message_text = f"Ура! Давайте создавать скорее задачу. Осталось совсем немного. " \
                   f"Пожалуйста, введите название задачи и отправьте его следующим сообщением."
    msg = bot.send_message(call.message.chat.id, text=message_text)
    bot.register_next_step_handler(msg, create_task_function, board_column_id)


def create_task_function(message, board_column_id):
    message_text = f"Отлично! {emoji['accept']} \n\n" \
                   f"Теперь осталось только добавить описание. Отправьте его следующим сообщением!"
    msg = bot.send_message(message.chat.id, text=message_text)
    bot.register_next_step_handler(msg, finish_creating_task_function, board_column_id, message.text)


def finish_creating_task_function(message, board_column_id, task_name):
    url = f'{URL}/api/tracker/v1/task/create'
    request_data = {
        "name": task_name,
        "description": message.text,
        "boardColumnId": board_column_id
    }
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response = requests.post(url, json=request_data, headers=headers)
    if response.status_code == 200:
        message_text = f"Задача успешно создана! {emoji['strong']} \n\n" \
                       f"Теперь ее можно брать в работу!"
        bot.send_message(message.chat.id, text=message_text)
    else:
        message_text = f"Происходит что-то неприятное с нашим сервером {emoji['sad']} \n\n" \
                       f"Попробуйте чуть позже."
        bot.send_message(message.chat.id, text=message_text)


def create_task_board_function(message, organization_id):
    url = f'{URL}/api/tracker/v1/taskBoard/create'
    request_data = {
        "name": message.text,
        "organizationId": organization_id
    }
    headers = {
        AUTH_HEADER: USER_TOKEN
    }
    response = requests.post(url, json=request_data, headers=headers)
    if response.status_code == 200:
        new_task_board = response.json()['payload']
        message_text = f"Доска \"{new_task_board['name']}\" успешно создана {emoji['accept']} \n\n" \
                       f"Теперь можно ей управлять на странице организации!"
        bot.send_message(message.chat.id, text=message_text)
    else:
        message_text = f"Происходит что-то неприятное с нашим сервером {emoji['sad']} \n\n" \
                       f"Попробуйте чуть позже."
        bot.send_message(message.chat.id, text=message_text)


@bot.message_handler(content_types=["audio", "document", "photo", "sticker", "video", "video_note", "voice"])
def handle_unexpected_content(message):
    message_text = f"*Вау!* {emoji['surprise']} \n\n" \
                   f"Я пока так не умею, но я достаточно быстро учусь. Возможно мои разработчики скоро научат меня " \
                   f"еще чему-нибудь новому и мы сможем общаться на одном языке. Кстати, их контакты можно найти, " \
                   f"нажав на кнопку *\"Помощь\"*, если Вы понимаете о чем я!"

    bot.send_message(message.chat.id,
                     text=message_text, parse_mode=ParseMode.MARKDOWN,
                     reply_markup=get_default_reply_keyboard())


def send_notify_about_assign_task(kafka_message):
    print(f"Message from kafka: {kafka_message}")
    kafka_message_json = json.loads(kafka_message)
    message_text = f"На Вас назначена новая задача! {emoji['cool']} \n\n" \
                   f"Название задачи: {kafka_message_json['taskName']} \n" \
                   f"Описание задачи: {kafka_message_json['taskDescription']} \n" \
                   f"Текущий статус задачи: {kafka_message_json['taskStatus']} \n\n" \
                   f"Вперед к задачам! {emoji['strong']}"

    bot.send_message(kafka_message_json['userTgChatId'], text=message_text)


def send_notify_about_doc_payment(kafka_message):
    print(f"Message from kafka: {kafka_message}")
    kafka_message_json = json.loads(kafka_message)
    message_text = f"Хорошие новости - для Вас загрузили квитанцию для оплаты! {emoji['cash']} \n\n" \
                   f"Сумма заработанных денег: {kafka_message_json['totalCash']} \n" \
                   f"Пригласите на вечеринку? {emoji['nerd']}"

    bot.send_message(kafka_message_json['userTgChatId'], text=message_text)


def kafka_consumer_assign():
    consumer = KafkaConsumer(
        bootstrap_servers=["localhost:29092"],
        auto_offset_reset="earliest",
        enable_auto_commit=True,
        group_id='custom_group',
        value_deserializer=lambda x: json.loads(x.decode("utf-8"))
    )

    consumer.subscribe(["TEAMSYNC.ASSIGN_TASK.V1"])

    for message in consumer:
        kafka_message = f"""
            Message received: {message.value}
            Message key: {message.key}
            Message partition: {message.partition}
            Message offset: {message.offset}
        """
        print(kafka_message)
        send_notify_about_assign_task(str(message.value).replace('\'', '\"'))


def kafka_consumer_payment_doc():
    consumer = KafkaConsumer(
        bootstrap_servers=["localhost:29092"],
        auto_offset_reset="earliest",
        enable_auto_commit=True,
        group_id='custom_group',
        value_deserializer=lambda x: json.loads(x.decode("utf-8"))
    )

    consumer.subscribe(["TEAMSYNC.PAYMENT_DOC.V1"])

    for message in consumer:
        kafka_message = f"""
            Message received: {message.value}
            Message key: {message.key}
            Message partition: {message.partition}
            Message offset: {message.offset}
        """
        print(kafka_message)
        send_notify_about_doc_payment(str(message.value).replace('\'', '\"'))


def run_bot():
    bot.infinity_polling()


def run_in_parallel(*fns):
    proc = []
    for fn in fns:
        p = Process(target=fn)
        p.start()
        proc.append(p)
    for p in proc:
        print(p)
        p.join()


if __name__ == '__main__':
    run_in_parallel(kafka_consumer_assign, kafka_consumer_payment_doc, run_bot)
