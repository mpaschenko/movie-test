package com.movies;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.security.auth.x500.X500Principal;

/**
 * Created by michaelpaschenko on 9/26/17.
 */

public class Encryption {

    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private static final String KEY_ALIAS_NAME = "movies-alias";
    private static final byte[] IV = new byte[]{103, 42, 100, 35, -77, 105, -120, -78, 81, -100, -112, 20};

    private static final int GCM_TAG_LENGTH = 128;
    private static final String AES_CIPHER = KeyProperties.KEY_ALGORITHM_AES + "/" +
            KeyProperties.BLOCK_MODE_GCM + "/" + KeyProperties.ENCRYPTION_PADDING_NONE;
    private final KeyStore keyStore;

    public Encryption() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        this.keyStore = KeyStore.getInstance(KEYSTORE_NAME);
        this.keyStore.load(null);
        generateAESKeyAndSaveToKeystore();
    }

    private void generateAESKeyAndSaveToKeystore() {
        try {
            if (!hasAESKeyInKeystore(KEY_ALIAS_NAME)) {

                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 25);
                KeyGenerator keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_NAME);
                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(KEY_ALIAS_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setCertificateSubject(new X500Principal("CN = Secured Preference Store, O = Movies Test"))
                        .setCertificateSerialNumber(BigInteger.ONE)
                        .setKeySize(GCM_TAG_LENGTH)
                        .setKeyValidityEnd(end.getTime())
                        .setKeyValidityStart(start.getTime())
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(false)
                        .build();
                keyGen.init(spec);
                keyGen.generateKey();
            }
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private boolean hasAESKeyInKeystore(final String ALIAS) {
        try {
            return keyStore.containsAlias(ALIAS) && keyStore.entryInstanceOf(ALIAS, KeyStore.SecretKeyEntry.class);
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        }
    }

    private SecretKey getAESKeyFromKeystore() {
        try {
            if (hasAESKeyInKeystore(KEY_ALIAS_NAME)) {
                final KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS_NAME, null);
                return entry.getSecretKey();
            } else {
                return null;
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableEntryException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String encrypt(String data) {
        try {
            final byte[] message = data.getBytes();
            final Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, getAESKeyFromKeystore(), new GCMParameterSpec(GCM_TAG_LENGTH, IV));
            byte[] encodedBytes = cipher.doFinal(message);
            return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        }
    }
}
