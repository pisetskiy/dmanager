package by.bsuir.ksis.dmanager.logic;

import lombok.Data;

import java.net.InetAddress;

@Data
class ConnectionParams {

    private InetAddress address;
    private int port;
    private String workingDirectory;
    private String fileName;
}
