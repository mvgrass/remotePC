#include "networkallocation.h"
#include <QUdpSocket>
#include <QNetworkInterface>
#include <QHostInfo>
#include <QDebug>

NetWorkAllocation::NetWorkAllocation(QObject *parent) : QObject(parent)
{
}

void NetWorkAllocation::run(){

    QList<QNetworkInterface> interfaces = QNetworkInterface::allInterfaces();

    QNetworkInterface interface;
    foreach(interface, interfaces){
        QList<QNetworkAddressEntry> entries = interface.addressEntries();
        QNetworkAddressEntry entry;
        foreach(entry, entries){
            if(entry.ip().protocol() == QAbstractSocket::IPv4Protocol
                    && entry.ip()!=QHostAddress::LocalHost){

                QUdpSocket* socket = new QUdpSocket();

                QByteArray hostINFO;
                hostINFO.append(QHostInfo::localHostName().toUtf8());

                socket->writeDatagram(hostINFO, entry.broadcast(), PORT);

                delete socket;
            }
        }
    }
}
