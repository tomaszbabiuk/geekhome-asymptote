package eu.geekhome.asymptote.services;

import java.net.InetAddress;
import java.util.Enumeration;

public interface AddressesPersistenceService {
    void save();
    void clear();
    void add(InetAddress address);
    void load();
    Enumeration<InetAddress> getAddresses();

    boolean isEmpty();

    boolean contains(InetAddress address);
}