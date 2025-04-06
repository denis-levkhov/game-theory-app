from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
import numpy as np
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI()

class MatricesInput(BaseModel):
    matrix1: List[List[float]]
    matrix2: List[List[float]]

def get_max_indices_by_column(matrix):
    logger.info("Начинаем поиск максимальных элементов по столбцам")
    max_indices = {}
    for j in range(matrix.shape[1]):
        max_value = np.max(matrix[:, j])
        indices = [i for i in range(matrix.shape[0]) if matrix[i, j] == max_value]
        max_indices[j] = indices
        logger.info(f"Столбец {j}: макс. значение {max_value}, индексы {indices}")
    return max_indices

def get_max_indices_by_row(matrix):
    logger.info("Начинаем поиск максимальных элементов по строкам")
    max_indices = {}
    for i in range(matrix.shape[0]):
        max_value = np.max(matrix[i, :])
        indices = [j for j in range(matrix.shape[1]) if matrix[i, j] == max_value]
        max_indices[i] = indices
        logger.info(f"Строка {i}: макс. значение {max_value}, индексы {indices}")
    return max_indices

def find_common_indices(col_max_indices, row_max_indices):
    logger.info("Ищем общие позиции максимальных элементов")
    common_positions = []
    for j, row_indices in col_max_indices.items():
        for i, col_indices in row_max_indices.items():
            if i in row_indices and j in col_indices:
                common_positions.append((i, j))
                logger.info(f"Общая позиция найдена: ({i}, {j})")
    logger.info(f"Итого общих позиций: {len(common_positions)}")
    return common_positions

def find_missing_max_rows(matrix):
    logger.info("Ищем строки, без максимального значения в столбцах")
    missing_rows = set(range(matrix.shape[0]))
    for col in range(matrix.shape[1]):
        column = matrix[:, col]
        max_val = np.max(column)
        max_indices = np.where(column == max_val)[0]
        missing_rows -= set(max_indices)
        logger.info(f"Столбец {col}: макс. значение {max_val}, индексы {list(max_indices)}")
    logger.info(f"Строки без максимума в столбцах: {list(missing_rows)}")
    return missing_rows

def find_missing_max_cols(matrix):
    logger.info("Ищем столбцы, без максимального значения в строках")
    missing_cols = set(range(matrix.shape[1]))
    for row in range(matrix.shape[0]):
        row_values = matrix[row, :]
        max_val = np.max(row_values)
        max_indices = np.where(row_values == max_val)[0]
        missing_cols -= set(max_indices)
        logger.info(f"Строка {row}: макс. значение {max_val}, индексы {list(max_indices)}")
    logger.info(f"Столбцы без максимума в строках: {list(missing_cols)}")
    return missing_cols

@app.post("/nash-clear")
def common_max_positions(input_data: MatricesInput):
    logger.info("Получен запрос /nash-clear")
    matrix1 = np.array(input_data.matrix1)
    matrix2 = np.array(input_data.matrix2)
    logger.info(f"Матрица 1:\n{matrix1}")
    logger.info(f"Матрица 2:\n{matrix2}")

    col_max_indices = get_max_indices_by_column(matrix1)
    row_max_indices = get_max_indices_by_row(matrix2)
    common_positions = find_common_indices(col_max_indices, row_max_indices)

    logger.info("Обработка запроса /nash-clear завершена")

    return {
        "col_max_indices": col_max_indices,
        "row_max_indices": row_max_indices,
        "common_positions": common_positions
    }

@app.post("/nlo")
def missing_max_positions(input_data: MatricesInput):
    logger.info("Получен запрос /nlo")
    matrix1 = np.array(input_data.matrix1)
    matrix2 = np.array(input_data.matrix2)
    logger.info(f"Матрица 1:\n{matrix1}")
    logger.info(f"Матрица 2:\n{matrix2}")

    missing_rows = find_missing_max_rows(matrix1)
    missing_cols = find_missing_max_cols(matrix2)

    logger.info("Обработка запроса /nlo завершена")

    return {
        "missing_rows_matrix1": list(missing_rows),
        "missing_cols_matrix2": list(missing_cols)
    }