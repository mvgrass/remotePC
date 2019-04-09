#ifndef NETWORKALLOCATION_H
#define NETWORKALLOCATION_H

#include <QObject>
#include <QUdpSocket>

class NetWorkAllocation : public QObject
{
    Q_OBJECT
public:
    explicit NetWorkAllocation(QObject *parent = nullptr);

signals:

public slots:
    void run();

private:
    QByteArray ipV4ToByteArray(const QHostAddress&);
    const unsigned int PORT = 23496;
};

#endif // NETWORKALLOCATION_H
