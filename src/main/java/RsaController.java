import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.join;

public class RsaController {
    // Encryption (left-side)
    // Input
    @FXML
    TextArea txtEncN; // Text-area for N

    @FXML
    TextArea txtEncMsgToEncrypt; //Text-area for message to encrypt

    // Output
    @FXML
    TextField txtEncP; // Textbox for P
    @FXML
    TextField txtEncQ; // Textbox for Q
    @FXML
    Label lblEncPQCalcTime; // Label for P and Q calculation time
    @FXML
    Label lblEncE; // Label for E
    @FXML
    TextField txtEncE; // Textbox for E
    @FXML
    Label lblEncECalcTime; // Label for E calculation time
    @FXML
    TextArea txtEncEncryptedMsg; // Text-area for encrypted message

    // Decryption (right-side)
    // Input
    @FXML
    TextArea txtDecN; // Text-area for N
    @FXML
    TextArea txtDecE; // Text-area for E
    @FXML
    TextArea txtDecEncryptedMsg; //Text-area for the encrypted message

    // Output
    @FXML
    TextField txtDecD; // Label for D
    @FXML
    TextArea txtDecDecryptedMsg; //Text-area for the decrypted message

    @FXML
    protected void onEncFindPAndQClick() {
        // Check if valid n input
        if (!isValidIntInput(txtEncN)) return;
        // Get n
        int n = parseInt(txtEncN.getText());

        // Set n to decrypt
        txtDecN.setText(txtEncN.getText());

        // Save starting time for p and q calculation
        long startTime = System.currentTimeMillis();

        // Calculate p
        int p = RsaHelper.getPFromN(n);

        // Calculate q
        int q = RsaHelper.getQFromPAndN(n, p);

        // Calculate p and q calculation time
        long duration = (System.currentTimeMillis() - startTime);

        // Check if p and q are prime
        if (!RsaHelper.isPrime(p) || !RsaHelper.isPrime(q)) {
            txtEncN.setText("Entered N is not relatively prime");
            return;
        }

        // Show calculation results in view
        txtEncP.setText(format("%s", p));
        txtEncQ.setText(format("%s", q));
        lblEncPQCalcTime.setText(format("%s ms", duration));
    }

    @FXML
    protected void onEncFindNextE() {
        // Get P and Q from textfield
        int p = parseInt(txtEncP.getText());
        int q = parseInt(txtEncQ.getText());

        // Save starting time for e calculation
        long startTime = System.currentTimeMillis();

        // Calculate and select random e
        int e = RsaHelper.getRandomE(p, q);

        // Calculate e calculation time
        long duration = (System.currentTimeMillis() - startTime);

        // Show calculation results in view
        txtEncE.setText(Integer.toString(e));
        txtDecE.setText(Integer.toString(e));
        lblEncECalcTime.setText(format("%s ms", duration));

        // Because we have all values to calculate d, we do that here
        if (!Objects.equals(txtDecN.getText(), "")) { onDecCalculateD(); }

        // Validate e result
        this.validateE();
    }

    @FXML
    protected void onEncEncryptM() {
        int e = parseInt(txtEncE.getText());
        int n = parseInt(txtEncN.getText());

        // Encrypt text message
        String[] encChars  = txtEncMsgToEncrypt.getText()
                // Convert to chars
                .chars()
                // Convert to big int
                .mapToObj(c -> new BigInteger(Integer.toString(c)))
                // Calculate modular exponentiation
                .map(c -> c.pow(e).mod(BigInteger.valueOf(n)).toString())
                // Create new String array
                .toArray(String[]::new);

        // Join encrypted string array with , 's
        String encMsg = join(",", encChars);

        // Show encrypted message
        txtEncEncryptedMsg.setText(encMsg);
        txtDecEncryptedMsg.setText(encMsg);
    }

    @FXML
    protected void onDecCalculateD() {
        // Check if all values are available
        if (!isValidIntInput(txtDecN) || !isValidIntInput(txtDecE)) return;

        // Get input value
        int n = parseInt(txtDecN.getText());
        int e = parseInt(txtDecE.getText());

        // Calculate output values
        int p = RsaHelper.getPFromN(n);
        int q = RsaHelper.getQFromPAndN(n, p);
        int phi = RsaHelper.calcPhi(p, q);
        int d = RsaHelper.modInverse(e, phi);

        // Show results in view
        txtDecD.setText(format("%s", d));
}

    @FXML
    protected void onDecDecryptMessage() {
        // Get input values
        int d = parseInt(txtDecD.getText());
        String rawInput = txtDecEncryptedMsg.getText();
        BigInteger n = new BigInteger(txtDecN.getText());

        StringBuilder decryptedMessage = new StringBuilder();

        try {
            String[] rawInputSplit = rawInput.split(",");
            for (String integer: rawInputSplit) {
                int ch = parseInt(integer);
                BigInteger characterInt = new BigInteger(Integer.toString(ch));
                ch = parseInt(characterInt.pow(d).mod(n).toString());
                decryptedMessage.append((char) ch);
            }
            txtDecDecryptedMsg.setText(decryptedMessage.toString());
        }
        catch (Exception e) {
            txtDecDecryptedMsg.setText("Wrong message");
        }
    }

    @FXML
    protected void validateE() {
        // Get inputs values
        int p = parseInt(txtEncP.getText());
        int q = parseInt(txtEncQ.getText());
        int eToCheck = parseInt(txtEncE.getText());

        // Calculate phi
        int phi = RsaHelper.calcPhi(p, q);

        // Get all possible values for e
        ArrayList<Integer> allPossibleE = RsaHelper.getAllPossibleE(phi);

        // Check if e is valid
        lblEncE.setText("E = " + (!allPossibleE.contains(eToCheck) ? "(INVALID)" : "(VALID)"));
    }

    private boolean isValidIntInput(TextArea textArea) {
        try {
            parseInt(textArea.getText());
            return true;
        } catch (Exception e) {
            textArea.setText("WRONG INPUT PLEASE ONLY USE INTEGERS");
            return false;
        }
    }
}
