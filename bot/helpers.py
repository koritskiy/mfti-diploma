import telebot

emoji = {
    'next': '➡️',
    'previous': '⬅️',
    'sad': '😔',
    'pin': '📍',
    'hello': '👋',
    'love': '❤️',
    'nerd': '🤓',
    'car': '🚘',
    'car_service': '🔧',
    'beauty_salon': '💆‍♀️',
    'archive': '📂',
    'write_feedback': '✍🏻',
    'rate': '⭐',
    'cancel': '❌',
    'alarm': '⏰',
    'description': '📝',
    'info': 'ℹ️',
    'accept': '✅',
    'cry': '😭',
    'cute': '☺️',
    'cash': '💵',
    'time': '⌛',
    'feedback': '💬',
    'edit': '✏️',
    'surprise': '😳',
    'calendar': '📅',
    'warning': '⚠️',
    'strong': '💪',
    'cool': '😎'
}

UNKNOWN_TEXT_HANDLER = f"Я так пока не умею {emoji['sad']} \n\n Нажмите *\"Помощь\"*, чтобы узнать, что я умею делать."

def get_default_reply_keyboard():
    buttons = [
        telebot.types.KeyboardButton('Моя организация'),
        telebot.types.KeyboardButton('Мои задачи'),
        telebot.types.KeyboardButton('Помощь')
    ]
    keyboard = telebot.types.ReplyKeyboardMarkup(row_width=2)
    keyboard.add(*buttons)
    return keyboard

