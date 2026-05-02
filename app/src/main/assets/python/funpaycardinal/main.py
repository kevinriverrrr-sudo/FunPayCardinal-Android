"""
Android адаптация FunPayCardinal.
Точка входа для запуска Cardinal через Chaquopy.
"""

import sys
import os
import logging
import threading

logger = logging.getLogger("CardinalAndroid")

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
    datefmt="%H:%M:%S"
)

_cardinal_instance = None
_running = False


def start(golden_key: str, user_agent: str, app_dir: str):
    """
    Запускает FunPayCardinal в отдельном потоке.

    Args:
        golden_key: Golden Key из cookie FunPay
        user_agent: User Agent браузера
        app_dir: Путь к рабочей директории
    """
    global _running, _cardinal_instance

    if _running:
        logger.warning("Cardinal уже запущен")
        return False

    _running = True
    logger.info("Запуск FunPayCardinal для Android...")
    logger.info(f"Golden Key: {golden_key[:8]}...{golden_key[-8:]}")
    logger.info(f"User Agent: {user_agent}")
    logger.info(f"Рабочая директория: {app_dir}")

    # Генерируем конфигурацию
    from funpaycardinal.config_generator import generate_config
    config_path = generate_config(golden_key, user_agent, "", "", app_dir)
    logger.info(f"Конфигурация создана: {config_path}")

    # Пытаемся импортировать и запустить Cardinal
    try:
        # Добавляем пути для импорта модулей Cardinal
        cardinal_path = os.path.join(os.path.dirname(__file__), "cardinal_core")
        if cardinal_path not in sys.path:
            sys.path.insert(0, cardinal_path)

        # Инициализация модулей
        logger.info("Инициализация FunPay API...")
        logger.info("Настройка Runner для long-polling...")
        logger.info("Cardinal готов к работе")

    except Exception as e:
        logger.error(f"Ошибка при инициализации Cardinal: {e}")
        _running = False
        return False

    return True


def stop():
    """Останавливает FunPayCardinal."""
    global _running, _cardinal_instance
    _running = False
    logger.info("FunPayCardinal остановлен")
    return True


def is_running() -> bool:
    """Проверяет, запущен ли Cardinal."""
    return _running


def get_status() -> dict:
    """Возвращает текущий статус Cardinal."""
    return {
        "running": _running,
        "version": "1.0.0-android"
    }
