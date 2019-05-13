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
    const quint32 PORT = 11498;
};

#endif // NETWORKALLOCATION_H
