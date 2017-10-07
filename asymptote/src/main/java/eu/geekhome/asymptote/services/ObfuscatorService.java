package eu.geekhome.asymptote.services;

public interface ObfuscatorService {
    String obfuscate(String string);
    String deobfuscate(String string);
}
