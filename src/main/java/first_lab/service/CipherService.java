package first_lab.service;

import first_lab.model.TrisemusCipher;
import org.springframework.stereotype.Service;

@Service
public class CipherService {

    private TrisemusCipher trisemusCipher = new TrisemusCipher("");

    public String encrypt(String plaintext) {
        return trisemusCipher.encrypt(plaintext);
    }

    public String decrypt(String ciphertext) {
        return trisemusCipher.decrypt(ciphertext);
    }
}