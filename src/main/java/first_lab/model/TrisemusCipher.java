package first_lab.model;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

// Класс для реализации шифра Трисемуса
public class TrisemusCipher {

    // Константа, содержащая русский алфавит
    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    // Двумерный массив для хранения таблицы Трисемуса
    private char[][] table;

    // Конструктор класса
    public TrisemusCipher(String keyword) {
        // Генерация таблицы на основе ключевого слова
        table = generateTable(keyword);
    }

    // Метод для генерации таблицы Трисемуса
    private char[][] generateTable(String keyword) {
        // Инициализация таблицы 6x6
        char[][] table = new char[6][6];
        // Множество для хранения уже использованных символов
        Set<Character> usedChars = new HashSet<>();
        int x = 0, y = 0;

        // Добавление символов из ключевого слова в таблицу
        for (char c : keyword.toUpperCase().toCharArray()) {
            if (!usedChars.contains(c) && ALPHABET.indexOf(c) != -1) {
                table[x][y] = c;
                usedChars.add(c);
                y++;
                if (y == 6) {
                    x++;
                    y = 0;
                }
            }
        }

        // Добавление оставшихся символов из алфавита в таблицу
        for (char c : ALPHABET.toCharArray()) {
            if (!usedChars.contains(c)) {
                table[x][y] = c;
                y++;
                if (y == 6) {
                    x++;
                    y = 0;
                }
            }
        }

        return table;
    }

    // Метод для шифрования текста
    public String encrypt(String text) {
        StringBuilder encryptedText = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            int[] position = findPosition(c);
            if (position[0] != -1) {
                encryptedText.append(table[position[0]][(position[1] + 1) % 6]);
            } else {
                encryptedText.append(c);
            }
        }
        return encryptedText.toString();
    }

    // Метод для дешифрования текста
    public String decrypt(String text) {
        StringBuilder decryptedText = new StringBuilder();
        for (char c : text.toUpperCase().toCharArray()) {
            int[] position = findPosition(c);
            if (position[0] != -1) {
                decryptedText.append(table[position[0]][(position[1] - 1 + 6) % 6]);
            } else {
                decryptedText.append(c);
            }
        }
        return decryptedText.toString();
    }

    // Метод для поиска позиции символа в таблице
    private int[] findPosition(char c) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (table[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    public void encryptFile(String inputFilePath, String outputFilePath, String keyword) {
        // Создаем экземпляр класса TrisemusCipher с заданным ключевым словом
        TrisemusCipher cipher = new TrisemusCipher(keyword);

        try {
            // Открываем файл для чтения
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            // Открываем файл для записи
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            // Читаем файл построчно
            while ((line = reader.readLine()) != null) {
                // Шифруем каждую строку
                String encryptedLine = cipher.encrypt(line);
                // Записываем зашифрованную строку в выходной файл
                writer.write(encryptedLine);
                //writer.newLine();  // Переход на новую строку
            }

            // Закрываем файлы
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decryptFile(String inputFilePath, String outputFilePath, String keyword) {
        // Создаем экземпляр класса TrisemusCipher с заданным ключевым словом
        TrisemusCipher cipher = new TrisemusCipher(keyword);

        try {
            // Открываем файл для чтения
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            // Открываем файл для записи
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            // Читаем файл построчно
            while ((line = reader.readLine()) != null) {
                // Дешифруем каждую строку
                String decryptedLine = cipher.decrypt(line);
                // Записываем расшифрованную строку в выходной файл
                writer.write(decryptedLine);
                //writer.newLine();  // Переход на новую строку
            }

            // Закрываем файлы
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
