package first_lab.controller;

import first_lab.model.TrisemusCipher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.nio.file.Files;

@Controller
public class CipherController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/encrypt-form")
    public ModelAndView showForm() {
        return new ModelAndView("encrypt");
    }

    @GetMapping("/decrypt-form")
    public ModelAndView showDecryptForm() {
        return new ModelAndView("decrypt");
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestParam String text, @RequestParam String keyword, Model model) {
        TrisemusCipher cipher = new TrisemusCipher(keyword);
        String encryptedText = cipher.encrypt(text);
        model.addAttribute("result", encryptedText);
        return "result";
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestParam String text, @RequestParam String keyword, Model model) {
        TrisemusCipher cipher = new TrisemusCipher(keyword);
        String decryptedText = cipher.decrypt(text);
        model.addAttribute("result", decryptedText);
        return "result";
    }

    @PostMapping("/encrypt-file")
    public ResponseEntity<?> encryptFile(@RequestParam("file") MultipartFile file, @RequestParam String keyword) {
        try {
            // Создаем временные файлы
            File inputFile = File.createTempFile("input", ".txt");
            File outputFile = File.createTempFile("output", ".txt");

            // Записываем содержимое загруженного файла во временный файл
            file.transferTo(inputFile);

            // Шифруем
            TrisemusCipher trisemusCipher  = new TrisemusCipher(keyword);
            trisemusCipher.encryptFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), keyword);

            // Возвращаем зашифрованный файл пользователю
            byte[] outputContent = Files.readAllBytes(outputFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "encrypted_file.txt");
            return new ResponseEntity<>(outputContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decrypt-file")
    public ResponseEntity<?> decryptFile(@RequestParam("file") MultipartFile file, @RequestParam String keyword) {
        try {
            // Создаем временные файлы
            File inputFile = File.createTempFile("input", ".txt");
            File outputFile = File.createTempFile("output", ".txt");

            // Записываем содержимое загруженного файла во временный файл
            file.transferTo(inputFile);

            // Дешифруем
            TrisemusCipher trisemusCipher  = new TrisemusCipher(keyword);

            trisemusCipher.decryptFile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath(), keyword);

            // Возвращаем расшифрованный файл пользователю
            byte[] outputContent = Files.readAllBytes(outputFile.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "decrypted_file.txt");
            return new ResponseEntity<>(outputContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
