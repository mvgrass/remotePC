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

                QByteArray hostINFO = ipV4ToByteArray(entry.ip());
                hostINFO.append("\n"+QHostInfo::localHostName());

                socket->writeDatagram(hostINFO, entry.broadcast(), PORT);

                delete socket;
            }
        }
    }
}

QByteArray NetWorkAllocation::ipV4ToByteArray(const QHostAddress & ip)
{
    QByteArray array;
    quint32 ipInt = ip.toIPv4Address();

    array.append((ipInt>>24)&255);
    array.append((ipInt>>16)&255);
    array.append((ipInt>>8)&255);
    array.append(ipInt&255);

    return array;
}

