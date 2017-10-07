package eu.geekhome.asymptote.services.impl;

import android.content.Context;

import com.efraespada.androidstringobfuscator.AndroidStringObfuscator;

import javax.inject.Inject;

import eu.geekhome.asymptote.services.ObfuscatorService;

public class EfraespadaObfuscationService implements ObfuscatorService {
    @Inject
    public EfraespadaObfuscationService(Context context) {
        AndroidStringObfuscator.init(context);
    }

    @Override
    public String obfuscate(String string) {
        return AndroidStringObfuscator.encryptString(string);
    }

    @Override
    public String deobfuscate(String string) {
        return AndroidStringObfuscator.decryptString(string);
    }
}
