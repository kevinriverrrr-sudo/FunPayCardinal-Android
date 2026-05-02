"""
Модуль генерации конфигурационных файлов для FunPayCardinal на Android.
Конвертирует параметры из Room Database в формат INI конфигурации Cardinal.
"""

import os
import logging

logger = logging.getLogger("CardinalAndroid")


def generate_config(golden_key: str, user_agent: str, telegram_token: str,
                    secret_password: str, app_dir: str) -> str:
    """
    Генерирует конфигурационные файлы FunPayCardinal в указанной директории.

    Args:
        golden_key: Golden Key из cookie FunPay (32 символа)
        user_agent: User Agent браузера
        telegram_token: Токен Telegram бота
        secret_password: Секретный пароль для Telegram панели
        app_dir: Путь к рабочей директории приложения

    Returns:
        Путь к созданному конфигурационному файлу
    """
    config_dir = os.path.join(app_dir, "configs")
    storage_dir = os.path.join(app_dir, "storage")
    cache_dir = os.path.join(storage_dir, "cache")
    products_dir = os.path.join(storage_dir, "products")
    plugins_dir = os.path.join(app_dir, "plugins")
    logs_dir = os.path.join(app_dir, "logs")

    # Создаём директории
    for d in [config_dir, cache_dir, products_dir, plugins_dir, logs_dir]:
        os.makedirs(d, exist_ok=True)

    config_path = os.path.join(config_dir, "_main.cfg")

    # Генерируем основной конфигурационный файл
    config_content = f"""[FunPay]
goldenKey: {golden_key}
userAgent: {user_agent}
autoRaise: true
autoResponse: false
autoDelivery: true
multiDelivery: false
autoRestore: true
autoDisable: true
locale: ru

[Telegram]
enabled: {str(telegram_token.strip() != "").lower()}
token: {telegram_token}
secretKeyHash:
blockLogin: false

[BlockList]
autoDelivery: false
autoResponse: false
notifications: false

[NewMessageView]
includeOwnMessages: false
includeSystemMessages: false

[Greetings]
enabled: false
text: Здравствуйте, $username!
cooldown: 86400

[OrderConfirm]
watermark: false
autoReply: false

[ReviewReply]
1:
2:
3:
4:
5:

[Proxy]
enabled: false
address:
checkOnStart: false

[Other]
watermarkEmoji:
requestsDelay: 4
language: ru
"""

    with open(config_path, "w", encoding="utf-8") as f:
        f.write(config_content)

    # Создаём пустые конфиги для автодоставки и автоответов
    for cfg_name in ["auto_delivery.cfg", "auto_response.cfg"]:
        cfg_path = os.path.join(config_dir, cfg_name)
        if not os.path.exists(cfg_path):
            with open(cfg_path, "w", encoding="utf-8") as f:
                f.write("# Конфигурация FunPayCardinal Android\n")
                f.write(f"# {cfg_name}\n")

    logger.info(f"Configuration generated at: {config_path}")
    return config_path


def get_config_path(app_dir: str) -> str:
    """Возвращает путь к основному конфигурационному файлу."""
    return os.path.join(app_dir, "configs", "_main.cfg")


def get_storage_path(app_dir: str) -> str:
    """Возвращает путь к директории хранения данных."""
    return os.path.join(app_dir, "storage")
